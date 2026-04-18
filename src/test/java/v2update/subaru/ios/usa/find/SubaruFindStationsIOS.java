package v2update.subaru.ios.usa.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import v2update.subaru.ios.usa.vehicleInfo.SubaruSubscriptionIOS;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class SubaruFindStationsIOS extends SeeTestKeywords {
    String testName = " Subaru Find Stations - IOS";
    boolean isStageRun = Boolean.valueOf(ConfigSingleton.configMap.get("stageRun"));

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                System.setProperty("cloudApp","subarustage");
                System.setProperty("platform","ios");
                createLog("Started: Email Login");
                testName = System.getProperty("cloudApp") + testName;
                //App Login
                iOS_Setup2_5(testName);
                selectionOfCountry_IOS("USA");
                sc.startStepsGroup("email SignIn EV");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin(ConfigSingleton.configMap.get("strSubaruEmail"), ConfigSingleton.configMap.get("strSubaruPwd"));
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
   /*
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
    }*/

    @Test
    @Order(1)
    public void paidSubscriptionsTest() {
        sc.startStepsGroup("Started Paid subscriptions");
        paidSubscriptions();
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
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Find']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='IconFind']", 0);
        click("NATIVE", "xpath=//*[@id='Find']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Stations']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Find nearby stations to recharge']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='currentLocation']", 0);
        //Last parked and vehicle icon missing
        click("NATIVE", "xpath=//*[@text='Stations']", 0, 1);
        sc.syncElements(10000, 30000);
        if  (sc.isElementFound("NATIVE", "xpath=//*[@placeholder='Search by address or zip code']"))
        {
            click("NATIVE", "xpath=//*[@placeholder='Search by address or zip code']", 0, 1);
            sc.syncElements(10000, 30000);
            click("NATIVE", "xpath=//*[@placeholder='Current Location']", 0, 1);
            sc.syncElements(10000, 30000);
            sendText("NATIVE", "xpath=//*[@placeholder='Search by address or zip code']", 0, "Dallas");
            sc.syncElements(5000, 15000);
            createLog("Started Search station by City Name");
            click("NATIVE", "xpath=//*[@text='Dallas, TX']", 0, 1);
            String chargeStationDetails = sc.elementGetProperty("NATIVE", "xpath=//*[@text='COD CITY HALL STATION 2']/following-sibling::*[contains(text(), 'mi')]", 0, "value");
            createLog("First Charging Station details present in Nearby Stations section: " + chargeStationDetails);
            verifyElementFound("NATIVE", "xpath=//*[@text='COD CITY HALL STATION 2']/following-sibling::*[contains(text(), 'Chargers')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='COD CITY HALL STATION 2']/following-sibling::*[contains(text(), 'Level')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='COD CITY HALL STATION 2']/following-sibling::*[contains(text(), '24 Hours')]", 0);
            click("NATIVE", "xpath=//*[@text='COD CITY HALL STATION 2']/following-sibling::*[contains(text(), 'Chargers')]", 0, 1);
            sc.syncElements(10000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='carlocation']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='currentLocation']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='COD CITY HALL STATION 2']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='1500 Marilla Street']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Open 24 Hours']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Directions']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Website']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Call Charge Station']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='In Network']", 0);
            //unable to fetch the locator from the website
            //need to write the code to verify the phone number
            click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
            createLog("Verification completed for First Charging Station details present in Nearby Stations section");
        } else{
            createLog("Charging Stations are not displayed in Nearby Stations section");
        }
        createLog("Started Search stations by zip code");
        verifyElementFound("NATIVE", "xpath=//*[@text='Nearby Stations']", 0);
        click("NATIVE", "xpath=//*[@placeholder='Search by address or zip code']", 0, 1);
        sc.syncElements(10000, 30000);
        click("NATIVE", "xpath=//*[@placeholder='Current Location']", 0, 1);
        sendText("NATIVE", "xpath=//*[@placeholder='Current Location']", 0, "75056");
        verifyElementFound("NATIVE", "xpath=//*[@text='The Colony, TX, United States']", 0);
        click("NATIVE", "xpath=//*[@text='The Colony, TX, United States']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Holiday Inn Express']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Holiday Inn Express']/following-sibling::*[contains(text(), 'Chargers')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Holiday Inn Express']/following-sibling::*[contains(text(), 'Level')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Holiday Inn Express']/following-sibling::*[contains(text(), 'mi')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Holiday Inn Express']/following-sibling::*[contains(text(), 'Open 24 Hours')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Holiday Inn Express']/following-sibling::*[contains(text(), '5290 Memorial Dr')]", 0);
        click("NATIVE", "xpath=//*[@text='Holiday Inn Express']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Holiday Inn Express']/following-sibling::*[contains(text(), 'mi')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Holiday Inn Express']/following-sibling::*[contains(text(), '5290 Memorial Dr')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Directions']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='In Network']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Call Charge Station']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Website']", 0);
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Nearby Stations']", 0);
        createLog("Search by Zip code is complete");
        createLog("Started search stations by Address");
        click("NATIVE", "xpath=//*[@placeholder='Search by address or zip code']", 0, 1);
        sc.syncElements(10000, 30000);
        click("NATIVE", "xpath=//*[@placeholder='Current Location']", 0, 1);
        sendText("NATIVE", "xpath=//*[@placeholder='Current Location']", 0, "1891 Augustine Rd");
        verifyElementFound("NATIVE", "xpath=//*[@text='1891 Augustine Rd']", 0);
        click("NATIVE", "xpath=//*[@text='1891 Augustine Rd']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Nearby Stations']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']/following-sibling::*[contains(text(), 'Chargers')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']/following-sibling::*[contains(text(), 'Level')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']/following-sibling::*[contains(text(), 'mi')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']/following-sibling::*[contains(text(), 'Open 24 Hours')]", 0);
        click("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='In Network']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']/following-sibling::*[contains(text(), 'Chargers')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']/following-sibling::*[contains(text(), 'Level')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']/following-sibling::*[contains(text(), 'Open 24 Hours')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OBE GOLDEN LAKES']/following-sibling::*[contains(text(), 'mi')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Directions']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Website']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Call Charge Station']", 0);
        createLog("Find Station Test Complete");
    }

    public void paidSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Paid Subscriptions");
        if(isStageRun) {
            createLog("Stage run - adding paid service");
            isStageApp = true;
//          sc.install(ConfigSingleton.configMap.get("cloudAppStageSubaruIOS"), false, false);
            sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaruStage"), false, true);
            sc.syncElements(5000,10000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            ios_handlepopups();
            checkIsLoginScreenDisplayed_IOS();
            environmentSelection_iOS("stage");
            selectionOfCountry_IOS("USA");
            selectionOfLanguage_IOS("English");
            ios_keepMeSignedIn(true);
            ios_emailLogin("subarustage_21mm@mail.tmnact.io", "Test@123");
            //Paid Subscription With Auto Renew OFF
            SubaruSubscriptionIOS.validateSubscriptionCard();
            SubaruSubscriptionIOS.validateSubscriptionScreen();
            SubaruSubscriptionIOS.paidSubscriptionWithAutoRenewOnOff(false, false);
            //Paid Subscription With Auto Renew ON and add new credit card
            SubaruSubscriptionIOS.validateSubscriptionCard();
            SubaruSubscriptionIOS.validateSubscriptionScreen();
            SubaruSubscriptionIOS.paidSubscriptionWithAutoRenewOnOff(true, true);
        } else {
            createLog("Production run - skipping add paid service");
            SubaruSubscriptionIOS.validateAddServiceScreen();
            SubaruSubscriptionIOS.addDriveConnectPaidService();
        }
    }

}

