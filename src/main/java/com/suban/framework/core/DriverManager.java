package com.suban.framework.core;

import com.suban.framework.config.ConfigReader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.time.Duration;

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

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setApp(new File(ConfigReader.getProperty("android.app.path")).getAbsolutePath())
                .setAutomationName("UiAutomator2")
                .setAppWaitActivity("*")
                .setNoReset(false)
                .setFullReset(false)
                .setNewCommandTimeout(Duration.ofSeconds(60))  // Add timeout
                .setAutoGrantPermissions(true)
                ;

        boolean isEmulatorToUse = ConfigReader.getBooleanProperty("device.use.emulator");
        if (isEmulatorToUse) {
            options.setDeviceName("Android Emulator");
            options.setAvd(ConfigReader.getProperty("android.avd.name"));
        } else {
            options.setDeviceName("Samsung");
            options.setUdid(ConfigReader.getProperty("android.device.udid")); // or your specific device name
        }
        String serverUrl = AppiumServer.getServerUrl();
        logger.info("Connecting to Appium server at: {}", serverUrl);

        try {
            return new AndroidDriver(new URI(serverUrl).toURL(), options);
        } catch (Exception e) {
            logger.error("Failed to create AndroidDriver with server at: {}", serverUrl, e);
            handleDriverCreationFailure();
            throw e;
        }
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
                // Auto-accept ALL iOS system permission dialogs (notifications, location, etc.)
                // This prevents dialogs from blocking element interactions
                .setAutoAcceptAlerts(true);

        boolean isEmulatorToUse = ConfigReader.getBooleanProperty("device.use.emulator");

        // Resolve app path — config stores the absolute .app path (e.g. DerivedData/.../Subaru.app)
        // Use it directly; only prepend user.dir for relative paths
        String appPath = ConfigReader.getProperty("ios.app.path");
        if (appPath != null && !appPath.isEmpty()) {
            File appFile = new File(appPath);
            String resolvedAppPath = appFile.isAbsolute() ? appPath : new File(System.getProperty("user.dir"), appPath).getAbsolutePath();
            options.setApp(resolvedAppPath);
            logger.info("iOS app path resolved to: {}", resolvedAppPath);
        }

        if (!isEmulatorToUse) {
            // Real device specific capabilities
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
        logger.info("Connecting to Appium server at: {}", serverUrl);

        try {
            return new IOSDriver(new URI(serverUrl).toURL(), options);
        } catch (Exception e) {
            logger.error("Failed to create IOSDriver with server at: {}", serverUrl, e);
            handleDriverCreationFailure();
            throw e;
        }
    }

    private static void handleDriverCreationFailure() {
        // If connection fails and we're not managing the server, try to restart
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
        if (driver == null) {
            return false;
        }

        try {
            // Try to get session ID to check if driver is still active
            driver.getSessionId();
            return true;
        } catch (Exception e) {
            logger.debug("Driver is not active: {}", e.getMessage());
            return false;
        }
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
        // Additional cleanup if needed
    }
}