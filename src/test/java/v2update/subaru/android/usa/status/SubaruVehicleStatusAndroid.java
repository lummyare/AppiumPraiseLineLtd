package v2update.subaru.android.usa.status;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruVehicleStatusAndroid extends SeeTestKeywords {
    String testName = "VehicleStatus-Android";

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
    public void doorsTest(){
        sc.startStepsGroup("Test - Doors");
        doors();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void windowsTest(){
        sc.startStepsGroup("Test - Windows");
        windows();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void vehicleInformationUpdatedTest(){
        sc.startStepsGroup("Test - Vehicle Information Updated");
        vehicleInformationUpdated();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void doors() {
        createLog("Started - doors validation");
        click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
        sc.syncElements(2000, 10000);
        sc.swipe("Down", sc.p2cx(70), 100);
        sc.syncElements(2000, 10000);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@id,'vehicle_status_refresh_icon')]", 0, 1000, 3, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);
        if (sc.isElementFound("NATIVE","xpath=//*[@text='Doors']/following-sibling::*[@text='Locked']")) {
            sc.report("Doors are locked", true);
            createLog("Doors are locked");
            verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_status_door_tile_primary_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_status_door_tile_secondary_icon_on'] | //*[@resource-id='vehicle_status_door_tile_secondary_icon_off']", 0);

            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);
        } else {
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Unlocked']", 0);
            sc.report("Doors are unlocked", true);
            createLog("Doors are unlocked");
            verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_status_door_tile_primary_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_status_door_tile_secondary_icon_on'] | //*[@resource-id='vehicle_status_door_tile_secondary_icon_off']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);
            createLog("Completed - doors validation");
        }
    }

    public static void windows() {
        createLog("Started - windows validation");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='vehicle_status_door_tile_title']")){
            reLaunchApp_android();
        }
        click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_status_door_tile_title']", 0);
        //swipe till windows sections
        sc.swipe("Down", sc.p2cx(70), 100);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@resource-id='vehicle_status_window_tile_title']", 0, 1000, 5, false);
        if(!(sc.isElementFound("NATIVE","xpath=//*[@text='Windows']"))){
            sc.swipe("down", sc.p2cy(30), 1000);
            sc.syncElements(5000, 10000);
        }
        //sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@resource-id='vehicle_status_window_tile_title']", 0, 1000, 3, false);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_status_window_tile_title']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Windows']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Windows']/following-sibling::*[@text='Closed']")){
            sc.report("Windows closed", true);
            createLog("Windows closed");
        } else {
            verifyElementFound("NATIVE", "xpath=//*[@text='Windows']/following-sibling::*[contains(@text,'open')]", 0);
            sc.report("Windows  open", true);
            createLog("Windows  open");
        }
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_status_window_tile_primary_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_status_window_tile_secondary_icon_on']|//*[@resource-id='vehicle_status_window_tile_secondary_icon_off']", 0);
        createLog("Completed - windows validation");
    }

    public static void vehicleInformationUpdated() {
        createLog("Started - vehicle Information Updated");
        if(!(sc.isElementFound("NATIVE","xpath=//*[@text='Status']",0))){
            reLaunchApp_android();
            verifyElementFound("NATIVE", "xpath=//*[@text='Status']", 0);
            click("NATIVE", "xpath=//*[@text='Status']",0,1);
            sc.syncElements(2000, 4000);
        }
        sc.swipe("Down", sc.p2cx(70), 100);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0, 1000, 5, false);
        if(!(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Vehicle Information Updated:')]"))){
            sc.swipe("down", sc.p2cy(30), 1000);
            sc.syncElements(5000, 10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0);
        String beforeRefreshValue = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0, "text");
        String[] lastUpdatedInfoArrBeforeRefresh = beforeRefreshValue.split(": ");
        String beforeRefresh = lastUpdatedInfoArrBeforeRefresh[1].substring(9).replaceAll(":", "");
        createLog("Before refresh time :" + lastUpdatedInfoArrBeforeRefresh[1].substring(9).replaceAll(":", ""));
        sc.report(lastUpdatedInfoArrBeforeRefresh[1], true);
        if(!lastUpdatedInfoArrBeforeRefresh[1].isEmpty()) {
            sc.report("Verify last updated details is displayed in Vehicle Status screen", true);
            createLog("Verify last updated details is displayed in Vehicle Status screen");
        }else{
            sc.report("Verify last updated details is not displayed in Vehicle Status screen", false);
            createLog("Verify last updated details is not displayed in Vehicle Status screen");
        }
        //Vehicle Status Information refresh icon
        click("NATIVE", "xpath=//*[@id='vehicle_status_refresh_icon']", 0, 1);
        sc.sleep(60000);
        sc.syncElements(30000, 60000);
        // Verify Last Updated in Vehicle Status screen after refresh
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0);
        String afterRefreshValue = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0, "text");
        String[] lastUpdatedInfoArrAfterRefresh = beforeRefreshValue.split(": ");
        createLog("After refresh time :" + lastUpdatedInfoArrAfterRefresh[1].substring(9).replaceAll(":", ""));

        int vehicleInfoBeforeRefresh = twentyFourHourFormat(beforeRefreshValue);
        int vehicleInfoAfterRefresh = twentyFourHourFormat(afterRefreshValue);

        createLog("Before refresh value: " +vehicleInfoBeforeRefresh);
        createLog("After refresh value: " +vehicleInfoAfterRefresh);
        Assertions.assertTrue(vehicleInfoAfterRefresh > vehicleInfoBeforeRefresh);
        createLog("Verified before refresh and after refresh value");
        createLog("Completed - vehicle Information Updated");
    }

    public static int twentyFourHourFormat(String vehicleInformationText) {
        createLog("Started: converting to 24Hour format");
        createLog("vehicle information text: "+vehicleInformationText);
        String[] vehicleInfoTimeArr = vehicleInformationText.split("at ");
        createLog("Vehicle Info Time after split: "+vehicleInfoTimeArr[1]);

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date time = null;
        try {
            time = parseFormat.parse(vehicleInfoTimeArr[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String refreshTime = displayFormat.format(time);

        String refreshValAfterFormat = refreshTime.replaceAll(":","");
        int formattedRefreshValIntVal = Integer.valueOf(refreshValAfterFormat);
        createLog("Refresh value after format: "+refreshValAfterFormat);
        createLog("Refresh value after format Int Val: "+formattedRefreshValIntVal);
        createLog("Completed: converting to 24Hour format");

        return formattedRefreshValIntVal;
    }
}
