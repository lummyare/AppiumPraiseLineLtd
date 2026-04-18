package v2update.subaru.ios.usa.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruHapticTouchIOS extends SeeTestKeywords {
    String testName = " - SubaruHapticTouch-IOS";

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
    public void hapticUnlock() {
        sc.startStepsGroup("Subaru Haptic Touch Unlock");
        clearPNS();
        appFinderHapticTouch();
        executeHapticTouchUnLock();
        PNS("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void hapticLock() {
        sc.startStepsGroup("Subaru Haptic Touch Lock");
        appFinderHapticTouch();
        executeHapticTouchLock();
        PNS("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void hapticStart() {
        sc.startStepsGroup("Subaru Haptic Touch Start");
        appFinderHapticTouch();
        executeHapticTouchStart();
        PNS("Start");
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void hapticStop() {
        sc.startStepsGroup("Subaru Remote Stop");
        executeRemoteStop();
        PNS("Stop");
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void SignOut() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void executeHapticTouchUnLock() {
        createLog("Started - Subaru Haptic Touch Unlock");
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Unlock']"))
            sc.click("NATIVE", "xpath=//*[@label='Unlock']", 0, 1);
        sc.report("Haptic Touch Unlock request Sent", true);
        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Unlocking");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();

        //verify remote unlock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now unlocked","already unlocked","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Unlock");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");

        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Subaru Haptic Touch Unlock");
    }

    public static void executeHapticTouchLock() {
        createLog("Started - Subaru Haptic Touch Lock");
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Lock']"))
            sc.click("NATIVE", "xpath=//*[@label='Lock']", 0, 1);
        sc.report("Haptic Touch Lock request Sent", true);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Locking");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();

        //verify remote lock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now locked","already locked", "Your request could not be completed because a keyfob was detected in your vehicle","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started", "Lock");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Subaru Haptic Touch Lock");
    }

    public static void executeHapticTouchStart() {
        createLog("Started - Subaru Haptic Touch Start");
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Start']"))
            sc.click("NATIVE", "xpath=//*[@label='Start']", 0, 1);
        sc.report("Haptic Touch Start request Sent", true);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Starting");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();

        //verify remote start notification is displayed
        remoteCommandNotificationVerification("Vehicle was started and will automatically shut off in 10 minutes","minute cycles without an occupant","keyfob was not left inside your vehicle","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Start");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Subaru Haptic Touch Start");
    }

    public static void executeRemoteStop() {
        createLog("Started - Subaru Remote Stop");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_engine_stop_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_engine_stop_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Stopping");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();

        //verify remote stop notification is displayed
        remoteCommandNotificationVerification("Remote Engine Stop Request was successful","Request was cancelled because engine was not started from app","Please ensure your vehicle is located in an area with cellular network coverage. If your vehicle has not been driven in more than 7 days, commands could fail until the vehicle is started","Stop");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Subaru Remote Stop");
    }

    public static void appFinderHapticTouch() {
        createLog("Started - App Finder and Haptic Touch");
        sc.deviceAction("Home");
        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
        sc.closeAllApplications();
        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case ("subarustage"):
                createLog("Subaru Stage");
                sc.swipeWhileNotFound("Right", sc.p2cx(35), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']", 0, 1000, 10, false);
                if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']"))
                    sc.longClick("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']", 0, 1, 0, 0);
                break;
            case ("subaru"):
                createLog("Subaru Prod");
                sc.swipeWhileNotFound("Right", sc.p2cx(35), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='SUBARU']", 0, 1000, 10, false);
                if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU']"))
                    sc.longClick("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU']", 0, 1, 0, 0);
                break;
            case ("subaruappstore"):
                createLog("Subaru App Store");
                sc.swipeWhileNotFound("Right", sc.p2cx(35), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='SUBARU']", 0, 1000, 10, false);
                if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU']"))
                    sc.longClick("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU']", 0, 1, 0, 0);
                break;
            case (""):
                if(System.getProperty("cloudApp").toLowerCase().contains("subaru")) {
                    sc.swipeWhileNotFound("Right", sc.p2cx(35), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']", 0, 1000, 10, false);
                    sc.syncElements(2000, 4000);
                    if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']"))
                        sc.longClick("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']", 0, 1, 0, 0);
                    break;
                }
        }
        createLog("Completed - App Finder and Haptic Touch");
    }
}
