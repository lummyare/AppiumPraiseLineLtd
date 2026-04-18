package v2update.subaru.android.usa.ev;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruChargeManagementAndroid extends SeeTestKeywords {
    String testName = "EV - ChargeManagement - Android";

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
    public void chargeInfoTest() {
        sc.startStepsGroup("Charge Info");
        chargeInfoValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void scheduleTest() {
        sc.startStepsGroup("schedule validations");
        scheduleValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    public void createScheduleTest() {
        sc.startStepsGroup("Create schedule");
        createAndDeleteSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    @Disabled //Not available yet
    public void findNearByStationTest() {
        sc.startStepsGroup("Find Near By Station");
        findNearByStationsValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void dashboardFindStationTest() throws IOException {
        sc.startStepsGroup("Test - Dashboard Find Station");
        dashboardFindStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }
    public static void  chargeInfoValidations(){
        sc.syncElements(20000,40000);
        createLog("Charge Info validations");
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_range_value_text']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_range_unit_text']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_battery_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_climate_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_fuel_bar_view']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_view_chevron_icon']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'until full')]")){
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Charge Flash Icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Charging' or @text='CHARGING']", 0);
        }else{
            verifyElementFound("NATIVE", "xpath=//*[@text='Find Stations']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'% charged')]", 0);
        }

        click("NATIVE","xpath=//*[contains(@text,'until full')] | //*[contains(@text,'charged')]",0,1);
        sc.syncElements(20000,40000);

        //verify miles under Charge Info heading
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Charge Info']", 0);


        String climateOnMiles=sc.elementGetProperty("NATIVE", "xpath=(//*[@id='est_miles_ac_on_widget']/child::*[@text])[1]",0,"text");
        String climateOffMiles=sc.elementGetProperty("NATIVE", "xpath=(//*[@id='est_miles_ac_off_widget']/child::*[@text])[1]",0,"text");
        createLog("Climate on mi value :"+ climateOnMiles);
        createLog("Climate off mi value :"+climateOffMiles);
        verifyElementFound("NATIVE", "xpath=//*[@text='CLIMATE ON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='CLIMATE OFF']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='CLIMATE ON']/following::*[@contentDescription='CLIMATE OFF']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='CLIMATE OFF']/following::*[@contentDescription='CLIMATE OFF']", 0);

    }


    public static void scheduleValidations(){
        sc.syncElements(5000,10000);
        createLog("schedule validations");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Create Schedule']")) {
            reLaunchApp_android();
            click("NATIVE","xpath=//*[contains(@text,'until full')] | //*[contains(@text,'charged')]",0,1);
            sc.syncElements(5000,10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Create Schedule']", 0);

        if (sc.isElementFound("NATIVE","xpath=//*[@text='Recharge in app']")) {
            createLog("No Saved schedules");
            verifyElementFound("NATIVE","xpath=//*[@text='Create a schedule to charge your vehicle.']",0);
        }
        else {
            createLog("Saved Schedule found");
        }

        //Expand Schedule card
        click("NATIVE","xpath=//*[@text='Create Schedule']",0,1);
        sc.syncElements(2000,5000);
        verifyElementFound("NATIVE","xpath=//*[@text='Charge Schedule']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Off-Peak Hours']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Start Time']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='End Time']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Set the time when you want your vehicle to stop charging']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Days of Week']",0);
        verifyElementFound("NATIVE","xpath=(//*[@text='S'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='M']",0);
        verifyElementFound("NATIVE","xpath=(//*[@text='T'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='W']",0);
        verifyElementFound("NATIVE","xpath=(//*[@text='T'])[2]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='F']",0);
        verifyElementFound("NATIVE","xpath=(//*[@text='S'])[2]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='multiday_create_schedule_image_btn']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='multiday_back_btn']",0);

        //navigate back to charging info page
        click("NATIVE","xpath=//*[@id='multiday_back_btn']",0,1);
        sc.syncElements(2000,4000);
        createAndDeleteSchedule();
    }

    public static void createAndDeleteSchedule(){
        sc.syncElements(5000,10000);
        createLog("Create Schedule");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Create Schedule']")) {
            reLaunchApp_android();
            click("NATIVE","xpath=//*[contains(@text,'until full')] | //*[contains(@text,'charged')]",0,1);
            sc.syncElements(20000,40000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Create Schedule']", 0);
        int existingScheduleCount= sc.getElementCount("NATIVE","xpath=//*[@id='multiday_item_card']");
        createLog("Number of schedules available before creating new schedule:"+existingScheduleCount);
        click("NATIVE","xpath=//*[@text='Create Schedule']",0,1);
        sc.syncElements(2000,4000);
        //Set the start Time
        click("NATIVE","xpath=//*[@id='multiday_start_time_time_picker']/descendant::*[contains(@text,':')]",0,1);
        sc.syncElements(4000,10000);
        click("NATIVE","xpath=//*[@text='OK']",0,1);
        sc.syncElements(4000,10000);

        //Set the End time
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Set the time when you want your vehicle to stop charging']")) {
            createLog("Setting the End Time");
            click("NATIVE", "xpath=//*[@id='multiday_end_time_switch']", 0, 1);
            sc.syncElements(5000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@id='multiday_end_time_time_picker']", 0);
        }
        click("NATIVE","xpath=//*[@id='multiday_end_time_time_picker']/descendant::*[contains(@text,':')]",0,1);
        sc.syncElements(4000,10000);
        click("NATIVE","xpath=//*[@text='OK']",0,1);
        sc.syncElements(4000,10000);
        click("NATIVE","xpath=//*[@text='M']",0,1);
        sc.syncElements(3000,10000);
        //Select tick mark
        click("NATIVE","xpath=//*[@id='multiday_create_schedule_image_btn']",0,1);
        sc.waitForElement("NATIVE","xpath=//*[@text='Create Schedule']",0,20);
        sc.syncElements(40000,80000);
        verifyElementFound("NATIVE","xpath=//*[@text='Charge Info']",0);
        int newScheduleCount= sc.getElementCount("NATIVE","xpath=//*[@id='multiday_item_card']");
        createLog("Number of schedules available after creating new schedule:"+newScheduleCount);
        //Verify Scheduled creation successful
        if(newScheduleCount>existingScheduleCount){
            sc.report("Schedule Created Successfully",true);
            createLog("Schedule Created Successfully");
        }else {
            sc.report("Schedule Creation failed",false);
            createErrorLog("Schedule Creation failed");
        }
        //delete schedule
        click("NATIVE","xpath=//*[@id='multiday_item_card']",0,1);
        sc.syncElements(4000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Charge Schedule']", 0);
        click("NATIVE","xpath=//*[@id='multiday_delete_schedule_image_btn']",0,1);
        sc.syncElements(4000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Delete Schedule']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Are you sure you want to delete this schedule?']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Go Back']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Yes, Delete']", 0);
        click("NATIVE","xpath=//*[@text='Yes, Delete']",0,1);
        sc.syncElements(10000,120000);
        sc.waitForElement("NATIVE","xpath=//*[@text='Charge Info']",0,20);

    }
    public void findNearByStationsValidations(){
        sc.syncElements(5000,10000);
        createLog("Find Near By Station Validations");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@content-desc,'History')] | //*[contains(@content-desc,'HISTORY')]")) {
            reLaunchApp_android();
            //VehicleSelectionAndroid.Switcher(strVIN);
            sc.syncElements(5000,10000);
            click("NATIVE","xpath=//*[contains(@text,'until full')] | //*[contains(@text,'charged')]",0,1);
            sc.syncElements(20000,40000);
        }


        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@contentDescription,'until fully charged')]")) {
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Find Nearby Station']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Find Nearby Station']", 0, 1);
            sc.syncElements(20000, 40000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Current Location']", 0);

            //Filters
            createLog("Partners filter validation");
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Partners']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Partners']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='ChargePoint']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='EV Connect']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='EVgo']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='FLO Network']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='close_icon']", 0);
            click("NATIVE", "xpath=//*[@content-desc='close_icon']", 0, 1);
            sc.syncElements(2000, 4000);

            createLog("Plug Types filter validation");
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Plug Types']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Plug Types']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Level 2']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='DCFast']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='close_icon']", 0);
            click("NATIVE", "xpath=//*[@content-desc='close_icon']", 0, 1);
            sc.syncElements(2000, 4000);

            createLog("Add to favorites");
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Favorites']", 0);
            //add to favorites
            for(int i=1; i<4;i++){
                sc.swipeWhileNotFound("Down", sc.p2cy(50), 2000, "NATIVE", "xpath=(//*[@content-desc='Add to favorite' and @onScreen='true'])[1]", 0, 2000, 15, false);
                if(sc.isElementFound("NATIVE","xpath=(//*[@content-desc='Add to favorite' and @onScreen='true'])[1]",0)){
                    break;
                }
            }
            String stationDetails=sc.elementGetProperty("NATIVE","xpath=(//*[@content-desc='Add to favorite'])[1]/parent::*",0,"content-desc");
            createLog("Station details:"+stationDetails);
            String stationName=stationDetails.split("\n")[0].trim();
            createLog("Station Name:"+stationName);
            click("NATIVE","xpath=(//*[@content-desc='Add to favorite'])[1]",0,1);
            sc.syncElements(3000, 30000);
            sc.swipeWhileNotFound("up", sc.p2cy(50), 2000, "NATIVE", "xpath=//*[@content-desc='Nearby Stations' and @onScreen='true']", 0, 1000, 15, false);

            click("NATIVE","xpath=//*[@content-desc='Favorites']",0,1);
            sc.syncElements(5000, 10000);
            //validate the station details in ->Found and issue and created ticket:
            if(sc.isElementFound("NATIVE","xpath=//*[@content-desc='No favorite station found']")){
                sc.report("add favorite station validation failed",false);
                createErrorLog("add favorite station validation failed");
            } else {
                sc.report("add favorite station validation passed",true);
                createLog("add favorite station validation passed");
            }

            click("NATIVE","xpath=//*[@content-desc='Favorites']",0,1);
            sc.syncElements(2000, 4000);
            createLog("Clear all filter");
            sc.swipeWhileNotFound("Left", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@content-desc='Clear All']", 0, 1000, 3, false);
            sc.syncElements(2000, 4000);
            sc.swipeWhileNotFound("Right", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@content-desc='Clear All']", 0, 1000, 3, false);

            //Search with zipcode
            click("NATIVE", "xpath=//*[@text='Current Location']", 0, 1);
            sc.syncElements(2000, 4000);
            sc.elementSendText("NATIVE", "xpath=//*[@text='Current Location']", 0, "76227");
            sc.syncElements(2000, 4000);
            if(sc.isElementFound("NATIVE","xpath=//*[@content-desc='Done']"))
                click("NATIVE","xpath=//*[@content-desc='Done']",0,1);
            sc.closeKeyboard();
            if(sc.isElementFound("NATIVE","xpath=//*[@contentDescription='Aubrey, Texas 76227, United States']"))
                click("NATIVE","xpath=//*[@contentDescription='Aubrey, Texas 76227, United States']",0,1);
            sc.syncElements(10000, 20000);
            int searchResults = sc.getAllValues("NATIVE", "xpath=//*[@content-desc='Directions']", "content-desc").length;
            if (searchResults > 0) {
                createLog(searchResults + " charging stations found");
                sc.report(searchResults + " charging stations found",true);
            }
            else {
                sc.report("No stations found",false);
                createErrorLog("No stations found");
            }
            //Expand charging station and verify details loaded
            click("NATIVE", "xpath=//*[@content-desc='Directions']/parent::*[contains(@class,'View')]", 0, 1);
            sc.syncElements(5000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Directions']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Send to Car']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Pricing']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Available Plugs']", 0);
            //verifyElementFound("NATIVE", "xpath=//*[@content-desc='Setup Wallet']", 0);
            click("NATIVE", "xpath=(//*[@class='android.widget.Button'])[1]", 0, 1);
            sc.syncElements(5000, 10000);
            //Navigate back to charge Info page
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Find Nearby Station']", 0);
        }
        else {
            sc.report("Vehicle is connected for charging. So, skipping test-find near by station",true);
            createLog("Vehicle is connected for charging. So, skipping test-find near by station");
        }
        click("NATIVE", "xpath=//*[@content-desc='close_icon']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_display_image']", 0);
    }
    public void cleanAssistValidations() {
        sc.syncElements(5000, 10000);
        createLog("Clean Assist Validations");
        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'History')] | //*[contains(@content-desc,'HISTORY')]")) {
            reLaunchApp_android();
            sc.syncElements(5000,10000);
            click("NATIVE","xpath=//*[contains(@text,'until full')] | //*[contains(@text,'charged')]",0,1);
            sc.syncElements(20000,40000);
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@content-desc,'Clean Assist')]", 0, 1000, 3, false);

        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Clean Assist')]", 0);

        String cleanAssistCardText = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@content-desc,'Clean Assist')]", 0, "content-desc");
        createLog("Clean Assist Card text: " + cleanAssistCardText);

        //Validate Charge your vehicle text
        if (cleanAssistCardText.contains("100% renewable energy") && cleanAssistCardText.contains("Enroll")) {
            sc.report(cleanAssistCardText, true);
            createLog(cleanAssistCardText);

            //Expand Clean Assist card
            createLog("Expanding Clean Assist card ");
            click("NATIVE", "xpath=xpath=//*[contains(@content-desc,'Clean Assist')]", 0, 1);
            sc.syncElements(20000, 40000);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Clean Assist']", 0);

            //What is clean Assist Validations
            createLog("Verifying clean Assist Validations? section");
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='What is Clean Assist?']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='What is Clean Assist?']//*[@class='android.widget.Button']", 0);
            if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'By opting in to the Clean Assist program, if you are enrolled in an LCFS State,')]")) {
                click("NATIVE", "xpath=//*[@content-desc='What is Clean Assist?']//*[@class='android.widget.Button']", 0, 1);
                sc.syncElements(5000, 10000);
            }
            //text Validations for Clean Assist Section
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'purchase renewable electricity to match the amount of electricity you used for charging')]", 0);

            //Collapse What is Clean Assist? section
            createLog("Collapsing What is Clean Assist? section");
            click("NATIVE", "xpath=//*[@content-desc='What is Clean Assist?']//*[@class='android.widget.Button']", 0, 1);
            sc.syncElements(5000, 10000);
            if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'By opting in to the Clean Assist program')]")) {
                createLog("Collapsed What is clean Assist section");
            } else
                createLog("failed to collapse What is Clean Assist Section");

            //Terms & Privacy validations
            createLog("Verifying Terms & Privacy section");
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Terms & Privacy']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Terms & Privacy']//*[@class='android.widget.Button']", 0);
            if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@contentDescription,'By tapping Accept')]")) {
                click("NATIVE", "xpath=//*[@content-desc='Terms & Privacy']//*[@class='android.widget.Button']", 0, 1);
                sc.syncElements(5000, 10000);
            }
            sc.swipeWhileNotFound("Down", sc.p2cy(40), 2000, "NATIVE",
                    "xpath=//*[@content-desc='Decline']", 0, 4000, 5, false);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'To learn more, please review the Clean Assist ')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Connected Services Privacy Notice']", 0);
            //Text Validations for Terms and Privacy portal:

            //strAppType = strAppType.substring(0, 1).toUpperCase() + strAppType.substring(1);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'to share vehicle charging data in order to participate in environmental initiatives as described in the ')]", 0);

            verifyElementFound("NATIVE", "xpath=//*[@content-desc='FAQs']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Decline']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Accept']", 0);

            //FAQ
            createLog("Verifying FAQ details in web page");
            click("NATIVE", "xpath=//*[@content-desc='FAQs']", 0, 1);
            sc.syncElements(10000, 30000);
            sc.verifyElementFound("NATIVE", "xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]", 0);
            String faqUrl = sc.elementGetProperty("NATIVE", "xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]", 0, "text");
            if(!(faqUrl.isEmpty())){
                sc.report("URL displayed:"+faqUrl,true);
                createLog("URL displayed:"+faqUrl);
            }else{
                sc.report("URL is empty",false);
                createErrorLog("URL is empty");
            }

            // Back to Clean Assit page
            for (int i = 1; i <= 3; i++) {
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(2000, 4000);
                if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Clean Assist']"))
                    break;
            }
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Clean Assist']", 0);

            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'back_button')]", 0);
            click("NATIVE", "xpath=//*[contains(@content-desc,'back_button')]", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_display_image']", 0);
        }
    }

    public static void dashboardFindStation() throws IOException {
        createLog("Verifying FindStation section on dashboard screen");
        //dashboard screen
        if (!sc.isElementFound("NATIVE", "xpath=//*[@contentDescription='tap to open vehicle switcher']", 0)) {
            reLaunchApp_android();
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Charging') or contains(@text,'until full') or contains(@text,'Remote Activation Pending') or contains(@text,'CHARGING')]")) {
            createLog("Vehicle is plugged in and Charging - FindStation will not be displayed");

            //verifying Charging section in dashboard
            if(sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'until full')]")) {
                createLog("Started : Vehicle is plugged in and Charging - Verifying Charging section on dashboard");
                verifyElementFound("NATIVE", "xpath=//*[@text='Charging']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@content-desc='Charge Flash Icon']", 0);
                createLog("Completed: Vehicle is plugged in and Charging - Verifying Charging section on dashboard");

                //click on Charging - Verify charge Info screen
                click("NATIVE","xpath=//*[@text='Charging']",0,1);
                sc.syncElements(20000, 40000);
                verifyElementFound("NATIVE", "xpath=//*[@content-desc='Charge Info']", 0);
                click("NATIVE","xpath=//*[@content-desc='close_icon']",0,1);
                sc.syncElements(10000, 20000);
                //verify dashboard
                verifyElementFound("NATIVE", "xpath=//*[@content-desc='Dashboard Vehicle Image']", 0);
            }

        } else {
            createLog("Vehicle is not plugged in and Charging - FindStation will be displayed");

            verifyElementFound("NATIVE", "xpath=//*[@text='Find Stations']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Charge Flash Icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@resource-id='fuel_widget_button_cta']", 0);


            click("NATIVE","xpath=//*[@text='Find Stations']",0,1);
            //Find Station screen
            createLog("Verifying FIND STATION screen");

            createLog("Handle Turn On Bluetooth popup if displayed");
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]")) {
                sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
                sc.syncElements(5000, 30000);
            }

            sc.syncElements(10000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[starts-with(@content-desc,'Nearby Stations')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Map')]", 0);

            //verify charge station details displayed in Nearby Stations screen
            verifyChargeStationDetails();
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Nearby Stations' and @onScreen='true']", 0);
            click("NATIVE","xpath=//*[@content-desc='back_button']",0,1);
            createLog("verified FIND STATIONS screen");
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Find Stations']", 0);
            createLog("Verified FindStation section on dashboard screen");
        }
    }

    public static void verifyChargeStationDetails() throws IOException {
        createLog("Verifying Charge Station Details");

        verifyElementFound("NATIVE", "xpath=//*[@text='Search by address or zip code.']", 0);
        //Search with zipcode
        sendText("NATIVE", "xpath=//*[@text='Search by address or zip code.']", 0, "76227");
        sc.syncElements(30000, 60000);
        int searchResults = sc.getAllValues("NATIVE", "xpath=//*[@content-desc='right_button']", "content-desc").length;
        if (searchResults > 0) {
            createLog(searchResults + " charging stations found");
            sc.report(searchResults + " charging stations found", true);
        } else {
            sc.report("No stations found", false);
            createErrorLog("No stations found");
        }
        //Expand charging station and verify details loaded
        click("NATIVE", "xpath=(//*[@content-desc='right_button'])[1]", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Directions']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Website']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Call Charge Station']", 0);
        click("NATIVE", "xpath=//*[contains(@content-desc,'back_button')]", 0, 1);
        createLog("Verified Charge Station Details");
    }
}
