package v2update.subaru.ios.usa.smoke;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import v2update.subaru.ios.usa.accountSettings.SubaruAccountsIOS;
import v2update.subaru.ios.usa.accountSettings.SubaruNotificationSettingsIOS;
import v2update.subaru.ios.usa.ev.SubaruChargeManagementIOS;
import v2update.subaru.ios.usa.remote.*;
import v2update.subaru.ios.usa.shop.SubaruShopManageSubscriptionsIOS;
import v2update.subaru.ios.usa.vehicleInfo.SubaruSubscriptionIOS;

import java.io.IOException;

import static v2update.subaru.ios.usa.accountSettings.SubaruDataPrivacyPortalIOS.*;
import static v2update.subaru.ios.usa.accountSettings.SubaruHelpAndFeedbackIOS.*;
import static v2update.subaru.ios.usa.accountSettings.SubaruSecuritySettingsIOS.*;
import static v2update.subaru.ios.usa.bottomTab.SubaruBottomTabIOS.validateBottomTab;
import static v2update.subaru.ios.usa.accountSettings.SubaruDriveConnectIOS.driveConnectSettingsScreen;
import static v2update.subaru.ios.usa.accountSettings.SubaruLinkedAccountsIOS.amazonMusic;
import static v2update.subaru.ios.usa.accountSettings.SubaruLinkedAccountsIOS.appleMusic;
import static v2update.subaru.ios.usa.accountSettings.SubaruPersonalInfoIOS.*;
import static v2update.subaru.ios.usa.bottomTab.SubaruBottomTabIOS.*;
import static v2update.subaru.ios.usa.dashboard.SubaruDashboardRefreshIOS.refreshInRemoteSection;
import static v2update.subaru.ios.usa.dashboard.SubaruFuelWidgetIOS.fuelWidgetEV;
import static v2update.subaru.ios.usa.ev.SubaruChargeManagementIOS.*;
import static v2update.subaru.ios.usa.find.SubaruDealersIOS.validateDealers;
import static v2update.subaru.ios.usa.find.SubaruDestinationsIOS.*;
import static v2update.subaru.ios.usa.find.SubaruFindStationsIOS.verifySearchStation;
import static v2update.subaru.ios.usa.health.SubaruHealthIOS.*;
import static v2update.subaru.ios.usa.pay.SubaruPayIOS.walletEV;
import static v2update.subaru.ios.usa.remote.SubaruGuestDriverIOS.*;
import static v2update.subaru.ios.usa.status.SubaruVehicleStatusIOS.*;
import static v2update.subaru.ios.usa.vehicleInfo.SubaruGloveBoxIOS.validateVehicleCapabilities;
import static v2update.subaru.ios.usa.vehicleInfo.SubaruUpdateNickNameIOS.updateVehicleNickName;
import static v2update.subaru.ios.usa.vehicleInfo.SubaruVehicleInfoDetailsIOS.vehicleInfoDetails;
import static v2update.subaru.ios.usa.vehicleInfo.SubaruVehicleSoftwareUpdateIOS.vehicleSoftwareUpdate;
import static v2update.subaru.ios.usa.vehicles.SubaruNoVehicleIOS.validateDashboardWithoutVehicle;
import static v2update.subaru.ios.usa.vehicles.SubaruVehicleSelectionIOS.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSmokeIOS extends SeeTestKeywords {
    String testName = " - SubaruSmoke-IOS";
    boolean isStageRun = Boolean.valueOf(ConfigSingleton.configMap.get("stageRun"));

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
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strSubaruEmail"),ConfigSingleton.configMap.get("strSubaruPwd"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    @Tag("Dashboard")
    @Tag("Health")
    public void vehicleAlertsTest() {
        sc.startStepsGroup("Test - Vehicle Alerts validations");
        vehicleAlerts();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    @Tag("Dashboard")
    @Tag("Health")
    public void keyFobTest() {
        sc.startStepsGroup("Test - Key Fob validations");
        keyFob();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    @Tag("Dashboard")
    @Tag("Health")
    public void vehicleHealthReportTest() {
        sc.startStepsGroup("Test - Vehicle Health Report validations");
        vehicleHealthReport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    @Tag("Dashboard")
    @Tag("Status")
    public void doorsTest(){
        sc.startStepsGroup("Test - Doors");
        doors();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    @Tag("Dashboard")
    @Tag("Status")
    public void windowsTest(){
        sc.startStepsGroup("Test - Windows");
        windows();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    @Tag("Dashboard")
    @Tag("Status")
    public void vehicleInformationUpdatedTest(){
        sc.startStepsGroup("Test - Vehicle Information Updated");
        vehicleInformationUpdated();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    @Tag("Dashboard")
    @Tag("Status")
    public void pullDownRefreshStatusSectionTest() {
        sc.startStepsGroup("Test - Pull Down Refresh Status Section");
        pullDownRefreshInStatusSection();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    @Tag("RemoteCommands")
    public void RemoteUnLock(){
        sc.startStepsGroup("Test - SubaruEV remote Unlock");
        clearPNS();
        SubaruRemoteEVIOS.executeRemoteUnLock();
        PNS("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    @Tag("RemoteCommands")
    public void RemoteLock(){
        sc.startStepsGroup("Test - SubaruEV remote Lock");
        SubaruRemoteEVIOS.executeRemoteLock();
        PNS("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    @Tag("RemoteCommands")
    public void RemoteStart(){
        sc.startStepsGroup("Test - SubaruEV remote Start");
        SubaruRemoteEVIOS.executeRemoteStart();
        PNS("EVStart");
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    @Tag("RemoteCommands")
    public void RemoteStop(){
        sc.startStepsGroup("Test - SubaruEV remote Stop");
        SubaruRemoteEVIOS.executeRemoteStop();
        PNS("Stop");
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    @Tag("RemoteCommands")
    public void RemoteLights(){
        sc.startStepsGroup("Test - SubaruEV remote Lights");
        SubaruRemoteEVIOS.executeRemoteLights();
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    @Tag("RemoteCommands")
    public void RemoteHazard(){
        sc.startStepsGroup("Test - SubaruEV remote Hazard");
        SubaruRemoteEVIOS.executeRemoteHazard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(14)
    @Tag("RemoteCommands")
    public void RemoteBuzzer(){
        sc.startStepsGroup("Test - SubaruEV remote Buzzer");
        SubaruRemoteEVIOS.executeRemoteBuzzer();
        sc.stopStepsGroup();
    }

    @Test
    @Order(15)
    @Tag("RemoteCommands")
    public void RemoteTrunkUnLock(){
        sc.startStepsGroup("Test - SubaruEV remote Trunk Unlock");
        SubaruRemoteEVIOS.executeRemoteTrunkUnlock();
        PNS("Trunk Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(16)
    @Tag("RemoteCommands")
    public void RemoteTrunkLock(){
        sc.startStepsGroup("Test - SubaruEV remote Trunk Lock ");
        SubaruRemoteEVIOS.executeRemoteTrunkLock();
        PNS("Trunk Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(17)
    @Tag("RemoteCommands")
    @Disabled
    public void RemoteHorn(){
        sc.startStepsGroup("Test - SubaruEV remote Horn");
        SubaruRemoteEVIOS.executeRemoteHorn();
        PNS("Horn");
        sc.stopStepsGroup();
    }

    @Test
    @Order(18)
    @Tag("RemoteCommands")
    public void StatusScreenDoorsUnlockLockTest(){
        sc.startStepsGroup("Test - Status Screen Doors Unlock Lock Validation");
        SubaruRemoteEVIOS.statusDoorsUnlockLockValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(19)
    @Tag("RemoteCommands")
    public void ClimateScreen(){
        sc.startStepsGroup("Test - SubaruEV remote schedule screen");
        SubaruRemoteEVIOS.GotoClimateScheduleSubaruEV();
        sc.stopStepsGroup();
    }

    @Test
    @Order(20)
    @Tag("RemoteCommands")
    public void RemoteClimateSettingsOn(){
        sc.startStepsGroup("Test - SubaruEV remote Climate Settings On");
        SubaruRemoteEVIOS.executeRemoteClimateSettingsOn();
        sc.stopStepsGroup();
    }

    @Test
    @Order(21)
    @Tag("RemoteCommands")
    public void RemoteClimateSettingsOFF(){
        sc.startStepsGroup("Test - SubaruEV remote Climate Settings Off");
        SubaruRemoteEVIOS.executeRemoteClimateSettingsOff();
        sc.stopStepsGroup();
    }

    @Test
    @Order(22)
    @Tag("RemoteCommands")
    public void deleteScheduleTest() {
        sc.startStepsGroup("Test - Delete All Schedules ");
        SubaruRemoteEVIOS.deleteSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(23)
    @Tag("RemoteCommands")
    public void createScheduleTest() {
        sc.startStepsGroup("Test - Create Schedule ");
        SubaruRemoteEVIOS.createSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(24)
    @Tag("RemoteCommands")
    public void editScheduleTest() {
        sc.startStepsGroup("Test - Modify Schedule ");
        SubaruRemoteEVIOS.editSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(25)
    @Tag("RemoteCommands")
    public void hapticUnlock() {
        sc.startStepsGroup("Subaru Haptic Touch Unlock");
        clearPNS();
        SubaruHapticTouchIOS.appFinderHapticTouch();
        SubaruHapticTouchIOS.executeHapticTouchUnLock();
        PNS("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(26)
    @Tag("RemoteCommands")
    public void hapticLock() {
        sc.startStepsGroup("Subaru Haptic Touch Lock");
        SubaruHapticTouchIOS.appFinderHapticTouch();
        SubaruHapticTouchIOS.executeHapticTouchLock();
        PNS("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(27)
    @Tag("RemoteCommands")
    public void hapticStart() {
        sc.startStepsGroup("Subaru Haptic Touch Start");
        SubaruHapticTouchIOS.appFinderHapticTouch();
        SubaruHapticTouchIOS.executeHapticTouchStart();
        PNS("Start");
        sc.stopStepsGroup();
    }

    @Test
    @Order(28)
    @Tag("RemoteCommands")
    public void hapticStop() {
        sc.startStepsGroup("Subaru Remote Stop");
        SubaruHapticTouchIOS.executeRemoteStop();
        PNS("Stop");
        sc.stopStepsGroup();
    }

    @Test
    @Order(29)
    @Tag("VehicleInfo")
    public void vehicleCapabilitiesTest() {
        sc.startStepsGroup("Test - Vehicle Capabilities");
        validateVehicleCapabilities();
        sc.stopStepsGroup();
    }

    @Test
    @Order(30)
    @Tag("VehicleInfo")
    public void updateNickNameTest(){
        sc.startStepsGroup("Test - Update Nickname");
        updateVehicleNickName("JTMABABA0PA002944");
        sc.stopStepsGroup();
    }

    @Test
    @Order(31)
    @Tag("VehicleInfo")
    public void vehicleSoftwareUpdateTest(){
        sc.startStepsGroup("Test - Vehicle Software Update");
        vehicleSoftwareUpdate();
        sc.stopStepsGroup();
    }
    @Test
    @Order(32)
    @Tag("Account")
    @Tag("PersonalInfo")
    public void personalInfo() {
        sc.startStepsGroup("Test- Personal Details");
        personalDetails();
        sc.stopStepsGroup();
    }
    @Test
    @Order(33)
    @Tag("Account")
    @Tag("PersonalInfo")
    public void homeAddressTest() {
        sc.startStepsGroup("Test - Home Address");
        homeAddress();
        sc.stopStepsGroup();
    }
    @Test
    @Order(34)
    @Tag("Account")
    @Tag("PersonalInfo")
    public void preferredLanguageTest() {
        sc.startStepsGroup("Test - Preferred Language");
        preferredLanguage();
        sc.stopStepsGroup();
    }
    @Test
    @Order(35)
    @Tag("Account")
    @Tag("SecuritySettings")
    public void securitySettingsTest() {
        sc.startStepsGroup("Test - security settings");
        securitySettings();
        sc.stopStepsGroup();
    }
    @Test
    @Order(36)
    @Tag("Account")
    @Tag("SecuritySettings")
    public void setPinTest() {
        sc.startStepsGroup("Test - Set or Reset pin");
        setPIN(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        sc.stopStepsGroup();
    }
    @Test
    @Order(37)
    @Tag("Account")
    @Tag("SecuritySettings")
    public void managedSavedProfileTest() {
        sc.startStepsGroup("Test - Managed Saved Profile");
        manageSavedProfile();
        sc.stopStepsGroup();
    }
    @Test
    @Order(38)
    @Tag("Account")
    @Tag("LinkedAccounts")
    public void appleMusicTest() {
        sc.startStepsGroup("Test - Apple Music");
        appleMusic();
        sc.stopStepsGroup();
    }
    @Test
    @Order(39)
    @Tag("Account")
    @Tag("LinkedAccounts")
    public void amazonMusicTest() {
        sc.startStepsGroup("Test - Amazon Music");
        amazonMusic();
        sc.stopStepsGroup();
    }
    @Test
    @Order(40)
    @Tag("Account")
    @Tag("DriveConnect")
    public void driveConnectSettings() {
        sc.startStepsGroup("Test - Drive Connect Settings");
        driveConnectSettingsScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(41)
    @Tag("Account")
    @Tag("HelpAndFeedback")
    @Disabled
    public void contactUsSubaruTest(){
        sc.startStepsGroup("Test -Help & Feedback Contact us");
        contactUsSubaru();
        sc.stopStepsGroup();
    }
    @Test
    @Order(42)
    @Tag("Account")
    @Tag("HelpAndFeedback")
    @Disabled
    public void manageYourPreference() {
        sc.startStepsGroup("Test - Help & Feedback Manage your preference");
        validateManagePreferences();
        sc.stopStepsGroup();
    }
    @Test
    @Order(43)
    @Tag("Account")
    @Tag("HelpAndFeedback")
    public void vehicleSupportSubaru21MM () {
        sc.startStepsGroup("Test - Help and feedback vehicle support");
        vehicleSupportSubaru();
        sc.stopStepsGroup();
    }
    @Test
    @Order(44)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void dataConsentsTest() throws Exception {
        sc.startStepsGroup("Test - Data privacy portal screen");
        DataConsent();
        sc.stopStepsGroup();
    }
    @Test
    @Order(45)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void masterDataConsentSubaru() {
        sc.startStepsGroup("Test - Master Data Consent");
        MasterDataConsent();
        sc.stopStepsGroup();
    }
    @Test
    @Order(46)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void DriveConnectSubaru() {
        sc.startStepsGroup("Test - Drive Connect");
        DriveConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(47)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void RemoteConnectSubaru() {
        sc.startStepsGroup("Test - Remote Connect");
        RemoteConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(48)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void SafetyConnectSubaru() {
        sc.startStepsGroup("Test - Safety Connect");
        SafetyConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(49)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void _ServiceConnectSubaru() {
        sc.startStepsGroup("Test -Service Connect Subaru");
        ServiceConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(50)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void AcceptDeclineMasterDataConsent() {
        sc.startStepsGroup("Test - AcceptDecline Master Data Consent");
        AcceptDeclineMasterDataConsentValidation("Subaru");
        sc.stopStepsGroup();
    }
    @Test
    @Order(51)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void DeclineServiceConnectCommunicationTest() {
        sc.startStepsGroup("Test - Decline Service Connect Communication");
        DeclineServiceConnectCommunication();
        sc.stopStepsGroup();
    }
    @Test
    @Order(52)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void AcceptServiceConnectCommunicationTest() {
        sc.startStepsGroup("Test - Accept Service Connect Communication");
        AcceptServiceConnectCommunication();
        sc.stopStepsGroup();
    }
    @Test
    @Order(53)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void PrivacyAndTermsOfUseTest() {
        sc.startStepsGroup("Test - Privacy & Terms of Use");
        PrivacyAndTermsOfUse();
        sc.stopStepsGroup();
    }

    @Test
    @Order(54)
    @Tag("Dashboard")
    public void bottomTab(){
        sc.startStepsGroup("Bottom Tab IOS");
        validateBottomTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(55)
    @Tag("Dashboard")
    public void serviceTab(){
        sc.startStepsGroup("Service Tab IOS");
        validateServiceTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(56)
    @Tag("Dashboard")
    public void payTab(){
        sc.startStepsGroup("Pay Tab IOS");
        validatePayTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(57)
    @Tag("Dashboard")
    public void homeTab(){
        sc.startStepsGroup("Home Tab IOS");
        validateHomeTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(58)
    @Tag("Dashboard")
    public void shopTab(){
        sc.startStepsGroup("Shop Tab IOS");
        validateShopTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(59)
    @Tag("Dashboard")
    public void findTab(){
        sc.startStepsGroup("Find Tab IOS");
        validateFindTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(60)
    @Tag("Dashboard")
    public void dashboardRefreshTest21MM() {
        sc.startStepsGroup("Test - Dashboard Refresh 21MM");
        refreshInRemoteSection();
        sc.stopStepsGroup();
    }

    @Test
    @Order(61)
    @Tag("Dashboard")
    public void fuelWidgetEVTest() throws IOException {
        sc.startStepsGroup("Fuel Widget EV");
        fuelWidgetEV();
        sc.stopStepsGroup();
    }
    @Test
    @Order(62)
    @Tag("ChargeManagement")
    public void chargeInfoTest() {
        sc.startStepsGroup("Test - Charge Information section");
        chargeInfoSection();
        sc.stopStepsGroup();
    }
    @Test
    @Order(63)
    @Tag("ChargeManagement")
    public void scheduleTest() {
        sc.startStepsGroup("Test - Schedule Test Card ");
        scheduleValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(64)
    @Tag("ChargeManagement")
    public void createScheduleEVTest() {
        sc.startStepsGroup("Test - Create Schedule ");
        SubaruChargeManagementIOS.createSchedule();
        sc.stopStepsGroup();
    }
    @Test
    @Order(65)
    @Tag("ChargeManagement")
    public void findStationTest() throws IOException {
        sc.startStepsGroup("Test - Find Station");
        findStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(66)
    @Tag("ChargeManagement")
    public void dashboardFindStationTest() throws IOException {
        sc.startStepsGroup("Test - Dashboard Find Station");
        dashboardFindStation();
        sc.stopStepsGroup();
    }
    @Test
    @Order(67)
    @Tag("Find")
    public void dealers(){
        sc.startStepsGroup("Test - Dealers");
        validateDealers();
        sc.stopStepsGroup();
    }
    @Test
    @Order(68)
    @Tag("Find")
    @Disabled
    public void findStationOnFind() throws IOException {
        sc.startStepsGroup("Test - Find->Stations EV");
        findStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(69)
    @Tag("Find")
    public void searchStation() throws IOException {
        sc.startStepsGroup("Search Station");
        verifySearchStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(70)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void GuestDriveHomePage() {
        sc.startStepsGroup("Guest Driver Home page validations");
        guestDriverHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(71)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestAndValetAlerts() {
        sc.startStepsGroup("Start: Guest And Valet Alerts Verification");
        //check Valet alerts is On or oFF
        guestAlertsValidation();
        //check Guest alerts is On or oFF
        valetAlertsValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(72)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestSettingsHomePage() {
        sc.startStepsGroup("Guest Setting home page");
        guestSettingsHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(73)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void valetSettingsHomePage() {
        sc.startStepsGroup("Valet Setting home page validations");
        valetSettingsHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(74)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsSpeed() {
        sc.startStepsGroup("Valet Driving Limits Speed");
        drivingLimitSpeed("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(75)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsMiles() {
        sc.startStepsGroup("Valet Driving Limits Miles");
        drivingLimitsMiles("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(76)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsCurfew() {
        sc.startStepsGroup("Valet Driving Limits Curfew");
        drivingLimitsCurfew("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(77)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsTime() {
        sc.startStepsGroup("Valet Driving Limits Time");
        drivingLimitsTime("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(78)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsArea() {
        sc.startStepsGroup("Valet Driving Limits Area");
        drivingLimitsArea("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(79)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsIgnition() {
        sc.startStepsGroup("Valet Driving Limits Ignition");
        drivingLimitsIgnition("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(80)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsSpeed() {
        sc.startStepsGroup("Guest Driving Limits Speed");
        drivingLimitSpeed("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(81)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsMiles() {
        sc.startStepsGroup("Guest Driving Limits Miles");
        drivingLimitsMiles("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(82)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsCurfew() {
        sc.startStepsGroup("Guest Driving Limits Curfew");
        drivingLimitsCurfew("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(83)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsTime() {
        sc.startStepsGroup("Guest Driving Limits Time");
        drivingLimitsTime("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(84)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsArea() {
        sc.startStepsGroup("Guest Driving Limits Area");
        drivingLimitsArea("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(85)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsIgnition() {
        sc.startStepsGroup("Guest Driving Limits Ignition");
        drivingLimitsIgnition("guest");
        sc.stopStepsGroup();
    }
    @Test
    @Order(86)
    @Tag("Vehicles")
    public void dashboardWithoutVehicle() {
        sc.startStepsGroup("Test - Dashboard with No Vehicle Registered");
        validateDashboardWithoutVehicle();
        sc.stopStepsGroup();
    }
    @Test
    @Order(87)
    @Tag("Vehicles")
    public void defaultVehicle() {
        sc.startStepsGroup("Test - Default Vehicle");
        Default("JTMABABA0PA002944");
        sc.stopStepsGroup();
    }

    @Test
    @Order(88)
    @Tag("Vehicles")
    public void switchVehicle() {
        sc.startStepsGroup("Test - Switch Vehicle");
        Switcher("JTMABABA0PA002944");
        sc.stopStepsGroup();
    }

    @Test
    @Order(89)
    @Tag("Vehicles")
    public void scanVin() {
        sc.startStepsGroup("Add Vehicle - Scan VIN");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        scanVinValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(90)
    @Tag("Shop")
    public void shopManageTrialSubscriptions() {
        sc.startStepsGroup("Test - Shop->Manage Subscription->Trial Subscriptions");
        SubaruShopManageSubscriptionsIOS.validateShopSubscriptionCard();
        SubaruShopManageSubscriptionsIOS.validateSubscriptionScreen();
        SubaruShopManageSubscriptionsIOS.validateSafetyConnect();
        SubaruShopManageSubscriptionsIOS.validateDriveConnect();
        SubaruShopManageSubscriptionsIOS.validateRemoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(91)
    @Tag("Shop")
    public void shopManagePaidSubscriptions() {
        sc.startStepsGroup("Test - Shop->Manage Subscription->Paid Subscriptions");
        SubaruShopManageSubscriptionsIOS.validateAddServiceScreen();
        SubaruShopManageSubscriptionsIOS.addDriveConnectPaidService();
        sc.stopStepsGroup();
    }

    @Test
    @Order(92)
    @Tag("VehicleInfo")
    public void vehicleInfoTest() {
        sc.startStepsGroup("Vehicle Info Details for Subaru -Test");
        createLog("Start: Vehicle Info Details for Subaru -Test");
        vehicleInfoDetails("JTMABABA0PA002944","2024  Solterra Limited");
        createLog("completed: Vehicle Info Details for Subaru -Test");
        sc.stopStepsGroup();
    }

    @Test
    @Order(93)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationsScreenTest(){
        sc.startStepsGroup("Test - Destinations Screen");
        validateDestinations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(94)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationsSearchTest(){
        sc.startStepsGroup("Test - Destinations Search");
        validateSearch();
        sc.stopStepsGroup();
    }

    @Test
    @Order(95)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationFavoritesTest(){
        sc.startStepsGroup("Test - Favorites");
        validateFavorites();
        sc.stopStepsGroup();
    }

    @Test
    @Order(96)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationSentToCarTest(){
        sc.startStepsGroup("Test - Sent To Car");
        validateSentToCar();
        sc.stopStepsGroup();
    }

    @Test
    @Order(97)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationHomeTest(){
        sc.startStepsGroup("Test - Destination Home");
        validateHome();
        sc.stopStepsGroup();
    }

    @Test
    @Order(98)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationWorkTest(){
        sc.startStepsGroup("Test - Destination Work");
        validateWork();
        sc.stopStepsGroup();
    }

    @Test
    @Order(99)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationRecentSectionTest(){
        sc.startStepsGroup("Test - Recent Destination Section");
        validateRecentSection();
        sc.stopStepsGroup();
    }

    @Test
    @Order(100)
    @Tag("Account")
    public void profilePictureTest() {
        sc.startStepsGroup("Profile Picture Validations");
        SubaruAccountsIOS.profilePicture();
        sc.stopStepsGroup();
    }

    @Test
    @Order(101)
    @Tag("Account")
    public void notificationsTest() {
        sc.startStepsGroup("Account->Notifications Test");
        SubaruAccountsIOS.notifications();
        sc.stopStepsGroup();
    }

    @Test
    @Order(102)
    @Tag("Account")
    public void darkModeValidationAccountsTest() throws IOException {
        sc.startStepsGroup("DarkMode Validation started");
        SubaruAccountsIOS.darkMode();
        SubaruAccountsIOS.changeToLightModeTheme();
        sc.startStepsGroup("DarkMode Validation Ended");

    }

    @Test
    @Order(103)
    @Tag("Account")
    public void takeATourOnAccountsScreenTest() {
        sc.startStepsGroup("Test - Accounts - Take A Tour");
        SubaruAccountsIOS.takeATour();
        sc.stopStepsGroup();
    }

    @Test
    @Order(104)
    @Tag("Account")
    @Tag("NotificationSettings")
    public void notificationSettingsTest() {
        sc.startStepsGroup("Test - Notification Settings");
        SubaruNotificationSettingsIOS.notificationSettings();
        sc.stopStepsGroup();
    }

    @Test
    @Order(105)
    @Tag("Account")
    @Tag("NotificationSettings")
    public void turnOffNotificationTest() {
        sc.startStepsGroup("Test - Turn Off Notification");
        SubaruNotificationSettingsIOS.turnOffNotification();
        sc.stopStepsGroup();
    }

    @Test
    @Order(106)
    @Tag("Account")
    @Tag("NotificationSettings")
    public void turnOnNotificationTest() {
        sc.startStepsGroup("Test - Turn On Notification");
        SubaruNotificationSettingsIOS.turnOnNotification();
        sc.stopStepsGroup();
    }

    @Test
    @Order(107)
    @Tag("VehicleInfo")
    @Tag("Subscriptions")
    public void trialSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Trial Subscriptions");
        SubaruSubscriptionIOS.validateSubscriptionCard();
        SubaruSubscriptionIOS.validateSubscriptionScreen();
        SubaruSubscriptionIOS.validateSafetyConnect();
        SubaruSubscriptionIOS.validateDriveConnect();
        SubaruSubscriptionIOS.validateRemoteConnect();
        sc.stopStepsGroup();
    }

    //Stage app needs to install please keep below tests at the end
    @Test
    @Order(108)
    @Tag("VehicleInfo")
    @Tag("Subscriptions")
    public void paidSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Paid Subscriptions");
        if(isStageRun) {
            createLog("Stage run - adding paid service");
            isStageApp = true;
            sc.install(ConfigSingleton.configMap.get("cloudAppStageSubaruIOS"), false, false);
            sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaruStage"), false, true);
            sc.syncElements(5000,10000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            ios_handlepopups();
            checkIsLoginScreenDisplayed_IOS();
            environmentSelection_iOS("stage");
            selectionOfCountry_IOS("USA");
            selectionOfLanguage_IOS("English");
            ios_keepMeSignedIn(true);
            ios_emailLogin("subarustage_21mm@mail.tmnact.io", "Test@222");
            //Paid Subscription With Auto Renew OFF
            SubaruSubscriptionIOS.validateSubscriptionCard();
            SubaruSubscriptionIOS.validateSubscriptionScreen();
            SubaruSubscriptionIOS.paidSubscriptionWithAutoRenewOnOff(false, false);
            //Paid Subscription With Auto Renew ON and add new credit card
            SubaruSubscriptionIOS.validateSubscriptionCard();
            SubaruSubscriptionIOS.validateSubscriptionScreen();
            SubaruSubscriptionIOS.paidSubscriptionWithAutoRenewOnOff(true, true);
        } else {
            createLog("Production run - skipping add paid service");
            SubaruSubscriptionIOS.validateAddServiceScreen();
            SubaruSubscriptionIOS.addDriveConnectPaidService();
        }
    }

    @Test
    @Order(109)
    @Tag("Pay")
    public void walletEVTest(){
        sc.startStepsGroup("Test - wallet EV");
        if(isStageRun) {
            createLog("Stage run - add or remove card");
            isStageApp = true;
            sc.install(ConfigSingleton.configMap.get("cloudAppStageSubaruIOS"), false, false);
            sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaruStage"), false, true);
            sc.syncElements(5000,10000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            ios_handlepopups();
            checkIsLoginScreenDisplayed_IOS();
            environmentSelection_iOS("stage");
            selectionOfCountry_IOS("USA");
            selectionOfLanguage_IOS("English");
            ios_keepMeSignedIn(true);
            ios_emailLogin("subarustage_21mm@mail.tmnact.io", "Test@222");
        } else {
            createLog("Production run - skipping add or remove card");
        }
        walletEV();
        sc.stopStepsGroup();
    }

    @Test
    @Order(110)
    @Tag("Account")
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }
}
