package v2update.subaru.android.usa.health;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruHealthAndroid extends SeeTestKeywords {
    String testName = "Health-Android";

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
        //selectionOfCountry_Android("USA");
        //selectionOfLanguage_Android("English");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    //Vehicle AAlerts, Key Fob and Vehicle Health Report
    @Test
    @Order(1)
    public void vehicleAlerts21MMTest() {
        sc.startStepsGroup("Test - Vehicle Alerts validations - 21MM");
        vehicleAlerts();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void keyFob21MMTest() {
        sc.startStepsGroup("Test - Key Fob validations - 21MM");
        keyFob();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void vehicleHealthReport21MMTest() {
        sc.startStepsGroup("Test - Vehicle Health Report validations - 21MM");
        vehicleHealthReport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void signOutTest21MM() {
        sc.startStepsGroup("Test - Sign out - 21MM");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleAlerts() {
        createLog("Started - Vehicle Alerts");
        if(!sc.isElementFound("NATIVE","xpath=//*[@content-desc='Car']")){
            reLaunchApp_android();
            click("NATIVE", "xpath=//*[@text='Health']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Car']", 0);
        }

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Vehicle Health Report']", 0, 1000, 5, false);
        if(!(sc.isElementFound("NATIVE","xpath=//*[contains(@resource-id,'vehicle_health_vehicle_alert_sub')]"))){
            sc.swipe("down", sc.p2cy(30), 1000);
            sc.syncElements(5000, 10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Alerts']", 0);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='No vehicle alerts']", 0)){
            createLog("No Vehicle Alerts for vehicle");
            verifyElementFound("NATIVE", "xpath=//*[@text='No vehicle alerts']", 0);
            /*  Raised defect for finding Vehicle Alerts Icon and status Icon . Added below xpaths for temporary*/
            verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_health_vehicle_alert_icon']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@id='vehicle_health_vehicle_alert_sub_icon_success'])[1]", 0);
        } else {
            createLog("ALERT - Vehicle Alerts for vehicle");
            //Validate Warning Alert icon
            createLog("vehicle contains vehicle alerts");
            verifyElementFound("NATIVE","xpath=//*[@id='vehicle_health_vehicle_alert_sub_icon_fail']",0);
            String alertsDetails=sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'active vehicle alert')]",0,"text");
            createLog("Alerts count details: "+alertsDetails);
        }
        createLog("Completed - Vehicle Alerts");
    }

    public static void keyFob() {
        createLog("Started - Key Fob");
        if(!sc.isElementFound("NATIVE","xpath=//*[@content-desc='Car']")){
            reLaunchApp_android();
            click("NATIVE", "xpath=//*[@text='Health']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Car']", 0);
        }
        sc.swipeWhileNotFound("Down",sc.p2cy(70),2000,"NATIVE","xpath=//*[@text='Key Fob']",0,1000,5,false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Key Fob']", 0);

        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Key Fob']//following-sibling::*[contains(@text,'Good')]", 0)){
            createLog("Key Fob battery level is Good");
            verifyElementFound("NATIVE", "xpath=//*[@text='Key Fob']//following-sibling::*[contains(@text,'Good')]", 0);

            //key fob screen
            createLog("Started - Key Fob Screen Validations");
            //
            click("NATIVE", "xpath=//*[@text='Key Fob']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Key Fob']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Key fob battery level is good.']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='OK']", 0);
            click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            sc.syncElements(5000, 30000);

            //verify Health screen is displayed
            verifyElementFound("NATIVE", "xpath=//*[@text='Safety Recalls']", 0);
            createLog("Completed - Key Fob Screen Validations");

        } else {
            createLog("ALERT - Key Fob battery level is Low");
            verifyElementFound("NATIVE","xpath=//*[@text='Key Fob']//following-sibling::*[contains(@text,'Low')]",0);

            //verify keyfob screen
            createLog("Started - Key Fob Screen Validations");

            click("NATIVE", "xpath=//*[@text='Key Fob']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Key Fob']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Key fob battery is low, please replace soon.']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='OK']", 0);
            click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            sc.syncElements(5000, 30000);

            //verify Health screen is displayed
            verifyElementFound("NATIVE", "xpath=//*[@text='Safety Recalls']", 0);
            createLog("Completed - Key Fob Screen Validations");
        }
        //
        createLog("Completed - Key Fob Validations");
    }

    public static void vehicleHealthReport() {
        createLog("Started - Vehicle Health Report");
        if(!sc.isElementFound("NATIVE","xpath=//*[@content-desc='Car']")){
            reLaunchApp_android();
            click("NATIVE", "xpath=//*[@text='Health']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Car']", 0);
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Vehicle Health Report']", 0, 1000, 5, false);

        if(!(sc.isElementFound("NATIVE","xpath=//*[contains(@resource-id,'vehicle_health_vehicle_alert_sub_icon')]"))){
            sc.swipe("down", sc.p2cy(30), 1000);
            sc.syncElements(5000, 10000);
        }

        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Health Report']", 0);

        //VHR screen
        createLog("Started - VHR Screen Validations");
        click("NATIVE", "xpath=//*[@text='Vehicle Health Report']", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=(//*[@text='Vehicle Health Report' or @text='VEHICLE HEALTH REPORT'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Vehicle Health Report' or @text='VEHICLE HEALTH REPORT'])[2]", 0);

        //Vehicle Alerts
        verifyElementFound("NATIVE", "xpath=//*[@id='vhrgen_alerts']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='no_va_img']")) {
            createLog("no vehicle alerts");
            verifyElementFound("NATIVE", "xpath=//*[@id='no_va_img']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'There are no Vehicle Alerts')]", 0);
        }else{
            createLog("vehicle contains vehicle alerts");
            String alertsDetails=sc.elementGetProperty("NATIVE","xpath=//*[@id='cv_desc']",0,"text");
            createLog("Alerts details: "+alertsDetails);
        }

        //Recall and Service Campaign
        verifyElementFound("NATIVE", "xpath=//*[@text='Recalls & Service Campaigns']", 0);
        verifyElementFound("NATIVE","xpath=//*[@text='Visit subaru.com and enter your VIN for the most up to date information on recalls and service campaigns.']",0);

        //Maintenance Status
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Vehicle Status']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Maintenance Status']", 0);
        verifyElementFound("NATIVE","xpath=//*[@id='subaru_maintenance_desc']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'The recommended maintenance intervals for your Subaru are based on both months and miles, whichever occurs first. Please refer to your Subaru Solterra Warranty and Maintenance Booklet for detail regarding Subaru')]",0);

        //Vehicle Status
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Vehicle Status' and @onScreen='true']", 0, 1000, 5, false);
        sc.swipe("Down",sc.p2cy(70), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Status']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vhrgen_md_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Miles driven']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vs_miles_driven']", 0);

        //smart key section
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='vs_smart_key_img' and @onScreen='true']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Smart Key Battery']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vs_smart_key_img']", 0);

        //Thank you section
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Thank you.' and @id='vhrgen_thankyou']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Thank you.' and @id='vhrgen_thankyou']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'1. Information provided is based on the last time data was collected from the vehicle and may not be up to date')]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'1. Information provided is based on the last time data was collected from the vehicle and may not be up to date')]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='vhrgen_notes6']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@id='vhrgen_notes6']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'3. To maintain the high performance of your vehicle, your dealer may recommend additional services')]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'3. To maintain the high performance of your vehicle, your dealer may recommend additional services')]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'If you have any questions regarding this service')]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'If you have any questions regarding this service')]", 0);

        //Click Back
        click("NATIVE","xpath=//*[@content-desc='Navigate up']",0,1);
        sc.syncElements(5000, 30000);
        createLog("Completed - Vehicle Health Report Validations");
    }
}
