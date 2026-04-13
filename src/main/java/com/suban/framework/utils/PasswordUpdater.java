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

        // Git: stage the updated src file and push so the new password persists in the repo
        if (srcFile.exists()) {
            gitCommitAndPush(srcFile, profileName, newPassword);
        }

        return newPassword;
    }

    /**
     * Stages the updated credentials file, commits with a descriptive message, and
     * pushes to origin/main. All git operations are run from the project root directory
     * (two levels above the target/test-classes directory, or derived from the src path).
     *
     * <p>Failures are logged as warnings — they are intentionally non-fatal so a git
     * connectivity issue never breaks the test run itself.
     *
     * @param srcFile     the credentials file that was just updated
     * @param profileName e.g. {@code "24MMEVDummy1"}
     * @param newPassword the password that was just stored
     */
    private static void gitCommitAndPush(File srcFile, String profileName, String newPassword) {
        // Derive project root: walk up from the src file until we find the directory
        // that contains a ".git" folder.
        File projectRoot = findGitRoot(srcFile);
        if (projectRoot == null) {
            logger.warn("[PasswordUpdater] Could not locate .git root from {} — skipping git push",
                srcFile.getAbsolutePath());
            return;
        }
        logger.info("[PasswordUpdater] Git root: {}", projectRoot.getAbsolutePath());

        // Relative path of the credentials file from the project root (for git add)
        String relativePath = projectRoot.toPath()
            .relativize(srcFile.toPath())
            .toString()
            .replace("\\", "/");  // normalise Windows separators

        String commitMessage = "[auto] Update " + profileName + " password after resetpwd run";

        try {
            runGitCommand(projectRoot, "git", "add", relativePath);
            logger.info("[PasswordUpdater] git add OK: {}", relativePath);

            runGitCommand(projectRoot, "git", "commit", "-m", commitMessage);
            logger.info("[PasswordUpdater] git commit OK");

            runGitCommand(projectRoot, "git", "push");
            logger.info("[PasswordUpdater] git push OK — new password for '{}' is now in the remote repo",
                profileName);
        } catch (Exception e) {
            logger.warn("[PasswordUpdater] git operation failed (non-fatal): {}", e.getMessage());
        }
    }

    /**
     * Walks up the directory tree from {@code startFile} until a directory
     * containing a {@code .git} folder is found.
     *
     * @return the project root directory, or {@code null} if not found
     */
    private static File findGitRoot(File startFile) {
        File dir = startFile.isDirectory() ? startFile : startFile.getParentFile();
        while (dir != null) {
            if (new File(dir, ".git").exists()) {
                return dir;
            }
            dir = dir.getParentFile();
        }
        return null;
    }

    /**
     * Runs a git command in the given working directory, waits for it to complete,
     * and throws a {@link RuntimeException} if the exit code is non-zero.
     *
     * @param workingDir the directory to run the command in
     * @param command    the command + args (e.g. {@code "git", "push"})
     */
    private static void runGitCommand(File workingDir, String... command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDir);
        pb.redirectErrorStream(true);   // merge stderr into stdout for logging
        Process process = pb.start();

        // Drain stdout so the process doesn't block on a full buffer
        String output = new String(process.getInputStream().readAllBytes());
        int exitCode = process.waitFor();

        if (!output.isBlank()) {
            logger.info("[PasswordUpdater] git output: {}", output.trim());
        }

        if (exitCode != 0) {
            throw new RuntimeException(
                "git command failed (exit " + exitCode + "): " + String.join(" ", command)
                + "\nOutput: " + output.trim());
        }
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
