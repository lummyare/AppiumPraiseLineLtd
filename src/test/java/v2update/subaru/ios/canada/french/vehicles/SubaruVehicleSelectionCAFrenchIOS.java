package v2update.subaru.ios.canada.french.vehicles;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruVehicleSelectionCAFrenchIOS extends SeeTestKeywords {
    String testName = "EV - VehicleSelection - IOS";

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
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void defaultVehicle() {
        sc.startStepsGroup("Test - Default Vehicle");
        Default("JTMABABA8RA060836");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void switchVehicle() {
        sc.startStepsGroup("Test - Switch Vehicle");
        Switcher("JTMABABA8RA060836");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void scanVin() {
        sc.startStepsGroup("Add Vehicle - Scan VIN");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        scanVinValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void signOut() {
        sc.startStepsGroup("Test - Sign Out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void Switcher(String strVin) {
        sc.startStepsGroup("Vehicle Switch");
        boolean vinFound = false;
        createLog("Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0);
        click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0, "value").replaceAll("\\W", "");
        createLog("Vehicle VIN found on the vehicle switcher page: " + defaultVin);
        if (strVin.contains(defaultVin)) {
            createLog("Vin#" + strVin + " is Already Default Vin");
            click("NATIVE", "xpath=//*[@id='vehicle_switcher_pull_down_button']", 0, 1);
            sc.syncElements(2000, 10000);
        } else {
            for (int i = 2; i <= 5; i++) {
                sc.elementSwipeWhileNotFound("NATIVE", "xpath=//*[@class='UIAScrollView']", "Right", sc.p2cx(20), 2000, "NATIVE", "xpath=//*[@class='UIAScrollView']//*[@class='UIAView']//*[@class='UIAImage'][" + i + "]", 0, 2000, 1, false);
                click("NATIVE", "xpath=//*[@class='UIAScrollView']//*[@class='UIAView']//*[@class='UIAImage'][" + i + "]", 0, 1);
                String vehicleSwitch = sc.elementGetProperty("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_textview']", 0, "value");
                createLog("Vehicle VIN found on the vehicle switcher page, Switching of the Vehicle started to switch VIN# : " + vehicleSwitch);
                verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_textview']", 0);
                if (strVin.equalsIgnoreCase(vehicleSwitch)) {
                    createLog("Vin is selected and vehicle switcher completed Selecting the vehicle" + strVin);
                    verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_select_button' and @enabled='true']", 0);
                    click("NATIVE", "xpath=//*[@id='vehicle_switcher_select_button']", 0, 1);
                    createLog("VIN# " + strVin + " Switched");
                    vinFound = true;
                    break;
                }
            }
            if (!vinFound) {
                createLog("VIN# " + strVin + " not found");
                sc.report("VIN# " + strVin + " Not Found", false);
                Assertions.assertFalse(true, "VIN# " + strVin + " Not Found");
            }
        }
        sc.stopStepsGroup();
    }


    public static void Default(String strVin) {
        sc.startStepsGroup("Default Vehicle");
        boolean vinFound = false;
        createLog("Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0);
        click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);

        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
//        verifyElementFound("NATIVE", "xpath=(//*[@text='AddIcon'])", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='VIN']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Image shown for illustration purposes only. Vehicle may not be as shown.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remove Vehicle']", 0);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0, "value").replaceAll("\\W", "");
        createLog("Vehicle VIN found on the vehicle switcher page: " + defaultVin);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0);
        if (strVin.contains(defaultVin)) {
            createLog("Vin#" + strVin + " is Already Default Vin");
            click("NATIVE", "xpath=//*[@id='vehicle_switcher_pull_down_button']", 0, 1);
            sc.syncElements(2000, 10000);
        } else {
            for (int i = 2; i <= 5; i++) {
                sc.elementSwipeWhileNotFound("NATIVE", "xpath=//*[@class='UIAScrollView']", "Right", sc.p2cx(20), 2000, "NATIVE", "xpath=//*[@class='UIAScrollView']//*[@class='UIAView']//*[@class='UIAImage'][" + i + "]", 0, 2000, 1, false);
                click("NATIVE", "xpath=//*[@class='UIAImage' and ./parent::*[./parent::*[@class='UIAScrollView']]][" + i + "]", 0, 1);
                String vehicleSwitch = sc.elementGetProperty("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_textview']", 0, "value");
                createLog("Vehicle VIN found on the vehicle switcher page, Switching of the Vehicle started to switch VIN# : " + vehicleSwitch);
                verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_textview']", 0);
                if (strVin.equalsIgnoreCase(vehicleSwitch)) {
                    createLog("Vin is selected and vehicle switcher completed Selecting the vehicle" + strVin);
                    verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_make_default_button' and @enabled='true']", 0);
                    click("NATIVE", "xpath=//*[@id='vehicle_switcher_make_default_button']", 0, 1);
                    sc.syncElements(2000, 8000);
                    click("NATIVE", "xpath=//*[@id='vehicle_switcher_select_button']", 0, 1);
                    createLog("VIN# " + strVin + " Defaulted");
                    vinFound = true;
                    break;
                }
            }
            if (!vinFound) {
                createLog("VIN# " + strVin + " not found");
                sc.report("VIN# " + strVin + " Not Found", false);
                Assertions.assertFalse(true, "VIN# " + strVin + " Not Found");
            }
        }
        sc.stopStepsGroup();
    }

    public static void scanVinValidations() {
        createLog("Started - Scan Vin Validations");
        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0);
        click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='AddIcon']", 0);
        click("NATIVE", "xpath=//*[@label='AddIcon']", 0, 1);
        sc.syncElements(2000, 20000);
        //clicking randomly on screen to close tooltip messages
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Select Scan QR Code if you have a capable model year 2022 or newer vehicle') or contains(@text,'Select Scan QR Code if you have a Lexus Interface capable model year 2022 or newer vehicle')]")) {
            click("NATIVE", "xpath=//*[@id='Add your Vehicle' or @id='ADD YOUR VEHICLE']", 0, 1);
            delay(2000);
            click("NATIVE", "xpath=//*[@id='Add your Vehicle' or @id='ADD YOUR VEHICLE']", 0, 1);
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='Add your Vehicle' or @id='ADD YOUR VEHICLE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='You can find your Vehicle Identification Number (VIN) on your driver side door panel or on the windshield.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='QR_ADD_VEHICLE_SCAN_VIN_BUTTON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='QR_SCAN_BUTTON']", 0);

        //click on SCAN VIN
        click("NATIVE", "xpath=//*[@accessibilityLabel='QR_ADD_VEHICLE_SCAN_VIN_BUTTON']", 0, 1);
        sc.syncElements(2000, 20000);

        if (sc.isElementFound("NATIVE", "xpath=//*[@id='This app requires Camera access to scan a barcode or take a picture']")) {
            sc.click("NATIVE", "xpath=//*[@id='OK' or @id='Ok']", 0, 1);
        }

        click("NATIVE", "xpath=//*[@id='Text' and @class='UIAButton']", 0, 1);

        createLog("Validating flash in Text section");
        //verify flash
        createLog("Validating Auto in Text section");
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='Auto']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='Auto']/preceding-sibling::*[@class='UIAImage']", 0);
        click("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='Auto']/preceding-sibling::*[@class='UIAImage']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Auto']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='flash']", 0);

        //flash Auto
        click("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Auto']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='Auto']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0);
        createLog("Validated Auto in Text section");
        //flash on
        createLog("Validating On in Text section");
        click("NATIVE", "xpath=//*[@id='flash']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0);
        click("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='On']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Auto']", 0);
        createLog("Validated On in Text section");
        //flash off
        createLog("Validating Off in Text section");
        click("NATIVE", "xpath=//*[@id='flash']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0);
        click("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='Off']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Auto']", 0);
        createLog("Validated Off in Text section");
        createLog("Validated flash in Text section");

        click("NATIVE", "xpath=//*[@id='Barcode' and @class='UIAButton']", 0, 1);
        sc.syncElements(4000, 7000);

        createLog("Validating flash in Barcode section");
        //verify flash
        createLog("Validating Off in Barcode section");
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='Off']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='flash']", 0);
        click("NATIVE", "xpath=//*[@id='flash']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Auto']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='flash']", 0);
        createLog("Validated Off in Barcode section");
        //flash Auto
        createLog("Validating Auto in Barcode section");
        click("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Auto']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='Auto']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0);
        createLog("Validated Auto in Barcode section");
        //flash on
        createLog("Validating On in Barcode section");
        click("NATIVE", "xpath=//*[@id='flash']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0);
        click("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='On']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Auto']", 0);
        createLog("Validated On in Barcode section");
        //flash off
        createLog("Validating Off in Barcode section");
        click("NATIVE", "xpath=//*[@id='flash']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0);
        click("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Off']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and @text='Off']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='On']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@knownSuperClass='UIButtonLabel' and @text='Auto']", 0);
        createLog("Validated Off in Barcode section");
        createLog("Validated flash in Barcode section");
        click("NATIVE", "xpath=//*[@id='Cancel']", 0, 1);
        createLog("Completed - Scan Vin Validations");
    }

}
