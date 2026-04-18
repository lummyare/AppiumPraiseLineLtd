package v2update.subaru.android.canada.French.service;

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
public class SubaruRoadsideAssistanceCAFrenchAndroid extends SeeTestKeywords {

    public SubaruRoadsideAssistanceCAFrenchAndroid() {
        ConfigSingleton.INSTANCE.loadConfigProperties();
    }

    String testName = "RoadsideAssistance-Android";

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
    public void roadsideAssistanceCard(){
        sc.startStepsGroup("Roadside Assistance Card");
        verifyRoadsideAssistanceCard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='top_nav_profile_icon']"))
            reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void verifyRoadsideAssistanceCard(){
        createLog("Verifying Roadside Assistance card in service bottom bar");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Servicio']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Service']"),0);
        click("NATIVE",convertTextToUTF8("//*[@text='Service']"),0,1);
        sc.syncElements(5000, 30000);

        //Roadside Assistance section
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='RoadSide Assist Icon']",0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Obtenez de laide sur la route']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text, 'Assistance routière')]"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text, 'Assistance routière')]"), 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Appelez Toyota Roadside']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir FAQ']"), 0);
        sc.syncElements(5000, 30000);

        //See FAQ
        createLog("Verifying See FAQ in web page");
        click("NATIVE", convertTextToUTF8("//*[@text='Voir FAQ']"), 0,1);
        sc.syncElements(5000, 30000);
        String expectedURL = "support.toyota.com";
        String url = sc.elementGetProperty("NATIVE","xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]",0,"text");
        click("NATIVE","xpath=//*[@id='close_button' or @content-desc='Close tab']",0,1);

        //confirm number
        click("NATIVE","xpath=//*[@text='Appelez Toyota Roadside']",0,1);

        String expectedPhoneNumber = "1 (800) 444-4195";
        String phoneNumber = sc.elementGetProperty("NATIVE","xpath=//*[@id='digits']",0,"text");
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
    }
}