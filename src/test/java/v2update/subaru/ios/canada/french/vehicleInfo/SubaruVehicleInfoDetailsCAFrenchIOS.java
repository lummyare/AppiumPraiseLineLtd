package v2update.subaru.ios.canada.french.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruVehicleInfoDetailsCAFrenchIOS extends SeeTestKeywords {

    String testName = " - SubaruVehicleInfoDetailsCAFrench-IOS";

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
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLogin("subarunextgen3@gmail.com","Test$12345");
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void vehicleInfoTest() {
        sc.startStepsGroup("Vehicle Info Details for Subaru -Test");
        createLog("Start: Vehicle Info Details for Subaru -Test");
        vehicleInfoDetails("JTMABABA0PA002944","2023  Solterra Touring");
        createLog("completed: Vehicle Info Details for Subaru -Test");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleInfoDetails(String strVin, String vehicleModel) {
        createLog("Verifying Vehicle Info Details in vehicle info screen");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(5000, 30000);

        createLog("Verifying Vehicle details in vehicle info screen");
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'"+vehicleModel+"')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='edit_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='edit_icon']/following::*[@XCElementType='XCUIElementTypeImage']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Plaque d') and contains(@text,'immatriculation')]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='eye_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'•••••••••••')]", 0);
        click("NATIVE", "xpath=//*[@text='eye_icon']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='eyeslash_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='copy_icon']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[contains(@text,'Plaque d') and contains(@text,'immatriculation')]/following::*[@text='"+strVin+"']"), 0);
        sc.longClick("NATIVE", "xpath=//*[@text='copy_icon']", 0, 1, 0, 0);
        createLog("Verified Vehicle details in vehicle info screen");

        //Verifying Glovebox section
        createLog("Verifying Glovebox section in vehicle info screen");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='globebox_icon']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Spécifications']"), 0);
        createLog("Verified Glovebox section in vehicle info screen");

        //Verifying Subscriptions section
        createLog("Verifying Subscriptions section in vehicle info screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Abonnements']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Services connectés pour votre véhicule']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='subscriptions_icon']", 0);
        createLog("Verified Subscriptions section in vehicle info screen");

        sc.swipe("Down", sc.p2cy(30), 1000);

        //Verifying Vehicle software section
        createLog("Verifying Vehicle software section in vehicle info screen");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Logiciel de véhicule']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleSoftware_icon']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='À jour']"), 0);
        createLog("Verifying Vehicle software section in vehicle info screen");

        //Verify Remove vehicle link
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Supprimer le véhicule']"), 0);

        verifyElementFound("NATIVE", "xpath=//*[@id='VehicleInfo_back_Btn']", 0);
        click("NATIVE", "xpath=//*[@id='VehicleInfo_back_Btn']", 0, 1);
        sc.syncElements(2000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);

        createLog("Verified Vehicle Info Details in vehicle info screen");
    }
}