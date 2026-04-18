package v2update.subaru.ios.usa.vehicles;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruVehicleSelectionIOS extends SeeTestKeywords {
    String testName = "EV - VehicleSelection - IOS";
    String addVehicleVin = "58ABZ1B17KU001401";

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
                ios_emailLogin("subarunextgen3@gmail.com", "Test$123456");
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
                ios_emailLogin("subarunextgen3@gmail.com","Test$123456");
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
        Default("JF1SG36307H748305");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void switchVehicle() {
        sc.startStepsGroup("Test - Switch Vehicle");
        Switcher("JF1SG36307H748305");
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


    public static void Switcher(String strVin) {
        sc.startStepsGroup("Vehicle Switch");
        boolean vinFound = false;
        createLog("Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='DashboardHeader_vehicleNamer']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='DashboardHeader_vehicleNamer']", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Véhicules']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0, "value").replaceAll("\\W", "");        createLog("Vehicle VIN found on the vehicle switcher page: " + defaultVin);
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
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='DashboardHeader_vehicleNamer']"))
            click("NATIVE", "xpath=//*[@accessibilityLabel='DashboardHeader_vehicleNamer']", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Véhicules']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='AddIcon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='VIN']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text=\"Image montrée à des fins d'illustration seulement. Le véhicule peut différer de l'illustration.\"]"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Supprimer le véhicule']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Primaire']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Sélectionner']"), 0);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0, "value").replaceAll("\\W", "");        createLog("Vehicle VIN found on the vehicle switcher page: " + defaultVin);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_switcher_vin_number_text_cta']", 0);
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
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='DashboardHeader_vehicleNamer']"))
            click("NATIVE", "xpath=//*[@accessibilityLabel='DashboardHeader_vehicleNamer']", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Véhicules']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='AddIcon']", 0);
        click("NATIVE", "xpath=//*[@label='AddIcon']", 0, 1);
        sc.syncElements(2000, 20000);
        //clicking randomly on screen to close tooltip messages
        if(sc.isElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'véhicule 2022 ou plus récent compatible') or contains(@text,'véhicule 2022 ou plus récent compatible avec Lexus Interface')]"))) {
            click("NATIVE", convertTextToUTF8("//*[@id='Ajoutez votre véhicule' or @id='AJOUTEZ VOTRE VÉHICULE']"), 0, 1);
            delay(2000);
            click("NATIVE", convertTextToUTF8("//*[@id='Ajoutez votre véhicule' or @id='AJOUTEZ VOTRE VÉHICULE']"), 0, 1);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='Ajoutez votre véhicule' or @id='AJOUTEZ VOTRE VÉHICULE']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Scanner le NIV' or @text='SCANNER LE NIV']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='QR_ADD_VEHICLE_SCAN_VIN_BUTTON']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Scanner le NIV' or @text='SCANNER LE NIV']"), 0);

        //click on SCAN VIN
        click("NATIVE", convertTextToUTF8("//*[@text='Scanner le NIV' or @text='SCANNER LE NIV']"), 0, 1);
        sc.syncElements(2000, 20000);

        if (sc.isElementFound("NATIVE", "xpath=//*[@id='This app requires Camera access to scan a barcode or take a picture']")) {
            sc.click("NATIVE", "xpath=//*[@id='OK' or @id='Ok']", 0, 1);
        }

        click("NATIVE", "xpath=//*[@id='Texte' and @class='UIAButton']", 0, 1);

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

        click("NATIVE", convertTextToUTF8("//*[@id='Code à barres' and @class='UIAButton']"), 0, 1);
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
        click("NATIVE", "xpath=//*[@id='Annuler']", 0, 1);
        createLog("Completed - Scan Vin Validations");
    }

    public void removeVehicleFromAccount() {
        createLog("Started - Remove Vehicle");

        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'no_vehicle')]")){
            createLog("no_vehicle_image is displayed in account");
        } else {
            createLog("vehicle_image is displayed in account - removing vehicle");
            if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='DashboardHeader_vehicleNamer']"))
                click("NATIVE", "xpath=//*[@accessibilityLabel='DashboardHeader_vehicleNamer']", 0, 1);
            sc.syncElements(3000, 9000);

            sc.swipe("Down", sc.p2cy(30), 500);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Supprimer le véhicule']"), 0);
            click("NATIVE", convertTextToUTF8("//*[@text='Supprimer le véhicule']"), 0, 1);
            sc.syncElements(4000, 12000);
            verifyElementFound("NATIVE", "xpath=//*[@text='RemoveVehicleIcon']", 0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voulez-vous vraiment supprimer ce véhicule de votre compte ?']"), 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer']", 0);
            click("NATIVE", "xpath=//*[@text='Supprimer']", 0, 1);
            sc.syncElements(20000, 60000);
            createLog("Verifying no_vehicle_image is displayed after removing vehicle from account");
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'no_vehicle')]", 0);
            createLog("Verified - no_vehicle_image is displayed after removing vehicle from account");
        }
        createLog("Completed - Remove Vehicle");
    }

    public void addVehicle() {
        createLog("Started - Add Vehicle Manually");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Ajouter un véhicule']")))
            reLaunchApp_iOS();

        //Add Vehicle for account with no vehicle
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Ajouter un véhicule']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Ajouter un véhicule']"), 0, 1);
        sc.syncElements(4000, 8000);
        //clicking randomly on screen to close tooltip messages
        if(sc.isElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'véhicule 2022 ou plus récent compatible') or contains(@text,'véhicule 2022 ou plus récent compatible avec Lexus Interface')]"))) {
            click("NATIVE", convertTextToUTF8("//*[@id='Ajoutez votre véhicule' or @id='AJOUTEZ VOTRE VÉHICULE']"), 0, 1);
            delay(2000);
            click("NATIVE", convertTextToUTF8("//*[@id='Ajoutez votre véhicule' or @id='AJOUTEZ VOTRE VÉHICULE']"), 0, 1);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='Ajoutez votre véhicule' or @id='AJOUTEZ VOTRE VÉHICULE']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='Saisissez le NIV manuellement' or @id='SAISISSEZ LE NIV MANUELLEMENT']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='QR_ADD_VEHICLE_MANUAL_VIN_BUTTON']", 0);
        click("NATIVE", "xpath=//*[@id='QR_ADD_VEHICLE_MANUAL_VIN_BUTTON']", 0, 1);

        sc.syncElements(4000, 12000);
        createLog("Verifying add your vehicle screen");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Ajoutez votre véhicule' or @text='AJOUTEZ VOTRE VÉHICULE']"), 0);
        //verifyElementFound("NATIVE", convertTextToUTF8("//*[@text=\"Scannez ou entrez le numéro d'identification de votre véhicule (NIV).\"]"), 0);
        //verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,\"Numéro d'identification du véhicule\")]"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,\"Où trouver votre numéro d'identification\") or contains(@text,\"OÙ TROUVER VOTRE NUMÉRO D'IDENTIFICATION\")]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='ADDVEHICLE_IMAGE_WHERE_TO_FIND_VIN']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Ajouter un véhicule' or @text='AJOUTER UN VÉHICULE']"), 0);
        click("NATIVE", "xpath=//*[@id='ADDVEHICLE_TEXTFIELD_VIN']", 0, 1);
        sc.sendText(addVehicleVin);
        sc.syncElements(2000, 10000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Done']"))
            click("NATIVE", "xpath=//*[@text='Done']", 0, 1);

        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[(@text='Ajouter un véhicule' or @text='AJOUTER UN VÉHICULE') and @class='UIAButton']"), 0);
        //click("NATIVE", convertTextToUTF8("xpath=//*[(@text='Ajouter un véhicule' or @text='AJOUTER UN VÉHICULE') and @class='UIAButton']"), 0, 1);
        click("NATIVE", "xpath=//*[@id='ADDVEHICLE_TEXTFIELD_VIN']", 0, 1);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='done']"))
            click("NATIVE", "xpath=//*[@text='done']", 0, 1);
        sc.syncElements(5000, 30000);
        createLog("Verified add your vehicle screen");

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails du véhicule' or @text='DÉTAILS DU VÉHICULE']"), 0);
        sendText("NATIVE", "xpath=//*[@id='VEHICLEDETAILS_TEXTFIELD_NICHNAME']", 0, "Testing add vehicle");
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Done']"))
            click("NATIVE", "xpath=//*[@text='Done']", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@text='Sauvegarder les modifications' or @text='SAUVEGARDER LES MODIFICATIONS'])[1]"), 0);
        click("NATIVE", "xpath=//*[@id='VEHICLEDETAILS_BUTTON_SAVE_CHANGES']", 0, 1);
        sc.syncElements(15000, 60000);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@label='Essai des Services connectés' or @label='ESSAI DES SERVICES CONNECTÉS'])[1]"), 0);
        click("NATIVE", "xpath=//*[(@text='Continuer' or @text='CONTINUER') and @class='UIAButton']", 0, 1);

        sc.syncElements(10000, 30000);
        //Home address
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Adresse de domicile' or @text='ADRESSE DE DOMICILE']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Adresse de domicile' or @text='ADRESSE DE DOMICILE']", 0);
            verifyElementFound("NATIVE", "xpath=//*[(@text='Continuer' or @text='CONTINUER') and @class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[(@text='Continuer' or @text='CONTINUER') and @class='UIAButton']", 0, 1);
        }
        sc.syncElements(5000, 30000);
        //Address verification screen
        if(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text=\"Vérification de l'adresse\" or @text=\"VÉRIFICATION DE L'ADRESSE\"]"))){
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text=\"Vérification de l'adresse\" or @text=\"VÉRIFICATION DE L'ADRESSE\"]"), 0);
            verifyElementFound("NATIVE", "xpath=//*[(@text='Continuez avec cette adresse' or @text='CONTINUEZ AVEC CETTE ADRESSE') and @class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[@id='ADDRESS_VERIFICATION_CONTINUE_BUTTON']", 0, 1);
        }
        sc.syncElements(10000, 60000);

        createLog("Started - Accepting data consent");
        //consent screen
        verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[1]", 0, 1000, 5, true);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[(@id='Confirmer et continuer' or @id='CONFIRMER ET CONTINUER') and @class='UIAButton']", 0, 1000, 5, false);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT'])[2]", 0, 1000, 5, true);
        sc.syncElements(2000, 10000);
        sc.click("NATIVE", "xpath=//*[(@id='Confirmer et continuer' or @id='CONFIRMER ET CONTINUER') and @class='UIAButton']", 0, 1);
        sc.syncElements(25000, 50000);
        createLog("Completed - Accepting data consent");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Félicitations!' or @text='FÉLICITATIONS!']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@text='Terminer la configuration' or @text='TERMINER LA CONFIGURATION'])[1]"), 0);
        click("NATIVE", "xpath=//*[@id='CONGRATULATIONS_BUTTON_FINISH_SETUP']", 0, 1);
        sc.syncElements(25000, 30000);

        //verify vehicle image
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(4000, 20000);
        verifyElementFound("NATIVE", "xpath=//*[@text='" + addVehicleVin + "']", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Supprimer le véhicule']"), 0);
        createLog("Completed - Add Vehicle Manually");
    }

}
