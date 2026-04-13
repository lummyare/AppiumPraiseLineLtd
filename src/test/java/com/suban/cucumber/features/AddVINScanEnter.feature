Feature: Add Vehicle by VIN Scan or Manual Entry
  As a logged-in Subaru OneApp user
  I want to add my vehicle by scanning or manually entering the VIN
  So that I can access connected vehicle services on the dashboard

  Background:
    Given the mobile application is launched
    And I am on the home screen
    And I proceed to Login Process with profile "21mmEVDummy1"

  # OB_E2E_030 — Highest Priority
  @AddVINScanEnter @addVehicle @vinScan @OB_E2E_030
  Scenario: OB_E2E_030 - Add Vehicle by Scanning VIN
    When I tap Add Vehicle on the dashboard or navigation
    And I select the Scan VIN option
    And I grant camera permission when the permission dialog appears
    And I point the camera at the vehicle VIN barcode
    Then the VIN should be auto-populated in the input field
    And the detected VIN should be correct
    When I tap Confirm to add the vehicle
    Then the vehicle should be successfully added to my account
    And the vehicle should be visible on the dashboard

  # OB_E2E_031 — Highest Priority
  @AddVINScanEnter @addVehicle @vinManual @OB_E2E_031
  Scenario: OB_E2E_031 - Add Vehicle by Manually Entering VIN
    When I tap Add Vehicle on the dashboard or navigation
    And I select Enter VIN Manually option
    And I type the 17-character VIN "JF1ZNAA14E8703230" into the VIN input field
    And I tap Submit to validate the VIN
    Then the system should validate the VIN and return vehicle details
    And I should see the vehicle make model and year displayed
    When I confirm the vehicle details and tap Add
    Then the vehicle should be successfully added to my account
    And the vehicle should be visible on the dashboard

  # OB_E2E_032 — Highest Priority
  @AddVINScanEnter @addVehicle @vinValidation @negativeTest @OB_E2E_032
  Scenario: OB_E2E_032 - VIN Entry Validation and Error Handling
    When I tap Add Vehicle on the dashboard or navigation
    And I select Enter VIN Manually option
    And I enter a VIN with fewer than 17 characters "JF1ZNAA14E870"
    Then I should see a VIN length validation error
    When I enter a VIN containing invalid characters "JF1ZNAA14I8703230"
    Then I should see an invalid VIN characters error
    When I enter a VIN already registered to another account "EXISTINGVIN123456"
    Then I should see a duplicate vehicle registration error
    When I enter a valid unregistered 17-character VIN "JF1ZNAA14E8703231"
    And I tap Submit to validate the VIN
    And I confirm the vehicle details and tap Add
    Then the vehicle should be successfully added to my account
    And the vehicle should be visible on the dashboard
