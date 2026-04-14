package com.suban.cucumber.stepdefinitions;

import com.suban.cucumber.hooks.TestHooks;
import com.suban.framework.config.AccountProfileLoader;
import com.suban.framework.config.AccountProfileLoader.AccountProfile;
import com.suban.framework.config.ConfigReader;
import com.suban.framework.core.DriverManager;
import com.suban.framework.pages.common.HomePage;
import com.suban.framework.pages.common.LoginPage;
import com.suban.framework.pages.common.LoginSuccessPage;
import com.suban.framework.utils.OTPCodeUtils;
import io.appium.java_client.InteractsWithApps;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Step definitions for Login functionality.
 */
public class LoginStepDefinitions {

    private static final Logger logger = LoggerFactory.getLogger(LoginStepDefinitions.class);

    private final TestHooks testHooks;
    private HomePage homePage;
    private LoginPage loginPage;
    private LoginSuccessPage loginSuccessPage;

    public LoginStepDefinitions(TestHooks testHooks) {
        this.testHooks = testHooks;
    }

    @Given("the mobile application is launched")
    public void the_mobile_application_is_launched() {
        logger.info("Mobile application is launched");

        if (testHooks.driver == null) {
            logger.warn("Driver not initialized in TestHooks, attempting to get current driver");
            testHooks.driver = DriverManager.getCurrentDriver();
        }

        Assert.assertNotNull(testHooks.driver, "Driver should be initialized before this step");
    }

    @And("I am on the home screen")
    public void i_am_on_the_home_screen() throws InterruptedException {
        logger.info("Navigating to home screen");
        homePage = new HomePage(testHooks.driver);
        // Give the app 3 seconds to finish launching before checking for alerts
        Thread.sleep(3000);

        if (DriverManager.isAndroid()) {
            // Android ONLY: click "Don't ask me again" on Verified Links dialog,
            // then click OK on the Security popup. Both happen before Sign In.
            // This must NOT run on iOS.
            logger.info("Android: handling launch popups (Verified Links + Security)");
            homePage.handleAndroidLaunchPopups();
        } else {
            // iOS ONLY: dismiss any native system permission dialogs
            // (notifications, location, etc.) using WebDriver alert API.
            homePage.dismissSystemAlerts();
        }

        logger.info("Successfully on home screen, alerts handled");
    }

