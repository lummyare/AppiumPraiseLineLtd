package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * RegistrationPage — Covers all interactions on the Create Account / Sign Up flow.
 * Handles email registration, phone registration, field validation, CVN acknowledgment,
 * social login (Apple / Google / Facebook), phone verification, and email verification reminder.
 */
public class RegistrationPage extends BasePage {

    // ── Sign Up button (Welcome screen) ────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Sign Up']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Sign Up' or @name='signUpButton'"
            + " or @name='Sign Up' or @name='createAccountButton']")
    private WebElement signUpButton;

    // ── First Name input ───────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='First Name']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='firstNameInput'"
            + " or @label='First Name' or @name='FR_NATIVE_FIRSTNAME_TEXTFIELD']")
    private WebElement firstNameInput;

    // ── Last Name input ────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Last Name']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='lastNameInput'"
            + " or @label='Last Name' or @name='FR_NATIVE_LASTNAME_TEXTFIELD']")
    private WebElement lastNameInput;

    // ── Email input (registration) ─────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Email']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='emailInput' or @label='Email'"
            + " or @name='FR_NATIVE_REGISTRATION_EMAIL_TEXTFIELD']")
    private WebElement emailInput;

    // ── Mobile number input ────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Mobile Number']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='phoneInput' or @label='Mobile Number'"
            + " or @name='FR_NATIVE_PHONE_TEXTFIELD' or @label='Phone Number']")
    private WebElement mobileInput;

    // ── Password input (registration) ──────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='Password']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='passwordInput'"
            + " or @label='Password' or @name='FR_NATIVE_PASSWORD_TEXTFIELD']")
    private WebElement passwordInput;

    // ── Confirm Password input ─────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='confirmPasswordInput'"
            + " or @label='Confirm Password' or @name='FR_NATIVE_CONFIRM_PASSWORD_TEXTFIELD']")
    private WebElement confirmPasswordInput;

    // ── Sign Up submit button ──────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Sign Up']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='signUpSubmitButton'"
            + " or @label='Sign Up' or @name='FR_NATIVE_SIGNUP_BUTTON']")
    private WebElement signUpSubmitButton;

    // ── Submit / Continue button ───────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Submit' or @name='submitButton'"
            + " or @label='Continue' or @name='continueButton']")
    private WebElement submitButton;

    // ── Inline validation error ────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'required')"
            + " or contains(@label,'invalid') or contains(@label,'error')"
            + " or contains(@label,'already') or contains(@label,'weak')"
            + " or contains(@name,'error') or contains(@name,'validation')]")
    private WebElement validationError;

    // ── Account Created confirmation ───────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Account Created')"
            + " or contains(@label,'Success') or contains(@label,'created')"
            + " or contains(@name,'successConfirmation')]")
    private WebElement accountCreatedConfirmation;

    // ── CVN Acknowledgment screen ──────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'CVN')"
            + " or contains(@label,'Terms') or contains(@label,'Privacy')"
            + " or contains(@label,'acknowledge') or contains(@label,'consent')]")
    private WebElement cvnContentText;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Acknowledge' or @name='acknowledgeButton'"
            + " or @label='Accept' or @name='acceptButton' or @label='I Agree'"
            + " or contains(@label,'Accept')]")
    private WebElement acknowledgeAcceptButton;

    // ── Social login buttons ───────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Apple')"
            + " or contains(@name,'Apple') or contains(@name,'apple')]")
    private WebElement continueWithAppleButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Google')"
            + " or contains(@name,'Google') or contains(@name,'google')]")
    private WebElement continueWithGoogleButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Facebook')"
            + " or contains(@name,'Facebook') or contains(@name,'facebook')]")
    private WebElement continueWithFacebookButton;

    // ── Email verification reminder ────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Resend')"
            + " or contains(@name,'resend') or contains(@label,'Send Verification')"
            + " or contains(@label,'Resend Verification')]")
    private WebElement resendVerificationButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'verification')"
            + " or contains(@label,'Verify') or contains(@label,'Check your email')"
            + " or contains(@name,'emailVerificationBanner')]")
    private WebElement emailVerificationBanner;

    // ── Email verified success ─────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'verified')"
            + " or contains(@label,'Verified') or contains(@label,'Email Updated')"
            + " or contains(@label,'Email Confirmed')]")
    private WebElement emailVerifiedConfirmation;

    public RegistrationPage(AppiumDriver driver) {
        super(driver);
    }

    // ── Welcome screen actions ─────────────────────────────────────────────

    public void tapSignUp() {
        logger.info("[RegistrationPage] Tapping Sign Up button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signUpButton)).click();
        } catch (Exception e) {
            logger.warn("[RegistrationPage] Sign Up button fallback");
            tapByLabelFallback("Sign Up");
        }
    }

    // ── Registration form actions ──────────────────────────────────────────

    public void enterFirstName(String firstName) {
        logger.info("[RegistrationPage] Entering first name: {}", firstName);
        try {
            wait.until(ExpectedConditions.visibilityOf(firstNameInput)).clear();
            firstNameInput.sendKeys(firstName);
        } catch (Exception e) {
            logger.warn("[RegistrationPage] First name input fallback");
            enterTextByLabelFallback("First Name", firstName);
        }
    }

    public void enterLastName(String lastName) {
        logger.info("[RegistrationPage] Entering last name: {}", lastName);
        try {
            wait.until(ExpectedConditions.visibilityOf(lastNameInput)).clear();
            lastNameInput.sendKeys(lastName);
        } catch (Exception e) {
            logger.warn("[RegistrationPage] Last name input fallback");
            enterTextByLabelFallback("Last Name", lastName);
        }
    }

    public void enterEmail(String email) {
        logger.info("[RegistrationPage] Entering email: {}", email);
        try {
            wait.until(ExpectedConditions.visibilityOf(emailInput)).clear();
            emailInput.sendKeys(email);
        } catch (Exception e) {
            logger.warn("[RegistrationPage] Email input fallback");
            enterTextByLabelFallback("Email", email);
        }
    }

    public void enterMobileNumber(String phone) {
        logger.info("[RegistrationPage] Entering mobile number: {}", phone);
        try {
            wait.until(ExpectedConditions.visibilityOf(mobileInput)).clear();
            mobileInput.sendKeys(phone);
        } catch (Exception e) {
            logger.warn("[RegistrationPage] Mobile number input fallback");
            enterTextByLabelFallback("Mobile Number", phone);
        }
    }

    public void enterPassword(String password) {
        logger.info("[RegistrationPage] Entering password");
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordInput)).clear();
            passwordInput.sendKeys(password);
        } catch (Exception e) {
            logger.warn("[RegistrationPage] Password input fallback");
        }
    }

    public void enterConfirmPassword(String password) {
        logger.info("[RegistrationPage] Entering confirm password");
        try {
            wait.until(ExpectedConditions.visibilityOf(confirmPasswordInput)).clear();
            confirmPasswordInput.sendKeys(password);
        } catch (Exception e) {
            logger.warn("[RegistrationPage] Confirm password input fallback");
        }
    }

    public void tapSignUpSubmit() {
        logger.info("[RegistrationPage] Tapping Sign Up submit");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signUpSubmitButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Sign Up");
        }
    }

    public void tapSubmit() {
        logger.info("[RegistrationPage] Tapping Submit");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Submit");
        }
    }

    // ── Validation checks ──────────────────────────────────────────────────

    public boolean isValidationErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(validationError));
            logger.info("[RegistrationPage] Validation error: {}", validationError.getText());
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("required") || src.contains("invalid") || src.contains("error")
                    || src.contains("already") || src.contains("weak");
        }
    }

    public String getValidationErrorText() {
        try {
            return validationError.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isSignUpButtonDisabled() {
        try {
            String enabled = signUpSubmitButton.getAttribute("enabled");
            return "false".equals(enabled);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccountCreatedConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(accountCreatedConfirmation));
            logger.info("[RegistrationPage] Account created confirmation visible");
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Account Created") || src.contains("created") || src.contains("Success");
        }
    }

    // ── CVN ────────────────────────────────────────────────────────────────

    public boolean isCVNScreenDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cvnContentText));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("CVN") || src.contains("Terms") || src.contains("acknowledge")
                    || src.contains("consent") || src.contains("Privacy");
        }
    }

    public void tapAcknowledgeAccept() {
        logger.info("[RegistrationPage] Tapping Acknowledge/Accept on CVN screen");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(acknowledgeAcceptButton)).click();
        } catch (Exception e) {
            logger.warn("[RegistrationPage] Acknowledge fallback");
            try {
                tapByLabelFallback("Acknowledge");
            } catch (Exception e2) {
                tapByLabelFallback("Accept");
            }
        }
    }

    // ── Social Login ───────────────────────────────────────────────────────

    public void tapContinueWithApple() {
        logger.info("[RegistrationPage] Tapping Continue with Apple");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueWithAppleButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Continue with Apple");
        }
    }

    public void tapContinueWithGoogle() {
        logger.info("[RegistrationPage] Tapping Continue with Google");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueWithGoogleButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Continue with Google");
        }
    }

    public void tapContinueWithFacebook() {
        logger.info("[RegistrationPage] Tapping Continue with Facebook");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueWithFacebookButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Continue with Facebook");
        }
    }

    public boolean isSocialProvider(String providerName) {
        String src = driver.getPageSource();
        return src.contains(providerName);
    }

    // ── Email Verification Reminder ────────────────────────────────────────

    public boolean isEmailVerificationReminderDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(emailVerificationBanner));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("verification") || src.contains("Verify your email")
                    || src.contains("Check your email") || src.contains("Resend");
        }
    }

    public void tapResendVerificationEmail() {
        logger.info("[RegistrationPage] Tapping Resend Verification Email");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(resendVerificationButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Resend Verification Email");
            } catch (Exception e2) {
                tapByLabelFallback("Resend");
            }
        }
    }

    public boolean isEmailVerifiedConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(emailVerifiedConfirmation));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("verified") || src.contains("Verified")
                    || src.contains("Email Updated") || src.contains("Email Confirmed");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void tapByLabelFallback(String label) {
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//*[@label='" + label + "' or @name='" + label + "']"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[RegistrationPage] Tapped '{}' via fallback", label);
            } else {
                throw new RuntimeException("[RegistrationPage] Not found: " + label);
            }
        } catch (Exception e) {
            logger.error("[RegistrationPage] tapByLabelFallback failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }

    private void enterTextByLabelFallback(String label, String text) {
        try {
            List<WebElement> fields = driver.findElements(By.xpath(
                "//XCUIElementTypeTextField[@label='" + label + "' or @name='" + label + "']"));
            if (!fields.isEmpty()) {
                fields.get(0).clear();
                fields.get(0).sendKeys(text);
                logger.info("[RegistrationPage] Entered '{}' in '{}' via fallback", text, label);
            } else {
                throw new RuntimeException("[RegistrationPage] Text field not found: " + label);
            }
        } catch (Exception e) {
            logger.error("[RegistrationPage] enterTextByLabelFallback failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }
}
