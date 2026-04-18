package v2update.ios.usa.shop;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.CommonUtils;
import v2update.ios.usa.vehicles.VehicleSelectionIOS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Subscriptions21MMIOS extends SeeTestKeywords {

    String testName = "Subscriptions21MM-IOS";
    static String strVin = "JTJJM7FX2E121DZ21";
    public static boolean isCancelSubscriptionAvailable = false;
    String subscriptionDetails = "";

    @BeforeAll
    public void setup() throws Exception {
        iOS_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("21mm email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("stagetesting_21mm"), ConfigSingleton.configMap.get("stagetesting_21mmPw"));
        sc.stopStepsGroup();
        /*
        Call Vehicle switcher from the vehicle switcher class and Default the vehicle 5TDADAB59RS000015
        iOS_DefaultVehicle("5TDADAB59RS000015"); // This method is deprecated
         */
        VehicleSelectionIOS.Switcher("800BDN99400000000");
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void subscriptionsCard(){
        // Validating subscription card under shop

        sc.startStepsGroup("Subscription Card");
        validateSubscriptionCard();
        sc.stopStepsGroup();
    }


    @Test
    @Order(2)
    public void subscriptionsScreen(){
        // validating subscription screen

        sc.startStepsGroup("Subscription Screen");
        validateSubscriptionScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void safetyConnect(){
        // validating safety connect card + details for 21mm

        sc.startStepsGroup("Safety Connect Screen");
        validateSafetyConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void driveConnect(){
        // validating drive connect card + details for 21mm

        sc.startStepsGroup("Drive Connect Screen");
        validateDriveConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void remoteConnect(){
        // validating remote connect card + details for 21mm

        sc.startStepsGroup("Remote Connect Screen");
        validateRemoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void goAnywhere(){
        // validating go anywhere  card + details for 21mm

        sc.startStepsGroup("Go Anywhere");
        validateGoAnywhere();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void premium(){
        // validating premium  card + details for 21mm

        sc.startStepsGroup("Premium");
        validatePremium();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void musicLover(){
        // validating music lover  card + details for 21mm

        sc.startStepsGroup("Music Lover");
        validateMusicLover();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void goAnywhereAlreadyExistsTest() {
        if (isStageApp==true){
            sc.startStepsGroup("Test - Go Anywhere subscription Bundle Already Exists");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='ConnectedVehicleScreen_dashboardRemoteTab']"))
            reLaunchApp_iOS();
        checkCancelSubscriptionAvailable("Go Anywhere");
        sc.stopStepsGroup();
    }else createLog("Did not run stage tests");

    }
    @Test
    @Order(10)
    public void addServiceGoAnywhereTest() {
        if (isStageApp == true) {
            sc.startStepsGroup("Test - Add Service Go Anywhere subscription Bundle");
            addServiceGoAnywhere();
            sc.stopStepsGroup();
        } else createLog("Did not run stage tests");
    }


    @Test
    @Order(11)
    public void updateServiceGoAnywhereTest() {
      if (isStageApp==true){
        sc.startStepsGroup("Test - Update Go Anywhere subscription Bundle");
        updateServiceGoAnywhere();
        sc.stopStepsGroup();
    } else createLog("Did not run stage tests");
}

    @Test
    @Order(12)
    public void cancelServiceGoAnywhereTest() {
        if(isStageApp==true){
        sc.startStepsGroup("Test - Cancel Go Anywhere subscription Bundle");
        cancelServiceGoAnywhere();
        sc.stopStepsGroup();
        }else createLog("Did not run stage tests");
    }

    @Test
    @Order(13)
    public void addMusicLoverService() {
        if (isStageApp==true){
        sc.startStepsGroup("Add Music Lover Service");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='ConnectedVehicleScreen_dashboardRemoteTab']"))
            reLaunchApp_iOS();
        addServiceMusicLover();
        sc.stopStepsGroup();
        }else createLog("Did not run stage tests");
    }

    @Test
    @Order(14)
    public void updateMusicLoverService() {
        if (isStageApp){
        sc.startStepsGroup("Update Music Lover Service");
        updateServiceMusicLover();
        sc.stopStepsGroup();
        }else createLog("Did not run stage tests");
    }

    @Test
    @Order(15)
    public void cancelMusicLoverService() {
        if (isStageApp){
        sc.startStepsGroup("Cancel Music Lover Service");
        cancelServiceMusicLover();
        sc.stopStepsGroup();
        }else createLog("Did not run stage tests");
    }
    @Test
    @Order(16)
    public void cancelServicePremiumTestIfExist() {
        if (isStageApp==true){
        sc.startStepsGroup("Test - Cancel Premium subscription Bundle");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='ConnectedVehicleScreen_dashboardRemoteTab']"))
            reLaunchApp_iOS();
        cancelIfPremiumDisplayed();
        sc.stopStepsGroup();
    }else createLog("Did not run stage tests");
}

    @Test
    @Order(17)
    public void addServicePremiumTest() {
        if (isStageApp==true){
        sc.startStepsGroup("Test - Add Service Premium subscription Bundle");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='ConnectedVehicleScreen_dashboardRemoteTab']"))
            reLaunchApp_iOS();
        addServicePremium();
        sc.stopStepsGroup();
        }else createLog("Did not run stage tests");
    }

    @Test
    @Order(18)
    public void updateServicePremiumTest() {
        if (isStageApp==true){
        sc.startStepsGroup("Test - Update Premium subscription Bundle");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='ConnectedVehicleScreen_dashboardRemoteTab']"))
            reLaunchApp_iOS();
        updateServicePremium();
        sc.stopStepsGroup();
        }else createLog("Did not run stage tests");
    }

    @Test
    @Order(19)
    public void cancelServicePremiumTest() {
        if (isStageApp==true){
        sc.startStepsGroup("Test - Cancel Premium subscription Bundle");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='ConnectedVehicleScreen_dashboardRemoteTab']"))
            reLaunchApp_iOS();
        cancelPremiumSubscription();
        sc.stopStepsGroup();
        }else createLog("Did not run stage tests");
    }

    @Test
    @Order(20)
    public void SignOut(){
        ios_emailSignOut();
    }
    public static void validateSubscriptionCard(){
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='IconShopHighlight']"))
            click("NATIVE","xpath=//*[@id='IconShop']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@id='Subscriptions']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='shop_manage_subscription_button']",0);
        click("NATIVE","xpath=//*[@id='shop_manage_subscription_button']",0,1);
    }

    public static void validateSubscriptionScreen(){
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@id='Subscriptions']",0);
        verifyElementFound("NATIVE","xpath=//*[@label='Trial Services']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Add Service']",0);
    }

    public static void validateSafetyConnect(){
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@value='Safety Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@label='subscription_safety_connect']",0);
        if(sc.isElementFound("NATIVE","xpath=(//*[@value='Up to 10-years, 4G Network Dependent'])[1]"))
            verifyElementFound("NATIVE","xpath=(//*[@value='Up to 10-years, 4G Network Dependent'])[1]",0);
        else
            verifyElementFound("NATIVE","xpath=(//*[@text='Safety Connect']/following::*[contains(@label,'Expires')])[1]",0);
        click("NATIVE","xpath=//*[@label='subscription_safety_connect']",0,1);

        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE","xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Cancel' or @id='CANCEL']",0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
    }

    public static void validateDriveConnect(){
        sc.syncElements(3000, 30000);
        click("NATIVE","xpath=//*[@id='shop_manage_subscription_button']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@label='Drive Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@label='Active']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@value,'Expires')]",1);
        verifyElementFound("NATIVE","xpath=//*[@label='subscription_drive_connect']",0);

        click("NATIVE","xpath=//*[@label='subscription_drive_connect']",0,1);

        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE","xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@value,'Drive')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Cancel' or @id='CANCEL']",0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
    }

    public static void validateRemoteConnect(){
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[contains(@value,'Remote Connect')]",0);
        verifyElementFound("NATIVE","xpath=(//*[contains(@value,'Remote Connect')]/following::*[@label='Active'])[1]",0);
        verifyElementFound("NATIVE","xpath=(//*[contains(@text,'Remote Connect')]/following::*[contains(@value,'Expires')])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[@label='subscription_remote_connect']",0);

        click("NATIVE","xpath=//*[@label='subscription_remote_connect']",0,1);

        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE","xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'With Remote Connect, manage your vehicle from the palm of your hand.') or contains(@id,'With Remote Connect, monitor battery status, locate the nearest charging station and more') or contains(@id,'Manage your vehicle from the palm of your hand') or contains(@id,'With features including engine start/stop, door lock/unlock, cabin climate controls and Digital Key – which gives others access to your vehicle')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Cancel' or @id='CANCEL']",0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);

    }
    public static void validateGoAnywhere(){
        sc.syncElements(3000, 30000);
        click("NATIVE","xpath=//*[@id='Add Service']",0,1);
        sc.syncElements(15000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Go Anywhere']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Go Anywhere']/following-sibling::*[@id='SUBSCRIPTIONS_CHECKBOX_UNCHECKED_ICON']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[contains(@text,'/Monthly')]", 0);
        verifyElementFound("NATIVE","xpath=//*[@text='Go Anywhere']/preceding-sibling::*[@id='SUBSCRIPTIONS_ARROW_ICON']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Go Anywhere']/following-sibling::*[@text='Drive Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Go Anywhere']/following-sibling::*[contains(@id,'Remote Connect')]",0);
        sc.syncElements(1000, 3000);

        //click on arrow next to subscription bundle name to navigate to Service Details screen
        sc.click("NATIVE", "xpath=//*[@text='Go Anywhere']/preceding-sibling::*[@text='subscription chevronRight']", 0, 1);
        sc.syncElements(10000, 60000);
        createLog("Verifying Go Anywhere subscription bundling Service Details screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere' or @text='GO ANYWHERE']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Subscription enables Drive Connect features')])[1]", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        createLog("Verified Go Anywhere subscription bundling Service Details screen");
    }

    public static void validatePremium(){
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Premium']/following-sibling::*[@id='SUBSCRIPTIONS_CHECKBOX_UNCHECKED_ICON']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Premium']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']/following-sibling::*[contains(@text,'/Monthly')]", 0);
        sc.swipe("DOWN",sc.p2cy(75),5000);
        verifyElementFound("NATIVE","xpath=//*[@text='Premium']/preceding-sibling::*[@id='SUBSCRIPTIONS_ARROW_ICON']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Premium']/following-sibling::*[@text='Drive Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Premium']/following-sibling::*[@text='Integrated Streaming']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Premium']/following-sibling::*[contains(@id,'Remote Connect')]",0);

        //click on arrow next to subscription bundle name to navigate to Service Details screen
        sc.click("NATIVE", "xpath=//*[@text='Premium']/preceding-sibling::*[@text='subscription chevronRight']", 0, 1);
        sc.syncElements(10000, 60000);
        createLog("Verifying Premium subscription bundling Service Details screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium' or @text='PREMIUM']", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        createLog("Verified Premium subscription bundling Service Details screen");
        sc.syncElements(4000, 8000);
    }

    public static void validateMusicLover(){
        sc.swipe("DOWN",sc.p2cy(25),5000);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Music Lover']/following-sibling::*[@id='SUBSCRIPTIONS_CHECKBOX_UNCHECKED_ICON']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Music Lover']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover']/following-sibling::*[contains(@text,'/Monthly')]", 0);
        verifyElementFound("NATIVE","xpath=//*[@text='Music Lover']/preceding-sibling::*[@id='SUBSCRIPTIONS_ARROW_ICON']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Premium']/following-sibling::*[@text='Integrated Streaming']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Premium']/following-sibling::*[contains(@id,'Remote Connect')]",0);

        //select music lover service details screen
        click("NATIVE", "xpath=(//*[@text='Music Lover']/preceding-sibling::*[@text='subscription chevronRight'])", 0, 1);
        sc.syncElements(5000, 30000);
        createLog("Verifying Music Lover Service Details");
        verifyElementFound("NATIVE", "xpath=//*[@text='MUSIC LOVER' or @text='Music Lover']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='MUSIC LOVER' or @text='Music Lover']", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@id='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Integrated Streaming']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        createLog("Verified Music Lover Service Details");
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(5000, 30000);
        //Manage Subscriptions screen
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
//        sc.syncElements(5000, 30000);
//        click("NATIVE","xpath=//*[@id='SubscriptionScreen_backAction']",0,1);
//        sc.syncElements(5000, 30000);
//        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
    }

    public static void checkCancelSubscriptionAvailable(String paidSubscriptionBundle) {
        createLog("Checking cancel subscription available for "+paidSubscriptionBundle+"");
        boolean existingSubFound = false;
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Add Service']")) {
            //click on shop tab
            verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
            click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
            sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
            //verify Manage Subscriptions
            sc.syncElements(20000, 60000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        }

        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        sc.swipe("Down", sc.p2cy(30), 2000);
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
            verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0);

            click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
            sc.syncElements(10000, 60000);

            createLog("Verify Update Subscription screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
            sc.syncElements(5000, 60000);

            if(sc.isElementFound("NATIVE","xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton']")) {
                createLog("cancel subscription is displayed");
                isCancelSubscriptionAvailable = true;
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
                //bug created for paid services locator
                //verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);
                //Check Auto Renew off
                sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@label,'Go Anywhere')]", 0, 1000, 5, false);
                verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew Off'])[1]", 0);

                //Add service
                addService(false);

                //Update subscription - Auto renew off
                sc.syncElements(5000, 60000);
                updateServiceGoAnywhere();

                click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
                sc.syncElements(10000, 60000);
                createLog("Verify Update Subscription screen");
                verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
                sc.syncElements(5000, 60000);
            }

            //Cancel subscription button is enabled
            verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0);
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
            sc.syncElements(10000, 60000);

            //Verify Cancellation Complete screen
            createLog("Verifying Cancellation Complete screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Cancellation Complete' or @label='CANCELLATION COMPLETE') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
            click("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAButton']", 0, 1);
            createLog("Verified Cancellation Complete screen");
            sc.syncElements(10000, 30000);
            createLog("Cancelled existing paid subscription : "+paidSubscriptionBundle+"");
        } else {
            //Navigate to dashboard screen
            //click Back in Subscriptions screen
            createLog("Navigating to dashboard screen");
            click("NATIVE", "xpath=//*[@text='back_button']", 0, 1);
            click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
            sc.syncElements(5000, 30000);
            sc.flickElement("NATIVE", "xpath=//*[@id='Vehicle image, double tap to open vehicle info.']", 0, "Down");
            sc.syncElements(10000, 30000);
            createLog("Navigated to dashboard screen");
        }
        createLog("Checked cancel subscription available for "+paidSubscriptionBundle+"");
    }
    public static void addService(boolean isVerifyCount) {
        createLog("Verifying add service Go Anywhere subscription bundling");
        //Paid subscriptions
        // verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);

        //Add Service
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Add Service']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@label='Add Service']", 0);
        sc.click("NATIVE", "xpath=//*[@label='Add Service']", 0, 1);
        sc.syncElements(20000, 60000);

        createLog("Verifying Manage Subscriptions screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_TABLE_VIEW']//following-sibling::*[@text='Paid Services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Purchase additional services for your') and @knownSuperClass='UIAccessibilityElement']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Go Anywhere']/parent::*[@class='UIAView']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Go Anywhere']//following::*[@text='Drive Connect'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Go Anywhere']//following::*[contains(@text,'Remote Connect')])[1]", 0);

        // $15.00/Monthly
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
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Go Anywhere']/parent::*[@class='UIAView']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/preceding-sibling::*[@text='subscription chevronRight']", 0);
        createLog("Verified Manage Subscriptions screen");

        //click on arrow next to subscription bundle name to navigate to Service Details screen
        sc.click("NATIVE", "xpath=//*[@text='Go Anywhere']/preceding-sibling::*[@text='subscription chevronRight']", 0, 1);
        sc.syncElements(10000, 60000);
        createLog("Verifying Go Anywhere subscription bundling Service Details screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere' or @text='GO ANYWHERE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Subscription enables Drive Connect features. Getting around doesn') and contains(@text,'t get much easier. Stay connected on the road with up to date navigation, live agent navigation assistance and a seamless virtual assistant. Remote Connect is included at no additional cost with this subscription. With Remote Connect, manage your vehicle from the palm of your hand. Start the engine, lock the doors, adjust climate settings and receive guest driver notifications all from the Lexus app.')]", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        createLog("Verified Go Anywhere subscription bundling Service Details screen");

        createLog("Verify selecting Go Anywhere subscription in Manage Subscription screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAStaticText']", 0);
        //check Continue to purchase button is disabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);

        //verify subscription checkbox is unchecked
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_CHECKBOX_UNCHECKED_ICON']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@text='subscription uncheck']", 0);
        click("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@text='subscription uncheck']", 0, 1);
        sc.syncElements(20000, 60000);
        createLog("Selecting Go Anywhere subscription in SELECT SUBSCRIPTION screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Select Subscription' or @label='SELECT SUBSCRIPTION') and @class='UIAStaticText']", 0);
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
        //Auto renew is unchecked
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_UNCHECKED']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0);
        click("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0, 1);
        //Auto renew is checked
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_CHECKED']", 0);

        //verify continue button is disabled and subscription icon is unchecked
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='false']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere' or @text='SELECT A SUBSCRIPTION FOR GO ANYWHERE']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_UNCHECKED_ICON']", 0);

        //select subscription
        click("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere' or @text='SELECT A SUBSCRIPTION FOR GO ANYWHERE']/following::*[@text='subscription uncheck']", 0, 1);
        sc.syncElements(3000, 9000);
        //verify subscription icon is checked and continue button is enabled
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere' or @text='SELECT A SUBSCRIPTION FOR GO ANYWHERE']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_CHECKED_ICON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='true']", 0);
        click("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 50000);
        createLog("Selected Go Anywhere subscription in SELECT SUBSCRIPTION screen");

        //verify subscription checkbox is checked and Continue to purchase button is enabled in Manage Subscription screen
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified selecting Go Anywhere subscription in Manage Subscription screen");

        //click on Continue to purchase
        sc.click("NATIVE", "xpath=//*[@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE' and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(15000, 60000);
        //consent screen
        verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[1]", 0, 1000, 5, true);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[2]", 0, 1000, 5, true);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[3]", 0, 1000, 5, true);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Amber Alert Assistance']//following::*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT']", 0, 1000, 10, true);
        sc.swipe("Down", sc.p2cy(70), 2000);
        sc.click("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']", 0, 1);

        sc.swipe("Down", sc.p2cy(30), 500);
        if (sc.isElementFound("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']"))
            sc.click("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']", 0, 1);

        //Home address
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0);
            click("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0, 1);
        }
        sc.syncElements(5000, 30000);
        //Address verification screen
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@text='Continue With This Address' or @text='CONTINUE WITH THIS ADDRESS') and @class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[(@text='Continue With This Address' or @text='CONTINUE WITH THIS ADDRESS') and @class='UIAButton']", 0, 1);
        }
        sc.syncElements(10000, 30000);

        //Payment method screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Payment Method' or @text='PAYMENT METHOD']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0);
        click("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0, 1);
        sc.syncElements(5000, 30000);

        //Checkout screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Checkout' or @text='CHECKOUT']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleImageView' and @width>250 and @height>100]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='" + strVin + "']", 0);
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
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='swipeToPay' and @visible='true']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Swiping \"Swipe to Pay\" authorizes the scheduled charges to the credit card provided as described in the Terms of Use including the Refund Policy')]", 0);
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
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);

        //Verify subscription is added
        //click vehicle image from Dashboard
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        sc.syncElements(5000, 30000);

        //verify subscription count
        //verifySubscriptionCount(isVerifyCount);

        //verify Manage Subscriptions
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);

        createLog("Verifying Go Anywhere subscription added under Paid Services section");
        //bug is creeated for paid services
        //   verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0, 1000, 5, false);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Auto Renew On')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'The next billing date')]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[@text='Drive Connect'])[1]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[@text='Drive Connect'])[1]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[@text='Remote Connect')[1]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[contains(@text,'Remote Connect')])[1]", 0);
        String subscriptionBundleDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'Go Anywhere')]",0,"value");
        createLog("Paid Service Subscription Bundle Details: "+subscriptionBundleDetails);
        createLog("Verified Go Anywhere subscription added under Paid Services section");

        //click on Go Anywhere subscription -
        click("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0, 1);
        sc.syncElements(20000, 60000);
        createLog("Verifying Go Anywhere subscription bundling Service Details screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere' or @text='GO ANYWHERE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Subscription enables Drive Connect features. Getting around doesn') and contains(@text,'t get much easier. Stay connected on the road with up to date navigation, live agent navigation assistance and a seamless virtual assistant. Remote Connect is included at no additional cost with this subscription. With Remote Connect, manage your vehicle from the palm of your hand. Start the engine, lock the doors, adjust climate settings and receive guest driver notifications all from the Lexus app.')]", 0);
        //verify Auto Renew On
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew On']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'The next billing date is')]", 0);
        String autoRenewDateDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'The next billing date is')]",0,"value");
        createLog("Auto renew next billing date details: "+autoRenewDateDetails);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0);
        createLog("Verified Go Anywhere subscription bundling Service Details screen");

        createLog("Verifying add service Go Anywhere subscription bundling");
    }

    public static void updateServiceGoAnywhere() {
        if(isCancelSubscriptionAvailable) {
            createLog("Verifying update Go Anywhere subscription");
            //click on Manage Subscription to update subscription
            click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
            sc.syncElements(20000, 60000);

            createLog("Verify Update Subscription screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew On']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'The next billing date is')]", 0);
            String autoRenewDateDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'The next billing date is')]",0,"value");
            createLog("Auto renew next billing date details: "+autoRenewDateDetails);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]/following::*[@text='Drive Connect']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]/following::*[contains(@text,'Remote Connect')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew' and @class='UIAButton']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='check empty']", 0);
            //check box - Auto renew
            switch (strAppType.toLowerCase()) {
                case ("toyota"):
                    verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'If you select \"Auto Renew\", you authorize us to automatically renew your Service subscription for the chosen subscription length beginning on the scheduled expiration date, and thereafter annually or monthly (depending on the selected subscription term), by charging the then current fee to the default payment method on your account unless you cancel your subscription or turn off \"Auto Renew\".') and contains(@text,'After Remote Connect trial ends, Remote Connect is included at no additional cost with a paid plan. Services require 4G cellular network availability.')])[1]", 0);
                    break;
                case ("lexus"):
                    verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'IF YOU SELECT \"AUTO RENEW\", YOU AUTHORIZE US TO AUTOMATICALLY RENEW YOUR SERVICE SUBSCRIPTION FOR THE CHOSEN SUBSCRIPTION LENGTH BEGINNING ON THE SCHEDULED EXPIRATION DATE, AND THEREAFTER ANNUALLY OR MONTHLY (DEPENDING ON THE SELECTED SUBSCRIPTION TERM), BY CHARGING THE THEN CURRENT FEE TO THE DEFAULT PAYMENT METHOD ON YOUR ACCOUNT UNLESS YOU CANCEL YOUR SUBSCRIPTION OR TURN OFF \"AUTO RENEW\".') and contains(@text,'AFTER REMOTE CONNECT TRIAL ENDS, REMOTE CONNECT IS INCLUDED AT NO ADDITIONAL COST WITH A PAID PLAN. SERVICES REQUIRE 4G CELLULAR NETWORK AVAILABILITY.')])[1]", 0);
                    break;
            }

            //update subscription button is not enabled
            verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @class='UIAButton' and @enabled='false']", 0);
            //un-check Auto Renew
            click("NATIVE", "xpath=//*[@text='check empty']", 0, 1);
            //update subscription button is enabled
            verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0);
            createLog("Verified Update Subscription screen");

            //click Update Subscription
            click("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0, 1);
            sc.syncElements(10000, 60000);

            //Verify Update Complete screen
            createLog("Verifying Update Complete screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Update Complete' or @label='UPDATE COMPLETE') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@label='Update Complete' or @label='UPDATE COMPLETE') and @class='UIAStaticText']/following::*[@class='UIAImage']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@label='Subscriptions Updated!' or @label='SUBSCRIPTIONS UPDATED!') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Your subscription is no longer going to auto renew']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
            click("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAButton']", 0, 1);
            createLog("Verified Update Complete screen");

            //Verify subscription is updated
            //click vehicle image from Dashboard
            verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
            click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
            sc.syncElements(5000, 30000);
            //verify Subscriptions
            sc.syncElements(20000, 60000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);

            createLog("Verifying Go Anywhere subscription updated under Paid Services section");
            // bug created for paid service loc issue
            //  verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0, 1000, 5, false);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Auto Renew Off')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[@text='Drive Connect'])[1]", 0, 1000, 5, false);
            verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[@text='Drive Connect'])[1]", 0);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[contains(@text,'Remote Connect')])[1]", 0, 1000, 5, false);
            verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following::*[contains(@text,'Remote Connect')])[1]", 0);
            String subscriptionBundleDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'Go Anywhere')]",0,"value");
            createLog("Paid Service Subscription Bundle Details: "+subscriptionBundleDetails);
            createLog("Verified Go Anywhere subscription updated under Paid Services section");

            //click on Go Anywhere subscription - check Auto Renew Off in Subscriptions screen
            click("NATIVE", "xpath=(//*[contains(@text,'Go Anywhere')]/following-sibling::*[@text='back_button'])[1]", 0, 1);
            sc.syncElements(20000, 60000);
            createLog("Verifying Go Anywhere subscription bundling Service Details screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere' or @text='GO ANYWHERE']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Subscription enables Drive Connect features. Getting around doesn') and contains(@text,'t get much easier. Stay connected on the road with up to date navigation, live agent navigation assistance and a seamless virtual assistant. Remote Connect is included at no additional cost with this subscription. With Remote Connect, manage your vehicle from the palm of your hand. Start the engine, lock the doors, adjust climate settings and receive guest driver notifications all from the Lexus app.')]", 0);
            //verify Auto Renew Off
            createLog("Verifying Auto Renew Off in Service Details Screen");
            verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew Off']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0);
            String expirationDateDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'Expires On')]",0,"value");
            createLog("Expiration date details: "+expirationDateDetails);
            createLog("Verifying Auto Renew Off in Service Details Screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0);
            createLog("Verified Go Anywhere subscription bundling Service Details screen");

            createLog("Verified update Go Anywhere subscription");
        } else {
            createLog("Go Anywhere is only one subscription enabled so cannot be cancelled");
            sc.report("Go Anywhere is only one subscription enabled so cannot be cancelled",false);
        }
    }
    public void addServiceGoAnywhere()  {
        if(isCancelSubscriptionAvailable) {
            //cancel existing subscription if present
            cancelExistingSubscription("Go Anywhere");

            click("NATIVE", "xpath=//*[@id='IconShop']", 0, 1);
            sc.syncElements(5000, 30000);
            sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
            sc.syncElements(20000, 60000);

            //add go anywhere subscription
            addService(true);

            createLog("Verified Go Anywhere subscription is added successfully");
        } else {
            createLog("Go Anywhere is only one subscription enabled so cannot be cancelled");
            sc.report("Go Anywhere is only one subscription enabled so cannot be cancelled",false);
        }
    }
    public void cancelExistingSubscription(String paidSubscriptionBundle) {
        boolean existingSubFound = false;
        createLog("Checking existing "+paidSubscriptionBundle+" paid subscription in subscriptions screen");

        //click on SHOP
        click("NATIVE", "xpath=//*[@id='IconShop']", 0, 1);
        sc.syncElements(5000, 30000);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        //verify Manage Subscriptions
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        // bug created for paid services
        // verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);

        for (int i=1; i<4; i++) {
            if(sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'"+paidSubscriptionBundle+"')]")) {
                createLog("Existing paid subscription : "+paidSubscriptionBundle+" found");
                existingSubFound = true;
                break;
            } else {
                sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'"+paidSubscriptionBundle+"')]", 0, 1000, i, false);
                createLog("Existing paid subscription "+paidSubscriptionBundle+" not found after "+i+" rounds of swipe");
            }
        }

        if(existingSubFound) {
            createLog("Cancelling existing paid subscription");
            click("NATIVE", "xpath=//*[contains(@text,'"+paidSubscriptionBundle+"')]", 0, 1);
            sc.syncElements(20000, 60000);
            createLog("Verifying Go Anywhere subscription bundling Service Details screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0);

            click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
            sc.syncElements(10000, 60000);

            createLog("Verify Update Subscription screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);

            //Cancel subscription button is enabled
            verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0);
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
            sc.syncElements(10000, 60000);

            //Verify Cancellation Complete screen
            createLog("Verifying Cancellation Complete screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Cancellation Complete' or @label='CANCELLATION COMPLETE') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
            click("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAButton']", 0, 1);
            createLog("Verified Cancellation Complete screen");
            sc.syncElements(10000, 30000);
            createLog("Cancelled existing paid subscription : "+paidSubscriptionBundle+"");
        } else {

            verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
            click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
            sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
            //verify Manage Subscriptions
        }
    }

    public void cancelServiceGoAnywhere() {
        if(isCancelSubscriptionAvailable) {
            createLog("Verifying Cancel Go Anywhere subscription");
            //click on Manage Subscription to update subscription
            click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
            sc.syncElements(10000, 60000);

            createLog("Verify Update Subscription screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew Off']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0);
            String expirationDateDetails = sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'Expires On')]",0,"value");
            createLog("Expiration date details: "+expirationDateDetails);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]/following::*[@text='Drive Connect']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]/following::*[contains(@text,'Remote Connect')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew' and @class='UIAButton']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='check empty']", 0);
            //check box - Auto renew
            switch (strAppType.toLowerCase()) {
                case ("toyota"):
                    verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'If you select \"Auto Renew\", you authorize us to automatically renew your Service subscription for the chosen subscription length beginning on the scheduled expiration date, and thereafter annually or monthly (depending on the selected subscription term), by charging the then current fee to the default payment method on your account unless you cancel your subscription or turn off \"Auto Renew\".') and contains(@text,'After Remote Connect trial ends, Remote Connect is included at no additional cost with a paid plan. Services require 4G cellular network availability.')])[1]", 0);
                    break;
                case ("lexus"):
                    verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'IF YOU SELECT \"AUTO RENEW\", YOU AUTHORIZE US TO AUTOMATICALLY RENEW YOUR SERVICE SUBSCRIPTION FOR THE CHOSEN SUBSCRIPTION LENGTH BEGINNING ON THE SCHEDULED EXPIRATION DATE, AND THEREAFTER ANNUALLY OR MONTHLY (DEPENDING ON THE SELECTED SUBSCRIPTION TERM), BY CHARGING THE THEN CURRENT FEE TO THE DEFAULT PAYMENT METHOD ON YOUR ACCOUNT UNLESS YOU CANCEL YOUR SUBSCRIPTION OR TURN OFF \"AUTO RENEW\".') and contains(@text,'AFTER REMOTE CONNECT TRIAL ENDS, REMOTE CONNECT IS INCLUDED AT NO ADDITIONAL COST WITH A PAID PLAN. SERVICES REQUIRE 4G CELLULAR NETWORK AVAILABILITY.')])[1]", 0);
                    break;
            }

            //Cancel subscription button is enabled
            verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0);
            createLog("Verified Update Subscription screen");

            //click Cancel Subscription
            click("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0, 1);
            sc.syncElements(10000, 60000);
            createLog("Verify Cancel Subscription screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Are you sure you want to cancel Go Anywhere?']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Subscription enables Drive Connect features. Getting around doesn') and contains(@text,'t get much easier. Stay connected on the road with up to date navigation, live agent navigation assistance and a seamless virtual assistant. Remote Connect is included at no additional cost with this subscription. With Remote Connect, manage your vehicle from the palm of your hand. Start the engine, lock the doors, adjust climate settings and receive guest driver notifications all from the Lexus app.')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Reason For Cancellation']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Cancelling the subscription will turn off this service* and any scheduled payments will not be processed.\\n*Monthly subscription service will remain active through the end of the current billing cycle.']", 0);

            //Confirm Cancellation button is not enabled
            verifyElementFound("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='false']", 0);
            //Select Reason for cancellation
            click("NATIVE", "xpath=//*[@text='Reason For Cancellation']/following-sibling::*[@class='UIAButton']", 0, 1);
            sc.syncElements(3000, 9000);
            sc.setPickerValues("NATIVE", "xpath=//*[@class='UIAPicker']", 0, 0, "down:1");
            //Confirm Cancellation button is enabled
            verifyElementFound("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='true']", 0);
            createLog("Verified Update Subscription screen");

            //click Confirm Subscription
            click("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='true']", 0, 1);
            sc.syncElements(10000, 60000);
            verifyElementFound("NATIVE", "xpath=//*[@text='You confirm that you no longer want Go Anywhere services']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Confirm']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Cancel']", 0);
            click("NATIVE", "xpath=//*[@text='Confirm']", 0, 1);
            sc.syncElements(10000, 60000);

            //Verify Cancellation Complete screen
            createLog("Verifying Cancellation Complete screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Cancellation Complete' or @label='CANCELLATION COMPLETE') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@label='Cancellation Complete' or @label='CANCELLATION COMPLETE') and @class='UIAStaticText']/following::*[@class='UIAImage']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Your cancellation request was successful.' or @text='YOUR CANCELLATION REQUEST WAS SUCCESSFUL.']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
            click("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAButton']", 0, 1);
            createLog("Verified Cancellation Complete screen");
            sc.syncElements(5000, 30000);

            sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
            //verify Subscriptions
            sc.syncElements(20000, 60000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);

            createLog("Verifying Go Anywhere subscription is not displayed under Paid Services section after cancel");
            //  verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);
            sc.swipe("Down", sc.p2cy(30), 500);
            sc.verifyElementNotFound("NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0);
            createLog("Verified Go Anywhere subscription is not displayed under Paid Services section after cancel");
            createLog("Verified Cancel Go Anywhere subscription");

            //navigate to dashboard
            //click Back in Subscriptions screen
            click("NATIVE", "xpath=//*[@text='back_button']", 0, 1);
            sc.syncElements(5000, 30000);
            sc.flickElement("NATIVE", "xpath=//*[@label='vehicle_image']", 0, "Down");
            sc.syncElements(5000, 30000);
        } else {
            createLog("Go Anywhere is only one subscription enabled so cannot be cancelled");
            sc.report("Go Anywhere is only one subscription enabled so cannot be cancelled",false);
        }
    }
    public void addServiceMusicLover(){
        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Subscriptions']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Add Service']", 0, 1000, 5, false);
        click("NATIVE", "xpath=//*[@id='Add Service']", 0, 1);
        sc.syncElements(5000, 30000);

        //manage subscription screen
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Paid Services'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@text='Music Lover']/preceding-sibling::*[@text='subscription chevronRight'])", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Integrated Streaming']", 1);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 2);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Music Lover']//following::*[contains(@text,'15.00/Monthly')])[1]", 0);
        click("NATIVE", "xpath=(//*[@text='Music Lover']/preceding-sibling::*[@text='subscription chevronRight'])", 0, 1);
        sc.syncElements(5000, 30000);

        //select music lover screen
        createLog("Verifying Music Lover Service Details");
        verifyElementFound("NATIVE", "xpath=//*[@text='MUSIC LOVER' or @text='Music Lover']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='MUSIC LOVER' or @text='Music Lover']", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@id='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Integrated Streaming']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        createLog("Verified Music Lover Service Details");
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);


        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAStaticText']", 0);
        //check Continue to purchase button is disabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);
        createLog("Continue to purchase button is disabled");

        //verify subscription checkbox is unchecked
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover']/following-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_CHECKBOX_UNCHECKED_ICON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover']/following-sibling::*[@text='subscription uncheck']", 0);
        click("NATIVE", "xpath=//*[@text='Music Lover']/following-sibling::*[@text='subscription uncheck']", 0, 1);
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Select Subscription' or @label='SELECT SUBSCRIPTION') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Music Lover' or @text='SELECT A SUBSCRIPTION FOR MUSIC LOVER']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Music Lover'])", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]", 0);
        //Auto renew is unchecked
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_UNCHECKED']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0);
        click("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0, 1);
        //Auto renew is checked
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_CHECKED']", 0);

        //Continue button is disabled
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='false']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Music Lover' or @text='SELECT A SUBSCRIPTION FOR MUSIC LOVER']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_UNCHECKED_ICON']", 0);

        //select subscription
        click("NATIVE", "xpath=//*[@text='Select a Subscription for Music Lover' or @text='SELECT A SUBSCRIPTION FOR MUSIC LOVER']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_UNCHECKED_ICON']", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_CHECKED_ICON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='true']", 0);
        click("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 50000);

        //subscription checkbox is checked and Continue to purchase button enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified selecting Music Lover subscription");

        //click on Continue to purchase
        sc.click("NATIVE", "xpath=//*[@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE' and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(15000, 60000);
        //consent screen
        verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[1]", 0, 1000, 5, true);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[2]", 0, 1000, 5, true);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[3]", 0, 1000, 5, true);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Amber Alert Assistance']//following::*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT']", 0, 1000, 10, true);
        sc.swipe("Down", sc.p2cy(70), 2000);
        sc.click("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']", 0, 1);

        sc.swipe("Down", sc.p2cy(30), 500);
        if (sc.isElementFound("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']"))
            sc.click("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']", 0, 1);

        //Home address validation
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0);
            click("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0, 1);
        }
        sc.syncElements(5000, 30000);
        //Address verification screen
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@text='Continue With This Address' or @text='CONTINUE WITH THIS ADDRESS') and @class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[(@text='Continue With This Address' or @text='CONTINUE WITH THIS ADDRESS') and @class='UIAButton']", 0, 1);
        }
        sc.syncElements(10000, 30000);

        //Payment validation
        verifyElementFound("NATIVE", "xpath=//*[@text='Payment Method' or @text='PAYMENT METHOD']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0);
        click("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0, 1);
        sc.syncElements(5000, 30000);

        //Checkout screen validation
        verifyElementFound("NATIVE", "xpath=//*[@text='Checkout' or @text='CHECKOUT']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicleImageView']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Month')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover']//following::*[contains(@text,'/Month')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='check empty']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Integrated Streaming']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Pay From']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Subtotal']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Subtotal']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Tax']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Tax']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Total']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Total']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);

        //terms validation
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='swipeToPay' and @visible='true']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Swiping \"Swipe to Pay\" authorizes the scheduled charges to the credit card provided as described in the Terms of Use including the Refund Policy')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='swipeToPay']", 0);
        sc.drag("NATIVE", "xpath=//*[@class='UIAView' and ./*[@class='UIAImage'] and ./parent::*[@text='swipeToPay']]", 0, 1000, 0);
        sc.syncElements(20000, 60000);

        //Success Screen validation
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Success']") && sc.isElementFound("NATIVE", "xpath=//*[@text='Purchase Submitted']")) {
            createLog("Music lover Subscription successful");
        } else {
            createLog("Music Lover Subscription is not successful");
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Success']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Success']/following-sibling::*[@class='UIAImage'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Purchase Submitted']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Month')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Integrated Streaming']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);

        verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='returnToDashboardButton']", 0, 1);
        sc.syncElements(15000, 60000);


        //Verify subscription is added
        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);

        //verify Manage Subscriptions
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);

        createLog("Verifying Music Lover subscription ");
        verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Music Lover')]", 0, 1000, 5, false);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Music Lover')]/following::*[@text='Integrated Streaming'])[1]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Music Lover')]/following::*[@text='Integrated Streaming'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Music Lover')]/following::*[contains(@text,'Remote Connect')])[1]", 0);

    }

    public void updateServiceMusicLover(){
        //click on Music Lover subscription -
        click("NATIVE", "xpath=//*[contains(@text,'Music Lover')]", 0, 1);
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover' or @text='MUSIC LOVER']", 0);
        //verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Subscription enables Drive Connect features. Getting around doesn') and contains(@text,'t get much easier. Stay connected on the road with up to date navigation, live agent navigation assistance and a seamless virtual assistant. This also provides you Remote Connect availability. Remote Connect is included at no additional cost with this subscription. With Remote Connect, manage your vehicle from the palm of your hand. Start the engine, lock the doors, adjust climate settings and receive guest driver notifications all from the Lexus app.')]", 0);
        //verify Auto Renew On
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew On']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'The next billing date is')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0);
        createLog("Verified Music Lover subscription Details screen");

        //click on manage subscription
        sc.click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
        sc.syncElements(20000, 60000);

        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew On']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'The next billing date is')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Music Lover')]/following::*[@text='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Music Lover')]/following::*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew' and @class='UIAButton']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='check empty']", 0);

        switch (strAppType.toLowerCase()) {
            case ("toyota"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'If you select \"Auto Renew\", you authorize us to automatically renew your Service subscription for the chosen subscription length beginning on the scheduled expiration date, and thereafter annually or monthly (depending on the selected subscription term), by charging the then current fee to the default payment method on your account unless you cancel your subscription or turn off \"Auto Renew\".') and contains(@text,'After Remote Connect trial ends, Remote Connect is included at no additional cost with a paid plan. Services require 4G cellular network availability.')])[1]", 0);
                break;
            case ("lexus"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'IF YOU SELECT \"AUTO RENEW\", YOU AUTHORIZE US TO AUTOMATICALLY RENEW YOUR SERVICE SUBSCRIPTION FOR THE CHOSEN SUBSCRIPTION LENGTH BEGINNING ON THE SCHEDULED EXPIRATION DATE, AND THEREAFTER ANNUALLY OR MONTHLY (DEPENDING ON THE SELECTED SUBSCRIPTION TERM), BY CHARGING THE THEN CURRENT FEE TO THE DEFAULT PAYMENT METHOD ON YOUR ACCOUNT UNLESS YOU CANCEL YOUR SUBSCRIPTION OR TURN OFF \"AUTO RENEW\".') and contains(@text,'AFTER REMOTE CONNECT TRIAL ENDS, REMOTE CONNECT IS INCLUDED AT NO ADDITIONAL COST WITH A PAID PLAN. SERVICES REQUIRE 4G CELLULAR NETWORK AVAILABILITY.')])[1]", 0);
                break;
        }

        //update subscription button is not enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @class='UIAButton' and @enabled='false']", 0);
        //un-check Auto Renew
        click("NATIVE", "xpath=//*[@text='check empty']", 0, 1);
        //update subscription button is enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified Update Subscription screen");

        //click Update Subscription
        click("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 60000);

        //Verify Update Complete screen
        createLog("Verifying Update Complete screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Complete' or @label='UPDATE COMPLETE') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Complete' or @label='UPDATE COMPLETE') and @class='UIAStaticText']/following::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Subscriptions Updated!' or @label='SUBSCRIPTIONS UPDATED!') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Your subscription is no longer going to auto renew']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
        click("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAButton']", 0, 1);
        createLog("Verified Update Complete screen");

        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        //verify Manage Subscriptions
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);

        //  verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Go Anywhere')]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Music Lover\\nAuto Renew Off\\nExpires On')]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Music Lover')]/following::*[@text='Integrated Streaming'])[1]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Music Lover')]/following::*[@text='Integrated Streaming'])[1]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Music Lover')]/following::*[contains(@text,'Remote Connect')])[1]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Music Lover')]/following::*[contains(@text,'Remote Connect')])[1]", 0);
        createLog("Verified Music Lover subscription update");

        //click on Music Lover subscription - check Auto Renew Off in Subscriptions screen
        click("NATIVE", "xpath=//*[contains(@text,'Music Lover')]", 0, 1);
        sc.syncElements(20000, 60000);
        createLog("Verifying Music Lover Service Details screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover' or @text='MUSIC LOVER']", 0);
        //verify Auto Renew Off
        createLog("Verifying Auto Renew Off in Service Details Screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew Off']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0);
        createLog("Verified update Music Lover subscription");

    }

    public void cancelServiceMusicLover(){
        click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
        sc.syncElements(10000, 60000);

        createLog("Verify Update Subscription screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Lover']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew Off']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Music Lover')]/following::*[@text='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Music Lover')]/following::*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew' and @class='UIAButton']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='check empty']", 0);
        //check box - Auto renew
        switch (strAppType.toLowerCase()) {
            case ("toyota"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'If you select \"Auto Renew\", you authorize us to automatically renew your Service subscription for the chosen subscription length beginning on the scheduled expiration date, and thereafter annually or monthly (depending on the selected subscription term), by charging the then current fee to the default payment method on your account unless you cancel your subscription or turn off \"Auto Renew\".') and contains(@text,'After Remote Connect trial ends, Remote Connect is included at no additional cost with a paid plan. Services require 4G cellular network availability.')])[1]", 0);
                break;
            case ("lexus"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'IF YOU SELECT \"AUTO RENEW\", YOU AUTHORIZE US TO AUTOMATICALLY RENEW YOUR SERVICE SUBSCRIPTION FOR THE CHOSEN SUBSCRIPTION LENGTH BEGINNING ON THE SCHEDULED EXPIRATION DATE, AND THEREAFTER ANNUALLY OR MONTHLY (DEPENDING ON THE SELECTED SUBSCRIPTION TERM), BY CHARGING THE THEN CURRENT FEE TO THE DEFAULT PAYMENT METHOD ON YOUR ACCOUNT UNLESS YOU CANCEL YOUR SUBSCRIPTION OR TURN OFF \"AUTO RENEW\".') and contains(@text,'AFTER REMOTE CONNECT TRIAL ENDS, REMOTE CONNECT IS INCLUDED AT NO ADDITIONAL COST WITH A PAID PLAN. SERVICES REQUIRE 4G CELLULAR NETWORK AVAILABILITY.')])[1]", 0);
                break;
        }

        //Cancel subscription button is enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified Update Subscription screen");

        //click Cancel Subscription
        click("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 60000);
        createLog("Verify Cancel Subscription screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Are you sure you want to cancel Music Lover?']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Reason For Cancellation']", 0);

        //Confirm Cancellation button is not enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='false']", 0);
        //Select Reason for cancellation
        click("NATIVE", "xpath=//*[@text='Reason For Cancellation']/following-sibling::*[@class='UIAButton']", 0, 1);
        sc.syncElements(3000, 9000);
        sc.setPickerValues("NATIVE", "xpath=//*[@class='UIAPicker']", 0, 0, "down:1");
        //Confirm Cancellation button is enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified Update Subscription screen");

        //click Confirm Subscription
        click("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@text='You confirm that you no longer want Music Lover services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Confirm']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Cancel']", 0);
        click("NATIVE", "xpath=//*[@text='Confirm']", 0, 1);
        sc.syncElements(10000, 60000);

        //Verify Cancellation Complete screen
        createLog("Verifying Cancellation Complete screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Cancellation Complete' or @label='CANCELLATION COMPLETE') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Cancellation Complete' or @label='CANCELLATION COMPLETE') and @class='UIAStaticText']/following::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Your cancellation request was successful.' or @text='YOUR CANCELLATION REQUEST WAS SUCCESSFUL.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
        click("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAButton']", 0, 1);
        createLog("Verified Cancellation Complete screen");
        sc.syncElements(5000, 30000);

        //click vehicle image from Dashboard
        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        //verify Manage Subscriptions
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[contains(@text,'Music Lover')]", 0);
        createLog("Verified Cancel Music Lover subscription");
    }
    public void addServicePremium() {
        //click on vehicle image
        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        //verify Manage Subscriptions
        sc.syncElements(5000, 10000);

        //Add Service
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Add Service']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@label='Add Service']", 0);
        sc.click("NATIVE", "xpath=//*[@label='Add Service']", 0, 1);
        sc.syncElements(20000, 60000);

        createLog("Verifying Manage Subscriptions screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_TABLE_VIEW']//following-sibling::*[@text='Paid Services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Purchase additional services for your') and @knownSuperClass='UIAccessibilityElement']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Premium']/following-sibling::*[@text='Drive Connect']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']/following-sibling::*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']/following-sibling::*[contains(@text,'Remote Connect')]", 0);

        /*if(sc.getElementCount("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]")==2){
            createLog("Premium subscription option: Remote Connect displayed");
        }else{
            createLog("Premium subscription option: Remote Connect not displayed");
        }*/
        // $15.00/Monthly
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']/following-sibling::*[contains(@text,'/Monthly')]", 0);
        String subscriptionAmountDetails = sc.elementGetText("NATIVE", "xpath=//*[@text='Premium']/following-sibling::*[contains(@text,'/Monthly')]", 0);
        createLog("subscription amount details: " + subscriptionAmountDetails);
        String[] subscriptionAmountArr = subscriptionAmountDetails.split("/");
        String subscriptionAmountWithUnit = subscriptionAmountArr[0];
        createLog("subscription amount with unit: " + subscriptionAmountWithUnit);
        String[] amountArr = subscriptionAmountArr[0].split("[.]");
        String amountInt = amountArr[0].substring(1);
        sc.report("Subscription amount is Numeric value ", CommonUtils.isNumeric(amountInt));
        //verify Subscription amount is greater than 0
        sc.report("Subscription amount is greater than 0 ", Integer.parseInt(amountInt) >= 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']/preceding-sibling::*[@text='subscription chevronRight']", 0);
        createLog("Verified Manage Subscriptions screen");

        //click on arrow next to subscription bundle name to navigate to Service Details screen
        sc.click("NATIVE", "xpath=//*[@text='Premium']/preceding-sibling::*[@text='subscription chevronRight']", 0, 1);
        sc.syncElements(10000, 60000);
        createLog("Verifying Premium subscription bundling Service Details screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium' or @text='PREMIUM']", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        createLog("Verified Premium subscription bundling Service Details screen");
        sc.syncElements(4000, 8000);

        createLog("Verify selecting Premium subscription in Manage Subscription screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAStaticText']", 0);
        //check Continue to purchase button is disabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);

        //verify subscription checkbox is unchecked
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']/following-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_CHECKBOX_UNCHECKED_ICON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']/following-sibling::*[@text='subscription uncheck']", 0);

        click("NATIVE", "xpath=//*[@text='Premium']/following-sibling::*[@text='subscription uncheck']", 0, 1);
        sc.syncElements(20000, 60000);
        createLog("Selecting Premium subscription in SELECT SUBSCRIPTION screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Select Subscription' or @label='SELECT SUBSCRIPTION') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Premium' or @text='SELECT A SUBSCRIPTION FOR PREMIUM']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Monthly')]", 0);
        // $25.00/Monthly
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']//following-sibling::*[contains(@text,'/Monthly')]", 0);
        subscriptionAmountDetails = sc.elementGetText("NATIVE", "xpath=//*[@text='Premium']//following-sibling::*[contains(@text,'/Monthly')]", 0);
        createLog("subscription amount details: " + subscriptionAmountDetails);
        subscriptionAmountArr = subscriptionAmountDetails.split("/");
        subscriptionAmountWithUnit = subscriptionAmountArr[0];
        createLog("subscription amount with unit: " + subscriptionAmountWithUnit);
        amountArr = subscriptionAmountArr[0].split("[.]");
        amountInt = amountArr[0].substring(1);
        sc.report("Subscription amount is Numeric value ", CommonUtils.isNumeric(amountInt));
        //verify Subscription amount is greater than 0
        sc.report("Subscription amount is greater than 0 ", Integer.parseInt(amountInt) >= 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Monthly')]/following-sibling::*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Monthly')]/following-sibling::*[@text='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Monthly')]/following-sibling::*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='subscription uncheck']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]", 0);
        switch (strAppType.toLowerCase()) {
            case ("toyota"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'If you select \"Auto Renew\", you authorize us to automatically renew your Service subscription for the chosen subscription length beginning on the scheduled expiration date, and thereafter annually or monthly (depending on the selected subscription term), by charging the then current fee to the default payment method on your account unless you cancel your subscription or turn off \"Auto Renew\".') and contains(@text,'After Remote Connect trial ends, Remote Connect is included at no additional cost with a paid plan. Services require 4G cellular network availability.')])[1]", 0);
                break;
            case ("lexus"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'IF YOU SELECT \"AUTO RENEW\", YOU AUTHORIZE US TO AUTOMATICALLY RENEW YOUR SERVICE SUBSCRIPTION FOR THE CHOSEN SUBSCRIPTION LENGTH BEGINNING ON THE SCHEDULED EXPIRATION DATE, AND THEREAFTER ANNUALLY OR MONTHLY (DEPENDING ON THE SELECTED SUBSCRIPTION TERM), BY CHARGING THE THEN CURRENT FEE TO THE DEFAULT PAYMENT METHOD ON YOUR ACCOUNT UNLESS YOU CANCEL YOUR SUBSCRIPTION OR TURN OFF \"AUTO RENEW\".') and contains(@text,'AFTER REMOTE CONNECT TRIAL ENDS, REMOTE CONNECT IS INCLUDED AT NO ADDITIONAL COST WITH A PAID PLAN. SERVICES REQUIRE 4G CELLULAR NETWORK AVAILABILITY.')])[1]", 0);
                break;
        }
        //Auto renew is unchecked
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_UNCHECKED']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0);
        click("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0, 1);
        //Auto renew is checked
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_CHECKED']", 0);

        //verify continue button is disabled and subscription icon is unchecked
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='false']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Premium' or @text='SELECT A SUBSCRIPTION FOR PREMIUM']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_UNCHECKED_ICON']", 0);

        //select subscription
        click("NATIVE", "xpath=//*[@text='Select a Subscription for Premium' or @text='SELECT A SUBSCRIPTION FOR PREMIUM']/following::*[@text='subscription uncheck']", 0, 1);
        sc.syncElements(3000, 9000);
        //verify subscription icon is checked and continue button is enabled
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Premium' or @text='SELECT A SUBSCRIPTION FOR PREMIUM']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_CHECKED_ICON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='true']", 0);
        click("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 50000);
        createLog("Selected Premium subscription in SELECT SUBSCRIPTION screen");

        //verify subscription checkbox is checked and Continue to purchase button is enabled in Manage Subscription screen
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified selecting Premium subscription in Manage Subscription screen");

        //click on Continue to purchase
        sc.click("NATIVE", "xpath=//*[@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE' and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(15000, 60000);
        //consent screen
        verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);


        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Connected Services Master Data Consent']]]", 0, 1000, 5, false);
        click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Connected Services Master Data Consent']]]", 0, 1);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Service Connect Communication']]]", 0, 1000, 5, false);
        click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Service Connect Communication']]]", 0, 1);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='External Vehicle Video Capture']]]", 0, 1000, 5, false);
        click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='External Vehicle Video Capture']]]", 0, 1);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Amber Alert Assistance']//following::*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT']", 0, 1000, 10, false);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Confirm And Continue' or @text='CONFIRM AND CONTINUE' and @class='UIAButton'", 0, 1000, 5, false);
        click("NATIVE", "xpath=(//*[@text='Confirm and Continue' or @text='CONFIRM AND CONTINUE' and @class='UIAButton'])[1]", 0, 1);
        sc.syncElements(20000, 40000);

