package v2update.subaru.ios.canada.french.smoke;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import v2update.subaru.ios.canada.french.pay.SubaruPayCAFrenchIOS;
import v2update.subaru.ios.canada.french.remote.SubaruGuestDriverCAFrenchIOS;
import v2update.subaru.ios.canada.french.remote.SubaruHapticTouchCAFrenchIOS;
import v2update.subaru.ios.canada.french.remote.SubaruRemoteEVCAFrenchIOS;
import v2update.subaru.ios.canada.french.status.SubaruVehicleStatusCAFrenchIOS;
import v2update.subaru.ios.canada.french.vehicleInfo.*;
import v2update.subaru.ios.canada.french.vehicles.SubaruNoVehicleCAFrenchIOS;
import v2update.subaru.ios.canada.french.vehicles.SubaruVehicleSelectionCAFrenchIOS;
import v2update.subaru.ios.canada.french.accountSettings.SubaruAccountsCAFrenchIOS;
import v2update.subaru.ios.canada.french.accountSettings.SubaruNotificationSettingsCAFrenchIOS;
import v2update.subaru.ios.canada.french.ev.SubaruChargeManagementCAFrenchIOS;
import v2update.subaru.ios.canada.french.shop.SubaruShopManageSubscriptionsCAFrenchIOS;
import v2update.subaru.ios.canada.french.vehicleInfo.SubaruSubscriptionCAFrenchIOS;

import java.io.IOException;

