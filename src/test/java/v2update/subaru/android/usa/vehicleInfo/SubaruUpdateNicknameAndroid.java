package v2update.subaru.android.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruUpdateNicknameAndroid extends SeeTestKeywords {
    String testName = "UpdateNickName-Android";

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
        createLog("Completed: 17cy email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

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
        String strUpdateNickName = "Subaru Solterra";
        String strNickNamePresent= "";
        createLog("Start:Verifying Update Nickname in vehicle info screen");
        if (!sc.isElementFound("NATIVE","xpath=//*[@text='Info']")){
            reLaunchApp_android();
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        click("NATIVE", "xpath=//*[@text='Info']", 0, 1);
        sc.syncElements(5000, 30000);

        //verify nickname section
        verifyElementFound("NATIVE", "xpath=(//*[@content-desc='Edit']/preceding::*[@class='android.widget.TextView'])[2]", 0);
        strNickNamePresent=sc.elementGetProperty("NATIVE","xpath=(//*[@content-desc='Edit']/preceding::*[@class='android.widget.TextView'])[2]",0,"text");
        createLog("NickName present on screen:"+strNickNamePresent);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='Edit']", 0);
        click("NATIVE", "xpath=//*[@content-desc='Edit']", 0, 1);
        sc.syncElements(2000, 8000);

        //verify update nickname screen
        createLog("Verifying Update Nickname screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Update Nickname']", 0);
        //cancel icon
        verifyElementFound("NATIVE", "xpath=//*[@text='Update Nickname']/preceding-sibling::*[@class='android.view.View']", 0);

        //NiceName
        verifyElementFound("NATIVE", "xpath=//*[@resource-id='vehicle_nickname_edit_text_field']", 0);

        // Save button should be disabled when Nickname field is blank
        /*clearTextFieldData("//*[@resource-id='vehicle_nickname_edit_text_field']");
        sc.syncElements(2000, 8000);
        //String isSaveButtonEnabledStr = sc.elementGetProperty("NAIVE","xpath=//*[@text='Save']/parent::*[@class='android.view.View']",0,"enabled");
        String isSaveButtonEnabledStr = getElementAttributeValue("//*[@text='Save']/parent::*[@class='android.view.View']","enabled");
        createLog("Save button enabled/disabled check after clearing nickname data: "+isSaveButtonEnabledStr);
        boolean isSaveButtonEnabled = Boolean.parseBoolean(isSaveButtonEnabledStr);
        if(isSaveButtonEnabled) {
            sc.report("When nickname field data is blank, Save button is enabled",false);
            createErrorLog("When nickname field data is blank, Save button is enabled");
        } else {
            sc.report("When nickname field data is blank, Save button is disabled",true);
            createLog("When nickname field data is blank, Save button is disabled");
        }

        createLog("Vehicle Name displayed: "+getElementAttributeValue("(//*[@text='Update Nickname']/following-sibling::*[@class='android.widget.TextView'])[1]","text"));
        sendText("NATIVE", "xpath=//*[@class='android.widget.EditText']", 0, strUpdateNickName);
        //check save button is enabled
        isSaveButtonEnabledStr = getElementAttributeValue("//*[@text='Save']/parent::*[@class='android.view.View']","enabled");
        createLog("Save button enabled/disabled check after entering nickname data: "+isSaveButtonEnabledStr);
        isSaveButtonEnabled = Boolean.parseBoolean(isSaveButtonEnabledStr);
        if(isSaveButtonEnabled) {
            sc.report("When nickname is present, Save button is enabled",true);
            createLog("When nickname is present, Save button is enabled");
        } else {
            sc.report("When nickname is present, Save button is disabled",false);
            createErrorLog("When nickname is present, Save button is disabled");
        }*/
        sendText("NATIVE", "xpath=//*[@class='android.widget.EditText']", 0, strUpdateNickName);
        click("NATIVE", "xpath=//*[@text='Save']/following-sibling::*[@class='android.widget.Button']", 0, 1);
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
        verifyElementFound("NATIVE", "xpath=//*[@text='Update Nickname']", 0);
        //verify nickname is updated - "NX 350 Vehicle"
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strUpdateNickName+"']", 0);
        sendText("NATIVE", "xpath=//*[@class='android.widget.EditText']", 0, strNickNamePresent);
        click("NATIVE", "xpath=//*[@text='Save']/following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(2000, 8000);
        //navigate back to vehicle info landing screen
        //verify nickname is updated - "vin number"
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strNickNamePresent+"']", 0);
        createLog("Reverted old nickname");
        //Vehicle Info screen - Back button
        click("NATIVE", "xpath=//*[@id='vehicle_info_back_box_cta']", 0, 1);
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
        createLog("Navigated back to vehicle info screen");
        createLog("End:Verify Update Nickname in vehicle info screen");
    }
}
