package com.suban.framework.recording;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Base64;

/**
 * RecordingManager — centralises all video recording and failure screenshot logic.
 *
 * <h2>Default (always-on) behaviour — no {@code @record} flag:</h2>
 * <ul>
 *   <li><b>PASS</b> — video saved to {@code test-output/videos/<ScenarioName>.mp4};
 *       any old failure screenshot for this scenario is deleted.</li>
 *   <li><b>FAIL</b> — failure screenshot saved to {@code test-output/screenshots/<ScenarioName>.png};
 *       video saved to {@code test-output/videos/<ScenarioName>.mp4};
 *       old video for this scenario is replaced.</li>
 *   <li><b>Rerun</b> — always replaces the previous file (same filename).</li>
 * </ul>
 *
 * <h2>{@code @record} override — {@code ./run.sh <shortcut> @record}:</h2>
 * <p>When {@code RECORD_SCREEN=true} env var is set by {@code run.sh}, ADDITIONALLY saves
 * a timestamped copy to {@code test-output/recordings/<RECORD_TIMESTAMP>/} that is never
 * overwritten. Every {@code @record} run produces a permanent, dated archive.</p>
 *
 * <p>Nothing in {@code test-output/} is pushed to git (covered by existing {@code .gitignore} rule).</p>
 */
public class RecordingManager {

    private static final Logger logger = LogManager.getLogger(RecordingManager.class);

    // ── Output directories ────────────────────────────────────────────────────
    /** Always-on: latest recording per scenario (old one replaced on rerun). */
    private static final String VIDEO_DIR      = "test-output/videos";
    private static final String SCREENSHOT_DIR = "test-output/screenshots";
    /** @record mode: timestamped archives, never overwritten. */
    private static final String RECORDINGS_DIR = "test-output/recordings";

    // ── Recording config ──────────────────────────────────────────────────────
    /**
     * Max recording duration per scenario.
     * WDA supports up to 600 seconds (10 minutes). Set to 600 to cover even the
     * longest scenarios (resetpwd runs ~4 min). WDA auto-stops at this limit and
     * returns whatever was recorded so far — it does NOT crash or error.
     */
    private static final int MAX_RECORDING_SECONDS = 600;

    // ── @record mode — read once at class load time ───────────────────────────
    /**
     * {@code true} when {@code run.sh} was invoked with {@code @record}.
     * Set via the {@code RECORD_SCREEN} environment variable.
     */
    private static final boolean RECORD_MODE_ENABLED =
            "true".equalsIgnoreCase(System.getenv("RECORD_SCREEN"));

    /**
     * Timestamp string injected by {@code run.sh} (e.g. {@code "20260413_205500"}).
     * Used as the sub-folder name inside {@code test-output/recordings/}.
     */
    private static final String RECORD_SESSION_TIMESTAMP =
            System.getenv("RECORD_TIMESTAMP") != null
                    ? System.getenv("RECORD_TIMESTAMP")
                    : "session";

    // ── Per-scenario state ────────────────────────────────────────────────────
    /** Whether a recording was successfully started for the current scenario. */
    private boolean recordingStarted = false;

    // ─────────────────────────────────────────────────────────────────────────
    //  PUBLIC API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Starts screen recording on the given driver.
     * Call this in the {@code @Before} Cucumber hook, after the driver is ready.
     *
     * @param driver       active AppiumDriver
     * @param scenarioName raw Cucumber scenario name (used only for logging here)
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
                    .withVideoType("mp4")           // h264/mp4 — smallest size, good quality
                    .withVideoQuality(IOSStartScreenRecordingOptions.VideoQuality.MEDIUM)
                    .withFps(10);                   // 10 fps is sufficient for UI tests

