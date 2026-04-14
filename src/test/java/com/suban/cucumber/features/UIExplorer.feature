Feature: UI Element Explorer — Deep Navigation Walker
  As a framework developer
  I want to automatically walk every tab and sub-screen of the Subaru OneApp
  And dump the full XML accessibility tree for each screen
  So that I can discover all element IDs, names, and labels to use in page objects

  # ──────────────────────────────────────────────────────────────────────────
  # Run this with:
  #   ./run.sh explore
  # Or manually:
  #   mvn clean test -Dcucumber.filter.tags="@uiexplorer"
  #
  # What it does:
  #   1. Logs in with 21mmEVDummy1
  #   2. Visits each bottom-nav tab: Dashboard, Service, Pay, Shop, Find
  #   3. On each tab, recursively taps every button / cell / card
  #      and dumps the XML for each new screen reached (up to 4 levels deep)
  #   4. Navigates back after each sub-screen, then returns to Dashboard
  #
  # Output — saved to logs/ui-elements/<timestamp>/:
  #   MASTER_REPORT.txt              ← start here (all screens, all elements)
  #   001_Dashboard_Landing/
  #     raw.xml                      ← full pretty-printed Appium XML
  #     elements.txt                 ← parsed element list with locator hints
  #   002_Service_Landing/
  #   003_Service_Landing_<Item>/
  #   …  (numbered so they sort in visit order)
  #
  # Open in Finder after run:
  #   open logs/ui-elements/<timestamp>
  # ──────────────────────────────────────────────────────────────────────────

  @uiexplorer @explore
  Scenario: Deep-explore all app screens and dump UI element accessibility trees
    Given the mobile application is launched
    And I am on the home screen
    And I proceed to Login Process with profile "21mmEVDummy1"
    And I start the UI Explorer session
    And I explore all app screens and dump their UI elements
    Then the UI element report is saved to the logs folder
