package v2update.subaru.android.usa.vehicles;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruVehicleSelectionAndroid extends SeeTestKeywords {
    String testName = "VehicleSelection-Android";

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
    public void defaultVehicleTest() {
        sc.startStepsGroup("Test - Default Vehicle");
        Default("JTMABABA8RA060836");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void switchVehicleTest() {
        sc.startStepsGroup("Test - Switch Vehicle");
        Switcher("JTMABABA8RA060836");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void scanVinValidations() {
        sc.startStepsGroup("Test - Scan VIN Validations");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        scanVIN();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void signOutTest() {
        sc.startStepsGroup("Sign Out Test");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void Switcher(String strVin) {
        boolean vinFound = false;
        createLog("Started:Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        //click open vehicle switcher icon
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0);
        click("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'vin_show_icon_cta')]",0);
        click("NATIVE","xpath=//*[contains(@id,'vin_show_icon_cta')]",0,1);
        sc.syncElements(2000,5000);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0, "text");
        createLog("Vehicle VIN found on the vehicle switcher screen: " + defaultVin);
        if (strVin.equalsIgnoreCase(defaultVin)) {
            createLog("Vin#" + strVin + " is Already Default Vin");
            sc.flickElement("NATIVE", "xpath=//*[contains(@text,'VIN')]", 0, "down");
            sc.syncElements(2000, 10000);
        } else {
            for (int i = 2; i <= 6; i++) {
                sc.elementSwipeWhileNotFound("NATIVE", "xpath=//*[@class='android.widget.ScrollView']", "Right", sc.p2cx(20), 2000, "NATIVE", "xpath=(//*[@content-desc='Switcher Vehicle Image'])[" + i + "]", 0, 2000, 1, false);
                click("NATIVE", "xpath=(//*[@content-desc='Switcher Vehicle Image'])[" + i + "]", 0, 1);
                sc.syncElements(3000, 9000);
                String vehicleSwitch =sc.elementGetProperty("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0, "text");
                createLog("Vehicle VIN found on the vehicle switcher screen, Switching of the Vehicle started to switch VIN# : " + vehicleSwitch);
                verifyElementFound("NATIVE", "xpath=//*[contains(@text,'VIN')]", 0);
                if (strVin.equalsIgnoreCase(vehicleSwitch)) {
                    sc.swipe("Down", sc.p2cy(30), 500);
                    sc.syncElements(2000, 8000);
                    createLog("Vin is selected and vehicle switcher completed Selecting the vehicle: " + strVin);
                    verifyElementFound("NATIVE", "xpath=//*[@text='Make Default']/following-sibling::*[@class='android.widget.Button'] | //*[@text='Default']", 0);
                    verifyElementFound("NATIVE", "xpath=//*[@text='Select']/following-sibling::*[@class='android.widget.Button' and @enabled='true']", 0);
                    click("NATIVE", "xpath=//*[@text='Select']/following-sibling::*[@class='android.widget.Button']", 0, 1);
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
        createLog("Completed:Switching to vehicle: " + strVin);
    }

    public static void Default(String strVin) {
        boolean vinFound = false;
        createLog("Started:Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']"))
            click("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']", 0, 1);
        sc.syncElements(3000, 9000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicles']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='addVehicle']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'VIN')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Image shown for illustration purposes only.\n" + " Vehicle may not be as shown.']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Remove Vehicle']", 0, 1000, 2, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remove Vehicle']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'vin_show_icon_cta')]",0);
        click("NATIVE","xpath=//*[contains(@id,'vin_show_icon_cta')]",0,1);
        sc.syncElements(2000,5000);
        String defaultVin = sc.elementGetProperty("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0, "text");
        createLog("Vehicle VIN found on the vehicle switcher screen: " + defaultVin);
        if (strVin.equalsIgnoreCase(defaultVin)) {
            createLog("Vin#" + strVin + " is Already Default Vin");
            sc.flickElement("NATIVE", "xpath=//*[contains(@text,'VIN')]", 0, "down");
            sc.syncElements(2000, 10000);
        } else {
            for (int i = 2; i <= 6; i++) {
                sc.elementSwipeWhileNotFound("NATIVE", "xpath=//*[@class='android.widget.ScrollView']", "Right", sc.p2cx(20), 2000, "NATIVE", "xpath=(//*[@content-desc='Switcher Vehicle Image'])[" + i + "]", 0, 2000, 1, false);
                click("NATIVE", "xpath=(//*[@content-desc='Switcher Vehicle Image'])[" + i + "]", 0, 1);
                sc.syncElements(3000, 9000);
                verifyElementFound("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0);
                String vehicleSwitch = sc.elementGetProperty("NATIVE", "xpath=//*[@text='VIN']/following-sibling::*[@class='android.widget.TextView']", 0, "text");
                createLog("Vehicle VIN found on the vehicle switcher screen, Switching of the Vehicle started to switch VIN# : " + vehicleSwitch);
                verifyElementFound("NATIVE", "xpath=//*[contains(@text,'VIN')]", 0);
                if (strVin.equalsIgnoreCase(vehicleSwitch)) {
                    sc.swipe("Down", sc.p2cy(30), 500);
                    sc.syncElements(2000, 8000);
                    createLog("Vin is selected and vehicle switcher completed Selecting the vehicle: " + strVin);
                    verifyElementFound("NATIVE", "xpath=//*[@text='Make Default']/following-sibling::*[@class='android.widget.Button' and @enabled='true']", 0);
                    click("NATIVE", "xpath=//*[@text='Make Default']/following-sibling::*[@class='android.widget.Button']", 0, 1);
                    sc.syncElements(3000, 9000);
                    createLog("VIN# " + strVin + " Defaulted");
                    vinFound = true;
                    sc.flickElement("NATIVE", "xpath=//*[contains(@text,'VIN')]", 0, "down");
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
        createLog("Completed:Switching to vehicle: " + strVin);
    }

    public static void scanVIN() {
        createLog("Started - Scan Vin Validations");
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
        verifyElementFound("NATIVE", "xpath=//*[@text='Add your vehicle']", 0);
        click("NATIVE", "xpath=//*[@id='bt_scan_vin']", 0, 1);
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_message']")) {
            sc.click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
        }
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
