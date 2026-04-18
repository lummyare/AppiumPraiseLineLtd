package v2update.subaru.android.canada.French.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDrivePulseCAFrenchAndroid extends SeeTestKeywords {
    String testName ="DrivePulseMexicoSpanish-Android";

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
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void findTestLexus(){
        sc.startStepsGroup("Test - Find PR Spanish");
        validateFindTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void drivePulseTest(){
        sc.startStepsGroup("Test - Drive Pulse PR Spanish");
        validateDrivePulseAndTrips();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOut(){
        sc.startStepsGroup("Test - Sign out");
        if (!sc.isElementFound("NATIVE","xpath=//*[@text='Open Vehicle Info']")) {
            reLaunchApp_android();
        }
        android_SignOut();
        sc.stopStepsGroup();

    }

    public static void validateFindTab() {

        click("NATIVE","xpath=//*[@text='Trouver']",0,1);
        sc.syncElements(5000, 30000);
        if (sc.isElementFound("NATIVE","xpath=//*[@text='While using the app']")){
            click("NATIVE","xpath=//*[@text='While using the app']",0,1);
        }
        verifyElementFound("NATIVE","xpath=//*[@id='find_vehicle_location_button']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Concessionnaires']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Trouver un concessionnaire']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Destinations']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Favoris, récents et envoyés à la voiture']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Drive Pulse et trajets']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Drive Pulse et trajets récents']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Concessionnaires']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Destinations']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Drive Pulse et trajets']"),0);
    }

    public static void validateDrivePulseAndTrips() {

        click("NATIVE","xpath=//*[@text='Encontrar']",0,1);
        sc.syncElements(5000, 30000);

        sc.swipeWhileNotFound("Down",sc.p2cy(40),2000,"NATIVE","xpath=//*[@contentDescription='Drive Pulse y viajes']",0,1000,3,false);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Drive Pulse et trajets']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Drive Pulse et trajets récents']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@content-desc='Drive Pulse et trajets']"), 0);
        createLog("Clicking Drive Pulse & Trips link on dashboard");
        click("NATIVE","xpath=//*[@text='Drive Pulse et trajets']",0,1);
        sc.syncElements(10000, 30000);
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@content-desc='Voyages']"))) {
            createLog("Drive Pulse & Trips screen is displayed");
        } else {createErrorLog("Drive Pulse & Trips screen is not displayed");}

        //Verify Trips
        createLog("Verifying Trips screen");
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Se désengager']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Voyages']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Voyage A']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Voyage B']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@content-desc='Les voyages récents ne sont pas encore disponibles']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(@contentDescription,'activation pour obtenir ces informations lors de votre premier voyage.')]"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@content-desc='Ces informations ne seront disponibles que pour vous. Nous ne partagerons jamais vos informations.']"),0);


        //Back to Dashboard
        sc.click("NATIVE", "xpath=//*[@contentDescription='back_button']", 0, 1);
        sc.click("NATIVE", "xpath=//*[./*[./*[@contentDescription='Home Tab']]]", 0, 1);
        createLog("Verified Drive Pulse & Trips screen");
    }
}
