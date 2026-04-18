package v2update.subaru.android.usa.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruFuelWidgetAndroid extends SeeTestKeywords {
    String testName="Dashboard Fuel Widget - Android";

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
    public void fuelWidgetTest(){
        sc.startStepsGroup("Fuel Widget EV");
        fuelWidget();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest(){
        sc.startStepsGroup("Sign Out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void fuelWidget() {
        createLog("Fuel Widget Validation EV Vehicle");
        sc.syncElements(5000,10000);
        if (!sc.isElementFound("NATIVE", "xpath=//*[@contentDescription='tap to open vehicle switcher']", 0)) {
            reLaunchApp_android();
        }
        //Fuel widget validation

        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_range_value_text']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_range_unit_text']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_battery_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_climate_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_fuel_bar_view']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_chevron_icon']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'until full')]")){
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Charge Flash Icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Charging']", 0);
        }else{
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Find Station')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'% charged')]", 0);
        }
        //progressBarColorValidation();
        //TODO EV not ready for testing
        //ChargeInfo screen Validations
        /*createLog("Started:ChargeInfo screen Validations");
        click("NATIVE","xpath=//*[@resource-id='fuel_view_chevron_icon']",0,1);
        sc.syncElements(10000,20000);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Charge Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='CLIMATE ON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='CLIMATE OFF']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@content-desc,'mi est.')])[1]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@content-desc,'mi est.')])[2]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'History')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Schedule')]", 0);
        sc.swipe("Down", sc.p2cy(40), 2000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Statistics')]", 0);
        sc.swipe("Down", sc.p2cy(40), 2000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Clean Assist')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Charging Station Settings']", 0);
        sc.swipe("Down", sc.p2cy(40), 2000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Wallet')]", 0);
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        sc.syncElements(1000,2000);
        createLog("Ended:ChargeInfo screen Validations");*/
    }
}
