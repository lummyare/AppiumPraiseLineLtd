package com.suban.cucumber.stepdefinitions;

import com.suban.cucumber.hooks.TestHooks;
import com.suban.framework.pages.common.UIExplorerPage;
import com.suban.framework.utils.UiElementExtractor;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Step definitions for the @uiexplorer scenario.
 *
 * Reuses:
 *  - "the mobile application is launched"  → LoginStepDefinitions
 *  - "I am on the home screen"             → LoginStepDefinitions
 *  - "I proceed to Login Process …"        → LoginStepDefinitions
 *
 * Only the exploration-specific steps are defined here.
 */
public class UIExplorerStepDefinitions {

    private static final Logger logger = LoggerFactory.getLogger(UIExplorerStepDefinitions.class);

    private final TestHooks testHooks;
    private UIExplorerPage explorerPage;

    // Resolved once in @And("I start the UI Explorer session")
    private String projectRoot;

    public UIExplorerStepDefinitions(TestHooks testHooks) {
        this.testHooks = testHooks;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Resolve the Maven project root from the classpath
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Resolves the Maven project root by walking up from the test-classes directory.
     *
     * Classpath resource path:  .../target/test-classes/config/config.properties
     * We walk up 3 levels      (test-classes → target → project root)
     * and verify we land on a directory containing pom.xml.
     */
    private static String resolveProjectRoot() {
        try {
            URL resource = UIExplorerStepDefinitions.class
                    .getClassLoader()
                    .getResource("config/config.properties");
            if (resource != null) {
                // file:/…/target/test-classes/config/config.properties
                File configFile = Paths.get(resource.toURI()).toFile();
                // up: config → test-classes → target → project-root
                File projectDir = configFile
                        .getParentFile()   // config/
                        .getParentFile()   // test-classes/
                        .getParentFile()   // target/
                        .getParentFile();  // project root
                if (new File(projectDir, "pom.xml").exists()) {
                    logger.info("[UIExplorer] Resolved project root: {}", projectDir.getAbsolutePath());
                    return projectDir.getAbsolutePath();
                }
            }
        } catch (Exception e) {
            logger.warn("[UIExplorer] Could not resolve project root via classpath: {}", e.getMessage());
        }

        // Fallback: use user.dir (works when Maven is run from the project root)
        String userDir = System.getProperty("user.dir", ".");
        logger.info("[UIExplorer] Using user.dir as project root: {}", userDir);
        return userDir;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Step definitions
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Initialises the output session directory and creates the UIExplorerPage.
     * Must run AFTER the login steps so the driver is ready and we are on the dashboard.
     */
    @And("I start the UI Explorer session")
    public void iStartTheUiExplorerSession() {
        logger.info("[UIExplorer] Starting UI Explorer session");
        Assert.assertNotNull(testHooks.driver,
                "Driver must be initialised before starting UI Explorer");

        projectRoot = resolveProjectRoot();
        explorerPage = new UIExplorerPage(testHooks.driver, projectRoot);

        String sessionDir = explorerPage.initOutputSession();
        logger.info("[UIExplorer] Session output directory: {}", sessionDir);
    }

    /**
     * Runs the full end-to-end screen exploration.
     * After each navigation, the current page source is captured and written to disk.
     */
    @And("I explore all app screens and dump their UI elements")
    public void iExploreAllAppScreensAndDumpTheirUiElements() throws InterruptedException {
        Assert.assertNotNull(explorerPage,
                "UI Explorer session must be started before exploring screens");

        logger.info("[UIExplorer] Beginning full-app screen exploration");
        explorerPage.exploreAllScreens();
        logger.info("[UIExplorer] Screen exploration finished. Visited: {}",
                explorerPage.getVisitedScreens());
    }

    /**
     * Asserts that the master report file was written and is non-empty.
     * Logs the path so the user can find it easily.
     */
    @Then("the UI element report is saved to the logs folder")
    public void theUiElementReportIsSavedToTheLogsFolder() {
        String sessionDir = UiElementExtractor.getSessionDir();
        Assert.assertNotNull(sessionDir, "Session directory must be initialised");

        File masterReport = new File(sessionDir, "MASTER_REPORT.txt");
        Assert.assertTrue(masterReport.exists(),
                "MASTER_REPORT.txt not found at: " + masterReport.getAbsolutePath());
        Assert.assertTrue(masterReport.length() > 0,
                "MASTER_REPORT.txt is empty — no elements were dumped");

        // Log the full path so it is clearly visible in the test output
        logger.info("[UIExplorer] ─────────────────────────────────────────────────────");
        logger.info("[UIExplorer]  EXPLORATION COMPLETE");
        logger.info("[UIExplorer]  Screens visited: {}", explorerPage.getVisitedScreens().size());
        logger.info("[UIExplorer]  Master report  : {}", masterReport.getAbsolutePath());
        logger.info("[UIExplorer]  Session folder : {}", sessionDir);
        logger.info("[UIExplorer]  Open the folder in Finder:");
        logger.info("[UIExplorer]    open \"{}\"", sessionDir);
        logger.info("[UIExplorer] ─────────────────────────────────────────────────────");

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║  UI EXPLORER — DONE                                          ║");
        System.out.printf ("║  Screens : %-51s ║%n", explorerPage.getVisitedScreens().size());
        System.out.printf ("║  Report  : %-51s ║%n",
                masterReport.getAbsolutePath().length() > 51
                        ? "…" + masterReport.getAbsolutePath().substring(
                                masterReport.getAbsolutePath().length() - 50)
                        : masterReport.getAbsolutePath());
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }
}
