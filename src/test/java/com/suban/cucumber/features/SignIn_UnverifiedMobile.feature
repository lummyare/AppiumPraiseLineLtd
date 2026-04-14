Feature: Sign-In with Unverified Mobile Number
  As a user with an unverified mobile number
  I want to complete mobile verification during sign-in
  So that I can fully access my dashboard and complete onboarding

  Background:
    Given the mobile application is launched
    And I am on the home screen

  # OB_E2E_011 — Highest Priority
  @SignIn_UnverifiedMobile @unverifiedMobile @OB_E2E_011
  Scenario: OB_E2E_011 - Sign-In with Unverified Mobile and Complete Verification Flow
    When I tap the Sign In button on the Welcome Back screen
    And I switch the input mode to Phone Number
    And I enter my registered phone number "5551234567"
    And I enter my password "Test@123"
    And I tap the Sign In submit button
    Then the system should detect an unverified mobile number
    And I should be redirected to the Phone Verification screen
    When I receive and enter the SMS verification code
    And I tap the Verify button
    Then I should be navigated to the app dashboard
    And the mobile number should now be marked as verified

  # OB_E2E_012 — Highest Priority
  @SignIn_UnverifiedMobile @unverifiedMobile @resend @OB_E2E_012
  Scenario: OB_E2E_012 - Unverified Mobile Verification with Resend and Throttling
    Given I am on the Phone Verification screen after sign-in
    When I tap Resend Code on the phone verification screen
    Then I should see a toast confirming a new code was sent
    When I rapidly tap Resend Code multiple times
    Then I should see a throttle warning message asking me to wait
    When the throttle period expires
    And I enter the latest valid SMS verification code
    And I tap the Verify button
    Then I should be navigated to the app dashboard

  # OB_E2E_013 — Highest Priority
  @SignIn_UnverifiedMobile @unverifiedMobile @fullOnboarding @OB_E2E_013
  Scenario: OB_E2E_013 - Full Onboarding Completion After Mobile Verification
    When I tap the Sign In button on the Welcome Back screen
    And I switch the input mode to Phone Number
    And I enter my registered phone number "5551234567"
    And I enter my password "Test@123"
    And I tap the Sign In submit button
    And the Phone Verification screen is displayed
    When I receive and enter the SMS verification code
    And I tap the Verify button
    Then I should be navigated to the Add Vehicle screen
    When I add a vehicle using a valid VIN "JF1ZNAA14E8703230"
    And I create and confirm a 5-digit PIN
    And I acknowledge the CVN Privacy and Connected Services terms
    Then I should be navigated to the main app dashboard
    And all active services should be displayed on the dashboard
