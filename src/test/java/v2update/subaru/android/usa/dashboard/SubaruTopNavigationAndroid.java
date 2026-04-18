package v2update.subaru.android.usa.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static v2update.android.usa.accountSettings.DarkLightModeAndroid.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruTopNavigationAndroid extends SeeTestKeywords {
    String testName="Dashboard Top Navigation - Android";

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
    public void topNavigationTest() {
        sc.startStepsGroup("Top Navigation on Dashboard");
        topNavigation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOut() {
        sc.startStepsGroup("Sign Out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void topNavigation() {
        createLog("Start: Top Navigation on Dashboard");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']",0)){
            reLaunchApp_android();
        }
        //Odometer verification
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Odometer')]",0);
        //vehicle Name verification
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Odometer')]//preceding-sibling::*[contains(@class,'TextView')]",0);
        String odometerText=sc.elementGetText("NATIVE","xpath=//*[contains(@text,'Odometer')]",0);
        String odometerText1=sc.elementGetProperty("NATIVE","xpath=//*[contains(@text,'Odometer')]",0,"text");
        createLog("Odometer text:"+odometerText);
        createLog("miles displayed for odometer text:"+odometerText.split("mi")[0].split("Odometer")[1]);
        //Vehicle Switcher icon verification
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Odometer')]//following-sibling::*[@class='android.view.View']",0);
        createLog("Ended: Top Navigation on Dashboard");
    }

    public static void chevronValidationLightMode() throws IOException {
        //check vehicle switcher chevron color
        darkMode();
        sc.flickElement("NATIVE", "xpath=//*[@contentDescription='Drag']", 0, "Down");
        sc.syncElements(2000,5000);
        String actualColor = darkColorValidation("//*[@contentDescription='tap to open vehicle switcher']");
        Assertions.assertEquals("black",actualColor);

    }

    public static void chevronValidationDarkMode() throws IOException {
        lightMode();
        sc.flickElement("NATIVE", "xpath=//*[@contentDescription='Drag']", 0, "Down");
        sc.syncElements(2000,5000);
        String actualColor = darkColorValidation("//*[@contentDescription='tap to open vehicle switcher']");
        Assertions.assertEquals("white",actualColor);
    }

}
