package v2update.subaru.android.canada.French.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruDealersCAFrenchAndroid extends SeeTestKeywords {
    String testName = "Dealers-Android";

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
                android_emailLoginFrench(ConfigSingleton.configMap.get("strEmailCAFrench"), ConfigSingleton.configMap.get("strPasswordCAFrench"));
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench(ConfigSingleton.configMap.get("strEmailCAFrench"), ConfigSingleton.configMap.get("strPasswordCAFrench"));
                sc.stopStepsGroup();
        }
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }


    @Test
    @Order(1)
    public void dealersTest() {
        sc.startStepsGroup("Dealers Under Find");
        validateDealers();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Open Vehicle Info']"))
            reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }


    public static void validateDealers() {
        createLog("Validate Dealers");
        sc.syncElements(5000, 8000);
        click("NATIVE", "xpath=//*[@text='Trouver']", 0, 1);
        sc.syncElements(5000, 8000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Concessionnaires']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Trouver un concessionnaire']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Concessionnaires']", 0);
        click("NATIVE", "xpath=//*[contains(@contentDescription,'Concessionnaires')]", 0, 1);
        sc.syncElements(5000, 8000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'"+convertTextToUTF8("Concessionnaire préféré")+"')]", 0);
        String preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@content-desc,'"+convertTextToUTF8("Concessionnaire préféré")+"')]/following::*[@class='android.view.View'])[1]", 0, "content-desc");
        createLog("Existing Preferred dealer is: "+preferredDealer);
        click("NATIVE", "xpath=//*[@contentDescription='"+convertTextToUTF8("Changer de concessionnaire préféré")+"']", 0, 1);
        sc.syncElements(5000, 30000);

        //Click Search Name to choose new preferred dealer
        sc.syncElements(5000, 30000);
       // verifyElementFound("NATIVE", "xpath=//*[@content-desc='Seleccionar concesionario'']", 0);
        click("NATIVE", "xpath=(//*[@content-desc='"+convertTextToUTF8("Sélectionnez un revendeur")+"']/*[@class='android.view.View'])[2]", 0, 1);
        //click("NATIVE", "xpath=(//*[@contentDescription='Select Dealer']/*[@class='android.view.View'])", 0, 1);
        sc.sendText("10019");
        click("NATIVE", "xpath=//*[@content-desc='Search' or @content-desc='Enter']", 0, 1);
        sc.syncElements(5000, 50000);
        //Dealer is not selectable issue exists
        click("NATIVE", "xpath=(//*[contains(@text,'Rechercher un nom')]/following::*[@class='android.view.View'])[8]", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='"+convertTextToUTF8("Concessionnaire préféré")+"']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='phone_small_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Toyota of Manhattan']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Appel']",0);
        verifyElementFound("NATIVE","xpath=//*[@content-desc='web_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Site Internet']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='detail_direction_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='instructions']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Heures de service']",0);
        click("NATIVE", "xpath=//*[@contentDescription='filter_plus_minimize']", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Lundi')]",0);
        click("NATIVE", "xpath=//*[@contentDescription='filter_plus_minimize']", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Prestations de service']",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[2]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'State Cert')]",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[2]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='"+convertTextToUTF8("Agréments")+"']",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[3]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'EV Charging Station')]",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[3]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Modes de paiement']",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[4]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Credit')]",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[4]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='"+convertTextToUTF8("Accessibilité")+"']",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[5]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Bilingual Staff')]",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[5]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Transport']",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[6]", 0, 1);
        sc.swipeWhileNotFound("DOWN", sc.p2cy(40), 2000, "NATIVE", "xpath=//*[@contentDescription='Pick Up/Delivery Options']", 0, 1000, 2, false);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Pick Up/Delivery Options']",0);
        click("NATIVE", "xpath=(//*[@contentDescription='filter_plus_minimize'])[5]", 0, 1);
        click("NATIVE", "xpath=//*[@contentDescription='remove_icon']", 0, 1);
    }
}