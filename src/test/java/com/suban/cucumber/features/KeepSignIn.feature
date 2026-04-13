Feature: Keep Me Signed In Session Persistence
  As a logged-in Subaru OneApp user
  I want to control whether my session persists between app launches
  So that I can balance convenience and security

  Background:
    Given the mobile application is launched
    And I am on the home screen
    And I proceed to Login Process with profile "21mmEVDummy1"

  # OB_E2E_040 — Highest Priority
  @KeepSignIn @keepSignedIn @sessionPersistence @OB_E2E_040
  Scenario: OB_E2E_040 - Enable Keep Me Signed In and Verify Session Persistence
    When I navigate to Account Settings
    And I navigate to Security Settings
    And I enable the Keep Me Signed In toggle
    Then the Keep Me Signed In toggle should be shown as enabled
    When I fully close and kill the app
    And I relaunch the app
    Then I should already be signed in and land directly on the dashboard
    And I should not see a Sign In prompt
    When I restart the device and relaunch the app
    Then my session should still persist
    And I should land directly on the dashboard without a sign-in prompt

  # OB_E2E_041 — Highest Priority
  @KeepSignIn @keepSignedIn @sessionExpiry @OB_E2E_041
  Scenario: OB_E2E_041 - Disable Keep Me Signed In and Verify Session Expiry
    When I navigate to Account Settings
    And I navigate to Security Settings
    And I toggle the Keep Me Signed In to OFF
    Then the Keep Me Signed In toggle should be shown as disabled
    When I fully close and kill the app
    And I relaunch the app
    Then I should be redirected to the Sign In or Welcome Back screen
    And my session should not have persisted
    When I sign in with my credentials
    Then I should be navigated to the app dashboard successfully

  # OB_E2E_042 — Highest Priority
  @KeepSignIn @keepSignedIn @biometric @OB_E2E_042
  Scenario: OB_E2E_042 - Keep Me Signed In Interaction with Biometric and PIN
    Given Keep Me Signed In is enabled and biometric authentication is enabled
    When I fully close and relaunch the app
    Then I should see the biometric authentication prompt
    When I authenticate via Face ID or Touch ID
    Then I should land on the dashboard confirming the session was maintained
    And I should not have been required to enter full credentials
    When I disable biometric authentication while keeping Keep Me Signed In ON
    And I fully close and relaunch the app
    Then the app should open directly to the dashboard with no authentication prompt of any kind
