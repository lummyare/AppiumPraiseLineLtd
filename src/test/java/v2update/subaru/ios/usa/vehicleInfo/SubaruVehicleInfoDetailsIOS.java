package v2update.subaru.ios.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruVehicleInfoDetailsIOS extends SeeTestKeywords {

    String testName = " - SubaruVehicleInfoDetails-IOS";

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
        iOS_Setup2_5(this.testName);
        environmentSelection_iOS("prod");
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void vehicleInfoTest() {
        sc.startStepsGroup("Vehicle Info Details for Subaru -Test");
        createLog("Start: Vehicle Info Details for Subaru -Test");
        vehicleInfoDetails("JTMABABA8RA060836","2024  Solterra Limited");
        createLog("completed: Vehicle Info Details for Subaru -Test");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleInfoDetails(String strVin, String vehicleModel) {
        createLog("Verifying Vehicle Info Details in vehicle info screen");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(5000, 30000);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Connected Services Support']", 0, 1000, 5, false);
        createLog("Verifying Vehicle details in vehicle info screen");
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'"+vehicleModel+"')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='edit_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='edit_icon']/following::*[@XCElementType='XCUIElementTypeImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Identification Number']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='eye_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'•••••••••••')]", 0);
        click("NATIVE", "xpath=//*[@text='eye_icon']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='eyeslash_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='copy_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Identification Number']/following::*[@text='"+strVin+"']", 0);
        sc.longClick("NATIVE", "xpath=//*[@text='copy_icon']", 0, 1, 0, 0);
        createLog("Verified Vehicle details in vehicle info screen");

        //Verifying Glovebox section
        createLog("Verifying Glovebox section in vehicle info screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Glovebox']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Specs']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='globebox_icon']", 0);
        createLog("Verified Glovebox section in vehicle info screen");

        //Verifying Subscriptions section
        createLog("Verifying Subscriptions section in vehicle info screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Subscriptions']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Connected services for your vehicle']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='subscriptions_icon']", 0);
        createLog("Verified Subscriptions section in vehicle info screen");

        sc.swipe("Down", sc.p2cy(30), 1000);

        //Verifying Vehicle software section
        createLog("Verifying Vehicle software section in vehicle info screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Software']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Up to date' or @text='Customer Action Complete']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleSoftware_icon']", 0);
        createLog("Verifying Vehicle software section in vehicle info screen");

        //Verify Remove vehicle link
        verifyElementFound("NATIVE", "xpath=//*[@text='Remove Vehicle']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@id='VehicleInfo_back_Btn']", 0);
        click("NATIVE", "xpath=//*[@id='VehicleInfo_back_Btn']", 0, 1);
        sc.syncElements(2000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);

        createLog("Verified Vehicle Info Details in vehicle info screen");
    }
}