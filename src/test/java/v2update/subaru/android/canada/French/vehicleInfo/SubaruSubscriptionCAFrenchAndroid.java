package v2update.subaru.android.canada.French.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruSubscriptionCAFrenchAndroid extends SeeTestKeywords {

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
    public void subscriptionsScreenValidations() {
        sc.startStepsGroup("Subscriptions Screen Validation - PR Spanish");
        subscriptionsScreenValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void subscriptionValidationsSafetyConnect() {
        sc.startStepsGroup("Subscriptions - Safety Connect Validation - PR Spanish");
        subscriptionValidationSafetyConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    public void subscriptionValidationsRemoteConnectDK() {
        sc.startStepsGroup("Subscriptions - Remote Connect With Digital Key - PR Spanish");
        subscriptionValidationRemoteConnectDK();
        sc.stopStepsGroup();
    }


    @Test
    @Order(4)
    public void subscriptionValidationsServiceConnect() {
        sc.startStepsGroup("Subscriptions - Service Connect validation - PR Spanish");
        subscriptionValidationServiceConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void subscriptionValidationsDriveConnect() {
        sc.startStepsGroup("Subscriptions - Drive Connect validation - PR Spanish");
        subscriptionValidationDriveConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void signOut() {
        sc.startStepsGroup("Signout");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void subscriptionsScreenValidation(){
        navigateToSubscriptionsScreen();
        verifyElementFound("NATIVE", "xpath=//*[@text='Abonnements']",0);
        sc.syncElements(2000, 30000);
        createLog("Verifying trial services ");
        //       verifyElementFound("NATIVE", "xpath=//*[@text='Trial Services']", 0);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Safety Connect']")){
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Service Icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Safety Connect']//preceding-sibling::*//*[@content-desc='Status Icon']", 0);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Service Connect']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Service Connect']//preceding::*//*[@content-desc='Service Icon']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Service Connect']//preceding::*//*[@content-desc='Status Icon']", 0);
        }
        sc.swipeWhileNotFound("Down",sc.p2cy(30),2000,"NATIVE","xpath=//*[@text='Remote Connect con Digital Key']//preceding-sibling::*//*[@content-desc='Status Icon']",0,1000,2,false);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Drive Connect']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding::*[3]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']//preceding-sibling::*//*[@content-desc='Status Icon']", 0);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Remote Connect con Digital Key']")){
            verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect con Digital Key']//preceding::*[3]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect con Digital Key']//preceding-sibling::*//*[@content-desc='Status Icon']", 0);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Ajouter un service']"), 0);
        createLog("Completed: Subscription screen validation");

    }
    public static void subscriptionValidationSafetyConnect() {
        if(!sc.isElementFound("NATIVE", "xpath=//*[@text='Safety Connect']",0))
        {
            reLaunchApp_android();
            navigateToSubscriptionsScreen();
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Safety Connect']", 0);
        click("NATIVE", "xpath=//*[@text='Safety Connect']", 0, 1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails du service'] | //*[@text='DETALLES DEL SERVICIO']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Safety Connect']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Safety Connect peut vous aider en envoyant de')]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='tv_trial_service']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='tv_expires_txt']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Annuler']"), 0);
        click("NATIVE","xpath=//*[@contentDescription='Revenir en arrière']",0,1);
        sc.syncElements(4000, 30000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Abonnements']"), 0);
        createLog("Completed: Safety Connect Validation");


    }

    public static void subscriptionValidationRemoteConnectDK() {
        if(!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Remote Connect avec la clé numérique']"),0))
        {
            reLaunchApp_android();
            navigateToSubscriptionsScreen();
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Remote Connect avec la clé numérique']"), 0);
        click("NATIVE",convertTextToUTF8("//*[@text='Remote Connect avec la clé numérique']"),0,1);
        sc.syncElements(2000, 30000);


        verifyElementFound("NATIVE", "xpath=//*[@text='Détails du service']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Remote Connect avec la clé numérique']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'trouvez la station de recharge la plus proche et bien plus encore. Vous ')]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='tv_trial_service']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='tv_expires_txt']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Annuler']"), 0);
        click("NATIVE","xpath=//*[@contentDescription='Revenir en arrière']",0,1);
        sc.syncElements(4000, 30000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Abonnements']"), 0);
        createLog("Completed: Remote Connect Validation");
    }

    public static void subscriptionValidationServiceConnect() {
        if(!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Service Connect']"),0))
        {
            reLaunchApp_android();
            navigateToSubscriptionsScreen();
            if(!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Service Connect']"),0))
            {
                createLog("Service Connect is not available");
            }
            else {
                verifyElementFound("NATIVE", "xpath=//*[@text='Service Connect']", 0);
                click("NATIVE", "xpath=//*[@text='Service Connect']", 0, 1);
                sc.syncElements(2000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='DÉTAILS DU SERVICE']", 0);
                verifyElementFound("NATIVE", "xpath=//*[contains(@resource-id,'tv_title')]", 0);
                verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Obtenez des informations actualisées sur l’état de santé ainsi que sur le niveau de carburant, le kilométrage, les alertes d’entretien et plus encore, avec Service Connect sur votre app.']"), 0);
                verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Mention légale : Les services dépendent de la connexion à un réseau sans fil compatible 4G fourni par un fournisseur de services sans fil tiers. Toyota n’est pas responsable des interruptions de service du réseau cellulaire et n’offrira pas d’indemnisation en cas de disponibilité réduite du service.')]"), 0);
                verifyElementFound("NATIVE", "xpath=//*[contains(@resource-id,'tv_trial_service')]", 0);
                verifyElementFound("NATIVE", "xpath=//*[contains(@resource-id,'tv_expires_txt')]", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='ANNULER']", 0);
                click("NATIVE",convertTextToUTF8("//*[@contentDescription='Revenir en arrière']"),0,1);
                sc.syncElements(4000, 30000);
            }
        }

    }

    public static void subscriptionValidationDriveConnect() {
        if(!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Drive Connect']"),0)) {
            reLaunchApp_android();
            navigateToSubscriptionsScreen();

            if (!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Drive Connect']"), 0)) {
                createLog("Drive Connect is not available");
            } else {
                verifyElementFound("NATIVE", "xpath=//*[@text='Drive Connect']", 0);
                click("NATIVE", "xpath=//*[@text='Drive Connect']", 0, 1);
                sc.syncElements(2000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='DÉTAILS DU SERVICE']", 0);
                verifyElementFound("NATIVE", "xpath=//*[contains(@resource-id,'tv_title')]", 0);
                //*[contains(@text,'Drive Connect améliore votre expérience de')]
                // verifyElementFound("NATIVE", "xpath=//*[contains(@resource-id,'tv_trial_service')]", 0);
                verifyElementFound("NATIVE", "xpath=//*[contains(@resource-id,'tv_expires_txt')]", 0);
                click("NATIVE", convertTextToUTF8("//*[@content-desc='Revenir en arrière']"), 0, 1);
                sc.syncElements(4000, 30000);
            }
        }
    }



    public static void navigateToSubscriptionsScreen()
    {
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Info']"))) {
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        click("NATIVE", "xpath=//*[@text='Info']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='vehicle_info_glove_box_icon']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Abonnements']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Abonnements']"), 0,1);


    }



}
