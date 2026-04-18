package v2update.subaru.android.canada.French.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruFindStationCAFrenchAndroid extends SeeTestKeywords {

    String testName = "FindStation-Android";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")){
            case(""):
                testName = System.getProperty("cloudApp") + testName;
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
        }
        android_Setup2_5(testName);
        sc.startStepsGroup("21mm login");
        selectionOfCountry_Android("canada");
        selectionOfLanguage_Android("french");
        android_keepMeSignedIn(true);
        android_emailLoginFrench("subarustageca@mail.tmnact.io", "Test$123");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }

    //Subaru Find Station same as Toyota/Lexus Find Station for PHEV
    @Test
    @Order(1)
    public void findStationOnFind() {
        sc.startStepsGroup("Find Station on Find");
        verifyFindStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void searchStation() {
        sc.startStepsGroup("Search Station");
        verifySearchStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Open Vehicle Info']"))
            reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }

    public void verifyFindStation(){
        createLog("Verifying Find Station under Find");
        click("NATIVE", "xpath=//*[@text='Trouver']", 0, 1);
        sc.syncElements(5000, 30000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Gares']",0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Trouver des stations à proximité pour recharger']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Gares']", 0);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Trouver des stations')]"))
            click("NATIVE", "xpath=//*[contains(@text,'Trouver des stations')]", 0, 1);
        sc.syncElements(3000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        sc.syncElements(5000, 30000);
    }

    public void verifySearchStation(){
        createLog("Verifying Searching for stations");
        click("NATIVE", "xpath=//*[@text='Recherche par adresse ou code postal']", 0, 1);
        sendText("NATIVE", "xpath=//*[@text='Recherche par adresse ou code postal']", 0, "95131");
        if (sc.isElementFound("NATIVE","xpath=//*[@contentDescription='Search']")) {
            click("NATIVE", "xpath=//*[@contentDescription='Search']", 0, 1);
            sc.syncElements(4000,8000);
        }
        click("NATIVE", "xpath=//*[contains(@text,'San Jose')]", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Détails de la borne de recharge']"),0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'km')]",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Ouvert 24 heures']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Instructions']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Instructions']",0);
    }
}