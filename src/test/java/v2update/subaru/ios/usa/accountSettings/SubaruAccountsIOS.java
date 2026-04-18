package v2update.subaru.ios.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.MobileElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruAccountsIOS extends SeeTestKeywords {
    String testName = "EV - Accounts EV - IOS";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                createLog("Started: Email Login");
                testName = System.getProperty("cloudApp") + testName;
                //App Login
                iOS_Setup2_5(testName);
                selectionOfCountry_IOS("USA");
                sc.startStepsGroup("email SignIn EV");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin("subarunextgen3@gmail.com", "Test$123456");
                createLog("Ended: Email Login");
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                iOS_Setup2_5(testName);
                sc.startStepsGroup("email SignIn EV");
                selectionOfCountry_IOS("USA");
                selectionOfLanguage_IOS("English");
                ios_keepMeSignedIn(true);
                ios_emailLogin("subarunextgen3@gmail.com","Test$123456");
                sc.stopStepsGroup();
        }
    }
    @AfterAll
    public void exit(){
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void profilePictureTest() {
        sc.startStepsGroup("Profile Picture Validations");
        profilePicture();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void notificationsTest() {
        sc.startStepsGroup("Account->Notifications Test");
        notifications();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void darkModeValidation() throws IOException {
        sc.startStepsGroup("DarkMode Validation started");
        darkMode();
        if(darkColorValidation("xpath=//*[@id='account_notification_account_button']").equalsIgnoreCase("white")){
            fail();
        }
        changeToLightModeTheme();
        sc.startStepsGroup("DarkMode Validation Ended");

    }

    @Test
    @Order(4)
    public void takeATourOnAccountsScreenTest() {
        sc.startStepsGroup("Test - Accounts - Take A Tour");
        takeATour();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void takeATour() {
        createLog("Started : Accounts - Take A Tour");
        if(!sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'Vehicle image')]", 0))
            reLaunchApp_iOS();
        sc.syncElements(5000, 10000);
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Take a Tour']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='TakeATour' and @XCElementType='XCUIElementTypeImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Explore the app to see what’s new']", 0);

        click("NATIVE", "xpath=//*[@text='Take a Tour']", 0, 1);
        sc.syncElements(5000, 30000);
        createLog("Started : See what’s new screen");

        //verifyElementFound("NATIVE", "xpath=//*[@id='ftue_image' and @XCElementType='XCUIElementTypeImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='See what’s new']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Check out our fresh new look designed to give you an even better experience. Features may vary depending on vehicle or location.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Close']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Start the Tour' and @id='ftue_seenew_button_cta']", 0);
        createLog("Completed : See what’s new screen");
        click("NATIVE", "xpath=//*[@text='Start the Tour' and @id='ftue_seenew_button_cta']", 0, 1);
        sc.syncElements(2000, 10000);

        //Start the Tour Screens
        createLog("Started : Start the Tour Screens");
        //vehicles screen
        verifyElementFound("NATIVE", "xpath=//*[@text='1 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='A new way to add or switch vehicles']", 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Tap your vehicle name to switch.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Close']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Next']", 0);
        click("NATIVE", "xpath=//*[@text='Next']", 0, 1);
        sc.syncElements(2000, 10000);

        //Remote connect screen
        verifyElementFound("NATIVE", "xpath=//*[@text='2 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Start, stop, lock and unlock your vehicle remotely. Plus give guest access to your vehicle.']", 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Close']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Next']", 0);
        click("NATIVE", "xpath=//*[@text='Next']", 0, 1);
        sc.syncElements(2000, 10000);

        //Vehicle Status screen
        verifyElementFound("NATIVE", "xpath=//*[@text='3 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Status']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Instantly check on the status of important alerts.']", 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Close']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Next']", 0);
        click("NATIVE", "xpath=//*[@text='Next']", 0, 1);
        sc.syncElements(2000, 10000);

        //Health screen
        verifyElementFound("NATIVE", "xpath=//*[@text='4 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Health']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Check your vehicle') and contains(@text,'maintenance, alerts')]", 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Close']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Next']", 0);
        click("NATIVE", "xpath=//*[@text='Next']", 0, 1);
        sc.syncElements(5000, 10000);

        //Vehicle Info screen
        verifyElementFound("NATIVE", "xpath=//*[@text='5 of 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Review your vehicle') and contains(@text,'manuals and warranties, subscriptions, and more.')]", 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Close']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Done']", 0);
        click("NATIVE", "xpath=//*[@text='Done']", 0, 1);
        sc.syncElements(2000, 10000);
        createLog("Completed : Start the Tour Screens");

        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Revisit the tutorial from your account, anytime.']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Enjoy the new app!']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Close']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='OK']", 0);
        click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
        sc.syncElements(4000, 16000);

        //verify dashboard
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed : Take A Tour");
    }

    public static void darkMode(){
        createLog("Started - Dark mode toggle on in account screen ");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='person']")){
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@text='DarkMode' and @class='UIAImage']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Dark Mode' and @class='UIAStaticText']",0);
        if(sc.isElementFound("NATIVE","xpath=//*[@value='toggleOff']")){
            click("NATIVE","xpath=//*[@value='toggleOff']",0,1);
        }
        verifyElementFound("NATIVE","xpath=//*[@value='toggleOn']",0);
        createLog("Completed - Dark mode toggle on in account screen ");
    }
    public static String darkColorValidation(String xpath) throws IOException {
        createLog("Started - color validation");
        sc.syncElements(2000, 30000);
        MobileElement elem = (MobileElement) driver.findElement(By.xpath(xpath));
        Point point = elem.getLocation();
        int leftX = point.getX();
        int leftY = point.getY();

        Dimension size = elem.getSize();
        int width = size.getWidth();
        int height = size.getHeight();

        int middleX = leftX + width / 2;
        int middleY = leftY + height / 2;
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        BufferedImage image = ImageIO.read(scrFile);
        // Getting pixel color by position x and y
        int clr = image.getRGB(middleX, middleY);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;

        createLog("Red Color value = " + red);
        createLog("Green Color value = " + green);
        createLog("Blue Color value = " + blue);

        // RGB value for black color (0, 0, 0)
        // RGB value for white color (255, 255, 255)
        String maximumValueColor = "";
        //if (red == 119 || red == 125 || red == 112 || red ==115 || red == 111 && green == 118 || green == 148 || green == 138 || green == 124 || green == 137 && blue == 188 || blue == 241 || blue == 215 || blue == 189 || blue == 221)
        if (red >= 240 && blue >= 240 && green >= 240){
            maximumValueColor = "white";
            createLog("Color validated White");
        }
        //else if (red == 122 || red == 96 || red == 98 || red == 118 || red == 117 && green == 128 || green == 109|| green == 132 || green == 144 || green == 142 && blue == 206 || blue == 165 || blue == 220 || blue == 221 || blue == 235)
        else if (red <= 50 && green <= 50 && blue <= 50) {
            maximumValueColor = "black";
            createLog("Color validated Black");
        }
        return maximumValueColor;
    }


    public static void profilePicture() {
        createLog("Started - Verifying profile picture");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]")) {
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account']", 0);
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE", "xpath=(//*[@label='Account Settings' or @label='ACCOUNT SETTINGS']/following::*[@text='view profile image'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='edit profile image']", 0);
        click("NATIVE", "xpath=//*[@label='edit profile image']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Take Photo']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Choose from Library']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Cancel']", 0);
        click("NATIVE", "xpath=//*[@label='Cancel']", 0, 1);
        sc.syncElements(2000, 10000);
        //click back in Account settings screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,30000);
        //Click close in accounts
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Verifying profile picture");
    }

    public static void notifications() {
        createLog("Started - Verifying Accounts->Notifications");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]")) {
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Notifications' and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Notifications' and @class='UIAImage']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='See notifications for your vehicle and account']", 0);
        click("NATIVE", "xpath=//*[@text='Notifications' and @class='UIAStaticText']", 0, 1);
        sc.syncElements(2000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_message']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']", 0);

        //applying notification filter
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@id,'notification filter')]")) {
            createLog("Applying notification filter - Remote Commands");
            click("NATIVE", "xpath=//*[contains(@id,'notification filter')]", 0, 1);
            sc.waitForElement("NATIVE", "xpath=//*[contains(@id,'Filter by') or contains(@id,'FILTER BY')]", 0,3000);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Remote Commands')]", 0);
            click("NATIVE", "xpath=//*[contains(@id,'Remote Commands')]", 0, 1);
            sc.waitForElement("NATIVE", "xpath=//*[@text='Notifications' or @text='NOTIFICATIONS']", 0,3000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Notifications' or @text='NOTIFICATIONS']", 0);
            createLog("Applied notification filter - Remote Commands");
        }

        String firstNotification = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"value");
        createLog("First notification displayed is: "+firstNotification);

        sc.syncElements(2000, 10000);
        //click back in Account settings screen
        click("NATIVE","xpath=//*[@text='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,30000);
        //Click close in accounts
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Accounts->Notifications");
    }

    public static void announcement() {
        createLog("Started - Verifying Accounts->Announcement");
        if(!sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Vehicle image')]")) {
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Account']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Announcement' and @class='UIAStaticText']", 0);
        /*
        Need to account with announcement
         */
        sc.syncElements(2000, 10000);
        //click back in Account settings screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,30000);
        //Click close in accounts
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Accounts->Notifications");
    }
    public static void changeToLightModeTheme()
    {
        if(!sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'Vehicle image')]", 0))
            reLaunchApp_iOS();
        sc.syncElements(5000, 10000);
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE","xpath=//*[@value='toggleOn']",0);
        click("NATIVE","xpath=//*[@value='toggleOn']",0,1);
        createLog("Completed - light mode toggle on");
    }


}
