package v2update.subaru.android.canada.French.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruUpdateNickNameCAFrenchAndroid extends SeeTestKeywords {
    String testName = "Subaru UpdateNickName-Android";
    static String strVin = "JF1SG36307H748305";

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
    public void updateNickNameTest(){
        sc.startStepsGroup("Test - Update Nickname");
        updateVehicleNickName();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        android_SignOut();
        sc.stopStepsGroup();
    }

    public static void updateVehicleNickName() {
        String strUpdateNickName = "NX 350 Vehicle";
        createLog("Verifying Update Nickname in vehicle info screen");

        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        click("NATIVE", "xpath=//*[@text='Info']", 0, 1);
        sc.syncElements(5000, 30000);

        //verify nickname section
        //verifyElementFound("NATIVE", "xpath=//*[@content-desc='Edit']/preceding::*[@class='android.widget.TextView'][@text='"+strVin+"']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Edit']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Edit']", 0, 1);
        sc.syncElements(2000, 8000);

        //verify update nickname screen
        createLog("Verifying Update Nickname screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Modifier le surnom']", 0);
        //cancel icon
        verifyElementFound("NATIVE", "xpath=//*[@text='Modifier le surnom']/preceding-sibling::*[@class='android.view.View']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strVin+"']", 0);

        // Save button should be disabled when Nickname field is blank -
        clearTextFieldData("//*[@class='android.widget.EditText']");
        sc.syncElements(2000, 8000);
        String isSaveButtonEnabledStr = getElementAttributeValue("xpath=//*[@text='Sauvegarder']/parent::*[@class='android.view.View']","enabled");
        createLog("Save button enabled/disabled check after clearing nickname data: "+isSaveButtonEnabledStr);
        boolean isSaveButtonEnabled = Boolean.parseBoolean(isSaveButtonEnabledStr);
        if(isSaveButtonEnabled) {
            sc.report("When nickname field data is blank, Save button is enabled",false);
        } else {
            sc.report("When nickname field data is blank, Save button is disabled",true);
        }

        createLog("Vehicle Name displayed: "+getElementAttributeValue("xpath=(//*[@text='Modifier le surnom']/following-sibling::*[@class='android.widget.TextView'])[1]","text"));
        sendText("NATIVE", "xpath=//*[@class='android.widget.EditText']", 0, strUpdateNickName);
        //check save button is enabled
        isSaveButtonEnabledStr = getElementAttributeValue("xpath=//*[@text='Sauvegarder']/parent::*[@class='android.view.View']","enabled");
        createLog("Save button enabled/disabled check after entering nickname data: "+isSaveButtonEnabledStr);
        isSaveButtonEnabled = Boolean.parseBoolean(isSaveButtonEnabledStr);
        if(isSaveButtonEnabled) {
            sc.report("When nickname is present, Save button is enabled",true);
        } else {
            sc.report("When nickname is present, Save button is disabled",false);
        }

        click("NATIVE", "xpath=//*[@text='Sauvegarder']/following-sibling::*[@class='android.widget.Button']", 0, 1);
        //navigates to vehicle info screen
        sc.syncElements(2000, 8000);
        //verify nickname is updated - "NX 350 Vehicle"
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strUpdateNickName+"']", 0);
        createLog("Verified Update Nickname screen");

        //Revert to old nickname
        createLog("Reverting old nickname");
        click("NATIVE", "xpath=//*[@content-desc='Edit']", 0, 1);
        sc.syncElements(2000, 8000);
        // update nickname screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Modifier le surnom']", 0);
        //verify nickname is updated - "NX 350 Vehicle"
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strUpdateNickName+"']", 0);
        sendText("NATIVE", "xpath=//*[@class='android.widget.EditText']", 0, strVin);
        click("NATIVE", "xpath=//*[@text='Sauvegarder']/following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(2000, 8000);
        //navigate back to vehicle info landing screen
        //verify nickname is updated - "vin number"
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strVin+"']", 0);

        //Vehicle Info screen - Back button
        click("NATIVE", "xpath=//*[@id='vehicle_info_back_box_cta']", 0, 1);

        createLog("Reverted old nickname");
        createLog("Verified Update Nickname in vehicle info screen");
    }
}
