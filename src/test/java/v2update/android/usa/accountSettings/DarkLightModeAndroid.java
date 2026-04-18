package v2update.android.usa.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
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
public class DarkLightModeAndroid extends SeeTestKeywords {
    String testName = " - DarkandLightModeAndroid";

    @BeforeAll
    public void setup() throws Exception {
        switch (ConfigSingleton.configMap.get("local")) {
            case (""):
                testName = System.getProperty("cloudApp") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("email SignIn 21MM");
                selectionOfCountry_Android("USA");
                selectionOfLanguage_Android("English");
                android_keepMeSignedIn(true);
                android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21MM"), ConfigSingleton.configMap.get("strPassword21MM"));
                sc.stopStepsGroup();
                break;
            default:
                testName = ConfigSingleton.configMap.get("local") + testName;
                android_Setup2_5(testName);
                sc.startStepsGroup("email SignIn 21MM");
                selectionOfCountry_Android("USA");
                selectionOfLanguage_Android("English");
                android_keepMeSignedIn(true);
                android_emailLoginIn(ConfigSingleton.configMap.get("strEmail21MM"), ConfigSingleton.configMap.get("strPassword21MM"));
                sc.stopStepsGroup();
        }
    }

    @AfterAll
    public void exit() {exitAll(testName);}

    @Test
    @Order(1)
    public void darkModeValidation() throws IOException {
        sc.startStepsGroup("DarkMode Validation");
        darkMode();
        if(darkColorValidation("//*[@text='Dark Mode']").equalsIgnoreCase("white")){
            fail();
        }
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void lightModeValidation() throws IOException{
        sc.startStepsGroup("LightMode Validation");
        lightMode();
        Assertions.assertEquals("white",darkColorValidation("//*[@text='Dark Mode']"));
        sc.stopStepsGroup();
    }

    public static void darkMode() {
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='top_nav_profile_icon']")){
            reLaunchApp_android();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 30);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@content-desc='Dark Mode']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='account_dark_mode_text']",0);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='account_dark_mode_switch_off']")){
            click("NATIVE","xpath=//*[@id='account_dark_mode_switch_off']",0,1);
        }
        verifyElementFound("NATIVE","xpath=//*[@id='account_dark_mode_switch_on']",0);
    }

    public static void lightMode() throws IOException {
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='top_nav_profile_icon']")){
            reLaunchApp_android();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 30);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        verifyElementFound("NATIVE","xpath=//*[@content-desc='Dark Mode']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='account_dark_mode_text']",0);
        if(darkColorValidation("//*[@text='Dark Mode']").equalsIgnoreCase("white")){
            createLog("Light Mode is ON");
        }
        else {
            click("NATIVE","xpath=//*[@id='account_dark_mode_switch_on' or @id='account_dark_mode_switch_off']",0,1);
        }
        verifyElementFound("NATIVE","xpath=//*[@id='account_dark_mode_switch_off']",0);
    }

    /*
White RGB Values
Account Settings R:255 G:255 B:255
24MM Remote R:253 G:253 B:253

Black RGB Values
Account Settings R:23 G:24 B:26
24MM Remote R:44 G:44 B:44
 */
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

        int middleX = leftX + width / 5;
        int middleY = leftY + height / 5;
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
        //RGB value for black color (0, 0, 0)
        // RGB value for white color (255, 255, 255)

        String maximumValueColor = "";
        if (red>=240 && green >= 240 && blue >= 240) {
            maximumValueColor = "white";
            createLog("Color validated White");
        } else if (red <= 50 && green <= 50 && blue <= 50) {
            maximumValueColor = "black";
            createLog("Color validated Black");
        }
        return maximumValueColor;
    }

}
