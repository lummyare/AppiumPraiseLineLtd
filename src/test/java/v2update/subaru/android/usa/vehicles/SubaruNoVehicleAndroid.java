package v2update.subaru.android.usa.vehicles;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruNoVehicleAndroid extends SeeTestKeywords {
    String testName = "NoVehicleTest-Android";

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
        android_emailLoginIn(ConfigSingleton.configMap.get("noVehicleUserName"),ConfigSingleton.configMap.get("noVehiclePassword"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void dashboardWithoutVehicle() {
        sc.startStepsGroup("Test - Dashboard with No Vehicle Registered");
        validateDashboardWithoutVehicle();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void emailSignOut() {
        sc.startStepsGroup("Sign Out Test");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void validateDashboardWithoutVehicle(){
        verifyElementFound("NATIVE","xpath=//*[@text='Stay connected']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='wherever you go']",0);
        verifyElementFound("NATIVE","xpath=//*[@content-desc='Stay Connected Image']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Add a Vehicle']",0);
        click("NATIVE","xpath=//*[@text='Add a Vehicle']",0,1);
        sc.syncElements(2000,5000);
        verifyElementFound("NATIVE","xpath=//*[@id='bt_scan_vin']",0);
    }
}
