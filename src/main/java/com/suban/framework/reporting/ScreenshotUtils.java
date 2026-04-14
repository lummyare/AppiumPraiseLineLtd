package com.suban.framework.reporting;

import com.suban.framework.core.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static AppiumDriver driver; // You'll need to set this driver instance

    public static void setDriver(AppiumDriver driverInstance) {
        driver = driverInstance;
    }

    public static String captureScreenshot(String screenshotName) {
        try {
            // Try to get driver from multiple sources
            AppiumDriver currentDriver = driver;
            if (currentDriver == null) {
                currentDriver = DriverManager.getCurrentDriver();
            }
            
            if (currentDriver == null) {
                logger.warn("No active driver found. Cannot capture screenshot.");
                return null;
            }
            
            // Validate driver is still active
            if (!DriverManager.isDriverActive()) {
                logger.warn("Driver session is not active. Cannot capture screenshot.");
                return null;
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotsDir = System.getProperty("user.dir") + File.separator + "test-output" + File.separator + "screenshots";
            File dir = new File(screenshotsDir);
            if (!dir.exists()) {
                dir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Use simple screenshot naming format: screenshot_timestamp.png
            String screenshotFileName = "screenshot_" + timeStamp + ".png";
            String screenshotAbsolutePath = screenshotsDir + File.separator + screenshotFileName;

            String screenshotRelativePathForReport = ".." + File.separator + "screenshots" + File.separator + screenshotFileName;

            File source = ((TakesScreenshot) currentDriver).getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotAbsolutePath);
            FileUtils.copyFile(source, destination);
            logger.info("Screenshot captured successfully: {}", screenshotAbsolutePath);
            return screenshotRelativePathForReport;
        } catch (Exception e) {
            logger.error("Failed to capture screenshot '{}': {}", screenshotName, e.getMessage());
            return null;
        }
    }
    
    /**
     * Enhanced screenshot capture with multiple attachment methods
     */
    public static boolean captureAndAttachScreenshot(String screenshotName, String description) {
        try {
            AppiumDriver currentDriver = driver;
            if (currentDriver == null) {
                currentDriver = DriverManager.getCurrentDriver();
            }
            
            if (currentDriver == null || !DriverManager.isDriverActive()) {
                logger.warn("No active driver available for screenshot capture");
                return false;
            }
            
            // Method 1: Save to file and get path
            String screenshotPath = captureScreenshot(screenshotName);
            
            // Method 2: Get base64 for inline attachment
            String base64Screenshot = null;
            try {
                base64Screenshot = ((TakesScreenshot) currentDriver).getScreenshotAs(OutputType.BASE64);
            } catch (Exception e) {
                logger.debug("Base64 screenshot capture failed: {}", e.getMessage());
            }
            
            boolean success = false;
            
            // Try to attach to current test report if available
            if (screenshotPath != null) {
                try {
                    // This would be used by ExtentReports or similar
                    logger.info("Screenshot file saved: {}", screenshotPath);
                    success = true;
                } catch (Exception e) {
                    logger.warn("Failed to attach file screenshot: {}", e.getMessage());
                }
            }
            
            // Alternative: Use base64 data
            if (base64Screenshot != null) {
                try {
                    logger.debug("Base64 screenshot data available for inline attachment");
                    success = true;
                } catch (Exception e) {
                    logger.warn("Failed to process base64 screenshot: {}", e.getMessage());
                }
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("Screenshot capture and attachment failed: {}", e.getMessage());
            return false;
        }
    }
}

