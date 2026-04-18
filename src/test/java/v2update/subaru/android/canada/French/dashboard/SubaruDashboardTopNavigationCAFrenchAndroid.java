package v2update.subaru.android.canada.French.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class SubaruDashboardTopNavigationCAFrenchAndroid extends SeeTestKeywords {

    String testName = "Dashboard Top Navigation CA-French- Android";

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
    public void topNavigationFor21mm(){
        sc.startStepsGroup("Top Navigation on Dashboard-French");
        topMenuNavigationValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    public void signOut21mm() {
        sc.startStepsGroup("Sign out-21mm-French");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void topMenuNavigationValidations(){
        createLog("Start: Top Navigation on Dashboard-French");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@text,'"+convertTextToUTF8("Odomètre")+"')]",0)){
            reLaunchApp_android();
        }
        //Odometer verification
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'"+convertTextToUTF8("Odomètre")+"')]",0);
        //vehicle Name verification
        createLog("Verify vehicle Name displayed");
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'"+convertTextToUTF8("Odomètre")+"')]//preceding-sibling::*[contains(@class,'TextView')]",0);
        String odometerText=sc.elementGetText("NATIVE","xpath=//*[contains(@text,'"+convertTextToUTF8("Odomètre")+"')]",0);
        createLog("Odometer text:"+odometerText);
        createLog("miles displayed for odometer text:"+odometerText.split("mi")[0].split(convertTextToUTF8("Odomètre"))[1]);
        //Vehicle Switcher icon verification
        createLog("Verify Vehicle Switcher Entry point (Down Arrow) displayed");
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'"+convertTextToUTF8("Odomètre")+"')]//following-sibling::*[@class='android.view.View']",0);
        createLog("Ended: Top Navigation on Dashboard-French");
    }

}
