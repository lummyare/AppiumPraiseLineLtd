package v2update.subaru.android.canada.French.vehicles;

import com.ctp.SeeTestKeywords;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruVehicleSelectionCAFrenchAndroid extends SeeTestKeywords {
    String testName = "SubaruCAFrenchVehicleSelection-Android";

    @BeforeAll
    public void setup() throws Exception {
        android_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("email Login-CA-French");
        createLog("Start: email Login-CA-French");
        selectionOfCountry_Android("canada");
        selectionOfLanguage_Android("french");
        android_keepMeSignedIn(true);
        android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
        createLog("End:email Login-CA-French");
        sc.stopStepsGroup();
        /*
        Call Vehicle switcher from the vehicle switcher class and Default the vehicle JTJHKCEZ1N2004122
        android_DefaultVehicle("Use Vin 450+"); // This method is deprecated
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
        Default("JTMABABA0PA002944");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void switchVehicle() {
        sc.startStepsGroup("Test - Switch Vehicle");
        Switcher("JTMABABA0PA002944");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void scanVinValidations() {
        sc.startStepsGroup("Test - Scan VIN Validations");
        scanVIN();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void emailSignOut() {
        sc.startStepsGroup("Sign Out Test");
        android_SignOut();
        sc.stopStepsGroup();
    }



    public static void Switcher(String strVin) {
        sc.startStepsGroup("Vehicle Switch");
        boolean vinFound = false;
        createLog("Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        //click open vehicle switcher icon
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0);
        click("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Véhicules']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0, "text").replaceAll("\\W", "");;
        createLog("Vehicle VIN found on the vehicle switcher screen: " + defaultVin);
        if (strVin.contains(defaultVin)) {
            createLog("Vin#" + strVin + " is Already Default Vin");
            click("NATIVE", "xpath=//*[@content-desc='Swipe down icon']", 0, 1);
            sc.syncElements(2000, 10000);
        } else {
            for (int i = 2; i <= 6; i++) {
                sc.elementSwipeWhileNotFound("NATIVE", "xpath=//*[@class='android.widget.ScrollView']", "Right", sc.p2cx(20), 2000, "NATIVE", "xpath=(//*[@content-desc='Switcher Vehicle Image'])[" + i + "]", 0, 2000, 1, false);
                click("NATIVE", "xpath=(//*[@content-desc='Switcher Vehicle Image'])[" + i + "]", 0, 1);
                sc.syncElements(3000, 9000);
                String vehicleSwitch = sc.elementGetProperty("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0, "text");
                createLog("Vehicle VIN found on the vehicle switcher screen, Switching of the Vehicle started to switch VIN# : " + vehicleSwitch);
                verifyElementFound("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0);
                if (strVin.equalsIgnoreCase(vehicleSwitch)) {
                    sc.swipe("Down", sc.p2cy(30), 500);
                    sc.syncElements(2000, 8000);
                    createLog("Vin is selected and vehicle switcher completed Selecting the vehicle: " + strVin);
                    verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Utiliser comme primarie']/following-sibling::*[@class='android.widget.Button']"), 0);
                    verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Sélectionner']/following-sibling::*[@class='android.widget.Button' and @enabled='true']"), 0);
                    click("NATIVE", convertTextToUTF8("xpath=//*[@text='Sélectionner']/following-sibling::*[@class='android.widget.Button']"), 0, 1);
                    sc.syncElements(3000, 9000);
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
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']"))
            click("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Véhicules']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='addVehicle']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='VIN']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text=\"Image montrée à des fins d'illustration seulement. Le véhicule peut différer de l'illustration.\"]"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", convertTextToUTF8("//*[@text='Supprimer le véhicule']"), 0, 1000, 2, false);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Supprimer le véhicule']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0, "text").replaceAll("\\W", "");
        createLog("Vehicle VIN found on the vehicle switcher screen: " + defaultVin);
        verifyElementFound("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0);
        if (strVin.contains(defaultVin)) {
            createLog("Vin#" + strVin + " is Already Default Vin");
            click("NATIVE", "xpath=//*[@content-desc='Swipe down icon']", 0, 1);
            sc.syncElements(2000, 10000);
        } else {
            for (int i = 2; i <= 6; i++) {
                sc.elementSwipeWhileNotFound("NATIVE", "xpath=//*[@class='android.widget.ScrollView']", "Right", sc.p2cx(20), 2000, "NATIVE", "xpath=(//*[@content-desc='Switcher Vehicle Image'])[" + i + "]", 0, 2000, 1, false);
                click("NATIVE", "xpath=(//*[@content-desc='Switcher Vehicle Image'])[" + i + "]", 0, 1);
                sc.syncElements(3000, 9000);
                String vehicleSwitch = sc.elementGetProperty("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0, "text");
                createLog("Vehicle VIN found on the vehicle switcher screen, Switching of the Vehicle started to switch VIN# : " + vehicleSwitch);
                verifyElementFound("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0);
                if (strVin.equalsIgnoreCase(vehicleSwitch)) {
                    sc.swipe("Down", sc.p2cy(30), 500);
                    sc.syncElements(2000, 8000);
                    createLog("Vin is selected and vehicle switcher completed Selecting the vehicle: " + strVin);
                    verifyElementFound("NATIVE", "xpath=//*[@text='Utiliser comme primarie']/following-sibling::*[@class='android.widget.Button' and @enabled='true']", 0);
                    click("NATIVE", "xpath=//*[@text='Utiliser comme primarie']/following-sibling::*[@class='android.widget.Button']", 0, 1);
                    sc.syncElements(3000, 9000);
                    createLog("VIN# " + strVin + " Defaulted");
                    vinFound = true;
                    sc.flickElement("NATIVE", "xpath=//*[@text='VIN']", 0, "down");
                    sc.syncElements(3000, 9000);
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

    public static void scanVIN() {
        createLog("Started - Scan Vin Validations");
        if(!sc.isElementFound("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']"))
            reLaunchApp_android();

        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0);
        click("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0, 1);
        sc.syncElements(3000, 9000);
        //on swiping or on flicking element closing vehicles screen
        if(!sc.isElementFound("NATIVE","xpath=//*[@content-desc='addVehicle']")){
            sc.swipeWhileNotFound("up", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@content-desc='addVehicle']", 0, 1000, 2, false);
            sc.syncElements(3000, 9000);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0);
            click("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0, 1);
            sc.syncElements(3000, 9000);
        }
        //add vehicle button attributes not displayed
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='addVehicle']", 0);
        click("NATIVE", "xpath=//*[@content-desc='addVehicle']", 0, 1);
        sc.syncElements(2000, 4000);

        //clicking randomly on screen to close tooltip messages
        if(sc.isElementFound("NATIVE","xpath=//*[@id='balloon_text']")) {
            sc.clickCoordinate(sc.p2cx(50),sc.p2cy(50),1);
            delay(2000);
            sc.clickCoordinate(sc.p2cx(50),sc.p2cy(65),1);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='AJOUTER VOTRE VÉHICULE']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Scanner le NIV' or @text='SCANNER LE NIV']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Saisissez le NIV manuellement' or @text='SAISISSEZ LE NIV MANUELLEMENT'] | //*[@text='Entrer le NIV manuellement']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Vous trouverez votre numéro')]"), 0);
        click("NATIVE", "xpath=//*[@id='bt_scan_vin']", 0, 1);
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_message']")) {
            sc.click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='TEXTE']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='CODE À BARRES']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Placez votre NIV à l') and contains(@text,'intérieur du cadre. Il scannera automatiquement.')]"), 0);
        click("NATIVE", "xpath=//*[@id='vin_scan']", 0, 1);
        sc.syncElements(2000, 4000);
        //verify flash element
        verifyElementFound("NATIVE", "xpath=//*[@id='scan_vin_flash_img']", 0);
        click("NATIVE", "xpath=//*[@id='barcode_scan']", 0, 1);
        sc.syncElements(4000, 8000);
        //verify flash element
        verifyElementFound("NATIVE", "xpath=//*[@id='scan_vin_flash_img']", 0);
        click("NATIVE", "xpath=//*[@id='scan_vin_close_img']", 0, 1);
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        if(sc.isElementFound("NATIVE","xpath=//*[@class='android.widget.ImageButton']"))
            click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(4000, 8000);
        sc.flickElement("NATIVE", "xpath=//*[@text='VIN']", 0, "down");
        createLog("Completed - Scan Vin Validations");
    }


}
