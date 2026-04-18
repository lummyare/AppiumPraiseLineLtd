package v2update.subaru.ios.canada.french.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDataPrivacyPortalCAFrenchIOS extends SeeTestKeywords {
    String testName = "DataPrivacyPortalCAFrench-IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                testName = System.getProperty("cloudApp") + testName;
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                break;
        }
        iOS_Setup2_5(testName);
        sc.startStepsGroup("email SignIn 21MM");
        createLog("Start: 21mm email Login- Canada Subaru Next Gen");
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLogin("subarustageca@mail.tmnact.io","Test$123");
        // ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("End: 21mm email Login-CA-French");
        sc.stopStepsGroup();
    }
    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void _DataConsent() {
        DataConsent();
    }
    public static void DataConsent() {
        sc.startStepsGroup("Data Privacy Portal - Data Consents");
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Announcements']"))
            sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
        navigateToDataPrivacyPortal();
        sc.syncElements(5000, 20000);
        sc.swipe("Down", sc.p2cy(40), 2000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[];
        dataConsent = new String[]{"Portail sur la protection des données", "Ce portail s'inscrit dans notre engagement envers la protection de vos renseignements personnels. Vous êtes aux commandes en ce qui a trait à vos données et à votre expérience de véhicule connecté.",
                "Consentements à l’utilisation des données", "Consentement à l’utilisation des données de référence pour les Services connectés", "Nécessaire pour la plupart des produits et services", "Consentements au marketing", "Marketing dans l’app",
                "Avis de confidentialité et conditions d’utilisation des Services connectés","Conditions d'utilisation",};
        for (String detailsName : dataConsent) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        sc.swipeWhileNotFound("Up", sc.p2cy(50), 3000, "NATIVE", convertTextToUTF8("//*[@accessibilityLabel='Consentement à l’utilisation des données de référence pour les Services connectés']"), 0, 1000, 1, false);
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void _MasterDataConsent() {
        MasterDataConsent();
    }
    public static void MasterDataConsent() {
        sc.startStepsGroup("Data Privacy Portal - Master Data Consents");
        if (!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@accessibilityLabel='Consentement à l’utilisation des données de référence pour les Services connectés']"))) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@accessibilityLabel='Accepted' and (./preceding-sibling::* | ./following-sibling::*)[@id='Nécessaire pour la plupart des produits et services']]"), 0);
        click("NATIVE", convertTextToUTF8("//*[@accessibilityLabel='Consentement à l’utilisation des données de référence pour les Services connectés']"), 0, 1);
        sc.syncElements(2000, 5000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Données partagées pour les services suivants:' and @class='UIAStaticText']"), 0);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[];
        dataConsent = new String[]{"Consentement aux données de référence", "Consentement à l’utilisation des données de référence pour les Services connectés", "Votre consentement au partage des données pour les produits et services indiqués ci-dessous vous permet d’accéder à une suite évoluée de technologies intelligentes conçues pour offrir une expérience de conduite plus sûre et plus personnalisable",
                "Drive Connect", "Remote Connect", "Safety Connect", "Service Connect", "Produits qui nécessitent le consentement à l’utilisation des données de référence pour les Services connectés",
                "Communications de Service Connect", "Consentement à l’utilisation de Drive Pulse et des trajets récents"};
        for (String detailsName : dataConsent) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        sc.swipeWhileNotFound("Up", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[contains(@id,'Drive Connect')]", 0, 1000, 1, false);
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void _DriveConnect(){
        DriveConnect();
    }
    public static void DriveConnect() {
        sc.startStepsGroup("Data Privacy Portal - Drive Connect");
        if (!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Drive Connect')]")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
            click("NATIVE", convertTextToUTF8("//*[@accessibilityLabel='Consentement à l’utilisation des données de référence pour les Services connectés']"), 0, 1);
            sc.syncElements(2000, 10000);
        }
        //Drive Connect
        click("NATIVE", "xpath=//*[contains(@id,'Drive Connect')]", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog("Started Drive Connect");
        createLog(actualText);
        createLog("Completed Drive Connect");
        String dataConsent[] = {"Données partagées avec Drive Connect","Fournit aux conducteurs des outils qui les aident à rester concentrés sur la route, comme un système de commande vocale, une couverture cartographique actualisée et un accès 24 h sur 24 aux agents du Centre de réponse, notamment","L’Assistant intelligent","La navigation infonuagique","Destination Assist",
                "Conduite","La direction du véhicule","Emplacement","Emplacement du véhicule","Information sur les événements déclenchés par le client", "La recherche de points d’intérêt",
                "Client","Nom","Adresse","Courriel","Téléphone","Des spécimens de voix",
                "Véhicule","Numéro d’identification du véhicule (NIV)","Marque du véhicule","Modèle du véhicule","Année du véhicule", "Refus du consentement"};
        for (String detailsName : dataConsent) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void _RemoteConnect(){
        RemoteConnect();
    }
    public static void RemoteConnect() {
        sc.startStepsGroup("Data Privacy Portal - Remote Connect");
        if (!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Remote Connect')]")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
            click("NATIVE", convertTextToUTF8("//*[@accessibilityLabel='Consentement à l’utilisation des données de référence pour les Services connectés']"), 0, 1);
            sc.syncElements(2000, 10000);
        }
        //Remote Connect
        sc.swipe("Down", sc.p2cy(40), 3000);
        click("NATIVE", "xpath=//*[contains(@id,'Remote Connect')]", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog("Started Remote Connect");
        createLog(actualText);
        createLog("Completed Remote Connect");
        String dataConsent[] = {"Données partagées avec Remote Connect","Offre aux conducteurs abonnés des fonctions de commodité et de connectivité à l’aide de leur téléphone intelligent, de leur montre intelligente, de l’Assistant Google ou des appareils compatibles avec Amazon Alexa.","Démarrage à distance, verrouillage/déverrouillage des portières (comprend le démarrage à distance à l’aide de la télécommande porte-clés)","Surveillance des conducteurs invités","Alertes du véhicule","Localisateur du véhicule","Gestion de la recharge (certains véhicules seulement)","Digital Key (certains véhicules seulement)",
                "Données partagées","Conduite","Surveillance des conducteurs invités (lorsque cette fonction est activée)","Emplacement","Emplacement du véhicule","Emplacement du téléphone (utilisé pour les véhicules électriques sans Remote Connect)",
                "Véhicule","État du véhicule","Portière verrouillée/déverrouillée","Vitre ouverte/fermée","Toit ouvrant ouvert/fermé",
                "Détails du trajet (distance du trajet A/distance du trajet B)","Préférences de température","Refus du consentement"};
        for (String detailsName : dataConsent) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void _SafetyConnect(){
        SafetyConnect();
    }
    public static void SafetyConnect() {
        sc.startStepsGroup("Data Privacy Portal - Safety Connect");
        //Safety Connect
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@id='Service Connect']", 0, 1000, 1, false);
        if (!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Safety Connect')]")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
            click("NATIVE", convertTextToUTF8("//*[@accessibilityLabel='Consentement à l’utilisation des données de référence pour les Services connectés']"), 0, 1);
            sc.syncElements(2000, 10000);
            sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@id='Service Connect']", 0, 1000, 1, false);
        }
        sc.swipe("Down", sc.p2cy(40), 3000);
        click("NATIVE", "xpath=//*[contains(@id,'Safety Connect')]", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog("Started Safety Connect");
        createLog(actualText);
        createLog("Completed Safety Connect");
        String dataConsent[] = {"Données partagées avec Safety Connect","Offre aux conducteurs une suite de services conçus pour les aider en cas d’urgence","Notification automatique de collision","Assistance en cas d’urgence","Assistance routière améliorée","Localisation de véhicule volé",
                "Client","Nom","Adresse","Téléphone","Courriel",
                "Données partagées","Emplacement","Emplacement du véhicule","Véhicule","Marque du véhicule","Modèle du véhicule","Année du véhicule",
                "Odomètre","Information sur les événements déclenchés par le client","Enregistrement des conversations lors des appels d’urgence","Refus du consentement"};
        for (String detailsName : dataConsent) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void _ServiceConnect(){
        ServiceConnect();
    }
    public static void ServiceConnect() {
        sc.startStepsGroup("Data Privacy Portal - Service Connect");
        //Service Connect
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@id='Products that require Connected Services Master Data Consent' and @class='UIAStaticText']", 0, 1000, 1, false);
        if (!sc.isElementFound("NATIVE","xpath=//*[@id='Service Connect']")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
            click("NATIVE", convertTextToUTF8("//*[@accessibilityLabel='Consentement à l’utilisation des données de référence pour les Services connectés']"), 0, 1);
            sc.syncElements(2000, 10000);
        }
        sc.swipe("Down", sc.p2cy(40), 3000);
        click("NATIVE", "xpath=//*[@id='Service Connect']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog("Started Service Connect");
        createLog(actualText);
        createLog("Completed Service Connect");
        String dataConsent[] = {"Données partagées avec Service Connect","Offre aux conducteurs des rapports de santé personnalisés sur le véhicule ainsi que des avertissements concernant le véhicule et l’entretien","Données partagées","Client","Nom","Courriel","Abonnement aux services télématiques",
                "Santé du véhicule","Information sur l’unité de commande électronique (ECU) du véhicule","Codes d’anomalies (DTC) du véhicule","Informations sur les avertissements et l’entretien du véhicule","Données figées du véhicule",
                "Bilan de santé du véhicule","Refus du consentement","consentement à l’utilisation des données de référence pour les Services connectés"};
        for (String detailsName : dataConsent) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void _AcceptDeclineMasterDataConsentValidation(){
        AcceptDeclineMasterDataConsentValidation();
    }
    public static void AcceptDeclineMasterDataConsentValidation() {
        sc.startStepsGroup("Data Privacy Portal - Accept Decline Master Data Consent validation");
        //Consent
        click("NATIVE", convertTextToUTF8("//*[(@id='Gérer les consentements' or @id='GÉRER LES CONSENTEMENTS') and @class='UIAButton']"), 0, 1);
        sc.syncElements(10000, 60000);
        //strAppType = strAppType.substring(0, 1).toUpperCase() + strAppType.substring(1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@label='Consentement à l’utilisation des données de référence pour les Services connectés']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@label,'En acceptant les Consentement à l’utilisation des données de référence pour les Services connectés, vous acceptez que, de façon régulière et continue, selon les capacités de votre véhicule, il transmette sans fil à Toyota et ses filiales son emplacement ainsi que les données sur la conduite et l’état de santé du véhicule afin de fournir les Services connectés et à des fins de recherche interne, de développement et d’analyse des données.')]"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE",  convertTextToUTF8("//*[@text='politique de confidentialité']"), 0, 1000, 3, false);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='politique de confidentialité']"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", convertTextToUTF8("//*[@text='modalités de service']"), 0, 1000, 3, false);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='modalités de service']"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", convertTextToUTF8("xpath=//*[(contains(@label,'Pour désactiver la capacité de transmission de données de votre véhicule, appuyez sur') and contains(@label,'Décliner')) and @visible='true']"), 0, 1000, 5, false);

        click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_DECLINE']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(50), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent1[] = {"Êtes-vous certain? Vous allez perdre TOUS les services.", "Ceci est le consentement à l’utilisation des données de référence pour les abonnements aux Services connectés et les produits et services associés. Si nous ne recevons pas les données de votre véhicule, nous ne pourrons pas fournir ces abonnements, ces produits et ces services. Cela comprend (le cas échéant) la fonction de démarrage à distance de la télécommande porte-clés.",
                "Important", "exige que votre véhicule reste associé à votre compte","pour que la transmission des données reste DÉSACTIVÉE. Si vous supprimez votre véhicule de votre compte, la capacité de transmission de données du véhicule sera automatiquement ACTIVÉE par défaut.", "Les Services connectés que vous allez perdre", "Abonnements",
                "Drive Connect","Remote Connect", "Safety Connect", "Service Connect", "Autres produits et services", "Communications de Service Connect", "Consentement à l’utilisation de Drive Pulse et des trajets récents"};
        for (String detailsName : dataConsent1) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    //Defect:
    public void _DeclineServiceConnectCommunication(){
        DeclineServiceConnectCommunication();
    }
    public static void DeclineServiceConnectCommunication() {
        if (!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@accessibilityLabel='Consentement à l’utilisation des données de référence pour les Services connectés']"))) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
        }
        sc.startStepsGroup("Data Privacy Portal - Decline Services Connect Communication");
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='Communications de Service Connect']", 0, 1000, 1, false);
        sc.syncElements(5000,10000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Accepted' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='Communications de Service Connect']]", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='Communications de Service Connect']", 0, 1);
        sc.syncElements(5000, 10000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[] = {"Communications de Service Connect", "Fournit au concessionnaire qui fait l’entretien de votre véhicule des alertes concernant l’état de santé et l’entretien du véhicule",
                "Données partagées", "Client", "Nom", "Courriel", "Abonnement aux services télématiques"
                ,"Santé du véhicule", "Information sur l’unité de commande électronique (ECU) du véhicule", "Codes d’anomalies (DTC) du véhicule", "Informations sur les avertissements et l’entretien du véhicule","Données figées du véhicule","Bilan de santé du véhicule"};
        for (String detailsName : dataConsent) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", convertTextToUTF8("//*[(@id='Gérer les consentements' or @id='GÉRER LES CONSENTEMENTS') and @class='UIAButton']"), 0, 1);
        sc.syncElements(5000, 10000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent1[] = {"Consentement à l’utilisation des communications de Service Connect", "(Services connectés requis)",
                "En acceptant les Communications Service Connect, vous consentez à partager des données sur l’état de santé du véhicule avec votre concessionnaire pour son propre usage et à lui permettre de vous téléphoner pour fixer des rendez-vous de service et présenter des offres de marketing liées au service au numéro que vous avez fourni."};
        for (String detailsName : dataConsent1) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_DECLINE']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
        click("NATIVE", "xpath=//*[(@id='Refuser le consentement' or @id='REFUSER LE CONSENTEMENT') and @class='UIAButton']", 0, 1);
        sc.syncElements(15000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Declined' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='Communications de Service Connect']]", 0);
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void _AcceptServiceConnectCommunication(){
        AcceptServiceConnectCommunication();
    }
    public static void AcceptServiceConnectCommunication() {
        sc.startStepsGroup("Data Privacy Portal - Accept Services Connect Communication");
        acceptDataConsent("Communications de Service Connect");
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    //Defect: Terms & use section is displayed for Lexus Mexico
    public void _PrivacyAndTermsOfUse(){
        PrivacyAndTermsOfUse();
    }
    public static void PrivacyAndTermsOfUse() {
        sc.startStepsGroup("Data Privacy Portal - Connected Services Privacy and Terms of Use");
        if (!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@accessibilityLabel='Consentement à l’utilisation des données de référence pour les Services connectés']"))) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
        }
        sc.swipe("Down", sc.p2cy(40), 3000);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_TERMS_OF_USE_CELL']", 0, 1000, 1, false);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_PRIVACY_NOTICE_CELL']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Avis de confidentialité']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_TERMS_OF_USE_CELL']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Conditions d') and contains(@text,'utilisation')]"), 0);
        //Privacy Notice
        click("NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_PRIVACY_NOTICE_CELL']", 0, 1);
        sc.syncElements(10000, 60000);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[contains(@text,'Politique de confidentialité de') and @class='UIAImage'])[1]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE' and ./parent::*[(./preceding-sibling::* | ./following-sibling::*)[@class='UIAWebView']]]", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT' and ./parent::*[(./preceding-sibling::* | ./following-sibling::*)[@class='UIAWebView']]]", 0, 1);
        sc.syncElements(2000, 10000);
        //Terms of Use
        click("NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_TERMS_OF_USE_CELL']", 0, 1);
        sc.syncElements(10000, 60000);
        click("NATIVE", "xpath=(//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT'])[1]", 0, 1);
        sc.syncElements(5000, 10000);
        sc.swipe("Up", sc.p2cy(20), 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    public void LogOut() {
        sc.startStepsGroup("Email Logout");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void acceptDataConsent(String DataConsent) {
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Declined' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + DataConsent + "']]", 0);
        click("NATIVE", "xpath=//*[contains(@accessibilityLabel,'" + DataConsent + "')]", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'" + DataConsent + "')]", 0);
        click("NATIVE", convertTextToUTF8("//*[(@id='Gérer les consentements' or @id='GÉRER LES CONSENTEMENTS') and @class='UIAButton']"), 0, 1);
        sc.syncElements(2000, 10000);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT']", 0, 1000, 1, false);
        click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT']", 0, 1);
        sc.syncElements(15000, 60000);
        if (DataConsent.equalsIgnoreCase("External Vehicle Video Capture")) {
            verifyElementFound("NATIVE", "xpath=//*[@id='CONSENT ACCEPTED' or @id='Consent Accepted']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'You accepted the Safety Sense Video Capture Consent')]", 0);
            click("NATIVE", "xpath=//*[@id='BACK TO DATA PRIVACY PORTAL' or @id='Back To Data Privacy Portal' and @class='UIAButton']", 0, 1);
            sc.syncElements(10000, 60000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Accepted' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + DataConsent + "']]", 0);
    }

    public static void navigateToDataPrivacyPortal() {
        createLog("Started : Navigating to Data Privacy Portal screen");
        sc.syncElements(2000, 10000);
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Se déconnecter']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(4000, 20000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Réglages du compte' or @text='RÉGLAGES DU COMPTE']"), 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_DATA_CONSENTS_CELL']", 0, 1000, 1, false);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Portail sur la protection des données']"), 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_DATA_CONSENTS_CELL']", 0, 1);
        sc.syncElements(5000, 30000);
        if (sc.isElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@label='Sélectionner un véhicule'])[1]"))) {
            createLog("select vehicle screen is displayed");
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[(@text='Portail sur la protection des données' or @text='PORTAIL SUR LA PROTECTION DES DONNÉES') and @accessibilityLabel='TOOLBAR_LABEL_TITLE']"), 0);
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[contains(@text,'Véhicule')])[1]"), 0);
            click("NATIVE", convertTextToUTF8("xpath=(//*[contains(@text,'Véhicule')])[1]"), 0, 1);
            sc.syncElements(15000, 60000);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='Portail sur la protection des données']"), 0);
        createLog("Completed : Navigating to Data Privacy Portal screen");
    }
}