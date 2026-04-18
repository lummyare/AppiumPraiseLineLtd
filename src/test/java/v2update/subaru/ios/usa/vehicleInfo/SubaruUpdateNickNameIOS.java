package v2update.subaru.ios.usa.vehicleInfo;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruUpdateNickNameIOS extends SeeTestKeywords {
    String testName = " - SubaruUpdateNickName-IOS";

    static String strVIN = "JTMABABA8RA060836";

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
        selectionOfCountry_IOS("USA");
        selectionOfLanguage_IOS("English");
        ios_keepMeSignedIn(true);
        ios_emailLogin(ConfigSingleton.configMap.get("strEmail21mmEV"),ConfigSingleton.configMap.get("strPassword21mmEV"));
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void updateNickNameTest(){
        sc.startStepsGroup("Test - Update Nickname");
        updateVehicleNickName(strVIN);
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void signOutTest() {
        sc.startStepsGroup("Test - Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void updateVehicleNickName(String strVin) {
        String strUpdateNickName = "2024 Solterra";
        createLog("Verifying Update Nickname in vehicle info screen");

        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Info']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(5000, 30000);

        //verify nickname section
        verifyElementFound("NATIVE", "xpath=(//*[@id='VehicleInfo_edit_Btn']/preceding-sibling::*[@class='UIAStaticText'])[2][@id='"+strVin+"']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='VehicleInfo_edit_Btn']", 0);
        click("NATIVE", "xpath=//*[@id='VehicleInfo_edit_Btn']", 0, 1);
        sc.syncElements(2000, 8000);

        //verify update nickname screen
        createLog("Verifying Update Nickname screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Update Nickname']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='cancel_icon']", 0);
        String vehicleName = getElementAttributeValue("(//*[@text='Update Nickname']/following-sibling::*[@class='UIAStaticText'])[1]","text");
        createLog("Vehicle Name displayed: "+vehicleName);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strVin+"']", 0);

        // Save button should be disabled when Nickname field is blank -
        clearTextFieldData("//*[@class='UIATextField']");
        sc.syncElements(2000, 8000);
        //String isSaveButtonEnabledStr = sc.elementGetProperty("NAIVE","//*[@id='VehicleNickName_save_button']",0,"enabled");
        String isSaveButtonEnabledStr = getElementAttributeValue("//*[@id='VehicleNickName_save_button']","enabled");
        createLog("Save button enabled/disabled check after clearing nickname data: "+isSaveButtonEnabledStr);
        boolean isSaveButtonEnabled = Boolean.parseBoolean(isSaveButtonEnabledStr);
        if(isSaveButtonEnabled) {
            sc.report("When nickname field data is blank, Save button is enabled",false);
        } else {
            sc.report("When nickname field data is blank, Save button is disabled",true);
        }

        sendText("NATIVE", "xpath=//*[@id='(Enter a nickname)']", 0, strUpdateNickName);
        //check save button is enabled
        //isSaveButtonEnabledStr = sc.elementGetProperty("NAIVE","//*[@id='VehicleNickName_save_button']",0,"enabled");
        isSaveButtonEnabledStr = getElementAttributeValue("//*[@id='VehicleNickName_save_button']","enabled");
        createLog("Save button enabled/disabled check after entering nickname data: "+isSaveButtonEnabledStr);
        isSaveButtonEnabled = Boolean.parseBoolean(isSaveButtonEnabledStr);
        if(isSaveButtonEnabled) {
            sc.report("When nickname is present, Save button is enabled",true);
        } else {
            sc.report("When nickname is present, Save button is disabled",false);
        }

        click("NATIVE", "xpath=//*[@id='VehicleNickName_save_button']", 0, 1);
        sc.syncElements(2000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strUpdateNickName+"']", 0);

        //navigate back to vehicle info landing screen
        sc.syncElements(2000, 8000);
        createLog("Verified Update Nickname screen");

        //verify nickname is updated - "2024 Solterra"
        verifyElementFound("NATIVE", "xpath=(//*[@id='VehicleInfo_edit_Btn']/preceding-sibling::*[@class='UIAStaticText'])[2][@id='"+strUpdateNickName+"']", 0);
        //go to dashboard and verify nickname updated
        click("NATIVE", "xpath=//*[@accessibilityLabel='VehicleInfo_back_Btn']", 0, 1);
        sc.syncElements(2000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='DashboardHeader_vehicleNamer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strUpdateNickName+"']", 0);

        //Revert to old nickname
        createLog("Reverting old nickname");
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);
        click("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0, 1);
        sc.syncElements(5000, 30000);
        click("NATIVE", "xpath=//*[@id='VehicleInfo_edit_Btn']", 0, 1);
        sc.syncElements(2000, 8000);
        // update nickname screen
        verifyElementFound("NATIVE", "xpath=//*[@text='Update Nickname']", 0);
        clearTextFieldData("//*[@class='UIATextField']");
        sc.syncElements(2000, 8000);
        sendText("NATIVE", "xpath=//*[@id='(Enter a nickname)']", 0, strVin);
        click("NATIVE", "xpath=//*[@id='VehicleNickName_save_button']", 0, 1);
        sc.syncElements(2000, 8000);

        //verify in vehicle info landing screen
        verifyElementFound("NATIVE", "xpath=//*[@text='"+strVin+"']", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@id='VehicleInfo_edit_Btn']/preceding-sibling::*[@class='UIAStaticText'])[2][@id='"+strVin+"']", 0);
        createLog("Reverted old nickname");

        //navigate to dashboard screen
        click("NATIVE", "xpath=//*[@accessibilityLabel='VehicleInfo_back_Btn']", 0, 1);
        sc.syncElements(2000, 8000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']", 0);

        createLog("Verified Update Nickname in vehicle info screen");
    }
}
