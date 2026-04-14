package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class AddAVehicle extends BasePage {
    public AddAVehicle(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='QR_ADD_VEHICLE_SCAN_VIN_BUTTON'] | //android.widget.TextView[contains(@text,'Scan VIN') or contains(@text,'Scan Barcode')]")
    @iOSXCUITFindBy(accessibility = "QR_ADD_VEHICLE_SCAN_VIN_BUTTON")
    private WebElement scanVINNBtn;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='Enter VIN Manually' or @content-desc='Enter VIN Manually'] | //android.widget.TextView[@text='Enter VIN Manually']")
    @iOSXCUITFindBy(accessibility = "Enter VIN Manually")
    private WebElement enterVINManuallyBtn;

    //Vehicle Identification Menu
    @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='ADDVEHICLE_LABEL_TITLE' or contains(@text,'Add Vehicle') or contains(@text,'Add a Vehicle')]")
    @iOSXCUITFindBy(accessibility = "ADDVEHICLE_LABEL_TITLE")
    private WebElement addVehicleTitle;

    @AndroidFindBy(xpath = "//android.widget.EditText[@content-desc='ADDVEHICLE_TEXTFIELD_VIN' or @hint='VIN' or @hint='Enter VIN' or contains(@hint,'17')]")
    @iOSXCUITFindBy(accessibility = "ADDVEHICLE_TEXTFIELD_VIN")
    private WebElement vinInputBox;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='Add Vehicle' or @content-desc='Add Vehicle'] | //android.widget.TextView[@text='Add Vehicle']")
    @iOSXCUITFindBy(accessibility ="Add Vehicle")
    private WebElement addVehicleBtn;
}
