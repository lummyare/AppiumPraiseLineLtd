package v2update.subaru.android.usa.pay;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruPayAndroid extends SeeTestKeywords {
    String testName = "Pay-Android-21MM";

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
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void wallet21mm() {
        sc.startStepsGroup("Pay-Wallet-21mm");
        wallet();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void signOutTest21MM() {
        sc.startStepsGroup("Test - Sign out - 21MM");
        android_SignOut();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void stgSignIn() {
        if(isStageApp){
            sc.startStepsGroup("21mm login");
            selectionOfCountry_Android("USA");
            selectionOfLanguage_Android("English");
            android_keepMeSignedIn(true);
            android_emailLoginIn(ConfigSingleton.configMap.get("paymentFlowTestEmail"), ConfigSingleton.configMap.get("paymentFlowTestPassword"));
            sc.stopStepsGroup();
        }
    }

    @Test
    @Order(5)
    public void addPayment(){
        if (isStageApp){
            sc.startStepsGroup("Add new card");
            addNewCard();
            sc.stopStepsGroup();
        }
    }
    @Test
    @Order(6)
    public void editPayment(){
        if (isStageApp){
            sc.startStepsGroup("edit payment");
            editPaymentMethod();
            sc.stopStepsGroup();
        }
    }
    @Test
    @Order(7)
    public void removePayment(){
        if (isStageApp){
            sc.startStepsGroup("remove payment");
            removePaymentMethod();
            sc.stopStepsGroup();
        }

    }

    public static void wallet(){
        createLog("Started: wallet validation");
        if(!(sc.isElementFound("NATIVE","xpath=//*[@content-desc='wallet_icon']"))){
            click("NATIVE", "xpath=//*[@content-desc='Pay']", 0, 1);
            sc.syncElements(5000, 30000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='wallet_icon']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Wallet']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@content-desc='wallet subscription card one']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Manage payment methods for your vehicle’s subscriptions.']", 0);
        click("NATIVE", "xpath=//*[@text='Manage payment methods for your vehicle’s subscriptions.']", 0, 1);
        sc.syncElements(5000, 10000);

        if(sc.isElementFound("NATIVE","xpath=//*[@content-desc='Wallet']//following-sibling::*[contains(@class,'ImageView')]",0)) {
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Wallet']//following-sibling::*[contains(@class,'ImageView')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@content-desc='Wallet']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Subscriptions')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@content-desc,'back_button')]", 0);
            click("NATIVE", "xpath=//*[contains(@content-desc,'Subscriptions')]", 0, 1);
            sc.syncElements(5000, 10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@text='PAYMENT METHODS' or @text='Payment Methods']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='title' or @text='Select a Vehicle']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='subtitle' and contains(@text,'Please select a vehicle')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='subtitle' and contains(@text,'Payment method applies')]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@id='chevron'])[1]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='image' or @content-desc='Vehicle']", 0);
        click("NATIVE", "xpath=(//*[@id='chevron'])[1]", 0, 1);
        sc.syncElements(5000, 10000);

        if(sc.isElementFound("NATIVE","xpath=//*[@text='Manage Subscription']")) {
            createLog("Manage Subscriptions is displayed");
            click("NATIVE", "xpath=//*[@text='Manage Subscription']", 0, 1);
            sc.syncElements(10000, 60000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Manage Subscription' or @text='MANAGE SUBSCRIPTION']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='PAYMENT METHODS' or @text='Payment Methods']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Wallet']", 0);
            click("NATIVE", "xpath=//*[@content-desc='Home Tab']", 0, 1);
            sc.syncElements(5000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_display_image']", 0);
        } else {
            createLog("Manage Subscriptions is not displayed - add new card");
            verifyElementFound("NATIVE", "xpath=//*[@text='Credit Card']", 0);
            addNewCard();
            removePaymentMethod();
        }
        createLog("Completed: wallet validation");
    }

    public static void addNewCard(){
        createLog("Started: Adding new card");
        if (sc.isElementFound("NATIVE","")) {
            reLaunchApp_android();
            click("NATIVE", "xpath=//*[@content-desc='Pay']", 0, 1);
            sc.syncElements(5000, 30000);
            click("NATIVE","xpath=//*[@id='regular_wallet']",0,1);
            sc.syncElements(2000,4000);
            if (sc.isElementFound("NATIVE","xpath=//*[@id='chevron']",0)) {
                click("NATIVE","xpath=//*[@id='chevron'][1]",0,1);
                sc.syncElements(2000,4000);
            }
        }
        int beforeCardCount = sc.getElementCount("NATIVE","xpath=//*[contains(@text,'************')]");
        createLog("Before adding new card - count is : "+beforeCardCount);

        click("NATIVE","xpath=//*[@text='Add a New Credit Card']",0,1);
        sc.syncElements(2000, 4000);
        sendText("NATIVE","xpath=(//*[@id='input-creditCardHolderName' or @class='android.widget.EditText'])[1]",0,strRandomName);
        sendText("NATIVE","xpath=(//*[@id='input-creditCardNumber' or @class='android.widget.EditText'])[2]",0,"5555555555554444");
        click("NATIVE","xpath=//*[@id='input-creditCardExpirationMonth' or @text='Expiration Month']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@text='02']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@id='input-creditCardExpirationYear' or @text='Expiration Year']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@text='2029']",0,1);
        sc.syncElements(1000, 3000);
        sc.syncElements(1000, 3000);
        sendText("NATIVE","xpath=(//*[@id='input-cardSecurityCode' or @class='android.widget.EditText'])[3]",0,"222");
        sendText("NATIVE","xpath=(//*[@id='input-creditCardAddress1' or @class='android.widget.EditText'])[4]",0,"Test");
        sendText("NATIVE","xpath=(//*[@id='input-creditCardCity' or @class='android.widget.EditText'])[5]",0,"Test");
        sc.swipe("DOWN",sc.p2cy(35),5000);
        sc.swipe("DOWN",sc.p2cy(35),5000);
        click("NATIVE","xpath=(//*[@text='State'])[2]",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@text='Alaska']",0,1);
        sc.swipe("DOWN",sc.p2cy(35),5000);
        sc.syncElements(1000, 3000);
        sendText("NATIVE","xpath=//*[@text='Zip Code']/following::*[@class='android.widget.EditText']",0,"75094");
        click("NATIVE","xpath=(//*[@class='android.widget.Image'])[2]",0,1);
        click("NATIVE","xpath=(//*[@class='android.widget.Image'])[3]",0,1);
        click("NATIVE","xpath=//*[@text='Continue to Review' and @enabled='true']",0,1);
        sc.syncElements(10000, 60000);

        int afterCardCount = sc.getElementCount("NATIVE","xpath=//*[contains(@text,'************')]");
        createLog("After adding new card - count is : "+afterCardCount);

        if(afterCardCount > beforeCardCount) {
            createLog("Added payment method successfully");
        } else {
            createErrorLog("Adding payment method test failed");
            fail();
        }
    }
    public void editPaymentMethod(){
        sc.syncElements(3000, 4000);
        click("NATIVE","xpath=//*[@text='************4444']",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='edit']",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='button1']",0,1);
        sc.syncElements(2000, 4000);
        sendText("NATIVE","xpath=//*[@id='cvv']",0,"222");
        sc.syncElements(2000, 4000);
        sendText("NATIVE","xpath=//*[@id='name']",0,strRandomName);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='save']",0,1);
        sc.syncElements(2000, 4000);

        if (!sc.isElementFound("NATIVE", "xpath=//*[@text='************4444']")) {
            createErrorLog("Editing payment method test failed");
            fail();
        }else{
            createLog("Edited payment method successfully");
        }
    }

    public static void removePaymentMethod() {
        createLog("Started: Deleting card");
        sc.syncElements(2000, 4000);

        int beforeCardCount = sc.getElementCount("NATIVE","xpath=//*[contains(@text,'************')]");
        createLog("Before deleting card - count is : "+beforeCardCount);

        click("NATIVE","xpath=(//*[contains(@text,'************')])[1]",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='edit']",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@text='Remove'] ",0,1);
        sc.syncElements(10000, 60000);

        int afterCardCount = sc.getElementCount("NATIVE","xpath=//*[contains(@text,'************')]");
        createLog("After deleting card - count is : "+afterCardCount);

        if(afterCardCount < beforeCardCount) {
            createLog("successfully deleted payment method");
        } else {
            createErrorLog("Deleting payment method test failed");
            fail();
        }
    }
}
