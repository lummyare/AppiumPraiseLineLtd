package v2update.subaru.android.canada.French.service;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
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
public class SubaruServiceAppointmentsCAFrenchAndroid extends SeeTestKeywords {
    String testName = "ServiceAppointments-AndroidPRSpanish";

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
                android_emailLoginFrench(ConfigSingleton.configMap.get("strEmailCAFrench"), ConfigSingleton.configMap.get("strPasswordCAFrench"));
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench(ConfigSingleton.configMap.get("strEmailCAFrench"), ConfigSingleton.configMap.get("strPasswordCAFrench"));
                sc.stopStepsGroup();
        }
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void serviceAppointmentCard(){
        sc.startStepsGroup("Service Appointment Card");
        verifyServiceAppointmentCard();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void scheduleNewAppointmentTest() {
        sc.startStepsGroup("Test - Schedule New Appointment");
        /*
        Make new appointment
         */
        scheduleNewAppointment();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void editAppointmentTest() {
        sc.startStepsGroup("Test - Edit Service Appointment");
        /*
        Edit appointment
         */
        editServiceAppointment();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void serviceAppointmentScreenTest() {
        sc.startStepsGroup("Test - Service Appointment Screen");
        /*
        Verify the scheduled appointment
         */
        serviceAppointmentScreen();
        sc.stopStepsGroup();
    }


    @Test
    @Order(6)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if(!sc.isElementFound("NATIVE","xpath=//*[@resource-id='dashboard_display_image']"))
            reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }
    public void verifyServiceAppointmentCard(){
        createLog("Verifying service appointment card in service bottom bar");
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Servicio']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@content-desc='Servicio']"),0);
        click("NATIVE",convertTextToUTF8("//*[@content-desc='Servicio']"),0,1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Cita de servicio']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Haga una cita']"),0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Service Appointment Icon']",0);
        createLog("Verified service appointment card in service bottom bar");
    }

    public void scheduleNewAppointment() {
        createLog("Verifying create new service appointment");
        click("NATIVE",convertTextToUTF8("//*[@text='Haga una cita']"),0,1);
        sc.syncElements(5000, 30000);


        click("NATIVE",convertTextToUTF8("//*[@content-desc='Haga una cita']"),0,1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@class='android.widget.EditText']", 0);
        //if odometer value is ------- then enter odometer details
        String odometerVal = sc.elementGetText("NATIVE", "xpath=//*[@class='android.widget.EditText']", 0);


        //validate new disclaimer - 2.0.11 changes
        strAppType = strAppType.substring(0, 1).toUpperCase() + strAppType.substring(1);
        //verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Al seleccionar ”Continuar,” autorizo a "+strAppType+" a compartir mi VIN, millaje actual, información de contacto y opciones de servicio con el concesionario.']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='"+strAppType+" Declaracion de privacidad']", 0);
        //click on privacy statement link
        click("NATIVE","xpath=//*[@content-desc='"+strAppType+" Declaracion de privacidad']",0,1);
        createLog("Verifying privacy statement details in web page");
        sc.syncElements(10000, 30000);
        String privacyUrlText = sc.elementGetProperty("NATIVE", "xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]", 0, "text").toLowerCase();
        String url = ""+strAppType+".com/privacy";
        url = url.toLowerCase();
        sc.report("Verify privacy statement url contains expected url", privacyUrlText.contains(url));
        sc.verifyElementFound("NATIVE", "xpath=//*[@text='YOUR PRIVACY RIGHTS']", 0);
        // click Done
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        sc.syncElements(5000, 30000);
        createLog("Verified privacy statement details in web page");

        //click on Connected Services Privacy Notice link
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Connected Services Aviso de Privacidad']"), 0);
        click("NATIVE",convertTextToUTF8("//*[@contentDescription='Connected Services Aviso de Privacidad']"),0,1);
        createLog("Verifying Connected Services Privacy Notice in web page");
        sc.syncElements(10000, 30000);
        String privacyNoticeUrlText = sc.elementGetProperty("NATIVE", "xpath=//*[(contains(@id,'url_bar') or @id='location_bar_edit_text') and (@class='android.widget.EditText' or @class='android.widget.TextView')]", 0, "text").toLowerCase();
        sc.report("Verify Connected Services Privacy Notice url contains expected url", privacyNoticeUrlText.contains(url));
        sc.verifyElementFound("NATIVE", "xpath=//*[@text='PRIVACY NOTICE']", 0);
        // click Done
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        sc.syncElements(5000, 30000);
        createLog("Verified Connected Services Privacy Notice in web page");

        if (sc.isElementFound("NATIVE", "xpath=//*[@contentDescription='Continuar']"))
            click("NATIVE", "xpath=//*[@contentDescription='Continuar']", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Servicios']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Encontrar servicios']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Concesionario recomendado']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Adaptada para satisfacer sus condiciones de manejo locales.']"), 0);
        //click("NATIVE","(//*[@content-desc='Concesionario recomendado']/following::*[@class='android.view.View'])",4,1);
        click("NATIVE","xpath=//*[@content-desc='Continuar (1)']",0,1);
        sc.syncElements(5000, 30000);
        click("NATIVE","xpath=//*[@contentDescription='Custom Advisor']",0,1);
        click("NATIVE","xpath=//*[@contentDescription='Continuar']",0,1);
        sc.syncElements(5000, 30000);
        click("NATIVE","xpath=//*[@contentDescription='Continuar']",0,1);
        sc.syncElements(5000, 30000);
        selectDateAndTime();


        click("NATIVE", "xpath=//*[@content-desc='Confirmar cita']", 0, 1);
        sc.syncElements(5000, 30000);



        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='¡Cita confirmada!']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@contentDescription='Completado']"), 0, 1);

        //Appointment details screen
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Detalles de la cita']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Llamar al concesionario']"), 0);


        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Detalles del servicio')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Asesor de servicio')]", 0);
        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Cancelar cita']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Editar']", 0);


        createLog("Verified create new service appointment");
    }

    public void editServiceAppointment() {
        createLog("Verifying edit service appointment");
        click("NATIVE", "xpath=//*[@content-desc='Editar']", 0, 1);
        sc.syncElements(10000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Editar cita']", 0);

        String odometerVal = sc.elementGetText("NATIVE",convertTextToUTF8("//*[@contentDescription='Odómetro']/following::*[1]"),0);
        odometerVal = odometerVal.replaceFirst(",","");
        createLog("Displayed odometer value is :"+odometerVal);
        sc.report("Odometer is Numeric value ", CommonUtils.isNumeric(odometerVal));
        //verify value is greater than 0
        sc.report("odometer numeric value is greater than 0 ", Integer.parseInt(odometerVal) > 0);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='*Todos los horarios disponibles están en la zona horaria del concesionario']"), 0);
        click("NATIVE", "xpath=//*[contains(@content-desc,'Servicios')]", 0, 1);
        click("NATIVE", "xpath=//*[@contentDescription='Continuar']", 0, 1);

        sc.syncElements(4000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Confirmar cita']", 0);
        click("NATIVE", "xpath=//*[@contentDescription='Confirmar cita']", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Completado']"), 0);
        click("NATIVE", "xpath=//*[@content-desc='Completado']", 0, 1);
        click("NATIVE", "xpath=(//*[@content-desc='Concesionario recomendado']/following::*[@class='android.view.View'])[5]", 0, 1);
        click("NATIVE", "xpath=//*[contains(@content-desc,'Continuar')]", 0, 1);


        sc.syncElements(4000, 8000);


        sc.syncElements(5000, 30000);
        click("NATIVE","xpath=//*[@contentDescription='Custom Advisor']",0,1);
        click("NATIVE","xpath=//*[@contentDescription='Continuar']",0,1);
        sc.syncElements(5000, 30000);
        click("NATIVE","xpath=//*[@contentDescription='Continuar']",0,1);
        sc.syncElements(5000, 30000);
        // select date and time
        selectDateAndTime();


        verifyElementFound("NATIVE", "xpath=//*[@text='*Todos los horarios disponibles "+convertTextToUTF8("están")+" en la zona horaria del concesionario']", 0);
        click("NATIVE", "xpath=//*[contains(@label,'Continuar')]", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Haga una cita']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Confirmar cita']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='Al hacer clic en ”Confirmar cita”, acepta recibir llamadas de marketing o mensajes de texto marcados automáticamente en el número proporcionado. El consentimiento no es una condición de ninguna compra. Se aplican tarifas de mensajes y datos.']"), 0);
        click("NATIVE", "xpath=//*[@content-desc='Confirmar cita']", 0, 1);
        sc.syncElements(15000, 30000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@contentDescription='¡Cita confirmada!']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@contentDescription='Completado']"), 0, 1);

        createLog("Verified edit service appointment");
    }
    public void serviceAppointmentScreen() {
        createLog("Verifying new appointment in Service Appointment Screen");

        reLaunchApp_android();
       // createLog("Vehicle switcher - 5TDADAB59RS000015");
      //  VehicleSelectionIOS.Switcher("5TDADAB59RS000015");
        createLog("Vehicle switch completed");

        createLog("Clicking Service link");
        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_serviceTab']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Servicio']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='IconService']",0);
        click("NATIVE","xpath=//*[@id='BottomTabBar_serviceTab']",0,1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE","xpath=//*[@id='ServiceApptsCard_serviceAppointmentsTitle']",0);
        click("NATIVE","xpath=//*[@id='ServiceApptsCard_makeAnAppointmentButton']",0,1);
        sc.syncElements(5000, 30000);

        createLog("Handle Turn On Bluetooth popup if displayed");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]")) {
            sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
            sc.syncElements(5000, 30000);
        }


        //verify appointment is displayed
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'"+convertTextToUTF8("Tu cita está próxima")+"')]", 0);

        //verify Time zone disclaimer CMQA1-1874
        verifyElementFound("NATIVE", "xpath=//*[@text='*Todos los horarios disponibles "+convertTextToUTF8("están")+" en la zona horaria del concesionario']", 0);

        createLog("Verified new appointment in Service Appointment Screen");
    }

    public static String getMonth(int month) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CST"));
        String _month = "";
        switch (month) {
            case 0:
                _month = "ene";
                break;
            case 1:
                _month = "feb";
                break;
            case 2:
                _month = "mar";
                break;
            case 3:
                _month = "abr";
                break;
            case 4:
                _month = "may";
                break;
            case 5:
                _month = "jun";
                break;
            case 6:
                _month = "jul";
                break;
            case 7:
                _month = "ago";
                break;
            case 8:
                _month = "sep";
                break;
            case 9:
                _month = "oct";
                break;
            case 10:
                _month = "nov";
                break;
            case 11:
                _month = "dic";
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
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SUNDAY)
                serviceDate = serviceDate + 1;
        }
        createLog("Configured date");

        createLog("Selecting date");
        if (serviceDate == 1) // when service date is in the next Calendar month
        {
            sc.syncElements(2000, 4000);
            sc.click("NATIVE", "xpath=//*[@contentDescription='Forward']", 0, 1);
            sc.syncElements(2000, 4000);
            _month = getMonth(month + 1);
        }

        for (int i = 0; i < 15; i++) {
            String dateText = "";
            if(serviceDate < 10) {
                xpathStr = "//*[contains(@contentDescription," + "'0" + serviceDate + "/" + _month + "'" + ")]";
            } else {
                xpathStr = "//*[contains(@contentDescription," + "'" + serviceDate + "/" + _month + "'" + ")]";
            }
            createLog("xpath is :"+xpathStr);
            dateText = sc.elementGetProperty("NATIVE",xpathStr,0,"content-desc");
            createLog("date text is :"+dateText);
            boolean isDayNotSelectable = dateText.contains("Blackout date");
            if (isDayNotSelectable) {
                createLog("day fall on dealer close day - checking next day: "+dateText);
                serviceDate = serviceDate + 1;
            } else {
                createLog("date does not fall on dealer close day - selecting date: "+dateText);
                click("NATIVE", xpathStr, 0, 1);
                sc.syncElements(5000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@content-desc='Available Times']", 0);
                try {
                    blnNoAvailableTimeDisplay = driver.findElement(By.xpath("//*[@content-desc='No available times, select another day' or @content-desc='Please select a date to view available timeslots']")).isDisplayed();
                } catch (Exception e){
                    createLog("Checking element display - ignore exception");
                }
                if(blnNoAvailableTimeDisplay){
                    createLog("Displays message - No available times- SELECTING ANOTHER DAY");
                    serviceDate = serviceDate + 1;
                } else {
                    createLog("Time displayed for selected date");
                    break;
                }
            }
        }
        createLog("Selected date");

        createLog("Selecting time");
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'AM') or contains(@content-desc,'am')][2]"))
            click("NATIVE", "xpath=//*[contains(@content-desc,'AM') or contains(@content-desc,'am')][2]", 0, 1);
        else
            click("NATIVE", "xpath=//*[contains(@content-desc,'PM') or contains(@content-desc,'pm')][2]", 0, 1);
        createLog("Selected time");
    }

    public void changePreferredDealer() {
        createLog("Verifying change preferred dealer");

        createLog("Clicking Service link");
        verifyElementFound("NATIVE","xpath=//*[@id='BottomTabBar_serviceTab']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Servicio']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='IconService']",0);
        click("NATIVE","xpath=//*[@id='BottomTabBar_serviceTab']",0,1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE","xpath=//*[@id='ServiceApptsCard_serviceAppointmentsTitle']",0);
        sc.syncElements(5000, 30000);
        click("NATIVE","xpath=//*[@id='ServiceApptsCard_makeAnAppointmentButton']",0,1);
        
        createLog("Handle Turn On Bluetooth popup if displayed");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]")) {
            sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
            sc.syncElements(5000, 30000);
        }
        
        sc.syncElements(10000, 60000);

        createLog("Verifying Preferred Dealer");
        //preferred dealer
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Concesionario preferido')]", 0);
        click("NATIVE", "xpath=//*[contains(@label,'Concesionario preferido')]", 0, 1);
        sc.syncElements(40000, 80000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Concesionario preferido')]", 0);
        String preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@label,'Concesionario preferido')]/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
        createLog("Existing Preferred dealer is: "+preferredDealer);
        click("NATIVE", "xpath=//*[@label='Cambiar Concesionario Preferido']", 0, 1);
        sc.syncElements(15000, 60000);

        //Click Search Name to choose new preferred dealer
        click("NATIVE", "xpath=(//*[@label='swipe_bar']/following-sibling::*[@class='UIAView'])[1]", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@label='Seleccionar concesionario']", 0);
        click("NATIVE", "xpath=//*[contains(@label,'"+convertTextToUTF8("Buscar nombre, ciudad o código postal")+"')]", 0, 1);
        sc.sendText("Dallas");
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='search']"))
            click("NATIVE", "xpath=//*[@label='search']", 0, 1);
        sc.syncElements(5000, 30000);

        //Selecting a new dealer from the list
        String preferredDealerResult = sc.elementGetProperty("NATIVE", "xpath=(//*[@label='Seleccionar concesionario']/following::*[@class='UIAStaticText'])[6]", 0, "value");
        createLog("first preferred dealer displayed is: "+preferredDealerResult);
        click("NATIVE", "xpath=(//*[@label='Seleccionar concesionario']/following::*[@class='UIAStaticText'])[6]", 0, 1);
        sc.syncElements(30000, 60000);

        preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@label,'Detalles del Concesionario')]/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
        String preferredDealerSubString = preferredDealer.substring(0,10);
        sc.report("verify Tustin Lexus preferred dealer is not displayed in preferred dealer result", !preferredDealerSubString.contains("Tustin Lex"));
        sc.syncElements(20000, 60000);
        click("NATIVE", "xpath=//*[@label='Establecer como concesionario preferido']", 0, 1);
        sc.syncElements(5000, 20000);
        //verify new preferred dealer success
        verifyElementFound("NATIVE", "xpath=//*[@label='"+convertTextToUTF8("Éxito")+"']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Ha establecido su concesionario preferido.')]", 0);
        click("NATIVE", "xpath=//*[@label='Realizado']", 0, 1);
        sc.syncElements(20000, 60000);

        preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@label,'Concesionario preferido')]/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
        preferredDealerSubString = preferredDealer.substring(0,10);
        sc.report("verify Tustin Lexus preferred dealer is not displayed in preferred dealer result", !preferredDealerSubString.contains("Tustin Lex"));

        click("NATIVE", "xpath=//*[@label='Haga una cita']", 0, 1);
        sc.syncElements(5000, 20000);

        //verify newly selected dealer is displayed in Odometer screen
        preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[@label='icon__location']/preceding-sibling::*[@class='UIAView'])[1]", 0, "value");
        preferredDealerSubString = preferredDealer.substring(0,10);
        sc.report("verify Tustin Lexus preferred dealer is not displayed in preferred dealer result", !preferredDealerSubString.contains("Tustin Lex"));
        //if odometer value is ------- then enter odometer details
        String odometerVal = sc.elementGetText("NATIVE", "xpath=//*[contains(@label,'"+convertTextToUTF8("Odómetro\\n1 de 5")+"')]", 0);
        if (odometerVal.contains("-------")) {
            click("NATIVE", "xpath=//*[@class='UIATextField' and ./preceding::*[@label='back_button']]", 0, 1);
            sc.sendText("2166");
            sc.sendText("'\n'");
            sc.syncElements(3000, 12000);
        }
        click("NATIVE", "xpath=//*[@label='Continuar']", 0, 1);
        sc.syncElements(5000, 20000);
        //click back button in Services screen
        click("NATIVE", "xpath=//*[@label='back_button']", 0, 1);
        sc.syncElements(5000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Cita de salida']", 0)) {
            click("NATIVE", "xpath=//*[@label='Si, salir']", 0, 1);
        }
        sc.syncElements(5000, 30000);

        //click make an appointment in Service Appointments screen - change dealer verification
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Haga una cita']", 0)) {
            createLog("Make An Appointment button is displayed for preferred dealer in Service Appointments screen");
            click("NATIVE", "xpath=//*[@label='Haga una cita']", 0, 1);
            sc.syncElements(5000, 20000);

            //click Change Dealer in Odometer screen
            click("NATIVE", "xpath=//*[@label='Cambiar distribuidor']", 0, 1);
            sc.syncElements(15000, 60000);
            click("NATIVE", "xpath=(//*[@label='swipe_bar']/following-sibling::*[@class='UIAView'])[1]", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@label='Seleccionar concesionario']", 0);
            click("NATIVE", "xpath=//*[contains(@label,'"+convertTextToUTF8("Buscar nombre, ciudad o código postal")+"')]", 0, 1);
            sc.sendText("Tustin Lexus");
            //sc.sendText("Park place lexus plano");
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='search']"))
                click("NATIVE", "xpath=//*[@label='search']", 0, 1);
            sc.syncElements(5000, 30000);
            preferredDealerResult = sc.elementGetProperty("NATIVE", "xpath=(//*[@label='Seleccionar concesionario']/following::*[@class='UIAStaticText'])[6]", 0, "value");
            createLog("first preferred dealer displayed is: "+preferredDealerResult);
            click("NATIVE", "xpath=(//*[@label='Seleccionar concesionario']/following::*[@class='UIAStaticText'])[6]", 0, 1);
            sc.syncElements(5000, 30000);
            click("NATIVE", "xpath=//*[@label='Seleccionar concesionario']", 0, 1);
            sc.syncElements(5000, 30000);
            //if odometer value is ------- then enter odometer details
            odometerVal = sc.elementGetText("NATIVE", "xpath=//*[contains(@label,'"+convertTextToUTF8("Odómetro\\n1 de 5")+"')]", 0);
            if (odometerVal.contains("-------")) {
                click("NATIVE", "xpath=//*[@class='UIATextField' and ./preceding::*[@label='back_button']]", 0, 1);
                sc.sendText("2166");
                sc.sendText("'\n'");
                sc.syncElements(3000, 12000);
            }
            click("NATIVE", "xpath=//*[@label='Continuar']", 0, 1);
            sc.syncElements(5000, 20000);
            //click back button in Services screen
            click("NATIVE", "xpath=//*[@label='back_button']", 0, 1);
            sc.syncElements(5000, 20000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='Cita de salida']", 0)) {
                click("NATIVE", "xpath=//*[@label='Si, salir']", 0, 1);
            }
            sc.syncElements(10000, 30000);


            //preferred dealer
            verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Concesionario preferido')]", 0);
            click("NATIVE", "xpath=//*[contains(@label,'Concesionario preferido')]", 0, 1);
            sc.syncElements(10000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Concesionario preferido')]", 0);
            preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@label,'Concesionario preferido')]/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
            createLog("Existing Preferred dealer is: "+preferredDealer);
            click("NATIVE", "xpath=//*[@label='Cambiar Concesionario Preferido']", 0, 1);
            sc.syncElements(20000, 60000);

            //Click Search Name to choose new preferred dealer
            click("NATIVE", "xpath=(//*[@label='swipe_bar']/following-sibling::*[@class='UIAView'])[1]", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@label='Seleccionar concesionario']", 0);
            click("NATIVE", "xpath=//*[contains(@label,'"+convertTextToUTF8("Buscar nombre, ciudad o código postal")+"')]", 0, 1);
            sc.sendText("Tustin Lexus");
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='search']"))
                click("NATIVE", "xpath=//*[@label='search']", 0, 1);
            sc.syncElements(5000, 30000);

            preferredDealerResult = sc.elementGetProperty("NATIVE", "xpath=(//*[@label='Seleccionar concesionario']/following::*[@class='UIAStaticText'])[6]", 0, "value");
            createLog("first preferred dealer displayed is: "+preferredDealerResult);
            click("NATIVE", "xpath=(//*[@label='Seleccionar concesionario']/following::*[@class='UIAStaticText'])[6]", 0, 1);
            sc.syncElements(30000, 60000);

            preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@label,'Detalles del Concesionario')]/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
            preferredDealerSubString = preferredDealer.substring(0,10);
            createLog("preferred dealer ::"+preferredDealerSubString);
            sc.report("verify Tustin Lexus preferred dealer is displayed in preferred dealer result", preferredDealerSubString.contains("Tustin Lex"));
            sc.syncElements(20000, 60000);
            click("NATIVE", "xpath=//*[@label='Establecer como concesionario preferido']", 0, 1);
            sc.syncElements(5000, 20000);
            //verify new preferred dealer success
            verifyElementFound("NATIVE", "xpath=//*[@label='"+convertTextToUTF8("Éxito")+"']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Ha establecido su concesionario preferido.')]", 0);
            click("NATIVE", "xpath=//*[@label='Realizado']", 0, 1);
            sc.syncElements(20000, 60000);

            preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@label,'Concesionario preferido')]/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
            preferredDealerSubString = preferredDealer.substring(0,10);

            sc.report("verify Tustin Lexus preferred dealer is displayed in preferred dealer result", preferredDealerSubString.contains("Tustin Lex"));
            click("NATIVE", "xpath=//*[@label='remove_icon']", 0, 1);
            sc.syncElements(10000, 20000);
            click("NATIVE", "xpath=//*[@label='Haga una cita']", 0, 1);
            sc.syncElements(5000, 20000);

            //verify newly selected dealer is displayed in Odometer screen
            preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[@label='icon__location']/preceding-sibling::*[@class='UIAView'])[1]", 0, "value");
            preferredDealerSubString = preferredDealer.substring(0,10);
            sc.report("verify Tustin Lexus preferred dealer is displayed in preferred dealer result", preferredDealerSubString.contains("Tustin Lex"));
            //if odometer value is ------- then enter odometer details
            odometerVal = sc.elementGetText("NATIVE", "xpath=//*[contains(@label,'"+convertTextToUTF8("Odómetro\\n1 de 5")+"')]", 0);
            if (odometerVal.contains("-------")) {
                click("NATIVE", "xpath=//*[@class='UIATextField' and ./preceding::*[@label='back_button']]", 0, 1);
                sc.sendText("2166");
                sc.sendText("'\n'");
                sc.syncElements(3000, 12000);
            }
            click("NATIVE", "xpath=//*[@label='Continuar']", 0, 1);
            sc.syncElements(5000, 20000);
            //click back button in Services screen
            click("NATIVE", "xpath=//*[@label='back_button']", 0, 1);
            sc.syncElements(5000, 20000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='Cita de salida']", 0)) {
                click("NATIVE", "xpath=//*[@label='Si, salir']", 0, 1);
            }
            sc.syncElements(5000, 30000);

        } else {
            createLog("Make An Appointment button is not displayed for preferred dealer in Service Appointments screen - changing preferred dealer");
            click("NATIVE", "xpath=//*[contains(@label,'Concesionario preferido')]", 0, 1);
            sc.syncElements(30000, 60000);
            verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Concesionario preferido')]", 0);
            preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@label,'Concesionario preferido')]/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
            click("NATIVE", "xpath=//*[@label='Cambiar Concesionario Preferido']", 0, 1);
            sc.syncElements(5000, 30000);

            click("NATIVE", "xpath=(//*[@label='swipe_bar']/following-sibling::*[@class='UIAView'])[1]", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@label='Seleccionar concesionario']", 0);
            click("NATIVE", "xpath=//*[contains(@label,'"+convertTextToUTF8("Buscar nombre, ciudad o código postal")+"')]", 0, 1);
            sc.sendText("Tustin Lexus");
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='search']"))
                click("NATIVE", "xpath=//*[@label='search']", 0, 1);
            sc.syncElements(5000, 30000);

            preferredDealerResult = sc.elementGetProperty("NATIVE", "xpath=(//*[@label='Seleccionar concesionario']/following::*[@class='UIAStaticText'])[6]", 0, "value");
            createLog("first preferred dealer displayed is: "+preferredDealerResult);
            click("NATIVE", "xpath=(//*[@label='Seleccionar concesionario']/following::*[@class='UIAStaticText'])[6]", 0, 1);
            sc.syncElements(20000, 60000);

            preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@label,'Detalles del Concesionario')]/following-sibling::*[@class='UIAStaticText'])[1]", 0, "value");
            preferredDealerSubString = preferredDealer.substring(0,10);
            sc.report("verify Tustin Lexus preferred dealer is displayed in preferred dealer result", preferredDealerSubString.contains("Tustin Lex"));
            click("NATIVE", "xpath=//*[@label='Establecer como concesionario preferido']", 0, 1);
            sc.syncElements(5000, 20000);
            //verify new preferred dealer success
            verifyElementFound("NATIVE", "xpath=//*[@label='Éxito']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@label,'Ha establecido su concesionario preferido.')]", 0);
            click("NATIVE", "xpath=//*[@label='Realizado']", 0, 1);
            sc.syncElements(5000, 20000);

            click("NATIVE", "xpath=//*[@label='Haga una cita']", 0, 1);
            sc.syncElements(5000, 20000);
            //click back button in Odometer screen
            click("NATIVE", "xpath=//*[@label='back_button']", 0, 1);
            sc.syncElements(5000, 20000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='Cita de salida']", 0)) {
                click("NATIVE", "xpath=//*[@label='Si, salir']", 0, 1);
            }
            sc.syncElements(5000, 30000);
        }

        //click back button in Service Appointments screen
        click("NATIVE", "xpath=//*[@label='back_button']", 0, 1);
        createLog("Verified change preferred dealer");
    }
}