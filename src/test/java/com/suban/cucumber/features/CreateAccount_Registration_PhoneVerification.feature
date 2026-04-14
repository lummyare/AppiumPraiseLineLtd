Feature: Phone Number Verification During Registration
  As a new Subaru OneApp user completing registration
  I want to verify my phone number via SMS
  So that I can proceed to the next registration step

  Background:
    Given the mobile application is launched
    And I am on the home screen

  # OB_E2E_021 — Highest Priority
  @CreateAccount_Registration_PhoneVerification @phoneVerification @registration @OB_E2E_021
  Scenario: OB_E2E_021 - Phone Number Verification During Registration
    Given I have completed the Sign Up name and email fields on the registration screen
    When the app navigates to the Phone Verification screen
    And I enter a new valid mobile number "5551230001"
    And I tap Send Code on the phone verification screen
    Then I should receive an SMS verification code
    When I enter the SMS verification code on the verification screen
    And I tap the Verify button
    Then I should be navigated to the password creation step
    When I complete the remaining registration steps
    Then I should successfully access the dashboard

  # OB_E2E_022 — Highest Priority
  @CreateAccount_Registration_PhoneVerification @phoneVerification @negativeTest @resend @OB_E2E_022
  Scenario: OB_E2E_022 - Phone Verification Error Handling and Resend Flow
    Given I am on the Phone Verification screen during registration
    When I enter an incorrect SMS code "999999"
    Then I should see an incorrect code error message
    When I enter incorrect codes until the maximum attempt limit is reached
    Then I should see a lockout message on the verification screen
    When I tap Resend Code on the phone verification screen
    Then I should see a confirmation toast that a new code was delivered
    When I rapidly tap Resend Code multiple times
    Then I should see a 60 second throttle warning message
    When the throttle period expires
    And I enter the latest valid SMS verification code
    And I tap the Verify button
    Then I should successfully complete registration
    And I should be navigated to the next onboarding step
