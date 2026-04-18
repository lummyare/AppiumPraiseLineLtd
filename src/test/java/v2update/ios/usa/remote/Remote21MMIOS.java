package v2update.ios.usa.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static v2update.ios.usa.status.VehicleStatusIOS.statusDoorsUnlockLockValidation;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Remote21MMIOS extends SeeTestKeywords {
    String testName = "Remote21MM-IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")){
            case(""):
                testName = System.getProperty("cloudApp") + testName;
                iOS_Setup2_5(testName);
                environmentSelection_iOS("prod");
                sc.startStepsGroup("email SignIn 21MM");
                selectionOfCountry_IOS("USA");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21MM"), ConfigSingleton.configMap.get("strPassword21MM"));
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                iOS_Setup2_5(testName);
                environmentSelection_iOS("prod");
                sc.startStepsGroup("email SignIn 21MM");
                selectionOfCountry_IOS("USA");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21MM"), ConfigSingleton.configMap.get("strPassword21MM"));
                sc.stopStepsGroup();
        }
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    //21mm Remote tests
    /*Execute Remote commands (start,stop,lock,unlock...)
        Verify for In-App Notification for success
        If the vehicle notification is not successful
            Verify that the vehicle is not active || Vehicle is in Use || Vehicle has not been started for more than 10 min twice
        Verify Push Notification*/

    @Test
    @Order(1)
    public void RemoteUnLock(){
        sc.startStepsGroup("21mm remote Unlock Test");
        clearPNS();
        executeRemoteUnLock();
        PNS("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void RemoteLock(){
        sc.startStepsGroup("21mm remote Lock Test");
        executeRemoteLock();
        PNS("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void RemoteStart(){
        sc.startStepsGroup("21mm remote Start Test");
        executeRemoteStart();
        PNS("Start");
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void RemoteStop(){
        sc.startStepsGroup("21mm remote Stop Test");
        executeRemoteStop();
        PNS("Stop");
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void RemoteLights(){
        sc.startStepsGroup("Test - 21MM remote Lights");
        executeRemoteLights();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void RemoteHazard(){
        sc.startStepsGroup("Test - 21MM remote Hazard");
        executeRemoteHazard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void RemoteClimateSettingsOn(){
        /* Execute Remote Climate
        verify that the vehicle Climate settings for Heated Seat/Vent/Max Min Slider/Defrost can be turned On and saved
        Verify that the settings are saved and reflect on next app launch or next page open
         */
        sc.startStepsGroup("21mm remote Climate Settings On Test");
        executeRemoteClimateSettingsOn();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void RemoteClimateSettingsOFF(){
        /* Execute Remote Climate
        veify that the vehicle Climate settings for Heated Seat/Vent/Max Min Slider/Defrost can be turned Off and saved
        Verify that the settings are saved and reflect on next app launch or next page open
         */
        sc.startStepsGroup("21mm remote Climate Settings Off Test");
        executeRemoteClimateSettingsOff();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void RemoteBuzzer(){
        sc.startStepsGroup("Test - 21MM remote Buzzer");
        executeRemoteBuzzer();
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    public void RemoteTrunkUnLock(){
        sc.startStepsGroup("Test - 21MM remote Trunk Unlock");
        executeRemoteTrunkUnlock();
        PNS("Trunk Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    public void RemoteTrunkLock(){
        sc.startStepsGroup("Test - 21MM remote Trunk Lock ");
        executeRemoteTrunkLock();
        PNS("Trunk Lock");
        sc.stopStepsGroup();
    }

    //Remote Horn not available for Highlander vehicle
    @Test
    @Order(12)
    @Disabled
    public void RemoteHorn(){
        sc.startStepsGroup("Test - 21MM remote Horn");
        executeRemoteHorn();
        PNS("Horn");
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    public void statusScreenDoors(){
        sc.startStepsGroup("Test - Status Screen Doors Unlock Lock Validation");
        statusDoorsUnlockLockValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(14)
    public void SignOut() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void executeRemoteUnLock() {
        createLog("Started - 21MM Remote Unlock");
        iOS_remoteUnLock();
        //verify remote unlock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now unlocked","already unlocked","Unlock");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Unlock");
    }

    public static void executeRemoteLock() {
        createLog("Started - 21MM Remote Lock");
        iOS_remoteLock();
        //verify remote lock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now locked","already locked", "Your request could not be completed because a keyfob was detected in your vehicle", "Lock");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Lock");
    }

    public static void executeRemoteStart() {
        createLog("Started - 21MM Remote Start");
        iOS_remoteStart();
        //verify remote start notification is displayed
        remoteCommandNotificationVerification("Vehicle was started and will automatically shut off in 10 minutes", "minute cycles without an occupant","keyfob was not left inside your vehicle","Start");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Start");
    }

    public static void executeRemoteStop() {
        createLog("Started - 21MM Remote Stop");
        iOS_remoteStop();
        //verify remote stop notification is displayed
        remoteCommandNotificationVerification("Remote Engine Stop Request was successful","Stop");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Stop");
    }

    public static void executeRemoteLights() {
        createLog("Started - 21MM Remote Lights");
        iOS_remoteLights();
        //verify remote lights notification is displayed
        remoteCommandNotificationVerification("The Remote head lamp command was successful and will turn off automatically","","Light");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Lights");
    }

    public static void executeRemoteHazard() {
        createLog("Started - 21MM Remote Hazard");
        iOS_remoteHazard();
        //verify remote hazard notification is displayed
        remoteCommandNotificationVerification("The Remote hazard flash command was successful and will turn off automatically","Hazard");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Hazard");
    }

    public static void executeRemoteBuzzer() {
        createLog("Started - 21MM Remote Buzzer");
        iOS_remoteBuzzer();
        //verify remote Buzzer notification is displayed
        remoteCommandNotificationVerification("The remote buzzer request was successful","Buzzer");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Buzzer");
    }

    public static void executeRemoteTrunkUnlock() {
        createLog("Started - 21MM Remote Trunk Unlock");
        iOS_remoteTrunkUnlock();
        //verify remote Trunk Unlock notification is displayed
        remoteCommandNotificationVerification("The remote trunk/hatch unlock was successful","Trunk/Hatch Unlock");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Trunk Unlock");
    }

    public static void executeRemoteTrunkLock() {
        createLog("Started - 21MM Remote Trunk Lock");
        iOS_remoteTrunkLock();
        //verify remote Trunk Lock notification is displayed
        remoteCommandNotificationVerification("The remote trunk/hatch lock was successful","Your request could not be completed because a keyfob was detected in your vehicle","Trunk Lock");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Trunk Lock");
    }

    public static void executeRemoteHorn() {
        createLog("Started - 21MM Remote Horn");
        iOS_remoteHorn();
        //verify remote Horn notification is displayed
        remoteCommandNotificationVerification("The remote horn request was successful","Horn");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - 21MM Remote Horn");
    }

    public static void executeRemoteClimateSettingsOn() {
        createLog("Started - 21MM Remote Climate Settings On");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            iOS_LaunchApp();
        sc.syncElements(2000, 10000);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        }
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_climate_button']", 0);
        click("NATIVE", "xpath=//*[@id='remote_climate_button']", 0, 1);
        sc.syncElements(15000, 60000);

        verifyElementFound("NATIVE","xpath=//*[@label='Climate']",0);
        /*
         Verify the custom settings are saved
         */

        verifyElementFound("NATIVE","xpath=//*[@label='Custom Climate Settings']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='When these settings are enabled, changes will take effect the next time you remote start your car.']",0);
        String settingsValue = sc.elementGetProperty("NATIVE","xpath=//*[@accessibilityLabel='climate_custom_climate_switch']",0,"value");
        createLog("Custom settings value is: "+settingsValue);
        if (settingsValue.equalsIgnoreCase("0"))
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='climate_custom_climate_switch' and @value='0']", 0, 1);
        String settingsValueTurnedOn = sc.elementGetProperty("NATIVE","xpath=//*[@accessibilityLabel='climate_custom_climate_switch']",0,"value");
        createLog("Custom settings value is: "+settingsValueTurnedOn);
        if (settingsValueTurnedOn.equalsIgnoreCase("1"))
            sc.report("Custom climate settings Toggle button is found and clicked", true);

        //cool and head icons
        verifyElementFound("NATIVE", "xpath=//*[@label='cool']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='heat']", 0);
        //Temperature Bar is displayed and editable
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_temperature_slider']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_temperature_value_text']")) {
            String temperature = sc.elementGetProperty("NATIVE",
                    "xpath=//*[@accessibilityLabel='climate_temperature_value_text']", 0, "value");
            createLog("Temperature value is :" + temperature);
            sc.report("Temperature set to :" + temperature, true);
        }
        else {
            createErrorLog("Temperature element is not Found");
            sc.report("Temperature element is not Found", false);
        }
        /*
        setting Heated Seat to ON
         */
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='heat' and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[@text='heat' and @accessibilityLabel='climate_seat_front_right_icon']")) {
            createLog("Driver and Passenger seat heat are already ON");
            sc.report("Driver and Passenger seat heat are already ON", true);
        }
        //both Driver and Passenger seat heat turned on
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='off' or @text='cool') and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[(@text='off' or @text='cool') and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='off' or @text='cool') and @accessibilityLabel='climate_seat_front_left_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_heat_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='heat' and @accessibilityLabel='climate_seat_front_left_icon']", 0);
            createLog("Driver Seat Heat Turned ON");
            sc.report("Driver Seat Turned ON", true);
            click("NATIVE", "xpath=//*[(@text='off' or @text='cool') and @accessibilityLabel='climate_seat_front_right_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_heat_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='heat' and @accessibilityLabel='climate_seat_front_right_icon']", 0);
            createLog("Passenger Seat Heat Turned ON");
            sc.report("Passenger Seat Heat Turned ON", true);
        }
        //Only Driver seat heat turned on
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='heat' and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[(@text='off' or @text='cool') and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='off' or @text='cool') and @accessibilityLabel='climate_seat_front_right_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_heat_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='heat' and @accessibilityLabel='climate_seat_front_right_icon']", 0);
            createLog("Passenger Seat Heat Turned ON");
        }

        //Only Passenger seat heat turned on
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='off' or @text='cool') and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[@text='heat' and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='off' or @text='cool') and @accessibilityLabel='climate_seat_front_left_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_heat_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='heat' and @accessibilityLabel='climate_seat_front_left_icon']", 0);
            createLog("Driver Seat Heat Turned ON");
        }

        /*
        SETTING COOL seat to ON
         */
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='cool' and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[@text='cool' and @accessibilityLabel='climate_seat_front_right_icon']")) {
            createLog("Driver and Passenger seat cool are already ON");
            sc.report("Driver and Passenger seat cool are already ON", true);
        }
        //both Driver and Passenger seat cool turned on
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='off' or @text='heat') and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[(@text='off' or @text='cool') and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='off' or @text='heat') and @accessibilityLabel='climate_seat_front_left_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_cool_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='cool' and @accessibilityLabel='climate_seat_front_left_icon']", 0);
            createLog("Driver Seat cool Turned ON");
            sc.report("Driver Seat cool Turned ON", true);
            click("NATIVE", "xpath=//*[(@text='off' or @text='heat') and @accessibilityLabel='climate_seat_front_right_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_cool_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='cool' and @accessibilityLabel='climate_seat_front_right_icon']", 0);
            createLog("Passenger Seat cool Turned ON");
            sc.report("Passenger Seat cool Turned ON", true);
        }
        //Only Driver seat cool turned on
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='cool' and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[(@text='off' or @text='heat') and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='off' or @text='heat') and @accessibilityLabel='climate_seat_front_right_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_cool_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='cool' and @accessibilityLabel='climate_seat_front_right_icon']", 0);
            createLog("Passenger Seat cool Turned ON");
        }

        //Only Passenger seat cool turned on
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='off' or @text='heat') and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[@text='cool' and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='off' or @text='heat') and @accessibilityLabel='climate_seat_front_left_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_cool_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='cool' and @accessibilityLabel='climate_seat_front_left_icon']", 0);
            createLog("Driver Seat Heat Turned ON");
        }

        /*
        Defrost Settings
         */

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='climate_save_button_cta' and @label='Save Settings' and @visible='true']", 0, 3000, 2, false);
        verifyElementFound("NATIVE", "xpath=//*[@label='Defrost']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_defrost_front_text' and @label='Front']", 0);

        String defrostFrontToggleVal = sc.elementGetProperty("NATIVE","xpath=(//*[@accessibilityLabel='climate_defrost_toggle'])[1]",0,"value");
        createLog("Front defrost toggle value is: "+defrostFrontToggleVal);
        if (defrostFrontToggleVal.equalsIgnoreCase("0")) {
            createLog("Front defrost Already OFF");
            sc.click("NATIVE", "xpath=(//*[@accessibilityLabel='climate_defrost_toggle'])[1]", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[@accessibilityLabel='climate_defrost_toggle' and @value='1'])[1]")) {
                createLog("Front defrost Turned ON");
                sc.report("Front defrost Turned ON", true);
            }
        } else if (defrostFrontToggleVal.equalsIgnoreCase("1")) {
            createLog("Front defrost Already ON");
            sc.report("Front defrost Already On", true);
        }

        String defrostBackToggleVal = sc.elementGetProperty("NATIVE","xpath=(//*[@accessibilityLabel='climate_defrost_toggle'])[2]",0,"value");
        createLog("Back defrost toggle value is: "+defrostBackToggleVal);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_defrost_rear_text' and @label='Back']", 0);
        if (defrostBackToggleVal.equalsIgnoreCase("0")) {
            createLog("Back defrost Already OFF");
            sc.click("NATIVE", "xpath=(//*[@accessibilityLabel='climate_defrost_toggle'])[2]", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[@accessibilityLabel='climate_defrost_toggle' and @value='1'])[2]")) {
                createLog("Back defrost Turned ON");
                sc.report("Back defrost Turned ON", true);
            }
        } else if (defrostBackToggleVal.equalsIgnoreCase("1")) {
            createLog("Back defrost Already ON");
            sc.report("Back defrost Already On", true);
        }
        /*
        save Climate Settings
         */
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_save_button_cta' and @label='Save Settings' and @visible='true']"))
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='climate_save_button_cta' and @label='Save Settings' and @visible='true']", 0, 1);
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_save_button_cta' and @label='Save Settings' and @visible='true']"))
            sc.swipe("up", sc.p2cy(30), 100);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='remote_climate_button']"))
            sc.report("Climate settings saved successfully", true);
        sc.syncElements(5000, 10000);

        createLog("Completed - 21MM Remote Climate Settings On");
    }

    public static void executeRemoteClimateSettingsOff() {
        createLog("Started - 21MM Remote Climate Settings OFF");
        sc.syncElements(2000, 10000);
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        sc.syncElements(2000, 10000);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        }
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_climate_button']", 0);
        click("NATIVE", "xpath=//*[@id='remote_climate_button']", 0, 1);
        sc.syncElements(15000, 60000);

        verifyElementFound("NATIVE","xpath=//*[@label='Climate']",0);
        /*
         Verify that all custom settings are toggled off
         */

        verifyElementFound("NATIVE","xpath=//*[@label='Custom Climate Settings']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='When these settings are enabled, changes will take effect the next time you remote start your car.']",0);
        String settingsValue = sc.elementGetProperty("NATIVE","xpath=//*[@accessibilityLabel='climate_custom_climate_switch']",0,"value");
        createLog("Custom settings value is: "+settingsValue);
        if (settingsValue.equalsIgnoreCase("1"))
            sc.report("Custom Climate settings Turn Off in progress", true);

        /*
        SETTING HEAT seat to OFF
         */
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_right_icon']")) {
            createLog("Driver and Passenger seat heat are already OFF");
            sc.report("Driver and Passenger seat heat are already OFF", true);
        }
        //Only Driver seat heat turned OFF
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[(@text='heat' or @text='cool') and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='heat' or @text='cool') and @accessibilityLabel='climate_seat_front_right_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_off_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_right_icon']", 0);
            createLog("Passenger Seat Heat Turned OFF");
        }

        //Only Passenger seat heat turned OFF
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='heat' or @text='cool') and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='heat' or @text='cool') and @accessibilityLabel='climate_seat_front_left_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_off_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_left_icon']", 0);
            createLog("Driver Seat Heat Turned OFF");
        }

        /*
        SETTING COOL seat to OFF
         */
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_right_icon']")) {
            createLog("Driver and Passenger seat cool are already OFF");
            sc.report("Driver and Passenger seat cool are already OFF", true);
        }
        //Only Driver seat cool turned OFF
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[(@text='cool' or @text='heat') and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='cool' or @text='heat') and @accessibilityLabel='climate_seat_front_right_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_off_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_right_icon']", 0);
            createLog("Passenger Seat cool Turned OFF");
        }

        //Only Passenger seat cool turned OFF
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='cool' or @text='heat') and @accessibilityLabel='climate_seat_front_left_icon']") && sc.isElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_right_icon']")) {
            click("NATIVE", "xpath=//*[(@text='cool' or @text='heat') and @accessibilityLabel='climate_seat_front_left_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_off_text']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='off' and @accessibilityLabel='climate_seat_front_left_icon']", 0);
            createLog("Driver Seat Heat Turned OFF");
        }


        /*
        defrost toggle element turning off if found ON
         */
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='climate_save_button_cta' and @label='Save Settings' and @visible='true']", 0, 3000, 2, false);
        verifyElementFound("NATIVE", "xpath=//*[@label='Defrost']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_defrost_front_text' and @label='Front']", 0);
        String defrostFrontToggleVal = sc.elementGetProperty("NATIVE","xpath=(//*[@accessibilityLabel='climate_defrost_toggle'])[1]",0,"value");
        createLog("Front defrost toggle value is: "+defrostFrontToggleVal);
        if (defrostFrontToggleVal.equalsIgnoreCase("1")) {
            createLog("Front defrost ON");
            sc.click("NATIVE", "xpath=(//*[@accessibilityLabel='climate_defrost_toggle'])[1]", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[@accessibilityLabel='climate_defrost_toggle' and @value='0'])[1]")) {
                createLog("Front defrost Turned OFF");
                sc.report("Front defrost Turned OFF", true);
            }
        } else if (defrostFrontToggleVal.equalsIgnoreCase("0")) {
            createLog("Front defrost Already OFF");
            sc.report("Front defrost Already OFF", true);
        }

        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_defrost_rear_text' and @label='Back']", 0);
        String defrostBackToggleVal = sc.elementGetProperty("NATIVE","xpath=(//*[@accessibilityLabel='climate_defrost_toggle'])[2]",0,"value");
        createLog("Back defrost toggle value is: "+defrostBackToggleVal);
        if (defrostBackToggleVal.equalsIgnoreCase("1")) {
            createLog("Back defrost ON");
            sc.click("NATIVE", "xpath=(//*[@accessibilityLabel='climate_defrost_toggle'])[2]", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[@accessibilityLabel='climate_defrost_toggle' and @value='0'])[2]")) {
                createLog("Back defrost Turned OFF");
                sc.report("Back defrost Turned OFF", true);
            }
        } else if (defrostBackToggleVal.equalsIgnoreCase("0")) {
            createLog("Back defrost Already OFF");
            sc.report("Back defrost Already OFF", true);
        }

        /*
        Save Settings with All Off
         */
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='climate_save_button_cta' and @label='Save Settings' and @visible='true']", 0, 3000, 2, false);
        //sc.swipe("Down",sc.p2cy(70), 2000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_save_button_cta' and @label='Save Settings' and @visible='true']"))
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='climate_save_button_cta' and @label='Save Settings' and @visible='true']", 0, 1);
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_save_button_cta' and @label='Save Settings' and @visible='true']"))
            sc.swipe("up", sc.p2cy(30), 100);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='remote_climate_button']"))
            sc.report("Climate settings saved successfully", true);
        sc.syncElements(5000, 10000);
        createLog("Completed - 21MM Remote Climate Settings OFF");
        //pull down advanced remote section if its displayed
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']"))
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
        sc.syncElements(3000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='IconBackArrow']"))
            sc.click("NATIVE", "xpath=//*[@label='IconBackArrow']", 0, 1);
        sc.syncElements(3000, 15000);
        verifyElementFound("NATIVE", "xpath=//*[@label='person']", 0);
    }
}