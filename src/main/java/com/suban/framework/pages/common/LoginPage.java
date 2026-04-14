package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    public LoginPage(AppiumDriver driver) {
        super(driver);
    }

    // Header elements
    @FindBy(id = "com.subaru.oneapp.stage:id/ivBack")
    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc='btn back' or @content-desc='Navigate up'] | //android.widget.Button[@content-desc='btn back']")
    @iOSXCUITFindBy(accessibility = "btn back")
    private WebElement backButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/tvWelcomeTitle")
    @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='FR_NATIVE_SIGNIN_TITLELABEL' or contains(@text,'Sign In') or contains(@text,'Welcome')]")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_TITLELABEL")
    private WebElement welcomeTitle;

    // Top image (iOS only)
    @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='FR_NATIVE_SIGNIN_TOPIMAGE'] | //android.widget.ImageView[contains(@content-desc,'logo') or contains(@content-desc,'top')]")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_TOPIMAGE")
    private WebElement topImage;

    // Login form elements
    @FindBy(id = "com.subaru.oneapp.stage:id/etName")
    @AndroidFindBy(xpath = "//android.widget.EditText[@content-desc='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD' or @hint='Email or Username' or @hint='Email or phone number']")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD")
    private WebElement emailOrMobileField;

    @FindBy(id = "com.subaru.oneapp.stage:id/btContinue")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='FR_NATIVE_SIGNIN_CONTINUE_BUTTON' or @text='Continue' or @text='CONTINUE']")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_CONTINUE_BUTTON")
    private WebElement continueButton;

    // Social login elements (included as WebElements but without methods)
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='FR_NATIVE_SIGNIN_APPLEID_BUTTON' or contains(@text,'Apple') or contains(@content-desc,'Apple')]")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_APPLEID_BUTTON")
    private WebElement signInWithAppleButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/btGoogle")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='FR_NATIVE_SIGNIN_GOOGLE_BUTTON' or contains(@text,'Google') or contains(@content-desc,'Google')]")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_GOOGLE_BUTTON")
    private WebElement signInWithGoogleButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/btFacebook")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='FR_NATIVE_SIGNIN_FACEBOOK_BUTTON' or contains(@text,'Facebook') or contains(@content-desc,'Facebook')]")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_FACEBOOK_BUTTON")
    private WebElement signInWithFacebookButton;

    // Registration link
    @FindBy(id = "com.subaru.oneapp.stage:id/tvRegister")
    @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='REGISTER' or @text='Register' or @text='REGISTER' or @text='Create Account'] | //android.widget.Button[@content-desc='REGISTER']")
    @iOSXCUITFindBy(accessibility = "REGISTER")
    private WebElement registerLink;

    // ========== REGISTRATION ELEMENTS ==========
    @AndroidFindBy(id = "com.subaru.oneapp.stage:id/etPassword")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD")
    private WebElement passwordField;

    @AndroidFindBy(id = "com.subaru.oneapp.stage:id/ivActivation")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_ENTER_PASSWORD_HIDE_PASSWORD_BUTTON")
    private WebElement showHidePasswordToggle;

    @AndroidFindBy(id ="com.subaru.oneapp.stage:id/tvEnterPassword")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_ENTER_PASSWORD_TITLELABEL")
    private WebElement enterYourPasswordText;

    @AndroidFindBy(id = "com.subaru.oneapp.stage:id/btSignIn")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON")
    private WebElement signInButton;

    @AndroidFindBy(id = "com.subaru.oneapp.stage:id/tvResetHint")
    @iOSXCUITFindBy(accessibility = "RESET IT")
    private WebElement resetPasswordLink;



    // LOCATORS - MFA Screen
//    @FindBy(id = "com.subaru.oneapp.stage:id/tvAccessTitle")
//    @iOSXCUITFindBy(accessibility = "FR_NATIVE_OTP_TITLELABEL")
//    private WebElement mfaTitle;

    @FindBy(id = "com.subaru.oneapp.stage:id/tvDetail")
    @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='FR_NATIVE_OTP_TITLELABEL' or contains(@text,'verification') or contains(@text,'Verification') or contains(@text,'code sent')]")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_OTP_TITLELABEL")
    private WebElement mfaDescription;

