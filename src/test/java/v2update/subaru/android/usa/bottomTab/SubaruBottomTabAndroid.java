package v2update.subaru.android.usa.bottomTab;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruBottomTabAndroid extends SeeTestKeywords {
    String testName = "BottomTab-Android";

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
    public void bottomTab(){
        sc.startStepsGroup("Bottom Tab Android");
        validateBottomTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void serviceTab(){
        sc.startStepsGroup("Service Tab Android");
        validateServiceTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void payTab(){
        sc.startStepsGroup("Pay Tab Android");
        validatePayTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void homeTab(){
        sc.startStepsGroup("Home Tab Android");
        validateHomeTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void shopTab(){
        sc.startStepsGroup("Shop Tab Android");
        validateShopTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void findTab(){
        sc.startStepsGroup("Find Tab Android");
        validateFindTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void signOut() {
        sc.startStepsGroup("Signout");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void validateBottomTab(){
        if(!(sc.isElementFound("NATIVE","xpath=//*[@content-desc='Service']"))) {
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE","xpath=//*[@content-desc='Service']",0);
        verifyElementFound("NATIVE","xpath=//*[@content-desc='Pay']",0);
        verifyElementFound("NATIVE","xpath=//*[@content-desc='Home Tab']",0);
        verifyElementFound("NATIVE","xpath=//*[@content-desc='Shop']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Find']",0);
    }

    public static void validateServiceTab(){
        //TODO Blank screen not ready for testing
        click("NATIVE","xpath=//*[@text='Service']",0,1);
        sc.syncElements(2000, 6000);
        //verifyElementFound("NATIVE","xpath=//*[@text='Service Appointments']",0);
        click("NATIVE","xpath=//*[@content-desc='Home Tab']",0,1);
    }
    public static void validatePayTab(){
        click("NATIVE","xpath=//*[@content-desc='Pay']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@text='Wallet']",0);
        sc.syncElements(2000, 6000);
    }
    public static void validateHomeTab(){
        if (!sc.isElementFound("NATIVE","xpath=//*[@content-desc='Home Tab']")) {
            reLaunchApp_android();
            click("NATIVE","xpath=//*[@text='Shop']",0,1);
            sc.syncElements(2000, 6000);
        }
        click("NATIVE","xpath=//*[@content-desc='Home Tab']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@text='Status']",0);
        click("NATIVE","xpath=//*[@content-desc='Home Tab']",0,1);
    }

    public static void validateShopTab(){
        click("NATIVE","xpath=//*[@text='Shop']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@text='Manage Subscriptions']",0);
        click("NATIVE","xpath=//*[@content-desc='Home Tab']",0,1);
    }
    public static void validateFindTab(){
        click("NATIVE","xpath=//*[@text='Find']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@text='Dealers']",0);
    }
}
