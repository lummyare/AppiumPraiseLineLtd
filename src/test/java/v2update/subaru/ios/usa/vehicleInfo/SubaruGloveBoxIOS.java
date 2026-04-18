package v2update.subaru.ios.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruGloveBoxIOS extends SeeTestKeywords {
    String testName = " - SubaruGloveBox-IOS";

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
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
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

        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@text='Glovebox']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Glovebox']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='globebox_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Specs']", 0);

        click("NATIVE", "xpath=//*[@text='Glovebox']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Glovebox' and @accessibilityLabel='TopNavBar_header']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='GloveboxScreen_vehicle_image']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Specs & Capabilities']", 0);

        click("NATIVE", "xpath=//*[@text='Specs & Capabilities']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Info' and @accessibilityLabel='TopNavBar_header']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Capabilities']", 0);
        click("NATIVE", "xpath=//*[@text='Capabilities']", 0, 1);
        sc.syncElements(5000, 30000);

        actualText = sc.getText("NATIVE");
        createLog(actualText);

        String capabilitiesDetails[] = {"Wi-Fi Connect with Integrated Streaming", "Drive Connect", "Digital Key", "Remote Connect (features may vary)",
                "HD Radio", "Safety Connect", "Service Connect", "SiriusXM® Satellite Audio", "Android Auto", "Apple CarPlay"};
        for (String detailsName : capabilitiesDetails) {
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
        verifyElementFound("NATIVE", "xpath=//*[@text='Glovebox' and @accessibilityLabel='TopNavBar_header']", 0);
        click("NATIVE", "xpath=//*[@text='Tap to go back']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Glovebox']", 0);
        click("NATIVE", "xpath=//*[@id='VehicleInfo_back_Btn']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        createLog("Verified vehicle capabilities");
    }
}
