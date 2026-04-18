package v2update.subaru.ios.usa.health;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.CommonUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruHealthIOS extends SeeTestKeywords {
    String testName = " - SubaruHealth-IOS";

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
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    //For SubaruNextGen+ only Vehicle Alerts, Key Fob and Vehicle Health Report

    @Test
    @Order(1)
    @Tag("Health")
    public void vehicleAlertsTest() {
        sc.startStepsGroup("Test - Vehicle Alerts validations");
        vehicleAlerts();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    @Tag("Health")
    public void keyFobTest() {
        sc.startStepsGroup("Test - Key Fob validations");
        keyFob();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    @Tag("Health")
    public void vehicleHealthReportTest() {
        sc.startStepsGroup("Test - Vehicle Health Report validations");
        vehicleHealthReport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    @Tag("SignOut")
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleAlerts() {
        createLog("Started - Vehicle Alerts");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@text='Health']", 0, 1);
        sc.syncElements(2000, 20000);

        //verify vehicle image in Health screen
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='vehicle_health_tab_vehicle_image_top']", 0);

        //For SubaruNextGen+ only Vehicle Alerts, Key Fob and Vehicle Health Report, Verify Safety Recalls, Service Campaigns tiles not displayed
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@text='Safety Recalls']", 0);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@text='Service Campaigns']", 0);

        sc.swipe("down", sc.p2cy(30), 1000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Alerts']", 0);

        if(sc.isElementFound("NATIVE", "xpath=//*[@text='vehicleAlertsGood']", 0)){
            createLog("No Vehicle Alerts for vehicle");
            verifyElementFound("NATIVE", "xpath=//*[@text='No vehicle alerts']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='vehicle_health_tab_vehicle_alerts' and @text='statusGood']", 0);
        } else {
            createLog("ALERT - Vehicle Alerts for vehicle");
            //Validate Warning Alert icon
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='vehicle_health_tab_vehicle_alerts' and @text='statusAlert']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='vehicle_health_tab_vehicle_alerts' and @text='vehicleAlertsBad']", 0);

            String vehicleAlertsDetails = sc.elementGetText("NATIVE", "xpath=//*[contains(@text,'active vehicle alert')]", 0);
            createLog("Vehicle Alerts details is: "+vehicleAlertsDetails);
            String[] vehicleAlertsCount = vehicleAlertsDetails.split("active vehicle");
            createLog("Vehicle Alerts count is: "+vehicleAlertsCount[0]);

            // Vehicle Alerts details screen
            click("NATIVE", "xpath=//*[contains(@text,'active vehicle alert')]", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@id='Vehicle Alerts']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@label='drag']/following-sibling::*[@text='vehicleAlertsBad'])[1]", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@label='drag']/following-sibling::*[@text='statusAlert']/following-sibling::*[@class='UIAStaticText'])[1]", 0);
            String vehicleAlertDet = sc.elementGetText("NATIVE", "xpath=(//*[@label='drag']/following-sibling::*[@text='statusAlert']/following-sibling::*[@class='UIAStaticText'])[1]", 0);
            createLog("Vehicle Alert details is: "+vehicleAlertDet);
            verifyElementFound("NATIVE", "xpath=//*[@text='Call Dealer']", 0);
            verifyCallDealer("Vehicle Alerts");

            //1st vehicle alert details
            click("NATIVE", "xpath=(//*[@label='drag']/following-sibling::*[@text='statusAlert']/following-sibling::*[@class='UIAStaticText'])[1]", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle Alerts' and @name='vehicle_health_details_title']", 0);
            String vehicleAlertOverview = sc.elementGetText("NATIVE", "xpath=//*[starts-with(@text,'Overview')]", 0);
            createLog("vehicle alert overview is: "+vehicleAlertOverview);
            verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Overview')]", 0);
            String vehicleAlertOverviewDetails = sc.elementGetText("NATIVE", "xpath=//*[@name='vehicle_health_details_overview_details_text']", 0);
            createLog("vehicle alert overview details is: "+vehicleAlertOverviewDetails);
            click("NATIVE", "xpath=//*[starts-with(@text,'Overview')]", 0, 1);
            sc.syncElements(2000, 10000);

            verifyElementFound("NATIVE", "xpath=//*[starts-with(@text,'Description')]", 0);
            String vehicleAlertDescription = sc.elementGetText("NATIVE", "xpath=//*[starts-with(@text,'Description')]", 0);
            createLog("Vehicle Alert description is: "+vehicleAlertDescription);
            click("NATIVE", "xpath=//*[starts-with(@text,'Description')]", 0, 1);
            sc.syncElements(2000, 10000);
            String vehicleAlertDescriptionDetails = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='vehicle_health_details_description_details_text']", 0);
            createLog("vehicle alert description details is: "+vehicleAlertDescriptionDetails);
            click("NATIVE", "xpath=//*[starts-with(@text,'Description')]", 0, 1);
            sc.syncElements(2000, 10000);

            verifyElementFound("NATIVE", "xpath=//*[@text='Call Dealer']", 0);
            verifyCallDealer("Vehicle Alerts");

            //click back to navigate to Health tab
            click("NATIVE", "xpath=//*[@accessibilityLabel='vehicle_health_details_back_button']", 0, 1);
            sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Alerts']", 0);
        }
        createLog("Completed - Vehicle Alerts");
    }

    public static void keyFob() {
        createLog("Started - Key Fob");
        sc.syncElements(2000, 20000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Key Fob']", 0);

        if(sc.isElementFound("NATIVE", "xpath=//*[@text='keyFobGood']", 0)){
            createLog("Key Fob battery level is Good");
            verifyElementFound("NATIVE", "xpath=//*[@text='Good']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='vehicle_health_tab_key_fob' and @text='statusGood']", 0);

            //key fob screen
            createLog("Started - Key Fob Screen Validations");

            click("NATIVE", "xpath=//*[@text='Key Fob']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@name='keyFobGood']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@name='Key Fob']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Key fob battery level is good.']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='OK']", 0);
            click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            sc.syncElements(5000, 30000);

            //verify Health screen is displayed
            verifyElementFound("NATIVE", "xpath=//*[@text='Key Fob']", 0);
            createLog("Completed - Key Fob Screen Validations");

        } else {
            createLog("ALERT - Key Fob battery level is Low");
            /*
            automation steps will be added once Key Fob are available - alert; red, exclamation point
             */
        }

        createLog("Completed - Key Fob Validations");
    }

    public static void vehicleHealthReport() {
        createLog("Started - Vehicle Health Report");
        sc.syncElements(2000, 20000);
        sc.swipe("down", sc.p2cy(30), 1000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Health Report']", 0);

        if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='vehicle_health_tab_vehicle_health_report' and @text='statusGood']", 0)){
            createLog("Vehicle Health Report is Good");
            verifyElementFound("NATIVE", "xpath=//*[@text='vehicleHealthReport']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='vehicle_health_tab_vehicle_health_report' and @text='statusGood']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='See report']", 0);
        } else {
            createLog("ALERT - Vehicle Health Report alerts displayed");
            /*
            automation steps will be added once vehicle health report alerts are available - alert; red, exclamation point
             */
        }

        //VHR screen
        createLog("Started - VHR Screen Validations");
        click("NATIVE", "xpath=//*[@text='Vehicle Health Report']", 0, 1);
        sc.syncElements(5000, 30000);

        //screen header verify
        verifyElementFound("NATIVE", "xpath=(//*[@id='Vehicle Health Report'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='back']", 0);

        verifyElementFound("NATIVE", "xpath=(//*[@id='Vehicle Health Report'])[2]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@id='Vehicle Health Report'])[2]/following-sibling::*[@class='UIAStaticText']", 0);
        String reportMonthYear = sc.elementGetText("NATIVE", "xpath=(//*[@id='Vehicle Health Report'])[2]/following-sibling::*[@class='UIAStaticText']", 0);
        createLog("Vehicle Health Report Month and Year is: "+reportMonthYear);
        sc.report("Verify Vehicle Health Report Month and Year is not empty",!reportMonthYear.isEmpty());

        //For SubaruNextGen+ only Vehicle Alerts, Key Fob and Vehicle Health Report
        verifyElementFound("NATIVE", "xpath=//*[@text='Recalls & Service Campaigns']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Visit subaru.com and enter your VIN for the most up to date information on recalls and service campaigns.']", 0);

        //Maintenance status
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Vehicle Status' and @height>35]", 0, 1000, 10, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Maintenance Status' and @height>35]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'The recommended maintenance intervals for your Subaru are based on both months and miles, whichever occurs first.  Please refer to your Subaru Solterra Maintenance Guide for detail regarding Subaru') and contains(@text,'s recommended Vehicle Maintenance Procedures')]", 0);

        //Vehicle Status
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Miles driven' and @height>35]", 0, 1000, 10, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Status' and @height>35]", 0);
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Miles driven' and @height>35]", 0, 1000, 10, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Miles driven' and @height>35]", 0);
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Miles driven']/following-sibling::*[contains(@text,'mile') and @height>35]", 0, 1000, 10, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Miles driven']/following-sibling::*[contains(@text,'mile')]", 0);
        String milesDriven = sc.elementGetText("NATIVE", "xpath=//*[@text='Miles driven']/following-sibling::*[contains(@text,'mile')]", 0);
        String milesDrivenArr[] = milesDriven.split(" ");
        sc.report("verify unit 'mile' for miles driven", milesDrivenArr[1].contains("mile"));
        sc.report("Miles driven is Numeric value ", CommonUtils.isNumeric(milesDrivenArr[0]));
        //verify miles driven is greater than 0
        sc.report("Miles driven is not greater than or equal to 0 ", Integer.parseInt(milesDrivenArr[0]) >= 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Status' and @class='UIAStaticText']", 0);

        //smart key section
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='SMART KEY BATTERY STATUS' or @text='Smart Key Battery']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='SMART KEY BATTERY STATUS' or @text='Smart Key Battery']", 0);
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='SMART KEY BATTERY STATUS' or @text='Smart Key Battery']/preceding-sibling::*[@class='UIAImage']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='SMART KEY BATTERY STATUS' or @text='Smart Key Battery']/preceding-sibling::*[@class='UIAImage']", 0);

        //Thank you section
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Thank you.' and @class='UIAStaticText']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Thank you.' and @class='UIAStaticText']", 0);
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='1. Information provided is based on the last time data was collected from the vehicle and may not be up to date. System functionality depends on vehicle connectivity. ']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='1. Information provided is based on the last time data was collected from the vehicle and may not be up to date. System functionality depends on vehicle connectivity. ']", 0);
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='2. Actual results will vary depending on vehicle maintenance, driving habits, & road conditions.']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='2. Actual results will vary depending on vehicle maintenance, driving habits, & road conditions.']", 0);
        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='3. To maintain the high performance of your vehicle, your dealer may recommend additional services. These additional services may not be covered under your vehicle warranty or maintenance plan.']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='3. To maintain the high performance of your vehicle, your dealer may recommend additional services. These additional services may not be covered under your vehicle warranty or maintenance plan.']", 0);

        sc.swipeWhileNotFound("down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[contains(@text,'If you have any questions regarding this service')])[2]", 0, 1000, 5, false);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='If you have any questions regarding this service, If you have questions regarding this service, please call SOLTERRA CONNECT Customer Care 1-866-384-3574 for assistance.']"), 0);

        sc.swipe("down", sc.p2cy(30), 1000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Disclosure© 2020 Information provided is based on the last time data was collected from the vehicle and may not be up to date. System functionality depends on vehicle connectivity.©2011-2016 Toyota Motor Sales, U.S.A., Inc. All information contained herein applies.']"), 0);

        createLog("Completed - VHR Screen Validations");

        //Click Back
        click("NATIVE","xpath=//*[@text='back']",0,1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Health']", 0);
        click("NATIVE", "xpath=//*[@text='Remote']", 0, 1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Vehicle image')]", 0);

        createLog("Completed - Vehicle Health Report Validations");
    }

    public static void verifyCallDealer(String strHealthOption) {
        createLog("Started : Verify Call Dealer for "+strHealthOption+" ");
        click("NATIVE", "xpath=//*[contains(@label,'Call')]", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@label,'Call')])[1]", 0);
        String assistanceNumber = sc.elementGetText("NATIVE", "xpath=(//*[contains(@label,'Call')])[1]", 0);
        createLog(assistanceNumber);
        String assistanceNumberArr[] = assistanceNumber.split("all");
        String assistanceContactNumber = assistanceNumberArr[1].replaceAll(" ", "").replaceAll("[()-]*", "");
        createLog(assistanceContactNumber);
        //verify assistance contact number length
        sc.report("Verify assistance contact number length is 10", assistanceContactNumber.length() == 10);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@label,'Cancel')])[1]", 0);
        click("NATIVE", "xpath=(//*[contains(@label,'Cancel')])[1]", 0, 1);
        sc.syncElements(2000, 10000);
        createLog("Completed : Verify Call Dealer for "+strHealthOption+" ");
    }

}