//        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[1]", 0, 1000, 5, true);
//        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[2]", 0, 1000, 5, true);
//        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[3]", 0, 1000, 5, true);
//        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Amber Alert Assistance']//following::*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT']", 0, 1000, 10, true);
//        sc.swipe("Down", sc.p2cy(70), 2000);
//        sc.click("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']", 0, 1);

        sc.swipe("Down", sc.p2cy(30), 500);
        if (sc.isElementFound("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']"))
            sc.click("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']", 0, 1);

        //Home address
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0);
            click("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0, 1);
        }
        sc.syncElements(5000, 30000);
        //Address verification screen
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@text='Continue With This Address' or @text='CONTINUE WITH THIS ADDRESS') and @class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[(@text='Continue With This Address' or @text='CONTINUE WITH THIS ADDRESS') and @class='UIAButton']", 0, 1);
        }
        sc.syncElements(5000, 30000);

        //Payment method screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Payment Method' or @text='PAYMENT METHOD']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0);
        click("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0, 1);
        sc.syncElements(5000, 30000);

        //Checkout screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Checkout' or @text='CHECKOUT']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleImageView' and @width>250 and @height>100]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='" + strVin + "']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Month')]", 0);
        // $15/Month
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']//following::*[contains(@text,'/Month')]", 0);
        subscriptionAmountDetails = sc.elementGetText("NATIVE", "xpath=//*[@text='Premium']//following::*[contains(@text,'/Month')]", 0);
        createLog("subscription amount details: " + subscriptionAmountDetails);
        subscriptionAmountArr = subscriptionAmountDetails.split("/");
        subscriptionAmountWithUnit = subscriptionAmountArr[0];
        createLog("subscription amount with unit: " + subscriptionAmountWithUnit);
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
        createLog("Subtotal amount details: " + subTotalWithUnit);
        verifyElementFound("NATIVE", "xpath=//*[@text='Tax']", 0);
        //$0.99
        verifyElementFound("NATIVE", "xpath=//*[@text='Tax']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        String taxWithUnit = sc.elementGetText("NATIVE", "xpath=//*[@text='Tax']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        createLog("Tax amount details: " + taxWithUnit);
        verifyElementFound("NATIVE", "xpath=//*[@text='Total']", 0);
        //$15.99
        verifyElementFound("NATIVE", "xpath=//*[@text='Total']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        String totalAmountWithUnit = sc.elementGetText("NATIVE", "xpath=//*[@text='Total']/following-sibling::*[@accessibilityLabel='valueLabel']", 0);
        createLog("Total amount details: " + totalAmountWithUnit);

        createLog("verifying total value is matching on adding subtotal & tax value in checkout screen");
        double subTotalVal = Double.parseDouble(subTotalWithUnit.substring(1));
        double taxVal = Double.parseDouble(taxWithUnit.substring(1));
        double totalVal = Double.parseDouble(totalAmountWithUnit.substring(1));
        createLog("Double subTotal value: " + subTotalVal);
        createLog("Double tax value: " + taxVal);
        createLog("Double total value: " + totalVal);
        double subTotalAndTax = subTotalVal + taxVal;
        createLog("(subTotal + tax) sum value: " + subTotalAndTax);
        Assertions.assertTrue(subTotalAndTax == totalVal);
        createLog("verified total value is matching on adding subtotal & tax value in checkout screen");

        //terms
        //verifyElementFound("NATIVE", "xpath=//*[@text=concat('Swiping ', '\"', 'Swipe to Pay', '\"', ' authorizes the scheduled charges to the credit card provided as described in the Terms of Use including the Refund Policy')]", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='swipeToPay' and @visible='true']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='swipeToPay']", 0);
        sc.drag("NATIVE", "xpath=//*[@class='UIAView' and ./*[@class='UIAImage'] and ./parent::*[@text='swipeToPay']]", 0, 1000, 0);
        sc.syncElements(20000, 60000);

        //Success Screen
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Success']") && sc.isElementFound("NATIVE", "xpath=//*[@text='Purchase Submitted']")) {
            createLog("Premium Subscription bundling purchase successful");
        } else {
            createLog("Premium Subscription bundling purchase is not successful");
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Success']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Success']/following-sibling::*[@class='UIAImage'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Purchase Submitted']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Month')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Integrated Streaming')]//preceding-sibling::*[@class='UIAImage']", 0);


        createLog("verifying total subscription amount is matching success screen with checkout screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Purchase Submitted']/preceding-sibling::*[@class='UIAStaticText' and contains(@text,'$')]", 0);
        String subscriptionAmount = sc.elementGetText("NATIVE", "xpath=//*[@text='Purchase Submitted']/preceding-sibling::*[@class='UIAStaticText' and contains(@text,'$')]", 0);
        createLog("Subscription amount with unit in success screen: " + subscriptionAmount);
        double subscriptionAmountVal = Double.parseDouble(subscriptionAmount.substring(1));
        createLog("Double subscriptionAmountVal value: " + subscriptionAmountVal);
        createLog("Double total value in Checkout screen: " + totalVal);
        Assertions.assertTrue(subscriptionAmountVal == totalVal);
        createLog("verified total subscription amount is matching success screen with checkout screen");

        verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='returnToDashboardButton']", 0, 1);
        sc.syncElements(15000, 60000);

        //verify dashboard screen
        verifyElementFound("NATIVE", "xpath=//*[@id='account_icon']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        //verify Manage Subscriptions
        sc.syncElements(5000, 10000);

        //verify Manage Subscriptions
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);

        createLog("Verifying Premium subscription added under Paid Services section");
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Premium')]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Premium\\nAuto Renew On\\nThe next billing date')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Premium')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Auto Renew On')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'The next billing date')]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[@text='Drive Connect'])", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[@text='Drive Connect'])[1]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Premium')]/following::*[contains(@text,'Integrated Streaming')]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[contains(@text,'Integrated Streaming')])[1]", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[contains(@text,'Remote Connect')])[1]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[contains(@text,'Remote Connect')])[1]", 0);

        String subscriptionBundleDetails = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Premium')]", 0, "value");
        createLog("Paid Service Subscription Bundle Details: " + subscriptionBundleDetails);
        createLog("Verified Premium subscription added under Paid Services section");

        //Subscription bundle Element is not exposed to go inside the details -  click on Premium subscription - https://toyotaconnected.atlassian.net/browse/OAD01-10409
        click("NATIVE", "xpath=//*[contains(@label,'Premium')]", 0, 1);
        sc.syncElements(20000, 60000);
        createLog("Verifying Premium subscription bundling Service Details screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium' or @text='PREMIUM']", 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text=concat('Subscription enables Drive Connect features. Getting around doesn', \"'\", 't get much easier. Stay connected on the road with up to date navigation, live agent navigation assistance and a seamless virtual assistant. This also provides you Remote Connect availability. Remote Connect is included at no additional cost with this subscription. With Remote Connect, manage your vehicle from the palm of your hand. Start the engine, lock the doors, adjust climate settings and receive guest driver notifications all from the Lexus app.')]", 0);
        //verify Auto Renew On
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew On']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'The next billing date is')]", 0);
        String autoRenewDateDetails = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'The next billing date is')]", 0, "value");
        createLog("Auto renew next billing date details: " + autoRenewDateDetails);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0);
        createLog("Verified Premium subscription bundling Service Details screen");
        createLog("Verified Premium subscription is added successfully");
    }

    public void updateServicePremium() {
        createLog("Verifying update Premium subscription");

        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        //verify Manage Subscriptions
        sc.syncElements(20000, 60000);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@label,'Premium')]", 0, 1000, 5, false);

        click("NATIVE", "xpath=//*[contains(@label,'Premium')]", 0, 1);
        sc.syncElements(5000, 10000);

        //click on Manage Subscription to update subscription
        click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
        sc.syncElements(20000, 60000);

        createLog("Verify Update Subscription screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'The next billing date is')]", 0);
        String autoRenewDateDetails = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'The next billing date is')]", 0, "value");
        createLog("Auto renew next billing date details: " + autoRenewDateDetails);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Premium')]/following::*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Premium')]/following::*[@text='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Premium')]/following::*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='check empty']", 0);
        switch (strAppType.toLowerCase()) {
            case ("toyota"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'If you select \"Auto Renew\", you authorize us to automatically renew your Service subscription for the chosen subscription length beginning on the scheduled expiration date, and thereafter annually or monthly (depending on the selected subscription term), by charging the then current fee to the default payment method on your account unless you cancel your subscription or turn off \"Auto Renew\".') and contains(@text,'After Remote Connect trial ends, Remote Connect is included at no additional cost with a paid plan. Services require 4G cellular network availability.')])[1]", 0);
                break;
            case ("lexus"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'IF YOU SELECT \"AUTO RENEW\", YOU AUTHORIZE US TO AUTOMATICALLY RENEW YOUR SERVICE SUBSCRIPTION FOR THE CHOSEN SUBSCRIPTION LENGTH BEGINNING ON THE SCHEDULED EXPIRATION DATE, AND THEREAFTER ANNUALLY OR MONTHLY (DEPENDING ON THE SELECTED SUBSCRIPTION TERM), BY CHARGING THE THEN CURRENT FEE TO THE DEFAULT PAYMENT METHOD ON YOUR ACCOUNT UNLESS YOU CANCEL YOUR SUBSCRIPTION OR TURN OFF \"AUTO RENEW\".') and contains(@text,'AFTER REMOTE CONNECT TRIAL ENDS, REMOTE CONNECT IS INCLUDED AT NO ADDITIONAL COST WITH A PAID PLAN. SERVICES REQUIRE 4G CELLULAR NETWORK AVAILABILITY.')])[1]", 0);
                break;
        }

        //update subscription button is not enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @class='UIAButton' and @enabled='false']", 0);
        //un-check Auto Renew
        click("NATIVE", "xpath=//*[@text='check empty']", 0, 1);
        //update subscription button is enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified Update Subscription screen");

        //click Update Subscription
        click("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 60000);

        //Verify Update Complete screen
        createLog("Verifying Update Complete screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Complete' or @label='UPDATE COMPLETE') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Complete' or @label='UPDATE COMPLETE') and @class='UIAStaticText']/following::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Subscriptions Updated!' or @label='SUBSCRIPTIONS UPDATED!') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Your subscription is no longer going to auto renew']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
        click("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAButton']", 0, 1);
        createLog("Verified Update Complete screen");
        sc.syncElements(10000, 20000);
        //Verify subscription is updated
        //click vehicle image from Dashboard
        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        sc.syncElements(5000, 30000);

        //verify Subscriptions
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);

        createLog("Verifying Premium subscription updated under Paid Services section");
        //bug is created for paid services issue
        // verifyElementFound("NATIVE", "xpath=//*[@text='Paid Services']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Premium')]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Premium')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Auto Renew Off')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0);



        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[@text='Drive Connect')[1]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[@text='Drive Connect'])[1]", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Premium')]/following::*[contains(@text,'Integrated Streaming')]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[contains(@text,'Integrated Streaming')])[1]", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[contains(@text,'Remote Connect')])[1]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Premium')]/following::*[contains(@text,'Remote Connect')])[1]", 0);
        String subscriptionBundleDetails = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Premium')]", 0, "value");
        createLog("Paid Service Subscription Bundle Details: " + subscriptionBundleDetails);
        createLog("Verified Premium subscription updated under Paid Services section");

        ////Subscription bundle Element is not exposed to go inside the details -  click on Premium subscription - https://toyotaconnected.atlassian.net/browse/OAD01-10409 click on Premium subscription - check Auto Renew Off in Subscriptions screen
        //       click("NATIVE", "xpath=//*[contains(@text,'Premium')]", 0, 1);
        click("NATIVE", "xpath=//*[contains(@label,'Premium')]", 0, 1);
        sc.syncElements(20000, 60000);
        createLog("Verifying Premium subscription bundling Service Details screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium' or @text='PREMIUM']", 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text=concat('Subscription enables Drive Connect features. Getting around doesn', \"'\", 't get much easier. Stay connected on the road with up to date navigation, live agent navigation assistance and a seamless virtual assistant. This also provides you Remote Connect availability. Remote Connect is included at no additional cost with this subscription. With Remote Connect, manage your vehicle from the palm of your hand. Start the engine, lock the doors, adjust climate settings and receive guest driver notifications all from the Lexus app.')]", 0);
        //verify Auto Renew Off
        createLog("Verifying Auto Renew Off in Service Details Screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew Off']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0);
        String expirationDateDetails = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0, "value");
        createLog("Expiration date details: " + expirationDateDetails);
        createLog("Verifying Auto Renew Off in Service Details Screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0);
        createLog("Verified Premium subscription bundling Service Details screen");

        createLog("Verified update Premium subscription");
    }

    public void cancelPremium(){

        click("NATIVE", "xpath=//*[contains(@label,'Premium')]", 0, 1);
        sc.syncElements(5000, 10000);
        click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
        sc.syncElements(10000, 60000);

        createLog("Verify Update Subscription screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Premium']", 0);
        String expirationDateDetails = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Expires On')]", 0, "value");
        createLog("Expiration date details: " + expirationDateDetails);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Premium')]/following::*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Premium')]/following::*[@text='Integrated Streaming']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Premium')]/following::*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Auto Renew' and @class='UIAButton']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='check empty']", 0);
        switch (strAppType.toLowerCase()) {
            case ("toyota"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'If you select \"Auto Renew\", you authorize us to automatically renew your Service subscription for the chosen subscription length beginning on the scheduled expiration date, and thereafter annually or monthly (depending on the selected subscription term), by charging the then current fee to the default payment method on your account unless you cancel your subscription or turn off \"Auto Renew\".') and contains(@text,'After Remote Connect trial ends, Remote Connect is included at no additional cost with a paid plan. Services require 4G cellular network availability.')])[1]", 0);
                break;
            case ("lexus"):
                verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'IF YOU SELECT \"AUTO RENEW\", YOU AUTHORIZE US TO AUTOMATICALLY RENEW YOUR SERVICE SUBSCRIPTION FOR THE CHOSEN SUBSCRIPTION LENGTH BEGINNING ON THE SCHEDULED EXPIRATION DATE, AND THEREAFTER ANNUALLY OR MONTHLY (DEPENDING ON THE SELECTED SUBSCRIPTION TERM), BY CHARGING THE THEN CURRENT FEE TO THE DEFAULT PAYMENT METHOD ON YOUR ACCOUNT UNLESS YOU CANCEL YOUR SUBSCRIPTION OR TURN OFF \"AUTO RENEW\".') and contains(@text,'AFTER REMOTE CONNECT TRIAL ENDS, REMOTE CONNECT IS INCLUDED AT NO ADDITIONAL COST WITH A PAID PLAN. SERVICES REQUIRE 4G CELLULAR NETWORK AVAILABILITY.')])[1]", 0);
                break;
        }

        //Cancel subscription button is enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified Update Subscription screen");

        //click Cancel Subscription
        click("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 60000);
        createLog("Verify Cancel Subscription screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Are you sure you want to cancel Premium?']", 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text=concat('Subscription enables Drive Connect features. Getting around doesn', \"'\", 't get much easier. Stay connected on the road with up to date navigation, live agent navigation assistance and a seamless virtual assistant. This also provides you Remote Connect availability. Remote Connect is included at no additional cost with this subscription. With Remote Connect, manage your vehicle from the palm of your hand. Start the engine, lock the doors, adjust climate settings and receive guest driver notifications all from the Lexus app.')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Reason For Cancellation']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Cancelling the subscription will turn off this service* and any scheduled payments will not be processed.\\n*Monthly subscription service will remain active through the end of the current billing cycle.']", 0);

        //Confirm Cancellation button is not enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='false']", 0);
        //Select Reason for cancellation
        click("NATIVE", "xpath=//*[@text='Reason For Cancellation']/following-sibling::*[@class='UIAButton']", 0, 1);
        sc.syncElements(3000, 9000);
        sc.setPickerValues("NATIVE", "xpath=//*[@class='UIAPicker']", 0, 0, "down:1");
        //Confirm Cancellation button is enabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified Update Subscription screen");

        //click Confirm Subscription
        click("NATIVE", "xpath=//*[(@label='Confirm Cancellation' or @label='CONFIRM CANCELLATION') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@text='You confirm that you no longer want Premium services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Confirm']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Cancel']", 0);
        click("NATIVE", "xpath=//*[@text='Confirm']", 0, 1);
        sc.syncElements(10000, 60000);

        //Verify Cancellation Complete screen
        createLog("Verifying Cancellation Complete screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Cancellation Complete' or @label='CANCELLATION COMPLETE') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Cancellation Complete' or @label='CANCELLATION COMPLETE') and @class='UIAStaticText']/following::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Your cancellation request was successful.' or @text='YOUR CANCELLATION REQUEST WAS SUCCESSFUL.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAStaticText']", 0);
        click("NATIVE", "xpath=//*[(@label='Back to Dashboard' or @label='BACK TO DASHBOARD') and @class='UIAButton']", 0, 1);
        createLog("Verified Cancellation Complete screen");
        sc.syncElements(5000, 30000);

        //Verify subscription is updated
        //click vehicle image from Dashboard
        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        sc.syncElements(5000, 30000);

        //verify Subscriptions
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);

        createLog("Verifying Premium subscription is not displayed under Paid Services section after cancel");
        sc.swipe("Down", sc.p2cy(30), 500);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[contains(@text,'Premium')]", 0);
        createLog("Verified Premium subscription is not displayed under Paid Services section after cancel");
        createLog("Verified Cancel Premium subscription");


    }
    public void cancelPremiumSubscription(){
        createLog("Verifying Cancel Premium subscription if already added");
        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
        //verify Manage Subscriptions
        sc.syncElements(20000, 60000);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@label,'Premium')]", 0, 1000, 5, false);
        //click on Manage Subscription to update subscription
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Premium')]")) {
            createLog("Premium subscription displayed");
            cancelPremium();
        } else {
            createErrorLog("Premium subscription not displayed in paid service list");
        }
    }
    public void cancelIfPremiumDisplayed() {
        createLog("Verifying Cancel Premium subscription if already added");
        verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
        click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);        //verify Manage Subscriptions
        sc.syncElements(20000, 60000);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@label,'Premium')]", 0, 1000, 5, false);
        //click on Manage Subscription to update subscription
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Premium')]")) {
            createLog("Premium subscription displayed");
            cancelPremium();
        } else {
            createLog("Premium subscription not displayed in paid service list");
        }
    }

    public static void addServiceFlow(String vinType, String strVehicleBrand) {
        createLog("Verifying add service flow Go Anywhere subscription bundling");

        checkCancelPaidSubscriptionAvailable("Go Anywhere");

        //Add Service
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Add Service']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@label='Add Service']", 0);
        sc.click("NATIVE", "xpath=//*[@label='Add Service']", 0, 1);
        sc.syncElements(20000, 60000);

        createLog("Verifying Manage Subscriptions screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_TABLE_VIEW']//following-sibling::*[@text='Paid Services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Purchase additional services for your') and @knownSuperClass='UIAccessibilityElement']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Go Anywhere']/parent::*[@class='UIAView']", 0, 1000, 5, false);
        verifyElementFound("NATIVE","xpath=//*[@text='Go Anywhere']/following-sibling::*[@text='Drive Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Go Anywhere']/following-sibling::*[contains(@id,'Remote Connect')]",0);

        // $15.00/Monthly
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
        verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere' or @text='GO ANYWHERE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Subscription enables Drive Connect features.')]", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*[@class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Remote Connect')]//preceding-sibling::*[@class='UIAImage']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        createLog("Verified Go Anywhere subscription bundling Service Details screen");

        createLog("Verify selecting Go Anywhere subscription in Manage Subscription screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAStaticText']", 0);
        //check Continue to purchase button is disabled
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='false']", 0);

        //verify subscription checkbox is unchecked
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_CHECKBOX_UNCHECKED_ICON']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@text='subscription uncheck']", 0);
        click("NATIVE", "xpath=//*[@text='Go Anywhere']/following-sibling::*[@text='subscription uncheck']", 0, 1);
        sc.syncElements(20000, 60000);
        createLog("Selecting Go Anywhere subscription in SELECT SUBSCRIPTION screen");
        verifyElementFound("NATIVE", "xpath=//*[(@label='Select Subscription' or @label='SELECT SUBSCRIPTION') and @class='UIAStaticText']", 0);
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
        //Auto renew is unchecked
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_UNCHECKED']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0);
        click("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@text='check empty']", 0, 1);
        //Auto renew is checked
        verifyElementFound("NATIVE", "xpath=(//*[@text='Auto Renew' and @class='UIAButton'])[1]/preceding-sibling::*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW_CHECKED']", 0);

        //verify continue button is disabled and subscription icon is unchecked
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='false']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere' or @text='SELECT A SUBSCRIPTION FOR GO ANYWHERE']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_UNCHECKED_ICON']", 0);

        //select subscription
        click("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere' or @text='SELECT A SUBSCRIPTION FOR GO ANYWHERE']/following::*[@text='subscription uncheck']", 0, 1);
        sc.syncElements(3000, 9000);
        //verify subscription icon is checked and continue button is enabled
        verifyElementFound("NATIVE", "xpath=//*[@text='Select a Subscription for Go Anywhere' or @text='SELECT A SUBSCRIPTION FOR GO ANYWHERE']/following::*[@accessibilityLabel='SUBSCRIPTIONS_PACKAGE_CELL_CHECKBOX_CHECKED_ICON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='true']", 0);
        click("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(10000, 50000);
        createLog("Selected Go Anywhere subscription in SELECT SUBSCRIPTION screen");

        //verify subscription checkbox is checked and Continue to purchase button is enabled in Manage Subscription screen
        verifyElementFound("NATIVE", "xpath=//*[(@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE') and @class='UIAButton' and @enabled='true']", 0);
        createLog("Verified selecting Go Anywhere subscription in Manage Subscription screen");

        //click on Continue to purchase
        sc.click("NATIVE", "xpath=//*[@label='Continue to Purchase' or @label='CONTINUE TO PURCHASE' and @class='UIAButton' and @enabled='true']", 0, 1);
        sc.syncElements(15000, 60000);
        //consent screen for 20TM VIN, the VIN #800BDN99400000000 used for test is a 20TM
        if(vinType=="20TM") {
            verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text=concat('By tapping the ', '\"', 'Accept', '\"', ' button, you direct and authorize "+strVehicleBrand+" to collect precise geolocation information, driving data, and vehicle health data wirelessly transmitted by your vehicle. "+strVehicleBrand+" will use this data to deliver Connected Services and for internal research, development, and data analysis.')]", 0);
            sc.syncElements(2000,4000);
            verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'If your vehicle is equipped with cloud navigation services and you perform a point of interest (POI) search, "+strVehicleBrand+" will provide Google with the location coordinates and search request criteria for the purpose of providing a relevant POI as a response')])", 0);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'If your vehicle is equipped with the Advance Drive System Service (Teammate) feature')])", 0, 1000, 5, false);
            verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'If your vehicle is equipped with the Advance Drive System Service (Teammate) feature')])", 0);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'"+strVehicleBrand+" will also collect sensor and/or image data from your vehicle')])", 0, 1000, 5, false);
            verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'"+strVehicleBrand+" will also collect sensor and/or image data from your vehicle')])", 0);
