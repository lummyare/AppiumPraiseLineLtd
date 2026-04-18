package v2update.subaru.ios.canada.french.dashboard;

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

public class SubaruTopNavigationCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruTopNavigationCAFrench-IOS";

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
        ios_emailLoginFrench("subarunextgen3@gmail.com","Test$12345");
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
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
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Odomètre')]"), 0);
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
