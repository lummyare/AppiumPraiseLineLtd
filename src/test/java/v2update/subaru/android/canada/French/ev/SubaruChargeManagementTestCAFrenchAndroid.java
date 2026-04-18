package v2update.subaru.android.canada.French.ev;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruChargeManagementTestCAFrenchAndroid extends SeeTestKeywords {
    String testName = "EV - SubaruChargeManagementTestCAFrenchAndroid";

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
        testName = System.getProperty("cloudApp") + testName;
        android_Setup2_5(testName);
        sc.startStepsGroup("21mm login");
        selectionOfCountry_Android("canada");
        selectionOfLanguage_Android("french");
        android_keepMeSignedIn(true);
        android_emailLoginFrench("subaruprod2_21mm@mail.tmnact.io", "Test@123");
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
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Trouver des stations']"), 0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Chargé à')]"), 0);
        }

        click("NATIVE",convertTextToUTF8("xpath=//*[contains(@text,'until full')] | //*[contains(@text,'Chargé à')]"),0,1);
        sc.syncElements(20000,40000);

        //verify miles under Charge Info heading
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Info sur la recharge']"), 0);


        String climateOnMiles=sc.elementGetProperty("NATIVE", convertTextToUTF8("xpath=//*[@text='mi' and ./parent::*[@id='est_miles_ac_on_widget']]/preceding-sibling::*"),0,"text");
        String climateOffMiles=sc.elementGetProperty("NATIVE", convertTextToUTF8("xpath=//*[@text='mi' and ./parent::*[@id='est_miles_ac_off_widget']]/preceding-sibling::*"),0,"text");
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
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Créer un horaire']"))) {
            reLaunchApp_android();
            click("NATIVE",convertTextToUTF8("xpath=//*[contains(@text,'until full')] | //*[contains(@text,'Chargé à')]"),0,1);
            sc.syncElements(5000,10000);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Créer un horaire']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Créer un horaire']"), 0,1);

        //Expand Schedule card
        sc.syncElements(2000,5000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Barème des charges']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Off-Peak Hours']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Heure de début']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Heure de fin']"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Jours de semaine']",0);
        verifyElementFound("NATIVE","xpath=(//*[@text='S'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='M']",0);
        verifyElementFound("NATIVE","xpath=(//*[@text='D'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='L']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='J']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='V']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='multiday_create_schedule_image_btn']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Retour']",0);

        //navigate back to charging info page
        click("NATIVE","xpath=//*[@contentDescription='Retour']",0,1);
        sc.syncElements(2000,4000);
        createAndDeleteSchedule();
    }

    public static void createAndDeleteSchedule(){
        sc.syncElements(5000,10000);
        createLog("Create Schedule");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Info sur la recharge']"))) {
            reLaunchApp_android();
            click("NATIVE",convertTextToUTF8("xpath=//*[contains(@text,'until full')] | //*[contains(@text,'Chargé à')]"),0,1);
            sc.syncElements(5000,10000);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Créer un horaire']"), 0);
        int existingScheduleCount= sc.getElementCount("NATIVE","xpath=//*[@id='multiday_item_card']");
        createLog("Number of schedules available before creating new schedule:"+existingScheduleCount);
        sc.syncElements(2000,4000);
        //Set the start Time
        click("NATIVE","xpath=//*[@id='multiday_start_time_time_picker']/descendant::*[contains(@text,':')]",0,1);
        sc.syncElements(4000,10000);
        click("NATIVE","xpath=//*[@text='OK']",0,1);
        sc.syncElements(4000,10000);


        //Set the End time
        if(sc.isElementFound("NATIVE",convertTextToUTF8("//*[@id='multiday_end_time_switch']"))) {
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
        sc.waitForElement("NATIVE",convertTextToUTF8("//*[@text='Créer un horaire']"),0,20);
        sc.syncElements(40000,80000);
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
        verifyElementFound("NATIVE", "xpath=//*[@text='Barème des charges']", 0);
        click("NATIVE","xpath=//*[@id='multiday_delete_schedule_image_btn']",0,1);
        sc.syncElements(4000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Retour']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Oui, supprimer']"), 0);
        click("NATIVE",convertTextToUTF8("xpath=//*[@text='Oui, supprimer']"),0,1);
        sc.syncElements(10000,120000);
        sc.waitForElement("NATIVE",convertTextToUTF8("//*[@text='Info sur la recharge']"),0,20);

    }
    public static void findNearByStationsValidations(){
        sc.syncElements(5000,10000);
        createLog("Find Near By Station Validations");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Trouver des stations'] | //*[@text='Trouver des stations']")) {
            reLaunchApp_android();
            sc.syncElements(5000,10000);
            click("NATIVE",convertTextToUTF8("xpath=//*[contains(@text,'Chargé ')] | //*[contains(@text,'chargé ')]"),0,1);
            sc.syncElements(20000,40000);
        }


        if(!sc.isElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Bornes de recharge']"))) {
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
    public static void dashboardFindStation() throws IOException {
        createLog("Verifying FindStation section on dashboard screen");
        //dashboard screen
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_display_image']")) {
            reLaunchApp_android();
            sc.syncElements(5000,10000);
            click("NATIVE",convertTextToUTF8("xpath=//*[@text='Trouver des stations']"),0,1);
            sc.syncElements(20000,40000);
        }

        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Allow SUBARU SOLTERRA CONNECT to access this device’s location?']", 0))
        {
            click("NATIVE", "xpath=//*[@text='While using the app']", 0,1);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Stations à proximité']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Bornes de recharge']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Recherche par adresse ou code postal']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Chercher']", 0);

        createLog("Verified FindStation section on dashboard screen");

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
