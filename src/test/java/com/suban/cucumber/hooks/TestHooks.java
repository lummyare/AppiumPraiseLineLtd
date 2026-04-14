package com.suban.cucumber.hooks;

import com.suban.framework.core.DriverManager;
import com.suban.framework.core.AppiumServer;
import com.suban.framework.core.DeviceManager;
import com.suban.framework.config.ConfigReader;
import com.suban.framework.reporting.ScreenshotUtils;
import com.suban.framework.monitoring.PerformanceMonitor;
import com.suban.framework.utils.TestEnhancementUtils;
import com.suban.framework.recording.RecordingManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Cucumber hooks for setup and teardown operations
 */
public class TestHooks {

    private static final Logger logger = LogManager.getLogger(TestHooks.class);
    public AppiumDriver driver;

    /** One RecordingManager instance per scenario (Cucumber creates a new TestHooks per scenario). */
    private final RecordingManager recordingManager = new RecordingManager();

    @BeforeAll
    public static void globalSetup() {
        try {
            logger.info("Starting global test setup");
            TestEnhancementUtils.initializeFramework();

            // Initialize Appium server (will connect to existing or start new)
            AppiumServer.ensureServerAvailable();
            logger.info("Appium server is ready for test execution");

        } catch (Exception e) {
            logger.error("Failed during global setup", e);
            throw new RuntimeException("Global setup failed", e);
        }
    }

    @Before
    public void setUp(Scenario scenario) {
        try {
            logger.info("Starting scenario: {}", scenario.getName());

            // Start performance monitoring
            PerformanceMonitor.startTest(scenario.getName());

            if (!scenario.getName().contains("demo")) {
                // Get driver (will reuse server and device if available)
                driver = DriverManager.getDriver(ConfigReader.getProperty("platform"));
                ScreenshotUtils.setDriver(driver);
                logger.info("Driver initialized for scenario: {}", scenario.getName());

                // Start video recording immediately after driver is ready
                recordingManager.startRecording(driver, scenario.getName());
            } else {
                logger.info("Demo scenario detected, skipping driver initialization");
            }

        } catch (Exception e) {
            logger.error("Failed to setup scenario: {}", scenario.getName(), e);

            // Try to recover by restarting server if needed
            try {
                if (!AppiumServer.isManagingServer()) {
                    logger.info("Attempting server recovery...");
                    AppiumServer.forceRestartServer();
                    driver = DriverManager.getDriver(ConfigReader.getProperty("platform"));
                    ScreenshotUtils.setDriver(driver);
                    logger.info("Recovery successful for scenario: {}", scenario.getName());
                    // Start recording after recovery
                    recordingManager.startRecording(driver, scenario.getName());
                } else {
                    throw e;
                }
            } catch (Exception recoveryException) {
                logger.error("Failed to recover from setup failure", recoveryException);
                throw new RuntimeException("Failed to setup test scenario", e);
            }
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            logger.info("Finishing scenario: {} - Status: {}", scenario.getName(), scenario.getStatus());

            // End performance monitoring
            PerformanceMonitor.endTest(scenario.getName());

            // Stop recording + handle screenshot/video save logic
            // RecordingManager handles: screenshot on fail, video always, old file cleanup
            AppiumDriver currentDriver = (driver != null && DriverManager.isDriverActive())
                ? driver : DriverManager.getCurrentDriver();
            recordingManager.stopAndSave(currentDriver, scenario.getName(), scenario.isFailed());

            // Legacy Cucumber report screenshot (attached inline to HTML report)
            if (scenario.isFailed()) {
                logger.info("Scenario failed, attaching screenshot to Cucumber report");
                captureFailureScreenshot(scenario);
            }

            // Only quit the driver after each test, keep emulator and server running
            DriverManager.quitDriver();
            logger.debug("Driver cleanup completed for scenario: {}", scenario.getName());

        } catch (Exception e) {
            logger.error("Error in tearDown for scenario: {}", scenario.getName(), e);
        }
    }

    @AfterAll
    public static void globalTearDown() {
        try {
            logger.info("Running global cleanup after all tests");

            // Generate performance report
            PerformanceMonitor.generatePerformanceReport();

            // Quit any remaining drivers
            DriverManager.quitAllDrivers();

            // Stop emulator and Appium server after all tests complete
            String platform = ConfigReader.getProperty("platform");
            boolean isEmulatorToUse = ConfigReader.getBooleanProperty("device.use.emulator");

            if (isEmulatorToUse) {
                if (platform.equalsIgnoreCase("android")) {
                    DeviceManager.stopAndroidEmulator();
                } else if (platform.equalsIgnoreCase("ios")) {
                    String simulatorUdid = ConfigReader.getProperty("ios.simulator.udid");
                    DeviceManager.stopIOSSimulator(simulatorUdid);
                }
                logger.info("Device/simulator stopped");
            }

            // Only stop server if we're managing it
            AppiumServer.cleanup();
            logger.info("Global cleanup completed");

        } catch (Exception e) {
            logger.error("Error during global cleanup: {}", e.getMessage(), e);
        }
    }

    /**
     * Capture screenshot on test failure with multiple fallback strategies
     */
    private void captureFailureScreenshot(Scenario scenario) {
        AppiumDriver currentDriver = null;

        try {
            // Strategy 1: Use the instance driver
            if (driver != null && DriverManager.isDriverActive()) {
                currentDriver = driver;
                logger.debug("Using instance driver for screenshot");
            } else {
                // Strategy 2: Get current driver from DriverManager
                currentDriver = DriverManager.getCurrentDriver();
                if (currentDriver != null && DriverManager.isDriverActive()) {
                    logger.debug("Using DriverManager current driver for screenshot");
                } else {
                    logger.warn("No active driver found for screenshot capture");
                    return;
                }
            }

            // Capture screenshot for Cucumber report
            byte[] screenshot = ((TakesScreenshot) currentDriver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
            logger.info("Screenshot attached to Cucumber report for failed scenario: {}", scenario.getName());

            // Also save screenshot to file system for ExtentReports
            try {
                ScreenshotUtils.setDriver(currentDriver);
                String screenshotPath = ScreenshotUtils.captureScreenshot("failure_" + scenario.getName().replaceAll("[^a-zA-Z0-9]", "_"));
                if (screenshotPath != null) {
                    logger.info("Screenshot also saved to file system: {}", screenshotPath);
                    // Store screenshot path for ExtentReport integration
                    com.suban.framework.reporting.ScreenshotRegistry.setScreenshotPath(scenario.getName(), screenshotPath);
                }
            } catch (Exception fileScreenshotException) {
                logger.warn("Failed to save screenshot to file system: {}", fileScreenshotException.getMessage());
            }

        } catch (Exception screenshotException) {
            logger.error("Failed to capture screenshot for Cucumber report: {}", screenshotException.getMessage());

            // Fallback: Try to get some diagnostic information
            try {
                if (currentDriver != null) {
                    String pageSource = currentDriver.getPageSource();
                    scenario.attach(pageSource.getBytes(), "text/html", "Page Source");
                    logger.info("Page source attached as fallback for failed screenshot");
                }
            } catch (Exception pageSourceException) {
                logger.error("Even page source capture failed: {}", pageSourceException.getMessage());
            }
        }
    }
}