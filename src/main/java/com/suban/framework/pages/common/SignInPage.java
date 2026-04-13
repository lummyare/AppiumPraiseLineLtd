package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * SignInPage — Covers all interactions on the Sign In screen.
 * Handles email sign-in, phone sign-in, forgot password, error validation,
 * lockout detection, and device verification prompts.
 */
public class SignInPage extends BasePage {

    // ── Email / Username input ─────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Email or Username']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD'"
            + " or @label='Email or phone number' or @name='emailInput' or @name='usernameInput']")
    private WebElement emailInput;

    // ── Password input ─────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Password']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='FR_NATIVE_SIGNIN_PASSWORD_TEXTFIELD'"
            + " or @label='Password' or @name='passwordInput']")
    private WebElement passwordInput;

    // ── Sign In submit button ──────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Sign In']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='FR_NATIVE_SIGNIN_BUTTON'"
            + " or @label='Sign In' or @name='signInSubmitButton']")
    private WebElement signInSubmitButton;

    // ── Phone number toggle ────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Phone')"
            + " or contains(@name,'Phone') or contains(@name,'phone')]"
            + " | //XCUIElementTypeStaticText[contains(@label,'Phone Number')]")
    private WebElement phoneNumberToggle;

    // ── Phone input (visible after toggle) ────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='phoneInput'"
            + " or contains(@label,'Phone') or @name='FR_NATIVE_SIGNIN_PHONE_TEXTFIELD']")
    private WebElement phoneInput;

    // ── Forgot Password link ───────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Forgot Password']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='forgotPasswordButton'"
            + " or @label='Forgot Password?' or @label='Forgot Password']"
            + " | //XCUIElementTypeStaticText[@label='Forgot Password?' or @label='Forgot Password']")
    private WebElement forgotPasswordLink;

    // ── Inline error message ───────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Invalid') or contains(@text,'incorrect')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Invalid')"
            + " or contains(@label,'incorrect') or contains(@label,'wrong')"
            + " or contains(@name,'errorMessage') or contains(@name,'error')]")
    private WebElement inlineErrorMessage;

    // ── Account lockout message ────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'locked')"
            + " or contains(@label,'Locked') or contains(@label,'too many')"
            + " or contains(@name,'lockout') or contains(@label,'temporarily')]")
    private WebElement lockoutMessage;

    // ── Device verification screen prompt ─────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Verify')"
            + " or contains(@label,'verification') or contains(@label,'new device')"
            + " or contains(@label,'unrecognized')]")
    private WebElement deviceVerificationPrompt;

    // ── Verification code input ────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='FR_NATIVE_OTP_TEXTFIELD'"
            + " or @name='otpInput' or @name='verificationCodeInput'"
            + " or contains(@label,'Enter code') or contains(@label,'Verification code')]")
    private WebElement verificationCodeInput;

    // ── Verify button ──────────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='verifyButton' or @label='Verify'"
            + " or @label='Submit' or @name='FR_NATIVE_OTP_SUBMIT_BUTTON']")
    private WebElement verifyButton;

    // ── Resend Code button ─────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Resend')"
            + " or contains(@name,'resend') or contains(@label,'Send Again')]")
    private WebElement resendCodeButton;

    public SignInPage(AppiumDriver driver) {
        super(driver);
    }

    // ── Sign In with Email ─────────────────────────────────────────────────

    public void enterEmail(String email) {
        logger.info("[SignInPage] Entering email: {}", email);
        wait.until(ExpectedConditions.visibilityOf(emailInput)).clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        logger.info("[SignInPage] Entering password");
        WebElement field = null;
        try {
            // Primary: page-factory annotation (FR_NATIVE_SIGNIN_PASSWORD_TEXTFIELD / Password / passwordInput)
            field = wait.until(ExpectedConditions.visibilityOf(passwordInput));
            logger.info("[SignInPage] Password field found via @iOSXCUITFindBy annotation");
        } catch (Exception e) {
            logger.warn("[SignInPage] Annotation locator failed for password — trying direct XPath");
            // Fallback: direct driver.findElement bypasses the page-factory proxy timeout
            String[] passwordXPaths = {
                "//XCUIElementTypeSecureTextField[@name='FR_NATIVE_SIGNIN_PASSWORD_TEXTFIELD']",
                "//XCUIElementTypeSecureTextField[@label='Password']",
                "//XCUIElementTypeSecureTextField[@name='passwordInput']",
                "//XCUIElementTypeSecureTextField",  // any secure text field on screen
            };
            for (String xpath : passwordXPaths) {
                try {
                    java.util.List<WebElement> els = driver.findElements(By.xpath(xpath));
                    if (!els.isEmpty()) {
                        field = els.get(0);
                        logger.info("[SignInPage] Password field found via XPath: {}", xpath);
                        break;
                    }
                } catch (Exception ex) { /* try next */ }
            }
        }
        if (field == null) {
            throw new RuntimeException("[SignInPage] enterPassword: could not locate password field via any strategy");
        }
        field.clear();
        field.sendKeys(password);
        logger.info("[SignInPage] Password entered");
        // Dismiss keyboard using Return key — hideKeyboard() always fails on this app.
        // This is the same pattern used in tapEmailContinue() and must be done here
        // so the SIGN IN button is visible before tapSignInSubmit() is called.
        try {
            field.sendKeys("\n");
            logger.info("[SignInPage] Keyboard dismissed via Return key after password entry");
        } catch (Exception e) {
            logger.debug("[SignInPage] Return key dismiss skipped: {}", e.getMessage());
        }
        try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    public void tapSignInSubmit() {
        logger.info("[SignInPage] Tapping Sign In submit button");
        // Strategy 1: page-factory annotation (FR_NATIVE_SIGNIN_BUTTON / Sign In / signInSubmitButton)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signInSubmitButton)).click();
            logger.info("[SignInPage] Sign In tapped via annotation");
            return;
        } catch (Exception e) {
            logger.warn("[SignInPage] Annotation locator failed for Sign In button — trying direct XPaths");
        }
        // Strategy 2: direct XPath — FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON is the confirmed
        // real accessibility ID on the password entry page (from LoginPage.java)
        String[] signInXPaths = {
            "//XCUIElementTypeButton[@name='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']",
            "//XCUIElementTypeButton[@name='FR_NATIVE_SIGNIN_BUTTON']",
            "//XCUIElementTypeButton[@label='SIGN IN']",
            "//XCUIElementTypeButton[@label='Sign In']",
            "//XCUIElementTypeButton[@name='signInSubmitButton']",
        };
        for (String xpath : signInXPaths) {
            try {
                java.util.List<WebElement> els = driver.findElements(By.xpath(xpath));
                if (!els.isEmpty()) {
                    els.get(0).click();
                    logger.info("[SignInPage] Sign In tapped via XPath: {}", xpath);
                    return;
                }
            } catch (Exception ex) { /* try next */ }
        }
        // Strategy 3: coordinate tap — Sign In button sits below the password field
        // at approximately (201, 750) on iPhone 17 Pro simulator.
        logger.warn("[SignInPage] All XPath strategies failed — falling back to coordinate tap (201, 750)");
        try {
            new io.appium.java_client.TouchAction<>((io.appium.java_client.PerformsTouchActions) driver)
                .tap(io.appium.java_client.touch.offset.PointOption.point(201, 750))
                .perform();
            logger.info("[SignInPage] Sign In tapped via coordinate (201, 750)");
        } catch (Exception coordEx) {
            throw new RuntimeException("[SignInPage] tapSignInSubmit: all strategies exhausted. " +
                "Dump the page source to find the real Sign In button name.", coordEx);
        }
    }

    // ── Phone Number Sign-In ───────────────────────────────────────────────

    public void switchToPhoneNumberMode() {
        logger.info("[SignInPage] Switching to phone number input mode");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(phoneNumberToggle)).click();
        } catch (Exception e) {
            logger.warn("[SignInPage] Phone toggle fallback");
            fallbackTapByLabel("Phone");
        }
    }

    public void enterPhoneNumber(String phone) {
        logger.info("[SignInPage] Entering phone number");
        try {
            wait.until(ExpectedConditions.visibilityOf(phoneInput)).clear();
            phoneInput.sendKeys(phone);
        } catch (Exception e) {
            // If phone input appeared in place of email input
            emailInput.clear();
            emailInput.sendKeys(phone);
        }
    }

    // ── Forgot Password ────────────────────────────────────────────────────


    // ── Continue button (email-first flow) ────────────────────────────────

    /**
     * Dismisses the keyboard and taps the CONTINUE button that appears after
     * entering an email on the Welcome Back / Sign In screen.
     * This navigates the user to the password entry page.
     * Called exclusively by the Reset Password flow — does NOT affect login flows.
     */
    public void tapEmailContinue() {
        logger.info("[SignInPage] Dismissing keyboard and tapping Continue");
        try {
            ((io.appium.java_client.HidesKeyboard) driver).hideKeyboard();
            Thread.sleep(500);
        } catch (Exception e) {
            logger.debug("[SignInPage] hideKeyboard skipped: {}", e.getMessage());
        }
        try {
            org.openqa.selenium.WebElement btn = driver.findElement(
                org.openqa.selenium.By.xpath(
                    "//XCUIElementTypeButton[@name='FR_NATIVE_SIGNIN_CONTINUE_BUTTON'"
                    + " or @label='CONTINUE' or @label='Continue'"
                    + " or @name='continueButton']"));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .elementToBeClickable(btn)).click();
            logger.info("[SignInPage] Tapped Continue successfully");
        } catch (Exception e) {
            logger.warn("[SignInPage] Continue primary tap failed, trying fallback: {}", e.getMessage());
            try { fallbackTapByLabel("CONTINUE"); }
            catch (Exception e2) { fallbackTapByLabel("Continue"); }
        }
    }

    public void tapForgotPassword() {
        logger.info("[SignInPage] Tapping Forgot Password");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
        } catch (Exception e) {
            fallbackTapByLabel("Forgot Password");
        }
    }

    // ── Verification ───────────────────────────────────────────────────────

    public boolean isDeviceVerificationPromptDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(deviceVerificationPrompt));
            return true;
        } catch (Exception e) {
            // Also check by page source content
            String src = driver.getPageSource();
            return src.contains("Verify") && (src.contains("new device") || src.contains("unrecognized")
                    || src.contains("VERIFICATION REQUIRED") || src.contains("verification"));
        }
    }

    public void enterVerificationCode(String code) {
        logger.info("[SignInPage] Entering verification code: {}", code);
        try {
            wait.until(ExpectedConditions.visibilityOf(verificationCodeInput)).clear();
            verificationCodeInput.sendKeys(code);
        } catch (Exception e) {
            // fallback: find any OTP-style text field
            List<WebElement> fields = driver.findElements(By.xpath(
                "//XCUIElementTypeTextField | //XCUIElementTypeSecureTextField"));
            if (!fields.isEmpty()) {
                fields.get(0).clear();
                fields.get(0).sendKeys(code);
            }
        }
    }

    public void tapVerify() {
        logger.info("[SignInPage] Tapping Verify button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(verifyButton)).click();
        } catch (Exception e) {
            fallbackTapByLabel("Verify");
        }
    }

    public void tapResendCode() {
        logger.info("[SignInPage] Tapping Resend Code");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(resendCodeButton)).click();
        } catch (Exception e) {
            fallbackTapByLabel("Resend");
        }
    }

    // ── Error / Lockout Detection ──────────────────────────────────────────

    public boolean isInlineErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(inlineErrorMessage));
            logger.info("[SignInPage] Inline error detected: {}", inlineErrorMessage.getText());
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Invalid") || src.contains("incorrect") || src.contains("wrong password");
        }
    }

    public boolean isLockoutMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(lockoutMessage));
            logger.info("[SignInPage] Account lockout message detected");
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("locked") || src.contains("Locked") || src.contains("too many");
        }
    }

    public boolean isSignInButtonDisabled() {
        try {
            String enabled = signInSubmitButton.getAttribute("enabled");
            return "false".equals(enabled);
        } catch (Exception e) {
            return false;
        }
    }

    public String getInlineErrorText() {
        try {
            return inlineErrorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────

    private void fallbackTapByLabel(String label) {
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//*[@label='" + label + "' or @name='" + label + "']"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[SignInPage] Tapped '{}' via label fallback", label);
            } else {
                throw new RuntimeException("[SignInPage] Element not found with label: " + label);
            }
        } catch (Exception e) {
            logger.error("[SignInPage] Fallback tap failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }
}
