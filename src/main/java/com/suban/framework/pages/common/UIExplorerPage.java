package com.suban.framework.pages.common;

import com.suban.framework.utils.UiElementExtractor;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

/**
 * UIExplorerPage — Deep Navigation Walker
 * ────────────────────────────────────────
 * Visits every tab in the Subaru OneApp bottom navigation bar and
 * recursively explores as many sub-screens as possible within each tab.
 *
 * Strategy
 * ────────
 * 1. For each bottom-nav tab (Dashboard, Service, Pay, Shop, Find):
 *    a. Tap the tab → dump the landing screen
 *    b. Discover all tappable navigation items on that screen
 *       (buttons, cells, menu rows, quick-action cards)
 *    c. For each discovered item:
 *       - Tap it → wait for screen to settle → dump
 *       - Recursively discover further tappable items (up to MAX_DEPTH)
 *       - Navigate back after each item
 *    d. After exhausting the tab, return to Dashboard
 *
 * 2. A visited-screen registry (based on page-source hash) prevents
 *    infinite loops — if we land on a screen already seen we skip it.
 *
 * 3. Navigation back — exhaustive escape sequence (in order):
 *    a. Standard iOS Back button / chevron in nav bar
 *    b. Chevron-down arrow at top of bottom-sheet modals (e.g. Vehicles sheet)
 *    c. Swipe down (bottom-sheet dismiss)
 *    d. Swipe left (pop navigation stack on some screens)
 *    e. Swipe right (edge-swipe back gesture)
 *    f. driver.navigate().back()
 *    g. Check after each attempt — if Dashboard is visible, stop immediately
 *    h. Repeat swipe sequence up to 3 times before giving up and force-tapping Dashboard tab
 *
 * Output
 * ──────
 * logs/ui-elements/<timestamp>/
 *   MASTER_REPORT.txt
 *   <NN>_<TabName>_<ScreenName>/
 *     raw.xml
 *     elements.txt
 */
public class UIExplorerPage extends BasePage {

    // ── Tuning constants ──────────────────────────────────────────────────────

    /** Maximum depth of recursive sub-screen exploration per tab. */
    private static final int MAX_DEPTH = 4;

    /** How many tappable items to explore per screen (prevents runaway exploration). */
    private static final int MAX_ITEMS_PER_SCREEN = 12;

    /** ms to wait after tapping an item before dumping the screen. */
    private static final long SETTLE_MS = 2500;

    /** ms to wait after tapping a tab. */
    private static final long TAB_SETTLE_MS = 3000;

    // ── Bottom navigation tab accessibility IDs ───────────────────────────────
    // These are the confirmed IDs from prior page-source dumps.
    // We also probe label-based fallbacks so the explorer self-heals if IDs change.
    private static final String TAB_DASHBOARD = "BottomTabBar_dashboardTab";
    private static final String TAB_SERVICE   = "BottomTabBar_serviceTab";

    // Pay / Shop / Find tabs — IDs to be discovered; fallback to label search.
    // We list every plausible name we might see in this app.
    private static final List<String[]> TAB_DEFINITIONS = Arrays.asList(
        // { displayName, knownAccessibilityId, fallbackLabel1, fallbackLabel2, … }
        new String[]{ "Dashboard", "BottomTabBar_dashboardTab", "Dashboard", "Home"  },
        new String[]{ "Service",   "BottomTabBar_serviceTab",   "Service",   "Services" },
        new String[]{ "Pay",       "BottomTabBar_payTab",       "Pay",       "Payment", "Payments" },
        new String[]{ "Shop",      "BottomTabBar_shopTab",      "Shop",      "Shopping", "Store"  },
        new String[]{ "Find",      "BottomTabBar_findTab",      "Find",      "Locate",  "Dealers" }
    );

