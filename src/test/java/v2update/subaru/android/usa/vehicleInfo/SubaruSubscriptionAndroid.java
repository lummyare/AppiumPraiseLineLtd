package v2update.subaru.android.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSubscriptionAndroid extends SeeTestKeywords {
    String testName = "Subscriptions V2 - Android";

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
    public void subscriptionPageTest() {
        sc.startStepsGroup("Test - Suscription Page");
        subscriptionPage();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void addServicesTest() {
        sc.startStepsGroup("Test - Add Services");
        addServices();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void subscriptionPage() {
        if (!(sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))){
            reLaunchApp_android();
        }
        sc.startStepsGroup("Subscriptions Page Validations");
        click("NATIVE", "xpath=//*[@text='Info']", 0, 1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Subscriptions']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Connected services for your vehicle']", 0);
        click("NATIVE", "xpath=//*[@text='Subscriptions']", 0, 1);
        sc.syncElements(2000, 30000);
        //subscriptions main screen validations
        verifyElementFound("NATIVE", "xpath=//*[@id='subscription_list_title']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='vehicle image']", 0);

        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Safety Connect']")){
            createLog("Safety Connect trial service is active");
            verifyElementFound("NATIVE", "xpath=//*[@text='Active\\nUp to 10-years, 4G Network Dependent']", 0);
            click("NATIVE", "xpath=//*[@text='Safety Connect']", 0, 1);
            sc.syncElements(2000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Service Details' or @text='SERVICE DETAILS']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='iv_service']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Safety Connect']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Trial Service']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Up to 10-years, 4G Network Dependent']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='btn_cancel_trials']", 0);

            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(2000, 30000);
        }

        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Drive Connect']")){
            createLog("Drive Connect trial service is active");
            click("NATIVE", "xpath=//*[@text='Drive Connect']", 0, 1);
            sc.syncElements(2000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Service Details' or @text='SERVICE DETAILS']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='iv_service']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Trial Service']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')] | //*[contains(@text,'Expires in')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='btn_cancel_trials']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(2000, 30000);
        }
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Remote Connect with Digital Key']")){
            createLog("Remote Connect with Digital Key trial service is active");
            click("NATIVE", "xpath=//*[@text='Remote Connect with Digital Key']", 0, 1);
            sc.syncElements(2000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Service Details' or @text='SERVICE DETAILS']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='iv_service']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect with Digital Key']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Trial Service']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')] | //*[contains(@text,'Expires in')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='btn_cancel_trials']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(2000, 30000);
        }else if(sc.isElementFound("NATIVE", "xpath=//*[@text='Remote Connect']")) {
            click("NATIVE", "xpath=//*[@text='Remote Connect']", 0, 1);
            sc.syncElements(2000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Service Details' or @text='SERVICE DETAILS']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='iv_service']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Trial Service']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')] | //*[contains(@text,'Expires in')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='btn_cancel_trials']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(2000, 30000);
        }
    }

    public void addServices() {
        //Add Service
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Add Service']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Add Service']", 0);
        sc.click("NATIVE", "xpath=//*[@text='Add Service']", 0, 1);
        sc.syncElements(30000, 60000);

        createLog("Verifying Manage Subscriptions screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Manage Subscription' or @text='MANAGE SUBSCRIPTION']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Purchase additional services for your')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue to Purchase' or @text='CONTINUE TO PURCHASE') and @enabled='false']", 0);
        //sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Wi-Fi Connect']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Wi-Fi Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Powered by AT&T']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='AT&T']", 0);

        //Navigate back to subscriptions screen
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Add Service']", 0);
    }
}
