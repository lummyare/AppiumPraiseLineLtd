package v2update.subaru.ios.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDriveConnectIOS extends SeeTestKeywords {

    static String testName = "Drive Connect Settings - IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                testName = System.getProperty("cloudApp") + testName;
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                break;
        }
        iOS_Setup2_5(testName);
        sc.startStepsGroup("email SignIn 21MM");
        createLog("Start: 21mm email Login-USA-English");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("End: 21mm email Login-USA-English");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void driveConnectSettingsTest() {
        sc.startStepsGroup("Test - Accounts -> Drive Connect Settings Screen");
        driveConnectSettingsScreen();
        sc.stopStepsGroup();
    }

    public static void driveConnectSettingsScreen() {
        createLog("Started : Verifying Drive Connect Settings screen in Accounts");
        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Vehicle image')]")) {
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 5000);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 5000);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account Settings' or @text='ACCOUNT SETTINGS']", 0);
        sc.swipe("Down", sc.p2cy(60), 2000);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@text='Drive Connect Settings' and @onScreen='true']", 0, 1000, 1, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect Settings' and @onScreen='true']", 0);
        click("NATIVE", "xpath=//*[@text='Drive Connect Settings' and @onScreen='true']", 0, 1);
        sc.syncElements(5000, 60000);
        //Drive connect settings screen
        verifyElementFound("NATIVE","xpath=//*[@text='Drive Connect Settings' or @text='DRIVE CONNECT SETTINGS']",0);
        //verify title, info, subtext
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Control which Drive Connect Intelligent Assistant notifications you') and contains(@text,'d like to receive in your vehicle')]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='The Intelligent Assistant and Cloud Navigation services can learn your routines and habits to send alerts to your vehicle.']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='(Requires a compatible vehicle with an active Drive Connect subscription)']"),0);

        //push notifications
        sc.swipe("Down", sc.p2cy(70), 3000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Allow Notifications*' or @text='ALLOW NOTIFICATIONS*']"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='These preferences will apply to all Drive Connect enabled vehicles']",0);

        //Get Allow notification switch value
        boolean allowNotificationSwitchBln = isSwitchEnabled("Allow Notifications", convertTextToUTF8("xpath=//*[(@text='Allow Notifications*' or @text='ALLOW NOTIFICATIONS*')]/following-sibling::* [@class='UIASwitch']"));
        if(allowNotificationSwitchBln) {
            createLog("Allow Notification switch is enabled - verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are displayed");
            verifyElementFound("NATIVE","xpath=//*[(@text='Cloud Navigation' or @text='CLOUD NAVIGATION') and @class='UIAStaticText']",0);
            verifyElementFound("NATIVE",convertTextToUTF8("xpath=//*[(@text='MAINTENANCE & FUEL' or @text='Maintenance & Fuel') and @class='UIAStaticText']"),0);
            verifyElementFound("NATIVE","xpath=//*[(@text='Weather' or @text='WEATHER') and @class='UIAStaticText']",0);

            //disable switch and verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are not displayed
            createLog("Disabling allow notification switch");
            click("NATIVE", convertTextToUTF8("xpath=//*[(@text='Allow Notifications*' or @text='ALLOW NOTIFICATIONS*')]/following-sibling::* [@class='UIASwitch']"), 0, 1);
            sc.syncElements(2000, 10000);
            boolean switchVal = isSwitchEnabled("Allow Notifications", convertTextToUTF8("xpath=//*[(@text='Allow Notifications*' or @text='ALLOW NOTIFICATIONS*')]/following-sibling::* [@class='UIASwitch']"));
            if(switchVal) {
                sc.report("Verifying disabling Allow notification section switch",false);
                createErrorLog("Allow notifications switch failed - switch is still enabled");
            } else {
                createLog("Disabled Allow Notifications section switch");
                sc.report("Disabling Allow Notification section switch",true);
                sc.verifyElementNotFound("NATIVE","xpath=//*[(@text='Cloud Navigation' or @text='CLOUD NAVIGATION') and @class='UIAStaticText']",0);
                sc.verifyElementNotFound("NATIVE",convertTextToUTF8("xpath=//*[(@text='MAINTENANCE & FUEL' or @text='Maintenance & Fuel') and @class='UIAStaticText']"),0);
                sc.verifyElementNotFound("NATIVE","xpath=//*[(@text='Weather' or @text='WEATHER') and @class='UIAStaticText']",0);
            }

            //re-enable switch and verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are displayed
            createLog("Re-enabling Allow Notification switch");
            click("NATIVE", convertTextToUTF8("xpath=//*[(@text='Allow Notifications*' or @text='ALLOW NOTIFICATIONS*')]/following-sibling::* [@class='UIASwitch']"), 0, 1);
            sc.syncElements(3000, 30000);
            switchVal = isSwitchEnabled("Allow Notifications", convertTextToUTF8("xpath=//*[(@text='Allow Notifications*' or @text='ALLOW NOTIFICATIONS*')]/following-sibling::* [@class='UIASwitch']"));
            if(switchVal) {
                createLog("Enabling Allow Notifications section switch");
                sc.report("Enabling Allow Notification section switch",true);
                sc.swipe("Down", sc.p2cy(70), 3000);
                verifyElementFound("NATIVE","xpath=//*[(@text='Cloud Navigation' or @text='CLOUD NAVIGATION') and @class='UIAStaticText']",0);
                verifyElementFound("NATIVE",convertTextToUTF8("xpath=//*[(@text='MAINTENANCE & FUEL' or @text='Maintenance & Fuel') and @class='UIAStaticText']"),0);
                verifyElementFound("NATIVE","xpath=//*[(@text='Weather' or @text='WEATHER') and @class='UIAStaticText']",0);
            } else {
                sc.report("Verifying enabling Allow notification section switch",false);
                createErrorLog("Allow notifications switch failed - switch is still disabled");
            }

        } else {
            createLog("Allow Notification switch is not enabled - verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are not displayed");
            sc.verifyElementNotFound("NATIVE","xpath=//*[(@text='Cloud Navigation' or @text='CLOUD NAVIGATION') and @class='UIAStaticText']",0);
            sc.verifyElementNotFound("NATIVE",convertTextToUTF8("xpath=//*[(@text='MAINTENANCE & FUEL' or @text='Maintenance & Fuel') and @class='UIAStaticText']"),0);
            sc.verifyElementNotFound("NATIVE","xpath=//*[(@text='Weather' or @text='WEATHER') and @class='UIAStaticText']",0);

            //enable Allow Notification switch and verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are displayed
            createLog("Enabling Allow Notification switch");
            click("NATIVE", convertTextToUTF8("xpath=//*[(@text='Allow Notifications*' or @text='ALLOW NOTIFICATIONS*')]/following-sibling::* [@class='UIASwitch']"), 0, 1);
            sc.syncElements(3000, 30000);
            boolean switchVal = isSwitchEnabled("Allow Notifications", convertTextToUTF8("xpath=//*[(@text='Allow Notifications*' or @text='ALLOW NOTIFICATIONS*')]/following-sibling::* [@class='UIASwitch']"));
            if(switchVal) {
                createLog("Enabling Allow Notifications section switch");
                sc.report("Enabling Allow Notification section switch",true);
                sc.swipe("Down", sc.p2cy(70), 3000);
                verifyElementFound("NATIVE","xpath=//*[(@text='Cloud Navigation' or @text='CLOUD NAVIGATION') and @class='UIAStaticText']",0);
                verifyElementFound("NATIVE",convertTextToUTF8("xpath=//*[(@text='MAINTENANCE & FUEL' or @text='Maintenance & Fuel') and @class='UIAStaticText']"),0);
                verifyElementFound("NATIVE","xpath=//*[(@text='Weather' or @text='WEATHER') and @class='UIAStaticText']",0);
            } else {
                sc.report("Verifying enabling Allow notification section switch",false);
                createErrorLog("Allow notifications switch failed - switch is still disabled");
            }
        }

        //other switches
        verifyElementFound("NATIVE","xpath=//*[(@text='Cloud Navigation' or @text='CLOUD NAVIGATION') and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Allow your vehicle to suggest destinations and show pre-departure alerts.' or @text='ALLOW YOUR VEHICLE TO SUGGEST DESTINATIONS AND SHOW PRE-DEPARTURE ALERTS.']",0);
        turnOnOffSwitch("Cloud Navigation","xpath=//*[(@text='Cloud Navigation' or @text='CLOUD NAVIGATION') and @class='UIASwitch']");

        verifyElementFound("NATIVE",convertTextToUTF8("xpath=//*[(@text='MAINTENANCE & FUEL' or @text='Maintenance & Fuel') and @class='UIAStaticText']"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Allow your vehicle to suggest nearby gas stations and send predictive maintenance alerts.' or @text='ALLOW YOUR VEHICLE TO SUGGEST NEARBY GAS STATIONS AND SEND PREDICTIVE MAINTENANCE ALERTS.']",0);
        turnOnOffSwitch("Maintenance & Charging",convertTextToUTF8("xpath=//*[(@text='MAINTENANCE & FUEL' or @text='Maintenance & Fuel') and @class='UIASwitch']"));

        sc.swipe("Down", sc.p2cy(70), 3000);
        verifyElementFound("NATIVE","xpath=//*[(@text='Weather' or @text='WEATHER') and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Allow your vehicle to send inclement weather alerts.' or @text='ALLOW YOUR VEHICLE TO SEND INCLEMENT WEATHER ALERTS.']",0);
        turnOnOffSwitch("Weather","xpath=//*[(@text='Weather' or @text='WEATHER') and @class='UIASwitch']");

        //verify disclaimer
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'* The Virtual Assistant toggle in the vehicle') and contains(@text,'s Notifications menu will also change this setting.')]"),0);

        //navigate to dashboard screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Back']", 0);
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        sc.syncElements(3000, 12000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account Settings' or @text='ACCOUNT SETTINGS']", 0);
        sc.syncElements(3000, 12000);
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        sc.syncElements(3000, 12000);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]",0);

        createLog("Completed : Verifying Drive Connect Settings screen in Accounts");
    }

    //common methods
    public static boolean isSwitchEnabled(String strSection, String strSwitchXpath) {
        boolean isSwitchEnabledBln;
        createLog("Getting Switch value for section : "+strSection+" ");
        String switchVal = sc.elementGetProperty("NATIVE", strSwitchXpath, 0, "value");
        if(switchVal.equalsIgnoreCase("1"))
            isSwitchEnabledBln = true;
        else
            isSwitchEnabledBln = false;
        //isSwitchEnabledBln = Boolean.parseBoolean(switchVal);
        createLog("Switch boolean value for section : "+strSection+" is - "+isSwitchEnabledBln);
        return isSwitchEnabledBln;
    }

    public static void turnOnOffSwitch(String strSection, String strSwitchXpath) {
        createLog("Started : Turn ON OFF switch for section : "+strSection);
        boolean isSwitchEnabledBln = isSwitchEnabled(strSection, strSwitchXpath);
        if(isSwitchEnabledBln) {
            createLog(" "+strSection+" switch is currently enabled - disabling switch");
            //disabling switch
            click("NATIVE", strSwitchXpath, 0, 1);
            sc.syncElements(3000, 30000);
            sc.swipe("Down", sc.p2cy(70), 3000);
            boolean disabledSwitchVal = isSwitchEnabled(strSection, strSwitchXpath);
            if(disabledSwitchVal) {
                sc.report("Verifying disabling "+strSection+" section switch failed",false);
                createErrorLog(" "+strSection+"  switch failed - switch is still enabled");
            } else {
                createLog("Disabled "+strSection+" section switch success");
                sc.report("Disabling "+strSection+" section switch", true);
            }
        } else {
            createLog(" "+strSection+" switch is currently disabled - enabling switch");
            //enabling switch
            click("NATIVE", strSwitchXpath, 0, 1);
            sc.syncElements(3000, 30000);
            sc.swipe("Down", sc.p2cy(70), 3000);
            boolean enabledSwitchVal = isSwitchEnabled(strSection, strSwitchXpath);
            if(enabledSwitchVal) {
                createLog("Enabled "+strSection+" section switch success");
                sc.report("Enabled "+strSection+" section switch", true);
            } else {
                sc.report("Verifying disabling "+strSection+" section switch",false);
                createErrorLog(" "+strSection+"  switch failed - switch is still enabled");
            }
        }
        createLog("Completed : Turn ON OFF switch for section : "+strSection);
    }

}
