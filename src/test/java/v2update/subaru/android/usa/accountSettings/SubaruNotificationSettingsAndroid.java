package v2update.subaru.android.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruNotificationSettingsAndroid extends SeeTestKeywords {
    String testName = "Account Settings - Notification Settings - Android";
    public static String[] notificationsList = {"Vehicle Health Report", "Rear Seat Reminder", "Account Updates Email", "Door(s) Unlocked", "Door(s) Open", "Hatch / Trunk Open", "Hazards On", "Hood Open", "Moonroof Open", "Window(s) Open", "Electric Vehicle Alerts", "Rear Seat Reminder"};
    public static String[] vehicleAlertList = {"Door(s) Unlocked", "Door(s) Open", "Hatch / Trunk Open", "Hazards On", "Hood Open", "Moonroof Open", "Window(s) Open", "Electric Vehicle Alerts"};

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
    public void NotificationsSettingsTest() {
        sc.startStepsGroup("Notification Settings");
        notificationSettingsValidations();
        sc.stopStepsGroup();

    }
    //Test Case Description: Turn off or Disable all the notifications and verify it is disabled
    @Test
    @Order(2)
    public void DisableNotificationsTest() {
        sc.startStepsGroup("Disable Notifications");
        switchOFFNotificationsAlerts(notificationsList);
        sc.stopStepsGroup();
    }

    //Test Case Description: Turn on or Enable all the notifications and verify it is enabled
    @Test
    @Order(3)
    public void EnableNotificationsTest() {
        sc.startStepsGroup("Enable Notifications");
        switchONNotificationsAlerts(notificationsList);
        sc.stopStepsGroup();
    }

    // Test Case Description: 1.Turn off Vehicle Alerts Push Notification and check all vehicle alerts turned off
    // 2.Turn on Vehicle Alerts Push Notification and check all vehicle alerts turned off
    @Test
    @Order(4)
    public void VehicleAlertsTest() {
        sc.startStepsGroup("Enable and Disable Vehicle Alerts");
        vehicleAlertsValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(5)
    public void signOut() {
        sc.startStepsGroup(" Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleAlertsValidations(){
        createLog("Started:Vehicle Alerts Validations");
        validateVehicleAlerts();
        //Navigate back to vehicle Overview page
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.waitForElement("NATIVE", "xpath=//*[@id='iv_profile_pic']", 0, 10);
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.waitForElement("NATIVE", "xpath=//*[@content-desc='Account'] | //*[@text='Account']", 0, 10);
        sc.flickElement("NATIVE", "xpath=//*[@content-desc='Account'] | //*[@text='Account']", 0, "Down");
        createLog("Ended:Vehicle Alerts Validations");
    }

    public static void verifyPushNotificationDisplayed(String notificationName) {
        createLog("Started:Push Notifications ");
        verifyElementFound("NATIVE", "xpath=//*[@text='" + notificationName + "']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='" + notificationName + "']//following-sibling::*[@id='np_subtitle']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@text='" + notificationName + "']//following-sibling::*[text()='Push Notification']", 0, 1000, 1, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='" + notificationName + "']//following-sibling::*[text()='Push Notification']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='" + notificationName + "']//following-sibling::*[@id='np_switch_pn']", 0);
        createLog("Ended:Push Notifications ");
    }

    public static void verifyEmailNotificationDisplayed(String notificationName) {
        createLog("Started:Email Notifications ");
        verifyElementFound("NATIVE", "xpath=//*[@text='" + notificationName + "']", 0);
        if (!(notificationName.contains("Health Report")))
            verifyElementFound("NATIVE", "xpath=//*[@text='" + notificationName + "']//following-sibling::*[@id='np_subtitle']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@text='" + notificationName + "']//following-sibling::*[@id='np_switch_email']", 0, 1000, 1, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='" + notificationName + "']//following-sibling::*[text()='Email']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='" + notificationName + "']//following-sibling::*[@id='np_switch_email']", 0);
        createLog("Ended:Email Notifications ");
    }

    public static String getSwitchValue(String alert) {
        String switchValue = null;
        if (alert.contains("Email")) {
            switchValue = sc.elementGetProperty("NATIVE", "xpath=//*[@text='" + alert.split("Email")[0].trim() + "']//following-sibling::*[@id='np_switch_email']", 0, "checked");
        } else {
            swipeToElement(alert);
            switchValue = sc.elementGetProperty("NATIVE", "xpath=//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']", 0, "checked");
        }
        return switchValue;
    }

    public static String getVehicleAlertSwitchValue(String alert) {
        String switchValue = null;
        swipeToElement(alert);
        switchValue = sc.elementGetProperty("NATIVE", "xpath=//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']", 0, "enabled");

        return switchValue;
    }

    public static String enableOrDisableSwitchAndGetSwitchValue(String alert) {
        String switchValue = null;
        if (alert.contains("Email")) {
            sc.click("NATIVE", "xpath=//*[@text='" + alert.split("Email")[0].trim() + "']//following-sibling::*[@id='np_switch_email']", 0, 1);
            sc.syncElements(200, 30000);
            switchValue = sc.elementGetProperty("NATIVE", "xpath=//*[@text='" + alert.split("Email")[0].trim() + "']//following-sibling::*[@id='np_switch_email']", 0, "checked");

        } else {
            swipeToElement(alert);
            sc.click("NATIVE", "xpath=//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']", 0, 1);
            sc.syncElements(200, 30000);
            switchValue = sc.elementGetProperty("NATIVE", "xpath=//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']", 0, "checked");
        }
        return switchValue;
    }

    public static void switchOFFNotificationsAlerts(String alerts[]) {
        String switchValue = null;
        for (String alert : alerts) {
            if (getSwitchValue(alert).equalsIgnoreCase("true")) {
                switchValue = enableOrDisableSwitchAndGetSwitchValue(alert);
                sc.report(alert + " Switcher is OFF", switchValue.equalsIgnoreCase("false"));
            } else {
                sc.report(alert + " is already OFF", true);
            }

        }
    }

    public static void switchONNotificationsAlerts(String alerts[]) {
        createLog("Started:Switch On Notifications Alerts");
        String switchValue = null;
        for (String alert : alerts) {
            if (getSwitchValue(alert).equalsIgnoreCase("false")) {
                switchValue = enableOrDisableSwitchAndGetSwitchValue(alert);
                sc.report(alert + " Switcher is ON", switchValue.equalsIgnoreCase("true"));
            } else {
                sc.report(alert + " is already ON", true);
            }
        }
        createLog("Ended:Switch On Notifications Alerts");

    }

    public static void swipeToElement(String element) {
        sc.swipeWhileNotFound("Down", sc.p2cy(60), 1000, "NATIVE", "xpath=//*[@text='" + element + "']", 0, 500, 3, false);
        if (!(sc.isElementFound("NATIVE", "xpath=//*[@text='" + element + "']"))) {
            sc.swipeWhileNotFound("Up", sc.p2cy(60), 1000, "NATIVE", "xpath=//*[@text='" + element + "']", 0, 500, 3, false);
            if (!(sc.isElementFound("NATIVE", "xpath=//*[@text='" + element + "']")))
                sc.report("swipe to " + element + " failed", true);
        }

    }

    public static void validateVehicleAlerts() {
        String alert = "Vehicle Alerts", switchValue = null;
        for (int i = 1; i <= 2; i++) {
            swipeToElement(alert);
            sc.click("NATIVE", "xpath=//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']", 0, 1);
            sc.syncElements(200, 30000);
            switchValue = sc.elementGetProperty("NATIVE", "xpath=//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']", 0, "checked");
            for (String vehicleAlert : vehicleAlertList) {
                if (getVehicleAlertSwitchValue(vehicleAlert).equalsIgnoreCase(switchValue)) {
                    sc.report("Vehicle alert " + vehicleAlert + " is " + switchValue, true);
                } else {
                    sc.report("Vehicle alert " + vehicleAlert + " is " + switchValue, false);
                }
            }

        }
    }
    public static void notificationSettingsValidations(){
        createLog("Started:Notification Settings Validations");
        if (sc.isElementFound("NATIVE","xpath=//*[@id='top_nav_profile_icon']")){
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account']", 0);
        click("NATIVE", "xpath=//*[@text='Account']", 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@id='iv_profile_pic']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='notification_settings_layout']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='iv_notification']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='notification_settings']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='arrow_notifications']", 0);

        click("NATIVE", "xpath=//*[@id='arrow_notifications']", 0, 1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='NOTIFICATION SETTINGS' or @text='Notification Settings']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Health Report']", 0);
        verifyPushNotificationDisplayed("Vehicle Health Report");

        verifyElementFound("NATIVE", "xpath=//*[@text='Rear Seat Reminder']", 0);
        verifyPushNotificationDisplayed("Rear Seat Reminder");

        verifyElementFound("NATIVE", "xpath=//*[@text='Account Updates']", 0);
        verifyEmailNotificationDisplayed("Account Updates");

        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Alerts']", 0);
        verifyPushNotificationDisplayed("Vehicle Alerts");
        createLog("Ended:Notification Settings Validations");
    }
}