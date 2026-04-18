package v2update.subaru.android.usa.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDealersAndroid extends SeeTestKeywords {
    String testName = "Dealers - Android";

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
        //selectionOfCountry_Android("USA");
        //selectionOfLanguage_Android("English");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void dealersTest() {
        sc.startStepsGroup("Dealers under Find");
        validateDealers();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))
            reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }


    public static void validateDealers() {
        createLog("Validate Dealers");
        sc.syncElements(5000, 8000);
        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Find']")){
            reLaunchApp_android();
        }
        click("NATIVE", "xpath=//*[@text='Find']", 0, 1);
        sc.syncElements(2000, 4000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@text='Dealers']",0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Find a dealer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Dealers']", 0);
        click("NATIVE", "xpath=//*[contains(@contentDescription,'Dealers')]", 0, 1);
        sc.syncElements(5000, 8000);
        //TODO Bug OAD01-24339, infinite load
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        sc.syncElements(5000, 30000);

        //If no preferred dealer in account -> Preferred Dealer screen will not display.
        if(sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Preferred Dealer')]")) {
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Preferred Dealer')]", 0);
            String preferredDealer = sc.elementGetProperty("NATIVE", "xpath=(//*[contains(@content-desc,'Preferred Dealer')]/following::*[@class='android.view.View'])[1]", 0, "content-desc");
            createLog("Existing Preferred dealer is: " + preferredDealer);
            click("NATIVE", "xpath=//*[@content-desc='Change Preferred Dealer']", 0, 1);
            sc.syncElements(5000, 30000);
        }

        //Click Search Name to choose new preferred dealer
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Select Dealer']", 0);
        click("NATIVE", "xpath=(//*[@content-desc='Select Dealer']//following-sibling::*[@class='android.view.View'])[1]", 0, 1);
        //click("NATIVE", "xpath=(//*[@contentDescription='Select Dealer']/*[@class='android.view.View'])", 0, 1);
        sc.sendText("10019");
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
        //click("NATIVE", "xpath=//*[@content-desc='Search' or @content-desc='Enter'] | //*[contains(@text,'Search Name')]", 0, 1);
        sc.syncElements(5000, 50000);
        //Dealer is not selectable issue exists
        click("NATIVE", "xpath=(//*[contains(@text,'Search Name, City')]/following::*[@class='android.view.View'])[8]", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='phone_small_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Manhattan')]",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Call']",0);
        verifyElementFound("NATIVE","xpath=//*[@content-desc='web_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Website']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='detail_direction_icon']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Directions']",0);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Service Hours']",0);
        click("NATIVE", "xpath=//*[@contentDescription='Service Hours']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Monday')]",0);
        click("NATIVE", "xpath=//*[@contentDescription='Service Hours']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Services']",0);
        click("NATIVE", "xpath=//*[@contentDescription='Services']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Car')]",0);
        click("NATIVE", "xpath=//*[@contentDescription='Services']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Amenities']",0);
        click("NATIVE", "xpath=//*[@contentDescription='Amenities']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Wifi')] | //*[contains(@contentDescription,'EV')]",0);
        click("NATIVE", "xpath=//*[@contentDescription='Amenities']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Payment Methods']",0);
        click("NATIVE", "xpath=//*[@contentDescription='Payment Methods']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Credit')]",0);
        click("NATIVE", "xpath=//*[@contentDescription='Payment Methods']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Accessibility']",0);
        click("NATIVE", "xpath=//*[@contentDescription='Accessibility']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Bilingual Staff')] |//*[contains(@contentDescription,'Wheelchair')]",0);
        click("NATIVE", "xpath=//*[@contentDescription='Accessibility']/*[contains(@contentDescription,'filter')]", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='Transportation']",0);
        sc.swipeWhileNotFound("DOWN", sc.p2cy(40), 2000, "NATIVE", "xpath=//*[@contentDescription='Transportation']/*[contains(@contentDescription,'filter')]", 0, 1000, 2, false);
        click("NATIVE", "xpath=//*[@contentDescription='Transportation']/*[contains(@contentDescription,'filter')]", 0, 1);
        sc.swipeWhileNotFound("DOWN", sc.p2cy(40), 2000, "NATIVE", "xpath=//*[contains(@contentDescription,'Pick Up')]", 0, 1000, 2, false);
        verifyElementFound("NATIVE","xpath=//*[contains(@contentDescription,'Pick Up')]",0);
        click("NATIVE", "xpath=//*[@contentDescription='Transportation']/*[contains(@contentDescription,'filter')]", 0, 1);

        //set preferred dealer
        if(sc.isElementFound("NATIVE","xpath=//*[@contentDescription='Set as Preferred Dealer']")) {
            createLog("Setting up preferred dealer");
            verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Set as Preferred Dealer']", 0);
            click("NATIVE", "xpath=//*[@contentDescription='Set as Preferred Dealer']", 0, 1);
            sc.syncElements(10000, 20000);
            createLog("verifying success message");
            verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Success']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Done']", 0);
            createLog("Navigating back to Preferred dealer page");
            click("NATIVE", "xpath=//*[@contentDescription='Done']", 0, 1);
            sc.syncElements(5000, 10000);
            createLog("Navigated back to Preferred dealer page");
            verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Preferred Dealer']", 0);
            createLog("Navigating back to Find page");
        }
        verifyElementFound("NATIVE","xpath=//*[@contentDescription='remove_icon']",0);
        createLog("Navigating back to Find page");
        for(int i=1; i<4; i++){
            if(sc.isElementFound("NATIVE","xpath=//*[@content-desc='Dealers']")){
                break;
            }else{
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(2000, 4000);
            }
        }
        sc.syncElements(5000, 10000);
        createLog("Navigated back to Find page");
        verifyElementFound("NATIVE","xpath=//*[@text='Find']",0);

    }
}
