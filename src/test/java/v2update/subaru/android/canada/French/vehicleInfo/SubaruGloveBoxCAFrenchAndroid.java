package v2update.subaru.android.canada.French.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruGloveBoxCAFrenchAndroid extends SeeTestKeywords {

    String testName = "Subaru GloveBox Android";
    static String actualText = "";

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
    public void capabilities() {
        sc.startStepsGroup("Capabilities");
        validateCapabilities();
        sc.stopStepsGroup();
    }


    @Test
    @Order(5)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }



    public static void validateCapabilities(){
        createLog("Start:Capabilities");

        navigateToVehicleGloveBoxPage();
        click("NATIVE", convertTextToUTF8("//*[@text='Spécifications et capacités']"), 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@id='specs_and_capabilities_back_button']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Specs']", 0);

        String descValues[] = sc.getAllValues("NATIVE", "xpath=//*[@class='android.view.View' and @content-desc]", "content-desc");
        for (String value : descValues) {
            actualText = actualText.concat(value);
        }
        sc.swipe("Down", sc.p2cy(60), 2000);

        descValues = sc.getAllValues("NATIVE", "xpath=//*[@class='android.view.View' and @content-desc]", "content-desc");
        for (String value : descValues) {
            actualText = actualText.concat(value);
        }

        String capabilitiesDetails[] = {"Drive Connect","Clé numérique", "//*[@text='Remote Connect (les caractéristiques peuvent varier)']","HD Radio",
                "Safety Connect", "Service Connect", "SiriusXM", "Android Auto", "Apple Play",};
        for (String detailsName : capabilitiesDetails) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }

        //click on back button to display Manual
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Retour']", 0);
        click("NATIVE", "xpath=//*[@contentDescription='Retour']", 0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0);
        createLog("End:Capabilities");
    }

    public static void navigateToVehicleGloveBoxPage(){
        createLog("Navigating to Glovebox page");
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Info']"))) {
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        click("NATIVE", "xpath=//*[@text='Info']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='vehicle_info_glove_box_icon']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boite à gants']"), 0);
        createLog("Navigated to Glovebox page");
    }



}
