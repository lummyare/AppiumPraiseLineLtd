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
     * Android-only: handles the two popups that appear immediately after app launch.
     *
     * Step 1 — "Verified Links" custom dialog:
     *   Clicks "Don't ask me again" by searching all visible text nodes.
     *   This is NOT a native AlertDialog so android:id/button* do NOT exist.
     *
     * Step 2 — "For your security..." dialog:
     *   Clicks "OK". Waits up to 6s for it to appear after dismissing step 1.
     *
     * No-op on iOS.
     */
    public void handleAndroidLaunchPopups() {
        if (!isAndroid()) {
            return;
        }

        // ── Step 1: Verified Links → click "Don't ask me again" ──
        logger.info("Android: waiting for Verified Links dialog...");
        try {
            WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(8));
            org.openqa.selenium.WebElement dontAsk = w.until(
                ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath(
                        "//*[contains(@text,'Don') and contains(@text,'ask')] | " +
                        "//*[@text=\"Don't ask me again\"] | " +
                        "//*[@content-desc=\"Don't ask me again\"]")
                )
            );
            dontAsk.click();
            logger.info("Android: clicked 'Don't ask me again' on Verified Links dialog");
        } catch (Exception e) {
            logger.info("Android: Verified Links dialog not found (may not appear every run): {}", e.getMessage());
        }

        // ── Step 2: "For your security" → click OK ──
        // Wait up to 6s for this second dialog to appear after dismissing the first
        logger.info("Android: waiting for Security popup...");
        try {
            WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(6));
            org.openqa.selenium.WebElement ok = w.until(
                ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath(
                        "//*[@text='OK'] | //*[@content-desc='OK']")
                )
            );
            ok.click();
            logger.info("Android: clicked OK on Security popup");
        } catch (Exception e) {
            logger.info("Android: Security popup not found: {}", e.getMessage());
        }
    }

    /**
     * @deprecated Use handleAndroidLaunchPopups() for Android popup handling.
     * Kept for backward-compat with any other callers.
     */
    public void handleVerifiedLinksAlert() {
        handleAndroidLaunchPopups();
    }

    /**
     * @deprecated Merged into handleAndroidLaunchPopups().
     * Kept for backward-compat with any other callers.
     */
    public void handleSecurityPopup() {
        // handled inside handleAndroidLaunchPopups() — no-op here
        if (!isAndroid()) return;
        logger.debug("handleSecurityPopup() called — security popup already handled in handleAndroidLaunchPopups()");
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
        // Android: popups (Verified Links + Security) are handled BEFORE this method
        // is called, inside the "I am on the home screen" step via handleAndroidLaunchPopups().
        // Do NOT call any alert/popup handler here — just click Sign In directly.
        // iOS: no popups to handle before Sign In.
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
