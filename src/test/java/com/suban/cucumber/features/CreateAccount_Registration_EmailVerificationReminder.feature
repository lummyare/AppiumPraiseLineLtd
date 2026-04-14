Feature: Email Verification Reminder
  As a new Subaru OneApp user who has not yet verified their email
  I want to receive reminders and be able to resend verification emails
  So that I can complete my account setup

  Background:
    Given the mobile application is launched
    And I am on the home screen

  # OB_E2E_023 — Highest Priority
  @CreateAccount_Registration_EmailVerificationReminder @emailVerification @reminder @OB_E2E_023
  Scenario: OB_E2E_023 - Email Verification Reminder Flow
    Given I have completed Sign Up with email "reminder_test@mail.tmnact.io" without verifying
    When I close the app without clicking the verification email link
    And I reopen the app
    Then I should see an email verification reminder screen or banner
    When I tap Resend Verification Email
    Then I should receive a new verification email
    When I click the new email verification link
    And I return to the app
    Then I should see the email verified confirmation
    And I should be navigated to the dashboard or next onboarding step without further prompts

  # OB_E2E_024 — Highest Priority
  @CreateAccount_Registration_EmailVerificationReminder @emailVerification @expiredLink @OB_E2E_024
  Scenario: OB_E2E_024 - Expired Verification Email Handling
    Given I have a previously received verification email link that has expired
    When I click the expired verification link
    Then I should see an appropriate link expiry error message
    When I return to the app
    And I tap Resend Verification Email
    Then I should receive a new valid verification email
    When I click the new valid verification link
    Then the email should be confirmed as verified
    And I should be able to proceed through onboarding to successfully access the dashboard
