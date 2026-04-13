package com.suban.framework.utils;

import com.suban.framework.monitoring.PerformanceMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for managing advanced test features
 * Provides easy access to performance monitoring and retry functionality
 */
public class TestEnhancementUtils {
    private static final Logger logger = LoggerFactory.getLogger(TestEnhancementUtils.class);
    
    /**
     * Initialize framework with all enhancements
     */
    public static void initializeFramework() {
        logger.info("=== Initializing Enhanced Test Framework ===");
        FrameworkConfigUtils.initializeFrameworkConfiguration();
        logger.info("=== Framework Initialization Complete ===");
    }
    
    /**
     * Start monitoring performance for a specific operation
     * @param operationName Name of the operation to monitor
     */
    public static void startPerformanceMonitoring(String operationName) {
        PerformanceMonitor.startTest(operationName);
        logger.debug("Started performance monitoring for: {}", operationName);
    }
    
    /**
     * End monitoring performance for a specific operation
     * @param operationName Name of the operation to stop monitoring
     */
    public static void endPerformanceMonitoring(String operationName) {
        PerformanceMonitor.endTest(operationName);
        logger.debug("Ended performance monitoring for: {}", operationName);
    }
    
    /**
     * Get performance metrics for a specific operation
     * @param operationName Name of the operation
     * @return PerformanceMetrics object or null if not found
     */
    public static PerformanceMonitor.PerformanceMetrics getPerformanceMetrics(String operationName) {
        return PerformanceMonitor.getPerformanceMetrics(operationName);
    }
    
    /**
     * Generate and log performance report
     */
    public static void generatePerformanceReport() {
        PerformanceMonitor.generatePerformanceReport();
    }
    
    /**
     * Check if an error is retryable based on RetryAnalyzer logic
     * @param throwable The exception to check
     * @return true if the error should be retried
     */
    public static boolean isRetryableError(Throwable throwable) {
        if (throwable == null) return false;
        
        String errorMessage = throwable.getMessage();
        if (errorMessage == null) return false;
        
        return errorMessage.contains("TimeoutException") ||
               errorMessage.contains("StaleElementReferenceException") ||
               errorMessage.contains("NoSuchElementException") ||
               errorMessage.contains("WebDriverException") ||
               errorMessage.contains("SessionNotCreatedException") ||
               errorMessage.contains("Unable to find element") ||
               errorMessage.contains("Element is not clickable") ||
               errorMessage.contains("Connection refused") ||
               errorMessage.contains("Socket timeout");
    }
}
