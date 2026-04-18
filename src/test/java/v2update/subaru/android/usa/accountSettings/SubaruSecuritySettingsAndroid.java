package v2update.subaru.android.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSecuritySettingsAndroid extends SeeTestKeywords {
    String testName = "Account - Security Settings - Android";

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
    public void securitySettingsPageTest() {
        sc.startStepsGroup("Security Settings");
        securitySettingsPageValidations();
        sc.stopStepsGroup();

    }

    @Test
    @Order(2)
    public void setResetPINTest() {
        sc.startStepsGroup("Set or Reset PIN");
        setResetPin();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void manageSavedProfileTest() {
        sc.startStepsGroup("Manage Saved Profile");
        manageSavedProfile();
        sc.stopStepsGroup();

    }
    @Test
    @Order(4)
    public void manageYourDataTest() {
        sc.startStepsGroup("Manage Your Data");
        manageYourData();
        sc.stopStepsGroup();
    }
    @Test
    @Order(5)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void securitySettingsPageValidations(){
        createLog("Start:Security page validations");
        navigateToSecuritySettingsScreen();
        sc.waitForElement("NATIVE", "xpath=//*[@id='account_security_login']", 0, 10);
        verifyElementFound("NATIVE", "xpath=//*[@id='biometric']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_security_biometric_login']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_security_biometric_login']//following-sibling::*", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_security_authentif']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_security_keepme_login']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_security_keepme_login']//following-sibling::*", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='messageauthentication']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_pin_textview']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_pin_details_textview']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='reset_pin_textview']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_profile_textview']", 0);
        sc.swipe("Down", sc.p2cy(40), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_profile_desc_textview']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='profile_saved_textview']", 0);
        createLog("End:Security page validations");
    }

    public static  void setResetPin(){
        createLog("Start:Set or Reset PIN validations");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@id='reset_pin_textview']", 0)) {
            navigateToSecuritySettingsScreen();
        }
        click("NATIVE", "xpath=//*[@id='reset_pin_textview']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(text(),'Verification Needed')]")) {
            click("NATIVE", "xpath=//*[@text='Confirm']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1' and @text='OK']"))
                click("NATIVE", "xpath=//*[@id='button1' and @text='OK']", 0, 2);
            delay(5000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='USE PASSWORD' or @text='Use Password'] | //*[@id='password_entry']")) {
                sendText("NATIVE", "xpath=//*[@id='password_entry']", 0, "101010");
                click("NATIVE", "xpath=//*[@text='Next']", 0, 1);
            } else if (sc.isElementFound("NATIVE", "xpath=//*[@id='etName']")) {
                sendText("NATIVE", "xpath=//*[@id='etName']", 0, "subarunextgen3@gmail.com");
                String email = sc.elementGetText("NATIVE", "xpath=//*[@id='etName']", 0);
                click("NATIVE", "xpath=//*[@id='btContinue']", 0, 1);
                sendText("NATIVE", "xpath=//*[@id='etPassword']", 0, "Subu213$$");
                click("NATIVE", "xpath=//*[@id='btSignIn']", 0, 1);
                delay(5000);
            }
            sc.syncElements(5000, 30000);
            for (int i = 0; i < 2; i++) {
                delay(2000);
                if (sc.isElementFound("NATIVE", "xpath=//*[@id='tv_rest_pin_label']"))
                    verifyElementFound("NATIVE", "xpath=//*[@id='tv_rest_pin_label']", 0);
                sc.syncElements(5000, 10000);
                sendText("NATIVE", "xpath=//*[@id='et_pin']", 0, "123456");
                delay(1000);
                if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='OK'] | //*[@text='OK']")) {
                    click("NATIVE", "xpath=//*[@content-desc='OK'] | //*[@text='OK']", 0, 1);
                }
            }
            sc.waitForElement("NATIVE", "xpath=//*[@text='SECURITY SETTINGS' or @text='Security Settings']", 0, 10);
            verifyElementFound("NATIVE", "xpath=//*[@text='SECURITY SETTINGS' or @text='Security Settings']", 0);
            createLog("End:Set or Reset PIN  validations");
        }else{
            sc.syncElements(5000, 30000);
            for (int i = 0; i < 2; i++) {
                delay(2000);
                if(sc.isElementFound("NATIVE","xpath=//*[@id='tv_rest_pin_label']"))
                    verifyElementFound("NATIVE", "xpath=//*[@id='tv_rest_pin_label']", 0);
                sc.syncElements(5000, 10000);
                sendText("NATIVE", "xpath=//*[@id='et_pin']", 0, "123456");
                delay(1000);
                if(sc.isElementFound("NATIVE","xpath=//*[@content-desc='OK'] | //*[@text='OK']")){
                    click("NATIVE", "xpath=//*[@content-desc='OK'] | //*[@text='OK']", 0, 1);
                }
            }
            sc.waitForElement("NATIVE", "xpath=//*[@text='SECURITY SETTINGS' or @text='Security Settings']", 0, 10);
            verifyElementFound("NATIVE", "xpath=//*[@text='SECURITY SETTINGS' or @text='Security Settings']", 0);
            createLog("End:Set or Reset PIN  validations");
        }
    }

    public static void manageSavedProfile(){
        createLog("Start: Manage Saved profile");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Manage Saved Profile']", 0)) {
            navigateToSecuritySettingsScreen();
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Manage Saved Profile']", 0, 1000, 1, false);
        click("NATIVE", "xpath=//*[@text='Manage Saved Profile']", 0, 1);
        sc.syncElements(5000, 10000);
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Here you can manage your profile')]")) {
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Here you can manage your profile')]", 0);
        }else if(sc.getElementCount("NATIVE","xpath=//*[@id='tv_driver_profile_details']")>0){
        }else{
            verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles in which your profile has been saved.'] | //*[@id='Vehicles in which your profile has been saved.']", 0);
        }
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='SECURITY SETTINGS' or @text='Security Settings']", 0);
        createLog("End: Manage Saved profile");
    }
    public static void manageYourData() {
        createLog("Start: Manage your data");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@id='delete_account']", 0)) {
            navigateToSecuritySettingsScreen();
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='delete_account']", 0, 1000, 1, false);
        verifyElementFound("NATIVE", "xpath=//*[@id='delete_account']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='delete_account_description']", 0);
        click("NATIVE", "xpath=//*[@id='delete_account']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='delete_my_personal_info']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='SECURITY SETTINGS' or @text='Security Settings']", 0);
        if(sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Navigate up']")) {
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(2000, 4000);
        }
        createLog("End: Manage your data");
    }
    public static void navigateToSecuritySettingsScreen() {
        if(!sc.isElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0))
            reLaunchApp_android();

        verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account']//following-sibling::*[contains(@class,'Button')]", 0);
        click("NATIVE", "xpath=//*[@text='Account']//following-sibling::*[contains(@class,'Button')]", 0, 1);
        click("NATIVE", "xpath=//*[@id='security_settings']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@contentDescription='OK'] | //*[@text='OK']"))
            sc.click("NATIVE", "xpath=//*[@contentDescription='OK'] | //*[@text='OK']", 0, 1);
        sc.syncElements(2000, 4000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Account']//following-sibling::*[contains(@class,'Button')]", 0)){
            verifyElementFound("NATIVE", "xpath=//*[@text='Account']//following-sibling::*[contains(@class,'Button')]", 0);
            click("NATIVE", "xpath=//*[@text='Account']//following-sibling::*[contains(@class,'Button')]", 0, 1);
            click("NATIVE", "xpath=//*[@id='security_settings']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@contentDescription='OK'] | //*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@contentDescription='OK'] | //*[@text='OK']", 0, 1);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='SECURITY SETTINGS' or @text='Security Settings']", 0);
        click("NATIVE", "xpath=//*[@text='SECURITY SETTINGS' or @text='Security Settings']", 0,1);

    }
}
