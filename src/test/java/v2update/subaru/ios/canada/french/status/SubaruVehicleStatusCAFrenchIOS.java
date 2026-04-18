package v2update.subaru.ios.canada.french.status;

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
public class SubaruVehicleStatusCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruVehicleStatusCAFrench-IOS";
    static String actualText = "";
    static String[] expectedText;

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
        environmentSelection_iOS("stage");
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLoginFrench("subarucasol@gmail.com","Test$1234");
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }
    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }


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
    public void pullDownRefreshStatusSectionTest() {
        sc.startStepsGroup("Test - Pull Down Refresh Status Section");
        pullDownRefreshInStatusSection();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void doors() {
        createLog("Started - doors validation");
        if(!sc.isElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@text='Statut']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Portières']"), 0);

        //vehicle image
        verifyElementFound("NATIVE", "xpath=//*[@text='CarTop']", 0);

        //verify tire pressure not available
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@text='Pression des pneus']", 0);

        if (sc.isElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Portières']/following-sibling::*[@text='Verrouillé']"))) {
            sc.report("Doors are locked", true);
            createLog("Doors are locked");
            //verify doors icon
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Portières']"), 0);
            //verify doors check icon
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@text='Portières']/following-sibling::*[@text='GoodStatus'])[1]"), 0);
        } else {
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Portières']/following-sibling::*[@text='Déverrouillé']"), 0);
            //verify doors icon
            verifyElementFound("NATIVE", "xpath=//*[@text='dooropen']", 0);
            //verify doors warning alerts icon
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Portières']/following-sibling::*[@text='Alert']"), 0);
            sc.report("Doors are unlocked", false);
            createErrorLog("Doors are unlocked");
        }
        createLog("Completed - doors validation");
    }

    public static void windows() {
        createLog("Started - windows validation");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Portières']"))) {
            reLaunchApp_iOS();
            click("NATIVE", "xpath=//*[@text='Statut']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Portières']"), 0);
        }
        //swipe till windows sections
        sc.swipe("Down", sc.p2cy(30), 2000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Vitres']", 0);
        if(sc.isElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Vitres']/following-sibling::*[@text='Fermée']"))){
            sc.report("Windows closed", true);
            createLog("Windows closed");
            //verify windows icon
            verifyElementFound("NATIVE", "xpath=//*[@text='WindowClosed']", 0);
            //verify windows check icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Vitres']/following-sibling::*[@text='GoodStatus']", 0);
        } else {
            verifyElementFound("NATIVE", "xpath=//*[@text='Vitres']/following-sibling::*[contains(@text,'ouvrir')]", 0);
            sc.report("Windows open", true);
            createLog("Windows open");
            //verify windows icon
            verifyElementFound("NATIVE", "xpath=//*[@text='WindowOpen']", 0);
            //verify windows warning alerts icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Vitres']/following-sibling::*[@text='Alert']", 0);
        }
        createLog("Completed - windows validation");
    }

    public static void vehicleInformationUpdated() {
        createLog("Started - vehicle Information Updated");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Portières']"))) {
            reLaunchApp_iOS();
            click("NATIVE", "xpath=//*[@text='Statut']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Portières']"), 0);
        }
        //swipe
        sc.swipe("Down", sc.p2cy(30), 2000);
        //swipe till vehicle information updated
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Info du véhicule actualisée:')]"), 0);

        String lastUpdatedInfo = sc.elementGetProperty("NATIVE", convertTextToUTF8("//*[contains(@text,'Info du véhicule actualisée:')]"), 0, "text");
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
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Info du véhicule actualisée:')]"), 0);
        lastUpdatedInfo = sc.elementGetProperty("NATIVE", convertTextToUTF8("//*[contains(@text,'Info du véhicule actualisée:')]"), 0, "text");
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
            String convertedSplitText = convertTextToUTF8(" à ");
            String[] vehicleInfoDateArr = vehicleInformationTextBeforeRefresh.split(convertedSplitText);
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

    public static int twentyFourHourFormat(String vehicleInformationText) {
        createLog("Started: converting to 24Hour format");
        createLog("vehicle information text: "+vehicleInformationText);
        String convertedSplitText = convertTextToUTF8("à ");
        String[] vehicleInfoTimeArr = vehicleInformationText.split(convertedSplitText);
        createLog("Vehicle Info Time after split: "+vehicleInfoTimeArr[1]);
        String vehicleInfoTimeAfterReplace = vehicleInfoTimeArr[1].replaceAll("[.]*","");
        createLog("Vehicle Info Time after replace: "+vehicleInfoTimeAfterReplace);

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date time = null;
        try {
            time = parseFormat.parse(vehicleInfoTimeAfterReplace);
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


    public static void pullDownRefreshInStatusSection() {
        createLog("Started - Pull Down Refresh Status Section");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Portières']"))) {
            reLaunchApp_iOS();
            click("NATIVE", "xpath=//*[@text='Statut']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Portières']"), 0);
        }

        createLog("Started - before vehicle Information Updated");
        //swipe till vehicle information updated
        sc.swipe("Down", sc.p2cy(30), 2000);
        //swipe till vehicle information updated
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Info du véhicule actualisée:')]"), 0);

        String lastUpdatedInfo = sc.elementGetProperty("NATIVE", convertTextToUTF8("//*[contains(@text,'Info du véhicule actualisée:')]"), 0, "text");
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

        sc.flickElement("NATIVE",convertTextToUTF8("//*[@text='Portières']"),0,"down");
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
        expectedText = new String[]{"À distance", "Double tap to open vehicle switcher!", "Statut", "Santé", "Service", "Payer", "Boutique", "Trouver", "Portières", "Vitres", "VehicleStatusScreen_refresh_button"};
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
        lastUpdatedInfo = sc.elementGetProperty("NATIVE", convertTextToUTF8("//*[contains(@text,'Info du véhicule actualisée:')]"), 0, "text");
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
            String convertedSplitText = convertTextToUTF8(" à ");
            String[] vehicleInfoDateArr = vehicleInformationTextBeforeRefresh.split(convertedSplitText);
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

        click("NATIVE", convertTextToUTF8("//*[@text='À distance']"), 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);

        createLog("Completed - Pull Down Refresh Status Section");
    }
}