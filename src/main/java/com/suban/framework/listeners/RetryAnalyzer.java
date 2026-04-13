package com.suban.framework.listeners;

import com.suban.framework.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry mechanism for flaky mobile tests
 * Automatically retries failed tests based on failure type
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class);
    private static final int MAX_RETRY_COUNT;
    private int retryCount = 0;
    
    static {
        // Try to get retry count from config, default to 2
        int configuredRetryCount = 2;
        try {
            configuredRetryCount = ConfigReader.getIntProperty("test.retry.count");
        } catch (Exception e) {
            logger.debug("Could not read retry count from config, using default: 2");
        }
        MAX_RETRY_COUNT = configuredRetryCount;
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            
            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                String errorMessage = throwable.getMessage();
                
                // Retry for specific mobile-related errors
                if (isRetryableError(errorMessage)) {
                    logger.warn("Test '{}' failed with retryable error. Retry {}/{}: {}", 
                        result.getMethod().getMethodName(), retryCount, MAX_RETRY_COUNT, errorMessage);
                    return true;
                } else {
                    logger.error("Test '{}' failed with non-retryable error: {}", 
                        result.getMethod().getMethodName(), errorMessage);
                }
            }
        }
        
        return false;
    }
    
    private boolean isRetryableError(String errorMessage) {
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
