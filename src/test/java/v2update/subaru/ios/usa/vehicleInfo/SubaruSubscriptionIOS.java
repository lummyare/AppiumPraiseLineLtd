package v2update.subaru.ios.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.CommonUtils;
import v2update.ios.usa.shop.Subscriptions21MMIOS;
import v2update.ios.usa.vehicleInfo.SubscriptionsIOS;

import static v2update.ios.usa.shop.Subscriptions21MMIOS.validateSubscriptionCard;
import static v2update.ios.usa.vehicles.VehicleSelectionIOS.Switcher;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSubscriptionIOS extends SeeTestKeywords {
    String testName = " - SubaruSubscription-IOS";

    boolean isStageRun = Boolean.valueOf(ConfigSingleton.configMap.get("stageRun"));
    public static boolean isCancelSubscriptionAvailable = false;

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
    public void trialSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Trial Subscriptions");
        validateSubscriptionCard();
        validateSubscriptionScreen();
        validateSafetyConnect();
        validateDriveConnect();
        validateRemoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void paidSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Paid Subscriptions");
        if(isStageRun) {
            createLog("Stage run - adding paid service");
            isStageApp = true;
            sc.install(ConfigSingleton.configMap.get("cloudAppStageSubaruIOS"), false, false);
            sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaruStage"), false, true);
            sc.syncElements(5000,10000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            ios_handlepopups();
            checkIsLoginScreenDisplayed_IOS();
            environmentSelection_iOS("stage");
            selectionOfCountry_IOS("USA");
            selectionOfLanguage_IOS("English");
            ios_keepMeSignedIn(true);
            ios_emailLogin("subarustage_21mm@mail.tmnact.io", "Toyo213$$");
            //Paid Subscription With Auto Renew OFF
            validateSubscriptionCard();
            validateSubscriptionScreen();
            paidSubscriptionWithAutoRenewOnOff(false, false);
            //Paid Subscription With Auto Renew ON and add new credit card
            validateSubscriptionCard();
            validateSubscriptionScreen();
            paidSubscriptionWithAutoRenewOnOff(true, true);
        } else {
            createLog("Production run - skipping add paid service");
            validateAddServiceScreen();
            addDriveConnectPaidService();
        }
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateSubscriptionCard(){
        createLog("Validating subscription section in Vehicle Info screen");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE", "xpath=//*[@label='Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Subscriptions']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Connected services for your vehicle']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='subscriptions_icon']",0);
        click("NATIVE","xpath=//*[@text='Subscriptions']",0,1);
        sc.syncElements(10000,60000);
        verifyElementFound("NATIVE","xpath=//*[@id='Subscriptions']",0);
        createLog("completed: Validating subscription section in Vehicle Info screen");
    }

    public static void validateSubscriptionScreen(){
        createLog("Validating subscription screen in Vehicle Info screen");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@id='Subscriptions']",0);
        verifyElementFound("NATIVE","xpath=//*[@label='Trial Services']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Add Service']",0);
        createLog("completed: Validating subscription screen in Vehicle Info screen");
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
            validateSubscriptionCard();
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

    public static void addServiceFlow(boolean autoRenew, boolean isAddNewCreditCard) {
        createLog("Verifying add service flow Go Anywhere subscription bundling");

        checkCancelPaidSubscriptionAvailable("Go Anywhere");
//*[@text='Paid Services' and ./parent::*[@text='SUBSCRIPTIONS_TABLE_VIEW']]
        //Add Service
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Add Service']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@label='Add Service']", 0);
        sc.click("NATIVE", "xpath=//*[@label='Add Service']", 0, 1);
        sc.syncElements(20000, 60000);

        createLog("Verifying Manage Subscriptions screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Manage Subscription']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_TABLE_VIEW']//following-sibling::*[@text='Paid Services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Purchase additional services for your') and @knownSuperClass='UIAccessibilityElement']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[contains(@id,'Remote Connect') and @visible='true']", 0, 1000, 5, false);
        verifyElementFound("NATIVE","xpath=(//*[@text='SUBSCRIPTIONS_TABLE_VIEW']/*/*[@text='SUBSCRIPTIONS_CELL_TITLE'])[1]",0);
        verifyElementFound("NATIVE","xpath=(//*[@text='SUBSCRIPTIONS_TABLE_VIEW']/*/*[@text='SUBSCRIPTIONS_CELL_TITLE'])[2]",0);
        sc.click("NATIVE", "xpath=(//*[@text='SUBSCRIPTIONS_TABLE_VIEW']/*/*[@text='SUBSCRIPTIONS_CELL_TITLE'])[1]", 0, 1);
        sc.syncElements(20000, 60000);

        // $15.00/Monthly
        verifyElementFound("NATIVE", "xpath=//*[@text='Select Subscription' and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Monthly Subscription']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Auto Renew' and ./parent::*[./parent::*[@text='SUBSCRIPTIONS_TABLE_VIEW']]]", 0);


        verifyElementFound("NATIVE", "xpath=(//*[@text='Go Anywhere']//following::*[contains(@text,'/Monthly')])[1]", 0);
        String subscriptionAmountDetails = sc.elementGetText("NATIVE", "xpath=(//*[@text='Go Anywhere']//following::*[contains(@text,'/Monthly')])[1]", 0);
        createLog("subscription amount details: "+subscriptionAmountDetails);
        String[] subscriptionAmountArr = subscriptionAmountDetails.split("/");
        String subscriptionAmountWithUnit = subscriptionAmountArr[0];
        createLog("subscription amount with unit: "+subscriptionAmountWithUnit);
        String[] amountArr = subscriptionAmountArr[0].split("[.]");
        String amountInt = amountArr[0].substring(1);
        sc.report("Subscription amount is Numeric value ", CommonUtils.isNumeric(amountInt));
        //verify Subscription amount is greater than 0
        sc.report("Subscription amount is greater than 0 ", Integer.parseInt(amountInt) >= 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/preceding-sibling::*[@text='subscription chevronRight']", 0);
        createLog("Verified Manage Subscriptions screen");

        //click on arrow next to subscription bundle name to navigate to Service Details screen
        sc.click("NATIVE", "xpath=//*[@text='Go Anywhere']/preceding-sibling::*[@text='subscription chevronRight']", 0, 1);
        sc.syncElements(10000, 60000);
        createLog("Verifying Go Anywhere subscription bundling Service Details screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        createLog("Verified Go Anywhere subscription bundling Service Details screen");

        createLog("Verify selecting Go Anywhere subscription in Manage Subscription screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription') and @class='UIAStaticText']", 0);
        //check Continue to purchase button is disabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);

        //verify subscription checkbox is unchecked
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_CHECKBOX_UNCHECKED_ICON']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@text='subscription uncheck']", 0);
        click("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@text='subscription uncheck']", 0, 1);
        sc.syncElements(20000, 60000);
        createLog("Selecting Go Anywhere subscription in SELECT SUBSCRIPTION screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Select Subscription') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere' or @text='SELECT A SUBSCRIPTION FOR GO ANYWHERE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Monthly')]", 0);
        // $15.00/Monthly
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']//following::*[contains(@text,'/Monthly')]", 0);
        subscriptionAmountDetails = sc.elementGetText("NATIVE", "xpath=//*[@text='Go Anywhere']//following::*[contains(@text,'/Monthly')]", 0);
        createLog("subscription amount details: "+subscriptionAmountDetails);
        subscriptionAmountArr = subscriptionAmountDetails.split("/");
        subscriptionAmountWithUnit = subscriptionAmountArr[0];
        createLog("subscription amount with unit: "+subscriptionAmountWithUnit);
        amountArr = subscriptionAmountArr[0].split("[.]");
        amountInt = amountArr[0].substring(1);
        sc.report("Subscription amount is Numeric value ", CommonUtils.isNumeric(amountInt));
        //verify Subscription amount is greater than 0
        sc.report("Subscription amount is greater than 0 ", Integer.parseInt(amountInt) >= 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='subscription uncheck']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]", 0);
        //By default Auto renew is unchecked
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_UNCHECKED']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0);

        if(autoRenew) {
            createLog("Paid subscription with Auto Renew - ON");
            click("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0, 1);
            //Auto-renew is checked
            verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_CHECKED']", 0);
        } else {
            createLog("Paid subscription with Auto Renew - OFF");
        }

        //verify continue button is disabled and subscription icon is unchecked
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue') and @class='UIAButton' and @enabled='false']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_UNCHECKED_ICON']", 0);

        //select subscription
        click("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere']/following::*[@text='subscription uncheck']", 0, 1);
        sc.syncElements(3000, 9000);
        //verify subscription icon is checked and continue button is enabled
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_CHECKED_ICON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue') and @class='UIAButton' and @enabled='true']", 0);
        click("NATIVE", "xpath=//*[(@text='Continue') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 50000);
        createLog("Selected Go Anywhere subscription in SELECT SUBSCRIPTION screen");

        //verify subscription checkbox is checked and Continue to purchase button is enabled in Manage Subscription screen
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified selecting Go Anywhere subscription in Manage Subscription screen");

        //click on Continue to purchase
        sc.click("NATIVE", "xpath=//*[@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE' and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(15000, 60000);

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
        if(isAddNewCreditCard) {
            createLog("Add a new credit card");
            addNewCard();
        } else {
            createLog("Use existing card");
            verifyElementFound("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0);
            click("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0, 1);
            sc.syncElements(5000, 30000);
        }

        //Checkout screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Checkout' or @text='CHECKOUT']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleImageView' and @width>250 and @height>100]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Month')]", 0);
        // $15/Month
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']//following::*[contains(@text,'/Month')]", 0);
        subscriptionAmountDetails = sc.elementGetText("NATIVE", "xpath=//*[@text='Go Anywhere']//following::*[contains(@text,'/Month')]", 0);
        createLog("subscription amount details: "+subscriptionAmountDetails);
        subscriptionAmountArr = subscriptionAmountDetails.split("/");
        subscriptionAmountWithUnit = subscriptionAmountArr[0];
        createLog("subscription amount with unit: "+subscriptionAmountWithUnit);
        String amount = subscriptionAmountArr[0].substring(1);
        sc.report("Subscription amount is Numeric value ", CommonUtils.isNumeric(amount));
        //verify Subscription amount is greater than 0
        sc.report("Subscription amount is greater than 0 ", Integer.parseInt(amount) >= 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='check empty']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Pay From']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Subtotal']", 0);
        //$15.00
        verifyElementFound("NATIVE", "xpath=//*[@text='Subtotal']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        String subTotalWithUnit = sc.elementGetText("NATIVE", "xpath=//*[@text='Subtotal']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        createLog("Subtotal amount details: "+subTotalWithUnit);
        verifyElementFound("NATIVE", "xpath=//*[@text='Tax']", 0);
        //$0.99
        verifyElementFound("NATIVE", "xpath=//*[@text='Tax']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        String taxWithUnit = sc.elementGetText("NATIVE", "xpath=//*[@text='Tax']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        createLog("Tax amount details: "+taxWithUnit);
        verifyElementFound("NATIVE", "xpath=//*[@text='Total']", 0);
        //$15.99
        verifyElementFound("NATIVE", "xpath=//*[@text='Total']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        String totalAmountWithUnit = sc.elementGetText("NATIVE", "xpath=//*[@text='Total']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        createLog("Total amount details: "+totalAmountWithUnit);

        createLog("verifying total value is matching on adding subtotal & tax value in checkout screen");
        double subTotalVal = Double.parseDouble(subTotalWithUnit.substring(1));
        double taxVal = Double.parseDouble(taxWithUnit.substring(1));
        double totalVal = Double.parseDouble(totalAmountWithUnit.substring(1));
        createLog("Double subTotal value: "+subTotalVal);
        createLog("Double tax value: "+taxVal);
        createLog("Double total value: "+totalVal);
        double subTotalAndTax = subTotalVal + taxVal;
        createLog("(subTotal + tax) sum value: "+ subTotalAndTax);
        Assertions.assertTrue(subTotalAndTax == totalVal);
        createLog("verified total value is matching on adding subtotal & tax value in checkout screen");

        //terms
        sc.flickElement("NATIVE", "xpath=//*[@text='Pay From']", 0, "up");
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='swipeToPay' and @visible='true']", 0, 1000, 5, false);
        //verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Swiping \"Swipe to Pay\" authorizes the scheduled charges and applicable taxes to the payment method provided as described in the Connected Services Terms of Use including the Refund Policy. The total amount to be charged on your billing date will be subject to the then current fee and applicable taxes.')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='swipeToPay']", 0);

        sc.drag("NATIVE","xpath=//*[@class='UIAView' and ./*[@class='UIAImage'] and ./parent::*[@text='swipeToPay']]",0,1000,0);
        sc.syncElements(20000, 60000);

        //Success Screen
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Success']") && sc.isElementFound("NATIVE", "xpath=//*[@text='Purchase Submitted']")) {
            createLog("Go Anywhere Subscription bundling purchase successful");
        } else {
            createLog("Go Anywhere Subscription bundling purchase is not successful");
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Success']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Success']/following-sibling::*[@class='UIAImage'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Purchase Submitted']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Month')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);

        createLog("verifying total subscription amount is matching success screen with checkout screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Purchase Submitted']/preceding-sibling::*[@class='UIAStaticText' and contains(@text,'$')]", 0);
        String subscriptionAmount = sc.elementGetText("NATIVE", "xpath=//*[@text='Purchase Submitted']/preceding-sibling::*[@class='UIAStaticText' and contains(@text,'$')]", 0);
        createLog("Subscription amount with unit in success screen: "+subscriptionAmount);
        double subscriptionAmountVal = Double.parseDouble(subscriptionAmount.substring(1));
        createLog("Double subscriptionAmountVal value: "+subscriptionAmountVal);
        createLog("Double total value in Checkout screen: "+totalVal);
        Assertions.assertTrue(subscriptionAmountVal == totalVal);
        createLog("verified total subscription amount is matching success screen with checkout screen");

        verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='returnToDashboardButton']", 0, 1);
        sc.syncElements(15000, 60000);

        //verify dashboard screen
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);

        //Verify subscription is added
        //click vehicle image from Dashboard
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(4000, 20000);
        verifyElementFound("NATIVE","xpath=//*[@text='Subscriptions']",0);
        click("NATIVE","xpath=//*[@text='Subscriptions']",0,1);
        //verify Manage Subscriptions
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='subscriptionscreen_vehicleImage']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[contains(@id,'Remote Connect') and @visible='true']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0);

        createLog("Verifying Go Anywhere subscription added under Paid Services section");

        createLog("Verified add service flow Go Anywhere subscription bundling");
    }

    public static void paidSubscriptionWithAutoRenewOnOff(boolean autoRenew, boolean isAddNewCreditCard) {
        createLog("Started - paid subscription with Auto Renew ON/OFF");

        addServiceFlow(autoRenew, isAddNewCreditCard);

        if(autoRenew) {
            createLog("Started - Subscription screen - Post paid subscription verification for Auto Renew - ON");
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[contains(@id,'Remote Connect') and @visible='true']", 0, 1000, 5, false);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Auto Renew On')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Billing starts on') or contains(@text,'The next billing date')]", 0);
            String autoRenewDateDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'Billing starts on') or contains(@text,'The next billing date')]",0,"value");
            createLog("Auto renew next billing date details: "+autoRenewDateDetails);
            verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[@text='Drive Connect'])[1]", 0);
            verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[contains(@text,'Remote Connect')])[1]", 0);
            createLog("Verified Go Anywhere subscription added under Paid Services section");

            //click on Go Anywhere subscription -
            click("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0, 1);
            sc.syncElements(20000, 60000);
            createLog("Verifying Go Anywhere subscription bundling Service Details screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']", 0);
            //verify Auto Renew On
            verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew On']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Billing starts on') or contains(@text,'The next billing date')]", 0);
            autoRenewDateDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'Billing starts on') or contains(@text,'The next billing date')]",0,"value");
            createLog("Auto renew next billing date details: "+autoRenewDateDetails);
            createLog("Completed - Post paid subscription verification for Auto Renew - ON");
        } else {
            createLog("Started - Post paid subscription verification for Auto Renew - OFF");
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[contains(@id,'Remote Connect') and @visible='true']", 0, 1000, 5, false);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew Off']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0);
            String subExpirationDateDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'Expires On')]",0,"value");
            createLog("Subscription expiration date details: "+subExpirationDateDetails);
            verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[@text='Drive Connect'])[1]", 0);
            verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[contains(@text,'Remote Connect')])[1]", 0);
            createLog("Verified Go Anywhere subscription added under Paid Services section");

            //click on Go Anywhere subscription -
            click("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0, 1);
            sc.syncElements(20000, 60000);
            createLog("Verifying Go Anywhere subscription bundling Service Details screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']", 0);
            //verify Auto Renew OFF
            verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew Off']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0);
            subExpirationDateDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'Expires On')]",0,"value");
            createLog("Subscription expiration date details: "+subExpirationDateDetails);
            createLog("Completed - Subscription screen - Post paid subscription verification for Auto Renew - OFF");

        }

        createLog("Completed - paid subscription with Auto Renew ON/OFF");
    }

    public static void addNewCard(){
        createLog("Started - Adding new card");
        verifyElementFound("NATIVE", "xpath=//*[@text='Add a New Credit Card']", 0);
        click("NATIVE", "xpath=//*[@text='Add a New Credit Card']", 0, 1);
        sc.syncElements(20000, 40000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Payment Information' or @text='PAYMENT INFORMATION']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Pay securely using your credit card' ) or contains(@text,'PAY SECURELY USING YOUR CREDIT CARD')]", 0);

        verifyElementFound("NATIVE", "xpath=//*[(@id='Name on Card' or @id='NAME ON CARD') and @class='UIAStaticText']", 0);
        sendText("NATIVE","xpath=(//*[(@id='Name on Card' or @id='NAME ON CARD') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,strRandomName);
        verifyElementFound("NATIVE", "xpath=//*[(@id='Card Number' or @id='CARD NUMBER') and @class='UIAStaticText']", 0);
        sendText("NATIVE","xpath=(//*[(@id='Card Number' or @id='CARD NUMBER') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,"5555555555554444");
        click("NATIVE","xpath=//*[@text='Exp. Date Month']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@id='02']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@text='Year']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@id='2029']",0,1);
        sc.syncElements(1000, 3000);
        sendText("NATIVE","xpath=(//*[@id='CVV']/following::*[@class='UIATextField'])[1]",0,"222");
        sc.swipe("Down", sc.p2cy(20), 5000);
        sc.syncElements(1000, 3000);
        sendText("NATIVE","xpath=(//*[(@id='Billing Address' or @id='BILLING ADDRESS') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,"Test");
        sc.syncElements(1000, 3000);
        sendText("NATIVE","xpath=(//*[(@id='City' or @id='CITY') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,"Test");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Country' and @class='UIAStaticText']", 0);
        click("NATIVE","xpath=(//*[@id='Country' and @class='UIAView']/following-sibling::*[@class='UIAView'])[1]",0,1);
        sc.syncElements(2000, 10000);
        click("NATIVE","xpath=//*[@id='United States']",0,1);
        sc.syncElements(2000, 10000);
        //state
        if(sc.isElementFound("NATIVE","xpath=//*[@text='- Select One -']")) {
            createLog("select state from drop down list");
            click("NATIVE","xpath=//*[@text='- Select One -']",0,1);
            sc.syncElements(1000, 3000);
            click("NATIVE","xpath=//*[@id='Alaska']",0,1);
        } else {
            createLog("Enter state");
            sendText("NATIVE","xpath=(//*[@id='State']/following::*[@class='UIATextField'])[1]",0,"Texas");
        }
        sc.syncElements(2000, 10000);
        sendText("NATIVE","xpath=(//*[(@id='Zip Code' or @id='ZIP CODE') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,"75094");
        sc.syncElements(2000,4000);
        if(sc.isElementFound("NATIVE","xpath=(//*[@id='payment'])[1]/following::*[@id='DEFAULT PAYMENT METHOD' or @id='Default Payment Method']")) {
            //displays both check box DEFAULT PAYMENT METHOD and consent
            //click on DEFAULT PAYMENT METHOD check box
            click("NATIVE","xpath=((//*[@id='payment'])[1]/following::*[@class='UIAView'])[1]",0,1);
            sc.syncElements(2000,4000);
            //click on consent check box
            click("NATIVE","xpath=(//*[@id='DEFAULT PAYMENT METHOD' or @id='Default Payment Method']/following::*[@class='UIAView'])[1]",0,1);
            sc.syncElements(2000,4000);
        } else {
            //displays only consent check box
            click("NATIVE","xpath=((//*[@id='payment'])[1]/following::*[@class='UIAView'])[1]",0,1);
            sc.syncElements(2000,4000);
        }
        click("NATIVE","xpath=//*[(@id='Continue to Review' or @id='CONTINUE TO REVIEW') and @enabled='true']",0,1);
        sc.syncElements(15000, 60000);
        createLog("Completed - Adding new card");
    }

    public static void checkCancelPaidSubscriptionAvailable(String paidSubscriptionBundle) {
        createLog("Checking cancel subscription available for "+paidSubscriptionBundle+"");
        boolean existingSubFound = false;
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Add Service']")) {
            reLaunchApp_iOS();
            verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
            click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
            sc.syncElements(4000, 20000);
            verifyElementFound("NATIVE","xpath=//*[@text='Subscriptions']",0);
            click("NATIVE","xpath=//*[@text='Subscriptions']",0,1);
            verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='subscriptionscreen_vehicleImage']", 0);
        }

        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        sc.swipe("Down", sc.p2cy(30), 2000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@label='Paid Services']")) {
            createLog("paid services section found");
            verifyElementFound("NATIVE", "xpath=//*[@label='Paid Services']", 0);
            for (int i=1; i<4; i++) {
                if(sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'"+paidSubscriptionBundle+"')]")) {
                    createLog("Existing paid subscription : "+paidSubscriptionBundle+" found");
                    existingSubFound = true;
                    break;
                } else {
                    sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@label,'"+paidSubscriptionBundle+"')]", 0, 1000, i, false);
                    createLog("Existing paid subscription "+paidSubscriptionBundle+" not found after "+i+" rounds of swipe");
                }
            }

            if(existingSubFound) {
                createLog("Cancelling existing paid subscription");
                click("NATIVE", "xpath=//*[contains(@label,'"+paidSubscriptionBundle+"')]", 0, 1);
                sc.syncElements(20000, 60000);
                createLog("Verifying Go Anywhere subscription bundling Service Details screen");
                verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details') and @class='UIAStaticText']", 0);
                verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription') and @class='UIAButton']", 0);

                click("NATIVE", "xpath=//*[(@label='Manage Subscription') and @class='UIAButton']", 0, 1);
                sc.syncElements(10000, 60000);

                createLog("Verify Update Subscription screen");
                verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
                sc.syncElements(5000, 60000);

                if(sc.isElementFound("NATIVE","xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton']")) {
                    createLog("cancel subscription is displayed");
                    isCancelSubscriptionAvailable = true;
                    //Cancel subscription button is enabled
                    verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription') and @class='UIAButton' and @enabled='true']", 0);
                    createLog("Verified Update Subscription screen");

                    //click Cancel Subscription
                    click("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0, 1);
                    sc.syncElements(10000, 60000);
                    createLog("Verify Cancel Subscription screen");
                    verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
                    verifyElementFound("NATIVE", "xpath=//*[@text='Are you sure you want to cancel "+paidSubscriptionBundle+"?']", 0);
                    verifyElementFound("NATIVE", "xpath=//*[@text='Reason For Cancellation']", 0);

                    //Select Reason for cancellation
                    click("NATIVE", "xpath=//*[@text='Reason For Cancellation']/following-sibling::*[@class='UIAButton']", 0, 1);
                    sc.syncElements(3000, 9000);
                    sc.setPickerValues("NATIVE", "xpath=//*[@class='UIAPicker']", 0, 0, "down:1");
                    //Confirm Cancellation button is enabled
                    verifyElementFound("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='true']", 0);

                    //click Confirm Subscription
                    click("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='true']", 0, 1);
                    sc.syncElements(10000, 60000);
                    verifyElementFound("NATIVE", "xpath=//*[@text='You confirm that you no longer want "+paidSubscriptionBundle+" services']", 0);
                    click("NATIVE", "xpath=//*[@text='Confirm']", 0, 1);
                    sc.syncElements(20000, 60000);

                    //Verify Cancellation Complete screen
                    createLog("Verifying Cancellation Complete screen");
                    verifyElementFound("NATIVE", "xpath=//*[(@label='Cancellation Complete' or @label='CANCELLATION COMPLETE') and @class='UIAStaticText']", 0);
                    verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
                    click("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAButton']", 0, 1);
                    createLog("Verified Cancellation Complete screen");
                    sc.syncElements(10000, 30000);
                    createLog("Cancelled existing paid subscription : "+paidSubscriptionBundle+"");
                    sc.syncElements(5000, 30000);
                    //go to subscriptions screen
                    verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
                    click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
                    sc.syncElements(4000, 20000);
                    verifyElementFound("NATIVE","xpath=//*[@text='Subscriptions']",0);
                    click("NATIVE","xpath=//*[@text='Subscriptions']",0,1);
                    verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
                    verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='subscriptionscreen_vehicleImage']", 0);
                }
                else {
                    isCancelSubscriptionAvailable = false;
                    createLog("cancel subscription not displayed");
                    //clicking back in update subscription
                    click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
                    //clicking back in Service Details screen
                    click("NATIVE", "xpath=//*[@text='Back']", 0, 1);

                    sc.syncElements(5000, 60000);

                    //subscriptions screen
                    verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
                    //Check Auto Renew off
                    sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@label,'Go Anywhere')]", 0, 1000, 5, false);
                    verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew Off'])[1]", 0);
                }
            }
        } else {
            createLog("Existing paid services section not found");
            //verify subscriptions screen
            verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        }
        createLog("Checked cancel subscription available for "+paidSubscriptionBundle+"");
    }
}
