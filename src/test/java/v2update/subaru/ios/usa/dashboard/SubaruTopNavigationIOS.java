package v2update.subaru.ios.usa.dashboard;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.CommonUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruTopNavigationIOS extends SeeTestKeywords {
    String testName = "EV - TopNavigation - IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                createLog("Started: Email Login");
                testName = System.getProperty("cloudApp") + testName;
                //App Login
                iOS_Setup2_5(testName);
                selectionOfCountry_IOS("USA");
                sc.startStepsGroup("email SignIn EV");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
                createLog("Ended: Email Login");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                iOS_Setup2_5(testName);
                sc.startStepsGroup("email SignIn EV");
                selectionOfCountry_IOS("USA");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
                sc.stopStepsGroup();
        }
    }
    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }
    @Test
    @Order(1)
    public void validateTopNavigationOnDashboard() {
        sc.startStepsGroup("Top Navigation on Dashboard");
        createLog("Start: Top Navigation on Dashboard");
        topMenuNavigationValidations();
        createLog("Ended: Top Navigation on Dashboard");
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOut() {
        sc.startStepsGroup("Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void topMenuNavigationValidations(){
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0)) {
            reLaunchApp_iOS();
        }
        //vehicle Name verification
        verifyElementFound("NATIVE", "xpath=//*[contains(@name,'vehicleName')]", 0);
        MobileElement elm = (MobileElement) driver.findElement(MobileBy.xpath("xpath=//*[contains(@name,'vehicleName')]"));
        createLog("Vehicle Name displayed as " + elm.getText());

        //Odometer verification
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Odometer')]", 0);
        createLog("Odometer text displayed");
        String odometerValue = sc.elementGetText("NATIVE", "xpath=//*[@id='DashboardHeader_odometer']", 0);
        createLog("Odometer Value: "+odometerValue+" displayed");
        String odometerValueStr = odometerValue.replace(",","");
        sc.report("Odometer value is Numeric value ", CommonUtils.isNumeric(odometerValueStr));
        sc.report("Odometer value is greater than 0 ", Integer.parseInt(odometerValueStr) > 0);
        //Verify units (mi/km) displayed
        verifyElementFound("NATIVE", "xpath=(//*[@id='DashboardHeader_odometer']/following-sibling::*)[1]", 0);
        String odometerUnit = sc.elementGetText("NATIVE", "xpath=(//*[@id='DashboardHeader_odometer']/following-sibling::*)[1]", 0);
        createLog("Odometer Unit displayed is: "+odometerUnit+"");
        sc.report("Verify Odometer Unit is mi ", odometerUnit.equalsIgnoreCase("mi"));

        //Vehicle Switcher icon verification
        verifyElementFound("NATIVE", "xpath=//*[@text='Double tap to open vehicle switcher!']", 0);
    }
}
