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
public class SubaruPersonalInfoAndroid extends SeeTestKeywords {
    String testName = "Personal Info - Android";

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
    public void test_personalDetails() {
        sc.startStepsGroup("Personal Details");
        validatePersonalDetails();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void test_homeAddress() {
        sc.startStepsGroup("Home Address");
        validateHomeAddress();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void test_preferredLanguage() {
        sc.startStepsGroup("Preferred Language");
        validatePreferredLanguage();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void validatePersonalDetails(){
        createLog("Started: validate Personal Details");
        if(!sc.isElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0)) {
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@text='Account']", 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0);
        click("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='textView_personal_details']", 0);
        click("NATIVE", "xpath=//*[@id='textView_personal_details']", 0, 1);

        //First Name
        verifyElementFound("NATIVE", "xpath=//*[@id='til_first_name']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='et_first_name']", 0);
        sendText("NATIVE", "xpath=//*[@id='et_first_name']", 0, "test first");
        sc.syncElements(3000, 60000);

        //Last Name
        verifyElementFound("NATIVE", "xpath=//*[@id='til_last_name']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='et_last_name']", 0);
        sendText("NATIVE", "xpath=//*[@id='et_last_name']", 0, "test last");
        sc.syncElements(3000, 60000);

        //Email Address
        verifyElementFound("NATIVE", "xpath=//*[@id='til_email_address']", 0);
        click("NATIVE", "xpath=//*[@id='til_email_address']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='et_edit_email_phone']", 0);
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='update_email_or_phone' and @enabled='false']", 0);
        sendText("NATIVE", "xpath=//*[@id='et_edit_email_phone']", 0, "testBZ4x_21mm@mail.tmnactupdate.io");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='update_email_or_phone' and @enabled='true']", 0);
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        verifyElementFound("NATIVE", "xpath=//*[@id='til_phone_number']", 0);

        //Profile Name
        verifyElementFound("NATIVE", "xpath=//*[@id='til_profile_name']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='et_profile_name']", 0);
        sendText("NATIVE", "xpath=//*[@id='et_profile_name']", 0, "test name");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='personal_detail_save_btn']", 0);
        click("NATIVE", "xpath=//*[@id='personal_detail_save_btn']", 0, 1);
        sc.syncElements(8000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Give Feedback']"))
            click("NATIVE", "xpath=//*[@id='feedback_dialog_close_image_view']", 0, 1);
        sc.syncElements(8000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='test name']", 0);


        verifyElementFound("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0);
        click("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='textView_personal_details']", 0);
        click("NATIVE", "xpath=//*[@id='textView_personal_details']", 0, 1);
        sendText("NATIVE", "xpath=//*[@id='et_first_name']", 0, "TEST");
        sc.syncElements(2000, 10000);
        sendText("NATIVE", "xpath=//*[@id='et_last_name']", 0, "BZ");
        sc.syncElements(2000, 10000);
        sendText("NATIVE", "xpath=//*[@id='et_profile_name']", 0, "test Bz");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='personal_detail_save_btn']", 0);
        click("NATIVE", "xpath=//*[@id='personal_detail_save_btn']", 0, 1);
        sc.syncElements(8000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Give Feedback']"))
            click("NATIVE", "xpath=//*[@id='feedback_dialog_close_image_view']", 0, 1);
        sc.syncElements(8000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='test Bz' or @text='Test Bz']", 0);
        createLog("Ended: validate Personal Details");
    }
    public static void validateHomeAddress(){
        createLog("Started: Validate Home Details");
        verifyElementFound("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0);
        click("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']", 0);
        click("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']", 0, 1);

        verifyElementFound("NATIVE", "xpath=//*[@id='country']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='state']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='line_1']", 0);
        sendText("NATIVE", "xpath=//*[@id='line_1']", 0, "Test Street1");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='city']", 0);
        sendText("NATIVE", "xpath=//*[@id='city']", 0, "Test City1");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='zip_code']", 0);
        sendText("NATIVE", "xpath=//*[@id='zip_code']", 0, "85001");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='billing_address_btn']", 0);
        click("NATIVE", "xpath=//*[@id='billing_address_btn']", 0, 1);
        sc.syncElements(4000, 80000);
        verifyElementFound("NATIVE", "xpath=//*[@id='subscription_continue_button']", 0);
        click("NATIVE", "xpath=//*[@id='subscription_continue_button']", 0, 1);
        sc.syncElements(5000, 80000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Give Feedback']"))
            click("NATIVE", "xpath=//*[@id='feedback_dialog_close_image_view']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0);
        click("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']", 0);
        click("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']", 0, 1);

        sendText("NATIVE", "xpath=//*[@id='line_1']", 0, "Test Street");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='city']", 0);
        sendText("NATIVE", "xpath=//*[@id='city']", 0, "Test City");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='zip_code']", 0);
        sendText("NATIVE", "xpath=//*[@id='zip_code']", 0, "85002");
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='billing_address_btn']", 0);
        click("NATIVE", "xpath=//*[@id='billing_address_btn']", 0, 1);
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='subscription_continue_button']", 0);
        click("NATIVE", "xpath=//*[@id='subscription_continue_button']", 0, 1);
        sc.syncElements(5000, 80000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Give Feedback']"))
            click("NATIVE", "xpath=//*[@id='feedback_dialog_close_image_view']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0);
        click("NATIVE", "xpath=//*[@text='PERSONAL INFO' or @text='Personal Info']", 0, 1);
        createLog("Started: Ended Home Details");
    }
    public static void validatePreferredLanguage(){
        createLog("Started:Preferred Language");
        verifyElementFound("NATIVE", "xpath=//*[@text='Preferred Language']", 0);
        click("NATIVE", "xpath=//*[@text='Preferred Language']", 0, 1);
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@text='ENGLISH' or @text='English']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='The selected language will be applied as the default for all connected services content.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SAVE' or @text='Save']", 0);
        createLog("Ended:Preferred Language");
    }
}
