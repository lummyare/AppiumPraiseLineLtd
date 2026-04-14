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
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'Phone')] | //android.widget.TextView[contains(@text,'Phone Number')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Phone')"
            + " or contains(@name,'Phone') or contains(@name,'phone')]"
            + " | //XCUIElementTypeStaticText[contains(@label,'Phone Number')]")
    private WebElement phoneNumberToggle;

    // ── Phone input (visible after toggle) ────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Phone' or @content-desc='phoneInput' or contains(@hint,'phone')]")
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
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'locked') or contains(@text,'Locked') or contains(@text,'too many') or contains(@text,'temporarily')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'locked')"
            + " or contains(@label,'Locked') or contains(@label,'too many')"
            + " or contains(@name,'lockout') or contains(@label,'temporarily')]")
    private WebElement lockoutMessage;

    // ── Device verification screen prompt ─────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Verify') or contains(@text,'verification') or contains(@text,'new device') or contains(@text,'unrecognized')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Verify')"
            + " or contains(@label,'verification') or contains(@label,'new device')"
            + " or contains(@label,'unrecognized')]")
    private WebElement deviceVerificationPrompt;

    // ── Verification code input ────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Verification Code' or @hint='Enter code' or @content-desc='otpInput']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='FR_NATIVE_OTP_TEXTFIELD'"
            + " or @name='otpInput' or @name='verificationCodeInput'"
            + " or contains(@label,'Enter code') or contains(@label,'Verification code')]")
    private WebElement verificationCodeInput;

    // ── Verify button ──────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Verify' or @text='Submit' or @content-desc='verifyButton'] | //android.widget.TextView[@text='Verify']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='verifyButton' or @label='Verify'"
            + " or @label='Submit' or @name='FR_NATIVE_OTP_SUBMIT_BUTTON']")
    private WebElement verifyButton;

    // ── Resend Code button ─────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Resend' or @text='Resend Code' or @content-desc='resendButton']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Resend')"
            + " or contains(@name,'resend') or contains(@label,'Send Again')]")
    private WebElement resendCodeButton;

    public SignInPage(AppiumDriver driver) {
        super(driver);
    }

    // ── Sign In with Email ─────────────────────────────────────────────────

    public void enterEmail(String email) {
        logger.info("[SignInPage] Entering email: {}", email);
        WebElement field = null;

        // Strategy 1: page-factory annotation (covers both iOS and Android via dual locators)
        try {
            field = wait.until(ExpectedConditions.visibilityOf(emailInput));
            logger.info("[SignInPage] Email field found via annotation");
        } catch (Exception e) {
            logger.warn("[SignInPage] Annotation locator failed for email — trying direct XPath fallbacks");
        }

        // Strategy 2: Android — try every known ForgeRock hint/content-desc variant directly
        if (field == null && isAndroid()) {
            String[] androidXPaths = {
                "//android.widget.EditText[@hint='Email or Username']",
                "//android.widget.EditText[@hint='Email or phone number']",
                "//android.widget.EditText[@hint='Username']",
                "//android.widget.EditText[@hint='Email']",
                "//android.widget.EditText[@content-desc='usernameInput']",
                "//android.widget.EditText[@content-desc='emailInput']",
                "//android.widget.EditText[@content-desc='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']",
                // Last resort: first EditText on screen (ForgeRock screens usually have email first)
                "(//android.widget.EditText)[1]",
            };
            for (String xpath : androidXPaths) {
                try {
                    List<WebElement> els = driver.findElements(By.xpath(xpath));
                    if (!els.isEmpty() && els.get(0).isDisplayed()) {
                        field = els.get(0);
                        logger.info("[SignInPage] Email field found via Android XPath: {}", xpath);
                        // Store the working locator hint for diagnostics
                        String hint = els.get(0).getAttribute("hint");
                        String cd   = els.get(0).getAttribute("content-desc");
                        logger.info("[SignInPage] Email field attributes — hint='{}' content-desc='{}'", hint, cd);
                        break;
                    }
                } catch (Exception ex) { /* try next */ }
            }
        }

        // Strategy 3: iOS — try known XPath variants directly
        if (field == null && isIOS()) {
            String[] iosXPaths = {
                "//XCUIElementTypeTextField[@name='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']",
                "//XCUIElementTypeTextField[@label='Email or phone number']",
                "//XCUIElementTypeTextField[@name='emailInput']",
                "//XCUIElementTypeTextField[@name='usernameInput']",
                "(//XCUIElementTypeTextField)[1]",
            };
            for (String xpath : iosXPaths) {
                try {
                    List<WebElement> els = driver.findElements(By.xpath(xpath));
                    if (!els.isEmpty() && els.get(0).isDisplayed()) {
                        field = els.get(0);
                        logger.info("[SignInPage] Email field found via iOS XPath: {}", xpath);
                        break;
                    }
                } catch (Exception ex) { /* try next */ }
            }
        }

        if (field == null) {
            // Dump all visible EditText/TextField elements to help diagnose the real locator
            dumpInputFields();
            throw new RuntimeException("[SignInPage] enterEmail: could not locate email/username field via any strategy");
        }

        field.clear();
        field.sendKeys(email);
        logger.info("[SignInPage] Email entered successfully");

        // Android: after entering email, click Continue to navigate to the password page.
        // The feature file has no separate Continue step — it must happen here.
        // iOS does not need this — the flow is handled differently.
        if (isAndroid()) {
            clickAndroidContinueButton();
        }
    }

    /**
     * Clicks the Continue button on the Android email entry page.
     * The button is a native view with content-desc='FR_NATIVE_SIGNIN_CONTINUE_BUTTON'
     * or resource-id='com.subaru.oneapp.stage:id/btContinue'.
     * Waits up to 8s for the button to be clickable after email entry.
     */
    private void clickAndroidContinueButton() {
        logger.info("[SignInPage] Android: clicking Continue button after email entry");
        String[] continueXPaths = {
            "//android.widget.Button[@content-desc='FR_NATIVE_SIGNIN_CONTINUE_BUTTON']",
            "//android.widget.Button[@resource-id='com.subaru.oneapp.stage:id/btContinue']",
            "//android.widget.Button[@text='Continue']",
            "//android.widget.Button[@text='CONTINUE']",
            "//android.widget.Button[@text='Next']",
            // Last resort: any enabled Button on screen
            "//android.widget.Button[@enabled='true']",
        };
        org.openqa.selenium.support.ui.WebDriverWait shortWait =
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8));
        for (String xpath : continueXPaths) {
            try {
                List<WebElement> els = driver.findElements(By.xpath(xpath));
                if (!els.isEmpty()) {
                    String txt = els.get(0).getAttribute("text");
                    String cd  = els.get(0).getAttribute("content-desc");
                    String rid = els.get(0).getAttribute("resource-id");
                    logger.info("[SignInPage] Continue found via '{}' — text='{}' content-desc='{}' resource-id='{}'",
                        xpath, txt, cd, rid);
                    shortWait.until(
                        org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                            els.get(0))).click();
                    logger.info("[SignInPage] Android: Continue clicked — waiting for password page");
                    // Wait for password page to load
                    try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    return;
                }
            } catch (Exception ex) {
                logger.debug("[SignInPage] Continue XPath '{}' failed: {}", xpath, ex.getMessage());
            }
        }
        // Dump all buttons so we can see what is on screen
        logger.warn("[SignInPage] Could not find Continue button — dumping all buttons:");
        try {
            List<WebElement> btns = driver.findElements(By.xpath("//android.widget.Button"));
            for (int i = 0; i < btns.size(); i++) {
                logger.warn("  Button[{}] text='{}' content-desc='{}' resource-id='{}' enabled='{}'",
                    i,
                    btns.get(i).getAttribute("text"),
                    btns.get(i).getAttribute("content-desc"),
                    btns.get(i).getAttribute("resource-id"),
                    btns.get(i).getAttribute("enabled"));
            }
        } catch (Exception e) {
            logger.warn("[SignInPage] Button dump failed: {}", e.getMessage());
        }
        throw new RuntimeException("[SignInPage] Android Continue button not found after email entry");
    }

    /**
     * Dumps all visible input fields to the log so we can identify the correct locator
     * when the email field cannot be found by any known strategy.
     */
    private void dumpInputFields() {
        try {
            logger.warn("[SignInPage] === INPUT FIELD DUMP (for locator diagnosis) ===");
            if (isAndroid()) {
                List<WebElement> editTexts = driver.findElements(By.xpath("//android.widget.EditText"));
                logger.warn("[SignInPage] Found {} EditText elements:", editTexts.size());
                for (int i = 0; i < editTexts.size(); i++) {
                    WebElement el = editTexts.get(i);
                    logger.warn("  [{}] hint='{}' text='{}' content-desc='{}' resource-id='{}' displayed={}",
                        i,
                        el.getAttribute("hint"),
                        el.getAttribute("text"),
                        el.getAttribute("content-desc"),
                        el.getAttribute("resource-id"),
                        el.isDisplayed());
                }
            } else {
                List<WebElement> fields = driver.findElements(
                    By.xpath("//XCUIElementTypeTextField | //XCUIElementTypeSecureTextField"));
                logger.warn("[SignInPage] Found {} TextField elements:", fields.size());
                for (int i = 0; i < fields.size(); i++) {
                    WebElement el = fields.get(i);
                    logger.warn("  [{}] name='{}' label='{}' value='{}' displayed={}",
                        i,
                        el.getAttribute("name"),
                        el.getAttribute("label"),
                        el.getAttribute("value"),
                        el.isDisplayed());
                }
            }
            logger.warn("[SignInPage] === END DUMP ===");
        } catch (Exception e) {
            logger.warn("[SignInPage] dumpInputFields failed: {}", e.getMessage());
        }
    }

    public void enterPassword(String password) {
        logger.info("[SignInPage] Entering password");
        WebElement field = null;

        // Android: password page is native — use Android-specific XPaths
        if (isAndroid()) {
            String[] androidPasswordXPaths = {
                "//android.widget.EditText[@resource-id='com.subaru.oneapp.stage:id/etPassword']",
                "//android.widget.EditText[@content-desc='FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD']",
                "//android.widget.EditText[@hint='Password']",
                "//android.widget.EditText[@hint='password']",
                "//android.widget.EditText[@password='true']",
                // Last resort: second EditText (first is email, second is password)
                "(//android.widget.EditText)[1]",
            };
            for (String xpath : androidPasswordXPaths) {
                try {
                    List<WebElement> els = driver.findElements(By.xpath(xpath));
                    if (!els.isEmpty() && els.get(0).isDisplayed()) {
                        field = els.get(0);
                        logger.info("[SignInPage] Android password field found via: {}", xpath);
                        break;
                    }
                } catch (Exception ex) { /* try next */ }
            }
            if (field == null) {
                // Dump all EditTexts to identify the real locator
                dumpInputFields();
                throw new RuntimeException("[SignInPage] Android: could not locate password field");
            }
        } else {
            // iOS path
            try {
                field = wait.until(ExpectedConditions.visibilityOf(passwordInput));
                logger.info("[SignInPage] iOS password field found via annotation");
            } catch (Exception e) {
                logger.warn("[SignInPage] iOS annotation locator failed for password — trying direct XPath");
                String[] iosPasswordXPaths = {
                    "//XCUIElementTypeSecureTextField[@name='FR_NATIVE_SIGNIN_PASSWORD_TEXTFIELD']",
                    "//XCUIElementTypeSecureTextField[@label='Password']",
                    "//XCUIElementTypeSecureTextField[@name='passwordInput']",
                    "//XCUIElementTypeSecureTextField",
                };
                for (String xpath : iosPasswordXPaths) {
                    try {
                        java.util.List<WebElement> els = driver.findElements(By.xpath(xpath));
                        if (!els.isEmpty()) {
                            field = els.get(0);
                            logger.info("[SignInPage] iOS password field found via XPath: {}", xpath);
                            break;
                        }
                    } catch (Exception ex) { /* try next */ }
                }
            }
            if (field == null) {
                throw new RuntimeException("[SignInPage] iOS: could not locate password field via any strategy");
            }
        }
        field.clear();
        field.sendKeys(password);
        logger.info("[SignInPage] Password entered");

        // ── Keyboard dismiss ──────────────────────────────────────────────────────
        // Android: DO NOT send \n after password entry.
        // On this ForgeRock native password screen, \n clears the field and
        // reverts the page back to the Continue button state — corrupting the
        // password before Sign In is tapped.  Use hideKeyboard() instead;
        // if that also fails it is safe to continue — btSignIn is a separate
        // native Button that is tappable regardless of keyboard visibility.
        //
        // iOS: keep the \n (Return key) dismiss — it works there and does not
        // corrupt the password field.
        if (isAndroid()) {
            try {
                ((io.appium.java_client.HidesKeyboard) driver).hideKeyboard();
                logger.info("[SignInPage] Android: keyboard hidden via hideKeyboard()");
            } catch (Exception e) {
                // hideKeyboard is a no-op if keyboard is already hidden — safe to ignore
                logger.debug("[SignInPage] Android: hideKeyboard skipped (keyboard may already be hidden): {}", e.getMessage());
            }
        } else {
            // iOS: Return key dismisses the keyboard without clearing the field
            try {
                field.sendKeys("\n");
                logger.info("[SignInPage] iOS: keyboard dismissed via Return key after password entry");
            } catch (Exception e) {
                logger.debug("[SignInPage] iOS: Return key dismiss skipped: {}", e.getMessage());
            }
        }
        try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    public void tapSignInSubmit() {
        logger.info("[SignInPage] Tapping Sign In submit button");

        // ── Android path ──────────────────────────────────────────────────────
        if (isAndroid()) {
            String[] androidSignInXPaths = {
                "//android.widget.Button[@resource-id='com.subaru.oneapp.stage:id/btSignIn']",
                "//android.widget.Button[@content-desc='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']",
                "//android.widget.Button[@text='Sign In']",
                "//android.widget.Button[@text='SIGN IN']",
                "//android.widget.Button[@text='Sign in']",
                // Last resort: first enabled button on screen
                "//android.widget.Button[@enabled='true']",
            };
            for (String xpath : androidSignInXPaths) {
                try {
                    List<WebElement> els = driver.findElements(By.xpath(xpath));
                    if (!els.isEmpty() && els.get(0).isDisplayed()) {
                        String txt = els.get(0).getAttribute("text");
                        String cd  = els.get(0).getAttribute("content-desc");
                        String rid = els.get(0).getAttribute("resource-id");
                        logger.info("[SignInPage] Android Sign In button found via '{}' — text='{}' content-desc='{}' resource-id='{}'",
                            xpath, txt, cd, rid);
                        wait.until(ExpectedConditions.elementToBeClickable(els.get(0))).click();
                        logger.info("[SignInPage] Android: Sign In button clicked");
                        return;
                    }
                } catch (Exception ex) {
                    logger.debug("[SignInPage] Android Sign In XPath '{}' failed: {}", xpath, ex.getMessage());
                }
            }
            // Dump all buttons so we can identify the real locator for next run
            logger.warn("[SignInPage] Android: could not find Sign In button — dumping all buttons:");
            try {
                List<WebElement> btns = driver.findElements(By.xpath("//android.widget.Button"));
                for (int i = 0; i < btns.size(); i++) {
                    logger.warn("  Button[{}] text='{}' content-desc='{}' resource-id='{}' enabled='{}'",
                        i,
                        btns.get(i).getAttribute("text"),
                        btns.get(i).getAttribute("content-desc"),
                        btns.get(i).getAttribute("resource-id"),
                        btns.get(i).getAttribute("enabled"));
                }
            } catch (Exception e) {
                logger.warn("[SignInPage] Button dump failed: {}", e.getMessage());
            }
            throw new RuntimeException("[SignInPage] Android: Sign In button not found via any strategy");
        }

        // ── iOS path ──────────────────────────────────────────────────────────

        // Pre-check: if the VERIFICATION REQUIRED modal is already on screen
        // (the app processed the login from the Return-key dismiss and jumped straight
        // to device verification), the SIGN IN button no longer exists — skip the tap.
        // completePostSignInFlow() in the step definitions will handle the modal.
        try {
            List<WebElement> sendCode = driver.findElements(
                By.xpath("//*[@name='SEND CODE' or @label='SEND CODE']"));
            if (!sendCode.isEmpty()) {
                logger.info("[SignInPage] VERIFICATION REQUIRED modal detected — " +
                    "login was triggered by Return key; skipping Sign In button tap");
                return;
            }
        } catch (Exception ignored) { }

        // Strategy 1: page-factory annotation
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signInSubmitButton)).click();
            logger.info("[SignInPage] Sign In tapped via annotation");
            return;
        } catch (Exception e) {
            logger.warn("[SignInPage] Annotation locator failed — trying direct XPaths");
        }

        // Strategy 2: confirmed real accessibility ID from LoginPage.java
        String[] signInXPaths = {
            "//XCUIElementTypeButton[@name='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']",
            "//XCUIElementTypeButton[@name='FR_NATIVE_SIGNIN_BUTTON']",
            "//XCUIElementTypeButton[@label='SIGN IN']",
            "//XCUIElementTypeButton[@label='Sign In']",
            "//XCUIElementTypeButton[@name='signInSubmitButton']",
        };
        for (String xpath : signInXPaths) {
            try {
                List<WebElement> els = driver.findElements(By.xpath(xpath));
                if (!els.isEmpty()) {
                    els.get(0).click();
                    logger.info("[SignInPage] Sign In tapped via XPath: {}", xpath);
                    return;
                }
            } catch (Exception ex) { /* try next */ }
        }

        // Strategy 3: W3C PointerInput coordinate tap at (201, 750)
        // TouchAction is deprecated and throws UnsupportedCommandException on modern WDA.
        logger.warn("[SignInPage] All XPath strategies failed — W3C coordinate tap (201, 750)");
        try {
            org.openqa.selenium.interactions.PointerInput finger =
                new org.openqa.selenium.interactions.PointerInput(
                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
            org.openqa.selenium.interactions.Sequence tap =
                new org.openqa.selenium.interactions.Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(
                java.time.Duration.ZERO,
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), 201, 750));
            tap.addAction(finger.createPointerDown(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(java.util.Collections.singletonList(tap));
            logger.info("[SignInPage] Sign In tapped via W3C coordinate (201, 750)");
        } catch (Exception coordEx) {
            throw new RuntimeException("[SignInPage] tapSignInSubmit: all strategies exhausted.", coordEx);
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
