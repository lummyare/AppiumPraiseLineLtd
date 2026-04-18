package v2update.ios.usa.shop;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import v2update.ios.usa.vehicles.VehicleSelectionIOS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Subscriptions17cyPlusIOS extends SeeTestKeywords {

    String testName = "Subscriptions17cyPlus-IOS";

    @BeforeAll
    public void setup() throws Exception {
        iOS_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("17cyPlus email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail17CYPlus"), ConfigSingleton.configMap.get("strPassword17CYPlus"));
        sc.stopStepsGroup();

        //VehicleSelectionIOS.Switcher("JTMFB3FV5MD006786");
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void subscriptionsCard() {
        // Validating subscription card under shop

        sc.startStepsGroup("Subscription Card");
        validateSubscriptionCard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void subscriptionsScreen() {
        // validating subscription screen

        sc.startStepsGroup("Subscription Screen");
        validateSubscriptionScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void destinationAssist() {
        // validating destination assist card + details for 17cy

        sc.startStepsGroup("Destination Assist Test Screen");
        validateDestinationAssist();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void safetyConnect() {
        // validating safety connect card + details for 17cy

        sc.startStepsGroup("Safety Connect Screen");
        validateSafetyConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void dynamicNavigation() {
        // validating dynamic navigation card + details for 17cy

        sc.startStepsGroup("Dynamic Navigation Screen");
        validateDynamicNavigation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void evRemoteConnect() {
        // validating ev remote connect card + details for 17cy

        sc.startStepsGroup("EV Remote Connect Screen");
        validateEVRemoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void SignOut() {
        ios_emailSignOut();
    }


    public static void validateSubscriptionCard() {
        click("NATIVE", "xpath=//*[@id='IconShop']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Subscriptions']", 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0);
        click("NATIVE", "xpath=//*[@id='shop_manage_subscription_button']", 0, 1);
    }

    public static void validateSubscriptionScreen() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Subscriptions']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='subscriptionscreen_trialtile']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Add Service']", 0);
    }

    public static void validateSafetyConnect() {
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@value='Safety Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='subscription_safety_connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Safety Connect']/following::XCUIElementTypeStaticText", 1);
        verifyElementFound("NATIVE", "xpath=//*[@label='Safety Connect']/following::XCUIElementTypeStaticText", 1);

        click("NATIVE", "xpath=//*[@label='subscription_safety_connect']", 0, 1);

        sc.syncElements(10000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@value,'Safety')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Cancel' or @id='CANCEL']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
    }

    public static void validateDynamicNavigation() {
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='subscription_dynamic_navigation']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Dynamic Navigation']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Dynamic Navigation']/following::XCUIElementTypeStaticText", 1);
        verifyElementFound("NATIVE", "xpath=//*[@label='Dynamic Navigation']/following::XCUIElementTypeStaticText", 2);

        click("NATIVE", "xpath=//*[@label='subscription_dynamic_navigation']", 0, 1);

        sc.syncElements(10000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@value,'Drive')]", 0);

        // trhere is no until text check with qa team
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Cancel' or @id='CANCEL']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
    }

    public static void validateEVRemoteConnect() {
        sc.syncElements(3000, 30000);
        sc.swipe("DOWN", sc.p2cy(35), 5000);
        verifyElementFound("NATIVE", "xpath=//*[@label='EV Remote Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='EV Remote Connect']/following::XCUIElementTypeStaticText", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='EV Remote Connect']/following::XCUIElementTypeStaticText", 1);
        verifyElementFound("NATIVE", "xpath=//*[@label='subscription_remote_connect']", 0);

        click("NATIVE", "xpath=//*[@label='subscription_remote_connect']", 0, 1);

        sc.syncElements(10000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@value,'With')]", 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Cancel' or @id='CANCEL']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(3000, 30000);
    }

    public static void validateDestinationAssist() {
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Destination Assist']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Destination Assist']/following::XCUIElementTypeStaticText", 1);
        verifyElementFound("NATIVE", "xpath=//*[@label='Destination Assist']/following::XCUIElementTypeStaticText", 2);
        verifyElementFound("NATIVE", "xpath=//*[@value='subscription_destination_assit']", 0);

        click("NATIVE", "xpath=//*[@value='subscription_destination_assit']", 0, 1);

        sc.syncElements(10000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Service Details' or @text='SERVICE DETAILS') and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_HEADER_IMAGE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@value,'Get')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Cancel' or @id='CANCEL']", 0);
        click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
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
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
    }
}

