Feature: Biometric Authentication
  As a logged-in Subaru OneApp user
  I want to enable and use biometric authentication
  So that I can securely access the app without entering my password every time

  Background:
    Given the mobile application is launched
    And I am on the home screen
    And I proceed to Login Process with profile "21mmEVDummy1"

  # OB_E2E_037 — Highest Priority
  @BiometricLogin @biometric @OB_E2E_037
  Scenario: OB_E2E_037 - Enable and Use Biometric Authentication End-to-End
    When I navigate to Account Settings
    And I navigate to Security Settings
    And I enable the Biometric Authentication toggle
    And I authenticate with my current password or PIN to confirm the change
    Then the Biometric Authentication toggle should be shown as enabled
    When I sign out of the app
    And I relaunch the app
    Then I should see a biometric authentication prompt
    When I authenticate using Face ID or Touch ID
    Then I should be navigated to the app dashboard without entering a password
    And biometric login should be confirmed as active

  # OB_E2E_038 — Highest Priority
  @BiometricLogin @biometric @negativeTest @OB_E2E_038
  Scenario: OB_E2E_038 - Biometric Failure and Fallback to Password
    Given biometric authentication is enabled for my account
    When I relaunch the app and the biometric prompt appears
    And I present an unrecognized biometric
    Then I should see a biometric failure message
    When I repeat biometric failure until the maximum attempts are reached
    Then I should see a fallback prompt to enter password or PIN
    When I enter the correct password or PIN
    Then I should be navigated to the app dashboard
    And I should be able to access all features securely

  # OB_E2E_039 — Highest Priority
  @BiometricLogin @biometric @timeout @OB_E2E_039
  Scenario: OB_E2E_039 - Biometric Re-Authentication Based on Timeout Setting
    Given biometric authentication is enabled for my account
    When I log in via biometrics and use the app
    And I background the app for over one minute
    And I reopen the app
    Then I should immediately see the biometric re-authentication prompt
    When I authenticate successfully via biometric
    Then I should land on the dashboard
    When I change the Require Authentication setting to 15 minutes
    And I background the app and return within 15 minutes
    Then I should not see a re-authentication prompt
    When I wait beyond 15 minutes and reopen the app
    Then I should see the biometric re-authentication prompt again
    When I authenticate via biometric
    Then I should be confirmed on the dashboard with full access
