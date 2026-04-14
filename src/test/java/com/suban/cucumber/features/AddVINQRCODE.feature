Feature: Add Vehicle via QR Code
  As a logged-in Subaru OneApp user
  I want to add my vehicle by scanning its QR code
  So that vehicle details are automatically retrieved and linked to my account

  Background:
    Given the mobile application is launched
    And I am on the home screen
    And I proceed to Login Process with profile "21mmEVDummy1"

  # OB_E2E_033 — Highest Priority
  @AddVINQRCODE @addVehicle @qrCode @OB_E2E_033
  Scenario: OB_E2E_033 - Add Vehicle via QR Code Registration
    When I tap Add Vehicle on the dashboard or navigation
    And I select QR Code Registration option
    And I grant camera permission when the permission dialog appears
    And I scan the vehicle QR code
    Then the system should decode the QR and retrieve the vehicle details
    And I should see the pre-filled vehicle information including VIN model and year
    When I tap Confirm to add the vehicle
    Then the vehicle should be successfully added to my account
    And the vehicle should be visible on the dashboard with all details populated

  # OB_E2E_034 — Highest Priority
  @AddVINQRCODE @addVehicle @qrCode @negativeTest @OB_E2E_034
  Scenario: OB_E2E_034 - QR Code Scan Failure and Fallback to Manual Entry
    When I tap Add Vehicle on the dashboard or navigation
    And I select QR Code Registration option
    And I grant camera permission when the permission dialog appears
    And I attempt to scan a damaged or unreadable QR code
    Then I should see a scan failure error message
    When I attempt to scan an invalid non-vehicle QR code
    Then I should see an invalid QR code error message
    When I tap the Enter Manually fallback option
    And I type the 17-character VIN "JF1ZNAA14E8703230" into the VIN input field
    And I tap Submit to validate the VIN
    And I confirm the vehicle details and tap Add
    Then the vehicle should be successfully added to my account
    And the vehicle should be visible on the dashboard
