package v2update.subaru.android.canada.French.smoke;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static v2update.subaru.android.canada.French.accountSettings.SubaruAccountTestsCAFrenchAndroid.*;
import static v2update.subaru.android.canada.French.accountSettings.SubaruDriveConnectCAFrenchAndroid.driveConnectSettingsScreen;
import static v2update.subaru.android.canada.French.accountSettings.SubaruHelpAndFeedbackCAFrenchAndroid.contactUs;
import static v2update.subaru.android.canada.French.accountSettings.SubaruHelpAndFeedbackCAFrenchAndroid.vehicleSupport;
import static v2update.subaru.android.canada.French.accountSettings.SubaruLinkedAccountsCAFrenchAndroid.amazonMusic;
import static v2update.subaru.android.canada.French.accountSettings.SubaruNotificationSettingsCAFrenchAndroid.*;
import static v2update.subaru.android.canada.French.accountSettings.SubaruPersonalInfoCAFrenchAndroid.*;
import static v2update.subaru.android.canada.French.accountSettings.SubaruSecuritySettingsCAFrenchAndroid.*;
import static v2update.subaru.android.canada.French.bottomTab.SubaruBottomTabCAFrenchAndroid.*;
import static v2update.subaru.android.canada.French.dashboard.SubaruDashboardRefreshCAFrenchAndroid.refresh;
import static v2update.subaru.android.canada.French.dashboard.SubaruDashboardTopNavigationCAFrenchAndroid.topMenuNavigationValidations;
import static v2update.subaru.android.canada.French.ev.SubaruChargeManagementTestCAFrenchAndroid.*;
import static v2update.subaru.android.canada.French.health.SubaruHealthCAFrenchAndroid.vehicleAlerts;
import static v2update.subaru.android.canada.French.health.SubaruHealthCAFrenchAndroid.vehicleHealthReport;
import static v2update.subaru.android.canada.French.pay.SubaruPayCAFrenchAndroid.*;
import static v2update.subaru.android.canada.French.shop.SubaruManageSubscriptions21MMCAFrenchAndroid.*;
import static v2update.subaru.android.canada.French.status.SubaruVehicleStatusCAFrenchAndroid.*;
import static v2update.subaru.android.canada.French.vehicleInfo.SubaruConnectedServicesSupportCAFrenchAndroid.connectedServicesSupport;
import static v2update.subaru.android.canada.French.vehicleInfo.SubaruGloveBoxCAFrenchAndroid.validateCapabilities;
import static v2update.subaru.android.canada.French.vehicleInfo.SubaruUpdateNickNameCAFrenchAndroid.updateVehicleNickName;
import static v2update.subaru.android.canada.French.vehicles.SubaruNoVehicleTestCAFrenchAndroid.validateDashboardWithoutVehicle;
import static v2update.subaru.android.canada.French.vehicles.SubaruVehicleSelectionCAFrenchAndroid.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruSmokeTranslationsCAFrenchAndroid extends SeeTestKeywords {


    public SubaruSmokeTranslationsCAFrenchAndroid() {
        ConfigSingleton.INSTANCE.loadConfigProperties();
    }

    String testName = " - SmokeTestCAFrench-Android";

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
               android_emailLoginFrench("subarustageca@mail.tmnact.io","Test@1234");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarustageca@mail.tmnact.io","Test@1234");
               
                sc.stopStepsGroup();
        }
    }


        @AfterAll
    public void exit() {
        exitAll(testName);
    }

    @Test
    @Order(1)
    @Tag("Dashboard")
    public void bottomTab(){
        sc.startStepsGroup("Bottom Tab Android CA Franch");
        validateBottomTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    @Tag("Dashboard")
    public void serviceTab(){
        sc.startStepsGroup("Service Tab Android CA French");
        validateServiceTab();
        sc.stopStepsGroup();
    }
    @Test
    @Order(3)
    @Tag("Dashboard")
    public void payTab(){
        sc.startStepsGroup("Pay Tab Android CA French");
        validatePayTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    @Tag("Dashboard")
    public void homeTab(){
        sc.startStepsGroup("Home Tab Android CA French");
        validateHomeTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    @Tag("Dashboard")
    public void shopTab(){
        sc.startStepsGroup("Shop Tab Android CA French");
        validateShopTab();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    @Tag("Dashboard")
    public void findTab(){
        sc.startStepsGroup("Find Tab Android CA French");
        validateFindTab();
        sc.stopStepsGroup();
    }


    @Test
    @Order(7)
    @Tag("Dashboard")
    public void dashboardRefreshTest21MM() {
        sc.startStepsGroup("Test - Dashboard Refresh");
        refresh();
        sc.stopStepsGroup();
    }
    @Test
    @Order(8)
    @Tag("Dashboard")
    public void topNavigationFor21mm(){
        sc.startStepsGroup("Top Navigation on Dashboard-French");
        topMenuNavigationValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    @Tag("Account")
    public void profilePictureTest() {
        sc.startStepsGroup("Test-Account Settings page");
        validateAccountScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(10)
    @Tag("Account")
    public void notificationsScreen21mm(){
        sc.startStepsGroup("Test-Notifications screen");
        validateNotificationsScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(11)
    @Tag("Account")
    public void takeATourScreen21mm(){
        sc.startStepsGroup("Test-Take a tour screen");
        validateTakeTourScreen();
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    @Tag("Account")
    public void darkModeValidationTest() throws IOException {
        sc.startStepsGroup("DarkMode Validation started");
        darkModeValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    @Tag("Account")
    @Tag("DriveConnect")
    public void driveConnectSettingsTest() {
        sc.startStepsGroup("Test - Accounts -> Drive Connect Settings Screen");
        driveConnectSettingsScreen();
        sc.stopStepsGroup();
    }
    @Test
    @Order(14)
    @Tag("Account")
    @Tag("HelpAndFeedback")
    public void contactUsTest() {
        sc.startStepsGroup("Contact Us");
        contactUs();
        sc.stopStepsGroup();
    }
    @Test
    @Order(15)
    @Tag("Account")
    @Tag("HelpAndFeedback")
    public void vehicleSupportTest() {
        sc.startStepsGroup("Vehicle Support");
        vehicleSupport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(16)
    @Tag("Account")
    @Tag("LinkedAccounts")
    public void amazonMusicTest() {
        sc.startStepsGroup("Test - Amazon Music");
        amazonMusic();
        sc.stopStepsGroup();
    }


    @Test
    @Order(17)
    @Tag("Account")
    @Tag("NotificationSettings")
    public void notificationsSettingsTest() {
        sc.startStepsGroup("Notification Settings");
        notificationSettingsValidations();
        sc.stopStepsGroup();

    }
    //Test Case Description: Turn off or Disable all the notifications and verify it is disabled
    @Test
    @Order(18)
    @Tag("Account")
    @Tag("NotificationSettings")
    public void disableNotificationsTest() {
        sc.startStepsGroup("Disable Notifications");
        switchOFFNotificationsAlerts(returnAlerts());
        sc.stopStepsGroup();
    }

    //Test Case Description: Turn on or Enable all the notifications and verify it is enabled
    @Test
    @Order(19)
    @Tag("Account")
    @Tag("NotificationSettings")
    public void enableNotificationsTest() {
        sc.startStepsGroup("Enable Notifications");
        switchONNotificationsAlerts(returnAlerts());
        sc.stopStepsGroup();
    }

    @Test
    @Order(20)
    @Tag("Account")
    @Tag("PersonalInfo")
    public void personalDetailsTest() {
        sc.startStepsGroup("Personal Details");
        validatePersonalDetails();
        sc.stopStepsGroup();
    }

    @Test
    @Order(21)
    @Tag("Account")
    @Tag("PersonalInfo")
    public void homeAddressTest() {
        sc.startStepsGroup("Home Address");
        validateHomeAddress();
        sc.stopStepsGroup();
    }

    @Test
    @Order(22)
    @Tag("Account")
    @Tag("PersonalInfo")
    public void preferredLanguageTest() {
        sc.startStepsGroup("Preferred Language");
        validatePreferredLanguage();
        sc.stopStepsGroup();
    }
    @Test
    @Order(23)
    @Tag("Account")
    @Tag("SecuritySettings")
    public void securitySettingsTest() {
        sc.startStepsGroup("Security Settings");
        securitySettings();
        sc.stopStepsGroup();
    }

    @Test
    @Order(24)
    @Tag("Account")
    @Tag("SecuritySettings")
    public void setPINTest() {
        sc.startStepsGroup("Set PIN");
        setPIN("subarustageca@mail.tmnact.io", "Test$123");
        sc.stopStepsGroup();
    }

    @Test
    @Order(25)
    @Tag("Account")
    @Tag("SecuritySettings")
    public void manageSavedProfileTest() {
        sc.startStepsGroup("Manage Saved Profile");
        manageSavedProfile();
        sc.stopStepsGroup();
    }

    @Test
    @Order(26)
    @Tag("Account")
    @Tag("SecuritySettings")
    public void manageYourDataTest() {
        sc.startStepsGroup("Manage Your Data");
        manageYourData();
        sc.stopStepsGroup();
    }

    @Test
    @Order(27)
    @Tag("Dashboard")
    @Tag("Health")
    public void vehicleAlertsTest() {
        sc.startStepsGroup("Test - Vehicle Alerts validations");
        vehicleAlerts();
        sc.stopStepsGroup();
    }


    @Test
    @Order(28)
    @Tag("Dashboard")
    @Tag("Health")
    public void vehicleHealthReportTest() {
        sc.startStepsGroup("Test - Vehicle Health Report validations");
        vehicleHealthReport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(29)
    @Tag("Pay")
    public void wallet21mm() {
        sc.startStepsGroup("Pay-Wallet-21mm-CA French");
        wallet();
        sc.stopStepsGroup();

    }
    @Test
    @Order(30)
    @Tag("Pay")
    public void addPayment(){
        if (isStageApp==true){
            sc.startStepsGroup("Add new card CA french");
            addNewCard();
            sc.stopStepsGroup();
        }
    }
    @Test
    @Order(31)
    @Tag("Pay")
    public void editPayment(){
        if (isStageApp==true){
            sc.startStepsGroup("edit payment CA french");
            editPaymentMethod();
            sc.stopStepsGroup();
        }
    }
    @Test
    @Order(32)
    @Tag("Pay")
    public void removePayment(){
        if (isStageApp==true){
            sc.startStepsGroup("remove payment CA French");
            removePaymentMethod();
            sc.stopStepsGroup();
        }

    }
    @Test
    @Order(33)
    @Tag("Shop")
    public void subscriptionsScreenValidations() {
        sc.startStepsGroup("Subscriptions Screen Validation - PR Spanish");
        subscriptionsScreenValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(34)
    @Tag("Shop")
    public void subscriptionValidationsSafetyConnect() {
        sc.startStepsGroup("Subscriptions - Safety Connect Validation - PR Spanish");
        subscriptionValidationSafetyConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(35)
    @Tag("Shop")
    public void subscriptionValidationsServiceConnect() {
        sc.startStepsGroup("Subscriptions - Service Connect validation - PR Spanish");
        subscriptionValidationServiceConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(36)
    @Tag("Shop")
    public void subscriptionValidationsDriveConnect() {
        sc.startStepsGroup("Subscriptions - Drive Connect validation - PR Spanish");
        subscriptionValidationDriveConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(37)
    @Tag("Shop")
    public void subscriptionValidationsRemoteConnectDK() {
        sc.startStepsGroup("Subscriptions - Remote Connect With Digital Key - PR Spanish");
        subscriptionValidationRemoteConnectDK();
        sc.stopStepsGroup();
    }
    @Test
    @Order(38)
    @Tag("Status")
    public void tirePressureTest(){
        sc.startStepsGroup("Test - Tire Pressure");
        tirePressure();
        sc.stopStepsGroup();
    }

    @Test //Bug where Open/Unlock Status wont show in different languages
    @Order(39)
    @Tag("Status")
    public void doorsTest(){
        sc.startStepsGroup("Test - Doors");
        doors();
        sc.stopStepsGroup();
    }

    @Test
    @Order(40)
    @Tag("Status")
    public void windowsTest(){
        sc.startStepsGroup("Test - Windows");
        windows();
        sc.stopStepsGroup();
    }

    @Test
    @Order(41)
    @Tag("Status")
    public void vehicleInformationUpdatedTest(){
        sc.startStepsGroup("Test - Vehicle Information Updated");
        vehicleInformationUpdated();
        sc.stopStepsGroup();
    }

    @Test
    @Order(42)
    @Tag("VehicleInfo")
    public void connectedServicesTest(){
        sc.startStepsGroup("Test - Connected Services Support");
        connectedServicesSupport();
        sc.stopStepsGroup();
    }

    @Test
    @Order(43)
    @Tag("VehicleInfo")
    public void capabilitiesTest() {
        sc.startStepsGroup("Capabilities");
        validateCapabilities();
        sc.stopStepsGroup();
    }

    @Test
    @Order(44)
    @Tag("VehicleInfo")
    public void vehicleInfoSubscriptionsScreenValidationsTest() {
        sc.startStepsGroup("Subscriptions Screen Validation - PR Spanish");
        subscriptionsScreenValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(45)
    @Tag("VehicleInfo")
    public void vehicleInfoSubscriptionValidationsSafetyConnectTest() {
        sc.startStepsGroup("Subscriptions - Safety Connect Validation - PR Spanish");
        subscriptionValidationSafetyConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(46)
    @Tag("VehicleInfo")
    public void vehicleInfoSubscriptionValidationsRemoteConnectDKTest() {
        sc.startStepsGroup("Subscriptions - Remote Connect With Digital Key - PR Spanish");
        subscriptionValidationRemoteConnectDK();
        sc.stopStepsGroup();
    }


    @Test
    @Order(47)
    @Tag("VehicleInfo")
    public void vehicleInfoSubscriptionValidationsServiceConnectTest() {
        sc.startStepsGroup("Subscriptions - Service Connect validation - PR Spanish");
        subscriptionValidationServiceConnect();
        sc.stopStepsGroup();
    }

    @Test
    @Order(48)
    @Tag("VehicleInfo")
    public void vehicleInfoSubscriptionValidationsDriveConnectTest() {
        sc.startStepsGroup("Subscriptions - Drive Connect validation - PR Spanish");
        subscriptionValidationDriveConnect();
        sc.stopStepsGroup();
    }
    @Test
    @Order(49)
    @Tag("VehicleInfo")
    public void updateNickNameTest(){
        sc.startStepsGroup("Test - Update Nickname");
        updateVehicleNickName();
        sc.stopStepsGroup();
    }

    @Test
    @Order(50)
    @Tag("Vehicles")
    public void dashboardWithoutVehicleTest() {
        sc.startStepsGroup("Test - Dashboard with No Vehicle Registered CA French");
        validateDashboardWithoutVehicle();
        sc.stopStepsGroup();
    }

    @Test
    @Order(51)
    public void defaultVehicleTest() {
        sc.startStepsGroup("Test - Default Vehicle");
        Default("JTMABABA0PA002944");
        sc.stopStepsGroup();
    }

    @Test
    @Order(52)
    @Tag("Vehicles")
    public void switchVehicleTest() {
        sc.startStepsGroup("Test - Switch Vehicle");
        Switcher("JTMABABA0PA002944");
        sc.stopStepsGroup();
    }

    @Test
    @Order(53)
    @Tag("Vehicles")
    public void scanVinValidationsTest() {
        sc.startStepsGroup("Test - Scan VIN Validations");
        scanVIN();
        sc.stopStepsGroup();
    }
    @Test
    @Order(54)
    @Tag("Dashboard")
    public void chargeInfoTest() {
        sc.startStepsGroup("Charge Info");
        chargeInfoValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(55)
    @Tag("RemoteCommands")
    public void scheduleTest() {
        sc.startStepsGroup("schedule validations");
        scheduleValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(56)
    @Tag("RemoteCommands")
    public void createScheduleTest() {
        sc.startStepsGroup("Create schedule");
        createAndDeleteSchedule();
        sc.stopStepsGroup();
    }

    @Test
    @Order(57)
    @Disabled //Not available yet
    @Tag("Dashboard")
    public void findNearByStationTest() {
        sc.startStepsGroup("Find Near By Station");
        findNearByStationsValidations();
        sc.stopStepsGroup();
    }
    @Test
    @Order(58)
    @Tag("Dashboard")
    public void dashboardFindStationTest() throws IOException {
        sc.startStepsGroup("Test - Dashboard Find Station");
        dashboardFindStation();
        sc.stopStepsGroup();
    }
    @Test
    @Order(59)
    @Tag("Account")
    public void signOut() {
        sc.startStepsGroup("SignOut");
        android_SignOut();
        sc.stopStepsGroup();
    }
}