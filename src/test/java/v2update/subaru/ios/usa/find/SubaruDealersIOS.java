package v2update.subaru.ios.usa.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruDealersIOS extends SeeTestKeywords {
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
    public void dealers(){
        sc.startStepsGroup("Test - Dealers");
        validateDealers();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateDealers(){
        createLog("Verifying validate dealer");
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@label='Dealers']",0);
        verifyElementFound("NATIVE","xpath=//*[@label='Find a dealer']",0);
        verifyElementFound("NATIVE","xpath=//*[@label='dealers']",0);
        click("NATIVE", "xpath=//*[@label='dealers']", 0, 1);

        sc.syncElements(5000, 30000);

        createLog("Handle Turn On Bluetooth popup if displayed");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]")) {
            sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
            sc.syncElements(5000, 30000);
        }


        verifyElementFound("NATIVE","xpath=//*[@id='Preferred Dealer']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Preferred Dealer']//following::XCUIElementTypeStaticText",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Preferred Dealer']//following::XCUIElementTypeStaticText",1);
        verifyElementFound("NATIVE","xpath=//*[@text='Preferred Dealer']//following::XCUIElementTypeStaticText",2);
        verifyElementFound("NATIVE","xpath=//*[@id='phone_small_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='web_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='detail_direction_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Call']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Website']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Directions']",0);

        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Service Hours']")) {
            verifyElementFound("NATIVE","xpath=//*[@id='Service Hours']",0);
            verifyElementFound("NATIVE","xpath=//*[@id='Service Hours']/following-sibling::*[@id='filter_plus_minimize']",0);
        }

        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Services']")) {
            verifyElementFound("NATIVE","xpath=//*[@id='Services']",0);
            verifyElementFound("NATIVE","xpath=//*[@id='Services']/following-sibling::*[@id='filter_plus_minimize']",0);
        }

        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Amenities']")) {
            verifyElementFound("NATIVE","xpath=//*[@id='Amenities']",0);
            verifyElementFound("NATIVE","xpath=//*[@id='Amenities']/following-sibling::*[@id='filter_plus_minimize']",0);
        }

        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Payment Methods']")) {
            verifyElementFound("NATIVE","xpath=//*[@id='Payment Methods']",0);
            verifyElementFound("NATIVE","xpath=//*[@id='Payment Methods']/following-sibling::*[@id='filter_plus_minimize']",0);
        }

        sc.swipe("Down", sc.p2cy(30), 500);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Accessibility']")) {
            verifyElementFound("NATIVE","xpath=//*[@id='Accessibility']",0);
            verifyElementFound("NATIVE","xpath=//*[@id='Accessibility']/following-sibling::*[@id='filter_plus_minimize']",0);
        }

        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Transportation']")) {
            verifyElementFound("NATIVE","xpath=//*[@text='Transportation']",0);
            verifyElementFound("NATIVE","xpath=//*[@text='Transportation']/following-sibling::*[@id='filter_plus_minimize']",0);
        }

        verifyElementFound("NATIVE","xpath=//*[@id='Change Preferred Dealer']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Make an Appointment' or @id='Call Dealer']",0);

        click("NATIVE","xpath=//*[@id='remove_icon']",0,1);
        sc.syncElements(5000, 30000);
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);

        createLog("Verified validate dealer");
    }
}
