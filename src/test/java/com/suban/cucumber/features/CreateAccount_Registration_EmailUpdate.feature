Feature: Email Address Update
  As a logged-in Subaru OneApp user
  I want to update my registered email address
  So that my account contact information stays current

  Background:
    Given the mobile application is launched
    And I am on the home screen
    And I proceed to Login Process with profile "21mmEVDummy1"

  # OB_E2E_019 — Highest Priority
  @CreateAccount_Registration_EmailUpdate @emailUpdate @OB_E2E_019
  Scenario: OB_E2E_019 - Successful Email Update with Verification
    Given I navigate to the Personal Details screen in Account Settings
    When I tap the Email field
    And I enter a new valid email address "updated_email@mail.tmnact.io"
    And I tap Submit to request the email update
    Then I should see a confirmation that a verification link was sent to the new email
    When I click the email update verification link from the new inbox
    Then I should see an Email Updated Successfully confirmation
    When I sign out of the app
    And I sign in using the new email address "updated_email@mail.tmnact.io" and password "Test@123"
    Then I should be navigated to the app dashboard
    And the old email should no longer be accepted for sign-in

  # OB_E2E_020 — Highest Priority
  @CreateAccount_Registration_EmailUpdate @emailUpdate @negativeTest @OB_E2E_020
  Scenario: OB_E2E_020 - Email Update Failure Scenarios
    Given I navigate to the Personal Details screen in Account Settings
    When I enter an invalid email format "invalidemail"
    Then I should see an email format validation error blocking the flow
    When I enter my current email address again "sub2_21mm@mail.tmnact.io"
    Then I should see a same email error message
    When I enter an email registered to another account "subarustg02_21mm@mail.tmnact.io"
    Then I should see a duplicate account error on the email field
    When I enter a valid new email and request the update
    And I wait for the email update link to expire
    And I attempt to use the expired email update link
    Then I should see an appropriate link expiry error
    When I initiate two rapid email update requests for different addresses
    And I click the first email update link
    Then the first link should be invalidated and show an error
    When I click the second email update link
    Then the email update should complete successfully
    And I should be able to sign in with the second new email address
