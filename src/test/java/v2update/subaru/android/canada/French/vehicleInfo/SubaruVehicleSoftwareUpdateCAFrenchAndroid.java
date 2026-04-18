package v2update.subaru.android.canada.French.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruVehicleSoftwareUpdateCAFrenchAndroid extends SeeTestKeywords {
    String testName = "Subaru Subscriptions Android";

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
    public void exit() {
        exitAll(this.testName);
    }


    @Test
    @Order(1)
    public void validateVehicleSoftwareUpdateTest() {
        sc.startStepsGroup("Validate Vehicle Software Update");
        vehicleSoftwareUpdate();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleSoftwareUpdate() {
        createLog("Start:Validate Vehicle Software Update");
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Info']//following-sibling::*[@class='android.widget.Button']")) {
            if(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@class='android.view.View' and (./preceding-sibling::* | ./following-sibling::*)[@text='Télécharger une Clé numérique'] and ./parent::*[@class='android.view.View']]"), 0)) {
                click("NATIVE", convertTextToUTF8("//*[@class='android.view.View' and (./preceding-sibling::* | ./following-sibling::*)[@text='Télécharger une Clé numérique'] and ./parent::*[@class='android.view.View']]"), 0, 1);
            }
        }
        click("NATIVE", "xpath=//*[@text='Info']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 30000);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", convertTextToUTF8("//*[@text='Logiciel de véhicule']"), 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Logiciel de véhicule']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='À jour']", 0);
        click("NATIVE", "xpath=//*[@text='Logiciel de véhicule']", 0,1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Logiciel de véhicule']", 0);

        if(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Votre logiciel est à jour']"))) {
            createLog("Vehicle software is up to date");
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Il n’y a pas de mise à jour logicielle pour l’instant.']"), 0);
        } else {
            createLog("Vehicle software update is available");
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Mise à jour du logiciel disponible')]", 0);
        }

        click("NATIVE", convertTextToUTF8("//*[@text='Retour au tableau de bord']"),0,1);
        sc.syncElements(5000,10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        createLog("End:Validate Vehicle Software Update");
    }
}
