package com.suban.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * UiElementExtractor
 * ──────────────────
 * Parses the raw Appium XML page source and extracts every UI element's
 * accessibility attributes into human-readable report files.
 *
 * Output per screen (written to logs/ui-elements/<timestamp>/<screenName>/):
 *   raw.xml          — pretty-printed full page source
 *   elements.txt     — structured list of every element with all attributes
 *
 * A master report (logs/ui-elements/<timestamp>/MASTER_REPORT.txt) is
 * appended after every screen dump and summarised at the end.
 *
 * Attributes captured for each element:
 *   type, name (accessibility ID), label, value, enabled, visible,
 *   x, y, width, height  (all from the XML attributes present on the node)
 */
public class UiElementExtractor {

    private static final Logger logger = LoggerFactory.getLogger(UiElementExtractor.class);

    /** Attributes we always print first (in this order) for readability. */
    private static final List<String> PRIORITY_ATTRS = Arrays.asList(
            "type", "name", "label", "value", "enabled", "visible",
            "accessible", "index", "x", "y", "width", "height"
    );

    /** Elements whose type contains these substrings are treated as containers and indented. */
    private static final List<String> CONTAINER_TYPES = Arrays.asList(
            "Application", "Window", "Other", "ScrollView", "Table",
            "CollectionView", "Cell", "Group", "NavigationBar", "TabBar",
            "Toolbar", "Alert", "Sheet", "LayoutItem", "WebView"
    );

    // ── Session-scoped output directory (set once per test run) ──────────────
    private static String sessionDir = null;

    /** Call once at the start of a UI Explorer test run to set up the output directory. */
    public static String initSession(String projectRoot) {
        String ts = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        sessionDir = projectRoot + "/logs/ui-elements/" + ts;
        try {
            Files.createDirectories(Paths.get(sessionDir));
            logger.info("[UiElementExtractor] Session output directory: {}", sessionDir);
        } catch (Exception e) {
            logger.error("[UiElementExtractor] Failed to create session directory: {}", e.getMessage(), e);
        }
        return sessionDir;
    }

    /** Returns the current session directory (null if initSession() has not been called). */
    public static String getSessionDir() {
        return sessionDir;
    }

    // ── Main API ──────────────────────────────────────────────────────────────

    /**
     * Dumps the page source for a named screen.
     *
     * @param screenName   human-readable name for this screen (e.g. "01_Dashboard")
     * @param pageSource   raw XML string from driver.getPageSource()
     * @param projectRoot  absolute path to the Maven project root (used to locate logs/)
     * @return             path to the directory where files were written
     */
    public static String dumpScreen(String screenName, String pageSource, String projectRoot) {
        if (sessionDir == null) {
            initSession(projectRoot);
        }

        // Sanitise screen name for use as a directory name
        String safeName = screenName.replaceAll("[^A-Za-z0-9_\\-]", "_");
        String screenDir = sessionDir + "/" + safeName;

        try {
            Files.createDirectories(Paths.get(screenDir));
        } catch (Exception e) {
            logger.error("[UiElementExtractor] Cannot create screen directory {}: {}", screenDir, e.getMessage());
            return screenDir;
        }

        logger.info("[UiElementExtractor] Dumping screen: {} → {}", screenName, screenDir);

        // 1. Write pretty-printed raw XML
        writeRawXml(pageSource, screenDir + "/raw.xml");

        // 2. Parse and write structured elements report
        List<UiElement> elements = parseElements(pageSource);
        writeElementsReport(screenName, elements, screenDir + "/elements.txt");

        // 3. Append to master report
        appendToMasterReport(screenName, elements, sessionDir + "/MASTER_REPORT.txt");

        logger.info("[UiElementExtractor] Screen '{}' — {} elements extracted", screenName, elements.size());
        return screenDir;
    }

