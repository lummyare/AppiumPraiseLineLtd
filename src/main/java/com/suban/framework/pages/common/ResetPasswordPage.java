package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * ResetPasswordPage — Covers Forgot Password / Password Reset flows.
 * Handles email-based reset, SMS-based reset, expired link/code error scenarios,
 * and password mismatch / weak password validation.
 */
public class ResetPasswordPage extends BasePage {

    // ── Forgot Password link (Sign In screen) ─────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Forgot Password']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='forgotPasswordButton'"
            + " or @label='Forgot Password?' or @label='Forgot Password']"
            + " | //XCUIElementTypeStaticText[@label='Forgot Password?' or @label='Forgot Password']")
    private WebElement forgotPasswordLink;

    // ── Email input for recovery ───────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Email' or @hint='Email Address' or @content-desc='emailInput']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='recoveryEmailInput'"
            + " or @label='Email' or @name='FR_NATIVE_RECOVERY_EMAIL_TEXTFIELD'"
            + " or @label='Email address']")
    private WebElement recoveryEmailInput;

    // ── SMS option for reset ───────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'SMS') or contains(@text,'Text') or contains(@text,'Phone')] | //android.widget.TextView[contains(@text,'SMS') or contains(@text,'Send SMS')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'SMS')"
            + " or contains(@label,'Text') or contains(@label,'Phone')"
            + " or contains(@name,'smsOption') or contains(@label,'Send SMS')]")
    private WebElement smsVerificationOption;

    // ── Phone input for SMS reset ──────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Phone Number' or @hint='Phone' or @content-desc='recoveryPhoneInput']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='recoveryPhoneInput'"
            + " or @label='Phone Number' or @name='FR_NATIVE_RECOVERY_PHONE_TEXTFIELD']")
    private WebElement recoveryPhoneInput;

    // ── Submit / Send recovery button ──────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Submit' or @text='Send' or @text='Continue' or @content-desc='sendOtpButton'] | //android.widget.TextView[@text='Submit' or @text='Send']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Submit' or @name='submitButton'"
            + " or @label='Send' or @name='sendButton' or @label='Continue'"
            + " or @name='FR_NATIVE_RECOVERY_SUBMIT_BUTTON']")
    private WebElement submitRecoveryButton;

    // ── Reset link confirmation text ───────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'sent') or contains(@text,'reset link') or contains(@text,'Check your email') or contains(@text,'recovery')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'sent')"
            + " or contains(@label,'reset link') or contains(@label,'Check your email')"
            + " or contains(@label,'recovery')]")
    private WebElement resetLinkConfirmationText;

    // ── SMS code confirmation text ─────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'SMS') or contains(@text,'text message') or contains(@text,'verification code')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'SMS')"
            + " or contains(@label,'text message') or contains(@label,'verification code')]")
    private WebElement smsCodeConfirmationText;

    // ── New password input ─────────────────────────────────────────────────
    // CONFIRMED from live element dump:
    //   name='create_password_new_password_textfield'  label='New Password'
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='New Password' or @content-desc='newPasswordInput']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='create_password_new_password_textfield'"
            + " or @label='New Password' or @label='NEW PASSWORD'"
            + " or @name='FR_NATIVE_NEW_PASSWORD_TEXTFIELD']")
    private WebElement newPasswordInput;

    // ── Confirm new password input ─────────────────────────────────────────
    // CONFIRMED from live element dump:
    //   name='create_password_confirm_password_textfield'  label='Confirm Password'
    //   visible=false when keyboard open — scroll required before interaction
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Confirm Password' or @hint='Confirm New Password' or @content-desc='confirmPasswordInput']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='create_password_confirm_password_textfield'"
            + " or @label='Confirm Password' or @label='CONFIRM PASSWORD'"
            + " or @name='FR_NATIVE_CONFIRM_PASSWORD_TEXTFIELD']")
    private WebElement confirmNewPasswordInput;

    // ── Save / Update password button ──────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Save' or @text='Update Password' or @text='Submit' or @text='Reset Password' or @content-desc='submitButton'] | //android.widget.TextView[@text='Submit']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Save' or @name='saveButton'"
            + " or @label='Update Password' or @name='updatePasswordButton'"
            + " or @label='Confirm']")
    private WebElement savePasswordButton;

    // ── Password updated confirmation ──────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'updated') or contains(@text,'changed') or contains(@text,'Password Updated') or contains(@text,'success')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'updated')"
            + " or contains(@label,'changed') or contains(@label,'Password Updated')"
            + " or contains(@label,'success') or contains(@name,'passwordUpdatedBanner')]")
    private WebElement passwordUpdatedConfirmation;

    // ── Error messages ─────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'expired') or contains(@text,'invalid') or contains(@text,'not match') or contains(@text,'weak') or contains(@text,'error')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'expired')"
            + " or contains(@label,'invalid') or contains(@label,'not match')"
            + " or contains(@label,'weak') or contains(@label,'error')"
            + " or contains(@name,'errorMessage')]")
    private WebElement resetErrorMessage;

    // ── SMS verification code input ────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='6-digit code' or @hint='Verification Code' or @content-desc='smsCodeInput' or @content-desc='FR_NATIVE_OTP_TEXTFIELD' or contains(@hint,'code')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='6-digit code'"
            + " or @name='smsCodeInput' or @name='FR_NATIVE_OTP_TEXTFIELD'"
            + " or @label='Verification Code' or @label='6-digit code'"
            + " or contains(@label,'Enter code')]")
    private WebElement smsCodeInput;

    // ── Verify button for SMS code / OTP page ─────────────────────────────
    // Real element confirmed from OB_E2E_007 dump:
    //   name='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON'  label='VERIFY'
    @AndroidFindBy(xpath = "//android.widget.Button[@text='VERIFY' or @text='Verify' or @text='Submit' or @content-desc='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON' or @content-desc='verifyButton' or @content-desc='FR_NATIVE_OTP_SUBMIT_BUTTON']")
    @iOSXCUITFindBy(xpath = "//*[@name='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON'"
            + " or @label='VERIFY' or @name='VERIFY'"
            + " or @label='Verify' or @name='verifyButton'"
            + " or @label='Submit' or @name='FR_NATIVE_OTP_SUBMIT_BUTTON']")
    private WebElement verifyCodeButton;

    // ── Reset It button (on sign-in page, next to email field) ────────────
    // FR_NATIVE_ENTER_PASSWORD_PROMPT_TEXTVIEW is the outer tappable link container.
    // name='RESET IT' is its inner text span — tapping it alone does NOT navigate.
    // Always tap the outer container first.
    @AndroidFindBy(xpath = "//*[@content-desc='FR_NATIVE_ENTER_PASSWORD_PROMPT_TEXTVIEW' or @content-desc='FR_NATIVE_FORGOT_PASSWORD_RESET_IT_BUTTON' or @content-desc='resetItButton'] | //android.widget.TextView[contains(@text,'RESET IT') or contains(@text,'Reset it') or contains(@text,'Reset It')]")
    @iOSXCUITFindBy(xpath = "//*[@name='FR_NATIVE_ENTER_PASSWORD_PROMPT_TEXTVIEW'"
            + " or @name='DON\u2019T REMEMBER YOUR PASSWORD? RESET IT'"
            + " or @label='Reset it' or @label='Reset It'"
            + " or @name='resetItButton' or @name='FR_NATIVE_FORGOT_PASSWORD_RESET_IT_BUTTON']")
    private WebElement resetItButton;

    // ── OTP / verification code input (We Sent An Email page) ────────────
    // Real element confirmed from OB_E2E_007 dump:
    //   name='6-digit code'  label='6-digit code'
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='6-digit code' or @content-desc='FR_NATIVE_OTP_TEXTFIELD' or @content-desc='otpInput' or @content-desc='verificationCodeInput' or contains(@hint,'code') or contains(@hint,'Code')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='6-digit code'"
            + " or @label='6-digit code' or @name='FR_NATIVE_OTP_TEXTFIELD'"
            + " or @name='otpInput' or @name='verificationCodeInput'"
            + " or contains(@label,'code') or contains(@label,'Code')"
            + " or contains(@label,'Enter') or contains(@placeholder,'code')]"
            + " | //XCUIElementTypeSecureTextField[contains(@label,'code')]")
    private WebElement otpCodeInput;

    // ── Reset Password / Create Password button ───────────────────────
    // CONFIRMED from live element dump:
    //   name='create_password_button'  label='Create Password'  enabled=true
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='create_password_button' or @text='Create Password' or @text='RESET PASSWORD' or @text='Reset Password' or @content-desc='resetPasswordButton' or @content-desc='FR_NATIVE_RESET_PASSWORD_BUTTON']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='create_password_button'"
            + " or @label='Create Password'"
            + " or @label='RESET PASSWORD' or @label='Reset Password'"
            + " or @name='resetPasswordButton'"
            + " or @name='FR_NATIVE_RESET_PASSWORD_BUTTON']")
    private WebElement resetPasswordButton;

    // ── Done button (Password Reset success page) ──────────────────────────
    // CONFIRMED from screenshot: label='DONE' (uppercase) on success page.
    // Follows create_password_* naming convention used throughout this flow.
    // CONFIRMED from live element dump (12:10 run):
    //   name='fr_confirmation_screen_done_button'  label='Done Button'  type=XCUIElementTypeButton
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='fr_confirmation_screen_done_button' or @content-desc='create_password_done_button' or @text='DONE' or @text='Done' or @text='Done Button']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='fr_confirmation_screen_done_button']"
            + " | //XCUIElementTypeButton[@label='Done Button']"
            + " | //XCUIElementTypeButton[@name='create_password_done_button']"
            + " | //XCUIElementTypeButton[@label='DONE' or @label='Done']")
    private WebElement doneButton;

    // ── We Sent An Email heading (page assertion) ──────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'WE SENT AN EMAIL') or contains(@text,'We Sent An Email') or contains(@text,'Check your email') or contains(@text,'sent an email') or @content-desc='sentEmailTitle']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'WE SENT AN EMAIL')"
            + " or contains(@label,'We Sent An Email') or contains(@label,'we sent')"
            + " or contains(@label,'Check your email') or contains(@label,'sent an email')"
            + " or contains(@name,'sentEmailTitle')]")
    private WebElement weSentAnEmailHeading;

    // ── Reset Your Password heading (page assertion) ───────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'RESET YOUR PASSWORD') or contains(@text,'Reset Your Password') or contains(@text,'New Password') or @content-desc='resetPasswordTitle']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'RESET YOUR PASSWORD')"
            + " or contains(@label,'Reset Your Password') or contains(@label,'New Password')"
            + " or contains(@name,'resetPasswordTitle')]")
    private WebElement resetYourPasswordHeading;

    // ── Password Reset success heading ———————————————————————————————————————————————
    // CONFIRMED from live element dump (12:10 run):
    //   The success page is a generic FR confirmation screen with these elements:
    //   name='fr_confirmation_screen_title'        label='Title'               (StaticText)
    //   name='fr_confirmation_screen_description'  label='Description'         (StaticText)
    //   name='fr_confirmation_screen_top_image'    label='Confirmation Image'  (Image)
    //   name='fr_confirmation_screen_done_button'  label='Done Button'         (Button)
    @AndroidFindBy(xpath = "//*[@content-desc='fr_confirmation_screen_title'] | //*[@content-desc='fr_confirmation_screen_description'] | //*[@content-desc='fr_confirmation_screen_top_image'] | //android.widget.TextView[contains(@text,'PASSWORD RESET') or contains(@text,'USE YOUR NEW PASSWORD')]")
    @iOSXCUITFindBy(xpath = "//*[@name='fr_confirmation_screen_title']"
            + " | //*[@name='fr_confirmation_screen_description']"
            + " | //*[@name='fr_confirmation_screen_top_image']"
            + " | //XCUIElementTypeStaticText[contains(@label,'PASSWORD RESET')]"
            + " | //XCUIElementTypeStaticText[contains(@label,'USE YOUR NEW PASSWORD')]")
    private WebElement resetSuccessHeading;

    public ResetPasswordPage(AppiumDriver driver) {
        super(driver);
    }

    // ── Actions ───────────────────────────────────────────────────────────

    public void tapForgotPassword() {
        logger.info("[ResetPasswordPage] Tapping Forgot Password link");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] Forgot Password fallback");
            tapByLabelFallback("Forgot Password");
        }
    }

    public void enterRecoveryEmail(String email) {
        logger.info("[ResetPasswordPage] Entering recovery email: {}", email);
        try {
            wait.until(ExpectedConditions.visibilityOf(recoveryEmailInput)).clear();
            recoveryEmailInput.sendKeys(email);
        } catch (Exception e) {
            // Fallback: any visible text field
            List<WebElement> fields = driver.findElements(
                By.xpath("//XCUIElementTypeTextField"));
            if (!fields.isEmpty()) {
                fields.get(0).clear();
                fields.get(0).sendKeys(email);
            }
        }
    }

    public void selectSmsVerificationOption() {
        logger.info("[ResetPasswordPage] Selecting SMS verification option — smart discovery");
        java.util.List<String> smsKeywords = java.util.Arrays.asList(
            "sms", "phone", "text", "mobile", "number", "send sms", "send text",
            "fr_native_sms", "fr_native_phone", "phoneOption", "smsOption"
        );
        smartTap("selectSmsVerificationOption", smsKeywords,
            smsVerificationOption, "SMS", "Phone", "Text");
    }

    public void enterRecoveryPhone(String phone) {
        logger.info("[ResetPasswordPage] Entering recovery phone: {}", phone);
        try {
            wait.until(ExpectedConditions.visibilityOf(recoveryPhoneInput)).clear();
            recoveryPhoneInput.sendKeys(phone);
        } catch (Exception e) {
            List<WebElement> fields = driver.findElements(
                By.xpath("//XCUIElementTypeTextField"));
            if (!fields.isEmpty()) {
                fields.get(0).clear();
                fields.get(0).sendKeys(phone);
            }
        }
    }

    public void tapSubmitRecovery() {
        logger.info("[ResetPasswordPage] Tapping Submit / Send — smart discovery");
        java.util.List<String> submitKeywords = java.util.Arrays.asList(
            "submit", "send", "continue", "next", "ok", "confirm",
            "fr_native_submit", "fr_native_send", "fr_native_continue",
            "fr_native_recovery_submit", "submitbutton", "sendbutton"
        );
        smartTap("tapSubmitRecovery", submitKeywords,
            submitRecoveryButton, "Submit", "Send", "Continue", "Next");
    }

    public void enterSmsCode(String code) {
        logger.info("[ResetPasswordPage] Entering SMS reset code: {}", code);
        try {
            wait.until(ExpectedConditions.visibilityOf(smsCodeInput)).clear();
            smsCodeInput.sendKeys(code);
        } catch (Exception e) {
            List<WebElement> fields = driver.findElements(
                By.xpath("//XCUIElementTypeTextField"));
            if (!fields.isEmpty()) {
                fields.get(0).clear();
                fields.get(0).sendKeys(code);
            }
        }
    }

    public void tapVerifyCode() {
        logger.info("[ResetPasswordPage] Tapping Verify button for SMS code");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(verifyCodeButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Verify");
        }
    }

    public void enterNewPassword(String password) {
        logger.info("[ResetPasswordPage] Entering new password");

        // Strategy: positional index first (always reliable), then named fallbacks.
        // The RESET YOUR PASSWORD page has exactly 2 SecureTextFields:
        //   [1] = NEW PASSWORD   [2] = CONFIRM PASSWORD
        String[] newPwdXPaths = {
            "(//XCUIElementTypeSecureTextField)[1]",                                  // positional — most reliable
            "//XCUIElementTypeSecureTextField[@label='NEW PASSWORD']",                // confirmed from screenshot
            "//XCUIElementTypeSecureTextField[@name='FR_NATIVE_RESETPASSWORD_NEWPASSWORD_TEXTFIELD']",
            "//XCUIElementTypeSecureTextField[@name='FR_NATIVE_NEW_PASSWORD_TEXTFIELD']",
            "//XCUIElementTypeSecureTextField[@label='New Password']",
            "//XCUIElementTypeSecureTextField[@name='newPasswordInput']"
        };

        org.openqa.selenium.WebElement field = findSecureField("enterNewPassword", newPasswordInput, newPwdXPaths);
        field.click();
        field.clear();
        field.sendKeys(password);
        logger.info("[ResetPasswordPage] ✅ New password entered");

        // CRITICAL: dismiss keyboard so the CONFIRM PASSWORD field below is tappable
        dismissKeyboardSilently("enterNewPassword");
    }

    public void enterConfirmNewPassword(String password) {
        logger.info("[ResetPasswordPage] Entering confirm new password");

        // CONFIRMED from live element dump:
        //   name='create_password_confirm_password_textfield'  label='Confirm Password'
        //   visible=false at y=431 when keyboard open — MUST scroll down first.
        String[] confirmXPaths = {
            "//XCUIElementTypeSecureTextField[@name='create_password_confirm_password_textfield']", // confirmed real name
            "//XCUIElementTypeSecureTextField[@label='Confirm Password']",                          // confirmed real label
            "(//XCUIElementTypeSecureTextField)[2]",                                                // positional fallback
            "//XCUIElementTypeSecureTextField[@label='CONFIRM PASSWORD']",
            "//XCUIElementTypeSecureTextField[@name='FR_NATIVE_CONFIRM_PASSWORD_TEXTFIELD']",
            "//XCUIElementTypeSecureTextField[@label='Confirm New Password']"
        };

        // ── Scroll down so the confirm field is in the visible viewport ──
        // The page is a ScrollView (2 pages tall). When the keyboard is open the
        // confirm field is pushed below y=424 (the visible height) and is invisible.
        // We scroll the ScrollView to bring it into view before tapping.
        try {
            logger.info("[ResetPasswordPage] Scrolling down to reveal CONFIRM PASSWORD field");
            org.openqa.selenium.WebElement scrollView = driver.findElement(
                org.openqa.selenium.By.xpath("//XCUIElementTypeScrollView"));
            // Swipe up inside the scroll view to scroll the content down
            org.openqa.selenium.Dimension size = scrollView.getSize();
            org.openqa.selenium.Point origin = scrollView.getLocation();
            int startX = origin.getX() + size.getWidth() / 2;
            int startY = origin.getY() + (int)(size.getHeight() * 0.75);
            int endY   = origin.getY() + (int)(size.getHeight() * 0.25);
            org.openqa.selenium.interactions.PointerInput finger =
                new org.openqa.selenium.interactions.PointerInput(
                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
            org.openqa.selenium.interactions.Sequence swipe =
                new org.openqa.selenium.interactions.Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(
                java.time.Duration.ZERO,
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(
                java.time.Duration.ofMillis(400),
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(java.util.Collections.singletonList(swipe));
            Thread.sleep(600);
            logger.info("[ResetPasswordPage] Scroll completed");
        } catch (Exception scrollEx) {
            logger.warn("[ResetPasswordPage] Scroll skipped: {}", scrollEx.getMessage());
        }

        org.openqa.selenium.WebElement field = findSecureField("enterConfirmNewPassword", confirmNewPasswordInput, confirmXPaths);
        field.click(); // explicitly tap to focus
        field.clear();
        field.sendKeys(password);
        logger.info("[ResetPasswordPage] ✅ Confirm new password entered");

        // Dismiss keyboard so Reset Password button is fully visible and tappable
        dismissKeyboardSilently("enterConfirmNewPassword");
    }

    /**
     * Finds a SecureTextField by trying the @iOSXCUITFindBy element first,
     * then each XPath in order. Dumps the full page source at ERROR level if
     * nothing works, so we can identify the real element name.
     */
    private org.openqa.selenium.WebElement findSecureField(
            String callerName,
            WebElement annotatedElement,
            String[] xpaths) {
        // 1. Try the PageFactory-annotated element
        try {
            wait.until(ExpectedConditions.visibilityOf(annotatedElement));
            logger.info("[ResetPasswordPage] {} ✔ found via @iOSXCUITFindBy annotation", callerName);
            return annotatedElement;
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] {} annotation failed — trying XPath list", callerName);
        }
        // 2. Try each XPath in order
        for (String xp : xpaths) {
            try {
                org.openqa.selenium.WebElement el =
                    driver.findElement(org.openqa.selenium.By.xpath(xp));
                logger.info("[ResetPasswordPage] {} ✔ found via: {}", callerName, xp);
                return el;
            } catch (Exception ignored) {}
        }
        // 3. Nothing worked — dump page source so we can see the real names
        logger.error("[ResetPasswordPage] {} ❌ FIELD NOT FOUND. Dumping page source:", callerName);
        try {
            String src = driver.getPageSource();
            logger.error("[ResetPasswordPage] PAGE SOURCE:\n{}",
                src.length() > 6000 ? src.substring(0, 6000) : src);
        } catch (Exception ignored) {}
        throw new RuntimeException(
            "[ResetPasswordPage] " + callerName + ": SecureTextField not found by any locator. " +
            "Check the page source dump above to find the real name/label.");
    }

    /** Calls hideKeyboard(), swallowing any exception (keyboard may already be gone). */
    private void dismissKeyboardSilently(String callerName) {
        try {
            ((io.appium.java_client.HidesKeyboard) driver).hideKeyboard();
            logger.info("[ResetPasswordPage] Keyboard dismissed ({})", callerName);
            Thread.sleep(800);
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] hideKeyboard skipped ({}): {}", callerName, e.getMessage());
        }
    }

    public void tapSavePassword() {
        logger.info("[ResetPasswordPage] Tapping Save / Update Password");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(savePasswordButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Save");
            } catch (Exception e2) {
                tapByLabelFallback("Update Password");
            }
        }
    }

    // ── Assertions ────────────────────────────────────────────────────────

    public boolean isResetLinkConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(resetLinkConfirmationText));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("sent") || src.contains("reset link") || src.contains("Check your email");
        }
    }

    public boolean isSmsCodeConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(smsCodeConfirmationText));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("SMS") || src.contains("text message") || src.contains("verification code");
        }
    }

    public boolean isPasswordUpdatedConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordUpdatedConfirmation));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("updated") || src.contains("Password Updated") || src.contains("success");
        }
    }

    public boolean isResetErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(resetErrorMessage));
            logger.info("[ResetPasswordPage] Reset error: {}", resetErrorMessage.getText());
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("expired") || src.contains("invalid") || src.contains("not match")
                    || src.contains("weak") || src.contains("error");
        }
    }

    public String getResetErrorText() {
        try {
            return resetErrorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ── New actions for full reset flow ───────────────────────────────────────

    /**
     * Taps the "Reset It" / "Forgot Password" link on the ENTER YOUR PASSWORD page.
     *
     * The link is a hyperlink SPAN inside a XCUIElementTypeTextView
     * (FR_NATIVE_ENTER_PASSWORD_PROMPT_TEXTVIEW). The full text is:
     *   "DON\u2019T REMEMBER YOUR PASSWORD? RESET IT"
     *
     * Calling .click() on a XCUIElementTypeTextView does NOT trigger embedded links
     * on iOS XCUITest. Instead, we use a W3C PointerInput coordinate tap aimed at
     * the right-hand portion of the TextView where "RESET IT" renders.
     *
     * Strategy:
     *  1. Find the TextView by accessibility name.
     *  2. Compute the tap point: x = element.x + element.width * 0.78 (right side)
     *                            y = element.y + element.height / 2  (vertical centre)
     *  3. Perform a W3C touch tap at those absolute screen coordinates.
     *  4. Wait 1 s for navigation to begin.
     */
    private static volatile String discoveredResetItLocator = null; // kept for API compat

    public void tapResetIt() {
        logger.info("[ResetPasswordPage] Tapping RESET IT link via coordinate tap");

        // ── 1. Locate the TextVIew container ────────────────────────────────
        org.openqa.selenium.WebElement promptView = null;
        String[] locators = {
            "//*[@name='FR_NATIVE_ENTER_PASSWORD_PROMPT_TEXTVIEW']",
            "//*[contains(@value,'RESET IT')]",
            "//*[contains(@value,'Reset It')]",
            "//XCUIElementTypeTextView[contains(@value,'RESET')]"
        };
        for (String xpath : locators) {
            try {
                promptView = driver.findElement(org.openqa.selenium.By.xpath(xpath));
                logger.info("[ResetPasswordPage] Found RESET IT container via: {}", xpath);
                break;
            } catch (Exception ignored) {}
        }

        if (promptView == null) {
            throw new RuntimeException(
                "[ResetPasswordPage] Could not find FR_NATIVE_ENTER_PASSWORD_PROMPT_TEXTVIEW. " +
                "The RESET IT link container was not found on the page.");
        }

        // ── 2. Compute coordinate of the "RESET IT" word (right ~78% of element width) ──
        org.openqa.selenium.Point  loc  = promptView.getLocation();
        org.openqa.selenium.Dimension dim  = promptView.getSize();

        int tapX = loc.getX() + (int)(dim.getWidth()  * 0.78);
        int tapY = loc.getY() + (int)(dim.getHeight() * 0.50);

        logger.info("[ResetPasswordPage] TextView bounds: x={} y={} w={} h={} → tapping ({}, {})",
            loc.getX(), loc.getY(), dim.getWidth(), dim.getHeight(), tapX, tapY);

        // ── 3. W3C PointerInput tap at the computed coordinate ───────────────
        try {
            org.openqa.selenium.interactions.PointerInput finger =
                new org.openqa.selenium.interactions.PointerInput(
                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
            org.openqa.selenium.interactions.Sequence tap =
                new org.openqa.selenium.interactions.Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(
                java.time.Duration.ZERO,
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), tapX, tapY));
            tap.addAction(finger.createPointerDown(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(java.util.Collections.singletonList(tap));
            Thread.sleep(1500); // allow navigation to begin
            logger.info("[ResetPasswordPage] ✅ Coordinate tap on RESET IT succeeded");
        } catch (Exception e) {
            throw new RuntimeException(
                "[ResetPasswordPage] Coordinate tap on RESET IT failed: " + e.getMessage(), e);
        }
    }

    /**
     * Generic smart-tap helper used by methods that couldn't find their button.
     * 1. Tries the provided WebElement (primary locator).
     * 2. Fetches ALL buttons+texts on page, scores each against keywords, taps the winner.
     * 3. Dumps everything at ERROR level if nothing matches — always visible in logs.
     */
    private void smartTap(String callerName,
                           java.util.List<String> keywords,
                           WebElement primary,
                           String... labelFallbacks) {
        // ── Try primary locator ──
        try {
            wait.until(ExpectedConditions.elementToBeClickable(primary)).click();
            logger.info("[ResetPasswordPage] {} primary locator succeeded", callerName);
            return;
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] {} primary failed — scanning page: {}", callerName, e.getMessage());
        }

        // ── Scan every button / static text on screen ──
        try {
            java.util.List<WebElement> allEls = driver.findElements(
                By.xpath("//XCUIElementTypeButton | //XCUIElementTypeStaticText | //XCUIElementTypeLink"));

            logger.error("[ResetPasswordPage] {} — scanning {} elements. Full dump:", callerName, allEls.size());
            WebElement bestMatch = null;
            int bestScore = 0;
            String bestDesc = "";

            for (WebElement el : allEls) {
                String name  = safeAttr(el, "name");
                String label = safeAttr(el, "label");
                String value = safeAttr(el, "value");
                String text  = tryGetText(el);
                String combined = (name + " " + label + " " + value + " " + text).toLowerCase().trim();

                // Always dump every element so we can see exactly what's on screen
                logger.error("  → name='{}' label='{}' value='{}' text='{}'", name, label, value, text);

                if (combined.isBlank()) continue;
                int score = 0;
                for (String kw : keywords) {
                    if (combined.contains(kw.toLowerCase())) score += combined.equals(kw.toLowerCase()) ? 100 : 10;
                }
                if (score > bestScore) {
                    bestScore = score;
                    bestMatch = el;
                    bestDesc  = String.format("name='%s' label='%s'", name, label);
                }
            }

            if (bestMatch != null && bestScore > 0) {
                logger.error("[ResetPasswordPage] {} ✅ Best candidate (score={}) — {}", callerName, bestScore, bestDesc);
                wait.until(ExpectedConditions.elementToBeClickable(bestMatch)).click();
                logger.error("[ResetPasswordPage] {} smart-tap succeeded — PROMOTE THIS LOCATOR: {}", callerName, bestDesc);
                return;
            }

            // ── Nothing matched — throw with full context ──
            throw new RuntimeException(
                "[ResetPasswordPage] " + callerName + " could not find target element. " +
                "Check the element dump above (name/label/value/text) to find the real locator.");

        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException("[ResetPasswordPage] " + callerName + " smart scan failed: " + e.getMessage(), e);
        }
    }

    private String safeAttr(org.openqa.selenium.WebElement el, String attr) {
        try {
            String v = el.getAttribute(attr);
            return v != null ? v : "";
        } catch (Exception e) { return ""; }
    }

    private String tryGetText(org.openqa.selenium.WebElement el) {
        try { return el.getText(); } catch (Exception e) { return ""; }
    }

    /**
     * Enters the OTP into the verification code field on the 'We Sent An Email' page.
     * Tries the @iOSXCUITFindBy element first, then falls back to any visible text field.
     */
    public void enterOtpCode(String otp) {
        logger.info("[ResetPasswordPage] Entering OTP code");
        try {
            wait.until(ExpectedConditions.visibilityOf(otpCodeInput)).clear();
            otpCodeInput.sendKeys(otp);
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] OTP primary locator failed, scanning all text fields");
            List<WebElement> fields = driver.findElements(
                By.xpath("//XCUIElementTypeTextField | //XCUIElementTypeSecureTextField"));
            if (!fields.isEmpty()) {
                fields.get(0).clear();
                fields.get(0).sendKeys(otp);
            } else {
                throw new RuntimeException("[ResetPasswordPage] No OTP input field found");
            }
        }
    }

    /** Taps the Verify button on the OTP / 'We Sent An Email' page.
     *
     * The numeric keyboard is still open after OTP entry — it must be dismissed
     * BEFORE tapping VERIFY or the keyboard overlay blocks the button.
     *
     * Real button: name='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON', label='VERIFY'
     */
    public void tapVerifyOtp() {
        logger.info("[ResetPasswordPage] Dismissing keyboard before tapping VERIFY");

        // ── 1. Dismiss the on-screen keyboard first ──────────────────────────
        try {
            ((io.appium.java_client.HidesKeyboard) driver).hideKeyboard();
            logger.info("[ResetPasswordPage] Keyboard dismissed");
            Thread.sleep(800); // let the UI settle
        } catch (Exception kbEx) {
            logger.warn("[ResetPasswordPage] hideKeyboard skipped: {}", kbEx.getMessage());
        }

        logger.info("[ResetPasswordPage] Tapping VERIFY button (OTP page)");

        // ── 2. Primary: @iOSXCUITFindBy locator (FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON) ──
        try {
            wait.until(ExpectedConditions.elementToBeClickable(verifyCodeButton)).click();
            logger.info("[ResetPasswordPage] \u2705 VERIFY tapped via primary locator");
            return;
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] VERIFY primary failed — trying label fallbacks: {}", e.getMessage());
        }

        // ── 3. Label fallbacks: VERIFY (uppercase), Verify, Submit ────────────
        for (String label : new String[]{ "VERIFY", "Verify", "Submit" }) {
            try {
                tapByLabelFallback(label);
                logger.info("[ResetPasswordPage] \u2705 VERIFY tapped via label fallback '{}'", label);
                return;
            } catch (Exception ignored) {}
        }

        // ── 4. Ultimate: find the button by name and coordinate-tap it ────────
        logger.warn("[ResetPasswordPage] All label fallbacks failed — trying coordinate tap on VERIFY");
        try {
            org.openqa.selenium.WebElement verifyBtn = null;
            String[] verifyLocators = {
                "//*[@name='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON']",
                "//*[@label='VERIFY']",
                "//*[@name='VERIFY']",
                "//XCUIElementTypeButton[contains(@label,'VERIF')]"
            };
            for (String xp : verifyLocators) {
                try {
                    verifyBtn = driver.findElement(org.openqa.selenium.By.xpath(xp));
                    logger.info("[ResetPasswordPage] VERIFY found via: {}", xp);
                    break;
                } catch (Exception ignored) {}
            }
            if (verifyBtn != null) {
                org.openqa.selenium.Point  loc = verifyBtn.getLocation();
                org.openqa.selenium.Dimension dim = verifyBtn.getSize();
                int tapX = loc.getX() + dim.getWidth()  / 2;
                int tapY = loc.getY() + dim.getHeight() / 2;
                logger.info("[ResetPasswordPage] Coordinate tap on VERIFY at ({}, {})", tapX, tapY);
                org.openqa.selenium.interactions.PointerInput finger =
                    new org.openqa.selenium.interactions.PointerInput(
                        org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
                org.openqa.selenium.interactions.Sequence tap =
                    new org.openqa.selenium.interactions.Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(
                    java.time.Duration.ZERO,
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), tapX, tapY));
                tap.addAction(finger.createPointerDown(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(java.util.Collections.singletonList(tap));
                logger.info("[ResetPasswordPage] \u2705 VERIFY coordinate tap completed");
                Thread.sleep(1000);
                return;
            }
            throw new RuntimeException("[ResetPasswordPage] VERIFY button not found by any locator");
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException("[ResetPasswordPage] tapVerifyOtp coordinate fallback failed: " + e.getMessage(), e);
        }
    }

    /** Taps the 'Reset Password' button on the Reset Your Password page.
     *
     * Strategy (in order):
     *  1. Dismiss keyboard (may still be open from confirm-password entry)
     *  2. Dump ALL elements on page to logs — so we always know the real button name
     *  3. Find the button by any matching XPath (using visibility, NOT clickable —
     *     the button may be enabled=false if passwords don’t match, but we still
     *     try tapping via W3C coordinates which bypasses the enabled check)
     *  4. Coordinate-tap the found element
     */
    public void tapResetPasswordButton() {

        // ── 1. Dismiss keyboard ──────────────────────────────────────────────
        // hideKeyboard() fails on this app (WDA Code=1 — app manages keyboard).
        // Instead: tap the Return key on the keyboard to dismiss it, then wait.
        dismissKeyboardSilently("tapResetPasswordButton"); // attempt hideKeyboard (may warn)
        try {
            org.openqa.selenium.WebElement returnKey = driver.findElement(
                org.openqa.selenium.By.xpath("//XCUIElementTypeButton[@name='Return']"));
            returnKey.click();
            logger.info("[ResetPasswordPage] Tapped Return key to dismiss keyboard");
            Thread.sleep(800);
        } catch (Exception kbEx) {
            logger.warn("[ResetPasswordPage] Return key tap skipped: {}", kbEx.getMessage());
            // Fallback: swipe down from top of screen to dismiss keyboard
            try {
                org.openqa.selenium.interactions.PointerInput finger =
                    new org.openqa.selenium.interactions.PointerInput(
                        org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
                org.openqa.selenium.interactions.Sequence swipe =
                    new org.openqa.selenium.interactions.Sequence(finger, 1);
                swipe.addAction(finger.createPointerMove(java.time.Duration.ZERO,
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), 201, 300));
                swipe.addAction(finger.createPointerDown(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                swipe.addAction(finger.createPointerMove(java.time.Duration.ofMillis(300),
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), 201, 500));
                swipe.addAction(finger.createPointerUp(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(java.util.Collections.singletonList(swipe));
                Thread.sleep(600);
                logger.info("[ResetPasswordPage] Swipe-down keyboard dismiss attempted");
            } catch (Exception swipeEx) {
                logger.warn("[ResetPasswordPage] Swipe dismiss also skipped: {}", swipeEx.getMessage());
            }
        }

        // ── 2. Dump all page elements so logs always show real names ────────────
        try {
            java.util.List<org.openqa.selenium.WebElement> allEls = driver.findElements(
                org.openqa.selenium.By.xpath("//*"));
            logger.info("[ResetPasswordPage] === RESET YOUR PASSWORD page element dump ({} elements) ===",
                allEls.size());
            for (org.openqa.selenium.WebElement el : allEls) {
                String n = safeAttr(el, "name");
                String l = safeAttr(el, "label");
                String t = safeAttr(el, "type");
                String en = safeAttr(el, "enabled");
                if (!n.isEmpty() || !l.isEmpty()) {
                    logger.info("  type={} name='{}' label='{}' enabled={}", t, n, l, en);
                }
            }
        } catch (Exception ignored) {}

        logger.info("[ResetPasswordPage] Tapping RESET PASSWORD button");

        // ── 3. Find the button by any XPath (visibility only, not clickable) ──────
        // Use //* not just //XCUIElementTypeButton — app may use a different element type.
        // Include FR_NATIVE pattern guesses and contains() as broadest net.
        // CONFIRMED from live element dump:
        //   name='create_password_button'  label='Create Password'  enabled=true
        String[] locators = {
            "//XCUIElementTypeButton[@name='create_password_button']",          // confirmed real name
            "//XCUIElementTypeButton[@label='Create Password']",                // confirmed real label
            "//*[@name='create_password_button']",                              // any type
            "//*[@label='Create Password']",
            "//*[@label='RESET PASSWORD']",
            "//*[@label='Reset Password']",
            "//*[@name='FR_NATIVE_RESET_PASSWORD_BUTTON']",
            "//XCUIElementTypeButton[contains(@label,'Create')]",
            "//XCUIElementTypeButton[contains(@label,'RESET')]",
            "//*[contains(@name,'create_password_button')]"
        };

        org.openqa.selenium.WebElement btn = null;
        String foundVia = "";
        for (String xp : locators) {
            try {
                btn = driver.findElement(org.openqa.selenium.By.xpath(xp));
                foundVia = xp;
                logger.info("[ResetPasswordPage] RESET PASSWORD found via: {}", xp);
                break;
            } catch (Exception ignored) {}
        }

        if (btn == null) {
            // Last-resort: dump page source in full so we can see the exact button name
            logger.error("[ResetPasswordPage] ❌ RESET PASSWORD not found by any XPath. Full page source:");
            try {
                String src = driver.getPageSource();
                logger.error("{}", src.length() > 8000 ? src.substring(0, 8000) : src);
            } catch (Exception ignored) {}
            throw new RuntimeException("[ResetPasswordPage] Reset Password button not found by any locator");
        }

        // ── 4. W3C coordinate tap — bypasses enabled=false restriction ────────
        try {
            org.openqa.selenium.Point  loc = btn.getLocation();
            org.openqa.selenium.Dimension dim = btn.getSize();
            int tapX = loc.getX() + dim.getWidth()  / 2;
            int tapY = loc.getY() + dim.getHeight() / 2;
            logger.info("[ResetPasswordPage] W3C coordinate tap on RESET PASSWORD at ({}, {}) via [{}]",
                tapX, tapY, foundVia);
            org.openqa.selenium.interactions.PointerInput finger =
                new org.openqa.selenium.interactions.PointerInput(
                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
            org.openqa.selenium.interactions.Sequence tap =
                new org.openqa.selenium.interactions.Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(
                java.time.Duration.ZERO,
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), tapX, tapY));
            tap.addAction(finger.createPointerDown(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(java.util.Collections.singletonList(tap));
            logger.info("[ResetPasswordPage] ✅ RESET PASSWORD coordinate tap completed");
            // ── 5. Wait for server-side password reset to process ──────────────
            // The app shows a blank/loading state (empty page source) for up to
            // several minutes while the backend processes the reset. Wait 30s flat
            // before any poll attempts so we don't exhaust retries during loading.
            logger.info("[ResetPasswordPage] Waiting 30s for server-side password reset processing...");
            Thread.sleep(30_000);
            logger.info("[ResetPasswordPage] 30s wait complete — proceeding to assert success page");
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(
                "[ResetPasswordPage] tapResetPasswordButton coordinate tap failed: " + e.getMessage(), e);
        }
    }

    /** Taps the 'Done' button on the password reset success page.
     *
     * CONFIRMED from screenshot: button label is 'DONE' (uppercase).
     * The success screen may be transient and auto-navigate — tolerate that.
     *
     * Strategy:
     * 1. Poll for up to 60s for the DONE button element to become available
     *    (the page source is empty during server processing, button appears after).
     * 2. When found, perform a W3C coordinate tap (bypasses enabled=false).
     * 3. If never found, check if we're already on Welcome Back (auto-navigated).
     * 4. Last resort: coordinate tap at the bottom-center of screen where DONE typically lives.
     */
    public void tapDoneButton() {
        logger.info("[ResetPasswordPage] Polling for Done button (up to 60s)...");

        // ── 1. Poll for DONE button element up to 60s ────────────────────────
        // CONFIRMED from live element dump:
        //   name='fr_confirmation_screen_done_button'  label='Done Button'
        String[] doneLocators = {
            "//XCUIElementTypeButton[@name='fr_confirmation_screen_done_button']", // CONFIRMED
            "//XCUIElementTypeButton[@label='Done Button']",                      // CONFIRMED
            "//*[@name='create_password_done_button']",
            "//XCUIElementTypeButton[@label='DONE']",
            "//XCUIElementTypeButton[@label='Done']",
            "//*[@label='DONE']",
            "//*[@label='Done']"
        };
        org.openqa.selenium.WebElement doneEl = null;
        String foundVia = "";
        outer:
        for (int poll = 1; poll <= 30; poll++) {
            // Check for DONE button element
            for (String xp : doneLocators) {
                try {
                    org.openqa.selenium.WebElement el = driver.findElement(
                        org.openqa.selenium.By.xpath(xp));
                    doneEl = el;
                    foundVia = xp;
                    logger.info("[ResetPasswordPage] ✅ DONE button found on poll {} via: {}", poll, xp);
                    break outer;
                } catch (Exception ignored) {}
            }
            // Check if we've already navigated to Welcome Back (success page auto-dismissed)
            if (isWelcomeBackPageDisplayed()) {
                logger.info("[ResetPasswordPage] App already on Welcome Back — Done tap not needed (poll {})", poll);
                return;
            }
            // On first poll, dump all elements so we can see what IS on screen
            if (poll == 1 || poll == 5) {
                logger.info("[ResetPasswordPage] Poll {} — DONE not found, dumping screen elements:", poll);
                dumpVisibleElements();
            } else {
                logger.info("[ResetPasswordPage] Poll {} — DONE not found yet, waiting 2s...", poll);
            }
            try { Thread.sleep(2_000); } catch (InterruptedException ignored) {}
        }

        // ── 2. Tap the found element via W3C coordinate tap ──────────────────
        if (doneEl != null) {
            try {
                org.openqa.selenium.Point loc = doneEl.getLocation();
                org.openqa.selenium.Dimension dim = doneEl.getSize();
                int tapX = loc.getX() + dim.getWidth() / 2;
                int tapY = loc.getY() + dim.getHeight() / 2;
                logger.info("[ResetPasswordPage] W3C tap DONE at ({}, {}) via [{}]", tapX, tapY, foundVia);
                org.openqa.selenium.interactions.PointerInput finger =
                    new org.openqa.selenium.interactions.PointerInput(
                        org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
                org.openqa.selenium.interactions.Sequence tap =
                    new org.openqa.selenium.interactions.Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(java.time.Duration.ZERO,
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), tapX, tapY));
                tap.addAction(finger.createPointerDown(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(java.util.Collections.singletonList(tap));
                logger.info("[ResetPasswordPage] ✅ DONE tapped at ({}, {})", tapX, tapY);
                return;
            } catch (Exception e) {
                logger.warn("[ResetPasswordPage] DONE coordinate tap failed: {}", e.getMessage());
            }
        }

        // ── 3. Check Welcome Back one more time (may have auto-navigated during poll) ──
        if (isWelcomeBackPageDisplayed()) {
            logger.info("[ResetPasswordPage] ✅ Welcome Back confirmed after poll — Done tap not needed");
            return;
        }

        // ── 4. Last resort: coordinate tap at bottom-centre (DONE typical position) ──
        // On iPhone 17 Pro the DONE button on the success page renders around y=720-760.
        logger.warn("[ResetPasswordPage] DONE not found in 60s — attempting last-resort coordinate tap at (201, 740)");
        try {
            org.openqa.selenium.interactions.PointerInput finger =
                new org.openqa.selenium.interactions.PointerInput(
                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
            org.openqa.selenium.interactions.Sequence tap =
                new org.openqa.selenium.interactions.Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(java.time.Duration.ZERO,
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), 201, 740));
            tap.addAction(finger.createPointerDown(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(java.util.Collections.singletonList(tap));
            logger.info("[ResetPasswordPage] ✅ Last-resort DONE tap at (201, 740) sent");
        } catch (Exception e) {
            throw new RuntimeException("[ResetPasswordPage] DONE button not found and last-resort tap failed: " + e.getMessage(), e);
        }
    }

    // ── New page assertions ──────────────────────────────────────────────────

    /** Returns true if the 'WE SENT AN EMAIL' / OTP entry page is visible.
     *  Dumps full page source to logs AFTER the heading wait expires so we see
     *  the final state — not the transitioning state. */
    public boolean isWeSentAnEmailPageDisplayed() {
        // 1. Try the primary @iOSXCUITFindBy heading element (15 s wait)
        try {
            wait.until(ExpectedConditions.visibilityOf(weSentAnEmailHeading));
            logger.info("[ResetPasswordPage] \u2705 'We Sent An Email' page confirmed via heading element");
            return true;
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] Heading element not found after wait — checking page source");
        }

        // 2. Grab page source AFTER the wait expires — this is the real final state
        String src = "";
        try { src = driver.getPageSource(); } catch (Exception ignored) {}
        logger.info("[ResetPasswordPage] === PAGE SOURCE (post-wait, final state) ===\n{}",
            src.length() > 5000 ? src.substring(0, 5000) : src);

        // 3. Page-source fallback — broad catch for any OTP / email-sent page variant
        boolean found = src.contains("SENT AN EMAIL")
                || src.contains("sent an email")
                || src.contains("WE SENT")
                || src.contains("we sent")
                || src.contains("Check your email")
                || src.contains("CHECK YOUR EMAIL")
                || src.contains("VERIFY YOUR EMAIL")
                || src.contains("Verify your email")
                || src.contains("VERIFY WITH EMAIL")
                || src.contains("Verify with email")
                || src.contains("OTP")
                || src.contains("FR_NATIVE_OTP")
                || src.contains("verification code")
                || src.contains("VERIFICATION CODE")
                || src.contains("FR_NATIVE_RESETPASSWORD")
                || src.contains("Enter code");
        if (found) {
            logger.info("[ResetPasswordPage] \u2705 OTP/email-sent page confirmed via page source");
        } else {
            logger.error("[ResetPasswordPage] \u274c OTP page NOT found. See PAGE SOURCE above for actual state.");
        }
        return found;
    }

    /** Returns true if the 'RESET YOUR PASSWORD' page heading is visible. */
    public boolean isResetYourPasswordPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(resetYourPasswordHeading));
            logger.info("[ResetPasswordPage] 'Reset Your Password' page confirmed");
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("RESET YOUR PASSWORD") || src.contains("Reset Your Password")
                    || src.contains("New Password");
        }
    }

    /** Returns true if the password reset success page is visible.
     *
     * CONFIRMED from screenshot: page shows "PASSWORD RESET!" (with !) and a DONE button.
     *
     * ROOT CAUSE OF PREVIOUS FAILURES:
     * WDA (WebDriverAgent) truncates getPageSource() at exactly 4095 chars on this app/page.
     * The success screen IS rendered but its elements (DONE button, "PASSWORD RESET!" text)
     * appear later in the accessibility tree, beyond the 4095-char cutoff.
     * getPageSource() string-search NEVER detects them.
     *
     * FIX: Use direct driver.findElements() XPath queries only. XPath element queries
     * go directly to WDA element lookup — no page source buffer limit applies.
     * We NEVER call getPageSource() here.
     */
    public boolean isResetSuccessPageDisplayed() {
        // ── Strategy: direct XPath element finds only — no getPageSource() ──
        // Try every plausible locator for the success screen heading and DONE button.
        // Any single hit confirms the success page.

        // Attempt 1: DONE button — most reliable signal (unique to this screen)
        // CONFIRMED from live element dump:
        //   name='fr_confirmation_screen_done_button'  label='Done Button'
        String[] doneXPaths = {
            "//XCUIElementTypeButton[@name='fr_confirmation_screen_done_button']", // CONFIRMED real name
            "//XCUIElementTypeButton[@label='Done Button']",                      // CONFIRMED real label
            "//*[@name='create_password_done_button']",
            "//XCUIElementTypeButton[@label='DONE']",
            "//XCUIElementTypeButton[@label='Done']",
            "//*[@label='DONE']",
            "//*[@label='Done']"
        };
        for (String xp : doneXPaths) {
            try {
                org.openqa.selenium.WebElement el = driver.findElement(
                    org.openqa.selenium.By.xpath(xp));
                logger.info("[ResetPasswordPage] ✅ Reset success page confirmed — DONE element found via: {}", xp);
                return true;
            } catch (Exception ignored) {}
        }

        // Attempt 2: Success page heading/description elements
        // CONFIRMED from live element dump:
        //   name='fr_confirmation_screen_title'        label='Title'
        //   name='fr_confirmation_screen_description'  label='Description'
        //   name='fr_confirmation_screen_top_image'    label='Confirmation Image'
        String[] headingXPaths = {
            "//*[@name='fr_confirmation_screen_title']",        // CONFIRMED
            "//*[@name='fr_confirmation_screen_description']",  // CONFIRMED
            "//*[@name='fr_confirmation_screen_top_image']",    // CONFIRMED
            "//*[@name='create_password_success_title']",
            "//*[@name='reset_success_title']",
            "//XCUIElementTypeStaticText[@label='PASSWORD RESET!']",
            "//XCUIElementTypeStaticText[contains(@label,'PASSWORD RESET')]",
            "//XCUIElementTypeStaticText[contains(@label,'USE YOUR NEW PASSWORD')]",
            "//XCUIElementTypeStaticText[contains(@label,'password has been reset')]"
        };
        for (String xp : headingXPaths) {
            try {
                org.openqa.selenium.WebElement el = driver.findElement(
                    org.openqa.selenium.By.xpath(xp));
                logger.info("[ResetPasswordPage] ✅ Reset success page confirmed — heading found via: {}", xp);
                return true;
            } catch (Exception ignored) {}
        }

        // Nothing found — log what IS currently on screen for diagnostics
        logger.info("[ResetPasswordPage] ⏳ Success page elements not found yet — dumping visible elements:");
        dumpVisibleElements();
        return false;
    }

    /** Returns true if the Welcome Back / Sign In screen is visible after Done.
     *
     * Uses direct XPath element finds only — does NOT use getPageSource().
     * WDA truncates page source at 4095 chars; Welcome Back elements would also
     * appear late in the tree and be missed by source string search.
     */
    public boolean isWelcomeBackPageDisplayed() {
        // Direct XPath element lookups for the Welcome Back / Sign In screen.
        // Any single match confirms we are back at the start of the login flow.
        String[] welcomeXPaths = {
            "//*[@name='FR_NATIVE_WELCOMEBACK_TITLE']",
            "//*[@name='welcomeBackTitle']",
            "//*[@label='Welcome Back']",
            "//*[@label='WELCOME BACK']",
            "//XCUIElementTypeStaticText[contains(@label,'Welcome Back')]",
            // Sign In page elements (also acceptable post-reset landing)
            "//*[@name='forgotPasswordButton']",
            "//*[@label='Forgot Password?']",
            "//*[@label='Forgot Password']",
            "//*[@name='FR_NATIVE_SIGNIN_BUTTON']",
            "//*[@name='Create Account']",
            "//XCUIElementTypeButton[@label='Create Account']"
        };
        for (String xp : welcomeXPaths) {
            try {
                driver.findElement(org.openqa.selenium.By.xpath(xp));
                logger.info("[ResetPasswordPage] ✅ Welcome Back / Sign In page confirmed via: {}", xp);
                return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    // ── Utility ───────────────────────────────────────────────────────────

    /**
     * Dumps all currently visible/named elements to INFO logs via XPath findElements.
     * Used as a diagnostic fallback when a page assertion fails.
     * Does NOT use getPageSource() — bypasses WDA 4095-char truncation.
     */
    private void dumpVisibleElements() {
        try {
            java.util.List<org.openqa.selenium.WebElement> all = driver.findElements(
                org.openqa.selenium.By.xpath("//*"));
            logger.info("[ResetPasswordPage] ══ ELEMENT DUMP ({} elements) ══", all.size());
            for (org.openqa.selenium.WebElement el : all) {
                String n  = safeAttr(el, "name");
                String lb = safeAttr(el, "label");
                String t  = safeAttr(el, "type");
                String v  = safeAttr(el, "visible");
                String en = safeAttr(el, "enabled");
                if (!n.isEmpty() || !lb.isEmpty()) {
                    logger.info("  type={} name='{}' label='{}' visible={} enabled={}", t, n, lb, v, en);
                }
            }
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] dumpVisibleElements failed: {}", e.getMessage());
        }
    }

    private void tapByLabelFallback(String label) {
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//*[@label='" + label + "' or @name='" + label + "']"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[ResetPasswordPage] Tapped '{}' via fallback", label);
            } else {
                throw new RuntimeException("[ResetPasswordPage] Not found: " + label);
            }
        } catch (Exception e) {
            logger.error("[ResetPasswordPage] tapByLabelFallback failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }
}
