package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    public HomePage(AppiumDriver driver) {
        super(driver);
    }

    // Dynamic element access
    public WebElement getSignInButton() {
        return getDynamicElement("id","login_login_btn");
    }

    // Language/Region Selectors
    @FindBy(id = "com.subaru.oneapp.stage:id/login_language_ll")
    @iOSXCUITFindBy(accessibility = "LOGIN_BUTTON_LANGUAGE")
    private WebElement languageSelector;

    @FindBy(id = "com.subaru.oneapp.stage:id/login_language_value_txt")
    @iOSXCUITFindBy(accessibility = "English")
    private WebElement currentLanguageText;

    @FindBy(id = "com.subaru.oneapp.stage:id/login_region_ll")
    @iOSXCUITFindBy(accessibility = "LOGIN_BUTTON_COUNTRY")
    private WebElement regionSelector;

    @FindBy(id = "com.subaru.oneapp.stage:id/login_region_value_txt")
    @iOSXCUITFindBy(accessibility = "USA")
    private WebElement currentRegionText;

    // Language Popup
    @FindBy(id = "com.subaru.oneapp.stage:id/item_language_txt")
    @iOSXCUITFindBy(accessibility = "test")
    private List<WebElement> languageOptions;

    @FindBy(id = "com.subaru.oneapp.stage:id/popup_recyclerview")
    @iOSXCUITFindBy(accessibility = "test")
    private WebElement languagePopupContainer;

    // Verified Links Alert
    @FindBy(id = "com.subaru.oneapp.stage:id/alertTitle")
    @iOSXCUITFindBy(accessibility = "test")
    private WebElement verifiedLinksAlertTitle;

    @FindBy(id = "android:id/button2") // Later button
    @iOSXCUITFindBy(accessibility = "test")
    private WebElement laterButton;

    @FindBy(id = "android:id/button3") // Don't ask again
    @iOSXCUITFindBy(accessibility = "test")
    private WebElement dontAskAgainButton;

    // Main Action Buttons
    @FindBy(id = "com.subaru.oneapp.stage:id/login_login_btn")
    @iOSXCUITFindBy(accessibility = "LOGIN_BUTTON_SIGNIN")
    private WebElement signInButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/login_sign_up_btn")
    @iOSXCUITFindBy(accessibility = "LOGIN_BUTTON_SIGNUP")
    private WebElement registerButton;

    @FindBy(id = "com.subaru.oneapp.stage:id/login_keep_login_txt")
    @iOSXCUITFindBy(accessibility = "LOGIN_BUTTON_KEEP_ME_LOGGED_IN")
    private WebElement keepMeSignedInCheckbox;


    // Language Methods
    public void openLanguageSelection() {
        clickWithLogging(languageSelector, "Language selector");
    }
    public String getCurrentLanguage() {
        return getTextWithLogging(currentLanguageText, "Current language text");
    }
    public void selectLanguage(String language) {
        openLanguageSelection();
        wait.until(ExpectedConditions.visibilityOf(languagePopupContainer));

        languageOptions.stream()
                .filter(option -> option.getText().equalsIgnoreCase(language))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Language not found: {}", language);
                    return new RuntimeException("Language not found: " + language);
                })
                .click();

        handleVerifiedLinksAlert();
    }


    // Region Methods
    public void selectRegion(String region) {
        clickWithLogging(regionSelector, "Region selector");
        handleVerifiedLinksAlert();
    }

    // Alert Handling
    /**
     * Dismisses the "Verified Links" custom dialog that appears on Android after app launch.
     * This is NOT a native Android AlertDialog — it is rendered by the app layer, so
     * android:id/button* IDs do NOT exist. We target the button by its visible text.
     * Clicks "Don't ask me again" so the dialog stops appearing on future launches.
     * Android-only — no-op on iOS.
     */
    public void handleVerifiedLinksAlert() {
        if (!isAndroid()) {
            return;
        }
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(6));

            // Primary: match by exact text (custom dialog, not a native AlertDialog)
            try {
                org.openqa.selenium.WebElement btn = shortWait.until(
                    ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.xpath(
                            "//*[@text=\"Don't ask me again\"] | " +
                            "//*[@content-desc=\"Don't ask me again\"] | " +
                            "//*[contains(@text,'Don') and contains(@text,'ask')]" ))
                );
                btn.click();
                logger.info("Verified Links — clicked 'Don't ask me again' (by text XPath)");
                return;
            } catch (Exception e1) {
                logger.debug("Verified Links dialog not found by text XPath: {}", e1.getMessage());
            }

            logger.info("Verified Links dialog not present or already dismissed");
        } catch (Exception e) {
            logger.info("handleVerifiedLinksAlert — no dialog found: {}", e.getMessage());
        }
    }

    /**
     * Dismisses the "For your security, please sign in using your login and password
     * to authenticate" dialog that appears on Android after the Verified Links popup.
     * This IS a native Android dialog — tries android:id/button1 first, then @text='OK'.
     * Android-only — no-op on iOS.
     */
    public void handleSecurityPopup() {
        if (!isAndroid()) {
            return;
        }
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(6));

            // Primary: @text='OK' XPath — works for both native and custom dialogs
            try {
                org.openqa.selenium.WebElement okByText = shortWait.until(
                    ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.xpath("//*[@text='OK' or @content-desc='OK']"))
                );
                okByText.click();
                logger.info("Security popup — clicked OK (by text XPath)");
                return;
            } catch (Exception e1) {
                logger.debug("Security popup OK by text XPath not found, trying native id");
            }

            // Fallback: native AlertDialog button1 resource-id
            try {
                org.openqa.selenium.WebElement okById = shortWait.until(
                    ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.id("android:id/button1"))
                );
                okById.click();
                logger.info("Security popup — clicked OK (by android:id/button1)");
                return;
            } catch (Exception e2) {
                logger.debug("Security popup OK by native id not found either");
            }

            logger.info("Security popup not present or already dismissed");
        } catch (Exception e) {
            logger.info("handleSecurityPopup — no dialog found: {}", e.getMessage());
        }
    }

    public boolean isVerifiedLinksAlertDisplayed() {
        try {
            return isElementDisplayed(verifiedLinksAlertTitle, "Verified Links alert");
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Dismisses any pending iOS system alerts (e.g. notifications, location).
     * autoAcceptAlerts=true on the driver handles these automatically, but
     * calling this explicitly before key interactions is a safety net.
     */
    public void dismissSystemAlerts() {
        try {
            driver.switchTo().alert().accept();
            logger.info("Dismissed a system alert via switchTo().alert().accept()");
            // Accept any additional stacked alerts
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException e) {
            // No alert present — normal, nothing to dismiss
        } catch (Exception e) {
            logger.debug("dismissSystemAlerts: {}", e.getMessage());
        }
    }

    public void clickSignIn() {
        // NOTE: dismissSystemAlerts() is intentionally NOT called here.
        // The Verified Links and Security popups are custom app-rendered dialogs
        // and must be handled by clicking their visible text buttons — NOT via
        // driver.switchTo().alert() which only works for native WebView alerts.
        if (isAndroid()) {
            handleVerifiedLinksAlert();
            handleSecurityPopup();
        }
        clickWithLogging(signInButton, "Sign In button");
    }

    public void clickRegister() {
        clickWithLogging(registerButton, "Register button");
    }

    public void toggleKeepMeSignedIn(boolean shouldCheck) {
        if (keepMeSignedInCheckbox.isSelected() != shouldCheck) {
            clickWithLogging(keepMeSignedInCheckbox, "Keep me signed in checkbox");
        }
    }

}
