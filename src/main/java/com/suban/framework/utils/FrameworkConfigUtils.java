package com.suban.framework.utils;

import com.suban.framework.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Framework configuration and optimization utilities
 */
public class FrameworkConfigUtils {
    private static final Logger logger = LoggerFactory.getLogger(FrameworkConfigUtils.class);
    
    private static final String EXPLICIT_WAIT_KEY = "explicit.wait";
    private static final String RETRY_COUNT_KEY = "test.retry.count";
    private static final String IMPLICIT_WAIT_KEY = "implicit.wait";
    private static final String SCREENSHOT_ON_FAILURE_KEY = "screenshot.on.failure";
    private static final String PERFORMANCE_MONITORING_KEY = "performance.monitoring.enabled";
    
    /**
     * Get explicit wait timeout in seconds
     */
    public static int getExplicitWaitTimeout() {
        try {
            String value = ConfigReader.getProperty(EXPLICIT_WAIT_KEY);
            return value != null ? Integer.parseInt(value) : 15;
        } catch (NumberFormatException e) {
            logger.warn("Invalid explicit wait timeout, using default: 15 seconds");
            return 15;
        }
    }
    
    /**
     * Get retry count for failed tests
     */
    public static int getRetryCount() {
        try {
            String value = ConfigReader.getProperty(RETRY_COUNT_KEY);
            return value != null ? Integer.parseInt(value) : 2;
        } catch (NumberFormatException e) {
            logger.warn("Invalid retry count, using default: 2");
            return 2;
        }
    }
    
    /**
     * Get implicit wait timeout in seconds
     */
    public static int getImplicitWaitTimeout() {
        try {
            String value = ConfigReader.getProperty(IMPLICIT_WAIT_KEY);
            return value != null ? Integer.parseInt(value) : 10;
        } catch (NumberFormatException e) {
            logger.warn("Invalid implicit wait timeout, using default: 10 seconds");
            return 10;
        }
    }
    
    /**
     * Check if screenshots should be taken on failure
     */
    public static boolean isScreenshotOnFailureEnabled() {
        String value = ConfigReader.getProperty(SCREENSHOT_ON_FAILURE_KEY);
        return value != null ? Boolean.parseBoolean(value) : true;
    }
    
    /**
     * Check if performance monitoring is enabled
     */
    public static boolean isPerformanceMonitoringEnabled() {
        String value = ConfigReader.getProperty(PERFORMANCE_MONITORING_KEY);
        return value != null ? Boolean.parseBoolean(value) : true;
    }
    
    /**
     * Initialize framework configuration
     */
    public static void initializeFrameworkConfiguration() {
        logger.info("=== Framework Configuration ===");
        logger.info("Explicit Wait Timeout: {} seconds", getExplicitWaitTimeout());
        logger.info("Implicit Wait Timeout: {} seconds", getImplicitWaitTimeout());
        logger.info("Test Retry Count: {}", getRetryCount());
        logger.info("Screenshot on Failure: {}", isScreenshotOnFailureEnabled());
        logger.info("Performance Monitoring: {}", isPerformanceMonitoringEnabled());
        logger.info("================================");
    }
}