//    @FindBy(id = "com.subaru.oneapp.stage:id/tvNameDetail")
//    private WebElement mfaEmailText;

//    @FindBy(id = "com.subaru.oneapp.stage:id/tvAccessTitle'")
//    private WebElement acessAccText;

    @FindBy(id = "com.subaru.oneapp.stage:id/etCode")
    @AndroidFindBy(xpath = "//android.widget.EditText[@content-desc='FR_NATIVE_OTP_INPUT_TEXTFIELD' or @hint='Verification Code' or @hint='Enter code' or @hint='Code' or contains(@hint,'digit')]")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_OTP_INPUT_TEXTFIELD")
    private WebElement activationCodeField;

    @FindBy(id = "com.subaru.oneapp.stage:id/mfaCheckbox")
    @AndroidFindBy(xpath = "//android.widget.CheckBox[contains(@text,'Remember') or contains(@content-desc,'REMEMBER')] | //android.widget.TextView[contains(@text,'REMEMBER DEVICE')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"REMEMBER DEVICE\"]")
    private WebElement rememberDeviceCheckbox;

    @FindBy(id = "com.subaru.oneapp.stage:id/btCodeSignIn")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON' or @text='VERIFY' or @text='Verify' or @text='Submit']")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON")
    private WebElement verifyAccountButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/tvReset")
    @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='REQUEST NEW CODE' or @text='REQUEST NEW CODE' or @text='Resend Code' or @text='Request New Code']")
    @iOSXCUITFindBy(accessibility = "REQUEST NEW CODE")
    private WebElement requestNewCodeLink;

    // ========== DEVICE VERIFICATION SCREEN ==========
    // Shown on first login from a new device: "VERIFICATION REQUIRED — SEND CODE"
    @iOSXCUITFindBy(accessibility = "SEND CODE")
    @AndroidFindBy(xpath = "//android.widget.Button[@text='SEND CODE']")
    private WebElement sendCodeButton;

    @iOSXCUITFindBy(accessibility = "VERIFY WITH EMAIL")
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='VERIFY WITH EMAIL']")
    private WebElement verifyWithEmailLink;

    // Biometric toggle switch (Android only — iOS does not show this screen)
    @AndroidFindBy(id = "com.subaru.oneapp.stage:id/switchEnable")
    @iOSXCUITFindBy(accessibility = "biometricToggle_notPresent_ios")
    private WebElement biometricToggleSwitch;

    // Save / Done button after biometric prompt
    // Android: the "complete_purchase" view; iOS: a "SAVE" or "DONE" button if shown
    @AndroidFindBy(id = "com.subaru.oneapp.stage:id/complete_purchase")
    @iOSXCUITFindBy(accessibility = "saveButton_notPresent_ios")
    private WebElement saveButton;

    // ========== SECURITY SETTINGS SCREEN ==========
    // "Enable your preferred security settings" — shown after OTP verification.
    // Has a "Keep Me Signed In" toggle and a Continue button at the bottom.
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Continue']")
    @iOSXCUITFindBy(accessibility = "Continue")
    private WebElement securitySettingsContinueButton;

    // Navigation
    public void clickBackButton() {
        clickWithLogging(backButton, "Back button");
    }

    // Form interactions
    public void enterEmailOrMobile(String text) {
        clickWithLogging(emailOrMobileField, "Email/Mobile field");
        emailOrMobileField.clear();
        emailOrMobileField.sendKeys(text);
        logger.info("Entered text in Email/Mobile field: {}", text);
    }

    public void enterPassword(String password) {
        clickWithLogging(passwordField, "Password field");
        passwordField.clear();
        passwordField.sendKeys(password);
        logger.info("Entered password");

    }

    public void togglePasswordVisibility() {
        clickWithLogging(showHidePasswordToggle, "Show/Hide password toggle");
    }

    public void clickSignIn() {
        clickWithLogging(signInButton, "Sign In button");
    }

    public void clickResetPassword() {
        clickWithLogging(resetPasswordLink, "Reset Password link");
    }

    public void clickContinue() {
        clickWithLogging(continueButton, "Continue button");
    }

    // Registration
    public void clickRegisterLink() {
        clickWithLogging(registerLink, "Register link");
    }

    // Verification methods
    public boolean isWelcomeTitleDisplayed() {
        return isElementDisplayed(welcomeTitle, "Welcome title");
    }

    public boolean isContinueButtonEnabled() {
        try {
            boolean enabled = continueButton.isEnabled();
            logger.info("Continue button enabled state: {}", enabled);
            return enabled;
        } catch (Exception e) {
            logger.error("Failed to check Continue button state", e);
            return false;
        }
    }

    public void enterActivationCode(String code) {
        clickWithLogging(activationCodeField, "Activation code field");
        activationCodeField.clear();
        activationCodeField.sendKeys(code);
        logger.info("Entered activation code");
    }

    public void toggleRememberDevice(boolean shouldCheck) {
        if (rememberDeviceCheckbox.isSelected() != shouldCheck) {
            clickWithLogging(rememberDeviceCheckbox, "Remember device checkbox");
        }
    }

    public void clickVerifyAccount() {
        clickWithLogging(verifyAccountButton, "Verify Account button");
    }

    public void clickRequestNewCode() {
        clickWithLogging(requestNewCodeLink, "Request new code link");
    }



    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordField, "Password field");
    }

    public boolean isSignInButtonEnabled() {
        try {
            boolean enabled = signInButton.isEnabled();
            logger.info("Sign In button enabled state: {}", enabled);
            return enabled;
        } catch (Exception e) {
            logger.error("Failed to check Sign In button state", e);
            return false;
        }
    }
