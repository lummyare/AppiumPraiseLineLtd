Feature: Subscription Trial Enrollment
  As a logged-in Subaru OneApp user
  I want to enroll in connected services trial subscriptions
  So that I can access Remote Connect Safety and other connected services

  Background:
    Given the mobile application is launched
    And I am on the home screen
    And I proceed to Login Process with profile "21mmEVDummy1"

  # OB_E2E_035 — Highest Priority
  @SubscriptionTrialEnrollment @subscription @enrollment @OB_E2E_035
  Scenario: OB_E2E_035 - Full Subscription Trial Enrollment Flow
    When the app presents the available trial subscriptions screen
    Then I should see the list of available trial services including Remote Connect Safety Connect and Service Connect
    When I review each trial service details
    And I select all desired trial services
    And I review and acknowledge the Connected Services Master Data Consent
    And I acknowledge the Service Connect consent
    And I acknowledge the Wi-Fi consent
    And I acknowledge the Marketing consent
    And I acknowledge the TSS Safety Sense consent
    And I tap Enroll in Trial
    Then I should see trial activation confirmations for each selected service
    When I navigate to the dashboard
    Then all subscribed services should be shown as active
    And each service should display the correct trial end date

  # OB_E2E_036 — Highest Priority
  @SubscriptionTrialEnrollment @subscription @partialEnrollment @OB_E2E_036
  Scenario: OB_E2E_036 - Partial Enrollment and Consent Management
    When the app presents the available trial subscriptions screen
    And I opt into Safety Connect service only
    And I decline the optional Marketing consent
    And I accept all required consents
    And I tap Enroll to complete enrollment
    Then only Safety Connect should be activated on the dashboard
    And unselected services should not be active
    When I navigate to Account Settings and the Data Privacy Portal
    Then all my consent preferences should be saved accurately
    And my declined Marketing consent should be recorded as declined
