package com.suban.framework.pages.common;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * WelcomePage — The first screen the user sees on app launch.
 * Contains Sign In and Sign Up entry points.
 *
 * Sign In button — confirmed real accessibility IDs:
 *   • 'LOGIN_BUTTON_SIGNIN'  ← CONFIRMED working via HomePage.clickSignIn() on fresh launch screen.
 *     This is the same button on the Welcome Back screen — same app screen, same accessibility ID.
 *   Coordinate fallback: W3C tap at (201, 750) — bottom-centre where Sign In renders on Welcome Back
 *   Real name will appear in dumpVisibleElements() log on first failed attempt.
 */
public class WelcomePage extends BasePage {

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

    /**
     * Tap the Sign In button on the Welcome / Welcome Back screen.
     *
     * Strategy (tried in order):
     *  1. Broad XPath: any XCUIElementTypeButton whose @name or @label contains "sign"
     *     case-insensitively (catches FR_NATIVE_SIGNIN_BUTTON, signInButton, Sign In, SIGN IN, etc.)
     *  2. Broader XPath: any XCUIElementTypeButton containing "log" (Log In variants)
     *  3. Element dump via dumpVisibleElements() so the real name appears in logs
     *  4. W3C PointerInput coordinate tap at (201, 750) — bottom-centre where the button
     *     renders on the Welcome Back screen at iPhone 17 Pro (390×844 pt logical size).
     *     If that also fails, throws RuntimeException.
     */
    public void tapSignIn() {
        logger.info("[WelcomePage] Tapping Sign In button");

        // ── Attempt 0: AccessibilityId — CONFIRMED same ID used by HomePage.clickSignIn() ──
        // HomePage uses @iOSXCUITFindBy(accessibility = "LOGIN_BUTTON_SIGNIN") and it works
        // on every app launch. The Welcome Back screen IS the same screen shown after reset.
        try {
            WebElement btn = driver.findElement(AppiumBy.accessibilityId("LOGIN_BUTTON_SIGNIN"));
            logger.info("[WelcomePage] Found Sign In via accessibilityId LOGIN_BUTTON_SIGNIN");
            btn.click();
            logger.info("[WelcomePage] Sign In tapped via accessibilityId — success");
            return;
        } catch (Exception e) {
            logger.warn("[WelcomePage] accessibilityId LOGIN_BUTTON_SIGNIN not found: {}", e.getMessage());
        }

        // ── Attempt 1: XPath fallbacks ──────────────────────────────────────────
        String[] signInXPaths = {
            // ✅ CONFIRMED via HomePage.clickSignIn() which uses accessibilityId="LOGIN_BUTTON_SIGNIN"
            //    This is the same button on both the fresh-launch screen and the Welcome Back screen.
            "//XCUIElementTypeButton[@name='LOGIN_BUTTON_SIGNIN']",
            // Other candidates:
            "//XCUIElementTypeButton[@name='FR_NATIVE_SIGNIN_BUTTON']",
            "//XCUIElementTypeButton[@name='fr_native_signin_button']",
            "//XCUIElementTypeButton[@name='signInButton']",
            "//XCUIElementTypeButton[@name='sign_in_button']",
            "//XCUIElementTypeButton[@label='Sign In']",
            "//XCUIElementTypeButton[@label='SIGN IN']",
            "//XCUIElementTypeButton[@name='Sign In']",
            // Broad contains matches:
            "//XCUIElementTypeButton[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]",
            "//XCUIElementTypeButton[contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]",
            "//XCUIElementTypeButton[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'signin')]",
            "//XCUIElementTypeButton[contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'signin')]",
            // Log In variants:
            "//XCUIElementTypeButton[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'log in')]",
            "//XCUIElementTypeButton[contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'log in')]",
            // Any element (not just button) with sign in label:
            "//*[@label='Sign In' or @name='Sign In' or @label='SIGN IN' or @name='SIGN IN']",
        };

        for (String xpath : signInXPaths) {
            try {
                List<WebElement> found = driver.findElements(By.xpath(xpath));
                if (!found.isEmpty()) {
                    WebElement btn = found.get(0);
                    logger.info("[WelcomePage] Found Sign In via XPath: {} — name='{}' label='{}'",
                        xpath, safeAttr(btn, "name"), safeAttr(btn, "label"));
                    btn.click();
                    logger.info("[WelcomePage] Sign In tapped successfully");
                    return;
                }
            } catch (Exception ex) {
                logger.debug("[WelcomePage] XPath attempt failed ({}): {}", xpath, ex.getMessage());
            }
        }

        // ── All XPaths failed — dump the full element tree so we can read the real name ──
        logger.warn("[WelcomePage] All Sign In XPath attempts failed — dumping visible elements");
        dumpVisibleElements();

        // ── Coordinate fallback: W3C tap at (201, 750) ──────────────────────
        // On Welcome Back screen the Sign In button renders near the bottom-centre.
        // Coordinates are in logical points on iPhone 17 Pro (390×844 pt).
        logger.warn("[WelcomePage] Attempting coordinate tap at (201, 750) for Sign In");
        try {
            tapAtCoordinates(201, 750);
            logger.info("[WelcomePage] Coordinate tap at (201, 750) sent — assuming Sign In tapped");
        } catch (Exception coordEx) {
            logger.error("[WelcomePage] Coordinate tap also failed: {}", coordEx.getMessage());
            throw new RuntimeException(
                "[WelcomePage] tapSignIn() exhausted all strategies. "
                + "Check dumpVisibleElements() output above for real button name.", coordEx);
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
        // Use XPath search since the page-factory @iOSXCUITFindBy annotation was removed
        // (it was pointing at the wrong element — FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD is a TextField)
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//XCUIElementTypeButton[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')"
                + " or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]"));
            return !els.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSignUpButtonDisplayed() {
        return isElementDisplayed(signUpButton, "Sign Up button");
    }

