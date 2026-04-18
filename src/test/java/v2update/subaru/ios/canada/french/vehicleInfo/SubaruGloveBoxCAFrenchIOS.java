package v2update.subaru.ios.canada.french.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruGloveBoxCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruGloveBoxCAFrench-IOS";

    static String actualText = "";

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

    //Subaru - Only vehicle capabilities
    @Test
    @Order(1)
    public void vehicleCapabilitiesTest() {
        sc.startStepsGroup("Test - Vehicle Capabilities");
        validateVehicleCapabilities();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateVehicleCapabilities() {
        createLog("Verifying Vehicle capabilities");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE", "xpath=//*[@label='Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(5000, 30000);

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0, 1000, 5, false);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='globebox_icon']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Spécifications']"), 0);

        click("NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boite à gants' and @accessibilityLabel='TopNavBar_header']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='GloveboxScreen_vehicle_image']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Spécifications et capacités']"), 0);

        click("NATIVE", convertTextToUTF8("//*[@text='Spécifications et capacités']"), 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Informations sur le véhicule' and @accessibilityLabel='TopNavBar_header']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Capacités']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Capacités']"), 0, 1);
        sc.syncElements(5000, 30000);

        actualText = sc.getText("NATIVE");
        createLog(actualText);

        String capabilitiesDetails[] = {"Wi-Fi Connect with Integrated Streaming", "Drive Connect", "Clé numérique", "Remote Connect (les caractéristiques peuvent varier)",
                "HD Radio", "Safety Connect", "Service Connect", "SiriusXM® Satellite Audio", "Android Auto", "Apple CarPlay"};
        for (String detailsName : capabilitiesDetails) {
            detailsName = convertTextToUTF8(detailsName);
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }

        //click on back button to display Manuals
        verifyElementFound("NATIVE", "xpath=//*[@text='Back']", 0);
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        sc.syncElements(2000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boite à gants' and @accessibilityLabel='TopNavBar_header']"), 0);
        click("NATIVE", "xpath=//*[@text='Tap to go back']", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0);
        click("NATIVE", "xpath=//*[@id='VehicleInfo_back_Btn']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        createLog("Verified vehicle capabilities");
    }
}
