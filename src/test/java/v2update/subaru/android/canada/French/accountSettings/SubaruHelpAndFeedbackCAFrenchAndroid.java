package v2update.subaru.android.canada.French.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruHelpAndFeedbackCAFrenchAndroid extends SeeTestKeywords {
    String testName = "SubaruHelpAndFeedbackCAFrench - Android";

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
    public void exit(){
        exitAll(this.testName);
    }


    @Test
    @Order(1)
    public void contactUsTest() {
        sc.startStepsGroup("Contact Us");
        contactUs();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    public void vehicleSupportTest() {
        sc.startStepsGroup("Vehicle Support");
        vehicleSupport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void contactUs() {
        createLog("Started: Contact Us Subaru");
        navigateToHelpAndFeedBackScreen();
        //Customer Care
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Nous joindre']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Appelez concernant tout autre problème']"), 0);
        click("NATIVE", "xpath=//*[@id='tv_contact_us_arrow']", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Nous joindre']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Sélectionner un véhicule']"), 0);
        click("NATIVE", "xpath=//*[@id='iv_arrow_image' and (./preceding-sibling::* | ./following-sibling::*)[@text='2023 Solterra Touring']]", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Nous joindre']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='SOLTERRA CONNECT Customer Care']"), 0);

        createLog("Ended: Contact Us Subaru");
    }

    public static void vehicleSupport() {
        createLog("Started: Support Lexus");
        if(!sc.isElementFound("NATIVE", "xpath=//*[@text='Soutien relatif au véhicule']",0)) {
            reLaunchApp_android();
            navigateToHelpAndFeedBackScreen();
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Soutien relatif au véhicule']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Aide pour les questions ou problèmes liés à votre compte']"), 0);
        click("NATIVE", "xpath=//*[@id='tv_vehicle_support_arrow']", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Soutien relatif au véhicule']"), 0);
        click("NATIVE", "xpath=//*[@id='iv_arrow_image' and (./preceding-sibling::* | ./following-sibling::*)[@text='2023 Solterra Touring']]", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Soutien relatif au véhicule']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='iv_car_image']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Contacter le concessionnaire']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Contacter le concessionnaire']"), 0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Concessionnaire préféré']"), 0);

        createLog("Ended: Support Lexus");
    }

    public static void navigateToHelpAndFeedBackScreen() {
        createLog("Started : Navigation to Linked Accounts Screen");
        verifyElementFound("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(5000, 30000);
        click("NATIVE", "xpath=//*[@text='Compte']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Aide et rétroaction']"),0);
        verifyElementFound("NATIVE","xpath=//*[@id='iv_feedback']",0);
        click("NATIVE",convertTextToUTF8("//*[@text='Aide et rétroaction']"),0,1);
        sc.syncElements(5000, 60000);
        //Linked Accounts screen
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Aide et rétroaction']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Revenir en arrière']"),0);
        createLog("Completed : Navigation to Help and Feedback Screen");
    }
}
