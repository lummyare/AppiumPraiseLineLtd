package com.suban.framework.core;

import com.suban.framework.config.ConfigReader;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AppiumServer {
    private static AppiumDriverLocalService service;   // used for iOS only
    private static Process androidProcess;             // raw process for Android
    private static final int port = Integer.parseInt(ConfigReader.getProperty("appium.port"));
    private static final Logger logger = LoggerFactory.getLogger(AppiumServer.class);
    private static final String APPIUM_URL = "http://127.0.0.1:" + port;

    public static void startOrConnectToServer() {
        // Already running (iOS managed service)
        if (service != null && service.isRunning()) {
            logger.info("Programmatically started Appium server (iOS) is already running on: {}", service.getUrl());
            return;
        }
        // Already running (Android raw process)
        if (androidProcess != null && androidProcess.isAlive() && isAppiumHealthy()) {
            logger.info("Programmatically started Appium server (Android) is already running on port {}", port);
            return;
        }

        // Kill any stale Appium on this port before starting fresh
        if (isAppiumServerRunning()) {
            logger.info("Stale Appium server detected on port {}. Killing it.", port);
            killExternalAppiumServer();
        }

        startNewServer();
    }

    /**
     * Kills any Appium process already occupying the configured port.
     */
    private static void killExternalAppiumServer() {
        try {
            // lsof finds the exact PID owning the port — more surgical than pkill
            ProcessBuilder lsof = new ProcessBuilder("lsof", "-ti", "tcp:" + port);
            lsof.redirectErrorStream(true);
            Process lsofProc = lsof.start();
            String pid = null;
            try (BufferedReader r = new BufferedReader(new InputStreamReader(lsofProc.getInputStream()))) {
                pid = r.readLine();
            }
            lsofProc.waitFor(3, TimeUnit.SECONDS);

            if (pid != null && !pid.isBlank()) {
                logger.info("Killing Appium process on port {} (PID: {})", port, pid.trim());
                new ProcessBuilder("kill", "-9", pid.trim()).start().waitFor(3, TimeUnit.SECONDS);
            } else {
                // Fallback: pkill by name
                new ProcessBuilder("pkill", "-f", "appium").start().waitFor(3, TimeUnit.SECONDS);
            }
            Thread.sleep(1500);
            logger.info("Stale Appium process terminated");
        } catch (Exception e) {
            logger.warn("Could not kill stale Appium process: {}", e.getMessage());
        }
    }

    /**
     * Resolves the full path to the 'appium' binary.
     * Checks common nvm / homebrew locations, then falls back to 'which appium'.
     */
    private static String resolveAppiumBinary() {
        String home = System.getProperty("user.home");
        String[] candidates = {
            home + "/.nvm/versions/node/v20.20.2/bin/appium",
            home + "/.nvm/versions/node/v20.19.0/bin/appium",
            home + "/.nvm/versions/node/v20.18.0/bin/appium",
            home + "/.nvm/versions/node/v18.20.8/bin/appium",
            "/opt/homebrew/bin/appium",
            "/usr/local/bin/appium",
        };
        for (String c : candidates) {
            if (new java.io.File(c).exists()) {
                logger.info("Found appium binary at: {}", c);
                return c;
            }
        }
        // Scan all nvm node versions
        java.io.File nvmVersions = new java.io.File(home + "/.nvm/versions/node");
        if (nvmVersions.exists() && nvmVersions.isDirectory()) {
            java.io.File[] versions = nvmVersions.listFiles();
            if (versions != null) {
                for (java.io.File v : versions) {
                    java.io.File bin = new java.io.File(v, "bin/appium");
                    if (bin.exists()) {
                        logger.info("Found appium binary via nvm scan: {}", bin.getAbsolutePath());
                        return bin.getAbsolutePath();
                    }
                }
            }
        }
        // 'which appium' with full PATH
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", "source ~/.nvm/nvm.sh 2>/dev/null; which appium");
            pb.environment().put("PATH", System.getenv("PATH") + ":/opt/homebrew/bin:/usr/local/bin");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = r.readLine();
                if (line != null && !line.isBlank()) {
                    logger.info("'which appium' returned: {}", line.trim());
                    return line.trim();
                }
            }
        } catch (Exception e) {
            logger.warn("Could not run 'which appium': {}", e.getMessage());
        }
        throw new RuntimeException("Cannot find 'appium' binary. Install with: npm install -g appium");
    }

    /**
     * For Android: launches Appium directly via ProcessBuilder with ANDROID_HOME
     * hard-set in the child process environment. This bypasses AppiumServiceBuilder
     * completely and guarantees the Node.js process sees ANDROID_HOME from the start.
     */
    private static void startAndroidAppiumProcess(String androidHome, String driverFlag) throws Exception {
        String appiumBin = resolveAppiumBinary();

        // Build the environment: copy current JVM env, then hard-set Android vars
        Map<String, String> env = new HashMap<>(System.getenv());
        env.put("ANDROID_HOME", androidHome);
        env.put("ANDROID_SDK_ROOT", androidHome);
        env.put("PATH", env.getOrDefault("PATH", "") + ":" + androidHome + "/platform-tools:" + androidHome + "/emulator");

        String ffmpegPath = System.getenv("FFMPEG_PATH");
        if (ffmpegPath != null && !ffmpegPath.isEmpty()) {
            env.put("FFMPEG_PATH", ffmpegPath);
        }

        // Build command: appium --port 4723 --address 127.0.0.1 --session-override
        //                       --log-level error --relaxed-security --use-drivers uiautomator2
        List<String> cmd = new ArrayList<>();
        cmd.add(appiumBin);
        cmd.add("--port");    cmd.add(String.valueOf(port));
        cmd.add("--address"); cmd.add("127.0.0.1");
        cmd.add("--session-override");
        cmd.add("--log-level");     cmd.add("error");
        cmd.add("--relaxed-security");
        cmd.add("--use-drivers");   cmd.add(driverFlag);

        logger.info("Launching Android Appium via ProcessBuilder: {}", String.join(" ", cmd));
        logger.info("  ANDROID_HOME = {}", androidHome);

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.environment().clear();
        pb.environment().putAll(env);
        pb.redirectErrorStream(true);   // merge stderr into stdout
        // Discard output (Appium writes a lot; we only need the health check)
        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);

        androidProcess = pb.start();
        logger.info("Android Appium process started (PID available via ProcessHandle)");

        // Wait up to 20 seconds for the server to become healthy
        int maxWaitMs = 20000;
        int pollMs    = 500;
        int elapsed   = 0;
        while (elapsed < maxWaitMs) {
            if (isAppiumHealthy()) {
                logger.info("Android Appium server is healthy on port {} after {}ms", port, elapsed);
                return;
            }
            Thread.sleep(pollMs);
            elapsed += pollMs;
        }
        throw new RuntimeException("Android Appium server did not become healthy within " + maxWaitMs + "ms");
    }

    private static void startNewServer() {
        logger.info("Starting new Appium server on port {}", port);

        String platform    = System.getProperty("platform", "ios");
        String driverFlag  = platform.equalsIgnoreCase("android") ? "uiautomator2" : "xcuitest";

        String androidHome = System.getenv("ANDROID_HOME");
        if (androidHome == null || androidHome.isEmpty()) {
            androidHome = System.getProperty("user.home") + "/Library/Android/sdk";
        }

        logger.info("Appium server starting for platform: {} (driver: {})", platform, driverFlag);

        if (platform.equalsIgnoreCase("android")) {
            // ── Android: launch via raw ProcessBuilder so ANDROID_HOME is guaranteed ──
            try {
                startAndroidAppiumProcess(androidHome, driverFlag);
                logger.info("Android Appium server started successfully on port {}", port);
            } catch (Exception e) {
                logger.error("Error starting Android Appium server: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to start Android Appium server", e);
            }
        } else {
            // ── iOS: keep using AppiumServiceBuilder (xcuitest doesn't need ANDROID_HOME) ──
            Map<String, String> appiumEnv = buildAppiumEnvironment();
            AppiumServiceBuilder builder = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")
                    .usingPort(port)
                    .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                    .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                    .withArgument(GeneralServerFlag.USE_DRIVERS, driverFlag)
                    .withEnvironment(appiumEnv);

            try {
                service = AppiumDriverLocalService.buildService(builder);
                service.start();
                if (service.isRunning()) {
                    logger.info("iOS Appium server started successfully on: {}", service.getUrl());
                } else {
                    throw new RuntimeException("Could not start iOS Appium server");
                }
            } catch (Exception e) {
                logger.error("Error starting iOS Appium server: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to start iOS Appium server", e);
            }
        }
    }

    /**
     * Builds environment map for iOS AppiumServiceBuilder (ffmpeg + path extras).
     */
    private static Map<String, String> buildAppiumEnvironment() {
        Map<String, String> env = new HashMap<>(System.getenv());

        String currentPath = env.getOrDefault("PATH", "");
        String[] extraDirs = {"/opt/homebrew/bin", "/usr/local/bin"};
        StringBuilder pathBuilder = new StringBuilder(currentPath);
        for (String dir : extraDirs) {
            if (!currentPath.contains(dir)) {
                if (pathBuilder.length() > 0) pathBuilder.append(":");
                pathBuilder.append(dir);
            }
        }
        env.put("PATH", pathBuilder.toString());
        logger.info("Appium child process PATH: {}", pathBuilder);

        String ffmpegPath = System.getenv("FFMPEG_PATH");
        if (ffmpegPath != null && !ffmpegPath.isEmpty()) {
            env.put("FFMPEG_PATH", ffmpegPath);
            logger.info("FFMPEG_PATH injected into Appium environment: {}", ffmpegPath);
        }

        String androidHome = System.getenv("ANDROID_HOME");
        if (androidHome == null || androidHome.isEmpty()) {
            androidHome = System.getProperty("user.home") + "/Library/Android/sdk";
        }
        env.put("ANDROID_HOME", androidHome);
        env.put("ANDROID_SDK_ROOT", androidHome);
        logger.info("ANDROID_HOME injected into Appium environment: {}", androidHome);

        return env;
    }

    public static void stopServer() {
        // Stop iOS service
        if (service != null && service.isRunning()) {
            try {
                service.stop();
                logger.info("iOS Appium server stopped");
            } catch (Exception e) {
                logger.error("Error stopping iOS Appium server: {}", e.getMessage());
            } finally {
                service = null;
            }
        }
        // Stop Android raw process
        if (androidProcess != null && androidProcess.isAlive()) {
            try {
                androidProcess.destroy();
                androidProcess.waitFor(5, TimeUnit.SECONDS);
                if (androidProcess.isAlive()) {
                    androidProcess.destroyForcibly();
                }
                logger.info("Android Appium process stopped");
            } catch (Exception e) {
                logger.error("Error stopping Android Appium process: {}", e.getMessage());
            } finally {
                androidProcess = null;
            }
        }
        // Also kill by port to be sure
        killExternalAppiumServer();
    }

    private static boolean isAppiumServerRunning() {
        return isPortOccupied(port) && isAppiumHealthy();
    }

    private static boolean isPortOccupied(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private static boolean isAppiumHealthy() {
        try {
            URL url = new URL(APPIUM_URL + "/status");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getServerUrl() {
        if (service != null && service.isRunning()) {
            return service.getUrl().toString();
        }
        return APPIUM_URL + "/wd/hub";
    }

    public static boolean isManagingServer() {
        if (service != null && service.isRunning()) return true;
        return androidProcess != null && androidProcess.isAlive();
    }

    public static void ensureServerAvailable() {
        try {
            startOrConnectToServer();
            if (!isAppiumHealthy()) {
                throw new RuntimeException("Appium server is not responding to health checks");
            }
            logger.info("Appium server is available at: {}", getServerUrl());
        } catch (Exception e) {
            logger.error("Failed to ensure Appium server availability", e);
            throw new RuntimeException("Failed to ensure Appium server availability", e);
        }
    }

    public static void cleanup() {
        try {
            if (isManagingServer()) {
                stopServer();
            }
            cleanupProcesses();
        } catch (Exception e) {
            logger.error("Error during cleanup", e);
        }
    }

    private static void cleanupProcesses() {
        try {
            ProcessBuilder pb = new ProcessBuilder("pkill", "-f", "WebDriverAgent");
            Process process = pb.start();
            process.waitFor(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.debug("Could not cleanup WebDriverAgent processes: {}", e.getMessage());
        }
    }

    public static void forceRestartServer() {
        logger.info("Force restarting Appium server...");
        stopServer();
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        startNewServer();
    }

    public static String getServerStatus() {
        if (service != null && service.isRunning()) return "iOS managed server running on " + service.getUrl();
        if (androidProcess != null && androidProcess.isAlive()) return "Android process server running on " + APPIUM_URL;
        if (isAppiumServerRunning()) return "External server detected on " + APPIUM_URL;
        return "No server detected";
    }
}
