package v2update.subaru.android.usa.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDashboardRefresh extends SeeTestKeywords {
    String testName="Dashboard Top Navigation - Android";

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
    public void dashboardRefreshTest(){
        sc.startStepsGroup("Test - Dashboard Refresh");
        refreshEV();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest(){
        sc.startStepsGroup("Sign Out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void refreshEV() {
        createLog("Started - Refresh dashboard");
        //swipe to refresh dashboard screen
        sc.syncElements(5000, 30000);
        if(!(sc.isElementFound("NATIVE", "xpath=//*[@id='dashboard_display_image']",0))){
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_display_image']", 0);
        sc.flickElement("NATIVE","xpath=//*[@id='dashboard_display_image']",0,"down");

        //OAD01-13433 - Spinner loading icon is not automation capable, this is Android Native Pull to refresh, handled by Native lib.

        createLog("Verifying dashboard details after refresh");
        sc.syncElements(10000, 30000);
        //Verify Dashboard texts
        String actualText = "";
        String descValues[] = sc.getAllValues("NATIVE", "xpath=//*[@class='android.widget.TextView' and @text]", "text");
        for (String value : descValues) {
            actualText = actualText.concat(value);
        }
        createLog(actualText);
        String[] expectedText = new String[]{"Remote", "Status", "Health", "Info", "Lock", "Start", "Unlock", "Service", "Pay", "Shop", "Find"};
        for (String strText : expectedText) {
            if (actualText.contains(strText)) {
                createLog("Validating text: " + strText);
                sc.report("Validation of " + strText, true);
            } else {
                sc.report("Validation of " + strText, false);
            }
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='fuel_view_fuel_bar_view']", 0);

        createLog("Verified dashboard details after refresh");
        createLog("Completed - Refresh dashboard");
    }
}
