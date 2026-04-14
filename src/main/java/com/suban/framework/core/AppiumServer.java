package com.suban.framework.core;

import com.suban.framework.config.ConfigReader;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AppiumServer {
    private static AppiumDriverLocalService service;
    private static final int port = Integer.parseInt(ConfigReader.getProperty("appium.port"));
    private static final Logger logger = LoggerFactory.getLogger(AppiumServer.class);
    private static final String APPIUM_URL = "http://127.0.0.1:" + port;

    public static void startOrConnectToServer() {
        if (service != null && service.isRunning()) {
            logger.info("Programmatically started Appium server is already running on: {}", service.getUrl());
            return;
        }

        if (isAppiumServerRunning()) {
            logger.info("External Appium server detected on port {}. Will connect to existing server.", port);
            // Don't start a new service, just use the existing one
            service = null; // Ensure we don't try to manage external server
            return;
        }

        // No server running, start our own
        startNewServer();
    }

    private static void startNewServer() {
        logger.info("Starting new Appium server on port {}", port);

        // Inject ffmpeg's directory into the Appium Node subprocess PATH so
        // Appium can find ffmpeg for video recording on Apple Silicon Macs
        // without touching the JVM's own PATH (which would break Appium auto-detection).
        Map<String, String> appiumEnv = buildAppiumEnvironment();

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(port)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .withEnvironment(appiumEnv);

        try {
            service = AppiumDriverLocalService.buildService(builder);
            service.start();

            if (service.isRunning()) {
                logger.info("Appium server started successfully on: {}", service.getUrl());
            } else {
                logger.error("Failed to start Appium server");
                throw new RuntimeException("Could not start Appium server");
            }
        } catch (Exception e) {
            logger.error("Error starting Appium server: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to start Appium server", e);
        }
    }

    /**
     * Builds a custom environment map for the Appium child process.
     * Extends the current PATH with directories where ffmpeg commonly lives on macOS
     * (Homebrew Apple Silicon: /opt/homebrew/bin, Homebrew Intel: /usr/local/bin).
     * We do NOT prepend to the global JVM PATH — only the spawned Appium server process
     * gets the extended PATH, so AppiumServiceBuilder still finds the correct appium binary.
     */
    private static Map<String, String> buildAppiumEnvironment() {
        Map<String, String> env = new HashMap<>(System.getenv());

        // Current PATH from the JVM process
        String currentPath = env.getOrDefault("PATH", "");

        // Directories to append if not already present
        String[] extraDirs = {"/opt/homebrew/bin", "/usr/local/bin"};
        StringBuilder pathBuilder = new StringBuilder(currentPath);
        for (String dir : extraDirs) {
            if (!currentPath.contains(dir)) {
                if (pathBuilder.length() > 0) pathBuilder.append(":");
                pathBuilder.append(dir);
            }
        }

        String newPath = pathBuilder.toString();
        env.put("PATH", newPath);
        logger.info("Appium child process PATH: {}", newPath);

        // Also propagate FFMPEG_PATH if set by run.sh (belt-and-suspenders)
        String ffmpegPath = System.getenv("FFMPEG_PATH");
        if (ffmpegPath != null && !ffmpegPath.isEmpty()) {
            env.put("FFMPEG_PATH", ffmpegPath);
            logger.info("FFMPEG_PATH injected into Appium environment: {}", ffmpegPath);
        }

        return env;
    }

    public static void stopServer() {
        if (service != null && service.isRunning()) {
            try {
                service.stop();
                logger.info("Programmatically started Appium server stopped");
            } catch (Exception e) {
                logger.error("Error stopping Appium server: {}", e.getMessage());
            } finally {
                service = null; // Reset service reference
            }
        } else {
            logger.info("No programmatically started server to stop (external server may still be running)");
        }
    }

    private static boolean isAppiumServerRunning() {
        return isPortOccupied(port) && isAppiumHealthy();
    }

    private static boolean isPortOccupied(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.debug("Port {} is available", port);
            return false;
        } catch (IOException e) {
            logger.debug("Port {} is occupied", port);
            return true;
        }
    }

    private static boolean isAppiumHealthy() {
        try {
            // Test if it's actually an Appium server by hitting the status endpoint
            URL url = new URL(APPIUM_URL + "/status");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            logger.debug("Appium server health check response code: {}", responseCode);

            return responseCode == 200;
        } catch (Exception e) {
            logger.debug("Appium server health check failed: {}", e.getMessage());
            return false;
        }
    }

    public static String getServerUrl() {
        if (service != null && service.isRunning()) {
            return service.getUrl().toString();
        }
        // Return the expected URL for external server
        return APPIUM_URL + "/wd/hub";
    }

    public static boolean isManagingServer() {
        return service != null && service.isRunning();
    }

    // Enhanced method to handle both scenarios
    public static void ensureServerAvailable() {
        try {
            startOrConnectToServer();

            // Verify server is accessible
            if (!isAppiumHealthy()) {
                throw new RuntimeException("Appium server is not responding to health checks");
            }

            logger.info("Appium server is available at: {}", getServerUrl());
        } catch (Exception e) {
            logger.error("Failed to ensure Appium server availability", e);
            throw new RuntimeException("Failed to ensure Appium server availability", e);
        }
    }

    // Cleanup method for test teardown
    public static void cleanup() {
        try {
            // Only stop if we started it programmatically
            if (isManagingServer()) {
                stopServer();
            }

            // Clean up any lingering processes
            cleanupProcesses();
        } catch (Exception e) {
            logger.error("Error during cleanup", e);
        }
    }

    private static void cleanupProcesses() {
        try {
            // Kill any orphaned WebDriverAgent processes
            ProcessBuilder pb = new ProcessBuilder("pkill", "-f", "WebDriverAgent");
            Process process = pb.start();
            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
            }
        } catch (Exception e) {
            logger.debug("Could not cleanup WebDriverAgent processes: {}", e.getMessage());
        }
    }

    // Method to force restart server (useful for troubleshooting)
    public static void forceRestartServer() {
        logger.info("Force restarting Appium server...");

        // Stop our managed server if running
        stopServer();

        // Try to kill any external servers
        try {
            ProcessBuilder pb = new ProcessBuilder("pkill", "-f", "appium");
            Process process = pb.start();
            boolean finished = process.waitFor(3, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
            }

            // Wait a moment for processes to fully terminate
            Thread.sleep(2000);
        } catch (Exception e) {
            logger.warn("Could not kill external Appium processes: {}", e.getMessage());
        }

        // Start fresh server
        startNewServer();
    }

    // Method to get server status for debugging
    public static String getServerStatus() {
        if (service != null && service.isRunning()) {
            return "Managed server running on " + service.getUrl();
        } else if (isAppiumServerRunning()) {
            return "External server detected on " + APPIUM_URL;
        } else {
            return "No server detected";
        }
    }
}