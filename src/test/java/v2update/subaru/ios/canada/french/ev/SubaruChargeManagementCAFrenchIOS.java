package v2update.subaru.ios.canada.french.ev;

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

import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruChargeManagementCAFrenchIOS extends SeeTestKeywords {
    String testName = "EV - ChargeManagement EV - IOS";
    final static String strVIN = "JTMABABA8RA060836";

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
    public void chargeInfoTest() {
        sc.startStepsGroup("Test - Charge Information section");
        chargeInfoSection();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    public void scheduleTest() {
        sc.startStepsGroup("Test - Schedule Test Card ");
        scheduleValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    public void createScheduleTest() {
        sc.startStepsGroup("Test - Create Schedule ");
        createSchedule();
        sc.stopStepsGroup();
    }
    @Test
    @Order(4)
    public void findStationTest() throws IOException {
        sc.startStepsGroup("Test - Find Station");
        findStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void dashboardFindStationTest() throws IOException {
        sc.startStepsGroup("Test - Dashboard Find Station");
        dashboardFindStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void chargeInfoSection() {
        sc.syncElements(10000, 60000);
        String[] dashboardPercentChargedArr = {};
        String[] dashboardMilesArr = {};
        createLog("Verifying charge miles section");
        if(sc.isElementFound("NATIVE","xpath=//*[@text='chargingBattery' or @text='PluggedIcon']")) {
            if(sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'charged')]", 0)) {
                createLog("plugged in but not charging");
                verifyElementFound("NATIVE", "xpath=//*[@text='PluggedIcon']", 0);
                String chargedVal = sc.elementGetText("NATIVE", "xpath=//*[@name='fuel_view_percent_label']", 0);
                createLog("Charged value is: "+chargedVal);
                String[] chargedValArr = chargedVal.split("%");
                sc.report("Verify Charged value is Numeric ", CommonUtils.isNumeric(chargedValArr[0]));
            } else {
                createLog("plugged in and charging");
                verifyElementFound("NATIVE", "xpath=//*[@text='chargingBattery']", 0);
                String timeUntilFullCharge=sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'until full')]",0,"value");
                createLog("Vehicle is plugged in and Charging"+timeUntilFullCharge);
                sc.report("Vehicle is charging can not access the Start time and End time",true);
            }
            String dashboardMiles = sc.elementGetProperty("NATIVE", "xpath=//*[@id='fuel_view_range_value_label']", 0, "value");
            dashboardMilesArr = dashboardMiles.split("[.]");
            createLog("Dashboard Miles after split:" + dashboardMilesArr[0]);
        } else {
            createLog("Verifying charged section on dashboard");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='mi est.']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'charged')]", 0);
            String percentCharged = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'charged')]", 0, "value");
            if (percentCharged.contains("%")) {
                createLog("Charged value section displays %");
                sc.report("Charged value section displays %", true);
            } else {
                createErrorLog("Expected :% | Actual :" + percentCharged);
                sc.report("Expected :% | Actual :" + percentCharged, false);
            }
            dashboardPercentChargedArr = percentCharged.split("%");
            createLog("Charge Status in % :" + dashboardPercentChargedArr[0]);
            String dashboardMiles = sc.elementGetProperty("NATIVE", "xpath=//*[@id='fuel_view_range_value_label']", 0, "value");
            dashboardMilesArr = dashboardMiles.split("[.]");
            createLog("Dashboard Miles after split:" + dashboardMilesArr[0]);
            verifyElementFound("NATIVE", "xpath=//*[@text='Find Stations']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Energy']", 0);
            createLog("verified charged section on dashboard");
        }

        click("NATIVE","xpath=//*[@text='Forward']",0,1);
        sc.syncElements(15000, 60000);
        createLog("Verifying charge info section");

        createLog("Handle Turn On Bluetooth popup if displayed");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]")) {
            sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
            sc.syncElements(5000, 30000);
        }

        verifyElementFound("NATIVE", "xpath=//*[@text='Charge Info']", 0);
        //10/23 Defect Raised - https://toyotaconnected.atlassian.net/browse/OAD01-23073 - [iOS]EV Flutter to Native - Charge Info -> Unit for Climate ON & Climate OFF is not displayed as "mi est."
        //verifying miles details '417 mi'
        //CLIMATE ON
        verifyElementFound("NATIVE", "xpath=//*[@text='CLIMATE ON']", 0);
        String climateOnMiles = sc.elementGetProperty("NATIVE", "xpath=(//*[@text='Charge Info']/following::*[contains(@text, 'mi est.')]/preceding-sibling::*[1])[1]", 0, "value");
        createLog("climate On Miles is: " + climateOnMiles);
        //verify miles are displayed in numeric
        Assertions.assertTrue(CommonUtils.isNumeric(climateOnMiles), "Miles should be in numeric value");
        int climateOnMilesValue = Integer.parseInt(climateOnMiles);
        //verify value is greater then 0
        Assertions.assertTrue(climateOnMilesValue > 0, "climateOnMiles should be greater than 0");
        //mi est next to climate on
        verifyElementFound("NATIVE","xpath=((//*[@class='UIAScrollView']/*[@class='UIAView'])[1]/*[@text='mi est.'])[1]",0);

        //CLIMATE OFF
        verifyElementFound("NATIVE", "xpath=//*[@text='CLIMATE OFF']", 0);
        String climateOffMiles = sc.elementGetProperty("NATIVE", "xpath=(//*[@text='Charge Info']/following::*[contains(@text, 'mi est.')]/preceding-sibling::*[1])[2]", 0, "value");
        createLog("climate Off Miles is: " + climateOffMiles);
        //verify climate off miles are displayed in numeric
        Assertions.assertTrue(CommonUtils.isNumeric(climateOffMiles),"Miles should be in numberic value for climate off");
        int climateOffMilesValue = Integer.parseInt(climateOffMiles);
        //verify value is greater than 0
        Assertions.assertTrue(climateOffMilesValue > 0, "ClimateOffMiles should be greater than 0");
        //mi est next to climate off
        verifyElementFound("NATIVE","xpath=((//*[@class='UIAScrollView']/*[@class='UIAView'])[1]/*[@text='mi est.'])[2]",0);
        createLog("Comparing Miles in dashboard and in charge info screen");
        int dashboardMilesInt = Integer.valueOf(dashboardMilesArr[0].trim());
        int climateOnMilesInt = Integer.valueOf(climateOnMilesValue);
        createLog("Miles in dashboard: "+dashboardMilesInt);
        createLog("Miles in charge info screen: "+climateOnMilesInt);
        Assertions.assertTrue( dashboardMilesInt == climateOnMilesInt);
        createLog("Compare successful for Miles in dashboard and in charge info screen");

        createLog("verified charge miles section");

        createLog("Verifying charging status section on charge info screen");
        if (sc.isElementFound("NATIVE","xpath=//*[contains(@text,'CHARGING') and @accessibilityLabel='fuel_view_charging_label']")) {
            createLog("Vehicle is plugged in and Charging");
            //battery icon
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='fuel_view_battery_icon']", 0);
            //10/23 defect raised - OAD01-23074 [iOS]EV Flutter to Native - charging details “until fully charged“ is not displayed in Charge info screen
            String timeUntilFullCharge=sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'CHARGING')]",0,"value");
            createLog("Vehicle is plugged in and Charging"+timeUntilFullCharge);
        } else if (sc.isElementFound("NATIVE","xpath=//*[contains(@text,'PLUGGED IN')]")) {
            createLog("Vehicle is plugged in but not Charging");
            String timeUntilFullCharge=sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'PLUGGED IN')]",0,"value");
            createLog("Vehicle is plugged in and Charging"+timeUntilFullCharge);
        } else {
            createLog("Vehicle is not Charging");
        }

        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@text,'CHARGING') and @accessibilityLabel='fuel_view_charging_label']")) {
            String chargingSectionChargePercentage = sc.elementGetProperty("NATIVE","xpath=//*[@text='%']/preceding-sibling::*[1]",0,"value");
            createLog("Percentage Charged in Charge Info Section: "+chargingSectionChargePercentage);
            Assertions.assertTrue(Integer.valueOf(dashboardPercentChargedArr[0]) == Integer.valueOf(chargingSectionChargePercentage));
            createLog("Compare successful for Percentage Charged in dashboard and in charge info screen");
        }
    }

    public static void scheduleValidations(){
        createLog("Creating Schedule");
        //  if(!sc.isElementFound("NATIVE","xpath=//*[starts-with(@text,'Schedule')]")) {
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Charge Info']")) {
            click("NATIVE","xpath=//*[@text='Forward']",0,1);
            sc.syncElements(15000, 60000);
            createLog("Verifying charge info section");
            verifyElementFound("NATIVE", "xpath=//*[@text='Charge Info']", 0);
        }

        //count before adding
        sc.swipeWhileNotFound("Down", sc.p2cy(30), 4000, "NATIVE", "xpath//*[@text='Create Schedule']", 0, 1000, 10, false);

        click("NATIVE","xpath=//*[@label='Create Schedule']",0,1);
        sc.syncElements(10000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Charge Schedule']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Start Time']", 0);
        //  verifyElementFound("NATIVE", "xpath=//*[@text='Time Picker']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='End Time']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Set the time when you want your vehicle to stop charging']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Days of Week']", 0);
        //days displayed
        verifyElementFound("NATIVE", "xpath=(//*[@label='Days of Week']/following-sibling::*[@text='S'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Days of Week']/following-sibling::*[@text='M']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@label='Days of Week']/following-sibling::*[@text='T'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Days of Week']/following-sibling::*[@text='W']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@label='Days of Week']/following-sibling::*[@text='T'])[2]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Days of Week']/following-sibling::*[@text='F']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@label='Days of Week']/following-sibling::*[@text='S'])[2]", 0);
    }


    public static void findStation() throws IOException {
        createLog("Verifying Find Nearby Station section in Charge Info screen");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Charge Info']")) {
            reLaunchApp_iOS();
            click("NATIVE","xpath=//*[@text='Forward']",0,1);
            sc.syncElements(10000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Charge Info']", 0);
        }
        sc.swipeWhileNotFound("up", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='CLIMATE ON']", 0, 1000, 8, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='CLIMATE ON']", 0);

        if (sc.isElementFound("NATIVE","xpath=//*[contains(@text,'CHARGING') or contains(@text,'until full') or contains(@text,'Remote Activation Pending')]")) {
            createLog("Vehicle is plugged in and Charging or Remote Activation is pending - find nearby station will not be displayed");
            sc.report("Vehicle is plugged in and Charging  or Remote Activation is pending - find nearby station will not be displayed", false);
            createLog("Vehicle is plugged in and Charging  or Remote Activation is pending- find nearby station will not be displayed");
        } else {
            createLog("Vehicle is not plugged in and Charging - find nearby station will be displayed");
            verifyElementFound("NATIVE", "xpath=//*[@text='Find Nearby Station']", 0);

            //Find Station screen
            click("NATIVE", "xpath=//*[@text='Find Nearby Station']", 0, 1);
            //
            verifyNearByStations();
        }
        sc.syncElements(5000, 30000);
        createLog("Verification completed find stations section");
    }
    public static void verifyChargeStationDetails() throws IOException {
        createLog("Verifying Charge Station Details");
        //existing Charge Station Details
        if (sc.isElementFound("NATIVE","xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]")) {
            createLog("First Charging Station details is displayed in Nearby Stations section");
            String chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]", 0, "value");
            createLog("First Charging Station details present in Nearby Stations section: " + chargeStationDetails);
            String stationNameStr = chargeStationDetails.substring(0,10);
            verifyElementFound("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]", 0);
            verifyElementFound("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]/*[@label='Send to Car'][1]", 0);
            verifyElementFound("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]/*[@label='Directions'][1]", 0);
            //verifyElementFound("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]/*[@label='Add to favorite'][1]", 0);

            createLog("Verification completed for First Charging Station details present in Nearby Stations section");

            createLog("Clicking First Charging Station in Nearby Stations section");
            click("NATIVE","xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]",0,1);
            createLog("Verifying CHARGE STATION DETAILS screen");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Map']", 0);

            if(sc.isElementFound("NATIVE","xpath=//*[@text='Out of Network']")) {
                createLog("Station is OUT OF NETWORK");
                createLog("Verifying OUT OF NETWORK CHARGE STATION DETAILS screen");
                String chargeStationName = sc.elementGetProperty("NATIVE", "xpath=((//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[2][contains(text(),'mi')]/preceding-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
                sc.report("Verify Station name: ", chargeStationName.contains(stationNameStr));
                verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[2][contains(text(),'mi')]", 0);
                if(sc.isElementFound("NATIVE","xpath=//*[@text='Add to favorite']")) {
                    createLog("Add to favorite is displayed");
                    verifyElementFound("NATIVE", "xpath=//*[@text='Add to favorite']", 0);
                    verifyElementFound("NATIVE", "xpath=(//*[@text='Add to favorite']/following-sibling::*[@class='UIAImage'])[1]", 0);

                    // add to favorite
                    click("NATIVE", "xpath=//*[@text='Add to favorite']", 0, 1);
                    sc.syncElements(5000, 30000);

                    //https://toyotaconnected.atlassian.net/browse/OAD01-14653
                    // after add to favorite verify color is blue
                    //elementColorValidation("//*[@text='Add to favorite']");
                }
                verifyElementFound("NATIVE", "xpath=//*[@text='Send to Car']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='Directions']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='Charge management within the app is only available for network stations.']", 0);
                //clicking send to car
                click("NATIVE","xpath=(//*[@text='Send to Car']/preceding::*[@class='UIAImage'])[2]",0,1);
                sc.sleep(5000);
                sc.syncElements(30000, 60000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Sent']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='Your destination address sent.']", 0);
                click("NATIVE","xpath=//*[@text='OK']",0,1);
                sc.syncElements(5000, 30000);
                //click back
                click("NATIVE","xpath=(//*[@text='swipe_bar']/following::*[@class='UIAButton'])[1]",0,1);
                createLog("Verified OUT OF NETWORK CHARGE STATION DETAILS screen");
            } else {
                createLog("Station is IN NETWORK");
                createLog("Verifying IN NETWORK CHARGE STATION DETAILS screen");
                String chargeStationName = sc.elementGetProperty("NATIVE", "xpath=((//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[2][contains(text(),'mi')]/preceding-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
                sc.report("Verify station name:", chargeStationName.contains(stationNameStr));
                verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[2][contains(text(),'mi')]", 0);
                if(sc.isElementFound("NATIVE","xpath=//*[@text='Add to favorite']")) {
                    createLog("Add to favorite is displayed");
                    verifyElementFound("NATIVE", "xpath=//*[@text='Add to favorite']", 0);
                    verifyElementFound("NATIVE", "xpath=(//*[@text='Add to favorite']/following-sibling::*[@class='UIAImage'])[2]", 0);

                    // add to favorite
                    click("NATIVE", "xpath=//*[@text='Add to favorite']", 0, 1);
                    sc.syncElements(5000, 30000);

                    //https://toyotaconnected.atlassian.net/browse/OAD01-14653
                    // after add to favorite verify color is blue
                    //elementColorValidation("//*[@text='Add to favorite']");
                }
                verifyElementFound("NATIVE", "xpath=//*[@text='Pricing']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='Send to Car']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='Directions']", 0);
                //clicking send to car
                click("NATIVE","xpath=(//*[@text='Send to Car']/preceding::*[@class='UIAImage'])[3]",0,1);
                sc.sleep(5000);
                sc.syncElements(30000, 60000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Sent']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='Your destination address sent.']", 0);
                click("NATIVE","xpath=//*[@text='OK']",0,1);
                sc.syncElements(5000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Available Plugs']", 0);
                if(sc.isElementFound("NATIVE","xpath=//*[@text='Setup Wallet']")) {
                    createLog("Wallet is not set - Setup Wallet is displayed station details screen");
                    verifyElementFound("NATIVE", "xpath=//*[@text='Setup Wallet']", 0);
                } else {
                    createLog("Wallet is set");
                }
                //click back
                click("NATIVE","xpath=(//*[@text='swipe_bar']/following::*[@class='UIAButton'])[1]",0,1);
                createLog("Verified IN NETWORK CHARGE STATION DETAILS screen");
                sc.syncElements(5000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Nearby Stations')]", 0);
            }
        }
        createLog("Verified Charge Station Details");
    }

    public static void dashboardFindStation() throws IOException {
        createLog("Verifying FindStation section on dashboard screen");
        //dashboard screen
        sc.syncElements(5000, 30000);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Charge Info']"))
            click("NATIVE","xpath=//*[@text='close_icon']",0,1);

        if(!sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'Vehicle image')]", 0)) {
            reLaunchApp_iOS();
        }

        sc.syncElements(5000, 30000);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'CHARGING') or contains(@text,'until full') or contains(@text,'Remote Activation Pending')]")) {
            createLog("Vehicle is plugged in and Charging - FindStation will not be displayed");

            //verifying Charging section in dashboard
            if(sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'until full')]")) {
                createLog("Started : Vehicle is plugged in and Charging - Verifying Charging section on dashboard");
                verifyElementFound("NATIVE", "xpath=//*[@id='Charging']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='Energy']", 0);
                createLog("Completed: Vehicle is plugged in and Charging - Verifying Charging section on dashboard");

                //click on Charging - Verify charge Info screen
                click("NATIVE","xpath=//*[@id='Charging']",0,1);
                sc.syncElements(15000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@id='Charge Info']", 0);
                click("NATIVE","xpath=//*[@text='close_icon']",0,1);

                //verify dashboard
                verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Vehicle image')]", 0);
            }

        } else {
            createLog("Vehicle is not plugged in and Charging - FindStation will be displayed");
            verifyElementFound("NATIVE", "xpath=//*[@text='Find Stations']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Energy']", 0);

            click("NATIVE","xpath=//*[@id='Find Stations']",0,1);
            //Find Station screen
            createLog("Verifying FIND STATION screen");

            createLog("Handle Turn On Bluetooth popup if displayed");
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]")) {
                sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
                sc.syncElements(5000, 30000);
            }

            sc.syncElements(10000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Nearby Stations')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@class='UIAView' and ./*[@text='My Location']]", 0);

            //verify charge station details displayed in Nearby Stations screen
            verifyChargeStationDetails();
            click("NATIVE","xpath=//*[@text='Back']",0,1);
            createLog("verified FIND STATIONS screen");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Find Stations']", 0);
            createLog("Verified FindStation section on dashboard screen");
        }
    }

    public static void elementColorValidation(String strElement) throws IOException {
        createLog("Started - color validation");
        sc.syncElements(2000, 30000);
        //color validation
//        String elementColorValue = sc.elementGetProperty("NATIVE", strElement, 0, "value");
//        String elementValue = elementColorValue.substring(0, elementColorValue.length() - 1);
//        createLog("Element Value is: " + elementValue);
        MobileElement elem = (MobileElement) driver.findElement(By.xpath(strElement));
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
        int clr = image.getRGB(middleX, middleY);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;

        System.out.println("Red Color value = " + red);
        System.out.println("Green Color value = " + green);
        System.out.println("Blue Color value = " + blue);

        //verify maximum color value
        int clrValue[] = {red, green, blue};
        int maximumValue = Arrays.stream(clrValue).max().getAsInt();
        createLog("color displayed on element " + maximumValue);
        sc.report("color displayed on element " + maximumValue, true);
        String maximumValueColor = "";
        if(maximumValue == 253){
            createErrorLog("color not changed: " + maximumValue);
        } else if (maximumValue == blue) {
            maximumValueColor = "blue";
        } else if (maximumValue == red) {
            maximumValueColor = "red";
        } else {
            maximumValueColor = "green";
        }
        //createLog("element is: " + elementColorValue);
        createLog("color displayed on element: " + maximumValueColor);
        sc.report("color displayed on element: " + maximumValueColor, true);

        createLog("Completed - color validation");
    }

    public static void verifyNearByStations() throws IOException {
        createLog("Verifying FIND STATION screen");
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Nearby Stations')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Map']", 0);

        //verify charge station details displayed in Nearby Stations screen
        verifyChargeStationDetails();

        //search with zip code
        createLog("Verify search with zip code");
        sc.syncElements(5000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Current Location']"))
            click("NATIVE", "xpath=//*[@id='Current Location']", 0, 1);
        sendText("NATIVE", "xpath=//*[@id='Current Location']", 0, "75067");
        sc.syncElements(5000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Done']"))
            click("NATIVE", "xpath=//*[@accessibilityLabel='Done']", 0, 1);
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[1]")) {
            createLog("Location is displayed for entered zip code");
            String locationDetails = sc.elementGetProperty("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
            createLog("First location details on entering zip code: " + locationDetails);
            click("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[1]", 0, 1);
            sc.syncElements(10000, 20000);
            //verify charge station details on searching through zip code
            verifyChargeStationDetails();
        } else {
            createLog("Charging Stations are not displayed in Nearby Stations section - for entered zip code");
        }
        createLog("Verified search with zip code");

        //search with county name
        createLog("Verify search with county name");
        sc.syncElements(5000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Current Location']"))
            click("NATIVE", "xpath=//*[@id='Current Location']", 0, 1);
        //deleting existing search text
        if (sc.isElementFound("NATIVE", "xpath=(//*[@id='Current Location']/following-sibling::*[@class='UIAImage'])[2]"))
            click("NATIVE", "xpath=(//*[@id='Current Location']/following-sibling::*[@class='UIAImage'])[2]", 0, 1);
        sc.syncElements(5000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Current Location']"))
            click("NATIVE", "xpath=//*[@id='Current Location']", 0, 1);
        sc.syncElements(3000, 15000);
        sendText("NATIVE", "xpath=//*[@id='Current Location']", 0, "Dallas");
        sc.syncElements(5000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Done']"))
            click("NATIVE", "xpath=//*[@accessibilityLabel='Done']", 0, 1);
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[1]")) {
            createLog("Location is displayed for entered county name");
            String locationDetails = sc.elementGetProperty("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
            createLog("First location details on entering  county name: " + locationDetails);
            click("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[1]", 0, 1);
            sc.syncElements(10000, 20000);
            //verify charge station details on searching through  county name
            verifyChargeStationDetails();
        } else {
            createLog("Charging Stations are not displayed in Nearby Stations section - for entered county name");
        }
        createLog("Verified search with county name");

        //Filters
        createLog("Verifying filter options");
        verifyElementFound("NATIVE", "xpath=//*[@text='Partners']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Plug Types']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Favorites']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Clear All']", 0);
        createLog("Verified filter options");

        //1.Partners
        createLog("Verifying PARTNERS filters");
        verifyElementFound("NATIVE", "xpath=//*[@text='Partners']", 0);
        click("NATIVE", "xpath=//*[@text='Partners']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='ChargePoint']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='EV Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='EVgo']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='FLO Network']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@text='EVgo']", 0, "UP");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Greenlots']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Shell Recharge']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='close_icon']/preceding-sibling::*[@class='UIAButton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@text='EVgo']", 0, "DOWN");
        sc.syncElements(5000, 30000);
        click("NATIVE", "xpath=//*[@text='ChargePoint']", 0, 1);
        click("NATIVE", "xpath=//*[@text='EV Connect']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='tick_icon']/preceding-sibling::*[@class='UIAButton']", 0);
        click("NATIVE", "xpath=//*[@text='tick_icon']/preceding-sibling::*[@class='UIAButton']", 0, 1);
        sc.syncElements(10000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Nearby Stations')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Partners\\n2']", 0);

        createLog("PARTNERS filter - First Charging Station details is displayed in Nearby Stations section");
        String chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]", 0, "value");
        createLog("PARTNERS filter - First Charging Station details present in Nearby Stations section: " + chargeStationDetails);
        boolean stationNameBln = chargeStationDetails.contains("ChargePoint") || chargeStationDetails.contains("EV Connect");
        sc.report("PARTNERS filter - Verify First Charging Station details contains correct partner name", stationNameBln);
        createLog("PARTNERS filter - Verify First Charging Station details contains correct partner name: " + stationNameBln);
        verifyChargeStationDetails();
        sc.drag("NATIVE", "xpath=//*[@text='Favorites']", 0, -200, 0);
        click("NATIVE", "xpath=//*[@text='Clear All']", 0, 1);
        sc.syncElements(10000, 30000);
        createLog("Verified PARTNERS filters");

        //2.Plug Types
        createLog("Verifying PLUG TYPES filters");
        verifyElementFound("NATIVE", "xpath=//*[@text='Plug Types']", 0);
        click("NATIVE", "xpath=//*[@text='Plug Types']", 0, 1);
        sc.syncElements(3000, 15000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Level 2']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='DCFast']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='close_icon']/preceding-sibling::*[@class='UIAButton']", 0);

        //selecting Level 2
        click("NATIVE", "xpath=//*[@text='Level 2']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='tick_icon']/preceding-sibling::*[@class='UIAButton']", 0);
        click("NATIVE", "xpath=//*[@text='tick_icon']/preceding-sibling::*[@class='UIAButton']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Nearby Stations')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Level 2']", 0);
        //click Level2 in Nearby stations
        click("NATIVE", "xpath=//*[@text='Level 2']", 0, 1);
        //click Level2 in filter options
        click("NATIVE", "xpath=//*[@text='Level 2']", 0, 1);
        //selecting DCFast
        click("NATIVE", "xpath=//*[@text='DCFast']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='tick_icon']/preceding-sibling::*[@class='UIAButton']", 0);
        click("NATIVE", "xpath=//*[@text='tick_icon']/preceding-sibling::*[@class='UIAButton']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='DCFast']", 0);

        createLog("PLUG TYPES filter - First Charging Station details is displayed in Nearby Stations section");
        chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]", 0, "value");
        createLog("PLUG TYPES filter - First Charging Station details present in Nearby Stations section: " + chargeStationDetails);
        verifyChargeStationDetails();
        sc.drag("NATIVE", "xpath=//*[@text='Favorites']", 0, -200, 0);
        click("NATIVE", "xpath=//*[@text='Clear All']", 0, 1);
        sc.syncElements(10000, 30000);
        createLog("Verified PLUG TYPES filters");

        //3.Favorites
        createLog("Verifying FAVORITES filters");
        verifyElementFound("NATIVE", "xpath=//*[@text='Favorites']", 0);
        createLog("First Charging Station details is displayed in Nearby Stations section");
        chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]", 0, "value");
        createLog("First Charging Station details present in Nearby Stations section: " + chargeStationDetails);
        String stationNameStr = chargeStationDetails.substring(0, 10);
        sc.swipeWhileNotFound("DOWN", sc.p2cy(50), 2000, "NATIVE", "xpath=(//*[@text='Add to favorite' and @width>0])[1]", 0, 1000, 15, false);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Add to favorite' and @width>0])[1]", 0);
        click("NATIVE", "xpath=(//*[@text='Add to favorite' and @width>0])[1]", 0, 1);
        sc.syncElements(5000, 30000);

        // after add to favorite verify color is blue
        //elementColorValidation("(//*[@text='Add to favorite' and @width>0])[1]");

        sc.swipeWhileNotFound("UP", sc.p2cy(50), 2000, "NATIVE", "xpath=//*[@text='Favorites']", 0, 1000, 15, false);
        click("NATIVE", "xpath=//*[@text='Favorites']", 0, 1);
        sc.syncElements(10000, 30000);
        if(sc.isElementFound("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]")) {
            createLog("Favorite station found");
            //details after marking station as favorite
            chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=((//*[@class='UIAScrollView'])[3]/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]", 0, "value");
            createLog("FAVORITE Charging Station details present in Nearby Stations section: " + chargeStationDetails);
            stationNameBln = chargeStationDetails.contains(stationNameStr);
            sc.report("FAVORITE filter - Verify First Charging Station details contains marked favorite station", stationNameBln);
            createLog("FAVORITE filter - Verify First Charging Station details contains marked favorite station: " + stationNameBln);
        } else {
            //https://toyotaconnected.atlassian.net/browse/OAD01-14654
            sc.report("No favorite station found", false);
            createLog("No favorite station found");
        }
        sc.drag("NATIVE", "xpath=//*[@text='Favorites']", 0, -200, 0);
        click("NATIVE", "xpath=//*[@text='Clear All']", 0, 1);
        sc.syncElements(10000, 30000);
        createLog("Verified FAVORITES filters");

        //click close icon to navigate to Charge Info screen
        sc.swipeWhileNotFound("UP", sc.p2cy(50), 2000, "NATIVE", "xpath=//*[@text='Nearby Stations']", 0, 1000, 5, false);
        sc.flickElement("NATIVE", "xpath=//*[@text='Nearby Stations']", 0, "down");
        //raised automation requirement ticket for close icon - https://toyotaconnected.atlassian.net/browse/OAD01-16357
        //click("NATIVE", "xpath=(//*[@text='swipe_bar']/preceding::*[@class='UIAButton'])[3]", 0, 1);
        createLog("verified FIND STATIONS screen");
        sc.syncElements(5000, 30000);
    }

    public static void goToChargeSchedule() {
        createLog("Navigating to Schedule Screen");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Forward']")){
            reLaunchApp_iOS();
        }
        click("NATIVE","xpath=//*[@text='Forward']",0,1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Charge Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Create Schedule']", 0);
        click("NATIVE","xpath=//*[@text='Create Schedule']",0,1);
        createLog("Verifying SCHEDULE screen");
        sc.syncElements(30000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Charge Schedule']", 0);
        createLog("Navigated to Schedule Screen");
    }

    public static void createSchedule(){
        createLog("Creating Schedule");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Charge Schedule']")) {
            if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Forward']")){
                reLaunchApp_iOS();
            }
            click("NATIVE","xpath=//*[@text='Forward']",0,1);
            sc.syncElements(10000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Charge Info']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Create Schedule']", 0);
        }

        //count before adding
        sc.swipeWhileNotFound("Down", sc.p2cy(30), 4000, "NATIVE", "xpath=//*[@text='Create Schedule']", 0, 1000, 10, false);
        int beforeAddScheduleCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@value, 'am') or contains(@value, 'pm')]") - 2;
        createLog("Before adding schedule - count is : "+beforeAddScheduleCount);
        sc.swipe("up", sc.p2cy(40), 3000);

        click("NATIVE","xpath=//*[@label='Create Schedule']",0,1);
        sc.syncElements(10000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Charge Schedule']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Start Time']", 0);
        //  verifyElementFound("NATIVE", "xpath=//*[@text='Time Picker']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='End Time']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Set the time when you want your vehicle to stop charging']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Days of Week']", 0);
        //days displayed
        verifyElementFound("NATIVE", "xpath=(//*[@label='Days of Week']/following-sibling::*[@text='S'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Days of Week']/following-sibling::*[@text='M']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@label='Days of Week']/following-sibling::*[@text='T'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Days of Week']/following-sibling::*[@text='W']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@label='Days of Week']/following-sibling::*[@text='T'])[2]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Days of Week']/following-sibling::*[@text='F']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@label='Days of Week']/following-sibling::*[@text='S'])[2]", 0);
        click("NATIVE","xpath=//*[@label='Days of Week']/following-sibling::*[@text='M']",0,1);
        click("NATIVE","xpath=(//*[@label='Days of Week']/following-sibling::*[@text='T'])[2]",0,1);
        click("NATIVE","xpath=//*[@label='Days of Week']/following-sibling::*[@text='F']",0,1);

        //save icon
        click("NATIVE","xpath=//*[@label='check']",0,1);
        sc.syncElements(60000, 240000);
        createLog("Schedule created successfully");
        createLog("Verifying created new schedule in Schedule screen");
        //sc.swipeWhileNotFound("Down", sc.p2cy(30), 2000, "NATIVE", "xpath=(//*[contains(@label,'Last Updated:')]/preceding::*[contains(@value,'Mon,Thu,Fri')])[1]", 0, 1000, 10, false);
        sc.swipeWhileNotFound("Down", sc.p2cy(30), 5000, "NATIVE", "xpath=//*[@text='Mon, Thu, Fri']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Mon, Thu, Fri']", 0);
        String createdNewSchedule = sc.elementGetProperty("NATIVE","xpath=//*[@text='Mon, Thu, Fri']",0,"value");
        createLog("Created New Schedule is : "+createdNewSchedule);

        //verify count after adding
        sc.swipeWhileNotFound("Down", sc.p2cy(30), 4000, "NATIVE", "xpath=//*[contains(@text,'Last updated']", 0, 1000, 10, false);
        int afterAddScheduleCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@value, 'am') or contains(@value, 'pm')]") - 2;
        createLog("After adding schedule - count is : "+afterAddScheduleCount);

        if(afterAddScheduleCount > beforeAddScheduleCount) {
            createLog("Added schedule successfully");
        } else {
            createErrorLog("Add schedule test failed");
            fail();
        }
        createLog("Verified created new schedule in Schedule screen");
        sc.swipe("up", sc.p2cy(40), 3000);
        deleteSchedule();
    }

    public static void deleteSchedule(){
        //DELETE SCHEDULE
        createLog("Deleting created schedule ");
        if(!sc.isElementFound("NATIVE","xpath=//*[starts-with(@text,'Schedule')]")) {
            goToChargeSchedule();
        }

        //swipe down to get count
        sc.swipeWhileNotFound("Down", sc.p2cy(30), 4000, "NATIVE", "xpath=//*[@text='Refresh']", 0, 1000, 10, false);
        int beforeDeleteCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@value, 'am') or contains(@value, 'pm')]") - 2;
        createLog("Before deleting schedule - count is : "+beforeDeleteCount);
        sc.swipe("up", sc.p2cy(40), 3000);

        sc.swipeWhileNotFound("Down", sc.p2cy(30), 5000, "NATIVE", "xpath=(//*[contains(@value,'Mon,Wed,Thu,Fri')])[1]", 0, 1000, 5, false);
        click("NATIVE","xpath=//*[@text='Mon, Wed, Thu, Fri']",0,1);
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Charge Schedule']", 0);
        // delete icon
        verifyElementFound("NATIVE", "xpath=//*[@text='trash']", 0);
        //save icon
        verifyElementFound("NATIVE", "xpath=//*[@text='check']", 0);
        click("NATIVE","xpath=//*[@text='trash']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Delete Schedule']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Are you sure you want to delete this schedule?']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Go Back']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Yes, Delete']", 0);
        click("NATIVE","xpath=//*[@label='Go Back']",0,1);
        sc.syncElements(5000, 30000);
        // delete icon
        verifyElementFound("NATIVE", "xpath=//*[@text='trash']", 0);
        click("NATIVE","xpath=//*[@text='trash']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Delete Schedule']", 0);
        click("NATIVE","xpath=//*[@label='Yes, Delete']",0,1);
        sc.syncElements(60000, 240000);
        createLog("Deleted created schedule");
        createLog("Verifying deleted schedule is not displayed in Schedule screen");

        //swipe down to get count
        sc.swipeWhileNotFound("Down", sc.p2cy(30), 4000, "NATIVE", "xpath=//*[contains(@label,'Last updated:') and @visible='true']", 0, 1000, 10, false);
        int afterDeleteCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@value, 'am') or contains(@value, 'pm')]") - 2;
        createLog("After delete schedule - count is : "+afterDeleteCount);

        if(afterDeleteCount < beforeDeleteCount) {
            createLog("Deleted schedule successfully");
        } else {
            createErrorLog("Delete schedule test failed");
            fail();
        }

        createLog("Verified deleted schedule is not displayed in Schedule screen");
    }
}
