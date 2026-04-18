package v2update.subaru.ios.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDataPrivacyPortalIOS extends SeeTestKeywords {
    String testName = "Account Settings - Data Privacy Portal - IOS";

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
        iOS_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);;
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void _DataConsent() throws Exception {
        DataConsent();
    }
    public static void DataConsent() throws Exception {
        sc.startStepsGroup("Data Privacy Portal - Data Consents");
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Announcements']"))
            sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
        navigateToDataPrivacyPortal();
        sc.syncElements(5000, 20000);
        sc.swipe("Down", sc.p2cy(40), 2000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[];
        dataConsent = new String[]{"2024 Solterra Limited", "Data Privacy Portal", "This Portal is an extension of our commitment to your privacy. You're in the driver seat when it comes to your data and the connected vehicle experience.",
                "Data Consent", "Connected Services Master Data Consent", "Required for most products and services", "Drive Pulse", "External Vehicle Video Capture", "Service Connect Communication",
                "Privacy", "Terms of Use", "To stop the sharing of data associated with all connected services and stop all transmissions of data by your vehicle, either decline the Connected Services Master Data Consent or press the SOS button in your vehicle."};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
            sc.swipeWhileNotFound("Up", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']", 0, 1000, 1, false);
            sc.stopStepsGroup();
        }
    }


    @Test
    @Order(2)
    public void _MasterDataConsent() {
        MasterDataConsent();
    }
    public static void MasterDataConsent() {
        sc.startStepsGroup("Data Privacy Portal - Master Data Consents");
        if (!sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
        }
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Accepted' and (./preceding-sibling::* | ./following-sibling::*)[@id='Required for most products and services']]", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']", 0, 1);
        sc.syncElements(2000, 5000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Data shared by service:' and @class='UIAStaticText']", 0);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[];

        dataConsent = new String[]{"Master Data Consent", "Connected Services Master Data Consent", "Your consent to share data as described below provides you with access to an advanced suite of smart technologies designed to provide a safer and more customizable driving experience",
                "Destination Assist", "Remote Connect", "Safety Connect", "Service Connect", "Products that require Connected Services Master Data Consent",
                "Drive Pulse", "External Vehicle Video Capture", "Service Connect Communication"};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }

        sc.swipeWhileNotFound("Up", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[contains(@id,'Destination Assist')]", 0, 1000, 1, false);
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void _DriveConnect(){
        DriveConnect();
    }
    public static void DriveConnect() {
        sc.startStepsGroup("Data Privacy Portal - Drive Connect");
        if (!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Drive Connect')]")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
            click("NATIVE", "xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000, 10000);
        }
        //Drive Connect
        click("NATIVE", "xpath=//*[contains(@id,'Drive Connect')]", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[] = {"Data shared with Drive Connect", "Provides drivers with the tools to stay focused on the road ahead, such as a voice-first command system, up-to-date map coverage and 24hr access to live response center agents. Its features include",
                "Intelligent Assistant", "Cloud Navigation", "Destination Assist", "Driving", "Direction Vehicle Headed", "Location", "Location of Vehicle", "Customer Event-Driven Info", "Point of Interest Search", "Customer", "Name",
                "Address", "Email Address", "Phone Number", "Voice Samples", "Vehicle", "Vehicle Identification Number (VIN)", "Vehicle Make", "Vehicle Model", "Vehicle Year", "Declining Consents"};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void _RemoteConnect(){
        RemoteConnect();
    }
    public static void RemoteConnect() {
        sc.startStepsGroup("Data Privacy Portal - Remote Connect");
        if (!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Remote Connect')]")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
            click("NATIVE", "xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000, 10000);
        }
        //Remote Connect
        sc.swipe("Down", sc.p2cy(40), 3000);
        click("NATIVE", "xpath=//*[contains(@id,'Remote Connect')]", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[] = {"Data shared with Remote Connect", "Provides subscribed drivers with a multiple array of convenience and connectivity features using", "smart watch as a companion to the smartphone, Toyota Action on Google Assistant, or Toyota Skill on Amazon Alexa enabled devices.",
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
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void _SafetyConnect(){
        SafetyConnect();
    }
    public static void SafetyConnect() {
        sc.startStepsGroup("Data Privacy Portal - Safety Connect");
        //Safety Connect
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@id='Service Connect']", 0, 1000, 1, false);
        if (!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Safety Connect')]")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
            click("NATIVE", "xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000, 10000);
            sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@id='Service Connect']", 0, 1000, 1, false);
        }
        sc.swipe("Down", sc.p2cy(40), 3000);
        click("NATIVE", "xpath=//*[contains(@id,'Safety Connect')]", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[] = {"Data shared with Safety Connect", "Provides drivers with a suite of services designed to assist in the event of an emergency:", "Automatic Collision Notification", "Emergency Assistance", "Enhanced Roadside Assistance",
                "Stolen Vehicle Locator", "Customer", "Name", "Address", "Phone Number", "Email Address", "Location",
                "Location of Vehicle", "Vehicle", "Vehicle Identification Number (VIN)", "Vehicle Make", "Vehicle Model", "Vehicle Year", "Vehicle Body Type", "Odometer", "Customer Event Driven Info", "Accident Photos, Text and Point of Impact", "Capture Emergency Phone Call", "Declining Consent"};
        for (String detailsName : dataConsent) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void _ServiceConnect(){
        ServiceConnect();
    }
    public static void ServiceConnect() {
        sc.startStepsGroup("Data Privacy Portal - Service Connect");
        //Service Connect
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@id='Products that require Connected Services Master Data Consent' and @class='UIAStaticText']", 0, 1000, 1, false);
        if (!sc.isElementFound("NATIVE","xpath=//*[@id='Service Connect']")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
            click("NATIVE", "xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000, 10000);
        }
        sc.swipe("Down", sc.p2cy(40), 3000);
        click("NATIVE", "xpath=//*[@id='Service Connect']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[] = {"Data shared with Service Connect", "Provides drivers with personalized vehicle health reports along with timely vehicle and maintenance alerts.",
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
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void _AcceptDeclineMasterDataConsentValidation(){
        AcceptDeclineMasterDataConsentValidation("Subaru");
    }
    public static void AcceptDeclineMasterDataConsentValidation(String strVehicleBrand) {
        sc.startStepsGroup("Data Privacy Portal - Accept Decline Master Data Consent validation");
        if (!sc.isElementFound("NATIVE","xpath=//*[@id='Manage Consent' and @class='UIAButton']")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
            click("NATIVE", "xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']", 0, 1);
            sc.syncElements(2000, 10000);
        }
        //Consent
        verifyElementFound("NATIVE", "xpath=//*[@id='Manage Consent' and @class='UIAButton']", 0);
        click("NATIVE", "xpath=//*[@id='Manage Consent' or @id='MANAGE CONSENT' and @class='UIAButton']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Master Data Consent']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'When you click the \"Accept\" button, you agree that on a regular and continuous basis, your vehicle wirelessly transmits location, driving and vehicle health data to "+strVehicleBrand+" and its affiliates in order to deliver Connected Services and for internal research, development and data analysis.')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='To learn more, review the Connected Services Privacy Notice and Terms of Use.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Privacy Notice']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Terms of Use']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'For the purpose of providing Cloud Navigation services, if you perform a search for a POI (Point of interest), "+strVehicleBrand+" will provide Google with the location coordinates and criteria associated with the search request for the purpose of providing a relevant POI as a response. Only the originating location of your search and criteria for your search are shared for this purpose and the information provided by "+strVehicleBrand+" is not associated with you or your vehicle. This information is subject to Google') and contains(@id,'Privacy Policy and Terms of Service.')]", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[(contains(@label,'To disable your vehicle') and contains(@label,'data transmission capability, press, \"Decline\"')) and @visible='true']", 0, 1000, 5, false);
        click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_DECLINE']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.swipe("Down", sc.p2cy(50), 3000);
        actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent1[] = {"Are you sure? You will lose ALL services", "This is the Master Data Consent for Connected Services subscriptions and associated products and services. Without receiving your vehicle", "data, we cannot provide such subscriptions, products and services",
                "Important", "account to keep data transmission OFF. If you remove your vehicle from your account, by default the vehicle's data transmission capability will automatically be turned ON.", "Connected Services you'll lose", "Subscriptions", "Destination Assist", "Drive Connect",
                "Remote Connect", "Safety Connect", "Service Connect", "products and services", "Drive Pulse", "Service Connect Communication"};
        for (String detailsName : dataConsent1) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(2000, 5000);
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void _DeclineServiceConnectCommunication(){
        DeclineServiceConnectCommunication();
    }
    public static void DeclineServiceConnectCommunication() {
        if (!sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
        }
        sc.startStepsGroup("Data Privacy Portal - Decline Services Connect Communication");
        sc.swipe("Down", sc.p2cy(40), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Service Connect Communication' and @visible='true']", 0);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='Service Connect Communication']", 0, 1000, 1, false);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Accepted' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='Service Connect Communication']]", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='Service Connect Communication']", 0, 1);
        sc.syncElements(5000, 10000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent[] = {"Service Connect Communication", "Provides your preferred servicing dealer with vehicle health and maintenance alerts allowing the dealership to communicate with you by email, cell phone call or text message using an automatic telephone dialing system for service appointments and service-related marketing via the method you choose and using the contact information you provide.",
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
        click("NATIVE", "xpath=//*[@id='Manage Consent' or @id='MANAGE CONSENT' and @class='UIAButton']", 0, 1);
        sc.syncElements(5000, 10000);
        sc.swipe("Down", sc.p2cy(40), 3000);
        actualText = sc.getText("NATIVE");
        createLog(actualText);
        String dataConsent1[] = {"Service Connect Communication Consent", "(Requires Connected Services)",
                "By accepting Service Connect Communication, you agree to share vehicle health data with your dealer for its own use and to allow them to contact you by email, cell phone call or text message using an automated telephone dialing system for service appointments and service-related marketing at the number you provided."};
        for (String detailsName : dataConsent1) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_DECLINE']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
        click("NATIVE", "xpath=//*[@id='Decline Consent' or @id='DECLINE CONSENT' and @class='UIAButton']", 0, 1);
        sc.syncElements(15000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Declined' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='Service Connect Communication']]", 0);
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    public void _AcceptServiceConnectCommunication(){
        AcceptServiceConnectCommunication();
    }
    public static void AcceptServiceConnectCommunication() {
        sc.startStepsGroup("Data Privacy Portal - Accept Services Connect Communication");
        acceptDataConsent("Service Connect Communication");
        sc.stopStepsGroup();
    }



    @Test
    @Order(10)
    public void _PrivacyAndTermsOfUse(){
        PrivacyAndTermsOfUse();
    }
    public static void PrivacyAndTermsOfUse() {
        sc.startStepsGroup("Data Privacy Portal - Connected Services Privacy and Terms of Use");
        if (!sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='Connected Services Master Data Consent']")) {
            reLaunchApp_iOS();
            navigateToDataPrivacyPortal();
        }
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_TERMS_OF_USE_CELL' and @visible='true']", 0, 1000, 1, false);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_PRIVACY_NOTICE_CELL']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_TERMS_OF_USE_CELL']", 0);
        //Privacy Notice
        click("NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_PRIVACY_NOTICE_CELL']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@text='Required for most products and services']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT' and ./parent::*[(./preceding-sibling::* | ./following-sibling::*)[@class='UIAWebView']]]", 0, 1);
        sc.syncElements(5000, 10000);
        //Terms of Use
        click("NATIVE", "xpath=//*[@accessibilityLabel='EDIT_DATA_CONSENTS_TERMS_OF_USE_CELL']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@text='Required for most products and services']", 0);
        //     verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE' and ./parent::*[(./preceding-sibling::* | ./following-sibling::*)[@class='UIAWebView']]]", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT' and ./parent::*[(./preceding-sibling::* | ./following-sibling::*)[@class='UIAWebView']]]", 0, 1);
        sc.syncElements(5000, 10000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);
        sc.syncElements(5000, 10000);
        sc.swipe("Up", sc.p2cy(20), 5000);
        sc.stopStepsGroup();
    }



    @Test
    @Order(11)
    public void LogOut() {
        sc.startStepsGroup("Email Logout");

        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void acceptDataConsent(String DataConsent) {
        sc.syncElements(2000, 10000);
        if(!sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='" + DataConsent + "' and @visible='true']")) {
            sc.swipe("Down", sc.p2cy(40), 2000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='" + DataConsent + "' and @visible='true']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Declined' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + DataConsent + "']]", 0);
        click("NATIVE", "xpath=//*[contains(@accessibilityLabel,'" + DataConsent + "')]", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'" + DataConsent + "')]", 0);
        click("NATIVE", "xpath=//*[@id='Manage Consent' or @id='MANAGE CONSENT' and @class='UIAButton']", 0, 1);
        sc.syncElements(2000, 10000);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT']", 0, 1000, 1, false);
        click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT']", 0, 1);
        sc.syncElements(15000, 60000);
        if (DataConsent.equalsIgnoreCase("External Vehicle Video Capture")) {
            verifyElementFound("NATIVE", "xpath=//*[@id='CONSENT ACCEPTED' or @id='Consent Accepted']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'You accepted the Safety Sense Video Capture Consent')]", 0);
            click("NATIVE", "xpath=//*[@id='BACK TO DATA PRIVACY PORTAL' or @id='Back To Data Privacy Portal' and @class='UIAButton']", 0, 1);
            sc.syncElements(10000, 60000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Accepted' and (./preceding-sibling::* | ./following-sibling::*)[@accessibilityLabel='" + DataConsent + "']]", 0);
    }

    public static void navigateToDataPrivacyPortal() {
        createLog("Started : Navigating to Data Privacy Portal screen");
        String vehicleModel = "2023 Solterra Touring";
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='person']")) {
            reLaunchApp_iOS();
        }
        sc.syncElements(2000, 5000);
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 5000);

        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(5000, 30000);
        sc.swipe("Down", sc.p2cy(40), 2000);
        sc.swipeWhileNotFound("Down", sc.p2cy(50), 3000, "NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_DATA_CONSENTS_CELL']", 0, 1000, 1, false);
        click("NATIVE", "xpath=//*[@accessibilityLabel='ACC_SETTINGS_DATA_CONSENTS_CELL']", 0, 1);
        sc.syncElements(5000, 30000);
        //*[(@text='Data Privacy Portal' or @text='DATA PRIVACY PORTAL') and @accessibilityLabel='TOOLBAR_LABEL_TITLE']
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Choose from the list of vehicles below to see the data consents for your vehicle and available services.' and ./parent::*[@text='Select a vehicle']]")) {
            createLog("select vehicle screen is displayed");
            verifyElementFound("NATIVE", "xpath=//*[(@text='Data Privacy Portal' or @text='DATA PRIVACY PORTAL') and @accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'"+vehicleModel+"')]", 0);
            click("NATIVE", "xpath=//*[contains(@text,'"+vehicleModel+"')]", 0, 1);
            sc.syncElements(15000, 60000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='Data Privacy Portal']", 0);
        createLog("Completed : Navigating to Data Privacy Portal screen");
    }
}
