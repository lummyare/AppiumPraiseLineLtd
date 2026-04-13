Feature: Create Account Registration
  As a new Subaru OneApp user
  I want to register a new account using email or phone
  So that I can access vehicle connected services

  Background:
    Given the mobile application is launched
    And I am on the home screen

  # OB_E2E_014 — Highest Priority
  @CreateAccount_Registration @registration @email @OB_E2E_014
  Scenario: OB_E2E_014 - Full Account Registration via Email
    When I tap the Sign Up button on the Welcome Back screen
    And I enter First Name "John"
    And I enter Last Name "Doe"
    And I enter a new valid email address "newuser_test@mail.tmnact.io"
    And I tap the Sign Up submit button
    Then I should receive a verification email
    When I click the email verification link
    And I enter a strong password "Secure@Pass1"
    And I confirm the password "Secure@Pass1"
    And I tap Submit to complete registration
    Then I should see an Account Created success confirmation
    And I should be navigated to the next onboarding step

  # OB_E2E_015 — Highest Priority
  @CreateAccount_Registration @registration @mobile @OB_E2E_015
  Scenario: OB_E2E_015 - Full Account Registration via Mobile Number
    When I tap the Sign Up button on the Welcome Back screen
    And I enter First Name "Jane"
    And I enter Last Name "Smith"
    And I enter a new valid mobile number "5559876543"
    And I tap the Sign Up submit button
    Then I should receive an SMS verification code
    When I enter the SMS verification code
    And I enter a strong password "Secure@Pass1"
    And I confirm the password "Secure@Pass1"
    And I tap Submit to complete registration
    Then I should see an Account Created success confirmation
    And I should be navigated to the next onboarding step

  # OB_E2E_016 — Highest Priority
  @CreateAccount_Registration @registration @validation @negativeTest @OB_E2E_016
  Scenario: OB_E2E_016 - Registration Validation and Error Handling
    When I tap the Sign Up button on the Welcome Back screen
    And I leave the First Name field empty
    Then the Sign Up button should be disabled
    When I enter special characters "@#$" in the Last Name field
    Then I should see a Last Name validation error
    When I enter an invalid email format "notanemail"
    Then the Sign Up button should remain disabled
    When I enter an already-registered email "sub2_21mm@mail.tmnact.io"
    And I tap the Sign Up submit button
    Then I should see a duplicate account error message
    When I enter a weak password "pass"
    Then I should see a password requirements error
    When I correct all fields with valid unique data
    And I complete registration successfully
    Then I should see an Account Created success confirmation