    // ── XPath patterns for "tappable navigation items" ────────────────────────
    // These are elements that, when tapped, are likely to open a new screen.
    // We intentionally exclude plain StaticText that has no interaction.
    private static final String TAPPABLE_XPATH =
        // Buttons that are NOT tab-bar tabs (to avoid switching tabs mid-exploration)
        "//XCUIElementTypeButton[not(contains(@name,'BottomTabBar'))"
        + " and not(contains(@name,'Tab'))"
        + " and (@name!='' or @label!='')]"
        + " | "
        // Table/Collection cells (typical list rows)
        + "//XCUIElementTypeCell[@enabled='true']"
        + " | "
        // Other tappable containers (cards, menu items)
        + "//XCUIElementTypeOther[@name!='' and @enabled='true' and string-length(@name)>3]";

    // ── State ─────────────────────────────────────────────────────────────────
    private final String projectRoot;
    private final List<String> visitedScreens = new ArrayList<>();

    /** Hashes of page sources already dumped — prevents re-dumping identical screens. */
    private final Set<Integer> seenPageSourceHashes = new LinkedHashSet<>();

    /** Global screen counter — gives each dump a sortable numeric prefix. */
    private int screenCounter = 0;

    public UIExplorerPage(AppiumDriver driver, String projectRoot) {
        super(driver);
        this.projectRoot = projectRoot;
    }

    public List<String> getVisitedScreens() { return visitedScreens; }

    // ═════════════════════════════════════════════════════════════════════════
    //  PUBLIC API
    // ═════════════════════════════════════════════════════════════════════════

    public String initOutputSession() {
        return UiElementExtractor.initSession(projectRoot);
    }