import static v2update.ios.canada.french.accountSettings.SecuritySettingsCAFrenchIOS.securitySettings;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruDataPrivacyPortalCAFrenchIOS.*;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruDataPrivacyPortalCAFrenchIOS.RemoteConnect;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruDriveConnectCAFrenchIOS.driveConnectSettingsScreen;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruHelpAndFeedbackCAFrenchIOS.contactUsSubaru;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruHelpAndFeedbackCAFrenchIOS.vehicleSupportSubaru;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruLinkedAccountsCAFrenchIOS.amazonMusic;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruLinkedAccountsCAFrenchIOS.appleMusic;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruPersonalInfoCAFrenchIOS.*;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruSecuritySettingsCAFrenchIOS.manageSavedProfile;
import static v2update.subaru.ios.canada.french.accountSettings.SubaruSecuritySettingsCAFrenchIOS.setPIN;
import static v2update.subaru.ios.canada.french.bottomTab.SubaruBottomTabCAFrenchIOS.*;
import static v2update.subaru.ios.canada.french.dashboard.SubaruDashboardRefreshCAFrenchIOS.refreshInRemoteSection;
import static v2update.subaru.ios.canada.french.dashboard.SubaruFuelWidgetCAFrenchIOS.fuelWidgetEV;
import static v2update.subaru.ios.canada.french.ev.SubaruChargeManagementCAFrenchIOS.*;
import static v2update.subaru.ios.canada.french.find.SubaruDealersCAFrenchIOS.validateDealers;
import static v2update.subaru.ios.canada.french.find.SubaruDestinationsCAFrenchIOS.*;
import static v2update.subaru.ios.canada.french.find.SubaruFindStationsCAFrenchIOS.verifySearchStation;
import static v2update.subaru.ios.canada.french.health.SubaruHealthCAFrenchIOS.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSmokeCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruSmoke-IOS";

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
        environmentSelection_iOS("stage");
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLogin("subarustageca@mail.tmnact.io","Test@1234");
     // ios_emailLogin(ConfigSingleton.configMap.get("strSubaruEmail"),ConfigSingleton.configMap.get("strSubaruPwd"));
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
        SubaruVehicleStatusCAFrenchIOS.doors();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    @Tag("Dashboard")
    @Tag("Status")
    public void windowsTest(){
        sc.startStepsGroup("Test - Windows");
        SubaruVehicleStatusCAFrenchIOS.windows();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    @Tag("Dashboard")
    @Tag("Status")
    public void vehicleInformationUpdatedTest(){
        sc.startStepsGroup("Test - Vehicle Information Updated");
        SubaruVehicleStatusCAFrenchIOS.vehicleInformationUpdated();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    @Tag("Dashboard")
    @Tag("Status")
    public void pullDownRefreshStatusSectionTest() {
        sc.startStepsGroup("Test - Pull Down Refresh Status Section");
        SubaruVehicleStatusCAFrenchIOS.pullDownRefreshInStatusSection();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    @Tag("RemoteCommands")
    public void RemoteUnLock(){
        sc.startStepsGroup("Test - SubaruEV remote Unlock");
        clearPNS();
        SubaruRemoteEVCAFrenchIOS.executeRemoteUnLock();
        PNS("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    @Tag("RemoteCommands")
    public void RemoteLock(){
        sc.startStepsGroup("Test - SubaruEV remote Lock");
        SubaruRemoteEVCAFrenchIOS.executeRemoteLock();
        PNS("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    @Tag("RemoteCommands")
    public void RemoteStart(){
        sc.startStepsGroup("Test - SubaruEV remote Start");
        SubaruRemoteEVCAFrenchIOS.executeRemoteStart();
        PNS("EVStart");
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    @Tag("RemoteCommands")
    public void RemoteStop(){
        sc.startStepsGroup("Test - SubaruEV remote Stop");
        SubaruRemoteEVCAFrenchIOS.executeRemoteStop();
        PNS("Stop");
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    @Tag("RemoteCommands")
    public void RemoteLights(){
        sc.startStepsGroup("Test - SubaruEV remote Lights");
        SubaruRemoteEVCAFrenchIOS.executeRemoteLights();
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    @Tag("RemoteCommands")
    public void RemoteHazard(){
        sc.startStepsGroup("Test - SubaruEV remote Hazard");
        SubaruRemoteEVCAFrenchIOS.executeRemoteHazard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(14)
    @Tag("RemoteCommands")
    public void RemoteBuzzer(){
        sc.startStepsGroup("Test - SubaruEV remote Buzzer");
        SubaruRemoteEVCAFrenchIOS.executeRemoteBuzzer();
        sc.stopStepsGroup();
    }

    @Test
    @Order(15)
    @Tag("RemoteCommands")
    public void RemoteTrunkUnLock(){
        sc.startStepsGroup("Test - SubaruEV remote Trunk Unlock");
        SubaruRemoteEVCAFrenchIOS.executeRemoteTrunkUnlock();
        PNS("Trunk Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(16)
    @Tag("RemoteCommands")
    public void RemoteTrunkLock(){
        sc.startStepsGroup("Test - SubaruEV remote Trunk Lock ");
        SubaruRemoteEVCAFrenchIOS.executeRemoteTrunkLock();
        PNS("Trunk Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(17)
    @Tag("RemoteCommands")
    public void RemoteHorn(){
        sc.startStepsGroup("Test - SubaruEV remote Horn");
        SubaruRemoteEVCAFrenchIOS.executeRemoteHorn();
        PNS("Horn");
        sc.stopStepsGroup();
    }

    @Test
    @Order(18)
    @Tag("RemoteCommands")
    public void StatusScreenDoorsUnlockLockTest(){
        sc.startStepsGroup("Test - Status Screen Doors Unlock Lock Validation");
        SubaruRemoteEVCAFrenchIOS.statusDoorsUnlockLockValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(19)
    @Tag("RemoteCommands")
    public void ClimateScreen(){
        sc.startStepsGroup("Test - SubaruEV remote schedule screen");
        SubaruRemoteEVCAFrenchIOS.GotoClimateScheduleSubaruEV();
        sc.stopStepsGroup();
    }

    @Test
    @Order(20)
    @Tag("RemoteCommands")
    public void RemoteClimateSettingsOn(){
        sc.startStepsGroup("Test - SubaruEV remote Climate Settings On");
        SubaruRemoteEVCAFrenchIOS.executeRemoteClimateSettingsOn();
        sc.stopStepsGroup();
    }

    @Test
    @Order(21)
    @Tag("RemoteCommands")
    public void RemoteClimateSettingsOFF(){
        sc.startStepsGroup("Test - SubaruEV remote Climate Settings Off");
        SubaruRemoteEVCAFrenchIOS.executeRemoteClimateSettingsOff();
        sc.stopStepsGroup();
    }

    @Test
    @Order(22)
    @Tag("RemoteCommands")
    public void deleteScheduleTest() {
        sc.startStepsGroup("Test - Delete All Schedules ");
        SubaruRemoteEVCAFrenchIOS.deleteSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(23)
    @Tag("RemoteCommands")
    public void createScheduleTest() {
        sc.startStepsGroup("Test - Create Schedule ");
        SubaruRemoteEVCAFrenchIOS.createSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(24)
    @Tag("RemoteCommands")
    public void editScheduleTest() {
        sc.startStepsGroup("Test - Modify Schedule ");
        SubaruRemoteEVCAFrenchIOS.editSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(25)
    @Tag("RemoteCommands")
    public void hapticUnlock() {
        sc.startStepsGroup("Subaru Haptic Touch Unlock");
        clearPNS();
        SubaruHapticTouchCAFrenchIOS.appFinderHapticTouch();
        SubaruHapticTouchCAFrenchIOS.executeHapticTouchUnLock();
        PNS("Unlock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(26)
    @Tag("RemoteCommands")
    public void hapticLock() {
        sc.startStepsGroup("Subaru Haptic Touch Lock");
        SubaruHapticTouchCAFrenchIOS.appFinderHapticTouch();
        SubaruHapticTouchCAFrenchIOS.executeHapticTouchLock();
        PNS("Lock");
        sc.stopStepsGroup();
    }

    @Test
    @Order(27)
    @Tag("RemoteCommands")
    public void hapticStart() {
        sc.startStepsGroup("Subaru Haptic Touch Start");
        SubaruHapticTouchCAFrenchIOS.appFinderHapticTouch();
        SubaruHapticTouchCAFrenchIOS.executeHapticTouchStart();
        PNS("Start");
        sc.stopStepsGroup();
    }

    @Test
    @Order(28)
    @Tag("RemoteCommands")
    public void hapticStop() {
        sc.startStepsGroup("Subaru Remote Stop");
        SubaruHapticTouchCAFrenchIOS.executeRemoteStop();
        PNS("Stop");
        sc.stopStepsGroup();
    }

    @Test
    @Order(29)
    @Tag("VehicleInfo")
    public void vehicleCapabilitiesTest() {
        sc.startStepsGroup("Test - Vehicle Capabilities");
        SubaruGloveBoxCAFrenchIOS.validateVehicleCapabilities();
        sc.stopStepsGroup();
    }

    @Test
    @Order(30)
    @Tag("VehicleInfo")
    public void updateNickNameTest(){
        sc.startStepsGroup("Test - Update Nickname");
        SubaruUpdateNickNameCAFrenchIOS.updateVehicleNickName("JTMABABA8RA060836");
        sc.stopStepsGroup();
    }

    @Test
    @Order(31)
    @Tag("VehicleInfo")
    public void vehicleSoftwareUpdateTest(){
        sc.startStepsGroup("Test - Vehicle Software Update");
        SubaruVehicleSoftwareUpdateCAFrenchIOS.vehicleSoftwareUpdate();
        sc.stopStepsGroup();
    }
    @Test
    @Order(32)
    @Tag("AccountSettings")
    @Tag("PersonalInfo")
    public void personalInfo() {
        sc.startStepsGroup("Test- Personal Details");
        personalDetails();
        sc.stopStepsGroup();
    }
    @Test
    @Order(33)
    @Tag("AccountSettings")
    @Tag("PersonalInfo")
    public void homeaddress() {
        sc.startStepsGroup("Test - Home Address");
        homeAddress();
        sc.stopStepsGroup();
    }
    @Test
    @Order(34)
    @Tag("AccountSettings")
    @Tag("PersonalInfo")
    public void preferredlanguage() {
        sc.startStepsGroup("Test - Preferred Language");
        preferredLanguage();
        sc.stopStepsGroup();
    }
    @Test
    @Order(35)
    @Tag("AccountSettings")
    @Tag("SecuritySettings")
    public void securitySettingsSubaru() {
        sc.startStepsGroup("Test - security settings");
        securitySettings();
        sc.stopStepsGroup();
    }
    @Test
    @Order(36)
    @Tag("AccountSettings")
    @Tag("SecuritySettings")
    public void setpin() {
        sc.startStepsGroup("Test - Set or Reset pin");
        setPIN(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        sc.stopStepsGroup();
    }
    @Test
    @Order(37)
    @Tag("AccountSettings")
    @Tag("SecuritySettings")
    public void managedsavedprofile() {
        sc.startStepsGroup("Test - Managed Saved Profile");
        manageSavedProfile();
        sc.stopStepsGroup();
    }
    @Test
    @Order(38)
    @Tag("AccountSettings")
    @Tag("LinkedAccounts")
    public void applemusicTest() {
        sc.startStepsGroup("Test - Apple Music");
        appleMusic();
        sc.stopStepsGroup();
    }
    @Test
    @Order(39)
    @Tag("AccountSettings")
    @Tag("LinkedAccounts")
    public void amazonmusicTest() {
        sc.startStepsGroup("Test - Amazon Music");
        amazonMusic();
        sc.stopStepsGroup();
    }
    @Test
    @Order(40)
    @Tag("AccountSettings")
    @Tag("DriveConnect")
    public void driveconnectsettings() {
        sc.startStepsGroup("Test - Drive Connect Settings");
        driveConnectSettingsScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(41)
    @Tag("Dashboard")
    public void bottomTab(){
        sc.startStepsGroup("Bottom Tab IOS");
        validateBottomTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(42)
    @Tag("Dashboard")
    public void serviceTab(){
        sc.startStepsGroup("Service Tab IOS");
        validateServiceTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(43)
    @Tag("Dashboard")
    public void payTab(){
        sc.startStepsGroup("Pay Tab IOS");
        validatePayTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(44)
    @Tag("Dashboard")
    public void homeTab(){
        sc.startStepsGroup("Home Tab IOS");
        validateHomeTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(45)
    @Tag("Dashboard")
    public void shopTab(){
        sc.startStepsGroup("Shop Tab IOS");
        validateShopTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(46)
    @Tag("Dashboard")
    public void findTab(){
        sc.startStepsGroup("Find Tab IOS");
        validateFindTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(47)
    @Tag("Dashboard")
    public void dashboardRefreshTest() {
        sc.startStepsGroup("Test - Dashboard Refresh");
        refreshInRemoteSection();
        sc.stopStepsGroup();
    }

    @Test
    @Order(48)
    @Tag("Dashboard")
    public void fuelWidgetEVTest() throws IOException {
        sc.startStepsGroup("Fuel Widget EV");
        fuelWidgetEV();
        sc.stopStepsGroup();
    }
    @Test
    @Order(49)
    @Tag("ChargeManagement")
    public void chargeInfoTest() {
        sc.startStepsGroup("Test - Charge Information section");
        chargeInfoSection();
        sc.stopStepsGroup();
    }
    @Test
    @Order(50)
    @Tag("ChargeManagement")
    public void scheduleTest() {
        sc.startStepsGroup("Test - Schedule Test Card ");
        scheduleValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(51)
    @Tag("ChargeManagement")
    public void createScheduleEVTest() {
        sc.startStepsGroup("Test - Create Schedule ");
        SubaruChargeManagementCAFrenchIOS.createSchedule();
        sc.stopStepsGroup();
    }
    @Test
    @Order(52)
    @Tag("ChargeManagement")
    public void findStationTest() throws IOException {
        sc.startStepsGroup("Test - Find Station");
        findStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(53)
    @Tag("ChargeManagement")
    public void dashboardFindStationTest() throws IOException {
        sc.startStepsGroup("Test - Dashboard Find Station");
        dashboardFindStation();
        sc.stopStepsGroup();
    }
    @Test
    @Order(54)
    @Tag("Find")
    public void dealers(){
        sc.startStepsGroup("Test - Dealers");
        validateDealers();
        sc.stopStepsGroup();
    }
    @Test
    @Order(55)
    @Tag("Find")
    public void findStationOnFind() throws IOException {
        sc.startStepsGroup("Test - Find->Stations EV");
        findStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(56)
    @Tag("Find")
    public void searchStation() throws IOException {
        sc.startStepsGroup("Search Station");
        verifySearchStation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(57)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void GuestDriveHomePage() {
        sc.startStepsGroup("Guest Driver Home page validations");
        SubaruGuestDriverCAFrenchIOS.guestDriverHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(58)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestAndValetAlerts() {
        sc.startStepsGroup("Start: Guest And Valet Alerts Verification");
        //check Valet alerts is On or oFF
        SubaruGuestDriverCAFrenchIOS.guestAlertsValidation();
        //check Guest alerts is On or oFF
        SubaruGuestDriverCAFrenchIOS.valetAlertsValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(59)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestSettingsHomePage() {
        sc.startStepsGroup("Guest Setting home page");
        SubaruGuestDriverCAFrenchIOS.guestSettingsHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(60)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void valetSettingsHomePage() {
        sc.startStepsGroup("Valet Setting home page validations");
        SubaruGuestDriverCAFrenchIOS.valetSettingsHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(61)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsSpeed() {
        sc.startStepsGroup("Valet Driving Limits Speed");
        SubaruGuestDriverCAFrenchIOS.drivingLimitSpeed("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(62)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsMiles() {
        sc.startStepsGroup("Valet Driving Limits Miles");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsMiles("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(63)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsCurfew() {
        sc.startStepsGroup("Valet Driving Limits Curfew");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsCurfew("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(64)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsTime() {
        sc.startStepsGroup("Valet Driving Limits Time");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsTime("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(65)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsArea() {
        sc.startStepsGroup("Valet Driving Limits Area");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsArea("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(66)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversValetDrivingLimitsIgnition() {
        sc.startStepsGroup("Valet Driving Limits Ignition");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsIgnition("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(67)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsSpeed() {
        sc.startStepsGroup("Guest Driving Limits Speed");
        SubaruGuestDriverCAFrenchIOS.drivingLimitSpeed("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(68)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsMiles() {
        sc.startStepsGroup("Guest Driving Limits Miles");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsMiles("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(69)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsCurfew() {
        sc.startStepsGroup("Guest Driving Limits Curfew");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsCurfew("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(70)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsTime() {
        sc.startStepsGroup("Guest Driving Limits Time");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsTime("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(71)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsArea() {
        sc.startStepsGroup("Guest Driving Limits Area");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsArea("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(72)
    @Tag("RemoteCommands")
    @Tag("GuestDriver")
    public void guestDriversGuestDrivingLimitsIgnition() {
        sc.startStepsGroup("Guest Driving Limits Ignition");
        SubaruGuestDriverCAFrenchIOS.drivingLimitsIgnition("guest");
        sc.stopStepsGroup();
    }
    @Test
    @Order(73)
    @Tag("Vehicles")
    public void dashboardWithoutVehicle() {
        sc.startStepsGroup("Test - Dashboard with No Vehicle Registered");
        SubaruNoVehicleCAFrenchIOS.validateDashboardWithoutVehicle();
        sc.stopStepsGroup();
    }
    @Test
    @Order(74)
    @Tag("Vehicles")
    public void defaultVehicle() {
        sc.startStepsGroup("Test - Default Vehicle");
        SubaruVehicleSelectionCAFrenchIOS.Default("JTMABABA8RA060836");
        sc.stopStepsGroup();
    }

    @Test
    @Order(75)
    @Tag("Vehicles")
    public void switchVehicle() {
        sc.startStepsGroup("Test - Switch Vehicle");
        SubaruVehicleSelectionCAFrenchIOS.Switcher("JTMABABA8RA060836");
        sc.stopStepsGroup();
    }

    @Test
    @Order(76)
    @Tag("Vehicles")
    public void scanVin() {
        sc.startStepsGroup("Add Vehicle - Scan VIN");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        SubaruVehicleSelectionCAFrenchIOS.scanVinValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(77)
    @Tag("Shop")
    public void shopManageTrialSubscriptions() {
        sc.startStepsGroup("Test - Shop->Manage Subscription->Trial Subscriptions");
        SubaruShopManageSubscriptionsCAFrenchIOS.validateShopSubscriptionCard();
        SubaruShopManageSubscriptionsCAFrenchIOS.validateSubscriptionScreen();
        SubaruShopManageSubscriptionsCAFrenchIOS.validateSafetyConnect();
        SubaruShopManageSubscriptionsCAFrenchIOS.validateDriveConnect();
        SubaruShopManageSubscriptionsCAFrenchIOS.validateRemoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(78)
    @Tag("Shop")
    public void shopManagePaidSubscriptions() {
        sc.startStepsGroup("Test - Shop->Manage Subscription->Paid Subscriptions");
        SubaruShopManageSubscriptionsCAFrenchIOS.validateAddServiceScreen();
        SubaruShopManageSubscriptionsCAFrenchIOS.addDriveConnectPaidService();
        sc.stopStepsGroup();
    }

    @Test
    @Order(79)
    @Tag("VehicleInfo")
    public void vehicleInfoTest() {
        sc.startStepsGroup("Vehicle Info Details for Subaru -Test");
        createLog("Start: Vehicle Info Details for Subaru -Test");
        SubaruVehicleInfoDetailsCAFrenchIOS.vehicleInfoDetails("JTMABABA8RA060836","2024  Solterra Limited");
        createLog("completed: Vehicle Info Details for Subaru -Test");
        sc.stopStepsGroup();
    }

    @Test
    @Order(80)
    @Tag("VehicleInfo")
    @Tag("Subscriptions")
    public void trialSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Trial Subscriptions");
        SubaruSubscriptionCAFrenchIOS.validateSubscriptionCard();
        SubaruSubscriptionCAFrenchIOS.validateSubscriptionScreen();
        SubaruSubscriptionCAFrenchIOS.validateSafetyConnect();
        SubaruSubscriptionCAFrenchIOS.validateDriveConnect();
        SubaruSubscriptionCAFrenchIOS.validateRemoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(81)
    @Tag("VehicleInfo")
    @Tag("Subscriptions")
    public void paidSubscriptions() {
        sc.startStepsGroup("Test - Vehicle Info->Paid Subscriptions");
        SubaruSubscriptionCAFrenchIOS.validateAddServiceScreen();
        SubaruSubscriptionCAFrenchIOS.addDriveConnectPaidService();
        sc.stopStepsGroup();
    }

    @Test
    @Order(82)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationsScreenTest(){
        sc.startStepsGroup("Test - Destinations Screen");
        validateDestinations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(83)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationsSearchTest(){
        sc.startStepsGroup("Test - Destinations Search");
        validateSearch();
        sc.stopStepsGroup();
    }

    @Test
    @Order(84)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationFavoritesTest(){
        sc.startStepsGroup("Test - Favorites");
        validateFavorites();
        sc.stopStepsGroup();
    }

    @Test
    @Order(85)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationSentToCarTest(){
        sc.startStepsGroup("Test - Sent To Car");
        validateSentToCar();
        sc.stopStepsGroup();
    }

    @Test
    @Order(86)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationHomeTest(){
        sc.startStepsGroup("Test - Destination Home");
        validateHome();
        sc.stopStepsGroup();
    }

    @Test
    @Order(87)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationWorkTest(){
        sc.startStepsGroup("Test - Destination Work");
        validateWork();
        sc.stopStepsGroup();
    }

    @Test
    @Order(88)
    @Tag("Find")
    @Tag("Destinations")
    public void destinationRecentSectionTest(){
        sc.startStepsGroup("Test - Recent Destination Section");
        validateRecentSection();
        sc.stopStepsGroup();
    }

    @Test
    @Order(89)
    @Tag("Pay")
    public void walletEVTest(){
        sc.startStepsGroup("Test - wallet EV");
        SubaruPayCAFrenchIOS.walletEV();
        sc.stopStepsGroup();
    }
    @Test
    @Order(90)
    @Tag("Account")
    public void profilePictureTest() {
        sc.startStepsGroup("Profile Picture Validations");
        SubaruAccountsCAFrenchIOS.profilePicture();
        sc.stopStepsGroup();
    }

    @Test
    @Order(91)
    @Tag("Account")
    public void notificationsTest() {
        sc.startStepsGroup("Account->Notifications Test");
        SubaruAccountsCAFrenchIOS.notifications();
        sc.stopStepsGroup();
    }

    @Test
    @Order(92)
    @Tag("Account")
    public void darkModeValidationAccountsTest() throws IOException {
        sc.startStepsGroup("DarkMode Validation started");
        SubaruAccountsCAFrenchIOS.darkMode();
        SubaruAccountsCAFrenchIOS.changeToLightModeTheme();
        sc.startStepsGroup("DarkMode Validation Ended");

    }

    @Test
    @Order(93)
    @Tag("Account")
    public void takeATourOnAccountsScreenTest() {
        sc.startStepsGroup("Test - Accounts - Take A Tour");
        SubaruAccountsCAFrenchIOS.takeATour();
        sc.stopStepsGroup();
    }

    @Test
    @Order(94)
    @Tag("Account")
    @Tag("NotificationSettings")
    public void notificationSettingsTest() {
        sc.startStepsGroup("Test - Notification Settings");
        SubaruNotificationSettingsCAFrenchIOS.notificationSettings();
        sc.stopStepsGroup();
    }

    @Test
    @Order(95)
    @Tag("Account")
    @Tag("NotificationSettings")
    public void turnOffNotificationTest() {
        sc.startStepsGroup("Test - Turn Off Notification");
        SubaruNotificationSettingsCAFrenchIOS.turnOffNotification();
        sc.stopStepsGroup();
    }

    @Test
    @Order(96)
    @Tag("Account")
    @Tag("NotificationSettings")
    public void turnOnNotificationTest() {
        sc.startStepsGroup("Test - Turn On Notification");
        SubaruNotificationSettingsCAFrenchIOS.turnOnNotification();
        sc.stopStepsGroup();
    }

    @Test
    @Order(97)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void _DataConsentTest()  {
        sc.startStepsGroup("Test - Data Consent Page");
        DataConsent();
        sc.stopStepsGroup();
    }

    @Test
    @Order(98)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void MasterDataConsentTest() {
        sc.startStepsGroup("Test - Master Data Consent");
        MasterDataConsent();
        sc.stopStepsGroup();
    }

    @Test
    @Order(99)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void DriveConnectTest() {
        sc.startStepsGroup("Test - Drive Connect");
        DriveConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(100)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void RemoteConnectTest() {
        sc.startStepsGroup("Test - Remote Connect");
        RemoteConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(101)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void SafetyConnectTest() {
        sc.startStepsGroup("Test - Safety Connect");
        SafetyConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(102)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void ServiceConnectTest() {
        sc.startStepsGroup("Test- Safety Connect");
        ServiceConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(103)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void _AcceptDeclineMasterDataConsentValidationTest() {
        sc.startStepsGroup("Test - Accept or Decline Master Data Consent");
        AcceptDeclineMasterDataConsentValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(104)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void _DeclineServiceConnectCommunicationTest() {
        sc.startStepsGroup("Test- Decline Service Connect Communication");
        DeclineServiceConnectCommunication();
        sc.stopStepsGroup();
    }

    @Test
    @Order(105)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void _AcceptServiceConnectCommunicationTest() {
        sc.startStepsGroup("Test - Accept Service Connect ");
        AcceptServiceConnectCommunication();
        sc.stopStepsGroup();
    }

    @Test
    @Order(106)
    @Tag("Account")
    @Tag("DataPrivacyPortal")
    public void _PrivacyAndTermsOfUseTest() {
        sc.startStepsGroup("Test - Privacy & terms of use ");
        PrivacyAndTermsOfUse();
        sc.stopStepsGroup();
    }

    @Test
    @Order(107)
    @Tag("Account")
    @Tag("HelpAndFeedback")
    public void contactUsSubaru21MMTest() {
        sc.startStepsGroup("Test - Contact us ");
        contactUsSubaru();
        sc.stopStepsGroup();
    }

    @Test
    @Order(108)
    @Tag("Account")
    @Tag("HelpAndFeedback")
    public void vehicleSupportSubaru21MMTest() {
        sc.startStepsGroup("Test - Vehicle Support");
        vehicleSupportSubaru();
        sc.stopStepsGroup();
    }

    @Test
    @Order(109)
    @Tag("Account")
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }
}
