package v2update.subaru.ios.canada.french.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruFindStationsCAFrenchIOS extends SeeTestKeywords {
    String testName = "EV - Dealer - IOS";

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
    @Test
    @Order(1)
    public void findStationOnFind() throws IOException {
        sc.startStepsGroup("Test - Find->Stations EV");
        findStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void searchStation() throws IOException {
        sc.startStepsGroup("Search Station");
        verifySearchStation();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }
    public static void findStation() {
        createLog("Verifying FIND STATION screen");
        createLog("Verifying Nearby Station section in Find->Stations section");
        if(!sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'Vehicle image')]", 0))
            reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@id='Find']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Stations']", 0);
        click("NATIVE", "xpath=//*[@text='Stations']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Nearby Stations')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Map']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicle_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='navigation_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(text(),'Charge Stations')]", 0);

        //existing Charge Station Details
        if (sc.isElementFound("NATIVE","xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[1]")) {
            createLog("First Charging Station details is displayed in Nearby Stations section");
            String chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]", 0, "value");
            createLog("First Charging Station details present in Nearby Stations section: " + chargeStationDetails);

            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'Chargers')]", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'Level')]", 0);
            createLog("Verification completed for First Charging Station details present in Nearby Stations section");

            createLog("Clicking First Charging Station in Nearby Stations section");
            click("NATIVE","xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]",0,1);
            createLog("Verifying CHARGE STATION DETAILS screen");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Map']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='vehicle_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='navigation_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Charge Station Details']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[2][contains(text(),'mi')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Directions']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='map_pin_location_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Website']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='web_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Call Charge Station']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='mobile_icon']", 0);
            //click back button to navigate to Nearby Station screen
            click("NATIVE","xpath=//*[@text='back_button']",0,1);
            createLog("Verified CHARGE STATION DETAILS screen");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Nearby Stations')]", 0);
        }

        //with zip code
        sc.syncElements(5000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Search by address or zip code.']"))
            click("NATIVE", "xpath=//*[@id='Search by address or zip code.']", 0, 1);
        sendText("NATIVE", "xpath=//*[@id='Search by address or zip code.']", 0, "75067");
        sc.syncElements(5000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Done']"))
            click("NATIVE", "xpath=//*[@accessibilityLabel='Done']", 0, 1);
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE","xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]")) {
            createLog("On Searching with Charging Stations is displayed in Nearby Stations section");
            createLog("First Charging Station details is displayed in Nearby Stations section");
            String chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Chargers')]", 0, "value");
            createLog("First Charging Station details present in Nearby Stations section: " + chargeStationDetails);

            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Chargers')]", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Level')]", 0);
            createLog("Verification completed for First Charging Station details present in Nearby Stations section");

            createLog("Clicking First Charging Station in Nearby Stations section");
            click("NATIVE","xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Chargers')]",0,1);
            createLog("Verifying CHARGE STATION DETAILS screen");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Map']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='vehicle_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='navigation_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Charge Station Details']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[2][contains(text(),'mi')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Directions']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='map_pin_location_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Website']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='web_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Call Charge Station']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='mobile_icon']", 0);
            //click back button to navigate to Nearby Station screen
            click("NATIVE","xpath=//*[@text='back_button']",0,1);
            createLog("Verified CHARGE STATION DETAILS screen");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Nearby Stations')]", 0);
        } else {
            createLog("Charging Stations are not displayed in Nearby Stations section");
        }

        //with county name
        //deleting existing text from the search bar
        sc.syncElements(5000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Search by address or zip code.']"))
            click("NATIVE", "xpath=//*[@id='Search by address or zip code.']", 0, 1);
        sendText("NATIVE", "xpath=//*[@id='Search by address or zip code.']", 0, "Dallas");
        sc.syncElements(5000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Done']"))
            click("NATIVE", "xpath=//*[@accessibilityLabel='Done']", 0, 1);
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE","xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]")) {
            createLog("On Searching with Charging Stations is displayed in Nearby Stations section");
            createLog("First Charging Station details is displayed in Nearby Stations section");
            String chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Chargers')]", 0, "value");
            createLog("First Charging Station details present in Nearby Stations section: " + chargeStationDetails);

            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Chargers')]", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Level')]", 0);
            createLog("Verification completed for First Charging Station details present in Nearby Stations section");

            createLog("Clicking First Charging Station in Nearby Stations section");
            click("NATIVE","xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Chargers')]",0,1);
            createLog("Verifying CHARGE STATION DETAILS screen");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Map']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='vehicle_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='navigation_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Charge Station Details']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAStaticText'])[2][contains(text(),'mi')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Directions']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='map_pin_location_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Website']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='web_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Call Charge Station']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='mobile_icon']", 0);
            //click back button to navigate to Nearby Station screen
            click("NATIVE","xpath=//*[@text='back_button']",0,1);
            createLog("Verified CHARGE STATION DETAILS screen");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Nearby Stations')]", 0);
        } else {
            createLog("Charging Stations are not displayed in Nearby Stations section");
        }
    }
    public static void verifySearchStation() {
        createLog("Verifying Nearby Station section in Find->Stations section");
        if(!sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'Vehicle image')]", 0))
            reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@id='Find']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Stations']", 0);
        click("NATIVE", "xpath=//*[@text='Stations']", 0, 1);
        sc.syncElements(10000, 30000);
        if  (sc.isElementFound("NATIVE", "xpath=//*[@id='Search by address or zip code.']"))
        {
            click("NATIVE", "xpath=//*[@id='Search by address or zip code.']", 0, 1);
            sendText("NATIVE", "xpath=//*[@id='Search by address or zip code.']", 0, "Dallas");
            sc.syncElements(5000, 15000);
            String chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[1]//*[@class='UIAView' and contains(text(),'mi')]", 0, "value");
            createLog("First Charging Station details present in Nearby Stations section: " + chargeStationDetails);
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Chargers')]", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following-sibling::*[@class='UIAView'])[2]//*[@class='UIAView' and contains(text(),'Level')]", 0);
            createLog("Verification completed for First Charging Station details present in Nearby Stations section");
        } else{
            createLog("Charging Stations are not displayed in Nearby Stations section");
        }
    }

}
