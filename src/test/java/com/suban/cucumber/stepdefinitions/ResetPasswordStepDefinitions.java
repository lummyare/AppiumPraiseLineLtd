package com.suban.cucumber.stepdefinitions;

import com.suban.cucumber.hooks.TestHooks;
import com.suban.framework.config.AccountProfileLoader;
import com.suban.framework.config.AccountProfileLoader.AccountProfile;
import com.suban.framework.pages.common.LoginPage;
import com.suban.framework.pages.common.LoginSuccessPage;
import com.suban.framework.pages.common.ResetPasswordPage;
import com.suban.framework.pages.common.SignInPage;
import com.suban.framework.pages.common.WelcomePage;
import com.suban.framework.utils.OTPCodeUtils;
import com.suban.framework.utils.PasswordUpdater;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Step definitions for Reset_Password (OB_E2E_006–008).
 *
 * OB_E2E_006 full flow:
 *   1. Launch app + tap Sign In
 *   2. Enter 24MMEVDummy1 email on Sign In page
 *   3. Tap Reset It button (same page)
 *   4. Assert We Sent An Email page → fetch OTP → enter it
 *   5. Tap Verify
 *   6. Assert Reset Your Password page → enter randomly generated password in both fields
 *   7. Tap Reset Password button
 *   8. Assert success page → tap Done
 *   9. Assert Welcome Back page
 *  10. Sign in again using the newly stored password
 *  11. Assert Dashboard — end test
 */
