Feature: CVN Acknowledgment During Registration
  As a new or migrating Subaru OneApp user
  I want to review and acknowledge the CVN terms
  So that I can proceed to the dashboard with full access

  Background:
    Given the mobile application is launched
    And I am on the home screen

  # OB_E2E_017 — Highest Priority
  @CreateAccount_Registration_CVN @cvn @registration @OB_E2E_017
  Scenario: OB_E2E_017 - CVN Acknowledgment During New Account Registration
    Given I have completed new account registration with email "newuser_cvn@mail.tmnact.io"
    When the CVN acknowledgment screen is presented
    Then I should see the CVN terms and privacy details displayed
    When I review the CVN content
    And I tap Acknowledge Accept on the CVN screen
    Then I should be navigated to the Add Vehicle or Dashboard screen
    And the CVN acknowledgment should be recorded

  # OB_E2E_018 — Highest Priority
  @CreateAccount_Registration_CVN @cvn @migration @OB_E2E_018
  Scenario: OB_E2E_018 - CVN Flow for Existing 21MM User Migrating to 24MM App
    When I tap the Sign In button on the Welcome Back screen
    And I enter my registered email address "sub2_21mm@mail.tmnact.io"
    And I enter my password "Test@123"
    And I tap the Sign In submit button
    Then the system should detect this is a first-time 24MM login for a migrated 21MM user
    And the CVN acknowledgment screen should be displayed
    When I review the CVN content
    And I tap Acknowledge Accept on the CVN screen
    Then I should be navigated to the home dashboard
    And all previously saved 21MM data should be retained and displayed correctly
    And favorites should be visible
    And home address should be visible
    And linked accounts should be visible
