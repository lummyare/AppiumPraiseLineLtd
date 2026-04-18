package v2update.subaru.android.usa.deepLinksAndroid;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDeeplinksAndroid extends SeeTestKeywords {
    String testName = "DeepLinks-Android";
    static boolean deepLinkPermission = false;
    //Check if non 21MM Deeplinks applies for subaru

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
        if (isStageApp) {
            createLog("App center Stage Login");
            android_emailLoginIn("subarustage_21mm@mail.tmnact.io","Toyo213$$");
        }
        else {
            createLog("App center Prod Login");
            android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        }
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void existingAccountTest() {
        sc.startStepsGroup("Existing Account DeepLink");
        deepLink("url/existing-account");
        verifyDashboardScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void manageSubscriptionTest() {
        sc.startStepsGroup("Manage Subscription DeepLink");
        deepLink("url/manage-subscriptions");
        verifyManageSubscriptionScreen();
        sc.stopStepsGroup();
    }

    //https://toyotaconnected.atlassian.net/browse/OAD01-15640 - defect V2.5
    @Test
    @Order(3)
    public void manageProfileTest() {
        sc.startStepsGroup("Manage Profile DeepLink");
        deepLink("url/manage-profile");
        verifyManageProfileScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void lastParkedTest() {
        sc.startStepsGroup("Last Parked DeepLink");
        deepLink("url/last-parked");
        verifyLastParkedLocationScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void guestDriverTest() {
        sc.startStepsGroup("Guest Driver DeepLink");
        deepLink("url/guest-driver");
        verifyGuestDriverScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void remoteTest() {
        sc.startStepsGroup("Remote DeepLink");
        deepLink("url/remote");
        verifyDashboardScreen();
        sc.stopStepsGroup();
    }

    //21MM
    @Test
    @Order(7)
    public void smsRegistration21MM() {
        sc.startStepsGroup("Test - 21MM sms registration DeepLink");
        deepLink("url/sms_registration");
        verifySmsRegistration21MMScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void smsLinkAccounts21MM() {
        sc.startStepsGroup("Test - 21MM sms link accounts DeepLink");
        deepLink("url/sms_linkaccounts");
        verifySmsLinkAccounts21MMScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void smsSubscription21MM() {
        sc.startStepsGroup("Test - 21MM sms subscription DeepLink");
        deepLink("url/sms_subscription");
        verifySmsSubscription21MMScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    public void smsPinReset21MM() {
        sc.startStepsGroup("Test - 21MM sms pinreset DeepLink");
        deepLink("url/sms_pinreset");
        verifySmsPinReset21MMScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    public void appReLoginTest() {
        sc.startStepsGroup("Test - Launch Manage Subscription Deeplink, login to app and verify Subscription screen");
        deepLink("url/manage-subscriptions");
        Android_handlepopups();
        android_keepMeSignedIn(true);
        isDeepLinksTest = true;
        android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        verifyManageSubscriptionScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    @Disabled
    public void signOut() {
        sc.startStepsGroup("Relogin - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void deepLink(String deepLinkUrl) {
        sc.closeAllApplications();
        sc.hybridClearCache();
        String deepLink = "";
        strAppType = strAppType.toLowerCase();

        if(isStageApp) {
            createLog("App center stage app");
            //example : https://subaru.test-app.link/manage-subscriptions
            deepLink = deepLinkUrl.replace("url", "https://" + strAppType + ".test-app.link");
        } else {
            createLog("App center prod app");
            //example : https://subaru.link/manage-subscriptions
            deepLink = deepLinkUrl.replace("url", "https://" + strAppType + ".app.link");
        }

        createLog("deep link is: " + deepLink);
        sc.deviceAction("Home");
        sc.syncElements(5000, 30000);

        sc.swipe("down", sc.p2cx(80), 100);
        verifyElementFound("NATIVE", "xpath=//*[@id='app_search_edit_text']", 0);
        click("NATIVE", "xpath=//*[@id='app_search_edit_text']", 0, 1);
        sc.sendText("Internet");
        verifyElementFound("NATIVE", "xpath=//*[(@id='app_label' and @content-desc='Internet') or (@id='label' and @text='Internet')]/preceding-sibling::*[@id='app_icon' or @id='icon']", 0);
        click("NATIVE", "xpath=//*[(@id='app_label' and @content-desc='Internet') or (@id='label' and @text='Internet')]/preceding-sibling::*[@id='app_icon' or @id='icon']", 0, 1);
        sc.syncElements(10000, 30000);
        //browser update and default app popup
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Update Samsung Internet?']"))
            click("NATIVE", "xpath=//*[@text='Cancel']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Updates to Samsung Internet Privacy Notice']"))
            click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Set Samsung Internet as the default browser?' or @text='Set as default']"))
            click("NATIVE", "xpath=//*[@text='Set' or @text='Set as default']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='location_bar_edit_text']"))
            click("NATIVE", "xpath=//*[@id='location_bar_edit_text']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='toolbar_delete_url']"))
            click("NATIVE", "xpath=//*[@id='toolbar_delete_url']", 0, 1);
        sc.sendText(deepLink);
        click("NATIVE", "xpath=//*[@content-desc='Go']", 0, 1);
        sc.syncElements(5000, 30000);

        //app center - so relaunch
        createLog("AppCenter app");
        deeplinks_ReLaunchApp_android();

        sc.syncElements(5000, 30000);

        //Verified Links
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='alertTitle' and (@text='Verified Links' or @text='Enlaces verificados' or @text='Liens vérifiés')]")) {
            click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
            createLog("Permissions set: " + deepLinkPermission);
            if (deepLinkPermission) {
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            } else {setDeepLinkPermission();}
        }
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Apps that can open links']")) {
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(2000, 4000);
        }
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Please allow Display over other apps setting')]",0)){
            click("NATIVE", "xpath=//*[@text='Cancel' and contains(@id,'button')]", 0, 1);
            sc.syncElements(1000, 2000);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
            click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']"))
            click("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Tap on the account icon to explore the Take a Tour option.']"))
            click("NATIVE", "xpath=//*[@text='OK'] | //*[@text='Ok']", 0, 1);
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
            sc.click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifyManageSubscriptionScreen() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Subscriptions']", 0);
        click("NATIVE", "xpath=//*[@id='subscription_back_button']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifyManageProfileScreen() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='PERSONAL DETAILS' or @text='Personal Details']", 0);
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifyDashboardScreen() {
        //Verify Dashboard texts
        delay(5000);
        sc.syncElements(5000, 50000);
        String descValues[] = sc.getAllValues("NATIVE", "xpath=//*[@class='android.view.View' and @content-desc]", "content-desc");
        String actualText="";
        String[] expectedText;
        for (String value : descValues) {
            actualText = actualText.concat(value);
        }
        createLog(actualText);
        expectedText = new String[]{"tap to open vehicle switcher", "Dashboard Vehicle Image", "Lock", "Start", "Unlock", "Service", "Pay", "Shop", "Find","Home Tab"};
        for (String strText : expectedText) {
            if (actualText.contains(strText)) {
                sc.report("Validation of " + strText, true);
            } else {
                sc.report("Validation of " + strText, false);
            }
        }
        sc.syncElements(5000, 30000);
    }

    public void verifyLastParkedLocationScreen() {
        sc.syncElements(5000, 30000);
        //Destinations section
        verifyElementFound("NATIVE", "xpath=//*[@text='Last Parked Location']", 0);
        // Click vehicle icon to go back to dashboard page
        click("NATIVE", "xpath=//*[@contentDescription='Home Tab']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifyGuestDriverScreen() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Guest Drivers']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Share Remote']", 0);
        click("NATIVE", "xpath=//*[@contentDescription='back_button']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    //21MM
    public static void verifySmsRegistration21MMScreen() {
        sc.syncElements(5000, 30000);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'take pictures and record video')]"))
            sc.click("NATIVE", "xpath=//*[@text='While using the app']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Inside your vehicle, hold your smartphone camera over the QR code on the multimedia screen to start the scanning process.' or @text='INSIDE YOUR VEHICLE, HOLD YOUR SMARTPHONE CAMERA OVER THE QR CODE ON THE MULTIMEDIA SCREEN TO START THE SCANNING PROCESS.']", 0);
        click("NATIVE", "xpath=//*[@id='bt_close' and @class='android.widget.ImageView']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public static void verifySmsLinkAccounts21MMScreen() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Linked Accounts' or @text='LINKED ACCOUNTS']", 0);
        String descValues[] = sc.getAllValues("NATIVE", "xpath=//*[@class='android.widget.TextView']", "text");
        String actualText="";
        String[] expectedText;
        for (String value : descValues) {
            actualText = actualText.concat(value);
        }
        createLog(actualText);
        expectedText = new String[]{"Music Services", "Enjoy streaming your audio in any supported vehicle using your linked accounts", "Apple Music", "Amazon Music"};
        for (String strText : expectedText) {
            if (actualText.contains(strText)) {
                sc.report("Validation of " + strText, true);
            } else {
                sc.report("Validation of " + strText, false);
            }
        }
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public static void verifySmsSubscription21MMScreen() {
        sc.syncElements(20000, 40000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Announcements']"))
            sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']") && sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Back']"))
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        sc.syncElements(20000, 40000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Subscriptions']", 0);
        click("NATIVE", "xpath=//*[@id='subscription_back_button']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public static void verifySmsPinReset21MMScreen() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Set PIN' or @text='SET PIN']", 0);
        String descValues[] = sc.getAllValues("NATIVE", "xpath=//*[@class='android.widget.TextView']", "text");
        String actualText="";
        String[] expectedText;
        for (String value : descValues) {
            actualText = actualText.concat(value);
        }
        createLog(actualText);
        expectedText = new String[]{"Create PIN", "Please create a 6 digit PIN to access your profile on Drive Connect capable vehicles."};
        for (String strText : expectedText) {
            if (actualText.contains(strText)) {
                sc.report("Validation of " + strText, true);
            } else {
                sc.report("Validation of " + strText, false);
            }
        }
        sc.closeKeyboard();
        //clicking on Back
        click("NATIVE","xpath=//*[@class='android.widget.ImageButton' and @knownSuperClass='android.widget.ImageButton']",0,1);
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        sc.syncElements(5000, 30000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Announcements']"))
            sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
    }

    public static void deeplinks_ReLaunchApp_android() {
        createLog("Relaunching App");
        String appPackage = "";
        ConfigSingleton.INSTANCE.loadConfigProperties();
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case ("subaru"):
                appPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                break;
            case ("subarustage"):
                appPackage = ConfigSingleton.configMap.get("appPackageStageSubaru");
                break;
            case ("subaruplaystore"):
                createLog("Launch Android Subaru In Market app");
                appPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                break;
            case (""):
                if (System.getProperty("cloudApp").equalsIgnoreCase("subaru")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                    break;
                }
                else if (System.getProperty("cloudApp").equalsIgnoreCase("subaruplaystore")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                    break;
                }
                else if (System.getProperty("cloudApp").equalsIgnoreCase("subarustage")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageStageSubaru");
                    break;
                }
                break;
            default:
                createLog("Kill and Relaunch failed");
        }
        sc.deviceAction("Home");
        sc.closeAllApplications();
        sc.launch(appPackage, false, false);
        sc.setProperty("element.visibility.level", "full");
        sc.syncElements(5000, 30000);
        Android_handlepopups();
    }

    public static void setDeepLinkPermission() {
        createLog("Setting deeplink permissions for Android");
        sc.swipeWhileNotFound("DOWN",sc.p2cy(50),2000,"NATIVE","xpath=//*[@text='SUBARU SOLTERRA CONNECT']",0,1000,10,false);
        sc.click("NATIVE","xpath=//*[@text='SUBARU SOLTERRA CONNECT']",0,1);
        sc.syncElements(2000,5000);
        sc.click("NATIVE","xpath=//*[@text='Supported web addresses']",0,1);
        sc.syncElements(2000,5000);
        for (int i = 1 ; i<4 ; i++) {
            if (sc.isElementFound("NATIVE","xpath=(//*[@id='switch_widget' and @checked = 'false'])["+i+"]")) {
                sc.click("NATIVE","xpath=(//*[@id='switch_widget' and @checked = 'false'])["+i+"]",0,1);
            }
        }
        deepLinkPermission = true;
        createLog("deeplink permissions enabled");
    }
}
