package v2update.subaru.ios.canada.french.find;

import com.ctp.SeeTestKeywords;
import com.ctp.framework.core.ConfigSingleton;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ReportPortalExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubaruDestinationsCAFrenchIOS extends SeeTestKeywords {
    String testName = " - SubaruDestinationsCAFrench-IOS";

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
    public void exit() {exitAll(this.testName);}

    @Test
    @Order(1)
    public void destinationsScreenTest(){
        sc.startStepsGroup("Test - Destinations Screen");
        validateDestinations();
        sc.stopStepsGroup();
    }

    @Test
    @Order(2)
    public void destinationsSearchTest(){
        sc.startStepsGroup("Test - Destinations Search");
        validateSearch();
        sc.stopStepsGroup();
    }

    @Test
    @Order(3)
    public void destinationFavoritesTest(){
        sc.startStepsGroup("Test - Favorites");
        validateFavorites();
        sc.stopStepsGroup();
    }

    @Test
    @Order(4)
    public void destinationSentToCarTest(){
        sc.startStepsGroup("Test - Sent To Car");
        validateSentToCar();
        sc.stopStepsGroup();
    }

    @Test
    @Order(5)
    public void destinationHomeTest(){
        sc.startStepsGroup("Test - Destination Home");
        validateHome();
        sc.stopStepsGroup();
    }

    @Test
    @Order(6)
    public void destinationWorkTest(){
        sc.startStepsGroup("Test - Destination Work");
        validateWork();
        sc.stopStepsGroup();
    }

    @Test
    @Order(7)
    public void destinationRecentSectionTest(){
        sc.startStepsGroup("Test - Recent Destination Section");
        validateRecentSection();
        sc.stopStepsGroup();
    }

    @Test
    @Order(8)
    public void signOut() {
        sc.startStepsGroup("Sign out");
        ios_emailSignOut();
        sc.stopStepsGroup();
    }

    public static void validateDestinations(){
        createLog("Started Validating Destinations Screen");
        if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Vehicle image')]")) {
            reLaunchApp_iOS();
        }
        sc.syncElements(5000, 30000);
        click("NATIVE", "xpath=//*[@id='IconFind' or @id='IconFindHighlight']", 0, 1);
        sc.syncElements(5000, 30000);

        sc.swipe("Down", sc.p2cy(30), 500);
        verifyElementFound("NATIVE","xpath=//*[@label='Destinations']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Favoris, récents et envoyés à la voiture']"),0);
        verifyElementFound("NATIVE","xpath=//*[@label='destinations']",0);
        click("NATIVE","xpath=//*[@label='Destinations']",0,1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE","xpath=//*[@id='TOOLBAR_LABEL_TITLE']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);

        verifyElementFound("NATIVE","xpath=//*[@text='Domicile']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Travail']",0);
        verifyElementFound("NATIVE","xpath=//*[@text='Favoris']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Envoyée à la voiture']"),0);

        createLog("Completed Validating Destinations Screen");
    }

    public static void validateSearch() {
        createLog("Started Validating Search Destinations");

        if(!sc.isElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Domicile']/following-sibling::*[@text='Définir']"))) {
            //Options icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Domicile']/following-sibling::*[@class='UIAButton']", 0);
            click("NATIVE","xpath=//*[@text='Domicile']/following-sibling::*[@class='UIAButton']",0,1);

            //Options screen
            verifyElementFound("NATIVE", "xpath=(//*[@text='Domicile'])[1]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer domicile']", 0);

            //Remove Home
            click("NATIVE","xpath=//*[@text='Supprimer domicile']",0,1);
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Domicile']/following-sibling::*[@text='Définir']"), 0);
        }
        if(!sc.isElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Travail']/following-sibling::*[@text='Définir']"))) {
            //Options icon
            verifyElementFound("NATIVE", "xpath=//*[@text='Travail']/following-sibling::*[@class='UIAButton']", 0);
            click("NATIVE","xpath=//*[@text='Travail']/following-sibling::*[@class='UIAButton']",0,1);

            //Options screen
            verifyElementFound("NATIVE", "xpath=(//*[@text='Travail'])[1]", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer travail']", 0);

            //Remove Work
            click("NATIVE","xpath=//*[@text='Supprimer travail']",0,1);
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Travail']/following-sibling::*[@text='Définir']"), 0);
            createLog("Verified Work options screen");
        }

        verifyElementFound("NATIVE", "xpath=//*[@value='Rechercher des destinations']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='MY_DESTIATIONS_SEARCH_BAR']//following-sibling::*[@class='UIAImage']", 0);

        click("NATIVE","xpath=//*[@value='Rechercher des destinations']",0,1);
        sc.sendText("1st main minneapolis");
        sc.syncElements(2000, 10000);
        //editor done button
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Done']", 0))
            click("NATIVE", "xpath=//*[@id='Done']", 0, 1);

        verifyElementFound("NATIVE", "xpath=//*[@text='Rechercher des destinations']", 0);

        int searchResultCount = sc.getElementCount("NATIVE", "xpath=//*[@id='MY_DESTIATIONS_TABLE']//following-sibling::*[@class='UIAView' and @XCElementType='XCUIElementTypeCell']");
        createLog("Search Result Count is: "+searchResultCount);

        if(searchResultCount > 0) {
            createLog("Search Success");
            String firstSearchResult = sc.elementGetText("NATIVE","xpath=(//*[@id='MY_DESTIATIONS_TABLE']//following-sibling::*[@class='UIAView' and @XCElementType='XCUIElementTypeCell'])[1]//following-sibling::*[@class='UIAStaticText']",0);
            createLog("Search result 1st details is: "+firstSearchResult);
            if(firstSearchResult.contains("Main"))
                createLog("Search result details validation passed");
            else
                createErrorLog("Search result details validation failed");
        } else {
            createErrorLog("Search Failed - displays search result count as "+searchResultCount+"");
        }

        //click 1st search result
        click("NATIVE", "xpath=(//*[@id='MY_DESTIATIONS_TABLE']//following-sibling::*[@class='UIAView' and @XCElementType='XCUIElementTypeCell'])[1]", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);

        //destination address details
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_NAME_LABEL']", 0);
        createLog("Destination Name Details: "+sc.elementGetText("NATIVE","xpath=//*[@id='POI_DETAILS_VIEW_NAME_LABEL']",0));
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_ADDRESS_LABEL']", 0);
        createLog("Destination Address Details: "+sc.elementGetText("NATIVE","xpath=//*[@id='POI_DETAILS_VIEW_ADDRESS_LABEL']",0));
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_DISTANCE_LABEL']", 0);
        createLog("Destination Distance Details : "+sc.elementGetText("NATIVE","xpath=//*[@id='POI_DETAILS_VIEW_DISTANCE_LABEL']",0));

        //Save or remove
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SAVE_REMOVE_BUTTON']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Sauvegarder']",0)) {
            createLog("Destination Address is not saved");
        } else {
            createLog("Destination Address is already saved");
            verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
            click("NATIVE", "xpath=//*[@id='Supprimer']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@id='Sauvegarder']", 0);
        }
        click("NATIVE", "xpath=//*[@id='Sauvegarder']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder la destination']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder comme domicile']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder comme travail']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder comme favoris']", 0);

        click("NATIVE", "xpath=//*[@text='Sauvegarder comme favoris']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);

        //Send To Car
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SENT_TO_CAR_BUTTON']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='Envoyer à la voiture']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@id='Envoyer à la voiture']"), 0, 1);

        if(sc.waitForElement("NATIVE",convertTextToUTF8("//*[@text='La destination sera disponible dans les véhicules compatibles avec Drive Connect.']"),0,60000)){
            createLog("Send To Car is success");
        } else {
            createErrorLog("Send To Car failed");
        }
        delay(5000);
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);

        createLog("Completed Validating Search Destinations");
    }

    public static void validateFavorites() {
        createLog("Started Validating Favorites");
        int beforeFavoriteCount = 0;
        int afterFavoriteCount = 0;
        String addressLine = "1584 Bloor St";

        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0)) {
            goToDestinations();
        }
        verifyElementFound("NATIVE","xpath=//*[@text='Favoris']",0);
        click("NATIVE","xpath=//*[@text='Favoris']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Favoris')]", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Modifier']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Retour']", 0);

        //Before favorite count in FAVORITES screen is
        beforeFavoriteCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
        createLog("Before favorite - count is : "+beforeFavoriteCount);

        if(sc.isElementFound("NATIVE","xpath=//*[@text='noFavorites']",0)) {
            createLog("Favorites are not saved");
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Ajoutez vos destinations favorites pour y accéder ici et dans votre véhicule.']"), 0);
        } else {
            createLog("Favorites already saved");

            //verify destination details
            //1st favorite
            String favoriteDetails = sc.elementGetText("NATIVE","xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[1]",0);
            createLog("First Favorite details is: "+favoriteDetails);
            //Click and navigate to Destination Details
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]", 0);
            click("NATIVE","xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]",0,1);
            verifyDestinationDetails();

            //click edit
            click("NATIVE","xpath=//*[@text='Modifier']",0,1);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Terminé']"), 0);

            //Before remove count is
            int beforeRemoveCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
            createLog("Before removing favorite - count is : "+beforeRemoveCount);

            //Remove Favorite
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]", 0);
            click("NATIVE","xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]",0,1);

            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@text='Supprimer la destination sauvegardée'])[1]"), 0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Êtes-vous sûr de vouloir supprimer cette entrée de vos destinations sauvegardées?']"), 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Annuler']", 0);
            click("NATIVE","xpath=//*[@text='Annuler']",0,1);

            verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]", 0);
            click("NATIVE","xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]",0,1);
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@text='Supprimer la destination sauvegardée'])[1]"), 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer']", 0);
            click("NATIVE","xpath=//*[@text='Supprimer']",0,1);

            //After remove count is
            int afterRemoveCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
            createLog("After removing favorite - count is : "+afterRemoveCount);

            if(afterRemoveCount < beforeRemoveCount) {
                createLog("Removed favorite successfully");
            } else {
                createErrorLog("Remove Favorite failed");
                fail();
            }

            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Terminé']"), 0);
            click("NATIVE",convertTextToUTF8("//*[@text='Terminé']"),0,1);

            verifyElementFound("NATIVE", "xpath=//*[@text='Modifier']", 0);

            //After favorite count in FAVORITES screen is
            afterFavoriteCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
            createLog("After favorite - count is : "+afterFavoriteCount);

        }

        //remove favorite if already saved / save again and validate - 6565 Headquarters Dr
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Favoris')]", 0);
        if(sc.isElementFound("NATIVE","xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]")) {
            createLog("Destination already favorite - remove favorite");
            click("NATIVE","xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]",0,1);
            sc.syncElements(3000, 30000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
            click("NATIVE", "xpath=//*[@id='Supprimer']", 0, 1);
            click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
            sc.syncElements(5000, 30000);
        }
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        sc.syncElements(5000, 30000);

        verifyElementFound("NATIVE", "xpath=//*[@value='Rechercher des destinations']", 0);
        click("NATIVE","xpath=//*[@value='Rechercher des destinations']",0,1);
        sc.sendText(addressLine);
        sc.syncElements(2000, 10000);
        //editor done button
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Done']", 0))
            click("NATIVE", "xpath=//*[@id='Done']", 0, 1);

        verifyElementFound("NATIVE", "xpath=//*[@text='Rechercher des destinations']", 0);

        int searchResultCount = sc.getElementCount("NATIVE", "xpath=//*[@id='MY_DESTIATIONS_TABLE']//following-sibling::*[@class='UIAView' and @XCElementType='XCUIElementTypeCell']");
        createLog("Search Result Count is: "+searchResultCount);

        //click address search result
        verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0);
        click("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0, 1);
        sc.syncElements(5000, 60000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        //Save or remove
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SAVE_REMOVE_BUTTON']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Sauvegarder']",0)) {
            createLog("Destination Address is not saved");
        } else {
            createLog("Destination Address is already saved");
            verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
            click("NATIVE", "xpath=//*[@id='Supprimer']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@id='Sauvegarder']", 0);
        }
        click("NATIVE", "xpath=//*[@id='Sauvegarder']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder la destination']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder comme domicile']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder comme travail']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder comme favoris']", 0);

        click("NATIVE", "xpath=//*[@text='Sauvegarder comme favoris']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);

        //Navigate back to My Destinations
        click("NATIVE", "xpath=//*[@text='Retour']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);

        //search saved favorite in FAVORITES screen
        verifyElementFound("NATIVE","xpath=//*[@text='Favoris']",0);
        click("NATIVE","xpath=//*[@text='Favoris']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Favoris')]", 0);
        verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0);

        //Navigate back to My Destinations
        click("NATIVE", "xpath=//*[@text='Retour']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);

        createLog("Completed Validating Favorites");
    }

    public static void validateSentToCar() {
        createLog("Started Validating Sent To Car");
        int beforeSentCount = 0;
        int afterSentCount = 0;
        String sentToCarAddressLine = "4313 Granville St";

        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0)) {
            goToDestinations();
        }
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Envoyée à la voiture']"),0);
        click("NATIVE",convertTextToUTF8("//*[@text='Envoyée à la voiture']"),0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Envoyée à la voiture')]"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='MY_SENT_TO_CAR_EDIT_FAVORITE_BUTTON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Retour']", 0);

        //Before favorite count in FAVORITES screen is
        beforeSentCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
        createLog("Before Sent To Car - count is : "+beforeSentCount);

        if(sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Aucune destination envoyée à la voiture']"),0)) {
            createLog("No destinations sent to car");
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Vous pouvez rechercher des destinations et les envoyer pour y accéder dans')]"), 0);
        } else {
            createLog("Sent To Car already contains destinations");

            //verify destination details
            //1st Sent To Car
            String sentToCarDetails = sc.elementGetText("NATIVE","xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[1]",0);
            createLog("First Sent To Car details is: "+sentToCarDetails);
            //Click and navigate to Destination Details
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]", 0);
            click("NATIVE","xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]",0,1);
            verifyDestinationDetails();

            //click edit
            click("NATIVE","xpath=//*[@id='MY_SENT_TO_CAR_EDIT_FAVORITE_BUTTON']",0,1);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Terminé']"), 0);

            //Before remove destination count is
            int beforeRemoveCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
            createLog("Before removing destination - count is : "+beforeRemoveCount);

            //Remove destination
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]", 0);
            click("NATIVE","xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]",0,1);

            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@text='Supprimer cette destination de celles envoyées à la voiture'])[1]"), 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Annuler']", 0);
            click("NATIVE","xpath=//*[@text='Annuler']",0,1);

            verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]", 0);
            click("NATIVE","xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])[1]",0,1);
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@text='Supprimer cette destination de celles envoyées à la voiture'])[1]"), 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer']", 0);
            click("NATIVE","xpath=//*[@text='Supprimer']",0,1);
            delay(3000);

            //After remove count is
            int afterRemoveCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
            createLog("After removing destination - count is : "+afterRemoveCount);

            if(afterRemoveCount < beforeRemoveCount) {
                createLog("Removed destination successfully - Sent To Car");
            } else {
                createErrorLog("Remove destination failed - Sent To Car");
                fail();
            }

            verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='Terminé']"), 0);
            click("NATIVE",convertTextToUTF8("//*[@id='Terminé']"),0,1);
            delay(2000);
            verifyElementFound("NATIVE", "xpath=//*[@id='MY_SENT_TO_CAR_EDIT_FAVORITE_BUTTON']", 0);

            //After count in SENT TO CAR screen is
            afterSentCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
            createLog("After Sent To Car - count is : "+afterSentCount);
        }

        if(sc.isElementFound("NATIVE","xpath=//*[@class='UIATable']//following-sibling::*[contains(@text,'"+sentToCarAddressLine+"')]")){
            //remove from address list first
            click("NATIVE","xpath=//*[@text='Modifier']",0,1);
            delay(2000);
            click("NATIVE","xpath=//*[@class='UIATable']//following-sibling::*[contains(@text,'"+sentToCarAddressLine+"')]/preceding-sibling::*[@class='UIAButton']",0,1);
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=(//*[@text='Supprimer cette destination de celles envoyées à la voiture'])[1]"), 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer']", 0);
            click("NATIVE","xpath=//*[@text='Supprimer']",0,1);
            delay(3000);
        }

        //Verify sent to car is getting updated on sending for destinations
        int beforeCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
        createLog("Before Sent To Car - count is : "+beforeCount);

        //Go back and search and sent to car to destination
        click("NATIVE","xpath=//*[@text='Retour']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);
        verifyElementFound("NATIVE", "xpath=//*[@value='Rechercher des destinations']", 0);
        click("NATIVE","xpath=//*[@value='Rechercher des destinations']",0,1);
        sc.sendText(sentToCarAddressLine);
        sc.syncElements(2000, 10000);
        //editor done button
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Done']", 0))
            click("NATIVE", "xpath=//*[@id='Done']", 0, 1);

        verifyElementFound("NATIVE", "xpath=//*[@text='Rechercher des destinations']", 0);

        int searchResultCount = sc.getElementCount("NATIVE", "xpath=//*[@id='MY_DESTIATIONS_TABLE']//following-sibling::*[@class='UIAView' and @XCElementType='XCUIElementTypeCell']");
        createLog("Search Result Count is: "+searchResultCount);

        //click address search result
        verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+sentToCarAddressLine+"')]", 0);
        click("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+sentToCarAddressLine+"')]", 0, 1);
        sc.syncElements(5000, 60000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        //Send To Car
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SENT_TO_CAR_BUTTON']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='Envoyer à la voiture']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@id='Envoyer à la voiture']"), 0, 1);

        if(sc.waitForElement("NATIVE",convertTextToUTF8("//*[@text='La destination sera disponible dans les véhicules compatibles avec Drive Connect.']"),0,60000)){
            createLog("Send To Car is success");
        } else {
            createErrorLog("Send To Car failed");
        }
        delay(5000);
        //click to navigate to Favorites/Sent To Car screen
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);
        verifyElementFound("NATIVE",convertTextToUTF8("//*[@text='Envoyée à la voiture']"),0);
        click("NATIVE",convertTextToUTF8("//*[@text='Envoyée à la voiture']"),0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Envoyée à la voiture')]"), 0);

        int afterCount = sc.getElementCount("NATIVE", "xpath=(//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton'])");
        createLog("After Sent To Car - count is : "+afterCount);
        if(afterCount > beforeCount) {
            createLog("Sent to car success");
        } else {
            createErrorLog("Sent to car failed");
            fail();
        }

        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);

        createLog("Completed Validating Sent To Car");
    }

    public static void validateHome() {
        createLog("Started Validating Home");
        String addressLine = "Toronto Island Park";
        String updateHomeAddress = "Ottawa University";

        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0)) {
            goToDestinations();
        }
        verifyElementFound("NATIVE","xpath=//*[@text='Domicile']",0);
        if(sc.isElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Domicile']/following-sibling::*[@text='Définir']"))) {
            createLog("Home destination address is not set yet");

            click("NATIVE","xpath=//*[@text='Domicile']",0,1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@label,'Définir l') and contains(@label,'adresse de domicile')]"), 0);
            verifyElementFound("NATIVE", "xpath=//*[@placeholder='Rechercher votre adresse de domicile']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Retour']", 0);

            //Search and set home address
            click("NATIVE","xpath=//*[@placeholder='Rechercher votre adresse de domicile']",0,1);
            sc.sendText(addressLine);
            sc.syncElements(2000, 10000);
            //editor done button
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Done']", 0))
                click("NATIVE", "xpath=//*[@id='Done']", 0, 1);

            int searchResultCount = sc.getElementCount("NATIVE", "xpath=//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton']");
            createLog("Search Result Count is: "+searchResultCount);

            //click address search result
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0);
            click("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0, 1);
            sc.syncElements(5000, 60000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
            //Save
            verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SAVE_REMOVE_BUTTON']", 0);
            //might be favorite or work destination
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0)) {
                click("NATIVE", "xpath=//*[@id='Supprimer']", 0, 1);
                sc.syncElements(2000, 10000);
            }
            verifyElementFound("NATIVE", "xpath=//*[@id='Sauvegarder']", 0);
            click("NATIVE", "xpath=//*[@id='Sauvegarder']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Domicile' and @id='POI_DETAILS_VIEW_NAME_LABEL']", 0);

            click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@label,'Définir l') and contains(@label,'adresse de domicile')]"), 0);
            click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);
            sc.verifyElementNotFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Domicile']/following-sibling::*[@text='Définir']"), 0);
        } else {
            createLog("Home destination address is already set");
        }
        click("NATIVE","xpath=//*[@text='Domicile']",0,1);
        sc.syncElements(5000, 30000);
        verifyDestinationDetails();
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);

        //click home options in Destination Details screen
        //click options
        createLog("Verify Home options screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Domicile']/following-sibling::*[@class='UIAButton']", 0);
        click("NATIVE","xpath=//*[@text='Domicile']/following-sibling::*[@class='UIAButton']",0,1);

        //Options screen
        verifyElementFound("NATIVE", "xpath=(//*[@text='Domicile'])[1]", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les détails']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Mettre à jour domicile']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer domicile']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='close']", 0);

        //close
        click("NATIVE","xpath=//*[@text='close']",0,1);
        sc.verifyElementNotFound("NATIVE", convertTextToUTF8("//*[@text='Mettre à jour domicile']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);

        //view details
        click("NATIVE","xpath=//*[@text='Domicile']/following-sibling::*[@class='UIAButton']",0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les détails']"), 0);
        click("NATIVE",convertTextToUTF8("//*[@text='Voir les détails']"),0,1);
        verifyDestinationDetails();
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);

        //Update home - 4356 Watson Cir
        click("NATIVE","xpath=//*[@text='Domicile']/following-sibling::*[@class='UIAButton']",0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Mettre à jour domicile']"), 0);
        click("NATIVE",convertTextToUTF8("//*[@text='Mettre à jour domicile']"),0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@label,'Définir l') and contains(@label,'adresse de domicile')]"), 0);
        //Search and set work address
        click("NATIVE","xpath=//*[@placeholder='Rechercher votre adresse de domicile']",0,1);
        sc.sendText(updateHomeAddress);
        sc.syncElements(2000, 10000);
        //editor done button
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Done']", 0))
            click("NATIVE", "xpath=//*[@id='Done']", 0, 1);

        int searchResultCount = sc.getElementCount("NATIVE", "xpath=//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton']");
        createLog("Search Result Count is: "+searchResultCount);

        //click address search result
        verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+updateHomeAddress+"')]", 0);
        click("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+updateHomeAddress+"')]", 0, 1);
        sc.syncElements(5000, 60000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        //Save
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SAVE_REMOVE_BUTTON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Sauvegarder' or @id='Supprimer']", 0);
        click("NATIVE", "xpath=//*[@id='Sauvegarder' or @id='Supprimer']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Sauvegarder' or @id='Supprimer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_NAME_LABEL']", 0);
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@label,'Définir l') and contains(@label,'adresse de domicile')]"), 0);
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);
        //verify address updated
        //view details
        click("NATIVE","xpath=//*[@text='Domicile']/following-sibling::*[@class='UIAButton']",0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les détails']"), 0);
        click("NATIVE",convertTextToUTF8("//*[@text='Voir les détails']"),0,1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_NAME_LABEL' and contains(@text,'Domicile')]", 0);
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);

        //Remove Home
        click("NATIVE","xpath=//*[@text='Domicile']/following-sibling::*[@class='UIAButton']",0,1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer domicile']", 0);
        click("NATIVE","xpath=//*[@text='Supprimer domicile']",0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Domicile']/following-sibling::*[@text='Définir']"), 0);
        createLog("Verified Home options screen");

        //Save as home by searching destination
        createLog("Started - Save as home by searching destination");
        verifyElementFound("NATIVE", "xpath=//*[@value='Rechercher des destinations']", 0);
        click("NATIVE","xpath=//*[@value='Rechercher des destinations']",0,1);
        sc.sendText(addressLine);
        sc.syncElements(2000, 10000);
        //editor done button
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Done']", 0))
            click("NATIVE", "xpath=//*[@id='Done']", 0, 1);

        verifyElementFound("NATIVE", "xpath=//*[@text='Rechercher des destinations']", 0);

        searchResultCount = sc.getElementCount("NATIVE", "xpath=//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton']");
        createLog("Search Result Count is: "+searchResultCount);

        //click address search result
        verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0);
        click("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0, 1);
        sc.syncElements(5000, 60000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        //Save or remove
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SAVE_REMOVE_BUTTON']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Sauvegarder']",0)) {
            createLog("Destination Address is not saved");
        } else {
            createLog("Destination Address is already saved");
            verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
            click("NATIVE", "xpath=//*[@id='Supprimer']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@id='Sauvegarder']", 0);
        }
        click("NATIVE", "xpath=//*[@id='Sauvegarder']", 0, 1);
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Sauvegarder la destination']")) {
            verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder la destination']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder comme domicile']", 0);
            click("NATIVE", "xpath=//*[@text='Sauvegarder comme domicile']",0, 1);
            sc.syncElements(2000, 10000);
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Domicile' and @id='POI_DETAILS_VIEW_NAME_LABEL']", 0);

        //Navigate back to My Destinations
        click("NATIVE", "xpath=//*[@text='Retour']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);

        //search Home Destination in Home screen
        verifyElementFound("NATIVE","xpath=//*[@text='Domicile']",0);
        sc.verifyElementNotFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Domicile']/following-sibling::*[@text='Définir']"), 0);

        click("NATIVE","xpath=//*[@text='Domicile']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Domicile' and @id='POI_DETAILS_VIEW_NAME_LABEL']", 0);

        //Navigate back to My Destinations
        click("NATIVE", "xpath=//*[@text='Retour']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);
        createLog("Started - Save as home by searching destination");

        createLog("Completed Validating Home");
    }

    public static void validateWork() {
        createLog("Started Validating Work");
        String addressLine = "Saskatoon Bay";
        String updateWorkAddress = "888 Dupont St";

        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0)) {
            goToDestinations();
        }
        verifyElementFound("NATIVE","xpath=//*[@text='Travail']",0);
        if(sc.isElementFound("NATIVE",convertTextToUTF8("xpath=//*[@text='Travail']/following-sibling::*[@text='Définir']"))) {
            createLog("Work destination address is not set yet");

            click("NATIVE","xpath=//*[@text='Travail']",0,1);
            sc.syncElements(5000, 30000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Définir l') and contains(@text,'adresse de travail')]"), 0);
            verifyElementFound("NATIVE", "xpath=//*[@placeholder='Rechercher votre adresse de travail']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Retour']", 0);

            //Search and set work address
            click("NATIVE","xpath=//*[@placeholder='Rechercher votre adresse de travail']",0,1);
            sc.sendText(addressLine);
            sc.syncElements(2000, 10000);
            //editor done button
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Done']", 0))
                click("NATIVE", "xpath=//*[@id='Done']", 0, 1);

            int searchResultCount = sc.getElementCount("NATIVE", "xpath=//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton']");
            createLog("Search Result Count is: "+searchResultCount);

            //click address search result
            verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0);
            click("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0, 1);
            sc.syncElements(5000, 60000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
            //Save
            verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SAVE_REMOVE_BUTTON']", 0);
            //might be favorite or home destinations
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0)) {
                click("NATIVE", "xpath=//*[@id='Supprimer']", 0, 1);
                sc.syncElements(2000, 10000);
            }
            verifyElementFound("NATIVE", "xpath=//*[@id='Sauvegarder']", 0);
            click("NATIVE", "xpath=//*[@id='Sauvegarder']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@text='Travail' and @id='POI_DETAILS_VIEW_NAME_LABEL']", 0);

            click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Définir l') and contains(@text,'adresse de travail')]"), 0);
            click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
            sc.syncElements(2000, 10000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);
            sc.verifyElementNotFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Travail']/following-sibling::*[@text='Définir']"), 0);
        } else {
            createLog("Work destination address is already set");
        }
        click("NATIVE","xpath=//*[@text='Travail']",0,1);
        sc.syncElements(5000, 30000);
        verifyDestinationDetails();
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);

        //click work options in Destination Details screen
        //click options
        createLog("Verify Work options screen");
        verifyElementFound("NATIVE", "xpath=//*[@text='Travail']/following-sibling::*[@class='UIAButton']", 0);
        click("NATIVE","xpath=//*[@text='Travail']/following-sibling::*[@class='UIAButton']",0,1);

        //Options screen
        verifyElementFound("NATIVE", "xpath=(//*[@text='Travail'])[1]", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les détails']"), 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Mettre à jour travail']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer travail']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='close']", 0);

        //close
        click("NATIVE","xpath=//*[@text='close']",0,1);
        sc.verifyElementNotFound("NATIVE", convertTextToUTF8("//*[@text='Mettre à jour travail']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);

        //view details
        click("NATIVE","xpath=//*[@text='Travail']/following-sibling::*[@class='UIAButton']",0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les détails']"), 0);
        click("NATIVE",convertTextToUTF8("//*[@text='Voir les détails']"),0,1);
        verifyDestinationDetails();
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);

        //Update work - 7777 E Zayante Rd
        click("NATIVE","xpath=//*[@text='Travail']/following-sibling::*[@class='UIAButton']",0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Mettre à jour travail']"), 0);
        click("NATIVE",convertTextToUTF8("//*[@text='Mettre à jour travail']"),0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Définir l') and contains(@text,'adresse de travail')]"), 0);
        //Search and set work address
        click("NATIVE","xpath=//*[@placeholder='Rechercher votre adresse de travail']",0,1);
        sc.sendText(updateWorkAddress);
        sc.syncElements(2000, 10000);
        //editor done button
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Done']", 0))
            click("NATIVE", "xpath=//*[@id='Done']", 0, 1);

        int searchResultCount = sc.getElementCount("NATIVE", "xpath=//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton']");
        createLog("Search Result Count is: "+searchResultCount);

        //click address search result
        verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+updateWorkAddress+"')][1]", 0);
        click("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+updateWorkAddress+"')][1]", 0, 1);
        sc.syncElements(5000, 60000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        //Save
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SAVE_REMOVE_BUTTON']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Sauvegarder']", 0);
        click("NATIVE", "xpath=//*[@id='Sauvegarder']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Travail' and @id='POI_DETAILS_VIEW_NAME_LABEL']", 0);
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@text,'Définir l') and contains(@text,'adresse de travail')]"), 0);
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);
        //verify address updated
        //view details
        click("NATIVE","xpath=//*[@text='Travail']/following-sibling::*[@class='UIAButton']",0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les détails']"), 0);
        click("NATIVE",convertTextToUTF8("//*[@text='Voir les détails']"),0,1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_ADDRESS_LABEL' and contains(@text,'Dupont St')]", 0);
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);

        //Remove Work
        click("NATIVE","xpath=//*[@text='Travail']/following-sibling::*[@class='UIAButton']",0,1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Supprimer travail']", 0);
        click("NATIVE","xpath=//*[@text='Supprimer travail']",0,1);
        verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Travail']/following-sibling::*[@text='Définir']"), 0);
        createLog("Verified Work options screen");

        //Save as work by searching destination
        createLog("Started - Save as home by searching destination");
        verifyElementFound("NATIVE", "xpath=//*[@value='Rechercher des destinations']", 0);
        click("NATIVE","xpath=//*[@value='Rechercher des destinations']",0,1);
        sc.sendText(addressLine);
        sc.syncElements(2000, 10000);
        //editor done button
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Done']", 0))
            click("NATIVE", "xpath=//*[@id='Done']", 0, 1);

        verifyElementFound("NATIVE", "xpath=//*[@text='Rechercher des destinations']", 0);

        searchResultCount = sc.getElementCount("NATIVE", "xpath=//*[@class='UIATable']//following-sibling::*[@XCElementType='XCUIElementTypeButton']");
        createLog("Search Result Count is: "+searchResultCount);

        //click address search result
        verifyElementFound("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0);
        click("NATIVE", "xpath=(//*[@class='UIATable']//following::*[1][@class='UIAStaticText'])[contains(@text,'"+addressLine+"')]", 0, 1);
        sc.syncElements(5000, 60000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        //Save or remove
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SAVE_REMOVE_BUTTON']", 0);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Sauvegarder']",0)) {
            createLog("Destination Address is not saved");
        } else {
            createLog("Destination Address is already saved");
            verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
            click("NATIVE", "xpath=//*[@id='Supprimer']", 0, 1);
            verifyElementFound("NATIVE", "xpath=//*[@id='Sauvegarder']", 0);
        }
        click("NATIVE", "xpath=//*[@id='Sauvegarder']", 0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder la destination']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Sauvegarder comme travail']", 0);

        click("NATIVE", "xpath=//*[@text='Sauvegarder comme travail']",0, 1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Travail' and @id='POI_DETAILS_VIEW_NAME_LABEL']", 0);

        //Navigate back to My Destinations
        click("NATIVE", "xpath=//*[@text='Retour']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);

        //search Work Destination in Work screen
        verifyElementFound("NATIVE","xpath=//*[@text='Travail']",0);
        sc.verifyElementNotFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Travail']/following-sibling::*[@text='Définir']"), 0);

        click("NATIVE","xpath=//*[@text='Travail']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='Supprimer']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@text='Travail' and @id='POI_DETAILS_VIEW_NAME_LABEL']", 0);

        //Navigate back to My Destinations
        click("NATIVE", "xpath=//*[@text='Retour']",0,1);
        sc.syncElements(3000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);
        createLog("Started - Save as work by searching destination");

        createLog("Completed Validating work");
    }

    public static void validateRecentSection() {
        createLog("Started Validating Recent Section");

        if(!sc.isElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0)) {
            goToDestinations();
        }

        verifyElementFound("NATIVE",convertTextToUTF8("xpath=(//*[@text='Récentes' and @class='UIAStaticText'])[1]"),0);

        int recentDestinationCount = sc.getElementCount("NATIVE", convertTextToUTF8("xpath=(//*[@text='Récentes' and @class='UIAStaticText'])[1]/following-sibling::*[@class='UIAView']"));
        createLog("Recent Destination Count is: "+recentDestinationCount);
        if(recentDestinationCount > 0) {
            createLog("Destinations are displayed in the Recent section");
            String firstRecentDetails = sc.elementGetText("NATIVE",convertTextToUTF8("xpath=(//*[@text='Récentes' and @class='UIAStaticText'])[1]/following-sibling::*[@class='UIAView'][1]//*[@class='UIAStaticText'][2]"),0);
            createLog("First Recent details is: "+firstRecentDetails);

            //click first recent details
            click("NATIVE",convertTextToUTF8("xpath=(//*[@text='Récentes' and @class='UIAStaticText'])[1]/following-sibling::*[@class='UIAView'][1]//*[@class='UIAStaticText'][2]"),0,1);
            sc.syncElements(5000, 30000);
            verifyDestinationDetails();
            verifyElementFound("NATIVE", "xpath=//*[@text='Mes destinations']", 0);
        } else {
            createLog("Destinations are not displayed in the Recent section");
        }
        createLog("Completed Validating Recent Section");

        //click back to navigate to find screen
        createLog("Navigating to dashboard screen");
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='IconFindHighlight']", 0);
        click("NATIVE","xpath=//*[@id='BottomTabBar_dashboardTab']",0,1);
        sc.syncElements(5000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='Vehicle image, double tap to open vehicle info.']", 0);
        createLog("Navigated to dashboard screen");
    }

    //Supporting methods

    public static void goToDestinations() {
        createLog("Started navigating to Destinations");
        reLaunchApp_iOS();
        click("NATIVE", "xpath=//*[@id='IconFind']", 0, 1);
        sc.syncElements(10000, 60000);
        sc.swipe("Down", sc.p2cy(30), 2000);
        verifyElementFound("NATIVE","xpath=//*[@label='Destinations']",0);
        click("NATIVE","xpath=//*[@label='Destinations']",0,1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE","xpath=//*[@text='Mes destinations']",0);
        createLog("Completed navigating to Destinations");
    }

    public static void verifyDestinationDetails() {
        createLog("Verifying destination details");

        verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Détails de la destination']"), 0);

        //destination address details
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_NAME_LABEL']", 0);
        createLog("Destination Name Details: "+sc.elementGetText("NATIVE","xpath=//*[@id='POI_DETAILS_VIEW_NAME_LABEL']",0));
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_ADDRESS_LABEL']", 0);
        createLog("Destination Address Details: "+sc.elementGetText("NATIVE","xpath=//*[@id='POI_DETAILS_VIEW_ADDRESS_LABEL']",0));
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_DISTANCE_LABEL']", 0);
        createLog("Destination Distance Details : "+sc.elementGetText("NATIVE","xpath=//*[@id='POI_DETAILS_VIEW_DISTANCE_LABEL']",0));

        //Save or remove
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SAVE_REMOVE_BUTTON']", 0);

        //Send To Car
        verifyElementFound("NATIVE", "xpath=//*[@id='POI_DETAILS_VIEW_SENT_TO_CAR_BUTTON']", 0);
        verifyElementFound("NATIVE", convertTextToUTF8("//*[@id='Envoyer à la voiture']"), 0);
        click("NATIVE", convertTextToUTF8("//*[@id='Envoyer à la voiture']"), 0, 1);

        if(sc.waitForElement("NATIVE",convertTextToUTF8("//*[@text='La destination sera disponible dans les véhicules compatibles avec Drive Connect.']"),0,60000)){
            createLog("Send To Car is success");
        } else {
            createErrorLog("Send To Car failed");
        }
        delay(5000);
        //click to navigate to Favorites/Sent To Car screen
        click("NATIVE", "xpath=//*[@text='Retour']", 0, 1);
        sc.syncElements(5000, 30000);

        createLog("Verified destination details");
    }
}
