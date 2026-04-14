package com.suban.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads named account credentials from {@code credentials/<profileName>.properties}
 * on the test classpath.
 *
 * <p>Each credentials file must contain at minimum:
 * <pre>
 *   login.email=...
 *   login.password=...
 * </pre>
 *
 * <p>Available profiles (files in src/test/resources/credentials/):
 * <ul>
 *   <li><b>21mmEVDummy1</b> — sub2_21mm@mail.tmnact.io / Test@123</li>
 *   <li><b>24MMEVDummy1</b> — subarustg02_21mm@mail.tmnact.io / Test@123</li>
 * </ul>
 *
 * <p>Usage in step definitions:
 * <pre>
 *   AccountProfile profile = AccountProfileLoader.load("21mmEVDummy1");
 *   String email    = profile.getEmail();
 *   String password = profile.getPassword();
 * </pre>
 */
public class AccountProfileLoader {

    private static final Logger logger = LoggerFactory.getLogger(AccountProfileLoader.class);
    private static final String CREDENTIALS_DIR = "credentials/";

    /**
     * Loads the named credentials profile from the classpath.
     *
     * @param profileName profile file name without extension,
     *                    e.g. {@code "21mmEVDummy1"} or {@code "24MMEVDummy1"}
     * @return an {@link AccountProfile} with email and password populated
     * @throws RuntimeException if the file is not found or cannot be parsed
     */
    public static AccountProfile load(String profileName) {
        String path = CREDENTIALS_DIR + profileName + ".properties";
        Properties props = new Properties();

        // Strategy 1: read directly from src/test/resources on disk.
        // This always reflects the latest password written by PasswordUpdater
        // without requiring a recompile / Maven resource copy.
        // The src path is derived from the classpath URL of the compiled copy.
        boolean loadedFromSrc = false;
        try {
            java.net.URL classpathUrl = AccountProfileLoader.class.getClassLoader().getResource(path);
            if (classpathUrl != null) {
                String srcPath = classpathUrl.getFile()
                    .replace("/target/test-classes/", "/src/test/resources/")
                    .replace("\\target\\test-classes\\", "\\src\\test\\resources\\");
                java.io.File srcFile = new java.io.File(srcPath);
                if (srcFile.exists()) {
                    try (java.io.FileInputStream fis = new java.io.FileInputStream(srcFile)) {
                        props.load(fis);
                        logger.info("Loaded credentials profile '{}' from src: {}", profileName, srcFile.getAbsolutePath());
                        loadedFromSrc = true;
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Could not load from src path — falling back to classpath: {}", e.getMessage());
        }

        // Strategy 2: classpath fallback (target/test-classes) — used if src not found
        if (!loadedFromSrc) {
            try (InputStream is = AccountProfileLoader.class.getClassLoader().getResourceAsStream(path)) {
                if (is == null) {
                    throw new RuntimeException(
                        "Credentials profile not found on classpath: " + path
                        + "  — available files should be under src/test/resources/credentials/");
                }
                props.load(is);
                logger.info("Loaded credentials profile '{}' from classpath", profileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load credentials profile: " + path, e);
            }
        }

        String email = props.getProperty("login.email");
        String password = props.getProperty("login.password");

        if (email == null || email.isBlank()) {
            throw new RuntimeException("login.email is missing in profile: " + profileName);
        }
        if (password == null || password.isBlank()) {
            throw new RuntimeException("login.password is missing in profile: " + profileName);
        }

        logger.info("Profile '{}' — email: {}", profileName, email);
        return new AccountProfile(profileName, email.trim(), password.trim());
    }

    /**
     * Immutable value object holding credentials for one account profile.
     */
    public static class AccountProfile {

        private final String profileName;
        private final String email;
        private final String password;

        AccountProfile(String profileName, String email, String password) {
            this.profileName = profileName;
            this.email       = email;
            this.password    = password;
        }

        public String getProfileName() { return profileName; }
        public String getEmail()       { return email; }
        public String getPassword()    { return password; }

        @Override
        public String toString() {
            return "AccountProfile{name='" + profileName + "', email='" + email + "'}";
        }
    }
}
