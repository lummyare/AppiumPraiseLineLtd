package v2update.subaru.android.canada.French.health;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruHealthCAFrenchAndroid extends SeeTestKeywords {
    String testName = "SubaruHealthCAFrenchAndroid";

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
    public void vehicleAlertsTest() {
        sc.startStepsGroup("Test - Vehicle Alerts validations");
        vehicleAlerts();
        sc.stopStepsGroup();
    }


    @Test
    @Order(2)
    public void vehicleHealthReportTest() {
        sc.startStepsGroup("Test - Vehicle Health Report validations");
        vehicleHealthReport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }


    public static void vehicleAlerts() {
        createLog("Started - Vehicle Alerts");
        if(!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Santé']"), 0))
        {
            reLaunchApp_android();
        }
        click("NATIVE", convertTextToUTF8("//*[@text='Santé']"),0,1);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Alertes de véhicule']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Aucune Alertes de véhicule']"), 0);
        if(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Aucune Alertes de véhicule']"), 0)){
            createLog("No Vehicle Alerts for vehicle");
            verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_health_vehicle_alert_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='vehicle_health_vehicle_alert_sub_icon_success' and @contentDescription='vehicle_alerts_tile']", 0);
        } else {
            createLog("ALERT - Vehicle Alerts for vehicle");
            //Validate Warning Alert icon
            createLog("vehicle contains vehicle alerts");
            verifyElementFound("NATIVE","xpath=//*[@id='vehicle_health_vehicle_alert_sub_icon_fail']",0);
        }
        createLog("Completed - Vehicle Alerts");
    }

    public static void vehicleHealthReport() {
        createLog("Started - Vehicle Health Report");
        if(!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Santé']"), 0))
        {
            reLaunchApp_android();
        }
        click("NATIVE", convertTextToUTF8("//*[@text='Santé']"),0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Bilan de santé du véhicule']"), 0);

        boolean recallAlert=false;

        if(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Bilan de santé du véhicule']"), 0)){
            createLog("ALERT - Vehicle Health Report alerts displayed");
            recallAlert=true;
        } else {
            createLog("Vehicle Health Report is Good");
            verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_health_vehicle_report_icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_health_vehicle_alert_sub_icon_success']", 1);
        }

        //VHR screen
        createLog("Started - VHR Screen Validations");
        click("NATIVE", convertTextToUTF8("//*[@text='Bilan de santé du véhicule']"), 0, 1);
        click("NATIVE", convertTextToUTF8("//*[@text='Alertes actuelles du véhicule']"), 0, 1);
        click("NATIVE", "xpath=//*[@id='no_va_img']", 0, 1);
        click("NATIVE", convertTextToUTF8("//*[contains(text(),'aucune alerte de véhicule pour votre Subaru.')]"), 0, 1);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Rappels et des campagnes de service']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Visitez subaru.com et entrez votre NIV pour l’information la plus actuelle au sujet des rappels et des campagnes de service.']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(text(),'État')]"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(text(),'Les intervalles d’entretien recommandés pour votre Subaru sont indiqués en mois et en kilomètres')]"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", convertTextToUTF8("//*[contains(text(),'Les informations fournies sont basées sur la dernière fois')]"), 0, 1000, 5, false);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Etat du véhicule']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vhrgen_md_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Miles parcourus']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vs_miles_driven']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='vs_smart_key_img']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Batterie de la clé intelligente']"), 0);

        createLog("Completed - Vehicle Health Report Validations");
        //
    }
}
