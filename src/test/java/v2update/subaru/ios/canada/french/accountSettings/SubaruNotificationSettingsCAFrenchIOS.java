package v2update.subaru.ios.canada.french.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruNotificationSettingsCAFrenchIOS extends SeeTestKeywords {
    String testName = "EV - NotificationSettings EV - IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                createLog("Started: Email Login");
                testName = System.getProperty("cloudApp") + testName;
                //App Login
                iOS_Setup2_5(testName);
                selectionOfCountry_IOS("USA");
                sc.startStepsGroup("email SignIn EV");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin("subarunextgen3@gmail.com", "Test$123456");
                createLog("Ended: Email Login");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                iOS_Setup2_5(testName);
                sc.startStepsGroup("email SignIn EV");
                selectionOfCountry_IOS("USA");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin("subarunextgen3@gmail.com","Test$123456");
                sc.stopStepsGroup();
        }
    }
    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }
    @Test
    @Order(1)
    public void notificationSettingsTest() {
        sc.startStepsGroup("Test - Notification Settings");
        notificationSettings();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void turnOffNotificationTest() {
        sc.startStepsGroup("Test - Turn Off Notification");
        turnOffNotification();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void turnOnNotificationTest() {
        sc.startStepsGroup("Test - Turn On Notification");
        turnOnNotification();
        sc.stopStepsGroup();
    }
    @Test
    @Order(4)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void notificationSettings() {
        createLog("Started : Notification Settings");
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(4000, 20000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_NOTIFICATION_SETTINGS_CELL']", 0, 1);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String expectedText[];
        switch (strAppType.toLowerCase()) {
            case ("lexus"):
                expectedText = new String[]{"NOTIFICATION SETTINGS", "Vehicle Health Report", "We'd love the chance to customize your experience. Consider the options below that let you choose what type of messages you receive.",
                        "PUSH NOTIFICATION", "EMAIL", "Account Updates", "App profile, password, vehicle nicknames, and other account changes.", "Rear Seat Reminder", "Receive a reminder to check if passengers or objects remain in the vehicle.",
                        "Vehicle Alerts", "Vehicle Status Alerts send notifications after the vehicle is turned off. You can turn your notifications on or off to customize the information you receive.", "DOOR(S) UNLOCKED", "DOOR(S) OPEN",
                        "HATCH / TRUNK OPEN", "HAZARDS ON", "HOOD OPEN", "MOONROOF OPEN", "WINDOW(S) OPEN", "ELECTRIC VEHICLE ALERTS", "PREDICTIVE ALERTS"};
                for (String detailsName : expectedText) {
                    if (actualText.contains(detailsName)) {
                        sc.report("Validation of " + detailsName, true);
                    } else {
                        sc.report("Validation of " + detailsName, false);
                    }
                }
                break;
            case ("toyota"):
                expectedText = new String[]{"Notification Settings", "Vehicle Health Report", "Push Notification", "Email", "Account Updates", "App profile, password, vehicle nicknames, and other account changes.", "Rear Seat Reminder",
                        "Receive maintenance updates and vehicle health reports by choosing the type of message you want to receive", "Receive a reminder to check if passengers or objects remain in the vehicle.", "Vehicle Alerts", "Vehicle Status Alerts send notifications after the vehicle is turned off. You can turn your notifications on or off to customize the information you receive.",
                        "Door(s) Unlocked", "Door(s) Open", "Hatch / Trunk Open", "Hazards On", "Hood Open", "Moonroof Open", "Window(s) Open", "Electric Vehicle Alerts", "Predictive Alerts"};
                for (String detailsName : expectedText) {
                    if (actualText.contains(detailsName)) {
                        sc.report("Validation of " + detailsName, true);
                    } else {
                        sc.report("Validation of " + detailsName, false);
                    }
                }
                break;
        }
        sc.swipeWhileNotFound("Up", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='Vehicle Health Report' and @class='UIAStaticText' and @visible='true']", 0, 1000, 5, false);
        createLog("Completed : Notification Settings");
    }

    public static void turnOffNotification() {
        createLog("Started : Turn OFF Notification");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Notification Settings' or @text='NOTIFICATION SETTINGS']")) {
            navigateToNotificationSettingsScreen();
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Notification Settings' or @text='NOTIFICATION SETTINGS']", 0);
        //Vehicle Health Report - Push Notification
        String status = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='PUSH NOTIFICATION' or @accessibilityLabel='Push Notification'] and ./parent::*[./parent::*[@accessibilityLabel='Vehicle Health Report']]]", 0);
        if (status.equalsIgnoreCase("On")) {
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='PUSH NOTIFICATION' or @accessibilityLabel='Push Notification'] and ./parent::*[./parent::*[@accessibilityLabel='Vehicle Health Report']]]", 0, 1);
        }
        //Vehicle Health Report - Email
        status = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='EMAIL' or @accessibilityLabel='Email'] and ./parent::*[./parent::*[@accessibilityLabel='Vehicle Health Report']]] | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Vehicle Health Report']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0);
        if (status.equalsIgnoreCase("On")) {
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='EMAIL' or @accessibilityLabel='Email'] and ./parent::*[./parent::*[@accessibilityLabel='Vehicle Health Report']]] | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Vehicle Health Report']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0, 1);
        }
        //Rear Seat Reminder - Push Notification
        status = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='PUSH NOTIFICATION' or @accessibilityLabel='Push Notification'] and ./parent::*[./parent::*[@accessibilityLabel='Rear Seat Reminder']]] | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Rear Seat Reminder']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0);
        if (status.equalsIgnoreCase("On")) {
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='PUSH NOTIFICATION' or @accessibilityLabel='Push Notification'] and ./parent::*[./parent::*[@accessibilityLabel='Rear Seat Reminder']]] | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Rear Seat Reminder']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0, 1);
        }
        //Account Updates - Email
        status = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='EMAIL' or @accessibilityLabel='Email'] and ./parent::*[./parent::*[@accessibilityLabel='Account Updates']]] | //*[@text='Account Updates' and @class='UIAStaticText']/following-sibling::*[@class='UIAView' and ./*[@text='EMAIL']]/*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0);
        if (status.equalsIgnoreCase("On")) {
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='EMAIL' or @accessibilityLabel='Email'] and ./parent::*[./parent::*[@accessibilityLabel='Account Updates']]] | //*[@text='Account Updates' and @class='UIAStaticText']/following-sibling::*[@class='UIAView' and ./*[@text='EMAIL']]/*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0, 1);
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(20), 5000, "NATIVE", "xpath=//*[@class='UIAView' and ./*[@text='PREDICTIVE ALERTS']]", 0, 1000, 1, false);
        String vehicleAlerts[];

        vehicleAlerts = new String[]{"DOOR(S) UNLOCKED", "DOOR(S) OPEN", "HATCH / TRUNK OPEN", "HAZARDS ON", "HOOD OPEN", "MOONROOF OPEN",
                "WINDOW(S) OPEN", "ELECTRIC VEHICLE ALERTS", "PREDICTIVE ALERTS"};
        switchOFFNotificationsAlerts(vehicleAlerts);

        sc.swipeWhileNotFound("Up", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='Vehicle Health Report' and @class='UIAStaticText' and @visible='true']", 0, 1000, 5, false);
        createLog("Completed : Turn OFF Notification");
    }

    public static void turnOnNotification() {
        createLog("Started : Turn ON Notification");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Notification Settings' or @text='NOTIFICATION SETTINGS']")) {
            navigateToNotificationSettingsScreen();
        }

        verifyElementFound("NATIVE", "xpath=//*[@text='Notification Settings' or @text='NOTIFICATION SETTINGS']", 0);
        sc.syncElements(5000,30000);
        //Vehicle Health Report - Push Notification
        String status = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='PUSH NOTIFICATION' or @accessibilityLabel='Push Notification'] and ./parent::*[./parent::*[@accessibilityLabel='Vehicle Health Report']]] | | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Vehicle Health Report']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0);
        if (status.equalsIgnoreCase("On")) {
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='PUSH NOTIFICATION' or @accessibilityLabel='Push Notification'] and ./parent::*[./parent::*[@accessibilityLabel='Vehicle Health Report']]] | | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Vehicle Health Report']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0, 1);
        }
        //Vehicle Health Report - Email
        status = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='EMAIL' or @accessibilityLabel='Email'] and ./parent::*[./parent::*[@accessibilityLabel='Vehicle Health Report']]] | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Vehicle Health Report']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0);
        if (status.equalsIgnoreCase("On")) {
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='EMAIL' or @accessibilityLabel='Email'] and ./parent::*[./parent::*[@accessibilityLabel='Vehicle Health Report']]] | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Vehicle Health Report']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0, 1);
        }
        //Rear Seat Reminder - Push Notification
        status = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='PUSH NOTIFICATION' or @accessibilityLabel='Push Notification'] and ./parent::*[./parent::*[@accessibilityLabel='Rear Seat Reminder']]] | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Rear Seat Reminder']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0);
        if (status.equalsIgnoreCase("On")) {
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='PUSH NOTIFICATION' or @accessibilityLabel='Push Notification'] and ./parent::*[./parent::*[@accessibilityLabel='Rear Seat Reminder']]] | //*[@text='PUSH NOTIFICATION' and ./parent::*[./parent::*[@text='Rear Seat Reminder']]]/following-sibling::*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0, 1);
        }
        //Account Updates - Email
        status = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='EMAIL' or @accessibilityLabel='Email'] and ./parent::*[./parent::*[@accessibilityLabel='Account Updates']]] | //*[@text='Account Updates' and @class='UIAStaticText']/following-sibling::*[@class='UIAView' and ./*[@text='EMAIL']]/*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0);
        if (status.equalsIgnoreCase("On")) {
            sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='EMAIL' or @accessibilityLabel='Email'] and ./parent::*[./parent::*[@accessibilityLabel='Account Updates']]] | //*[@text='Account Updates' and @class='UIAStaticText']/following-sibling::*[@class='UIAView' and ./*[@text='EMAIL']]/*[@id='SETTINGS_PAGE_TOGGLE_SWITCH']", 0, 1);
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(20), 5000, "NATIVE", "xpath=//*[@class='UIAView' and ./*[@text='PREDICTIVE ALERTS']]", 0, 1000, 1, false);
        String vehicleAlerts[];

        vehicleAlerts = new String[]{"DOOR(S) UNLOCKED", "DOOR(S) OPEN", "HATCH / TRUNK OPEN", "HAZARDS ON", "HOOD OPEN", "MOONROOF OPEN",
                "WINDOW(S) OPEN", "ELECTRIC VEHICLE ALERTS", "PREDICTIVE ALERTS"};
        switchOnNotificationsAlerts(vehicleAlerts);

        click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
        sc.syncElements(5000,30000);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,30000);
        //Click close in accounts
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed : Turn ON Notification");
    }

    public static void switchOFFNotificationsAlerts(String alerts[]) {
        String switchValue = null;
        for (String alert : alerts) {
            sc.swipeWhileNotFound("Down", sc.p2cy(20), 5000, "NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='REAR SEAT REMINDER' or @accessibilityLabel='Rear Seat Reminder']]", 0, 1000, 1, false);
            switchValue = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + alert + "']]", 0);
            if (switchValue.equalsIgnoreCase("On")) {
                sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + alert + "']]", 0, 1);
                sc.swipe("Down", sc.p2cy(20), 5000);
                switchValue = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + alert + "']]", 0);
                sc.report(alert + " status is OFF", switchValue.equalsIgnoreCase("Off"));
            } else {
                sc.report(alert + " is already OFF", true);
            }
        }
    }

    public static void switchOnNotificationsAlerts(String alerts[]) {
        String switchValue = null;
        for (String alert : alerts) {
            sc.swipeWhileNotFound("Down", sc.p2cy(20), 5000, "NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='REAR SEAT REMINDER' or @accessibilityLabel='Rear Seat Reminder']]", 0, 1000, 1, false);
            switchValue = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + alert + "']]", 0);
            if (switchValue.equalsIgnoreCase("Off")) {
                sc.click("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + alert + "']]", 0, 1);
                sc.swipe("Down", sc.p2cy(20), 5000);
                switchValue = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='SETTINGS_PAGE_TOGGLE_SWITCH' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + alert + "']]", 0);
                sc.report(alert + " status is ON", switchValue.equalsIgnoreCase("On"));
            } else {
                sc.report(alert + " is already ON", true);
            }
        }
    }

    public static void navigateToNotificationSettingsScreen() {
        createLog("Started: navigating to notification settings screen");
        reLaunchApp_iOS();
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(4000, 20000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_NOTIFICATION_SETTINGS_CELL']", 0, 1);
        sc.syncElements(2000, 10000);
        createLog("Completed: navigating to notification settings screen");
    }
}
