package v2update.subaru.android.usa.smoke;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import v2update.subaru.android.usa.accountSettings.SubaruPersonalInfoAndroid;
import v2update.subaru.android.usa.find.SubaruDestinationsAndroid;
import v2update.subaru.android.usa.remote.SubaruHapticTouchAndroid;
import v2update.subaru.android.usa.remote.SubaruRemote21MMEVAndroid;

import java.io.IOException;

import static v2update.subaru.android.usa.accountSettings.SubaruDataPrivacyPortalAndroid.*;
import static v2update.subaru.android.usa.accountSettings.SubaruDriveConnectSettingsAndroid.driveConnectSettingsScreen;
import static v2update.subaru.android.usa.accountSettings.SubaruHelpAndFeedbackAndroid.*;
import static v2update.subaru.android.usa.accountSettings.SubaruLinkedAccountsAndroid.*;
import static v2update.subaru.android.usa.accountSettings.SubaruNotificationSettingsAndroid.*;
import static v2update.subaru.android.usa.accountSettings.SubaruPersonalInfoAndroid.*;
import static v2update.subaru.android.usa.accountSettings.SubaruSecuritySettingsAndroid.*;
import static v2update.subaru.android.usa.bottomTab.SubaruBottomTabAndroid.*;
import static v2update.subaru.android.usa.dashboard.SubaruDashboardRefresh.refreshEV;
import static v2update.subaru.android.usa.dashboard.SubaruFuelWidgetAndroid.fuelWidget;
import static v2update.subaru.android.usa.dashboard.SubaruTopNavigationAndroid.*;
import static v2update.subaru.android.usa.deepLinksAndroid.SubaruDeeplinksAndroid.*;
import static v2update.subaru.android.usa.ev.SubaruChargeManagementAndroid.*;
import static v2update.subaru.android.usa.find.SubaruDealersAndroid.validateDealers;
import static v2update.subaru.android.usa.find.SubaruDestinationsAndroid.*;
import static v2update.subaru.android.usa.health.SubaruHealthAndroid.*;
import static v2update.subaru.android.usa.pay.SubaruPayAndroid.*;
import static v2update.subaru.android.usa.remote.SubaruGuestDriverAndroid.*;
import static v2update.subaru.android.usa.shop.SubaruSubscriptionsAndroid.subscriptionsScreenValidation;
import static v2update.subaru.android.usa.status.SubaruVehicleStatusAndroid.*;
import static v2update.subaru.android.usa.vehicleInfo.SubaruGloveBoxAndroid.capabilities;
import static v2update.subaru.android.usa.vehicleInfo.SubaruSubscriptionAndroid.subscriptionPage;
import static v2update.subaru.android.usa.vehicleInfo.SubaruUpdateNicknameAndroid.updateVehicleNickName;
import static v2update.subaru.android.usa.vehicleInfo.SubaruVehicleSoftwareUpdateAndroid.vehicleSoftwareUpdate;
import static v2update.subaru.android.usa.vehicles.SubaruNoVehicleAndroid.validateDashboardWithoutVehicle;
import static v2update.subaru.android.usa.vehicles.SubaruVehicleSelectionAndroid.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSmokeAndroid extends SeeTestKeywords {
    String testName = " -SubaruSmokeAndroid-EV";
    boolean isStageRun = Boolean.parseBoolean(ConfigSingleton.configMap.get("stageRun"));

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                testName = System.getProperty("cloudApp") + testName;
                break;
            default :
                testName = ConfigSingleton.configMap.get("local") + testName;
        }
        android_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_Android("USA");
        selectionOfLanguage_Android("English");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strSubaruEmail"),ConfigSingleton.configMap.get("strSubaruPwd"));
        //android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        //Dismiss Download DK Widget
        if (sc.isElementFound("NATIVE","xpath=//*[@text='Download Digital Key']"))
            click("NATIVE","xpath=//*[@text='Download Digital Key']/following-sibling::*[@class='android.view.View']",0,1);
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    //Dashboard
    @Test
    @Order(1)
    @Tag("Dashboard")
    public void topNavigationTest() {
        sc.startStepsGroup("Top Navigation Validation");
        topNavigation();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    @Tag("Dashboard")
    public void bottomTabTest() {
        sc.startStepsGroup("Bottom Tab Validation");
        validateBottomTab();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    @Tag("Dashboard")
    public void pullDownRefreshTest() {
        sc.startStepsGroup("Bottom Tab Validation");
        refreshEV();
        sc.stopStepsGroup();
    }

    //EV
    @Test
    @Order(1)
    @Tag("ChargeManagement")
    public void fuelWidgetTest() {
        sc.startStepsGroup("Fuel Widget Validation EV");
        fuelWidget();
        sc.stopStepsGroup();
    }
    @Test
    @Order(2)
    @Tag("ChargeManagement")
    public void chargeInfoTest() {
        sc.startStepsGroup("Charge Information section Validation");
        chargeInfoValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    @Tag("ChargeManagement")
    public void scheduleTest() {
        sc.startStepsGroup("Charge Schedule section Validation");
        scheduleValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(4)
    @Tag("ChargeManagement")
    @Disabled
    public void statisticsTest() {
        sc.startStepsGroup("Test - Statistics section");
        //Fast Follower
        sc.stopStepsGroup();
    }
    @Test
    @Order(5)
    @Tag("ChargeManagement")
    @Disabled
    public void cleanAssistTest() {
        sc.startStepsGroup("Test - Clean Assist section");
        //Fast Follower
        sc.stopStepsGroup();
    }
    @Test
    @Order(6)
    @Tag("ChargeManagement")
    @Disabled
    public void chargingStationSettingsTest() {
        sc.startStepsGroup("Test - Charging Station Settings");
        //Fast Follower
        sc.stopStepsGroup();
    }
    @Test
    @Order(7)
    @Tag("ChargeManagement")
    @Disabled
    public void findStationTest() throws IOException {
        sc.startStepsGroup("Test - Find Station");
        //Fast Follower
        sc.stopStepsGroup();
    }
    @Test
    @Order(8)
    @Tag("ChargeManagement")
    public void dashboardFindStationTest() throws IOException {
        sc.startStepsGroup("Dashboard Find Station Validation");
        dashboardFindStation();
        sc.stopStepsGroup();
    }

    //Status
    @Test
    @Order(9)
    @Tag("Dashboard")
    @Tag("Status")
    public void doorsTest() {
        sc.startStepsGroup("Doors Validation");
        doors();
        sc.stopStepsGroup();
    }
    @Test
    @Order(10)
    @Tag("Dashboard")
    @Tag("Status")
    public void windowsTest() {
        sc.startStepsGroup("Windows Validation");
        windows();
        sc.stopStepsGroup();
    }
    @Test
    @Order(11)
    @Tag("Dashboard")
    @Tag("Status")
    public void vehicleInformationUpdatedTest(){
        sc.startStepsGroup("Status-Vehicle Information Updated Validation");
        vehicleInformationUpdated();
        sc.stopStepsGroup();
    }

    //Health
    @Test
    @Order(12)
    @Tag("Dashboard")
    @Tag("Health")
    public void vehicleAlertsTest() {
        sc.startStepsGroup("Vehicle Alert Validation");
        vehicleAlerts();
        sc.stopStepsGroup();
    }
    @Test
    @Order(13)
    @Tag("Dashboard")
    @Tag("Health")
    public void keyFobTest() {
        sc.startStepsGroup("Vehicle Alert Validation");
        keyFob();
        sc.stopStepsGroup();
    }
    @Test
    @Order(14)
    @Tag("Dashboard")
    @Tag("Health")
    public void vehicleHealthReportTest() {
        sc.startStepsGroup("Vehicle Alert Validation");
        vehicleHealthReport();
        sc.stopStepsGroup();
    }

    //Find
    @Test
    @Order(15)
    @Tag("Dashboard")
    public void findTabTest() {
        sc.startStepsGroup("Find Tab Validation");
        validateFindTab();
        sc.stopStepsGroup();
    }
    @Test
    @Order(16)
    @Tag("Find")
    @Disabled
    public void findStationsTest() {
        sc.startStepsGroup("Find Station Validation");
        //Not ready for testing in prod
        sc.stopStepsGroup();
    }
    @Test
    @Order(17)
    @Tag("Find")
    @Disabled //Bug OAD01-24339
    public void findDealersTest() {
        sc.startStepsGroup("Find Dealers Validation");
        validateDealers();
        sc.stopStepsGroup();
    }
    @Test
    @Order(18)
    @Tag("Find")
    public void destinationTest() {
        sc.startStepsGroup("Destinations Validation");
        validateDestinations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(19)
    @Tag("Find")
    public void destinationHomeAddressTest() {
        sc.startStepsGroup("Destinations Home Address Validation");
        SubaruDestinationsAndroid.validateHomeAddress();
        sc.stopStepsGroup();
    }
    @Test
    @Order(20)
    @Tag("Find")
    public void destinationWorkAddressTest() {
        sc.startStepsGroup("Destinations Work Address Validation");
        validateWorkAddress();
        sc.stopStepsGroup();
    }
    @Test
    @Order(21)
    @Tag("Find")
    public void send2CarTest() {
        sc.startStepsGroup("Send2Car Validation");
        validateSend2Car(homeAddress);
        sc.stopStepsGroup();
    }
    @Test
    @Order(22)
    @Tag("Find")
    public void destinationFavAddressTest() {
        sc.startStepsGroup("Destinations Fav Address Validation");
        validateFavAddress();
        sc.stopStepsGroup();
    }

    //Pay
    @Test
    @Order(23)
    @Tag("Dashboard")
    public void payTabTest() {
        sc.startStepsGroup("Pay Tab Validation");
        validatePayTab();
        sc.stopStepsGroup();
    }
    @Test
    @Order(24)
    @Tag("Pay")
    public void walletTest() {
        sc.startStepsGroup("Wallet Validation");
        wallet();
        sc.stopStepsGroup();
    }

    //Shop
    @Test
    @Order(25)
    @Tag("Dashboard")
    public void shopTabTest() {
        sc.startStepsGroup("Shop Tab Validation");
        validateShopTab();
        sc.stopStepsGroup();
    }
    @Test
    @Order(26)
    @Tag("Shop")
    public void shopSubscriptionTest() {
        sc.startStepsGroup("Shop Manage Subscription Validation");
        subscriptionsScreenValidation();
        sc.stopStepsGroup();
    }

    //VehicleInfo
    @Test
    @Order(27)
    @Tag("Dashboard")
    public void homeTabTest() {
        sc.startStepsGroup("Home Tab Validation");
        validateHomeTab();
        sc.stopStepsGroup();
    }
    @Test
    @Order(28)
    @Tag("VehicleInfo")
    public void updateNicknameTest() {
        sc.startStepsGroup("Update Nickname Validation");
        updateVehicleNickName();
        sc.stopStepsGroup();
    }
    @Test
    @Order(29)
    @Tag("VehicleInfo")
    public void CapabilitiesTest() {
        sc.startStepsGroup("Capabilities Validation");
        capabilities();
        sc.stopStepsGroup();
    }
    @Test
    @Order(30)
    @Tag("VehicleInfo")
    public void ManageSubscriptionTest() {
        sc.startStepsGroup("Manage Subscription Validation");
        subscriptionPage();
        sc.stopStepsGroup();
    }
    @Test
    @Order(31)
    @Tag("VehicleInfo")
    public void vehicleSoftwareUpdateTest() {
        sc.startStepsGroup("Vehicle Software Update Validation");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        vehicleSoftwareUpdate();
        sc.stopStepsGroup();
    }

    //Account Settings
    @Test
    @Order(32)
    @Tag("AccountSettings")
    public void personalDetailsTest() {
        sc.startStepsGroup("Personal Details Validations");
        validatePersonalDetails();
        sc.stopStepsGroup();
    }
    @Test
    @Order(33)
    @Tag("AccountSettings")
    public void homeAddressTest() {
        sc.startStepsGroup("Home Address Validations");
        SubaruPersonalInfoAndroid.validateHomeAddress();
        sc.stopStepsGroup();
    }
    @Test
    @Order(34)
    @Tag("AccountSettings")
    public void preferredLanguageTest() {
        sc.startStepsGroup("Preferred Language Validations");
        validatePreferredLanguage();
        sc.stopStepsGroup();
    }
    @Test
    @Order(35)
    @Tag("AccountSettings")
    public void notificationsSettingsTest() {
        sc.startStepsGroup("Notification Setting Validations");
        notificationSettingsValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(36)
    @Tag("AccountSettings")
    public void switchOFFNotificationsAlertsTest() {
        sc.startStepsGroup("Notification Alerts OFF Validations");
        switchOFFNotificationsAlerts(notificationsList);
        sc.stopStepsGroup();
    }
    @Test
    @Order(37)
    @Tag("AccountSettings")
    public void switchONNotificationsAlertsTest() {
        sc.startStepsGroup("Notification Alerts ON Validations");
        switchONNotificationsAlerts(notificationsList);
        sc.stopStepsGroup();
    }
    @Test
    @Order(38)
    @Tag("AccountSettings")
    public void notificationVehicleAlertsTest() {
        sc.startStepsGroup("Notification Setting - Vehicle Alerts Validations");
        vehicleAlertsValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(39)
    @Tag("AccountSettings")
    public void securitySettingsTest() {
        sc.startStepsGroup("Security Setting Validations");
        securitySettingsPageValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(40)
    @Tag("AccountSettings")
    public void resetPinTest() {
        sc.startStepsGroup("Pin Reset Validations");
        setResetPin();
        sc.stopStepsGroup();
    }
    @Test
    @Order(41)
    @Tag("AccountSettings")
    public void manageSaveProfileTest() {
        sc.startStepsGroup("Manage Save Profile Validations");
        manageSavedProfile();
        sc.stopStepsGroup();
    }
    @Test
    @Order(42)
    @Tag("AccountSettings")
    public void manageYourDataTest() {
        sc.startStepsGroup("Manage Your Data Validations");
        manageYourData();
        sc.stopStepsGroup();
    }
    @Test
    @Order(43)
    @Tag("AccountSettings")
    public void appleMusicTest() {
        sc.startStepsGroup("Apple Music Validations");
        appleMusic();
        sc.stopStepsGroup();
    }
    @Test
    @Order(44)
    @Tag("AccountSettings")
    public void amazonMusicTest() {
        sc.startStepsGroup("Amazon Music Validations");
        amazonMusic();
        sc.stopStepsGroup();
    }
    @Test
    @Order(45)
    @Tag("AccountSettings")
    public void driveConnectTest() {
        sc.startStepsGroup("Drive Connect Validations");
        driveConnectSettingsScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(46)
    @Tag("AccountSettings")
    public void contactUsTest() {
        sc.startStepsGroup("Contact Us Validations");
        contactUs();
        sc.stopStepsGroup();
    }
    @Test
    @Order(47)
    @Tag("AccountSettings")
    public void vehicleSupportTest() {
        sc.startStepsGroup("Vehicle Support Validations");
        vehicleSupport();
        sc.stopStepsGroup();
    }
    @Test
    @Order(48)
    @Tag("AccountSettings")
    public void dataPrivacyPortalTest() {
        sc.startStepsGroup("Data Privacy Portal Validations");
        dataConsentHomePage();
        sc.stopStepsGroup();
    }
    @Test
    @Order(49)
    @Tag("AccountSettings")
    public void masterDataConsentTest() {
        sc.startStepsGroup("Master Data Consent Validations");
        masterDataConsent();
        sc.stopStepsGroup();
    }
    @Test
    @Order(50)
    @Tag("AccountSettings")
    public void driveConnectPrivacyPortalTest() {
        sc.startStepsGroup("Data Privacy Portal - Drive Connect Master Data Consents");
        driveConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(51)
    @Tag("AccountSettings")
    public void remoteConnectPrivacyPortalTest() {
        sc.startStepsGroup("Data Privacy Portal - Remote Connect Master Data Consents");
        remoteConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(52)
    @Tag("AccountSettings")
    public void safetyConnectPrivacyPortalTest() {
        sc.startStepsGroup("Data Privacy Portal - Safety Connect Master Data Consents");
        safetyConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(53)
    @Tag("AccountSettings")
    public void masterDataConsentPrivacyPortalTest() {
        sc.startStepsGroup("Data Privacy Portal - Service Connect Master Data Consents");
        serviceConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(54)
    @Tag("AccountSettings")
    public void privacyAndTermsTest() {
        sc.startStepsGroup("Data Privacy Portal - Connected Services Privacy and Terms of Use");
        privacyAndTerms();
        sc.stopStepsGroup();
    }
    @Test
    @Order(55)
    @Tag("AccountSettings")
    public void darkModeTest() throws IOException {
        sc.startStepsGroup("Dark Light Mode Tests");
        chevronValidationDarkMode();
        sc.stopStepsGroup();
    }
    @Test
    @Order(56)
    @Tag("AccountSettings")
    public void lightModeTest() throws IOException {
        sc.startStepsGroup(" Light Mode Tests");
        chevronValidationLightMode();
        sc.stopStepsGroup();
    }

    //Remote
    @Test
    @Order(57)
    @Tag("RemoteCommands")
    public void remoteUnLock(){
        sc.startStepsGroup("Test - SubaruEV remote Unlock");
        clearPNS_android();
        SubaruRemote21MMEVAndroid.executeRemoteUnLock();
        android_PNS("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(58)
    @Tag("RemoteCommands")
    public void remoteLock(){
        sc.startStepsGroup("Test - SubaruEV remote Lock");
        SubaruRemote21MMEVAndroid.executeRemoteLock();
        android_PNS("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(59)
    @Tag("RemoteCommands")
    public void remoteStart(){
        sc.startStepsGroup("Test - SubaruEV remote Start");
        SubaruRemote21MMEVAndroid.executeRemoteStart();
        android_PNS("EVStart");
        sc.stopStepsGroup();
    }

    @Test
    @Order(60)
    @Tag("RemoteCommands")
    public void remoteStop(){
        sc.startStepsGroup("Test - SubaruEV remote Stop");
        SubaruRemote21MMEVAndroid.executeRemoteStop();
        android_PNS("Stop");
        sc.stopStepsGroup();
    }

    @Test
    @Order(61)
    @Tag("RemoteCommands")
    public void remoteLights(){
        sc.startStepsGroup("Test - SubaruEV remote Lights");
        SubaruRemote21MMEVAndroid.executeRemoteLight();
        sc.stopStepsGroup();
    }

    @Test
    @Order(62)
    @Tag("RemoteCommands")
    public void remoteHazard(){
        sc.startStepsGroup("Test - SubaruEV remote Hazard");
        SubaruRemote21MMEVAndroid.executeRemoteHazards();
        sc.stopStepsGroup();
    }

    @Test
    @Order(63)
    @Tag("RemoteCommands")
    public void remoteBuzzer(){
        sc.startStepsGroup("Test - SubaruEV remote Buzzer");
        SubaruRemote21MMEVAndroid.executeRemoteBuzzer();
        sc.stopStepsGroup();
    }

    @Test
    @Order(64)
    @Tag("RemoteCommands")
    public void remoteTrunkUnLock(){
        sc.startStepsGroup("Test - SubaruEV remote Trunk Unlock");
        SubaruRemote21MMEVAndroid.executeRemoteTrunkUnlock();
        sc.stopStepsGroup();
    }

    @Test
    @Order(65)
    @Tag("RemoteCommands")
    public void remoteTrunkLock(){
        sc.startStepsGroup("Test - SubaruEV remote Trunk Lock ");
        SubaruRemote21MMEVAndroid.executeRemoteTrunkLock();
        sc.stopStepsGroup();
    }

    @Test
    @Order(66)
    @Tag("RemoteCommands")
    public void remoteHorn(){
        sc.startStepsGroup("Test - SubaruEV remote Horn");
        SubaruRemote21MMEVAndroid.executeRemoteHorn();
        android_PNS("Horn");
        sc.stopStepsGroup();
    }
    @Test
    @Order(67)
    @Tag("RemoteCommands")
    public void remoteClimateSettingsON(){
        sc.startStepsGroup("Test - SubaruEV remote Climate ON");
        SubaruRemote21MMEVAndroid.executeRemoteClimateSettingsToggleON();
        sc.stopStepsGroup();
    }
    @Test
    @Order(68)
    @Tag("RemoteCommands")
    public void remoteClimateSettingsOFF(){
        sc.startStepsGroup("Test - SubaruEV remote Climate ON");
        SubaruRemote21MMEVAndroid.executeRemoteClimateSettingsToggleOff();
        sc.stopStepsGroup();
    }
    @Test
    @Order(69)
    @Tag("RemoteCommands")
    public void hapticUnlockTest(){
        sc.startStepsGroup("Test - SubaruEV remote Haptic Unlock");
        SubaruHapticTouchAndroid.hapticTouchUnlock();
        sc.stopStepsGroup();
    }

    //Haptic Touch
    @Test
    @Order(70)
    @Tag("RemoteCommands")
    public void hapticLockTest(){
        sc.startStepsGroup("Test - SubaruEV remote Haptic Lock");
        SubaruHapticTouchAndroid.hapticTouchLock();
        sc.stopStepsGroup();
    }
    @Test
    @Order(71)
    @Tag("RemoteCommands")
    public void hapticStartTest(){
        sc.startStepsGroup("Test - SubaruEV remote Haptic Start");
        SubaruHapticTouchAndroid.hapticTouchStart();
        sc.stopStepsGroup();
    }
    @Test
    @Order(72)
    @Tag("RemoteCommands")
    public void hapticStopTest(){
        sc.startStepsGroup("Test - SubaruEV remote Haptic Stop");
        SubaruRemote21MMEVAndroid.executeRemoteStop();
        sc.stopStepsGroup();
    }

    //GuestDriver
    @Test
    @Order(73)
    @Tag("GuestDriver")
    public void guestDriverHomePageTest() {
        sc.startStepsGroup("Guest Driver Home page validations");
        guestDriverHomePageValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(74)
    @Tag("GuestDriver")
    public void guestAndValetTest() {
        sc.startStepsGroup("Guest And Valet Alerts Verification");
        //check Valet alerts is On or oFF
        guestAlertsValidation();
        //check Guest alerts is On or oFF
        valetAlertsValidation();
        sc.stopStepsGroup();
    }
    @Test
    @Order(75)
    @Tag("GuestDriver")
    public void valetSettingsHomePageTest() {
        sc.startStepsGroup("Start: Valet Setting home page validations");
        valetSettingsHomePageValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(76)
    @Tag("GuestDriver")
    public void valetDrivingLimitSpeedTest() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Speed");
        drivingLimitSpeed("valet");
        sc.stopStepsGroup();
    }
    @Test
    @Order(77)
    @Tag("GuestDriver")
    public void valetDrivingLimitMileTest() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Miles");
        drivingLimitsMiles("valet");
        sc.stopStepsGroup();
    }
    @Test
    @Order(78)
    @Tag("GuestDriver")
    public void valetDrivingLimitAreaTest() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Area");
        drivingLimitsArea("valet");
        sc.stopStepsGroup();
    }
    @Test
    @Order(79)
    @Tag("GuestDriver")
    public void valetDrivingLimitCurfewTest() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Curfew");
        drivingLimitsCurfew("valet");
        sc.stopStepsGroup();
    }
    @Test
    @Order(80)
    @Tag("GuestDriver")
    public void valetDrivingLimitTimeTest() {
        sc.startStepsGroup("Guest Driver-Valet Driving Limits Time");
        drivingLimitsTime("valet");
        sc.stopStepsGroup();
    }
    @Test
    @Order(81)
    @Tag("GuestDriver")
    public void guestDrivingLimitSpeedTest() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Speed");
        drivingLimitSpeed("Guest");
        sc.stopStepsGroup();
    }
    @Test
    @Order(82)
    @Tag("GuestDriver")
    public void guestDrivingLimitMilesTest() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Miles");
        drivingLimitsMiles("Guest");
        sc.stopStepsGroup();
    }
    @Test
    @Order(83)
    @Tag("GuestDriver")
    public void guestDrivingLimitAreaTest() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Area");
        drivingLimitsArea("Guest");
        sc.stopStepsGroup();
    }
    @Test
    @Order(84)
    @Tag("GuestDriver")
    public void guestDrivingLimitCurfewTest() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Curfew");
        drivingLimitsCurfew("Guest");
        sc.stopStepsGroup();
    }
    @Test
    @Order(85)
    @Tag("GuestDriver")
    public void guestDrivingLimitTimeTest() {
        sc.startStepsGroup("Guest Driver-Guest Driving Limits Time");
        drivingLimitsTime("Guest");
        sc.stopStepsGroup();
    }

    //DeepLink
    @Test
    @Order(86)
    @Tag("Deeplinks")
    public void smsRegistraion21MMTest() {
        sc.startStepsGroup("Test - 21MM sms registration DeepLink");
        deepLink("url/sms_registration");
        verifySmsRegistration21MMScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(87)
    @Tag("Deeplinks")
    public void smsLinkAccounts21MMTest() {
        sc.startStepsGroup("Test - 21MM sms link accounts DeepLink");
        deepLink("url/sms_linkaccounts");
        verifySmsLinkAccounts21MMScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(88)
    @Tag("Deeplinks")
    public void smsSubscription21MMTest() {
        sc.startStepsGroup("Test - 21MM sms subscription DeepLink");
        deepLink("url/sms_subscription");
        verifySmsSubscription21MMScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(89)
    @Tag("Deeplinks")
    public void smsPinReset21MMTest() {
        sc.startStepsGroup("Test - 21MM sms pinreset DeepLink");
        deepLink("url/sms_pinreset");
        verifySmsPinReset21MMScreen();
        sc.stopStepsGroup();
    }

    //Miscellaneous
    @Test
    @Order(90)
    @Tag("Vehicles")
    public void defaultVehicle() {
        sc.startStepsGroup("Test - Default Vehicle");
        Default("JTMABABA0PA002944");
        sc.stopStepsGroup();
    }

    @Test
    @Order(91)
    @Tag("Vehicles")
    public void switchVehicle() {
        sc.startStepsGroup("Test - Switch Vehicle");
        Switcher("JTMABABA0PA002944");
        sc.stopStepsGroup();
    }
    @Test
    @Order(92)
    @Tag("Vehicles")
    public void scanVinTest() {
        sc.startStepsGroup("Add Vehicle - Scan VIN");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@content-desc='tap to open vehicle switcher']"))
            reLaunchApp_android();
        scanVIN();
        sc.stopStepsGroup();
    }
    @Test
    @Order(93)
    @Tag("AccountSettings")
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }
    @Test
    @Order(94)
    @Tag("Vehicles")
    public void dashboardWithoutVehicle() {
        sc.startStepsGroup("Test - Dashboard with No Vehicle Registered");
        createLog("Loggin in to email with no VIN");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("noVehicleUserName"), ConfigSingleton.configMap.get("noVehiclePassword"));
        validateDashboardWithoutVehicle();
        sc.stopStepsGroup();
    }

    //Stage app needs to install please keep below tests at the end
    @Test
    @Order(95)
    @Tag("VehicleInfo")
    @Tag("Subscriptions")
    public void paidSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Paid Subscriptions");
        if(isStageRun) {
            createLog("Stage run - adding paid service");
            isStageApp = true;
            sc.install(ConfigSingleton.configMap.get("cloudAppStageSubaruAndroid"), false, false);
            sc.launch(ConfigSingleton.configMap.get("appPackageStageSubaru"), false, false);
            strAppPackage = ConfigSingleton.configMap.get("appPackageStageSubaru");
            cloudAppName = ConfigSingleton.configMap.get("cloudAppStageSubaruAndroid");
            sc.syncElements(5000,10000);
            Android_handlepopups();
            checkIsLoginScreenDisplayed_android();
            selectionOfCountry_Android("USA");
            selectionOfLanguage_Android("English");
            android_keepMeSignedIn(true);
            android_emailLoginIn("subarustage_21mm@mail.tmnact.io", "Test@222");
            //Paid Subscription With Auto Renew OFF
            //TODO create method
            //Paid Subscription With Auto Renew ON and add new credit card
            //TODO create method

        } else {createLog("Production run - skipping add paid service");}
        sc.stopStepsGroup();
    }
    @Test
    @Order(96)
    @Tag("Pay")
    public void walletEVTest(){
        sc.startStepsGroup("Test - wallet EV");
        if(isStageRun) {
            createLog("Stage run - add or remove card");
            isStageApp = true;
            addNewCard();
            removePaymentMethod();
        }
        sc.stopStepsGroup();
    }
    @Test
    @Order(97)
    @Tag("AccountSettings")
    public void signOutStageTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }
}
