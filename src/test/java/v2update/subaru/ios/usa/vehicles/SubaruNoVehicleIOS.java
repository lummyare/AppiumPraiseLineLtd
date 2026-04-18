package v2update.subaru.ios.usa.vehicles;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruNoVehicleIOS extends SeeTestKeywords {
    String testName = "EV - NoVehicle - IOS";

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
                ios_emailLogin(ConfigSingleton.configMap.get("noVehicleUserName"), ConfigSingleton.configMap.get("noVehiclePassword"));
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
                ios_emailLogin(ConfigSingleton.configMap.get("noVehicleUserName"), ConfigSingleton.configMap.get("noVehiclePassword"));
                sc.stopStepsGroup();
        }
    }
    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }
    @Test
    @Order(1)
    public void dashboardWithoutVehicle() {
        sc.startStepsGroup("Test - Dashboard with No Vehicle Registered");
        validateDashboardWithoutVehicle();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    public void signOut() {
        sc.startStepsGroup("Signout");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateDashboardWithoutVehicle() {
        verifyElementFound("NATIVE","xpath=//*[@text='person']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Stay connected']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='wherever you go']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='no_vehicle_subaru_background']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Add a Vehicle']",0);
        click("NATIVE","xpath=//*[@text='Add a Vehicle']",0,1);
        sc.syncElements(2000,5000);

    }
}
