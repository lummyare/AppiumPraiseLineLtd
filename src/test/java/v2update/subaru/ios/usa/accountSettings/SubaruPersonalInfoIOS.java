package v2update.subaru.ios.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruPersonalInfoIOS extends SeeTestKeywords {

    String testName = " Account Settings - Personal Info IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                testName = System.getProperty("cloudApp") + testName;
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                break;
        }
        iOS_Setup2_5(testName);
        sc.startStepsGroup("email SignIn 21MM");
        createLog("Start: 21mm email Login-USA-English");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("End: 21mm email Login-USA-English");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void personalDetailsTest() {
        sc.startStepsGroup("Test - Personal Details");
        personalDetails();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void homeAddressTest() {
        sc.startStepsGroup("Test - Home Address");
        homeAddress();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void preferredLanguageTest() {
        sc.startStepsGroup("Test - Preferred Language");
        preferredLanguage();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void personalDetails() {
        createLog("Started : Personal Details");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='person' or @id='user_profile_button']")) {
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0);
        click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='Personal Details']", 0);
        click("NATIVE", "xpath=//*[@id='Personal Details']", 0, 1);

        //First Name
        verifyElementFound("NATIVE", "xpath=//*[@id='First Name is']", 0);
        sendText("NATIVE", "xpath=//*[@id='First Name is']", 0, "test first");
        sc.syncElements(3000, 60000);

        //Last Name
        verifyElementFound("NATIVE", "xpath=//*[@id='Last Name is']", 0);
        sendText("NATIVE", "xpath=//*[@id='Last Name is']", 0, "test last");
        sc.syncElements(3000, 60000);

        //Email Address
        verifyElementFound("NATIVE", "xpath=//*[@id='Email Address is']", 0);
        click("NATIVE", "xpath=//*[@id='Email Address is']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='PROFILE_UPDATE_TEXTFIELD_EMAIL_PHONE']", 0);
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='PROFILE_UPDATE_VERIFY_BUTTON' and @enabled='false']", 0);
        sendText("NATIVE", "xpath=//*[@id='PROFILE_UPDATE_TEXTFIELD_EMAIL_PHONE']", 0, "testBZ4x_21mm@mail.tmnactupdate.io");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='PROFILE_UPDATE_VERIFY_BUTTON' and @enabled='true']", 0);
        click("NATIVE", "xpath=//*[@id='PROFILE_UPDATE_BACK_BUTTON']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='Mobile Number is' or @id='Enter Mobile Number']", 0);

        //Profile Name
        verifyElementFound("NATIVE", "xpath=//*[@placeholder='Profile Name']", 0);
        sendText("NATIVE", "xpath=//*[@placeholder='Profile Name']", 0, "test name");
        sc.syncElements(3000, 60000);

        verifyElementFound("NATIVE", "xpath=(//*[@id='SAVE' or @id='Save'])[1]", 0);
        click("NATIVE", "xpath=(//*[@id='SAVE' or @id='Save'])[1]", 0, 1);
        sc.syncElements(8000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Give Feedback']"))
            click("NATIVE", "xpath=//*[@id='feedback_dialog_close_image_view']", 0, 1);
        sc.syncElements(8000, 10000);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='test name']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0);
        click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='Personal Details']", 0);
        click("NATIVE", "xpath=//*[@id='Personal Details']", 0, 1);
        sendText("NATIVE", "xpath=//*[@id='First Name is']", 0, "TEST");
        sc.syncElements(2000, 10000);
        sendText("NATIVE", "xpath=//*[@id='Last Name is']", 0, "Bz");
        sc.syncElements(2000, 10000);
        sendText("NATIVE", "xpath=//*[@placeholder='Profile Name']", 0, "Test Bz");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=(//*[@id='SAVE' or @id='Save'])[1]", 0);
        click("NATIVE", "xpath=(//*[@id='SAVE' or @id='Save'])[1]", 0, 1);
        sc.syncElements(8000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Give Feedback']"))
            click("NATIVE", "xpath=//*[@id='feedback_dialog_close_image_view']", 0, 1);
        sc.syncElements(8000, 10000);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='Test Bz' or @id='test Bz']", 0);

        createLog("Completed : Personal Details");
    }

    public static void homeAddress() {
        createLog("Started : Home Address");

        if(!sc.isElementFound("NATIVE", "xpath=//*[@id='Home Address']")) {
            reLaunchApp_iOS();
            click("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0, 1);
            sc.syncElements(2000, 10000);
            click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0);
            click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@text='Personal Info']", 0);
        }

        verifyElementFound("NATIVE", "xpath=//*[@id='Home Address']", 0);
        click("NATIVE", "xpath=//*[@id='Home Address']", 0, 1);
        sc.syncElements(3000, 60000);

        verifyElementFound("NATIVE", "xpath=//*[@id='PI_COUNTRY']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='PI_STATE']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@id='PI_STREETADDRESS']", 0);
        sendText("NATIVE", "xpath=//*[@id='PI_STREETADDRESS']", 0, "Test Street1");
        sc.syncElements(3000, 60000);

        verifyElementFound("NATIVE", "xpath=//*[@id='PI_CITY']", 0);
        sendText("NATIVE", "xpath=//*[@id='PI_CITY']", 0, "Test City1");
        sc.syncElements(3000, 60000);

        verifyElementFound("NATIVE", "xpath=//*[@id='PI_ZIPCODE']", 0);
        sendText("NATIVE", "xpath=//*[@id='PI_ZIPCODE']", 0, "85001");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='PI_SUBMIT_BUTTON']", 0);
        click("NATIVE", "xpath=//*[@id='PI_SUBMIT_BUTTON']", 0, 1);
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='ADDRESS_VERIFICATION_CONTINUE_BUTTON']", 0);
        click("NATIVE", "xpath=//*[@id='ADDRESS_VERIFICATION_CONTINUE_BUTTON']", 0, 1);
        sc.syncElements(3000, 60000);
        click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0, 1);
        click("NATIVE", "xpath=//*[@id='Home Address']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='85001']", 0);

        sendText("NATIVE", "xpath=//*[@id='PI_STREETADDRESS']", 0, "Test Street");
        sc.syncElements(3000, 60000);
        sendText("NATIVE", "xpath=//*[@id='PI_CITY']", 0, "Test City");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='PI_ZIPCODE']", 0);
        sendText("NATIVE", "xpath=//*[@id='PI_ZIPCODE']", 0, "85002");
        verifyElementFound("NATIVE", "xpath=//*[@id='PI_SUBMIT_BUTTON']", 0);
        click("NATIVE", "xpath=//*[@id='PI_SUBMIT_BUTTON']", 0, 1);
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='ADDRESS_VERIFICATION_CONTINUE_BUTTON']", 0);
        click("NATIVE", "xpath=//*[@id='ADDRESS_VERIFICATION_CONTINUE_BUTTON']", 0, 1);
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0);
        click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0, 1);

        createLog("Completed : Home Address");
    }

    public static void preferredLanguage() {
        createLog("Started : Preferred Language");

        if(!sc.isElementFound("NATIVE", "xpath=//*[@id='Preferred Language']")) {
            reLaunchApp_iOS();
            click("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0, 1);
            sc.syncElements(2000, 10000);
            click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0);
            click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_PERSONAL_INFO_CELL']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@text='Personal Info']", 0);
        }

        verifyElementFound("NATIVE", "xpath=//*[@id='Preferred Language']", 0);
        click("NATIVE", "xpath=//*[@id='Preferred Language']", 0, 1);
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='The selected language will be applied as the default for all connected services content.']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@id='SAVE' or @id='Save'])[1]", 0);

        createLog("Completed : Preferred Language");
    }
}