    /**
     * Login step that accepts a named credentials profile.
     * Profile files live in src/test/resources/credentials/<profileName>.properties.
     *
     * Available profiles:
     *   - 21mmEVDummy1  → sub2_21mm@mail.tmnact.io / Test@123
     *   - 24MMEVDummy1  → subarustg02_21mm@mail.tmnact.io / Test@123
     */
    @And("I proceed to Login Process with profile {string}")
    public void iProceedToLoginProcessWithProfile(String profileName) throws Exception {
        logger.info("Initiating Login Process with profile: {}", profileName);

        // ── Load credentials from the named profile ──
        AccountProfile profile = AccountProfileLoader.load(profileName);
        logger.info("Using account: {}", profile);

        homePage.clickSignIn();

        loginPage = new LoginPage(testHooks.driver);
        loginPage.performLogin(profile.getEmail(), profile.getPassword());

        // Wait briefly for whatever screen appears after Sign In
        Thread.sleep(3000);

        // Handle "VERIFICATION REQUIRED" device-verification screen.
        // Tap "VERIFY WITH EMAIL" so the OTP goes to the email address,
        // which is reliably fetchable via the Toyota OTP API.
        if (loginPage.isDeviceVerificationScreenDisplayed()) {
            logger.info("Device verification screen detected — tapping VERIFY WITH EMAIL");
            loginPage.tapVerifyWithEmail();
            // Wait 5 seconds for the server to register the fresh email OTP.
            logger.info("Waiting 5s for server to register fresh email OTP...");
            Thread.sleep(5000);
        } else {
            logger.info("No device verification screen — proceeding directly to OTP entry");
            Thread.sleep(2000);
        }

        // Fetch OTP for the exact email used to sign in.
        // The email is passed explicitly so the API body always targets the correct account.
        String otpCode = OTPCodeUtils.fetchOTP(profile.getEmail());
        logger.info("OTP fetched for '{}': {} — entering now", profile.getEmail(), otpCode);
        loginPage.completeMfaVerification(otpCode);

        // disableBiometricAndSave() is a no-op on iOS (gracefully skipped).
        loginPage.disableBiometricAndSave();

        loginSuccessPage = new LoginSuccessPage(testHooks.driver);

        // ── Step 1: "Enable your preferred security settings" screen ──
        Thread.sleep(2000);
        if (loginPage.isSecuritySettingsScreenDisplayed()) {
            logger.info("Security settings screen detected — tapping Continue");
            loginPage.tapSecuritySettingsContinue();
            Thread.sleep(2000);
        } else {
            logger.info("No security settings screen — proceeding");
        }

        // ── Step 2: Onboarding / FTUE screen 1 — tap Skip ──
        Thread.sleep(2000);
        if (loginSuccessPage.isSkipDisplayed()) {
            logger.info("Onboarding screen detected — tapping Skip");
            loginSuccessPage.clickSkipForNow();
            Thread.sleep(2000);
        } else {
            logger.info("No onboarding Skip screen — proceeding");
        }

        // ── Step 3: FTUE screen 2 — tap OK ──
        Thread.sleep(2000);
        if (loginSuccessPage.isFtueOkDisplayed()) {
            logger.info("FTUE OK screen detected — tapping OK");
            loginSuccessPage.clickFtueOk();
            Thread.sleep(2000);
        } else {
            logger.info("No FTUE OK screen — proceeding");
        }

        // ── Step 4: Dashboard — dismiss any popup/overlay ──
        Thread.sleep(3000);
        logger.info("Checking for dashboard popups to dismiss");
        loginSuccessPage.dismissDashboardPopups();

        // ── Step 5: "Update Mobile Number" modal ──
        // May appear on top of the dashboard after all onboarding screens are done.
        // Close it via the X button (SheetWithCloseButton_sheetCloseButton image).
        Thread.sleep(2000);
        if (loginSuccessPage.isUpdateMobileNumberModalDisplayed()) {
            logger.info("Update Mobile Number modal detected — closing it");
            loginSuccessPage.closeUpdateMobileNumberModal();
            Thread.sleep(1500);
            logger.info("Update Mobile Number modal closed");
        } else {
            logger.info("No Update Mobile Number modal detected — proceeding to dashboard assertion");
        }

        // ── Dashboard assertion ──
        // Verify the app is on the Dashboard by checking Remote, Status, and Health
        // quick-action buttons (confirmed visible from page source dump).
        // Falls back to nav-bar tab check if those aren’t found.
        // Throws AssertionError only if absolutely nothing dashboard-related is visible.
        logger.info("Asserting Dashboard page for profile: {}", profileName);
        loginSuccessPage.assertDashboardByRemoteButtons();

        // ── Final wait ──
        logger.info("Waiting 3 seconds before finishing the test");
        Thread.sleep(3000);

        waitForLoginSuccess();
    }

    /**
     * Waits for the post-login screen to appear using an explicit wait
     * rather than a fixed sleep.
     */
    private void waitForLoginSuccess() {
        try {
            int waitSeconds = ConfigReader.getIntProperty("explicit.wait");
            WebDriverWait loginWait = new WebDriverWait(testHooks.driver, Duration.ofSeconds(waitSeconds));
            // Wait until the page source changes from the login/MFA screen
            // — a lightweight check that the app has moved forward
            loginWait.until(driver -> {
                String src = driver.getPageSource();
                return src != null && !src.contains("FR_NATIVE_OTP") && !src.contains("FR_NATIVE_ENTER_PASSWORD");
            });
            logger.info("Login completed — post-login screen detected");
        } catch (Exception e) {
            logger.warn("Could not confirm post-login screen within wait period: {}", e.getMessage());
        }
    }

    /**
     * Returns the correct bundle/package ID for the app under test.
     * Reads from config rather than hard-coding.
     */
    private String getBundleId() {
        if (ConfigReader.isIOS()) {
            return ConfigReader.getProperty("ios.bundle.id");
        } else {
            return ConfigReader.getAndroidPackageId();
        }
    }

    /**
     * Refresh app to clean state — used optionally between scenarios.
     */
    private void refreshAppToCleanState() throws InterruptedException {
        try {
            logger.info("Attempting to terminate and relaunch app");
            String bundleId = getBundleId();
            ((InteractsWithApps) testHooks.driver).terminateApp(bundleId);
            Thread.sleep(1000);
            ((InteractsWithApps) testHooks.driver).activateApp(bundleId);
            Thread.sleep(2000);
            logger.info("App terminate and relaunch completed");
        } catch (Exception e1) {
            logger.warn("Terminate/relaunch failed: {}", e1.getMessage());
            try {
                testHooks.driver.navigate().back();
                Thread.sleep(1000);
                logger.info("Navigation-based refresh completed");
            } catch (Exception e2) {
                logger.warn("Navigation refresh also failed: {}", e2.getMessage());
                testHooks.driver.getPageSource(); // driver responsiveness check
            }
        }
    }
}
