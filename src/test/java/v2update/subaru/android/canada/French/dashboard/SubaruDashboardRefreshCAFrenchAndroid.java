package v2update.subaru.android.canada.French.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDashboardRefreshCAFrenchAndroid extends SeeTestKeywords {

    String testName = "DashboardRefreshCAFrench-Android";
   static String actualText = "";
    static String[] expectedText;

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")){
            case(""):
                testName = System.getProperty("cloudApp") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
                sc.stopStepsGroup();
        }
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void dashboardRefreshTest21MM() {
        sc.startStepsGroup("Test - Dashboard Refresh");
        refresh();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest21MM() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }



    public static void refresh() {
        createLog("Started - Refresh dashboard");
        //swipe to refresh dashboard screen
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_display_image']", 0);
        sc.flickElement("NATIVE","xpath=//*[@id='dashboard_display_image']",0,"down");

        //OAD01-13433 - Spinner loading icon is not automation capable, this is Android Native Pull to refresh, handled by Native lib.

        createLog("Verifying dashboard details after refresh");
        sc.syncElements(10000, 30000);
        //Verify Dashboard texts
        String descValues[] = sc.getAllValues("NATIVE", "xpath=//*[@class='android.widget.TextView' and @text]", "text");
        for (String value : descValues) {
            actualText = actualText.concat(value);
        }
        createLog(actualText);
        expectedText = new String[]{"Odomètre","Autonomie", "À distance", "Statut", "Santé", "Info", "Verrouiller", "Démarrer", "Déverrouiller", "Service", "Payer", "Boutique", "Trouver"};
        for (String strText : expectedText) {
            strText = convertTextToUTF8(strText);
            if (actualText.contains(strText)) {
                createLog("Validating text: " + strText);
                sc.report("Validation of " + strText, true);
            } else {
                sc.report("Validation of " + strText, false);
            }
        }
        createLog("Verified dashboard details after refresh");
        createLog("Completed - Refresh dashboard");
    }
}
