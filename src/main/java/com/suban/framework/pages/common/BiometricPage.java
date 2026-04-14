package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * BiometricPage — Handles Biometric Authentication setup and interactions.
 * Covers Face ID / Touch ID enable flow, biometric failure, fallback to password,
 * and session timeout / re-auth scenarios.
 */
public class BiometricPage extends BasePage {

    // ── Biometric prompt / system dialog ──────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Face ID') or contains(@text,'Touch ID') or contains(@text,'Biometric') or contains(@text,'fingerprint')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Face ID')"
            + " or contains(@label,'Touch ID') or contains(@label,'Biometric')"
            + " or contains(@label,'fingerprint')]")
    private WebElement biometricPromptText;

    // ── Biometric failure message ──────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'failed') or contains(@text,'not recognized') or contains(@text,'Try again')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'failed')"
            + " or contains(@label,'not recognized') or contains(@label,'Try again')"
            + " or contains(@label,'Face ID not recognized') or contains(@name,'biometricError')]")
    private WebElement biometricFailureMessage;

    // ── Fallback to password/PIN prompt ───────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'password') or contains(@text,'PIN') or contains(@text,'Enter Password') or contains(@text,'Use Password')] | //android.widget.TextView[contains(@text,'Use Password')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'password')"
            + " or contains(@label,'PIN') or contains(@label,'Enter Password')"
            + " or contains(@label,'Use Password') or contains(@name,'fallbackPasswordButton')]")
    private WebElement fallbackPasswordButton;

    // ── Password input (fallback) ──────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Password' or @content-desc='fallbackPasswordInput']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='fallbackPasswordInput'"
            + " or @label='Password' or @name='FR_NATIVE_SIGNIN_PASSWORD_TEXTFIELD']")
    private WebElement fallbackPasswordInput;

    // ── Enable Biometric toggle (in Settings) ─────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Switch[contains(@content-desc,'Biometric') or contains(@content-desc,'Face ID') or contains(@content-desc,'Touch ID')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSwitch[contains(@label,'Biometric')"
            + " or contains(@label,'Face ID') or contains(@label,'Touch ID')"
            + " or contains(@name,'biometricToggle')]")
    private WebElement biometricToggle;

    // ── Keep Me Signed In toggle ───────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Switch[contains(@content-desc,'Keep') or contains(@content-desc,'Stay signed in') or contains(@content-desc,'Keep Me Signed')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSwitch[contains(@label,'Keep')"
            + " or contains(@label,'Stay signed in') or contains(@label,'Stay Signed In')"
            + " or contains(@name,'keepSignedInToggle') or contains(@label,'Keep Me Signed')]")
    private WebElement keepSignedInToggle;

    // ── Require authentication setting ────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'Require') or contains(@text,'Authentication')] | //android.widget.TextView[contains(@text,'Require Authentication')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Require')"
            + " or contains(@label,'Authentication') or contains(@name,'requireAuthSetting')]")
    private WebElement requireAuthSetting;

    // ── Biometric active confirmation ──────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Biometric enabled') or contains(@text,'Face ID enabled') or contains(@text,'Biometric active')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Biometric enabled')"
            + " or contains(@label,'Face ID enabled') or contains(@label,'Biometric active')"
            + " or contains(@name,'biometricEnabledConfirmation')]")
    private WebElement biometricActiveConfirmation;

    // ── Re-auth prompt ─────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Re-authenticate') or contains(@text,'re-authentication') or contains(@text,'Authenticate again') or contains(@text,'Verify your identity')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Re-authenticate')"
            + " or contains(@label,'re-authentication') or contains(@label,'Authenticate again')"
            + " or contains(@label,'Verify your identity')]")
    private WebElement reAuthPrompt;

    // ── Confirm change with password field ────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Enter Password' or @hint='Current Password' or @content-desc='confirmPasswordInput']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='confirmPasswordInput'"
            + " or @label='Enter Password' or @label='Current Password']")
    private WebElement confirmChangePasswordInput;

    // ── OK / Confirm button ────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='OK' or @text='Confirm' or @content-desc='confirmButton'] | //android.widget.TextView[@text='OK']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='OK' or @name='okButton'"
            + " or @label='Confirm' or @name='confirmButton']")
    private WebElement confirmButton;

    public BiometricPage(AppiumDriver driver) {
        super(driver);
    }

    // ── Visibility checks ─────────────────────────────────────────────────

    public boolean isBiometricPromptDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(biometricPromptText));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Face ID") || src.contains("Touch ID")
                    || src.contains("Biometric") || src.contains("fingerprint");
        }
    }

    public boolean isBiometricFailureDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(biometricFailureMessage));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("not recognized") || src.contains("failed") || src.contains("Try again");
        }
    }

    public boolean isFallbackPasswordPromptDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(fallbackPasswordButton));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Use Password") || src.contains("Enter Password") || src.contains("PIN");
        }
    }

    public boolean isBiometricToggleEnabled() {
        try {
            String val = biometricToggle.getAttribute("value");
            return "1".equals(val) || "true".equalsIgnoreCase(val);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isKeepSignedInToggleEnabled() {
        try {
            String val = keepSignedInToggle.getAttribute("value");
            return "1".equals(val) || "true".equalsIgnoreCase(val);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReAuthPromptDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(reAuthPrompt));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Re-authenticate") || src.contains("Authenticate again")
                    || src.contains("Verify your identity") || isBiometricPromptDisplayed();
        }
    }

    // ── Actions ───────────────────────────────────────────────────────────

    public void enableBiometricToggle() {
        logger.info("[BiometricPage] Enabling Biometric Authentication toggle");
        try {
            String val = biometricToggle.getAttribute("value");
            if ("0".equals(val) || "false".equalsIgnoreCase(val)) {
                biometricToggle.click();
                logger.info("[BiometricPage] Biometric toggle enabled");
            } else {
                logger.info("[BiometricPage] Biometric toggle already enabled");
            }
        } catch (Exception e) {
            logger.warn("[BiometricPage] Biometric toggle fallback");
            tapByLabelFallback("Biometric");
        }
    }

    public void disableBiometricToggle() {
        logger.info("[BiometricPage] Disabling Biometric Authentication toggle");
        try {
            String val = biometricToggle.getAttribute("value");
            if ("1".equals(val) || "true".equalsIgnoreCase(val)) {
                biometricToggle.click();
                logger.info("[BiometricPage] Biometric toggle disabled");
            } else {
                logger.info("[BiometricPage] Biometric toggle already disabled");
            }
        } catch (Exception e) {
            logger.warn("[BiometricPage] Biometric toggle disable fallback");
        }
    }

    public void enableKeepSignedInToggle() {
        logger.info("[BiometricPage] Enabling Keep Me Signed In toggle");
        try {
            String val = keepSignedInToggle.getAttribute("value");
            if ("0".equals(val) || "false".equalsIgnoreCase(val)) {
                keepSignedInToggle.click();
            }
        } catch (Exception e) {
            tapByLabelFallback("Keep");
        }
    }

    public void disableKeepSignedInToggle() {
        logger.info("[BiometricPage] Disabling Keep Me Signed In toggle");
        try {
            String val = keepSignedInToggle.getAttribute("value");
            if ("1".equals(val) || "true".equalsIgnoreCase(val)) {
                keepSignedInToggle.click();
            }
        } catch (Exception e) {
            logger.warn("[BiometricPage] Keep Signed In toggle disable fallback");
        }
    }

    public void authenticateWithPassword(String password) {
        logger.info("[BiometricPage] Authenticating with password to confirm biometric change");
        try {
            wait.until(ExpectedConditions.visibilityOf(confirmChangePasswordInput)).clear();
            confirmChangePasswordInput.sendKeys(password);
            tapByLabelFallback("Confirm");
        } catch (Exception e) {
            // If a secure text field is visible directly
            List<WebElement> secureFields = driver.findElements(
                By.xpath("//XCUIElementTypeSecureTextField"));
            if (!secureFields.isEmpty()) {
                secureFields.get(0).clear();
                secureFields.get(0).sendKeys(password);
            }
            tapByLabelFallback("OK");
        }
    }

    public void tapFallbackPasswordButton() {
        logger.info("[BiometricPage] Tapping fallback Use Password button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(fallbackPasswordButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Use Password");
            } catch (Exception e2) {
                tapByLabelFallback("Enter Password");
            }
        }
    }

    public void enterFallbackPassword(String password) {
        logger.info("[BiometricPage] Entering fallback password");
        try {
            wait.until(ExpectedConditions.visibilityOf(fallbackPasswordInput)).clear();
            fallbackPasswordInput.sendKeys(password);
        } catch (Exception e) {
            List<WebElement> secureFields = driver.findElements(
                By.xpath("//XCUIElementTypeSecureTextField"));
            if (!secureFields.isEmpty()) {
                secureFields.get(0).clear();
                secureFields.get(0).sendKeys(password);
            }
        }
    }

    public void simulateBiometricSuccess() {
        logger.info("[BiometricPage] Simulating biometric success (simulator: matchingFace / matchingFinger)");
        try {
            // On iOS simulators Face ID can be simulated via Appium TouchID
            ((io.appium.java_client.ios.IOSDriver) driver).performTouchID(true);
            logger.info("[BiometricPage] Biometric success simulated");
        } catch (Exception e) {
            logger.warn("[BiometricPage] Biometric simulation not supported: {}", e.getMessage());
        }
    }

    public void simulateBiometricFailure() {
        logger.info("[BiometricPage] Simulating biometric failure");
        try {
            ((io.appium.java_client.ios.IOSDriver) driver).performTouchID(false);
            logger.info("[BiometricPage] Biometric failure simulated");
        } catch (Exception e) {
            logger.warn("[BiometricPage] Biometric failure simulation not supported: {}", e.getMessage());
        }
    }

    public void setRequireAuthentication(String duration) {
        logger.info("[BiometricPage] Setting Require Authentication to: {}", duration);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(requireAuthSetting)).click();
            Thread.sleep(1000);
            tapByLabelFallback(duration);
        } catch (Exception e) {
            logger.warn("[BiometricPage] Could not set require authentication: {}", e.getMessage());
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────

    private void tapByLabelFallback(String label) {
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//*[@label='" + label + "' or @name='" + label + "']"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[BiometricPage] Tapped '{}' via fallback", label);
            } else {
                throw new RuntimeException("[BiometricPage] Not found: " + label);
            }
        } catch (Exception e) {
            logger.error("[BiometricPage] tapByLabelFallback failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }
}
