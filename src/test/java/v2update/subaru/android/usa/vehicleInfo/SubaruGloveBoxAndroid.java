package v2update.subaru.android.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruGloveBoxAndroid extends SeeTestKeywords {
    String testName = "GloveBox Android";

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
        android_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_Android("USA");
        selectionOfLanguage_Android("English");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: 17cy email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void capabilitiesTest() {
        sc.startStepsGroup("Capabilities");
        capabilities();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Sign Out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void capabilities() {
        createLog("Start:Capabilities");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@content-desc,'Capabilities')]")){
            reLaunchApp_android();
            navigateToVehicleGloveBoxPage();
            click("NATIVE", "xpath=//*[contains(@contentDescription,'Specs & Capabilities')]", 0, 1);
            sc.syncElements(2000, 4000);

        }

        String actualText = "";
        String descValues[] = sc.getAllValues("NATIVE", "xpath=//*[@class='android.view.View' and @content-desc]", "content-desc");
        for (String value : descValues) {
            actualText = actualText.concat(value);
        }
        sc.swipe("Down", sc.p2cy(60), 2000);

        descValues = sc.getAllValues("NATIVE", "xpath=//*[@class='android.view.View' and @content-desc]", "content-desc");
        for (String value : descValues) {
            actualText = actualText.concat(value);
        }

        String capabilitiesDetails[] = {"Wi-Fi Connect with Integrated Streaming", "Drive Connect", "Digital Key", "Remote Connect (features may vary)",
                "HD Radio", "Safety Connect", "Service Connect", "SiriusXM", "Android Auto", "Apple CarPlay"};
        for (String detailsName : capabilitiesDetails) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }

        //click on back button to display Manual
        verifyElementFound("NATIVE", "xpath=//*[@id='specs_and_capabilities_back_button']", 0);
        click("NATIVE", "xpath=//*[@id='specs_and_capabilities_back_button']", 0, 1);
        createLog("End:Capabilities");
    }

    public static void navigateToVehicleGloveBoxPage() {
        createLog("Navigating to Glovebox page");
        sc.swipeWhileNotFound("Down",sc.p2cy(50),1000,"NATIVE","xpath=//*[@text='Info']",0,1000,3,false);
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        click("NATIVE", "xpath=//*[@text='Info']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Glovebox')]", 0);
        click("NATIVE", "xpath=//*[contains(@text,'Glovebox')]", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Glovebox']", 0);
        createLog("Navigated to Glovebox page");
    }
}

