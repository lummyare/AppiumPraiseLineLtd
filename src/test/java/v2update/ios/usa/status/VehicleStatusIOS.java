package v2update.ios.usa.status;

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
public class VehicleStatusIOS extends SeeTestKeywords {
    String testName = "VehicleStatus-IOS";
    static String actualText = "";
    static String[] expectedText;

    @BeforeAll
    public void setup() throws Exception {
        iOS_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("21mm email Login-USA-English");
        createLog("Start: 21mm email Login-USA-English");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("hevemail"), ConfigSingleton.configMap.get("hevpassword"));
        createLog("End: 21mm email Login-USA-English");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void tirePressure21MM(){
        sc.startStepsGroup("Test - Tire Pressure 21MM");
        tirePressure();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void doors21MM(){
        sc.startStepsGroup("Test - Doors 21MM");
        doors();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void windows21MM(){
        sc.startStepsGroup("Test - Windows 21MM");
        windows();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void vehicleInformationUpdated21MM(){
        sc.startStepsGroup("Test - Vehicle Information Updated 21MM");
        vehicleInformationUpdated();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void signOutTest21MM() {
        sc.startStepsGroup("Test - Sign out - 21MM");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    //17CY Plus

    @Test
    @Order(6)
    public void emailSignIn17CYPlus() {
        sc.startStepsGroup("email SignIn 17CY Plus");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail17CYPlus"), ConfigSingleton.configMap.get("strPassword17CYPlus"));
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void tirePressure17CYPlus(){
        sc.startStepsGroup("Test - Tire Pressure 17CY Plus");
        tirePressure();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void doors17CYPlus(){
        sc.startStepsGroup("Test - Doors 17CY Plus");
        doors();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void windows17CYPlus(){
        sc.startStepsGroup("Test - Windows 17CY Plus");
        windows();
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    public void vehicleInformationUpdated17CYPlus(){
        sc.startStepsGroup("Test - Vehicle Information Updated 17CY Plus");
        vehicleInformationUpdated();
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    public void signOutTest17CYPlus() {
        sc.startStepsGroup("Test - Sign out - 17CYPlus");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    //17CY

    @Test
    @Order(12)
    public void emailSignIn17CY() {
        sc.startStepsGroup("email SignIn 17CY");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail17CY"), ConfigSingleton.configMap.get("strPassword17CY"));
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    public void tirePressure17CY(){
        sc.startStepsGroup("Test - Tire Pressure 17CY");
        tirePressure();
        sc.stopStepsGroup();
    }

    @Test
    @Order(14)
    public void doors17CY(){
        sc.startStepsGroup("Test - Doors 17CY");
        doors();
        sc.stopStepsGroup();
    }

    @Test
    @Order(15)
    public void windows17CY(){
        sc.startStepsGroup("Test - Windows 17CY");
        windows();
        sc.stopStepsGroup();
    }

    @Test
    @Order(16)
    public void vehicleInformationUpdated17CY(){
        sc.startStepsGroup("Test - Vehicle Information Updated 17CY");
        vehicleInformationUpdated();
        sc.stopStepsGroup();
    }

    @Test
    @Order(17)
    public void statusRefreshTest() {
        sc.startStepsGroup("Test - Pull Down Status Refresh");
        refreshInStatusSection();
        sc.stopStepsGroup();
    }

    @Test
    @Order(18)
    public void signOutTest17CY() {
        sc.startStepsGroup("Test - Sign out - 17CY");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void tirePressure() {
        createLog("Started - tire pressure validation");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
        sc.syncElements(40000, 80000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Tire Pressure']", 0);
        if(sc.isElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Tire Pressure']/following-sibling::*[@text='N/A']"))){
            sc.report("Tire pressure status is N/A", false);
            createErrorLog("Tire pressure status is N/A");
        } else if(sc.isElementFound("NATIVE","xpath=//*[@text='Tire Pressure']/following-sibling::*[@text='Good']")){
            sc.report("Tire pressure status is Good", true);
            createLog("Tire pressure status is Good");
            //verify tire pressure icon
            verifyElementFound("NATIVE", "xpath=//*[@text='TirePressure']", 0);
            //verify tire pressure check icon
            verifyElementFound("NATIVE", "xpath=(//*[@text='Tire Pressure']/following-sibling::*[@text='GoodStatus'])[1]", 0);
        } else {
            sc.report("Tire pressure status is not Good", false);
            createLog("Tire pressure status is not Good");
            //verify tire pressure icon
            verifyElementFound("NATIVE", "xpath=//*[@id='LowTirePressure']", 0);
            //verify tire pressure warning alerts icon
            verifyElementFound("NATIVE", "xpath=(//*[@text='Tire Pressure']/following-sibling::*[@text='Alert'])[1]", 0);
        }

        //verify tire pressure value
        boolean tirePressureBln = sc.isElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Tire Pressure']/following-sibling::*[@text='N/A']"), 0);
        if (tirePressureBln) {
            sc.report("Tire pressure displayed as Information Unavailable", false);
            createErrorLog("Tire pressure status is N/A");
        } else {
            for (int i = 1; i <= 7; i++) {
                String tirePressureValue = sc.elementGetProperty("NATIVE", "xpath=(//*[@text='ConnectedVehicleScreen_dashboardVehicleStatusTab']//*[@class='UIAStaticText'])[" + i + "]", 0, "text");
                //verify tire pressure value is greater than 0 and less than or equal to 42
                sc.report("Verify Tire pressure numeric value is greater than or equal to 0 ", Integer.parseInt(tirePressureValue) >= 0);
                sc.report("Verify Tire pressure value is less than or equal to 42 ", Integer.parseInt(tirePressureValue) <= 42);
                i = i + 1;
            }
        }

        //verify tire pressure unit
        if (tirePressureBln) {
            sc.report("Tire pressure displayed as Information Unavailable", false);
            createErrorLog("Tire pressure status is N/A");
        } else {
            for (int i = 2; i <= 8; i++) {
                String tirePressureUnit = sc.elementGetProperty("NATIVE", "xpath=(//*[@text='ConnectedVehicleScreen_dashboardVehicleStatusTab']//*[@class='UIAStaticText'])[" + i + "]", 0, "text");
                //verify tire pressure unit is psi
                sc.report("Verify Tire pressure unit is psi", tirePressureUnit.equalsIgnoreCase("psi"));
                i = i + 1;
            }
        }

        //Tire pressure updated
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Tire Status Updated:')]", 0);
        String tireStatusUpdatedInfo = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Tire Status Updated:')]", 0, "text");
        createLog("Tire status updated info: "+tireStatusUpdatedInfo);
        String[] tireStatusUpdatedInfoArr = tireStatusUpdatedInfo.split(": ");
        sc.report("Verify tire pressure updated info is displayed in Vehicle Status screen", !tireStatusUpdatedInfoArr[1].isEmpty());

        createLog("Completed - tire pressure validation");
    }

    public static void doors() {
        createLog("Started - doors validation");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Tire Pressure']"))
            reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);

        if (sc.isElementFound("NATIVE","xpath=//*[@text='Doors']/following-sibling::*[@text='Locked']")) {
            sc.report("Doors are locked", true);
            createLog("Doors are locked");
            //verify doors icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);
            //verify doors check icon
            verifyElementFound("NATIVE", "xpath=(//*[@text='Doors']/following-sibling::*[@text='GoodStatus'])[1]", 0);
        } else if (sc.isElementFound("NATIVE","xpath=//*[@text='Doors']/following-sibling::*[@text='Unlocked']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Unlocked']", 0);
            //verify doors icon
            verifyElementFound("NATIVE", "xpath=//*[@text='dooropen']", 0);
            //verify doors warning alerts icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Alert']", 0);
            sc.report("Doors are unlocked", false);
            createErrorLog("Doors are unlocked");
        } else {
            //hatch open
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Hatch open']", 0);
            //verify doors icon
            verifyElementFound("NATIVE", "xpath=//*[@text='dooropen']", 0);
            //verify doors warning alerts icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Alert']", 0);
            sc.report("Hatch is open", false);
            createErrorLog("Hatch is open");
        }
        createLog("Completed - doors validation");
    }

    public static void windows() {
        createLog("Started - windows validation");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Tire Pressure']"))
            reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Tire Pressure']", 0);
        //swipe till windows sections
        sc.swipe("Down", sc.p2cy(30), 2000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Windows']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Windows']/following-sibling::*[@text='Closed']")){
            sc.report("Windows closed", true);
            createLog("Windows closed");
            //verify windows icon
            verifyElementFound("NATIVE", "xpath=//*[@text='WindowClosed']", 0);
            //verify windows check icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Windows']/following-sibling::*[@text='GoodStatus']", 0);
        } else {
            verifyElementFound("NATIVE", "xpath=//*[@text='Windows']/following-sibling::*[contains(@text,'open')]", 0);
            sc.report("Windows or moonroof open", true);
            createLog("Windows or moonroof open");
            //verify windows icon
            verifyElementFound("NATIVE", "xpath=//*[@text='moonroof open' or @text='WindowOpen']", 0);
            //verify windows warning alerts icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Windows']/following-sibling::*[@text='Alert']", 0);
        }
        createLog("Completed - windows validation");
    }

    public static void vehicleInformationUpdated() {
        createLog("Started - vehicle Information Updated");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Tire Pressure']"))
            reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
        sc.syncElements(2000, 10000);
        //swipe
        sc.swipe("Down", sc.p2cy(30), 2000);
        //swipe till vehicle information updated
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0);

        String lastUpdatedInfo = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0, "text");
        String vehicleInformationTextBeforeRefresh = lastUpdatedInfo;
        String[] lastUpdatedInfoArrBeforeRefresh = lastUpdatedInfo.split(": ");
        String beforeRefresh = lastUpdatedInfoArrBeforeRefresh[1].substring(9).replaceAll(":", "");
        createLog(beforeRefresh);
        createLog("Before refresh time :" + lastUpdatedInfoArrBeforeRefresh[1].substring(9).replaceAll(":", ""));
        sc.report(lastUpdatedInfoArrBeforeRefresh[1], true);
        sc.report("Verify last updated details is displayed in Vehicle Status screen", !lastUpdatedInfoArrBeforeRefresh[1].isEmpty());
        sc.sleep(60000);
        //Vehicle Status Information refresh icon
        click("NATIVE", "xpath=//*[@text='VehicleStatusScreen_refresh_button']", 0, 1);
        sc.syncElements(60000, 120000);
        sc.swipe("Down", sc.p2cy(30), 2000);
        sc.syncElements(5000, 10000);

        // Verify Last Updated in Vehicle Status screen after refresh
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0);
        lastUpdatedInfo = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0, "text");
        String vehicleInformationTextAfterRefresh = lastUpdatedInfo;
        String[] lastUpdatedInfoArrAfterRefresh = lastUpdatedInfo.split(": ");
        String afterRefresh = lastUpdatedInfoArrAfterRefresh[1].substring(9).replaceAll(":", "");
        createLog(afterRefresh);
        createLog("After refresh time :" + lastUpdatedInfoArrAfterRefresh[1].substring(9).replaceAll(":", ""));
        sc.report("Verify last updated details is displayed in Vehicle Status screen", !lastUpdatedInfoArrAfterRefresh[1].isEmpty());

        createLog("Vehicle info text before refresh: " +vehicleInformationTextBeforeRefresh);
        createLog("Vehicle info text after refresh: " +vehicleInformationTextAfterRefresh);

        if(vehicleInformationTextBeforeRefresh.equals(vehicleInformationTextAfterRefresh)) {
            createErrorLog("Vehicle information is not updated after refresh- " +"Before Refresh: "+vehicleInformationTextBeforeRefresh +" and After Refresh: "+vehicleInformationTextAfterRefresh);
        } else {
            String[] vehicleInfoDateArr = vehicleInformationTextBeforeRefresh.split(" at");
            createLog("Vehicle Info date after split (Today/Yesterday/any date): "+vehicleInfoDateArr[0]);

            int beforeRefreshValueInt = twentyFourHourFormat(vehicleInformationTextBeforeRefresh);
            int afterRefreshValueInt = twentyFourHourFormat(vehicleInformationTextAfterRefresh);

            createLog("Before refresh value: " +beforeRefreshValueInt);
            createLog("After refresh value: " +afterRefreshValueInt);

            if(vehicleInfoDateArr[0].equalsIgnoreCase("today")) {
                createLog("Before & After refresh value falls on same day - today");
                Assertions.assertTrue(afterRefreshValueInt > beforeRefreshValueInt);
            } else {
                createLog("Before & After refresh value does not fall on same day");
                Assertions.assertTrue(afterRefreshValueInt > 0);
            }
        }
        createLog("Verified before refresh and after refresh value");
        createLog("Completed - vehicle Information Updated");
    }

    public static void performRemoteUnlock() {
        createLog("Started - unlock remote command execution");
        //Click remote tab
        click("NATIVE", "xpath=//*[@text='Remote']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_door_unlock_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_door_unlock_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Unlocking");

        //Notifications validation
        iOS_goToNotificationsScreen();
        //verify remote unlock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now unlocked","Unlock");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        
        createLog("Completed unlock remote command execution");
    }

    public static void verifyRemoteLock() {
        createLog("Verifying - Lock remote command in Status screen");
        //Verify lock button
        verifyElementFound("NATIVE", "xpath=//*[@text='VehicleStatusScreen_lock_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@text='VehicleStatusScreen_lock_button']", 0, 1, 0, 0);
        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Locked']", 0);

        iOS_goToNotificationsScreen();
        //verify remote lock notification is displayed
        remoteCommandNotificationVerification("The vehicle is now locked","already locked","Lock");

        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        createLog("Verification successful - Lock remote command in Status screen");
    }

    public static void statusDoorsUnlockLockValidation() {
        createLog("Started - Status doors Unlock Lock validation");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'A new iOS update is now available')])[1]"))
            sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        } else {
            reLaunchApp_iOS();
        }
        click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);

        if (sc.isElementFound("NATIVE","xpath=//*[@text='Doors']/following-sibling::*[@text='Locked']")) {
            sc.report("Doors are locked", true);
            createLog("Doors are locked");
            //verify doors icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);
            //verify doors check icon
            verifyElementFound("NATIVE", "xpath=(//*[@text='Doors']/following-sibling::*[@text='GoodStatus'])[1]", 0);

            //perform remote unlock - Lock button should be displayed in Status->Doors section
            performRemoteUnlock();

            //verify Doors unlock text and lock button is displayed in Status->Doors section
            sc.syncElements(2000, 10000);
            click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Unlocked']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='VehicleStatusScreen_lock_button']", 0);

        } else {
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Unlocked']", 0);
            sc.report("Doors are unlocked", true);
            createLog("Doors are unlocked");
            //verify doors icon
            verifyElementFound("NATIVE", "xpath=//*[@text='dooropen']", 0);
            //verify doors warning alerts icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Doors']/following-sibling::*[@text='Alert']", 0);

        }
        //verify remote lock in status screen
        verifyRemoteLock();

        createLog("Completed - Status doors Unlock Lock validation");
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

    public static void refreshInStatusSection() {
        createLog("Started - Pull Down Refresh Status Section");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        //swipe to refresh Status screen
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Vehicle image')]", 0);

        click("NATIVE", "xpath=//*[@text='Status']", 0, 1);
        sc.syncElements(10000, 60000);

        createLog("Started - before vehicle Information Updated");
        //swipe till vehicle information updated
        sc.swipe("Down", sc.p2cy(30), 2000);
        //swipe till vehicle information updated
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0);

        String lastUpdatedInfo = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0, "text");
        String vehicleInformationTextBeforeRefresh = lastUpdatedInfo;
        String[] lastUpdatedInfoArrBeforeRefresh = lastUpdatedInfo.split(": ");
        String beforeRefresh = lastUpdatedInfoArrBeforeRefresh[1].substring(9).replaceAll(":", "");
        createLog(beforeRefresh);
        createLog("Before refresh time :" + lastUpdatedInfoArrBeforeRefresh[1].substring(9).replaceAll(":", ""));
        sc.report(lastUpdatedInfoArrBeforeRefresh[1], true);
        sc.report("Verify last updated details is displayed in Vehicle Status screen", !lastUpdatedInfoArrBeforeRefresh[1].isEmpty());
        sc.sleep(60000);
        sc.swipe("up", sc.p2cy(30), 2000);
        sc.syncElements(5000, 30000);
        createLog("Completed - before vehicle Information Updated");

        sc.flickElement("NATIVE","xpath=//*[@text='Tire Pressure']",0,"down");
        //verify refresh spinner icon
        if(sc.waitForElement("NATIVE","xpath=//*[@text='Refresh']",0,20000)){
            createLog("Refresh - Spinner icon displayed");
        } else {
            createLog("Refresh - Spinner icon not displayed");
        }
        delay(5000);
        sc.syncElements(30000, 60000);
        if(!sc.waitForElement("NATIVE","xpath=//*[@text='Refresh']",0,20000)){
            createLog("Refresh - Spinner icon not displayed");
        } else {
            createLog("Refresh - Spinner icon is still displayed after 30 sec wait");
        }

        createLog("Verifying status details after refresh");
        sc.syncElements(5000, 60000);
        //Verify Status texts
        actualText = sc.getText("NATIVE");
        createLog(actualText);
        expectedText = new String[]{"Odometer", "Double tap to open vehicle switcher!", "Fuel", "Remote", "Status", "Health", "Service", "Pay", "Shop", "Find", "Tire Pressure", "Doors", "Windows", "VehicleStatusScreen_refresh_button"};
        for (String strText : expectedText) {
            if (actualText.contains(strText)) {
                createLog("Validating text: " + strText);
                sc.report("Validation of " + strText, true);
            } else {
                sc.report("Validation of " + strText, false);
            }
        }
        createLog("Verified status details after refresh");


        createLog("Started - after vehicle Information Updated");
        //swipe till vehicle information updated
        sc.swipe("Down", sc.p2cy(30), 2000);
        //swipe till vehicle information updated
        // Verify Last Updated in Vehicle Status screen after refresh
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0);
        lastUpdatedInfo = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'Vehicle Information Updated:')]", 0, "text");
        String vehicleInformationTextAfterRefresh = lastUpdatedInfo;
        String[] lastUpdatedInfoArrAfterRefresh = lastUpdatedInfo.split(": ");
        String afterRefresh = lastUpdatedInfoArrAfterRefresh[1].substring(9).replaceAll(":", "");
        createLog(afterRefresh);
        createLog("After refresh time :" + lastUpdatedInfoArrAfterRefresh[1].substring(9).replaceAll(":", ""));
        sc.report("Verify last updated details is displayed in Vehicle Status screen", !lastUpdatedInfoArrAfterRefresh[1].isEmpty());

