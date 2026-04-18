package v2update.subaru.android.canada.French.accountSettings;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruLinkedAccountsCAFrenchAndroid extends SeeTestKeywords {
    String testName = "SubaruLinkedAccountsCAFrench - Android";

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
                android_emailLoginFrench("subarunextgen3@gmail.com", "Test$12345");
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
    public void exit(){
        exitAll(this.testName);
    }

    @Test
    @Order(1)
    public void amazonMusicTest() {
        sc.startStepsGroup("Test - Amazon Music");
        amazonMusic();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        reLaunchApp_android();
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void amazonMusic() {
        createLog("Verifying Amazon Music");

        if (!sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Comptes associés']"))) {
            reLaunchApp_android();
            navigateToLinkedAccountsScreen();
        }
        //Amazon Music
        verifyElementFound("NATIVE", "xpath=//*[@text='Amazon Music']", 0);
        //Amazon music icon
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Amazon Music' and @class='android.widget.ImageView']", 0);
        //check Amazon Music is linked or not
        if (sc.isElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Amazon Music']/following-sibling::*[contains(@text,'Associé')]"))) {
            createLog("Amazon music is linked");
            sc.report("Amazon music is linked",true);
            if (sc.isElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Amazon Music']/following-sibling::*[contains(@text,'défaut')]"))) {
                createLog("Amazon music is defaulted");
                sc.report("Amazon music is defaulted", true);
            } else {
                createLog("Amazon music is not defaulted");
            }
        } else {
            createLog("Amazon music is not linked");
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Amazon Music']/following-sibling::*[contains(@text,'Associer ')]"), 0);
        }

        //Amazon music link status
        boolean isAmazonMusicLinked = sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Amazon Music']/following-sibling::*[contains(@text,'Associé')]"));
        createLog("is Amazon music linked: "+isAmazonMusicLinked);

        if(isAmazonMusicLinked) {
            createLog("Amazon music is linked");
            //pending - need to add elements once linked
        } else if (sc.isElementFound("NATIVE","xpath=//*[@text='Enter the characters you see below']")) {
            createLog("Amazon music is not linked");
            verifyElementFound("NATIVE", "xpath=//*[@text='Type the characters you see in this image:']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
        }
        else {
            createLog("Amazon music is not linked");
            click("NATIVE", convertTextToUTF8("//*[contains(text(),'Associer')]"), 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='agree button']", 0)) {
                createLog("Linking amazon music");
                click("NATIVE", "xpath=//*[@text='agree button']", 0, 1);
                sc.syncElements(5000, 10000);
                verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Comptes associés']"), 0);
            }
            else {
                verifyElementFound("NATIVE", "xpath=//*[@id='ap_email_login']", 0);
                sendText("NATIVE", "xpath=//*[@id='ap_email_login']", 0, "ev_21mm@mail.tmnact.io");
                click("NATIVE", "xpath=//*[@text='Continuer']", 0, 1);
                sendText("NATIVE", "xpath=//*[@id='ap_password']", 0, "Test@123");
                sc.syncElements(2000, 4000);
                click("NATIVE", "xpath=//*[@id='auth-signin-button']", 0, 1);
                sc.syncElements(10000, 15000);
            }

            isAmazonMusicLinked = sc.isElementFound("NATIVE", "xpath=//*[@text='Amazon Music']/following-sibling::*[contains(@text,'Associé')]");
            createLog("is Amazon music is linked: "+isAmazonMusicLinked);
            Assertions.assertTrue(isAmazonMusicLinked);
            sc.syncElements(5000, 10000);
        }
        createLog("Verified Amazon Music");



        //navigate to dashboard screen
        verifyElementFound("NATIVE","xpath=//*[@text='Comptes associés']",0);
        click("NATIVE", convertTextToUTF8("//*[@contentDescription='Revenir en arrière']"), 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        //click back
        click("NATIVE", "xpath=(//*[@class='android.widget.ImageButton'])[1]", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@contentDescription='Drag']", 0, "Down");
        sc.syncElements(3000, 12000);
        verifyElementFound("NATIVE","xpath=//*[@id='dashboard_display_image']",0);
        createLog("Completed : Amazon Music");
    }
    public static void navigateToLinkedAccountsScreen() {
        createLog("Started : Navigation to Linked Accounts Screen");
        verifyElementFound("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(5000, 30000);
        click("NATIVE", "xpath=//*[@text='Compte']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Compte']", 0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Comptes associés']"),0);
        verifyElementFound("NATIVE","xpath=//*[@id='iv_linked_accounts']",0);
        click("NATIVE",convertTextToUTF8("//*[@text='Comptes associés']"),0,1);
        sc.syncElements(5000, 60000);
        //Linked Accounts screen
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Comptes associés']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Services musicaux']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[contains(text(),'Profitez de la diffusion en continu de votre musique dans')]"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Bloquer le contenu explicite']"),0);
        verifyElementFound("NATIVE","xpath=//*[@id='music_explicit_preference_switch']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Comptes associés']",0);
        createLog("Completed : Navigation to Linked Accounts Screen");
    }
}
