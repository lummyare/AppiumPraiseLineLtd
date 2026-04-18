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
public class SubaruConnectedServicesSupportCAFrenchAndroid extends SeeTestKeywords {
    String testName = "ConnectedServicesSupport-Android";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")){
            case(""):
                System.setProperty("cloudApp","subarustage");
                System.setProperty("platform","Android");
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
    public void connectedServicesTest(){
        sc.startStepsGroup("Test - Connected Services Support");
        connectedServicesSupport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void connectedServicesSupport() {
        createLog("Verifying Connected Services Support in vehicle info screen");
        if(sc.isElementFound("NATIVE",convertTextToUTF8("//*[@class='android.view.View' and (./preceding-sibling::* | ./following-sibling::*)[@text='Télécharger une Clé numérique'] and ./parent::*[@class='android.view.View']]"),0))
        {
            click("NATIVE", convertTextToUTF8("//*[@class='android.view.View' and (./preceding-sibling::* | ./following-sibling::*)[@text='Télécharger une Clé numérique'] and ./parent::*[@class='android.view.View']]"),0,1);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']//following-sibling::*[@class='android.widget.Button']", 0);
        click("NATIVE", "xpath=//*[@text='Info']//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Apoyo a servicios conectados']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Obtener apoyo']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_info_connected_service_icon']", 0);

        click("NATIVE", "xpath=//*[@text='Apoyo a servicios conectados']", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]", 0);
        String urlText = sc.elementGetText("NATIVE", "xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]", 0);
        createLog("URL text:" + urlText);
        if (urlText.contains("connectedtechnologysupport.com")) {
            createLog("Web URL contains connectedtechnologysupport.com");
            sc.report("Web URL contains connectedtechnologysupport.com", true);
        } else {
            createErrorLog("Web URL does not contains connectedservicessupport.com");
            sc.report("Web URL does not contains connectedservicessupport.com", false);
        }
        for (int i = 1; i < 3; i++) {
            sc.syncElements(1000, 2000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Apoyo a servicios conectados']")) {
                createLog("navigated back to vehicle info page");
                break;
            }
            else ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        }

        createLog("Verified Connected Services Support in vehicle info screen");
    }

}
