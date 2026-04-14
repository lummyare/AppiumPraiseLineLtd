package com.suban.framework.core;

import com.suban.framework.config.ConfigReader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static ThreadLocal<AppiumDriver> driverThreadLocal = new ThreadLocal<>();

    public static AppiumDriver getDriver(String platform) throws Exception {
        if (driverThreadLocal.get() != null) {
            logger.info("Returning existing driver for platform: {}", platform);
            return driverThreadLocal.get();
        }

        logger.info("Creating new driver for platform: {}", platform);

        // Ensure Appium server is available before creating driver
        AppiumServer.ensureServerAvailable();

        // Start device/simulator if using emulator
        boolean isEmulatorToUse = ConfigReader.getBooleanProperty("device.use.emulator");
        if (isEmulatorToUse) {
            startDevice(platform);
        }

        AppiumDriver driver;
        switch (platform.toLowerCase()) {
            case "android":
                driver = createAndroidDriver();
                // Set Android resource ID prefix so BasePage.getDynamicElement() works
                String androidPrefix = ConfigReader.getProperty("app.subaru.android.prefix");
                if (androidPrefix != null && !androidPrefix.isEmpty()) {
                    System.setProperty("app.dynamic.prefix", androidPrefix);
                    logger.info("Android resource ID prefix set to: {}", androidPrefix);
                }
                break;
            case "ios":
                driver = createIOSDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }

        driverThreadLocal.set(driver);
        logger.info("Driver created and stored for platform: {}", platform);
        return driver;
    }

    /**
     * Auto-discovers the latest .apk file in the configured apps folder.
     * Falls back to android.app.path if no APK found in the folder.
     * This lets you drop a new APK into src/test/resources/apps/ and run
     * without changing any config — the newest file is always used.
     */
    private static String resolveApkPath() {
        String projectRoot = System.getProperty("user.dir");

        // 1. Try auto-discovery from folder
        String folderPath = ConfigReader.getProperty("android.app.folder");
        if (folderPath != null && !folderPath.isEmpty()) {
            File apkFolder = new File(folderPath).isAbsolute()
                    ? new File(folderPath)
                    : new File(projectRoot, folderPath);

            if (apkFolder.exists() && apkFolder.isDirectory()) {
                File[] apks = apkFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".apk"));
                if (apks != null && apks.length > 0) {
                    // Pick the most recently modified APK
                    Optional<File> latest = Arrays.stream(apks)
                            .max(Comparator.comparingLong(File::lastModified));
                    if (latest.isPresent()) {
                        String path = latest.get().getAbsolutePath();
                        logger.info("APK auto-discovered (latest in apps/): {}", path);
                        return path;
                    }
                }
            }
        }

        // 2. Fallback to explicit path in config
        String configPath = ConfigReader.getProperty("android.app.path");
        if (configPath != null && !configPath.isEmpty()) {
            File f = new File(configPath);
            String resolved = f.isAbsolute() ? configPath : new File(projectRoot, configPath).getAbsolutePath();
            logger.info("APK from config path: {}", resolved);
            return resolved;
        }

        throw new RuntimeException("No APK found. Put a .apk file in src/test/resources/apps/ or set android.app.path in config.properties");
    }

    private static void startDevice(String platform) throws Exception {
        logger.info("Starting device/simulator for platform: {}", platform);

        if (platform.equalsIgnoreCase("android")) {
            String avdName = ConfigReader.getProperty("android.avd.name");
            DeviceManager.startAndroidEmulator(avdName);
        } else if (platform.equalsIgnoreCase("ios")) {
            String simulatorUdid = ConfigReader.getProperty("ios.simulator.udid");
            DeviceManager.startIOSSimulator(simulatorUdid);
        }
    }

    private static AndroidDriver createAndroidDriver() throws Exception {
        logger.info("Creating AndroidDriver instance");

        String apkPath = resolveApkPath();
        String packageName = ConfigReader.getProperty("android.package");
        String mainActivity = ConfigReader.getProperty("android.main.activity");

        boolean useEmulator = ConfigReader.getBooleanProperty("device.use.emulator");

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setApp(apkPath)
                .setAppPackage(packageName)
                .setAppActivity(mainActivity)
                .setAutomationName("UiAutomator2")
                .setAppWaitActivity("*")
                .setNoReset(false)
                .setFullReset(false)
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setAutoGrantPermissions(true);

        // ── ANDROID_HOME as a capability ─────────────────────────────────────────
        // Appium 2.x's UiAutomator2 driver checks ANDROID_HOME from the Node.js
        // process environment snapshot taken at plugin load time — BEFORE our Java
        // code injects it into the child process map. Passing androidSdkRoot as a
        // session capability bypasses that check entirely and is the definitive fix.
        String androidSdkPath = System.getenv("ANDROID_HOME");
        if (androidSdkPath == null || androidSdkPath.isEmpty()) {
            androidSdkPath = System.getProperty("user.home") + "/Library/Android/sdk";
            logger.info("ANDROID_HOME not in env, using fallback: {}", androidSdkPath);
        }
        options.setCapability("appium:androidSdkRoot", androidSdkPath);
        logger.info("androidSdkRoot capability set to: {}", androidSdkPath);

        // Real device installation — a 300MB+ debug APK can take 2–3 minutes to
        // transfer and install over USB. Without these timeouts Appium aborts the
        // session before the APK is installed.
        if (!useEmulator) {
            // Allow debug/test-only APKs to be sideloaded on physical devices
            options.setCapability("appium:allowTestPackages", true);
            // Time (ms) allowed for a single adb command — default is 20000 (20 s)
            // which is far too short for pushing a 338 MB APK over USB.
            options.setCapability("appium:adbExecTimeout", 120000);
            // Time (ms) Appium waits for the full app installation to finish.
            options.setCapability("appium:androidInstallTimeout", 180000);
            // Time (ms) for UiAutomator2 server APK installation on device.
            options.setCapability("appium:uiautomator2ServerInstallTimeout", 120000);
            // Time (ms) to wait for the app to launch after installation.
            options.setCapability("appium:appWaitDuration", 60000);
            logger.info("Real device mode: extended install timeouts applied (adbExecTimeout=120s, androidInstallTimeout=180s)");
        }

        // Resolve the UDID — always use explicit UDID so Appium connects to the
        // already-running device/emulator rather than trying to launch a new one.
        // Auto-detect a running emulator from 'adb devices' if config says emulator-5554
        // or device.use.emulator=true.
        String resolvedUdid = resolveAndroidUdid();
        options.setDeviceName(ConfigReader.getProperty("android.device.name"));
        options.setUdid(resolvedUdid);
        logger.info("Android UDID resolved to: {}", resolvedUdid);

        String serverUrl = AppiumServer.getServerUrl();
        logger.info("Connecting to Appium server at: {} for Android", serverUrl);
        logger.info("APK: {}", apkPath);
        logger.info("Package: {} / Activity: {}", packageName, mainActivity);

        try {
            return new AndroidDriver(new URI(serverUrl).toURL(), options);
        } catch (Exception e) {
            logger.error("Failed to create AndroidDriver: {}", e.getMessage(), e);
            handleDriverCreationFailure();
            throw e;
        }
    }

    /**
     * Resolves the Android device/emulator UDID to connect to.
     *
     * <p>Priority:
     * <ol>
     *   <li>If {@code android.device.udid} in config is set to a real device serial
     *       (does NOT start with "emulator"), use it as-is.</li>
     *   <li>Otherwise (config is "emulator-5554" or device.use.emulator=true), run
     *       {@code adb devices} and pick the first online emulator. This auto-detects
     *       the port even if it changed (emulator-5556, etc.).</li>
     *   <li>If no emulator found via adb, fall back to the config value.</li>
     * </ol>
     */
    private static String resolveAndroidUdid() {
        String configUdid = ConfigReader.getProperty("android.device.udid");
        boolean useEmulator = ConfigReader.getBooleanProperty("device.use.emulator");

        // If config has a physical device serial (not an emulator), use it directly
        if (configUdid != null && !configUdid.isEmpty()
                && !configUdid.startsWith("emulator")
                && !useEmulator) {
            logger.info("Using physical device UDID from config: {}", configUdid);
            return configUdid;
        }

        // Otherwise auto-detect running emulator via adb devices
        try {
            ProcessBuilder pb = new ProcessBuilder("adb", "devices");
            // Ensure adb is on PATH by checking known locations
            String androidHome = System.getenv("ANDROID_HOME");
            if (androidHome == null || androidHome.isEmpty()) {
                androidHome = System.getProperty("user.home") + "/Library/Android/sdk";
            }
            pb.environment().put("ANDROID_HOME", androidHome);
            pb.environment().put("PATH",
                System.getenv("PATH") + ":" + androidHome + "/platform-tools");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // adb devices output format: "emulator-5554\tdevice"
                    if (line.startsWith("emulator-") && line.contains("\tdevice")) {
                        String detected = line.split("\t")[0].trim();
                        logger.info("Auto-detected running emulator via adb: {}", detected);
                        return detected;
                    }
                }
            }
            process.waitFor();
        } catch (Exception e) {
            logger.warn("Could not auto-detect emulator via adb: {}", e.getMessage());
        }

        // Final fallback — use whatever is in config
        String fallback = (configUdid != null && !configUdid.isEmpty()) ? configUdid : "emulator-5554";
        logger.info("Using UDID fallback: {}", fallback);
        return fallback;
    }

    private static IOSDriver createIOSDriver() throws Exception {
        logger.info("Creating IOSDriver instance");

        XCUITestOptions options = new XCUITestOptions()
                .setDeviceName(ConfigReader.getProperty("ios.simulator.name"))
                .setPlatformVersion(ConfigReader.getProperty("ios.platform.version"))
                .setUdid(ConfigReader.getProperty("ios.simulator.udid"))
                .setAutomationName("XCUITest")
                .setBundleId(ConfigReader.getProperty("ios.bundle.id"))
                .setNoReset(false)
                .setFullReset(false)
                .setAutoAcceptAlerts(true);

        boolean isEmulatorToUse = ConfigReader.getBooleanProperty("device.use.emulator");

        String appPath = ConfigReader.getProperty("ios.app.path");
        if (appPath != null && !appPath.isEmpty()) {
            File appFile = new File(appPath);
            String resolvedAppPath = appFile.isAbsolute() ? appPath : new File(System.getProperty("user.dir"), appPath).getAbsolutePath();
            options.setApp(resolvedAppPath);
            logger.info("iOS app path resolved to: {}", resolvedAppPath);
        }

        if (!isEmulatorToUse) {
            options.setCapability("xcodeOrgId", ConfigReader.getProperty("ios.xcode.org.id"));
            options.setCapability("xcodeSigningId", "iPhone Developer");
            options.setCapability("updatedWDABundleId", ConfigReader.getProperty("ios.wda.bundle.id"));
            options.setCapability("showXcodeLog", true);
            options.setCapability("useNewWDA", false);
            options.setCapability("preventWDAAttachments", true);
            options.setCapability("wdaLaunchTimeout", 120000);
            options.setCapability("wdaConnectionTimeout", 120000);
        }

        String serverUrl = AppiumServer.getServerUrl();
        logger.info("Connecting to Appium server at: {} for iOS", serverUrl);

        try {
            return new IOSDriver(new URI(serverUrl).toURL(), options);
        } catch (Exception e) {
            logger.error("Failed to create IOSDriver: {}", e.getMessage(), e);
            handleDriverCreationFailure();
            throw e;
        }
    }

    private static void handleDriverCreationFailure() {
        if (!AppiumServer.isManagingServer()) {
            logger.info("Attempting to restart Appium server...");
            try {
                AppiumServer.forceRestartServer();
            } catch (Exception restartException) {
                logger.error("Failed to restart Appium server", restartException);
            }
        }
    }

    public static AppiumDriver getCurrentDriver() {
        return driverThreadLocal.get();
    }

    public static boolean isDriverActive() {
        AppiumDriver driver = driverThreadLocal.get();
        if (driver == null) return false;
        try {
            driver.getSessionId();
            return true;
        } catch (Exception e) {
            logger.debug("Driver is not active: {}", e.getMessage());
            return false;
        }
    }

    /** Returns true if the current active driver is an AndroidDriver. */
    public static boolean isAndroid() {
        AppiumDriver driver = driverThreadLocal.get();
        return driver instanceof AndroidDriver;
    }

    /** Returns true if the current active driver is an IOSDriver. */
    public static boolean isIOS() {
        AppiumDriver driver = driverThreadLocal.get();
        return driver instanceof IOSDriver;
    }

    public static void quitDriver() {
        AppiumDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error during driver quit", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    public static void quitAllDrivers() {
        quitDriver();
    }
}
