package com.suban.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_PATH = "config/config.properties";
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    static {
        loadConfig();
    }

    private static void loadConfig() {
        properties = new Properties();
        // Load from classpath (works regardless of working directory or IDE)
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            if (input == null) {
                throw new RuntimeException("Config file not found on classpath: " + CONFIG_PATH);
            }
            properties.load(input);
            logger.info("Loaded config file from classpath: {}", CONFIG_PATH);
        } catch (IOException e) {
            logger.error("Failed to load config file: {}", CONFIG_PATH, e);
            throw new RuntimeException("Failed to load config file: " + CONFIG_PATH, e);
        }
    }

    public static String getProperty(String key) {
        // Allow system property / environment variable overrides (useful for CI)
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null && !envValue.isEmpty()) {
            logger.debug("Property '{}' overridden by environment variable", key);
            return envValue;
        }

        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isEmpty()) {
            logger.debug("Property '{}' overridden by system property", key);
            return sysProp;
        }

        String value = properties.getProperty(key);
        if (value == null) {
            logger.error("Property '{}' not found in config file", key);
            throw new RuntimeException("Property '" + key + "' not found in config file");
        }
        logger.debug("Property loaded: {}={}", key, value);
        return value.trim();
    }

    public static int getIntProperty(String key) {
        int value = Integer.parseInt(getProperty(key));
        logger.debug("Integer property loaded: {}={}", key, value);
        return value;
    }

    public static boolean getBooleanProperty(String key) {
        boolean value = Boolean.parseBoolean(getProperty(key));
        logger.debug("Boolean property loaded: {}={}", key, value);
        return value;
    }

    /**
     * Gets the Android package ID based on current app configuration
     */
    public static String getAndroidPackageId() {
        String currentApp = getProperty("current.app");
        String packageKey = "app." + currentApp + ".android.package";
        String packageId = getProperty(packageKey);
        logger.debug("Android package ID for app '{}': {}", currentApp, packageId);
        return packageId;
    }

    public static String getPlatform() {
        return getProperty("platform");
    }

    public static boolean isAndroid() {
        return "android".equalsIgnoreCase(getPlatform());
    }

    public static boolean isIOS() {
        return "ios".equalsIgnoreCase(getPlatform());
    }
}
