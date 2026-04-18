package v2update.subaru.android.canada.French.vehicles;


import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruNoVehicleTestCAFrenchAndroid extends SeeTestKeywords {
    String testName = "Subaru NoVehicleTestCAFrench-Android";
    @BeforeAll
    public void setup() throws Exception {
        android_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("21mm email Login");
        selectionOfCountry_Android("canada");
        selectionOfLanguage_Android("french");
        android_keepMeSignedIn(true);
        android_emailLoginFrench(ConfigSingleton.configMap.get("noVehicleUserName"), ConfigSingleton.configMap.get("noVehiclePassword"));
        sc.stopStepsGroup();
    }
    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void dashboardWithoutVehicle() {
        sc.startStepsGroup("Test - Dashboard with No Vehicle Registered CA French");
        validateDashboardWithoutVehicle();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOut(){
        sc.startStepsGroup("Test - Signout");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void validateDashboardWithoutVehicle(){
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Rester connecté']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='peu importe où tu vas']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Ajouter un véhicule']"),0);
        click("NATIVE",convertTextToUTF8("//*[@text='Ajouter un véhicule']"),0,1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Scanner le NIV']"),0);
    }
}