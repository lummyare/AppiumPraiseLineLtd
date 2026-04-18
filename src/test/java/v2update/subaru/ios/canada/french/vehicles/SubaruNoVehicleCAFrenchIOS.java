package v2update.subaru.ios.canada.french.vehicles;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruNoVehicleCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruNoVehicleCAFrench - IOS";

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
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLoginFrench(ConfigSingleton.configMap.get("noVehicleUserName"), ConfigSingleton.configMap.get("noVehiclePassword"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
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
        sc.startStepsGroup("SignOut");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateDashboardWithoutVehicle() {
        verifyElementFound("NATIVE","xpath=//*[@name='user_profile_button']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Rester connecté']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='peu importe où tu vas']"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='no_vehicle_subaru_background']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Ajouter un véhicule']"),0);
        sc.syncElements(2000,5000);
    }
}
