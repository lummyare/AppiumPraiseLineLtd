package v2update.subaru.android.usa.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruFindStationsAndroid extends SeeTestKeywords {
    String testName = "FindStation-Android";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                testName = System.getProperty("cloudApp") + testName;
                break;
            default :
                testName = ConfigSingleton.configMap.get("local") + testName;
                break;
        }
        android_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_Android("USA");
        selectionOfLanguage_Android("English");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strSubaruEmail"),ConfigSingleton.configMap.get("strSubaruPwd"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}
//TODO Test this class in stage
    @Test
    @Order(1)
    public void findStationOnFind() {
        sc.startStepsGroup("Find Station on Find");
        verifyFindStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void searchStation() {
        sc.startStepsGroup("Search Station");
        verifySearchStation();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public void verifyFindStation(){
        createLog("Verifying Find Station under Find");
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Stations']"))
            reLaunchApp_android();
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@text='Find']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Find']",0);
        click("NATIVE","xpath=//*[@text='Find']",0,1);
        sc.syncElements(5000, 30000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Stations']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Find nearby stations to recharge']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Stations']", 0);
        click("NATIVE","xpath=//*[@text='Stations']",0,1);
        sc.syncElements(3000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@text='Nearby Stations']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Vehicle location icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Current location icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='find_stations_back_cta']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Charge Stations']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text, 'nearby')]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Search by address or zip code.']",0);
        createLog("NearBy Stations complete");
    }

    public void verifySearchStation(){
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Nearby Stations']"))
            reLaunchApp_android();
        createLog("Verifying Searching for stations");
        sc.syncElements(5000, 10000);
        click("NATIVE","xpath=//*[@text='Find']",0,1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        sc.syncElements(5000, 10000);
        click("NATIVE","xpath=//*[@text='Stations']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@text='Search by address or zip code.']",0);
        click("NATIVE", "xpath=//*[@text='Search by address or zip code.']", 0, 1);
        sc.syncElements(5000, 30000);
        sendText("NATIVE", "//*[@text='Search by address or zip code.']", 0, "Dallas");
        sc.sendText("Dallas");
        click("NATIVE", "xpath=//*[@class='android.widget.ImageView' and ./parent::*[@contentDescription='Enter']]", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Nearby Stations']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='COD CITY HALL STATION 1']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Chargers']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Level']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text, 'mi')]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text, 'Hours')]",0);
        click("NATIVE", "xpath=//*[@text='COD CITY HALL STATION 1']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Nearby Stations']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='COD CITY HALL STATION 1']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='1500 Marilla Street']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Open 24 Hours']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Directions']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text, 'mi')]",0);
        createLog("Searcy by city validation complete");
    }

}