//    public boolean isMfaScreenDisplayed() {
//        return isElementDisplayed(mfaTitle, "MFA screen title");
//    }
//
//    public String getMfaEmailText() {
//        return getTextWithLogging(mfaEmailText, "MFA email text");
//    }

    public boolean isVerifyAccountButtonEnabled() {
        try {
            boolean enabled = verifyAccountButton.isEnabled();
            logger.info("Verify Account button enabled state: {}", enabled);
            return enabled;
        } catch (Exception e) {
            logger.error("Failed to check Verify Account button state", e);
            return false;
        }
    }

    /**
     * Checks if the "VERIFICATION REQUIRED" device-verification screen is showing.
     * This appears on first login from a new simulator/device.
     */
    public boolean isDeviceVerificationScreenDisplayed() {
        try {
            return isElementDisplayed(sendCodeButton, "Send Code button");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Taps "SEND CODE" on the device verification screen to trigger an OTP via SMS.
     * Prefer tapVerifyWithEmail() instead — email OTP is reliably fetchable via API.
     */
    public void tapSendCode() {
        clickWithLogging(sendCodeButton, "Send Code button");
        logger.info("Tapped SEND CODE — OTP will be sent to registered phone number");
    }

    /**
     * Taps "VERIFY WITH EMAIL" on the device verification screen.
     * This triggers an OTP email to the account's registered email address.
     * The same OTP fetch API endpoint returns the emailed code.
     *
     * After tapping, the app may show a confirmation screen with a SEND CODE
     * button. If that intermediate screen appears, we tap it automatically.
     */
    public void tapVerifyWithEmail() {
        clickWithLogging(verifyWithEmailLink, "Verify With Email link");
        logger.info("Tapped VERIFY WITH EMAIL — OTP will be sent to registered email address");

        // The app sometimes shows an intermediate "We will send a code to your email"
        // confirmation screen with a SEND CODE button before the OTP entry screen.
        // Wait briefly and tap it if it appears.
        try {
            Thread.sleep(2000);
            if (isElementDisplayed(sendCodeButton, "Send Code confirmation button")) {
                clickWithLogging(sendCodeButton, "Send Code confirmation button");
                logger.info("Tapped SEND CODE on email confirmation screen");
            }
        } catch (Exception e) {
            // No intermediate confirmation screen — OTP entry screen came up directly
            logger.info("No intermediate confirmation screen — OTP entry screen appeared directly");
        }
    }

    /**
     * Returns true if the OTP entry screen is displayed after tapping
     * either SEND CODE or VERIFY WITH EMAIL.
     * Checks for the activation code input field.
     */
    public boolean isOtpEntryScreenDisplayed() {
        try {
            return isElementDisplayed(activationCodeField, "OTP entry field");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Full login flow: email → Continue → password → Sign In.
     *
     * Android: ForgeRock renders its login screens inside a WebView.
     * We switch to the WEBVIEW context before each interaction and
     * use CSS/HTML selectors instead of native Android locators.
     * After the full flow is complete we switch back to NATIVE_APP.
     *
     * iOS: XCUITest sees through WebViews transparently — no context
     * switching needed, existing locators continue to work.
     */
    public void performLogin(String emailOrMobile, String password) {
        if (isAndroid()) {
            performLoginAndroid(emailOrMobile, password);
        } else {
            performLoginIOS(emailOrMobile, password);
        }
    }

    /** iOS login — unchanged original flow */
    private void performLoginIOS(String emailOrMobile, String password) {
        enterEmailOrMobile(emailOrMobile);
        clickContinue();
        enterPassword(password);
        enterYourPasswordText.click();
        clickSignIn();
    }

    /**
     * Android login — ForgeRock screens live in a WebView.
     * Switch to WEBVIEW context, interact via CSS selectors, switch back.
     */
    private void performLoginAndroid(String emailOrMobile, String password) {
        logger.info("[LoginPage] Android login: switching to WebView context");
        boolean inWebView = switchToWebView(15);

        if (inWebView) {
            logger.info("[LoginPage] In WebView context: {}", currentContext());
            try {
                // ── Step 1: Enter email in WebView ──
                org.openqa.selenium.WebElement emailField = new org.openqa.selenium.support.ui.WebDriverWait(
                        driver, java.time.Duration.ofSeconds(10))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.cssSelector(
                            "input[type='email'], input[type='text'], input[name*='mail'], "
                            + "input[name*='user'], input[name*='login'], input[placeholder*='mail'], "
                            + "input[placeholder*='Email'], input[placeholder*='Username'], input")));
                emailField.clear();
                emailField.sendKeys(emailOrMobile);
                logger.info("[LoginPage] Android: email entered in WebView");

                // ── Step 2: Click Continue in WebView ──
                org.openqa.selenium.WebElement continueBtn = driver.findElement(
                    org.openqa.selenium.By.cssSelector(
                        "button[type='submit'], button[name*='continue'], "
                        + "button[name*='next'], input[type='submit'], "
                        + "button:not([disabled])"));
                continueBtn.click();
                logger.info("[LoginPage] Android: Continue clicked in WebView");
                Thread.sleep(2000);

                // ── Step 3: Enter password in WebView ──
                org.openqa.selenium.WebElement passwordField = new org.openqa.selenium.support.ui.WebDriverWait(
                        driver, java.time.Duration.ofSeconds(10))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.cssSelector(
                            "input[type='password'], input[name*='pass'], "
                            + "input[placeholder*='assword'], input[placeholder*='Password']")));
                passwordField.clear();
                passwordField.sendKeys(password);
                logger.info("[LoginPage] Android: password entered in WebView");

                // ── Step 4: Click Sign In in WebView ──
                org.openqa.selenium.WebElement signInBtn = driver.findElement(
                    org.openqa.selenium.By.cssSelector(
                        "button[type='submit'], input[type='submit'], "
                        + "button[name*='signin'], button[name*='login'], "
                        + "button:not([disabled])"));
                signInBtn.click();
                logger.info("[LoginPage] Android: Sign In clicked in WebView");

            } catch (Exception e) {
                logger.warn("[LoginPage] WebView CSS selectors failed — falling back to native: {}", e.getMessage());
                switchToNative();
                // Fall back to native flow
                enterEmailOrMobile(emailOrMobile);
                clickContinue();
                enterPassword(password);
                clickSignIn();
                return;
            } finally {
                switchToNative();
                logger.info("[LoginPage] Switched back to NATIVE_APP after login");
            }
        } else {
            // No WebView found — use native locators as fallback
            logger.warn("[LoginPage] No WebView context found — trying native locators");
            enterEmailOrMobile(emailOrMobile);
            clickContinue();
            enterPassword(password);
            clickSignIn();
        }
    }

    public void completeMfaVerification(String code) {
        //todo: fetch activation code
        enterActivationCode(code);
        mfaDescription.click();
        //toggleRememberDevice(rememberDevice);
        clickVerifyAccount();
    }

    // Method to disable biometric unlock
    public void disableBiometric() {
        // On iOS the biometric toggle screen is not shown after OTP verification
        // — the app moves directly to the home screen. Silently skip if element
        // is not found (covers both iOS and Android when screen is absent).
        try {
            wait.until(ExpectedConditions.visibilityOf(biometricToggleSwitch));
            if (biometricToggleSwitch.isDisplayed() && biometricToggleSwitch.isEnabled()) {
                // Check if the switch is already in the ON state (checked="true")
                String checked = biometricToggleSwitch.getAttribute("checked");
                if ("true".equals(checked)) {
                    biometricToggleSwitch.click();
                    logger.info("Biometric unlock disabled successfully");
                } else {
                    logger.info("Biometric unlock is already disabled");
                }
            }
        } catch (Exception e) {
            // Element not present (iOS / screen not shown) — safe to skip
            logger.info("Biometric toggle not visible — skipping (iOS or screen not shown)");
        }
    }

    // Method to enable biometric unlock
    public void enableBiometric() {
        try {
            if (biometricToggleSwitch.isDisplayed() && biometricToggleSwitch.isEnabled()) {
                // Check if the switch is in the OFF state (checked="false")
                String checked = biometricToggleSwitch.getAttribute("checked");
                if ("false".equals(checked)) {
                    biometricToggleSwitch.click();
                    logger.info("Biometric unlock enabled successfully");
                } else {
                    logger.info("Biometric unlock is already enabled");
                }
            }
        } catch (Exception e) {
            // Element absent on iOS — skip gracefully
            logger.info("Biometric toggle not present — skipping enableBiometric(): {}", e.getMessage());
        }
    }

    // Method to click the Save button
    public void clickSaveBiometricBtn() {
        // On iOS the biometric/save prompt may not appear at all after OTP —
        // the element locator above is a deliberate non-match placeholder.
        // Wrap entirely in try/catch so iOS runs continue uninterrupted.
        try {
            if (saveButton.isDisplayed() && saveButton.isEnabled()) {
                saveButton.click();
                logger.info("Save button clicked successfully");
            }
        } catch (Exception e) {
            // Element absent on iOS (no biometric save screen) — skip gracefully
            logger.info("Save button not present — skipping (iOS or screen not shown): {}", e.getMessage());
        }
    }

    // Combined method to disable biometric and save
    public void disableBiometricAndSave() {
        disableBiometric();
        clickSaveBiometricBtn();
    }

    // Combined method to enable biometric and save
    public void enableBiometricAndSave() {
        try {
            wait.until(ExpectedConditions.visibilityOf(biometricToggleSwitch));
            if (biometricToggleSwitch.isDisplayed() && biometricToggleSwitch.isEnabled()) {
                enableBiometric();
                clickSaveBiometricBtn();
            }
        } catch (Exception e) {
            logger.info("Biometric unlock is not visible");
        }
    }

    // ========== SECURITY SETTINGS SCREEN ==========

    /**
     * Returns true if the "Enable your preferred security settings" screen
     * is currently displayed (shown after OTP verification on iOS).
     */
    public boolean isSecuritySettingsScreenDisplayed() {
        try {
            return isElementDisplayed(securitySettingsContinueButton, "Security Settings Continue button");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Taps the Continue button on the "Enable your preferred security settings" screen.
     * Silently skips if not present (screen may not appear on every run).
     */
    public void tapSecuritySettingsContinue() {
        try {
            clickWithLogging(securitySettingsContinueButton, "Security Settings Continue button");
            logger.info("Tapped Continue on security settings screen");
        } catch (Exception e) {
            logger.info("Security settings Continue button not present — skipping: {}", e.getMessage());
        }
    }
}