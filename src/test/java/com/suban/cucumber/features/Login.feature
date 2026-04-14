Feature: User Login Functionality
  As a user of the mobile application
  I want to be able to login with my credentials
  So that I can access the secure areas of the application

  @login @smoke @21mmEVDummy1
  Scenario: Successful login with 21mmEVDummy1 account
    Given the mobile application is launched
    And I am on the home screen
    And I proceed to Login Process with profile "21mmEVDummy1"

  @login @24MMEVDummy1
  Scenario: Successful login with 24MMEVDummy1 account
    Given the mobile application is launched
    And I am on the home screen
    And I proceed to Login Process with profile "24MMEVDummy1"