    /**
     * Assert the Welcome Back screen is displayed by finding the WELCOME BACK label.
     * Uses findElement (not getPageSource) to avoid WDA 4095-char truncation.
     */
    public boolean isWelcomeBackPageDisplayed() {
        try {
            driver.findElement(By.xpath("//*[@label='WELCOME BACK']"));
            logger.info("[WelcomePage] WELCOME BACK confirmed");
            return true;
        } catch (Exception e) {
            logger.warn("[WelcomePage] WELCOME BACK not found: {}", e.getMessage());
            return false;
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

    // ── Private helpers ─────────────────────────────────────────────────────

    /**
     * Dump all visible elements to the log.
     * Uses driver.findElements(By.xpath("//*")) — immune to WDA 4095-char page source truncation.
     */
    private void dumpVisibleElements() {
        try {
            List<WebElement> all = driver.findElements(By.xpath("//*"));
            logger.info("[WelcomePage] ══ ELEMENT DUMP ({} elements) ══", all.size());
            for (WebElement el : all) {
                String n  = safeAttr(el, "name");
                String lb = safeAttr(el, "label");
                String t  = safeAttr(el, "type");
                String v  = safeAttr(el, "visible");
                String en = safeAttr(el, "enabled");
                if (!n.isEmpty() || !lb.isEmpty()) {
                    logger.info("  type={} name='{}' label='{}' visible={} enabled={}", t, n, lb, v, en);
                }
            }
        } catch (Exception e) {
            logger.warn("[WelcomePage] dumpVisibleElements failed: {}", e.getMessage());
        }
    }

    /**
     * W3C PointerInput tap at absolute screen coordinates (logical points).
     * Works even when accessibility IDs are unknown — never relies on hideKeyboard().
     */
    private void tapAtCoordinates(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
        logger.info("[WelcomePage] W3C tap sent at ({}, {})", x, y);
    }

    private void tapByLabelFallback(String label) {
        try {
            List<WebElement> els = driver.findElements(
                By.xpath("//*[@label='" + label + "' or @name='" + label + "']"));
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

    private String safeAttr(WebElement el, String attr) {
        try {
            String v = el.getAttribute(attr);
            return v != null ? v : "";
        } catch (Exception e) { return ""; }
    }
}
