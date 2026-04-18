package v2update.ios.canada.french.accountSettings;

import com.ctp.CTMailClient;
import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecuritySettingsCAFrenchIOS extends SeeTestKeywords {
    String testName = "SecuritySettingsCAFrench-IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")){
            case(""):
                testName = System.getProperty("cloudApp") + testName;
                iOS_Setup2_5(testName);
                sc.startStepsGroup("email SignIn 21MM");
                selectionOfCountry_IOS("canada");
                selectionOfLanguage_IOS("french");
                ios_keepMeSignedIn(true);
                //need to replace to prod account
                ios_emailLoginFrench(ConfigSingleton.configMap.get("strEmailCAFrench"), ConfigSingleton.configMap.get("strPasswordCAFrench"));
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                iOS_Setup2_5(testName);
                sc.startStepsGroup("email SignIn 21MM");
                selectionOfCountry_IOS("canada");
                selectionOfLanguage_IOS("french");
                ios_keepMeSignedIn(true);
                //need to replace to prod account
                ios_emailLoginFrench(ConfigSingleton.configMap.get("strEmailCAFrench"), ConfigSingleton.configMap.get("strPasswordCAFrench"));
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
        setPIN(ConfigSingleton.configMap.get("strEmailCAFrench"), ConfigSingleton.configMap.get("strPasswordCAFrench"));
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
        ios_emailSignOut();
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
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Se déconnecter']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Paramètres de sécurité']"), 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_SECURITY_SETTINGS_CELL']", 0, 1);
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

        //if(sc.isElementFound("NATIVE","xpath=//*[@id='Set PIN']")) {
        click("NATIVE", "xpath=//*[@id='Set PIN' or @id='Réinitialiser le NIP']", 0, 1);
        sc.syncElements(2000, 20000);
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@id='Vérifier' and @class='UIAStaticText']"))) {
            sc.syncElements(2000, 20000);
            click("NATIVE", "xpath=//*[@id='Confirmer']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']")) {
                sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0, strUsername);
                click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_CONTINUE_BUTTON']", 0, 1);
                sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD']", 0, strPassword);
                click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']", 0, 1);
                delay(5000);
                //enter activation code if OTP screen displayed
                createLog("enter activation code if OTP screen displayed");
                if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_TITLELABEL']")) {
                    String activationCode = new CTMailClient().getActivationCode(strUsername);
                    sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_INPUT_TEXTFIELD']", 0, activationCode);
                    click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON']", 0, 1);
                    sc.syncElements(2000, 5000);
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            delay(2000);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='CREATE_PIN_LABEL_TITLE']", 0);
            sendText("NATIVE", "xpath=//*[@class='UIAView' and (./preceding-sibling::* | ./following-sibling::*)[@class='UIAView' and ./*[@class='UIAView']] and ./*[@class='UIAView' and ./*[./*[@class='UIAView']]] and ./parent::*[@class='UIAView']]", 0, "123456");
            delay(1000);
            if(sc.isElementFound("NATIVE","xpath=//*[@class='UIAButton' and contains(text(),'accord')]")){
                click("NATIVE", "xpath=//*[@class='UIAButton' and contains(text(),'accord')]", 0, 1);
                delay(1000);
            }
        }

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='PARAMÈTRES DE SÉCURITÉ' or @text='Paramètres de sécurité']"), 0);
        createLog("Completed Set PIN");
    }

    public static void manageSavedProfile() {
        createLog("Started Manage Saved Profile");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='PARAMÈTRES DE SÉCURITÉ' or @text='Paramètres de sécurité']"))) {
            navigateToSecuritySettingsScreen();
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='PARAMÈTRES DE SÉCURITÉ' or @text='Paramètres de sécurité']"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", convertTextToUTF8("//*[@id='Vos profils enregistrés' and @class='UIAStaticText' and ./parent::*[@class='UIATable']]"), 0, 1000, 1, true);
        click("NATIVE", convertTextToUTF8("//*[@id='Gérer le profil enregistré']"), 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
        if(sc.isElementFound("NATIVE",convertTextToUTF8("//*[contains(@id,'Vous pouvez gérer ici votre profil')]"))) {
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@id,'Vous pouvez gérer ici votre profil')]"), 0);
        }else if(sc.getElementCount("NATIVE","xpath=//*[@accessibilityLabel='Driver Profiles']")>0){
            createLog("Driver Profile found");
        }else{
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Vehicles in which your profile has been saved.']", 0);
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(4000,8000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='PARAMÈTRES DE SÉCURITÉ' or @text='Paramètres de sécurité']"), 0);
        createLog("Completed Manage Saved Profile");
    }

    public static void manageYourData() {
        createLog("Started Manage Your Data");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='PARAMÈTRES DE SÉCURITÉ' or @text='Paramètres de sécurité']"))) {
            navigateToSecuritySettingsScreen();
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='PARAMÈTRES DE SÉCURITÉ' or @text='Paramètres de sécurité']"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='Supprimer le compte']", 0, 1000, 1, false);
        click("NATIVE", "xpath=//*[@id='Supprimer le compte']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer le compte']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'La suppression de votre compte et de vos données personnelles est permanente.')]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Annuler' or @text='ANNULER']", 0);
        click("NATIVE", "xpath=//*[@class='UIAImage']", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='PARAMÈTRES DE SÉCURITÉ' or @text='Paramètres de sécurité']"), 0);
        //click back in security settings screen
        click("NATIVE", "xpath=//*[@label='Retour']", 0, 1);
        //click back in Account settings screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,30000);
        //Drag to close in accounts
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed Manage Your Data");
    }

    public static void navigateToSecuritySettingsScreen() {
        createLog("Started: navigating to security settings screen");
        reLaunchApp_iOS();
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Paramètres de sécurité']"), 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_SECURITY_SETTINGS_CELL']", 0, 1);
        sc.syncElements(2000, 10000);
        createLog("Completed: navigating to security settings screen");
    }
}