    /**
     * Main entry point.
     * Iterates over every defined bottom-nav tab in TAB_DEFINITIONS order,
     * explores each tab deeply, then returns to Dashboard.
     */
    public void exploreAllScreens() throws InterruptedException {

        for (String[] tabDef : TAB_DEFINITIONS) {
            String tabDisplayName = tabDef[0];
            logger.info("[UIExplorer] ══════ Starting tab: {} ══════", tabDisplayName);

            // Try to tap this tab
            boolean reached = tapTab(tabDef);
            if (!reached) {
                logger.warn("[UIExplorer] Tab '{}' not found — skipping", tabDisplayName);
                continue;
            }
            sleep(TAB_SETTLE_MS);

            // Dump the tab landing screen
            String landingName = pad() + tabDisplayName + "_Landing";
            dumpIfNew(landingName);

            // Deep-explore this tab
            exploreDepth(tabDisplayName, 1);

            // Return to Dashboard between tabs
            logger.info("[UIExplorer] Returning to Dashboard after tab: {}", tabDisplayName);
            returnToDashboard();
            sleep(TAB_SETTLE_MS);
        }

        // Final dump of Dashboard on return
        dumpIfNew("FINAL_Dashboard");

        // Write summary
        UiElementExtractor.finaliseMasterReport(visitedScreens);
        logger.info("[UIExplorer] ══════ Exploration complete — {} screens dumped ══════",
                visitedScreens.size());
        logger.info("[UIExplorer] Reports: {}", UiElementExtractor.getSessionDir());
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  CORE RECURSIVE EXPLORER
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Discovers all tappable items on the current screen and visits each one.
     * After visiting an item (and its children recursively), navigates back.
     *
     * @param parentName  human-readable name of the current screen (for logging)
     * @param depth       current recursion depth (stops at MAX_DEPTH)
     */
    private void exploreDepth(String parentName, int depth) throws InterruptedException {
        if (depth > MAX_DEPTH) {
            logger.info("[UIExplorer] Max depth ({}) reached at '{}' — going back", MAX_DEPTH, parentName);
            return;
        }

        // Snapshot tappable elements from the current page source
        // (we parse XML rather than using live elements to avoid StaleElement errors)
        List<TappableItem> items = discoverTappableItems();
        logger.info("[UIExplorer] [depth={}] '{}' — discovered {} tappable items",
                depth, parentName, items.size());

        int explored = 0;
        for (TappableItem item : items) {
            if (explored >= MAX_ITEMS_PER_SCREEN) {
                logger.info("[UIExplorer] [depth={}] Reached max items/screen ({}) — stopping here",
                        depth, MAX_ITEMS_PER_SCREEN);
                break;
            }

            logger.info("[UIExplorer] [depth={}] Tapping item: '{}'", depth, item.label());

            // Capture page source hash BEFORE tapping so we can detect if screen changed
            String sourceBefore = safePageSource();

            boolean tapped = tapItemByXPath(item);
            if (!tapped) {
                logger.info("[UIExplorer] Could not tap '{}' — skipping", item.label());
                continue;
            }
            sleep(SETTLE_MS);

            String sourceAfter = safePageSource();

            // If page source didn't change, this tap didn't navigate anywhere
            if (sourceAfter.equals(sourceBefore)) {
                logger.info("[UIExplorer] Screen unchanged after tapping '{}' — skipping", item.label());
                continue;
            }

            // New screen reached — dump it
            String screenName = pad() + parentName + "_" + sanitise(item.label());
            boolean isNew = dumpIfNew(screenName);

            if (isNew) {
                // Recurse deeper into this screen
                exploreDepth(screenName, depth + 1);
            }

            // Navigate back to where we were
            navigateBack(parentName);
            sleep(SETTLE_MS);

            explored++;
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PAGE SOURCE PARSING — discover tappable items without live elements
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Parses the current page source XML and returns a flat list of items
     * that are likely to trigger navigation when tapped.
     *
     * We parse XML rather than using driver.findElements() to avoid
     * StaleElementReferenceException during iteration.
     */
    private List<TappableItem> discoverTappableItems() {
        List<TappableItem> result = new ArrayList<>();
        String xml = safePageSource();
        if (xml.isEmpty()) return result;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            // Collect Buttons, Cells, and navigable Others
            collectTappable(doc.getDocumentElement(), result);

        } catch (Exception e) {
            logger.warn("[UIExplorer] Failed to parse page source for item discovery: {}", e.getMessage());
        }

        // Deduplicate by label
        Set<String> seen = new LinkedHashSet<>();
        List<TappableItem> deduped = new ArrayList<>();
        for (TappableItem item : result) {
            String key = item.type() + "|" + item.name() + "|" + item.label();
            if (!key.equals("||") && seen.add(key)) {
                deduped.add(item);
            }
        }
        return deduped;
    }

    private void collectTappable(org.w3c.dom.Node node, List<TappableItem> result) {
        if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
            Element el = (Element) node;
            String type    = el.getAttribute("type");
            String name    = el.getAttribute("name");
            String label   = el.getAttribute("label");
            String enabled = el.getAttribute("enabled");
            String visible = el.getAttribute("visible");

            // Skip disabled or invisible
            boolean isEnabled = !"false".equals(enabled);
            boolean isVisible = !"false".equals(visible);
            if (!isEnabled || !isVisible) {
                // still traverse children — a disabled container may have enabled children
                traverseChildren(node, result);
                return;
            }

            // Skip tab bar items (we handle tabs separately)
            if (name.contains("BottomTabBar") || label.equalsIgnoreCase("tab")) {
                traverseChildren(node, result);
                return;
            }

            // Skip items with no useful identifier
            String displayLabel = !label.isEmpty() ? label : name;
            if (displayLabel.isEmpty()) {
                traverseChildren(node, result);
                return;
            }

            // Skip extremely short labels that are likely icons/decorations
            if (displayLabel.length() < 2) {
                traverseChildren(node, result);
                return;
            }

            boolean isButton = type.contains("Button");
            boolean isCell   = type.contains("Cell");
            boolean isOther  = type.contains("Other") || type.contains("Group");

            if (isButton || isCell) {
                // Parse bounds for coordinate-tap fallback
                int x = parseInt(el.getAttribute("x"));
                int y = parseInt(el.getAttribute("y"));
                int w = parseInt(el.getAttribute("width"));
                int h = parseInt(el.getAttribute("height"));
                result.add(new TappableItem(type, name, displayLabel, x, y, w, h));
                // Don't traverse children of buttons/cells (they are leaf interactions)
                return;
            }

            if (isOther && name.length() > 3) {
                int x = parseInt(el.getAttribute("x"));
                int y = parseInt(el.getAttribute("y"));
                int w = parseInt(el.getAttribute("width"));
                int h = parseInt(el.getAttribute("height"));
                result.add(new TappableItem(type, name, displayLabel, x, y, w, h));
                // Still traverse children — there may be buttons inside
            }
        }
        traverseChildren(node, result);
    }

    private void traverseChildren(org.w3c.dom.Node node, List<TappableItem> result) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            collectTappable(children.item(i), result);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  TAP HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Tries to tap the bottom-nav tab described by tabDef.
     * Attempts: (1) known accessibility ID, (2) fallback labels.
     */
    private boolean tapTab(String[] tabDef) {
        // tabDef[1] = known ID, tabDef[2..n] = fallback labels
        for (int i = 1; i < tabDef.length; i++) {
            String candidate = tabDef[i];
            try {
                WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(4));
                WebElement el = w.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@name='" + candidate + "'] | //*[@label='" + candidate + "']")));
                el.click();
                logger.info("[UIExplorer] Tapped tab '{}' via '{}'", tabDef[0], candidate);
                return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    /**
     * Taps a TappableItem by locating it via accessibility ID first,
     * then by label, then by coordinate tap as final fallback.
     */
    private boolean tapItemByXPath(TappableItem item) {
        // Strategy 1: accessibility name
        if (!item.name().isEmpty()) {
            try {
                List<WebElement> els = driver.findElements(
                    By.xpath("//" + xcuiType(item.type()) + "[@name='" + escapeSingle(item.name()) + "']"));
                if (!els.isEmpty() && els.get(0).isDisplayed()) {
                    els.get(0).click();
                    return true;
                }
            } catch (Exception ignored) {}
        }

        // Strategy 2: label
        if (!item.label().isEmpty()) {
            try {
                List<WebElement> els = driver.findElements(
                    By.xpath("//" + xcuiType(item.type()) + "[@label='" + escapeSingle(item.label()) + "']"));
                if (!els.isEmpty() && els.get(0).isDisplayed()) {
                    els.get(0).click();
                    return true;
                }
            } catch (Exception ignored) {}
        }

        // Strategy 3: coordinate tap at element centre
        if (item.w() > 0 && item.h() > 0) {
            try {
                int cx = item.x() + item.w() / 2;
                int cy = item.y() + item.h() / 2;
                tapCoordinate(cx, cy);
                logger.info("[UIExplorer] Tapped '{}' via coordinate ({},{})", item.label(), cx, cy);
                return true;
            } catch (Exception ignored) {}
        }

        return false;
    }

    /** Performs a coordinate tap using W3C Actions (Appium 2 compatible). */
    private void tapCoordinate(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence seq = new Sequence(finger, 1);
        seq.addAction(finger.createPointerMove(Duration.ZERO,
            PointerInput.Origin.viewport(), x, y));
        seq.addAction(finger.createPointerDown(
            PointerInput.MouseButton.LEFT.asArg()));
        seq.addAction(finger.createPointerUp(
            PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(java.util.Arrays.asList(seq));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  NAVIGATION — BACK (exhaustive escape sequence)
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Full escape sequence for getting back from ANY screen — including
     * bottom-sheet modals (like the Vehicles sheet) that have no standard
     * iOS Back button.
     *
     * Order of attempts per iteration:
     *  1. Standard Back button / chevron in nav bar
     *  2. Chevron-down arrow button (bottom-sheet dismiss handle)
     *  3. Swipe down (full-screen dismiss gesture)
     *  4. Swipe left  (swipe back from right edge)
     *  5. Swipe right (some custom nav patterns)
     *  6. driver.navigate().back()
     *
     * After each gesture we wait 1.5 s and check if the screen changed.
     * If Dashboard is reached at any point, we stop immediately.
     * If all else fails after MAX_BACK_ATTEMPTS we force-tap the Dashboard tab.
     */
    private static final int MAX_BACK_ATTEMPTS = 3;

    private void navigateBack(String fromScreen) {
        logger.info("[UIExplorer] Attempting to navigate back from '{}'", fromScreen);
        String sourceBefore = safePageSource();

        for (int attempt = 1; attempt <= MAX_BACK_ATTEMPTS; attempt++) {
            logger.info("[UIExplorer] Back attempt {}/{} from '{}'", attempt, MAX_BACK_ATTEMPTS, fromScreen);

            // ── 1. Standard iOS Back button / chevron in navigation bar ──────
            if (tryBackButton(fromScreen)) {
                sleep(1500);
                if (screenChanged(sourceBefore) || isDashboardVisible()) return;
            }

            // ── 2. Chevron-down (▼) arrow — bottom-sheet dismiss handle ──────
            // The Vehicles sheet shows a chevron.down image/button at the very top.
            if (tryChevronDown(fromScreen)) {
                sleep(1500);
                if (screenChanged(sourceBefore) || isDashboardVisible()) return;
            }

            // ── 3. Swipe DOWN — dismisses bottom sheets ───────────────────────
            logger.info("[UIExplorer] [attempt {}] Swiping DOWN", attempt);
            swipeDown();
            sleep(1500);
            if (screenChanged(sourceBefore) || isDashboardVisible()) return;

            // ── 4. Swipe LEFT — pops nav stack on some screens ───────────────
            logger.info("[UIExplorer] [attempt {}] Swiping LEFT", attempt);
            swipeLeft();
            sleep(1500);
            if (screenChanged(sourceBefore) || isDashboardVisible()) return;

            // ── 5. Swipe RIGHT — edge-swipe back ─────────────────────────────
            logger.info("[UIExplorer] [attempt {}] Swiping RIGHT", attempt);
            swipeRight();
            sleep(1500);
            if (screenChanged(sourceBefore) || isDashboardVisible()) return;

            // ── 6. driver.navigate().back() ───────────────────────────────────
            try {
                driver.navigate().back();
                logger.info("[UIExplorer] navigate().back() from '{}'", fromScreen);
            } catch (Exception e) {
                logger.warn("[UIExplorer] navigate().back() failed: {}", e.getMessage());
            }
            sleep(1500);
            if (screenChanged(sourceBefore) || isDashboardVisible()) return;

            // Update sourceBefore for next round (we may have moved partially)
            sourceBefore = safePageSource();
        }

        // ── Last resort: force-tap the Dashboard tab ──────────────────────────
        logger.warn("[UIExplorer] All back strategies exhausted — force-tapping Dashboard tab");
        forceDashboardTab();
        sleep(2000);
    }

    // ── Back strategy helpers ─────────────────────────────────────────────────

    /** Taps the standard iOS Back button or chevron.left in the nav bar. */
    private boolean tryBackButton(String fromScreen) {
        try {
            List<WebElement> btns = driver.findElements(By.xpath(
                "//XCUIElementTypeNavigationBar//XCUIElementTypeButton[1]"
                + " | //XCUIElementTypeButton[@name='Back' or @name='back'"
                + "   or @name='chevron.left' or @label='Back']"
                + " | //XCUIElementTypeImage[@name='chevron.left']"));
            if (!btns.isEmpty()) {
                btns.get(0).click();
                logger.info("[UIExplorer] Back button tapped from '{}'", fromScreen);
                return true;
            }
        } catch (Exception e) {
            logger.debug("[UIExplorer] Back button not found: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Taps the chevron-down (▼) dismiss handle at the top of bottom-sheet modals.
     * In the Vehicles sheet this appears as a small down-arrow above the title.
     * We look for:
     *  - XCUIElementTypeButton with name/label containing 'chevron.down' or 'down'
     *  - XCUIElementTypeImage named 'chevron.down'
     *  - Any element in the top 15% of the screen (y < screenH * 0.15) that is
     *    horizontally centred (x > screenW * 0.3 && x < screenW * 0.7)
     *    — this catches unlabelled dismiss handles
     */
    private boolean tryChevronDown(String fromScreen) {
        // Strategy A: named chevron-down element
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//XCUIElementTypeButton[contains(@name,'chevron.down')"
                + "  or contains(@label,'chevron.down')"
                + "  or @name='dismiss' or @name='Dismiss']"
                + " | //XCUIElementTypeImage[contains(@name,'chevron.down')]"
                + " | //XCUIElementTypeOther[contains(@name,'handle') or contains(@name,'Handle')]"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[UIExplorer] Chevron-down tapped from '{}'", fromScreen);
                return true;
            }
        } catch (Exception e) {
            logger.debug("[UIExplorer] Chevron-down element not found: {}", e.getMessage());
        }

        // Strategy B: coordinate tap at top-centre of screen (where the ▼ handle lives)
        // iPhone 17 Pro: ~393 wide × ~852 tall in logical points.
        // The handle is at roughly (screenW/2, 140) — just below the status bar.
        try {
            Dimension size = driver.manage().window().getSize();
            int cx = size.width / 2;
            int cy = (int)(size.height * 0.13);  // ~13% down = handle zone
            logger.info("[UIExplorer] Tapping top-centre for chevron-down handle at ({}, {})", cx, cy);
            tapCoordinate(cx, cy);
            return true;
        } catch (Exception e) {
            logger.debug("[UIExplorer] Top-centre tap failed: {}", e.getMessage());
        }
        return false;
    }

    // ── Swipe gestures ────────────────────────────────────────────────────────

    /**
     * Swipe DOWN — starts at 40% height, ends at 90% height.
     * Dismisses bottom sheets and pull-down modals.
     */
    private void swipeDown() {
        performSwipe(0.5, 0.40, 0.5, 0.90, 400);
    }

    /**
     * Swipe LEFT — starts at 80% width, ends at 10% width.
     * Some screens navigate back with a left swipe.
     */
    private void swipeLeft() {
        performSwipe(0.80, 0.50, 0.10, 0.50, 400);
    }

    /**
     * Swipe RIGHT (edge swipe) — starts near the left edge (5%), ends at 80%.
     * Triggers iOS edge-swipe-back gesture.
     */
    private void swipeRight() {
        performSwipe(0.05, 0.50, 0.80, 0.50, 400);
    }

    /**
     * Performs a swipe from (startXRatio, startYRatio) to (endXRatio, endYRatio)
     * using W3C Pointer Actions. All ratios are 0.0–1.0 of screen dimensions.
     *
     * @param durationMs  how long the swipe gesture takes in ms (longer = slower = more natural)
     */
    private void performSwipe(double startXRatio, double startYRatio,
                               double endXRatio,   double endYRatio,
                               long durationMs) {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = (int)(size.width  * startXRatio);
            int startY = (int)(size.height * startYRatio);
            int endX   = (int)(size.width  * endXRatio);
            int endY   = (int)(size.height * endYRatio);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(
                PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(durationMs),
                PointerInput.Origin.viewport(), endX, endY));
            swipe.addAction(finger.createPointerUp(
                PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(java.util.Arrays.asList(swipe));
        } catch (Exception e) {
            logger.warn("[UIExplorer] Swipe failed: {}", e.getMessage());
        }
    }

    // ── Screen-change detection ───────────────────────────────────────────────

    /** Returns true if the current page source is different from the given snapshot. */
    private boolean screenChanged(String previousSource) {
        String current = safePageSource();
        boolean changed = !current.equals(previousSource);
        if (changed) logger.info("[UIExplorer] Screen changed — back navigation succeeded");
        return changed;
    }

    /**
     * Returns true if the Dashboard is currently visible.
     * Uses the Remote/Status/Health buttons or the Dashboard tab as anchor.
     */
    private boolean isDashboardVisible() {
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//XCUIElementTypeButton[@name='Remote_Status_Health_View']"
                + " | //*[@name='" + TAB_DASHBOARD + "' and @selected='true']"));
            if (!els.isEmpty()) {
                logger.info("[UIExplorer] Dashboard detected — stopping back navigation");
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    /** Returns to the Dashboard tab. */
    private void returnToDashboard() {
        if (isDashboardVisible()) {
            logger.info("[UIExplorer] Already on Dashboard");
            return;
        }
        forceDashboardTab();
    }

    /** Force-taps the Dashboard bottom-nav tab regardless of current screen. */
    private void forceDashboardTab() {
        try {
            WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(4));
            WebElement dash = w.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@name='" + TAB_DASHBOARD + "']"
                       + " | //*[@label='Dashboard' or @label='Home']"
                       + " | //XCUIElementTypeTabBar//XCUIElementTypeButton[1]")));
            dash.click();
            logger.info("[UIExplorer] Tapped Dashboard tab");
        } catch (Exception e) {
            logger.warn("[UIExplorer] Dashboard tab not tappable: {}", e.getMessage());
            // Absolute last resort
            try { driver.navigate().back(); } catch (Exception ignored) {}
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  DUMP HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Dumps the current screen only if its page-source hash has not been seen before.
     * Returns true if a new dump was written, false if screen was already seen.
     */
    private boolean dumpIfNew(String screenName) {
        String src = safePageSource();
        int hash = src.hashCode();

        if (seenPageSourceHashes.contains(hash)) {
            logger.info("[UIExplorer] Screen '{}' already seen (hash match) — skipping dump", screenName);
            return false;
        }
        seenPageSourceHashes.add(hash);

        logger.info("[UIExplorer] Dumping NEW screen: {}", screenName);
        UiElementExtractor.dumpScreen(screenName, src, projectRoot);
        visitedScreens.add(screenName);
        return true;
    }

    private String safePageSource() {
        try {
            return driver.getPageSource();
        } catch (Exception e) {
            logger.warn("[UIExplorer] getPageSource() failed: {}", e.getMessage());
            return "";
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  UTILITIES
    // ═════════════════════════════════════════════════════════════════════════

    /** Zero-padded screen counter prefix for alphabetical sort in Finder. */
    private String pad() {
        screenCounter++;
        return String.format("%03d_", screenCounter);
    }

    private static String sanitise(String s) {
        return s.replaceAll("[^A-Za-z0-9_\\-]", "_").replaceAll("_+", "_");
    }

    private static String xcuiType(String rawType) {
        if (rawType == null || rawType.isEmpty()) return "XCUIElementTypeOther";
        return rawType.startsWith("XCUIElementType") ? rawType : "XCUIElementType" + rawType;
    }

    private static String escapeSingle(String s) {
        return s.replace("'", "\\'");
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  DATA RECORD
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Lightweight record of a tappable UI item discovered from the page source XML.
     * Holds all data needed to tap it without holding a live WebElement reference
     * (which would go stale).
     */
    private static class TappableItem {
        private final String type;
        private final String name;
        private final String label;
        private final int x, y, w, h;

        TappableItem(String type, String name, String label, int x, int y, int w, int h) {
            this.type  = type;
            this.name  = name;
            this.label = label;
            this.x = x; this.y = y; this.w = w; this.h = h;
        }

        String type()  { return type; }
        String name()  { return name; }
        String label() { return label; }
        int x() { return x; }
        int y() { return y; }
        int w() { return w; }
        int h() { return h; }

        @Override public String toString() {
            return type + "[name=" + name + ", label=" + label + "]";
        }
    }
}
