package com.suban.framework.core;

import com.suban.framework.config.ConfigReader;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class DeviceManager {

    private static final long BOOT_TIMEOUT_SECONDS = 120;
    private static final long SHUTDOWN_TIMEOUT_SECONDS = 30;
    private static final long POLL_INTERVAL_MS = 1000;

    private static final Logger logger = LoggerFactory.getLogger(DeviceManager.class);

    // ─── Android ──────────────────────────────────────────────────────────────

    public static void startAndroidEmulator(String avdName) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("emulator", "-avd", avdName, "-no-snapshot-load");
        pb.redirectErrorStream(true);
        pb.start(); // intentionally not waiting — emulator runs in background

        long startTime = System.currentTimeMillis();
        long timeoutMillis = BOOT_TIMEOUT_SECONDS * 1000;

        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            if (isEmulatorReady()) {
                logger.info("Android emulator is ready");
                return;
            }
            Thread.sleep(POLL_INTERVAL_MS);
        }

        throw new TimeoutException("Emulator did not become ready within " + BOOT_TIMEOUT_SECONDS + " seconds");
    }

    public static void stopAndroidEmulator() throws IOException, InterruptedException {
        boolean isEmulatorToUse = ConfigReader.getBooleanProperty("device.use.emulator");
        if (!isEmulatorToUse) {
            logger.info("Emulator not managed by framework — skipping stop");
            return;
        }

        logger.info("Stopping Android emulator via adb emu kill");
        ProcessBuilder pb = new ProcessBuilder("adb", "emu", "kill");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        boolean finished = process.waitFor(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            logger.warn("adb emu kill timed out — process forcibly terminated");
        } else {
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                logger.info("Android emulator stopped successfully");
            } else {
                logger.warn("adb emu kill exited with code {}", exitCode);
            }
        }
    }

    private static boolean isEmulatorReady() throws IOException {
        return checkAdbProperty("sys.boot_completed", "1") &&
                checkAdbProperty("init.svc.bootanim", "stopped");
    }

    private static boolean checkAdbProperty(String property, String expectedValue) throws IOException {
        Process process = new ProcessBuilder("adb", "shell", "getprop", property)
                .redirectErrorStream(true)
                .start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String value = reader.readLine();
            return expectedValue.equals(value != null ? value.trim() : null);
        }
    }

    // ─── iOS ──────────────────────────────────────────────────────────────────

    public static void startIOSSimulator(String udid) throws IOException, InterruptedException {
        if (isSimulatorBooted(udid)) {
            logger.info("Simulator {} is already booted", udid);
            return;
        }

        Process bootProcess = new ProcessBuilder("xcrun", "simctl", "boot", udid).start();
        if (bootProcess.waitFor() != 0) {
            logger.error("Failed to boot simulator {}", udid);
            throw new IOException("Failed to boot simulator " + udid);
        }

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < TimeUnit.SECONDS.toMillis(BOOT_TIMEOUT_SECONDS)) {
            if (isSimulatorBooted(udid)) {
                logger.info("Simulator {} successfully booted", udid);
                return;
            }
            Thread.sleep(POLL_INTERVAL_MS);
        }

        throw new TimeoutException("Simulator " + udid + " did not boot within " + BOOT_TIMEOUT_SECONDS + " seconds");
    }

    public static void stopIOSSimulator(String udid) throws IOException, InterruptedException {
        if (!isSimulatorBooted(udid)) {
            logger.info("Simulator {} is already shutdown", udid);
            return;
        }

        Process shutdownProcess = new ProcessBuilder("xcrun", "simctl", "shutdown", udid).start();
        if (shutdownProcess.waitFor() != 0) {
            logger.error("Failed to shutdown simulator {}", udid);
            throw new IOException("Failed to shutdown simulator " + udid);
        }

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < TimeUnit.SECONDS.toMillis(SHUTDOWN_TIMEOUT_SECONDS)) {
            if (!isSimulatorBooted(udid)) {
                logger.info("Simulator {} successfully shutdown", udid);
                return;
            }
            Thread.sleep(POLL_INTERVAL_MS);
        }

        throw new TimeoutException("Simulator " + udid + " did not shutdown within " + SHUTDOWN_TIMEOUT_SECONDS + " seconds");
    }

    private static boolean isSimulatorBooted(String udid) throws IOException {
        Process listProcess = new ProcessBuilder("xcrun", "simctl", "list", "devices").start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(listProcess.getInputStream()))) {
            String line;
            boolean foundDevice = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains(udid)) {
                    foundDevice = true;
                    if (line.contains("(Booted)")) {
                        return true;
                    }
                }
            }

            if (!foundDevice) {
                throw new IOException("Simulator with UDID " + udid + " not found");
            }
        }
        return isSimulatorReady(udid);
    }

    private static boolean isSimulatorReady(String udid) throws IOException {
        try {
            Process process = new ProcessBuilder("xcrun", "simctl", "spawn", udid,
                    "launchctl", "print", "system").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.lines().anyMatch(line -> line.contains("com.apple.SpringBoard"));
            }
        } catch (IOException e) {
            return false;
        }
    }
}
