package com.suban.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Random;

/**
 * PasswordUpdater — generates a new password and persists it back to the
 * credentials properties file so other scenarios automatically pick it up.
 *
 * <p>Password format: {@code Test@} followed by 4 random digits, e.g. {@code Test@7423}.
 * The prefix {@code Test@} is constant so the password always satisfies the app's
 * complexity rules (upper-case, lower-case, digit, special character).
 *
 * <p>Usage:
 * <pre>
 *   String newPwd = PasswordUpdater.generateAndStore("24MMEVDummy1");
 *   // newPwd == "Test@7423" (example)
 *   // credentials/24MMEVDummy1.properties now has login.password=Test@7423
 * </pre>
 *
 * <p>The file is written to the same location on disk so that the next run of
 * {@link com.suban.framework.config.AccountProfileLoader#load(String)} returns
 * the updated password without any manual intervention.
 */
public class PasswordUpdater {

    private static final Logger logger = LoggerFactory.getLogger(PasswordUpdater.class);
    private static final String PASSWORD_PREFIX = "Test@";
    private static final String CREDENTIALS_CLASSPATH_DIR = "credentials/";

    /**
     * Generates a new password ({@code Test@XXXX}) and writes it to the named
     * profile's properties file on disk.
     *
     * @param profileName profile name matching the file in
     *                    {@code src/test/resources/credentials/}, e.g. {@code "24MMEVDummy1"}
     * @return the newly generated password string
     * @throws RuntimeException if the properties file cannot be located or written
     */
    public static String generateAndStore(String profileName) {
        String newPassword = generatePassword();
        logger.info("[PasswordUpdater] Generated new password for profile '{}': {}", profileName, newPassword);

        // Locate the file on disk via the classpath URL — works in both IDE and Maven
        String classpathPath = CREDENTIALS_CLASSPATH_DIR + profileName + ".properties";
        URL resourceUrl = PasswordUpdater.class.getClassLoader().getResource(classpathPath);

        if (resourceUrl == null) {
            throw new RuntimeException(
                "[PasswordUpdater] Cannot find credentials file on classpath: " + classpathPath
                + " — ensure src/test/resources/credentials/" + profileName + ".properties exists");
        }

        File targetFile = new File(resourceUrl.getFile());
        logger.info("[PasswordUpdater] Writing updated password to target: {}", targetFile.getAbsolutePath());

        // Load the existing properties, update the password, write back
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(targetFile)) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("[PasswordUpdater] Failed to read properties file: " + targetFile, e);
        }

        props.setProperty("login.password", newPassword);
        String comment = "Account profile: " + profileName + " — password updated by PasswordUpdater during test run";

        // Write 1: target/test-classes — used by AccountProfileLoader within this Maven run
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            props.store(fos, comment);
        } catch (IOException e) {
            throw new RuntimeException("[PasswordUpdater] Failed to write target file: " + targetFile, e);
        }

        // Write 2: src/test/resources/credentials — survives mvn clean so the next run
        // picks up the last reset password instead of reverting to Test@123.
        // Path is derived from the target path by replacing the target segment with src.
        String targetPath = targetFile.getAbsolutePath();
        String srcPath = targetPath
            .replace("/target/test-classes/", "/src/test/resources/")
            .replace("\\target\\test-classes\\", "\\src\\test\\resources\\");
        File srcFile = new File(srcPath);
        if (srcFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream(srcFile)) {
                props.store(fos, comment);
                logger.info("[PasswordUpdater] Also written to src: {}", srcFile.getAbsolutePath());
            } catch (IOException e) {
                logger.warn("[PasswordUpdater] Could not write src file (non-fatal): {}", e.getMessage());
            }
        } else {
            logger.warn("[PasswordUpdater] src file not found — password will NOT persist across mvn clean: {}", srcPath);
        }

        logger.info("[PasswordUpdater] Successfully stored new password for profile '{}'", profileName);
        return newPassword;
    }

    /**
     * Generates a password: constant prefix {@code Test@} + 4 random digits.
     * Example outputs: {@code Test@3812}, {@code Test@0047}, {@code Test@9256}.
     */
    public static String generatePassword() {
        Random rng = new Random();
        // Zero-pad to always produce exactly 4 digits (e.g. 0047, 0123)
        int digits = rng.nextInt(10000);
        return PASSWORD_PREFIX + String.format("%04d", digits);
    }
}
