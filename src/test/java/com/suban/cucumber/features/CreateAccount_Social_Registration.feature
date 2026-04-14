Feature: Social Account Registration and Login
  As a new or returning Subaru OneApp user
  I want to register or sign in using a social identity provider
  So that I can access my account without a manual password

  Background:
    Given the mobile application is launched
    And I am on the home screen

  # OB_E2E_025 — Highest Priority
  @CreateAccount_Social_Registration @socialLogin @registration @OB_E2E_025
  Scenario: OB_E2E_025 - Successful Account Registration via Social Login Apple
    When I tap the Sign Up button on the Welcome Back screen
    And I select Continue with Apple as the social login option
    And I authenticate via Apple using Face ID or Apple credentials
    And I grant the required app permissions
    Then the app should create an account using my Apple profile data
    And I should see an Account Created confirmation
    And I should be navigated to the next onboarding step without requiring a manual password

  # OB_E2E_026 — Highest Priority
  @CreateAccount_Social_Registration @socialLogin @signIn @OB_E2E_026
  Scenario: OB_E2E_026 - Social Login for Returning User
    When I tap the Sign In button on the Welcome Back screen
    And I select Continue with Apple as the social login option
    And I authenticate via Apple using Face ID or Apple credentials
    Then I should be navigated directly to the home dashboard
    And I should not need to enter a password

  # OB_E2E_027 — Highest Priority
  @CreateAccount_Social_Registration @socialLogin @cancellation @OB_E2E_027
  Scenario: OB_E2E_027 - Social Login Cancellation and Fallback to Another Provider
    When I tap the Sign Up button on the Welcome Back screen
    And I select Continue with Google as the social login option
    And I cancel on the Google authorization screen
    Then I should be returned to the Sign Up screen
    And no account should have been created
    When I select Continue with Apple as an alternative social provider
    And I authenticate via Apple successfully
    Then I should see an Account Created confirmation
    And I should be navigated to the next onboarding step
