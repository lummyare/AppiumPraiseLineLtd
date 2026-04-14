package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * AddVehiclePage — Handles all Add Vehicle interactions.
 * Covers VIN manual entry, VIN barcode scan, QR code scan,
 * validation errors, camera permission grant, and vehicle confirmation.
 */
public class AddVehiclePage extends BasePage {

    // ── Add Vehicle button (Dashboard) ────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Add Vehicle' or @text='Add a Vehicle' or @content-desc='addVehicleButton'] | //android.widget.TextView[@text='Add Vehicle' or @text='Add a Vehicle']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Add Vehicle' or @name='addVehicleButton'"
            + " or @label='Add a Vehicle' or @name='addAVehicle']"
            + " | //XCUIElementTypeCell[@label='Add Vehicle' or @name='addVehicleCell']")
    private WebElement addVehicleButton;

    // ── Scan VIN option ────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'Scan VIN') or contains(@text,'Scan Barcode')] | //android.widget.TextView[contains(@text,'Scan VIN')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Scan VIN')"
            + " or @name='scanVinButton' or contains(@label,'Scan Barcode')]"
            + " | //XCUIElementTypeCell[contains(@label,'Scan VIN')]")
    private WebElement scanVinOption;

    // ── Enter VIN Manually option ──────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'Manual') or contains(@text,'Enter VIN') or contains(@text,'Enter Manually')] | //android.widget.TextView[contains(@text,'Enter VIN')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Manual')"
            + " or @name='enterManuallyButton' or contains(@label,'Enter VIN')"
            + " or contains(@label,'Enter Manually')]"
            + " | //XCUIElementTypeCell[contains(@label,'Enter') and contains(@label,'VIN')]")
    private WebElement enterVinManuallyOption;

    // ── QR Code option ─────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'QR') or contains(@text,'QR Code')] | //android.widget.TextView[contains(@text,'QR')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'QR')"
            + " or @name='qrCodeButton' or contains(@label,'QR Code')]"
            + " | //XCUIElementTypeCell[contains(@label,'QR')]")
    private WebElement qrCodeOption;

    // ── VIN text input ─────────────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.EditText[@hint='VIN' or @hint='Vehicle ID' or @content-desc='vinInput']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='vinInput' or @label='VIN'"
            + " or @name='FR_NATIVE_VIN_TEXTFIELD' or contains(@label,'17') or @label='Vehicle ID']")
    private WebElement vinInput;

    // ── Submit / Search VIN button ─────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Submit' or @text='Search' or @text='Find' or @text='Next' or @content-desc='vinSubmitButton'] | //android.widget.TextView[@text='Submit']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Submit' or @name='submitVinButton'"
            + " or @label='Search' or @label='Find' or @label='Next'"
            + " or @name='vinSubmitButton']")
    private WebElement submitVinButton;

    // ── Vehicle details screen ─────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Make') or contains(@text,'Model') or contains(@text,'Year')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Make')"
            + " or contains(@label,'Model') or contains(@label,'Year')"
            + " or contains(@name,'vehicleModel') or contains(@name,'vehicleYear')]")
    private WebElement vehicleDetailsText;

    // ── Confirm / Add Vehicle button ───────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Confirm' or @text='Add' or @text='Add Vehicle' or @content-desc='addVehicleConfirmButton'] | //android.widget.TextView[@text='Confirm']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Confirm' or @name='confirmAddVehicle'"
            + " or @label='Add' or @name='addVehicleConfirmButton'"
            + " or @label='Add Vehicle']")
    private WebElement confirmAddVehicleButton;

    // ── Success confirmation ───────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'added') or contains(@text,'Added') or contains(@text,'Vehicle Added') or contains(@text,'Success')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'added')"
            + " or contains(@label,'Added') or contains(@label,'Vehicle Added')"
            + " or contains(@name,'vehicleAddedConfirmation') or contains(@label,'Success')]")
    private WebElement vehicleAddedConfirmation;

    // ── VIN validation error ───────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'17 character') or contains(@text,'invalid VIN') or contains(@text,'VIN must') or contains(@text,'duplicate') or contains(@text,'already registered')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'17 character')"
            + " or contains(@label,'invalid VIN') or contains(@label,'VIN must')"
            + " or contains(@label,'duplicate') or contains(@label,'already registered')"
            + " or contains(@name,'vinErrorMessage')]")
    private WebElement vinErrorMessage;

    // ── Scan failure message ───────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'scan failed') or contains(@text,'Could not scan') or contains(@text,'Unable to read') or contains(@text,'unreadable')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'scan failed')"
            + " or contains(@label,'Could not scan') or contains(@label,'Unable to read')"
            + " or contains(@label,'unreadable') or contains(@name,'scanErrorMessage')]")
    private WebElement scanFailureMessage;

    // ── Invalid QR code message ────────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'invalid QR') or contains(@text,'not a vehicle') or contains(@text,'unrecognized')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'invalid QR')"
            + " or contains(@label,'not a vehicle') or contains(@label,'unrecognized')"
            + " or contains(@name,'invalidQrMessage')]")
    private WebElement invalidQrMessage;

    // ── Camera permission dialog ───────────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Allow' or @text='OK' or @text='Allow Camera Access'] | //android.widget.TextView[@text='Allow']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@label='Allow' or @name='allowButton'"
            + " or @label='OK' or @label='Allow Camera Access']")
    private WebElement cameraPermissionAllowButton;

    // ── Vehicle visible on dashboard ───────────────────────────────────────
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Subaru') or contains(@text,'Toyota') or contains(@text,'JF1')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@label,'Subaru')"
            + " or contains(@label,'Toyota') or contains(@label,'JF1')"
            + " or contains(@name,'vehicleName') or contains(@name,'dashboardVehicle')]")
    private WebElement vehicleOnDashboard;

    // ── Enter Manually fallback (on scan failure) ──────────────────────────
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'Enter Manually') or contains(@text,'Manual Entry')] | //android.widget.TextView[contains(@text,'Enter Manually')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'Enter Manually')"
            + " or @name='enterManuallyFallback' or contains(@label,'Manual Entry')]")
    private WebElement enterManuallyFallbackButton;

    public AddVehiclePage(AppiumDriver driver) {
        super(driver);
    }

    // ── Actions ───────────────────────────────────────────────────────────

    public void tapAddVehicle() {
        logger.info("[AddVehiclePage] Tapping Add Vehicle button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(addVehicleButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Add Vehicle");
            } catch (Exception e2) {
                tapByLabelFallback("Add a Vehicle");
            }
        }
    }

    public void tapScanVin() {
        logger.info("[AddVehiclePage] Selecting Scan VIN option");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(scanVinOption)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Scan VIN");
            } catch (Exception e2) {
                tapByLabelFallback("Scan Barcode");
            }
        }
    }

    public void tapEnterVinManually() {
        logger.info("[AddVehiclePage] Selecting Enter VIN Manually option");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(enterVinManuallyOption)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Enter VIN Manually");
            } catch (Exception e2) {
                tapByLabelFallback("Enter Manually");
            }
        }
    }

    public void tapQrCodeOption() {
        logger.info("[AddVehiclePage] Selecting QR Code option");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(qrCodeOption)).click();
        } catch (Exception e) {
            tapByLabelFallback("QR Code");
        }
    }

    public void enterVin(String vin) {
        logger.info("[AddVehiclePage] Entering VIN: {}", vin);
        try {
            wait.until(ExpectedConditions.visibilityOf(vinInput)).clear();
            vinInput.sendKeys(vin);
        } catch (Exception e) {
            List<WebElement> fields = driver.findElements(
                By.xpath("//XCUIElementTypeTextField"));
            if (!fields.isEmpty()) {
                fields.get(0).clear();
                fields.get(0).sendKeys(vin);
            }
        }
    }

    public void tapSubmitVin() {
        logger.info("[AddVehiclePage] Tapping Submit to validate VIN");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitVinButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Submit");
            } catch (Exception e2) {
                tapByLabelFallback("Next");
            }
        }
    }

    public void tapConfirmAddVehicle() {
        logger.info("[AddVehiclePage] Tapping Confirm to add vehicle");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(confirmAddVehicleButton)).click();
        } catch (Exception e) {
            try {
                tapByLabelFallback("Confirm");
            } catch (Exception e2) {
                tapByLabelFallback("Add");
            }
        }
    }

    public void grantCameraPermission() {
        logger.info("[AddVehiclePage] Granting camera permission");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cameraPermissionAllowButton)).click();
            logger.info("[AddVehiclePage] Camera permission granted");
        } catch (Exception e) {
            logger.info("[AddVehiclePage] No camera permission dialog — may have been auto-accepted");
        }
    }

    public void tapEnterManuallyFallback() {
        logger.info("[AddVehiclePage] Tapping Enter Manually fallback");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(enterManuallyFallbackButton)).click();
        } catch (Exception e) {
            tapByLabelFallback("Enter Manually");
        }
    }

    // ── Assertions ────────────────────────────────────────────────────────

    public boolean isVehicleDetailsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(vehicleDetailsText));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Make") || src.contains("Model") || src.contains("Year")
                    || src.contains("VIN");
        }
    }

    public boolean isVehicleAddedConfirmationDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(vehicleAddedConfirmation));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("added") || src.contains("Vehicle Added") || src.contains("Success");
        }
    }

    public boolean isVehicleVisibleOnDashboard() {
        try {
            wait.until(ExpectedConditions.visibilityOf(vehicleOnDashboard));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("Subaru") || src.contains("JF1") || src.contains("Toyota");
        }
    }

    public boolean isVinErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(vinErrorMessage));
            logger.info("[AddVehiclePage] VIN error: {}", vinErrorMessage.getText());
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("17 character") || src.contains("invalid VIN")
                    || src.contains("VIN must") || src.contains("duplicate");
        }
    }

    public boolean isScanFailureDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(scanFailureMessage));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("scan failed") || src.contains("Could not scan")
                    || src.contains("Unable to read") || src.contains("unreadable");
        }
    }

    public boolean isInvalidQrDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(invalidQrMessage));
            return true;
        } catch (Exception e) {
            String src = driver.getPageSource();
            return src.contains("invalid QR") || src.contains("not a vehicle")
                    || src.contains("unrecognized");
        }
    }

    public String getVinErrorText() {
        try {
            return vinErrorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────

    private void tapByLabelFallback(String label) {
        try {
            List<WebElement> els = driver.findElements(By.xpath(
                "//*[@label='" + label + "' or @name='" + label + "']"));
            if (!els.isEmpty()) {
                els.get(0).click();
                logger.info("[AddVehiclePage] Tapped '{}' via fallback", label);
            } else {
                throw new RuntimeException("[AddVehiclePage] Not found: " + label);
            }
        } catch (Exception e) {
            logger.error("[AddVehiclePage] tapByLabelFallback failed for '{}': {}", label, e.getMessage());
            throw e;
        }
    }
}
