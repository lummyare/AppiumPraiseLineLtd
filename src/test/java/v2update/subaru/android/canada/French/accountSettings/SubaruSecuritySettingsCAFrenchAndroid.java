package v2update.subaru.android.canada.French.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSecuritySettingsCAFrenchAndroid extends SeeTestKeywords {
    String testName = "SubaruSecuritySettingsCAFrench-Android";

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
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
                sc.stopStepsGroup();
        }
    }

    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void securitySettingsTest() {
        sc.startStepsGroup("Security Settings");
        securitySettings();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void setPINTest() {
        sc.startStepsGroup("Set PIN");
        setPIN("subarustageca@mail.tmnact.io", "Test$123");
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void manageSavedProfileTest() {
        sc.startStepsGroup("Manage Saved Profile");
        manageSavedProfile();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void manageYourDataTest() {
        sc.startStepsGroup("Manage Your Data");
        manageYourData();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    //methods
    public static void resetPasswordMFAScreenLockedAccountIOS(String strEmail) {
        createLog("Start: Reset Password MFA Screen Locked Account");
        verifyElementFound("NATIVE", "xpath=//*[@id='LOGIN_BUTTON_SIGNIN']", 0);
        click("NATIVE", "xpath=//*[@id='LOGIN_BUTTON_SIGNIN']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0);
        sendText("NATIVE", "xpath=//*[@id='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0, strEmail);
        sc.syncElements(2000, 30000);
        String emailLogin = sc.elementGetText("NATIVE", "xpath=//*[@id='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0);
        click("NATIVE", "xpath=//*[@id='FR_NATIVE_SIGNIN_CONTINUE_BUTTON']", 0, 1);
        sc.syncElements(5000, 30000);
        String emailMFA = sc.elementGetText("NATIVE", "xpath=//*[@id='FR_NATIVE_OTP_ACCOUNT_LABEL']", 0);
        if (emailLogin.equalsIgnoreCase(emailMFA))
            createLog("Valid email address displayed in MFA screen");
        else{
            createLog("email address not matching in MFA screen");
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='ACCESS YOUR ACCOUNT' or @text='Access your account']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='FR_NATIVE_OTP_INPUT_TEXTFIELD']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='WE HAVE SENT YOU A 6-DIGIT TEMPORARY ACTIVATION CODE TO YOUR EMAIL LISTED BELOW' or @text='We have sent you a 6-digit temporary Activation Code to your Email listed below']", 0);

        click("NATIVE", "xpath=//*[@id='btn back']", 0, 1);
        createLog("Completed: Reset Password MFA Screen Locked Account");
    }

    public static void securitySettings() {
        createLog("Started: Security Settings");
        sc.waitForElement("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 30);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Se déconnecter']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Paramètres de sécurité' or @text='PARAMÈTRES DE SÉCURITÉ']"), 0);
        click("NATIVE", "xpath=//*[@id='arrow_padlock']", 0, 1);
        sc.syncElements(2000, 10000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        System.out.println(actualText);

        switch (strAppType.toLowerCase()) {
            case ("lexus"):
                String expectedText[] = {"PARAMÈTRES DE SÉCURITÉ",
                        "Authentification requise", "15 Minutes", "Me garder connecté", "L'activation de la fonction d'authentification ajoutera un degré de protection supplémentaire à votre compte de l'app Lexus.",
                        "NIP du compte", "Un code à 6 chiffres est utilisé pour accéder à votre profil sur les véhicules compatibles avec Drive Connect.", "Réinitialiser le NIP", "Vos profils enregistrés", "Voir les véhicules où votre profil a été enregistré.",  "Vos renseignements personnels", "Contrôlez vos données ou soumettez une demande de suppression de vos détails personnels",
                        "Supprimer le compte"};
                for (String detailsName : expectedText) {
                    detailsName = convertTextToUTF8(detailsName);
                    if (actualText.contains(detailsName)) {
                        sc.report("Validation of " + detailsName, true);
                    } else {
                        sc.report("Validation of " + detailsName, false);
                    }
                }
                break;
            case ("toyota"):
                String expectedText1[] = {"Paramètres de sécurité",
                        "Authentification requise", "15 Minutes", "Me garder connecté", "L'activation de la fonction d'authentification ajoutera un degré de protection supplémentaire à votre compte de l'app Lexus.",
                        "NIP du compte", "Un code à 6 chiffres est utilisé pour accéder à votre profil sur les véhicules compatibles avec Drive Connect.", "Réinitialiser le NIP", "Vos profils enregistrés", "Voir les véhicules où votre profil a été enregistré.",  "Vos renseignements personnels", "Contrôlez vos données ou soumettez une demande de suppression de vos détails personnels",
                        "Supprimer le compte"};
                for (String detailsName : expectedText1) {
                    detailsName = convertTextToUTF8(detailsName);
                    if (actualText.contains(detailsName)) {
                        sc.report("Validation of " + detailsName, true);
                    } else {
                        sc.report("Validation of " + detailsName, false);
                    }
                }
                break;
        }
        createLog("Completed: Security Settings");
    }

    public static void setPIN(String strUsername, String strPassword) {
        createLog("Started Set PIN");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='PARAMÈTRES DE SÉCURITÉ' or @text='Paramètres de sécurité']"))) {
            navigateToSecuritySettingsScreen();
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='PARAMÈTRES DE SÉCURITÉ' or @text='Paramètres de sécurité']"), 0);

        click("NATIVE", "xpath=//*[@text='Réinitialiser le NIP']", 0, 1);
        sc.syncElements(2000, 20000);
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Confirmer']"))) {
            sc.syncElements(2000, 20000);
            click("NATIVE", "xpath=//*[@text='Confirmer']", 0, 1);
            if(sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Based on your last time login, please login again to reset your pin']")))
            {
                click("NATIVE", "xpath=//*[@id='button1']", 0,1);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='etName']")) {
                sendText("NATIVE", "xpath=//*[@id='etName']", 0, "subarustageca@mail.tmnact.io");
                click("NATIVE", convertTextToUTF8("//*[@text='Continuer']"), 0, 1);
                sendText("NATIVE", convertTextToUTF8("//*[@text='Mot de passe']"), 0, "Test$123");
                click("NATIVE", convertTextToUTF8("//*[@text='Connexion']"), 0, 1);
                delay(5000);
            }
        }
        if(sc.isElementFound("NATIVE", "xpath=//*[@id='et_pin']", 0))
        {
            sendText("NATIVE", "xpath=//*[@id='et_pin']", 0, "123456");
        }
        if(sc.isElementFound("NATIVE", "xpath=//*[@id='button1']", 0)) {
            click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Réinitialiser le NIP']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Confirmer le NIP']"), 0);
        sendText("NATIVE", "xpath=//*[@id='et_pin']", 0, "123456");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Paramètres de sécurité']"), 0);

        createLog("Completed Set PIN");
    }

    public static void manageSavedProfile() {
        createLog("Started Manage Saved Profile");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Paramètres de sécurité']"))) {
            reLaunchApp_android();
            navigateToSecuritySettingsScreen();
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Paramètres de sécurité']"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", convertTextToUTF8("//*[@text='Vos profils enregistrés']"), 0, 1000, 1, true);
        click("NATIVE", convertTextToUTF8("//*[@text='Gérer le profil enregistré']"), 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Revenir en arrière']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Vos profils enregistrés']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Vous pouvez gérer ici votre profil, qui est enregistré sur tout véhicule compatible dont vous n’êtes pas le conducteur principal.']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='no_fav_pref_iv']", 0);
        click("NATIVE", convertTextToUTF8("//*[@contentDescription='Revenir en arrière']"), 0, 1);
        sc.syncElements(4000,8000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Paramètres de sécurité']"), 0);
        createLog("Completed Manage Saved Profile");
    }

    public static void manageYourData() {
        createLog("Started Manage Your Data");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Paramètres de sécurité']"))) {
            reLaunchApp_android();
            navigateToSecuritySettingsScreen();
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Paramètres de sécurité']"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='Supprimer le compte']", 0, 1000, 1, false);
        click("NATIVE", convertTextToUTF8("//*[@text='Supprimer le compte']"), 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer le compte']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'La suppression de votre compte et de vos données personnelles est permanente.')]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Annuler' or @text='ANNULER']", 0);
        click("NATIVE", convertTextToUTF8("//*[@contentDescription='Revenir en arrière']"), 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Paramètres de sécurité']"), 0);
        //click back in Account settings screen
        click("NATIVE", convertTextToUTF8("//*[@contentDescription='Revenir en arrière']"), 0, 1);
        //click("NATIVE", convertTextToUTF8("//*[@contentDescription='Revenir en arrière']"), 0, 1);
        sc.syncElements(10000,30000);
        //Drag to close in accounts
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton' and ./parent::*[@id='toolbar']]", 0,1);
        sc.flickElement("NATIVE", "xpath=//*[@contentDescription='Drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@text='Info']",0);
        createLog("Completed Manage Your Data");
    }

    public static void navigateToSecuritySettingsScreen() {
        createLog("Started: navigating to security settings screen");
        reLaunchApp_iOS();
        sc.waitForElement("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 30);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@id='arrow_padlock']", 0, 1);
        sc.syncElements(2000, 10000);
        createLog("Completed: navigating to security settings screen");
    }
}