            ((CanRecordScreen) driver).startRecordingScreen(options);
            recordingStarted = true;
            logger.info("[RecordingManager] Screen recording started for: {}", scenarioName);
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("ffmpeg") || msg.contains("screen capture process")) {
                logger.error("[RecordingManager] *** ffmpeg not found — video recording is disabled. ***");
                logger.error("[RecordingManager] Install it with: brew install ffmpeg");
                logger.error("[RecordingManager] Then re-run the test. No other changes needed.");
            } else {
                logger.warn("[RecordingManager] Failed to start screen recording for '{}': {}",
                        scenarioName, e.getMessage());
            }
            recordingStarted = false;
        }
    }

    /**
     * Stops the recording and saves files based on the scenario outcome.
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
            // Passed — remove any old failure screenshot for this scenario
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
                logger.warn("[RecordingManager] Recording returned empty — scenario '{}' likely exceeded " +
                    "the WDA recording time limit. No video saved for this scenario.", scenarioName);
                return;
            }

            byte[] videoBytes = Base64.getDecoder().decode(base64Video);
            String safeScenarioName = safeFileName(scenarioName);
            String filename = safeScenarioName + ".mp4";

            // ── Always-on: replace previous video for this scenario ───────────
            deleteOldFile(VIDEO_DIR, safeScenarioName, ".mp4");
            File latestVideo = saveFile(VIDEO_DIR, filename, videoBytes);
            if (latestVideo != null) {
                logger.info("[RecordingManager] Video saved ({}) → {}",
                        scenarioFailed ? "FAIL" : "PASS", latestVideo.getAbsolutePath());
            }

            // ── @record mode: also save a timestamped copy that is never deleted ─
            if (RECORD_MODE_ENABLED) {
                String recordDir = RECORDINGS_DIR + "/" + RECORD_SESSION_TIMESTAMP;
                // Prefix with PASS/FAIL so it's obvious when browsing the folder
                String archivedFilename = (scenarioFailed ? "FAIL_" : "PASS_") + filename;
                File archivedVideo = saveFile(recordDir, archivedFilename, videoBytes);
                if (archivedVideo != null) {
                    logger.info("[RecordingManager] @record archive saved → {}",
                            archivedVideo.getAbsolutePath());
                }
            }

        } catch (Exception e) {
            logger.error("[RecordingManager] Failed to stop/save recording for '{}': {}",
                    scenarioName, e.getMessage());
            recordingStarted = false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Captures a PNG screenshot and saves it to {@code test-output/screenshots/}.
     * Replaces any previous screenshot for the same scenario.
     */
    private void captureFailureScreenshot(AppiumDriver driver, String scenarioName) {
        if (driver == null) {
            logger.warn("[RecordingManager] Driver null — cannot capture screenshot for: {}", scenarioName);
            return;
        }
        try {
            String safeScenarioName = safeFileName(scenarioName);

            // Always replace the previous failure screenshot for this scenario
            deleteOldFile(SCREENSHOT_DIR, safeScenarioName, ".png");

            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String filename = safeScenarioName + ".png";
            File saved = saveFile(SCREENSHOT_DIR, filename, screenshotBytes);
            if (saved != null) {
                logger.info("[RecordingManager] Failure screenshot saved → {}", saved.getAbsolutePath());
            }

            // @record mode: also save a timestamped copy of the failure screenshot
            if (RECORD_MODE_ENABLED) {
                String recordDir = RECORDINGS_DIR + "/" + RECORD_SESSION_TIMESTAMP;
                File archivedShot = saveFile(recordDir, "FAIL_" + filename, screenshotBytes);
                if (archivedShot != null) {
                    logger.info("[RecordingManager] @record screenshot archive saved → {}",
                            archivedShot.getAbsolutePath());
                }
            }

        } catch (Exception e) {
            logger.error("[RecordingManager] Failed to capture failure screenshot for '{}': {}",
                    scenarioName, e.getMessage());
        }
    }

    /**
     * Deletes an existing file at {@code dir/baseName+ext} if it exists.
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
                    logger.warn("[RecordingManager] Could not delete: {}", target.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            logger.warn("[RecordingManager] Error deleting [{}/{}{}]: {}", dir, baseName, ext, e.getMessage());
        }
    }

    /**
     * Writes {@code data} to {@code dir/filename}, creating the directory tree if needed.
     *
     * @return the saved {@link File}, or {@code null} on error
     */
    private File saveFile(String dir, String filename, byte[] data) {
        try {
            File directory = new File(dir);
            if (!directory.exists() && !directory.mkdirs()) {
                logger.error("[RecordingManager] Could not create directory: {}", directory.getAbsolutePath());
                return null;
            }
            File file = new File(directory, filename);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data);
            }
            return file;
        } catch (Exception e) {
            logger.error("[RecordingManager] Failed to save [{}/{}]: {}", dir, filename, e.getMessage());
            return null;
        }
    }

    /**
     * Converts a raw Cucumber scenario name to a filesystem-safe string.
     * Non-alphanumeric characters become underscores; runs of underscores are
     * collapsed; leading/trailing underscores are trimmed; result is capped at
     * 120 characters to avoid OS path-length issues.
     *
     * <p>Example: {@code "OB_E2E_006 - Reset Password Flow"} → {@code "OB_E2E_006_Reset_Password_Flow"}
     */
    public static String safeFileName(String scenarioName) {
        if (scenarioName == null || scenarioName.isBlank()) return "unnamed_scenario";
        String safe = scenarioName
                .trim()
                .replaceAll("[^a-zA-Z0-9_\\-]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_+|_+$", "");
        return safe.length() > 120 ? safe.substring(0, 120) : safe;
    }
}
