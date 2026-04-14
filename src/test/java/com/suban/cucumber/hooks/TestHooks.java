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
                String platform = ConfigReader.getProperty("platform");
                driver = DriverManager.getDriver(platform);
                ScreenshotUtils.setDriver(driver);
                logger.info("Driver initialized for platform={}, scenario: {}", platform, scenario.getName());

                // Brief pause to let the app session fully initialise before recording starts.
                // Without this, startRecordingScreen can silently fail on a still-launching session.
                Thread.sleep(1500);
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
                    // Brief pause then start recording after recovery
                    Thread.sleep(1500);
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

            // Attach screenshot inline to Cucumber HTML report (for report readability).
            // RecordingManager already saved the PNG file to test-output/screenshots/,
            // so here we ONLY attach to the report — we skip the redundant file save.
            if (scenario.isFailed()) {
                logger.info("Scenario failed, attaching screenshot to Cucumber report");
                attachScreenshotToReport(scenario);
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
     * Attaches a screenshot to the Cucumber HTML report for inline visibility.
     * File saving is handled exclusively by RecordingManager to avoid duplication.
     */
    private void attachScreenshotToReport(Scenario scenario) {
        AppiumDriver currentDriver = null;
        try {
            if (driver != null && DriverManager.isDriverActive()) {
                currentDriver = driver;
            } else {
                currentDriver = DriverManager.getCurrentDriver();
                if (currentDriver == null || !DriverManager.isDriverActive()) {
                    logger.warn("No active driver found for Cucumber report screenshot");
                    return;
                }
            }
            byte[] screenshot = ((TakesScreenshot) currentDriver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
            logger.info("Screenshot attached to Cucumber report for: {}", scenario.getName());
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to Cucumber report: {}", e.getMessage());
            // Fallback: attach page source so there is still something in the report
            try {
                if (currentDriver != null) {
                    scenario.attach(currentDriver.getPageSource().getBytes(), "text/html", "Page Source");
                }
            } catch (Exception ignored) {}
        }
    }
}