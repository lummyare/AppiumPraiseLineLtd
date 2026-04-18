package v2update.subaru.ios.usa.bottomTab;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class SubaruBottomTabIOS extends SeeTestKeywords {
    String testName = "EV - BottomTab - IOS";

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
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
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
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
                sc.stopStepsGroup();
        }
    }
    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void bottomTab(){
        sc.startStepsGroup("Bottom Tab IOS");
        validateBottomTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void serviceTab(){
        sc.startStepsGroup("Service Tab IOS");
        validateServiceTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void payTab(){
        sc.startStepsGroup("Pay Tab IOS");
        validatePayTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void homeTab(){
        sc.startStepsGroup("Home Tab IOS");
        validateHomeTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void shopTab(){
        sc.startStepsGroup("Shop Tab IOS");
        validateShopTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void findTab(){
        sc.startStepsGroup("Find Tab IOS");
        validateFindTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void signOut() {
        sc.startStepsGroup("Signout");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateBottomTab(){
        verifyElementFound("NATIVE","xpath=//*[@id='Service']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_serviceTab']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='IconService']",0);

        verifyElementFound("NATIVE","xpath=//*[@id='Pay']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='IconPay']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_payTab']",0);

        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0);

        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_shopTab']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='IconShop']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Shop']",0);

        verifyElementFound("NATIVE","xpath=//*[@id='IconFind']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Find']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_findTab']",0);
    }

    public static void validateServiceTab(){
        click("NATIVE","xpath=//*[@id='Service']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@id='ServiceApptsCard_serviceAppointmentsTitle']",0);
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
    }
    public static void validatePayTab(){
        click("NATIVE","xpath=//*[@id='Pay']",0,1);
        sc.syncElements(2000, 6000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@id='regular_wallet']", 0))
            verifyElementFound("NATIVE","xpath=//*[@id='regular_wallet']",0);
        else
            verifyElementFound("NATIVE","xpath=//*[@text='Default Card']",0);
        sc.syncElements(2000, 6000);
    }
    public static void validateHomeTab(){
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@text='Status']",0);
    }

    public static void validateShopTab(){
        click("NATIVE","xpath=//*[@id='Shop']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@id='shop_manage_subscription_button']",0);
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
    }
    public static void validateFindTab(){
        click("NATIVE","xpath=//*[@id='Find']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@text='Dealers']",0);
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
    }


}
