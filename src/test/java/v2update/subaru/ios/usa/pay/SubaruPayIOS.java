package v2update.subaru.ios.usa.pay;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.ctp.framework.utilities.Utils;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruPayIOS extends SeeTestKeywords {
    String testName = " - SubaruPay-IOS";
    boolean isStageRun = Boolean.valueOf(ConfigSingleton.configMap.get("stageRun"));

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
        ios_emailLogin("subarunextgen3@gmail.com","Test$123456");
        createLog("Completed: Subaru email Login");
        sc.stopStepsGroup();
    }

    @AfterAll
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void walletEVTest(){
        sc.startStepsGroup("Test - wallet EV");
        if(isStageRun) {
            createLog("Stage run - add or remove card");
            isStageApp = true;
            sc.install(ConfigSingleton.configMap.get("cloudAppStageSubaruIOS"), false, false);
            sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaruStage"), false, true);
            sc.syncElements(5000,10000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            ios_handlepopups();
            checkIsLoginScreenDisplayed_IOS();
            environmentSelection_iOS("stage");
            selectionOfCountry_IOS("USA");
            selectionOfLanguage_IOS("English");
            ios_keepMeSignedIn(true);
            ios_emailLogin("subarustage_21mm@mail.tmnact.io", "Toyo213$$");
        } else {
            createLog("Production run - skipping add or remove card");
        }
        walletEV();
        sc.stopStepsGroup();
    }

    public static void walletEV() {
        createLog("Verifying wallet in pay bottom bar");
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='BottomTabBar_payTab']")) {
            reLaunchApp_iOS();
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='BottomTabBar_payTab']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Pay']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='IconPay']", 0);
        click("NATIVE", "xpath=//*[@id='BottomTabBar_payTab']", 0, 1);
        sc.syncElements(3000, 30000);
        //verify pay icon is highlighted
        verifyElementFound("NATIVE", "xpath=//*[@text='IconPayHighlight']", 0);

        verifyElementFound("NATIVE", "xpath=//*[@text='Wallet']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Default Card' or contains(@text,'Manage payment\n methods for your vehicle')]", 0);

        click("NATIVE", "xpath=//*[@text='Wallet']", 0, 1);
        sc.syncElements(10000, 20000);

        if(sc.isElementFound("NATIVE","xpath=//*[@text='Default']")) {
            createLog("Existing cards available");
            verifyElementFound("NATIVE", "xpath=//*[@text='Default']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Amex') or contains(@text,'Visa')]", 0);
            //existing card details
            click("NATIVE", "xpath=//*[contains(@text,'••••')]", 0, 1);
            sc.syncElements(5000, 20000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Default']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Amex') or contains(@text,'Visa')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'••••')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Exp')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@class='UIAScrollView']/following-sibling::*[@class='UIAImage']", 0);
            //delete card
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAImage'])[1]", 0);
            click("NATIVE", "xpath=(//*[@class='UIAImage'])[1]", 0, 1);
            sc.syncElements(5000, 20000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Remove Card']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Are you sure you want to remove this card?']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Cancel']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Yes, Remove']", 0);
            click("NATIVE", "xpath=//*[@text='Cancel']", 0, 1);
            sc.syncElements(5000, 20000);
            click("NATIVE", "xpath=//*[@text='back_button_action\\nback_button']", 0, 1);
            sc.syncElements(10000, 20000);

            //Add card button
            verifyElementFound("NATIVE", "xpath=//*[@text='Wallet']/following-sibling::*[@class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[@text='Wallet']/following-sibling::*[@class='UIAButton']", 0, 1);
            createLog("Verifying ADD CARD screen");
            strAppType = strAppType.substring(0,1).toUpperCase() + strAppType.substring(1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Add Card']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@label='Card Info']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@label='Billing Address']", 0);
            addCreditCardEV();
            // Done button
            if(sc.isElementFound("NATIVE","xpath=//*[@label='Done']"))
                click("NATIVE","xpath=//*[@label='Done']",0,1);

            //click back button to navigate to Wallet screen
            sc.swipe("up", sc.p2cy(70), 2000);
            verifyElementFound("NATIVE", "xpath=//*[contains(@label,'back_button_action')]", 0);
            click("NATIVE","xpath=//*[contains(@label,'back_button_action')]",0,1);
            createLog("verified ADD CARD screen");
            sc.syncElements(10000, 30000);

            verifyElementFound("NATIVE", "xpath=(//*[@class='UIAScrollView']/following::*[@class='UIAImage'])[1]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Charging Stations')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='Subscriptions\n" + "Manage Payments']", 0);

            //verify Charging Stations & screen
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Charging Stations')]", 0);
            click("NATIVE","xpath=//*[contains(@id,'Charging Stations')]",0,1);
            createLog("Verifying STATIONS screen");
            sc.syncElements(10000, 60000);
            verifyElementFound("NATIVE", "xpath=//*[@id='Stations']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Default']", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Amex') or contains(@text,'Visa')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[contains(@text,'••••')]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@label='Transactions']", 0);
            if(sc.isElementFound("NATIVE","xpath=//*[@text='No Transactions Found']")) {
                createLog("Transactions are not displayed");
            } else {
                createLog("Transactions are displayed");
                verifyElementFound("NATIVE", "xpath=(//*[@label='Transactions']/following::*[@class='UIAImage'])[1][contains(@label,'$')]", 0);
            }

            //click back button to navigate to Wallet screen
            click("NATIVE","xpath=//*[contains(@label,'back_button_action')]",0,1);
            createLog("verified STATIONS screen");
            sc.syncElements(10000, 30000);
        } else if(sc.isElementFound("NATIVE","xpath=//*[@text='Wallet']/following-sibling::*[@class='UIAButton']")){
            createLog("Set up your payment methods is displayed");
            //Add card button
            verifyElementFound("NATIVE", "xpath=//*[@text='Wallet']/following-sibling::*[@class='UIAButton']", 0);
            click("NATIVE", "xpath=//*[@text='Wallet']/following-sibling::*[@class='UIAButton']", 0, 1);
            createLog("Verifying ADD CARD screen");
            strAppType = strAppType.substring(0,1).toUpperCase() + strAppType.substring(1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", "xpath=//*[@label='Add Card']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@label='Card Info']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@label='Billing Address']", 0);
            addCreditCardEV();
            // Done button
            if(sc.isElementFound("NATIVE","xpath=//*[@label='Done']"))
                click("NATIVE","xpath=//*[@label='Done']",0,1);

            sc.swipe("up",sc.p2cy(70),3000);

            //click back button to navigate to Wallet screen
            if(sc.isElementFound("NATIVE","xpath=//*[contains(@label,'back_button_action') and @visible='true']"))
                click("NATIVE","xpath=//*[contains(@label,'back_button_action') and @visible='true']",0,1);
            createLog("verified ADD CARD screen");
            sc.syncElements(10000, 30000);
        }

        //verify Payment & screen
        createLog("Verifying Subscriptions and Payment screen");
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Payment Method']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Select a Vehicle']", 0);
        click("NATIVE","xpath=//*[@text='subscription_chevronRight']",0,1);
        sc.syncElements(3000, 30000);
        if(sc.isElementFound("NATIVE", "xpath=//*[@text='To add a payment method, continue to Manage Subscriptions to add a connected services product.']")) {
            createLog("Payment method is not added - To add a payment method, continue to Manage Subscriptions to add a connected services product.");
            verifyElementFound("NATIVE", "xpath=//*[@text='Manage Subscriptions']", 0);
            if(isStageApp) {
                createLog("Stage app - add subscription");
            } else {
                createLog("Production app - skipping subscription");
                click("NATIVE","xpath=//*[@text='Cancel']",0,1);
                //click back button to navigate to Wallet screen
                click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Wallet']", 0);
                click("NATIVE","xpath=//*[@accessibilityLabel='BottomTabBar_dashboardTab']",0,1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle image, double tap to open vehicle info.']", 0);
            }
        } else if(sc.isElementFound("NATIVE", "xpath=//*[@text='Subscriptions Payments']")) {
            createLog("subscription payments is displayed - add/remove credit card");
            if(isStageApp) {
                createLog("Stage app - add or remove card");
                addCreditCard();
                removeCreditCard();

                click("NATIVE","xpath=//*[@text='Back']",0,1);
                sc.syncElements(3000, 30000);
                click("NATIVE","xpath=//*[@text='Back']",0,1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Wallet']", 0);
                click("NATIVE","xpath=//*[@accessibilityLabel='BottomTabBar_dashboardTab']",0,1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle image, double tap to open vehicle info.']", 0);
            } else {
                createLog("Production app - skipping remove card");
                click("NATIVE","xpath=//*[@text='Cancel']",0,1);
                //click back button to navigate to Wallet screen
                click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Wallet']", 0);
                click("NATIVE","xpath=//*[@accessibilityLabel='BottomTabBar_dashboardTab']",0,1);
                sc.syncElements(3000, 30000);
                verifyElementFound("NATIVE", "xpath=//*[@text='Vehicle image, double tap to open vehicle info.']", 0);
            }
        }
        createLog("Verified wallet in pay bottom bar");
    }

    public static void addCreditCardEV() {
        createLog("Adding credit card");
        verifyElementFound("NATIVE", "xpath=//*[@id='Credit Card Number']", 0);
        sendText("NATIVE", "xpath=//*[@id='Credit Card Number']", 0, "5555555555554444");
        verifyElementFound("NATIVE", "xpath=//*[@id='First Name']", 0);
        sendText("NATIVE", "xpath=//*[@id='First Name']", 0, Utils.getRandomName());
        verifyElementFound("NATIVE", "xpath=//*[@id='Last Name']", 0);
        sendText("NATIVE", "xpath=//*[@id='Last Name']", 0, Utils.getRandomName());
        sendText("NATIVE", "xpath=//*[contains(@id,'Exp')]", 0, "022029");
        sendText("NATIVE", "xpath=//*[@id='CVV Code']", 0, "222");
        sc.syncElements(1000, 3000);
        sendText("NATIVE", "xpath=//*[@id='Billing Address' and @class='UIATextField']", 0, "Test");
        click("NATIVE", "xpath=//*[@text='done']", 0, 1);
        //sendText("NATIVE", "xpath=//*[contains(@id,'City')]", 0, "Test");
        click("NATIVE", "xpath=//*[contains(@id,'City')]", 0, 1);
        delay(1000);
        sc.sendText("Test");
        delay(1000);
        click("NATIVE", "xpath=//*[@text='done']", 0, 1);

        //sendText("NATIVE", "xpath=//*[contains(@id,'State')]", 0, "TX");
        click("NATIVE", "xpath=//*[contains(@id,'State')]", 0, 1);
        delay(1000);
        sc.sendText("TX");
        delay(1000);
        click("NATIVE", "xpath=//*[@text='done']", 0, 1);

        delay(1000);
        click("NATIVE", "xpath=//*[@id='ZIP Code']", 0, 1);
        sc.sendText("75067");
        delay(1000);
        click("NATIVE", "xpath=//*[contains(@id,'Country')]", 0, 1);
        sc.sendText("US");
        click("NATIVE", "xpath=//*[@text='done']", 0, 1);
        click("NATIVE", "xpath=//*[@id='Save']", 0, 1);
        sc.syncElements(5000, 30000);
        if (isStageApp) {
            if (!sc.isElementFound("NATIVE", "xpath=//*[@text='************4444']")) {
                createErrorLog("Add payment method test failed");
                fail();
            } else {
                createLog("Add payment method successfully");
            }
        }
    }

    public static void addCreditCard() {
        createLog("Adding credit card");

        int beforeCardCount = sc.getElementCount("NATIVE","xpath=//*[contains(@text,'************')]");
        createLog("Before adding new card - count is : "+beforeCardCount);

        verifyElementFound("NATIVE", "xpath=//*[@text='Add a New Credit Card']", 0);
        click("NATIVE", "xpath=//*[@text='Add a New Credit Card']", 0, 1);
        sc.syncElements(20000, 40000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Payment Information' or @text='PAYMENT INFORMATION']", 0);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Pay securely using your credit card' ) or contains(@text,'PAY SECURELY USING YOUR CREDIT CARD')]", 0);

        verifyElementFound("NATIVE", "xpath=//*[(@id='Name on Card' or @id='NAME ON CARD') and @class='UIAStaticText']", 0);
        sendText("NATIVE","xpath=(//*[(@id='Name on Card' or @id='NAME ON CARD') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,strRandomName);
        verifyElementFound("NATIVE", "xpath=//*[(@id='Card Number' or @id='CARD NUMBER') and @class='UIAStaticText']", 0);
        sendText("NATIVE","xpath=(//*[(@id='Card Number' or @id='CARD NUMBER') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,"5555555555554444");
        click("NATIVE","xpath=//*[@text='Exp. Date Month']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@id='02']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@text='Year']",0,1);
        sc.syncElements(1000, 3000);
        click("NATIVE","xpath=//*[@id='2029']",0,1);
        sc.syncElements(1000, 3000);
        sendText("NATIVE","xpath=(//*[@id='CVV']/following::*[@class='UIATextField'])[1]",0,"222");
        sc.swipe("Down", sc.p2cy(20), 5000);
        sc.syncElements(1000, 3000);
        sendText("NATIVE","xpath=(//*[(@id='Billing Address' or @id='BILLING ADDRESS') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,"Test");
        sc.syncElements(1000, 3000);
        sendText("NATIVE","xpath=(//*[(@id='City' or @id='CITY') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,"Test");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Country' and @class='UIAStaticText']", 0);
        click("NATIVE","xpath=(//*[@id='Country' and @class='UIAView']/following-sibling::*[@class='UIAView'])[1]",0,1);
        sc.syncElements(2000, 10000);
        click("NATIVE","xpath=//*[@id='United States']",0,1);
        sc.syncElements(2000, 10000);
        //state
        if(sc.isElementFound("NATIVE","xpath=//*[@text='- Select One -']")) {
            createLog("select state from drop down list");
            click("NATIVE","xpath=//*[@text='- Select One -']",0,1);
            sc.syncElements(1000, 3000);
            click("NATIVE","xpath=//*[@id='Alaska']",0,1);
        } else {
            createLog("Enter state");
            sendText("NATIVE","xpath=(//*[@id='State']/following::*[@class='UIATextField'])[1]",0,"Texas");
        }
        sc.syncElements(2000, 10000);
        sendText("NATIVE","xpath=(//*[(@id='Zip Code' or @id='ZIP CODE') and @class='UIAStaticText']/following::*[@class='UIATextField'])[1]",0,"75094");
        sc.syncElements(2000,4000);
        if(sc.isElementFound("NATIVE","xpath=(//*[@id='payment'])[1]/following::*[@id='DEFAULT PAYMENT METHOD' or @id='Default Payment Method']")) {
            //displays both check box DEFAULT PAYMENT METHOD and consent
            //click on DEFAULT PAYMENT METHOD check box
            click("NATIVE","xpath=((//*[@id='payment'])[1]/following::*[@class='UIAView'])[1]",0,1);
            sc.syncElements(2000,4000);
            //click on consent check box
            click("NATIVE","xpath=(//*[@id='DEFAULT PAYMENT METHOD' or @id='Default Payment Method']/following::*[@class='UIAView'])[1]",0,1);
            sc.syncElements(2000,4000);
        } else {
            //displays only consent check box
            click("NATIVE","xpath=((//*[@id='payment'])[1]/following::*[@class='UIAView'])[1]",0,1);
            sc.syncElements(2000,4000);
        }
        click("NATIVE","xpath=//*[(@id='Continue to Review' or @id='CONTINUE TO REVIEW') and @enabled='true']",0,1);
        sc.syncElements(15000, 60000);

        int afterCardCount = sc.getElementCount("NATIVE","xpath=//*[contains(@text,'************')]");
        createLog("After adding new card - count is : "+afterCardCount);

        if(isStageApp) {
            if(afterCardCount > beforeCardCount) {
                createLog("Added payment method successfully");
            } else {
                createErrorLog("Adding payment method test failed");
                fail();
            }
        }
    }

    public static void removeCreditCard() {
        createLog("Started: Deleting card");
        sc.syncElements(2000, 4000);

        int beforeCardCount = sc.getElementCount("NATIVE","xpath=//*[contains(@text,'************')]");
        createLog("Before deleting card - count is : "+beforeCardCount);

        click("NATIVE","xpath=(//*[contains(@text,'************')])[1]",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='Edit']",0,1);
        sc.syncElements(2000, 4000);
        click("NATIVE","xpath=//*[@id='Remove'] ",0,1);
        sc.syncElements(15000, 60000);

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
