package v2update.subaru.ios.usa.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import v2update.ios.usa.remote.Remote21MMIOS;
import v2update.ios.usa.vehicles.VehicleSelectionIOS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruGuestDriverIOS extends SeeTestKeywords {
    String testName = "Dev Test Guest - IOS";

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
        environmentSelection_iOS("prod");
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();

    }
    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void GuestDriveHomePage() {
        sc.startStepsGroup("Guest Driver Home page validations");
        guestDriverHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    @Disabled
    public void GuestDriverShareRemote() {
        sc.startStepsGroup("Guest Driver Share Remote");
        guestDriverRemoteShare();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    @Disabled
    public void LogoutPrimary() {
        sc.startStepsGroup("Test - Sign out Primary");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    @Disabled
    public void LoginWithGuestDriver() {
        sc.startStepsGroup("Email Login Guest");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("stageGuestDriver_username"), ConfigSingleton.configMap.get("stageGuestDriver_password"));
        VehicleSelectionIOS.Default("JT2AE71D0D0014320");
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    @Disabled
    public void ActivateGuestRemote() throws Exception {
        sc.startStepsGroup("Activating  Guest Remote");
        activateGuest();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    @Disabled
    public void GuestLogout() {
        sc.startStepsGroup("Sign out Guest email");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    @Disabled
    public void LoginWithGuest() {
        sc.startStepsGroup("Email Login Guest");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("stageGuestDriver_username"), ConfigSingleton.configMap.get("stageGuestDriver_password"));
        VehicleSelectionIOS.Default("JT2AE71D0D0014320");
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    @Disabled
    public void ValidateRemoteCommandsForGuest() {
        sc.startStepsGroup("Validate Remote commands on Guest Driver");
        validateRemoteCommandsOnDashboard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(9)
    @Disabled
    public void RemoteLockGuest() {
        sc.startStepsGroup("Validate Guest Remote Lock");
        Remote21MMIOS.executeRemoteLock();
        sc.stopStepsGroup();
    }

    @Test
    @Order(10)
    @Disabled
    public void LogoutGuest() {
        sc.startStepsGroup("Sign out Guest email");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(11)
    @Disabled
    public void EmailLoginPrimary() {
        sc.startStepsGroup("Email Login Primary");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("stagetesting_21mm"), ConfigSingleton.configMap.get("stagetesting_21mmPw"));
        VehicleSelectionIOS.Default("JT2AE71D0D0014320");
        sc.stopStepsGroup();
    }

    @Test
    @Order(12)
    @Disabled
    public void RemoveDriver() {
        sc.startStepsGroup("Remove Driver");
        removeDriver();
        sc.stopStepsGroup();
    }

    @Test
    @Order(13)
    @Disabled
    public void ActivatePrimaryRemote() throws Exception {
        sc.startStepsGroup("Activate Primary Remote");
        activatePrimaryRemote();
        sc.stopStepsGroup();
    }

    @Test
    @Order(14)
    @Disabled
    public void PrimaryUserLogout() {
        sc.startStepsGroup("Sign out primary user");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(15)
    @Disabled
    public void EmailLoginPrimaryForRemoteVerification() {
        sc.startStepsGroup("Email Login Primary For remote verification");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("stagetesting_21mm"), ConfigSingleton.configMap.get("stagetesting_21mmPw"));
        VehicleSelectionIOS.Default("JT2AE71D0D0014320");
        sc.stopStepsGroup();
    }

    @Test
    @Order(16)
    @Disabled
    public void validateRemoteCommands() {
        sc.startStepsGroup("Validate Remote commands on primary Driver");
        validateRemoteCommandsOnDashboard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(17)
    @Disabled
    public void RemoteLockPrimary() {
        sc.startStepsGroup("Validate Primary Remote Lock");
        Remote21MMIOS.executeRemoteLock();
        sc.stopStepsGroup();
    }

    @Test
    @Order(18)
    @Disabled
    public void PrimaryLogout() {
        sc.startStepsGroup("Test - Sign out Primary");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }


    @Test
    @Order(19)
    public void guestAndValetAlerts() {
        sc.startStepsGroup("Start: Guest And Valet Alerts Verification");
        //check Valet alerts is On or oFF
        guestAlertsValidation();
        //check Guest alerts is On or oFF
        valetAlertsValidation();
        sc.stopStepsGroup();
    }

    @Test
    @Order(20)
    public void guestSettingsHomePage() {
        sc.startStepsGroup("Guest Setting home page");
        guestSettingsHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(21)
    public void valetSettingsHomePage() {
        sc.startStepsGroup("Valet Setting home page validations");
        valetSettingsHomePageValidations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(22)
    public void guestDriversValetDrivingLimitsSpeed() {
        sc.startStepsGroup("Valet Driving Limits Speed");
        drivingLimitSpeed("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(23)
    public void guestDriversValetDrivingLimitsMiles() {
        sc.startStepsGroup("Valet Driving Limits Miles");
        drivingLimitsMiles("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(24)
    public void guestDriversValetDrivingLimitsCurfew() {
        sc.startStepsGroup("Valet Driving Limits Curfew");
        drivingLimitsCurfew("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(25)
    public void guestDriversValetDrivingLimitsTime() {
        sc.startStepsGroup("Valet Driving Limits Time");
        drivingLimitsTime("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(26)
    public void guestDriversValetDrivingLimitsArea() {
        sc.startStepsGroup("Valet Driving Limits Area");
        drivingLimitsArea("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(27)
    public void guestDriversValetDrivingLimitsIgnition() {
        sc.startStepsGroup("Valet Driving Limits Ignition");
        drivingLimitsIgnition("valet");
        sc.stopStepsGroup();
    }

    @Test
    @Order(28)
    public void guestDriversGuestDrivingLimitsSpeed() {
        sc.startStepsGroup("Guest Driving Limits Speed");
        drivingLimitSpeed("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(29)
    public void guestDriversGuestDrivingLimitsMiles() {
        sc.startStepsGroup("Guest Driving Limits Miles");
        drivingLimitsMiles("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(30)
    public void guestDriversGuestDrivingLimitsCurfew() {
        sc.startStepsGroup("Guest Driving Limits Curfew");
        drivingLimitsCurfew("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(31)
    public void guestDriversGuestDrivingLimitsTime() {
        sc.startStepsGroup("Guest Driving Limits Time");
        drivingLimitsTime("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(32)
    public void guestDriversGuestDrivingLimitsArea() {
        sc.startStepsGroup("Guest Driving Limits Area");
        drivingLimitsArea("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(33)
    public void guestDriversGuestDrivingLimitsIgnition() {
        sc.startStepsGroup("Guest Driving Limits Ignition");
        drivingLimitsIgnition("guest");
        sc.stopStepsGroup();
    }

    @Test
    @Order(34)
    public void signOut() {
        sc.startStepsGroup("Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void drivingLimitSpeed(String profile) {
        createLog("Started: "+profile+" Driving Limit Speed");
        navigateToSharedRemotePage();
        selectProfile(profile);
        //verify driving limits
        verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
        click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);

        //speed validation
        createLog("Speed validation");
        verifyElementFound("NATIVE", "xpath=//*[@text='Speed' and @class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=((//*[@class='UIAScrollView']/*[@class='UIAView'])[1]/*[@text='Speed' and @class='UIAStaticText'])[1]", 0);

        String firstSpeedDetails = sc.elementGetProperty("NATIVE","xpath=((//*[@class='UIAScrollView']/*[@class='UIAView'])[1]/*[@text='Speed' and @class='UIAStaticText'])[2]",0,"id");
        createLog("First Speed Details :"+firstSpeedDetails);
        click("NATIVE", "xpath=//*[@text='Speed' and @class='UIAImage']", 0, 1);
        sc.syncElements(3000, 30000);

        //speed details page
        createLog("Speed details page");
        verifyElementFound("NATIVE", "xpath=//*[@id='speed_title']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='speed_switch']", 0);

        //Defect already opened for Toyota/Lexus to make it mph
        sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@id='90 mi/h']", 0, 1000, 5, false);
        sc.swipe("Down", sc.p2cy(60), 2000);
        sc.swipe("Down", sc.p2cy(60), 2000);
        //select speed
        createLog("Select speed and verify it's reflected");
        verifyElementFound("NATIVE", "xpath=//*[@id='70 mi/h']", 0);
        click("NATIVE", "xpath=//*[@id='70 mi/h']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='speed_switch_title']/following-sibling::*[@text='70 mi/h']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Back']", 0);
        click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
        sc.syncElements(2000, 4000);
        //Speed header and value is are same. Need dev to look into it.
        //https://toyotaconnected.atlassian.net/browse/OAD01-25123
        //verifyElementFound("NATIVE", "xpath=(//*[contains(@id,'70 miles/hr') or contains(@id,'70 Miles/hr')])[1]", 0);
        sc.swipe("Down", sc.p2cy(60), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Save']", 0);
        click("NATIVE", "xpath=//*[@text='Save']", 0, 1);
        sc.syncElements(30000, 90000);
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
            click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        }
//      //Speed header and value is are same. Need dev to look into it.
       //https://toyotaconnected.atlassian.net/browse/OAD01-25123
        //verifyElementFound("NATIVE", "xpath=(//*[contains(@id,'70 miles/hr') or contains(@id,'70 Miles/hr')])[1]", 0);
        createLog("Completed: "+profile+" Driving Limit Speed");
    }

    public static void drivingLimitsMiles(String profileName) {
        createLog("Started: "+profileName+" Driving Limit Miles");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")){
            reLaunchApp_iOS();
            navigateToSharedRemotePage();
            selectProfile(profileName);
            click("NATIVE", "xpath=//*[@text='Define Driving Limits']",0,1);
            verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
            click("NATIVE", "xpath=//*[@text='Driving Limits']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        }
        //miles validation
        createLog("miles validation");
        //Miles header and miles value have the same value
        //https://toyotaconnected.atlassian.net/browse/OAD01-25123
   //   verifyElementFound("NATIVE", "xpath=(//*[contains(@id,'Miles') or contains(@id,'miles')])[2]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Miles' and @class='UIAImage']", 0);
        String firstMilesDetails = sc.elementGetProperty("NATIVE","xpath=((//*[@class='UIAScrollView']/*[@class='UIAView'])[1]/*[@text='Miles' and @class='UIAStaticText'])[2]",0,"id");
        createLog("First Miles Details :"+firstMilesDetails);
        click("NATIVE", "xpath=//*[@text='Miles' and @class='UIAImage']", 0, 1);
        sc.syncElements(5000, 30000);

        //miles details page
        createLog("miles details page validations");
        click("NATIVE", "xpath=//*[@text='Time Picker']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'AM')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label, 'clock')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'min')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='miles_switch']", 0);

        //select miles
        createLog("Select 5 miles and verify 5 miles displayed");
        verifyElementFound("NATIVE", "xpath=//*[@id='5 mi']", 0);
        click("NATIVE", "xpath=//*[@id='5 mi']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@id='Back']", 0);
        click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
        sc.syncElements(2000, 4000);
        //Same value for both text and header https://toyotaconnected.atlassian.net/browse/OAD01-25123
//        verifyElementFound("NATIVE", "xpath=(//*[contains(@id,'Miles\n5 miles') or contains(@id,'Miles\n5 Miles')])[1]", 0);
        sc.swipe("Down", sc.p2cy(60), 2000);
        createLog("Save miles change");
        click("NATIVE", "xpath=//*[@text='Save']", 0, 1);
        sc.syncElements(30000, 90000);
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
            click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@id='Driving Limits']", 0);
        }
        //Same value for both text and header https://toyotaconnected.atlassian.net/browse/OAD01-25123
//        verifyElementFound("NATIVE", "xpath=(//*[contains(@id,'Miles\n5 miles') or contains(@id,'Miles\n5 Miles')])[1]", 0);
        createLog("Completed: "+profileName+" Driving Limit Miles");
    }

    public static void drivingLimitsCurfew(String profileName) {
        createLog("Started: "+profileName+" Driving Limit Curfew");
        String actualText;
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")){
            reLaunchApp_iOS();
            navigateToSharedRemotePage();
            selectProfile(profileName);
            verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
            click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        }
        //curfew validation
        createLog("Curfew page");
        verifyElementFound("NATIVE", "xpath=(//*[@text='Curfew'])[2]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Curfew'])[1]", 0);
    //  Same value for both text and header https://toyotaconnected.atlassian.net/browse/OAD01-25123
    //  String firstCurfewDetails = sc.elementGetProperty("NATIVE","((//*[@class='UIAScrollView']/*[@class='UIAView'])[1]/*[@text='Curfew' and @class='UIAStaticText'])[2]",0,"id");
    //  createLog("First Curfew Details :"+firstCurfewDetails);
        click("NATIVE", "xpath=(//*[@text='Curfew'])[1]", 0, 1);
        sc.syncElements(5000, 30000);

        //curfew details page
        createLog("Curfew details page");
 //     verifyElementFound("NATIVE", "xpath=(//*[contains(@id,'AM')])[1]", 0);
 //     verifyElementFound("NATIVE", "xpath=(//*[contains(@id,'AM')])[1]", 0);
 //     verifyElementFound("NATIVE", "xpath=(//*[contains(@id,'AM')])[2]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='curfew_switch']", 0);
        String startTIme = sc.elementGetProperty("NATIVE", "xpath=(//*[@text='Time Picker'])[1]", 0, "value");
        createLog("Curfew Start Time :"+startTIme.substring(startTIme.length()-2));

        actualText = sc.getText("NATIVE");
        createLog(actualText);

        String daysOfWeek[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (String detailsName : daysOfWeek) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }

        if(sc.isElementFound("NATIVE","xpath=//*[@id='Sunday']/following-sibling::*[@id='Selected'][1]")){
            createLog("Sunday is checked - unchecking sunday");
            click("NATIVE", "xpath=//*[@id='Sunday']/following-sibling::*[@id='Selected'][1]", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[contains(text(), 'Tue, Wed, Thu, Fri, Sat, Mon')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Back']", 0);
            click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
            sc.syncElements(5000, 30000);
            //text and header value is the same
      //    verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Mo, Tu, We, Th, Fr, Sa')]", 0);
            sc.swipe("Down", sc.p2cy(60), 2000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Save']", 0);
            click("NATIVE", "xpath=//*[@text='Save']", 0, 1);
            sc.syncElements(5000, 30000);
            //text and header value is the same
     //     verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Mo, Tu, We, Th, Fr, Sa')]", 0);
        } else {
            createLog("Sunday is already unchecked - checking sunday");
            verifyElementFound("NATIVE", "xpath=//*[@id='Sunday']", 0);
        }
        verifyElementFound("NATIVE","xpath=//*[@text='Back']",0);
        click("NATIVE","xpath=//*[@text='Back']",0,1);
        createLog("Completed: "+profileName+" Driving Limit Curfew");
    }

    public static void drivingLimitsTime(String profileName) {
        createLog("Started: "+profileName+" Driving Limit Time");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Share Remote']")){
            reLaunchApp_iOS();
            navigateToSharedRemotePage();
            selectProfile(profileName);
            verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
            click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        }

        //Time validation
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Time')])[1]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@text='Time'])[2]", 0);
        //Text and headr value is the same,
    //  String firstTimeDetails = sc.elementGetProperty("NATIVE","xpath=(//*[contains(@id,'Time')])[1]",0,"id");
    //  createLog("First Time Details :"+firstTimeDetails);
        click("NATIVE", "xpath=(//*[contains(@text,'Time')])[1]", 0, 1);
        sc.syncElements(5000, 30000);

        //time details page
        click("NATIVE", "xpath=//*[@text='Time Picker']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label, 'clock')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'min')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label, 'AM')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'time_switch')][3]", 0);

        //select miles
        verifyElementFound("NATIVE", "xpath=//*[@text='2 Hrs']", 0);
        click("NATIVE", "xpath=//*[@text='2 Hrs']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Selected']", 0);
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        sc.syncElements(2000, 4000);
        // Text and header value is the same
        // verifyElementFound("NATIVE", "xpath=//*[contains(@id,'2 hrs') or contains(@id,'2 Hrs')]", 0);
        sc.swipe("Down", sc.p2cy(60), 2000);
        click("NATIVE", "xpath=//*[@text='Save']", 0,1);
        sc.syncElements(15000, 30000);
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
            click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@id='Driving Limits']", 0);
        }
       //text and heaedr value is the same
     //  verifyElementFound("NATIVE", "xpath=//*[contains(@id,'2 hrs') or contains(@id,'2 Hrs')]", 0);
        sc.swipe("Up", sc.p2cy(20), 5000);
        sc.syncElements(2000, 4000);
        createLog("Completed: "+profileName+" Driving Limit Time");
    }

    public static void drivingLimitsArea(String profileName) {
        createLog("Started: "+profileName+" Driving Limit Area");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")){
            reLaunchApp_iOS();
            navigateToSharedRemotePage();
            selectProfile(profileName);
            verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
            click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        }
        //Area validation
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Area')])[1]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[contains(@text,'Area')])[2]", 0);
       //Text and headr value is the same
    //  String firstAreaDetails = sc.elementGetProperty("NATIVE","xpath=(//*[contains(@id,'Area')])[1]",0,"id");
    //  createLog("First Area Details :"+firstAreaDetails);
        click("NATIVE", "xpath=(//*[contains(@text,'Area')])[1]", 0, 1);
        sc.syncElements(5000, 30000);

        //Area details page
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'area_title')]", 0);
       //Map verification
        verifyElementFound("NATIVE", "xpath=//*[@id='AnnotationContainer']", 0);
        //vehicle icon
        verifyElementFound("NATIVE", "xpath=//*[@id='car']", 0);
        //current location icon
        verifyElementFound("NATIVE", "xpath=//*[@text='mapLocation']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Search Name, City, or Zip Code')]", 0);

        //select miles
        verifyElementFound("NATIVE", "xpath=//*[@id='10 mi']", 0);
        click("NATIVE", "xpath=//*[@id='10 mi']", 0, 1);
        sc.syncElements(2000, 4000);
        //verify check mark is displayed next to 10 miles
        verifyElementFound("NATIVE", "xpath=//*[@id='5 mi']/following-sibling::*[@text='Selected']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='area_switch_title']/following-sibling::*[@text='10 mi']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='area_switch']", 0);
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        sc.syncElements(2000, 4000);

       //header and text value is the same
    //  verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Area\n10 miles')]", 0);
        sc.swipe("Down", sc.p2cy(60), 2000);
        click("NATIVE", "xpath=//*[@text='Save']", 0,1);
        sc.syncElements(15000, 30000);
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
            click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        }
        //text and header value is the same
     // verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Area\n10 miles')]", 0);
        sc.syncElements(2000, 4000);
        createLog("Completed: "+profileName+" Driving Limit Area");
    }

    public static void drivingLimitsIgnition(String profileName) {
        createLog("Started: "+profileName+" Driving Limit Ignition");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")){
            reLaunchApp_iOS();
            navigateToSharedRemotePage();
            selectProfile(profileName);
            verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
            click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        }
        //Ignition validation
        verifyElementFound("NATIVE", "xpath=//*[@text='Ignition'][1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Ignition'][2]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Ignition']/following-sibling::*[@class='UIASwitch']", 0);
        String ignitionValue = sc.elementGetProperty("NATIVE","xpath=//*[@text='Ignition']/following-sibling::*[@class='UIASwitch']",0,"value");
        createLog("Ignition value is :"+ignitionValue);

        if(ignitionValue.equals("1")){
            createLog("Ignition is ON - turning OFF now");
            click("NATIVE", "xpath=//*[@text='Ignition']/following-sibling::*[@class='UIASwitch']", 0, 1);
            sc.syncElements(2000, 10000);
            sc.swipe("Down", sc.p2cy(60), 2000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Save']", 0);
            sc.syncElements(5000, 30000);
            if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")) {
                verifyElementFound("NATIVE", "xpath=//*[@id='driver_settings_icon']", 0);
                click("NATIVE", "xpath=//*[@id='driver_settings_icon']", 0, 1);
                sc.syncElements(5000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@id='Driving Limits']", 0);
            }
            ignitionValue = sc.elementGetProperty("NATIVE","xpath=//*[@text='Ignition']/following-sibling::*[@class='UIASwitch']",0,"value");
            createLog("Ignition value after turning OFF :"+ignitionValue);
            if(ignitionValue.equals("0")) {
                sc.report("Ignition value is changed",true);
                createLog("Ignition turned off: "+ignitionValue);
            } else {
                sc.report("Ignition value is not changed",false);
                createErrorLog("Ignition is not turned OFF");
            }
        } else {
            createLog("Ignition is already OFF - turning ON now");
            click("NATIVE", "xpath=//*[@text='Ignition']/following-sibling::*[@class='UIASwitch']", 0, 1);
            sc.syncElements(2000, 10000);
            sc.swipe("Down", sc.p2cy(60), 2000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Save']", 0);
            sc.syncElements(5000, 30000);
            if(!sc.isElementFound("NATIVE","xpath=//*[@text='Driving Limits']")) {
                verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
                click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
                sc.syncElements(5000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
            }
            ignitionValue = sc.elementGetProperty("NATIVE","xpath=//*[@text='Ignition']/following-sibling::*[@class='UIASwitch']",0,"value");
            createLog("Ignition value after turning ON :"+ignitionValue);
            if(ignitionValue.equals("1")) {
                sc.report("Ignition value is changed",true);
                createLog("Ignition turned ON: "+ignitionValue);
            } else {
                sc.report("Ignition value is not changed",false);
                createErrorLog("Ignition is not turned ON");
            }
        }
        createLog("Completed: "+profileName+" Driving Limit Ignition");
    }

    public static void navigateToSharedRemotePage() {
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Share Remote']")) {
            createLog("Share Remote button not found. Relaunching the app");
            reLaunchApp_iOS();
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
            sc.syncElements(3000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@id='remote_guest_driver_card']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Share remote and track speed limits']", 0);
            //click share remote and track speed limits
            click("NATIVE", "xpath=//*[@text='Share remote and track speed limits']", 0, 1);
            sc.syncElements(5000, 60000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
            createLog("Guest Driver screen displayed");
            sc.syncElements(5000, 10000);
        }
    }

    public static void selectProfile(String profileName) {
        if (profileName.equalsIgnoreCase("guest")) {
            //click on the guest picture
            createLog("Selecting Guest settings page");
            verifyElementFound("NATIVE", "xpath=(//*[@id='guest_option'])[1]", 0);
            click("NATIVE", "xpath=(//*[@id='guest_option'])[1]", 0, 1);
        } else if (profileName.equalsIgnoreCase("valet")) {
            //click on the valet picture
            createLog("Selecting valet settings page");
            verifyElementFound("NATIVE", "xpath=(//*[@id='valet_option'])[1]", 0);
            click("NATIVE", "xpath=(//*[@id='valet_option'])[1]", 0, 1);
        } else {
            createErrorLog("Please provide profile name as Guest or Valet");
        }
    }

    public static void guestDriverHomePageValidations() {
        createLog("Started: Guest Driver Home Page Validations");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_guest_driver_card']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Share remote and track speed limits' or contains(@id,'Share your vehicle')]", 0);

        if(sc.isElementFound("NATIVE","xpath=//*[@id='guest']/following-sibling::*[@id='check' and @class='UIAImage']")) {
            createLog("Guest profile is enabled - verifying Guest Alerts ON");
            //Open GuestDriver Home page and verify Guest profile is Alerts ON
            verifyElementFound("NATIVE", "xpath=//*[@text='guest']", 0);
            click("NATIVE", "xpath=//*[@text='guest']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
            verifyElementFound("NATIVE","xpath=//*[@text='Description of guest drivers']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Guest')]", 0);
            //verify Guest alerts in ON
            verifyElementFound("NATIVE", "xpath=//*[@id='Guest\nAlerts ON']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Guest')]/following-sibling::*[@id='checkedIcon']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@id='profile_pic_image'])[2]", 0);

            //verify Valet alerts is OFF
            verifyElementFound("NATIVE", "xpath=//*[@id='valet_option']/following-sibling::*[@text='Alerts Off']", 0);
            verifyElementFound("NATIVE","xpath=//*[@id='valet_option']/following-sibling::*[@text='unCheckedIcon']",0);
            verifyElementFound("NATIVE", "xpath=(//*[@id='valet_option'])[1]", 0);

            verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='back_button']", 0);
            //navigate back to advanced remote
            click("NATIVE", "xpath=//*[@id='back_button']", 0, 1);
            sc.syncElements(3000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
            createLog("Completed : Guest profile is enabled - verified Guest Alerts ON");
        }
        else if(sc.isElementFound("NATIVE","xpath=//*[@id='valet']/following-sibling::*[@id='check' and @class='UIAImage']")) {
            createLog("Valet profile is enabled - verifying valet Alerts ON");
            //Open GuestDriver Home page and verify Valet profile is Alerts ON
            verifyElementFound("NATIVE", "xpath=//*[@id='valet']", 0);
            click("NATIVE", "xpath=//*[@id='valet']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
            verifyElementFound("NATIVE","xpath=//*[@text='Description of guest drivers']",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Valet')]", 0);
            //verify Valet alerts in ON
            verifyElementFound("NATIVE", "xpath=//*[@id='valet_option']/following-sibling::*[@text='Alerts On']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='valet_option']/following-sibling::*[@text='checkedIcon']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@id='valet_option'])[1]", 0);


            //verify Guest alerts is OFF
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='Alerts Off']", 0);
            //No tick icon when OFF
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='unCheckedIcon']", 0);
            verifyElementFound("NATIVE", "xpath=(//*[@id='valet_option'])[1]", 0);

            verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='back_button']", 0);
            //navigate back to advanced remote
            click("NATIVE", "xpath=//*[@id='back_button']", 0, 1);
            sc.syncElements(3000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
            createLog("Completed : Valet profile is enabled - verified valet Alerts ON");
        }
        else {
            createLog("Verifying valet and guest Alerts OFF");
            //Open GuestDriver Home page and verify Guest and Valet profile is Alerts Off
            click("NATIVE", "xpath=//*[@text='valet']", 0, 1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Description of guest drivers']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Guest')]", 0);
            //Verify Valet alerts is Off
            verifyElementFound("NATIVE", "xpath=//*[@label='valet']/following-sibling::*[@text='Alerts Off'][1]",0);
            verifyElementFound("NATIVE", "xpath=//*[@text='valet']", 0);

            //Verify Guest alerts is Off
            verifyElementFound("NATIVE", "xpath=//*[@label='Guest']/following-sibling::*[@text='Alerts Off']",0);
            verifyElementFound("NATIVE", "xpath=//*[@text='guest']", 0);

            verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='back_button']", 0);
            //navigate back to advanced remote
            click("NATIVE", "xpath=//*[@id='back_button']", 0, 1);
            sc.syncElements(3000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
        }
        createLog("Completed: Guest Driver Home Page Validations");
    }

    public static void guestAlertsValidation() {
        createLog("Started: Guest Alerts Validations");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Share Remote')]"))
            reLaunchApp_iOS();
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_guest_driver_card']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Share remote and track speed limits']", 0);
        //click share remote and track speed limits
        click("NATIVE", "xpath=//*[@text='Share remote and track speed limits']", 0, 1);
        sc.syncElements(5000, 60000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest Drivers']", 0);

        if (sc.isElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='Alerts On']", 0)) {
            createLog("Guest Alerts already turned ON");
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='checkedIcon']", 0);
            createLog("Turn off guest alerts");
            click("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='checkedIcon']", 0, 1);
            sc.syncElements(10000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='Alerts Off']")) {
                createLog("Guest Alerts OFF");
            } else
                createErrorLog("Guest Alerts not turned OFF");
        } else {
            createLog("Guest Alerts are turned OFF");
            //verify Guest alerts is OFF
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='Alerts Off']", 0);
            //No tick icon when OFF
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='unCheckedIcon']", 0);
            createLog("Turning ON Guest Alerts");
            click("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='unCheckedIcon']", 0, 1);
            sc.syncElements(10000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='Alerts On']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='checkedIcon']", 0);
            createLog("Guest Alerts are turned ON");
            //switch off alerts
            click("NATIVE", "xpath=//*[@text='guest']/following-sibling::*[@text='checkedIcon']", 0, 1);
            sc.syncElements(10000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='Alerts On']")) {
                createErrorLog("Guest alerts are not turned off");
            } else
                createLog("Guest alerts turned off");
        }
        createLog("Completed: Guest Alerts Validations");
    }

    public static void valetAlertsValidation() {
        createLog("Started: Valet Alerts Validations");
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='valet_option']/following-sibling::*[@text='Alerts On']", 0)) {
            createLog("Valet Alerts already turned ON");
            verifyElementFound("NATIVE", "xpath=//*[@id='valet_option']/following-sibling::*[@text='checkedIcon']", 0);
            createLog("Turn off Valet alerts");
            click("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='Alerts Off']", 0, 1);
            sc.syncElements(10000, 50000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='Alerts Off']")) {
                createLog("Valet Alerts OFF");
            } else
                createErrorLog("Valet Alerts not turned OFF");
        } else {
            createLog("Valet Alerts are turned OFF");
            verifyElementFound("NATIVE", "xpath=//*[@id='guest_option']/following-sibling::*[@text='Alerts Off']", 0);
            createLog("Turning ON Valet Alerts");
            click("NATIVE", "xpath=//*[@text='valet']/following-sibling::*[@text='unCheckedIcon'][1]", 0, 1);
            sc.syncElements(5000, 50000);
            verifyElementFound("NATIVE", "xpath=//*[@id='valet_option']/following-sibling::*[@text='Alerts On']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='valet_option']/following-sibling::*[@text='checkedIcon']", 0);
            createLog("Valet Alerts are turned ON");
            //switch off alerts
            click("NATIVE", "xpath=//*[@text='valet']/following-sibling::*[@text='checkedIcon'][1]", 0, 1);
            sc.syncElements(10000, 40000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='valet_option']/following-sibling::*[@text='Alerts On']")) {
                createErrorLog("Valet alerts not turned off");
            } else
                createLog("Valet alerts turned off");
        }
        createLog("Completed: Valet Alerts Validations");
    }

    public static void guestSettingsHomePageValidations() {
        createLog("Started: Guest Settings Home Page Validations");
        navigateToSharedRemotePage();
        verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
        //click on the guest picture
        verifyElementFound("NATIVE", "xpath=(//*[@id='guest_option'])[1]", 0);
        click("NATIVE", "xpath=(//*[@id='guest_option'])[1]", 0,1);
        sc.syncElements(3000, 30000);
        //verify guest page
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='guest']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='slider']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'get notified when your guest driver goes over the driving limits you set.')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);
        //verify driving limits
        click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Driving Limits']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Miles')]", 0);
        String actualText = sc.getText("NATIVE");
        createLog(actualText);

        String drivingLimitDetailsGuest[] = {"Driving Limits", "Miles", "Speed", "Area", "Curfew", "Time", "Ignition"};
        for (String detailsName : drivingLimitDetailsGuest) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }

        }
        verifyElementFound("NATIVE", "xpath=//*[@id='Back']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Save']", 0);
        click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
        sc.syncElements(5000, 10000);
        //Guest screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Guest']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Back']", 0);
        click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
        createLog("Completed: Guest Settings Home Page Validations");
    }

    public static void valetSettingsHomePageValidations() {
        createLog("Started: valet Settings Home Page Validations");
        navigateToSharedRemotePage();
        verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
        //click on the valet picture
        verifyElementFound("NATIVE", "xpath=(//*[@id='valet_option'])[1]", 0);
        click("NATIVE", "xpath=(//*[@id='valet_option'])[1]", 0,1);

        //verify valets page
        verifyElementFound("NATIVE", "xpath=//*[@text='Valet']", 0);
        //iCON
        verifyElementFound("NATIVE", "xpath=//*[@id='valet']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Driving Limits')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='slider']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'get notified when your guest driver goes over the driving limits you set.')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0);

        //verify driving limits
        click("NATIVE", "xpath=//*[@text='Define Driving Limits']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Miles')]", 0);

        String actualText = sc.getText("NATIVE");
        createLog(actualText);

        String drivingLimitDetailsValet[] = {"Driving Limits", "Speed", "Area", "Curfew", "Time", "Ignition"};
        for (String detailsName : drivingLimitDetailsValet) {
            if (actualText.contains(detailsName)) {
                sc.report("Validation of " + detailsName, true);
            } else {
                sc.report("Validation of " + detailsName, false);
            }
        }

        verifyElementFound("NATIVE", "xpath=//*[@id='Back']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Save']", 0);
        click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Valet']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Back']", 0);
        click("NATIVE", "xpath=//*[@id='Back']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Share Remote']", 0);
        createLog("Completed: valet Settings Home Page Validations");
    }

    public static void guestDriverRemoteShare(){
        createLog("Started: Share remote to guest driver");
        navigateToSharedRemotePage();
        //Guest Driver screen
        verifyElementFound("NATIVE", "xpath=//*[@id='Share Remote']", 0);
        click("NATIVE","xpath=//*[@id='Share Remote']",0,1);
        sc.syncElements(3000, 30000);

        //share remote screen
        verifyElementFound("NATIVE", "xpath=//*[@id='Share Remote']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Remote Access')]",0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Give driver access to remote commands with mobile app')]",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Add Driver']",0);
        createLog("Adding driver");
        click("NATIVE", "xpath=//*[@id='Add Driver']", 0, 1);
        sc.syncElements(3000, 30000);

        //Add Driver screen
        verifyElementFound("NATIVE", "xpath=//*[@id='Add Driver']",0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Driver needs to have the')]",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Search by Email or Phone']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Search']",0);
        createLog("Searching for email:guestdriver_21mm@mail.tmnact.io");
        sendText("NATIVE","xpath=//*[@id='Search by Email or Phone']",0,"guestdriver_21mm@mail.tmnact.io");
        sc.syncElements(3000, 30000);
        click("NATIVE", "xpath=//*[@id='Search']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='gu*** dr****' or @id='GU*** DR****']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='guestdriver_21mm@mail.tmnact.io']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='valet_driver_image']",0);
        click("NATIVE", "xpath=//*[@id='Invite Driver']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Invite Sent']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='tick_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Back to Dashboard']", 0);
        click("NATIVE", "xpath=//*[@id='Back to Dashboard']", 0, 1);
        sc.syncElements(10000, 60000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']"))
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
        sc.syncElements(5000, 15000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Your guest has remote access')]",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='To regain control of your remote and share it with someone else, remove the driver.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remove Driver']",0);
        createLog("Completed: Share remote to guest driver");
    }
    public static void validateRemoteCommandsOnDashboard(){
        createLog("Started: Verify Remote commands displayed");
        verifyElementFound("NATIVE", "xpath=//*[@text='Fuel']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Lock']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Start']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Unlock']", 0);
        createLog("Completed: Verify Remote commands displayed");
    }
    public static void removeDriver(){
        createLog("Started: Remove Driver");
        strVINType = setVinType("JTJHKCEZ1N2004122");
        if(strVINType.equalsIgnoreCase("21MM")){
            VehicleSelectionIOS.Default("JTJHKCEZ1N2004122");
        }else if(strVINType.equalsIgnoreCase("17CYPLUS")) {
            VehicleSelectionIOS.Default("JTMFB3FV5MD006786");
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_state_remote_share_remove_button']", 0);
        click("NATIVE", "xpath=//*[@id='remote_state_remote_share_remove_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Remove Driver']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Remove Driver to regain control of your remote and share it with someone else.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remove']", 0);
        click("NATIVE", "xpath=//*[@text='Remove']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Activate Remote')]",0);
        createLog("Completed: Remove Driver");
    }

    public static void activatePrimaryRemote() throws Exception{
        createLog("Started: Activate primary remote");
        strVINType = setVinType("JT2AE71D0D0014320");
        if(strVINType.equalsIgnoreCase("21MM")){
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Activate Remote')]",0);
            verifyElementFound("NATIVE", "xpath=//*[@id='To use remote features like start, stop, lock, and unlock, we need to verify you as the owner of the vehicle.']",0);
            sc.flickElement("NATIVE", "xpath=//*[@text='Info']", 0, "Up");
            verifyElementFound("NATIVE", "xpath=//*[@text='Activate']",0);
            click("NATIVE", "xpath=//*[@text='Activate']", 0,1);
            sc.syncElements(5000,10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Remote activation verification']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SHOW_TITLE_LABEL']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SHOW_BODY_LABEL']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SHOW_INSTRUCTION_BUTTON']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SKIP_INSTRUCTIONS_BUTTON']",0);
            click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);

        }else if(strVINType.equalsIgnoreCase("17CYPLUS")){
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Activate Remote')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'To use remote features like start, stop, lock and unlock, we need to verify you as the owner of the vehicle.')]",0);
            verifyElementFound("NATIVE", "xpath=//*[@id='Activate']",0);
            click("NATIVE", "xpath=//*[@id='Activate']", 0,1);
            sc.syncElements(5000,10000);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Send Activation Code') or contains(@id,'SEND ACTIVATION CODE')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Text Message')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Email')]",0);
            click("NATIVE", "xpath=//*[@id='Cancel' or  @id='CANCEL']", 0,1);
        }
        createLog("Started:Remote activation for Primary:stagetesting_21mm");
        RemoteAuthForGuestDriver("stg", "stagetesting_21mm@mail.tmnact.io", "JT2AE71D0D0014320");
        createLog("Completed:Remote activation for Primary:stagetesting_21mm");
        createLog("Completed: Activate primary remote");
    }
    public static void activateGuest() throws Exception{
        createLog("Started: Activate Guest");
        strVINType = setVinType("JT2AE71D0D0014320");
        if(strVINType.equalsIgnoreCase("21MM")){
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Activate Remote')]",0);
            verifyElementFound("NATIVE", "xpath=//*[@id='To use remote features like start, stop, lock, and unlock, we need to verify you as the owner of the vehicle.']",0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Activate']",0);
            click("NATIVE", "xpath=//*[@text='Activate']", 0,1);
            sc.syncElements(5000,10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Guest access verification']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_LABEL_TITLE']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SHOW_TITLE_LABEL']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SHOW_BODY_LABEL']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SHOW_INSTRUCTION_BUTTON']",0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='VERIFY_REMOTE_SKIP_INSTRUCTIONS_BUTTON']",0);
            click("NATIVE", "xpath=//*[@accessibilityLabel='TOOLBAR_BUTTON_LEFT']", 0, 1);

        }else if(strVINType.equalsIgnoreCase("17CYPLUS")){
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Charged') or contains(@id,'charged')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Activate Remote')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'To use remote features like start, stop, lock and unlock, we need to verify you as the owner of the vehicle.')]",0);
            verifyElementFound("NATIVE", "xpath=//*[@id='Activate']",0);
            click("NATIVE", "xpath=//*[@id='Activate']", 0,1);
            sc.syncElements(5000,10000);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Send Activation Code') or contains(@id,'SEND ACTIVATION CODE')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Text Message')]",0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Email')]",0);
            click("NATIVE", "xpath=//*[@id='Cancel' or  @id='CANCEL']", 0,1);
        }

        sc.syncElements(5000,10000);
        createLog("Started:Remote activation for Guest Driver:stageguestdriver_21mm");
        RemoteAuthForGuestDriver("stg", "stageguestdriver_21mm@mail.tmnact.io", "JT2AE71D0D0014320");
        createLog("Completed: Remote activation for Guest Driver:stageguestdriver_21mm");
        createLog("Completed: Activate Guest");
    }
}