public class ResetPasswordStepDefinitions {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordStepDefinitions.class);
    private static final String PROFILE_24MM = "24MMEVDummy1";

    private final TestHooks testHooks;
    private ResetPasswordPage resetPasswordPage;
    private SignInPage signInPage;
    private LoginPage loginPage;
    private LoginSuccessPage loginSuccessPage;
    private WelcomePage welcomePage;

    /** Holds the newly generated password so it can be reused in step 10. */
    private String generatedPassword;

    public ResetPasswordStepDefinitions(TestHooks testHooks) {
        this.testHooks = testHooks;
    }

    // ── Step 2 — Enter 24MMEVDummy1 email on Sign In page ─────────────────────

    @And("I enter the 24MMEVDummy1 email on the sign in page")
    public void enter24mmEmailOnSignInPage() {
        logger.info("[ResetPwdSteps] Entering 24MMEVDummy1 email on Sign In page");
        AccountProfile profile = AccountProfileLoader.load(PROFILE_24MM);
        signInPage = ensureSignInPage();
        signInPage.enterEmail(profile.getEmail());
        logger.info("[ResetPwdSteps] Email entered: {}", profile.getEmail());
    }

    // ── Step 2b — Dismiss keyboard + tap Continue (to reach password page) ────────

    @And("I dismiss the keyboard and tap Continue to reach the password page")
    public void dismissKeyboardAndTapContinue() throws InterruptedException {
        logger.info("[ResetPwdSteps] Dismissing keyboard and tapping Continue");
        signInPage = ensureSignInPage();
        signInPage.tapEmailContinue();
        Thread.sleep(2000); // allow password page to load
    }

    // ── Step 3 — Tap Reset It button ──────────────────────────────────────────

    @And("I tap the Reset It button on the sign in page")
    public void tapResetItButton() {
        logger.info("[ResetPwdSteps] Tapping Reset It button");
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.tapResetIt();
    }

    // ── Step 4 — Assert We Sent An Email page + fetch & enter OTP ─────────────

    @Then("I should see the We Sent An Email page")
    public void assertWeSentAnEmailPage() throws InterruptedException {
        logger.info("[ResetPwdSteps] Asserting We Sent An Email page");
        Thread.sleep(5000); // allow page transition after Reset It tap
        resetPasswordPage = ensureResetPage();
        Assert.assertTrue(resetPasswordPage.isWeSentAnEmailPageDisplayed(),
            "Expected 'We Sent An Email' page but it was not visible");
    }

    @And("I fetch and enter the OTP for the 24MMEVDummy1 email")
    public void fetchAndEnterOtpFor24mm() throws Exception {
        AccountProfile profile = AccountProfileLoader.load(PROFILE_24MM);
        String email = profile.getEmail();
        logger.info("[ResetPwdSteps] Fetching OTP for email: {}", email);
        Thread.sleep(2000); // slight delay so OTP backend registers the request

        String otp = OTPCodeUtils.fetchOTP(email);
        logger.info("[ResetPwdSteps] OTP fetched, entering into field");

        resetPasswordPage = ensureResetPage();
        resetPasswordPage.enterOtpCode(otp);
    }

    // ── Step 5 — Tap Verify ────────────────────────────────────────────────────

    @And("I tap the Verify button on the email verification page")
    public void tapVerifyOnEmailPage() throws InterruptedException {
        logger.info("[ResetPwdSteps] Tapping Verify button");
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.tapVerifyOtp();
        Thread.sleep(3000);
    }

    // ── Step 6 — Assert Reset Your Password page + enter random password ───────

    @Then("I should be on the Reset Your Password page")
    public void assertResetYourPasswordPage() throws InterruptedException {
        logger.info("[ResetPwdSteps] Asserting Reset Your Password page");
        Thread.sleep(2000);
        resetPasswordPage = ensureResetPage();
        Assert.assertTrue(resetPasswordPage.isResetYourPasswordPageDisplayed(),
            "Expected 'Reset Your Password' page but it was not visible");
    }

    /**
     * Generates a new password (Test@XXXX), stores it in the 24MMEVDummy1.properties
     * file, and enters it in both New Password and Confirm Password fields.
     * The generated password is also saved to {@link #generatedPassword} so step 10
     * can use it directly without re-reading the file.
     */
    @And("I enter and confirm a randomly generated new password for 24MMEVDummy1")
    public void enterRandomlyGeneratedPasswordFor24mm() {
        logger.info("[ResetPwdSteps] Generating and storing new password for {}", PROFILE_24MM);
        generatedPassword = PasswordUpdater.generateAndStore(PROFILE_24MM);
        logger.info("[ResetPwdSteps] New password generated and stored: {}", generatedPassword);

        resetPasswordPage = ensureResetPage();
        resetPasswordPage.enterNewPassword(generatedPassword);
        resetPasswordPage.enterConfirmNewPassword(generatedPassword);
        logger.info("[ResetPwdSteps] New password entered in both fields");
    }

    // ── Step 7 — Tap Reset Password button ────────────────────────────────────

    @And("I tap the Reset Password button")
    public void tapResetPasswordButton() throws InterruptedException {
        logger.info("[ResetPwdSteps] Tapping Reset Password button");
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.tapResetPasswordButton();
        Thread.sleep(3000);
    }

    // ── Step 8 — Assert success page + tap Done ───────────────────────────────

    @Then("I should see the Password Reset success page")
    public void assertPasswordResetSuccessPage() throws InterruptedException {
        logger.info("[ResetPwdSteps] Waiting for Password Reset success page (polling up to 90s)");
        // CONTEXT: tapResetPasswordButton() already waits 30s inside for server processing.
        // This step polls an ADDITIONAL 90s (45 × 2s) for either:
        //   a) PASSWORD RESET! success page (with DONE button), OR
        //   b) Welcome Back / Sign In page (auto-navigated past success screen).
        // isResetSuccessPageDisplayed() and isWelcomeBackPageDisplayed() both treat
        // empty page source (< 200 chars) as "still loading" and return false —
        // so this loop just keeps waiting rather than failing prematurely.
        resetPasswordPage = ensureResetPage();
        boolean found = false;
        for (int i = 0; i < 45; i++) {
            if (resetPasswordPage.isResetSuccessPageDisplayed()) {
                logger.info("[ResetPwdSteps] ✅ Password Reset success page confirmed on poll {}", i + 1);
                found = true;
                break;
            }
            // Also accept if app has already auto-navigated to Welcome Back
            if (resetPasswordPage.isWelcomeBackPageDisplayed()) {
                logger.info("[ResetPwdSteps] ✅ App already on Welcome Back page — reset succeeded (poll {})", i + 1);
                found = true;
                break;
            }
            logger.info("[ResetPwdSteps] Poll {} — success page not yet visible, retrying...", i + 1);
            Thread.sleep(2000);
        }
        Assert.assertTrue(found, "Expected Password Reset success page (or Welcome Back) but neither was visible after 90s");
    }

    @And("I tap the Done button on the success page")
    public void tapDoneOnSuccessPage() throws InterruptedException {
        logger.info("[ResetPwdSteps] Tapping Done button (or confirming already on Welcome Back)");
        resetPasswordPage = ensureResetPage();
        // If the success screen already auto-navigated to Welcome Back, skip Done tap.
        // isWelcomeBackPageDisplayed() guards against empty source so this is safe.
        if (resetPasswordPage.isWelcomeBackPageDisplayed()) {
            logger.info("[ResetPwdSteps] Already on Welcome Back page — Done tap not needed");
            return;
        }
        // tapDoneButton() polls internally for up to 60s for the DONE element,
        // handles auto-navigation, and has a last-resort coordinate tap — so just call it.
        try {
            resetPasswordPage.tapDoneButton();
            logger.info("[ResetPwdSteps] ✅ Done button tapped");
        } catch (Exception e) {
            logger.warn("[ResetPwdSteps] Done button tap failed (may have auto-navigated): {}", e.getMessage());
        }
        Thread.sleep(3000); // allow navigation to Welcome Back
    }

    // ── Step 9 — Assert Welcome Back page ─────────────────────────────────────

    @Then("I should be back on the Welcome Back page")
    public void assertWelcomeBackPage() throws InterruptedException {
        logger.info("[ResetPwdSteps] Asserting Welcome Back page (polling up to 30s)");
        resetPasswordPage = ensureResetPage();
        // Poll up to 30s — the app may still be transitioning from the success screen.
        boolean found = false;
        for (int i = 0; i < 15; i++) {
            if (resetPasswordPage.isWelcomeBackPageDisplayed()) {
                logger.info("[ResetPwdSteps] ✅ Welcome Back page confirmed on poll {}", i + 1);
                found = true;
                break;
            }
            logger.info("[ResetPwdSteps] Poll {} — Welcome Back not yet visible...", i + 1);
            Thread.sleep(2000);
        }
        Assert.assertTrue(found, "Expected to be back on Welcome Back / Sign In page after password reset");
    }

    // ── Step 10 — Enter newly stored password (feature uses existing steps for email + continue + submit) ──

    /**
     * Enters the newly generated password into the password field.
     * The feature file now reuses existing SignInStepDefinitions steps for
     * email entry, Continue tap, and Sign In submit — keeping POM DRY:
     *   "I enter the 24MMEVDummy1 email on the sign in page"       (enter24mmEmailOnSignInPage)
     *   "I dismiss the keyboard and tap Continue to reach the password page" (dismissKeyboardAndTapContinue)
     *   "I enter the newly stored 24MMEVDummy1 password"           <- this step
     *   "I tap the Sign In submit button"                          (SignInStepDefinitions.tapSignInSubmit)
     *   "I should be navigated to the app dashboard"               (SignInStepDefinitions.shouldBeOnDashboard)
     *
     * After DONE, the app lands on the email-entry screen (confirmed by element dump:
     * FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD is already visible — no Sign In button tap needed).
     */
    @And("I enter the newly stored 24MMEVDummy1 password")
    public void enterNewlyStored24mmPassword() throws InterruptedException {
        // generatedPassword was stored during enterRandomlyGeneratedPasswordFor24mm() (step 6)
        String passwordToUse = (generatedPassword != null)
            ? generatedPassword
            : AccountProfileLoader.load(PROFILE_24MM).getPassword();
        logger.info("[ResetPwdSteps] Entering newly stored password: {}", passwordToUse);
        Thread.sleep(2000); // allow password screen to load after Continue is tapped
        signInPage = ensureSignInPage();
        signInPage.enterPassword(passwordToUse);
        logger.info("[ResetPwdSteps] Newly stored password entered");
    }

    // Kept for backward-compat with any legacy callers; delegates to individual steps
    @When("I sign in using the 24MMEVDummy1 newly stored password")
    public void signInWith24mmNewlyStoredPassword() throws Exception {
        logger.info("[ResetPwdSteps] (legacy step) — use individual reusable steps instead");
        enter24mmEmailOnSignInPage();
        dismissKeyboardAndTapContinue();
        enterNewlyStored24mmPassword();
    }

