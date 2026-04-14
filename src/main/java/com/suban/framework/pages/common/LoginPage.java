package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

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
    // Shown on first login from a new device: "VERIFICATION REQUIRED"
    // Android element dump confirmed:
    //   Button[4] text='Send Code' resource-id='com.subaru.oneapp.stage:id/btContinue'
    @iOSXCUITFindBy(accessibility = "SEND CODE")
    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id='com.subaru.oneapp.stage:id/btContinue' and (@text='Send Code' or @text='SEND CODE' or @text='Send code')]" +
        " | //android.widget.Button[@text='Send Code' or @text='SEND CODE' or @text='Send code']")
    private WebElement sendCodeButton;

    // Android element dump confirmed:
    //   TextView[12] text='Verify with email' resource-id='com.subaru.oneapp.stage:id/tvVerifyWithEmail'
    @iOSXCUITFindBy(accessibility = "VERIFY WITH EMAIL")
    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.subaru.oneapp.stage:id/tvVerifyWithEmail']" +
        " | //android.widget.TextView[@text='Verify with email' or @text='VERIFY WITH EMAIL' or @text='Verify With Email']")
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
        // Android: check for 'Verify with email' TextView (resource-id='tvVerifyWithEmail')
        // which is always present on the Verification Required screen.
        // Fallback: also check for Send Code button (text='Send Code').
        if (isAndroid()) {
            try {
                List<org.openqa.selenium.WebElement> verifyEmail = driver.findElements(
                    org.openqa.selenium.By.xpath(
                        "//android.widget.TextView[@resource-id='com.subaru.oneapp.stage:id/tvVerifyWithEmail']" +
                        " | //android.widget.TextView[@text='Verify with email' or @text='VERIFY WITH EMAIL']"));
                if (!verifyEmail.isEmpty() && verifyEmail.get(0).isDisplayed()) {
                    logger.info("[LoginPage] Android: Verification Required screen detected via tvVerifyWithEmail");
                    return true;
                }
            } catch (Exception ignored) { }
            // Fallback: Send Code button
            try {
                List<org.openqa.selenium.WebElement> sendCode = driver.findElements(
                    org.openqa.selenium.By.xpath(
                        "//android.widget.Button[@text='Send Code' or @text='SEND CODE']"));
                if (!sendCode.isEmpty() && sendCode.get(0).isDisplayed()) {
                    logger.info("[LoginPage] Android: Verification Required screen detected via Send Code button");
                    return true;
                }
            } catch (Exception ignored) { }
            // Also check page source for 'Verification Required' title
            try {
                String src = driver.getPageSource();
                if (src.contains("Verification Required") || src.contains("tvVerifyWithEmail")) {
                    logger.info("[LoginPage] Android: Verification Required screen detected via page source");
                    return true;
                }
            } catch (Exception ignored) { }
            return false;
        }
        // iOS path
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
        // Android: click the 'Verify with email' TextView directly by resource-id
        // (annotation locator may not resolve in time via PageFactory on Android).
        if (isAndroid()) {
            String[] verifyEmailXPaths = {
                "//android.widget.TextView[@resource-id='com.subaru.oneapp.stage:id/tvVerifyWithEmail']",
                "//android.widget.TextView[@text='Verify with email']",
                "//android.widget.TextView[@text='VERIFY WITH EMAIL']",
                "//android.widget.TextView[@text='Verify With Email']",
            };
            boolean clicked = false;
            for (String xpath : verifyEmailXPaths) {
                try {
                    List<org.openqa.selenium.WebElement> els = driver.findElements(
                        org.openqa.selenium.By.xpath(xpath));
                    if (!els.isEmpty() && els.get(0).isDisplayed()) {
                        els.get(0).click();
                        logger.info("[LoginPage] Android: tapped 'Verify with email' via: {}", xpath);
                        clicked = true;
                        break;
                    }
                } catch (Exception ignored) { }
            }
            if (!clicked) {
                // Fall back to annotation
                clickWithLogging(verifyWithEmailLink, "Verify With Email link (annotation)");
            }
        } else {
            clickWithLogging(verifyWithEmailLink, "Verify With Email link");
        }
        logger.info("Tapped VERIFY WITH EMAIL — OTP will be sent to registered email address");

        // After tapping 'Verify with email', the app shows the 'Send Code' button
        // (confirmed resource-id='btContinue' text='Send Code') before the OTP entry screen.
        // Tap it automatically so the OTP is triggered.
        try {
            Thread.sleep(2000);
            // Android: use direct XPath for Send Code (resource-id btContinue, text 'Send Code')
            if (isAndroid()) {
                String[] sendCodeXPaths = {
                    "//android.widget.Button[@resource-id='com.subaru.oneapp.stage:id/btContinue' and @text='Send Code']",
                    "//android.widget.Button[@text='Send Code']",
                    "//android.widget.Button[@text='SEND CODE']",
                };
                boolean sendClicked = false;
                for (String xpath : sendCodeXPaths) {
                    try {
                        List<org.openqa.selenium.WebElement> els = driver.findElements(
                            org.openqa.selenium.By.xpath(xpath));
                        if (!els.isEmpty() && els.get(0).isDisplayed()) {
                            els.get(0).click();
                            logger.info("[LoginPage] Android: tapped Send Code button via: {}", xpath);
                            sendClicked = true;
                            break;
                        }
                    } catch (Exception ignored) { }
                }
                if (!sendClicked) {
                    logger.info("[LoginPage] Android: Send Code button not found after Verify with email tap — OTP entry may have appeared directly");
                }
            } else {
                // iOS path
                if (isElementDisplayed(sendCodeButton, "Send Code confirmation button")) {
                    clickWithLogging(sendCodeButton, "Send Code confirmation button");
                    logger.info("Tapped SEND CODE on email confirmation screen");
                }
            }
        } catch (Exception e) {
            logger.info("No intermediate Send Code screen — OTP entry screen appeared directly: {}", e.getMessage());
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
     *
     * Flow:
     *   1. Switch to WEBVIEW context
     *   2. Enter email → click Continue (page 1)
     *   3. Wait for password page to load → enter password → click Sign In (page 2)
     *   4. Switch back to NATIVE_APP
     *
     * CSS selectors are used because the ForgeRock HTML form is not visible
     * to native Android locators (EditText / Button).
     */
    private void performLoginAndroid(String emailOrMobile, String password) {
        logger.info("[LoginPage] Android login: switching to WebView context");
        boolean inWebView = switchToWebView(15);

        if (!inWebView) {
            logger.warn("[LoginPage] No WebView context found — trying native locators as fallback");
            enterEmailOrMobile(emailOrMobile);
            clickContinue();
            enterPassword(password);
            clickSignIn();
            return;
        }

        logger.info("[LoginPage] In WebView context: {}", currentContext());
        try {
            // ── PAGE 1: Email entry ────────────────────────────────────────────────

            // Wait for email input to be ready
            org.openqa.selenium.WebElement emailField =
                webViewWait(10).until(
                    org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                        findEmailInput()));
            emailField.clear();
            emailField.sendKeys(emailOrMobile);
            logger.info("[LoginPage] Android WebView: email entered — '{}'", emailOrMobile);

            // Click Continue — must be the submit button on THIS page (email page)
            // FR_NATIVE_SIGNIN_CONTINUE_BUTTON is the known name; fall back to
            // the only visible submit button if that name isn't found.
            clickWebViewContinueButton();
            logger.info("[LoginPage] Android WebView: Continue clicked — waiting for password page");

            // ── PAGE 2: Password entry ──────────────────────────────────────────────

            // Wait up to 10s for the password field to appear (new page load)
            org.openqa.selenium.WebElement passwordField =
                webViewWait(10).until(
                    org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.cssSelector(
                            "input[type='password']")));
            passwordField.clear();
            passwordField.sendKeys(password);
            logger.info("[LoginPage] Android WebView: password entered");

            // Click Sign In — must be the submit button on THIS page (password page)
            clickWebViewSignInButton();
            logger.info("[LoginPage] Android WebView: Sign In clicked");

        } catch (Exception e) {
            logger.error("[LoginPage] WebView login failed: {}", e.getMessage(), e);
            throw new RuntimeException("[LoginPage] performLoginAndroid failed: " + e.getMessage(), e);
        } finally {
            switchToNative();
            logger.info("[LoginPage] Switched back to NATIVE_APP after WebView login");
        }
    }

    /**
     * Returns a By locator for the email input field using multiple CSS selector
     * strategies, tried in priority order via a compound selector.
     * ForgeRock typically names the field 'IDToken1' or uses type='text'.
     */
    private org.openqa.selenium.By findEmailInput() {
        // ForgeRock uses name='IDToken1' for the username/email field on page 1
        // Fall back through common input types in order of specificity
        return org.openqa.selenium.By.cssSelector(
            "input[name='IDToken1'], "
            + "input[name='username'], "
            + "input[name='email'], "
            + "input[type='email'], "
            + "input[type='text']:not([name*='pass'])");
    }

    /**
     * Clicks the Continue button on the email page (WebView context).
     * ForgeRock uses name='IDToken1' submit or a button with value/text 'Next'/'Continue'.
     * Tries specific selectors first, falls back to the first enabled submit button.
     */
    private void clickWebViewContinueButton() throws Exception {
        org.openqa.selenium.support.ui.WebDriverWait w = webViewWait(8);

        // Strategy 1: ForgeRock standard submit button name
        String[] continueSelectors = {
            "input[type='submit'][value='Next']",
            "input[type='submit'][value='Continue']",
            "input[type='submit'][value='CONTINUE']",
            "button[name='FR_NATIVE_SIGNIN_CONTINUE_BUTTON']",
            "button[type='submit'][name*='continue']",
            "button[type='submit'][name*='next']",
            "button[type='submit'][name*='login']",
            // Last resort: any enabled submit on this page
            "input[type='submit']:not([disabled])",
            "button[type='submit']:not([disabled])",
        };

        for (String sel : continueSelectors) {
            try {
                java.util.List<org.openqa.selenium.WebElement> els =
                    driver.findElements(org.openqa.selenium.By.cssSelector(sel));
                if (!els.isEmpty()) {
                    String val = els.get(0).getAttribute("value");
                    String txt = els.get(0).getText();
                    String nm  = els.get(0).getAttribute("name");
                    logger.info("[LoginPage] Continue button found via '{}' — value='{}' text='{}' name='{}'",
                        sel, val, txt, nm);
                    els.get(0).click();
                    return;
                }
            } catch (Exception ex) {
                logger.debug("[LoginPage] Continue selector '{}' failed: {}", sel, ex.getMessage());
            }
        }

        // Dump all buttons so we can see what's actually on the page
        dumpWebViewButtons();
        throw new RuntimeException(
            "[LoginPage] clickWebViewContinueButton: no Continue/submit button found on email page. "
            + "Check dumpWebViewButtons() output above.");
    }

    /**
     * Clicks the Sign In submit button on the password page (WebView context).
     * Separate from clickWebViewContinueButton to avoid cross-page confusion.
     */
    private void clickWebViewSignInButton() throws Exception {
        String[] signInSelectors = {
            "input[type='submit'][value='Sign In']",
            "input[type='submit'][value='Login']",
            "input[type='submit'][value='SIGN IN']",
            "button[name='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']",
            "button[type='submit'][name*='signin']",
            "button[type='submit'][name*='login']",
            // Last resort: any enabled submit
            "input[type='submit']:not([disabled])",
            "button[type='submit']:not([disabled])",
        };

        for (String sel : signInSelectors) {
            try {
                java.util.List<org.openqa.selenium.WebElement> els =
                    driver.findElements(org.openqa.selenium.By.cssSelector(sel));
                if (!els.isEmpty()) {
                    String val = els.get(0).getAttribute("value");
                    String txt = els.get(0).getText();
                    String nm  = els.get(0).getAttribute("name");
                    logger.info("[LoginPage] Sign In button found via '{}' — value='{}' text='{}' name='{}'",
                        sel, val, txt, nm);
                    els.get(0).click();
                    return;
                }
            } catch (Exception ex) {
                logger.debug("[LoginPage] Sign In selector '{}' failed: {}", sel, ex.getMessage());
            }
        }

        dumpWebViewButtons();
        throw new RuntimeException(
            "[LoginPage] clickWebViewSignInButton: no Sign In/submit button found on password page. "
            + "Check dumpWebViewButtons() output above.");
    }

    /** Explicit wait scoped to the current WebView context. */
    private org.openqa.selenium.support.ui.WebDriverWait webViewWait(int seconds) {
        return new org.openqa.selenium.support.ui.WebDriverWait(
            driver, java.time.Duration.ofSeconds(seconds));
    }

    /** Dumps all button/input[submit] elements visible in the WebView to the log. */
    private void dumpWebViewButtons() {
        try {
            java.util.List<org.openqa.selenium.WebElement> btns =
                driver.findElements(org.openqa.selenium.By.cssSelector(
                    "button, input[type='submit'], input[type='button']"));
            logger.warn("[LoginPage] WebView button dump ({} elements):", btns.size());
            for (int i = 0; i < btns.size(); i++) {
                org.openqa.selenium.WebElement b = btns.get(i);
                logger.warn("  [{}] tag={} type='{}' name='{}' value='{}' text='{}' disabled='{}'",
                    i,
                    b.getTagName(),
                    safeAttr(b, "type"),
                    safeAttr(b, "name"),
                    safeAttr(b, "value"),
                    b.getText(),
                    safeAttr(b, "disabled"));
            }
        } catch (Exception e) {
            logger.warn("[LoginPage] dumpWebViewButtons failed: {}", e.getMessage());
        }
    }

    private String safeAttr(org.openqa.selenium.WebElement el, String attr) {
        try {
            String v = el.getAttribute(attr);
            return v != null ? v : "";
        } catch (Exception e) { return ""; }
    }

    public void completeMfaVerification(String code) {
        logger.info("[LoginPage] completeMfaVerification: entering OTP code");

        // Wait for OTP entry screen to be fully loaded after Send Code tap
        try { Thread.sleep(3000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }

        if (isAndroid()) {
            // Android: use direct resource-id XPaths confirmed from app inspection
            // OTP field: resource-id='com.subaru.oneapp.stage:id/etCode'
            // Verify button: resource-id='com.subaru.oneapp.stage:id/btCodeSignIn'
            String[] otpFieldXPaths = {
                "//android.widget.EditText[@resource-id='com.subaru.oneapp.stage:id/etCode']",
                "//android.widget.EditText[@content-desc='FR_NATIVE_OTP_INPUT_TEXTFIELD']",
                "//android.widget.EditText[@hint='Verification Code' or @hint='Enter code' or @hint='Code']",
                "(//android.widget.EditText)[1]",
            };
            org.openqa.selenium.WebElement otpField = null;
            for (String xpath : otpFieldXPaths) {
                try {
                    List<org.openqa.selenium.WebElement> els = driver.findElements(
                        org.openqa.selenium.By.xpath(xpath));
                    if (!els.isEmpty() && els.get(0).isDisplayed()) {
                        otpField = els.get(0);
                        logger.info("[LoginPage] Android OTP field found via: {}", xpath);
                        break;
                    }
                } catch (Exception ignored) { }
            }
            if (otpField == null) {
                // Dump all EditTexts for diagnosis
                try {
                    List<org.openqa.selenium.WebElement> all = driver.findElements(
                        org.openqa.selenium.By.xpath("//android.widget.EditText"));
                    logger.warn("[LoginPage] OTP field not found — {} EditTexts on screen:", all.size());
                    for (int i = 0; i < all.size(); i++) {
                        logger.warn("  EditText[{}] hint='{}' resource-id='{}' content-desc='{}'",
                            i, all.get(i).getAttribute("hint"),
                            all.get(i).getAttribute("resource-id"),
                            all.get(i).getAttribute("content-desc"));
                    }
                } catch (Exception ignored) { }
                throw new RuntimeException("[LoginPage] Android: OTP entry field not found");
            }
            otpField.click();
            otpField.clear();
            otpField.sendKeys(code);
            logger.info("[LoginPage] Android: OTP entered");

            // Dismiss keyboard so the Verify button becomes visible and tappable
            try {
                ((io.appium.java_client.HidesKeyboard) driver).hideKeyboard();
                logger.info("[LoginPage] Android: keyboard hidden after OTP entry");
            } catch (Exception e) {
                logger.debug("[LoginPage] Android: hideKeyboard skipped after OTP (already hidden?): {}", e.getMessage());
            }
            try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }

            // Tap Verify / Sign In button
            String[] verifyXPaths = {
                "//android.widget.Button[@resource-id='com.subaru.oneapp.stage:id/btCodeSignIn']",
                "//android.widget.Button[@content-desc='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON']",
                "//android.widget.Button[@text='Verify' or @text='VERIFY' or @text='Submit']",
                "//android.widget.Button[@text='Sign In' or @text='SIGN IN']",
                "//android.widget.Button[@enabled='true']",
            };
            boolean verifyClicked = false;
            for (String xpath : verifyXPaths) {
                try {
                    List<org.openqa.selenium.WebElement> els = driver.findElements(
                        org.openqa.selenium.By.xpath(xpath));
                    if (!els.isEmpty() && els.get(0).isDisplayed()) {
                        String txt = els.get(0).getAttribute("text");
                        String rid = els.get(0).getAttribute("resource-id");
                        logger.info("[LoginPage] Android Verify button found via '{}' — text='{}' resource-id='{}'", xpath, txt, rid);
                        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(els.get(0)))
                            .click();
                        logger.info("[LoginPage] Android: Verify button clicked");
                        verifyClicked = true;
                        break;
                    }
                } catch (Exception ignored) { }
            }
            if (!verifyClicked) {
                // Dump buttons for diagnosis
                try {
                    List<org.openqa.selenium.WebElement> btns = driver.findElements(
                        org.openqa.selenium.By.xpath("//android.widget.Button"));
                    logger.warn("[LoginPage] Verify button not found — {} buttons on screen:", btns.size());
                    for (int i = 0; i < btns.size(); i++) {
                        logger.warn("  Button[{}] text='{}' resource-id='{}' enabled='{}'",
                            i, btns.get(i).getAttribute("text"),
                            btns.get(i).getAttribute("resource-id"),
                            btns.get(i).getAttribute("enabled"));
                    }
                } catch (Exception ignored) { }
                throw new RuntimeException("[LoginPage] Android: Verify button not found after OTP entry");
            }
        } else {
            // iOS path — unchanged
            enterActivationCode(code);
            clickVerifyAccount();
        }
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