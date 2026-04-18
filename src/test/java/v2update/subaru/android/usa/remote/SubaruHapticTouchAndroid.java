package v2update.subaru.android.usa.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruHapticTouchAndroid extends SeeTestKeywords {
    String testName = "HapticTouch-Android";

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
    public void hapticUnlock() {
        sc.startStepsGroup("21mm Haptic Touch Unlock Command");
        hapticTouchUnlock();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void hapticLock() {
        sc.startStepsGroup("21mm Haptic Touch Lock Command");
        hapticTouchLock();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void hapticStart() {
        sc.startStepsGroup("21mm Haptic Touch Start Command");
        hapticTouchStart();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void hapticStop() {
        sc.startStepsGroup("21MM Remote Stop");
        SubaruRemote21MMEVAndroid.executeRemoteStop();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void signout() {
        sc.startStepsGroup("Sign out of the app");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void hapticTouchUnlock() {
        createLog("Started:Haptic touch unlock");
        hapticTouchAppSearch();
        sc.waitForElement("NATIVE", "xpath=//*[@text='Unlock']", 0, 4000);
        sc.click("NATIVE", "xpath=//*[@text='Unlock']", 0, 1);
        sc.report("Haptic Touch Remote Unlock sent", true);
        sc.syncElements(25000, 50000);
        android_handlePages();
        //verify Unlock notification
        android_goToNotificationsScreen();
        SubaruRemote21MMEVAndroid.remoteUnlockNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Ended:Haptic touch unlock");
    }

    public static void hapticTouchLock() {
        createLog("Started:Haptic touch Lock");
        hapticTouchAppSearch();
        sc.waitForElement("NATIVE", "xpath=//*[@text='Lock']", 0, 4000);
        sc.click("NATIVE", "xpath=//*[@text='Lock']", 0, 1);
        sc.report("Haptic Touch Remote Lock sent", true);
        sc.syncElements(25000, 50000);
        android_handlePages();
        sc.syncElements(5000, 10000);
        //verify Unlock notification
        android_goToNotificationsScreen();
        SubaruRemote21MMEVAndroid.remoteLockNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        createLog("Ended:Haptic touch Lock");
    }

    public static void hapticTouchStart() {
        createLog("Started:Haptic touch Start");
        hapticTouchAppSearch();
        sc.waitForElement("NATIVE", "xpath=//*[contains(@text,'Start')]", 0, 4000);
        sc.click("NATIVE", "xpath=//*[contains(@text,'Start')]", 0, 1);
        sc.report("Haptic Touch Remote Start sent", true);
        sc.syncElements(25000, 50000);
        android_handlePages();
        //verify Unlock notification
        android_goToNotificationsScreen();
        SubaruRemote21MMEVAndroid.remoteStartNotificationDisplayed();
        android_goToRemoteCommandsScreen();
        sc.deviceAction("Home");
        createLog("Ended:Haptic touch Start");
    }

    public static void hapticTouchAppSearch(){
        createLog("Start: Haptic touch app search ");
        sc.deviceAction("Home");
        for (int i = 1; i <= 2; i++) {
            sc.swipe("Down", sc.p2cx(40), 100);
            sc.syncElements(2000, 4000);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Search')]")) {
                break;
            }
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Search')]")) {

            click("NATIVE", "xpath=//*[contains(@text,'Search')]", 0, 1);
            sc.syncElements(2000, 4000);
            sc.sendText("subaru");
            sc.closeKeyboard();
            //((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        }
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'SUBARU') and contains(@id,'label')]", 0);
        createLog("Ended: Haptic touch app search ");
    }
}
