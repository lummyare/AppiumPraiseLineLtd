Feature: Sign-In on New or Unrecognized Device
  As a registered Subaru OneApp user signing in on a new device
  I want to complete additional device verification
  So that my account remains secure and the device becomes trusted

  Background:
    Given the mobile application is launched
    And I am on the home screen

  # OB_E2E_009 — Highest Priority
  @SignIn_NewDevice @newDevice @OB_E2E_009
  Scenario: OB_E2E_009 - Full Sign-In Flow on a New Device with Verification
    When I tap the Sign In button on the Welcome Back screen
    And I enter my registered email address "sub2_21mm@mail.tmnact.io"
    And I enter my password "Test@123"
    And I tap the Sign In submit button
    Then the system should detect a new unrecognized device
    And I should see a device verification prompt
    When I retrieve and enter the device verification code via email
    And I tap the Verify button
    Then I should be navigated to the app dashboard
    When I close and relaunch the app
    Then I should not see a device verification prompt again
    And I should land directly on the dashboard or sign in screen without reverification

  # OB_E2E_010 — Highest Priority
  @SignIn_NewDevice @newDevice @negativeTest @OB_E2E_010
  Scenario: OB_E2E_010 - New Device Sign-In with Invalid Code and Resend
    When I tap the Sign In button on the Welcome Back screen
    And I enter my registered email address "sub2_21mm@mail.tmnact.io"
    And I enter my password "Test@123"
    And I tap the Sign In submit button
    And the device verification screen is displayed
    When I enter an incorrect device verification code "000000"
    Then I should see a verification code error message
    When I enter incorrect device codes until the maximum attempts are reached
    Then I should see a verification lockout message
    When I tap Resend Code on the device verification screen
    Then I should receive a new valid verification code
    When I enter the correct new verification code
    And I tap the Verify button
    Then I should be navigated to the app dashboard
