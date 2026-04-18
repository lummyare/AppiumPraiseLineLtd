package v2update.subaru.android.canada.French.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruFuelWidgetCAFrenchAndroid extends SeeTestKeywords {
    String testName = "Dashboard Fuel Widget CA-French- Android";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")){
            case(""):
                testName = System.getProperty("cloudApp") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
                sc.stopStepsGroup();
        }
    }
    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }


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
        //swipe to refresh dashboard screen
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_display_image']", 0);

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
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Chargé à')]"), 0);
        }
    }
}
