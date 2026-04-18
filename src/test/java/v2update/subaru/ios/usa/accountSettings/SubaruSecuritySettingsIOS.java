package v2update.subaru.ios.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSecuritySettingsIOS extends SeeTestKeywords {
    String testName = "Account Settings - Security Settings - IOS";

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
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }


    @Test
    @Order(1)
    public void securitySettingsTest() {
        sc.startStepsGroup("Security Settings");
        securitySettings();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void setPINTest() {
        sc.startStepsGroup("Set PIN");
        setPIN(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
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
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void securitySettings() {
        createLog("Started: Security Settings");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='person']")) {
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_SECURITY_SETTINGS_CELL']", 0, 1);
        sc.syncElements(2000, 10000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);

        String expectedText1[] = {"Security Settings", "Settings",
                "Require Authentication", "15 Minutes", "Keep Me Signed In", "Enabling the Require Authentication feature will add a layer of security to your Toyota app account. After running the app in the background for the time specified Biometric authentication is required",
                "Account PIN", "A 6-digit code used to access your profile on Drive Connect capable vehicles.", "Reset PIN", "Your Saved Profile", "View vehicles where your profile has been saved.", "Your Personal Information", "Control your data or submit a request to delete personal details",
                "Manage Your Data"};
        for (String detailsName : expectedText1) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);

            }
            break;
        }

        createLog("Completed: Security Settings");
    }

    public static void setPIN(String strUsername, String strPassword) {
        createLog("Started Set PIN");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='SECURITY SETTINGS' or @id='Security Settings']")) {
            navigateToSecuritySettingsScreen();
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='SECURITY SETTINGS' or @id='Security Settings']", 0);

        //if(sc.isElementFound("NATIVE","xpath=//*[@id='Set PIN']")) {
        click("NATIVE", "xpath=//*[@id='Set PIN' or @id='Reset PIN']", 0, 1);
        sc.syncElements(2000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Verification needed' and @class='UIAStaticText']")) {
            sc.syncElements(2000, 20000);
            click("NATIVE", "xpath=//*[@id='Confirm']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']")) {
                sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0, strUsername);
                click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_CONTINUE_BUTTON']", 0, 1);
                sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD']", 0, strPassword);
                click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']", 0, 1);
                delay(5000);
            }
        }
        for (int i = 0; i < 2; i++) {
            delay(2000);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='CREATE_PIN_LABEL_TITLE']", 0);
            sendText("NATIVE", "xpath=//*[@class='UIAView' and (./preceding-sibling::* | ./following-sibling::*)[@class='UIAView' and ./*[@class='UIAView']] and ./*[@class='UIAView' and ./*[./*[@class='UIAView']]] and ./parent::*[@class='UIAView']]", 0, "123456");
            delay(1000);
            if(sc.isElementFound("NATIVE","xpath=//*[@id='OK'] | //*[@text='OK']")){
                click("NATIVE", "xpath=//*[@id='OK'] | //*[@text='OK']", 0, 1);
                delay(1000);
            }
        }

        verifyElementFound("NATIVE", "xpath=//*[@id='SECURITY SETTINGS' or @id='Security Settings']", 0);
        createLog("Completed Set PIN");
    }

    public static void manageSavedProfile() {
        createLog("Started Manage Saved Profile");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='SECURITY SETTINGS' or @id='Security Settings']")) {
            navigateToSecuritySettingsScreen();
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='SECURITY SETTINGS' or @id='Security Settings']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='Your Personal Information' and @class='UIAStaticText' and ./parent::*[@class='UIATable']]", 0, 1000, 1, true);
        click("NATIVE", "xpath=//*[@id='Manage Saved Profile']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Here you can manage your')]")) {
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Here you can manage your')]", 0);
        }else if(sc.getElementCount("NATIVE","xpath=//*[@accessibilityLabel='Driver Profiles']")>0){
            createLog("Driver Profile found");
        }else{
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Vehicles in which your profile has been saved.']", 0);
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(4000,8000);
        verifyElementFound("NATIVE", "xpath=//*[@id='SECURITY SETTINGS' or @id='Security Settings']", 0);
        createLog("Completed Manage Saved Profile");
    }

    public static void manageYourData() {
        createLog("Started Manage Your Data");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='SECURITY SETTINGS' or @id='Security Settings']")) {
            navigateToSecuritySettingsScreen();
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='SECURITY SETTINGS' or @id='Security Settings']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='Manage Your Data' and @visible='true']", 0, 1000, 1, false);
        click("NATIVE", "xpath=//*[@id='Manage Your Data']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Delete My Personal Information']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Your Privacy Choices']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='SECURITY SETTINGS' or @id='Security Settings']", 0);
        //click back in security settings screen
        click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
        //click back in Account settings screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,30000);
        //Click close in accounts
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed Manage Your Data");
    }

    public static void navigateToSecuritySettingsScreen() {
        createLog("Started: navigating to security settings screen");
        reLaunchApp_iOS();
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_SECURITY_SETTINGS_CELL']", 0, 1);
        sc.syncElements(2000, 10000);
        createLog("Completed: navigating to security settings screen");
    }
}
