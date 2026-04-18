package v2update.subaru.ios.canada.french.pay;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruPayCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruPayCAFrench-IOS";

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
        iOS_Setup2_5(this.testName);
        environmentSelection_iOS("prod");
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin("subarunextgen3@gmail.com","Test$123456");
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void walletEVTest() {
        sc.startStepsGroup("Test - wallet EV");
        walletEV();
        sc.stopStepsGroup();
    }

    public static void walletEV() {
        createLog("Verifying wallet in pay bottom bar");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@id='BottomTabBar_payTab']")) {
            reLaunchApp_iOS();
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='BottomTabBar_payTab']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Payer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='IconPay']", 0);
        click("NATIVE", "xpath=//*[@id='BottomTabBar_payTab']", 0, 1);
        sc.syncElements(3000, 30000);
        //verify pay icon is highlighted
        verifyElementFound("NATIVE", "xpath=//*[@text='IconPayHighlight']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Portefeuille']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Gérer le paiement')]"), 0);

        click("NATIVE", "xpath=//*[@text='Portefeuille']", 0, 1);
        sc.syncElements(10000, 20000);

        //verify Payment & screen
        createLog("Verifying Subscriptions and Payment screen");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Méthode de paiement']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Sélectionner un véhicule']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='subscription_chevronRight' or contains(@text,'La méthode de paiement')]"), 0, 1);
        sc.syncElements(3000, 30000);
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Pour ajouter une méthode de paiement, allez à ‘Gérer les abonnements’ pour ajouter un produit de services connectés.']"))) {
            createLog(convertTextToUTF8("//*[@text='Pour ajouter une méthode de paiement, allez à ‘Gérer les abonnements’ pour ajouter un produit de services connectés.']"));
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Gérer les abonnements']"), 0);
            if (isStageApp) {
                createLog("Stage app - add subscription");
                addCreditCard();
                removeCreditCard();

                click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
                sc.syncElements(3000, 30000);
                click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Portefeuille']", 0);
                click("NATIVE", "xpath=//*[@accessibilityLabel='BottomTabBar_dashboardTab']", 0, 1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle image, double tap to open vehicle info.']", 0);
            } else {
                createLog("Production app - skipping subscription");
                click("NATIVE", "xpath=//*[@text='Annuler']", 0, 1);
                //click back button to navigate to Wallet screen
                click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Portefeuille']", 0);
                click("NATIVE", "xpath=//*[@accessibilityLabel='BottomTabBar_dashboardTab']", 0, 1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle image, double tap to open vehicle info.']", 0);
            }
        } else if (sc.isElementFound("NATIVE", "xpath=//*[@text='Paiements des abonnements']")) {
            createLog("subscription payments is displayed - add/remove credit card");
            if (isStageApp) {
                createLog("Stage app - add or remove card");
                addCreditCard();
                removeCreditCard();

                click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
                sc.syncElements(3000, 30000);
                click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Portefeuille']", 0);
                click("NATIVE", "xpath=//*[@accessibilityLabel='BottomTabBar_dashboardTab']", 0, 1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle image, double tap to open vehicle info.']", 0);
            } else {
                createLog("Production app - skipping remove card");
                click("NATIVE", "xpath=//*[@text='Annuler']", 0, 1);
                //click back button to navigate to Wallet screen
                click("NATIVE", "xpath=//*[@id='TOOLBAR_BUTTON_LEFT']", 0, 1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Portefeuille']", 0);
                click("NATIVE", "xpath=//*[@accessibilityLabel='BottomTabBar_dashboardTab']", 0, 1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle image, double tap to open vehicle info.']", 0);
            }
        }
        createLog("Verified wallet in pay bottom bar");
    }

    public static void addCreditCard() {
        createLog("Adding credit card");

        int beforeCardCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@text,'************')]");
        createLog("Before adding new card - count is : " + beforeCardCount);

        verifyElementFound("NATIVE", "xpath=//*[@text='Ajouter une nouvelle carte de crédit']", 0);
        click("NATIVE", "xpath=//*[@text='Ajouter une nouvelle carte de crédit']", 0, 1);
        sc.syncElements(20000, 40000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Renseignements sur le paiement']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Pay securely using your credit card' ) or contains(@text,'PAY SECURELY USING YOUR CREDIT CARD')]", 0);

        verifyElementFound("NATIVE", "xpath=//*[(@id='Name on Card' or @id='NAME ON CARD') and @class='UIAStaticText']", 0);
        sendText("NATIVE", "xpath=(//*[(@id='Name on Card' or @id='NAME ON CARD') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]", 0, strRandomName);
        verifyElementFound("NATIVE", "xpath=//*[(@id='Card Number' or @id='CARD NUMBER') and @class='UIAStaticText']", 0);
        sendText("NATIVE", "xpath=(//*[(@id='Card Number' or @id='CARD NUMBER') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]", 0, "5555555555554444");
        click("NATIVE", "xpath=//*[@text='Exp. Date Month']", 0, 1);
        sc.syncElements(1000, 3000);
        click("NATIVE", "xpath=//*[@id='02']", 0, 1);
        sc.syncElements(1000, 3000);
        click("NATIVE", "xpath=//*[@text='Year']", 0, 1);
        sc.syncElements(1000, 3000);
        click("NATIVE", "xpath=//*[@id='2029']", 0, 1);
        sc.syncElements(1000, 3000);
        sendText("NATIVE", "xpath=(//*[@id='CVV']/following::*[@class='UIATextField'])[1]", 0, "222");
        sc.swipe("Down", sc.p2cy(20), 5000);
        sc.syncElements(1000, 3000);
        sendText("NATIVE", "xpath=(//*[(@id='Billing Address' or @id='BILLING ADDRESS') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]", 0, "Test");
        sc.syncElements(1000, 3000);
        sendText("NATIVE", "xpath=(//*[(@id='City' or @id='CITY') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]", 0, "Test");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Country' and @class='UIAStaticText']", 0);
        click("NATIVE", "xpath=(//*[@id='Country' and @class='UIAView']/following-sibling::*[@class='UIAView'])[1]", 0, 1);
        sc.syncElements(2000, 10000);
        click("NATIVE", "xpath=//*[@id='United States']", 0, 1);
        sc.syncElements(2000, 10000);
        //state
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='- Select One -']")) {
            createLog("select state from drop down list");
            click("NATIVE", "xpath=//*[@text='- Select One -']", 0, 1);
            sc.syncElements(1000, 3000);
            click("NATIVE", "xpath=//*[@id='Alaska']", 0, 1);
        } else {
            createLog("Enter state");
            sendText("NATIVE", "xpath=(//*[@id='State']/following::*[@class='UIATextField'])[1]", 0, "Texas");
        }
        sc.syncElements(2000, 10000);
        sendText("NATIVE", "xpath=(//*[(@id='Zip Code' or @id='ZIP CODE') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]", 0, "75094");
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=(//*[@id='payment'])[1]/following::*[@id='DEFAULT PAYMENT METHOD' or @id='Default Payment Method']")) {
            //displays both check box DEFAULT PAYMENT METHOD and consent
            //click on DEFAULT PAYMENT METHOD check box
            click("NATIVE", "xpath=((//*[@id='payment'])[1]/following::*[@class='UIAView'])[1]", 0, 1);
            sc.syncElements(2000, 4000);
            //click on consent check box
            click("NATIVE", "xpath=(//*[@id='DEFAULT PAYMENT METHOD' or @id='Default Payment Method']/following::*[@class='UIAView'])[1]", 0, 1);
            sc.syncElements(2000, 4000);
        } else {
            //displays only consent check box
            click("NATIVE", "xpath=((//*[@id='payment'])[1]/following::*[@class='UIAView'])[1]", 0, 1);
            sc.syncElements(2000, 4000);
        }
        click("NATIVE", "xpath=//*[(@id='Continue to Review' or @id='CONTINUE TO REVIEW') and @enabled='true']", 0, 1);
        sc.syncElements(15000, 60000);

        int afterCardCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@text,'************')]");
        createLog("After adding new card - count is : " + afterCardCount);

        if (isStageApp) {
            if (afterCardCount > beforeCardCount) {
                createLog("Added payment method successfully");
            } else {
                createErrorLog("Adding payment method test failed");
                fail();
            }
        }
    }

    public static void removeCreditCard() {
        createLog("Started: Deleting card");
        sc.syncElements(2000, 4000);

        int beforeCardCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@text,'************')]");
        createLog("Before deleting card - count is : " + beforeCardCount);

        click("NATIVE", "xpath=(//*[contains(@text,'************')])[1]", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@id='Modifier']", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@text='Supprimer'] ", 0, 1);
        sc.syncElements(15000, 60000);

        int afterCardCount = sc.getElementCount("NATIVE", "xpath=//*[contains(@text,'************')]");
        createLog("After deleting card - count is : " + afterCardCount);

        if (afterCardCount < beforeCardCount) {
            createLog("successfully deleted payment method");
        } else {
            createErrorLog("Deleting payment method test failed");
            fail();
        }
    }
}
