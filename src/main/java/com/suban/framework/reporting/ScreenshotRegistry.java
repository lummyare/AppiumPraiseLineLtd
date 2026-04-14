package com.suban.framework.reporting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Thread-safe storage for screenshot paths to be shared between hooks and event listeners
 */
public class ScreenshotRegistry {
    
    private static final Map<String, String> screenshotPaths = new ConcurrentHashMap<>();
    
    /**
     * Store screenshot path for a scenario
     * @param scenarioName The scenario name
     * @param screenshotPath The path to the screenshot
     */
    public static void setScreenshotPath(String scenarioName, String screenshotPath) {
        if (scenarioName != null && screenshotPath != null) {
            screenshotPaths.put(scenarioName, screenshotPath);
        }
    }
    
    /**
     * Get screenshot path for a scenario
     * @param scenarioName The scenario name
     * @return The screenshot path or null if not found
     */
    public static String getScreenshotPath(String scenarioName) {
        return screenshotPaths.get(scenarioName);
    }
    
    /**
     * Remove screenshot path for a scenario (cleanup)
     * @param scenarioName The scenario name
     */
    public static void removeScreenshotPath(String scenarioName) {
        screenshotPaths.remove(scenarioName);
    }
    
    /**
     * Clear all screenshot paths (cleanup)
     */
    public static void clearAll() {
        screenshotPaths.clear();
    }
}
