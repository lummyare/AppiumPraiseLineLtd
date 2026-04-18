package v2update.subaru.android.canada.French.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDriveConnectCAFrenchAndroid extends SeeTestKeywords {
    String testName = "SubaruHelpAndFeedbackCAFrench - Android";

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
    public void exit(){
        exitAll(this.testName);
    }
    @Test
    @Order(1)
    public void driveConnectSettingsTest() {
        sc.startStepsGroup("Test - Accounts -> Drive Connect Settings Screen");
        driveConnectSettingsScreen();
        sc.stopStepsGroup();
    }

    public static void driveConnectSettingsScreen() {
        createLog("Started : Verifying Drive Connect Settings screen in Accounts");
        navigateToDriveConnectSettingsScreen();

        //verify title, info, subtext
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Les services d’assistant intelligent et de navigation infonuagique peuvent apprendre vos routines et vos habitudes pour envoyer des alertes à votre véhicule.']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='(Nécessite un véhicule compatible avec un abonnement à Drive Connect actif.)']"),0);

        //verify all push notifications are enabled
        if(sc.isElementFound("NATIVE", "xpath=//*[@id='va_allow_notifications_switch' and @checked='true']/preceding-sibling::*[@text='Permet les notifications*']"))
        {
            verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Permet les notifications*']"),0);
            verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Ces préférences s’appliqueront à tous les véhicules dans lesquels Drive Connect est activé']"),0);

            verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Navigation Infonuagique']"),0);
            verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Permettez à votre véhicule de suggérer des destinations et d’afficher des alertes avant le départ']"),0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='va_connected_navigation_switch']/preceding-sibling::*[@text='Navigation Infonuagique']"),0);

            verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Entretien et carburant']"),0);
            verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Permettez à votre véhicule de suggérer les stations-service à proximité et d’envoyer des alertes d’entretien prédictives']"),0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='va_maintenance_fuel_switch']/preceding-sibling::*[@text='Entretien et carburant']"),0);

            sc.swipe("Down", sc.p2cy(50), 3000);
            verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Météo']"),0);
            verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Permettez à votre véhicule d’envoyer des alertes de mauvais temps']"),0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='va_weather_switch']/preceding-sibling::*[@text='Météo']"),0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(text(),'* Vous pouvez également modifier ce paramètre à l’aide du bouton')]"),0);

            click("NATIVE", "xpath=//*[@id='va_allow_notifications_switch' and @checked='true']",0,1);
            if(!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@id='va_maintenance_fuel_switch']/preceding-sibling::*[@text='Entretien et carburant']"),0))
            {
                verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='va_allow_notifications_switch' and @checked='false']/preceding-sibling::*[@text='Permet les notifications*']"),0);
                createLog("All other notifications disabled and not visible");
            }
        } else if(!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@id='va_maintenance_fuel_switch']/preceding-sibling::*[@text='Entretien et carburant']"),0))
        {
            createLog("All other notifications disabled and not visible");
        }
        //revert back settings
        click("NATIVE", "xpath=//*[@id='va_allow_notifications_switch' and @checked='true']",0,1);
        createLog("Completed : Verifying Drive Connect Settings screen in Accounts");
    }

    public static void navigateToDriveConnectSettingsScreen() {
        createLog("Started : Navigation to Linked Accounts Screen");
        verifyElementFound("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(5000, 30000);
        click("NATIVE", "xpath=//*[@text='Compte']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Réglages de Drive Connect']"),0);
        verifyElementFound("NATIVE","xpath=//*[@id='iv_va_settings']",0);
        click("NATIVE",convertTextToUTF8("//*[@text='Réglages de Drive Connect']"),0,1);
        sc.syncElements(5000, 60000);
        //Linked Accounts screen
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Réglages de Drive Connect']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Revenir en arrière']"),0);
        createLog("Completed : Navigation to Drive Connect Settings Screen");
    }
}
