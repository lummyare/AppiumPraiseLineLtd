package com.suban.cucumber.stepdefinitions;

import com.suban.cucumber.hooks.TestHooks;
import com.suban.framework.config.AccountProfileLoader;
import com.suban.framework.config.AccountProfileLoader.AccountProfile;
import com.suban.framework.config.ConfigReader;
import com.suban.framework.core.DriverManager;
import com.suban.framework.pages.common.HomePage;
import com.suban.framework.pages.common.LoginPage;
import com.suban.framework.pages.common.LoginSuccessPage;
import com.suban.framework.pages.common.WelcomePage;
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
    private WelcomePage welcomePage;
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
        welcomePage = new WelcomePage(testHooks.driver);
        // Give the app 3 seconds to finish launching before checking for alerts
        Thread.sleep(3000);

        if (DriverManager.isAndroid()) {
            logger.info("Android: handling launch popups (Verified Links + Security)");
            homePage.handleAndroidLaunchPopups();
        } else {
            homePage.dismissSystemAlerts();
        }

        logger.info("Successfully on home screen, alerts handled");
    }

    @And("I proceed to Login Process with profile {string}")
    public void iProceedToLoginProcessWithProfile(String profileName) throws Exception {
        logger.info("Initiating Login Process with profile: {}", profileName);

        AccountProfile profile = AccountProfileLoader.load(profileName);
        logger.info("Using account: {}", profile);

        // iOS launch can be flaky on the splash / welcome screen.
        // Reuse the stronger WelcomePage sign-in strategy with XPath + coordinate fallbacks.
        if (DriverManager.isIOS()) {
            logger.info("iOS detected — using WelcomePage.tapSignIn() for resilient Sign In entry");
            welcomePage.tapSignIn();
        } else {
            homePage.clickSignIn();
        }

        loginPage = new LoginPage(testHooks.driver);
        loginPage.performLogin(profile.getEmail(), profile.getPassword());

        Thread.sleep(3000);

        boolean needsOtp = false;
        if (loginPage.isDeviceVerificationScreenDisplayed()) {
            logger.info("Device verification screen detected — tapping VERIFY WITH EMAIL");
            loginPage.tapVerifyWithEmail();
            logger.info("Waiting 5s for server to register fresh email OTP...");
            Thread.sleep(5000);
            needsOtp = true;
        } else if (loginPage.isOtpEntryScreenDisplayed()) {
            logger.info("OTP entry screen detected directly after Sign In");
            needsOtp = true;
        } else {
            logger.info("No device verification / OTP screen detected — continuing to post-login flow");
        }

        if (needsOtp) {
            String otpCode = OTPCodeUtils.fetchOTP(profile.getEmail());
            logger.info("OTP fetched for '{}': {} — entering now", profile.getEmail(), otpCode);
            loginPage.completeMfaVerification(otpCode);
        }

        loginPage.disableBiometricAndSave();

        loginSuccessPage = new LoginSuccessPage(testHooks.driver);

        Thread.sleep(2000);
        if (loginPage.isSecuritySettingsScreenDisplayed()) {
            logger.info("Security settings screen detected — tapping Continue");
            loginPage.tapSecuritySettingsContinue();
            Thread.sleep(2000);
        } else {
            logger.info("No security settings screen — proceeding");
        }

        Thread.sleep(2000);
        if (loginSuccessPage.isSkipDisplayed()) {
            logger.info("Onboarding screen detected — tapping Skip");
            loginSuccessPage.clickSkipForNow();
            Thread.sleep(2000);
        } else {
            logger.info("No onboarding Skip screen — proceeding");
        }

        Thread.sleep(2000);
        if (loginSuccessPage.isFtueOkDisplayed()) {
            logger.info("FTUE OK screen detected — tapping OK");
            loginSuccessPage.clickFtueOk();
            Thread.sleep(2000);
        } else {
            logger.info("No FTUE OK screen — proceeding");
        }

        Thread.sleep(3000);
        logger.info("Checking for dashboard popups to dismiss");
        loginSuccessPage.dismissDashboardPopups();

        Thread.sleep(2000);
        if (loginSuccessPage.isUpdateMobileNumberModalDisplayed()) {
            logger.info("Update Mobile Number modal detected — closing it");
            loginSuccessPage.closeUpdateMobileNumberModal();
            Thread.sleep(1500);
            logger.info("Update Mobile Number modal closed");
        } else {
            logger.info("No Update Mobile Number modal detected — proceeding to dashboard assertion");
        }

        logger.info("Asserting Dashboard page for profile: {}", profileName);
        loginSuccessPage.assertDashboardByRemoteButtons();

        logger.info("Waiting 3 seconds before finishing the test");
        Thread.sleep(3000);

        waitForLoginSuccess();
    }

    private void waitForLoginSuccess() {
        try {
            int waitSeconds = ConfigReader.getIntProperty("explicit.wait");
            WebDriverWait loginWait = new WebDriverWait(testHooks.driver, Duration.ofSeconds(waitSeconds));
            loginWait.until(driver -> {
                String src = driver.getPageSource();
                return src != null && !src.contains("FR_NATIVE_OTP") && !src.contains("FR_NATIVE_ENTER_PASSWORD");
            });
            logger.info("Login completed — post-login screen detected");
        } catch (Exception e) {
            logger.warn("Could not confirm post-login screen within wait period: {}", e.getMessage());
        }
    }

    private String getBundleId() {
        if (ConfigReader.isIOS()) {
            return ConfigReader.getProperty("ios.bundle.id");
        } else {
            return ConfigReader.getAndroidPackageId();
        }
    }

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
                testHooks.driver.getPageSource();
            }
        }
    }
}
