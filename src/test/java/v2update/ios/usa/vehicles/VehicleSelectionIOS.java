package v2update.ios.usa.vehicles;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VehicleSelectionIOS extends SeeTestKeywords {
    String testName = "V2-VehicleSelection-IOS";
    static String addVehicleVin = "58ABZ1B17KU001401";

    @BeforeAll
    public void setup() throws Exception {
        iOS_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("21mm email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail17CY"), ConfigSingleton.configMap.get("strPassword17CY"));
        sc.stopStepsGroup();
        /*
        Call Vehicle switcher from the vehicle switcher class and Default the vehicle JTJHKCEZ1N2004122
        iOS_DefaultVehicle("Use Vin 450+"); // This method is deprecated
         */
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void defaultVehicle() {
        sc.startStepsGroup("Test - Default Vehicle");
        Default("4T1BZ1FB5KU014237");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void switchVehicle() {
        sc.startStepsGroup("Test - Switch Vehicle");
        Switcher("4T1BZ1FB5KU014237");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void scanVin() {
        sc.startStepsGroup("Add Vehicle - Scan VIN");
        if(!sc.isElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        scanVinValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void emailSignOut() {
        sc.startStepsGroup("Test - Sign Out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void emailSignInVehicle() {
        sc.startStepsGroup("Test - Sign In - Add/Remove Vehicle Account");
        createLog("Start: Add/Remove Vehicle - email Login-USA-English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmailAddVehicle"), ConfigSingleton.configMap.get("strPasswordAddVehicle"));
        createLog("End: Add/Remove Vehicle - email Login-USA-English");
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void removeVehicleIfVehicleExists() {
        sc.startStepsGroup("Test - Remove Vehicle if vehicle exists in account");
        removeVehicleFromAccount();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void addVehicleManually() {
        sc.startStepsGroup("Test - Add Vehicle Manually");
        addVehicle17CY(addVehicleVin);
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void removeVehicle() {
        sc.startStepsGroup("Test - Remove Vehicle");
        removeVehicleFromAccount();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void emailSignOutVehicle() {
        sc.startStepsGroup("Test - Sign Out - Add/Remove Vehicle Account");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void Switcher(String strVin) {
        sc.startStepsGroup("Vehicle Switch");
        boolean vinFound = false;
        createLog("Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Double tap to open vehicle switcher!']", 0);
        //click("NATIVE", "xpath=//*[@text='Double tap to open vehicle switcher!']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0);
        click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
        verifyElementFound("NATIVE","xpath=//*[@id='vehicle_switcher_vin_show_icon_cta']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0);
        click("NATIVE","xpath=//*[@id='vehicle_switcher_vin_show_icon_cta']",0,1);
        sc.syncElements(2000,5000);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0, "value");
        createLog("Vehicle VIN found on the vehicle switcher page: " + defaultVin);
        if (strVin.equalsIgnoreCase(defaultVin)) {
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
    public static void addVinManually17Plus(String strVin){
        createLog("Started - Add Vin Manually");
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]"))
            click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='AddIcon']", 0);
        click("NATIVE", "xpath=//*[@label='AddIcon']", 0, 1);
        sc.syncElements(2000, 20000);
        //clicking randomly on screen to close tooltip messages
        verifyElementFound("NATIVE","xpath=//*[@id='Scan VIN']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Enter VIN Manually']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='You can find your 17-digit Vehicle Identification Number (VIN) on your driver side door panel or on the windshield.']",0);
        verifyElementFound("NATIVE","xpath=//*[@class='UIAImage' and @value='At the driver side of the dashboard near the windshield or the rear edge of the driver side door']",0);
        click("NATIVE","xpath=//*[@id='Enter VIN Manually']",0,1);
        sc.syncElements(2000,4000);
        verifyElementFound("NATIVE","xpath=//*[@id='ADDVEHICLE_LABEL_TITLE']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Enter your Vehicle Identification Number (VIN)']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Vehicle Identification Number']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='ADDVEHICLE_TEXTFIELD_VIN']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='ADDVEHICLE_BUTTON_SCAN']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='ADDVEHICLE_IMAGE_WHERE_TO_FIND_VIN']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='ADDVEHICLE_BUTTON_ADDVEHICLE' and @enabled='false']",0);
        click("NATIVE","xpath=//*[@id='ADDVEHICLE_TEXTFIELD_VIN']",0,1);
        sendText("NATIVE","xpath=//*[@id='ADDVEHICLE_TEXTFIELD_VIN']",0,strVin); //58ABZ1B17KU001401
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Done']")){
            click("NATIVE","xpath=//*[@id='Done']",0,1);
        }
        verifyElementFound("NATIVE","xpath=//*[@id='ADDVEHICLE_BUTTON_ADDVEHICLE' and @enabled='true']",0);
        click("NATIVE","xpath=//*[@id='ADDVEHICLE_BUTTON_ADDVEHICLE' and @enabled='true']",0,1);
        sc.syncElements(5000,10000);
        click("NATIVE","xpath=//*[@id='VEHICLEDETAILS_BUTTON_SAVE_CHANGES']",0,1);
        sc.syncElements(15000,25000);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Connected Services')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='No Thanks']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Continue' and @enabled='true']",0);
        click("NATIVE","xpath=//*[@id='Continue' and @enabled='true']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@id='TOOLBAR_LABEL_TITLE' and @text='Home Address']",0);
        click("NATIVE","xpath=//*[@id='Continue']",0,1);
        sc.syncElements(2000,4000);
        click("NATIVE","xpath=//*[@id='ADDRESS_VERIFICATION_CONTINUE_BUTTON']",0,1);
        sc.syncElements(2000,4000);
        for(int i=0;i<=2;i++){
            sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@id='COMBINED_DATACONSENT_ACCEPT']"+"["+i+"]", 0, 1000, 1, true);
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@id='Confirm and Continue']", 0, 1000, 1, true);
        verifyElementFound("NATIVE","xpath=//*[@id='CONGRATULATIONS_IMAGE_ICON']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='CONGRATULATIONS_BUTTON_FINISH_SETUP']",0);
        click("NATIVE","xpath=//*[@id='CONGRATULATIONS_BUTTON_FINISH_SETUP']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@id='Vehicle image, double tap to open vehicle info.']",0);
    }
    public static void RemoveVehicle(String strVin) {
        sc.startStepsGroup("Vehicle Switch");
        boolean vinRemoved = false;
        createLog("Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Double tap to open vehicle switcher!']", 0);
        //click("NATIVE", "xpath=//*[@text='Double tap to open vehicle switcher!']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0);
        click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_textview']", 0);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_textview']", 0, "value");
        createLog("Vehicle VIN found on the vehicle switcher page: " + defaultVin);
        if (strVin.equalsIgnoreCase(defaultVin)) {
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
                    click("NATIVE", "xpath=//*[@id='vehicle_switcher_remove_vehicle_button']", 0, 1);
                    sc.syncElements(10000, 30000);
                    verifyElementFound("NATIVE", "xpath=//*[@id='bottom_sheet_remove_vehicle_title_textview']", 0);
                    click("NATIVE", "xpath=//*[@label='Remove']", 0, 1);
                    if(sc.isElementFound("NATIVE","xpath=//*[@id='The vehicle has been removed from your account']",0)) {
                        createLog("VIN# " + strVin + " Removed");
                        sc.syncElements(10000, 30000);
                        vinRemoved = true;
                        break;
                    } else {
                        createLog("VIN# " + strVin + " not found");
                        sc.report("VIN# " + strVin + " Not Found", false);
                        Assertions.assertFalse(true, "VIN# " + strVin + " Not Found and Not removed");
                    }
                }
            }
            if (!vinRemoved) {
                createLog("VIN# " + strVin + " not found");
                sc.report("VIN# " + strVin + " Not Found", false);
                Assertions.assertFalse(true, "VIN# " + strVin + " Not Found and Not removed");
            }
        }
        sc.stopStepsGroup();
    }

    public static void Default(String strVin) {
        sc.startStepsGroup("Default Vehicle");
        boolean vinFound = false;
        createLog("Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        //if (sc.isElementFound("NATIVE", "xpath=//*[@text='Double tap to open vehicle switcher!']"))
        //    click("NATIVE", "xpath=//*[@text='Double tap to open vehicle switcher!']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]"))
            click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
//        verifyElementFound("NATIVE", "xpath=(//*[@text='AddIcon'])", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='VIN']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Image shown for illustration purposes only. Vehicle may not be as shown.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remove Vehicle']", 0);
        verifyElementFound("NATIVE","xpath=//*[@id='vehicle_switcher_vin_show_icon_cta']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0);
        click("NATIVE","xpath=//*[@id='vehicle_switcher_vin_show_icon_cta']",0,1);
        sc.syncElements(2000,5000);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0, "value");
        createLog("Vehicle VIN found on the vehicle switcher page: " + defaultVin);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_textview']", 0);
        if (strVin.equalsIgnoreCase(defaultVin)) {
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

    public void scanVinValidations() {
        createLog("Started - Scan Vin Validations");
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]"))
            click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='AddIcon']", 0);
        click("NATIVE", "xpath=//*[@label='AddIcon']", 0, 1);
        sc.syncElements(2000, 20000);
        //clicking randomly on screen to close tooltip messages
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Select Scan QR Code if you have a capable model year 2022 or newer vehicle') or contains(@text,'Select Scan QR Code if you have a Lexus Interface capable model year 2022 or newer vehicle')]")) {
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

    public static void removeVehicleFromAccount() {
        createLog("Started - Remove Vehicle");

        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'no_vehicle')]")){
            createLog("no_vehicle_image is displayed in account");
        } else {
            createLog("vehicle_image is displayed in account - removing vehicle");
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Odometer')]"))
                click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);
            sc.syncElements(3000, 9000);

            sc.swipe("Down", sc.p2cy(30), 500);
            verifyElementFound("NATIVE", "xpath=//*[@text='Remove Vehicle']", 0);
            click("NATIVE", "xpath=//*[@text='Remove Vehicle']", 0, 1);
            sc.syncElements(4000, 12000);
            verifyElementFound("NATIVE", "xpath=//*[@text='RemoveVehicleIcon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Are you sure you want to remove this vehicle from your account?']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Remove']", 0);
            click("NATIVE", "xpath=//*[@text='Remove']", 0, 1);
            sc.syncElements(20000, 60000);
            createLog("Verifying no_vehicle_image is displayed after removing vehicle from account");
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'no_vehicle')]", 0);
            createLog("Verified - no_vehicle_image is displayed after removing vehicle from account");
        }
        createLog("Completed - Remove Vehicle");
    }

    public static void addVehicle17CY(String strVin) {
        createLog("Started - Add Vehicle Manually : "+strVin);
        // Add Vehicle for account with no vehicle
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Add a Vehicle']")){
            createLog("No vehicle exists in the account, adding vehicle...");
            verifyElementFound("NATIVE", "xpath=//*[@text='Add a Vehicle']", 0);
            click("NATIVE", "xpath=//*[@text='Add a Vehicle']", 0, 1);
            sc.syncElements(4000, 8000);
        }
        else {
            click("NATIVE", "xpath=//*[contains(@id,'Odometer')]", 0, 1);
//            reLaunchApp_iOS();
//            iOS_LaunchApp();
            sc.syncElements(2000,4000);
            verifyElementFound("NATIVE","xpath=//*[@id='vehicle_switcher_add_Vehicle_button']",0);
            click("NATIVE","xpath=//*[@id='vehicle_switcher_add_Vehicle_button']",0,1);
        }

        //clicking randomly on screen to close tooltip messages
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Select Scan QR Code if you have a capable model year 2022 or newer vehicle') or contains(@text,'Select Scan QR Code if you have a Lexus Interface capable model year 2022 or newer vehicle')]")) {
            click("NATIVE", "xpath=//*[@id='Add your Vehicle' or @id='ADD YOUR VEHICLE']", 0, 1);
            delay(2000);
            click("NATIVE", "xpath=//*[@id='Add your Vehicle' or @id='ADD YOUR VEHICLE']", 0, 1);
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='QR_ADD_VEHICLE_MANUAL_VIN_BUTTON']", 0);
        click("NATIVE", "xpath=//*[@id='QR_ADD_VEHICLE_MANUAL_VIN_BUTTON']", 0, 1);

        sc.syncElements(4000, 12000);
        createLog("Verifying add your vehicle screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Add your Vehicle' or @text='ADD YOUR VEHICLE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Enter your Vehicle Identification Number (VIN)']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Identification Number']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Scan VIN via camera']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle indentification number text field']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='ADDVEHICLE_IMAGE_WHERE_TO_FIND_VIN']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='ADDVEHICLE_BUTTON_ADDVEHICLE']", 0);
        sendText("NATIVE", "xpath=//*[@id='ADDVEHICLE_TEXTFIELD_VIN']", 0, strVin);
        click("NATIVE", "xpath=//*[@id='ADDVEHICLE_BUTTON_ADDVEHICLE']", 0, 1);
        sc.syncElements(15000, 60000);
        createLog("Verified add your vehicle screen");

        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Details' or @text='VEHICLE DETAILS']", 0);
        sc.swipe("Down", sc.p2cy(50), 3000);
        sendText("NATIVE", "xpath=//*[@id='VEHICLEDETAILS_TEXTFIELD_NICHNAME']", 0, "Testing add vehicle");
        click("NATIVE", "xpath=//*[@id='VEHICLEDETAILS_BUTTON_SAVE_CHANGES']", 0, 1);
        sc.syncElements(30000, 60000);

        if(strVin.contains("2TSTG2AA0LC040505")) { //2TXBD30E6CW476827
            verifyElementFound("NATIVE", "xpath=(//*[@label='Connected Services - Trial' or @label='CONNECTED SERVICES - TRIAL'])[1]", 0);
            verifyElementFound("NATIVE","xpath=//*[@id='No Thanks']",0);
            click("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton']", 0, 1);

            sc.syncElements(10000, 30000);
            //Home address
            if(sc.isElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']")){
                verifyElementFound("NATIVE", "xpath=//*[@text='Home Address' or @text='HOME ADDRESS']", 0);
                verifyElementFound("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton']", 0);
                click("NATIVE", "xpath=//*[(@text='Continue' or @text='CONTINUE') and @class='UIAButton']", 0, 1);
            }
            sc.syncElements(5000, 30000);
            //Address verification screen
            if(sc.isElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']")){
                verifyElementFound("NATIVE", "xpath=//*[@text='Address Verification' or @text='ADDRESS VERIFICATION']", 0);
                verifyElementFound("NATIVE", "xpath=//*[(@text='Continue With This Address' or @text='CONTINUE WITH THIS ADDRESS') and @class='UIAButton']", 0);
                click("NATIVE", "xpath=//*[@id='ADDRESS_VERIFICATION_CONTINUE_BUTTON']", 0, 1);
            }
            sc.syncElements(10000, 60000);

            createLog("Started - Accepting data consent");
            //consent screen
            verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);
