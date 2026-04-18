package v2update.subaru.ios.canada.french.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSubscriptionCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruSubscriptionCAFrench-IOS";

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
    public void trialSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Trial Subscriptions");
        validateSubscriptionCard();
        validateSubscriptionScreen();
        validateSafetyConnect();
        validateDriveConnect();
        validateRemoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void paidSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Paid Subscriptions");
        validateAddServiceScreen();
        addDriveConnectPaidService();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateSubscriptionCard(){
        createLog("Validating subscription section in Vehicle Info screen");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE", "xpath=//*[@label='Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Abonnements']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Services connectés pour votre véhicule']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='subscriptions_icon']", 0);
        click("NATIVE","xpath=//*[@text='Abonnements']",0,1);
        sc.syncElements(10000,60000);
        verifyElementFound("NATIVE","xpath=//*[@id='Abonnements']",0);
        createLog("completed: Validating subscription section in Vehicle Info screen");
    }

    public static void validateSubscriptionScreen(){
        createLog("Validating subscription screen in Vehicle Info screen");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@id='Abonnements']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'Services d') and contains(@text,'essai')]"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Ajouter un service']",0);
        createLog("completed: Validating subscription screen in Vehicle Info screen");
    }

    public static void validateSafetyConnect(){
        createLog("Started - Verify Safety Connect");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@value='Safety Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@label='subscription_safety_connect']",0);
        //Trial status
        verifyElementFound("NATIVE","xpath=//*[@text='Safety Connect']/following::*[1]",0);
        String safetyConnectStatus = sc.elementGetText("NATIVE","xpath=//*[@text='Safety Connect']/following::*[1]",0);
        createLog("Safety connect status is: "+safetyConnectStatus);
        if(safetyConnectStatus.equalsIgnoreCase("Active")) {
            createLog("Safety connect status is ACTIVE");
        } else {
            createLog("Safety connect status is NOT ACTIVE");
        }
        if(sc.isElementFound("NATIVE",convertTextToUTF8("xpath=(//*[@text='Safety Connect']/following::*[@value='Jusqu’à 10 ans, dépendant d’un réseau 4G'])[1]"))) {
            verifyElementFound("NATIVE",convertTextToUTF8("xpath=(//*[@text='Safety Connect']/following::*[@value='Jusqu’à 10 ans, dépendant d’un réseau 4G'])[1]"),0);
            createLog("Safety Connect Trial is Up to 10-years");
        }
        else {
            verifyElementFound("NATIVE","xpath=(//*[@text='Safety Connect']/following::*[contains(@label,'Expire dans')])[1]",0);
            String safetyConnectExpiryDate = sc.elementGetText("NATIVE","xpath=(//*[@text='Safety Connect']/following::*[contains(@label,'Expire dans')])[1]",0);
            createLog("Safety connect expiry date is: "+safetyConnectExpiryDate);
        }
        click("NATIVE","xpath=//*[@label='subscription_safety_connect']",0,1);

        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Détails du service' and @class='UIAStaticText']"),0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Safety Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Essai d') and contains(@text,'un service')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']",0);
        if(sc.isElementFound("NATIVE",convertTextToUTF8("xpath=//*[@value='Jusqu’à 10 ans, dépendant d’un réseau 4G']"))) {
            verifyElementFound("NATIVE",convertTextToUTF8("xpath=//*[@value='Jusqu’à 10 ans, dépendant d’un réseau 4G']"),0);
            createLog("Service Details screen - Safety Connect Trial is Up to 10-years");
        }
        else {
            verifyElementFound("NATIVE","xpath=//*[contains(@label,'Expire dans')]",0);
            String safetyConnectExpiryDate = sc.elementGetText("NATIVE","xpath=//*[contains(@label,'Expire dans')]",0);
            createLog("Service Details screen - Safety connect expiry date is: "+safetyConnectExpiryDate);
        }
        verifyElementFound("NATIVE","xpath=//*[@id='Annuler']",0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        createLog("Completed - Verify Safety Connect");
    }

    public static void validateDriveConnect(){
        createLog("Started - Verify Drive Connect");
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@label='Drive Connect']",0);
        //Trial status
        verifyElementFound("NATIVE","xpath=//*[@text='Drive Connect']/following::*[1]",0);
        String driveConnectStatus = sc.elementGetText("NATIVE","xpath=//*[@text='Drive Connect']/following::*[1]",0);
        createLog("Drive connect status is: "+driveConnectStatus);
        if(driveConnectStatus.equalsIgnoreCase("Active")) {
            createLog("Drive connect status is ACTIVE");
        } else {
            createLog("Drive connect status is NOT ACTIVE");
        }
        verifyElementFound("NATIVE","xpath=(//*[contains(@text,'Drive Connect')]/following::*[contains(@value,'Expire dans')])[1]",0);
        String subExpirationDateDetails = sc.elementGetProperty("NATIVE","xpath=(//*[contains(@text,'Drive Connect')]/following::*[contains(@value,'Expire dans')])[1]",0,"value");
        createLog("Subscription expiration date details: "+subExpirationDateDetails);
        verifyElementFound("NATIVE","xpath=//*[@label='subscription_drive_connect']",0);

        click("NATIVE","xpath=//*[@label='subscription_drive_connect']",0,1);

        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Détails du service' and @class='UIAStaticText']"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Drive Connect']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Essai d') and contains(@text,'un service')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@label,'Expire dans')]",0);
        String driveConnectExpiryDate = sc.elementGetText("NATIVE","xpath=//*[contains(@label,'Expire dans')]",0);
        createLog("Service Details screen - Drive connect expiry date is: "+driveConnectExpiryDate);
        verifyElementFound("NATIVE","xpath=//*[@id='Annuler']",0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        createLog("Completed - Verify Drive Connect");
    }

    public static void validateRemoteConnect(){
        createLog("Started - Verify Remote Connect");
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[contains(@value,'Remote Connect')]",0);
        verifyElementFound("NATIVE","xpath=(//*[contains(@value,'Remote Connect')]/following::*[@label='Active'])[1]",0);
        //Trial status
        verifyElementFound("NATIVE","xpath=//*[contains(@value,'Remote Connect')]/following::*[1]",0);
        String remoteConnectStatus = sc.elementGetText("NATIVE","xpath=//*[contains(@value,'Remote Connect')]/following::*[1]",0);
        createLog("Remote connect status is: "+remoteConnectStatus);
        if(remoteConnectStatus.equalsIgnoreCase("Active")) {
            createLog("Remote connect status is ACTIVE");
        } else {
            createLog("Remote connect status is NOT ACTIVE");
        }
        verifyElementFound("NATIVE","xpath=(//*[contains(@text,'Remote Connect')]/following::*[contains(@value,'Expire dans')])[1]",0);
        String subExpirationDateDetails = sc.elementGetProperty("NATIVE","xpath=(//*[contains(@text,'Remote Connect')]/following::*[contains(@value,'Expire dans')])[1]",0,"value");
        createLog("Subscription expiration date details: "+subExpirationDateDetails);

        click("NATIVE","xpath=//*[contains(@text,'Remote Connect')]",0,1);

        sc.syncElements(20000, 60000);
        verifyElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Détails du service' and @class='UIAStaticText']"),0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Remote Connect')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_TITLE_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Essai d') and contains(@text,'un service')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='SUBSCRIPTIONS_DETAILS_SUBSCRIPTION_PLAN_LABEL']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@label,'Expire dans')]",0);
        String remoteConnectExpiryDate = sc.elementGetText("NATIVE","xpath=//*[contains(@label,'Expire dans')]",0);
        createLog("Service Details screen - Remote connect expiry date is: "+remoteConnectExpiryDate);
        verifyElementFound("NATIVE","xpath=//*[@id='Annuler']",0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        createLog("Completed - Verify Remote Connect");
    }

    public static void validateAddServiceScreen() {
        createLog("Started - Validation add service screen");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Ajouter un service']")) {
            validateSubscriptionCard();
            validateSubscriptionScreen();
        }
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Ajouter un service']",0);
        click("NATIVE","xpath=//*[@text='Ajouter un service']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'Gérer l') and contains(@text,'abonnement')]"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Services payants' and ./parent::*[@text='SUBSCRIPTIONS_TABLE_VIEW']]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Achetez des services')]",0);

        //Drive Connect
        verifyElementFound("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[1]",0);
        click("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[1]",0,1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[(contains(@text,'Choisir l') and contains(@text,'abonnement')) and @class='UIAStaticText']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Sélectionnez un abonnement pour Drive Connect']"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Abonnement mensuel']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='/ mois']",0);
        verifyElementFound("NATIVE","xpath=(//*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW' and @text='Renouvellement automatique'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[@label='Continuer' and @class='UIAButton']",0);
        //click back
        verifyElementFound("NATIVE","xpath=//*[@label='remote services remove']",0);
        click("NATIVE","xpath=//*[@label='remote services remove']",0,1);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'Gérer l') and contains(@text,'abonnement')]"),0);

        //Remote Connect
        verifyElementFound("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[2]",0);
        click("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[2]",0,1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[(contains(@text,'Choisir l') and contains(@text,'abonnement')) and @class='UIAStaticText']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'Sélectionnez un abonnement pour Remote Connect')]"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Abonnement mensuel']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='/ mois']",0);
        verifyElementFound("NATIVE","xpath=(//*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW' and @text='Renouvellement automatique'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[@label='Continuer' and @class='UIAButton']",0);
        //click back
        verifyElementFound("NATIVE","xpath=//*[@label='remote services remove']",0);
        click("NATIVE","xpath=//*[@label='remote services remove']",0,1);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'Gérer l') and contains(@text,'abonnement')]"),0);

        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'SERVICE D') and contains(@text,'UN TIERS') and @class='UIAStaticText']")) {
            createLog("Started - validation 3RD PARTY SERVICE section");
            verifyElementFound("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[3]",0);
            click("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[3]",0,1);
            sc.syncElements(2000, 30000);

            verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Détails du service']"),0);
            verifyElementFound("NATIVE","xpath=//*[@text='Wi-Fi Connect']",0);
            verifyElementFound("NATIVE",convertTextToUTF8("//*[@id='Go To AT&T']"),0);
            click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
            createLog("Completed - validation 3RD PARTY SERVICE section");
        }
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'Gérer l') and contains(@text,'abonnement')]"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Continuez pour acheter' and @class='UIAButton']",0);

        click("NATIVE","xpath=//*[@text='Retour']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Ajouter un service']",0);
        createLog("Completed - Validation add service screen");
    }

    public static void addDriveConnectPaidService() {
        createLog("Started - Add Drive Connect Paid Service");
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Ajouter un service']",0);
        click("NATIVE","xpath=//*[@text='Ajouter un service']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'Gérer l') and contains(@text,'abonnement')]"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Services payants' and ./parent::*[@text='SUBSCRIPTIONS_TABLE_VIEW']]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Achetez des services')]",0);

        //verify Continuer to purchase button is disabled
        verifyElementFound("NATIVE","xpath=//*[(@text='Continuez pour acheter' and @class='UIAButton') and @enabled='false']",0);

        //Drive Connect
        verifyElementFound("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[1]",0);
        click("NATIVE","xpath=(//*[@label='SUBSCRIPTIONS_CELL_TITLE'])[1]",0,1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[(contains(@text,'Choisir l') and contains(@text,'abonnement')) and @class='UIAStaticText']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Sélectionnez un abonnement pour Drive Connect']"),0);
        verifyElementFound("NATIVE","xpath=//*[@text='Abonnement mensuel']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='/ mois']",0);
        verifyElementFound("NATIVE","xpath=(//*[@accessibilityLabel='SUBSCRIPTIONS_AUTO_RENEW' and @text='Renouvellement automatique'])[1]",0);
        verifyElementFound("NATIVE","xpath=//*[(@label='Continuer' and @class='UIAButton') and @enabled='false']",0);
        //click Abonnement mensuel
        click("NATIVE","xpath=//*[@text='Abonnement mensuel']",0,1);
        //verify Continuer button is enabled
        verifyElementFound("NATIVE","xpath=//*[(@label='Continuer' and @class='UIAButton') and @enabled='true']",0);
        //click continue
        click("NATIVE","xpath=//*[(@label='Continuer' and @class='UIAButton') and @enabled='true']",0,1);

        sc.syncElements(5000, 30000);
        //verify continue to purchase button is enabled
        verifyElementFound("NATIVE","xpath=//*[(@text='Continuez pour acheter' and @class='UIAButton') and @enabled='true']",0);
        //Click continue to purchase button
        click("NATIVE","xpath=//*[(@text='Continuez pour acheter' and @class='UIAButton') and @enabled='true']",0,1);
        sc.syncElements(2000, 30000);
        //consent screen
        verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and @onScreen='true'])[1]", 0, 1000, 5, false);
        sc.swipe("Down", sc.p2cy(70), 2000);
        click("NATIVE","xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and @onScreen='true'])[1]",0,1);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and @onScreen='true'])[2]", 0, 1000, 5, false);
        sc.swipe("Down", sc.p2cy(70), 2000);
        click("NATIVE","xpath=(//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT' and @onScreen='true'])[2]",0,1);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[(@id='Confirmer et continuer' and @class='UIAButton') and @enabled='true']", 0, 1000, 5, false);
        sc.swipe("Down", sc.p2cy(70), 2000);
        click("NATIVE", "xpath=//*[(@id='Confirmer et continuer' and @class='UIAButton') and @enabled='true']", 0, 1);
        //Home address
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='Adresse de domicile']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Adresse de domicile']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0);
            click("NATIVE", "xpath=//*[@text='PI_SUBMIT_BUTTON' and @enabled='true']", 0, 1);
        }
        sc.syncElements(5000, 30000);
        //Address verification screen
        if(sc.isElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Vérification de l') and contains(@text,'adresse')]"))){
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Vérification de l') and contains(@text,'adresse')]"), 0);
            verifyElementFound("NATIVE", "xpath=//*[(@text='Continuez avec cette adresse') and @class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[(@text='Continuez avec cette adresse') and @class='UIAButton']", 0, 1);
        }
        sc.syncElements(10000, 30000);

        //Payment method screen
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Méthode de paiement']"), 0);
        createLog("Completed - Add Drive Connect Paid Service");
    }
}
