package v2update.subaru.android.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruLinkedAccountsAndroid extends SeeTestKeywords {
    String testName = "Linked Accounts - Android";

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
        selectionOfCountry_Android("USA");
        selectionOfLanguage_Android("English");
        android_keepMeSignedIn(true);
        android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void appleMusicTest() {
        sc.startStepsGroup("Test - Apple Music");
        appleMusic();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void amazonMusicTest() {
        sc.startStepsGroup("Test - Amazon Music");
        amazonMusic();
        sc.stopStepsGroup();
    }

    public static void appleMusic() {
        createLog("Started : Apple Music");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info']")) {
            reLaunchApp_android();
        }
        navigateToLinkedAccountsScreen();

        //Block Explicit Content switch
        verifyElementFound("NATIVE","xpath=//*[@text='Block explicit content']",0);
        turnOnOffSwitch("Block explicit content","//*[@id='music_explicit_preference_switch']");

        //Apple Music
        createLog("Verifying Apple Music");
        verifyElementFound("NATIVE", "xpath=//*[@text='Apple Music']", 0);
        //apple music icon
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Apple Music' and @class='android.widget.ImageView']", 0);
        //check Apple Music is linked or not
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Apple Music']/following-sibling::*[contains(@text,'Linked')]")) {
            createLog("Apple music is linked");
            sc.report("Apple music is linked",true);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Apple Music']/following-sibling::*[contains(@text,'Default')]")) {
                createLog("Apple music is defaulted");
                sc.report("Apple music is defaulted", true);
            } else {
                createLog("Apple music is not defaulted");
            }
        } else {
            createLog("Apple music is not linked");
            verifyElementFound("NATIVE", "xpath=//*[@text='Apple Music']/following-sibling::*[contains(@text,'Link Account')]", 0);
        }

        //apple music link status
        boolean isAppleMusicLinked = sc.isElementFound("NATIVE", "xpath=//*[@text='Apple Music']/following-sibling::*[contains(@text,'Linked')]");
        createLog("is apple music is linked: "+isAppleMusicLinked);

        if(isAppleMusicLinked)
            click("NATIVE", "xpath=//*[@id='apple_music_linked_tv']", 0, 1);
        else
            click("NATIVE", "xpath=//*[@id='apple_music_link_tv']", 0, 1);
        sc.syncElements(5000, 10000);

        if(isAppleMusicLinked) {
            createLog("Apple music is linked");
            verifyElementFound("NATIVE", "xpath=//*[@text='Apple Music']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Amazon Music']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Set as default']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Unlink Account']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Save']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@contentDescription='Navigate up']", 0);
            click("NATIVE", "xpath=//*[@contentDescription='Navigate up']", 0, 1);
            sc.syncElements(5000, 10000);
        } else {
            createLog("Apple music is not linked");
            click("NATIVE", "xpath=//*[@content-desc='Close tab']", 0, 1);
            sc.syncElements(5000, 10000);
        }
        createLog("Verified Apple Music");
    }

    public static void amazonMusic() {
        createLog("Verifying Amazon Music");

        if (!sc.isElementFound("NATIVE", "xpath=//*[(@text='Linked Accounts' or @text='LINKED ACCOUNTS')]")) {
            reLaunchApp_android();
            navigateToLinkedAccountsScreen();
        }
        //Amazon Music
        verifyElementFound("NATIVE", "xpath=//*[@text='Amazon Music']", 0);
        //Amazon music icon
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Amazon Music' and @class='android.widget.ImageView']", 0);
        //check Amazon Music is linked or not
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Amazon Music']/following-sibling::*[contains(@text,'Linked')]")) {
            createLog("Amazon music is linked");
            sc.report("Amazon music is linked",true);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Amazon Music']/following-sibling::*[contains(@text,'Default')]")) {
                createLog("Amazon music is defaulted");
                sc.report("Amazon music is defaulted", true);
            } else {
                createLog("Amazon music is not defaulted");
            }
        } else {
            createLog("Amazon music is not linked");
            verifyElementFound("NATIVE", "xpath=//*[@text='Amazon Music']/following-sibling::*[contains(@text,'Link Account')]", 0);
        }

        //Amazon music link status
        boolean isAmazonMusicLinked = sc.isElementFound("NATIVE", "xpath=//*[@text='Amazon Music']/following-sibling::*[contains(@text,'Linked')]");
        createLog("is Amazon music is linked: "+isAmazonMusicLinked);

        click("NATIVE", "xpath=//*[@id='amazon_music_link_tv' or @id='amazon_music_linked_tv']", 0, 1);
        sc.syncElements(5000, 10000);

        if(isAmazonMusicLinked) {
            createLog("Amazon music is linked");
            verifyElementFound("NATIVE","xpath=//*[@text='Set as default']",0);
            verifyElementFound("NATIVE","xpath=//*[@text='Streaming quality']",0);
            verifyElementFound("NATIVE","xpath=//*[@text='Unlink Account']",0);
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(5000, 10000);
        } else if (sc.isElementFound("NATIVE","xpath=//*[@text='Enter the characters you see below']")) {
            createLog("Amazon music is not linked");
            verifyElementFound("NATIVE", "xpath=//*[@text='Type the characters you see in this image:']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        } else {
            createLog("Amazon music is not linked");
            verifyElementFound("NATIVE", "xpath=//*[((@text='Sign in' and @class='android.widget.TextView') or @text='Amazon.com') or (@text='Linked Accounts' or @text='LINKED ACCOUNTS')]", 0);
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(5000, 10000);
        }
        createLog("Verified Amazon Music");

        //navigate to dashboard screen
        verifyElementFound("NATIVE","xpath=//*[@text='Linked Accounts' or @text='LINKED ACCOUNTS']",0);
        click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account' or @text='ACCOUNT']", 0);
        //click back
        click("NATIVE", "xpath=(//*[@class='android.widget.ImageButton'])[1]", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@content-desc='drag' or @content-desc='Drag']", 0, "Down");
        sc.syncElements(3000, 12000);
        verifyElementFound("NATIVE","xpath=//*[@id='dashboard_display_image']",0);
        createLog("Completed : Amazon Music");
    }

    //common methods
    public static boolean isSwitchEnabled(String strSection, String strSwitchXpath) {
        boolean isSwitchEnabledBln;
        createLog("Getting Switch value for section : "+strSection+" ");
        String switchVal = sc.elementGetProperty("NATIVE", strSwitchXpath, 0, "checked");
        isSwitchEnabledBln = Boolean.parseBoolean(switchVal);
        createLog("Switch boolean value for section : "+strSection+" is - "+isSwitchEnabledBln);
        return isSwitchEnabledBln;
    }

    public static void turnOnOffSwitch(String strSection, String strSwitchXpath) {
        createLog("Started : Turn ON OFF switch for section : "+strSection);
        boolean isSwitchEnabledBln = isSwitchEnabled(strSection, strSwitchXpath);
        if(isSwitchEnabledBln) {
            createLog(" "+strSection+" switch is currently enabled - disabling switch");
            //disabling switch
            click("NATIVE", strSwitchXpath, 0, 1);
            sc.syncElements(3000, 30000);
            sc.swipe("Down", sc.p2cy(70), 3000);
            boolean disabledSwitchVal = isSwitchEnabled(strSection, strSwitchXpath);
            if(disabledSwitchVal) {
                sc.report("Verifying disabling "+strSection+" section switch failed",false);
                createErrorLog(" "+strSection+"  switch failed - switch is still enabled");
            } else {
                createLog("Disabled "+strSection+" section switch success");
                sc.report("Disabling "+strSection+" section switch", true);
            }
        } else {
            createLog(" "+strSection+" switch is currently disabled - enabling switch");
            //enabling switch
            click("NATIVE", strSwitchXpath, 0, 1);
            sc.syncElements(3000, 30000);
            sc.swipe("Down", sc.p2cy(70), 3000);
            boolean enabledSwitchVal = isSwitchEnabled(strSection, strSwitchXpath);
            if(enabledSwitchVal) {
                createLog("Enabled "+strSection+" section switch success");
                sc.report("Enabled "+strSection+" section switch", true);
            } else {
                sc.report("Verifying disabling "+strSection+" section switch",false);
                createErrorLog(" "+strSection+"  switch failed - switch is still enabled");
            }
        }
        createLog("Completed : Turn ON OFF switch for section : "+strSection);
    }

    public static void navigateToLinkedAccountsScreen() {
        createLog("Started : Navigation to Linked Accounts Screen");
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(5000, 30000);
        click("NATIVE", "xpath=//*[@text='Account']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account' or @text='ACCOUNT']", 0);
        verifyElementFound("NATIVE","xpath=//*[(@text='Linked Accounts' or @text='LINKED ACCOUNTS') and @id='tv_linked_account']",0);
        click("NATIVE", "xpath=//*[(@text='Linked Accounts' or @text='LINKED ACCOUNTS') and @id='tv_linked_account']", 0, 1);
        sc.syncElements(5000, 60000);
        //Linked Accounts screen
        verifyElementFound("NATIVE","xpath=//*[@text='Linked Accounts' or @text='LINKED ACCOUNTS']",0);
        createLog("Completed : Navigation to Linked Accounts Screen");
    }
}
