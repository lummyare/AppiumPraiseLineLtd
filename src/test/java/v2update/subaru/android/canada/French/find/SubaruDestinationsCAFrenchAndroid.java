package v2update.subaru.android.canada.French.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Random;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruDestinationsCAFrenchAndroid extends SeeTestKeywords{
    String testName = "Destinations-Android";
    static String homeAddress = randomAddress();
    static String workAddress = randomAddress();

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
                android_emailLoginFrench("subarustageca@mail.tmnact.io", "Test$123");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("21mm login");
                selectionOfCountry_Android("canada");
                selectionOfLanguage_Android("french");
                android_keepMeSignedIn(true);
                android_emailLoginFrench("subarustageca@mail.tmnact.io", "Test$123");
                sc.stopStepsGroup();
        }
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }


    @Test
    @Order(1)
    public void Destinations() {
        sc.startStepsGroup("Destinations under Find");
        validateDestinations();
        sc.stopStepsGroup();
    }
///Home address is not able to setup issue exists
    @Test
    @Order(2)
    public void DestinationsHomeAddress() {
        sc.startStepsGroup("Validate Home Address");
        validateHomeAddress();
        sc.stopStepsGroup();
    }

    //Work address is not able to setup issue exists
    @Test
    @Order(3)
    public void DestinationsWorkAddress(){
        sc.startStepsGroup("Validate Work Address");
        validateWorkAddress();
        sc.stopStepsGroup();
    }
    //Send2Car is not getting removed issue exists
    @Test
    @Order(4)
    public void Send2Car() {
        sc.startStepsGroup("Validating Send to Car");
        validateSend2Car(this.homeAddress);
        sc.stopStepsGroup();
    }
    // favorite is not getting removed issues exists
    @Test
    @Order(5)
    public void DestinationsFavAddress() {
        sc.startStepsGroup("Validate Clearing Fav Address");
        validateFavAddress();
        sc.stopStepsGroup();
    }


    @Test
    @Order(6)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Open Vehicle Info']"))
            reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }


    public void validateDestinations() {
        createLog("Validate Destinations");
        click("NATIVE", "xpath=//*[@text='Trouver']", 0, 1);
        sc.syncElements(5000, 8000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Destinations']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Favoris, récents et envoyés à la voiture")+"']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Destinations']", 0);
        click("NATIVE", "xpath=//*[contains(@contentDescription,'Destinations')]", 0, 1);
        sc.syncElements(5000, 8000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']|//*[@text='MES DESTINATIONS']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Domicile']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Travail']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Favoris']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Envoyée à la voiture")+"']", 0);
    }

    public static void validateSend2Car(String address) {
        createLog(" Home destination- Send to Car");
        verifyElementFound("NATIVE", "xpath=//*[@id='etMapSearch']", 0);
        click("NATIVE", "xpath=//*[@id='etMapSearch']", 0, 1);
        click("NATIVE", "xpath=//*[@id='etSearchBox']", 0, 1);
        sendText("NATIVE", "xpath=//*[@id='etSearchBox']", 0, address);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=(//*[@text=" + "'" + address + "'" + "])[2]", 0, 1);
        sc.syncElements(10000, 20000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Envoyer à la voiture']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Envoyer à la voiture']"), 0, 1);
        createLog("Send2Car destination sent...");
        sc.syncElements(4000, 8000);
        for (int i = 0; i <= 1; i++) {
            click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        }
        click("NATIVE", "xpath=//*[@id='stc_pref_tv']", 0, 1);
        sc.syncElements(4000, 8000);
        click("NATIVE", "xpath=//*[@id='tv_stc_done']", 0, 1);
        sc.syncElements(4000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@text=" + "'" + address + "'" + "]", 0);
        click("NATIVE", "xpath=//*[@id='destination_minus_iv' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text=" + "'" + address + "'" + "]]]", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Supprimer cette destination de celles envoyées à la voiture")+"']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Êtes-vous sûr de vouloir supprimer cette destination de celles envoyées à la voiture?")+"']", 0);
        click("NATIVE", "xpath=//*[@text='Supprimer']", 0, 1);
        sc.syncElements(4000, 8000);
        click("NATIVE", "xpath=//*[@id='tv_stc_done']", 0, 1);
        sc.syncElements(4000, 8000);
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
    }

    public static void validateHomeAddress() {
        createLog(homeAddress);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='home_pref_tv']")) {
            click("NATIVE", "xpath=//*[@id='home_pref_tv']", 0, 1);
            sc.syncElements(2000, 5000);
            createLog("Checking if the Home address exists");
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='remove_pref_tv']")) {
                createLog("Removing Existing Saved Home Address ");
                click("NATIVE", "xpath=//*[@id='remove_pref_tv']", 0, 1);
                sc.syncElements(2000, 4000);
                verifyElementFound("NATIVE", "xpath=//*[@id='home_setup_tv']", 0);
                click("NATIVE", "xpath=//*[@id='home_setup_tv']", 0, 1);
            }
            createLog("Searching Home address...");
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'domicile')]|//*[contains(@text,'DOMICILE')]", 0);
            click("NATIVE", "xpath=//*[@id='etSearchBox']", 0, 1);
            sendText("NATIVE", "xpath=//*[@id='etSearchBox']", 0, homeAddress);
            delay(4000);
            //click("NATIVE", "xpath=//*[@id='location_item_main_ll']", 0, 1);
            click("NATIVE", "xpath=(//*[@text=" + "'" + homeAddress + "'" + "])[2]", 0, 1);
            sc.syncElements(4000, 8000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Sauvegarder']")) {
                click("NATIVE", "xpath=//*[@text='Sauvegarder']", 0, 1);
                sc.syncElements(3000, 5000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer']", 0);
                createLog("Home Destination Saved successfully");
                for (int i = 0; i <= 2; i++) {
                    click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
                }
                click("NATIVE", "xpath=//*[contains(@contentDescription,'Destinations')]", 0, 1);
                verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']|//*[@text='MES DESTINATIONS']", 0);
                sc.verifyElementNotFound("NATIVE","xpath=//*[@id='home_setup_tv']",0);
                createLog("My Destinations Page available...");
            }

        } else {
            createLog("My Destinations Page not ready....");
        }
    }

    public static void validateWorkAddress() {
        createLog(workAddress);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='work_pref_tv']")) {
            click("NATIVE", "xpath=//*[@id='work_pref_tv']", 0, 1);
            sc.syncElements(2000, 5000);
            createLog("Checking if the Work address exists");
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Supprimer travail']")) {
                createLog("Removing Existing Saved Work Address ");
                click("NATIVE", "xpath=//*[@text='Supprimer travail']", 0, 1);
                sc.syncElements(2000, 4000);
                verifyElementFound("NATIVE", "xpath=//*[@id='work_setup_tv']", 0);
                click("NATIVE", "xpath=//*[@id='work_setup_tv']", 0, 1);
            }
            createLog("Searching Work address...");
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'travail')]|//*[contains(@text,'TRAVAIL')]", 0);
            click("NATIVE", "xpath=//*[@id='etSearchBox']", 0, 1);
            sendText("NATIVE", "xpath=//*[@id='etSearchBox']", 0, workAddress);
            delay(4000);
           // click("NATIVE", "xpath=//*[@id='location_item_main_ll']", 0, 1);
            click("NATIVE", "xpath=(//*[@text=" + "'" + workAddress + "'" + "])[2]", 0, 1);
            sc.syncElements(4000, 8000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Sauvegarder']")) {
                click("NATIVE", "xpath=//*[@text='Sauvegarder']", 0, 1);
                sc.syncElements(3000, 5000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer']", 0);
                createLog("Work Destination Saved successfully");
                for (int i = 0; i <= 2; i++) {
                    click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
                }
                click("NATIVE", "xpath=//*[contains(@contentDescription,'Destinations')]", 0, 1);
                verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']|//*[@text='MES DESTINATIONS']", 0);
                sc.verifyElementNotFound("NATIVE","xpath=//*[@id='work_setup_tv']",0);
                createLog("My Destinations Page available...");
            }

        } else {
            createLog("My Destinations Page not ready....");
        }
    }

    public void validateFavAddress() {
        verifyElementFound("NATIVE", "xpath=//*[@id='etMapSearch']", 0);
        click("NATIVE", "xpath=//*[@id='etMapSearch']", 0, 1);
        click("NATIVE", "xpath=//*[@id='etSearchBox']", 0, 1);
        sendText("NATIVE", "xpath=//*[@id='etSearchBox']", 0, homeAddress);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=(//*[@text=" + "'" + homeAddress + "'" + "])[2]", 0, 1);
        sc.syncElements(10000, 20000);
        click("NATIVE", "xpath=//*[@id='tv_share_favorites']", 0, 1);
        //click("NATIVE", "xpath=//*[@text='Save as Favorites']", 0, 1);
        sc.syncElements(3000, 5000);
        for (int i = 0; i <= 1; i++) {
            click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        }
        click("NATIVE", "xpath=//*[@id='fav_pref_tv']", 0, 1);
        sc.syncElements(3000, 5000);
        click("NATIVE", "xpath=//*[@id='tv_fav_done']", 0, 1);
        sc.syncElements(4000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@text=" + "'" + homeAddress + "'" + "]", 0);
        click("NATIVE", "xpath=//*[@id='destination_favorite_minus_iv' and (./preceding-sibling::* | ./following-sibling::*)[./*[@text=" + "'" + homeAddress + "'" + "]]]", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Supprimer la destination sauvegardée")+"']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("Supprimer la destination sauvegardée")+"']", 0);
        click("NATIVE", "xpath=//*[@text='Supprimer']", 0, 1);
        sc.syncElements(4000, 8000);
        click("NATIVE", "xpath=//*[@id='tv_fav_done']", 0, 1);
        sc.syncElements(4000, 8000);
        for (int i = 0; i <=1; i++) {
            click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        }
    }

    public static String randomAddress() {
        ArrayList<String> addresses = new ArrayList<String>();
        addresses.add("8401 Memorial Ln");
        addresses.add("7675 Legacy Dr");
        addresses.add("2827 W Wheatland Rd");

        Random address = new Random();
        int randomitem = address.nextInt(addresses.size());
        String randomAdress = addresses.get(randomitem);
        return randomAdress;
    }
}
