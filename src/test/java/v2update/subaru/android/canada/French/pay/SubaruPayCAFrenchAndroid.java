package v2update.subaru.android.canada.French.pay;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruPayCAFrenchAndroid extends SeeTestKeywords {
    String testName = "Pay-Android-21MM-PR Spanish";

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
    public void wallet21mm() {
        sc.startStepsGroup("Pay-Wallet-21mm-CA French");
        wallet();
        sc.stopStepsGroup();

    }
    @Test
    @Order(4)
    public void addPayment(){
        if (isStageApp==true){
            sc.startStepsGroup("Add new card CA french");
            addNewCard();
            sc.stopStepsGroup();
        }
    }
    @Test
    @Order(5)
    public void editPayment(){
        if (isStageApp==true){
            sc.startStepsGroup("edit payment CA french");
            editPaymentMethod();
            sc.stopStepsGroup();
        }
    }
    @Test
    @Order(6)
    public void removePayment(){
        if (isStageApp==true){
            sc.startStepsGroup("remove payment CA French");
            removePaymentMethod();
            sc.stopStepsGroup();
        }

    }

    public static void wallet(){
        createLog("Started: wallet validation");
        click("NATIVE","xpath=//*[@text='Payer']",0,1);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='wallet_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Portefeuille']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Gérer le paiement méthodes pour votre véhicule abonnements.']", 0);
        click("NATIVE", "xpath=//*[@id='regular_wallet']", 0, 1);
        sc.syncElements(5000, 10000);

        if(sc.isElementFound("NATIVE","xpath=//*[@content-desc='Wallet']//following-sibling::*[contains(@class,'ImageView')]",0)) {
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Wallet']//following-sibling::*[contains(@class,'ImageView')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Wallet']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Subscriptions')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'back_button')]", 0);
            click("NATIVE", "xpath=xpath=//*[contains(@content-desc,'Subscriptions')]", 0, 1);
            sc.syncElements(5000, 10000);
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Méthode de paiement']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Sélectionner un véhicule']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Veuillez sélectionner un véhicule pour ajouter ou gérer votre méthode de paiement.']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'La méthode de paiement')]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='chevron']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='image' or @content-desc='Vehicle']", 0);

    }
    public static void  addNewCard(){
        createLog("Started: add new card validation");
        navigateToPaymentMethodScreen();
        click("NATIVE",convertTextToUTF8("//*[@text='Ajouter une nouvelle carte de crédit']"),0,1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Renseignements sur le paiement']"),0);
        sendText("NATIVE","xpath=//*[@id='input-creditCardHolderName']",0,strRandomName);
        sendText("NATIVE","xpath=//*[@id='input-creditCardNumber']",0,"5555555555554444");
        click("NATIVE","xpath=//*[@id='input-creditCardExpirationMonth']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@text='02']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@id='input-creditCardExpirationYear']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@text='2029']",0,1);
        sc.syncElements(1000, 3000);
        sc.syncElements(1000, 3000);
        sendText("NATIVE","xpath=//*[@id='input-cardSecurityCode']",0,"222");
        sendText("NATIVE","xpath=//*[@id='input-creditCardAddress1']",0,"Test");
        sendText("NATIVE","xpath=//*[@id='input-creditCardCity']",0,"Test");
        sc.swipe("DOWN",sc.p2cy(35),5000);
        sc.swipe("DOWN",sc.p2cy(35),5000);
        click("NATIVE","xpath=//*[@id='input-creditCardState']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@text='Alaska']",0,1);
        sendText("NATIVE","xpath=//*[@id='input-creditCardPostalCode']",0,"75067");
        click("NATIVE","xpath=//*[@id='mat-checkbox-1-input']",0,1);
        click("NATIVE","xpath=//*[@text='Continue to Review']",0,1);
        sc.syncElements(3000, 4000);

        if (sc.getElementCount("NATIVE","xpath=//*[@text='************4444']")==1) {
            createErrorLog("Add payment method test failed");
            fail();
        }else{
            createLog("Add payment method successfully");
        }
    }
    public static void editPaymentMethod(){
        sc.syncElements(3000, 4000);
        click("NATIVE","xpath=//*[@text='************4444']",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='edit']",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='button1']",0,1);
        sc.syncElements(2000, 4000);
        sendText("NATIVE","xpath=//*[@id='cvv']",0,"222");
        sc.syncElements(2000, 4000);
        sendText("NATIVE","xpath=//*[@id='name']",0,strRandomName);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='save']",0,1);
        sc.syncElements(2000, 4000);

        if (sc.getElementCount("NATIVE","xpath=//*[@text='************4444']")==1) {
            createErrorLog("Editing payment method test failed");
            fail();
        }else{
            createLog("Edited payment method successfully");
        }
    }

    public static void  removePaymentMethod() {
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@text='************4444']",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='edit']",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='button3']",0,1);
        sc.syncElements(3000, 6000);
        if (sc.getElementCount("NATIVE","xpath=//*[@text='************4444']")==2) {
            createErrorLog("Removing payment method test failed");
            fail();
        }else{
            createLog("Removed payment method successfully");
        }
    }

    public static void  navigateToPaymentMethodScreen()
    {
        if(!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']", 0))
        {
            reLaunchApp_android();
        }
        click("NATIVE","xpath=//*[@text='Payer']",0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Gérer le paiement méthodes pour votre véhicule abonnements.']"), 0);
        click("NATIVE", "xpath=//*[@id='regular_wallet']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Méthode de paiement']"), 0);

        click("NATIVE", "xpath=//*[@id='chevron']", 0,1);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='button1']",0))
        {
            click("NATIVE","xpath=//*[@id='button1']",0,1);

        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Services payants']"), 0);
        click("NATIVE", "xpath=//*[@id='cb_check']",0,1);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(text(),'Sélectionnez un abonnement pou')]"), 0);
        click("NATIVE", "xpath=//*[@id='cl_content']",0,1);
        click("NATIVE", convertTextToUTF8("//*[@text='Renouvellement automatique']"),0,1);
        click("NATIVE", convertTextToUTF8("//*[@text='Continuer']"),0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Services payants']"), 0);
        click("NATIVE", convertTextToUTF8("//*[contains(text(),'Continuer')]"),0,1);
        sc.swipeWhileNotFound("Down", sc.p2cy(80), 2000, "NATIVE", "xpath=//*[@text='Accept']", 0, 1000, 2, false);
        click("NATIVE", convertTextToUTF8("//*[@text='Accept']"),0,1);

        sc.swipeWhileNotFound("Down", sc.p2cy(50), 2000, "NATIVE", "xpath=//*[contains(text(),'Service Connect Communication Consent')]/following-sibling::*[@text='Accept']" , 0, 1000, 2, false);

        click("NATIVE", "xpath=//*[contains(text(),'Service Connect Communication Consent')]/following-sibling::*[@text='Accept']",0,1);
        click("NATIVE", convertTextToUTF8("//*[@text='Confirmer et continuer']"),0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Adresse de domicile']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Veuillez entrer votre adresse de facturation. Nous avons besoin de cette information pour appliquer les taxes appropriées.']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Continuer']"),0,1);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Méthode de paiement']"), 0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Ajouter une nouvelle carte de crédit']"),0);

    }
}