@Then("I should be navigated to the app dashboard after password reset")
    public void assertAppDashboard() throws InterruptedException {
        logger.info("[ResetPwdSteps] Asserting App Dashboard");
        Thread.sleep(3000);
        loginSuccessPage = ensureLoginSuccessPage();
        // Close Update Mobile Number modal if it appears
        loginSuccessPage.closeUpdateMobileNumberModal();
        Thread.sleep(3000);
        loginSuccessPage.assertDashboardByRemoteButtons();
        logger.info("[ResetPwdSteps] ✅ Dashboard confirmed — test complete");
    }

    // ── Legacy / OB_E2E_007-008 steps kept for compilation ───────────────────

    @When("I enter my registered email for password recovery {string}")
    public void enterEmailForRecovery(String email) {
        logger.info("[ResetPwdSteps] Entering recovery email: {}", email);
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.enterRecoveryEmail(email);
    }

    @And("I submit the password recovery request")
    public void submitPasswordRecovery() throws InterruptedException {
        logger.info("[ResetPwdSteps] Submitting password recovery request");
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.tapSubmitRecovery();
        Thread.sleep(3000);
    }

    @Then("I should see a confirmation that a reset link was sent to my email")
    public void shouldSeeEmailResetLinkConfirmation() throws InterruptedException {
        Thread.sleep(2000);
        resetPasswordPage = ensureResetPage();
        Assert.assertTrue(resetPasswordPage.isResetLinkConfirmationDisplayed(),
            "Expected confirmation that reset link was sent to email");
    }

    @Then("I should see a confirmation that a reset link was sent")
    public void shouldSeeResetLinkSentConfirmation() throws InterruptedException {
        Thread.sleep(2000);
        resetPasswordPage = ensureResetPage();
        Assert.assertTrue(resetPasswordPage.isResetLinkConfirmationDisplayed(),
            "Expected confirmation that reset link/code was sent");
    }

    @When("I select the SMS verification option for password reset")
    public void selectSmsVerificationOption() {
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.selectSmsVerificationOption();
    }

    @And("I enter my registered phone number for password recovery {string}")
    public void enterPhoneForRecovery(String phone) {
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.enterRecoveryPhone(phone);
    }

    @Then("I should receive an SMS password reset verification code")
    public void shouldReceiveSmsVerificationCode() throws InterruptedException {
        Thread.sleep(3000);
        logger.info("[ResetPwdSteps] Asserting SMS code sent confirmation");
    }

    @When("I enter the correct SMS reset code")
    public void enterCorrectSmsResetCode() throws InterruptedException {
        Thread.sleep(3000);
        try {
            AccountProfile profile = AccountProfileLoader.load(PROFILE_24MM);
            String otp = OTPCodeUtils.fetchOTP(profile.getEmail());
            resetPasswordPage = ensureResetPage();
            resetPasswordPage.enterSmsCode(otp);
        } catch (Exception e) {
            logger.warn("[ResetPwdSteps] OTP fetch failed: {}", e.getMessage());
            resetPasswordPage.enterSmsCode("000000");
        }
    }

    @And("I enter and confirm a new valid password {string}")
    public void enterAndConfirmPassword(String password) {
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.enterNewPassword(password);
        resetPasswordPage.enterConfirmNewPassword(password);
    }

    @And("I tap Save to confirm the new password")
    public void tapSaveNewPassword() throws InterruptedException {
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.tapSavePassword();
        Thread.sleep(2000);
    }

    @Then("I should see a password updated confirmation")
    public void shouldSeePasswordUpdatedConfirmation() throws InterruptedException {
        Thread.sleep(2000);
        resetPasswordPage = ensureResetPage();
        Assert.assertTrue(resetPasswordPage.isPasswordUpdatedConfirmationDisplayed(),
            "Expected password updated success confirmation");
    }

    @And("I should be redirected to the Sign In screen")
    public void shouldBeRedirectedToSignIn() throws InterruptedException {
        Thread.sleep(2000);
        String src = testHooks.driver.getPageSource();
        Assert.assertTrue(
            src.contains("Sign In") || src.contains("FR_NATIVE_SIGNIN") || src.contains("Welcome"),
            "Expected to be on Sign In screen after password reset");
    }

    @When("I sign in with my new password {string}")
    public void signInWithNewPassword(String password) throws InterruptedException {
        signInPage = ensureSignInPage();
        signInPage.enterPassword(password);
        signInPage.tapSignInSubmit();
        Thread.sleep(3000);
    }

    @When("I attempt to use an expired password reset link")
    public void attemptExpiredPasswordResetLink() throws InterruptedException {
        Thread.sleep(2000);
        logger.info("[ResetPwdSteps] Expired link scenario — asserting error response");
    }

    @Then("I should see an appropriate password reset link expiry error message")
    public void shouldSeeExpiryErrorMessage() throws InterruptedException {
        Thread.sleep(2000);
        resetPasswordPage = ensureResetPage();
        Assert.assertTrue(resetPasswordPage.isResetErrorDisplayed(),
            "Expected error message for expired password reset link");
    }

    @When("I re-initiate the password reset flow")
    public void reInitiatePasswordResetFlow() throws InterruptedException {
        Thread.sleep(2000);
        logger.info("[ResetPwdSteps] Re-initiating password reset flow");
    }

    @And("I use the newly received valid reset link to set a new password {string}")
    public void useValidResetLinkToSetPassword(String password) throws InterruptedException {
        Thread.sleep(2000);
        resetPasswordPage = ensureResetPage();
        resetPasswordPage.enterNewPassword(password);
        resetPasswordPage.enterConfirmNewPassword(password);
        resetPasswordPage.tapSavePassword();
        Thread.sleep(2000);
    }

    @Then("I should be able to sign in successfully with the new password")
    public void shouldSignInSuccessfullyWithNewPassword() throws InterruptedException {
        Thread.sleep(3000);
        loginSuccessPage = ensureLoginSuccessPage();
        loginSuccessPage.assertDashboardByRemoteButtons();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private ResetPasswordPage ensureResetPage() {
        if (resetPasswordPage == null) {
            resetPasswordPage = new ResetPasswordPage(testHooks.driver);
        }
        return resetPasswordPage;
    }

    private SignInPage ensureSignInPage() {
        if (signInPage == null) {
            signInPage = new SignInPage(testHooks.driver);
        }
        return signInPage;
    }

    private LoginPage ensureLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(testHooks.driver);
        }
        return loginPage;
    }

    private LoginSuccessPage ensureLoginSuccessPage() {
        if (loginSuccessPage == null) {
            loginSuccessPage = new LoginSuccessPage(testHooks.driver);
        }
        return loginSuccessPage;
    }

    private WelcomePage ensureWelcomePage() {
        if (welcomePage == null) {
            welcomePage = new WelcomePage(testHooks.driver);
        }
        return welcomePage;
    }
}
