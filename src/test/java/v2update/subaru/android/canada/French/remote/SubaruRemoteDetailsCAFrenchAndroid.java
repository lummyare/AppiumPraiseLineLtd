package v2update.subaru.android.canada.French.remote;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruRemoteDetailsCAFrenchAndroid extends SeeTestKeywords {

    String testName = "RemoteDetails-PR-Spanish-Android";

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
    public void validateRemoteCommands(){
        sc.startStepsGroup("Remote Details For Orange Vehicle-2442-PR-Spanish");
        remoteCommandsVerificationOnDashboard();
        sc.stopStepsGroup();

    }
    @Test
    @Order(2)
    public void SignOut21mm(){
        sc.startStepsGroup("Sign out 21mm-PR-Spanish");
        android_SignOut();
        sc.stopStepsGroup();
    }


    public static void remoteCommandsVerificationOnDashboard(){
        if ((sc.isElementFound("NATIVE","xpath=//*[@text='Info']"))){
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE","xpath=//*[@text='Info']",0);
        //Remote
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Verrouiller']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@content-desc='Verrouiller']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@id='remote_door_lock_button']"),0);
        //Start and Stop
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Démarrer']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@contentDescription='Démarrer']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@id='remote_engine_start_button']"),0);
        //Unlock
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Déverrouiller']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@content-desc='Déverrouiller']"),0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@id='remote_door_unlock_button']"),0);

        verifyElementFound("NATIVE",convertTextToUTF8("//*[@id='dashboard_remote_open_iconbutton']"),0);

    }
}
