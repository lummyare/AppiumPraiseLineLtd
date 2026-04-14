package com.suban.framework.monitoring;

import com.suban.framework.core.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Performance monitoring for mobile app testing
 * Tracks test execution times, memory usage, and app performance
 */
public class PerformanceMonitor {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitor.class);
    private static final Map<String, Long> testStartTimes = new ConcurrentHashMap<>();
    private static final Map<String, Long> testExecutionTimes = new ConcurrentHashMap<>();
    private static final Map<String, PerformanceMetrics> performanceData = new ConcurrentHashMap<>();
    
    public static void startTest(String testName) {
        long startTime = System.currentTimeMillis();
        testStartTimes.put(testName, startTime);
        logger.debug("Performance monitoring started for test: {}", testName);
    }
    
    public static void endTest(String testName) {
        Long startTime = testStartTimes.get(testName);
        if (startTime != null) {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            testExecutionTimes.put(testName, executionTime);
            
            // Collect additional performance metrics
            collectPerformanceMetrics(testName);
            
            logger.info("Test '{}' completed in {}ms", testName, executionTime);
            testStartTimes.remove(testName);
        }
    }
    
    private static void collectPerformanceMetrics(String testName) {
        try {
            AppiumDriver driver = DriverManager.getCurrentDriver();
            if (driver != null) {
                PerformanceMetrics metrics = new PerformanceMetrics();
                metrics.setExecutionTime(testExecutionTimes.get(testName));
                metrics.setMemoryUsage(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                metrics.setTimestamp(System.currentTimeMillis());
                
                performanceData.put(testName, metrics);
            }
        } catch (Exception e) {
            logger.warn("Failed to collect performance metrics for test: {}", testName, e);
        }
    }
    
    public static PerformanceMetrics getPerformanceMetrics(String testName) {
        return performanceData.get(testName);
    }
    
    public static Map<String, Long> getAllExecutionTimes() {
        return new HashMap<>(testExecutionTimes);
    }
    
    public static void generatePerformanceReport() {
        logger.info("=== Performance Report ===");
        testExecutionTimes.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> {
                String testName = entry.getKey();
                Long executionTime = entry.getValue();
                PerformanceMetrics metrics = performanceData.get(testName);
                
                logger.info("Test: {} | Execution Time: {}ms | Memory: {}MB", 
                    testName, 
                    executionTime,
                    metrics != null ? metrics.getMemoryUsage() / (1024 * 1024) : "N/A");
            });
        logger.info("=== End Performance Report ===");
    }
    
    public static class PerformanceMetrics {
        private long executionTime;
        private long memoryUsage;
        private long timestamp;
        
        // Getters and setters
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
        
        public long getMemoryUsage() { return memoryUsage; }
        public void setMemoryUsage(long memoryUsage) { this.memoryUsage = memoryUsage; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}
