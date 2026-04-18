package v2update.subaru.ios.canada.french.bottomTab;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruBottomTabCAFrenchIOS extends SeeTestKeywords {
    String testName = "SubaruBottomTabCAFrench-IOS";

    @BeforeAll
    public void setup() throws Exception {
        iOS_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("email Login");
        createLog("Start: email Login");
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLoginFrench("subarucasol@gmail.com", "Test$1234");
        createLog("Completed: email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }


    @Test
    @Order(1)
    public void bottomTab(){
        sc.startStepsGroup("Bottom Tab IOS CA French");
        validateBottomTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void serviceTab(){
        sc.startStepsGroup("Service Tab IOS MX Spanish");
        validateServiceTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void payTab(){
        sc.startStepsGroup("Pay Tab IOS CA French");
        validatePayTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void homeTab(){
        sc.startStepsGroup("Home Tab IOS CA French");
        validateHomeTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void shopTab(){
        sc.startStepsGroup("Shop Tab IOS CA French");
        validateShopTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void findTab(){
        sc.startStepsGroup("Find Tab IOS CA French");
        validateFindTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void signOut() {
        sc.startStepsGroup("Signout");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateBottomTab(){
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@label='Service']"),0);
        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_serviceTab']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='IconService']",0);

        verifyElementFound("NATIVE",convertTextToUTF8("//*[@label='Payer']"),0);
        verifyElementFound("NATIVE","xpath=//*[@id='IconPay']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_payTab']",0);

        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0);

        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_shopTab']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='IconShop']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@label='Boutique']"),0);

        verifyElementFound("NATIVE","xpath=//*[@id='IconFind']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@label='Trouver']"),0);
        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_findTab']",0);
    }

    public static void validateServiceTab(){
        click("NATIVE",convertTextToUTF8("//*[@label='Service']"),0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@label='Programmer un entretien']"),0);
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
    }
    public static void validatePayTab(){
        click("NATIVE","xpath=//*[@label='Payer']",0,1);
        sc.syncElements(2000, 6000);

    }
    public static void validateHomeTab(){
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@label='Statut']"),0);
    }

    public static void validateShopTab(){
        click("NATIVE",convertTextToUTF8("//*[@label='Boutique']"),0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Gérer les abonnements']"),0);
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
    }
    public static void validateFindTab(){
        click("NATIVE","xpath=//*[@label='Trouver' and @id='BottomTabBar_findTab']",0,1);
        sc.syncElements(2000, 6000);
        verifyElementFound("NATIVE","xpath=//*[@label='Concessionnaires']",0);
    }
}
