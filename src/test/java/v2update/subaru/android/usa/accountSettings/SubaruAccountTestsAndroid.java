package v2update.subaru.android.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruAccountTestsAndroid extends SeeTestKeywords {
    String testName="Account Test - Android";

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
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void accountScreenValidations() {
        sc.startStepsGroup("Test-Account Settings page");
        validateAccountScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    public void notificationsScreen(){
        sc.startStepsGroup("Test-Notifications screen");
        validateNotificationsScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    @Disabled
    public void takeATourScreen21mm(){
        //TODO not available for testing
        sc.startStepsGroup("Test-Take a tour screen");
        validateTakeTourScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(4)
    public void darkModeValidations21mm(){
        sc.startStepsGroup("Test-Dark Mode");
        validateDarkMode();
        sc.stopStepsGroup();
    }
    @Test
    @Order(5)
    public void signOut21mm() {
        sc.startStepsGroup(" Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public void validateAccountScreen() {
        createLog("Started:validate Account Screen");
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sign Out']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Profile Picture']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Manage your Profile & Settings']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Inbox']", 0);
        //Notifications
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Notifications']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Notifications']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='See notifications for your vehicle and account']", 0);
        //Take a Tour
        verifyElementFound("NATIVE", "xpath=//*[@text='Take A Tour']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Explore the app to see what’s new']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Take A Tour']", 0);
        //Dark Mode
        verifyElementFound("NATIVE", "xpath=//*[@id='account_dark_mode_text']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Dark Mode']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='account_dark_mode_switch_off']", 0);
        createLog("Ended:validate Account Screen");
    }

    public void validateNotificationsScreen() {
        createLog("Started:validate Notifications Screen");
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@text='Notifications']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@class='android.widget.ImageButton']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Notifications'] | //*[@text='NOTIFICATIONS']", 0);
        //Validate Notifications displayed
        int count=sc.getElementCount("NATIVE","xpath=//*[@id='nh_title']");
        if(count>0){
            createLog("Notifications list :"+count+" displayed");
        }else
            createErrorLog("Notifications list is empty");

        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(5000, 10000);
        createLog("Ended:validate Notifications Screen");
    }

    public void validateTakeTourScreen() {
        createLog("Started:validate Take A Tour Screen");
        click("NATIVE", "xpath=//*[@text='Take A Tour']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='See What’s New']", 0);
        click("NATIVE", "xpath=//*[@text='Start the Tour']//following-sibling::*[contains(@class,'Button')]", 0, 1);
        sc.syncElements(5000, 10000);

        //Validate 5 screens for Take a tour.
        waitForElement("NATIVE", "xpath=//*[contains(@text,'add or switch vehicles')]",0,10);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'add or switch vehicles')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='1 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Next']//following-sibling::*[@class='android.widget.Button']", 0);
        click("NATIVE", "xpath=//*[@text='Next']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 10000);
        //Screen2
        waitForElement("NATIVE", "xpath=//*[@text='Remote Connect']",0,10);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='2 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Next']//following-sibling::*[@class='android.widget.Button']", 0);
        click("NATIVE", "xpath=//*[@text='Next']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 10000);
        //Screen3
        waitForElement("NATIVE", "xpath=//*[@text='Vehicle Status']",0,10);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Status']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='3 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Next']//following-sibling::*[@class='android.widget.Button']", 0);
        click("NATIVE", "xpath=//*[@text='Next']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 10000);
        //Screen4
        waitForElement("NATIVE", "xpath=//*[@text='Health']",0,10);
        verifyElementFound("NATIVE", "xpath=//*[@text='Health']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='4 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Next']//following-sibling::*[@class='android.widget.Button']", 0);
        click("NATIVE", "xpath=//*[@text='Next']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 10000);
        //Screen5
        waitForElement("NATIVE", "xpath=//*[@text='Vehicle Info']",0,10);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='5 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Done']//following-sibling::*[@class='android.widget.Button']", 0);
        click("NATIVE", "xpath=//*[@text='Done']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK'] | //*[@text='Ok']")) {
            click("NATIVE", "xpath=//*[@text='OK'] | //*[@text='Ok']", 0, 1);
            sc.syncElements(5000, 10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        createLog("Ended:validate Take A Tour Screen");
    }

    public void validateDarkMode() {
        createLog("Started:validate Dark Mode");
        if(!(sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))){
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(5000, 10000);
        sc.swipe("Down", sc.p2cx(70), 100);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_dark_mode_text']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Dark Mode']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='account_dark_mode_switch_off']", 0);
        click("NATIVE", "xpath=//*[@resource-id='account_dark_mode_switch_off']", 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='account_dark_mode_switch_on']", 0);
        click("NATIVE", "xpath=//*[@resource-id='account_dark_mode_switch_on']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='account_dark_mode_switch_off']", 0);
        createLog("Ended:validate Dark Mode");
    }

    public void waitForElement(String strZone, String element, int intIndex,int timeout) {
        try {
            sc.waitForElement(strZone,element,intIndex,timeout);
            createLog("Element :" + element + " Found");
            sc.report("Element :" + element + " Found", true);
        } catch (Exception IgnoreException) {
            createErrorLog("Element"+element+" not found");
            sc.report("" + IgnoreException, false);
        }
    }
}
