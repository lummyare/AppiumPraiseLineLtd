package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * WelcomePage — The first screen the user sees on app launch.
 * Contains Sign In and Sign Up entry points.
 * Accessibility IDs are discovered via UIExplorer and confirmed from page source.
 */
public class WelcomePage extends BasePage {

    // ── Sign In button on Welcome Back screen ──────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Sign In']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD'"
            + " or @label='Sign In' or @name='signInButton' or @name='Sign In']")
    private WebElement signInButton;

    // ── Sign Up button ──────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Sign Up']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Sign Up' or @name='signUpButton'"
            + " or @name='Sign Up' or @name='createAccountButton']")
    private WebElement signUpButton;

    // ── Social login buttons ────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Apple')"
            + " or contains(@name,'Apple') or contains(@name,'apple')]")
    private WebElement continueWithAppleButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Google')"
            + " or contains(@name,'Google') or contains(@name,'google')]")
    private WebElement continueWithGoogleButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Facebook')"
            + " or contains(@name,'Facebook') or contains(@name,'facebook')]")
    private WebElement continueWithFacebookButton;

    // ── Region/Language screen ──────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Confirm' or @label='Confirm']")
    private WebElement confirmRegionLanguageButton;

    public WelcomePage(AppiumDriver driver) {
        super(driver);
    }

    // ── Actions ────────────────────────────────────────────────────────────

    public void tapSignIn() {
        logger.info("[WelcomePage] Tapping Sign In button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
        } catch (Exception e) {
            logger.warn("[WelcomePage] Explicit sign-in locator failed — scanning page for Sign In button");
            tapByLabelFallback("Sign In");
        }
    }

    public void tapSignUp() {
        logger.info("[WelcomePage] Tapping Sign Up button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signUpButton)).click();
        } catch (Exception e) {
            logger.warn("[WelcomePage] Explicit sign-up locator failed — scanning page for Sign Up button");
            tapByLabelFallback("Sign Up");
        }
    }

    public void tapContinueWithApple() {
        logger.info("[WelcomePage] Tapping Continue with Apple");
        clickWithLogging(continueWithAppleButton, "Continue with Apple");
    }

    public void tapContinueWithGoogle() {
        logger.info("[WelcomePage] Tapping Continue with Google");
        clickWithLogging(continueWithGoogleButton, "Continue with Google");
    }

    public void tapContinueWithFacebook() {
        logger.info("[WelcomePage] Tapping Continue with Facebook");
        clickWithLogging(continueWithFacebookButton, "Continue with Facebook");
    }

    public boolean isSignInButtonDisplayed() {
        return isElementDisplayed(signInButton, "Sign In button");
    }

    public boolean isSignUpButtonDisplayed() {
        return isElementDisplayed(signUpButton, "Sign Up button");
    }

    /**
     * Generic label-based button tap used as fallback when accessibility IDs change.
     * Scans the page source and taps the first element matching the given label.
     */
    private void tapByLabelFallback(String label) {
        try {
            List<WebElement> els = driver.findElements(
                org.openqa.selenium.By.xpath(
                    "//*[@label='" + label + "' or @name='" + label + "']"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[WelcomePage] Tapped '{}' via label fallback", label);
            } else {
                logger.error("[WelcomePage] Could not find '{}' via any strategy", label);
                throw new RuntimeException("Button not found: " + label);
            }
        } catch (Exception e) {
            logger.error("[WelcomePage] tapByLabelFallback failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }

    // ── Region/Language ─────────────────────────────────────────────────────

    public boolean isRegionLanguageScreenDisplayed() {
        try {
            List<WebElement> els = driver.findElements(
                org.openqa.selenium.By.xpath(
                    "//*[contains(@label,'Region') or contains(@label,'Language')"
                    + " or contains(@name,'Region') or contains(@name,'Language')]"));
            return !els.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectRegion(String region) {
        logger.info("[WelcomePage] Selecting region: {}", region);
        tapByLabelFallback(region);
    }

    public void selectLanguage(String language) {
        logger.info("[WelcomePage] Selecting language: {}", language);
        tapByLabelFallback(language);
    }

    public void tapConfirmRegionLanguage() {
        logger.info("[WelcomePage] Confirming region/language selection");
        try {
            confirmRegionLanguageButton.click();
        } catch (Exception e) {
            tapByLabelFallback("Confirm");
        }
    }
}
