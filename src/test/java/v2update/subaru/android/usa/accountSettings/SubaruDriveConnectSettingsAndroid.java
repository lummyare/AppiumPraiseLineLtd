package v2update.subaru.android.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDriveConnectSettingsAndroid extends SeeTestKeywords {
    String testName = " Drive Connect Settings - Android";

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
    public void driveConnectSettingsTest() {
        sc.startStepsGroup("Test - Accounts -> Drive Connect Settings Screen");
        driveConnectSettingsScreen();
        sc.stopStepsGroup();
    }

    public static void driveConnectSettingsScreen() {
        createLog("Started : Verifying Drive Connect Settings screen in Accounts");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']")) {
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@text='Account']", 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account' or @text='ACCOUNT']", 0);
        sc.swipe("Down", sc.p2cy(70), 3000);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[(@text='Drive Connect Settings' or @text='DRIVE CONNECT SETTINGS') and @onScreen='true']", 0, 1000, 1, false);
        verifyElementFound("NATIVE","xpath=//*[(@text='Drive Connect Settings' or @text='DRIVE CONNECT SETTINGS') and @onScreen='true']",0);
        //verify icon
        verifyElementFound("NATIVE","xpath=//*[(@content-desc='Drive Connect Settings' or @content-desc='DRIVE CONNECT SETTINGS') and @id='iv_va_settings']",0);
        click("NATIVE", "xpath=//*[(@text='Drive Connect Settings' or @text='DRIVE CONNECT SETTINGS') and @onScreen='true']", 0, 1);
        sc.syncElements(5000, 60000);
        //Drive connect settings screen
        verifyElementFound("NATIVE","xpath=//*[@text='Drive Connect Settings' or @text='DRIVE CONNECT SETTINGS']",0);
        //verify title, info, subtext
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Control which Drive Connect Intelligent notifications you') and contains(@text,'d like to receive in your vehicle')]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='The Intelligent Assistant and Cloud Navigation services can learn your routines and habits to send alerts to your vehicle.']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='(Requires a compatible vehicle with an active Drive Connect subscription)']"),0);

        //push notifications
        sc.swipe("Down", sc.p2cy(70), 3000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Allow Notifications*']"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='These preferences will apply to all Drive Connect enabled vehicles.']",0);

        //Get Allow notification switch value
        boolean allowNotificationSwitchBln = isSwitchEnabled("Allow Notifications", "//*[@id='va_allow_notifications_switch']");
        if(allowNotificationSwitchBln) {
            createLog("Allow Notification switch is enabled - verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are displayed");
            verifyElementFound("NATIVE","xpath=//*[@text='Cloud Navigation']",0);
            verifyElementFound("NATIVE","xpath=//*[@text='Maintenance & Fuel']",0);
            verifyElementFound("NATIVE","xpath=//*[@text='Weather']",0);

            //disable switch and verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are not displayed
            createLog("Disabling allow notification switch");
            click("NATIVE", "xpath=//*[@id='va_allow_notifications_switch']", 0, 1);
            sc.syncElements(2000, 10000);
            boolean switchVal = isSwitchEnabled("Allow Notifications", "//*[@id='va_allow_notifications_switch']");
            if(switchVal) {
                sc.report("Verifying disabling Allow notification section switch",false);
                createErrorLog("Allow notifications switch failed - switch is still enabled");
            } else {
                createLog("Disabled Allow Notifications section switch");
                sc.report("Disabling Allow Notification section switch",true);
                sc.verifyElementNotFound("NATIVE","xpath=//*[@text='Cloud Navigation']",0);
                sc.verifyElementNotFound("NATIVE","xpath=//*[@text='Maintenance & Fuel']",0);
                sc.verifyElementNotFound("NATIVE","xpath=//*[@text='Weather']",0);
            }

            //re-enable switch and verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are displayed
            createLog("Re-enabling Allow Notification switch");
            click("NATIVE", "xpath=//*[@id='va_allow_notifications_switch']", 0, 1);
            sc.syncElements(3000, 30000);
            switchVal = isSwitchEnabled("Allow Notifications", "//*[@id='va_allow_notifications_switch']");
            if(switchVal) {
                createLog("Enabling Allow Notifications section switch");
                sc.report("Enabling Allow Notification section switch",true);
                sc.swipe("Down", sc.p2cy(70), 3000);
                verifyElementFound("NATIVE","xpath=//*[@text='Cloud Navigation']",0);
                verifyElementFound("NATIVE","xpath=//*[@text='Maintenance & Fuel']",0);
                verifyElementFound("NATIVE","xpath=//*[@text='Weather']",0);
            } else {
                sc.report("Verifying enabling Allow notification section switch",false);
                createErrorLog("Allow notifications switch failed - switch is still disabled");
            }

        } else {
            createLog("Allow Notification switch is not enabled - verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are not displayed");
            sc.verifyElementNotFound("NATIVE","xpath=//*[@text='Cloud Navigation']",0);
            sc.verifyElementNotFound("NATIVE","xpath=//*[@text='Maintenance & Fuel']",0);
            sc.verifyElementNotFound("NATIVE","xpath=//*[@text='Weather']",0);

            //enable Allow Notification switch and verify other sections(Cloud Navigation, Maintenance & Charging and Weather) are displayed
            createLog("Enabling Allow Notification switch");
            click("NATIVE", "xpath=//*[@id='va_allow_notifications_switch']", 0, 1);
            sc.syncElements(3000, 30000);
            boolean switchVal = isSwitchEnabled("Allow Notifications", "//*[@id='va_allow_notifications_switch']");
            if(switchVal) {
                createLog("Enabling Allow Notifications section switch");
                sc.report("Enabling Allow Notification section switch",true);
                sc.swipe("Down", sc.p2cy(70), 3000);
                verifyElementFound("NATIVE","xpath=//*[@text='Cloud Navigation']",0);
                verifyElementFound("NATIVE","xpath=//*[@text='Maintenance & Fuel']",0);
                verifyElementFound("NATIVE","xpath=//*[@text='Weather']",0);
            } else {
                sc.report("Verifying enabling Allow notification section switch",false);
                createErrorLog("Allow notifications switch failed - switch is still disabled");
            }
        }

        //other switches
        verifyElementFound("NATIVE","xpath=//*[@text='Cloud Navigation']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Allow your vehicle to suggest destinations and show pre-departure alerts.']",0);
        turnOnOffSwitch("Cloud Navigation","//*[@id='va_connected_navigation_switch']");

        verifyElementFound("NATIVE","xpath=//*[@text='Maintenance & Fuel']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Allow your vehicle to suggest nearby gas stations and send predictive maintenance alerts.']",0);
        turnOnOffSwitch("Maintenance & Fuel","//*[@id='va_maintenance_fuel_switch']");

        sc.swipe("Down", sc.p2cy(70), 3000);
        verifyElementFound("NATIVE","xpath=//*[@text='Weather']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Allow your vehicle to send inclement weather alerts.']",0);
        turnOnOffSwitch("Weather","//*[@id='va_weather_switch']");

        //verify disclaimer
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'* The Virtual Assistant toggle in the vehicle') and contains(@text,'s Notifications menu will also change this setting.')]"),0);

        //navigate to dashboard screen
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account' or @text='ACCOUNT']", 0);
        //click back
        click("NATIVE", "xpath=(//*[@class='android.widget.ImageButton'])[1]", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@content-desc='drag' or @content-desc='Drag']", 0, "Down");
        sc.syncElements(3000, 12000);
        verifyElementFound("NATIVE","xpath=//*[@id='dashboard_display_image']",0);

        createLog("Completed : Verifying Drive Connect Settings screen in Accounts");
    }

    //common methods
    public static boolean isSwitchEnabled(String strSection, String strSwitchXpath) {
        boolean isSwitchEnabledBln;
        createLog("Getting Switch value for section : "+strSection+" ");
        String switchVal = sc.elementGetProperty("NATIVE", strSwitchXpath, 0, "checked");
        isSwitchEnabledBln = Boolean.parseBoolean(switchVal);
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
