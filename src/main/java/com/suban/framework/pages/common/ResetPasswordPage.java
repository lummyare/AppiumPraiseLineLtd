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
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='newPasswordInput'"
            + " or @label='New Password' or @name='FR_NATIVE_NEW_PASSWORD_TEXTFIELD']")
    private WebElement newPasswordInput;

    // ── Confirm new password input ─────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='confirmNewPasswordInput'"
            + " or @label='Confirm New Password' or @label='Confirm Password'"
            + " or @name='FR_NATIVE_CONFIRM_PASSWORD_TEXTFIELD']")
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
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='smsCodeInput'"
            + " or @name='FR_NATIVE_OTP_TEXTFIELD' or @label='Verification Code'"
            + " or contains(@label,'Enter code')]")
    private WebElement smsCodeInput;

    // ── Verify button for SMS code / OTP page ─────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Verify' or @name='verifyButton'"
            + " or @label='Submit' or @name='FR_NATIVE_OTP_SUBMIT_BUTTON']")
    private WebElement verifyCodeButton;

    // ── Reset It button (on sign-in page, next to email field) ────────────
    // Promoted from smart-discovery run: actual element is name='RESET IT'
    @iOSXCUITFindBy(xpath = "//*[@name='RESET IT' or @label='RESET IT'"
            + " or @name='FR_NATIVE_ENTER_PASSWORD_PROMPT_TEXTVIEW'"
            + " or @label='Reset it' or @label='Reset It'"
            + " or @name='resetItButton' or @name='FR_NATIVE_FORGOT_PASSWORD_RESET_IT_BUTTON']")
    private WebElement resetItButton;

    // ── OTP / verification code input (We Sent An Email page) ────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='FR_NATIVE_OTP_TEXTFIELD'"
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
        try {
            wait.until(ExpectedConditions.visibilityOf(newPasswordInput)).clear();
            newPasswordInput.sendKeys(password);
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] New password input fallback");
        }
    }

    public void enterConfirmNewPassword(String password) {
        logger.info("[ResetPasswordPage] Entering confirm new password");
        try {
            wait.until(ExpectedConditions.visibilityOf(confirmNewPasswordInput)).clear();
            confirmNewPasswordInput.sendKeys(password);
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] Confirm password input fallback");
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
     * Taps the "Reset It" / "Forgot Password" button on the password page.
     *
     * Strategy (in order):
     * 1. Try the hard-coded accessibility ID locator.
     * 2. Try a broad XCUIElementTypeButton xpath scan — score every button by
     *    how closely its name/label/value matches known reset-related keywords.
     *    Tap the highest-scoring candidate, log its exact name+label+value so
     *    the winning locator can be promoted to the @iOSXCUITFindBy annotation.
     * 3. Last resort: tap any button whose text contains "forgot" (case-insensitive).
     *
     * The discovered locator is stored in {@code discoveredResetItLocator} so that
     * subsequent calls in the same session skip straight to the winner.
     */
    private static volatile String discoveredResetItLocator = null; // session-level cache

    public void tapResetIt() {
        logger.info("[ResetPasswordPage] Tapping Reset It / Forgot Password button");

        // ── 0. Use cached winning locator from a previous call in this session ──
        if (discoveredResetItLocator != null) {
            logger.info("[ResetPasswordPage] Using cached locator: {}", discoveredResetItLocator);
            try {
                org.openqa.selenium.WebElement cached = driver.findElement(
                    org.openqa.selenium.By.xpath(discoveredResetItLocator));
                wait.until(ExpectedConditions.elementToBeClickable(cached)).click();
                logger.info("[ResetPasswordPage] Cached locator succeeded");
                return;
            } catch (Exception e) {
                logger.warn("[ResetPasswordPage] Cached locator failed, re-discovering: {}", e.getMessage());
                discoveredResetItLocator = null;
            }
        }

        // ── 1. Primary @iOSXCUITFindBy locator ────────────────────────────────
        try {
            wait.until(ExpectedConditions.elementToBeClickable(resetItButton)).click();
            logger.info("[ResetPasswordPage] Primary locator succeeded");
            return;
        } catch (Exception e) {
            logger.warn("[ResetPasswordPage] Primary locator failed — starting element discovery");
        }

        // ── 2. Smart page scan: dump ALL element types, score by keyword relevance ──
        java.util.List<String> keywords = java.util.Arrays.asList(
            "reset it", "reset", "forgot", "forgot password", "forgot your password",
            "trouble", "can't sign in", "can't login", "recover", "recovery",
            "fr_native_forgot", "fr_native_reset", "forgotpassword", "resetpassword"
        );

        try {
            // Scroll up slightly first — the Forgot Password link is often below the fold
            // Uses W3C PointerInput (java-client 10 / Selenium 4) — no deprecated TouchAction
            logger.info("[ResetPasswordPage] Scrolling up to reveal off-screen elements before scanning");
            try {
                org.openqa.selenium.Dimension size = driver.manage().window().getSize();
                int startX = size.getWidth() / 2;
                int startY = (int) (size.getHeight() * 0.75);
                int endY   = (int) (size.getHeight() * 0.35);
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
                    java.time.Duration.ofMillis(600),
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, endY));
                swipe.addAction(finger.createPointerUp(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(java.util.Collections.singletonList(swipe));
                Thread.sleep(600);
            } catch (Exception scrollErr) {
                logger.warn("[ResetPasswordPage] Scroll attempt failed (non-fatal): {}", scrollErr.getMessage());
            }

            // Scan ALL element types — Button, StaticText, Link, Other, Any
            java.util.List<org.openqa.selenium.WebElement> allButtons = driver.findElements(
                org.openqa.selenium.By.xpath(
                    "//XCUIElementTypeButton" +
                    " | //XCUIElementTypeStaticText" +
                    " | //XCUIElementTypeLink" +
                    " | //XCUIElementTypeOther" +
                    " | //XCUIElementTypeAny[@name != '' or @label != '']"));

            logger.info("[ResetPasswordPage] Found {} elements (all types) — scanning for Reset It", allButtons.size());

            org.openqa.selenium.WebElement bestMatch = null;
            int bestScore = 0;
            String bestDesc = "";

            for (org.openqa.selenium.WebElement el : allButtons) {
                String name  = safeAttr(el, "name");
                String label = safeAttr(el, "label");
                String value = safeAttr(el, "value");
                String text  = "";
                try { text = el.getText(); } catch (Exception ignored) {}

                // Combine all identifiers into one lower-case string for scoring
                String combined = (name + " " + label + " " + value + " " + text).toLowerCase().trim();
                if (combined.isBlank()) continue;

                // Log every element for diagnosis
                logger.error("[ResetPasswordPage] Element — name='{}' label='{}' value='{}' text='{}'",
                    name, label, value, text);

                int score = 0;
                for (String kw : keywords) {
                    String kwLower = kw.toLowerCase();
                    // Score each attribute individually:
                    // 200 pts — name or label is EXACTLY the keyword (the real button)
                    // 50 pts  — name or label CONTAINS the keyword
                    // 10 pts  — value or text contains the keyword (container/label text)
                    if (name.toLowerCase().equals(kwLower))        score += 200;
                    else if (label.toLowerCase().equals(kwLower))  score += 200;
                    else if (name.toLowerCase().contains(kwLower)) score += 50;
                    else if (label.toLowerCase().contains(kwLower))score += 50;
                    if (value.toLowerCase().contains(kwLower))     score += 10;
                    if (text.toLowerCase().contains(kwLower))      score += 10;
                }

                if (score > bestScore) {
                    bestScore = score;
                    bestMatch = el;
                    bestDesc  = String.format("name='%s' label='%s' value='%s' text='%s'",
                                              name, label, value, text);
                }
            }

            if (bestMatch != null && bestScore > 0) {
                logger.info("[ResetPasswordPage] ✅ Best Reset It candidate (score={}) — {}",
                    bestScore, bestDesc);

                // Build and cache a precise xpath for next time using the winning name/label
                String winningName  = safeAttr(bestMatch, "name");
                String winningLabel = safeAttr(bestMatch, "label");
                if (!winningName.isBlank()) {
                    discoveredResetItLocator = "//*[@name='" + winningName + "']";
                } else if (!winningLabel.isBlank()) {
                    discoveredResetItLocator = "//*[@label='" + winningLabel + "']";
                }
                logger.info("[ResetPasswordPage] 📌 Discovered locator (promote to @iOSXCUITFindBy): {}",
                    discoveredResetItLocator);

                wait.until(ExpectedConditions.elementToBeClickable(bestMatch)).click();
                logger.info("[ResetPasswordPage] Smart-discovered element tapped successfully");
                return;
            }

            // ── 3. Log the full page dump if nothing matched, to help diagnosis ──
            logger.error("[ResetPasswordPage] ❌ No Reset It candidate found. Full button dump:");
            for (org.openqa.selenium.WebElement el : allButtons) {
                logger.error("  → name='{}' label='{}' value='{}' text='{}'",
                    safeAttr(el, "name"), safeAttr(el, "label"),
                    safeAttr(el, "value"), tryGetText(el));
            }
            throw new RuntimeException(
                "[ResetPasswordPage] Could not find Reset It / Forgot Password button. " +
                "Check the element dump in the logs above and update the @iOSXCUITFindBy locator.");

        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException("[ResetPasswordPage] tapResetIt discovery failed: " + e.getMessage(), e);
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

    /** Taps the Verify button on the OTP / 'We Sent An Email' page. */
    public void tapVerifyOtp() {
        logger.info("[ResetPasswordPage] Tapping Verify button (OTP page)");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(verifyCodeButton)).click();
        } catch (Exception e) {
            try { tapByLabelFallback("Verify"); } catch (Exception e2) {
                tapByLabelFallback("Submit");
            }
        }
    }

    /** Taps the 'Reset Password' button on the Reset Your Password page. */
    public void tapResetPasswordButton() {
        logger.info("[ResetPasswordPage] Tapping Reset Password button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(resetPasswordButton)).click();
        } catch (Exception e) {
            try { tapByLabelFallback("Reset Password"); } catch (Exception e2) {
                try { tapByLabelFallback("Reset password"); } catch (Exception e3) {
                    tapByLabelFallback("Save");
                }
            }
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

    /** Returns true if the 'WE SENT AN EMAIL' page heading is visible. */
    public boolean isWeSentAnEmailPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(weSentAnEmailHeading));
            logger.info("[ResetPasswordPage] 'We Sent An Email' page confirmed");
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("SENT AN EMAIL") || src.contains("sent an email")
                    || src.contains("Check your email") || src.contains("we sent");
        }
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
