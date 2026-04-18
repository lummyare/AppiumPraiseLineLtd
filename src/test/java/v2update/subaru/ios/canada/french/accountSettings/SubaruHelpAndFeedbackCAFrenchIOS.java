package v2update.subaru.ios.canada.french.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruHelpAndFeedbackCAFrenchIOS extends SeeTestKeywords{

    static String testName = "HelpAndFeedbackCAFrench-IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                testName = System.getProperty("cloudApp") + testName;
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                break;
        }
        iOS_Setup2_5(testName);
        sc.startStepsGroup("email SignIn 21MM");
        createLog("Start: 21mm email Login- Canada Subaru Next Gen");
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLogin("subarustageca@mail.tmnact.io","Test$123");
//      ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("End: 21mm email Login-USA-English");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }


    @Test
    @Order(1)
    public void contactUsSubaru21MM() {
        sc.startStepsGroup("Contact Us Subaru - 21MM");
        contactUsSubaru();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void vehicleSupportSubaru21MM() {
        sc.startStepsGroup("Test - Vehicle Support Subaru - 21MM");
        vehicleSupportSubaru();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest21MM() {
        sc.startStepsGroup("Test - Sign out 21MM");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void contactUsSubaru() {
        createLog("Started : Contact Us Lexus");

        //navigate to contact us
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Nous joindre']")) {
            reLaunchApp_iOS();
            //navigate to FAQs
            navigateToHelpAndFeedbackScreen();
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Nous joindre']", 0);
        click("NATIVE", "xpath=//*[@text='Nous joindre']", 0, 1);
        sc.syncElements(5000, 10000);
        //select lexus vehicle
        if (sc.isElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@label='Sélectionner un véhicule'])[1]"))) {
            createLog("select vehicle screen is displayed");
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[contains(@text,'Véhicule')])[1]"), 0);
            click("NATIVE", convertTextToUTF8("xpath=(//*[contains(@text,'Véhicule')])[1]"), 0, 1);
            sc.syncElements(15000, 60000);
        }
        sc.syncElements(5000, 10000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Nous joindre' or @text='NOUS JOINDRE']", 0);

        //click and verify Solterra Connect Customer Care
        verifyElementFound("NATIVE", "xpath=//*[@text='Appeler Service clientèle Subaru']", 0);
        click("NATIVE", "xpath=//*[@text='Appeler Service clientèle Subaru']", 0, 1);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@label,'Call')])[1]", 0);
        String assistanceNumber = sc.elementGetText("NATIVE", "xpath=(//*[contains(@label,'Call')])[1]", 0);
        createLog(assistanceNumber);
        String assistanceNumberArr[] = assistanceNumber.split("all");
        String assistanceContactNumber = assistanceNumberArr[1].replaceAll(" ", "").replaceAll("[()-]*", "");
        createLog(assistanceContactNumber);

        //verify Solterra Connect Customer Care contact number length
        sc.report("Verify assistance contact number length is 11", assistanceContactNumber.length() == 11);

        //verify Solterra Connect Customer contact number
        sc.report("Verify assistance contact number ", assistanceContactNumber.equals("18002638802"));
        verifyElementFound("NATIVE", "xpath=(//*[@id='Cancel'])[1]", 0);
        click("NATIVE", "xpath=(//*[@id='Cancel'])[1]", 0, 1);
        createLog("Completed : Contact Us Subaru");
    }

    public static void vehicleSupportSubaru() {
        createLog("Started : Vehicle Support Lexus");

        if (!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Soutien relatif au véhicule']"))) {
            reLaunchApp_iOS();
            //navigate to FAQs
            navigateToHelpAndFeedbackScreen();
        }
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Soutien relatif au véhicule']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Soutien relatif au véhicule']"), 0, 1);
        sc.syncElements(5000, 10000);
        //select lexus vehicle
        if (sc.isElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@label='Sélectionner un véhicule'])[1]"))) {
            createLog("select vehicle screen is displayed");
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[contains(@text,'Véhicule')])[1]"), 0);
            click("NATIVE", convertTextToUTF8("xpath=(//*[contains(@text,'Véhicule')])[1]"), 0, 1);
            sc.syncElements(15000, 60000);
        }
        sc.syncElements(5000, 10000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Soutien relatif au véhicule' or @text='SOUTIEN RELATIF AU VÉHICULE']"), 0);

        //Recalls
        verifyElementFound("NATIVE", "xpath=(//*[@id='VEHICLE_DETAILS_TABLE_CELL_TITLE'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@id='VEHICLE_DETAILS_TABLE_CELL_TITLE'])[1]", 0);
        click("NATIVE", "xpath=(//*[@id='VEHICLE_DETAILS_TABLE_CELL_TITLE'])[1]", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE' and (@text='Bilan de santé de votre véhicule' or @text='BILAN DE SANTÉ DE VOTRE VÉHICULE')]"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@id='Bilan de santé de votre véhicule' or @id='BILAN DE SANTÉ DE VOTRE VÉHICULE']"), 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);

        //Contact Dealer
        //Defect in v2_5 https://toyotaconnected.atlassian.net/browse/OAD01-14418
        verifyElementFound("NATIVE", "xpath=(//*[@id='VEHICLE_DETAILS_TABLE_CELL_TITLE'])[4]", 0);
        click("NATIVE", "xpath=(//*[@id='VEHICLE_DETAILS_TABLE_CELL_TITLE'])[4]", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Concessionnaire préféré']"), 0);
        click("NATIVE", "xpath=//*[@label='remove_icon']", 0, 1);

    }

    public static void navigateToHelpAndFeedbackScreen() {
        createLog("Started : Navigation to Help And Feedback Screen");
        reLaunchApp_iOS();
        //navigate Help and Feedback
        sc.syncElements(2000, 10000);
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Se déconnecter']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(4000, 20000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Réglages du compte' or @text='RÉGLAGES DU COMPTE']"), 0);
        sc.swipe("Down", sc.p2cy(60), 2000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Aide et rétroaction']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='ACC_SETTINGS_FEEDBACK_CELL']", 0);
        click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_FEEDBACK_CELL']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Aide et rétroaction' or @text='AIDE ET RÉTROACTION']"), 0);
        createLog("Completed : Navigation to Help And Feedback Screen");
    }
}
