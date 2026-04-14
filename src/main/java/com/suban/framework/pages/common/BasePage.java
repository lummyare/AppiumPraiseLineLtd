package com.suban.framework.pages.common;

import com.suban.framework.config.ConfigReader;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

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
}
