package com.suban.framework.listeners;

import com.suban.framework.core.DriverManager;
import com.suban.framework.reporting.ReportUtils;
import com.suban.framework.reporting.ScreenshotUtils;
import com.suban.framework.monitoring.PerformanceMonitor;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestListenerUtils implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListenerUtils.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test suite started: {}", context.getName());
        // Note: Report initialization is handled by CucumberEventListener for Cucumber tests
        // This avoids duplicate report initialization and blank report generation
        ReportUtils.initReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test in the report when a test method starts
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        String fullTestName = testDescription != null && !testDescription.isEmpty() ?
                testName + " - " + testDescription : testName;

        logger.info("Test started: {}", fullTestName);
        ReportUtils.createTest(fullTestName);
        ReportUtils.logInfo("Test started: " + fullTestName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getMethod().getMethodName());
        ReportUtils.logTestResult(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getMethod().getMethodName());
        ReportUtils.logTestResult(result);
        // Enhanced screenshot capture for failed tests
        captureTestFailureScreenshot(result);
    }
    
    /**
     * Capture screenshot for test failure with enhanced error handling
     */
    private void captureTestFailureScreenshot(ITestResult result) {
        try {
            // Set current driver in ScreenshotUtils if available
            AppiumDriver currentDriver = DriverManager.getCurrentDriver();
            if (currentDriver != null) {
                ScreenshotUtils.setDriver(currentDriver);
            }
            
            String testName = result.getMethod().getMethodName();
            String screenshotPath = ScreenshotUtils.captureScreenshot("testng_failure_" + testName);
            
            if (screenshotPath != null) {
                logger.info("Screenshot captured for failure: {}", screenshotPath);
                ReportUtils.addScreenshot(screenshotPath, "Test Failure Screenshot");
                ReportUtils.logFail("Screenshot captured for failure: " + testName);
                
                // Also try to attach as base64 if possible
                try {
                    if (currentDriver != null && DriverManager.isDriverActive()) {
                        String base64Screenshot = ((TakesScreenshot) currentDriver).getScreenshotAs(OutputType.BASE64);
                        ReportUtils.logFail("Screenshot Data: data:image/png;base64," + base64Screenshot);
                        logger.debug("Base64 screenshot also added to report");
                    }
                } catch (Exception base64Exception) {
                    logger.debug("Base64 screenshot attachment failed: {}", base64Exception.getMessage());
                }
                
            } else {
                logger.warn("No screenshot captured - driver not available");
                ReportUtils.logFail("Screenshot not available - driver not active");
                
                // Try to capture diagnostic information
                try {
                    if (currentDriver != null) {
                        String pageSource = currentDriver.getPageSource();
                        ReportUtils.logInfo("Page Source (as diagnostic info): " + pageSource.substring(0, Math.min(500, pageSource.length())) + "...");
                    }
                } catch (Exception diagnosticException) {
                    logger.debug("Diagnostic info capture also failed: {}", diagnosticException.getMessage());
                }
            }
            
        } catch (Exception e) {
            logger.error("Error during screenshot capture process: {}", e.getMessage());
            ReportUtils.logFail("Screenshot capture failed: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {} Reason: {}", result.getMethod().getMethodName(), result.getThrowable());
        ReportUtils.logTestResult(result);
        //ReportUtils.logWarning("Test was skipped due to: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test suite finished: {}", context.getName());
        
        // Generate performance report for TestNG tests
        PerformanceMonitor.generatePerformanceReport();

        // Note: Report flushing is handled by CucumberEventListener for Cucumber tests
        // This avoids duplicate flushing and ensures proper report generation
         ReportUtils.flushReports();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: {}", result.getMethod().getMethodName());
        ReportUtils.createTest(result.getMethod().getMethodName() + " (Failed but % Passed)");
        ReportUtils.logWarning("Test failed but within success percentage: " + result.getMethod().getMethodName());
        // Handle tests that failed but are within success percentage
    }
}

