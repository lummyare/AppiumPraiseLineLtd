Feature: Demo Report Enhancement
  As a QA engineer
  I want to see beautiful and well-structured test reports
  So that I can easily understand test results

  @demo @smoke
  Scenario: Successful report demonstration
    Given the application is initialized
    When I perform a basic test
    Then the test should pass successfully
    And the report should display beautifully

  @demo @negative
  Scenario: Failed report demonstration
    Given the application is initialized
    When I perform a test that fails
    Then the test should fail as expected
    And the failure should be clearly visible in the report

  @demo @parameterized
  Scenario Outline: Data-driven report demonstration
    Given the application is initialized
    When I test with data "<testData>"
    Then the result should be "<expectedResult>"

    Examples:
      | testData | expectedResult |
      | valid1   | success        |
      | valid2   | success        |
      | invalid  | failure        |
