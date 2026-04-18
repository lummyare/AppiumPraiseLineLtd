package v2update.subaru.ios.usa.shop;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruShopManageSubscriptionsIOS extends SeeTestKeywords {
    String testName = " - SubaruShopManageSubscription-IOS";

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
    public void shopManageTrialSubscriptions() {
        sc.startStepsGroup("Test - Shop->Manage Subscription->Trial Subscriptions");
        validateShopSubscriptionCard();
        validateSubscriptionScreen();
        validateSafetyConnect();
        validateDriveConnect();
        validateRemoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void shopManagePaidSubscriptions() {
        sc.startStepsGroup("Test - Shop->Manage Subscription->Paid Subscriptions");
        validateAddServiceScreen();
        addDriveConnectPaidService();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateShopSubscriptionCard(){
        createLog("Validating subscription section in Shop screen");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE","xpath=//*[@id='IconShop']",0);
        click("NATIVE","xpath=//*[@id='IconShop']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@id='Subscriptions']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='shop_manage_subscription_button']",0);
        click("NATIVE","xpath=//*[@id='shop_manage_subscription_button']",0,1);
        sc.syncElements(5000,60000);
        verifyElementFound("NATIVE","xpath=//*[@id='Subscriptions']",0);
        createLog("completed: Validating subscription section in Shop screen");
    }

    public static void validateSubscriptionScreen(){
        createLog("Validating subscription screen in Shop->Subscription screen");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@id='Subscriptions']",0);
        verifyElementFound("NATIVE","xpath=//*[@label='Trial Services']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Add Service']",0);
        createLog("completed: Validating subscription screen in Shop->Subscription screen");
    }

    public static void validateSafetyConnect(){
        createLog("Started - Verify Safety Connect");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@value='Safety Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@label='subscription_safety_connect']",0);
        //Trial status
        verifyElementFound("NATIVE","xpath=//*[@text='Safety Connect']/following::*[1]",0);
        String safetyConnectStatus = sc.elementGetText("NATIVE","xpath=//*[@text='Safety Connect']/following::*[1]",0);
        createLog("Safety connect status is: "+safetyConnectStatus);
        if(safetyConnectStatus.equalsIgnoreCase("Active")) {
            createLog("Safety connect status is ACTIVE");
        } else {
            createLog("Safety connect status is NOT ACTIVE");
        }
        if(sc.isElementFound("NATIVE","xpath=(//*[@text='Safety Connect']/following::*[@value='Up to 10-years, 4G Network Dependent'])[1]")) {
            verifyElementFound("NATIVE","xpath=(//*[@text='Safety Connect']/following::*[@value='Up to 10-years, 4G Network Dependent'])[1]",0);
            createLog("Safety Connect Trial is Up to 10-years");
        }
        else {
            verifyElementFound("NATIVE","xpath=(//*[@text='Safety Connect']/following::*[contains(@label,'Expires')])[1]",0);
            String safetyConnectExpiryDate = sc.elementGetText("NATIVE","xpath=(//*[@text='Safety Connect']/following::*[contains(@label,'Expires')])[1]",0);
            createLog("Safety connect expiry date is: "+safetyConnectExpiryDate);
        }
        click("NATIVE","xpath=//*[@label='subscription_safety_connect']",0,1);

        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE","xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Safety Connect' or @text='SAFETY CONNECT']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Trial Service']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']",0);
        if(sc.isElementFound("NATIVE","xpath=//*[@value='Up to 10-years, 4G Network Dependent']")) {
            verifyElementFound("NATIVE","xpath=//*[@value='Up to 10-years, 4G Network Dependent']",0);
            createLog("Service Details screen - Safety Connect Trial is Up to 10-years");
        }
        else {
            verifyElementFound("NATIVE","xpath=//*[contains(@label,'Expires')]",0);
            String safetyConnectExpiryDate = sc.elementGetText("NATIVE","xpath=//*[contains(@label,'Expires')]",0);
            createLog("Service Details screen - Safety connect expiry date is: "+safetyConnectExpiryDate);
        }
        verifyElementFound("NATIVE","xpath=//*[@id='Cancel' or @id='CANCEL']",0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        createLog("Completed - Verify Safety Connect");
    }

    public static void validateDriveConnect(){
        createLog("Started - Verify Drive Connect");
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@label='Drive Connect']",0);
        //Trial status
        verifyElementFound("NATIVE","xpath=//*[@text='Drive Connect']/following::*[1]",0);
        String driveConnectStatus = sc.elementGetText("NATIVE","xpath=//*[@text='Drive Connect']/following::*[1]",0);
        createLog("Drive connect status is: "+driveConnectStatus);
        if(driveConnectStatus.equalsIgnoreCase("Active")) {
            createLog("Drive connect status is ACTIVE");
        } else {
            createLog("Drive connect status is NOT ACTIVE");
        }
        verifyElementFound("NATIVE","xpath=(//*[contains(@text,'Drive Connect')]/following::*[contains(@value,'Expires')])[1]",0);
        String subExpirationDateDetails = sc.elementGetProperty("NATIVE","xpath=(//*[contains(@text,'Drive Connect')]/following::*[contains(@value,'Expires')])[1]",0,"value");
        createLog("Subscription expiration date details: "+subExpirationDateDetails);
        verifyElementFound("NATIVE","xpath=//*[@label='subscription_drive_connect']",0);

        click("NATIVE","xpath=//*[@label='subscription_drive_connect']",0,1);

        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE","xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Drive Connect' or @text='DRIVE CONNECT']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Trial Service']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@label,'Expires')]",0);
        String driveConnectExpiryDate = sc.elementGetText("NATIVE","xpath=//*[contains(@label,'Expires')]",0);
        createLog("Service Details screen - Drive connect expiry date is: "+driveConnectExpiryDate);
        verifyElementFound("NATIVE","xpath=//*[@id='Cancel' or @id='CANCEL']",0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        createLog("Completed - Verify Drive Connect");
    }

    public static void validateRemoteConnect(){
        createLog("Started - Verify Remote Connect");
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[contains(@value,'Remote Connect')]",0);
        verifyElementFound("NATIVE","xpath=(//*[contains(@value,'Remote Connect')]/following::*[@label='Active'])[1]",0);
        //Trial status
        verifyElementFound("NATIVE","xpath=//*[contains(@value,'Remote Connect')]/following::*[1]",0);
        String remoteConnectStatus = sc.elementGetText("NATIVE","xpath=//*[contains(@value,'Remote Connect')]/following::*[1]",0);
        createLog("Remote connect status is: "+remoteConnectStatus);
        if(remoteConnectStatus.equalsIgnoreCase("Active")) {
            createLog("Remote connect status is ACTIVE");
        } else {
            createLog("Remote connect status is NOT ACTIVE");
        }
        verifyElementFound("NATIVE","xpath=(//*[contains(@text,'Remote Connect')]/following::*[contains(@value,'Expires')])[1]",0);
        String subExpirationDateDetails = sc.elementGetProperty("NATIVE","xpath=(//*[contains(@text,'Remote Connect')]/following::*[contains(@value,'Expires')])[1]",0,"value");
        createLog("Subscription expiration date details: "+subExpirationDateDetails);

        click("NATIVE","xpath=//*[contains(@text,'Remote Connect')]",0,1);

        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE","xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'With Remote Connect, manage your vehicle from the palm of your hand.') or contains(@id,'With Remote Connect, control your vehicle from the palm of your hand.') or contains(@id,'With Remote Connect, monitor battery status, locate the nearest charging station and more') or contains(@id,'Manage your vehicle from the palm of your hand') or contains(@id,'With features including engine start/stop, door lock/unlock, cabin climate controls and Digital Key – which gives others access to your vehicle') or contains(@id,'With Remote Connect, check battery status, find the nearest charging station and more')]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Remote Connect')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Trial Service']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@label,'Expires')]",0);
        String remoteConnectExpiryDate = sc.elementGetText("NATIVE","xpath=//*[contains(@label,'Expires')]",0);
        createLog("Service Details screen - Remote connect expiry date is: "+remoteConnectExpiryDate);
        verifyElementFound("NATIVE","xpath=//*[@id='Cancel' or @id='CANCEL']",0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        createLog("Completed - Verify Remote Connect");
    }

    public static void validateAddServiceScreen() {
        createLog("Started - Validation add service screen");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='Add Service']")) {
            validateShopSubscriptionCard();
            validateSubscriptionScreen();
        }
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@id='Add Service']",0);
        click("NATIVE","xpath=//*[@id='Add Service']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Manage Subscription' and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Paid Services' and ./parent::*[@text='SUBSCRIPTIONS_TABLE_VIEW']]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Purchase additional services')]",0);

        //Drive Connect
        verifyElementFound("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[1]",0);
        click("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[1]",0,1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Select Subscription' and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Select a Subscription for Drive Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Monthly Subscription']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='/ Month']",0);
        verifyElementFound("NATIVE","xpath=(//*[@name='SUBSCRIPTIONS_AUTO_RENEW'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[@label='Continue' and @class='UIAButton']",0);
        //click back
        verifyElementFound("NATIVE","xpath=//*[@label='remote services remove']",0);
        click("NATIVE","xpath=//*[@label='remote services remove']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@text='Manage Subscription' and @class='UIAStaticText']",0);

        //Remote Connect
        verifyElementFound("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[2]",0);
        click("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[2]",0,1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Select Subscription' and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Select a Subscription for Remote Connect')]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Monthly Subscription']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='/ Month']",0);
        verifyElementFound("NATIVE","xpath=(//*[@name='SUBSCRIPTIONS_AUTO_RENEW'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[@label='Continue' and @class='UIAButton']",0);
        //click back
        verifyElementFound("NATIVE","xpath=//*[@label='remote services remove']",0);
        click("NATIVE","xpath=//*[@label='remote services remove']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@text='Manage Subscription' and @class='UIAStaticText']",0);

        if(sc.isElementFound("NATIVE","xpath=//*[@text='3RD PARTY SERVICE' and @class='UIAStaticText']")) {
            createLog("Started - validation 3RD PARTY SERVICE section");
            verifyElementFound("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[3]",0);
            click("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[3]",0,1);
            sc.syncElements(2000, 30000);

            verifyElementFound("NATIVE","xpath=//*[@text='Service Details' and @class='UIAStaticText']",0);
            verifyElementFound("NATIVE","xpath=//*[@text='Wi-Fi Connect']",0);
            verifyElementFound("NATIVE",convertTextToUTF8("//*[@id='Go To AT&T']"),0);
            click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
            createLog("Completed - validation 3RD PARTY SERVICE section");
        }
        verifyElementFound("NATIVE","xpath=//*[@text='Manage Subscription' and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Continue to Purchase' and @class='UIAButton']",0);

        click("NATIVE","xpath=//*[@text='Back']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@id='Add Service']",0);
        createLog("Completed - Validation add service screen");
    }

    public static void addDriveConnectPaidService() {
        createLog("Started - Add Drive Connect Paid Service");
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@id='Add Service']",0);
        click("NATIVE","xpath=//*[@id='Add Service']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Manage Subscription' and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Paid Services' and ./parent::*[@text='SUBSCRIPTIONS_TABLE_VIEW']]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Purchase additional services')]",0);

        //verify continue to purchase button is disabled
        verifyElementFound("NATIVE","xpath=//*[(@text='Continue to Purchase' and @class='UIAButton') and @enabled='false']",0);

        //Drive Connect
        verifyElementFound("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[1]",0);
        click("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[1]",0,1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Select Subscription' and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Select a Subscription for Drive Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Monthly Subscription']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='/ Month']",0);
        verifyElementFound("NATIVE","xpath=(//*[@name='SUBSCRIPTIONS_AUTO_RENEW'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[(@label='Continue' and @class='UIAButton') and @enabled='false']",0);
        //click monthly subscription
        click("NATIVE","xpath=//*[@text='Monthly Subscription']",0,1);
        //verify continue button is enabled
        verifyElementFound("NATIVE","xpath=//*[(@label='Continue' and @class='UIAButton') and @enabled='true']",0);
        //click continue
        click("NATIVE","xpath=//*[(@label='Continue' and @class='UIAButton') and @enabled='true']",0,1);

        sc.syncElements(5000, 30000);
        //verify continue to purchase button is enabled
        verifyElementFound("NATIVE","xpath=//*[(@text='Continue to Purchase' and @class='UIAButton') and @enabled='true']",0);
        //Click continue to purchase button
        click("NATIVE","xpath=//*[(@text='Continue to Purchase' and @class='UIAButton') and @enabled='true']",0,1);
        sc.syncElements(2000, 30000);
        //consent screen
        verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and @onScreen='true'])[1]", 0, 1000, 5, false);
        sc.swipe("Down", sc.p2cy(70), 2000);
        click("NATIVE","xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and @onScreen='true'])[1]",0,1);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and @onScreen='true'])[2]", 0, 1000, 5, false);
        sc.swipe("Down", sc.p2cy(70), 2000);
        click("NATIVE","xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and @onScreen='true'])[2]",0,1);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[(@id='Confirm and Continue' and @class='UIAButton') and @enabled='true']", 0, 1000, 5, false);
        sc.swipe("Down", sc.p2cy(70), 2000);
        click("NATIVE", "xpath=//*[(@id='Confirm and Continue' and @class='UIAButton') and @enabled='true']", 0, 1);
        //Home address
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Home Address']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Home Address']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0);
            click("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0, 1);
        }
        sc.syncElements(5000, 30000);
        //Address verification screen
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Address Verification']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Address Verification']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@text='Continue With This Address') and @class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[(@text='Continue With This Address') and @class='UIAButton']", 0, 1);
        }
        sc.syncElements(10000, 30000);

        //Payment method screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Payment Method']", 0);
        createLog("Completed - Add Drive Connect Paid Service");
    }
}
