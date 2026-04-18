package v2update.android.usa.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Remote17CYPlusAndroid extends SeeTestKeywords {
    String testName = "Remote17CYPlus-Android";

    @BeforeAll
    public void setup() throws Exception {
        android_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("17CyPlus email Login");
        createLog("Start: 17CyPlus email Login");
        selectionOfCountry_Android("USA");
        selectionOfLanguage_Android("English");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strEmail17CYPlus"), ConfigSingleton.configMap.get("strPassword17CYPlus"));
        createLog("Completed: 17CyPlus email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void RemoteUnLock(){
        sc.startStepsGroup("17CYPlus Remote Unlock");
        clearPNS_android();
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        executeRemoteUnlock();
        android_PNS("Unlock");
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    public void RemoteLock(){
        sc.startStepsGroup("17CYPlus Remote Lock");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']")) {
            reLaunchApp_android();
        }
        executeRemoteLock();
        android_PNS("Lock");
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    public void RemoteStart(){
        sc.startStepsGroup("17CYPlus Remote Start");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']")) {
            reLaunchApp_android();
        }
        executeRemoteClimateStart();
        sc.stopStepsGroup();
    }
    @Test
    @Order(4)
    public void RemoteStop(){
        sc.startStepsGroup("17CYPlus Remote Stop");
        executeRemoteClimateStop();
        sc.stopStepsGroup();
    }
    @Test
    @Order(5)
    public void RemoteHazard(){
        sc.startStepsGroup("17CYPlus Remote Hazard");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']")) {
            reLaunchApp_android();
        }
        executeRemoteHazards();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void ClimateScreen(){
        sc.startStepsGroup("17CYPlus Climate Screen");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']")) {
            reLaunchApp_android();
        }
        remoteClimateScheduleScreenValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void AddClimateSchedule(){
        sc.startStepsGroup("17CYPlus Create Climate Schedule");
        createNewClimateSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void editTheClimateSchedule(){
        sc.startStepsGroup("17CYPlus Edit Climate Schedule");
        editClimateSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void SignOut(){
        android_SignOut();
    }

    public void executeRemoteUnlock() {
        createLog("Started - 17CYPlus Remote Unlock");
        verifyElementFound("NATIVE","xpath=//*[@text='Unlock']",0);
        sc.longClick("NATIVE", "xpath=//*[@text='Unlock']",0,1,0,0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Unlocking");

        android_goToNotificationsScreen();
        remoteUnlockNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 17CYPlus Remote Unlock");
    }
    public static void executeRemoteLock() {
        createLog("Started - 17CYPlus Remote Lock");
        verifyElementFound("NATIVE","xpath=//*[@text='Lock']",0);
        sc.longClick("NATIVE", "xpath=//*[@text='Lock']",0,1,0,0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Locking");

        android_goToNotificationsScreen();
        remoteLockNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Completed - 17CYPlus Remote Lock");
    }
    public static void executeRemoteClimateStart(){
        createLog("Started - 17CYPlus Remote Climate Start");
        verifyElementFound("NATIVE","xpath=//*[@text='Start']",0);
        click("NATIVE", "xpath=//*[@text='Start']",0,1);
        sc.syncElements(25000,50000);
        remoteClimateStart();
        createLog("Completed - 17CYPlus Remote Climate Start");
    }
    public static void executeRemoteClimateStop(){
        createLog("Started - 17CYPlus Remote Climate Stop");
        remoteClimateStop();
        createLog("Completed - 17CYPlus Remote Climate Stop");
    }
    public static void remoteLockNotificationDisplayed() {
        createLog("Remote Lock Notification verifications");
        sc.waitForElement("NATIVE", "xpath=(//*[@id='nh_title'])[1]", 0, 10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=(//*[@id='nh_title'])[1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=(//*[@id='nh_date'])[1]", 0);
        boolean dateCondition =  firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago");
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
    }
    public static void remoteUnlockNotificationDisplayed() {
        createLog("Remote Unlock Notification verifications");
        sc.waitForElement("NATIVE","xpath=(//*[@id='nh_title'])[1]",0,10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=(//*[@id='nh_title'])[1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=(//*[@id='nh_date'])[1]", 0);
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
    }
    public static void remoteHazardNotificationDisplayed(){
        createLog("Remote Hazards Notification verifications");
        sc.waitForElement("NATIVE","xpath=(//*[@id='nh_title'])[1]",0,10);
        String firstNotification = sc.elementGetText("NATIVE", "xpath=(//*[@id='nh_title'])[1]", 0);
        String firstNotificationDate = sc.elementGetText("NATIVE", "xpath=(//*[@id='nh_date'])[1]", 0);
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
    }
    public static void executeRemoteHazards() {
        createLog("Started - 17CYPlusPHEV Remote Hazards");
        click("NATIVE","xpath=//*[@id='dashboard_remote_open_iconbutton']",0,1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Hazards']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_hazard_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_Android_remoteCommandAction("Hazards on");

        android_goToNotificationsScreen();
        remoteHazardNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        sc.syncElements(5000, 10000);
        createLog("Completed - 17CYPlusPHEV Remote Hazards");
    }
    public static void remoteClimateStart() {
        sc.waitForElement("NATIVE", "xpath=//*[@id='climate_title']", 0, 10);
        verifyElementFound("NATIVE", "xpath=//*[@id='climate_title']", 0);
        click("NATIVE", "xpath=//*[@text='Start Climate']", 0, 1);
        sc.syncElements(5000, 20000);
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Apply']")) {
            click("NATIVE", "xpath=//*[@text='Apply']", 0, 1);
        }
        sc.syncElements(75000,150000);
        sc.waitForElement("NATIVE", "xpath=//*[@text='Turn Off Climate']", 0, 40);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Turn Off Climate']")) {
            sc.report("Remote climate Start successful", true);
            createLog("Remote climate Start successful");
        }else{
            sc.report("Remote climate Start unsuccessful", false);
            createErrorLog("Remote climate Start unsuccessful");
        }
        sc.syncElements(5000, 10000);
    }
    public static void remoteClimateStop() {
        sc.waitForElement("NATIVE", "xpath=//*[@id='climate_title']", 0, 40);
        sc.waitForElement("NATIVE", "xpath=//*[@text='Turn Off Climate']", 0, 40);
        click("NATIVE", "xpath=//*[@text='Turn Off Climate']", 0, 1);
        sc.syncElements(25000, 50000);
        sc.waitForElement("NATIVE", "xpath=//*[@text='Turn Off']", 0, 10);
        sc.click("NATIVE", "xpath=//*[@text='Turn Off']", 0, 1);
        sc.syncElements(25000, 50000);
        sc.waitForElement("NATIVE", "xpath=//*[@text='Start Climate']", 0, 30);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Start Climate']")){
            sc.report("Remote climate Stop successful", true);
            createLog("Remote climate Stop successful");
        }else{
            sc.report("Remote climate Stop unsuccessful", false);
            createLog("Remote climate Stop unsuccessful");
        }
        click("NATIVE","xpath=//*[@content-desc='back_button']",0,1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@resource-id='dashboard_display_image']",0);
    }

    public static void remoteClimateScheduleScreenValidation(){
        createLog("Started - 17CYPlus Remote Climate Start");
        verifyElementFound("NATIVE","xpath=//*[@text='Start']",0);
        click("NATIVE", "xpath=//*[@text='Start']",0,1);
        sc.syncElements(25000,50000);
        click("NATIVE", "xpath=//*[@class='android.view.View' and ./*[@text='Schedule']]",0,1);
        sc.syncElements(25000,50000);
        deleteClimateSchedule();
        click("NATIVE", "xpath=//*[@contentDescription='Add']",0,1);

        verifyElementFound("NATIVE","xpath=//*[@id='climate_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='When you want climate settings to start']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_time_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_date_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_time_value']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_time_am_switch']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_time_pm_switch']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_date_value']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_day_of_week_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Mo']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Tu']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='We']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Th']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Fr']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Sa']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Su']",0);

        //Click Back Button
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        //click("NATIVE", "xpath=(//*[./*[@id='climate_title']]/child::*)[1]",0,1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Add']",0);
        createLog("Completed - 17CYPlus Remote Climate Start");
    }

    public static void deleteClimateSchedule(){
        createLog("Starting Delete Schedules");
        if (sc.isElementFound("NATIVE","xpath=//*[@id='climate_schedule_title']")){
            int countOfCurrentSchedule = sc.getElementCount("NATIVE","xpath=//*[@id='climate_schedule_title']");
            if (countOfCurrentSchedule>0) {
                createLog("Total Number of Schedules: "+countOfCurrentSchedule);
                for (int i = 0; i < countOfCurrentSchedule; i++) {
                    click("NATIVE", "xpath=(//*[@id='climate_schedule_title'])[1]", 0, 1);
                    sc.syncElements(25000, 50000);
                    click("NATIVE", "xpath=//*[@id='climate_schedule_delete_cta']", 0, 1);
                    sc.syncElements(25000, 50000);
                    verifyElementFound("NATIVE", "xpath=//*[@content-desc='Caution']", 0);
                    verifyElementFound("NATIVE", "xpath=//*[@text='Remove Schedule']", 0);
                    verifyElementFound("NATIVE", "xpath=//*[@text='Are you sure you want to remove this climate schedule?']", 0);
                    verifyElementFound("NATIVE", "xpath=//*[@id='climate_schedule_delete_sheet_cancel_cta']", 0);
                    click("NATIVE", "xpath=//*[@text='Remove']", 0, 1);
                    createLog("Deleted schedule: "+i);
                }
            }
            sc.syncElements(25000,50000);
            verifyElementFound("NATIVE","xpath=//*[@contentDescription='Add']",0);
        }
        createLog("Completed Delete Schedules");
    }
    public static void createNewClimateSchedule(){
        createLog("Started - Add climate schedule");
        if (sc.isElementFound("NATIVE","xpath=//*[@text='Start']")) {
            click("NATIVE", "xpath=//*[@text='Start']", 0, 1);
            sc.syncElements(10000, 20000);
            click("NATIVE", "xpath=//*[@class='android.view.View' and ./*[@text='Schedule']]", 0, 1);
            sc.syncElements(10000, 20000);
        }

        if(sc.isElementFound("NATIVE","xpath=//*[@id='climate_schedule_title']")) {
            createLog("Existing schedules are displayed");
            sc.verifyElementNotFound("NATIVE","xpath=//*[contains(@content-desc,'Vehicle data is not available')]",0);
            verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_title']",0);
        } else {
            createLog("Existing schedules are not displayed");
            sc.verifyElementNotFound("NATIVE","xpath=//*[contains(@content-desc,'Vehicle data is not available')]",0);
            verifyElementFound("NATIVE","xpath=//*[contains(@text,'button below to set the day and start time you want your vehicle to begin heating or cooling')]",0);
        }

        int countOfCurrentScheduleBefore = sc.getElementCount("NATIVE","xpath=//*[@id='climate_schedule_title']");
        click("NATIVE", "xpath=//*[@contentDescription='Add']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='When you want climate settings to start']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_time_title']",0);
        click("NATIVE","xpath=//*[@id='climate_schedule_detail_time_value']",0,1);
        click("NATIVE","xpath=//*[@content-desc='1']",0,1);
        click("NATIVE","xpath=//*[@content-desc='30']",0,1);
        click("NATIVE","xpath=//*[@id='pm_label']",0,1);
        click("NATIVE","xpath=//*[@id='button1']",0,1);

        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_date_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_date_value']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_schedule_detail_day_of_week_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Mo']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Tu']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='We']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Th']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Fr']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Sa']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Su']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Climate']",0);

        click("NATIVE","xpath=//*[@text='Mo']",0,1);
        click("NATIVE","xpath=//*[@text='Th']",0,1);

        click("NATIVE","xpath=//*[@id='climate_schedule_create_cta']",0,1);
        sc.syncElements(25000,50000);

        int countOfCurrentScheduleAfter = sc.getElementCount("NATIVE","xpath=//*[@id='climate_schedule_title']");

        if (!(countOfCurrentScheduleAfter>countOfCurrentScheduleBefore)){
            createLog("CLIMATE SCHEDULE COULD NOT BE CREATED");
            fail();
        }else{
            createLog("CLIMATE SCHEDULE SUCCESSFULLY CREATED");
        }
        createLog("Started - Completed climate schedule");
    }

    public static void editClimateSchedule() {
        createLog("Started - Edit climate schedule");
        if (!sc.isElementFound("NATIVE","xpath=//*[@content-desc='Add']")) {
            reLaunchApp_android();
            verifyElementFound("NATIVE","xpath=//*[@text='Start']",0);
            click("NATIVE", "xpath=//*[@text='Start']", 0, 1);
            sc.syncElements(10000, 20000);
            click("NATIVE", "xpath=//*[@class='android.view.View' and ./*[@text='Schedule']]", 0, 1);
            sc.syncElements(10000, 20000);
        }
        verifyElementFound("NATIVE","xpath=(//*[@text='Monday,Thursday'])[1]",0);
        click("NATIVE","xpath=(//*[@text='Monday,Thursday'])[1]",0,1);
        sc.syncElements(5000,50000);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_title' and @text='Climate Schedule']",0);
        click("NATIVE","xpath=//*[@text='Tu']",0,1);
        click("NATIVE","xpath=//*[@id='climate_schedule_create_cta']",0,1);
        sc.syncElements(5000,50000);
        verifyElementFound("NATIVE","xpath=//*[@id='climate_title']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Add']",0);
        verifyElementFound("NATIVE","xpath=(//*[@text='Monday,Thursday,Tuesday'])[1]",0);

        //navigate to dashboard
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        //click("NATIVE","xpath=(//*[./*[@id='climate_title']]/child::*)[1]",0,1);
        sc.syncElements(5000,50000);
        verifyElementFound("NATIVE","xpath=//*[@id='dashboard_display_image']",0);
        createLog("Completed - Edit climate schedule");
    }
}