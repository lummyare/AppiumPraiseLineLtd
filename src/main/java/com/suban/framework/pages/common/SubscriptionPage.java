package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * SubscriptionPage — Handles Subscription Trial Enrollment flow.
 * Covers trial offer presentation, enrollment, skip/decline, and confirmation.
 */
public class SubscriptionPage extends BasePage {

    // ── Trial offer screen ─────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Trial')"
            + " or contains(@label,'trial') or contains(@label,'Subscription')"
            + " or contains(@label,'subscription') or contains(@label,'Free Trial')"
            + " or contains(@name,'trialOfferText')]")
    private WebElement trialOfferText;

    // ── Trial duration text ────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'days')"
            + " or contains(@label,'months') or contains(@label,'month')"
            + " or contains(@label,'30') or contains(@label,'90') or contains(@label,'free for')]")
    private WebElement trialDurationText;

    // ── Start Trial button ─────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Start Trial' or @name='startTrialButton'"
            + " or @label='Start Free Trial' or @label='Enroll'"
            + " or contains(@label,'Start') and contains(@label,'Trial')"
            + " or @name='enrollSubscriptionButton']")
    private WebElement startTrialButton;

    // ── Decline / Not Now / Skip button ───────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Not Now' or @name='notNowButton'"
            + " or @label='Skip' or @name='skipButton' or @label='No Thanks'"
            + " or @label='Decline' or @name='declineTrialButton']")
    private WebElement declineTrialButton;

    // ── Enrollment confirmation ────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Trial activated')"
            + " or contains(@label,'enrolled') or contains(@label,'Enrolled')"
            + " or contains(@label,'Subscription Active') or contains(@label,'trial started')"
            + " or contains(@name,'trialConfirmation')]")
    private WebElement trialEnrollmentConfirmation;

    // ── Trial features list ────────────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Remote')"
            + " or contains(@label,'Connected') or contains(@label,'features')"
            + " or contains(@label,'included') or contains(@name,'featuresList')]")
    private WebElement trialFeaturesText;

    // ── Trial period remaining text ────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'remaining')"
            + " or contains(@label,'expires') or contains(@label,'ends')"
            + " or contains(@name,'trialRemainingDays')]")
    private WebElement trialRemainingText;

    // ── Subscribe / Upgrade button ─────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Subscribe' or @name='subscribeButton'"
            + " or @label='Upgrade' or @label='Manage Subscription'"
            + " or @name='upgradeButton']")
    private WebElement subscribeButton;

    // ── Apple Pay / Payment method button ─────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Apple Pay')"
            + " or contains(@name,'applePayButton') or contains(@label,'Payment')"
            + " or contains(@label,'pay')]")
    private WebElement paymentButton;

    // ── Terms and Conditions link ──────────────────────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Terms')"
            + " or contains(@label,'Conditions') or contains(@label,'Privacy')]"
            + " | //XCUIElementTypeStaticText[contains(@label,'Terms')]")
    private WebElement termsLink;

    // ── Renewal reminder / cancel before reminder ──────────────────────────
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'cancel')"
            + " or contains(@label,'Cancel') or contains(@label,'renew')"
            + " or contains(@label,'billing')]")
    private WebElement renewalReminderText;

    public SubscriptionPage(AppiumDriver driver) {
        super(driver);
    }

    // ── Visibility Checks ─────────────────────────────────────────────────

    public boolean isTrialOfferScreenDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(trialOfferText));
            logger.info("[SubscriptionPage] Trial offer screen is displayed");
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Trial") || src.contains("trial") || src.contains("Free Trial")
                    || src.contains("Subscription");
        }
    }

    public boolean isTrialEnrollmentConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(trialEnrollmentConfirmation));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Trial activated") || src.contains("enrolled")
                    || src.contains("Subscription Active") || src.contains("trial started");
        }
    }

    public boolean isTrialFeaturesDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(trialFeaturesText));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Remote") || src.contains("Connected") || src.contains("features");
        }
    }

    public boolean isTrialDurationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(trialDurationText));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("days") || src.contains("months") || src.contains("free for");
        }
    }

    public boolean isRenewalReminderDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(renewalReminderText));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("cancel") || src.contains("renew") || src.contains("billing");
        }
    }

    public String getTrialRemainingText() {
        try {
            return trialRemainingText.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ── Actions ───────────────────────────────────────────────────────────

    public void tapStartTrial() {
        logger.info("[SubscriptionPage] Tapping Start Trial / Enroll button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(startTrialButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Start Trial");
            } catch (Exception e2) {
                try {
                    tapByLabelFallback("Enroll");
                } catch (Exception e3) {
                    tapByLabelFallback("Start Free Trial");
                }
            }
        }
    }

    public void tapDeclineTrial() {
        logger.info("[SubscriptionPage] Tapping Decline / Not Now / Skip trial");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(declineTrialButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Not Now");
            } catch (Exception e2) {
                try {
                    tapByLabelFallback("Skip");
                } catch (Exception e3) {
                    tapByLabelFallback("No Thanks");
                }
            }
        }
    }

    public void tapSubscribe() {
        logger.info("[SubscriptionPage] Tapping Subscribe / Upgrade button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(subscribeButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Subscribe");
            } catch (Exception e2) {
                tapByLabelFallback("Upgrade");
            }
        }
    }

    public void tapTermsAndConditions() {
        logger.info("[SubscriptionPage] Tapping Terms and Conditions link");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(termsLink)).click();
        } catch (Exception e) {
            tapByLabelFallback("Terms");
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────

    private void tapByLabelFallback(String label) {
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//*[@label='" + label + "' or @name='" + label + "'"
                + " or contains(@label,'" + label + "')]"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[SubscriptionPage] Tapped '{}' via fallback", label);
            } else {
                throw new RuntimeException("[SubscriptionPage] Not found: " + label);
            }
        } catch (Exception e) {
            logger.error("[SubscriptionPage] tapByLabelFallback failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }
}
