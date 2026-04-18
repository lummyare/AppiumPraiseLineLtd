package v2update.subaru.android.usa.accountSettings;

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
public class SubaruHelpAndFeedbackAndroid extends SeeTestKeywords {
    String testName = "Help and Feedback-Android";

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
    public void contactUsTest() {
        sc.startStepsGroup("Contact Us Lexus");
        contactUs();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    public void vehicleSupportTest() {
        sc.startStepsGroup("Vehicle Support Lexus");
        vehicleSupport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void contactUs() {
        createLog("Started: Contact Us Subaru");
        //navigate to contact us
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@text='Account']", 0, 1);
        sc.syncElements(2000, 4000);
        sc.swipe("Down", sc.p2cy(50), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@id='tv_feedback']", 0);
        click("NATIVE", "xpath=//*[@id='tv_feedback']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='tv_contact_us']", 0);
        click("NATIVE", "xpath=//*[@id='tv_contact_us']", 0, 1);

        //Customer Care
        verifyElementFound("NATIVE", "xpath=//*[@text='SOLTERRA CONNECT Customer Care']", 0);
        click("NATIVE", "xpath=//*[@text='SOLTERRA CONNECT Customer Care']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='1 866-384-3574'] | //*[@text='1 (866) 384-3574'] | //*[@text='18663843574']", 0);
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        sc.syncElements(5000, 10000);
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK)); sc.syncElements(5000, 10000);

        createLog("Ended: Contact Us Subaru");
    }

    public static void vehicleSupport() {
        createLog("Started: Support Lexus");
        reLaunchApp_android();
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@text='Account']", 0, 1);
        sc.syncElements(2000, 4000);
        sc.swipe("Down", sc.p2cy(50), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@id='tv_feedback']", 0);
        click("NATIVE", "xpath=//*[@id='tv_feedback']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Support']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Get help with questions and issues related to a vehicle on your account']", 0);
        click("NATIVE", "xpath=//*[@text='Vehicle Support']", 0, 1);
        //select lexus vehicle
        //sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='2022 NX 350 5 Door SUV 4X4']", 0, 1000, 5, false);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='2022 NX 350 5-DOOR SUV 4X4']"))
            click("NATIVE", "xpath=//*[@text='2022 NX 350 5-DOOR SUV 4X4']", 0, 1);
        sc.syncElements(5000, 10000);

        verifyElementFound("NATIVE", "xpath=//*[@id='iv_car_image']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='2024 Solterra Limited']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='JTMABABA8RA060836']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Recalls']", 0);
        click("NATIVE", "xpath=//*[@text='Recalls']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Health Report']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);

        verifyElementFound("NATIVE", "xpath=//*[@text='Contact Dealer']", 0);
        //TODO contact dealer opens select dealer and loads infintely
        /*click("NATIVE", "xpath=//*[@text='Contact Dealer']", 0, 1);
        sc.syncElements(5000, 10000);*/

        createLog("Ended: Support Lexus");
    }

}
