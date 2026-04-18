package v2update.subaru.android.canada.French.status;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruVehicleStatusCAFrenchAndroid extends SeeTestKeywords{
    String testName ="VehicleStatus-CA-French-Android";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")){
            case(""):
                testName = System.getProperty("cloudApp") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
                sc.stopStepsGroup();
        }
    }
    @AfterAll
    public void exit() {exitAll(this.testName);}

    //This uses US region VIN in test
    @Test
    @Order(1)
    public void tirePressureTest(){
        sc.startStepsGroup("Test - Tire Pressure");
        tirePressure();
        sc.stopStepsGroup();
    }

    @Test //Bug where Open/Unlock Status wont show in different languages
    @Order(2)
    public void doorsTest(){
        sc.startStepsGroup("Test - Doors");
        doors();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void windowsTest(){
        sc.startStepsGroup("Test - Windows");
        windows();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void vehicleInformationUpdatedTest(){
        sc.startStepsGroup("Test - Vehicle Information Updated");
        vehicleInformationUpdated();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void tirePressure() {
        createLog("Started - tire pressure validation");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        click("NATIVE", "xpath=//*[@text='Statut']", 0, 1);
        sc.syncElements(2000, 10000);

        //vehicle image validation
        verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_status_vehicle_image']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Pression des pneus']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Pression des pneus']/following-sibling::*[@text='Bien']")){
            sc.report("Tire pressure status is Good", true);
            createLog("Tire pressure status is Good");
            //verify tire pressure icon - elements not exposed - https://toyotaconnected.atlassian.net/browse/OAD01-12701
            //verifyElementFound("NATIVE", "xpath=(//*[@text='Pression des pneus']/preceding-sibling::*[@class='android.view.View'])[1]", 0);
            //verify tire pressure check icon - elements not exposed
            //verifyElementFound("NATIVE", "xpath=(//*[@text='Pression des pneus']/preceding-sibling::*[@class='android.view.View'])[2]", 0);
        } else {
            sc.report("Tire pressure status is not Good", false);
            createLog("Tire pressure status is not Good");
            //verify tire pressure icon - elements not exposed - https://toyotaconnected.atlassian.net/browse/OAD01-12701
            //verifyElementFound("NATIVE", "xpath=(//*[@text='Pression des pneus']/preceding-sibling::*[@class='android.view.View'])[1]", 0);
            //verify tire pressure warning alerts icon - elements not exposed
            //verifyElementFound("NATIVE", "xpath=(//*[@text='Pression des pneus']/preceding-sibling::*[@class='android.view.View'])[2]", 0);
        }

        //verify tire pressure value
        String tirePressure = sc.elementGetProperty("NATIVE", "xpath=(//*[@class='android.widget.ImageView']/following::*[@class='android.widget.TextView'])[1]", 0, "text");
        if (tirePressure.equalsIgnoreCase("Informations non disponibles")) {
            sc.report("Tire pressure displayed as Information Unavailable", tirePressure.equalsIgnoreCase("Informations non disponibles"));
        } else {
            for (int i = 7; i <= 13; i++) {
                String tirePressureValue = sc.elementGetProperty("NATIVE", "xpath=(//*[@class='android.widget.ImageView']/following::*[@class='android.widget.TextView'])[" + i + "]", 0, "text");
                createLog("Tire Pressure Value: "+tirePressureValue);
                //verify tire pressure value is greater than 0 and less than or equal to 42
                sc.report("Verify Tire pressure numeric value is greater than or equal to 0 ", Integer.parseInt(tirePressureValue) >= 0);
                sc.report("Verify Tire pressure value is less than or equal to 42 ", Integer.parseInt(tirePressureValue) <= 42);

                i = i + 1;
            }
        }

        //verify tire pressure unit
        if (tirePressure.equalsIgnoreCase("Informations non disponibles")) {
            sc.report("Tire pressure displayed as Information Unavailable", tirePressure.equalsIgnoreCase("Informations non disponibles"));
        } else {
            for (int i = 8; i <= 14; i++) {
                String tirePressureUnit = sc.elementGetProperty("NATIVE", "xpath=(//*[@class='android.widget.ImageView']/following::*[@class='android.widget.TextView'])[" + i + "]", 0, "text");
                createLog("Tire Pressure Unit: "+tirePressureUnit);
                //verify tire pressure unit is psi
                sc.report("Verify Tire pressure unit is psi", tirePressureUnit.equalsIgnoreCase("psi"));

                i = i + 1;
            }
        }

        //Tire pressure updated
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'"+convertTextToUTF8("Statut des pneus actualisé")+" :')]", 0);
        String tireStatusUpdatedInfo = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'"+convertTextToUTF8("Statut des pneus actualisé")+" :')]", 0, "text");
        createLog("Tire status updated info: "+tireStatusUpdatedInfo);
        String[] tireStatusUpdatedInfoArr = tireStatusUpdatedInfo.split(": ");
        sc.report("Verify tire pressure updated info is displayed in Vehicle Status screen", !tireStatusUpdatedInfoArr[1].isEmpty());

        createLog("Completed - tire pressure validation");
    }

    public static void doors() {
        createLog("Started - doors validation");
        click("NATIVE", "xpath=//*[@text='Statut']", 0, 1);
        sc.syncElements(2000, 10000);
        //swipe if door tile not found
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 2000, "NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Portières")+"']/parent::*[@class='android.view.View'][@height>'200']", 0, 1000, 2, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Portières")+"']/parent::*[@class='android.view.View'][@height>'200']", 0);

        if (sc.isElementFound("NATIVE","xpath=//*[@text='"+convertTextToUTF8("Portières")+"']/following-sibling::*[@text='"+convertTextToUTF8("Verrouillé")+"']")) {
            sc.report("Doors are locked", true);
            createLog("Doors are locked");
            performRemoteUnlock();

            //verify Doors unlock text and lock button is displayed in Status->Doors section
            click("NATIVE", "xpath=//*[@text='Statut']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Portières")+"']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Verrouiller']", 0);

        } else {
            verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Portières")+"']/following-sibling::*[@text='"+convertTextToUTF8("Verrouiller")+"']", 0);
            sc.report("Doors are unlocked", true);
            createLog("Doors are unlocked");
        }
        //verify remote lock in status screen
        verifyRemoteLock();

        createLog("Completed - doors validation");
    }

    public static void windows() {
        createLog("Started - windows validation");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Pression des pneus']"))
            reLaunchApp_android();
        click("NATIVE", "xpath=//*[@text='Statut']", 0, 1);
        sc.syncElements(2000, 10000);
        //swipe if window tile not found
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 2000, "NATIVE", "xpath=//*[@text='Vitres']", 0, 1000, 2, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vitres']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Vitres']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 2000, "NATIVE", "xpath=//*[contains(@text,'"+convertTextToUTF8("Info du véhicule actualisée")+" :')]", 0, 1000, 2, false);
        if(sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Fermeé']/preceding-sibling::*[@text='Vitres']"))){
            sc.report("Windows closed", true);
            createLog("Windows closed");
        } else {
            verifyElementFound("NATIVE", "xpath=//*[@text='Vitres']/following-sibling::*[@text='Toit ouvrant ouvert']", 0);
            sc.report("Windows moonroof open", true);
            createLog("Windows moonroof open");
        }
        createLog("Completed - windows validation");
    }

    public static void vehicleInformationUpdated() {
        createLog("Started - vehicle Information Updated");
        //swipe till vehicle information updated Info du véhicule actualisée :
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 2000, "NATIVE", "xpath=//*[contains(@text,'"+convertTextToUTF8("Info du véhicule actualisée")+" :')]", 0, 1000, 2, false);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'"+convertTextToUTF8("Info du véhicule actualisée")+" :')]", 0);

        String lastUpdatedInfo = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'"+convertTextToUTF8("Info du véhicule actualisée")+" :')]", 0, "text");
        String[] lastUpdatedInfoArrBeforeRefresh = lastUpdatedInfo.split(": ");
        String beforeRefresh = lastUpdatedInfoArrBeforeRefresh[1].substring(9).replaceAll(":", "");
        String beforeRefreshValue = beforeRefresh.substring(0, beforeRefresh.length() - 5);
        createLog("Before refresh time :" + lastUpdatedInfoArrBeforeRefresh[1].substring(9).replaceAll(":", ""));
        sc.report(lastUpdatedInfoArrBeforeRefresh[1], true);
        createLog("Before" +beforeRefreshValue);
        sc.report("Verify last updated details is displayed in Vehicle Status screen", !lastUpdatedInfoArrBeforeRefresh[1].isEmpty());
        int beforeRefreshIntValue= Integer.parseInt(beforeRefreshValue.replaceAll("[a-zA-ZÀ-Ÿ ]",""));
        sc.sleep(60000);
        createLog("After Int Value" +beforeRefreshIntValue);
        //Vehicle Status Information refresh icon
        click("NATIVE", "xpath=//*[@id='vehicle_status_refresh_icon']", 0, 1);
        sc.syncElements(30000, 60000);

        // Verify Last Updated in Vehicle Status screen after refresh
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'"+convertTextToUTF8("Info du véhicule actualisée")+" :')]", 0);
        lastUpdatedInfo = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'"+convertTextToUTF8("Info du véhicule actualisée")+" :')]", 0, "text");
        String[] lastUpdatedInfoArrAfterRefresh = lastUpdatedInfo.split(": ");
        String afterRefresh = lastUpdatedInfoArrAfterRefresh[1].substring(9).replaceAll(":", "");
        String afterRefreshValue = afterRefresh.substring(0, afterRefresh.length() - 5);
        createLog("After" +afterRefreshValue);
        createLog("After refresh time :" + lastUpdatedInfoArrAfterRefresh[1].substring(9).replaceAll(":", ""));
        int afterRefreshValueIntValue= Integer.parseInt(afterRefreshValue.replaceAll("[a-zA-ZÀ-Ÿ ]",""));
        createLog("After Int Value" +afterRefreshValueIntValue);

        Assertions.assertTrue(afterRefreshValueIntValue > beforeRefreshIntValue);
        sc.report(lastUpdatedInfoArrAfterRefresh[1], true);

        createLog("Completed - vehicle Information Updated");
    }

    public static void performRemoteUnlock() {
        createLog("Started - unlock remote command execution");
        //Click remote tab
        click("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("À distance")+"']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Advanced Remote' or @content-desc='Advance Remote Screen']"))
            click("NATIVE", "xpath=//*[@content-desc='Advanced Remote' or @content-desc='Advance Remote Screen']", 0, 1);
        //Verify unlock icon in Advanced remote screen
        verifyElementFound("NATIVE", "xpath=(//*[@text='"+convertTextToUTF8("Déverrouiller")+"'])[1]", 0);
        sc.longClick("NATIVE", "xpath=(//*[@text='"+convertTextToUTF8("Déverrouiller")+"'])[1]", 0, 1, 0, 0);
        sc.waitForElement("NATIVE", "xpath=//*[@id='toast_title_text']", 0, 40000);
        verifyElementFound("NATIVE", "xpath=//*[@id='toast_title_text']", 0);
        sc.syncElements(5000, 10000);

        createLog("Completed unlock remote command execution");
    }

    public static void verifyRemoteLock() {
        createLog("Verifying - Lock remote command in Status screen");
        //Verify lock button
        verifyElementFound("NATIVE", "xpath=//*[@text='Verrouiller']", 0);
        sc.longClick("NATIVE", "xpath=//*[@text='Verrouiller']", 0, 1, 0, 0);
        sc.waitForElement("NATIVE", "xpath=//*[@id='toast_title_text']", 0, 40000);
        verifyElementFound("NATIVE", "xpath=//*[@id='toast_title_text']", 0);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Portières")+"']/following-sibling::*[@text='"+convertTextToUTF8("Verrouillé")+"']", 0);

        //Notification Validation, Methods dont cover French text
        android_goToNotificationsScreen();
        verifyElementFound("NATIVE", "xpath=//*[@class='android.view.ViewGroup' and @width>0 and ./*[contains(text(),'maintenant verrouillé')] and ./*[@id='iv_unread_mes']]", 0);

        //Navigate back to Dashboard screen
        //click back button in notifications screen
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(3000, 15000);
        sc.flickElement("NATIVE", "xpath=//*[@text='Compte']", 0, "Down");
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        sc.syncElements(2000, 4000);
        createLog("Verification successful - Lock remote command in Status screen");
    }
}