        createLog("Vehicle info text before refresh: " +vehicleInformationTextBeforeRefresh);
        createLog("Vehicle info text after refresh: " +vehicleInformationTextAfterRefresh);

        if(vehicleInformationTextBeforeRefresh.equals(vehicleInformationTextAfterRefresh)) {
            createErrorLog("Vehicle information is not updated after refresh- " +"Before Refresh: "+vehicleInformationTextBeforeRefresh +" and After Refresh: "+vehicleInformationTextAfterRefresh);
        } else {
            String[] vehicleInfoDateArr = vehicleInformationTextBeforeRefresh.split(" at");
            createLog("Vehicle Info date after split (Today/Yesterday/any date): "+vehicleInfoDateArr[0]);

            int beforeRefreshValueInt = twentyFourHourFormat(vehicleInformationTextBeforeRefresh);
            int afterRefreshValueInt = twentyFourHourFormat(vehicleInformationTextAfterRefresh);

            createLog("Before refresh value: " +beforeRefreshValueInt);
            createLog("After refresh value: " +afterRefreshValueInt);

            if(vehicleInfoDateArr[0].equalsIgnoreCase("today")) {
                createLog("Before & After refresh value falls on same day - today");
                Assertions.assertTrue(afterRefreshValueInt > beforeRefreshValueInt);
            } else {
                createLog("Before & After refresh value does not fall on same day");
                Assertions.assertTrue(afterRefreshValueInt > 0);
            }
        }
        createLog("Verified before refresh and after refresh value");
        createLog("Completed - after vehicle Information Updated");

        createLog("Completed - Pull Down Refresh Status Section");
    }
}
