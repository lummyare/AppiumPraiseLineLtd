package v2update.subaru.android.canada.French.bottomTab;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruBottomTabCAFrenchAndroid extends SeeTestKeywords{
    String testName = "SubaruBottomTabCAFrench-Android";

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
    public void bottomTab(){
        sc.startStepsGroup("Bottom Tab Android CA Franch");
        validateBottomTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void serviceTab(){
        sc.startStepsGroup("Service Tab Android CA French");
        validateServiceTab();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    public void payTab(){
        sc.startStepsGroup("Pay Tab Android CA French");
        validatePayTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void homeTab(){
        sc.startStepsGroup("Home Tab Android CA French");
        validateHomeTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void shopTab(){
        sc.startStepsGroup("Shop Tab Android CA French");
        validateShopTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void findTab(){
        sc.startStepsGroup("Find Tab Android CA French");
        validateFindTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void signOut() {
        sc.startStepsGroup("Signout");
        android_SignOut();
        sc.stopStepsGroup();
    }
    public static void validateBottomTab(){
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@content-desc='Service']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Payer']"),0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Home Tab']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@content-desc='Boutique']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Trouver']"),0);
    }

    public static void validateServiceTab(){
        click("NATIVE",convertTextToUTF8("//*[@content-desc='Service']"),0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Prendre rendez-vous']"),0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Home Tab']",0);
    }

    public static void validatePayTab(){
        sc.syncElements(2000, 6000);
        click("NATIVE",convertTextToUTF8("//*[@contentDescription='Payer']"),0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Portefeuille']"),0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Home Tab']",0);
        sc.syncElements(2000, 6000);
    }
    public static void validateHomeTab(){
        click("NATIVE","xpath=//*[@contentDescription='Home Tab']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Statut']"),0);
    }
    public static void validateShopTab(){
        click("NATIVE",convertTextToUTF8("//*[@content-desc='Boutique']"),0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Gérer les abonnements']"),0);
        click("NATIVE","xpath=//*[@contentDescription='Home Tab']",0,1);
        sc.syncElements(2000, 6000);
    }

    public static void validateFindTab(){
        click("NATIVE",convertTextToUTF8("//*[@content-desc='Trouver']"),0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@text='Concessionnaires']",0);
        click("NATIVE","xpath=//*[@contentDescription='Home Tab']",0,1);
    }
}
