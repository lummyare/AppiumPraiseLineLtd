package v2update.subaru.android.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruVehicleSoftwareUpdateAndroid extends SeeTestKeywords {
    String testName = "Vehicle Software Update - Android";

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
        android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: 17cy email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void validateVehicleSoftwareUpdateTest() {
        sc.startStepsGroup("Validate Vehicle Software Update");
        vehicleSoftwareUpdate();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleSoftwareUpdate() {
        createLog("Start:Validate Vehicle Software Update");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1' and @text='OK']"))
            click("NATIVE", "xpath=//*[@id='button1' and @text='OK']", 0, 2);

        if(sc.isElementFound("NATIVE", "xpath=//*[@class='android.widget.Button' and (./preceding-sibling::* | ./following-sibling::*)[@text='Info']]")) {
            if(sc.isElementFound("NATIVE", "xpath=//*[@text='Download Digital Key']", 0)) {
                click("NATIVE", "xpath=//*[@class='android.view.View' and (./preceding-sibling::* | ./following-sibling::*)[@text='Download Digital Key'] and ./parent::*[@class='android.view.View']]", 0, 1);
            }
        }
        click("NATIVE", "xpath=//*[@text='Info']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 30000);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Vehicle Software') and @height>350]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Vehicle Software')]", 0);
        if(sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Up to date')] | //*[contains(@text,'Up to Date')]")) {
            createLog("Vehicle software is up to date");
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Up to date')] | //*[contains(@text,'Up to Date')]", 0);
        } else {
            createLog("Vehicle software update is available");
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Software update available')]", 0);
        }
        click("NATIVE", "xpath=//*[contains(@text,'Vehicle Software')]", 0, 1);
        sc.syncElements(5000, 30000);
        //Validate Vehicle Software update screen
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Your Software is Up to Date']")) {
            createLog("Verifying Vehicle software is up to Date");
            verifyElementFound("NATIVE", "xpath=//*[@text='There are no software updates at this time.']", 0);
            //verify back to dashboard button
            verifyElementFound("NATIVE", "xpath=//*[@class='android.widget.Button']", 0);
            click("NATIVE", "xpath=//*[@id='vehicle_software_back_button']", 0, 1);
            sc.syncElements(4000, 20000);
            verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_info_back_box_cta']", 0);
            click("NATIVE", "xpath=//*[contains(@text,'Vehicle Software')]", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='There are no software updates at this time.']", 0);
            click("NATIVE", "xpath=//*[@class='android.widget.Button']", 0, 1);
            sc.syncElements(4000, 20000);
            if(sc.isElementFound("NATIVE", "xpath=//*[@text='Please allow Display over other apps setting to enable auto launch feature.']", 0))
            {
                click("NATIVE", "xpath=//*[@text='Cancel']", 0, 1);
            }
            verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
            createLog("Verified Vehicle software is up to Date");
        } else {
            createLog("Vehicle software is not up to Date");
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='DCM & Driver Assist' or @text='DCM & DRIVER ASSIST']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='New software version available' or @text='NEW SOFTWARE VERSION AVAILABLE']", 0);
            sc.swipe("down", sc.p2cy(20), 5000);
            verifyElementFound("NATIVE", "xpath=//*[@id='continue_software_update_btn']", 0);

            //click on continue
            click("NATIVE", "xpath=//*[@id='continue_software_update_btn']", 0, 1);
            sc.syncElements(5000, 30000);
            createLog("Verifying Vehicle software Agree and Install Screen");
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='DCM & Driver Assist' or @text='DCM & DRIVER ASSIST']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@content-desc='Agree & Install' or @text='AGREE & INSTALL'])[1]", 0);
            click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
            createLog("Verified Vehicle software Agree and Install Screen");
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='DCM & Driver Assist' or @text='DCM & DRIVER ASSIST']", 0);

            click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
            if(sc.isElementFound("NATIVE","xpath=//*[@id='vehicle_info_back_box_cta']"))
                click("NATIVE", "xpath=//*[@id='vehicle_info_back_box_cta']", 0, 1);
            createLog("Vehicle software is not up to Date");
        }
        sc.syncElements(5000,10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        createLog("End:Validate Vehicle Software Update");
    }
}
