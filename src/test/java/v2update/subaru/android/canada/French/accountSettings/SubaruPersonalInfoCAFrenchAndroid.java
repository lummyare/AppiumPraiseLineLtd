package v2update.subaru.android.canada.French.accountSettings;

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
public class SubaruPersonalInfoCAFrenchAndroid extends SeeTestKeywords {

    String testName = "SubaruPersonalInfoCAFrench - Android";

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
                android_emailLoginFrench("subarustageca@mail.tmnact.io", "Test$123");
                sc.stopStepsGroup();
        }
    }
    public void exit(){
        exitAll(this.testName);
    }


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
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@text='Compte']", 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Infos personnels' or @text='INFOS PERSONNELS']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Infos personnels' or @text='INFOS PERSONNELS']"), 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='textView_personal_details']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Coordonnées Personnelles']"), 0);
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
        verifyElementFound("NATIVE", "xpath=//*[@id='personal_detail_save_btn']", 0);
        click("NATIVE", "xpath=//*[@id='personal_detail_save_btn']", 0, 1);
        sc.syncElements(8000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='feedback_dialog_close_image_view']"))
            click("NATIVE", "xpath=//*[@id='feedback_dialog_close_image_view']", 0, 1);
        sc.syncElements(8000, 10000);
    }
    public static void validateHomeAddress(){
        createLog("Started: Validate Home Details");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Infos personnels' or @text='INFOS PERSONNELS']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Infos personnels' or @text='INFOS PERSONNELS']"), 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='textView_billing_address']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Adresse de domicile']", 0);
        click("NATIVE", "xpath=//*[@id='textView_billing_address']", 0, 1);

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
        createLog("Started: Ended Home Details");
    }
    public static void validatePreferredLanguage(){
        createLog("Started:Preferred Language");
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Infos personnels' or @text='INFOS PERSONNELS']")){
            click("NATIVE","xpath=//*[@text='Infos personnels' or @text='INFOS PERSONNELS']",0,1);
            sc.syncElements(2000, 80000);

        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Langue préférée']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Langue préférée']"), 0, 1);
        sc.syncElements(3000, 60000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Français']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='La langue sélectionnée sera appliquée par défaut à tous les services connectés disponibles.']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder' or @text='SAUVEGARDER']", 0);
        createLog("Ended:Preferred Language");
    }

}