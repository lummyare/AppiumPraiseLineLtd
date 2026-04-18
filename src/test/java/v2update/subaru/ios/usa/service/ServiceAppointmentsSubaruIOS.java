package v2update.subaru.ios.usa.service;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import utils.CommonUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceAppointmentsSubaruIOS extends SeeTestKeywords {
    String testName = "ServiceAppointments-IOS";
    static int day;

    @BeforeAll
    public void setup() throws Exception {
        testName = System.getProperty("cloudApp") + testName;
        iOS_Setup2_5(this.testName);
        //App Login
        sc.startStepsGroup("21mm email Login");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strSubaruEmail"), ConfigSingleton.configMap.get("strSubaruPwd"));
        sc.stopStepsGroup();
        /*
        Call Vehicle switcher from the vehicle switcher class and Default the vehicle 5TDADAB59RS000015
        iOS_DefaultVehicle("5TDADAB59RS000015"); // This method is deprecated
         */
        //VehicleSelectionIOS.Switcher("JTJHKCEZ1N2004122");
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void serviceAppointmentCard() {
        sc.startStepsGroup("Service Appointment Card");
        verifyServiceAppointmentCard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void callDealer() {
        sc.startStepsGroup("Test - Schedule New Appointment");
        callDealerSubaru();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void changePreferredDealerTest() {
        sc.startStepsGroup("Test - Change Preferred Dealer");
        reLaunchApp_iOS();
        //VehicleSelectionIOS.Switcher("JTJHKCEZ1N2004122");
        /*
        change the preferred dealer
         */
        changePreferredDealer();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void verifyServiceAppointmentCard() {
        createLog("Verifying service appointment card in service bottom bar");
        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'Vehicle image')]"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE", "xpath=//*[@id='BottomTabBar_serviceTab']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Service']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='IconService']", 0);
        click("NATIVE", "xpath=//*[@id='BottomTabBar_serviceTab']", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@id='ServiceApptsCard_serviceAppointmentsTitle']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='ServiceApptsCard_makeAnAppointmentButton']", 0);

        createLog("Verified service appointment card in service bottom bar");
    }

    public static void callDealerSubaru() {
        createLog("Verifying create new service appointment");
        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'Vehicle image')]"))
            reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@id='Service']", 0, 1);
        sc.syncElements(5000, 30000);
        //Bug "Call dealer CTA is not displayed. Once displayed will update the script

        /*
        click("NATIVE","xpath=//*[@id='ServiceApptsCard_makeAnAppointmentButton']",0,1);
        sc.syncElements(5000, 20000);
        verifyElementFound("NATIVE","xpath=//*[@id='DealerServiceAppointment_NavigationBar']",0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Make an Appointment']", 0);

        click("NATIVE", "xpath=//*[@label='Make an Appointment']", 0, 1);
        sc.syncElements(4000, 30000);

        verifyElementFound("NATIVE","xpath=//*[@text='Odometer' and @accessibilityLabel='AppointmentScreen_NavigationBar']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='1 of 5']", 0);
        //if odometer value is ------- then enter odometer details
        String odometerVal = sc.elementGetText("NATIVE", "xpath=//*[@id='DSA_OdometerValue']", 0);
        if (odometerVal.contains("-------")) { //TODO check this case
            click("NATIVE", "xpath=//*[@id='DSA_OdometerTextField']", 0, 1);
            sc.sendText("2166");
            ((IOSDriver) driver).getKeyboard().sendKeys("'\n'");
         //   sc.closeKeyboard();
            sc.syncElements(1000, 12000);
        }

        //validate disclaimer - Based on VIN brand
        strAppType = strAppType.substring(0, 1).toUpperCase() + strAppType.substring(1);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'By selecting') and contains(@label,'I authorize') and contains(@label,'to share my VIN, current mileage, contact information and service options with the dealer')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label=' Toyota Privacy Statement' or @label=' Lexus Privacy Statement']", 0);

        //click on privacy statement link
        click("NATIVE","xpath=//*[@label=' Toyota Privacy Statement' or @label=' Lexus Privacy Statement']",0,1);
        createLog("Verifying privacy statement details in web page");
        sc.syncElements(10000, 30000);
        //No locators to validate webpage contents
        //sc.verifyElementFound("NATIVE", "xpath=//*[@label='Address' and @class='UIAView']", 0);
        String privacyUrlText = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'lexus.com') or contains(@text,'toyota.com')]", 0, "value").toLowerCase();
        String url = strAppType+".com";
        url = url.toLowerCase();
        sc.report("Verify privacy statement url contains expected url", privacyUrlText.contains(url));

        //No locators to validate webpage contents
        //sc.verifyElementFound("NATIVE", "xpath=//*[contains(@label,'YOUR PRIVACY RIGHTS') or contains(@label,'Your Privacy Rights')]", 0);
        // No Back/Done button
        sc.launch(strAppPackage,false,false);
        sc.syncElements(4000,10000);
        createLog("Verified privacy statement details in web page");

        //click on Connected Services Privacy Notice link
        verifyElementFound("NATIVE", "xpath=//*[@label='Connected Services Privacy Notice']", 0);
        click("NATIVE","xpath=//*[@label='Connected Services Privacy Notice']",0,1);
        createLog("Verifying Connected Services Privacy Notice in web page");
        sc.syncElements(10000, 30000);
        sc.verifyElementFound("NATIVE", "xpath=//*[contains(@text,'lexus.com') or contains(@text,'toyota.com')]", 0);
        String privacyNoticeUrlText = sc.elementGetProperty("NATIVE", "xpath=//*[contains(@text,'lexus.com') or contains(@text,'toyota.com')]", 0, "value").toLowerCase();
        sc.report("Verify Connected Services Privacy Notice url contains expected url", privacyNoticeUrlText.contains(url));
        //sc.verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Connected Services Privacy Notice')]", 0);

        // No Done/Back button
        sc.launch(strAppPackage,false,false);
        sc.syncElements(5000, 30000);
        createLog("Verified Connected Services Privacy Notice in web page");

        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Continue']"))
            click("NATIVE", "xpath=//*[@text='Continue']", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Services']", 0);
        //Mileage tab
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Current') and @class='UIAButton']",0);
        click("NATIVE","xpath=//*[@id='Go Down']",0,1);
        sc.syncElements(2000,5000);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Previous')]",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Next')]",0);
        click("NATIVE","xpath=//*[contains(@id,'Next')]",0,1);
        verifyElementFound("NATIVE","xpath=//*[@id='Continue']",0);
        click("NATIVE","xpath=//*[@id='Continue']",0,1);
        sc.syncElements(2000,5000);
        //Check Factory Recommended
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Factory Recommended']/following::*[@id='Add']",0)) {
            createLog("Factory Recommended available");
            click("NATIVE","xpath=//*[@id='Factory Recommended']/following::*[@id='Add']",0,1);
            sc.syncElements(2000,5000);
            verifyElementFound("NATIVE","xpath=//*[@id='Factory recommended services based on your vehicle maintenance schedule.']",0);
        }
        else if(sc.isElementFound("NATIVE","xpath=//*[@id='Factory Recommended']",0)) {
            createLog("Factory Recommended available");
            verifyElementFound("NATIVE","xpath=//*[@id='Factory recommended services based on your vehicle maintenance schedule.']",0);
        } else {createLog("Factory Recommended not available");}

        //Check Dealer Recommended
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Dealer Recommended']/following::*[@id='Add']",0)) {
            createLog("Dealer Recommended available");
            click("NATIVE","xpath=//*[@id='Dealer Recommended']/following::*[@id='Add']",0,1);
            sc.syncElements(2000,5000);
            verifyElementFound("NATIVE","xpath=//*[@id='Dealer recommended services tailored to meet your local driving conditions.']",0);
        }
        else if(sc.isElementFound("NATIVE","xpath=//*[@id='Dealer Recommended']",0)) {
            createLog("Dealer Recommended available");
            verifyElementFound("NATIVE","xpath=//*[@id='Dealer recommended services tailored to meet your local driving conditions.']",0);
        } else {createLog("Dealer Recommended not available");}

        //All Services
        click("NATIVE","xpath=//*[@id='All Services']",0,1);
        sc.syncElements(1000,5000);
        //Select First available service
        verifyElementFound("NATIVE","xpath=//*[@id='Square'][1]",0);
        click("NATIVE","xpath=//*[@id='Square'][1]",0,1);

        click("NATIVE", "xpath=//*[contains(@label,'Continue')]", 0, 1);
        sc.syncElements(3000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Advisor']", 0);
        click("NATIVE", "xpath=//*[contains(@label,'Continue')]", 0, 1);
        sc.syncElements(3000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Transportation']", 0);
        click("NATIVE", "xpath=//*[contains(@label,'Continue')]", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Date &Time']", 0);
        // select date and time
        selectDateAndTime();

        //verify Time zone disclaimer CMQA1-1874, missing OAD01-24382
        //verifyElementFound("NATIVE", "xpath=//*[@text='*All available times are in dealership timezone']", 0);
        click("NATIVE", "xpath=//*[contains(@label,'Continue')]", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Make an Appointment')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Confirm Appointment']", 0);

        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'By clicking') and contains(@label,'you consent to receive auto dialed marketing calls or text messages at the number provided. Consent is not a condition of any purchase. Message and data rates apply.')]", 0);
        click("NATIVE", "xpath=//*[@label='Confirm Appointment']", 0, 1);
        sc.syncElements(20000, 50000);

        verifyElementFound("NATIVE", "xpath=//*[@label='Appointment Confirmed!']", 0);
        click("NATIVE", "xpath=//*[@label='Done']", 0, 1);

        //Appointment details screen
        sc.syncElements(5000, 30000);
        //Check Enjoying App is displayed
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@value,'Are you enjoying')]",0)) {
            enjoyingAppAlert_iOS();
        }
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Appointment Details')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Call']", 0);

        //verify Time zone disclaimer CMQA1-1874
        //verifyElementFound("NATIVE", "xpath=//*[contains(@text,'*All available times are in dealership timezone')]", 0);

        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Service Details')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Service Advisor')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Transportation')]", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@label='Cancel Appointment']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Edit']", 0);

        createLog("Verified create new service appointment");*/
    }

    public static String getMonth(int month) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CST"));
        String _month = "";
        switch (month) {
            case 0:
                _month = "Jan";
                break;
            case 1:
                _month = "Feb";
                break;
            case 2:
                _month = "Mar";
                break;
            case 3:
                _month = "Apr";
                break;
            case 4:
                _month = "May";
                break;
            case 5:
                _month = "Jun";
                break;
            case 6:
                _month = "Jul";
                break;
            case 7:
                _month = "Aug";
                break;
            case 8:
                _month = "Sep";
                break;
            case 9:
                _month = "Oct";
                break;
            case 10:
                _month = "Nov";
                break;
            case 11:
                _month = "Dec";
                break;
        }
        return _month;
    }

    public static void selectDateAndTime() {
        //boolean isDayNotSelectable = false;
        boolean blnNoAvailableTimeDisplay = false;
        String xpathStr = "";
        createLog("Configuring date");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CST"));
        Date currentDate = new Date();
        int month = cal.get(Calendar.MONTH);
        String _month = "";
        cal.setTime(currentDate);
        int year = cal.get(Calendar.YEAR);

        int serviceDate = cal.get(Calendar.DAY_OF_MONTH) + 7; // scheduling appointment after 1 week, so that we can cancel appointment
        int serviceDay = cal.get(Calendar.DAY_OF_WEEK) + 2;
        if (serviceDay > 7)
            serviceDay = serviceDay - 7;

        if (serviceDay == Calendar.SUNDAY) {
            serviceDate = serviceDate + 1;
        }

        if (serviceDate > 28) {
            serviceDate = 1;
            cal.set(year, month + 1, 1);
            day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SUNDAY)
                serviceDate = serviceDate + 1;
        }
        createLog("Configured date");

        createLog("Selecting date");
        if (serviceDate == 1 || (day == Calendar.SUNDAY)) // when service date is in the next Calendar mont2
        {
            createLog("Click next month");
            sc.click("NATIVE", "xpath=//*[@text='Forward']", 0, 1);
            sc.syncElements(2000, 4000);
            _month = getMonth(month + 1);
        }

        for (int i = 0; i < 15; i++) {
            String dateText = "";
            xpathStr = "//*[@label='" + serviceDate + "']";
            createLog("xpath is :" + xpathStr);
            //TODO, No blackout dates in current build: OAD01-24388
           /* dateText = sc.elementGetProperty("NATIVE",xpathStr,0,"label");
            createLog("date text is :"+dateText);
            boolean isDayNotSelectable = dateText.contains("Blackout date");
            createLog("isDayNotSelectable is: "+isDayNotSelectable);
            if (isDayNotSelectable) {
                createLog("day fall on dealer close day - checking next day: "+dateText);
                serviceDate = serviceDate + 1;
            } else {
                createLog("date does not fall on dealer close day - selecting date: "+dateText);
                click("NATIVE", xpathStr, 0, 1);
                sc.syncElements(10000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@label='Available Times']", 0);
                try {
                    blnNoAvailableTimeDisplay = driver.findElement(By.xpath("//*[@label='No available times, select another day' or @label='Please select a date to view available timeslots']")).isDisplayed();
                } catch (Exception e){
                    createLog("Checking element display - ignore exception");
                }
                if(blnNoAvailableTimeDisplay) {
                    createLog("Displays message - No available times- SELECTING ANOTHER DAY OR Please select a date to view available timeslots");
                    serviceDate = serviceDate + 1;
                } else {
                    createLog("Time displayed for selected date");
                    break;
                }
            }*/
        }
        click("NATIVE", xpathStr, 0, 1);
        createLog("Selected date");

        createLog("Selecting time");
        createLog("Selecting time");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@label,'AM')])[1]"))
            click("NATIVE", "xpath=(//*[contains(@label,'AM')])[1]", 0, 1);
        else
            click("NATIVE", "xpath=(//*[contains(@label,'PM')])[1]", 0, 1);
        createLog("Selected time");
    }

    public void changePreferredDealer() {
        createLog("Verifying change preferred dealer");

        createLog("Clicking Service link");
        verifyElementFound("NATIVE", "xpath=//*[@id='BottomTabBar_serviceTab']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Service']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='IconService']", 0);
        click("NATIVE", "xpath=//*[@id='BottomTabBar_serviceTab']", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@id='ServiceApptsCard_serviceAppointmentsTitle']", 0);
        sc.syncElements(5000, 30000);
        click("NATIVE", "xpath=//*[@id='ServiceApptsCard_makeAnAppointmentButton']", 0, 1);

        createLog("Handle Turn On Bluetooth popup if displayed");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]")) {
            sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
            sc.syncElements(5000, 30000);
        }

        sc.syncElements(5000, 60000);

        createLog("Verifying Preferred Dealer");
        //preferred dealer
        verifyElementFound("NATIVE", "xpath=//*[@label='LocationPinIcon']", 0);
        click("NATIVE", "xpath=//*[@label='LocationPinIcon']", 0, 1);
        sc.syncElements(4000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Preferred Dealer']", 0);
        String currentPreferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[@id='DealerServiceAppointment_PreferredDealerTileText'])[1]", 0, "value");
        createLog("Existing Preferred dealer is: " + currentPreferredDealer);
        click("NATIVE", "xpath=//*[@text='Preferred Dealer']", 0, 1);
        sc.syncElements(4000, 10000);
        click("NATIVE", "xpath=//*[@label='Change Preferred Dealer']", 0, 1);
        sc.syncElements(4000, 10000);

        //preferred Dealer screen
        verifyElementFound("NATIVE", "xpath=//*[@label='Preferred Dealer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Back']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Call' and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Website']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Directions' and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'mi')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Change Preferred Dealer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Call Dealer']", 0);
        click("NATIVE", "xpath=//*[@text='Change Preferred Dealer']", 0, 1);
        sc.syncElements(4000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select Dealer']", 0);

        //search for a dealer
        createLog("Started -Preferred dealer search");
        verifyElementFound("NATIVE", "xpath=//*[@placeholder='Search Name, City, or Zip Code']", 0);
        click("NATIVE", "xpath=//*[@placeholder='Search Name, City, or Zip Code']", 0, 1);
        sendText("NATIVE", "xpath=//*[@placeholder='Search Name, City, or Zip Code']", 0, "Dallas");
        click("NATIVE", "xpath=//*[@text='search']", 0, 1);
        sc.syncElements(4000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Select Dealer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sewell Subaru']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='7800 Lemmon Ave']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'mi')]", 0);
        click("NATIVE", "xpath=//*[@text='Sewell Subaru']", 0, 1);
        sc.syncElements(4000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Dealer Details']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sewell Subaru']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='7800 Lemmon Ave']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Call' and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Website']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Directions' and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Call' and @class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Call Dealer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Set as Preferred Dealer']", 0);
        click("NATIVE", "xpath=//*[@text='Set as Preferred Dealer']", 0, 1);

        //Success message after changing preferred dealer
        verifyElementFound("NATIVE", "xpath=//*[@text='Success']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'you Preferred Dealer')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Done']", 0);
        click("NATIVE", "xpath=//*[@text='Done']", 0, 1);
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sewell Subaru']", 0);
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sewell Subaru']", 0);
        createLog("Completed Preferred dealer validation");

        /*

        //Click Search Name to choose new preferred dealer
        click("NATIVE", "xpath=//*[@id='Search Name, City, or Zip Code']", 0, 1);
        sc.sendText("Dallas");
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='search']"))
            click("NATIVE", "xpath=//*[@label='search']", 0, 1);
        sc.syncElements(5000, 30000);

        //Selecting a new dealer from the list
        String preferredDealerResult = sc.elementGetProperty("NATIVE", "xpath=(//*[@id='SelectPreferreDealer_DealerItem'])[1]", 0, "value");
        createLog("first preferred dealer displayed is: "+preferredDealerResult);
        click("NATIVE", "xpath=(//*[@id='SelectPreferreDealer_DealerItem'])[1]", 0, 1);
        sc.syncElements(3000, 6000);

        String preferredDealerResultName = sc.elementGetProperty("NATIVE","xpath=//*[@id='VehicleDealerDetails_DealerAddressText'][1]",0,"value");
        if (preferredDealerResultName.equalsIgnoreCase(currentPreferredDealer)) {
            createLog("First dealer result and current dealer is the same");
            //Go Back and get the second result
            click("NATIVE","xpath=//*[@text='Back']",0,1);
            sc.syncElements(2000,4000);

            preferredDealerResult = sc.elementGetProperty("NATIVE", "xpath=(//*[@id='SelectPreferreDealer_DealerItem'])[2]", 0, "value");
            createLog("Second preferred dealer displayed is: "+preferredDealerResult);
            click("NATIVE", "xpath=(//*[@id='SelectPreferreDealer_DealerItem'])[2]", 0, 1);
            sc.syncElements(3000, 6000);
            preferredDealerResultName = sc.elementGetProperty("NATIVE","xpath=//*[@id='VehicleDealerDetails_DealerAddressText'][1]",0,"value");
        }

        click("NATIVE", "xpath=//*[@label='Set as Preferred Dealer']", 0, 1);
        sc.syncElements(5000, 20000);
        //verify new preferred dealer success
        verifyElementFound("NATIVE", "xpath=//*[@label='Success']", 0);
        //TODO Typo below, Bug: OAD01-24710
        //verifyElementFound("NATIVE", "xpath=//*[contains(@label,'set your Preferred Dealer.')]", 0);
        click("NATIVE", "xpath=//*[@label='Done']", 0, 1);
        sc.syncElements(4000, 8000);

        for (int i=0; i<3; i++) {
            click("NATIVE","xpath=//*[@text='Back']",0,1);
            sc.syncElements(2000,4000);
            if (sc.isElementFound("NATIVE","xpath=//*[@label='Make an Appointment']",0)) {
                break;
            }
        }

        //verify newly selected dealer is displayed in Odometer screen
        click("NATIVE", "xpath=//*[@label='Make an Appointment']", 0, 1);
        sc.syncElements(5000, 20000);

        String preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[@id='DealerServiceAppointment_PreferredDealerTileText' and @class='UIAStaticText'])[1]", 0, "value");
        sc.report("verify new Set Preferred Dealer", preferredDealer.equalsIgnoreCase(preferredDealerResultName));
        //if odometer value is ------- then enter odometer details
        String odometerVal = sc.elementGetText("NATIVE", "xpath=//*[@id='DSA_OdometerValue']", 0);
        if (odometerVal.contains("-------")) {
            click("NATIVE", "xpath=//*[@id='DSA_OdometerTextField']", 0, 1);
            sc.sendText("2166");
            ((IOSDriver) driver).getKeyboard().sendKeys("'\n'");
            sc.syncElements(3000, 12000);
        }
        //click back button in Services screen
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        sc.syncElements(2000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Exit Appointment']", 0)) {
            click("NATIVE", "xpath=//*[@label='Yes, Exit']", 0, 1);
            sc.syncElements(4000, 30000);
        }

        //click make an appointment in Service Appointments screen - change dealer verification
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Make an Appointment']", 0)) {
            createLog("Make An Appointment button is displayed for preferred dealer in Service Appointments screen");
            click("NATIVE", "xpath=//*[@label='Make an Appointment']", 0, 1);
            sc.syncElements(4000, 10000);

            click("NATIVE", "xpath=//*[@label='Change Preferred Dealer']", 0, 1);
            sc.syncElements(5000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Select Dealer']", 0);
            click("NATIVE", "xpath=//*[@id='Search Name, City, or Zip Code']", 0, 1);
            sc.sendText("Toyota of Dallas");
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='search']"))
                click("NATIVE", "xpath=//*[@label='search']", 0, 1);
            sc.syncElements(5000, 30000);
            preferredDealerResult = sc.elementGetProperty("NATIVE", "xpath=(//*[@id='SelectPreferreDealer_DealerItem'])[1]", 0, "value");
            createLog("first preferred dealer displayed is: "+preferredDealerResult);
            click("NATIVE", "xpath=(//*[@id='SelectPreferreDealer_DealerItem'])[1]", 0, 1);
            sc.syncElements(5000, 30000);

            preferredDealerResultName = sc.elementGetProperty("NATIVE","xpath=//*[@id='VehicleDealerDetails_DealerAddressText'][1]",0,"value");
            createLog("preferred dealer ::"+preferredDealerResultName);
            sc.report("verify Toyota of Dallas preferred dealer is displayed in preferred dealer result", preferredDealerResultName.contains("Toyota of Dallas"));
            click("NATIVE", "xpath=//*[@label='Set as Preferred Dealer']", 0, 1);
            sc.syncElements(5000, 20000);
            //verify new preferred dealer success
            verifyElementFound("NATIVE", "xpath=//*[@label='Success']", 0);
            //TODO Typo below, Bug: OAD01-24710
            //verifyElementFound("NATIVE", "xpath=//*[contains(@label,'set your Preferred Dealer.')]", 0);
            click("NATIVE", "xpath=//*[@label='Done']", 0, 1);
            sc.syncElements(4000, 8000);

            for (int i=0; i<3; i++) {
                click("NATIVE","xpath=//*[@text='Back']",0,1);
                sc.syncElements(2000,4000);
                if (sc.isElementFound("NATIVE","xpath=//*[@label='Yes, Exit']",0)) {
                    click("NATIVE","xpath=//*[@label='Yes, Exit']",0,1);
                    sc.syncElements(2000,4000);
                    break;
                }
                else if (sc.isElementFound("NATIVE","xpath=//*[@label='Make an Appointment']",0)) {
                    break;
                }
            }

            //verify newly selected Preferred Dealer
            currentPreferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[@id='DealerServiceAppointment_PreferredDealerTileText' and @class='UIAStaticText'])[1]", 0, "value");
            sc.report("verify Toyota of Dallas preferred dealer is displayed in preferred dealer result", currentPreferredDealer.contains("Toyota of Dallas"));
        } else {
            createLog("Make An Appointment button is not displayed for preferred dealer in Service Appointments screen - changing preferred dealer");
            click("NATIVE", "xpath=//*[@label='LocationPinIcon']", 0, 1);
            sc.syncElements(4000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Preferred Dealer']", 0);
            click("NATIVE", "xpath=//*[@label='Change Preferred Dealer']", 0, 1);
            sc.syncElements(4000, 10000);

            //Click Search Name to choose new preferred dealer
            click("NATIVE", "xpath=//*[@id='Search Name, City, or Zip Code']", 0, 1);
            sc.sendText("Toyota of Dallas");
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='search']"))
                click("NATIVE", "xpath=//*[@label='search']", 0, 1);
            sc.syncElements(5000, 30000);

            preferredDealerResult = sc.elementGetProperty("NATIVE", "xpath=(//*[@id='SelectPreferreDealer_DealerItem'])[1]", 0, "value");
            createLog("first preferred dealer displayed is: "+preferredDealerResult);
            click("NATIVE", "xpath=(//*[@id='SelectPreferreDealer_DealerItem'])[1]", 0, 1);
            sc.syncElements(3000, 6000);

            preferredDealerResultName = sc.elementGetProperty("NATIVE","xpath=//*[@id='VehicleDealerDetails_DealerAddressText'][1]",0,"value");
            click("NATIVE", "xpath=//*[@label='Set as Preferred Dealer']", 0, 1);
            sc.syncElements(5000, 20000);
            //verify new preferred dealer success
            verifyElementFound("NATIVE", "xpath=//*[@label='Success']", 0);
            //TODO Typo below, Bug:OAD01-24710
            //verifyElementFound("NATIVE", "xpath=//*[contains(@label,'set your Preferred Dealer.')]", 0);
            click("NATIVE", "xpath=//*[@label='Done']", 0, 1);
            sc.syncElements(4000, 8000);

            //Back to Service Page
            for (int i=0; i<3; i++) {
                click("NATIVE","xpath=//*[@text='Back']",0,1);
                sc.syncElements(2000,4000);
                if (sc.isElementFound("NATIVE","xpath=//*[@label='Make an Appointment']",0)) {
                    break;
                }
            }
        }
        createLog("Verified change preferred dealer");
    }*/
    }
}