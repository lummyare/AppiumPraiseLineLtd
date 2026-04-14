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
     * Dismisses the "Verified Links" dialog that appears on Android after app launch.
     * Clicks "Later" to dismiss. This dialog does not appear on iOS — the method
     * is a no-op when running on iOS.
     */
    public void handleVerifiedLinksAlert() {
        // This popup only appears on Android
        if (!isAndroid()) {
            return;
        }
        try {
            // Wait briefly for the dialog to appear
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Strategy 1: click "Later" by resource-id (android:id/button2)
            try {
                shortWait.until(ExpectedConditions.elementToBeClickable(laterButton));
                clickWithLogging(laterButton, "Verified Links — Later button (by id)");
                return;
            } catch (Exception e1) {
                logger.debug("laterButton by id not found, trying text XPath");
            }

            // Strategy 2: click "Later" by visible text (XPath fallback)
            try {
                org.openqa.selenium.WebElement laterByText = shortWait.until(
                    ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.xpath("//*[@text='Later' or @content-desc='Later']"))
                );
                laterByText.click();
                logger.info("Clicked Verified Links — Later button (by text XPath)");
                return;
            } catch (Exception e2) {
                logger.debug("laterButton by text XPath not found either");
            }

            logger.info("Verified Links alert not present or already dismissed");
        } catch (Exception e) {
            logger.info("Verified Links alert not present: {}", e.getMessage());
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
        dismissSystemAlerts();
        handleVerifiedLinksAlert();
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
