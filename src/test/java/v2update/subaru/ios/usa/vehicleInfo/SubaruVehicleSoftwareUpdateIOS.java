package v2update.subaru.ios.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruVehicleSoftwareUpdateIOS extends SeeTestKeywords {
    String testName = " - SubaruVehicleSoftwareUpdate-IOS";

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
    public void vehicleSoftwareUpdateTest(){
        sc.startStepsGroup("Test - Vehicle Software Update");
        vehicleSoftwareUpdate();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleSoftwareUpdate() {
        createLog("Verifying Vehicle Software Update in vehicle info screen");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE", "xpath=//*[@label='Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(5000, 30000);

        sc.swipe("down", sc.p2cy(30), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Software']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleSoftware_icon']", 0);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Up to date']")) {
            createLog("Vehicle software is up to date");
            verifyElementFound("NATIVE", "xpath=//*[@text='Up to date']", 0);
        } else if (sc.isElementFound("NATIVE", "xpath=//*[@text='Customer Action Complete']")) {
            createLog("Customer Action Complete");
            verifyElementFound("NATIVE", "xpath=//*[@text='Customer Action Complete']", 0);
        } else {
            createLog("Vehicle software update is available");
            verifyElementFound("NATIVE", "xpath=//*[@text='Software update available']", 0);
        }
        click("NATIVE", "xpath=//*[@text='Vehicle Software']", 0, 1);
        sc.syncElements(5000, 30000);
        //Validate Vehicle Software update screen
        if(sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Your Software is Up to Date')]")) {
            createLog("Verifying Vehicle software is up to Date");
            verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Software']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='There are no software updates at this time.']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Back to Dashboard']", 0);
            click("NATIVE", "xpath=//*[@text='Tap to go back']", 0, 1);
            createLog("Verified Vehicle software is up to Date");
        } else if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Software Update Failed')]")) {
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Software Update Failed\\nThe software will update the next time you start your vehicle.')]", 0);
            createErrorLog("Software Update Failed");
        } else {
            createLog("Vehicle software is not up to Date");
            verifyElementFound("NATIVE", "xpath=//*[@text='DCM & Driver Assist' or @text='DCM & DRIVER ASSIST']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='New software version is available']", 0);
            sc.swipe("down", sc.p2cy(20), 5000);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='OTA_UPDATE_CONTINUE_BUTTON' and @enabled='true']", 0);

            //click on continue
            click("NATIVE", "xpath=//*[@accessibilityLabel='OTA_UPDATE_CONTINUE_BUTTON' and @enabled='true']", 0, 1);
            sc.syncElements(5000, 30000);
            createLog("Verifying Vehicle software Agree and Install Screen");
            verifyElementFound("NATIVE", "xpath=//*[@text='DCM & Driver Assist' or @text='DCM & DRIVER ASSIST']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@text='Agree & Install' or @text='AGREE & INSTALL'])[1]", 0);
            click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
            createLog("Verified Vehicle software Agree and Install Screen");

            verifyElementFound("NATIVE", "xpath=//*[@text='DCM & Driver Assist' or @text='DCM & DRIVER ASSIST']", 0);
            click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
            createLog("Vehicle software is not up to Date");
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Vehicle Software']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleSoftware_icon']", 0);
        click("NATIVE", "xpath=//*[@id='VehicleInfo_back_Btn']", 0, 1);
        sc.syncElements(2000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        createLog("Verified Vehicle Software Update in vehicle info screen");
    }
}
