package v2update.subaru.ios.usa.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDashboardRefreshIOS extends SeeTestKeywords {
    String testName = "DashboardRefresh-IOS";
    static String actualText = "";
    static String[] expectedText;

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
    public void dashboardRefreshTest21MM() {
        sc.startStepsGroup("Test - Dashboard Refresh 21MM");
        refreshInRemoteSection();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }
    public static void refreshInRemoteSection() {
        createLog("Started - Refresh dashboard");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        //swipe to refresh dashboard screen
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        sc.flickElement("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0,"down");
        sc.syncElements(5000, 30000);
        //verify refresh spinner icon
        if(sc.waitForElement("NATIVE","xpath=(//*[@label='In progress'])[1]",0,20000)){
            createLog("Refresh - Spinner icon displayed");
        } else {
            createLog("Refresh - Spinner icon not displayed");
        }
        delay(5000);
        sc.syncElements(15000, 60000);
        if(!sc.waitForElement("NATIVE","xpath=(//*[@label='In progress'])[1]",0,20000)){
            createLog("Refresh - Spinner icon not displayed");
        } else {
            createLog("Refresh - Spinner icon is still displayed after 30 sec wait");
        }

        createLog("Verifying dashboard details after refresh");
        sc.syncElements(5000, 60000);
        //Verify Dashboard texts
        actualText = sc.getText("NATIVE");
        createLog(actualText);
        expectedText = new String[]{"Odometer", "Double tap to open vehicle switcher!", "Fuel", "Remote", "Status", "Health", "Vehicle image, double tap to open vehicle info.", "Info", "Swipe up to open advanced remote", "Lock", "Start", "Unlock", "Service", "Pay", "Shop", "Find"};
        for (String strText : expectedText) {
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
