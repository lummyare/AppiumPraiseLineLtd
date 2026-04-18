package v2update.subaru.android.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDataPrivacyPortalAndroid extends SeeTestKeywords {
    String testName = "Account Settings-Data Privacy Portal - Android";

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
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void dataConsentHomePageValidations() {
        sc.startStepsGroup("Data Privacy Portal Home Page Validations");
        dataConsentHomePage();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void masterDataConsentComponents() {
        sc.startStepsGroup("Data Privacy Portal - Master Data Consents");
        masterDataConsent();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void driveConnectTest() {
        sc.startStepsGroup("Data Privacy Portal - Drive Connect Master Data Consents");
        driveConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void remoteConnectTest() {
        sc.startStepsGroup("Data Privacy Portal - Remote Connect Master Data Consents");
        remoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void safetyConnectTest() {
        sc.startStepsGroup("Data Privacy Portal - Safety Connect Master Data Consents");
        safetyConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void serviceConnectTest() {
        sc.startStepsGroup("Data Privacy Portal - Service Connect Master Data Consents");
        serviceConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void acceptDeclineMasterDataConsentTest() {
        sc.startStepsGroup("Data Privacy Portal - Accept or Decline Master Data Consents");
        acceptDeclineMasterDataConsent();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void declineServiceConnectCommunicationTest() {
        sc.startStepsGroup("Data Privacy Portal - Accept Service Connect Communication");
        serviceConnectCommunicationConsent("Decline");
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void acceptServiceConnectCommunicationTest() {
        sc.startStepsGroup("Data Privacy Portal - Accept Service Connect Communication");
        serviceConnectCommunicationConsent("Accept");
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    public void privacyAndTermsOfUseTest() {
        sc.startStepsGroup("Data Privacy Portal - Connected Services Privacy and Terms of Use");
        privacyAndTerms();
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    public void signOutTest(){
        sc.startStepsGroup("Sign Out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void dataConsentHomePage() {
        navigateToDataPrivacyPortalScreen();
        sc.syncElements(5000, 20000);
        sc.swipe("Down", sc.p2cy(40), 2000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String [] dataConsent = new String[]{"2024 Solterra Limited", "Data Privacy Portal", "This Portal is an extension of our commitment to your privacy. You're in the driver seat when it comes to your data and the connected vehicle experience.",
                "Data Consent", "Connected Services Master Data Consent", "Required for most products and services", "Service Connect Communication",
                "Privacy", "Terms of Use", "To stop the sharing of data associated with all connected services and stop all transmissions of data by your vehicle, either decline the Connected Services Master Data Consent or press the SOS button in your vehicle."};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        sc.swipeWhileNotFound("Up", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@text='Connected Services Master Data Consent']", 0, 1000, 1, false);
    }

    public static void masterDataConsent() {
        if (!sc.isElementFound("NATIVE","xpath=//*[@text='Connected Services Master Data Consent']")) {
            reLaunchApp_android();
            navigateToDataPrivacyPortalScreen();
        }
        verifyElementFound("NATIVE","xpath=//*[@text='Connected Services Master Data Consent']",0);
        click("NATIVE", "xpath=//*[@text='Connected Services Master Data Consent']", 0, 1);
        sc.syncElements(2000, 5000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Data shared by service:']", 0);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String[] dataConsent = {"Master Data Consent", "Connected Services Master Data Consent", "Your consent to share data as described below provides you with access to an advanced suite of smart technologies designed to provide a safer and more customizable driving experience",
                "Drive Connect", "Remote Connect", "Safety Connect", "Service Connect", "Products that require Connected Services Master Data Consent",
                "Service Connect Communication"};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        sc.swipeWhileNotFound("Up", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@text='Destination Assist')]", 0, 1000, 1, false);
    }

    public static void driveConnect() {
        if (!sc.isElementFound("NATIVE","xpath=//*[@text='Drive Connect']")) {
            reLaunchApp_android();
            navigateToDataPrivacyPortalScreen();
            click("NATIVE", "xpath=//*[@text='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000,10000);
        }
        click("NATIVE","xpath=//*[@text='Drive Connect']",0,1);
        sc.syncElements(2000,5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String[] dataConsent = {"Data shared with Drive Connect", "Provides drivers with the tools to stay focused on the road ahead, such as a voice-first command system, up-to-date map coverage and 24hr access to live response center agents. Its features include",
                "Intelligent Assistant", "Cloud Navigation", "Destination Assist", "Driving", "Direction Vehicle Headed", "Location", "Location of Vehicle", "Customer Event-Driven Info", "Point of Interest Search", "Customer", "Name",
                "Address", "Email Address", "Phone Number", "Voice Samples", "Vehicle", "Vehicle Identification Number (VIN)", "Vehicle Make", "Vehicle Model", "Vehicle Year", "Declining Consents"};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(2000, 5000);
    }

    public static void remoteConnect() {
        if (!sc.isElementFound("NATIVE","xpath=//*[@text='Remote Connect']")) {
            reLaunchApp_android();
            navigateToDataPrivacyPortalScreen();
            click("NATIVE", "xpath=//*[@text='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000,10000);
        }
        sc.swipe("Down", sc.p2cy(40), 3000);
        click("NATIVE", "xpath=//*[@text='Remote Connect']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String[] dataConsent = {"Data shared with Remote Connect", "Provides subscribed drivers with a multiple array of convenience and connectivity features using", "smart watch as a companion to the smartphone, Toyota Action on Google Assistant, or Toyota Skill on Amazon Alexa enabled devices.",
                "Remote Start, Door Unlock/Lock", "Guest Driver Monitor", "Vehicle Alerts", "Vehicle Finder", "EV Charge Management (Select Vehicles Only)", "Digital Key (Select Vehicles Only)", "Driving",
                "Guest Driver Monitoring (When Activated)", "Location", "Location of Vehicle", "Location of Phone (Used for Electric Vehicles without EV Remote Connect)", "Vehicle",
                "Vehicle Status", "Door locked/unlocked, open/closed", "Window open/closed", "Hood open/closed", "Trunk open/closed", "Moonroof open/closed", "Trip Details (Trip A Miles/Trip B Miles)", "Climate Preferences (Select Vehicles Only)"};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(2000, 5000);
    }

    public static void safetyConnect() {
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@text='Service Connect']", 0, 1000, 1, false);
        if (!sc.isElementFound("NATIVE","xpath=//*[@text='Safety Connect']")) {
            reLaunchApp_android();
            navigateToDataPrivacyPortalScreen();
            click("NATIVE", "xpath=//*[@text='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000,10000);
            sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@text='Service Connect']", 0, 1000, 1, false);
        }
        sc.swipe("Down", sc.p2cy(40), 3000);
        click("NATIVE", "xpath=//*[@text='Safety Connect']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String[] dataConsent = {"Data shared with Safety Connect", "Provides drivers with a suite of services designed to assist in the event of an emergency:", "Automatic Collision Notification", "Emergency Assistance", "Enhanced Roadside Assistance",
                "Stolen Vehicle Locator", "Customer", "Name", "Address", "Phone Number", "Email Address", "Location",
                "Location of Vehicle", "Vehicle", "Vehicle Identification Number (VIN)", "Vehicle Make", "Vehicle Model", "Vehicle Year", "Vehicle Body Type", "Odometer", "Customer Event Driven Info", "Accident Photos, Text and Point of Impact", "Capture Emergency Phone Call", "Declining Consent"};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(2000, 5000);
    }

    public static void serviceConnect() {
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@text='Products that require Connected Services Master Data Consent']", 0, 1000, 1, false);
        if (!sc.isElementFound("NATIVE","xpath=//*[@text='Service Connect']")) {
            reLaunchApp_android();
            navigateToDataPrivacyPortalScreen();
            click("NATIVE", "xpath=//*[@text='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000,10000);
        }
        sc.swipe("Down", sc.p2cy(40), 3000);
        click("NATIVE", "xpath=//*[@text='Service Connect']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String[] dataConsent = {"Data shared with Service Connect", "Provides drivers with personalized vehicle health reports along with timely vehicle and maintenance alerts.",
                "Customer", "Name", "Email Address", "Telematics Subscription",
                "Vehicle Health", "Vehicle Electronics Control Unit (ECU) information", "Vehicle Diagnostic Trouble Codes (DTCs)",
                "Vehicle Warning and Maintenance Information", "Vehicle Freeze Frame Data", "Vehicle Health Report", "Declining Consent"};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(2000, 5000);
    }

    public void acceptDeclineMasterDataConsent() {
        if (!sc.isElementFound("NATIVE","xpath=//*[@id='data_consent_positive_button']")) {
            reLaunchApp_android();
            navigateToDataPrivacyPortalScreen();
            click("NATIVE", "xpath=//*[@text='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000,10000);
        }
        click("NATIVE","xpath=//*[@id='data_consent_positive_button']",0,1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Connected Services Master Data Consent')]", 0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Your vehicle wirelessly transmits location, driving, and vehicle health data')]",0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,' to direct and authorize Toyota and its affiliates to use your vehicle')]", 0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'In addition, you direct Toyota to provide Subaru with the location and vehicle health data for customer service and quality purposes in accordance with the Subaru Privacy Policy.')]",0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'To learn more, please review the Connected Services Privacy Notice and Connected Services Terms of Use.')]", 0);

        //Decline Consent Page
        click("NATIVE","xpath=//*[@id='combined_data_consent_negative_button']",0,1);
        sc.syncElements(2000,5000);
        sc.swipe("Down", sc.p2cy(50), 3000);
        actualText = sc.getText("NATIVE");
        createLog(actualText);
        String[] declineConsent = {"Are you sure? You will lose ALL services", "This is the Master Data Consent for Connected Services subscriptions and associated products and services. Without receiving your vehicle", "data, we cannot provide such subscriptions, products and services",
                "Important", "account to keep data transmission OFF. If you remove your vehicle from your account, by default the vehicle's data transmission capability will automatically be turned ON.", "Connected Services you'll lose", "Subscriptions", "Destination Assist", "Drive Connect",
                "Remote Connect", "Safety Connect", "Service Connect", "products and services", "Drive Pulse", "Service Connect Communication"};
        for (String detailsName : declineConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE","xpath=//*[@class='android.widget.ImageButton']",0,1);
        sc.syncElements(2000,5000);
        click("NATIVE","xpath=//*[@class='android.widget.ImageButton']",0,1);
        sc.syncElements(2000,5000);
        click("NATIVE","xpath=//*[@class='android.widget.ImageButton']",0,1);
        sc.syncElements(2000,5000);
    }

    public void serviceConnectCommunicationConsent(String expectedConsentStatus) {
        if(!sc.isElementFound("NATIVE","xpath=//*[@test='Connected Services Master Data Consent']")) {
            reLaunchApp_android();
            navigateToDataPrivacyPortalScreen();
        }
        sc.swipe("Down", sc.p2cy(40), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Service Connect Communication' and @onScreen='true']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='Service Connect Communication']", 0, 1000, 1, false);
        sc.syncElements(5000, 10000);
        click("NATIVE", "xpath=//*[@text='Service Connect Communication']", 0, 1);
        sc.syncElements(5000, 10000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String[] dataConsent = {"Service Connect Communication", "Provides your preferred servicing dealer with vehicle health and maintenance alerts allowing the dealership to communicate with you by email, cell phone call or text message using an automatic telephone dialing system for service appointments and service-related marketing via the method you choose and using the contact information you provide.",
                "Data Shared", "Customer", "Name", "Email Address", "Phone Number", "Telematics Subscription"
                , "Vehicle Health", "Vehicle Electronics Control Unit", "Vehicle Diagnostic Trouble Codes", "Vehicle Warning and Maintenance"
                , "Vehicle Freeze Frame Data", "Vehicle Health Report"};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE","xpath=//*[@id='data_consent_positive_button']",0,1);
        sc.syncElements(2000,5000);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'Service Connect Communication Consent')]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'you agree to share vehicle health data with Subaru Retailers for their own use and allow Subaru Retailers to communicate with you by email')]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@text,'To learn more, review the Connected Services Privacy Notice and Terms of Use')]",0);

        switch (expectedConsentStatus) {
            case ("Accept"):
                click("NATIVE","xpath=//*[@id='combined_data_consent_positive_button']",0,1);
                sc.syncElements(15000, 60000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Accepted' and (./preceding-sibling::* | ./following-sibling::*)[@text='Service Connect Communication']]", 0);
                break;
            case ("Decline"):
                click("NATIVE","xpath=//*[@id='combined_data_consent_negative_button']",0,1);
                sc.syncElements(5000, 10000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Are you sure you want to decline Service Connect Communication?']", 0);
                click("NATIVE", "xpath=//*[@id='data_consent_positive_button']", 0, 1);
                sc.syncElements(15000, 60000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Declined' and (./preceding-sibling::* | ./following-sibling::*)[@text='Service Connect Communication']]", 0);
                break;
            default:
                createErrorLog("Consent Status not given: "+expectedConsentStatus);
                break;
        }
    }

    public static void privacyAndTerms() {
        createLog("Started:Privacy and terms validations");
        if (!sc.isElementFound("NATIVE","xpath=//*[@text='Service Connect Communication']",0)) {
            reLaunchApp_android();
            navigateToDataPrivacyPortalScreen();
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(30),
                3000, "NATIVE", "xpath=//*[contains(@text,'To stop the sharing') and @onScreen='true']", 0, 1000, 5, false);
        verifyElementFound("NATIVE", "xpath=//*[@id='privacy_tex']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='terms_text']", 0);

        //Privacy Notice
        click("NATIVE", "xpath=//*[@id='privacy_tex']", 0, 1);
        sc.syncElements(3000, 30000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='terms_accept']")) {
            click("NATIVE", "xpath=//*[@id='terms_accept']", 0, 1);
            sc.syncElements(3000, 30000);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Continue')]"))
            click("NATIVE", "xpath=//*[contains(@text,'Continue')]", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'thanks')]"))
            click("NATIVE", "xpath=//*[contains(@text,'thanks')]", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Continue')]"))
            click("NATIVE", "xpath=//*[contains(@text,'Continue')]", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='terms_accept']")) {
            click("NATIVE", "xpath=//*[@id='terms_accept']", 0, 1);
            sc.syncElements(3000, 30000);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Continue')]"))
            click("NATIVE", "xpath=//*[contains(@text,'Continue')]", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]", 0);
        String urlText = sc.elementGetProperty("NATIVE","xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]",0,"text");
        createLog("URL text is: "+urlText);
        String urlText1 = "toyota.com";
        String urlText2 = "lexus.com";
        sc.report("Verify url contains expected url", urlText.contains(urlText1) || urlText.contains(urlText2));
        if(sc.isElementFound("NATIVE","xpath=//*[@id='close_button' or @content-desc='Close tab']"))
            click("NATIVE", "xpath=//*[@id='close_button' or @content-desc='Close tab']", 0, 1);
        else
            navigateToDataPrivacyPortalScreen();
        sc.swipeWhileNotFound("Down", sc.p2cy(30), 3000, "NATIVE", "xpath=//*[contains(@text,'To stop the sharing') and @onScreen='true']", 0, 1000, 5, false);
        //Terms of Use
        click("NATIVE", "xpath=//*[@id='terms_text']", 0, 1);
        sc.syncElements(3000, 30000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='terms_accept']")) {
            click("NATIVE", "xpath=//*[@id='terms_accept']", 0, 1);
            sc.syncElements(3000, 30000);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Continue')]"))
            click("NATIVE", "xpath=//*[contains(@text,'Continue')]", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'thanks')]"))
            click("NATIVE", "xpath=//*[contains(@text,'thanks')]", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Continue')]"))
            click("NATIVE", "xpath=//*[contains(@text,'Continue')]", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='terms_accept']")) {
            click("NATIVE", "xpath=//*[@id='terms_accept']", 0, 1);
            sc.syncElements(3000, 30000);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Continue')]"))
            click("NATIVE", "xpath=//*[contains(@text,'Continue')]", 0, 1);

        verifyElementFound("NATIVE", "xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]", 0);
        urlText = sc.elementGetProperty("NATIVE","xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]",0,"text");
        createLog("URL text is: "+urlText);
        urlText1 = "toyota.com";
        urlText2 = "lexus.com";
        sc.report("Verify url contains expected url", urlText.contains(urlText1) || urlText.contains(urlText2));

        if(sc.isElementFound("NATIVE","xpath=//*[@id='close_button' or @content-desc='Close tab']"))
            click("NATIVE", "xpath=//*[@id='close_button' or @content-desc='Close tab']", 0, 1);
        else
            navigateToDataPrivacyPortalScreen();
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(2000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Select a vehicle']")) {
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(2000, 20000);
        }
        click("NATIVE", "xpath=//*[@content-desc='Navigate up' or @class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(2000, 20000);
        sc.flickElement("NATIVE", "xpath=//*[@content-desc='Drag']", 0, "Down");
        createLog("Ended:Privacy and terms validations");
    }

    public static void navigateToDataPrivacyPortalScreen() {
        createLog("Started: Navigating to Data Privacy Portal Page");
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account']", 0);
        click("NATIVE", "xpath=//*[@text='Account']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='iv_profile_pic']", 0);
        sc.swipe("down", sc.p2cy(50), 5000);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@id='tv_legal' and @onScreen='true']", 0, 1000, 5, false);
        click("NATIVE", "xpath=//*[@id='tv_legal']", 0, 1);
        sc.syncElements(5000, 50000);
        sc.verifyElementFound("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0);
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Select a vehicle']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Select a vehicle']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='legal_description']", 0);
            sc.click("NATIVE", "xpath=(//*[@id='iv_arrow_image'])[1]", 0, 1);
            sc.syncElements(5000, 50000);
        }
        createLog("Ended: Navigate to Data Privacy Portal Page");
    }
}