//            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@text=concat('Select the ', '\"', 'Decline', '\"', ' button for "+strVehicleBrand+" to not collect such data, which will disable your vehicle’s data transmission capability. You may withdraw consent at any time by pressing your vehicle’s SOS button or changing your vehicle’s privacy settings in the "+strVehicleBrand+" app.')])", 0, 1000, 5, true);
//            verifyElementFound("NATIVE", "xpath=(//*[@text=concat('Select the ', '\"', 'Decline', '\"', ' button for "+strVehicleBrand+" to not collect such data, which will disable your vehicle’s data transmission capability. You may withdraw consent at any time by pressing your vehicle’s SOS button or changing your vehicle’s privacy settings in the "+strVehicleBrand+" app.')])", 0);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='Connected Services Privacy Notice' and @visible='true']", 0, 1000, 5, false);
            verifyElementFound("NATIVE", "xpath=//*[@id='Connected Services Privacy Notice']", 0);

        }
        else {
            //  Consent screens for 21MM non 20TM VINs
            verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@label,'When you click the \"Accept\" button, you agree that on a regular and continuous basis, your vehicle wirelessly transmits location, driving and vehicle health data to "+strVehicleBrand+" and its affiliates in order to deliver Connected Services and for internal research, development and data analysis.')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@label='To learn more, review the Connected Services Privacy Notice and Terms of Use.']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Privacy Notice']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Terms of Use']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'For the purpose of providing Cloud Navigation services, if you perform a search for a POI (Point of interest), Lexus will provide Google with the location coordinates and criteria associated with the search request for the purpose of providing a relevant POI as a response. Only the originating location of your search and criteria for your search are shared for this purpose and the information provided by Lexus is not associated with you or your vehicle. This information is subject to Google') and contains(@id,'Privacy Policy and Terms of Service.')]", 0);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[(contains(@label,'To disable your vehicle') and contains(@label,'data transmission capability, press, \"Decline\"')) and @visible='true']", 0, 1000, 5, false);
            verifyElementFound("NATIVE", "xpath=//*[(contains(@label,'To disable your vehicle') and contains(@label,'data transmission capability, press, \"Decline\"')) and @visible='true']", 0);
        }

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[1]", 0, 1000, 5, true);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[2]", 0, 1000, 5, true);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[3]", 0, 1000, 8, true);
        sc.swipe("Down", sc.p2cy(70), 2000);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']", 0, 1000, 8, false);
        //if Confirm and Continue button not enabled, click last accept button again
        if(!sc.isElementFound("NATIVE","xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton' and @enabled='true']")) {
            sc.click("NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[3]", 0, 1);
        }
        sc.click("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton']", 0, 1);

        sc.syncElements(15000, 60000);
        //Home address
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0);
            click("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0, 1);
        }
        sc.syncElements(5000, 30000);
        //Address verification screen
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@text='Continue With This Address' or @text='CONTINUE WITH THIS ADDRESS') and @class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[(@text='Continue With This Address' or @text='CONTINUE WITH THIS ADDRESS') and @class='UIAButton']", 0, 1);
        }
        sc.syncElements(10000, 30000);

        //Payment method screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Payment Method' or @text='PAYMENT METHOD']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0);
        click("NATIVE", "xpath=(//*[@text='Credit Card' and @class='UIAStaticText']//following::*[@class='UIAView'])[1]", 0, 1);
        sc.syncElements(5000, 30000);

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
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='swipeToPay' and @visible='true']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Swiping \"Swipe to Pay\" authorizes the scheduled charges and applicable taxes to the payment method provided as described in the Connected Services Terms of Use including the Refund Policy. The total amount to be charged on your billing date will be subject to the then current fee and applicable taxes.')]", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='swipeToPay']", 0);

        createLog("Verified add service flow Go Anywhere subscription bundling");
    }

    public static void checkCancelPaidSubscriptionAvailable(String paidSubscriptionBundle) {
        createLog("Checking cancel subscription available for "+paidSubscriptionBundle+"");
        boolean existingSubFound = false;
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Add Service']")) {
            //click on shop tab
            verifyElementFound("NATIVE", "xpath=//*[@id='Shop']", 0);
            click("NATIVE", "xpath=//*[@id='Shop']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
            sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
            //verify Manage Subscriptions
            sc.syncElements(20000, 60000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        }

        verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        sc.swipe("Down", sc.p2cy(30), 2000);
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
            verifyElementFound("NATIVE", "xpath=//*[(@label='Service Details' or @label='SERVICE DETAILS') and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0);

            click("NATIVE", "xpath=//*[(@label='Manage Subscription' or @label='MANAGE SUBSCRIPTION') and @class='UIAButton']", 0, 1);
            sc.syncElements(10000, 60000);

            createLog("Verify Update Subscription screen");
            verifyElementFound("NATIVE", "xpath=//*[(@label='Update Subscription' or @label='UPDATE SUBSCRIPTION') and @knownSuperClass='UILabel']", 0);
            sc.syncElements(5000, 60000);

            if(sc.isElementFound("NATIVE","xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton']")) {
                createLog("cancel subscription is displayed");
                isCancelSubscriptionAvailable = true;
                //Cancel subscription button is enabled
                verifyElementFound("NATIVE", "xpath=//*[(@label='Cancel Subscription' or @label='CANCEL SUBSCRIPTION') and @class='UIAButton' and @enabled='true']", 0);
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
                verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
                sc.click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
                //verify Manage Subscriptions
                sc.syncElements(20000, 60000);
                verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
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
        } else {
            //verify subscriptions screen
            verifyElementFound("NATIVE", "xpath=//*[@label='Subscriptions']", 0);
        }
        createLog("Checked cancel subscription available for "+paidSubscriptionBundle+"");
    }
}
