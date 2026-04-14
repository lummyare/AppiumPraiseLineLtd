package com.suban.framework.recording;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.screenrecording.BaseStartScreenRecordingOptions;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;

/**
 * RecordingManager — centralises all video recording and failure screenshot logic.
 *
 * <p>Behaviour per scenario outcome:
 * <ul>
 *   <li><b>PASS</b>  — save MP4 video; delete any old screenshot for this scenario.</li>
 *   <li><b>FAIL</b>  — save PNG screenshot at point of failure; save MP4 video;
 *                       delete any previously stored video for this scenario
 *                       (replaced by the new failure recording).</li>
 *   <li><b>Rerun PASS after previous FAIL</b> — new video saved; old failure screenshot
 *                       deleted automatically by the PASS branch above.</li>
 * </ul>
 *
 * <p>Files are stored under {@code test-output/videos/} and {@code test-output/screenshots/}.
 * Both directories are gitignored via the existing {@code test-output/} rule in {@code .gitignore}.
 */
public class RecordingManager {

    private static final Logger logger = LogManager.getLogger(RecordingManager.class);

    // ── Output directories ────────────────────────────────────────────────────
    private static final String VIDEO_DIR       = "test-output/videos";
    private static final String SCREENSHOT_DIR  = "test-output/screenshots";

    // ── Recording config ──────────────────────────────────────────────────────
    /**
     * Maximum recording duration. Appium iOS caps at 3 minutes per segment;
     * using 10 min causes WDA to auto-stop and return what it has.
     * Keeping at 3 min is safe and covers all current scenarios comfortably.
     */
    private static final int MAX_RECORDING_SECONDS = 180;

    // ── State ─────────────────────────────────────────────────────────────────
    /** Whether a recording was successfully started for the current scenario. */
    private boolean recordingStarted = false;

    // ─────────────────────────────────────────────────────────────────────────
    //  PUBLIC API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Starts screen recording on the given driver.
     * Call this in the {@code @Before} Cucumber hook, after the driver is ready.
     *
     * @param driver  active AppiumDriver
     * @param scenarioName  raw Cucumber scenario name (used only for logging here)
     */
    public void startRecording(AppiumDriver driver, String scenarioName) {
        if (driver == null) {
            logger.warn("[RecordingManager] Driver is null — cannot start recording for: {}", scenarioName);
            return;
        }
        if (!(driver instanceof CanRecordScreen)) {
            logger.warn("[RecordingManager] Driver does not support screen recording for: {}", scenarioName);
            return;
        }
        try {
            IOSStartScreenRecordingOptions options = new IOSStartScreenRecordingOptions()
                    .withTimeLimit(Duration.ofSeconds(MAX_RECORDING_SECONDS))
                    // h264 + mp4 = smallest file size with good quality on iOS simulator
                    .withVideoType("mp4")
                    .withVideoQuality(IOSStartScreenRecordingOptions.VideoQuality.MEDIUM)
                    .withFps(10);   // 10 fps is enough for UI tests and keeps file size tiny

            ((CanRecordScreen) driver).startRecordingScreen(options);
            recordingStarted = true;
            logger.info("[RecordingManager] Screen recording started for: {}", scenarioName);
        } catch (Exception e) {
            logger.warn("[RecordingManager] Failed to start screen recording for '{}': {}", scenarioName, e.getMessage());
            recordingStarted = false;
        }
    }

