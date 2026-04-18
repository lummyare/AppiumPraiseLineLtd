package v2update.subaru.ios.canada.french.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static v2update.subaru.ios.canada.french.remote.SubaruRemoteEVCAFrenchIOS.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruHapticTouchCAFrenchIOS extends SeeTestKeywords {
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
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLoginFrench("subaruprod2_21mm@mail.tmnact.io","Test@123");
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
        PNSFrench("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void hapticLock() {
        sc.startStepsGroup("Subaru Haptic Touch Lock");
        appFinderHapticTouch();
        executeHapticTouchLock();
        PNSFrench("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void hapticStart() {
        sc.startStepsGroup("Subaru Haptic Touch Start");
        appFinderHapticTouch();
        executeHapticTouchStart();
        PNSFrench("Start");
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void hapticStop() {
        sc.startStepsGroup("Subaru Remote Stop");
        executeRemoteStop();
        PNSFrench("Stop");
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
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@label='Déverrouiller']")))
            sc.click("NATIVE", convertTextToUTF8("//*[@label='Déverrouiller']"), 0, 1);
        sc.report("Haptic Touch Unlock request Sent", true);
        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandActionFrench(convertTextToUTF8("Déverrouillage"));

        //Notifications screen navigation
        iOS_goToNotificationsScreenFrench();

        //verify remote unlock notification is displayed
        remoteCommandNotificationVerificationFrench(convertTextToUTF8("Le véhicule est maintenant déverrouillé"),convertTextToUTF8("déjà débloqué"),convertTextToUTF8("Veuillez vous assurer que votre véhicule est situé dans une zone desservie par un réseau cellulaire. Si votre véhicule na pas été conduit depuis plus de 7 jours"),"","Unlock");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Compte']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");

        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Subaru Haptic Touch Unlock");
    }

    public static void executeHapticTouchLock() {
        createLog("Started - Subaru Haptic Touch Lock");
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Verrouiller']"))
            sc.click("NATIVE", "xpath=//*[@label='Verrouiller']", 0, 1);
        sc.report("Haptic Touch Lock request Sent", true);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandActionFrench(convertTextToUTF8("Verrouillage"));

        //Notifications screen navigation
        iOS_goToNotificationsScreenFrench();

        //verify remote lock notification is displayed
        remoteCommandNotificationVerificationFrench(convertTextToUTF8("Le véhicule est maintenant verrouillé"),convertTextToUTF8("déjà verrouillé"), convertTextToUTF8("Your request could not be completed because a keyfob was detected in your vehicle"),convertTextToUTF8("Veuillez vous assurer que votre véhicule est situé dans une zone desservie par un réseau cellulaire. Si votre véhicule na pas été conduit depuis plus de 7 jours"), "Lock");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Compte']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Subaru Haptic Touch Lock");
    }

    public static void executeHapticTouchStart() {
        createLog("Started - Subaru Haptic Touch Start");
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@label='Démarrer']")))
            sc.click("NATIVE", convertTextToUTF8("//*[@label='Démarrer']"), 0, 1);
        sc.report("Haptic Touch Start request Sent", true);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandActionFrench(convertTextToUTF8("Démarrage"));

        //Notifications screen navigation
        iOS_goToNotificationsScreenFrench();

        //verify remote start notification is displayed
        remoteCommandNotificationVerificationFrench(convertTextToUTF8("Le véhicule a démarré et s'éteindra automatiquement dans 20 minutes"),convertTextToUTF8("sera désactivée si le moteur tourne pendant deux cycles"),convertTextToUTF8("keyfob was not left inside your vehicle"),convertTextToUTF8("Veuillez vous assurer que votre véhicule est situé dans une zone desservie par un réseau cellulaire. Si votre véhicule na pas été conduit depuis plus de 7 jours"),"Start");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Compte']",0);
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
        verify_iOS_remoteCommandActionFrench(convertTextToUTF8("Arrêt"));

        //Notifications screen navigation
        iOS_goToNotificationsScreenFrench();

        //verify remote stop notification is displayed
        remoteCommandNotificationVerificationFrench(convertTextToUTF8("La demande d'arrêt du moteur à distance a réussi"),convertTextToUTF8("Request was cancelled because engine was not started from app"),convertTextToUTF8("Veuillez vous assurer que votre véhicule est situé dans une zone desservie par un réseau cellulaire. Si votre véhicule na pas été conduit depuis plus de 7 jours"),"","Stop");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Compte']",0);
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
