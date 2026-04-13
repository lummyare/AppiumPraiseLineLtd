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

        // ── ALWAYS dump first so real element names are visible in every run's log ──
        // This is the only reliable way to discover the true accessibility ID of the
        // Sign In button on the Welcome Back screen (which differs from fresh launch).
        dumpVisibleElements();

        // ── Attempt 0: AccessibilityId LOGIN_BUTTON_SIGNIN ─────────────────────
        // Confirmed: present on fresh app launch screen. NOT present on Welcome Back
        // screen after password reset (log confirmed: NoSuchElement at 12:57:49).
        // Keep as attempt 0 for the fresh-launch call path (step 1 of OB_E2E_006).
        try {
            WebElement btn = driver.findElement(AppiumBy.accessibilityId("LOGIN_BUTTON_SIGNIN"));
            logger.info("[WelcomePage] Found Sign In via accessibilityId LOGIN_BUTTON_SIGNIN");
            btn.click();
            logger.info("[WelcomePage] Sign In tapped via accessibilityId — success");
            return;
        } catch (Exception e) {
            logger.warn("[WelcomePage] LOGIN_BUTTON_SIGNIN not found (expected on Welcome Back screen)");
        }

        // ── Attempt 1: Exact-name XPaths — NO broad 'contains' to avoid false positives ──
        // IMPORTANT: FR_NATIVE_SIGNIN_CONTINUE_BUTTON (label='CONTINUE') was a false
        // positive — its name contains 'signin' but it is the email-entry CONTINUE button.
        // All 'contains' XPaths are intentionally removed. Only exact name/label matches.
        String[] signInXPaths = {
            // Exact names — confirmed or highly likely for this app:
            "//XCUIElementTypeButton[@name='LOGIN_BUTTON_SIGNIN']",
            "//XCUIElementTypeButton[@name='FR_NATIVE_SIGNIN_BUTTON']",
            "//XCUIElementTypeButton[@name='fr_native_signin_button']",
            "//XCUIElementTypeButton[@name='signInButton']",
            "//XCUIElementTypeButton[@name='sign_in_button']",
            // Exact label matches (what the user sees on screen):
            "//XCUIElementTypeButton[@label='Sign In']",
            "//XCUIElementTypeButton[@label='SIGN IN']",
            "//XCUIElementTypeButton[@name='Sign In']",
            // Wildcard element type, exact label/name — safe (not a contains match):
            "//*[@label='Sign In' or @name='Sign In' or @label='SIGN IN' or @name='SIGN IN']",
        };

        for (String xpath : signInXPaths) {
            try {
                List<WebElement> found = driver.findElements(By.xpath(xpath));
                if (!found.isEmpty()) {
                    WebElement btn = found.get(0);
                    String foundName = safeAttr(btn, "name");
                    String foundLabel = safeAttr(btn, "label");
                    // Safety guard: reject known false positives
                    if (foundName.equals("FR_NATIVE_SIGNIN_CONTINUE_BUTTON")) {
                        logger.warn("[WelcomePage] Rejected false positive: FR_NATIVE_SIGNIN_CONTINUE_BUTTON");
                        continue;
                    }
                    logger.info("[WelcomePage] Found Sign In via XPath: {} — name='{}' label='{}'",
                        xpath, foundName, foundLabel);
                    btn.click();
                    logger.info("[WelcomePage] Sign In tapped successfully via XPath");
                    return;
                }
            } catch (Exception ex) {
                logger.debug("[WelcomePage] XPath attempt failed ({}): {}", xpath, ex.getMessage());
            }
        }

        // ── Coordinate fallback: W3C tap at (201, 750) ──────────────────────────
        // On Welcome Back screen (after password reset) the Sign In button renders
        // near the bottom-centre at approximately y=750 on iPhone 17 Pro (390×844 pt).
        // The element dump above will show the real name for next-run lock-in.
        logger.warn("[WelcomePage] All exact XPaths failed — using coordinate tap at (201, 750)");
        try {
            tapAtCoordinates(201, 750);
            logger.info("[WelcomePage] Coordinate tap at (201, 750) sent for Sign In");
        } catch (Exception coordEx) {
            logger.error("[WelcomePage] Coordinate tap also failed: {}", coordEx.getMessage());
            throw new RuntimeException(
                "[WelcomePage] tapSignIn() exhausted all strategies. "
                + "Check dumpVisibleElements() output above for the real Sign In button name.", coordEx);
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
