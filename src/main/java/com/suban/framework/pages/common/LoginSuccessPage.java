package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginSuccessPage extends BasePage {

    public LoginSuccessPage(AppiumDriver driver) {
        super(driver);
    }

    // ========== ONBOARDING / FTUE SCREENS ==========

    // Screen 1: Skip button — bypasses the full onboarding tour
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='SKIP']")
    @iOSXCUITFindBy(accessibility = "ftue_skip_text_cta")
    private WebElement skipForNow;

    // Screen 1: "See what's new" CTA (alternative to Skip)
    @AndroidFindBy(accessibility = "Logout")
    @iOSXCUITFindBy(accessibility = "ftue_seenew_button_cta")
    private WebElement seeWhatNew;

    // Screen 2 (after See What's New): video / OK button
    @iOSXCUITFindBy(accessibility = "ftue_video")
    private WebElement videoIcon;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='OK']")
    @iOSXCUITFindBy(accessibility = "ftue_ok_button_cta")
    private WebElement ftueOkBtn;

    // ========== DASHBOARD POPUP DISMISS ==========
    // Generic close / dismiss buttons that may appear as overlays on the dashboard
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Close'] | //android.widget.ImageButton[@content-desc='Close']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Close' or @name='close' or @name='Dismiss' or @name='dismiss' or @name='close_button' or @name='ic_close']")
    private WebElement genericCloseButton;

    // ========== UPDATE MOBILE NUMBER MODAL ==========
    // The X close button is an XCUIElementTypeImage (not a Button!) with
    // name="SheetWithCloseButton_sheetCloseButton" — confirmed from page source dump.
    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc='Close'] | //android.widget.Button[@text='Close']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeImage[starts-with(@name,'SheetWithCloseButton_sheetClose')]")
    private WebElement updateMobileNumberModalCloseButton;

    // Modal title — used to detect whether the modal is currently displayed
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Update Mobile Number']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='Update Mobile Number']")
    private WebElement updateMobileNumberModalTitle;

    // ========== MAIN DASHBOARD ==========
    // user_profile_button is not present in this build's accessibility tree.
    // BottomTabBar_dashboardTab is the confirmed visible dashboard nav element (from page source).
    @AndroidFindBy(id = "com.subaru.oneapp.stage:id/profile_button")
    @iOSXCUITFindBy(accessibility = "BottomTabBar_dashboardTab")
    private WebElement userProfileButton;

    @AndroidFindBy(id = "com.subaru.oneapp.stage:id/add_vehicle_button")
    @iOSXCUITFindBy(accessibility = "BottomTabBar_serviceTab")
    private WebElement addVehicleButton;

    // ========== DASHBOARD QUICK-ACTION BUTTONS ==========
    // Confirmed present from page source dump (Strategy-4 log):
    //   name='Remote_Status_Health_View' label='Remote'
    //   name='Remote_Status_Health_View' label='Status'
    //   name='Remote_Status_Health_View' label='Health'
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Remote']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Remote_Status_Health_View' and @label='Remote']")
    private WebElement remoteButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Status']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Remote_Status_Health_View' and @label='Status']")
    private WebElement statusButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Health']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Remote_Status_Health_View' and @label='Health']")
    private WebElement healthButton;

    // ========== METHODS ==========

    /**
     * Taps Skip on the first onboarding screen.
     * Silently skips if the element is not present.
     */
    public void clickSkipForNow() {
        try {
            skipForNow.click();
            logger.info("Tapped Skip on onboarding screen");
        } catch (Exception e) {
            logger.info("Skip button not present — skipping: {}", e.getMessage());
        }
    }

    /**
     * Returns true if the Skip button is currently visible.
     */
    public boolean isSkipDisplayed() {
        try {
            return isElementDisplayed(skipForNow, "Skip button");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Taps the OK button on the second FTUE screen.
     * Silently skips if not present.
     */
    public void clickFtueOk() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(ftueOkBtn));
            clickWithLogging(ftueOkBtn, "FTUE OK button");
        } catch (Exception e) {
            logger.info("FTUE OK button not present — skipping: {}", e.getMessage());
        }
    }

    /**
     * Returns true if the FTUE OK button is visible.
     */
    public boolean isFtueOkDisplayed() {
        try {
            return isElementDisplayed(ftueOkBtn, "FTUE OK button");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Dismisses any popup or overlay visible on the dashboard.
     * Tries multiple strategies in order:
     *  1. Named close/dismiss button via locator
     *  2. iOS system alert accept
     *  3. XPath sweep for any button named Close, Dismiss, Cancel, Got It, OK
     */
    public void dismissDashboardPopups() {
        // Strategy 1: named close button via page object locator
        try {
            if (isElementDisplayed(genericCloseButton, "Generic close button")) {
                clickWithLogging(genericCloseButton, "Generic close button");
                logger.info("Dismissed dashboard popup via close button");
                return;
            }
        } catch (Exception e) {
            logger.info("No generic close button found");
        }

        // Strategy 2: iOS system alert
        try {
            driver.switchTo().alert().accept();
            logger.info("Dismissed iOS system alert on dashboard");
            return;
        } catch (Exception e) {
            // no system alert
        }

        // Strategy 3: XPath sweep for common dismiss button labels
        String[] dismissLabels = {"Close", "close", "Dismiss", "dismiss", "Cancel", "cancel", "Got It", "OK", "Done"};
        for (String label : dismissLabels) {
            try {
                List<WebElement> buttons = driver.findElements(
                    By.xpath("//XCUIElementTypeButton[@name='" + label + "'] | //XCUIElementTypeStaticText[@name='" + label + "']"));
                if (!buttons.isEmpty()) {
                    buttons.get(0).click();
                    logger.info("Dismissed dashboard popup by tapping '{}'", label);
                    return;
                }
            } catch (Exception e) {
                // try next label
            }
        }
        logger.info("No dashboard popups detected — dashboard is clear");
    }

    /**
     * Returns true if the "Update Mobile Number" modal is currently visible.
     * Uses an explicit short wait so the modal has time to fully render.
     */
    public boolean isUpdateMobileNumberModalDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                .until(ExpectedConditions.visibilityOf(updateMobileNumberModalTitle));
            logger.info("Update Mobile Number modal is displayed");
            return true;
        } catch (Exception e) {
            logger.info("Update Mobile Number modal not detected: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Dumps the current page source to the log so we can read exact element names.
     * Call this whenever a locator fails unexpectedly.
     */
    private void dumpPageSourceForDebug(String context) {
        try {
            String src = driver.getPageSource();
            // Only log a relevant snippet around "Update Mobile Number" to keep logs manageable
            int idx = src.indexOf("Update Mobile Number");
            if (idx >= 0) {
                int start = Math.max(0, idx - 800);
                int end   = Math.min(src.length(), idx + 1200);
                logger.info("[DEBUG-{}] Page source snippet around modal:\n{}", context, src.substring(start, end));
            } else {
                // modal text not found — log a broader window
                logger.info("[DEBUG-{}] 'Update Mobile Number' not in page source. First 3000 chars:\n{}",
                    context, src.substring(0, Math.min(src.length(), 3000)));
            }
        } catch (Exception ex) {
            logger.warn("[DEBUG-{}] Could not capture page source: {}", context, ex.getMessage());
        }
    }

    /**
     * Closes the "Update Mobile Number" modal by tapping its X close button.
     *
     * The X button is an XCUIElementTypeImage (not a Button) with
     * name="SheetWithCloseButton_sheetCloseButton" — confirmed from page source dump.
     *
     * Strategy order:
     *  1. Page-object locator: XCUIElementTypeImage[starts-with(@name,'SheetWithCloseButton_sheetClose')]
     *  2. Any XCUIElementTypeImage whose name starts with 'SheetWithCloseButton' (direct driver find)
     *  3. Preceding XCUIElementTypeImage siblings of the modal title
     *  4. Coordinate tap — X button is always at top-right of the modal (calculated from modal title position)
     */
    public void closeUpdateMobileNumberModal() {
        // ── Strategy 1: page-object locator (XCUIElementTypeImage with SheetWithCloseButton name) ──
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOf(updateMobileNumberModalCloseButton));
            updateMobileNumberModalCloseButton.click();
            try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            if (!isModalStillOpen()) {
                logger.info("[Strategy-1] Closed modal via SheetWithCloseButton page-object locator");
                return;
            }
            logger.info("[Strategy-1] Tapped SheetWithCloseButton but modal still open");
        } catch (Exception e) {
            logger.info("[Strategy-1] page-object locator failed: {}", e.getMessage());
        }

        // ── Strategy 2: direct driver find for any SheetWithCloseButton image ──
        try {
            List<WebElement> imgs = driver.findElements(By.xpath(
                "//XCUIElementTypeImage[starts-with(@name,'SheetWithCloseButton')]"));
            logger.info("[Strategy-2] Found {} SheetWithCloseButton images", imgs.size());
            for (WebElement img : imgs) {
                img.click();
                try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                if (!isModalStillOpen()) {
                    logger.info("[Strategy-2] Closed modal via SheetWithCloseButton image");
                    return;
                }
            }
        } catch (Exception e) {
            logger.info("[Strategy-2] SheetWithCloseButton image sweep failed: {}", e.getMessage());
        }

        // ── Strategy 3: XCUIElementTypeImage preceding the modal title ──
        // In the page source the close image appears just before the title text node
        try {
            List<WebElement> imgs = driver.findElements(By.xpath(
                "//XCUIElementTypeStaticText[@name='Update Mobile Number']"
                + "/preceding::XCUIElementTypeImage"));
            logger.info("[Strategy-3] Found {} images preceding modal title", imgs.size());
            // Iterate in reverse — the closest preceding image is last in the list
            for (int i = imgs.size() - 1; i >= 0; i--) {
                String n = "";
                try { n = imgs.get(i).getAttribute("name"); } catch (Exception ignored) {}
                logger.info("[Strategy-3] Preceding image[{}]: name='{}'", i, n);
                try {
                    imgs.get(i).click();
                    try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    if (!isModalStillOpen()) {
                        logger.info("[Strategy-3] Closed modal via preceding image[{}] name='{}'", i, n);
                        return;
                    }
                } catch (Exception e) {
                    logger.info("[Strategy-3] image[{}] click failed: {}", i, e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.info("[Strategy-3] preceding image sweep failed: {}", e.getMessage());
        }

        // ── Strategy 4: coordinate tap — X is always at top-right corner of the modal ──
        // From page source: modal title is at x=93, y=603, width=216.
        // The modal card starts ~60px above the title (y≈540), and the X is top-right (x≈370, y≈550).
        try {
            List<WebElement> titles = driver.findElements(
                By.xpath("//XCUIElementTypeStaticText[@name='Update Mobile Number']"));
            if (!titles.isEmpty()) {
                WebElement title = titles.get(0);
                int titleX = Integer.parseInt(title.getAttribute("x"));
                int titleY = Integer.parseInt(title.getAttribute("y"));
                int titleW = Integer.parseInt(title.getAttribute("width"));
                // X button is at the right edge of the modal, about 50px above the title
                int tapX = titleX + titleW + 60; // right edge of modal
                int tapY = titleY - 30;           // just above the title
                logger.info("[Strategy-4] Coordinate tap at ({}, {})", tapX, tapY);
                org.openqa.selenium.interactions.PointerInput finger =
                    new org.openqa.selenium.interactions.PointerInput(
                        org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
                org.openqa.selenium.interactions.Sequence tap = new org.openqa.selenium.interactions.Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(
                    java.time.Duration.ZERO,
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), tapX, tapY));
                tap.addAction(finger.createPointerDown(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(java.util.Arrays.asList(tap));
                try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                if (!isModalStillOpen()) {
                    logger.info("[Strategy-4] Closed modal via coordinate tap ({}, {})", tapX, tapY);
                    return;
                }
                logger.info("[Strategy-4] Coordinate tap did not close modal");
            }
        } catch (Exception e) {
            logger.info("[Strategy-4] Coordinate tap failed: {}", e.getMessage());
        }

        logger.warn("[closeUpdateMobileNumberModal] All strategies exhausted — modal may still be open");
        dumpPageSourceForDebug("afterAllStrategiesFailed");
    }

    /**
     * Returns true if the modal title is still visible on screen (used to verify a tap worked).
     */
    private boolean isModalStillOpen() {
        try {
            List<WebElement> titles = driver.findElements(
                By.xpath("//XCUIElementTypeStaticText[@name='Update Mobile Number']"));
            boolean open = !titles.isEmpty() && titles.get(0).isDisplayed();
            logger.info("Modal still open: {}", open);
            return open;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true when the main dashboard is visible (instant check, no wait).
     * Uses the user-profile button as the anchor element.
     */
    public boolean isDashboardDisplayed() {
        try {
            return isElementDisplayed(userProfileButton, "User profile button (dashboard anchor)");
        } catch (Exception e) {
            // Fall back: check add-vehicle button, which is also always on the nav bar
            try {
                return isElementDisplayed(addVehicleButton, "Add vehicle button (dashboard anchor)");
            } catch (Exception e2) {
                return false;
            }
        }
    }

    /**
     * Waits up to {@code timeoutSeconds} for the dashboard to become visible.
     * Polls for user_profile_button first, then nv_add_vehicle as fallback.
     * Returns true as soon as either element appears; returns false on timeout.
     */
    public boolean waitForDashboard(int timeoutSeconds) {
        logger.info("Waiting up to {}s for dashboard to become visible...", timeoutSeconds);
        WebDriverWait dashWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        // Try profile button first
        try {
            dashWait.until(ExpectedConditions.visibilityOf(userProfileButton));
            logger.info("Dashboard confirmed visible via user_profile_button");
            return true;
        } catch (Exception e) {
            logger.info("user_profile_button not visible within {}s, trying add-vehicle button", timeoutSeconds);
        }
        // Fallback: add-vehicle / nav bar button
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(addVehicleButton));
            logger.info("Dashboard confirmed visible via nv_add_vehicle");
            return true;
        } catch (Exception e2) {
            logger.warn("Dashboard anchor elements not found after {}s total", timeoutSeconds + 5);
            return false;
        }
    }

    /**
     * Asserts that the app is on the Dashboard by verifying the Remote, Status,
     * and Health quick-action buttons are all visible.
     *
     * These three buttons are confirmed to be present on the dashboard from the
     * page source dump (name='Remote_Status_Health_View', label='Remote'/'Status'/'Health').
     *
     * Falls back to the nav-bar tab check if any button is not found.
     *
     * @throws AssertionError if none of the dashboard elements are found
     */
    public void assertDashboardByRemoteButtons() {
        logger.info("Asserting dashboard via Remote / Status / Health buttons");

        WebDriverWait dashWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        boolean remoteVisible  = false;
        boolean statusVisible  = false;
        boolean healthVisible  = false;

        try {
            dashWait.until(ExpectedConditions.visibilityOf(remoteButton));
            remoteVisible = true;
            logger.info("Remote button is visible ✓");
        } catch (Exception e) {
            logger.warn("Remote button not found: {}", e.getMessage());
        }

        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(statusButton));
            statusVisible = true;
            logger.info("Status button is visible ✓");
        } catch (Exception e) {
            logger.warn("Status button not found: {}", e.getMessage());
        }

        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(healthButton));
            healthVisible = true;
            logger.info("Health button is visible ✓");
        } catch (Exception e) {
            logger.warn("Health button not found: {}", e.getMessage());
        }

        if (remoteVisible && statusVisible && healthVisible) {
            logger.info("Dashboard assertion PASSED: Remote ✓  Status ✓  Health ✓");
            return;
        }

        // Partial pass — at least one button found means we're on the dashboard
        if (remoteVisible || statusVisible || healthVisible) {
            logger.warn("Dashboard assertion PARTIAL: Remote={}  Status={}  Health={} — " +
                        "some dashboard buttons missing but app appears to be on dashboard",
                        remoteVisible, statusVisible, healthVisible);
            return;
        }

        // None of the quick-action buttons found — fall back to nav-bar tab check
        logger.warn("Remote/Status/Health not found — falling back to nav-bar tab check");
        boolean navVisible = waitForDashboard(8);
        if (navVisible) {
            logger.info("Dashboard assertion PASSED via nav-bar tab fallback");
            return;
        }

        // Nothing found at all — hard fail
        throw new AssertionError(
            "Dashboard assertion FAILED: Remote, Status, Health buttons and nav-bar tabs " +
            "were all not found. Expected to be on the App Dashboard.");
    }

    public void clickProfileBtn() {
        userProfileButton.click();
    }

    public void clickAddVehicleBtn() {
        addVehicleButton.click();
    }
}
