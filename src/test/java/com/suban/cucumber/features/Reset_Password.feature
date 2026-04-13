Feature: Password Reset
  As a registered Subaru OneApp user who has forgotten their password
  I want to reset my password via email OTP verification
  So that I can regain access to my account using a new password

  # OB_E2E_006 — Password Reset via Email OTP (24MMEVDummy1)
  @Reset_Password @passwordReset @OB_E2E_006 @24MMEVDummy1
  Scenario: OB_E2E_006 - Password Reset via Email Verification for 24MMEVDummy1

    # Step 1 — Launch and tap Sign In
    Given the mobile application is launched
    And I am on the home screen
    And I tap the Sign In button on the Welcome Back screen

    # Step 2 — Enter 24MMEVDummy1 email on the Sign In page
    And I enter the 24MMEVDummy1 email on the sign in page

    # Step 2b — Dismiss keyboard and tap Continue to reach the password page
    And I dismiss the keyboard and tap Continue to reach the password page

    # Step 3 — Click Reset It button on the password page
    And I tap the Reset It button on the sign in page

    # Step 4 — WE SENT AN EMAIL page: fetch OTP and enter it
    Then I should see the We Sent An Email page
    And I fetch and enter the OTP for the 24MMEVDummy1 email

    # Step 5 — Click Verify button
    And I tap the Verify button on the email verification page

    # Step 6 — Reset Your Password page: enter randomly generated new password
    Then I should be on the Reset Your Password page
    And I enter and confirm a randomly generated new password for 24MMEVDummy1

    # Step 7 — Click Reset Password button
    And I tap the Reset Password button

    # Step 8 — Assert Password Reset success page and click Done
    Then I should see the Password Reset success page
    And I tap the Done button on the success page

    # Step 9 — Assert navigate back to Welcome Back page
    Then I should be back on the Welcome Back page

    # Step 10 & 11 — Sign in again with newly stored password, assert Dashboard
    When I sign in using the 24MMEVDummy1 newly stored password
    Then I should be navigated to the app dashboard

  # OB_E2E_007 — SMS Reset (placeholder — kept for completeness)
  @Reset_Password @passwordReset @sms @OB_E2E_007
  Scenario: OB_E2E_007 - Password Reset via SMS Verification
    Given the mobile application is launched
    And I am on the home screen
    And I tap the Sign In button on the Welcome Back screen
    And I enter the 24MMEVDummy1 email on the sign in page
    And I dismiss the keyboard and tap Continue to reach the password page
    When I select the SMS verification option for password reset
    And I enter my registered phone number for password recovery "5551234567"
    And I submit the password recovery request
    Then I should receive an SMS password reset verification code
    When I enter the correct SMS reset code
    And I enter and confirm a new valid password "NewPass@2026"
    And I tap Save to confirm the new password
    Then I should see a password updated confirmation
    When I sign in with my new password "NewPass@2026"
    Then I should be navigated to the app dashboard after password reset

  # OB_E2E_008 — Error / negative test (placeholder)
  @Reset_Password @passwordReset @negativeTest @OB_E2E_008
  Scenario: OB_E2E_008 - Password Reset Failure Scenarios with Expired Link or Code
    Given the mobile application is launched
    And I am on the home screen
    And I tap the Sign In button on the Welcome Back screen
    And I enter the 24MMEVDummy1 email on the sign in page
    And I dismiss the keyboard and tap Continue to reach the password page
    When I enter my registered email for password recovery "subarustg02_21mm@mail.tmnact.io"
    And I submit the password recovery request
    Then I should see a confirmation that a reset link was sent
    When I attempt to use an expired password reset link
    Then I should see an appropriate password reset link expiry error message
    When I re-initiate the password reset flow
    And I enter my registered email for password recovery "subarustg02_21mm@mail.tmnact.io"
    And I submit the password recovery request
    And I use the newly received valid reset link to set a new password "NewPass@2026"
    Then I should be able to sign in successfully with the new password
