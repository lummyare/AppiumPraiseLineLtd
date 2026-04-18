package v2update.subaru.android.usa.shop;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSubscriptionsAndroid extends SeeTestKeywords {
    String testName = "Manage Subscriptions 21MM - Android";

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
    public void subscriptionsScreenValidations() {
        sc.startStepsGroup("Subscriptions Screen Validation");
        subscriptionsScreenValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void manageSubscriptionsScreenValidations() {
        sc.startStepsGroup("Subscriptions Screen Validation");
        manageSubscriptionScreenValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Sign Out Test");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void subscriptionsScreenValidation() {
        if (!(sc.isElementFound("NATIVE","xpath=//*[@text='Shop']"))){
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Shop']", 0);
        click("NATIVE", "xpath=//*[@text='Shop']", 0, 1);
        sc.syncElements(2000, 30000);

        createLog("Verifying Subscriptions on shop screen ");
        verifyElementFound("NATIVE", "xpath=//*[@text='Subscriptions']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Manage Subscriptions']", 0);
        click("NATIVE", "xpath=//*[@text='Manage Subscriptions']", 0, 1);
        sc.syncElements(2000, 30000);
        //bug
        //  verifyElementFound("NATIVE", "xpath=//*[@text='Subscriptions']", 0);
        createLog("Verifying trial services ");
        //bug
        // verifyElementFound("NATIVE", "xpath=//*[@text='Trial Services']", 0);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Safety Connect']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Safety Connect']//preceding::*//*[@content-desc='Service Icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Safety Connect']//preceding::*//*[@content-desc='Status Icon']", 0);
        }
        sc.swipe("Down", sc.p2cy(20), 5000);
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Drive Connect']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding::*[3]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*//*[@content-desc='Status Icon']", 0);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Remote Connect with Digital Key']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect with Digital Key']//preceding::*[3]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect with Digital Key']//preceding-sibling::*//*[@content-desc='Status Icon']", 0);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Add Service']", 0);
        click("NATIVE", "xpath=//*[@text='Add Service']", 0, 1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'MANAGE SUBSCRIPTION')] | //*[contains(@text,'Manage Subscription')]", 0);
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        sc.syncElements(2000, 30000);
    }

    public void manageSubscriptionScreenValidation() {
        if(!(sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'MANAGE SUBSCRIPTION')] | //*[contains(@text,'Manage Subscription')]"))){
            createLog("Navigating to Manage subscription page");
            navigateBackToSubscriptionPage();
            click("NATIVE", "xpath=//*[@text='Add Service']", 0, 1);
            sc.syncElements(2000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'MANAGE SUBSCRIPTION')] | //*[contains(@text,'Manage Subscription')]", 0);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Purchase additional services for your')]", 0);
        createLog("Verifying paid services ");
        //Drive Connect
        verifyElementFound("NATIVE","xpath=//*[@text='Drive Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Drive Connect']/preceding-sibling::*[@id='cb_check']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Drive Connect']/following-sibling::*[@id='iv_arrow']",0);
        click("NATIVE", "xpath=//*[@text='Drive Connect']/following-sibling::*[@id='iv_arrow']", 0, 1);
        sc.syncElements(2000, 30000);
        createLog("Verifying Drive Connect service details");
        verifyElementFound("NATIVE", "xpath=//*[@text='Service Details']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Subscriptions' and @id='iv_service']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Drive Connect empowers you to enhance your drive with increased command inside your vehicle. Through the voice-activated Intelligent Assistant, which will learn your driving preferences and work with Cloud Navigation')]",0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        createLog("Verified Drive Connect service details");
        sc.syncElements(2000, 30000);

        //Remote Connect with Digital Key
        verifyElementFound("NATIVE","xpath=//*[@text='Remote Connect with Digital Key']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Remote Connect with Digital Key']/preceding-sibling::*[@id='cb_check']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Remote Connect with Digital Key']/following-sibling::*[@id='iv_arrow']",0);
        click("NATIVE", "xpath=//*[@text='Remote Connect with Digital Key']/following-sibling::*[@id='iv_arrow']", 0, 1);
        sc.syncElements(2000, 30000);
        createLog("Verifying Remote Connect with Digital Key service details");
        verifyElementFound("NATIVE", "xpath=//*[@text='Service Details']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Subscriptions' and @id='iv_service']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect with Digital Key']", 0);
        verifyElementFound("NATIVE","xpath=//*[@text='With Remote Connect, check battery status, find the nearest charging station and more. Plus, with Digital Key, give access to your vehicle to others from almost anywhere.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        createLog("Verified Drive Connect service details");
        sc.syncElements(2000, 30000);

        createLog("Verifying 3rd party service");
        verifyElementFound("NATIVE", "xpath=//*[@text='3RD PARTY SERVICE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Wi-Fi Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Powered by AT&T']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Wi-Fi Connect']//following-sibling::*[1]", 0);
        click("NATIVE", "xpath=//*[@text='Wi-Fi Connect']//following-sibling::*[1]", 0, 1);
        sc.syncElements(2000, 30000);
        createLog("Verifying Wi-Fi Connect  service details");
        verifyElementFound("NATIVE", "xpath=//*[@text='Wi-Fi Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Connect up to 5 compatible devices')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='To continue using your Wi-Fi, go to AT&T to manage your Wi-Fi service.']", 0);
        sc.swipeWhileNotFound("DOWN", sc.p2cy(30), 2000, "NATIVE", "xpath=//*[@id='iv_carrier_icon']", 0, 1000, 2, false);
        verifyElementFound("NATIVE", "xpath=//*[@id='iv_carrier_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go to AT&T to manage your account.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go To AT&T']", 0);
        createLog("Verified 3rd party service");
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'MANAGE SUBSCRIPTION')] | //*[contains(@text,'Manage Subscription')]", 0);
    }

    public void addService() {}

    public void navigateBackToSubscriptionPage(){
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Add Service']")){
            for (int i = 1; i < 5; i++) {
                sc.syncElements(1000, 2000);
                if (sc.isElementFound("NATIVE", "xpath=//*[@text='Add Service']")) {
                    createLog("navigated back to add service page");
                    break;
                }
                else ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            }
        }
    }
}
