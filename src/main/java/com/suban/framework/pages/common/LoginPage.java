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
    @iOSXCUITFindBy(accessibility = "btn back")
    private WebElement backButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/tvWelcomeTitle")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_TITLELABEL")
    private WebElement welcomeTitle;

    // Top image (iOS only)
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_TOPIMAGE")
    private WebElement topImage;

    // Login form elements
    @FindBy(id = "com.subaru.oneapp.stage:id/etName")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD")
    private WebElement emailOrMobileField;

    @FindBy(id = "com.subaru.oneapp.stage:id/btContinue")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_CONTINUE_BUTTON")
    private WebElement continueButton;

    // Social login elements (included as WebElements but without methods)
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_APPLEID_BUTTON")
    private WebElement signInWithAppleButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/btGoogle")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_GOOGLE_BUTTON")
    private WebElement signInWithGoogleButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/btFacebook")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_SIGNIN_FACEBOOK_BUTTON")
    private WebElement signInWithFacebookButton;

    // Registration link
    @FindBy(id = "com.subaru.oneapp.stage:id/tvRegister")
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
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_OTP_TITLELABEL")
    private WebElement mfaDescription;

//    @FindBy(id = "com.subaru.oneapp.stage:id/tvNameDetail")
//    private WebElement mfaEmailText;

//    @FindBy(id = "com.subaru.oneapp.stage:id/tvAccessTitle'")
//    private WebElement acessAccText;

    @FindBy(id = "com.subaru.oneapp.stage:id/etCode")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_OTP_INPUT_TEXTFIELD")
    private WebElement activationCodeField;

    @FindBy(id = "com.subaru.oneapp.stage:id/mfaCheckbox")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"REMEMBER DEVICE\"]")
    private WebElement rememberDeviceCheckbox;

    @FindBy(id = "com.subaru.oneapp.stage:id/btCodeSignIn")
    @iOSXCUITFindBy(accessibility = "FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON")
    private WebElement verifyAccountButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/tvReset")
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

    public void performLogin(String emailOrMobile, String password) {
        enterEmailOrMobile(emailOrMobile);
        clickContinue();
        enterPassword(password);
        enterYourPasswordText.click();
        clickSignIn();
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