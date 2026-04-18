package v2update.subaru.ios.usa.accountSettings;

import com.ctp.Aeye;
import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import com.github.romankh3.image.comparison.model.Rectangle;
import io.appium.java_client.MobileElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruHelpAndFeedbackIOS extends SeeTestKeywords{

    static String testName = " Help and Feedback - IOS";

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
        iOS_Setup2_5(testName);
        sc.startStepsGroup("email SignIn 21MM");
        createLog("Start: 21mm email Login-USA-English");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
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
    public void manageYourPreferences21MM() {
        sc.startStepsGroup("Test - Manage Your Preferences - 21MM");
        validateManagePreferences();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void vehicleSupportSubaru21MM() {
        sc.startStepsGroup("Test - Vehicle Support Subaru - 21MM");
        vehicleSupportSubaru();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void signOutTest21MM() {
        sc.startStepsGroup("Test - Sign out 21MM");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void contactUsSubaru() {
        createLog("Started : Contact Us Subaru");

        //navigate to contact us
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 5000);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 5000);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(5000, 30000);
        sc.swipe("Down", sc.p2cy(60), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@id='ACC_SETTINGS_FEEDBACK_CELL']", 0);
        click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_FEEDBACK_CELL']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Contact Us']", 0);
        click("NATIVE", "xpath=//*[@id='Contact Us']", 0, 1);
        //sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='2022 NX 350 5 Door SUV 4X4 vehicle']", 0, 1000, 5, false);
        if(sc.isElementFound("NATIVE", "xpath=//*[@id='Call SOLTERRA CONNECT Customer Care']"))
            click("NATIVE", "xpath=//*[@id='Call SOLTERRA CONNECT Customer Care']", 0, 1);
        sc.syncElements(5000, 30000);
        // Use XPath to find the element with specific text, even if there are duplicates
        verifyElementFound("NATIVE", "xpath=(//*[contains(@label,'Call')])[1]", 0);
        String assistanceNumber = sc.elementGetText("NATIVE", "xpath=(//*[contains(@label,'Call')])[1]", 0);

        createLog(assistanceNumber);
        String assistanceNumberArr[] = assistanceNumber.split("all");
        String assistanceContactNumber = assistanceNumberArr[1].replaceAll(" ", "").replaceAll("[()-]*", "");
        createLog(assistanceContactNumber);
        //verify assistance contact number length
        sc.report("Verify assistance contact number length is 11", assistanceContactNumber.length() == 11);
        //verify assistance contact number
        sc.report("Verify assistance contact number ", assistanceContactNumber.equals("18004444195"));

        verifyElementFound("NATIVE", "xpath=(//*[@id='Cancel'])[1]", 0);
        click("NATIVE", "xpath=(//*[@id='Cancel'])[1]", 0, 1);

        createLog("Phone number verfied");
        createLog("Completed : Contact Us Subaru");
    }

    public static void vehicleSupportSubaru() {
        createLog("Started : Vehicle Support Lexus");

        if(!sc.isElementFound("NATIVE","xpath=//*[@id='Vehicle Support']")) {
            reLaunchApp_iOS();
            //navigate to FAQs
            navigateToHelpAndFeedbackScreen();
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='Vehicle Support']", 0);
        click("NATIVE", "xpath=//*[@id='Vehicle Support']", 0, 1);
        //select Subaru vehicle
        if(sc.isElementFound("NATIVE", "xpath=//*[@id='2023 Solterra Touring']"))
            click("NATIVE", "xpath=//*[@id='2023 Solterra Touring']", 0, 1);
        sc.syncElements(5000, 10000);

        //Recalls
        verifyElementFound("NATIVE", "xpath=(//*[@id='VEHICLE_DETAILS_TABLE_CELL_TITLE'])[1]", 0);
        click("NATIVE", "xpath=(//*[@id='VEHICLE_DETAILS_TABLE_CELL_TITLE'])[1]", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Health Report']", 0);
        click("NATIVE", "xpath=//*[@text='Back']",0,1);
        //Contact Dealer pending due to oneapp defects
        verifyElementFound("NATIVE", "xpath=(//*[@id='VEHICLE_DETAILS_TABLE_CELL_TITLE'])[2]", 0);
        click("NATIVE", "xpath=(//*[@id='VEHICLE_DETAILS_TABLE_CELL_TITLE'])[2]", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Preferred Dealer') or @label='Select Dealer']", 0);
        click("NATIVE", "xpath=//*[@text='Back']",0,1);
        createLog("Completed : Vehicle Support Subaru");
    }

    public static void validateManagePreferences() {
        createLog("Started : Validate Manage Preferences");

        if(!sc.isElementFound("NATIVE","xpath=//*[@id='Manage Your Preferences']")) {
            reLaunchApp_iOS();
            //navigate to FAQs
            navigateToHelpAndFeedbackScreen();
        }

        verifyElementFound("NATIVE","xpath=//*[@id='Manage Your Preferences']", 0);
        click("NATIVE", "xpath=//*[@id='Manage Your Preferences']", 0 ,1);
        sc.syncElements(15000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Address' and @class='UIAView']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='My Toyota & Lexus Communications Profile']",0);
        click("NATIVE", "xpath=//*[@id='Done' and @class='UIAButton']",0,1);
        click("NATIVE","xpath=//*[@id='Back']", 0,1);
        sc.syncElements(5000, 10000);
        click("NATIVE","xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0,1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        sc.syncElements(5000, 10000);
        createLog("Completed : Validate Manage Preferences");
    }

    public static void navigateToHelpAndFeedbackScreen() {
        createLog("Started : Navigation to Help And Feedback Screen");
        //navigate to FAQs
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 5000);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 5000);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(5000, 30000);
        sc.swipe("Down", sc.p2cy(60), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@id='ACC_SETTINGS_FEEDBACK_CELL']", 0);
        click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_FEEDBACK_CELL']", 0, 1);
        sc.syncElements(5000, 30000);
        createLog("Completed : Navigation to Help And Feedback Screen");
    }

}

