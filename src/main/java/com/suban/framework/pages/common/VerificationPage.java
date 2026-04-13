package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * VerificationPage — Handles OTP / verification code entry screens.
 * Covers device verification, phone/SMS verification, email verification,
 * unverified mobile warnings, and Verify-with-Email flow during Sign In.
 */
public class VerificationPage extends BasePage {

    // ── Screen title / prompt ──────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Verify')"
            + " or contains(@label,'Verification') or contains(@label,'VERIFICATION REQUIRED')"
            + " or contains(@label,'new device') or contains(@label,'unrecognized')]")
    private WebElement verificationPromptText;

    // ── OTP / Code input field ─────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@hint,'code')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='FR_NATIVE_OTP_TEXTFIELD'"
            + " or @name='otpInput' or @name='verificationCodeInput'"
            + " or contains(@label,'Enter code') or contains(@label,'Verification code')]")
    private WebElement otpInput;

    // ── Send Code button ───────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Send Code' or @name='sendCodeButton'"
            + " or @label='Send' or contains(@label,'Send') or @name='FR_NATIVE_SEND_CODE_BUTTON']")
    private WebElement sendCodeButton;

    // ── Verify with Email option ───────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'email')"
            + " or contains(@label,'Email') or @name='verifyWithEmailButton'"
            + " or contains(@label,'Verify via email')]")
    private WebElement verifyWithEmailButton;

    // ── Verify / Submit button ─────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Verify' or @name='verifyButton'"
            + " or @label='Submit' or @name='FR_NATIVE_OTP_SUBMIT_BUTTON'"
            + " or @label='Continue']")
    private WebElement verifySubmitButton;

    // ── Resend Code button ─────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Resend')"
            + " or contains(@name,'resend') or contains(@label,'Send Again')]")
    private WebElement resendCodeButton;

    // ── Error / lockout message ────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'incorrect')"
            + " or contains(@label,'wrong') or contains(@label,'invalid')"
            + " or contains(@label,'locked') or contains(@label,'too many')"
            + " or contains(@name,'codeErrorMessage')]")
    private WebElement codeErrorMessage;

    // ── Throttle / rate-limit toast ────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'wait')"
            + " or contains(@label,'60 second') or contains(@label,'too many requests')"
            + " or contains(@label,'throttle')]")
    private WebElement throttleMessage;

    // ── Resend confirmation toast ──────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'new code')"
            + " or contains(@label,'sent') or contains(@label,'delivered')"
            + " or contains(@label,'Code sent')]")
    private WebElement resendConfirmationToast;

    // ── Unverified mobile warning screen ──────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'unverified')"
            + " or contains(@label,'Unverified') or contains(@label,'verify your phone')"
            + " or contains(@label,'mobile not verified')]")
    private WebElement unverifiedMobileWarning;

    // ── Continue button (post-verification) ───────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Continue' or @name='continueButton'"
            + " or @label='Done' or @name='doneButton']")
    private WebElement continueButton;

    public VerificationPage(AppiumDriver driver) {
        super(driver);
    }

    // ── Visibility Checks ─────────────────────────────────────────────────

    public boolean isVerificationScreenDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(verificationPromptText));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("VERIFICATION REQUIRED") || src.contains("Verify")
                    || src.contains("new device") || src.contains("unrecognized");
        }
    }

    public boolean isUnverifiedMobileWarningDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(unverifiedMobileWarning));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("unverified") || src.contains("Unverified")
                    || src.contains("verify your phone");
        }
    }

    public boolean isCodeErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(codeErrorMessage));
            logger.info("[VerificationPage] Code error: {}", codeErrorMessage.getText());
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("incorrect") || src.contains("wrong") || src.contains("invalid code")
                    || src.contains("locked") || src.contains("too many");
        }
    }

    public boolean isThrottleMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(throttleMessage));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("wait") || src.contains("60 second") || src.contains("throttle");
        }
    }

    public boolean isResendConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(resendConfirmationToast));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Code sent") || src.contains("new code") || src.contains("delivered");
        }
    }

    // ── Actions ───────────────────────────────────────────────────────────

    public void tapSendCode() {
        logger.info("[VerificationPage] Tapping Send Code button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(sendCodeButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Send Code");
        }
    }

    public void tapVerifyWithEmail() {
        logger.info("[VerificationPage] Tapping Verify with Email");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(verifyWithEmailButton)).click();
        } catch (Exception e) {
            // Search for any email-related option
            List<WebElement> els = driver.findElements(By.xpath(
                "//*[contains(@label,'email') or contains(@label,'Email')"
                + " or contains(@name,'email') or contains(@name,'Email')]"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[VerificationPage] Tapped email option via search");
            } else {
                throw new RuntimeException("[VerificationPage] Verify with Email button not found");
            }
        }
    }

    public void enterOtpCode(String code) {
        logger.info("[VerificationPage] Entering OTP/verification code: {}", code);
        try {
            wait.until(ExpectedConditions.visibilityOf(otpInput)).clear();
            otpInput.sendKeys(code);
        } catch (Exception e) {
            // Fallback: try any visible text field
            List<WebElement> fields = driver.findElements(
                By.xpath("//XCUIElementTypeTextField"));
            if (!fields.isEmpty()) {
                fields.get(0).clear();
                fields.get(0).sendKeys(code);
            } else {
                throw new RuntimeException("[VerificationPage] OTP input not found");
            }
        }
    }

    public void tapVerifySubmit() {
        logger.info("[VerificationPage] Tapping Verify / Submit button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(verifySubmitButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Verify");
            } catch (Exception e2) {
                tapByLabelFallback("Submit");
            }
        }
    }

    public void tapResendCode() {
        logger.info("[VerificationPage] Tapping Resend Code");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(resendCodeButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Resend");
            } catch (Exception e2) {
                tapByLabelFallback("Send Again");
            }
        }
    }

    public void tapContinue() {
        logger.info("[VerificationPage] Tapping Continue");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Continue");
        }
    }

    public String getCodeErrorText() {
        try {
            return codeErrorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────

    private void tapByLabelFallback(String label) {
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//*[@label='" + label + "' or @name='" + label + "']"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[VerificationPage] Tapped '{}' via fallback", label);
            } else {
                throw new RuntimeException("[VerificationPage] Not found: " + label);
            }
        } catch (Exception e) {
            logger.error("[VerificationPage] tapByLabelFallback failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }
}
