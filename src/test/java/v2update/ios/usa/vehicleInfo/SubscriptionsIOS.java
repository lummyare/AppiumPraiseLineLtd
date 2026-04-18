package v2update.ios.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import v2update.ios.usa.shop.Subscriptions17cyIOS;
import v2update.ios.usa.shop.Subscriptions17cyPlusIOS;
import v2update.ios.usa.shop.Subscriptions21MMIOS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubscriptionsIOS extends SeeTestKeywords {
    String testName = "Subscriptions-IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")){
            case(""):
                testName = System.getProperty("cloudApp") + testName;
                iOS_Setup2_5(testName);
                sc.startStepsGroup("email SignIn 21MM");
                selectionOfCountry_IOS("USA");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21MM"), ConfigSingleton.configMap.get("strPassword21MM"));
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                iOS_Setup2_5(testName);
                sc.startStepsGroup("email SignIn 21MM");
                selectionOfCountry_IOS("USA");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21MM"), ConfigSingleton.configMap.get("strPassword21MM"));
                sc.stopStepsGroup();
        }
    }

    @AfterAll
    public void exit() {
        exitAll(testName);
    }

    @Test
    @Order(1)
    public void subscriptionsCard21MM() {
        sc.startStepsGroup("Subscription Card 21MM");
        validateSubscriptionCard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void subscriptionsScreen21MM() {
        sc.startStepsGroup("Subscription Screen 21MM");
        validateSubscriptionScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void subscriptions21MM() {
        sc.startStepsGroup("Validate Subscriptions 21MM");
        Subscriptions21MMIOS.validateSafetyConnect();
        Subscriptions21MMIOS.validateRemoteConnect();
        Subscriptions21MMIOS.validateGoAnywhere();
        Subscriptions21MMIOS.validatePremium();
        Subscriptions21MMIOS.validateMusicLover();
        Subscriptions21MMIOS.addServiceFlow("","Lexus");
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void signOut21MM(){
        ios_emailSignOut();
    }

    @Test
    @Order(5)
    public void signIn17CYPlus() {
        sc.startStepsGroup("17CYPlus email login");
        createLog("Start: 17CYPlus email login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail17CYPlus"), ConfigSingleton.configMap.get("strPassword17CYPlus"));
        //VehicleSelectionIOS.Switcher("JTMFB3FV5MD006786");
        createLog("Ended: 17CYPlus email login");
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void subscriptionsCard17CYPlus() {
        sc.startStepsGroup("Subscription Card 17CYPlus");
        validateSubscriptionCard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void subscriptionsScreen17CYPlus() {
        sc.startStepsGroup("Subscription Screen 17CYPlus");
        validateSubscriptionScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void subscriptions17CYPlus() {
        sc.startStepsGroup("Validate Subscriptions 17CYPlus");
        Subscriptions17cyPlusIOS.validateDestinationAssist();
        Subscriptions17cyPlusIOS.validateSafetyConnect();
        //Subscriptions17cyPlusIOS.validateDynamicNavigation(); //this feature has been sunset
        Subscriptions17cyPlusIOS.validateEVRemoteConnect();
        validateAddServiceScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void signOut17CYPlus() {
        sc.startStepsGroup("SignOut-17CYPlus");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    public void signIn17CY() {
        sc.startStepsGroup("17CY email login");
        createLog("Start: 17CY email login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail17CY"), ConfigSingleton.configMap.get("strPassword17CY"));
        createLog("Ended: 17CY email login");
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    public void subscriptionsCard17CY() {
        sc.startStepsGroup("Subscription Card 17CY");
        validateSubscriptionCard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    public void subscriptionsScreen17CY() {
        sc.startStepsGroup("Subscription Screen 17CY");
        validateSubscriptionScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    public void subscriptions17CY() {
        sc.startStepsGroup("Validate Subscriptions 17CY");
        Subscriptions17cyIOS.validateSafetyConnect17CY();
        Subscriptions17cyIOS.validateRemoteConnect17CY();
        validateAddServiceScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(14)
    public void signOut17CY() {
        sc.startStepsGroup("SignOut-17CY");
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

    public static void validateAddServiceScreen() {
        sc.syncElements(3000, 30000);
        click("NATIVE","xpath=//*[@id='Add Service']",0,1);
        sc.syncElements(15000, 30000);
        verifyElementFound("NATIVE","xpath=//*[(@text='Manage Subscription' or @text='MANAGE SUBSCRIPTION') and @class='UIAStaticText']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Paid Services' and ./parent::*[@text='SUBSCRIPTIONS_TABLE_VIEW']]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Purchase additional services')]",0);
        click("NATIVE","xpath=//*[@text='Back']",0,1);
        sc.syncElements(3000, 30000);
        click("NATIVE","xpath=//*[@id='SubscriptionScreen_backAction']",0,1);
        sc.syncElements(3000, 30000);
        click("NATIVE","xpath=//*[@id='VehicleInfo_back_Btn']",0,1);
        sc.syncElements(3000, 30000);
    }
}
