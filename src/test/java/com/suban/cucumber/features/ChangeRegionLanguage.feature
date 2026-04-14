Feature: Region and Language Selection
  As a Subaru OneApp user
  I want to select my preferred region and language
  So that the app is displayed in my language throughout all screens

  Background:
    Given the mobile application is launched
    And I am on the home screen

  # OB_E2E_028 — Highest Priority
  @ChangeRegionLanguage @regionLanguage @onboarding @OB_E2E_028
  Scenario: OB_E2E_028 - Language and Region Selection During Onboarding
    When the app presents the Region and Language selection screen
    And I select region "US"
    And I select language "en-us"
    And I tap Confirm on the region language screen
    Then the app UI should update to reflect the selected language
    When I proceed to Sign Up or Sign In
    Then the selected language should persist throughout the entire onboarding flow

  # OB_E2E_029 — Highest Priority
  @ChangeRegionLanguage @regionLanguage @postLogin @OB_E2E_029
  Scenario: OB_E2E_029 - Changing Region and Language Post-Login
    Given I proceed to Login Process with profile "21mmEVDummy1"
    When I navigate to Account Settings
    And I navigate to Personal Info
    And I navigate to Preferred Language settings
    And I select a different language "French"
    And I tap Save on the language settings screen
    Then the app UI should refresh in the new selected language
    When I navigate through multiple screens
    Then the new language should persist across all screens
    When I sign out and sign back in with profile "21mmEVDummy1"
    Then my language preference should be retained after re-login
