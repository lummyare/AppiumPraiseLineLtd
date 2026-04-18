package v2update.subaru.android.usa.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import v2update.android.usa.remote.Remote17CYPlusAndroid;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruGuestDriverAndroid extends SeeTestKeywords {
    String testName = "GuestDriver-Android";

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
    public void GuestDriveHomePage() {
        sc.startStepsGroup("Guest Driver Home page validations");
        guestDriverHomePageValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    @Disabled
    public void test_guestDriverShareRemote() {
        sc.startStepsGroup("Guest Driver Share Remote");
        guestDriverShareRemote();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    @Disabled
    public void test_logoutPrimary() {
        sc.startStepsGroup("Test - Sign out Primary");
        android_SignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    @Disabled
    public void test_LoginWithGuestDriver() {
        sc.startStepsGroup("Email Login Guest");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strGuestDriverEmail"), ConfigSingleton.configMap.get("strGuestDriverPassword"));
        sc.stopStepsGroup();

    }

    @Test
    @Order(5)
    @Disabled
    public void test_ActivateGuestRemote() throws Exception {
        sc.startStepsGroup("Activate Guest Remote");
        activateGuest();
        sc.stopStepsGroup();

    }
    @Test
    @Order(6)
    @Disabled
    public void signOut(){
        sc.startStepsGroup("Test - Sign out Guest");
        android_SignOut();
        sc.stopStepsGroup();
    }
    @Test
    @Order(7)
    @Disabled
    public void test_LoginWithGuestDriverAndValidateRemoteCommands() {
        sc.startStepsGroup("Email Login Guest-Validate remote commands");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strGuestDriverEmail"), ConfigSingleton.configMap.get("strGuestDriverPassword"));
        validateRemoteCommandsDisplayed();
        sc.stopStepsGroup();

    }

    @Test
    @Order(8)
    @Disabled
    public void remoteLockGuest() {
        sc.startStepsGroup("Validate Guest Remote Lock");
        Remote17CYPlusAndroid.executeRemoteLock();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    @Disabled
    public void test_logoutGuest() {
        sc.startStepsGroup("Sign out Guest email");
        android_SignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)@Disabled
    public void test_emailLoginPrimary() {
        sc.startStepsGroup("Email Login Primary");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strEmail17CYPlus"), ConfigSingleton.configMap.get("strPassword17CYPlus"));
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    @Disabled
    public void test_removeDriver() {
        sc.startStepsGroup("Remove Driver");
        removeDriver();
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    @Disabled
    public void test_ActivatePrimaryRemote() throws Exception {
        sc.startStepsGroup("Activate Primary Remote");
        activatePrimaryRemote();
        sc.stopStepsGroup();
    }
    @Test
    @Order(13)
    @Disabled
    public void test_logoutPrimaryForActivateRemote() {
        sc.startStepsGroup("Logout Primary For Activate Remote");
        android_SignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(14)
    @Disabled
    public void test_emailLoginPrimaryForRemoteValidations() {
        sc.startStepsGroup("Email Login Primary for Remote validations");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strEmail17CYPlus"), ConfigSingleton.configMap.get("strPassword17CYPlus"));
        validateRemoteCommandsDisplayed();
        sc.stopStepsGroup();
    }

    @Test
    @Order(15)
    @Disabled
    public void test_logout17CY() {
        sc.startStepsGroup("Test - Sign out Primary-17CY");
        android_SignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(15)
    public void guestAndValetAlerts() {
        sc.startStepsGroup("Guest And Valet Alerts Verification");
        //check Valet alerts is On or oFF
        guestAlertsValidation();
        //check Guest alerts is On or oFF
        valetAlertsValidation();
        sc.stopStepsGroup();

    }

    @Test
    @Order(16)
    public void guestSettingsHomePage() {
        sc.startStepsGroup("Start: Guest Setting home page");
        guestSettingsHomePageValidations();
        sc.stopStepsGroup();

    }

    @Test
    @Order(17)
    public void valetSettingsHomePage() {
        sc.startStepsGroup("Start: Valet Setting home page validations");
        valetSettingsHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(18)
    public void test_guestDriverValetDrivingLimitsSpeed() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Speed");
        drivingLimitSpeed("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(19)
    public void test_guestDriverValetDrivingLimitsMiles() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Miles");
        drivingLimitsMiles("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(20)
    public void guestDriverValetDrivingLimitsArea() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Area");
        drivingLimitsArea("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(21)
    public void test_guestDriverValetDrivingLimitsCurfew() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Curfew");
        drivingLimitsCurfew("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(22)
    public void test_guestDriverValetDrivingLimitsTime() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Time");
        drivingLimitsTime("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(23)
    public void test_guestDriverGuestDrivingLimitsSpeed() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Speed");
        drivingLimitSpeed("Guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(24)
    public void test_guestDriverGuestDrivingLimitsMiles() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Miles");
        drivingLimitsMiles("Guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(25)
    public void guestDriverGuestDrivingLimitsArea() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Area");
        drivingLimitsArea("Guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(26)
    public void test_guestDriverGuestDrivingLimitsCurfew() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Curfew");
        drivingLimitsCurfew("Guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(27)
    public void test_guestDriverGuestDrivingLimitsTime() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Time");
        drivingLimitsTime("Guest");
        sc.stopStepsGroup();
    }

    public void validateRemoteCommandsDisplayed(){
        createLog("Started: Verify Remote commands displayed");
        verifyElementFound("NATIVE", "xpath=//*[@text='fuel']",0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Charged') or contains(@content-desc,'charged')]",0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Lock']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Start']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Unlock']", 0);
        createLog("Ended: Verify Remote commands displayed");
    }
    public void removeDriver(){
        //TODO Update when Remote Auth API key is available
        createLog("Started: Remove Driver");
        strVINType = setVinType("JTMFB3FV5MD006786");
        if(strVINType.equalsIgnoreCase("21MM")){
            //VehicleSelectionAndroid.Default("5TDADAB59RS000015");
        }else if(strVINType.equalsIgnoreCase("17CYPLUS")) {
            //VehicleSelectionAndroid.Default("JTMFB3FV5MD006786");
        }
        click("NATIVE", "xpath=//*[@contentDescription='Remove Driver']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Remove Driver']", 0);
        click("NATIVE", "xpath=//*[@contentDescription='Remove']", 0, 1);
        sc.syncElements(10000, 30000);
        createLog("Ended: Remove Driver");
    }
    public void activatePrimaryRemote() throws Exception{
        //TODO Update when Remote Auth API key is available
        createLog("Started: Activate primary remote");
        strVINType = setVinType("JTMFB3FV5MD006786");
        if(strVINType.equalsIgnoreCase("21MM")){
            verifyElementFound("NATIVE", "xpath=//*[@text='fuel']",0);
            //verifyElementFound("NATIVE", "xpath=//*[@content-desc='battery_charge']",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Charged') or contains(@content-desc,'charged')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Activate Remote')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'To use remote features like start, stop, lock and unlock, we need to verify you as the owner of the vehicle.')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Enter Authorization Code')]",0);
            click("NATIVE", "xpath=//*[@content-desc='Enter Authorization Code']", 0,1);
            click("NATIVE", "xpath=//*[@contentDescription='Navigate up']", 0, 1);
        }else if(strVINType.equalsIgnoreCase("17CYPLUS")){
            verifyElementFound("NATIVE", "xpath=//*[@text='fuel']",0);
            //verifyElementFound("NATIVE", "xpath=//*[@content-desc='battery_charge']",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Charged') or contains(@content-desc,'charged')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Activate Remote')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'To use remote features like start, stop, lock and unlock, we need to verify you as the owner of the vehicle.')]",0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Activate']",0);
            click("NATIVE", "xpath=//*[@content-desc='Activate']", 0,1);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Send Activation Code') or contains(@text,'SEND ACTIVATION CODE')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Text Message')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Email')]",0);
            click("NATIVE", "xpath=//*[@id='cancel_button']", 0,1);
        }
        createLog("Started: remote activation");
        RemoteAuthForGuestDriver("prod", "17cytest_21mm@mail.tmnact.io", "JTMFB3FV5MD006786");
        createLog("Ended: remote activation");
        createLog("Ended: Activate primary remote");
    }

    public static void guestDriverHomePageValidations() {
        createLog("Started: Guest Driver Home Page Validations");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();

        click("NATIVE","xpath=//*[@id='dashboard_remote_open_iconbutton']",0,1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Share your vehicles remote' or @text='Share remote and track speed limits']", 0);

        //Open GuestDriver Home page
        click("NATIVE", "xpath=//*[@id='guest_driver_valet_icon_cta']", 0, 1);
        sc.syncElements(4000, 8000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Share your vehicle')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Back']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Valet']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='guest_driver_valet_icon_cta']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='guest_driver_advance_icon_cta']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
        createLog("Ended: Guest Driver Home Page Validations");
    }
    public void guestDriverShareRemote(){
        createLog("Started: Share remote to guest");
        click("NATIVE","xpath=//*[@id='dashboard_remote_open_iconbutton']",0,1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
        click("NATIVE", "xpath=//*[@id='guest_driver_valet_icon_cta']", 0, 1);
        sc.syncElements(3000, 6000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
        click("NATIVE", "xpath=//*[@text='Share Remote']", 0, 1);
        sc.syncElements(5000, 10000);

        verifyElementFound("NATIVE", "xpath=//*[@id='remote_share_toolbar_title']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='guest_avatar_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remote Access']",0);
        createLog("Add driver");
        //TODO Bug: OAD01-23369, replace below locator when fixed
        click("NATIVE", "xpath=//*[@id='share_remote_cta']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='add_guest_driver_title']",0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Driver needs to have the')]",0);
        click("NATIVE", "xpath=//*[@text='Search by Email or Phone']", 0, 1);
        //sendText("NATIVE", "xpath=//*[@text='Search by Email or Phone']",0, ConfigSingleton.configMap.get("strGuestDriverEmail") );
        sc.sendText(ConfigSingleton.configMap.get("strGuestDriverEmail"));
        click("NATIVE", "xpath=//*[@text='Search']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='GU*** DR****']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='guestdriver_21mm@mail.tmnact.io']",0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Guest Driver']",0);
        createLog("Invite driver");
        //TODO Finish when Remote Auth API key is available
        //click("NATIVE", "xpath=//*[@text='Invite Driver']", 0, 1);
        sc.syncElements(5000, 10000);
        sc.waitForElement("NATIVE","xpath=//*[@content-desc='Invite Sent']",0,10);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Invite Sent']",0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='tick_icon']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Back to Dashboard']", 0, 1);
        sc.syncElements(5000, 10000);
        sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
        sc.syncElements(10000, 20000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Your guest has remote access')]",0);
        createLog("Ended: Share remote to guest");
    }
    public void activateGuest() throws Exception{
        //TODO Update when Remote Auth API key is available
        createLog("Started: activate Guest");
        strVINType = setVinType("JTMFB3FV5MD006786");
        if(strVINType.equalsIgnoreCase("21MM")){
            verifyElementFound("NATIVE", "xpath=//*[@text='fuel']",0);
            //verifyElementFound("NATIVE", "xpath=//*[@content-desc='battery_charge']",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Charged') or contains(@content-desc,'charged')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Activate Remote')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'To use remote features like start, stop, lock and unlock, we need to verify you as the owner of the vehicle.')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Enter Authorization Code')]",0);
            click("NATIVE", "xpath=//*[@content-desc='Enter Authorization Code']", 0,1);
            //need to update for 21mm vehicle
            /*
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SHOW_TITLE_LABEL']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SHOW_BODY_LABEL']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SHOW_INSTRUCTION_BUTTON']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SKIP_INSTRUCTIONS_BUTTON']",0);
            */
            click("NATIVE", "xpath=//*[@contentDescription='Navigate up']", 0, 1);
        }else if(strVINType.equalsIgnoreCase("17CYPLUS")){
            verifyElementFound("NATIVE", "xpath=//*[@text='fuel']",0);
            //verifyElementFound("NATIVE", "xpath=//*[@content-desc='battery_charge']",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Charged') or contains(@content-desc,'charged')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Activate Remote')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'To use remote features like start, stop, lock and unlock, we need to verify you as the owner of the vehicle.')]",0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Activate']",0);
            click("NATIVE", "xpath=//*[@content-desc='Activate']", 0,1);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Send Activation Code') or contains(@text,'SEND ACTIVATION CODE')]",0);
            //verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Text Message')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Email')]",0);
            click("NATIVE", "xpath=//*[@id='cancel_button']", 0,1);
        }
        RemoteAuthForGuestDriver("prod", "guestdriver_21mm@mail.tmnact.io", "JTMFB3FV5MD006786");
        sc.syncElements(5000, 10000);
        createLog("Ended: activate Guest");
    }
    public static void valetAlertsValidation() {
        createLog("Started: activate Valet");
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Valet']/following-sibling::*[@text='Alerts are On']", 0)) {
            createLog("Valet Alerts already turned ON");
            createLog("Turning OFF valets alerts");
            click("NATIVE", "xpath=//*[@text='Valet']", 0, 1);
            sc.syncElements(10000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Valet']/following-sibling::*[@text='Alerts are Off']")) {
                createLog("Valet Alerts OFF");
            } else
                createErrorLog("Valet Alerts not turned OFF");
        } else {
            createLog("Valet Alerts are turned OFF");
            createLog("Turning ON Valet Alerts");
            click("NATIVE", "xpath=//*[@text='Valet']", 0, 1);
            sc.syncElements(10000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Valet']/following-sibling::*[@text='Alerts are On']", 0);
            createLog("Valet Alerts are turned ON");
            //switch off alerts
            click("NATIVE", "xpath=//*[@text='Valet']", 0, 1);
            sc.syncElements(10000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Valet']/following-sibling::*[@text='Alerts are On']")) {
                createErrorLog("Valet alerts not turned off");
            } else
                createLog("Valet alerts turned off");
        }
    }

    public static void guestAlertsValidation() {
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Guest']/following-sibling::*[@text='Alerts are On']", 0)) {
            createLog("Guest Alerts already turned ON");
            createLog("Turning OFF Guest alerts");
            click("NATIVE", "xpath=//*[@text='Guest']", 0, 1);
            sc.syncElements(10000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Guest']/following-sibling::*[@text='Alerts are Off']")) {
                createLog("Guest Alerts OFF");
            } else
                createErrorLog("Guest Alerts not turned OFF");
        } else {
            createLog("Turning On Guest alerts");
            click("NATIVE", "xpath=//*[@text='Guest']", 0, 1);
            sc.syncElements(10000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest']/following-sibling::*[@text='Alerts are On']", 0);
            createLog("Guest alerts turned On");
            createLog("Turning off guest alerts");
            click("NATIVE", "xpath=//*[@text='Guest']", 0, 1);
            sc.syncElements(10000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Guest']/following-sibling::*[@text='Alerts are On']")) {
                createErrorLog("Guest alerts are still turned on");
            } else createLog("Guest alerts are turned off");
        }
    }

    public static void guestSettingsHomePageValidations() {
        navigateToSharedRemotePage();
        verifyElementFound("NATIVE", "xpath=//*[@id='guest_driver_advance_icon_cta']", 0);
        //click on the guest picture
        click("NATIVE", "xpath=//*[@id='guest_driver_advance_icon_cta']", 0, 1);
        sc.syncElements(5000,10000);
        //verify guest page
        verifyElementFound("NATIVE", "xpath=//*[@id='guest_avatar_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        //No locator for Drive limit icon
        //verifyElementFound("NATIVE", "xpath=//*[@content-desc='driver_settings_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'get notified when your guest driver goes over the driving limits you set.')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dl_save_cta' and @text='Define Driving Limits']", 0);
        //verify driving limits
        sc.click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        String actualText[] = sc.getAllValues("NATIVE","xpath=//*[@class='android.view.View']/child::*[@text]","text");
        List<String> actualTextList = Arrays.asList(actualText);
        createLog(actualTextList.toString());

        String drivingLimitDetailsGuest[] = {"Driving Limits", "Speed", "Area", "Curfew", "Time", "Ignition"};

        for (String detailsName : drivingLimitDetailsGuest) {
            if (actualTextList.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
                createLog("Validation of " + detailsName+" successful");
            } else {
                sc.report("Validation of " + detailsName+" unsuccessful", false);
                createErrorLog("");
            }

        }
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Back']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dl_save_cta']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Back']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Back']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Back']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
    }

    public static void valetSettingsHomePageValidations() {
        navigateToSharedRemotePage();
        //click on the valet picture
        verifyElementFound("NATIVE", "xpath=//*[@id='guest_driver_valet_icon_cta']", 0);
        click("NATIVE", "xpath=//*[@id='guest_driver_valet_icon_cta']", 0, 1);

        //verify valets page
        verifyElementFound("NATIVE", "xpath=//*[@id='guest_avatar_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        //No locator for drive limits icon
        //verifyElementFound("NATIVE", "xpath=//*[@content-desc='driver_settings_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'get notified when your guest driver goes over the driving limits you set.')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dl_save_cta' and @text='Define Driving Limits']", 0);


        //verify driving limits
        click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);

        String actualText[] = sc.getAllValues("NATIVE","xpath=//*[@class='android.view.View']/child::*[@text]","text");
        List<String> actualTextList = Arrays.asList(actualText);
        createLog(actualTextList.toString());

        String drivingLimitDetailsGuest[] = {"Driving Limits", "Speed", "Area", "Curfew", "Time", "Ignition"};

        for (String detailsName : drivingLimitDetailsGuest) {
            if (actualTextList.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
                createLog("Validation of " + detailsName+" successful");
            } else {
                sc.report("Validation of " + detailsName+" unsuccessful", false);
                createErrorLog("Validation of " + detailsName+" unsuccessful");
            }
        }
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Back']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dl_save_cta']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Back']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Valet']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Back']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Back']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
    }
    public static void navigateToSharedRemotePage() {
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Share Remote']")) {
            createLog("Share Remote button not found. Relaunching the app");
            reLaunchApp_android();
            click("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, 1);
            sc.syncElements(5000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
            click("NATIVE", "xpath=//*[@id='guest_driver_valet_icon_cta']", 0, 1);
            sc.syncElements(5000, 10000);
            createLog("Guest Driver screen displayed");
        }
    }

    public static void selectProfile(String profileName) {
        if (profileName.equalsIgnoreCase("guest")) {
            //click on the valet picture
            createLog("Selecting Guest settings page");
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_driver_advance_icon_cta']", 0);
            click("NATIVE", "xpath=//*[@id='guest_driver_advance_icon_cta']", 0, 1);
            sc.syncElements(5000, 10000);
        } else if (profileName.equalsIgnoreCase("valet")) {
            //click on the valet picture
            createLog("Selecting valet settings page");
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_driver_valet_icon_cta']", 0);
            click("NATIVE", "xpath=//*[@id='guest_driver_valet_icon_cta']", 0, 1);
            sc.syncElements(5000, 10000);
        } else {
            createErrorLog("Please provide profile name as Guest or Valet");
        }
    }
    public static void drivingLimitSpeed(String profileName){
        createLog("Started:"+profileName+" Driving Limits Speed");
        navigateToSharedRemotePage();
        selectProfile(profileName);
        //click on driving limits
        click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
        sc.syncElements(5000, 30000);

        //speed validation
        verifyElementFound("NATIVE", "xpath=//*[@text='Speed']", 0);
        click("NATIVE", "xpath=//*[@text='Speed']", 0, 1);
        sc.syncElements(5000, 30000);

        //speed details page
        verifyElementFound("NATIVE", "xpath=//*[@id='speed_title']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='speed_switch']", 0);

        String actualText = "";
        for(int i=1; i<=4; i++) {
            String descValues[] = sc.getAllValues("NATIVE", "xpath=//*[@class='android.view.View']/child::*[@text]", "text");
            for (String value : descValues) {
                actualText = actualText.concat(value);
            }
            sc.swipe("Down", sc.p2cy(60), 2000);
        }

        //TODO Bug OAD01-23372
        //String speedValues[] = {"5 mph", "10 mph", "15 mph", "20 mph", "25 mph", "30 mph", "35 mph", "40 mph",
        //        "45 mph", "50 mph", "55 mph", "60 mph", "65 mph", "70 mph", "75 mph", "80 mph", "85 mph", "90 mph",};
        String speedValues[] = {"5 mi/h", "10 mi/h", "15 mi/h", "20 mi/h", "25 mi/h", "30 mi/h", "35 mi/h", "40 mi/h",
                "45 mi/h", "50 mi/h", "55 mi/h", "60 mi/h", "65 mi/h", "70 mi/h", "75 mi/h", "80 mi/h", "85 mi/h", "90 mi/h",};
        for (String detailsName : speedValues) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
                createLog("Validation of " + detailsName+" successful");
            } else {
                sc.report("Validation of " + detailsName, false);
                createErrorLog("Validation of " + detailsName+" unsuccessful");
            }
        }

        //select speed
        click("NATIVE", "xpath=//*[@text='70 mi/h']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='speed_back_button']", 0);
        click("NATIVE", "xpath=//*[@id='speed_back_button']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='dl_save_cta']", 0);
        click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
        sc.syncElements(60000, 90000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0)){
            selectProfile(profileName);
            click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
            sc.syncElements(5000,10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'70 mi/h') or contains(@text,'70 Miles/hr')]", 0);
        createLog("Ended:"+profileName+"Driving Limits Speed");

    }
    public static void drivingLimitsMiles(String profileName){
        createLog("Started:"+profileName+"Driving Limits Miles");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='dl_save_cta' and @text='Save']")){
            navigateToSharedRemotePage();
            selectProfile(profileName);
            click("NATIVE","xpath=//*[@id='dl_save_cta']",0,1);
            sc.syncElements(5000,10000);
        }
        //miles validation
        verifyElementFound("NATIVE", "xpath=//*[@text='Miles']", 0);
        click("NATIVE", "xpath=//*[@text='Miles']", 0, 1);
        sc.syncElements(15000, 30000);

        //miles details page
        verifyElementFound("NATIVE", "xpath=//*[@text='Max Miles']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Reset Time']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='This will also reset the Max Time']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='miles_time_picker']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='miles_time_am']", 0);
        verifyElementFound("NATIVE","xpath=//*[@id='miles_time_pm']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Max Miles']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='miles_switch']", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='1000 mi' or @id='1000 miles']", 0, 1000, 5, false);
        //select miles
        verifyElementFound("NATIVE", "xpath=//*[@text='5 miles' or @text='5 mi']", 0);
        click("NATIVE", "xpath=//*[@text='5 mi' or @text='5 miles']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='miles_back_button']", 0);
        click("NATIVE", "xpath=//*[@id='miles_back_button']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='dl_save_cta']", 0);
        click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
        sc.syncElements(60000, 90000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0)){
            selectProfile(profileName);
            click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
            sc.syncElements(5000,10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Miles']/following-sibling::*[@text='5 miles' or @text='5 mi']", 0);
        createLog("Ended:"+profileName+"Driving Limits Miles");
    }

    public static void drivingLimitsArea(String profileName) {
        createLog("Started:"+profileName+" Driving Limits Area");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='dl_save_cta' and @text='Save']")){
            navigateToSharedRemotePage();
            selectProfile(profileName);
            click("NATIVE","xpath=//*[@id='dl_save_cta']",0,1);
            sc.syncElements(5000,10000);
        }
        //Area Validation
        verifyElementFound("NATIVE", "xpath=//*[@text='Area']", 0);
        click("NATIVE", "xpath=//*[@text='Area']", 0, 1);
        sc.syncElements(15000, 30000);

        //Area details page
        verifyElementFound("NATIVE","xpath=//*[@id='area_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Search by address or zip code']",0);
        verifyElementFound("NATIVE","xpath=//*[@content-desc='Vehicle Position']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Max Miles']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='area_switch']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Max Miles']",0);

        //select miles
        verifyElementFound("NATIVE", "xpath=//*[@text='10 mi' or @text='10 miles']", 0);
        click("NATIVE", "xpath=//*[@text='10 mi' or @text='10 miles']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='area_back_button']", 0);
        click("NATIVE", "xpath=//*[@id='area_back_button']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='dl_save_cta']", 0);
        click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
        sc.syncElements(60000, 90000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0)){
            selectProfile(profileName);
            click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
            sc.syncElements(5000,10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Area']/following-sibling::*[@text='10 miles' or @text='10 mi']", 0);
        createLog("Ended:"+profileName+"Driving Limits Area");
    }

    public static void drivingLimitsCurfew(String profileName){
        createLog("Started:"+profileName+" Driving Limits Curfew");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='dl_save_cta' and @text='Save']")){
            navigateToSharedRemotePage();
            selectProfile(profileName);
            click("NATIVE","xpath=//*[@id='dl_save_cta']",0,1);
            sc.syncElements(5000,10000);
        }
        //curfew validation
        verifyElementFound("NATIVE", "xpath=//*[@text='Curfew']", 0);
        click("NATIVE", "xpath=//*[@text='Curfew']", 0, 1);
        sc.syncElements(10000, 30000);

        //curfew details page
        verifyElementFound("NATIVE","xpath=//*[@id='curfew_start_time_picker_title']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='curfew_start_time_picker']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='curfew_start_time_am']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='curfew_start_time_pm']", 0);
        verifyElementFound("NATIVE","xpath=//*[@id='curfew_end_time_picker_title']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='curfew_end_time_picker']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='curfew_end_time_am']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='curfew_end_time_pm']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='curfew_switch']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sunday ']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Monday ']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Tuesday ']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Wednesday ']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@text='Sunday ']", 0, "Up");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Thursday ']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Friday ']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Saturday ']", 0);

        //Start Time
        click("NATIVE","xpath=//*[@id='curfew_start_time_picker']",0,1);
        click("NATIVE","xpath=//*[@content-desc='5']",0,1);
        click("NATIVE","xpath=//*[@id='button1']",0,1);

        //End Time
        click("NATIVE","xpath=//*[@id='curfew_end_time_picker']",0,1);
        click("NATIVE","xpath=//*[@content-desc='5']",0,1);
        click("NATIVE","xpath=//*[@content-desc='30']",0,1);
        click("NATIVE","xpath=//*[@id='button1']",0,1);

        //select days
        click("NATIVE", "xpath=//*[@text='Saturday ']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='curfew_back_button']", 0);
        click("NATIVE", "xpath=//*[@id='curfew_back_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Sun, Mon, Tue, Wed, Thu, Fri')]", 0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'5:00') and contains(@text,'5:30')]",0);

        //update value again
        click("NATIVE", "xpath=//*[@text='Curfew']", 0, 1);
        sc.syncElements(5000, 30000);
        sc.flickElement("NATIVE", "xpath=//*[@text='Sunday ']", 0, "Up");
        click("NATIVE", "xpath=//*[@text='Saturday ']", 0, 1);
        click("NATIVE", "xpath=//*[@id='curfew_back_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Everyday')]", 0);
        createLog("Ended:"+profileName+" Driving Limits Curfew");
    }

    public static void drivingLimitsTime(String profileName){
        createLog("Started:"+profileName+"Driving Limits Time");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='dl_save_cta' and @text='Save']")){
            navigateToSharedRemotePage();
            selectProfile(profileName);
            click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
            sc.syncElements(5000, 30000);
        }
        //Time validation
        verifyElementFound("NATIVE", "xpath=//*[@text='Time']", 0);
        click("NATIVE", "xpath=//*[@text='Time']", 0, 1);
        sc.syncElements(5000, 30000);

        //time details page
        verifyElementFound("NATIVE", "xpath=//*[@text='Time']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Reset Time']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='This will also reset the Max Time']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='time_picker']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='time_am']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='time_pm']", 0);
        verifyElementFound("NATIVE","xpath=//*[@text='Max Time']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='time_switch']", 0);

        //select max time
        click("NATIVE", "xpath=//*[@text='1.0 Hr']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='time_back_button']", 0);
        click("NATIVE", "xpath=//*[@id='time_back_button']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='dl_save_cta']", 0);
        click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
        sc.syncElements(60000, 90000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0)){
            selectProfile(profileName);
            click("NATIVE", "xpath=//*[@id='dl_save_cta']", 0, 1);
            sc.syncElements(5000,10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='1.0 Hr']", 0);
        createLog("Ended:"+profileName+"Driving Limits Time");
    }
}
