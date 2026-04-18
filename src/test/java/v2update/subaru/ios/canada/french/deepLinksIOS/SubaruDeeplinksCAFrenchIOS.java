package v2update.subaru.ios.canada.french.deepLinksIOS;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDeeplinksCAFrenchIOS extends SeeTestKeywords {
    static String testName = " - SubaruDeepLinksIOS-IOS";
    String actualText = "";
    String[] expectedText;

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
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        if (isStageApp) {
            createLog("App center Stage Login");
            environmentSelection_iOS("stage");
            ios_emailLogin("subarustage_21mm@mail.tmnact.io","Toyo213$$");
        }
        else {
            createLog("App center Prod Login");
            environmentSelection_iOS("prod");
            ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        }
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

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

    @Test
    @Order(7)
    public void scheduleServiceTest() {
        sc.startStepsGroup("Schedule Service DeepLink");
        deepLink("url/schedule-service");
        verifyScheduleServiceScreen();
        sc.stopStepsGroup();
    }

    // disabled since manage-garage deep-link is no longer valid - https://toyotaconnected.atlassian.net/browse/OAD01-6254
    @Test
    @Order(8)
    @Disabled
    public void manageGarageTest() {
        sc.startStepsGroup("Manage Garage DeepLink");
        deepLink("url/manage-garage");
        sc.stopStepsGroup();
    }

    // disabled since dynamic navi deep-link is no longer valid - https://toyotaconnected.atlassian.net/browse/OAD01-6254
    @Test
    @Order(9)
    @Disabled
    public void manageDynamicNaviTest() {
        sc.startStepsGroup("Manage Dynamic Navi DeepLink");
        deepLink("url/manage-dynamicnavi");
        sc.stopStepsGroup();
    }

    //21MM
    @Test
    @Order(10)
    public void smsRegistration21MM() {
        sc.startStepsGroup("Test - 21MM sms registration DeepLink");
        deepLink("url/sms_registration");
        verifySmsRegistration21MMScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    public void smsLinkAccounts21MM() {
        sc.startStepsGroup("Test - 21MM sms link accounts DeepLink");
        deepLink("url/sms_linkaccounts");
        verifySmsLinkAccounts21MMScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    public void smsSubscription21MM() {
        sc.startStepsGroup("Test - 21MM sms subscription DeepLink");
        deepLink("url/sms_subscription");
        verifySmsSubscription21MMScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    public void smsPinReset21MM() {
        sc.startStepsGroup("Test - 21MM sms pinreset DeepLink");
        deepLink("url/sms_pinreset");
        verifySmsPinReset21MMScreen(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        sc.stopStepsGroup();
    }

    @Test
    @Order(14)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(15)
    public void appReLoginTest() {
        sc.startStepsGroup("Test - Launch Manage Subscription Deeplink, login to app and verify Subscription screen");
        deepLink("url/manage-subscriptions");
        ios_keepMeSignedIn(true);
        isDeepLinksTest = true;
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        verifyManageSubscriptionScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(16)
    public void signOut() {
        sc.startStepsGroup("ReLogin - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    // common verification methods
    public void deepLink(String deepLinkUrl) {
        //sc.hybridClearCache();
        iOS_safariClearCache();
        String deepLink = "";
        strAppType = strAppType.toLowerCase();
        if(isStageApp) {
            createLog("App center stage app");
            //example : https://subaru.test-app.link/manage-subscriptions
            deepLink = deepLinkUrl.replace("url", "https://" + strAppType + ".test-app.link");
        } else {
            createLog("App center prod app");
            //example : https://subaru.link/manage-subscriptions
            deepLink = deepLinkUrl.replace("url", "https://" + strAppType + "app.link");
        }

        createLog("deep link is: " + deepLink);
        sc.deviceAction("Home");
        sc.swipe("Up", sc.p2cx(70), 100);
        verifyElementFound("NATIVE", "xpath=//*[@class='UIAView' and ./parent::*[@placeholder='Search']]", 0);
        sc.sendText("Safari");
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Safari']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='Safari']", 0, 1);
        sc.syncElements(5000, 30000);

        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Open this page in')]")) {
            //sc.hybridClearCache();
            iOS_safariClearCache();
            sc.deviceAction("Home");
            sc.swipe("Up", sc.p2cx(70), 100);
            verifyElementFound("NATIVE", "xpath=//*[@class='UIAView' and ./parent::*[@placeholder='Search']]", 0);
            sc.sendText("Safari");
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Safari']", 0);
            click("NATIVE", "xpath=//*[@accessibilityLabel='Safari']", 0, 1);
            sc.syncElements(5000, 30000);
        }

        if (sc.isElementFound("NATIVE", "xpath=(//*[@accessibilityLabel='URL'])[1]"))
            click("NATIVE", "xpath=(//*[@accessibilityLabel='URL'])[1]", 0, 1);
        else
            click("NATIVE", "xpath=//*[@label='Address']", 0, 1);
        sc.sendText(deepLink);
        if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Go']"))
            click("NATIVE", "xpath=//*[@accessibilityLabel='Go']", 0, 1);
        sc.syncElements(5000, 30000);

        //app center - so relaunch
        createLog("AppCenter app");
        iOS_LaunchApp();

        sc.syncElements(5000, 30000);
    }

    public void checkIsDashboardDisplayed() {
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='theme_change_icon']"))
            ios_emailSignOut();
    }

    public void verifyManageSubscriptionScreen() {
        sc.syncElements(30000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='SubscriptionScreen_backAction']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifyManageProfileScreen() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Personal Details']", 0);
        click("NATIVE", "xpath=//*[@label='Back']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifyScheduleServiceScreen() {
        sc.syncElements(30000, 60000);
        //ios_handlePages();
        verifyElementFound("NATIVE", "xpath=//*[@label='Schedule Maintenance']", 0);
        click("NATIVE", "xpath=//*[@label='Schedule Maintenance']/preceding::*[@class='UIAImage']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifyDashboardScreen() {
        sc.syncElements(5000, 30000);
        //Verify Dashboard texts
        actualText = sc.getText("NATIVE");
        createLog(actualText);
        expectedText = new String[]{"Odometer", "Range", "Fuel", "person", "Double tap to open vehicle switcher", "Vehicle image, double tap to open vehicle info", "Remote", "Status", "Health", "Info", "Swipe up to open advanced remote", "lock", "Start", "unlock", "IconService", "IconPay", "IconVehicle", "IconShop", "IconFind", "Service", "Pay","Shop","Find"};
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
        click("NATIVE", "xpath=//*[@accessibilityLabel='BottomTabBar_dashboardTab']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifyGuestDriverScreen() {
        sc.syncElements(5000, 30000);
        //Parking section
        verifyElementFound("NATIVE", "xpath=//*[@label='Guest Drivers']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Share Remote']", 0);
        click("NATIVE", "xpath=//*[@text='back_button']", 0, 1);
        sc.syncElements(5000, 30000);
    }


    //21MM
    public void verifySmsRegistration21MMScreen() {
        sc.syncElements(5000, 30000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='QR_SCAN_PERMISSION_LABEL_TITLE']")) {
            verifyElementFound("NATIVE", "xpath=//*[@label='Scanning the QR Code connects your account to the vehicle so you can register and enable available services.']", 0);
            click("NATIVE", "xpath=//*[@id='QR_SCAN_PERMISSION_BUTTON_CAMERA']", 0, 1);
            sc.syncElements(5000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@label,'Would Like to Access the Camera')])[1]"))
                click("NATIVE", "xpath=//*[@label='OK' or @label='Allow']", 0, 1);
        }
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Inside your vehicle, hold your smartphone camera over the QR code on the multimedia screen to start the scanning process.']", 0);
        click("NATIVE", "xpath=//*[@label='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifySmsLinkAccounts21MMScreen() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Linked Accounts' or @label='LINKED ACCOUNTS']", 0);
        actualText = sc.getText("NATIVE");
        createLog(actualText);
        expectedText = new String[]{"Music Services", "Enjoy streaming your audio in any supported vehicle using your linked accounts", "Apple Music", "Amazon Music"};
        for (String strText : expectedText) {
            if (actualText.contains(strText)) {
                sc.report("Validation of " + strText, true);
            } else {
                sc.report("Validation of " + strText, false);
            }
        }
        click("NATIVE", "xpath=//*[@label='Back']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifySmsSubscription21MMScreen() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='SubscriptionScreen_backAction']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifySmsPinReset21MMScreen(String strUsername, String strPassword) {
        sc.syncElements(5000, 30000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@label='Verification needed' and @class='UIAStaticText']")) {
            verifyElementFound("NATIVE", "xpath=//*[@label='Verification needed' and @class='UIAStaticText']", 0);
            actualText = sc.getText("NATIVE");
            createLog(actualText);
            expectedText = new String[]{"Verification needed", "Cancel", "Confirm", "In order to verify your identity, please sign in again. Note, you will not be signed out"};
            for (String strText : expectedText) {
                if (actualText.contains(strText)) {
                    sc.report("Validation of " + strText, true);
                } else {
                    sc.report("Validation of " + strText, false);
                }
            }
            click("NATIVE", "xpath=//*[@id='Confirm']", 0, 1);
            sc.syncElements(5000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']")) {
                sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0, strUsername);
                click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_CONTINUE_BUTTON']", 0, 1);
                sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD']", 0, strPassword);
                click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']", 0, 1);
                delay(5000);
            }
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Reset PIN']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Enter new PIN']", 0);
        for(int i=1;i<=6;i++){
            sc.syncElements(1000,2000);
            click("NATIVE","xpath=//*[@id='"+i+"']",0,1);
        }
        verifyElementFound("NATIVE","xpath=//*[@text='Your chosen PIN could be considered weak and easy to guess.  Would you like to proceed?']",0);
        click("NATIVE","xpath=//*[@id='OK']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@text='Confirm PIN']",0);
        for(int i=1;i<=6;i++){
            sc.syncElements(1000,2000);
            click("NATIVE","xpath=//*[@id='"+i+"']",0,1);
        }
        verifyElementFound("NATIVE","xpath=//*[@text='PIN successfully reset']",0);
        click("NATIVE", "xpath=//*[@label='Back']", 0, 1);
        sc.syncElements(5000, 30000);
    }
}
