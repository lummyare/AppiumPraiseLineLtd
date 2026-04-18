package v2update.subaru.ios.usa.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruRemoteEVIOS extends SeeTestKeywords {
    String testName = " - SubaruRemoteEV-IOS";

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
        iOS_Setup2_5(this.testName);
        environmentSelection_iOS("prod");
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void RemoteUnLock(){
        sc.startStepsGroup("Test - SubaruEV remote Unlock");
        clearPNS();
        executeRemoteUnLock();
        PNS("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void RemoteLock(){
        sc.startStepsGroup("Test - SubaruEV remote Lock");
        executeRemoteLock();
        PNS("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void RemoteStart(){
        sc.startStepsGroup("Test - SubaruEV remote Start");
        executeRemoteStart();
        PNS("EVStart");
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void RemoteStop(){
        sc.startStepsGroup("Test - SubaruEV remote Stop");
        executeRemoteStop();
        PNS("Stop");
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void RemoteLights(){
        sc.startStepsGroup("Test - SubaruEV remote Lights");
        executeRemoteLights();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void RemoteHazard(){
        sc.startStepsGroup("Test - SubaruEV remote Hazard");
        executeRemoteHazard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void RemoteBuzzer(){
        sc.startStepsGroup("Test - SubaruEV remote Buzzer");
        executeRemoteBuzzer();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void RemoteTrunkUnLock(){
        sc.startStepsGroup("Test - SubaruEV remote Trunk Unlock");
        executeRemoteTrunkUnlock();
        PNS("Trunk Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void RemoteTrunkLock(){
        sc.startStepsGroup("Test - SubaruEV remote Trunk Lock ");
        executeRemoteTrunkLock();
        PNS("Trunk Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    public void RemoteHorn(){
        sc.startStepsGroup("Test - SubaruEV remote Horn");
        executeRemoteHorn();
        PNS("Horn");
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    public void StatusScreenDoorsUnlockLockTest(){
        sc.startStepsGroup("Test - Status Screen Doors Unlock Lock Validation");
        statusDoorsUnlockLockValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    public void ClimateScreen(){
        sc.startStepsGroup("Test - SubaruEV remote schedule screen");
        GotoClimateScheduleSubaruEV();
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    public void RemoteClimateSettingsOn(){
        sc.startStepsGroup("Test - SubaruEV remote Climate Settings On");
        executeRemoteClimateSettingsOn();
        sc.stopStepsGroup();
    }

    @Test
    @Order(14)
    public void RemoteClimateSettingsOFF(){
        sc.startStepsGroup("Test - SubaruEV remote Climate Settings Off");
        executeRemoteClimateSettingsOff();
        sc.stopStepsGroup();
    }

    @Test
    @Order(15)
    public void deleteScheduleTest() {
        sc.startStepsGroup("Test - Delete All Schedules ");
        deleteSchedule();
        sc.stopStepsGroup();
    }
    @Test
    @Order(16)
    public void createScheduleTest() {
        sc.startStepsGroup("Test - Create Schedule ");
        createSchedule();
        sc.stopStepsGroup();
    }
    @Test
    @Order(17)
    public void editScheduleTest() {
        sc.startStepsGroup("Test - Modify Schedule ");
        editSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(18)
    public void SignOut() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void executeRemoteUnLock() {
        createLog("Started - SubaruEV Remote Unlock");
        iOS_remoteUnLock();
        //verify remote unlock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now unlocked","already unlocked","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Unlock");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Unlock");
    }

    public static void executeRemoteLock() {
        createLog("Started - SubaruEV Remote Lock");
        iOS_remoteLock();
        //verify remote lock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now locked","already locked", "Your request could not be completed because a keyfob was detected in your vehicle","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started", "Lock");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Lock");
    }

    public static void executeRemoteStart() {
        createLog("Started - SubaruEV Remote Start");
        iOS_remoteStart();
        //verify remote start notification is displayed
        remoteCommandNotificationVerification("Vehicle was started and will automatically shut off in 20 minutes","minute cycles without an occupant","keyfob was not left inside your vehicle","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Start");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Start");
    }

    public static void executeRemoteStop() {
        createLog("Started - SubaruEV Remote Stop");
        iOS_remoteStop();
        //verify remote stop notification is displayed
        remoteCommandNotificationVerification("Remote Engine Stop Request was successful","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Stop");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Stop");
    }

    public static void executeRemoteLights() {
        createLog("Started - SubaruEV Remote Lights");
        iOS_remoteLights();
        //verify remote lights notification is displayed
        remoteCommandNotificationVerification("The Remote head lamp command was successful and will turn off automatically","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Light");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Lights");
    }

    public static void executeRemoteHazard() {
        createLog("Started - SubaruEV Remote Hazard");
        iOS_remoteHazard();
        //verify remote hazard notification is displayed
        remoteCommandNotificationVerification("The Remote hazard flash command was successful and will turn off automatically","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Hazard");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Hazard");
    }

    public static void executeRemoteBuzzer() {
        createLog("Started - SubaruEV Remote Buzzer");
        iOS_remoteBuzzer();
        //verify remote Buzzer notification is displayed
        remoteCommandNotificationVerification("The remote buzzer request was successful","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Buzzer");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Buzzer");
    }

    public static void executeRemoteTrunkUnlock() {
        createLog("Started - SubaruEV Remote Trunk Unlock");
        iOS_remoteTrunkUnlock();
        //verify remote Trunk Unlock notification is displayed
        remoteCommandNotificationVerification("The remote trunk/hatch unlock was successful","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Trunk/Hatch Unlock");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Trunk Unlock");
    }

    public static void executeRemoteTrunkLock() {
        createLog("Started - SubaruEV Remote Trunk Lock");
        iOS_remoteTrunkLock();
        //verify remote Trunk Lock notification is displayed
        remoteCommandNotificationVerification("The remote trunk/hatch lock was successful","Your request could not be completed because a keyfob was detected in your vehicle","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Trunk Lock");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Trunk Lock");
    }

    public static void executeRemoteHorn() {
        createLog("Started - SubaruEV Remote Horn");
        iOS_remoteHorn();
        //verify remote Horn notification is displayed
        remoteCommandNotificationVerification("The remote horn request was successful","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Horn");
        //navigate back to dashboard screen
        iOS_goToDashboardFromNotificationScreen();
        createLog("Completed - SubaruEV Remote Horn");
    }

    public static void executeRemoteClimateSettingsOn() {
        createLog("Started - Subaru Remote Climate Settings On");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_tab_settings_title']")) {
            reLaunchApp_iOS();
            verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
            verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@id='remote_climate_button']", 0);
            click("NATIVE", "xpath=//*[@id='remote_climate_button']", 0, 1);
            sc.syncElements(15000, 60000);

        }
        sc.syncElements(2000, 10000);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        }

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
                    "//*[@accessibilityLabel='climate_temperature_value_text']", 0, "value");
            createLog("Temperature value is :" + temperature);
            sc.report("Temperature set to :" + temperature, true);
        }
        else {
            createErrorLog("Temperature element is not Found");
            sc.report("Temperature element is not Found", false);
        }

        //SEAT SETTINGS
        climateSeatSettings();

        //STEERING WHEEL
        climateSteeringWheelSettings();

        //DEFROST SETTINGS
        climateDefrostSettings();

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
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_climate_button']", 0);

        createLog("Completed - Subaru Remote Climate Settings On");
    }

    public static void executeRemoteClimateSettingsOff() {
        createLog("Started - Subaru Remote Climate Settings OFF");
        sc.syncElements(2000, 10000);
        if (!sc.isElementFound("NATIVE", "xpath=//*[@id='remote_climate_button']")) {
            reLaunchApp_iOS();
            verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
            verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@id='remote_climate_button']", 0);
        }
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

        //SEAT SETTINGS
        climateSeatSettings();

        //STEERING WHEEL
        climateSteeringWheelSettings();

        //DEFROST SETTINGS
        climateDefrostSettings();

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
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_climate_button']", 0);
        createLog("Completed - Subaru Remote Climate Settings OFF");
        //pull down advanced remote section if its displayed
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']"))
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
        sc.syncElements(3000, 15000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Back']"))
            sc.click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        sc.syncElements(3000, 15000);
        verifyElementFound("NATIVE", "xpath=//*[@label='person']", 0);
    }

    public static void GotoClimateScheduleSubaruEV(){
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
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@label='Climate']",0);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_tab_schedule_title']",0);
        click("NATIVE","xpath=//*[@accessibilityLabel='climate_tab_schedule_title']",0,1);
        sc.syncElements(5000, 60000);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_tab_settings_title']",0);
        click("NATIVE","xpath=//*[@accessibilityLabel='climate_tab_settings_title']",0,1);
        sc.syncElements(5000, 60000);
    }
    public static void createSchedule(){
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String _currentDate = formatter.format(currentDate);
        createLog("Creating Schedule");
        if(!sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_tab_schedule_title']")) {
            GotoClimateScheduleSubaruEV();
        }
        click("NATIVE","xpath=//*[@text='Schedule']",0,1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_add_cta' and @onScreen='true']",0);
        if(sc.isElementFound("NATIVE","xpath=//*[@class='UIAView' and ./*[contains(@text,'Schedule ')]]")){
            deleteSchedule();
        }
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@label,'Please tap the') and contains(@label,'button below to set the day and start time you want your vehicle to begin heating or cooling')]")) {
            createLog("Existing schedules not present");
        }
        //count before adding
        int beforeAddScheduleCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@text,'Schedule ')]");
        createLog("Before adding schedule - count is : "+beforeAddScheduleCount);
        sc.swipe("up", sc.p2cy(40), 3000);
        // create Schedule
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_add_cta']",0);
        click("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_add_cta']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Start Time']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Date']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Climate']",0);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_create_cta']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='When you want climate settings to start']",0);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_time_value']",0);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_time_am_switch' and @value='AM']",0);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_time_am_switch' and @value='PM']",0);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_date_value']",0);
        String dateDisplayed = sc.elementGetProperty("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_date_value']",0,"value");
        createLog("Date displayed is: "+dateDisplayed);
        sc.syncElements(2000,4000);
        click("NATIVE","xpath=//*[@text='Mo']",0,1);
        click("NATIVE","xpath=//*[@text='Tu']",0,1);
        click("NATIVE","xpath=//*[@text='We']",0,1);

        //climate section
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_climate_card' and @label='Climate']",0);
        click("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_climate_card' and @label='Climate']",0,1);
        //cool and head icons
        verifyElementFound("NATIVE", "xpath=//*[@label='cool']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='heat']", 0);
        //Temperature Bar is displayed and editable
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_temperature_slider']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_temperature_value_text']")) {
            String temperature = sc.elementGetProperty("NATIVE", "xpath=//*[@accessibilityLabel='climate_temperature_value_text']", 0, "value");
            createLog("Temperature value is :" + temperature);
            sc.report("Temperature set to :" + temperature, true);
        }
        else {
            createErrorLog("Temperature element is not Found");
            sc.report("Temperature element is not Found", false);
        }

        //SEAT SETTINGS
        climateSeatSettings();

        //STEERING WHEEL
        climateSteeringWheelSettings();

        //DEFROST SETTINGS
        climateDefrostSettings();


        //click back
        click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_title']",0);

        //save icon
        click("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_create_cta']",0,1);
        sc.syncElements(15000,30000);
        createLog("Schedule created successfully");
        createLog("Verifying created new schedule in Schedule screen");

        //verify count after adding
        int afterAddScheduleCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@text,'Schedule ')]");
        createLog("After adding schedule - count is : "+afterAddScheduleCount);

        if(afterAddScheduleCount > beforeAddScheduleCount) {
            createLog("Added schedule successfully");
        } else {
            createErrorLog("Add schedule test failed");
            fail();
        }
        createLog("Verified created new schedule in Schedule screen");
    }
    public static void editSchedule() {
        int dayInt = 0;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CST"));
        Date currentDate = new Date();
        createLog("Current date is :"+currentDate);
        dayInt = cal.get(Calendar.DAY_OF_MONTH);
        createLog("Current day to be selected is : "+dayInt);
        dayInt = dayInt + 1; // next day
        createLog("Next Day to be selected is : "+dayInt);
        String selectedTime = "";
        createLog("Started - modify schedule");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_tab_schedule_title']")) {
            GotoClimateScheduleSubaruEV();
        }
        click("NATIVE","xpath=//*[@text='Schedule']",0,1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_add_cta' and @onScreen='true']",0);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Schedule ')]")) {
            click("NATIVE", "xpath=//*[contains(@text,'Schedule ')]", 0, 1);
            sc.syncElements(10000,30000);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_schedule_title']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_schedule_delete_cta']", 0);
            click("NATIVE","xpath=//*[@id='When you want climate settings to start']",0,1);
            click("NATIVE","xpath=//*[@text='Th']",0,1);
            click("NATIVE","xpath=//*[@text='Fr']",0,1);

            //climate section
            verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_climate_card' and @label='Climate']",0);
            click("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_climate_card' and @label='Climate']",0,1);
            //cool and head icons
            verifyElementFound("NATIVE", "xpath=//*[@label='cool']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@label='heat']", 0);
            //Temperature Bar is displayed and editable
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_temperature_slider']", 0);
            if(sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_temperature_value_text']")) {
                String temperature = sc.elementGetProperty("NATIVE", "xpath=//*[@accessibilityLabel='climate_temperature_value_text']", 0, "value");
                createLog("Temperature value is :" + temperature);
                sc.report("Temperature set to :" + temperature, true);
            }
            else {
                createErrorLog("Temperature element is not Found");
                sc.report("Temperature element is not Found", false);
            }

            //SEAT SETTINGS
            climateSeatSettings();

            //STEERING WHEEL
            climateSteeringWheelSettings();

            //DEFROST SETTINGS
            climateDefrostSettings();

            //click back
            click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
            verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_title']",0);

            click("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_create_cta']",0,1);
            sc.syncElements(15000,30000);
            verifyElementFound("NATIVE","xpath=//*[contains(@text,'Monday,Tuesday,Wednesday,Thursday,Friday')]",0);
            createLog("Schedule Modified successfully");

            //Add date
            createLog("Add Schedule Date");
            click("NATIVE", "xpath=//*[contains(@text,'Schedule ')]", 0, 1);
            sc.syncElements(10000,30000);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_schedule_title']", 0);
            verifyElementFound("NATIVE","xpath=//*[@text='Date']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_schedule_detail_date_value']", 0);
            click("NATIVE", "xpath=//*[@accessibilityLabel='climate_schedule_detail_date_value']", 0, 1);
            sc.syncElements(2000,10000);
            verifyElementFound("NATIVE","xpath=//*[@text='Previous Month']",0);
            verifyElementFound("NATIVE","xpath=//*[@text='Next Month']",0);
            if(dayInt > 30) {
                //click next month
                click("NATIVE", "xpath=//*[@text='Next Month']", 0, 1);
                dayInt = 1;
                createLog("Next month date to be selected is: "+dayInt);
            }
            String day = String.valueOf(dayInt);
            click("NATIVE", "xpath=//*[@text='"+day+"']", 0, 1);
            sc.syncElements(3000,30000);
            sc.flickElement("NATIVE", "xpath=//*[@text='Previous Month']", 0, "down");
            verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_date_value']",0);
            String selectedDate = sc.elementGetProperty("NATIVE", "xpath=//*[@accessibilityLabel='climate_schedule_detail_date_value']", 0, "value");
            createLog("Selected date is :" + selectedDate);
            if(selectedDate.contains(day)) {
                createLog("Date selected");
                sc.report("Date selected", true);
            }
            else {
                createErrorLog("Date not selected");
                sc.report("Date not selected", false);
            }

            //change time
            verifyElementFound("NATIVE","xpath=//*[@text='Start Time']",0);
            verifyElementFound("NATIVE","xpath=(//*[(@accessibilityLabel='climate_schedule_detail_time_pm_switch' or @accessibilityLabel='climate_schedule_detail_time_am_switch') and (@value='AM' or @value='PM')])[1]",0);
            click("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_detail_time_value']",0,1);
            sc.syncElements(2000,30000);
            verifyElementFound("NATIVE","xpath=//*[@class='UIAPicker']",0);
            if(sc.isElementFound("NATIVE","xpath=//*[@text='AM' and @class='UIAPickerWheel']")){
                verifyElementFound("NATIVE","xpath=//*[@text='AM' and @class='UIAPickerWheel']",0);
                //flick up and select PM
                createLog("Flick up and select PM");
                sc.flickElement("NATIVE", "xpath=//*[@text='AM' and @class='UIAPickerWheel']", 0, "UP");
                sc.syncElements(2000,30000);
                verifyElementFound("NATIVE","xpath=//*[@text='PM' and @class='UIAPickerWheel']",0);
                createLog("Selected PM");
                selectedTime = "PM";
            } else {
                verifyElementFound("NATIVE","xpath=//*[@text='PM' and @class='UIAPickerWheel']",0);
                //flick down and select AM
                createLog("Flick down and select AM");
                sc.flickElement("NATIVE", "xpath=//*[@text='PM' and @class='UIAPickerWheel']", 0, "down");
                sc.syncElements(2000,30000);
                verifyElementFound("NATIVE","xpath=//*[@text='AM' and @class='UIAPickerWheel']",0);
                createLog("Selected AM");
                selectedTime = "AM";
            }
            sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            sc.verifyElementNotFound("NATIVE","xpath=//*[@class='UIAPicker']",0);
            click("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_create_cta']",0,1);
            sc.syncElements(15000,30000);
            sc.verifyElementNotFound("NATIVE","xpath=//*[contains(@text,'Monday,Tuesday,Wednesday,Thursday,Friday')]",0);
            String savedDateValue = sc.elementGetProperty("NATIVE", "xpath=(//*[@accessibilityLabel='climate_schedule_date_text'])[1]", 0, "value");
            createLog("Save date is :" + savedDateValue);
            if(savedDateValue.contains(day)) {
                createLog("Date saved");
                sc.report("Date saved", true);
            }
            else {
                createErrorLog("Date not saved");
                sc.report("Date not saved", false);
            }
            String savedTimeValue = sc.elementGetProperty("NATIVE", "xpath=(//*[@accessibilityLabel='climate_schedule_time_text'])[1]", 0, "value");
            createLog("Save Time is :" + savedTimeValue);
            if(savedTimeValue.contains(selectedTime)) {
                createLog("Time saved");
                sc.report("Time saved", true);
            }
            else {
                createErrorLog("Time not saved");
                sc.report("Time not saved", false);
            }
            createLog("Schedule Date Added successfully");
        }
    }
    public static void deleteSchedule(){
        if(!sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_tab_schedule_title']")){
            GotoClimateScheduleSubaruEV();
        }
        click("NATIVE","xpath=//*[@text='Schedule']",0,1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='climate_schedule_add_cta' and @onScreen='true']",0);
        int scheduleCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@text,'Schedule ')]");
        createLog("Climate Schedule - count is : "+scheduleCount);
        if(scheduleCount > 0){
            for(int i=1;i<=scheduleCount;i++) {
                createLog("Deleting schedules");
                if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Schedule ')]")) {
                    click("NATIVE", "xpath=(//*[contains(@text,'Schedule ')])[1]", 0, 1);
                    verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_schedule_delete_cta']", 0);
                    click("NATIVE", "xpath=//*[@accessibilityLabel='climate_schedule_delete_cta']", 0, 1);
                    sc.syncElements(2000, 10000);
                    verifyElementFound("NATIVE", "xpath=//*[@text='Remove']", 0);
                    click("NATIVE", "xpath=//*[@text='Remove']", 0, 1);
                    sc.syncElements(5000, 60000);
                    int scheduleCountAfterDelete = sc.getElementCount("NATIVE", "xpath=//*[contains(@text,'Schedule ')]");
                    if (scheduleCount > scheduleCountAfterDelete) {
                        createLog("Schedule" + i + " Deleted");
                    } else {
                        createLog("schedule is not deleted");
                        fail();
                    }
                } else if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Schedule ')]")) {
                    createLog("All Schedules Cleared");
                    break;
                }
            }
        }
        else {
            createLog("No Climate Schedules Present");
        }

    }

    public static void climateSeatSettings() {
        createLog("Started : Climate seat settings");
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
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='climate_seat_heat_text']", 0, 1);
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
        createLog("Completed: Climate seat settings");
    }

    public static void climateSteeringWheelSettings() {
        createLog("Started : Climate SteeringWheel Settings");
        /*
        Steering wheel
         */
        sc.flickElement("NATIVE", "xpath=(//*[@accessibilityLabel='climate_steering_wheel_title' or @accessibilityLabel='climate_defrost_title'])[1]", 0, "UP");
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_steering_wheel_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Steering Wheel']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_steering_wheel_heating_text']", 0);

        String steeringWheelToggleVal = sc.elementGetProperty("NATIVE","xpath=//*[@accessibilityLabel='climate_steering_wheel_switch']",0,"value");
        createLog("Steering wheel toggle value is: "+steeringWheelToggleVal);
        if (steeringWheelToggleVal.equalsIgnoreCase("0")) {
            createLog("Steering wheel Already OFF");
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='climate_steering_wheel_switch']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='climate_steering_wheel_switch' and @value='1']")) {
                createLog("Steering wheel Turned ON");
                sc.report("Steering wheel Turned ON", true);
            }
        } else if (steeringWheelToggleVal.equalsIgnoreCase("1")) {
            createLog("Steering wheel Already ON");
            sc.report("Steering wheel Already On", true);
        }
        createLog("Completed : Climate SteeringWheel Settings");
    }

    public static void climateDefrostSettings() {
        createLog("Started : Climate Defrost Settings");
        /*
        Defrost Settings
         */
        sc.flickElement("NATIVE", "xpath=(//*[@accessibilityLabel='climate_steering_wheel_title' or @accessibilityLabel='climate_defrost_title'])[1]", 0, "UP");
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
        createLog("Completed : Climate Defrost Settings");
    }

    public static void statusDoorsUnlockLockValidation() {
        createLog("Started - Status doors Unlock Lock validation");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'A new iOS update is now available')])[1]"))
            sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        } else {
            reLaunchApp_iOS();
        }
        click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);

        if (sc.isElementFound("NATIVE","xpath=//*[@text='Doors']/following-sibling::*[@text='Locked']")) {
            sc.report("Doors are locked", true);
            createLog("Doors are locked");
            //verify doors icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);
            //verify doors check icon
            verifyElementFound("NATIVE", "xpath=(//*[@text='Doors']/following-sibling::*[@text='GoodStatus'])[1]", 0);

            //perform remote unlock - Lock button should be displayed in Status->Doors section
            performRemoteUnlock();

            //verify Doors unlock text and lock button is displayed in Status->Doors section
            sc.syncElements(2000, 10000);
            click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Unlocked']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='VehicleStatusScreen_lock_button']", 0);

        } else {
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Unlocked']", 0);
            sc.report("Doors are unlocked", true);
            createLog("Doors are unlocked");
            //verify doors icon
            verifyElementFound("NATIVE", "xpath=//*[@text='dooropen']", 0);
            //verify doors warning alerts icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Alert']", 0);

        }
        //verify remote lock in status screen
        verifyRemoteLock();

        createLog("Completed - Status doors Unlock Lock validation");
    }

    public static void performRemoteUnlock() {
        createLog("Started - unlock remote command execution");
        //Click remote tab
        click("NATIVE", "xpath=//*[@text='Remote']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_door_unlock_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_door_unlock_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Unlocking");

        //Notifications validation
        iOS_goToNotificationsScreen();
        //verify remote unlock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now unlocked","Unlock");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");

        createLog("Completed unlock remote command execution");
    }

    public static void verifyRemoteLock() {
        createLog("Verifying - Lock remote command in Status screen");
        //Verify lock button
        verifyElementFound("NATIVE", "xpath=//*[@text='VehicleStatusScreen_lock_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@text='VehicleStatusScreen_lock_button']", 0, 1, 0, 0);
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Locked']", 0);

        iOS_goToNotificationsScreen();
        //verify remote lock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now locked","already locked","Lock");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        createLog("Verification successful - Lock remote command in Status screen");
    }
}
