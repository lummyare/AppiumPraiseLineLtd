package v2update.subaru.android.canada.French.login;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SubaruLoginAndroid extends SeeTestKeywords {
    String testName = "21mm email Login-Canada-french";
    @BeforeAll
    public void setup() throws Exception {
        android_Setup2_5(this.testName);
        sc.startStepsGroup("21mm email Login-Canada-french");
        createLog("Start: 21mm email Login-Canada-french");
        selectionOfCountry_Android("Canada");
        selectionOfLanguage_Android("french");
        android_keepMeSignedIn(true);
        android_emailLoginFrench(ConfigSingleton.configMap.get("hevemail"), ConfigSingleton.configMap.get("hevpassword"));
        android_SignOut();
        createLog("End: 21mm email Login-Canada-french");
        sc.startStepsGroup("21mm email Login-Canada-french");
    }
    @AfterAll
    public void exit() {
        exitAll(this.testName);
    }
}
