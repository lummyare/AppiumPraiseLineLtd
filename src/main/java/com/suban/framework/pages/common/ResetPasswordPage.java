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
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='recoveryEmailInput'"
            + " or @label='Email' or @name='FR_NATIVE_RECOVERY_EMAIL_TEXTFIELD'"
            + " or @label='Email address']")
    private WebElement recoveryEmailInput;

    // ── SMS option for reset ───────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'SMS')"
            + " or contains(@label,'Text') or contains(@label,'Phone')"
            + " or contains(@name,'smsOption') or contains(@label,'Send SMS')]")
    private WebElement smsVerificationOption;

    // ── Phone input for SMS reset ──────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='recoveryPhoneInput'"
            + " or @label='Phone Number' or @name='FR_NATIVE_RECOVERY_PHONE_TEXTFIELD']")
    private WebElement recoveryPhoneInput;

    // ── Submit / Send recovery button ──────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Submit' or @name='submitButton'"
            + " or @label='Send' or @name='sendButton' or @label='Continue'"
            + " or @name='FR_NATIVE_RECOVERY_SUBMIT_BUTTON']")
    private WebElement submitRecoveryButton;

    // ── Reset link confirmation text ───────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'sent')"
            + " or contains(@label,'reset link') or contains(@label,'Check your email')"
            + " or contains(@label,'recovery')]")
    private WebElement resetLinkConfirmationText;

    // ── SMS code confirmation text ─────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'SMS')"
            + " or contains(@label,'text message') or contains(@label,'verification code')]")
    private WebElement smsCodeConfirmationText;

    // ── New password input ─────────────────────────────────────────────────
    // Real element: label='NEW PASSWORD' (confirmed visible in screenshot)
    // FR_NATIVE pattern follows the app's naming convention
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@label='NEW PASSWORD'"
            + " or @name='FR_NATIVE_RESETPASSWORD_NEWPASSWORD_TEXTFIELD'"
            + " or @name='FR_NATIVE_NEW_PASSWORD_TEXTFIELD'"
            + " or @label='New Password' or @name='newPasswordInput']")
    private WebElement newPasswordInput;

    // ── Confirm new password input ─────────────────────────────────────────
    // Real element: label='CONFIRM PASSWORD' (confirmed visible in screenshot)
    // FR_NATIVE pattern follows the app's naming convention
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@label='CONFIRM PASSWORD'"
            + " or @name='FR_NATIVE_RESETPASSWORD_CONFIRMPASSWORD_TEXTFIELD'"
            + " or @name='FR_NATIVE_CONFIRM_PASSWORD_TEXTFIELD'"
            + " or @label='Confirm New Password' or @label='Confirm Password'"
            + " or @name='confirmNewPasswordInput']")
    private WebElement confirmNewPasswordInput;

    // ── Save / Update password button ──────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Save' or @name='saveButton'"
            + " or @label='Update Password' or @name='updatePasswordButton'"
            + " or @label='Confirm']")
    private WebElement savePasswordButton;

    // ── Password updated confirmation ──────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'updated')"
            + " or contains(@label,'changed') or contains(@label,'Password Updated')"
            + " or contains(@label,'success') or contains(@name,'passwordUpdatedBanner')]")
    private WebElement passwordUpdatedConfirmation;

    // ── Error messages ─────────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'expired')"
            + " or contains(@label,'invalid') or contains(@label,'not match')"
            + " or contains(@label,'weak') or contains(@label,'error')"
            + " or contains(@name,'errorMessage')]")
    private WebElement resetErrorMessage;

    // ── SMS verification code input ────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='6-digit code'"
            + " or @name='smsCodeInput' or @name='FR_NATIVE_OTP_TEXTFIELD'"
            + " or @label='Verification Code' or @label='6-digit code'"
            + " or contains(@label,'Enter code')]")
    private WebElement smsCodeInput;

    // ── Verify button for SMS code / OTP page ─────────────────────────────
    // Real element confirmed from OB_E2E_007 dump:
    //   name='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON'  label='VERIFY'
    @iOSXCUITFindBy(xpath = "//*[@name='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON'"
            + " or @label='VERIFY' or @name='VERIFY'"
            + " or @label='Verify' or @name='verifyButton'"
            + " or @label='Submit' or @name='FR_NATIVE_OTP_SUBMIT_BUTTON']")
    private WebElement verifyCodeButton;

    // ── Reset It button (on sign-in page, next to email field) ────────────
    // FR_NATIVE_ENTER_PASSWORD_PROMPT_TEXTVIEW is the outer tappable link container.
    // name='RESET IT' is its inner text span — tapping it alone does NOT navigate.
    // Always tap the outer container first.
    @iOSXCUITFindBy(xpath = "//*[@name='FR_NATIVE_ENTER_PASSWORD_PROMPT_TEXTVIEW'"
            + " or @name='DON\u2019T REMEMBER YOUR PASSWORD? RESET IT'"
            + " or @label='Reset it' or @label='Reset It'"
            + " or @name='resetItButton' or @name='FR_NATIVE_FORGOT_PASSWORD_RESET_IT_BUTTON']")
    private WebElement resetItButton;

    // ── OTP / verification code input (We Sent An Email page) ────────────
    // Real element confirmed from OB_E2E_007 dump:
    //   name='6-digit code'  label='6-digit code'
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='6-digit code'"
            + " or @label='6-digit code' or @name='FR_NATIVE_OTP_TEXTFIELD'"
            + " or @name='otpInput' or @name='verificationCodeInput'"
            + " or contains(@label,'code') or contains(@label,'Code')"
            + " or contains(@label,'Enter') or contains(@placeholder,'code')]"
            + " | //XCUIElementTypeSecureTextField[contains(@label,'code')]")
    private WebElement otpCodeInput;

    // ── Reset Password button (Reset Your Password page) ──────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Reset Password'"
            + " or @name='resetPasswordButton' or @label='Reset password'"
            + " or @name='FR_NATIVE_RESET_PASSWORD_BUTTON' or @label='Save'"
            + " or @label='Update Password']")
    private WebElement resetPasswordButton;

    // ── Done button (Password Reset success page) ──────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Done' or @name='doneButton'"
            + " or @name='FR_NATIVE_RESET_SUCCESS_DONE_BUTTON']")
    private WebElement doneButton;

    // ── We Sent An Email heading (page assertion) ──────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'WE SENT AN EMAIL')"
            + " or contains(@label,'We Sent An Email') or contains(@label,'we sent')"
            + " or contains(@label,'Check your email') or contains(@label,'sent an email')"
            + " or contains(@name,'sentEmailTitle')]")
    private WebElement weSentAnEmailHeading;

    // ── Reset Your Password heading (page assertion) ───────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'RESET YOUR PASSWORD')"
            + " or contains(@label,'Reset Your Password') or contains(@label,'New Password')"
            + " or contains(@name,'resetPasswordTitle')]")
    private WebElement resetYourPasswordHeading;

    // ── Password Reset success heading ────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'PASSWORD RESET')"
            + " or contains(@label,'Password Reset') or contains(@label,'Successfully Reset')"
            + " or contains(@label,'Password has been reset') or contains(@label,'success')"
            + " or contains(@name,'resetSuccessTitle')]")
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

        // The CONFIRM PASSWORD field is ALWAYS the second SecureTextField on this page.
        String[] confirmXPaths = {
            "(//XCUIElementTypeSecureTextField)[2]",                                     // positional — most reliable
            "//XCUIElementTypeSecureTextField[@label='CONFIRM PASSWORD']",               // confirmed from screenshot
            "//XCUIElementTypeSecureTextField[@name='FR_NATIVE_RESETPASSWORD_CONFIRMPASSWORD_TEXTFIELD']",
            "//XCUIElementTypeSecureTextField[@name='FR_NATIVE_CONFIRM_PASSWORD_TEXTFIELD']",
            "//XCUIElementTypeSecureTextField[@label='Confirm New Password']",
            "//XCUIElementTypeSecureTextField[@label='Confirm Password']",
            "//XCUIElementTypeSecureTextField[@name='confirmNewPasswordInput']"
        };

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
     * IMPORTANT: keyboard may still be open from confirm-password entry.
     * Always dismiss it first or the button is obscured / not clickable.
     */
    public void tapResetPasswordButton() {
        logger.info("[ResetPasswordPage] Dismissing keyboard before tapping Reset Password");

        // ── 1. Dismiss keyboard first ────────────────────────────────────────
        dismissKeyboardSilently("tapResetPasswordButton");

        logger.info("[ResetPasswordPage] Tapping Reset Password button");

        // ── 2. Primary @iOSXCUITFindBy locator ──────────────────────────────
        try {
            wait.until(ExpectedConditions.elementToBeClickable(resetPasswordButton)).click();
            logger.info("[ResetPasswordPage] ✅ Reset Password tapped via primary locator");
            return;
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] Reset Password primary failed: {}", e.getMessage());
        }

        // ── 3. Label fallbacks ──────────────────────────────────────────
        for (String label : new String[]{ "RESET PASSWORD", "Reset Password", "Reset password", "Save", "Update Password" }) {
            try {
                tapByLabelFallback(label);
                logger.info("[ResetPasswordPage] ✅ Reset Password tapped via label fallback '{}'", label);
                return;
            } catch (Exception ignored) {}
        }

        // ── 4. Coordinate-tap fallback ─────────────────────────────────────
        logger.warn("[ResetPasswordPage] All label fallbacks failed — trying coordinate tap on Reset Password");
        try {
            org.openqa.selenium.WebElement btn = null;
            String[] locators = {
                "//*[@name='FR_NATIVE_RESET_PASSWORD_BUTTON']",
                "//*[@label='RESET PASSWORD']",
                "//*[@label='Reset Password']",
                "//XCUIElementTypeButton[contains(@label,'RESET')]",
                "//XCUIElementTypeButton[contains(@label,'Reset')]"
            };
            for (String xp : locators) {
                try {
                    btn = driver.findElement(org.openqa.selenium.By.xpath(xp));
                    logger.info("[ResetPasswordPage] Reset Password button found via: {}", xp);
                    break;
                } catch (Exception ignored) {}
            }
            if (btn != null) {
                org.openqa.selenium.Point  loc = btn.getLocation();
                org.openqa.selenium.Dimension dim = btn.getSize();
                int tapX = loc.getX() + dim.getWidth()  / 2;
                int tapY = loc.getY() + dim.getHeight() / 2;
                logger.info("[ResetPasswordPage] Coordinate tap on Reset Password at ({}, {})", tapX, tapY);
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
                logger.info("[ResetPasswordPage] ✅ Reset Password coordinate tap completed");
                Thread.sleep(1000);
                return;
            }
            throw new RuntimeException("[ResetPasswordPage] Reset Password button not found by any locator");
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException("[ResetPasswordPage] tapResetPasswordButton coordinate fallback failed: " + e.getMessage(), e);
        }
    }

    /** Taps the 'Done' button on the password reset success page. */
    public void tapDoneButton() {
        logger.info("[ResetPasswordPage] Tapping Done button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(doneButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Done");
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

    /** Returns true if the password reset success page is visible. */
    public boolean isResetSuccessPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(resetSuccessHeading));
            logger.info("[ResetPasswordPage] Reset success page confirmed");
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("PASSWORD RESET") || src.contains("Password Reset")
                    || src.contains("Successfully Reset") || src.contains("password has been reset");
        }
    }

    /** Returns true if the Welcome Back / Welcome screen is visible after Done. */
    public boolean isWelcomeBackPageDisplayed() {
        try {
            String src = driver.getPageSource();
            return src.contains("Welcome Back") || src.contains("WELCOME BACK")
                    || src.contains("Sign In") || src.contains("FR_NATIVE_SIGNIN")
                    || src.contains("Create Account");
        } catch (Exception e) {
            return false;
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────

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
