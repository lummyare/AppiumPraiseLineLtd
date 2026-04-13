Feature: Sign-In via Email and Phone Login
  As a registered Subaru OneApp user
  I want to sign in using my email or phone number
  So that I can access my dashboard and connected vehicle services

  Background:
    Given the mobile application is launched
    And I am on the home screen

  # OB_E2E_001 — Highest Priority
  @SignIn_Email_PhoneLogin @signIn @smoke @OB_E2E_001
  Scenario: OB_E2E_001 - Successful Sign-In via Email
    When I tap the Sign In button on the Welcome Back screen
    And I enter my registered email address "sub2_21mm@mail.tmnact.io"
    And I enter my password "Test@123"
    And I tap the Sign In submit button
    Then I should be navigated to the app dashboard
    And my session should be confirmed active

  # OB_E2E_002 — Highest Priority
  @SignIn_Email_PhoneLogin @signIn @OB_E2E_002
  Scenario: OB_E2E_002 - Successful Sign-In via Phone Number
    When I tap the Sign In button on the Welcome Back screen
    And I switch the input mode to Phone Number
    And I enter my registered phone number "5551234567"
    And I enter my password "Test@123"
    And I tap the Sign In submit button
    Then I should be navigated to the app dashboard
    And my session should be confirmed active

  # OB_E2E_003 — Highest Priority
  @SignIn_Email_PhoneLogin @signIn @negativeTest @OB_E2E_003
  Scenario: OB_E2E_003 - Invalid Credentials and Account Lockout Flow
    When I tap the Sign In button on the Welcome Back screen
    And I enter my registered email address "sub2_21mm@mail.tmnact.io"
    And I enter an incorrect password "WrongPass1"
    And I tap the Sign In submit button
    Then I should see an inline credential error message
    When I repeat incorrect password entry 4 more times
    Then I should see an account lockout message
    And the Sign In button should be disabled
    When the lockout period expires
    And I enter my correct password "Test@123"
    And I tap the Sign In submit button
    Then I should be navigated to the app dashboard

  # OB_E2E_004 — Highest Priority
  @SignIn_Email_PhoneLogin @signIn @forgotPassword @OB_E2E_004
  Scenario: OB_E2E_004 - Forgot Password End-to-End Recovery Flow
    When I tap the Sign In button on the Welcome Back screen
    And I tap Forgot Password
    And I enter my registered email for password recovery "sub2_21mm@mail.tmnact.io"
    And I submit the password recovery request
    Then I should see a confirmation that a reset link was sent
    When I enter and confirm a new valid password "NewPass@2026"
    And I save the new password
    Then I should be redirected to the Sign In screen
    When I sign in with my new password "NewPass@2026"
    Then I should be navigated to the app dashboard

  # OB_E2E_005 — Highest Priority
  @SignIn_Email_PhoneLogin @signIn @newDevice @OB_E2E_005
  Scenario: OB_E2E_005 - Sign-In on a New Unrecognized Device
    When I tap the Sign In button on the Welcome Back screen
    And I enter my registered email address "sub2_21mm@mail.tmnact.io"
    And I enter my password "Test@123"
    And I tap the Sign In submit button
    Then the system should detect a new unrecognized device
    And I should see a device verification prompt
    When I retrieve and enter the device verification code via email
    And I tap the Verify button
    Then I should be navigated to the app dashboard
    And the device should be trusted for future logins
