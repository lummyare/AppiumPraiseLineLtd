package v2update.subaru.ios.canada.french.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDashboardRefreshCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruDashboardRefreshCAFrench-IOS";
    static String actualText = "";
    static String[] expectedText;

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
        ios_emailLoginFrench("subarunextgen3@gmail.com","Test$12345");
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void dashboardRefreshTest() {
        sc.startStepsGroup("Test - Dashboard Refresh");
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
        expectedText = new String[]{"Odomètre", "Double tap to open vehicle switcher!", "À distance", "Statut", "Santé", "Vehicle image, double tap to open vehicle info.", "Info", "Swipe up to open advanced remote", "Verrouiller", "Démarrer", "Déverrouiller", "Service", "Payer", "Boutique", "Trouver"};
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
