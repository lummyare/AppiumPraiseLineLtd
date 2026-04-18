package v2update.subaru.ios.canada.french.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruVehicleSoftwareUpdateCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruVehicleSoftwareUpdateCAFrench-IOS";

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
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLogin("subarunextgen3@gmail.com","Test$12345");
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void vehicleSoftwareUpdateTest(){
        sc.startStepsGroup("Test - Vehicle Software Update");
        vehicleSoftwareUpdate();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleSoftwareUpdate() {
        createLog("Verifying Vehicle Software Update in vehicle info screen");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE", "xpath=//*[@label='Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(5000, 30000);

        sc.swipe("down", sc.p2cy(30), 2000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Logiciel de véhicule']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='vehicleSoftware_icon']", 0);
        if(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='À jour']"))) {
            createLog("Vehicle software is up to date");
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='À jour']"), 0);
        } else if (sc.isElementFound("NATIVE", "xpath=//*[@text='Customer Action Complete']")) {
            createLog("Customer Action Complete");
            verifyElementFound("NATIVE", "xpath=//*[@text='Customer Action Complete']", 0);
        } else {
            createLog("Vehicle software update is available");
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Mise à jour du logiciel disponible']"), 0);
        }
        click("NATIVE", convertTextToUTF8("//*[@text='Logiciel de véhicule']"), 0, 1);
        sc.syncElements(5000, 30000);
        //Validate Vehicle Software update screen
        if(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Votre logiciel est à jour']"))) {
            createLog("Verifying Vehicle software is up to Date");
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Logiciel de véhicule']"), 0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Il n’y a pas de mise à jour logicielle pour l’instant.']"), 0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Retour au tableau de bord']"), 0);
            click("NATIVE", convertTextToUTF8("//*[@text='Retour au tableau de bord']"), 0, 1);
            createLog("Verified Vehicle software is up to Date");
        } else if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Échec de la mise à jour du logiciel')]"))) {
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Échec de la mise à jour du logiciel')]"), 0);
            createErrorLog("Software Update Failed");
        } else {
            createLog("Vehicle software is not up to Date");
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Une nouvelle version du logiciel est disponible']"), 0);
            sc.swipe("down", sc.p2cy(20), 5000);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='OTA_UPDATE_CONTINUE_BUTTON' and @enabled='true']", 0);

            //click on continue
            click("NATIVE", "xpath=//*[@accessibilityLabel='OTA_UPDATE_CONTINUE_BUTTON' and @enabled='true']", 0, 1);
            sc.syncElements(5000, 30000);
            createLog("Verifying Vehicle software Agree and Install Screen");
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='DCM et assistance au conducteur' or @text='DCM & AIDE AU CONDUCTEUR']"), 0);
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@text='Accepter et installer' or @text='ACCEPTER ET INSTALLER'])[1]"), 0);
            click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
            createLog("Verified Vehicle software Agree and Install Screen");

            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='DCM et assistance au conducteur' or @text='DCM & AIDE AU CONDUCTEUR']"), 0);
            click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
            createLog("Vehicle software is not up to Date");
        }
        sc.syncElements(2000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        createLog("Verified Vehicle Software Update in vehicle info screen");
    }
}