    /**
     * Stops the recording and saves/discards based on the scenario outcome.
     *
     * <p>Call this in the {@code @After} Cucumber hook, before the driver is quit.
     *
     * @param driver         active AppiumDriver
     * @param scenarioName   raw Cucumber scenario name
     * @param scenarioFailed {@code true} if the scenario failed
     */
    public void stopAndSave(AppiumDriver driver, String scenarioName, boolean scenarioFailed) {
        // ── Screenshot on failure ─────────────────────────────────────────────
        if (scenarioFailed) {
            captureFailureScreenshot(driver, scenarioName);
        } else {
            // Test passed — clean up any old failure screenshot for this scenario
            deleteOldFile(SCREENSHOT_DIR, safeFileName(scenarioName), ".png");
        }

        // ── Video ─────────────────────────────────────────────────────────────
        if (!recordingStarted || driver == null || !(driver instanceof CanRecordScreen)) {
            logger.warn("[RecordingManager] No active recording to stop for: {}", scenarioName);
            recordingStarted = false;
            return;
        }

        try {
            String base64Video = ((CanRecordScreen) driver).stopRecordingScreen();
            recordingStarted = false;

            if (base64Video == null || base64Video.isBlank()) {
                logger.warn("[RecordingManager] Recording returned empty data for: {}", scenarioName);
                return;
            }

            // Delete previous video for this scenario (pass or fail — always replace)
            deleteOldFile(VIDEO_DIR, safeFileName(scenarioName), ".mp4");

            // Save new video
            String filename = safeFileName(scenarioName) + ".mp4";
            File videoFile = saveFile(VIDEO_DIR, filename, Base64.getDecoder().decode(base64Video));
            if (videoFile != null) {
                logger.info("[RecordingManager] Video saved ({}) → {}",
                        scenarioFailed ? "FAIL" : "PASS", videoFile.getAbsolutePath());
            }

        } catch (Exception e) {
            logger.error("[RecordingManager] Failed to stop/save recording for '{}': {}", scenarioName, e.getMessage());
            recordingStarted = false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    /** Captures a PNG screenshot and saves it to {@code test-output/screenshots/}. */
    private void captureFailureScreenshot(AppiumDriver driver, String scenarioName) {
        if (driver == null) {
            logger.warn("[RecordingManager] Driver null — cannot capture failure screenshot for: {}", scenarioName);
            return;
        }
        try {
            // Delete old screenshot for this scenario before saving new one
            deleteOldFile(SCREENSHOT_DIR, safeFileName(scenarioName), ".png");

            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String filename = safeFileName(scenarioName) + ".png";
            File saved = saveFile(SCREENSHOT_DIR, filename, screenshotBytes);
            if (saved != null) {
                logger.info("[RecordingManager] Failure screenshot saved → {}", saved.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("[RecordingManager] Failed to capture failure screenshot for '{}': {}", scenarioName, e.getMessage());
        }
    }

    /**
     * Deletes any existing file in {@code dir} whose base name equals {@code baseName}
     * and whose extension equals {@code ext} (case-insensitive).
     */
    private void deleteOldFile(String dir, String baseName, String ext) {
        try {
            File directory = new File(dir);
            if (!directory.exists()) return;
            File target = new File(directory, baseName + ext);
            if (target.exists()) {
                boolean deleted = target.delete();
                if (deleted) {
                    logger.info("[RecordingManager] Deleted old file: {}", target.getAbsolutePath());
                } else {
                    logger.warn("[RecordingManager] Could not delete old file: {}", target.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            logger.warn("[RecordingManager] Error deleting old file [{}/{}{}]: {}", dir, baseName, ext, e.getMessage());
        }
    }

    /**
     * Writes {@code data} to {@code dir/filename}, creating the directory if needed.
     *
     * @return the saved {@link File}, or {@code null} on error
     */
    private File saveFile(String dir, String filename, byte[] data) {
        try {
            File directory = new File(dir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    logger.error("[RecordingManager] Could not create directory: {}", directory.getAbsolutePath());
                    return null;
                }
            }
            File file = new File(directory, filename);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data);
            }
            return file;
        } catch (Exception e) {
            logger.error("[RecordingManager] Failed to save file [{}/{}]: {}", dir, filename, e.getMessage());
            return null;
        }
    }

    /**
     * Converts a raw Cucumber scenario name to a filesystem-safe string.
     * Replaces any character that is not alphanumeric, dash, or underscore with {@code _},
     * then trims leading/trailing underscores and collapses runs of underscores.
     * Truncated to 120 characters to avoid OS path-length limits.
     *
     * <p>Example: {@code "OB_E2E_006 - Reset Password Flow"} → {@code "OB_E2E_006_Reset_Password_Flow"}
     */
    public static String safeFileName(String scenarioName) {
        if (scenarioName == null || scenarioName.isBlank()) return "unnamed_scenario";
        String safe = scenarioName
                .trim()
                .replaceAll("[^a-zA-Z0-9_\\-]", "_")   // replace unsafe chars
                .replaceAll("_+", "_")                   // collapse multiple underscores
                .replaceAll("^_+|_+$", "");              // trim leading/trailing underscores
        return safe.length() > 120 ? safe.substring(0, 120) : safe;
    }
}
