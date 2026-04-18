package v2update.subaru.android.usa.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruRemote21MMEVAndroid extends SeeTestKeywords {
    String testName = "Remote-21MMEV Android";

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
    public void RemoteUnLock() {
        sc.startStepsGroup("Test - 21MM remote Unlock");
        clearPNS_android();
        executeRemoteUnLock();
        android_PNS("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void RemoteLock() {
        sc.startStepsGroup("Test - 21MM remote Lock");
        executeRemoteLock();
        android_PNS("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void RemoteStart() {
        sc.startStepsGroup("Test - 21MM remote Start");
        executeRemoteStart();
        android_PNS("Start");
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    @Disabled
    public void RemoteStop() {
        sc.startStepsGroup("Test - 21MM remote Stop");
        executeRemoteStop();
        android_PNS("Stop");
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void RemoteLight() {
        sc.startStepsGroup("Test - 21MM remote Light");
        executeRemoteLight();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void RemoteHazard() {
        sc.startStepsGroup("Test - 21MM remote Hazard");
        executeRemoteHazards();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void RemoteClimateSettingsOn() {
        /*
        Execute Remote Climate
        verify that the vehicle Climate settings for Heated Seat/Vent/Max Min Slider/Defrost can be turned On and saved
        Verify that the settings are saved and reflect on next app launch or next page open
         */
        sc.startStepsGroup("Test - 21MM remote Climate Settings On");
        executeRemoteClimateSettingsToggleON();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void RemoteClimateSettingsOFF() {
        /*
        Execute Remote Climate
        verify that the vehicle Climate settings for Heated Seat/Vent/Max Min Slider/Defrost can be turned Off and saved
        Verify that the settings are saved and reflect on next app launch or next page open
         */
        sc.startStepsGroup("Test - 21MM remote Climate Settings Off");
        executeRemoteClimateSettingsToggleOff();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void RemoteBuzzer() {
        sc.startStepsGroup("Test - 21MM remote Buzzer");
        executeRemoteBuzzer();
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    public void RemoteTrunkUnLock() {
        sc.startStepsGroup("Test - 21MM remote Trunk Unlock");
        executeRemoteTrunkUnlock();
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    public void RemoteTrunkLock() {
        sc.startStepsGroup("Test - 21MM remote Trunk Lock ");
        executeRemoteTrunkLock();
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    public void RemoteHorn() {
        sc.startStepsGroup("Test - 21MM remote Horn");
        executeRemoteHorn();
        android_PNS("Horn");
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    //Respective Tests methods
    public static void executeRemoteUnLock() {
        createLog("Started - 21MM Remote Unlock");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
            reLaunchApp_android();
        verifyElementFound("NATIVE", "xpath=//*[@text='Unlock']", 0);
        sc.longClick("NATIVE", "xpath=//*[@contentDescription='Unlock']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Unlocking");

        android_goToNotificationsScreen();
        remoteUnlockNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Unlock");
    }

    public static void executeRemoteLock() {
        createLog("Started - 21MM Remote Lock");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
            reLaunchApp_android();
        verifyElementFound("NATIVE", "xpath=//*[@text='Lock']", 0);
        sc.longClick("NATIVE", "xpath=//*[@contentDescription='Lock']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Locking");

        android_goToNotificationsScreen();
        remoteLockNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Lock");
    }

    public static void executeRemoteStart() {
        createLog("Started - 21MM Remote Start");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
            reLaunchApp_android();
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Start']", 0);
        sc.longClick("NATIVE", "xpath=//*[@text='Start']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Starting");

        android_goToNotificationsScreen();
        remoteStartNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Start");
    }

    public static void executeRemoteStop() {
        createLog("Started - 21MM Remote Stop");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
            reLaunchApp_android();
        verifyElementFound("NATIVE", "xpath=//*[@text='Stop']", 0);
        sc.longClick("NATIVE", "xpath=//*[@text='Stop']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Stopping");

        android_goToNotificationsScreen();
        remoteStopNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Stop");
    }

    public static void executeRemoteLight() {
        createLog("Started - 21MM Remote Light");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
            reLaunchApp_android();
        click("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Light']", 0);
        sc.longClick("NATIVE", "xpath=//*[@contentDescription='Light']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Lights on");

        android_goToNotificationsScreen();
        remoteLightsNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Light");
    }

    public static void executeRemoteHazards() {
        createLog("Started - 21MM Remote HazardS");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
            reLaunchApp_android();
        click("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Hazards']", 0);
        sc.longClick("NATIVE", "xpath=//*[@contentDescription='Hazards']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Hazards on");
        android_goToNotificationsScreen();
        remoteHazardNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Hazard");
    }

    public static void executeRemoteBuzzer() {
        createLog("Started - 21MM Remote Buzzer");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
            reLaunchApp_android();
        click("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Buzzer']", 0);
        sc.longClick("NATIVE", "xpath=//*[@contentDescription='Buzzer']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Buzzer on");

        android_goToNotificationsScreen();
        remoteBuzzerNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Buzzer");
    }

    public static void executeRemoteHorn() {
        createLog("Started - 21MM Remote Horn");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
            reLaunchApp_android();
        click("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Horn']", 0);
        sc.longClick("NATIVE", "xpath=//*[@contentDescription='Horn']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Horn on");

        android_goToNotificationsScreen();
        remoteHornNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Horn");
    }

    public static void executeRemoteTrunkUnlock() {
        createLog("Started - 21MM Remote Trunk Unlock");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Unlock Trunk']")) {
            click("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, 1);
            sc.syncElements(5000, 10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Unlock Trunk']", 0);
        sc.longClick("NATIVE", "xpath=//*[@contentDescription='Unlock Trunk']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Trunk Unlocking");

        android_goToNotificationsScreen();
        remoteTrunkUnlockNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Trunk Unlock");
    }

    public static void executeRemoteTrunkLock() {
        createLog("Started - 21MM Remote Trunk Lock");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
            reLaunchApp_android();
        click("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, 1);
        sc.syncElements(5000, 10000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Lock Trunk']", 0);
        sc.longClick("NATIVE", "xpath=//*[@contentDescription='Lock Trunk']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Trunk Locking");

        android_goToNotificationsScreen();
        remoteTrunkLockNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 21MM Remote Trunk Lock");
    }

    public static void remoteUnlockNotificationDisplayed() {
        createLog("Verifying 21MM Remote Unlock notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") | firstNotificationDate.contains("minutes ago");
        if (firstNotification.contains("multiple doors unlocked") && dateCondition) {
            sc.report("Remote UnLock Notification Displayed", true);
            createLog("Remote UnLock Notification Displayed");
        } else if (firstNotification.contains("Doors are already unlocked") && dateCondition) {
            sc.report("Remote UnLock Notification Displayed", true);
            createLog("Remote UnLock Notification Displayed");
        } else if (firstNotification.contains("The vehicle is now unlocked") && dateCondition) {
            sc.report("Remote UnLock Notification Displayed", true);
            createLog("Remote UnLock Notification Displayed");
        } else {
            sc.report("Remote UnLock Notification not found.", false);
            createErrorLog("Remote UnLock Notification not found");
        }
        createLog("Verified 21MM Remote Unlock notification is displayed");
    }

    public static void remoteLockNotificationDisplayed() {
        createLog("Verifying 21MM Remote Lock notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") | firstNotificationDate.contains("minutes ago");
        if (firstNotification.contains("All your doors are already locked") && dateCondition) {
            sc.report("Remote Lock Notification Displayed", true);
            createLog("Remote Lock Notification Displayed");
        } else if (firstNotification.contains("The vehicle is now locked") && dateCondition) {
            sc.report("Remote Lock Notification Displayed", true);
            createLog("Remote Lock Notification Displayed");
        }else if (firstNotification.contains("keyfob was detected in your vehicle") && dateCondition) {
            sc.report("Remote Lock Notification Displayed", true);
            createLog("Remote Lock Notification Displayed");
        } else {
            sc.report("Remote Lock Notification not found", false);
            createErrorLog("Remote Lock Notification not found");
        }
        createLog("Verified 21MM Remote Lock notification is displayed");
    }

    public static void remoteStartNotificationDisplayed() {
        createLog("Verifying 21MM Remote Start notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        String secondNotification = sc.elementGetText("NATIVE", "xpath=(//*[@id='nh_title'])[2]", 0);
        String secondNotificationDate = sc.elementGetText("NATIVE", "xpath=(//*[@id='nh_date'])[2]", 0);
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") | firstNotificationDate.contains("minutes ago");
        boolean dateConditionSecond = secondNotificationDate.contains("second ago") | secondNotificationDate.contains("minute ago") | firstNotificationDate.contains("minutes ago");
        if (firstNotification.contains("Vehicle was started and will automatically shut off in 20 minutes") && dateCondition) {
            sc.report("Remote Start Notification Displayed", true);
            createLog("Remote Start Notification Displayed");
        } else if (secondNotification.contains("Vehicle was started and will automatically shut off in 20 minutes") && dateConditionSecond) {
            sc.report("Remote Start Notification Displayed", true);
            createLog("Remote Start Notification Displayed");
        }else if (firstNotification.contains("keyfob was not left inside your vehicle") && dateCondition) {
            sc.report("Remote Start Notification Displayed", true);
            createLog("Remote Start Notification Displayed");
        } else if (firstNotification.contains("minute cycles without an occupant") && dateCondition) {
            sc.report("Remote Start Notification Displayed", true);
            createLog("Remote Start Notification Displayed");
        } else {
            sc.report("Remote Start Notification not found", false);
            createErrorLog("Remote Start Notification not found");
        }
        createLog("Verified 21MM Remote Start notification is displayed");
    }

    public static void remoteStopNotificationDisplayed() {
        createLog("Verifying 21MM Remote Stop notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") | firstNotificationDate.contains("minutes ago");
        if (firstNotification.contains("Engine Stop Request was successful") && dateCondition) {
            sc.report("Remote Stop Notification Displayed", true);
            createLog("Remote Stop Notification Displayed");
        }else if (firstNotification.contains("keyfob was detected in your vehicle") && dateCondition) {
            sc.report("Remote Lock Notification Displayed", true);
            createLog("Remote Lock Notification Displayed");
        } else {
            sc.report("Remote Stop Notification not found", false);
            createErrorLog("Remote Stop Notification not found");
        }
        createLog("Verified 21MM Remote Stop notification is displayed");
    }

    public static void remoteLightsNotificationDisplayed() {
        createLog("Verifying 21MM Remote Lights notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") | firstNotificationDate.contains("minutes ago");
        if (firstNotification.contains("The Remote head lamp command was successful and will turn off automatically") && dateCondition) {
            sc.report("Remote Lights Successful", true);
            createLog("Remote Lights Successful");
        } else {
            sc.report("Remote Lights notification successful", false);
            createErrorLog("Remote Lights notification not found");
        }
        createLog("Verified 21MM Remote Lights notification is displayed");
    }

    public static void remoteHazardNotificationDisplayed() {
        createLog("Verifying 21MM Remote Hazard notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago");
        if (firstNotification.contains("Vehicle hazard lights have been turned on and will turn off in 60 seconds") && dateCondition) {
            sc.report("Remote Hazards Notification Displayed", true);
            createLog("Remote Hazards Notification Displayed");
        } else if (firstNotification.contains("Vehicle hazard lights are already off") && dateCondition) {
            sc.report("Remote Hazards Notification Displayed", true);
            createLog("Remote Hazards Notification Displayed");
        } else if (firstNotification.contains("Vehicle hazard lights are already on") && dateCondition) {
            sc.report("Remote Hazards Notification Displayed", true);
            createLog("Remote Hazards Notification Displayed");
        } else if (firstNotification.contains("This request cannot be completed while vehicle is being driven. Please park your vehicle and try again") && dateCondition) {
            sc.report("Remote Hazards Notification Displayed", true);
            createLog("Remote Hazards Notification Displayed");
        } else if (firstNotification.contains("The Remote hazard flash command was successful and will turn off automatically") && dateCondition) {
            sc.report("Remote Hazards Notification Displayed", true);
            createLog("Remote Hazards Notification Displayed");
        } else {
            sc.report("Remote Hazards Notification not found", false);
            createErrorLog("Remote Hazards Notification not found");
        }
        createLog("Verified 21MM Remote Hazard notification is displayed");
    }

    public static void remoteBuzzerNotificationDisplayed() {
        createLog("Verifying 21MM Remote Buzzer notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") | firstNotificationDate.contains("minutes ago");
        if (firstNotification.contains("The remote buzzer request was successful.") && dateCondition) {
            sc.report("Remote Buzzer Successful", true);
            createLog("Remote Buzzer Successful");
        } else {
            sc.report("Remote Buzzer notification successful", false);
            createErrorLog("Remote Buzzer notification not found");
        }
        createLog("Verified 21MM Remote Buzzer notification is displayed");
    }

    public static void remoteHornNotificationDisplayed() {
        createLog("Verifying 21MM Remote Horn notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago");
        if (firstNotification.contains("horn request was successful") && dateCondition) {
            sc.report("Remote Horn Notification Displayed", true);
            createLog("Remote Horn Notification Displayed");
        } else {
            sc.report("Remote Horn Notification not found", false);
            createErrorLog("Remote Horn Notification not found");
        }
        createLog("Verified 21MM Remote Horn notification is displayed");
    }

    public static void remoteTrunkUnlockNotificationDisplayed() {
        createLog("Verifying 21MM Remote Trunk Unlock notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        boolean dateCondition = firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago");
        if (firstNotification.contains("The remote trunk/hatch unlock was successful") && dateCondition) {
            sc.report("Remote Trunk UnLock Notification Displayed", true);
            createLog("Remote Trunk UnLock Notification Displayed");
        }else if (firstNotification.contains("keyfob was detected in your vehicle") && dateCondition) {
            sc.report("Remote Lock Notification Displayed", true);
            createLog("Remote Lock Notification Displayed");
        } else {
            sc.report("Remote Trunk UnLock Notification not found", false);
            createErrorLog("Remote Trunk UnLock Notification not found");
        }
        createLog("Verified 21MM Remote Trunk Unlock notification is displayed");
    }

    public static void remoteTrunkLockNotificationDisplayed() {
        createLog("Verifying 21MM Remote Trunk Lock notification is displayed");
        sc.waitForElement("NATIVE", "xpath=//*[@id='nh_title'][1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_title'][1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=//*[@id='nh_date'][1]", 0);
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago");
        if (firstNotification.contains("The remote trunk/hatch lock was successful") && dateCondition) {
            sc.report("Remote Trunk Lock Notification Displayed", true);
            createLog("Remote Trunk Lock Notification Displayed");
        }else if (firstNotification.contains("keyfob was detected in your vehicle") && dateCondition) {
            sc.report("Remote Lock Notification Displayed", true);
            createLog("Remote Lock Notification Displayed");
        } else {
            sc.report("Remote Trunk Lock  Notification not found", false);
            createErrorLog("Remote Trunk Lock  Notification not found");
        }
        createLog("Verified 21MM Remote Trunk Lock notification is displayed");
    }

    public static void executeRemoteClimateSettingsToggleON() {
        sc.syncElements(5000, 10000);
        createLog("Started - 21MM Remote Climate Settings Toggle ON");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Climate']")) {
            click("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, 1);
            sc.syncElements(5000, 10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Climate']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Climate']", 0, 1);
        sc.syncElements(25000, 50000);
        /*
         Verify the custom settings are saved
         */
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_custom_climate_switch' and @checked='false']"))
            click("NATIVE", "xpath=//*[@id='climate_custom_climate_switch' and @checked='false']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_custom_climate_switch' and @checked='true']"))
            sc.report("Custom climate settings Toggle button is found and clicked", true);
        /*
        temp Bar is displayed and editable
         */
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Ac']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@class='android.widget.SeekBar']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Defrost']", 0);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_custom_climate_switch' and @checked='true']")) {
            String text = sc.elementGetProperty("NATIVE", "xpath=//*[@id='climate_temperature_value_text']", 0, "text");
            sc.report("Temperature set to " + text, true);
            createLog("Temperature set to " + text);
        } else {
            sc.report("Temp is not found ", false);
            createErrorLog("Temp is not found ");
        }

        /*
        setting Heated Seat to ON
         */
        //Driver Side
        if (sc.isElementFound("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]/child::*[@id='climate_seat_off_icon']")) {
            createLog("Driver Side Seat is off");
            click("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]", 0, 1);
            click("NATIVE","xpath=//*[./*[@id='climate_seat_heat_icon']]",0,1);
            sc.report("Driver Seat Turned ON", true);
        } else if (sc.isElementFound("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]/child::*[@id='climate_seat_vent_icon']")) {
            createLog("Driver Seat set to Vent");
            click("NATIVE","xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]",0,1);
            click("NATIVE","xpath=//*[./*[@id='climate_seat_heat_icon']]",0,1);
            sc.report("Driver Seat Turned ON", true);
        }
        else {
            if (!sc.isElementFound("NATIVE","xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]/child::*[@id='climate_seat_heat_icon']"))
                createErrorLog("Driver Seat status unknown");
            sc.report("Driver Seat Heat is Already ON", true);
        }
        //Passenger Side
        if (sc.isElementFound("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]/child::*[@id='climate_seat_off_icon']")) {
            createLog("Passenger Seat is Off");
            click("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]", 0, 1);
            click("NATIVE","xpath=//*[./*[@id='climate_seat_heat_icon']]",0,1);
            sc.report("Passenger side Heat seat ON", true);
        } else if (sc.isElementFound("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]/child::*[@id='climate_seat_vent_icon']")) {
            createLog("Passenger Seat set to Vent");
            click("NATIVE","xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]",0,1);
            click("NATIVE","xpath=//*[./*[@id='climate_seat_heat_icon']]",0,1);
            sc.report("Passenger Seat Heat ON", true);
        }
        else {
            if (!sc.isElementFound("NATIVE","xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]/child::*[@id='climate_seat_heat_icon']"))
                createErrorLog("Passenger Seat status not known");
            sc.report("Passenger Seat Heat is Already ON", true);
        }

        sc.swipeWhileNotFound("Down", sc.p2cy(60), 2000, "NATIVE", "xpath=//*[@id='climate_save_button_cta']", 0, 3000, 2, false);
        /*
        Steering Wheel toggle On
         */
        verifyElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_heating_text']",0);
        if (sc.isElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_switch' and @checked='false']")) {
            createLog("Steering Wheel Seat is Off");
            click("NATIVE","xpath=//*[@id='climate_steering_wheel_switch' and @checked='false']",0,1);
            verifyElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_switch' and @checked='true']",0);
            sc.report("Steering Wheel Heat Turned On",true);
        }
        else {sc.report("Steering Wheel Heat Already On",true);}
        /*
        Defrost Settings
         */
        //Front
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_defrost_front_switch' and @checked='false']")) {
            click("NATIVE", "xpath=//*[@id='climate_defrost_front_switch' and @checked='false']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@id='climate_defrost_front_switch' and @checked='true']",0);
            sc.report("Front defrost Turned On", true);
        } else if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_defrost_front_switch' and @checked='true']")) {
            sc.report("Front defrost Already On", true);
        }
        //Back
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_defrost_rear_switch' and @checked='false']")) {
            click("NATIVE", "xpath=//*[@id='climate_defrost_rear_switch' and @checked='false']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@id='climate_defrost_rear_switch' and @checked='true']",0);
            sc.report("Back defrost Turned On", true);
        } else if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_defrost_rear_switch' and @checked='true']")) {
            sc.report("Back defrost Already On", true);
        }

        /*
        save Climate Settings
         */
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_save_button_cta']"))
            click("NATIVE", "xpath=//*[@id='climate_save_button_cta']", 0, 1);
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Save Settings']"))
            sc.swipe("up", sc.p2cy(30), 100);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Info')]"))
            sc.report("Climate settings saved", true);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='top_nav_profile_icon']"))
            sc.report("Climate settings saved successfully", true);
        sc.syncElements(5000, 10000);
        createLog("Completed - 21MM Remote Climate Settings Toggle ON");
    }

    public static void executeRemoteClimateSettingsToggleOff() {
        sc.syncElements(5000, 10000);
        createLog("Started - 21MM Remote Climate Settings Toggle OFF");
        /*
         Remote Climate steps
         */
        if (!sc.isElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']")) {
            reLaunchApp_android();
            click("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, 1);
            sc.syncElements(5000, 10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Climate']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Climate']", 0, 1);
        sc.syncElements(25000, 50000);
         /*
         Check if Custom settings is ON
         */
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_custom_climate_switch' and @checked='false']")) {
            createLog("Custom Climate Settings On, Toggling ON");
            click("NATIVE", "xpath=//*[@id='climate_custom_climate_switch' and @checked='false']", 0, 1);
        }

         /*
        Heated Seat Settings Turning off if found ON
         */
        //Driver Seat
        if (sc.isElementFound("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]/child::*[@id='climate_seat_heat_icon']")) {
            createLog("Driver Seat Heat On, turning Driver Seat Off");
            click("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]", 0, 1);
            click("NATIVE","xpath=//*[./*[@id='climate_seat_off_icon']]",0,1);
            sc.report("Driver Seat Turned OFF", true);
        } else if (sc.isElementFound("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]/child::*[@id='climate_seat_vent_icon']")) {
            createLog("Driver Seat Vent On, turning Driver Seat Off");
            click("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]", 0, 1);
            click("NATIVE","xpath=//*[./*[@id='climate_seat_off_icon']]",0,1);
            sc.report("Driver Seat Turned OFF", true);
        }
        else {
            if (!sc.isElementFound("NATIVE","xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[1]/child::*[@id='climate_seat_off_icon']"))
                createErrorLog("Driver Seat status unknown");
            sc.report("Driver side Seat Heat is Already OFF", true);
        }
        //Passenger Seat
        if (sc.isElementFound("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]/child::*[@id='climate_seat_heat_icon']")) {
            createLog("Passenger Seat Heat On, turning Passenger Seat Off");
            click("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]", 0, 1);
            click("NATIVE", "xpath=//*[./*[@id='climate_seat_off_icon']]",0,1);
            sc.report("Passenger side Heat seat OFF", true);
        } else if (sc.isElementFound("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]/child::*[@id='climate_seat_vent_icon']")) {
            createLog("Passenger Seat Vent On, turning Passenger Seat Off");
            click("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]", 0, 1);
            click("NATIVE","xpath=//*[./*[@id='climate_seat_off_icon']]",0,1);
            sc.report("Passenger Seat Turned OFF", true);
        }
        else {
            if (!sc.isElementFound("NATIVE", "xpath=(//*[@contentDescription='Seat climate']/following-sibling::*)[2]/child::*[@id='climate_seat_off_icon']"))
                createErrorLog("Passenger Seat status unknown");
            sc.report("Passenger side Seat Heat is Already OFF", true);
        }

        sc.swipeWhileNotFound("Down", sc.p2cy(60), 2000, "NATIVE", "xpath=//*[@id='climate_save_button_cta']", 0, 3000, 2, false);
        /*
        Steering Wheel Turn OFF
         */
        verifyElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_heating_text']",0);
        if (sc.isElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_switch' and @checked='true']")) {
            createLog("Steering Wheel Seat is ON");
            click("NATIVE","xpath=//*[@id='climate_steering_wheel_switch' and @checked='true']",0,1);
            verifyElementFound("NATIVE","xpath=//*[@id='climate_steering_wheel_switch' and @checked='false']",0);
            sc.report("Steering Wheel Heat Turned OFF",true);
        }
        else {sc.report("Steering Wheel Heat Already OFF",true);}

        /*
        Defrost Settings
         */
        //Front
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_defrost_front_switch' and @checked='true']")) {
            click("NATIVE", "xpath=//*[@id='climate_defrost_front_switch' and @checked='true']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@id='climate_defrost_front_switch' and @checked='false']",0);
            sc.report("Front defrost Turned OFF", true);
        } else if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_defrost_front_switch' and @checked='false']")) {
            sc.report("Front defrost Already OFF", true);
        }
        //Back
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_defrost_rear_switch' and @checked='true']")) {
            click("NATIVE", "xpath=//*[@id='climate_defrost_rear_switch' and @checked='true']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@id='climate_defrost_rear_switch' and @checked='false']",0);
            sc.report("Back defrost Turned OFF", true);
        } else if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_defrost_rear_switch' and @checked='false']")) {
            sc.report("Back defrost Already OFF", true);
        }

        /*
        Turn Custom Climate Settings OFF
         */
        sc.swipeWhileNotFound("Up", sc.p2cy(60), 2000, "NATIVE", "xpath=//*[@id='climate_custom_climate_title']", 0, 3000, 2, false);
        createLog("Toggling Climate Settings OFF");
        sc.click("NATIVE","xpath=//*[@id='climate_custom_climate_switch']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_custom_climate_switch' and @checked='false']",0);

        /*
        Verify Temp bar is Off
         */
        if (sc.isElementFound("NATIVE","xpath=//*[@class='android.widget.SeekBar' and @enable='false]"))
            sc.report("Temp bar is disabled",true);
        else {sc.report("Temp bar is disabled", false);}

        /*
        save Climate Settings
         */
        sc.swipeWhileNotFound("Down", sc.p2cy(60), 2000, "NATIVE", "xpath=//*[@id='climate_save_button_cta']", 0, 3000, 2, false);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='climate_save_button_cta']"))
            sc.click("NATIVE", "xpath=//*[@id='climate_save_button_cta']", 0, 1);
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Save Settings']"))
            sc.swipe("up", sc.p2cy(30), 100);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Info')]"))
            sc.report("Climate settings saved", true);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='top_nav_profile_icon']"))
            sc.report("Climate settings saved successfully", true);
        sc.syncElements(5000, 10000);
        createLog("Completed - 21MM Remote Climate Settings Toggle OFF");
    }
}