//        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[1]", 0, 1000, 5, true);
//        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[2]", 0, 1000, 5, true);
//        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[3]", 0, 1000, 8, true);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Connected Services Master Data Consent']]][@visible='true']", 0, 1000, 5, false);
            click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Connected Services Master Data Consent']]][@visible='true']", 0, 1);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Service Connect Communication']]][@visible='true']", 0, 1000, 5, false);
            click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Service Connect Communication']]][@visible='true']", 0, 1);
            //sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Insure Connect']]][@visible='true']", 0, 1000, 5, false);
            //click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text='Insure Connect']]][@visible='true']", 0, 1);
            sc.swipeWhileNotFound("Down",sc.p2cy(70),2000,"NATIVE","xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[contains(@text,'In-App Marketing ')]]][@visible='true']",0,1000,5,false);
            click("NATIVE","xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and (./preceding-sibling::* | ./following-sibling::*)[./*[contains(@text,'In-App Marketing ')]]][@visible='true']",0,1);

            sc.swipe("Down", sc.p2cy(70), 2000);
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton'][@visible='true']", 0, 1000, 8, false);
            //if Confirm and Continue button not enabled, click last accept button again
            if(!sc.isElementFound("NATIVE","xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton' and @enabled='true']")) {
                sc.click("NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[3]", 0, 1);
            }
            sc.click("NATIVE", "xpath=//*[(@id='CONFIRM AND CONTINUE' or @id='Confirm and Continue') and @class='UIAButton'][@visible='true']", 0, 1);
            sc.syncElements(25000, 50000);
            createLog("Completed - Accepting data consent");
        }

        verifyElementFound("NATIVE", "xpath=(//*[@id='CONGRATULATIONS_LABEL_TITLE'])[1]", 0);
        click("NATIVE", "xpath=//*[@id='CONGRATULATIONS_BUTTON_FINISH_SETUP']", 0, 1);
        sc.syncElements(25000, 50000);

        //verify vehicle image
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        Switcher(strVin);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(4000, 20000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Vehicle Identification Number']//following-sibling::*[@text='" + strVin + "']", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remove Vehicle']", 0);
        createLog("Completed - Add Vehicle Manually");
    }
}