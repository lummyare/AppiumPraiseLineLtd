package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * SettingsPage — Handles Account Settings, Security Settings, Personal Info,
 * Region/Language preferences, and Sign Out interactions.
 */
public class SettingsPage extends BasePage {

    // ── Account / Profile tab (bottom nav) ────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Account') or contains(@text,'Profile') or contains(@text,'My Account')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Account')"
            + " or contains(@label,'Profile') or contains(@name,'accountTab')"
            + " or contains(@name,'profileTab') or contains(@label,'My Account')]")
    private WebElement accountTab;

    // ── Account Settings menu item ─────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Account Settings' or @text='Settings' or @content-desc='accountSettingsButton']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Account Settings' or @name='accountSettingsButton'"
            + " or @label='Settings' or @name='settingsButton']"
            + " | //XCUIElementTypeCell[@label='Account Settings' or @name='accountSettingsCell']")
    private WebElement accountSettingsCell;

    // ── Security Settings item ─────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Security' or @text='Security Settings' or @content-desc='securitySettingsButton']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Security' or @name='securitySettingsButton'"
            + " or @label='Security Settings']"
            + " | //XCUIElementTypeCell[@label='Security' or @label='Security Settings'"
            + " or @name='securitySettingsCell']")
    private WebElement securitySettingsCell;

    // ── Personal Info / Personal Details item ─────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Personal Info' or @text='Personal Details' or @text='Profile' or @content-desc='personalInfoButton']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Personal Info' or @name='personalInfoButton'"
            + " or @label='Personal Details' or @label='Profile']"
            + " | //XCUIElementTypeCell[@label='Personal Info' or @label='Personal Details'"
            + " or @name='personalInfoCell']")
    private WebElement personalInfoCell;

    // ── Preferred Language item ────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Language') or contains(@text,'Preferred Language')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Language')"
            + " or contains(@label,'Preferred Language') or contains(@name,'languageCell')]"
            + " | //XCUIElementTypeCell[contains(@label,'Language') or contains(@label,'Preferred')]")
    private WebElement preferredLanguageCell;

    // ── Language selection options ─────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'English') or contains(@text,'French') or contains(@text,'Spanish') or contains(@text,'Japanese')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'English')"
            + " or contains(@label,'French') or contains(@label,'Spanish')"
            + " or contains(@label,'Japanese')]")
    private WebElement languageOption;

    // ── Save button ────────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Save' or @text='Done' or @content-desc='saveButton'] | //android.widget.TextView[@text='Save']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Save' or @name='saveButton'"
            + " or @label='Done' or @name='doneButton']")
    private WebElement saveButton;

    // ── Sign Out button ────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Sign Out' or @text='Log Out' or @content-desc='signOutButton'] | //android.widget.TextView[@text='Sign Out' or @text='Log Out']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Sign Out' or @name='signOutButton'"
            + " or @label='Log Out' or @name='logOutButton']"
            + " | //XCUIElementTypeCell[@label='Sign Out' or @name='signOutCell']")
    private WebElement signOutButton;

    // ── Sign Out confirmation ──────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Sign Out' or @text='Yes, Sign Out' or @text='Confirm' or @content-desc='confirmSignOutButton'] | //android.widget.TextView[@text='Sign Out']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Sign Out' or @label='Yes, Sign Out'"
            + " or @label='Confirm' or @name='confirmSignOutButton']")
    private WebElement confirmSignOutButton;

    // ── Language update confirmation ───────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'updated') or contains(@text,'saved') or contains(@text,'Language changed')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'updated')"
            + " or contains(@label,'saved') or contains(@label,'Language changed')"
            + " or contains(@name,'languageUpdateConfirmation')]")
    private WebElement languageUpdateConfirmation;

    // ── Email field in Personal Details ───────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Email') and contains(@text,'@')] | //android.widget.LinearLayout[contains(@content-desc,'Email')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeCell[contains(@label,'Email')]"
            + " | //XCUIElementTypeButton[contains(@label,'Email') and contains(@label,'@')]")
    private WebElement emailFieldCell;

    // ── Email input in edit mode ───────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Email' or @hint='Email address' or @content-desc='emailInput']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='emailInput' or @label='Email'"
            + " or @label='Email address']")
    private WebElement emailInput;

    // ── Submit email update button ─────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Submit' or @text='Update' or @text='Save' or @content-desc='submitButton']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Submit' or @name='submitButton'"
            + " or @label='Update' or @label='Save']")
    private WebElement submitEmailButton;

    // ── Email update confirmation ──────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'verification link') or contains(@text,'Email Updated') or contains(@text,'Check your email')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'verification link')"
            + " or contains(@label,'Email Updated') or contains(@label,'Check your email')]")
    private WebElement emailUpdateConfirmation;

    // ── Error messages ────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'invalid') or contains(@text,'same email') or contains(@text,'already registered') or contains(@text,'duplicate') or @content-desc='emailErrorMessage']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'invalid')"
            + " or contains(@label,'same email') or contains(@label,'already registered')"
            + " or contains(@label,'duplicate') or contains(@name,'emailErrorMessage')]")
    private WebElement emailErrorMessage;

    public SettingsPage(AppiumDriver driver) {
        super(driver);
    }

    // ── Navigation ────────────────────────────────────────────────────────

    public void tapAccountTab() {
        logger.info("[SettingsPage] Tapping Account/Profile tab");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(accountTab)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Account");
            } catch (Exception e2) {
                tapByLabelFallback("Profile");
            }
        }
    }

    public void tapAccountSettings() {
        logger.info("[SettingsPage] Tapping Account Settings");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(accountSettingsCell)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Account Settings");
            } catch (Exception e2) {
                tapByLabelFallback("Settings");
            }
        }
    }

    public void tapSecuritySettings() {
        logger.info("[SettingsPage] Tapping Security Settings");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(securitySettingsCell)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Security Settings");
            } catch (Exception e2) {
                tapByLabelFallback("Security");
            }
        }
    }

    public void tapPersonalInfo() {
        logger.info("[SettingsPage] Tapping Personal Info");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(personalInfoCell)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Personal Info");
            } catch (Exception e2) {
                tapByLabelFallback("Personal Details");
            }
        }
    }

    public void tapPreferredLanguage() {
        logger.info("[SettingsPage] Tapping Preferred Language");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(preferredLanguageCell)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Preferred Language");
            } catch (Exception e2) {
                tapByLabelFallback("Language");
            }
        }
    }

    public void selectLanguage(String language) {
        logger.info("[SettingsPage] Selecting language: {}", language);
        try {
            List<WebElement> options = driver.findElements(By.xpath(
                "//*[@label='" + language + "' or @name='" + language + "'"
                + " or contains(@label,'" + language + "')]"));
            if (!options.isEmpty()) {
                options.get(0).click();
                logger.info("[SettingsPage] Selected language: {}", language);
            } else {
                throw new RuntimeException("[SettingsPage] Language not found: " + language);
            }
        } catch (Exception e) {
            logger.error("[SettingsPage] Could not select language '{}': {}", language, e.getMessage());
            throw e;
        }
    }

    public void tapSave() {
        logger.info("[SettingsPage] Tapping Save");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Save");
            } catch (Exception e2) {
                tapByLabelFallback("Done");
            }
        }
    }

    public void tapSignOut() {
        logger.info("[SettingsPage] Tapping Sign Out");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signOutButton)).click();
            Thread.sleep(1000);
            // Confirm if a dialog appears
            try {
                wait.until(ExpectedConditions.elementToBeClickable(confirmSignOutButton)).click();
            } catch (Exception ignored) {
                // No confirmation dialog
            }
        } catch (Exception e) {
            try {
                tapByLabelFallback("Sign Out");
            } catch (Exception e2) {
                tapByLabelFallback("Log Out");
            }
        }
    }

    // ── Email Update ──────────────────────────────────────────────────────

    public void tapEmailField() {
        logger.info("[SettingsPage] Tapping Email field in Personal Details");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(emailFieldCell)).click();
        } catch (Exception e) {
            tapByLabelFallback("Email");
        }
    }

    public void enterNewEmail(String email) {
        logger.info("[SettingsPage] Entering new email: {}", email);
        try {
            wait.until(ExpectedConditions.visibilityOf(emailInput)).clear();
            emailInput.sendKeys(email);
        } catch (Exception e) {
            List<WebElement> fields = driver.findElements(
                By.xpath("//XCUIElementTypeTextField"));
            if (!fields.isEmpty()) {
                fields.get(0).clear();
                fields.get(0).sendKeys(email);
            }
        }
    }

    public void tapSubmitEmailUpdate() {
        logger.info("[SettingsPage] Tapping Submit for email update");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitEmailButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Submit");
            } catch (Exception e2) {
                tapByLabelFallback("Update");
            }
        }
    }

    // ── Assertions ────────────────────────────────────────────────────────

    public boolean isLanguageUpdateConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(languageUpdateConfirmation));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("updated") || src.contains("saved") || src.contains("Language changed");
        }
    }

    public boolean isEmailUpdateConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(emailUpdateConfirmation));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("verification link") || src.contains("Email Updated")
                    || src.contains("Check your email");
        }
    }

    public boolean isEmailErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(emailErrorMessage));
            logger.info("[SettingsPage] Email error: {}", emailErrorMessage.getText());
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("invalid") || src.contains("same email")
                    || src.contains("already registered") || src.contains("duplicate");
        }
    }

    public String getEmailErrorText() {
        try {
            return emailErrorMessage.getText();
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
                logger.info("[SettingsPage] Tapped '{}' via fallback", label);
            } else {
                throw new RuntimeException("[SettingsPage] Not found: " + label);
            }
        } catch (Exception e) {
            logger.error("[SettingsPage] tapByLabelFallback failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }
}
