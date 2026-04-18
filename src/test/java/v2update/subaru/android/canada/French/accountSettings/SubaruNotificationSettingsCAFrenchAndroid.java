package v2update.subaru.android.canada.French.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruNotificationSettingsCAFrenchAndroid extends SeeTestKeywords {
    String testName = "Account Settings - Notification Settings - Android";
    public static String notificationsList[] = {"Reporte de Condición del Vehículo", "Reporte de Condición del Vehículo", "Recordatorio del asiento trasero", "Actualizaciones de cuenta", "PUERTAS DESBLOQUEADAS", "PUERTAS ABIERTAS", "CAJUELA ABIERTA", "LUCES DE EMERGENCIA ENCENDIDAS", "COFRE ABIERTO", "TECHO CORREDIZO ABIERTO", "Ventanas Abiertas", convertTextToUTF8("Estado del vehículo eléctrico'"), "Predictive Alerts"};
    public static String[] vehicleAlertList = {""};

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
                android_emailLoginFrench("subarustageca@mail.tmnact.io", "Test$123");
                sc.stopStepsGroup();
        }
    }
    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void testa_NotificationsSettings() {
        sc.startStepsGroup("Notification Settings");
        notificationSettingsValidations();
        sc.stopStepsGroup();

    }
    //Test Case Description: Turn off or Disable all the notifications and verify it is disabled
    @Test
    @Order(2)
    public void testb_DisableNotifications() {
        sc.startStepsGroup("Disable Notifications");
        switchOFFNotificationsAlerts(returnAlerts());
        sc.stopStepsGroup();
    }

    //Test Case Description: Turn on or Enable all the notifications and verify it is enabled
    @Test
    @Order(3)
    public void testc_EnableNotifications() {
        sc.startStepsGroup("Enable Notifications");
        switchONNotificationsAlerts(returnAlerts());
        sc.stopStepsGroup();
    }


    @Test
    @Order(4)
    public void signOut() {
        sc.startStepsGroup(" Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void vehicleAlertsValidations(){
        createLog("Started:Vehicle Alerts Validations");
        validateVehicleAlerts();
        //Navigate back to vehicle Overview page
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.waitForElement("NATIVE", "xpath=//*[@id='iv_profile_pic']", 0, 10);
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.waitForElement("NATIVE", "xpath=//*[@text='Cuenta']", 0, 10);
        sc.flickElement("NATIVE", "xpath=//*[@text='Cuenta']", 0, "Down");
        createLog("Ended:Vehicle Alerts Validations");
    }

    public static void verifyPushNotificationDisplayed(String notificationName) {
        createLog("Started:Push Notifications ");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='" + notificationName + "']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='" + notificationName + "']//following-sibling::*[@id='np_subtitle']"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", convertTextToUTF8("//*[@text='" + notificationName + "']//following-sibling::*[@text='Notification push']"), 0, 1000, 1, false);
        verifyElementFound("NATIVE",  convertTextToUTF8("//*[@text='" + notificationName + "']//following-sibling::*[@text='Notification push']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='" + notificationName + "']//following-sibling::*[@id='np_switch_pn']"), 0);
        createLog("Ended:Push Notifications ");
    }

    public static void verifyEmailNotificationDisplayed(String notificationName) {
        createLog("Started:Email Notifications ");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='" + notificationName + "']"), 0);
        if (!(notificationName.contains("Health Report")))
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='" + notificationName + "']//following-sibling::*[@id='np_subtitle']"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", convertTextToUTF8("//*[@text='" + notificationName + "']//following-sibling::*[@id='np_switch_email']"), 0, 1000, 1, false);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='" + notificationName + "']//following-sibling::*[contains(@text,'Courriel')"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='" + notificationName + "']//following-sibling::*[@id='np_switch_email']"), 0);
        createLog("Ended:Email Notifications ");
    }

    public static String getSwitchValue(String alert) {
        String switchValue = null;
        if (alert.contains(convertTextToUTF8("Correo Electrónico"))) {
            switchValue = sc.elementGetProperty("NATIVE", convertTextToUTF8("//*[@text='" + alert +"']//following-sibling::*[@id='alert_item_switch']"), 0, "checked");
        } else {
            swipeToElement(alert);
            switchValue = sc.elementGetProperty("NATIVE", convertTextToUTF8("//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch' or @id='np_switch_email']"), 0, "checked");
        }
        return switchValue;
    }

    public static String getVehicleAlertSwitchValue(String alert) {
        String switchValue = null;
        swipeToElement(alert);
        switchValue = sc.elementGetProperty("NATIVE", convertTextToUTF8("//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']"), 0, "enabled");

        return switchValue;
    }

    public static String enableOrDisableSwitchAndGetSwitchValue(String alert) {
        String switchValue = null;
        if (alert.contains("Email")) {
            sc.click("NATIVE", "xpath=//*[@text='" + alert.split("Email")[0].trim() + "']//following-sibling::*[@id='np_switch_email']", 0, 1);
            sc.syncElements(200, 30000);
            switchValue = sc.elementGetProperty("NATIVE", "xpath=//*[@text='" + alert.split("Email")[0].trim() + "']//following-sibling::*[@id='np_switch_email']", 0, "checked");

        } else {
            swipeToElement(alert);
            sc.click("NATIVE", convertTextToUTF8("//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch' or @id='np_switch_email']"), 0, 1);
            sc.syncElements(200, 30000);
            switchValue = sc.elementGetProperty("NATIVE", convertTextToUTF8("//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']"), 0, "checked");
        }
        return switchValue;
    }

    public static void switchOFFNotificationsAlerts(String alerts[]) {
        String switchValue = null;
        for (String alert : alerts) {
            if (getSwitchValue(alert).equalsIgnoreCase("true")) {
                switchValue = enableOrDisableSwitchAndGetSwitchValue(alert);
                sc.report(alert + " Switcher is OFF", switchValue.equalsIgnoreCase("false"));
            } else {
                sc.report(alert + " is already OFF", true);
            }

        }
    }

    public static void switchONNotificationsAlerts(String alerts[]) {

        createLog("Started:Switch On Notifications Alerts");
        String switchValue = null;
        for (String alert : alerts) {
            if (getSwitchValue(alert).equalsIgnoreCase("false")) {
                switchValue = enableOrDisableSwitchAndGetSwitchValue(alert);
                sc.report(alert + " Switcher is ON", switchValue.equalsIgnoreCase("true"));
            } else {
                sc.report(alert + " is already ON", true);
            }
        }
        createLog("Ended:Switch On Notifications Alerts");

    }

    public static void swipeToElement(String element) {
        sc.swipeWhileNotFound("Down", sc.p2cy(60), 1000, "NATIVE", "xpath=//*[@text='" + element + "']", 0, 500, 3, false);
        if (!(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='" + element + "']")))) {
            sc.swipeWhileNotFound("Up", sc.p2cy(60), 1000, "NATIVE", "xpath=//*[@text='" + element + "']", 0, 500, 3, false);
            if (!(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='" + element + "']"))))
                sc.report("swipe to " + element + " failed", true);
        }

    }

    public static void validateVehicleAlerts() {
        String alert = "Vehicle Alerts", switchValue = null;
        for (int i = 1; i <= 2; i++) {
            swipeToElement(alert);
            sc.click("NATIVE", "xpath=//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']", 0, 1);
            sc.syncElements(200, 30000);
            switchValue = sc.elementGetProperty("NATIVE", "xpath=//*[@text='" + alert + "']//following-sibling::*[@id='np_switch_pn' or @id='alert_item_switch']", 0, "checked");
            for (String vehicleAlert : vehicleAlertList) {
                if (getVehicleAlertSwitchValue(vehicleAlert).equalsIgnoreCase(switchValue)) {
                    sc.report("Vehicle alert " + vehicleAlert + " is " + switchValue, true);
                } else {
                    sc.report("Vehicle alert " + vehicleAlert + " is " + switchValue, false);
                }
            }

        }
    }
    public static void notificationSettingsValidations(){
        createLog("Started:Notification Settings Validations");
        if (!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Réglages des notifications' or @text='RÉGLAGES DES NOTIFICATIONS' ]"))){
            reLaunchApp_android();
            verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
            click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
            sc.syncElements(2000, 4000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
            click("NATIVE", "xpath=//*[@text='Compte']", 0, 1);
            sc.syncElements(2000, 4000);
        }


        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Réglages des notifications' or @text='RÉGLAGES DES NOTIFICATIONS' ]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='arrow_notifications']", 0);

        click("NATIVE", "xpath=//*[@id='arrow_notifications']", 0, 1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Réglages des notifications' or @text='RÉGLAGES DES NOTIFICATIONS' ]"), 0);
        verifyPushNotificationDisplayed("Bilan de santé de votre véhicule");

        sc.swipeWhileNotFound("Up", sc.p2cy(50), 3000, "NATIVE",convertTextToUTF8("//*[@text='Rappel de passager arrière']") , 0, 1000, 1, false);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Rappel de passager arrière']"), 0);
        verifyPushNotificationDisplayed("Rappel de passager arrière");


        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Mises à jour de compte']"), 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE","xpath=//*[@text='Alertes du véhicule']" , 0, 1000, 1, false);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Alertes du véhicule']"), 0);
        verifyPushNotificationDisplayed("Alertes du véhicule");
        createLog("Ended:Notification Settings Validations");

    }
    public static String[] returnAlerts() {
        if (strAppType.toLowerCase().equals("lexus")){
            return vehicleAlertList = new String[]{"PORTIÈRE(S) DÉVERROUILLÉE(S)", "PORTIÈRE(S) OUVERTE(S)", "HAYON/COFFRE OUVERT", "FEUX DE DÉTRESSE ALLUMÉS", "CAPOT OUVERT", "TOIT OUVRANT OUVERT", "GLACE(S) OUVERTE(S)", "ALERTES VÉHICULE ÉLECTRIQUE", "PREDICTIVE ALERTS"};

        }else{
            return vehicleAlertList = new String[]{"Portière(s) déverrouillée(s)", "Portière(s) ouverte(s)", "Hayon/coffre ouvert", "Feux de détresse allumés", "Capot ouvert", "Toit ouvrant ouvert", "Glace(s) ouverte(s)", "Alertes Véhicule électrique", "Predictive Alerts"};
        }


    }
}