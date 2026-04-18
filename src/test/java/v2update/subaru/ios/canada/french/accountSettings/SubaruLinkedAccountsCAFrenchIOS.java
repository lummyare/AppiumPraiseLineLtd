package v2update.subaru.ios.canada.french.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruLinkedAccountsCAFrenchIOS extends SeeTestKeywords {
    static String testName = "  Linked Accounts - IOS";

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
        iOS_Setup2_5(testName);
        sc.startStepsGroup("email SignIn 21MM");
        createLog("Start: 21mm email Login-USA-English");
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"), ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("End: 21mm email Login-USA-English");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }

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

        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Vehicle image')]")) {
            reLaunchApp_iOS();
        }
        //navigate to linked accounts screen
        navigateToLinkedAccountsScreen();
        verifyElementFound("NATIVE", "xpath=//*[(@text='Linked Accounts' or @text='LINKED ACCOUNTS') and @accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Enjoy streaming your audio in any supported vehicle using your linked accounts.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Block explicit content']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='LINKED_ACCOUNTS_VIEW_CONTROLLER_EXPLICIT_SWITCH']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Apple Music']", 0);
        //apple music icon
        verifyElementFound("NATIVE", "xpath=(//*[@text='Apple Music']/following-sibling::*[@class='UIAImage'])[1]", 0);
        //apple music navigation icon
        verifyElementFound("NATIVE", "xpath=(//*[@text='Apple Music']/following-sibling::*[@class='UIAImage'])[2]", 0);
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

        click("NATIVE", "xpath=//*[@text='Apple Music']", 0, 1);
        sc.syncElements(5000, 10000);

        if(isAppleMusicLinked) {
            createLog("Apple music is linked");
            verifyElementFound("NATIVE", "xpath=//*[@text='Apple Music' and @accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@class='UIAImage']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Set as default']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LINKED_ACCOUNT_DETAILS_VIEW_CONTROLLER_SAVE_BUTTON']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Unlink Account' and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Back']", 0);
        } else {
            createLog("Apple music is not linked");
        }
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        sc.syncElements(5000, 10000);
        createLog("Completed : Apple Music");
    }

    public static void amazonMusic() {
        createLog("Started : Amazon Music");

        if (!sc.isElementFound("NATIVE", "xpath=//*[(@text='Linked Accounts' or @text='LINKED ACCOUNTS') and @accessibilityLabel='TOOLBAR_LABEL_TITLE']")) {
            reLaunchApp_iOS();
            //navigate to linked accounts screen
            navigateToLinkedAccountsScreen();
        }
        verifyElementFound("NATIVE", "xpath=//*[(@text='Linked Accounts' or @text='LINKED ACCOUNTS') and @accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Music Services']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Enjoy streaming your audio in any supported vehicle using your linked accounts.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Block explicit content']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='LINKED_ACCOUNTS_VIEW_CONTROLLER_EXPLICIT_SWITCH']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Amazon Music']", 0);
        //Amazon music icon
        verifyElementFound("NATIVE", "xpath=(//*[@text='Amazon Music']/following-sibling::*[@class='UIAImage'])[1]", 0);
        //Amazon music navigation icon
        verifyElementFound("NATIVE", "xpath=(//*[@text='Amazon Music']/following-sibling::*[@class='UIAImage'])[2]", 0);
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

        click("NATIVE", "xpath=//*[@text='Amazon Music']", 0, 1);
        sc.syncElements(5000, 10000);

        if(isAmazonMusicLinked) {
            createLog("Amazon music is linked");
            verifyElementFound("NATIVE", "xpath=//*[@text='Amazon Music' and @accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@class='UIAImage']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Set as default']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LINKED_ACCOUNT_DETAILS_VIEW_CONTROLLER_SAVE_BUTTON']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Unlink Account' and @class='UIAStaticText']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Back']", 0);
        } else {
            createLog("Amazon music is not linked");
            if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Sign in' and @class='UIAStaticText')]")) {
                verifyElementFound("NATIVE", "xpath=//*[@text='Sign in' and @class='UIAStaticText']", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='Create a new Amazon account' and @class='UIAStaticText']", 0);
            } else if (sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Sign in')]")) {
                verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Sign in')]", 0);
                verifyElementFound("NATIVE", "xpath=//*[@text='Continue']", 0);
            } else {
                verifyElementFound("NATIVE", "xpath=//*[@id='Amazon.com']", 0);
                verifyElementFound("NATIVE", "xpath=(//*[@id='Type the characters you see in this image:'])[1]", 0);
            }
        }
        click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        sc.syncElements(5000, 10000);

        //navigate back to dashboard screen
        navigateBackToDashboardScreen();
        createLog("Completed : Amazon Music");
    }

    public static void navigateToLinkedAccountsScreen() {
        createLog("Started : Navigation to Linked Accounts Screen");
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 5000);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 5000);
        verifyElementFound("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(5000, 30000);
        sc.swipe("Down", sc.p2cy(60), 2000);
        verifyElementFound("NATIVE", "xpath=//*[@id='ACC_SETTINGS_MUSIC_CELL']", 0);
        click("NATIVE", "xpath=//*[@id='ACC_SETTINGS_MUSIC_CELL']", 0, 1);
        sc.syncElements(4000, 20000);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Linked Accounts' or @text='LINKED ACCOUNTS') and @accessibilityLabel='TOOLBAR_LABEL_TITLE']", 0);
        createLog("Completed : Navigation to Linked Accounts Screen");
    }

    public static void navigateBackToDashboardScreen() {
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Linked Accounts' or @text='LINKED ACCOUNTS') and @accessibilityLabel='TOOLBAR_LABEL_TITLE']")) {
            createLog("Started : Navigate Back to dashboard Screen");
            verifyElementFound("NATIVE", "xpath=//*[@text='Back']", 0);
            click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
            sc.syncElements(3000, 12000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Account Settings' or @text='ACCOUNT SETTINGS']", 0);
            sc.syncElements(3000, 12000);
            click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
            verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
            sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
            sc.syncElements(3000, 12000);
            verifyElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]",0);
            createLog("Completed : Navigate Back to dashboard Screen");
        } else {
            createLog("Unable to navigate back to dashboard Screen");
        }
    }
}

