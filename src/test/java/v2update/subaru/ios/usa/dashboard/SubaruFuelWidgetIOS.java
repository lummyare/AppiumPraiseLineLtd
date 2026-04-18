package v2update.subaru.ios.usa.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.MobileElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import utils.CommonUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruFuelWidgetIOS extends SeeTestKeywords {
    String testName = "EV - FuelWidget EV - IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                createLog("Started: Email Login");
                testName = System.getProperty("cloudApp") + testName;
                //App Login
                iOS_Setup2_5(testName);
                selectionOfCountry_IOS("USA");
                sc.startStepsGroup("email SignIn EV");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
                createLog("Ended: Email Login");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                iOS_Setup2_5(testName);
                sc.startStepsGroup("email SignIn EV");
                selectionOfCountry_IOS("USA");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
                sc.stopStepsGroup();
        }
    }
    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }

    //EV Vehicles - Lexus-RZ or Toyota-BZ4X
    @Test
    @Order(1)
    public void fuelWidgetEVTest() throws IOException {
        sc.startStepsGroup("Fuel Widget EV");
        fuelWidgetEV();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }
    public static void fuelWidgetEV() throws IOException {
        createLog("Started : Fuel Widget Validation EV Vehicle");
        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Vehicle image')]", 0)) {
            reLaunchApp_iOS();
        }
        //cool fan icon and mi est.
        verifyElementFound("NATIVE", "xpath=//*[@text='mi est.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='CoolfanIcon']", 0);
        //verify Distance to empty miles displayed with unit mi
        String distanceUnit = sc.elementGetText("NATIVE", "xpath=//*[@id='fuel_view_range_unit_label']", 0);
        String[] distanceUnitArr = distanceUnit.split(" ");
        if (distanceUnitArr[0].equalsIgnoreCase("mi")) {
            createLog("estimated miles displayed as mi");
            sc.report("estimated miles unit displayed as mi", true);
        } else {
            createErrorLog("Expected unit:mi | Actual unit:" + distanceUnitArr[0]);
            sc.report("Expected unit:mi | Actual unit:" + distanceUnitArr[0], false);
        }
        //verify distance to empty value displayed is numeric
        String distanceValue = sc.elementGetText("NATIVE", "xpath=//*[@id='fuel_view_range_value_label']", 0);
        sc.report("estimated miles is Numeric value ", CommonUtils.isNumeric(distanceValue));
        createLog("estimated miles value displayed in numeric");

        //Battery Charged value / Charging value
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='chargingBattery' or @text='PluggedIcon']", 0)) {
            createLog("Battery charging plugged in");
            if(sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'charged')]", 0)) {
                createLog("plugged in but not charging");
                verifyElementFound("NATIVE", "xpath=//*[@text='PluggedIcon']", 0);
                String chargedVal = sc.elementGetText("NATIVE", "xpath=//*[@name='fuel_view_percent_label']", 0);
                createLog("Charged value is: "+chargedVal);
                if (chargedVal.contains("%")) {
                    createLog("Charged value section displays %");
                    sc.report("Charged value section displays %", true);
                } else {
                    createErrorLog("Expected :% | Actual :" + chargedVal);
                    sc.report("Expected :% | Actual :" + chargedVal, false);
                }
                String[] chargedValArr = chargedVal.split("%");
                sc.report("Verify Charged value is Numeric ", CommonUtils.isNumeric(chargedValArr[0]));
            } else {
                createLog("plugged in and charging");
                verifyElementFound("NATIVE", "xpath=//*[@text='chargingBattery']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='fuel_view_unplug_cta']", 0);
                verifyElementFound("NATIVE", "xpath=//*[contains(@text,'until full')]", 0);
                String estimatedTime = sc.elementGetText("NATIVE", "xpath=//*[contains(@text,'until full')]", 0);
                createLog("Estimated time to until full: "+estimatedTime);
            }
        } else {
            createLog("vehicle not charging");
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'BatteryLevel')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'% charged')]", 0);
            String chargedVal = sc.elementGetText("NATIVE", "xpath=//*[@name='fuel_view_percent_label']", 0);
            createLog("Charged value is: "+chargedVal);
            String[] chargedValArr = chargedVal.split("%");
            sc.report("Verify Charged value is Numeric ", CommonUtils.isNumeric(chargedValArr[0]));
        }
        progressBarColorValidationEV();
        createLog("Completed : Fuel Widget Validation EV Vehicle");
    }
    public static void progressBarColorValidationEV() throws IOException {
        createLog("Started - progress bar color validation");
        sc.syncElements(2000, 30000);
        //progress bar color validation
        String progressBarValue = sc.elementGetProperty("NATIVE","xpath=//*[@id='fuel_view_fuel_bar_view']",0,"value");
        String barValue = progressBarValue.substring(0,progressBarValue.length()-1);
        createLog("Progress bar Value is: "+barValue);
        MobileElement elem = (MobileElement) driver.findElement(By.xpath("//*[@id='fuel_view_fuel_bar_view']"));
        Point point = elem.getLocation();
        int leftX = point.getX();
        int leftY = point.getY();

        Dimension size = elem.getSize();
        int width = size.getWidth();
        int height = size.getHeight();

        int middleX = leftX + width / 2;
        int middleY = leftY + height / 2;
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        BufferedImage image = ImageIO.read(scrFile);
        // Getting pixel color by position x and y
        int clr = image.getRGB(middleX,middleY);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;

        System.out.println("Red Color value = " + red);
        System.out.println("Green Color value = " + green);
        System.out.println("Blue Color value = " + blue);

        //verify maximum color value
        int clrValue[] = {red, green, blue};
        int maximumValue = Arrays.stream(clrValue).max().getAsInt();
        createLog("color displayed on progress bar " + maximumValue);
        sc.report("color displayed on progress bar " + maximumValue, true);
        String maximumValueColor = "";
        if (maximumValue == blue) {
            maximumValueColor = "blue";
        } else if (maximumValue == red) {
            maximumValueColor = "red";
        } else {
            maximumValueColor = "green";
        }
        createLog("Fuel is: "+progressBarValue);
        createLog("color displayed on progress bar " + maximumValueColor);
        sc.report("color displayed on progress bar " + maximumValueColor, true);

        //get the value of the battery charge remaining
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'BatteryLevel')]")){
            String batteryLevel = sc.elementGetText("NATIVE", "xpath=//*[contains(@text,'BatteryLevel')]", 0);
            createLog("Current Battery Level is: " + batteryLevel);
            String chargeAvailable = sc.elementGetText("NATIVE", "xpath=//*[@id='fuel_view_percent_label']", 0);
            createLog("% charge available is: " + chargeAvailable);
            String chargeValueDisplayed = chargeAvailable.substring(0, chargeAvailable.indexOf("%"));
            sc.report("charge % " + chargeValueDisplayed, true);
            chargeValueDisplayed.trim();
            int chargeValue = Integer.parseInt(chargeValueDisplayed);
            sc.report("charge % " + chargeValue, true);
            createLog("charge % " + chargeValue);

            if (chargeValue > 30 && Integer.parseInt(barValue)> 30) {
                if (maximumValueColor.equalsIgnoreCase("green") == true) {
                    createLog("displayed color - Green");
                    sc.report("displayed color - Green", true);
                } else {
                    createErrorLog("displayed color - not Green");
                    sc.report("displayed color - not Green", true);
                }
            }
            else {
                if (maximumValueColor.equalsIgnoreCase("red") == true) {
                    createLog("displayed color - Red");
                    sc.report("displayed color - Red", true);
                } else {
                    createErrorLog("displayed color - not Red");
                    sc.report("displayed color - not Red", true);
                }
            }
        }

        //if battery is charging
        if(sc.isElementFound("NATIVE","xpath=//*[@text='chargingBattery']")) {
            createLog("Battery is charging");
            String chargeAvailable = sc.elementGetText("NATIVE", "xpath=//*[contains(@text,'until full')]", 0);
            sc.report("remaining time to fully charge battery is: " + chargeAvailable, true);
            createLog("remaining time to fully charge battery is: " + chargeAvailable);
        }

        createLog("Completed - progress bar color validation");
    }
}

