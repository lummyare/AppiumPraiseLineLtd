package com.suban.framework.pages.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class AddAVehicle extends BasePage {
    public AddAVehicle(AppiumDriver driver) {
        super(driver);
    }

    @iOSXCUITFindBy(accessibility = "QR_ADD_VEHICLE_SCAN_VIN_BUTTON")
    private WebElement scanVINNBtn;

    @iOSXCUITFindBy(accessibility = "Enter VIN Manually")
    private WebElement enterVINManuallyBtn;
    //Vehicle Identification Menu
    @iOSXCUITFindBy(accessibility = "ADDVEHICLE_LABEL_TITLE")
    private WebElement addVehicleTitle;

    @iOSXCUITFindBy(accessibility = "ADDVEHICLE_TEXTFIELD_VIN")
    private WebElement vinInputBox;
    @iOSXCUITFindBy(accessibility ="Add Vehicle")
    private WebElement addVehicleBtn;
}