    /**
     * Writes a final summary section to the master report (call once at the end of the test).
     */
    public static void finaliseMasterReport(List<String> screenNames) {
        if (sessionDir == null) return;
        String masterPath = sessionDir + "/MASTER_REPORT.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(masterPath, true))) {
            pw.println();
            pw.println("══════════════════════════════════════════════════════════════════════");
            pw.println("  EXPLORATION COMPLETE");
            pw.println("══════════════════════════════════════════════════════════════════════");
            pw.println("  Screens visited (" + screenNames.size() + "):");
            for (String s : screenNames) {
                pw.println("    • " + s);
            }
            pw.println();
            pw.println("  To find an element quickly, search this file for:");
            pw.println("    name=       → accessibility ID  (use with AppiumBy.accessibilityId)");
            pw.println("    label=      → display text      (use with @label in predicate string)");
            pw.println("    type=       → XCUIElementType   (use in XPath: //XCUIElementType<type>)");
            pw.println();
            pw.println("  Raw XML per screen: logs/ui-elements/<timestamp>/<ScreenName>/raw.xml");
            pw.println("══════════════════════════════════════════════════════════════════════");
        } catch (Exception e) {
            logger.error("[UiElementExtractor] Error writing final summary: {}", e.getMessage());
        }
    }

    // ── Parsing ───────────────────────────────────────────────────────────────

    /** Parses the XML page source and returns a flat list of UiElement records. */
    static List<UiElement> parseElements(String pageSource) {
        List<UiElement> result = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Disable external DTD resolution (security + speed)
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(
                    pageSource.getBytes(StandardCharsets.UTF_8)));
            traverseNode(doc.getDocumentElement(), 0, result);
        } catch (Exception e) {
            logger.error("[UiElementExtractor] XML parse error: {}", e.getMessage(), e);
        }
        return result;
    }

    /** Recursively walks the DOM tree, collecting every element node. */
    private static void traverseNode(Node node, int depth, List<UiElement> result) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element el = (Element) node;
            UiElement ui = new UiElement();
            ui.depth = depth;
            ui.tagName = el.getTagName();

            NamedNodeMap attrs = el.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                ui.attributes.put(attr.getNodeName(), attr.getNodeValue());
            }

            // Derive type: XCUITest puts type in @type attr; if missing, use tag name
            ui.type = ui.attributes.getOrDefault("type", ui.tagName);
            ui.name = ui.attributes.getOrDefault("name", "");
            ui.label = ui.attributes.getOrDefault("label", "");

            result.add(ui);
        }

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            traverseNode(children.item(i), depth + 1, result);
        }
    }

    // ── File Writers ─────────────────────────────────────────────────────────

    /** Writes the page source as pretty-printed XML. */
    private static void writeRawXml(String rawXml, String outputPath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(
                    rawXml.getBytes(StandardCharsets.UTF_8)));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));

            Files.write(Paths.get(outputPath), sw.toString().getBytes(StandardCharsets.UTF_8));
            logger.info("[UiElementExtractor] Raw XML written: {}", outputPath);
        } catch (Exception e) {
            // Fallback: write the raw string as-is
            logger.warn("[UiElementExtractor] Pretty-print failed, writing raw: {}", e.getMessage());
            try {
                Files.write(Paths.get(outputPath), rawXml.getBytes(StandardCharsets.UTF_8));
            } catch (Exception ex) {
                logger.error("[UiElementExtractor] Cannot write raw XML: {}", ex.getMessage());
            }
        }
    }

    /** Writes the structured elements list to a text report. */
    private static void writeElementsReport(String screenName, List<UiElement> elements, String outputPath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            printReportHeader(pw, screenName, elements.size());

            // Group elements by type for quick lookup section
            printQuickLookupSection(pw, elements);

            // Full detail section
            pw.println();
            pw.println("──────────────────────────────────────────────────────────────────────");
            pw.println("  FULL ELEMENT LIST  (indented to reflect DOM depth)");
            pw.println("──────────────────────────────────────────────────────────────────────");
            pw.println();

            int idx = 1;
            for (UiElement el : elements) {
                String indent = "  ".repeat(Math.min(el.depth, 20));
                pw.printf("%s[%d] type=%-40s name=%-50s label=%-40s%n",
                        indent, idx++,
                        quote(el.type),
                        quote(el.name),
                        quote(el.label));

                // Print remaining attributes (value, bounds, etc.)
                for (String key : PRIORITY_ATTRS) {
                    if (key.equals("type") || key.equals("name") || key.equals("label")) continue;
                    String val = el.attributes.get(key);
                    if (val != null && !val.isEmpty()) {
                        pw.printf("%s         %-12s = %s%n", indent, key, val);
                    }
                }
                // Any extra attributes not in PRIORITY_ATTRS
                for (Map.Entry<String, String> e : el.attributes.entrySet()) {
                    if (!PRIORITY_ATTRS.contains(e.getKey()) && !e.getValue().isEmpty()) {
                        pw.printf("%s         %-12s = %s%n", indent, e.getKey(), e.getValue());
                    }
                }
                pw.println();
            }
        } catch (Exception e) {
            logger.error("[UiElementExtractor] Error writing elements report: {}", e.getMessage(), e);
        }
    }

    /** Appends this screen's data to the master report. */
    private static void appendToMasterReport(String screenName, List<UiElement> elements, String masterPath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(masterPath, true))) {
            pw.println();
            pw.println("══════════════════════════════════════════════════════════════════════");
            pw.printf ("  SCREEN: %s  (%d elements)%n", screenName, elements.size());
            pw.println("══════════════════════════════════════════════════════════════════════");
            pw.println();

            // Only actionable elements (those with a name or label) go in the master
            int actionable = 0;
            for (UiElement el : elements) {
                boolean hasName  = !el.name.isEmpty();
                boolean hasLabel = !el.label.isEmpty();
                if (!hasName && !hasLabel) continue;

                actionable++;
                pw.printf("  [%-45s] name=%-50s label=%s%n",
                        el.type, quote(el.name), quote(el.label));

                // Bounds and value on the same indent
                String x = el.attributes.getOrDefault("x", "");
                String y = el.attributes.getOrDefault("y", "");
                String w = el.attributes.getOrDefault("width", "");
                String h = el.attributes.getOrDefault("height", "");
                String v = el.attributes.getOrDefault("value", "");
                if (!x.isEmpty()) {
                    pw.printf("                  bounds=(%s,%s)  size=%sx%s%s%n",
                            x, y, w, h,
                            v.isEmpty() ? "" : "  value=" + quote(v));
                }

                // Locator hints
                if (hasName) {
                    pw.println("                  → AppiumBy.accessibilityId(\"" + el.name + "\")");
                    pw.println("                  → //" + xcuiType(el.type) + "[@name='" + el.name + "']");
                }
                if (hasLabel && !el.label.equals(el.name)) {
                    pw.println("                  → //" + xcuiType(el.type) + "[@label='" + el.label + "']");
                }
                pw.println();
            }
            pw.println("  Actionable elements (with name or label): " + actionable + " / " + elements.size());
        } catch (Exception e) {
            logger.error("[UiElementExtractor] Error appending to master report: {}", e.getMessage(), e);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static void printReportHeader(PrintWriter pw, String screenName, int totalCount) {
        String line = "═".repeat(70);
        pw.println(line);
        pw.println("  UI ELEMENT REPORT");
        pw.println("  Screen : " + screenName);
        pw.println("  Date   : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        pw.println("  Total  : " + totalCount + " elements");
        pw.println(line);
    }

    private static void printQuickLookupSection(PrintWriter pw, List<UiElement> elements) {
        pw.println();
        pw.println("──────────────────────────────────────────────────────────────────────");
        pw.println("  QUICK LOOKUP — elements with accessibility IDs (name attribute)");
        pw.println("  Copy these directly into your Page Object @iOSXCUITFindBy annotations");
        pw.println("──────────────────────────────────────────────────────────────────────");
        pw.println();

        boolean found = false;
        for (UiElement el : elements) {
            if (!el.name.isEmpty()) {
                found = true;
                pw.printf("  %-45s  name=\"%s\"%n", xcuiType(el.type), el.name);
                if (!el.label.isEmpty() && !el.label.equals(el.name)) {
                    pw.printf("  %-45s  label=\"%s\"%n", "", el.label);
                }
            }
        }
        if (!found) {
            pw.println("  (no elements with accessibility name found on this screen)");
        }

        pw.println();
        pw.println("──────────────────────────────────────────────────────────────────────");
        pw.println("  QUICK LOOKUP — elements with visible labels (static text / buttons)");
        pw.println("──────────────────────────────────────────────────────────────────────");
        pw.println();

        found = false;
        for (UiElement el : elements) {
            if (!el.label.isEmpty() && el.name.isEmpty()) {
                found = true;
                pw.printf("  %-45s  label=\"%s\"%n", xcuiType(el.type), el.label);
            }
        }
        if (!found) {
            pw.println("  (all labelled elements also have a name attribute)");
        }
    }

    /** Wraps a string value in double-quotes; returns (empty) if blank. */
    private static String quote(String s) {
        return s.isEmpty() ? "(empty)" : "\"" + s + "\"";
    }

    /**
     * Maps the raw type string from the XML to the XCUIElementType prefix
     * used in XPath. Examples:
     *   "XCUIElementTypeButton" → "XCUIElementTypeButton"  (already prefixed)
     *   "Button"                → "XCUIElementTypeButton"
     */
    private static String xcuiType(String rawType) {
        if (rawType == null || rawType.isEmpty()) return "XCUIElementTypeOther";
        if (rawType.startsWith("XCUIElementType")) return rawType;
        return "XCUIElementType" + rawType;
    }

    // ── Data class ────────────────────────────────────────────────────────────

    /** Lightweight record of a single UI element node. */
    public static class UiElement {
        public int depth;
        public String tagName   = "";
        public String type      = "";
        public String name      = "";   // accessibility ID
        public String label     = "";   // display text
        public Map<String, String> attributes = new LinkedHashMap<>();
    }
}
