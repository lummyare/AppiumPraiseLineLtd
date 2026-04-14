package com.suban.framework.pages.common;

import com.suban.framework.config.ConfigReader;
import com.suban.framework.core.DriverManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Set;

public abstract class BasePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected Logger logger;

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        // Read wait timeout from config instead of hardcoding 20s
        int waitSeconds = getExplicitWaitSeconds();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
        this.logger = LoggerFactory.getLogger(this.getClass());
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    private int getExplicitWaitSeconds() {
        try {
            return ConfigReader.getIntProperty("explicit.wait");
        } catch (Exception e) {
            logger.warn("Could not read explicit.wait from config, defaulting to 15s");
            return 15;
        }
    }

    public WebElement getDynamicElement(String locatorType, String staticSuffix) {
        try {
            String prefix = ConfigReader.getProperty("app.dynamic.prefix");
            String fullValue = (prefix != null && !prefix.isEmpty())
                    ? prefix + staticSuffix
                    : staticSuffix;

            switch (locatorType.toLowerCase()) {
                case "id":
                    return driver.findElement(By.id(fullValue));
                case "xpath":
                    return driver.findElement(By.xpath(staticSuffix)); // XPath doesn't use ID prefix
                case "accessibilityid":
                    return driver.findElement(AppiumBy.accessibilityId(staticSuffix)); // accessibility IDs don't use prefix
                default:
                    logger.error("{} is not a valid locator type", locatorType);
                    throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
            }
        } catch (Exception e) {
            logger.error("Failed to find element with dynamic locator: {}", staticSuffix, e);
            throw e;
        }
    }

    protected void clickWithLogging(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            logger.info("Clicked on: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to click on: {}", elementName, e);
            throw e;
        }
    }

    protected String getTextWithLogging(WebElement element, String elementName) {
        try {
            String text = wait.until(ExpectedConditions.visibilityOf(element)).getText();
            logger.info("Retrieved text from {}: {}", elementName, text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from: {}", elementName, e);
            throw e;
        }
    }

    protected boolean isElementDisplayed(WebElement element, String elementName) {
        try {
            boolean displayed = element.isDisplayed();
            logger.info("Element {} is displayed: {}", elementName, displayed);
            return displayed;
        } catch (Exception e) {
            logger.info("Element not visible: {}", elementName);
            return false;
        }
    }

    // ── Platform helpers ─────────────────────────────────────────────────────

    /** Returns true when running against an Android device/emulator. */
    protected boolean isAndroid() {
        return driver instanceof AndroidDriver;
    }

    /** Returns true when running against an iOS simulator/device. */
    protected boolean isIOS() {
        return driver instanceof IOSDriver;
    }

    /**
     * Dismisses the on-screen keyboard in a platform-safe way.
     * iOS: hideKeyboard() always throws — use the Return key instead.
     * Android: hideKeyboard() works reliably.
     */
    protected void dismissKeyboard() {
        if (isAndroid()) {
            try {
                ((AndroidDriver) driver).hideKeyboard();
            } catch (Exception e) {
                logger.debug("hideKeyboard had no effect (keyboard may already be hidden)");
            }
        } else {
            // iOS — press Return key to dismiss
            try {
                driver.findElement(By.xpath("//XCUIElementTypeKeyboard"))
                      .sendKeys("\n");
            } catch (Exception e) {
                logger.debug("iOS keyboard dismiss via Return key — keyboard may already be hidden");
            }
        }
    }

    /**
     * Returns the Android resource-ID prefix for this app.
     * Used by getDynamicElement() to build full IDs like "com.subaru.oneapp.stage:id/emailInput".
     */
    protected String androidPrefix() {
        String p = ConfigReader.getProperty("app.subaru.android.prefix");
        return (p != null && !p.isEmpty()) ? p : "com.subaru.oneapp.stage:id/";
    }

    // ── WebView / Context switching ──────────────────────────────────────────

    /**
     * Switches Appium context to the first available WEBVIEW on Android.
     * The ForgeRock login/OTP screens are rendered inside a WebView —
     * native Android locators (EditText, Button) won't find elements there.
     * No-op on iOS (XCUITest handles WebViews natively).
     *
     * @param waitSeconds how long to poll for a WEBVIEW context to appear
     * @return true if successfully switched to a WEBVIEW context
     */
    protected boolean switchToWebView(int waitSeconds) {
        if (!isAndroid()) return true; // iOS handles WebViews transparently
        try {
            long deadline = System.currentTimeMillis() + (waitSeconds * 1000L);
            while (System.currentTimeMillis() < deadline) {
                Set<String> contexts = ((AndroidDriver) driver).getContextHandles();
                logger.info("[WebView] Available contexts: {}", contexts);
                for (String ctx : contexts) {
                    if (ctx.startsWith("WEBVIEW")) {
                        ((AndroidDriver) driver).context(ctx);
                        logger.info("[WebView] Switched to context: {}", ctx);
                        return true;
                    }
                }
                logger.debug("[WebView] No WEBVIEW context yet — retrying in 1s");
                Thread.sleep(1000);
            }
            logger.warn("[WebView] No WEBVIEW context found after {}s — staying in NATIVE_APP", waitSeconds);
            return false;
        } catch (Exception e) {
            logger.warn("[WebView] switchToWebView failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Switches back to NATIVE_APP context.
     * Call this after finishing all WebView interactions.
     */
    protected void switchToNative() {
        if (!isAndroid()) return;
        try {
            ((AndroidDriver) driver).context("NATIVE_APP");
            logger.info("[WebView] Switched back to NATIVE_APP context");
        } catch (Exception e) {
            logger.warn("[WebView] switchToNative failed: {}", e.getMessage());
        }
    }

    /**
     * Returns the current Appium context name (e.g. "NATIVE_APP" or "WEBVIEW_...").
     */
    protected String currentContext() {
        if (!isAndroid()) return "NATIVE_APP";
        try {
            return ((AndroidDriver) driver).getContext();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
}
