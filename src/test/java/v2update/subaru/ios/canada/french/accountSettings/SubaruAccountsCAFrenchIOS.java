package v2update.subaru.ios.canada.french.accountSettings;

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

public class SubaruAccountsCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruAccountsCAFrench-IOS";

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
        environmentSelection_iOS("prod");
        //App Login
        sc.startStepsGroup("Subaru email Login");
        createLog("Start: Subaru email Login");
        selectionOfCountry_IOS("canada");
        selectionOfLanguage_IOS("french");
        ios_keepMeSignedIn(true);
        ios_emailLoginFrench("subarunextgen3@gmail.com","Test$12345");
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
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
        //TO DO : Add steps to validate remote command notification with filter
        notifications();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void takeATourOnAccountsScreenTest() {
        sc.startStepsGroup("Test - Accounts - Take A Tour");
        takeATour();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void darkModeValidation() throws IOException {
        sc.startStepsGroup("DarkMode Validation started");
        darkMode();
        if(darkColorValidation("//*[@id='account_notification_account_button']").equalsIgnoreCase("black")){
            fail();
        }
        changeToLightModeTheme();
        sc.startStepsGroup("DarkMode Validation Ended");

    }

    @Test
    @Order(5)
    public void signOut() {
        sc.startStepsGroup("SignOut");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }
    public static void profilePicture() {
        createLog("Started - Verifying profile picture");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Vehicle image, double tap to open vehicle info.'] | //*[@text='Info']")) {
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Compte']"), 0);
        verifyElementFound("NATIVE","xpath=//*[@text='User']", 0);

        //Click on Accounts
        click("NATIVE", "xpath=//*[@id='account_notification_account_button']", 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE","xpath=//*[@text='edit profile image']", 0);
        verifyElementFound("NATIVE","xpath=//*[@text='view profile image' and ./parent::*[@class='UIATable']]", 0);

        click("NATIVE", "xpath=//*[@text='edit profile image']", 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Prenez une photo']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Choisir dans la bibliothèque']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Annuler']"), 0);

        click("NATIVE", convertTextToUTF8("//*[@text='Annuler']"), 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE","xpath=//*[@text='view profile image' and ./parent::*[@class='UIATable']]", 0);
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,30000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Compte']"), 0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Verifying profile picture");
    }

    public static void notifications() {
        createLog("Started - Verifying Accounts->Notifications");
        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Vehicle image')]")) {
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Boîte de réception']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Notifications' and @class='UIAStaticText']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Notifications' and @class='UIAImage']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les notifications pour votre véhicule et votre compte']"), 0);
        click("NATIVE", "xpath=//*[@text='Notifications' and @class='UIAStaticText']", 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE", "xpath=//*[@text='Notifications']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='TOOLBAR_BUTTON_LEFT']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Tous les véhicules']"), 0);

        //applying notification filter
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@id,'notification filter')]")) {
            createLog("Applying notification filter - Remote Commands");
            click("NATIVE", "xpath=//*[contains(@id,'notification filter')]", 0, 1);
            sc.waitForElement("NATIVE", "xpath=//*[@text='Filtrer par']", 0,3000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Commandes à distance']"), 0);
            click("NATIVE", convertTextToUTF8("//*[@text='Commandes à distance']"), 0, 1);
            sc.waitForElement("NATIVE", "xpath=//*[@text='Notifications']", 0,3000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Notifications']", 0);
            createLog("Applied notification filter - Remote Commands");
        }

        String firstNotification = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"value");
        createLog("First notification displayed is: "+firstNotification);

        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(10000,30000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Compte']"), 0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed - Verifying profile picture");
    }

    public static void darkMode(){
        createLog("Started - Dark mode toggle on in account screen ");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='person']")){
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@text='DarkMode' and @class='UIAImage']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Mode sombre' and @class='UIAStaticText']",0);
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


    public static void takeATour() {
        createLog("Started : Accounts - Take A Tour");
        if(!sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'Vehicle image')]", 0))
            reLaunchApp_iOS();
        sc.syncElements(5000, 10000);
        sc.waitForElement("NATIVE", "xpath=//*[@id='person']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(2000, 10000);

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Démonstration']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='TakeATour' and @XCElementType='XCUIElementTypeImage']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Explorez l’appli pour découvrir les nouveautés']"), 0);

        click("NATIVE", convertTextToUTF8("//*[@text='Démonstration']"), 0, 1);
        sc.syncElements(5000, 30000);
        createLog("Started : See what’s new screen");

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les nouveautés']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Découvrez notre nouveau look, qui se traduira par une expérience encore améliorée. Les caractéristiques peuvent varier en fonction du véhicule ou de l’emplacement.']"), 0);
        sc.isElementFound("NATIVE","xpath=//*[@id='ftue_close_icon_cta']");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Lancer l’aperçu' and @id='ftue_seenew_button_cta']"), 0);
        createLog("Completed : See what’s new screen");
        click("NATIVE", convertTextToUTF8("//*[@text='Lancer l’aperçu' and @id='ftue_seenew_button_cta']"), 0, 1);
        sc.syncElements(2000, 10000);

        //Start the Tour Screens
        createLog("Started : Start the Tour Screens");
        //vehicles screen
        verifyElementFound("NATIVE", "xpath=//*[@text='1 sur 5']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Une nouvelle façon d’ajouter un véhicule ou de changer de véhicule']"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Touchez le nom du véhicule pour changer de véhicule.']"), 0);
        sc.isElementFound("NATIVE","xpath=//*[@id='ftue_close_icon_cta']");
        verifyElementFound("NATIVE", "xpath=//*[@text='Suivant']", 0);
        click("NATIVE", "xpath=//*[@text='Suivant']", 0, 1);
        sc.syncElements(2000, 10000);

        //Remote connect screen
        verifyElementFound("NATIVE", "xpath=//*[@text='2 sur 5']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Remote Connect']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Démarrez, arrêtez, verrouillez et déverrouillez votre véhicule à distance. Et permettez à un conducteur invité d’avoir accès à votre véhicule.']"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        sc.isElementFound("NATIVE","xpath=//*[@id='ftue_close_icon_cta']");
        verifyElementFound("NATIVE", "xpath=//*[@text='Suivant']", 0);
        click("NATIVE", "xpath=//*[@text='Suivant']", 0, 1);
        sc.syncElements(2000, 10000);

        //Vehicle Status screen
        verifyElementFound("NATIVE", "xpath=//*[@text='3 sur 5']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='État du véhicule']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Vérifiez instantanément l’état des alertes importantes.']"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        sc.isElementFound("NATIVE","xpath=//*[@id='ftue_close_icon_cta']");
        verifyElementFound("NATIVE", "xpath=//*[@text='Suivant']", 0);
        click("NATIVE", "xpath=//*[@text='Suivant']", 0, 1);
        sc.syncElements(2000, 10000);

        //Health screen
        verifyElementFound("NATIVE", "xpath=//*[@text='4 sur 5']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Santé']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Vérifiez l') and contains(@text,'entretien et les alertes de votre véhicule et plus encore.')]"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        sc.isElementFound("NATIVE","xpath=//*[@id='ftue_close_icon_cta']");
        verifyElementFound("NATIVE", "xpath=//*[@text='Suivant']", 0);
        click("NATIVE", "xpath=//*[@text='Suivant']", 0, 1);
        sc.syncElements(5000, 10000);

        //Vehicle Info screen
        verifyElementFound("NATIVE", "xpath=//*[@text='5 sur 5']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Informations sur le véhicule']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Consultez les manuels et les garanties de votre véhicule, les abonnements, etc.')]"), 0);
        //verifyElementFound("NATIVE", "xpath=//*[@text='Video']", 0);
        sc.isElementFound("NATIVE","xpath=//*[@id='ftue_close_icon_cta']");
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Terminé']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@text='Terminé']"), 0, 1);
        sc.syncElements(2000, 10000);
        createLog("Completed : Start the Tour Screens");

        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Revisiter le tutoriel à partir de votre compte, en tout temps']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Profitez de la nouvelle appli!']", 0);
        sc.isElementFound("NATIVE","xpath=//*[@id='ftue_close_icon_cta']");
        verifyElementFound("NATIVE", "xpath=//*[@text='OK']", 0);
        click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
        sc.syncElements(4000, 16000);

        //verify dashboard
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed : Take A Tour");
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
