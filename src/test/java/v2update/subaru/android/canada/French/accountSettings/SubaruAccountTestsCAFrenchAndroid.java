package v2update.subaru.android.canada.French.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import static v2update.android.usa.accountSettings.DarkLightModeAndroid.darkColorValidation;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruAccountTestsCAFrenchAndroid extends SeeTestKeywords {
    String testName = "SubaruAccountTest-AndroidCAFrench";
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CST"));

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")){
            case(""):
                testName = System.getProperty("cloudApp") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("Canada");
                selectionOfLanguage_Android("French");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                 android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("Canada");
                selectionOfLanguage_Android("French");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$1234");
                sc.stopStepsGroup();
        }
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }


    @Test
    @Order(1)
    public void profilePictureTest() {
        sc.startStepsGroup("Test-Account Settings page");
        validateAccountScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    public void notificationsScreen21mm(){
        sc.startStepsGroup("Test-Notifications screen");
        validateNotificationsScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    public void takeATourScreen21mm(){
        sc.startStepsGroup("Test-Take a tour screen");
        validateTakeTourScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void darkModeValidationTest() throws IOException {
        sc.startStepsGroup("DarkMode Validation started");
        darkModeValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void signOut() {
        sc.startStepsGroup(" Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }


    public static void validateAccountScreen() {
        createLog("Started:validate Account Screen");
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Se déconnecter']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Profile Picture']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Gérer votre profil et vos paramètres']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boîte de réception']"), 0);
        //Notifications
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Notifications']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les notifications pour votre véhicule et votre compte']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Notifications']", 0);
        //Take a Tour
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Démonstration']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Explorez l’appli pour découvrir les nouveautés']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Démonstration']", 0);
        //Dark Mode
        verifyElementFound("NATIVE", "xpath=//*[@text='Mode sombre']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Mode sombre']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_dark_mode_switch_off']", 0);
        createLog("Ended:validate Account Screen");
    }

    public static void validateNotificationsScreen() {
        createLog("Started:validate Notifications Screen");
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@text='Notifications']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@class='android.widget.ImageButton']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Notifications'] | //*[@text='NOTIFICATIONS']", 0);
        //Validate Notifications displayed
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(5000, 10000);
        createLog("Ended:validate Notifications Screen");
    }
    public static void validateTakeTourScreen(){
        createLog("Started:validate Take A Tour Screen");
        if (!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Se déconnecter']"))){
            reLaunchApp_android();
            click("NATIVE","xpath=//*[@id='top_nav_profile_icon']",0,1);
            sc.syncElements(5000, 10000);
        }
        click("NATIVE", convertTextToUTF8("//*[@text='Démonstration']"), 0, 1);
        sc.syncElements(5000, 30000);
        createLog("Started : See what’s new screen");

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les nouveautés']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Découvrez notre nouveau look, qui se traduira par une expérience encore améliorée. Les caractéristiques peuvent varier en fonction du véhicule ou de l’emplacement.']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Tour Close Icon']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Lancer l’aperçu']"), 0);
        createLog("Completed : See what’s new screen");
        click("NATIVE", convertTextToUTF8("//*[@text='Lancer l’aperçu']"), 0, 1);
        sc.syncElements(2000, 10000);

        //Start the Tour Screens
        createLog("Started : Start the Tour Screens");
        //vehicles screen
        verifyElementFound("NATIVE", "xpath=//*[@text='1 sur 5']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Une nouvelle façon d’ajouter un véhicule ou de changer de véhicule']"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Touchez le nom du véhicule pour changer de véhicule.']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Tour Close Icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Suivant']", 0);
        click("NATIVE", "xpath=//*[@text='Suivant']", 0, 1);
        sc.syncElements(2000, 10000);

        //Remote connect screen
        verifyElementFound("NATIVE", "xpath=//*[@text='2 sur 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Démarrez, arrêtez, verrouillez et déverrouillez votre véhicule à distance. Et permettez à un conducteur invité d’avoir accès à votre véhicule.']"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Tour Close Icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Suivant']", 0);
        click("NATIVE", "xpath=//*[@text='Suivant']", 0, 1);
        sc.syncElements(2000, 10000);

        //Vehicle Status screen
        verifyElementFound("NATIVE", "xpath=//*[@text='3 sur 5']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='État du véhicule']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Vérifiez instantanément l’état des alertes importantes.']"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Tour Close Icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Suivant']", 0);
        click("NATIVE", "xpath=//*[@text='Suivant']", 0, 1);
        sc.syncElements(2000, 10000);

        //Health screen
        verifyElementFound("NATIVE", "xpath=//*[@text='4 sur 5']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Santé']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Vérifiez l') and contains(@text,'entretien et les alertes de votre véhicule et plus encore.')]"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Tour Close Icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Suivant']", 0);
        click("NATIVE", "xpath=//*[@text='Suivant']", 0, 1);
        sc.syncElements(5000, 10000);

        //Vehicle Info screen
        verifyElementFound("NATIVE", "xpath=//*[@text='5 sur 5']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Informations sur le véhicule']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Consultez les manuels et les garanties de votre véhicule, les abonnements, etc.')]"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Tour Close Icon']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Terminé']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Terminé']"), 0, 1);
        sc.syncElements(2000, 10000);
        createLog("Completed : Start the Tour Screens");

        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Revisiter le tutoriel à partir de votre compte, en tout temps.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Profitez de la nouvelle appli!']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Tour Close Icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Ok']", 0);
        click("NATIVE", "xpath=//*[@text='OK' or @text='Ok']", 0, 1);
        sc.syncElements(4000, 16000);

        //verify dashboard
        verifyElementFound("NATIVE","xpath=//*[@id='dashboard_display_image']",0);
        createLog("Completed : Take A Tour");
    }
    public static void waitForElement(String strZone, String element, int intIndex,int timeout) {
        try {
            sc.waitForElement(strZone,element,intIndex,timeout);
            createLog("Element :" + element + " Found");
            sc.report("Element :" + element + " Found", true);
        } catch (Exception IgnoreException) {
            createErrorLog("Element"+element+" not found");
            sc.report("" + IgnoreException, false);
        }
    }

    public static void darkModeValidation() throws IOException {
        //check vehicle switcher chevron color
        darkMode();
        sc.flickElement("NATIVE", "xpath=//*[@contentDescription='Drag']", 0, "Down");
        sc.syncElements(2000,5000);
        String actualColor = darkColorValidation("//*[@contentDescription='tap to open vehicle switcher']");
        Assertions.assertEquals("black",actualColor);

    }

    public static void darkMode() {
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='top_nav_profile_icon']")){
            reLaunchApp_android();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 30);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Compte']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Mode sombre']"), 0);

        if(sc.isElementFound("NATIVE","xpath=//*[@id='account_dark_mode_switch_off']")){
            click("NATIVE","xpath=//*[@id='account_dark_mode_switch_off']",0,1);
        }
        verifyElementFound("NATIVE","xpath=//*[@id='account_dark_mode_switch_on']",0);
    }
}
