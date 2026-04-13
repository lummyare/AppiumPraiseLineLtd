package com.suban.framework.utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;

/**
 * Mobile-specific gesture utilities for touch interactions
 * Supports swipe, scroll, tap, pinch, and other mobile gestures
 */
public class MobileGestureUtils {
    private static final Logger logger = LoggerFactory.getLogger(MobileGestureUtils.class);
    
    /**
     * Swipe in specified direction
     */
    public static void swipe(AppiumDriver driver, Direction direction, int durationMs) {
        Dimension size = driver.manage().window().getSize();
        int startX, startY, endX, endY;
        
        switch (direction) {
            case UP:
                startX = size.width / 2;
                startY = (int) (size.height * 0.8);
                endX = size.width / 2;
                endY = (int) (size.height * 0.2);
                break;
            case DOWN:
                startX = size.width / 2;
                startY = (int) (size.height * 0.2);
                endX = size.width / 2;
                endY = (int) (size.height * 0.8);
                break;
            case LEFT:
                startX = (int) (size.width * 0.8);
                startY = size.height / 2;
                endX = (int) (size.width * 0.2);
                endY = size.height / 2;
                break;
            case RIGHT:
                startX = (int) (size.width * 0.2);
                startY = size.height / 2;
                endX = (int) (size.width * 0.8);
                endY = size.height / 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        
        performSwipe(driver, startX, startY, endX, endY, durationMs);
    }
    
    /**
     * Perform swipe between two points
     */
    public static void performSwipe(AppiumDriver driver, int startX, int startY, int endX, int endY, int durationMs) {
        logger.debug("Performing swipe from ({}, {}) to ({}, {}) in {}ms", startX, startY, endX, endY, durationMs);
        
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Arrays.asList(swipe));
    }
    
    /**
     * Scroll to element by text (Android UiAutomator / iOS Predicate)
     */
    public static WebElement scrollToElementByText(AppiumDriver driver, String text) {
        logger.debug("Scrolling to element with text: {}", text);
        
        if (driver instanceof AndroidDriver) {
            return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().textContains(\"" + text + "\"))"
            ));
        } else if (driver instanceof IOSDriver) {
            // For iOS, use predicate string
            return driver.findElement(AppiumBy.iOSNsPredicateString("label CONTAINS '" + text + "'"));
        } else {
            throw new IllegalArgumentException("Unsupported driver type");
        }
    }
    
    /**
     * Tap at specific coordinates
     */
    public static void tapAtCoordinates(AppiumDriver driver, int x, int y) {
        logger.debug("Tapping at coordinates: ({}, {})", x, y);
        
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Arrays.asList(tap));
    }
    
    /**
     * Long press on element
     */
    public static void longPress(AppiumDriver driver, WebElement element, int durationMs) {
        logger.debug("Performing long press on element for {}ms", durationMs);
        
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.x + size.width / 2;
        int centerY = location.y + size.height / 2;
        
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);
        
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX, centerY));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), centerX, centerY));
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Arrays.asList(longPress));
    }
    
    /**
     * Pinch to zoom (zoom in)
     */
    public static void pinchToZoom(AppiumDriver driver, int durationMs) {
        logger.debug("Performing pinch to zoom gesture");
        
        Dimension size = driver.manage().window().getSize();
        int centerX = size.width / 2;
        int centerY = size.height / 2;
        
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");
        
        Sequence sequence1 = new Sequence(finger1, 1);
        Sequence sequence2 = new Sequence(finger2, 1);
        
        // Start positions (close to center)
        sequence1.addAction(finger1.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX - 50, centerY));
        sequence2.addAction(finger2.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX + 50, centerY));
        
        // Press down
        sequence1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        
        // Move apart (zoom in)
        sequence1.addAction(finger1.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), centerX - 150, centerY));
        sequence2.addAction(finger2.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), centerX + 150, centerY));
        
        // Release
        sequence1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        sequence2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Arrays.asList(sequence1, sequence2));
    }
    
    /**
     * Double tap on element
     */
    public static void doubleTap(AppiumDriver driver, WebElement element) {
        logger.debug("Performing double tap on element");
        
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.x + size.width / 2;
        int centerY = location.y + size.height / 2;
        
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence doubleTap = new Sequence(finger, 1);
        
        // First tap
        doubleTap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX, centerY));
        doubleTap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        // Short pause by moving to same position
        doubleTap.addAction(finger.createPointerMove(Duration.ofMillis(100), PointerInput.Origin.viewport(), centerX, centerY));
        
        // Second tap
        doubleTap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Arrays.asList(doubleTap));
    }
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
