package com.ctp;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ctp.framework.api.APIkeywordsImpl;
import com.ctp.framework.core.ConfigSingleton;
import com.ctp.framework.utilities.TokenGeneration;
import com.ctp.framework.utilities.Utils;
import com.ctp.pojoModel.api.GatewayInfoPojo;
import com.epam.reportportal.service.ReportPortal;
import com.experitest.appium.SeeTestClient;
import com.github.romankh3.image.comparison.model.Rectangle;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.restassured.http.Header;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.fail;

public class SeeTestKeywords {

    public SeeTestKeywords() {
        ConfigSingleton.INSTANCE.loadConfigProperties();
    }

    // This class will be used to create custom keywords. sc client can be used for defined keywords. Common methods also can be added to this class
    public static SeeTestClient sc = null;
    public static AppiumDriver driver = null;
    public static String strRandomName = Utils.getRandomName() + " " + Utils.getRandomName();
    private static HttpResponse<InputStream> responseInputStream;
    public static String strAppType = "";
    public static boolean isStageApp = false;
    public static String cloudAppName = "";
    public static String strVINType = "";
    private static final Logger logger = LoggerFactory.getLogger(SeeTestKeywords.class);
    public static Rectangle statusBarRectangle = null;
    public static String deviceTag = "";
    public static boolean blnImageTag = false;
    public static boolean blnDeclinePopup = false;
    public static String selectedLanguage = "";
    public static String selectedCountry = "";
    public static String elementsDumpBundleId = "";
    public static String strAppPackage = "";
    public static boolean isInMarketApp = false;
    public static boolean isDeepLinksTest = false;

    // S3 bucket cred & Variables
    static AWSCredentials credentials = new BasicAWSCredentials(
            "***REMOVED***",
            "***REMOVED***"
    );
    static AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();
    //private String valToUpdate;

    // App set up for android and iOS
    public static void android_Setup(String port, String udid, String strPackageName, String testName) {
        createLog("Android set up and launch application started");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("udid", udid);
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        try {
            driver = new AndroidDriver(new URL("http://localhost:" + port + "/wd/hub"), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.setLogLevel(Level.INFO);
        sc = new SeeTestClient(driver);
        //sc.setReporter("html", System.getProperty("user.dir") + "//reports//" + testName + new Date(), testName);
        sc.setShowReport(true);
        sc.startStepsGroup("Setup");
        sc.closeAllApplications();
        //sc.hybridClearCache();
        //sc.install(appPath, false, false);
        sc.applicationClearData(strPackageName);
        sc.launch(strPackageName, false, true);
        sc.setProperty("element.visibility.level", "full");
        sc.syncElements(10000, 30000);
        if (blnDeclinePopup) {
            createLog("Test requires permission pop up decline");
            Android_declineHandlePopups();
        } else {
            createLog("Test requires permission pop up accept");
            Android_handlepopups();
        }
        sc.stopStepsGroup();
        createLog("Android set up and launch application completed");
    }

    public static void android_Setup1(String strPackageName, String testName, String cloudApp) throws MalformedURLException, UnirestException {
        createLog("Android set up and launch application started");
        String accessKey = ConfigSingleton.configMap.get("accessKey");
        String tags = System.getProperty("tags");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", testName);
        caps.setCapability("accessKey", accessKey);
        if (blnImageTag) {
            createLog("tag is provided - imageDevices");
            caps.setCapability("deviceQuery", "@os='android' and tag='imageDevices'");
        } else {
            createLog("tag is not provided");
            caps.setCapability("deviceQuery", "@os='android'");
        }
        caps.setCapability("newSessionWaitTimeout", 7200); // Queue Time = 2 hours
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
//        caps.setCapability(MobileCapabilityType.APP, cloudApp);
//        caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appPackage);
//        caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, appActivity);
        HttpResponse<String> response;
        String strAvailable = null;
        try {
            createLog("waitForDevice - boolean image tag is set to false in test class - checking for device availability without device tag");
            response = Unirest.get("https://tmna.experitest.com/api/v1/devices?query=@os=" + "'" + "android" + "'")
                    .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey")).asString();
            createLog(response.toString());
            List<Object> devices = JsonPath.read(response.getBody(), "$.data..displayStatus");
            List<Object> deviceName = JsonPath.read(response.getBody(), "$.data..udid");
            int countDevices = devices.size();
            int countDeviceName = deviceName.size();
            for(int c=0;c<=countDevices-1;c++){
                if(devices.get(c).toString().contains("Available")) {
                    for(int d=0;d<=countDeviceName-1;d++) {
                        createLog("installing on device : " + deviceName.get(d));

                        createLog("Android - Device Connection initiated");
                        driver = new AndroidDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
                        createLog("Android - Device Connection established");
                        sc = new SeeTestClient(driver);
                        sc.setShowReport(true);
                        sc.startStepsGroup("Setup");
                        sc.closeAllApplications();
                        //sc.hybridClearCache();

                        sc.install(cloudApp, false, false);
                        sc.applicationClearData(strPackageName);
                        sc.launch(strPackageName, false, true);
                        driver.quit();
                    }
                }
            }
        } catch (Exception e) {
            createErrorLog("Wait for device method threw error: " + e);
        }
        createLog("Android - Device Connection initiated");
        driver = new AndroidDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
        createLog("Android Device connection established");
        sc = new SeeTestClient(driver);
        sc.setShowReport(true);
        sc.startStepsGroup("Setup");
        sc.closeAllApplications();
        //sc.hybridClearCache();
        MobileElement statusBar = (MobileElement) driver.findElement(By.xpath("//*[@id='status_bar_contents']"));
        statusBarRectangle = new Aeye(System.getProperty("user.dir"), driver).getElementRectangle(statusBar);
        if (cloudApp.equals("")) {
            sc.applicationClearData(strPackageName);
            sc.launch(strPackageName, false, true);
        } else {
            sc.install(cloudApp, false, false);
            sc.applicationClearData(strPackageName);
            sc.launch(strPackageName, false, true);
        }
        sc.setProperty("element.visibility.level", "full");
        sc.syncElements(10000, 30000);
        if (blnDeclinePopup) {
            createLog("Test requires permission pop up decline");
            Android_declineHandlePopups();
        } else {
            createLog("Test requires permission pop up accept");
            Android_handlepopups();
        }
        sc.stopStepsGroup();
        createLog("Android set up and launch application completed");
    }
    public static void android_Setup(String strPackageName, String udid, String cloudApp) throws MalformedURLException, UnirestException {
        createLog("Android set up and launch application started");
        String accessKey = ConfigSingleton.configMap.get("accessKey");
        String tags = System.getProperty("tags");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", "AppInstall-"+System.getProperty("cloudApp")+System.getProperty("platform"));
        caps.setCapability("udid", udid);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("newSessionWaitTimeout", 7200); // Queue Time = 2 hours
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
//        caps.setCapability(MobileCapabilityType.APP, cloudApp);
//        caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appPackage);
//        caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, appActivity);
        HttpResponse<String> response;
        String strAvailable = null;
        try {
            createLog("waitForDevice - boolean image tag is set to false in test class - checking for device availability without device tag");
            response = Unirest.get("https://tmna.experitest.com/api/v1/devices?query=@os=" + "'" + "android" + "'")
                    .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey")).asString();
            createLog(response.toString());
            List<Object> devices = JsonPath.read(response.getBody(), "$.data..displayStatus");
            List<Object> deviceName = JsonPath.read(response.getBody(), "$.data..udid");
            int countDevices = devices.size();
            int countDeviceName = deviceName.size();


                createLog("Android - Device Connection initiated");
                driver = new AndroidDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
                createLog("Android - Device Connection established");
                sc = new SeeTestClient(driver);
                sc.setShowReport(true);
                sc.startStepsGroup("Setup");
                sc.closeAllApplications();
                //sc.hybridClearCache();
                createLog("installing on device : " + udid + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
                sc.install(cloudApp, false, false);
                createLog("installation completed on : " + udid + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
                sc.applicationClearData(strPackageName);
                sc.launch(strPackageName, false, true);
                driver.quit();

        } catch (Exception e) {
            createErrorLog("Wait for device method threw error: " + e);
        }
        createLog("Android - Device Connection initiated");
        driver = new AndroidDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
        createLog("Android Device connection established");
        sc = new SeeTestClient(driver);
        sc.setShowReport(true);
        sc.startStepsGroup("Setup");
        sc.closeAllApplications();
        //sc.hybridClearCache();
        MobileElement statusBar = (MobileElement) driver.findElement(By.xpath("//*[@id='status_bar_contents']"));
        statusBarRectangle = new Aeye(System.getProperty("user.dir"), driver).getElementRectangle(statusBar);
        if (cloudApp.equals("")) {
            sc.applicationClearData(strPackageName);
            sc.launch(strPackageName, false, true);
        } else {
            sc.install(cloudApp, false, false);
//            sc.applicationClearData(strPackageName);
//            sc.launch(strPackageName, false, true);
        }
//        sc.setProperty("element.visibility.level", "full");
//        sc.syncElements(10000, 30000);
//        if (blnDeclinePopup) {
//            createLog("Test requires permission pop up decline");
//            Android_declineHandlePopups();
//        } else {
//            createLog("Test requires permission pop up accept");
//            Android_handlepopups();
//        }
        sc.stopStepsGroup();
        createLog("Android set up and launch application completed");
    }
    public static void android_Uninstall(String strPackageName, String udid, String cloudApp) throws MalformedURLException, UnirestException {
        createLog("Android app uninstallation started");
        String accessKey = ConfigSingleton.configMap.get("accessKey");
        String tags = System.getProperty("tags");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", "AppUninstall-"+System.getProperty("cloudApp")+System.getProperty("platform"));
        caps.setCapability("udid", udid);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("newSessionWaitTimeout", 7200); // Queue Time = 2 hours
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        HttpResponse<String> response;
        String strAvailable = null;
        try {
            createLog("waitForDevice - boolean image tag is set to false in test class - checking for device availability without device tag");
            response = Unirest.get("https://tmna.experitest.com/api/v1/devices?query=@os=" + "'" + "android" + "'")
                    .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey")).asString();
            createLog(response.toString());
            List<Object> devices = JsonPath.read(response.getBody(), "$.data..displayStatus");
            List<Object> deviceName = JsonPath.read(response.getBody(), "$.data..udid");
            int countDevices = devices.size();
            int countDeviceName = deviceName.size();


            createLog("Android - Device Connection initiated");
            driver = new AndroidDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
            createLog("Android - Device Connection established");
            sc = new SeeTestClient(driver);
            sc.setShowReport(true);
            sc.startStepsGroup("Setup");
            sc.closeAllApplications();
            //sc.hybridClearCache();
            createLog("Uninstalling App from device : " + udid + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
            sc.uninstall(strPackageName);
            createLog("App Uninstall completed on : " + udid + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
        } catch (Exception e) {
            createErrorLog("Wait for device method threw error: " + e);
        }
    }
    public static void android_SetupCTA(String CTAIP, String CTAUDID, String strPackageName, String testName) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("udid", CTAUDID);
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        try {
            driver = new AndroidDriver(new URL(CTAIP), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.setLogLevel(Level.INFO);
        sc = new SeeTestClient(driver);
       // sc.setReporter("html", System.getProperty("user.dir") + "//reports//" + testName + new Date(), testName);
        sc.setShowReport(true);
        sc.startStepsGroup("Setup");
        sc.closeAllApplications();
        //sc.hybridClearCache();
        //sc.install(appPath, false, false);
        sc.applicationClearData(strPackageName);
        sc.launch(strPackageName, false, true);
        sc.setProperty("element.visibility.level", "full");
        sc.syncElements(10000, 30000);
        Android_handlepopups();
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='login_login_btn']")) {
            createLog("Login page ready");
        } else {
            createLog("Login page was not Ready ... Retrying launching app again");
            sc.launch(strPackageName, false, true);
            Android_handlepopups();
            verifyElementFound("NATIVE", "xpath=//*[@id='login_login_btn']", 0);
        }
        sc.stopStepsGroup();
    }


    public static void iOS_Setup(String port, String udid, String strPackageName, String cloudApp) {
        createLog("iOS set up and launch application started");
        ConfigSingleton.INSTANCE.loadConfigProperties();
        String accessKey = ConfigSingleton.configMap.get("accessKey");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", "AppInstall-"+System.getProperty("cloudApp")+System.getProperty("platform"));
        caps.setCapability("udid", udid);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("attach.crash.log.to.report", true);
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        if(Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            try {
                HttpResponse<String> response;
                createLog("waitForDevice - boolean image tag is set to false in test class - checking for device availability without device tag");
                response = Unirest.get("https://tmna.experitest.com/api/v1/devices?query=@os=" + "'" + "ios" + "'")
                        .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey")).asString();
                createLog(response.toString());
                List<Object> devices = JsonPath.read(response.getBody(), "$.data..displayStatus");
                List<Object> deviceName = JsonPath.read(response.getBody(), "$.data..udid");
                int countDevices = devices.size();
//                int countDeviceName = deviceName.size();

//                for(int i=0;i<=countDevices-1;i++)
////                for (String element : array) {
//                    if (deviceName.get(i).toString().contains("Available") )
//                        createLog("installing on device : " + deviceName.get(i).toString());
//                    if (blnImageTag) {
//                        createLog("tag is provided - imageDevices");
//                        caps.setCapability("deviceQuery", "@os='iOS' and tag='imageDevices'");
//                    } else {
//                        createLog("tag is not provided");
//                        caps.setCapability("deviceQuery", "@os='iOS'");
//                    }
                createLog("IOS - Device Connection initiated");
                driver = new IOSDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
                createLog("IOS - Device Connection established");
                sc = new SeeTestClient(driver);
                sc.setShowReport(true);
                sc.startStepsGroup("Setup");
                sc.closeAllApplications();
                //sc.hybridClearCache();
                createLog("installing on device : " + udid + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
                sc.install(cloudApp, false, false);
//                createLog("installation completed on : " + udid + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
//                sc.applicationClearData(strPackageName);
//                sc.launch(strPackageName, false, true);
            } catch (Exception e) {
                createErrorLog("Wait for device method threw error: " + e);
            }

//            sc.setProperty("element.visibility.level", "full");
//            if (blnDeclinePopup) {
//                createLog("Test requires permission pop up decline");
//                ios_declinePreSetup();
//                ios_declineHandlePopups();
//            } else {
//                createLog("Test requires permission pop up accept");
//                ios_handlepopups();
//            }
            createLog("iOS set up and launch application completed");
            sc.stopStepsGroup();

        }
        else{
            try {
                driver = new IOSDriver(new URL("http://localhost:" + port + "/wd/hub"), caps);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            driver.setLogLevel(Level.INFO);
            //return driver;
            sc = new SeeTestClient(driver);
            sc.setShowReport(true);
            sc.startStepsGroup("Setup");
            sc.closeAllApplications();
            //sc.hybridClearCache();
            //sc.install(appPath, false, false);
            sc.applicationClearData(strPackageName);
            sc.launch(strPackageName, false, true);
            driver.quit();
            if (blnDeclinePopup) {
                createLog("Test requires permission pop up decline");
                ios_declineHandlePopups();
            } else {
                createLog("Test requires permission pop up accept");
                ios_handlepopups();
            }
            createLog("iOS set up and launch application completed");
            sc.stopStepsGroup();
        }
    }
    public static void iOS_Uninstall(String port, String udid, String strPackageName, String cloudApp) {
        createLog("iOS app uninstallation started");
        ConfigSingleton.INSTANCE.loadConfigProperties();
        String accessKey = ConfigSingleton.configMap.get("accessKey");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", "AppUninstall-"+System.getProperty("cloudApp")+System.getProperty("platform"));
        caps.setCapability("udid", udid);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("attach.crash.log.to.report", true);
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        if(Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            try {
                HttpResponse<String> response;
                createLog("waitForDevice - boolean image tag is set to false in test class - checking for device availability without device tag");
                response = Unirest.get("https://tmna.experitest.com/api/v1/devices?query=@os=" + "'" + "ios" + "'")
                        .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey")).asString();
                createLog(response.toString());
                List<Object> devices = JsonPath.read(response.getBody(), "$.data..displayStatus");
                List<Object> deviceName = JsonPath.read(response.getBody(), "$.data..udid");
                int countDevices = devices.size();
                createLog("IOS - Device Connection initiated");
                driver = new IOSDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
                createLog("IOS - Device Connection established");
                sc = new SeeTestClient(driver);
                sc.setShowReport(true);
                sc.startStepsGroup("Setup");
                sc.closeAllApplications();
                //sc.hybridClearCache();
                createLog("Uninstalling app from device : " + udid + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
                sc.uninstall(strPackageName);
                createLog("App uninstallation completed from device : " + udid + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
            } catch (Exception e) {
                createErrorLog("Wait for device method threw error: " + e);
            }
            sc.stopStepsGroup();
        }
    }

    public static void ios_SetUpCTA(String CTAIP, String udid, String strPackageName, String s) {
        System.out.println(System.getProperties().toString());
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("udid", udid);
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins
        caps.setCapability("attach.crash.log.to.report", true);
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        try {
            driver = new IOSDriver(new URL(CTAIP), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            createErrorLog("driver connection could not be established" + e);
        }
        driver.setLogLevel(Level.INFO);
        sc = new SeeTestClient(driver);
        sc.setShowReport(true);
        if (!System.getProperty("ctaXML").equalsIgnoreCase("oneapp_btpairing")) {
            sc.startStepsGroup("Setup");
            sc.closeAllApplications();
            //sc.hybridClearCache();
            //sc.install(appPath, false, false);
            sc.applicationClearData(strPackageName);
            sc.launch(strPackageName, false, true);
            sc.setProperty("element.visibility.level", "full");
            sc.syncElements(10000, 30000);
            ios_handlepopups();
//        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']")) {
//            createLog("Login page ready");
//        } else {
//            createLog("Login page was not Ready ... Retrying launching app again");
//            sc.launch(strPackageName, false, true);
//            ios_handlepopups();
//            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']", 0);
//        }
            sc.stopStepsGroup();
        }
    }

    public static void iOS_Setup1(String strPackageName, String testName, String cloudApp) throws MalformedURLException, UnirestException {
        createLog("iOS set up and launch application started");
        ConfigSingleton.INSTANCE.loadConfigProperties();
        String accessKey = ConfigSingleton.configMap.get("accessKey");
//        String tag= System.getProperty("tag");
//        String ph ="PHONE";
//        createLog("@os='ios' and @category='PHONE' and @tag="+"'"+tag+"'");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", testName);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("newSessionWaitTimeout", 7200); // Queue Time = 2 hours
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins
        if (blnImageTag) {
            createLog("tag is provided - imageDevices");
            caps.setCapability("deviceQuery", "@os='iOS' and tag='imageDevices'");
        } else {
            createLog("tag is not provided");
            caps.setCapability("deviceQuery", "@os='iOS'");
        }
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("attach.crash.log.to.report", true);
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
//        waitForDevice("ios");
        HttpResponse<String> response;
        String strAvailable = null;
        try {
            createLog("waitForDevice - boolean image tag is set to false in test class - checking for device availability without device tag");
            response = Unirest.get("https://tmna.experitest.com/api/v1/devices?query=@os=" + "'" + "ios" + "'")
                    .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey")).asString();
            createLog(response.toString());
            List<Object> devices = JsonPath.read(response.getBody(), "$.data..displayStatus");
            List<Object> deviceName = JsonPath.read(response.getBody(), "$.data..udid");
            int countDevices = devices.size();
            int countDeviceName = deviceName.size();
            for(int c=0;c<=countDevices-1;c++){
                if(devices.get(c).toString().contains("Available")) {
                    for(int d=0;d<=countDeviceName-1;d++) {
                        createLog("installing on device : " + deviceName.get(d));
                        createLog("IOS - Device Connection initiated");
                        driver = new IOSDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
                        createLog("IOS - Device Connection established");
                        sc = new SeeTestClient(driver);
                        sc.setShowReport(true);
                        sc.startStepsGroup("Setup");
                        sc.closeAllApplications();
                        //sc.hybridClearCache();
                        sc.install(cloudApp, false, false);
                        sc.applicationClearData(strPackageName);
                        sc.launch(strPackageName, false, true);
                        driver.quit();
                    }
                }
            }
        } catch (Exception e) {
            createErrorLog("Wait for device method threw error: " + e);
        }

        sc.setProperty("element.visibility.level", "full");
        if (blnDeclinePopup) {
            createLog("Test requires permission pop up decline");
            ios_declinePreSetup();
            ios_declineHandlePopups();
        } else {
            createLog("Test requires permission pop up accept");
            ios_handlepopups();
        }
        createLog("iOS set up and launch application completed");
        sc.stopStepsGroup();
    }
    public static void iOS_Setup(String strPackageName, String testName, String cloudApp) throws MalformedURLException, UnirestException {
        createLog("iOS set up and launch application started");
        ConfigSingleton.INSTANCE.loadConfigProperties();
        String accessKey = ConfigSingleton.configMap.get("accessKey");
//        String tag= System.getProperty("tag");
//        String ph ="PHONE";
//        createLog("@os='ios' and @category='PHONE' and @tag="+"'"+tag+"'");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", testName);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("newSessionWaitTimeout", 7200); // Queue Time = 2 hours
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("attach.crash.log.to.report", true);
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
//        waitForDevice("ios");
        HttpResponse<String> response;
        String strAvailable = null;
        try {
            createLog("waitForDevice - boolean image tag is set to false in test class - checking for device availability without device tag");
            response = Unirest.get("https://tmna.experitest.com/api/v1/devices?query=@os=" + "'" + "ios" + "'")
                    .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey")).asString();
            createLog(response.toString());
            List<Object> devices = JsonPath.read(response.getBody(), "$.data..displayStatus");
            List<Object> deviceName = JsonPath.read(response.getBody(), "$.data..udid");
            int countDevices = devices.size();
            int countDeviceName = deviceName.size();
            String[] array = {"00008101-00011CD00E68001E","00008110-000C10CA2288401E","00008110-000A4DDA2105801E","00008101-001964E221A1001E","00008030-001C34913685802E"};

            for(int i=0;i<=countDevices-1;i++) {
//                for (String element : array) {
                    if (deviceName.get(i).toString().contains("Available") )
                        createLog("installing on device : " + deviceName.get(i).toString());
                    if (blnImageTag) {
                        createLog("tag is provided - imageDevices");
                        caps.setCapability("deviceQuery", "@os='iOS' and tag='imageDevices'");
                    } else {
                        createLog("tag is not provided");
                        caps.setCapability("deviceQuery", "@os='iOS'");
                    }
                    createLog("IOS - Device Connection initiated");
                    driver = new IOSDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
                    createLog("IOS - Device Connection established");
                    sc = new SeeTestClient(driver);
                    sc.setShowReport(true);
                    sc.startStepsGroup("Setup");
                    sc.closeAllApplications();
                    //sc.hybridClearCache();
                    createLog("installing on device : " + deviceName.get(i).toString() + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
                    sc.install(cloudApp, false, false);
                    createLog("installation completed on : " + deviceName.get(i).toString() + "-App-" + System.getProperty("cloudApp") + "-" + System.getProperty("platform"));
                    sc.applicationClearData(strPackageName);
                    sc.launch(strPackageName, false, true);

            }
        } catch (Exception e) {
            createErrorLog("Wait for device method threw error: " + e);
        }

        sc.setProperty("element.visibility.level", "full");
        if (blnDeclinePopup) {
            createLog("Test requires permission pop up decline");
            ios_declinePreSetup();
            ios_declineHandlePopups();
        } else {
            createLog("Test requires permission pop up accept");
            ios_handlepopups();
        }
        createLog("iOS set up and launch application completed");
        sc.stopStepsGroup();
    }

    public static void iOS_Setup2_5(String testName) throws MalformedURLException, UnirestException, UnknownHostException {
        createLog("iOS set up and launch application started");
        ConfigSingleton.INSTANCE.loadConfigProperties();
        String accessKey = ConfigSingleton.configMap.get("accessKey");
//        String tag= System.getProperty("tag");
//        String ph ="PHONE";
//        createLog("@os='ios' and @category='PHONE' and @tag="+"'"+tag+"'");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", testName);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("newSessionWaitTimeout", 7200); // Queue Time = 2 hours
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins

//        if (blnImageTag) {
//            createLog("tag is provided - imageDevices");
//            caps.setCapability("deviceQuery", "@os='iOS' and tag='imageDevices'");
//        } else {
//            createLog("tag is not provided");
//            caps.setCapability("deviceQuery", "@os='iOS'");
//        }

        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("attach.crash.log.to.report", true);
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        createLog("Current IP: " + InetAddress.getLocalHost() + "Computer Name is : " + InetAddress.getLocalHost().getHostName());
        if (System.getProperty("cloudApp") != null) {
            waitForDevice("ios");
        }
        if (Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            createLog("Cloud IOS - Device Connection initiated");
            driver = new IOSDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
        } else {
            createLog("Local IOS - Device Connection initiated");
            closeAppiumSessions();
            driver = new IOSDriver(new URL("http://localhost:" + ConfigSingleton.configMap.get("port") + "/wd/hub"), caps);
        }

        createLog("IOS - Device Connection established");
        sc = new SeeTestClient(driver);
//        if(System.getProperty("tag")!=null) {
//            createLog("Settings device location to Tustin-CA");
//            sc.setLocation("33.726090", "-117.820080");
//        }
        //sc.setReporter("html", System.getProperty("user.dir") + "//reports//" + testName + new Date(), testName);
        sc.setShowReport(true);
        sc.startStepsGroup("Setup");
        sc.closeAllApplications();
        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
        setLocationPermission();
        //sc.hybridClearCache();
//        MobileElement statusBar = (MobileElement) driver.findElementByXPath("//*[@class='UIAStatusBar']");
//        statusBarRectangle = new Aeye(System.getProperty("user.dir"), driver).getElementRectangle(statusBar);

//        sc.install(cloudApp, false, false);
        if (Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            switch (System.getProperty("cloudApp").toLowerCase()) {
                case ("lexusstage"):
                    strAppType = "lexus";
                    isStageApp = true;
                    sc.install(ConfigSingleton.configMap.get("cloudAppStageV2_5LexusIOS"), false, false);
                    try {
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameLexusStage"), false, true);
                    }
                    catch (Exception e) {
                        createLog(String.valueOf(e));
                        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Untrusted Enterprise Developer' and @XCElementType='XCUIElementTypeStaticText']")) {
                            trustApp_iOS();
                            sc.launch(ConfigSingleton.configMap.get("strPackageNameLexusStage"), false, true);
                        }
                    }
                    if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                        click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5LexusIOS");
                    break;
                case ("toyotastage"):
                    strAppType = "toyota";
                    isStageApp = true;
                    sc.install(ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaIOS"), false, false);
                    try {
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), false, true);
                    }catch (Exception e) {
                        createLog(String.valueOf(e));
                        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Untrusted Enterprise Developer' and @XCElementType='XCUIElementTypeStaticText']")) {
                            trustApp_iOS();
                            sc.launch(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), false, true);
                        }
                    }
                    if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                        click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaIOS");
                    break;
                case ("lexus"):
                    strAppType = "lexus";
                    isStageApp = false;
                    try {
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameLexus"), false, true);
                    }catch (Exception e) {
                        createLog(String.valueOf(e));
                        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Untrusted Enterprise Developer' and @XCElementType='XCUIElementTypeStaticText']")) {
                            trustApp_iOS();
                            sc.launch(ConfigSingleton.configMap.get("strPackageNameLexus"), false, true);
                        }
                    }
                    if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                        click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5LexusIOS");
                    break;
                case ("toyota"):
                    strAppType = "toyota";
                    isStageApp = false;
                    try {
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameToyota"), false, true);
                    }catch (Exception e) {
                        createLog(String.valueOf(e));
                        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Untrusted Enterprise Developer' and @XCElementType='XCUIElementTypeStaticText']")) {
                            trustApp_iOS();
                            sc.launch(ConfigSingleton.configMap.get("strPackageNameToyota"), false, true);
                        }
                    }
                    if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                        click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaIOS");
                    break;
                case ("subaru"):
                    strAppType = "subaru";
                    isStageApp = false;
                    sc.install(ConfigSingleton.configMap.get("cloudAppSubaruIOS"), false, false);
                    setLocationPermission();
                    try {
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaru"), false, true);
                    } catch (Exception e) {
                        createLog(String.valueOf(e));
                        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Untrusted Enterprise Developer' and @XCElementType='XCUIElementTypeStaticText']")) {
                            trustApp_iOS();
                            sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaru"), false, true);
                        }
                    }
                    if (sc.isElementFound("NATIVE", "xpath=//*[@label='Change to Always Allow']", 0))
                        click("NATIVE", "xpath=//*[@label='Change to Always Allow']", 0, 1);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameSubaru");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppSubaruIOS");
                    break;
                case ("subarustage"):
                    strAppType = "subaru";
                    isStageApp = true;
                    sc.install(ConfigSingleton.configMap.get("cloudAppStageSubaruIOS"), false, false);
                    setLocationPermission();
                    try{
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaruStage"), false, true);
                    }catch (Exception e){
                        createLog(String.valueOf(e));
                        if(sc.isElementFound("NATIVE","xpath=//*[@id='Untrusted Enterprise Developer' and @XCElementType='XCUIElementTypeStaticText']")){
                            trustApp_iOS();
                            sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaruStage"), false, true);
                        }
                    }
                    if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                        click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameSubaruStage");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageSubaruIOS");
                    break;
                case ("toyotaappstore"):
                    strAppType = "toyota";
                    isInMarketApp = true;
                    sc.launch(ConfigSingleton.configMap.get("strPackageNameAppStoreToyota"), false, true);
                    if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                        click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreToyota");
                    break;
                case ("lexusappstore"):
                    strAppType = "lexus";
                    isInMarketApp = true;
                    sc.launch(ConfigSingleton.configMap.get("strPackageNameAppStoreLexus"), false, true);
                    if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                        click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreLexus");
                    break;
                case ("subaruappstore"):
                    strAppType = "subaru";
                    isStageApp = false;
                    isInMarketApp = true;
                    setLocationPermission();
                    sc.launch(ConfigSingleton.configMap.get("strPackageNameAppStoreSubaru"), false, true);
                    if (sc.isElementFound("NATIVE", "xpath=//*[@label='Change to Always Allow']", 0))
                        click("NATIVE", "xpath=//*[@label='Change to Always Allow']", 0, 1);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreSubaru");
                    break;
                case (""):
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexusstage") {
                        strAppType = "lexus";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameLexusStage"), false, true);
                        sc.syncElements(10000,20000);
                        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5LexusIOS");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyotastage") {
                        strAppType = "toyota";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), false, true);
                        sc.syncElements(10000,20000);
                        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaIOS");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "subarustage") {
                        strAppType = "subaru";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaruStage"), false, true);
                        sc.syncElements(10000,20000);
                        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameSubaruStage");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppStageSubaruIOS");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyota") {
                        strAppType = "toyota";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameToyota"), false, true);
                        sc.syncElements(10000,20000);
                        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaIOS");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexus") {
                        strAppType = "lexus";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameLexus"), false, true);
                        sc.syncElements(10000,20000);
                        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5LexusIOS");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "subaru") {
                        strAppType = "subaru";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaru"), false, true);
                        sc.syncElements(10000,20000);
                        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameSubaru");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppSubaruIOS");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyotaappstore") {
                        strAppType = "toyota";
                        isInMarketApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameAppStoreToyota"), false, true);
                        sc.syncElements(10000,20000);
                        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreToyota");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexusappstore") {
                        strAppType = "lexus";
                        isInMarketApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameAppStoreLexus"), false, true);
                        sc.syncElements(10000,20000);
                        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreLexus");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "subaruappstore") {
                        strAppType = "subaru";
                        isInMarketApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameAppStoreSubaru"), false, true);
                        sc.syncElements(10000,20000);
                        if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                            click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreSubaru");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "") {
                        createLog("No System Variable or Local value provided, Please provide cloudApp value if running on Cloud or provide local value if running locally");
                        break;
                    }
                    break;
            }
            if (isInMarketApp) {
                sc.report("Build Version" + ConfigSingleton.configMap.get("buildver"), true);
                createLog("Build Version: " + ConfigSingleton.configMap.get("buildver"));
            } else {
                sc.report("Build Version: " + getAppVersion(cloudAppName), true);
                createLog("Build Version: " + getAppVersion(cloudAppName));
            }
        } else {
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("lexusstage")) {
                strAppType = "lexus";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameLexusStage"), false, true);
                if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                    click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
            }
            else if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("toyotastage")) {
                strAppType = "toyota";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), false, true);
                if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                    click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
            }
            if (ConfigSingleton.configMap.get("local").toLowerCase() == "subarustage") {
                strAppType = "subaru";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaruStage"), false, true);
                sc.syncElements(10000,20000);
                if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                    click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameSubaruStage");
            }
            else if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("toyota")) {
                strAppType = "toyota";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameToyota"), false, true);
                if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                    click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
            }
            else if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("lexus")) {
                strAppType = "lexus";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameLexus"), false, true);
                if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                    click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
            }
            if (ConfigSingleton.configMap.get("local").toLowerCase() == "subaru") {
                strAppType = "subaru";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameSubaru"), false, true);
                sc.syncElements(10000,20000);
                if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                    click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameSubaru");
            }
            else if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyotaappstore") {
                strAppType = "toyota";
                isInMarketApp = true;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameAppStoreToyota"), false, true);
                sc.syncElements(10000,20000);
                if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                    click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreToyota");
            }
            else if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexusappstore") {
                strAppType = "lexus";
                isInMarketApp = true;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameAppStoreLexus"), false, true);
                sc.syncElements(10000,20000);
                if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                    click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreLexus");
            }
            if (ConfigSingleton.configMap.get("local").toLowerCase() == "subaruappstore") {
                strAppType = "subaru";
                isInMarketApp = true;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameAppStoreSubaru"), false, true);
                sc.syncElements(10000,20000);
                if(sc.isElementFound("NATIVE","xpath=//*[@label='Change to Always Allow']",0))
                    click("NATIVE","xpath=//*[@label='Change to Always Allow']",0,1);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreSubaru");
            }
            else if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("")) {
                createLog("No System Variable or Local value provided, Please provide cloudApp value if running on Cloud or provide local value if running locally");
            }
            sc.report("Build Version: " + ConfigSingleton.configMap.get("buildver"), true);
            createLog("Build Version: " + ConfigSingleton.configMap.get("buildver"));
        }
        sc.syncElements(5000, 50000);
//        sc.setProperty("element.visibility.level", "full");
        if (blnDeclinePopup) {
            createLog("Test requires permission pop up decline");
            ios_declinePreSetup();
            ios_declineHandlePopups();
        } else {
            createLog("Test requires permission pop up accept");
            ios_handlepopups();
        }
        createLog("iOS set up and launch application completed");
        checkIsLoginScreenDisplayed_IOS();
        sc.stopStepsGroup();
    }
    public static void iOS_Setup2_5(String testName,boolean install) throws MalformedURLException, UnirestException, UnknownHostException {
        createLog("iOS set up and launch application started");
        closeAppiumSessions();
        ConfigSingleton.INSTANCE.loadConfigProperties();
        String accessKey = ConfigSingleton.configMap.get("accessKey");
//        String tag= System.getProperty("tag");
//        String ph ="PHONE";
//        createLog("@os='ios' and @category='PHONE' and @tag="+"'"+tag+"'");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", testName);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("newSessionWaitTimeout", 7200); // Queue Time = 2 hours
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins

//        if (blnImageTag) {
//            createLog("tag is provided - imageDevices");
//            caps.setCapability("deviceQuery", "@os='iOS' and tag='imageDevices'");
//        } else {
//            createLog("tag is not provided");
//            caps.setCapability("deviceQuery", "@os='iOS'");
//        }
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("attach.crash.log.to.report", true);
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        if (System.getProperty("cloudApp") != null) {
            waitForDevice("ios");
        }
        if (Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            createLog("Cloud IOS - Device Connection initiated");
            driver = new IOSDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
        } else {
            createLog("Local IOS - Device Connection initiated");
            driver = new IOSDriver(new URL("http://localhost:" + ConfigSingleton.configMap.get("port") + "/wd/hub"), caps);
        }
        createLog("IOS - Device Connection established");
        sc = new SeeTestClient(driver);
//        if(System.getProperty("tag")!=null) {
//            createLog("Settings device location to Tustin-CA");
//            sc.setLocation("33.726090", "-117.820080");
//        }
        sc.setShowReport(true);
        sc.startStepsGroup("Setup");
        sc.closeAllApplications();
        //sc.hybridClearCache();
//        MobileElement statusBar = (MobileElement) driver.findElementByXPath("//*[@class='UIAStatusBar']");
//        statusBarRectangle = new Aeye(System.getProperty("user.dir"), driver).getElementRectangle(statusBar);

        sc.install(System.getProperty("cloudApp"), false, false);
        if (Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            switch (System.getProperty("cloudApp").toLowerCase()) {
                case ("lexusstage"):
                    strAppType = "lexus";
                    isStageApp = true;
                    sc.launch(ConfigSingleton.configMap.get("strPackageNameLexusStage"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
                    break;
                case ("toyotastage"):
                    strAppType = "toyota";
                    isStageApp = true;
                    sc.launch(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                    break;
                case ("lexus"):
                    strAppType = "lexus";
                    isStageApp = false;
                    sc.launch(ConfigSingleton.configMap.get("strPackageNameLexus"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
                    break;
                case ("toyota"):
                    strAppType = "toyota";
                    isStageApp = false;
                    sc.launch(ConfigSingleton.configMap.get("strPackageNameToyota"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
                    break;
                case (""):
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexusstage") {
                        strAppType = "lexus";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameLexusStage"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyotastage") {
                        strAppType = "toyota";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyota") {
                        strAppType = "toyota";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameToyota"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexus") {
                        strAppType = "lexus";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameLexus"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "") {
                        createLog("No System Variable or Local value provided, Please provide cloudApp value if running on Cloud or provide local value if running locally");
                        break;
                    }
                    break;
            }
        } else {
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("lexusstage")) {
                strAppType = "lexus";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameLexusStage"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
            }
            else if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("toyotastage")) {
                strAppType = "toyota";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
            }
            else if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("toyota")) {
                strAppType = "toyota";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameToyota"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
            }
            else if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("lexus")) {
                strAppType = "lexus";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("strPackageNameLexus"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
            }
            else if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("")) {
                createLog("No System Variable or Local value provided, Please provide cloudApp value if running on Cloud or provide local value if running locally");
            }
        }

        sc.setProperty("element.visibility.level", "full");
        if (blnDeclinePopup) {
            createLog("Test requires permission pop up decline");
            ios_declinePreSetup();
            ios_declineHandlePopups();
        } else {
            createLog("Test requires permission pop up accept");
            ios_handlepopups();
        }
        createLog("iOS set up and launch application completed");
        sc.stopStepsGroup();
    }

    public static void android_Setup2_5(String testName) throws MalformedURLException, UnirestException, UnknownHostException {
        createLog("Android set up and launch application started");
        ConfigSingleton.INSTANCE.loadConfigProperties();
        String accessKey = ConfigSingleton.configMap.get("accessKey");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", testName);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("newSessionWaitTimeout", 7200); // Queue Time = 2 hours
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        createLog("Current IP: " + InetAddress.getLocalHost() + "Computer Name is : " + InetAddress.getLocalHost().getHostName());
        if (System.getProperty("cloudApp") != null) {
            waitForDevice("android");
        }
        if (Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            createLog("Cloud Android - Device Connection initiated");
            driver = new AndroidDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
        } else {
            createLog("Local Android - Device Connection initiated");
            closeAppiumSessions();
            driver = new AndroidDriver(new URL("http://localhost:" + ConfigSingleton.configMap.get("port") + "/wd/hub"), caps);
        }
        createLog("Android - Device Connection established");
        sc = new SeeTestClient(driver);
        sc.setShowReport(true);
        sc.startStepsGroup("Setup");
        sc.closeAllApplications();
        setLocationPermissionAndroid();
        if (Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            switch (System.getProperty("cloudApp").toLowerCase()) {
                case ("lexusstage"):
                    strAppType = "lexus";
                    isStageApp = true;
                    sc.install(ConfigSingleton.configMap.get("cloudAppStageV2_5LexusAndroid"), false, false);
                    sc.launch(ConfigSingleton.configMap.get("appPackageStageLexus"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageStageLexus");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5LexusAndroid");
                    break;
                case ("toyotastage"):
                    strAppType = "toyota";
                    isStageApp = true;
                    sc.install(ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaAndroid"), false, false);
                    sc.launch(ConfigSingleton.configMap.get("appPackageStageToyota"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageStageToyota");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaAndroid");
                    break;
                case ("lexus"):
                    strAppType = "lexus";
                    isStageApp = false;
                    sc.launch(ConfigSingleton.configMap.get("appPackageProdLexus"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5LexusAndroid");
                    break;
                case ("toyota"):
                    strAppType = "toyota";
                    isStageApp = false;
                    sc.launch(ConfigSingleton.configMap.get("appPackageProdToyota"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaAndroid");
                    break;
                case ("lexusplaystore"):
                    strAppType = "lexus";
                    isInMarketApp = true;
                    sc.launch(ConfigSingleton.configMap.get("appPackageProdLexus"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
                    break;
                case ("toyotaplaystore"):
                    strAppType = "toyota";
                    isInMarketApp = true;
                    sc.launch(ConfigSingleton.configMap.get("appPackageProdToyota"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                    break;
                case ("subaru"):
                    strAppType = "subaru";
                    isStageApp = false;
                    sc.install(ConfigSingleton.configMap.get("cloudAppSubaruAndroid"), false, false);
                    sc.launch(ConfigSingleton.configMap.get("appPackageProdSubaru"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppSubaruAndroid");
                    break;
                case ("subarustage"):
                    strAppType = "subaru";
                    isStageApp = true;
                    sc.install(ConfigSingleton.configMap.get("cloudAppStageSubaruAndroid"), false, false);
                    sc.launch(ConfigSingleton.configMap.get("appPackageStageSubaru"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageStageSubaru");
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageSubaruAndroid");
                    break;
                case ("subaruplaystore"):
                    strAppType = "subaru";
                    isInMarketApp = true;
                    sc.launch(ConfigSingleton.configMap.get("appPackageProdSubaru"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                    break;
                case (""):
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexusstage") {
                        strAppType = "lexus";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("appPackageStageLexus"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("appPackageStageLexus");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5LexusAndroid");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyotastage") {
                        strAppType = "toyota";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaAndroid");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyota") {
                        strAppType = "toyota";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("appPackageProdToyota"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaAndroid");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexus") {
                        strAppType = "lexus";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("appPackageProdLexus"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5LexusAndroid");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "subarustage") {
                        strAppType = "subaru";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("appPackageStageSubaru"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("appPackageStageSubaru");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppStageSubaruAndroid");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "subaru") {
                        strAppType = "subaru";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("appPackageProdSubaru"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                        cloudAppName = ConfigSingleton.configMap.get("cloudAppSubaruAndroid");
                        break;
                    }

                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "") {
                        createErrorLog("No System Variable or Local value provided, Please provide cloudApp value if running on Cloud or provide local value if running locally");
                        break;
                    }
                    break;
            }
            if (isInMarketApp) {
                sc.report("Build Version" + ConfigSingleton.configMap.get("buildver"), true);
                createLog("Build Version: " + ConfigSingleton.configMap.get("buildver"));
            } else {
                sc.report("Build Version: " + getAppVersion(cloudAppName), true);
                createLog("Build Version: " + getAppVersion(cloudAppName));
            }
        } else {
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("lexusstage")) {
                strAppType = "lexus";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("appPackageStageLexus"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageStageLexus");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("toyotastage")) {
                strAppType = "toyota";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("appPackageStageToyota"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageStageToyota");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("toyota")) {
                strAppType = "toyota";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("appPackageProdToyota"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("lexus")) {
                strAppType = "lexus";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("appPackageProdLexus"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("toyotaplaystore")) {
                strAppType = "toyota";
                isInMarketApp = true;
                sc.launch(ConfigSingleton.configMap.get("appPackageProdToyota"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("lexusplaystore")) {
                strAppType = "lexus";
                isInMarketApp = true;
                sc.launch(ConfigSingleton.configMap.get("appPackageProdLexus"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("subarustage")) {
                strAppType = "subaru";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("appPackageStageSubaru"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageStageSubaru");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("subaru")) {
                strAppType = "subaru";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("appPackageProdSubaru"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("subaruplaystore")) {
                strAppType = "subaru";
                isInMarketApp = true;
                sc.launch(ConfigSingleton.configMap.get("appPackageProdSubaru"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("")) {
                createErrorLog("No System Variable or Local value provided, Please provide cloudApp value if running on Cloud or provide local value if running locally");
            }
            sc.report("Build Version: " + ConfigSingleton.configMap.get("buildver"), true);
            createLog("Build Version: " + ConfigSingleton.configMap.get("buildver"));
        }
        sc.syncElements(5000, 50000);
//        sc.setProperty("element.visibility.level", "full");
        sc.syncElements(10000, 30000);
        if (blnDeclinePopup) {
            createLog("Test requires permission pop up decline");
            Android_declineHandlePopups();
        } else {
            createLog("Test requires permission pop up accept");
            if (sc.isElementFound("NATIVE","xpath=//*[@id='button2']")){
                click("NATIVE","xpath=//*[@id='button2']",0,1);
            }
            if(sc.isElementFound("NATIVE","xpath=//*[@text='OK']")){
                click("NATIVE","xpath=//*[@text='OK']",0,1);
            }
            Android_handlepopups();
            if (sc.isElementFound("NATIVE","xpath=//*[@id='login_login_btn']")){
                createLog("Login Page is Ready");
            }else{
//               android_SignOut();
                checkIsLoginScreenDisplayed_android();
            }
        }
        createLog("Android set up and launch application completed");
//        checkIsLoginScreenDisplayed_android();
        sc.stopStepsGroup();
    }
    public static void android_Setup2_5(String testName,boolean install) throws MalformedURLException, UnirestException {
        createLog("Android set up and launch application started");
        closeAppiumSessions();
        ConfigSingleton.INSTANCE.loadConfigProperties();
        String accessKey = ConfigSingleton.configMap.get("accessKey");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testName", testName);
        caps.setCapability("accessKey", accessKey);
        caps.setCapability("newSessionWaitTimeout", 7200); // Queue Time = 2 hours
        caps.setCapability("endSessionWaitTimeout", 30); // Driver Quit Timeout = 30 seconds
        caps.setCapability("newCommandTimeout", 300); // New Command Timeout = 5 mins
        caps.setCapability("reportFormat", "video,html");
        caps.setCapability("reportDirectory", System.getProperty("user.dir") + "//reports");
        if (System.getProperty("cloudApp") != null) {
            waitForDevice("android");
        }
        if (Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            createLog("Cloud Android - Device Connection initiated");
            driver = new AndroidDriver(new URL("https://tmna.experitest.com/wd/hub"), caps);
        } else {
            createLog("Local Android - Device Connection initiated");
            driver = new AndroidDriver(new URL("http://localhost:" + ConfigSingleton.configMap.get("port") + "/wd/hub"), caps);
        }
        createLog("Android - Device Connection established");
        sc = new SeeTestClient(driver);
        sc.setShowReport(true);
        sc.startStepsGroup("Setup");
        sc.closeAllApplications();
        sc.install(System.getProperty("cloudApp"),false,false);
        if (Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
            switch (System.getProperty("cloudApp").toLowerCase()) {
                case ("lexusstage"):
                    strAppType = "lexus";
                    isStageApp = true;
                    sc.launch(ConfigSingleton.configMap.get("appPackageStageLexus"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageStageLexus");
                    break;
                case ("toyotastage"):
                    strAppType = "toyota";
                    isStageApp = true;
                    sc.launch(ConfigSingleton.configMap.get("appPackageStageToyota"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageStageToyota");
                    break;
                case ("lexus"):
                    strAppType = "lexus";
                    isStageApp = false;
                    sc.launch(ConfigSingleton.configMap.get("appPackageProdLexus"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
                    break;
                case ("toyota"):
                    strAppType = "toyota";
                    isStageApp = false;
                    sc.launch(ConfigSingleton.configMap.get("appPackageProdToyota"), false, true);
                    strAppPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                    break;
                case (""):
                    sc.report("Build Version: " + getAppVersion(cloudAppName), true);
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexusstage") {
                        strAppType = "lexus";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("appPackageStageLexus"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("appPackageStageLexus");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyotastage") {
                        strAppType = "toyota";
                        isStageApp = true;
                        sc.launch(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "toyota") {
                        strAppType = "toyota";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("appPackageProdToyota"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "lexus") {
                        strAppType = "lexus";
                        isStageApp = false;
                        sc.launch(ConfigSingleton.configMap.get("appPackageProdLexus"), false, true);
                        strAppPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
                        break;
                    }
                    if (ConfigSingleton.configMap.get("local").toLowerCase() == "") {
                        createErrorLog("No System Variable or Local value provided, Please provide cloudApp value if running on Cloud or provide local value if running locally");
                        break;
                    }
                    break;
            }
        } else {
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("lexusstage")) {
                strAppType = "lexus";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("appPackageStageLexus"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageStageLexus");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("toyotastage")) {
                strAppType = "toyota";
                isStageApp = true;
                sc.launch(ConfigSingleton.configMap.get("appPackageStageToyota"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageStageToyota");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("toyota")) {
                strAppType = "toyota";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("appPackageProdToyota"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("lexus")) {
                strAppType = "lexus";
                isStageApp = false;
                sc.launch(ConfigSingleton.configMap.get("appPackageProdLexus"), false, true);
                strAppPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
            }
            if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("")) {
                createErrorLog("No System Variable or Local value provided, Please provide cloudApp value if running on Cloud or provide local value if running locally");
            }
        }

        sc.setProperty("element.visibility.level", "full");
        sc.syncElements(10000, 30000);
        if (blnDeclinePopup) {
            createLog("Test requires permission pop up decline");
            Android_declineHandlePopups();
        } else {
            createLog("Test requires permission pop up accept");
            Android_handlepopups();
        }
        sc.stopStepsGroup();
        createLog("Android set up and launch application completed");
    }
    /* Custom Methods for Scripting
    // 1. click - Clicks defaulted to 5000. If element not found within 5 seconds then use sc.syncElements(intSilentTime, intTimeout);
    // 2. sendText - SendText defaulted to 5000. If element not found within 5 seconds then use sc.syncElements(intSilentTime, intTimeout);
       3. verifyElementFound - verifyElement found throws exception which stops the rest of the tests - This custom method handles the exception
     */

    public static void click(String strZone, String strElement, int intIndex, int intClickCount) {
        boolean blnFound;
        try {
            for (int i = 0; i < 5; i++) {
                blnFound = sc.isElementFound(strZone, strElement, intIndex);
                if (blnFound)
                    break;
                else {
                    delay(1000);
                }
            }
            sc.click(strZone, strElement, intIndex, intClickCount);
            createLog(strElement+ ": Element Clicked");
        } catch (Exception e) {
            sc.report("Element not clicked: " + e, false);
            createErrorLog("Element not clicked: " + e);
        }
    }

    public static void sendText(String strZone, String strElement, int intIndex, String strText) {
        boolean blnFound = false;
        try {
            for (int i = 0; i < 5; i++) {
                blnFound = sc.isElementFound(strZone, strElement, intIndex);
                if (blnFound)
                    break;
                else {
                    delay(1000);
                }
            }
            sc.elementSendText(strZone, strElement, intIndex, strText);
            sc.closeKeyboard();
        } catch (Exception e) {
            sc.report("SendText failed: " + e, false);
            createErrorLog("Could not send text: " + e);
        }
    }

    public static void verifyElementFound(String strZone, String element, int intIndex) {
        try {
            sc.verifyElementFound(strZone, element, intIndex);
            createLog("Element :" + element + " Found");
            sc.report("Verification Passed for :" + element, true);
        } catch (Exception IgnoreException) {
            createErrorLog("Verification Failed for :" + element);
            sc.report("verification failed with exception with Exception" + IgnoreException, false);
        }
    }

    /* Common Application Methods for Scripting
        1. ios_handlepopups
        2. android_handlepopups
        3. ios_gotodashboard
        4. ios_emailLogin
        5. ios_emailSignOut
        6. android_emailLoginIn
        7. android_signOut
        8. android_vehicleSwitcher(String strVin)
        9. iOS_vehicleSwitcher(String strVin)
        10 setVinType()
        11 ios_relaunch()
        12 android_relaunch()
     */

    public static void ios_handlepopups() {
        
        for (int i = 0; i < 2; i++) {
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]"))
                sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']")||sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']"))
                break;
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Would Like to Use Bluetooth') and @XCElementType='XCUIElementTypeAlert']"))
                sc.click("NATIVE", "xpath=//*[@text='OK' or @id='Allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Would Like to Send You Notifications') and @XCElementType='XCUIElementTypeAlert']"))
                sc.click("NATIVE", "xpath=//*[@id='Allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Keep Only While Using']"))
                sc.click("NATIVE", "xpath=//*[@id='Keep Only While Using']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Later' or @id='LATER']"))
                sc.click("NATIVE", "xpath=//*[@id='Later' or @id='LATER']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Try Face ID')]"))
                click("NATIVE", "xpath=//*[@id='Cancel']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='OK']"))
                sc.click("NATIVE", "xpath=//*[@id='OK']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Always Allow']"))
                sc.click("NATIVE", "xpath=//*[@id='Always Allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Allow While Using App']"))
                sc.click("NATIVE", "xpath=//*[@id='Allow While Using App']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Allow' or @id='Allow Once']"))
                sc.click("NATIVE", "xpath=//*[@id='Allow' or @id='Allow Once']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Announcements']"))
                sc.flickElement("NATIVE", "xpath=//*[@id='Announcements']", 0, "DOWN");
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Change to Only While Using']"))
                sc.click("NATIVE", "xpath=//*[@id='Change to Only While Using']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='Ok' or contains(@label,'accord')]"))
                sc.click("NATIVE", "xpath=//*[@label='Ok' or contains(@label,'accord')]", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'A new iOS update is now available')])[1]"))
                sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']"))
                break;
        }
    }

    public static void Android_handlepopups() {
        for (int i = 0; i < 3; i++) {
            sc.syncElements(2000, 4000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Dashboard Vehicle Image']"))
                break;
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'Please allow Display over other apps setting') or contains(@text,'Permita la configurac') or contains(@text,'Veuillez autoriser')]", 0)) {
                click("NATIVE", "xpath=//*[(@text='Cancel' or @text='Cancelar' or  contains(@text,'accord')) and contains(@id,'button')]", 0, 1);
                sc.syncElements(1000, 2000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='btn_later']"))
                click("NATIVE", "xpath=//*[@id='btn_later']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='alertTitle' and (@text='Verified Links' or @text='Enlaces verificados' or @text='Liens vérifiés')]")) {
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            }
            if(sc.isElementFound("NATIVE","xpath=//*[@text='Apps that can open links']")) {
                click("NATIVE", "xpath=//*[@content-desc='Navigate up']", 0, 1);
                sc.syncElements(2000, 4000);
            }
            if (sc.isElementFound("NATIVE", "xpath= //*[@text='Background Location Permission']")) {
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                sc.syncElements(2000, 4000);
            }
            if(sc.isElementFound("NATIVE","xpath=//*[@content-desc='Location permission' or @text='Location permission']")) {
                click("NATIVE", "xpath=//*[@id='allow_always_radio_button']", 0, 1);
                click("NATIVE", "xpath=(//*[@content-desc='Navigate up' or @contentDescription='Back'])[1]", 0, 1);
                sc.syncElements(2000, 4000);
            }

            if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Please allow Display over other apps setting') or contains(@text,'Permita la configurac') or contains(@text,'Veuillez autoriser')]",0)){
                click("NATIVE", "xpath=//*[(@text='Cancel' or @text='Cancelar' or  contains(@text,'accord')) and contains(@id,'button')]", 0, 1);
                sc.syncElements(1000, 2000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']"))
                click("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Tap on the account icon to explore the Take a Tour option.']"))
                click("NATIVE", "xpath=//*[@text='OK'] | //*[@text='Ok']", 0, 1);
            sc.syncElements(2000, 4000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
                sc.click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']"))
                click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='login_login_btn']"))
                break;
        }
    }

    public static void android_handlePages() {
        for (int i = 0; i < 3; i++) {
            sc.syncElements(2000, 4000);
            /*if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);*/
            if(sc.isElementFound("NATIVE","xpath=//*[@text='We have updated our Connected Services Master Data Consent']")) {
                click("NATIVE", "xpath=//*[@id='mdc_continue']", 0, 1);
                sc.syncElements(5000, 10000);
                click("NATIVE", "xpath=//*[@text='Accept' or @text='ACCEPT']", 0, 1);
                sc.syncElements(5000, 10000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='btn_later']"))
                click("NATIVE", "xpath=//*[@id='btn_later']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
                click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@contentDescription='Skip For Now' or @contentDescription='Skip for Now']")) {
                click("NATIVE", "xpath=//*[@contentDescription='Skip For Now' or @contentDescription='Skip for Now']", 0, 1);
                sc.syncElements(5000, 20000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']"))
                click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
            if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Please allow Display over other apps setting')]",0)){
                click("NATIVE", "xpath=//*[@text='Cancel' and contains(@id,'button')]", 0, 1);
            }

            if (sc.isElementFound("NATIVE", "xpath=//*[@id='alertTitle' and @id='button1']")) {
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Navigate')]")) {
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(10000, 20000);
            }
            /*if (sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Navigate')]"))
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Back']"))
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));*/

            // Skip For Now
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']"))
                click("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Tap on the account icon to explore the Take a Tour option.']"))
                click("NATIVE", "xpath=//*[@text='OK'] | //*[@text='Ok']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Find a Station']"))
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Announcements']"))
                sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@resource-id='dashboard_display_image' or @content-desc='Stay Connected Image']")) {
                turnOnNotifications();
                  break;
            }
            sc.syncElements(2000, 4000);
        }
    }

    public static void android_translationsGotoDashBoard() {
        for (int i = 0; i < 2; i++) {
            sc.syncElements(4000, 8000);
           /* if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);*/
            //Find Nearby Devices
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_button']"))
                sc.click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
            //Display on Top
            if(sc.isElementFound("NATIVE",convertTextToUTF8("//*[contains(@text,'Veuillez autoriser') or contains(@text,'Permita la configuración')]"),0)){
                click("NATIVE", "xpath=//*[(@text='Annuler' or @text='Cancelar') and contains(@id,'button')]", 0, 1);
                sc.syncElements(1000, 2000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='alertTitle' and @id='button1']")) {
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
            //Display Over App
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Navigate')]")) {
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(10000, 20000);
            }

            //FTUE Skip for Now
            if (sc.isElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Ignorer pour l’instant' or @text='Omitir por ahora']"))) {
                click("NATIVE", convertTextToUTF8("//*[@text='Ignorer pour l’instant' or @text='Omitir por ahora']"), 0, 1);
                sc.syncElements(2000, 4000);
                if(sc.isElementFound("NATIVE","xpath=//*[@text='Ok']",0)){
                    click("NATIVE", "xpath=//*[@text='Ok']", 0, 1);
                    sc.syncElements(2000, 4000);
                }
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']"))
                click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
            if (sc.isElementFound("NATIVE", convertTextToUTF8("xpath=//*[@contentDescription='Annonces'] | //*[@contentDescription='Anuncios'] | //*[@text='Annonces'] | //*[@text='Anuncios']")))
                sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Info']"))
                break;
            //
        }
    }

    public static void ios_gotodashboard() {
        reLaunchApp_iOS();
    }

    public static void android_gotodashboard() {
        reLaunchApp_android();
    }

    public static void ios_keepMeSignedIn(boolean blnKeepMeLoggedIn) {

        //checking is login screen is displayed
        checkIsLoginScreenDisplayed_IOS();
        if (blnKeepMeLoggedIn) {
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@value,'unselected')]"))
                click("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_KEEP_ME_LOGGED_IN']", 0, 1);
            createLog("Keep Logged in Selected");
        } else {
            if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@value,'unselected')]"))
                click("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_KEEP_ME_LOGGED_IN']", 0, 1);
            createLog("Keep Logged in Not Selected");
        }
    }

    public static void android_keepMeSignedIn(boolean blnKeepMeLoggedIn) {
        createLog("Selecting Keep me signed in");
        //checking is login screen is displayed
        checkIsLoginScreenDisplayed_android();

        if (blnKeepMeLoggedIn) {
            if (!sc.isElementFound("NATIVE", "xpath=//*[@id='login_keep_login_txt' and @checked='true']"))
                click("NATIVE", "xpath=//*[@id='login_keep_login_txt']", 0, 1);
        } else {
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='login_keep_login_txt' and @checked='true']"))
                click("NATIVE", "xpath=//*[@id='login_keep_login_txt']", 0, 1);
        }
    }

    public static void ios_emailLogin(String strEmail, String strPassword) {
        createLog("Email Login Started");
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']")) {
            click("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']", 0, 1);
            sc.syncElements(5000, 10000);
        }
        sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']" +
                "", 0, strEmail);
        String email = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_CONTINUE_BUTTON']", 0, 1);
        sc.syncElements(2000, 4000);
        sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD']", 0, strPassword);
        click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']", 0, 1);
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_TITLELABEL']")) {
//            String activationCode = getEmailAccessCode(email);
              String activationCode = new CTMailClient().getActivationCode(email);
            sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_INPUT_TEXTFIELD']", 0, activationCode);
            click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON']", 0, 1);
            sc.syncElements(3000, 30000);
            if(sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_INPUT_TEXTFIELD']")) {
                createLog("Retrying to enter new code");
                click("NATIVE", "xpath=//*[@id='Request a new Code' or @id='REQUEST A NEW CODE']", 0, 1);
                sc.syncElements(3000, 30000);
                activationCode = new CTMailClient().getActivationCode(email);
                sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_INPUT_TEXTFIELD']", 0, activationCode);
                click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON']", 0, 1);
            }
        }
        createLog("Email Login done");
        sc.syncElements(5000, 30000);
        // Biometrics Page
        if (blnDeclinePopup) {
            createLog("Post email login pop ups handle started - location permission decline");
            ios_declineHandlePages();
            createLog("Post email login pop ups handle finished - location permission decline");
        } else {
            createLog("Post email login pop ups handle started");
            ios_handlePages();
            createLog("Post email login pop ups handle finished");
        }
        createLog("Post email login pop ups handle finished");
        if(isDeepLinksTest) {
            createLog("Deeplink test - skipping dashboard screen verification");
        } else {
            verifyElementFound("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']",0);
        }
    }

    //Get Email access code using account email id through IDP API
    public static String getEmailAccessCode(String accountEmailId) {

        HttpResponse<String> response = null;

        createLog("Started - Getting Email OTP for - " + accountEmailId);

        String BASE_URL = "https://openidm.stg.toyotadriverslogin.com";
        String BEARER_TOKEN = "Bearer H8XkZLbwfKi42mHuYSEoKg";
        String COOKIE = "INGRESSCOOKIE=1702409113.335.3246.492036|cd4cac83873a50776a6eeb7fdff3d8be; session-jwt=eyJ0eXAiOiJKV1QiLCJraWQiOiJvcGVuaWRtLWp3dHNlc3Npb25obWFjLWtleSIsImN0eSI6IkpXVCIsImFsZyI6IkhTMjU2In0.ZXlKMGVYQWlPaUpLVjFRaUxDSnJhV1FpT2lKdmNHVnVhV1J0TFd4dlkyRnNhRzl6ZENJc0ltVnVZeUk2SWtFeE1qaERRa010U0ZNeU5UWWlMQ0poYkdjaU9pSlNVMEV4WHpVaWZRLkxLRkRjcjNvVnh3U0l1QVA0bWVFYWY0bmlaVW5KN0R6QUJvQV9XVG1DMUlRdUVFWlAtei0tS21ZZ1lpNWZHdWdWRThlUzhKY2wwZ3FYU1NyUDMyU3NpenRuNnZuRmtKZVppelp5ZlFfZ2I2bWoycms1OXp2LWlqVndsSFIyVjlRQ0w4aGYxSXdhMHZGOXpya2lnSk0wY3lyZ1JfZEtuOEdETURsS29rOHczVmE1VVc0V0NmZ1h1WS10bWhFaHVRdDg4MFB5ZWN6cHd0M04yZUJiZ25wY3VkMXVPbDhrdnlHekpqWTBGME1LejN5YVJZZGpFQ01vTy1rNG5vUFpzVEVaZGpZTG9JdXhfOEpreWdqbC14TU96Tkw4NFdiOVNMaTkyYU52VmFqeWgwdnl6VGtrSVNDZ0RDck5xNlNLdDk1MGwwT2w2dW5Db2hIZ29lY3BzRzUwUS5pY0N5UmR2djNEVE94SEczMm1DSEZnLmtmWWxoT0lhZy16VjFBSXBuX1F3dGpZc1FFQVh1aDNOT1Rfa0UyY2FGek52alY0Z1c5TlhTOW9qNndPc1BXdGw3TjlCNFJ4LXVoU3VtUjMxWmFicF9VM0JjM3Bld0JSUWNBc01mdmNVMGNhS2VmaWdiWHdFemRlSHNaN202VGk1bi1JZmd2Q0ZhUnFjQmRHbkIyNWJqdlFpSEdqUEJBYjVsdU0yRnlqQ3dkSzg0QlE4TUFlZ2x6SDB0ZWNFZE12MTl0N2pGVjRjODRQcEo0azdiUk5IaWJaU0k0OFBUT2JMRWhDOVpUQWdFQkxXak9HUUFvc2VzV25hck1tZzdtTlhjTlZKQktHRkprMG4yX29wUV9iNkhoVUVJNUd3b3EyeERzWVVFVnZYZlFYSmxUSks1ZzRKNHVWbVN0RkZDa3NhejBUdDBrU2FRTHBqTElXSFJhazNySUowYzF4Zl9pcEhTcS0yMVdNZzhyeWM0VXpuSGo3Sm9jenNlWG1YMW5PRk9TY2ozS3ZCRV9nZVhwOHEwZDVHdWpzY3JHZ2lHNjRWUTFpaHRrT09vZzhiOS1vR3JCN0Q5cVVlYzhLUGRGMFBtQlA3NWxuVmdVYUM5NGc2ajRFdDVZQ0dZam9VUEx3ckJHTWFsREdiY05VV0FEZi1ZdlQ0TFJ2TWVOR1dGNm5wWC0xX3NkdWtVZEVZaENRQmxEMV9lbTZBTDJWUXB3Sm5oMXNDS21YUlhBTEtlSWZ5U2VUNnJUQmFQbDk4VkZlbzIyeDUtb2ZheHZ3YU56WE1FTy0zSzl3ZGN6cHBpUGlONWhGWTVTZnlhWmlZRDJlOGdYS2RBYWNqeDhuOVY2WFRRMTBxNFJmMU1FNk8tTkZJSllpWGpBLkk3TmFKUnB1bEVxNkh3VUxzUklmVHc.YzF_M1Xhtljy_tspPmyffcwSabkPLBtMf2HcaWww6rs; session-jwt=eyJ0eXAiOiJKV1QiLCJraWQiOiJvcGVuaWRtLWp3dHNlc3Npb25obWFjLWtleSIsImN0eSI6IkpXVCIsImFsZyI6IkhTMjU2In0.ZXlKMGVYQWlPaUpLVjFRaUxDSnJhV1FpT2lKdmNHVnVhV1J0TFd4dlkyRnNhRzl6ZENJc0ltVnVZeUk2SWtFeE1qaERRa010U0ZNeU5UWWlMQ0poYkdjaU9pSlNVMEV4WHpVaWZRLlJKaFBaU2YtN29aLV9ZdGxSWVZDTWlFc1pZZ3hHeW82M01KcHZRUEVaNGFKbmVHakE1Ml9qNnVVNWtSZldjUUYyNXRDeWNRbVNnYlZ6UVZxckNxSVpNLTZyUHk2Vko1NEc4bENEYTJrWmd2SlVuOGdEVWtZdk54LXo3LWRyNWkzc19SZk5oS21jbWoyQXhSSWplVVRzeWhjcVAySUhUbE5oTTVnODN0WU90YWV4VHBZTzBUVmlhZUFlVW9JRVhDcGI4RGZGUVBfSmxtLXRpV1B4NmRVODlGb3J0ZGtjNUphRWtMNmFVMVFFM05iZ3JDZm1DT1k1Z2JPa1JPV1N1d3BObmxLREVXZF85MHI2WnRzLWZobTYzTTVqem9FZGdaY2ZNVERrZVFpSWZOOXZIeGRlTHM2Tm9Md1l2b3FuUXhhLUpJYTVhM2VacGxIVmc0SkhCYVRKdy41c0ozY0ZjWGJ3YndDRC1mMjRubzB3LlJIQzZreFZLVjA4eGVZUlA3R291ZlN4R3RmdW12eGRwZzI5VnBFRU84c1VTYXZzWG5vWFJpMjk1Tm40cnBHalNpS256akI3Y3o0d21fNnhsU1lxTVRRMVBuTTZVM1VhN3pGMGVHa0VMYnlwY0hMRHQ1VE1GbWozR09RVEZVRmZmNS13UWRFVWFvV0pTRVAyRFg4NExYUnhNWFVUSDkyN0xVTWZlaEItN3RTWUg3V0xQakNQbTBpVjk1U19KYnV0NTdOQUhHMHpIYVcwZElzR3V6QUVEQjVDUlNIdHJnbWd6aWFYMWw2VjJ4anNGWmNpX0VnNzVQa2ktV0hyTVAxNEp5c01wLXQ4eXd2NXBITlA0cnFwZHVsZExjSjFqVGU4TkxndDg1TWU4ektGaEpxQWNqLUx1X1BlX2hGTUo0Q0NYRG9ITjhuSURVc09aSm8wNS11OVcyZThqenhPTjlIemh3TmZZMC1XWXRBeXZGOHRuc1VrMXdhalVRYmJLajN5Rk0xT25lRWRWVVNYeldjYjVybDFQVV8zcmxVbU9ROXpRZk9WMHY0UkZxXy1yRmZGc2pxUlRSellEbGpzNDRUQmJlWVZqc0pyazJFRHAxcXFRTGZMYkFiSl9ZMkVvRVNyQ0EyQlpoOEZlaDBrdXRGWGdjZk92WWVUSnl3WG1fb3UtX2UyT2w5b2V4OGxXell0RS1UNlhCUDc2ZXhvZDZqM3ZzTVdCc3RaVjNMejJOUGVkOGt5bzZLdFRjZ1JxaXh2TzE5aHMzVGhISmo5ME5hV2RhTlhlLUFXeVpOaEQzOXJzMnhhcmlSaEtmNlBfZ0Q0elVvd2pIbTBIUVE2SHU1cGg5b1hzXzJ3S1RSUG1LdU5nWGZMWnZnLjVCMFhfZDlZMG9XbjFIUVN6bGZXU2c.QqrTY7m0s5Ywbph3nveC9fdGP8q5t8w5kM5_1TjsfHU";
     /*  switch (System.getProperty("region").toUpperCase()) {
            case ("NA"):
                BASE_URL = "https://openidm.stg.toyotadriverslogin.com";
                BEARER_TOKEN = "Bearer H8XkZLbwfKi42mHuYSEoKg";
                COOKIE = "INGRESSCOOKIE=1702409113.335.3246.492036|cd4cac83873a50776a6eeb7fdff3d8be; session-jwt=eyJ0eXAiOiJKV1QiLCJraWQiOiJvcGVuaWRtLWp3dHNlc3Npb25obWFjLWtleSIsImN0eSI6IkpXVCIsImFsZyI6IkhTMjU2In0.ZXlKMGVYQWlPaUpLVjFRaUxDSnJhV1FpT2lKdmNHVnVhV1J0TFd4dlkyRnNhRzl6ZENJc0ltVnVZeUk2SWtFeE1qaERRa010U0ZNeU5UWWlMQ0poYkdjaU9pSlNVMEV4WHpVaWZRLkxLRkRjcjNvVnh3U0l1QVA0bWVFYWY0bmlaVW5KN0R6QUJvQV9XVG1DMUlRdUVFWlAtei0tS21ZZ1lpNWZHdWdWRThlUzhKY2wwZ3FYU1NyUDMyU3NpenRuNnZuRmtKZVppelp5ZlFfZ2I2bWoycms1OXp2LWlqVndsSFIyVjlRQ0w4aGYxSXdhMHZGOXpya2lnSk0wY3lyZ1JfZEtuOEdETURsS29rOHczVmE1VVc0V0NmZ1h1WS10bWhFaHVRdDg4MFB5ZWN6cHd0M04yZUJiZ25wY3VkMXVPbDhrdnlHekpqWTBGME1LejN5YVJZZGpFQ01vTy1rNG5vUFpzVEVaZGpZTG9JdXhfOEpreWdqbC14TU96Tkw4NFdiOVNMaTkyYU52VmFqeWgwdnl6VGtrSVNDZ0RDck5xNlNLdDk1MGwwT2w2dW5Db2hIZ29lY3BzRzUwUS5pY0N5UmR2djNEVE94SEczMm1DSEZnLmtmWWxoT0lhZy16VjFBSXBuX1F3dGpZc1FFQVh1aDNOT1Rfa0UyY2FGek52alY0Z1c5TlhTOW9qNndPc1BXdGw3TjlCNFJ4LXVoU3VtUjMxWmFicF9VM0JjM3Bld0JSUWNBc01mdmNVMGNhS2VmaWdiWHdFemRlSHNaN202VGk1bi1JZmd2Q0ZhUnFjQmRHbkIyNWJqdlFpSEdqUEJBYjVsdU0yRnlqQ3dkSzg0QlE4TUFlZ2x6SDB0ZWNFZE12MTl0N2pGVjRjODRQcEo0azdiUk5IaWJaU0k0OFBUT2JMRWhDOVpUQWdFQkxXak9HUUFvc2VzV25hck1tZzdtTlhjTlZKQktHRkprMG4yX29wUV9iNkhoVUVJNUd3b3EyeERzWVVFVnZYZlFYSmxUSks1ZzRKNHVWbVN0RkZDa3NhejBUdDBrU2FRTHBqTElXSFJhazNySUowYzF4Zl9pcEhTcS0yMVdNZzhyeWM0VXpuSGo3Sm9jenNlWG1YMW5PRk9TY2ozS3ZCRV9nZVhwOHEwZDVHdWpzY3JHZ2lHNjRWUTFpaHRrT09vZzhiOS1vR3JCN0Q5cVVlYzhLUGRGMFBtQlA3NWxuVmdVYUM5NGc2ajRFdDVZQ0dZam9VUEx3ckJHTWFsREdiY05VV0FEZi1ZdlQ0TFJ2TWVOR1dGNm5wWC0xX3NkdWtVZEVZaENRQmxEMV9lbTZBTDJWUXB3Sm5oMXNDS21YUlhBTEtlSWZ5U2VUNnJUQmFQbDk4VkZlbzIyeDUtb2ZheHZ3YU56WE1FTy0zSzl3ZGN6cHBpUGlONWhGWTVTZnlhWmlZRDJlOGdYS2RBYWNqeDhuOVY2WFRRMTBxNFJmMU1FNk8tTkZJSllpWGpBLkk3TmFKUnB1bEVxNkh3VUxzUklmVHc.YzF_M1Xhtljy_tspPmyffcwSabkPLBtMf2HcaWww6rs; session-jwt=eyJ0eXAiOiJKV1QiLCJraWQiOiJvcGVuaWRtLWp3dHNlc3Npb25obWFjLWtleSIsImN0eSI6IkpXVCIsImFsZyI6IkhTMjU2In0.ZXlKMGVYQWlPaUpLVjFRaUxDSnJhV1FpT2lKdmNHVnVhV1J0TFd4dlkyRnNhRzl6ZENJc0ltVnVZeUk2SWtFeE1qaERRa010U0ZNeU5UWWlMQ0poYkdjaU9pSlNVMEV4WHpVaWZRLlJKaFBaU2YtN29aLV9ZdGxSWVZDTWlFc1pZZ3hHeW82M01KcHZRUEVaNGFKbmVHakE1Ml9qNnVVNWtSZldjUUYyNXRDeWNRbVNnYlZ6UVZxckNxSVpNLTZyUHk2Vko1NEc4bENEYTJrWmd2SlVuOGdEVWtZdk54LXo3LWRyNWkzc19SZk5oS21jbWoyQXhSSWplVVRzeWhjcVAySUhUbE5oTTVnODN0WU90YWV4VHBZTzBUVmlhZUFlVW9JRVhDcGI4RGZGUVBfSmxtLXRpV1B4NmRVODlGb3J0ZGtjNUphRWtMNmFVMVFFM05iZ3JDZm1DT1k1Z2JPa1JPV1N1d3BObmxLREVXZF85MHI2WnRzLWZobTYzTTVqem9FZGdaY2ZNVERrZVFpSWZOOXZIeGRlTHM2Tm9Md1l2b3FuUXhhLUpJYTVhM2VacGxIVmc0SkhCYVRKdy41c0ozY0ZjWGJ3YndDRC1mMjRubzB3LlJIQzZreFZLVjA4eGVZUlA3R291ZlN4R3RmdW12eGRwZzI5VnBFRU84c1VTYXZzWG5vWFJpMjk1Tm40cnBHalNpS256akI3Y3o0d21fNnhsU1lxTVRRMVBuTTZVM1VhN3pGMGVHa0VMYnlwY0hMRHQ1VE1GbWozR09RVEZVRmZmNS13UWRFVWFvV0pTRVAyRFg4NExYUnhNWFVUSDkyN0xVTWZlaEItN3RTWUg3V0xQakNQbTBpVjk1U19KYnV0NTdOQUhHMHpIYVcwZElzR3V6QUVEQjVDUlNIdHJnbWd6aWFYMWw2VjJ4anNGWmNpX0VnNzVQa2ktV0hyTVAxNEp5c01wLXQ4eXd2NXBITlA0cnFwZHVsZExjSjFqVGU4TkxndDg1TWU4ektGaEpxQWNqLUx1X1BlX2hGTUo0Q0NYRG9ITjhuSURVc09aSm8wNS11OVcyZThqenhPTjlIemh3TmZZMC1XWXRBeXZGOHRuc1VrMXdhalVRYmJLajN5Rk0xT25lRWRWVVNYeldjYjVybDFQVV8zcmxVbU9ROXpRZk9WMHY0UkZxXy1yRmZGc2pxUlRSellEbGpzNDRUQmJlWVZqc0pyazJFRHAxcXFRTGZMYkFiSl9ZMkVvRVNyQ0EyQlpoOEZlaDBrdXRGWGdjZk92WWVUSnl3WG1fb3UtX2UyT2w5b2V4OGxXell0RS1UNlhCUDc2ZXhvZDZqM3ZzTVdCc3RaVjNMejJOUGVkOGt5bzZLdFRjZ1JxaXh2TzE5aHMzVGhISmo5ME5hV2RhTlhlLUFXeVpOaEQzOXJzMnhhcmlSaEtmNlBfZ0Q0elVvd2pIbTBIUVE2SHU1cGg5b1hzXzJ3S1RSUG1LdU5nWGZMWnZnLjVCMFhfZDlZMG9XbjFIUVN6bGZXU2c.QqrTY7m0s5Ywbph3nveC9fdGP8q5t8w5kM5_1TjsfHU";
                break;
            case ("AU"):
                BASE_URL = "https://openidm.stg.toyotadriverslogin.com.au";
                BEARER_TOKEN = "Bearer Nv2ZFVdl7qFMd2UWXkWS";
                COOKIE = "route=1710932718.683.14465.751792|e5760e916e7701980c0f6b18c9952713; session-jwt=eyJ0eXAiOiJKV1QiLCJraWQiOiJvcGVuaWRtLWp3dHNlc3Npb25obWFjLWtleSIsImN0eSI6IkpXVCIsImFsZyI6IkhTMjU2In0.ZXlKMGVYQWlPaUpLVjFRaUxDSnJhV1FpT2lKdmNHVnVhV1J0TFd4dlkyRnNhRzl6ZENJc0ltVnVZeUk2SWtFeE1qaERRa010U0ZNeU5UWWlMQ0poYkdjaU9pSlNVMEV4WHpVaWZRLkt0OTgyVE03aXBqRjlBMy1zYXJrbjNHRllGeHZOblVhMWNWSV9iNTRkLTBuQkZ1VnYySFJ6MWpaQlZ5UU9XMmExNmh1SDk1cmhXc04wcGxuaVdQVlNFcWRJZGVHVVF2dE9leGNkakZpLTB2U2ZuU2VoWmNrRndiaTExYU12TXRuWGMxck82Q0dIb2h2b2JMMGQyVlpELXRsdlRuM0JtSThDcTFVOUhxZldSd21rVzRJVHEtanp2bXNoQnlMUEVFTkVvN0RxVDQwRVgtSUVGamZHb1hLWUtNZlBWWDRpVF9lbGo4NlNnc0xNRWlBbUpWWC16UmNzMDBQcXFGQ0JDa25PejBaanRnT21abTJValFDYTZocVpCZGduUWdkdGpkdkg2TVp2aGxaZmFuZlU4U2NoTUdZWW14aFB2NU81cmJEcEpEeXRhcUFNVHctSzQ2aU91NGtldy5za21UNWk4SWxLclR2YXhNYldOekZBLld2bklsMHhwTXlNcHNNRS1qaDZoNlRoek94MGtzckkydFgyMVlMbWR3M1R0ajZjTWFEMGhnNFREdGdwQTNoVDUtWU1IaVdYVnlkM0VqYzRXUlJwbnNsdXJremFBdlZIaXFkVTU2T0JDN1JHa05hdVNMTjRlWExoU3BZblVpMmw5T3lzb0NzRzRlRGl6U3NFV2NxNzJ4Yjh5UURJREl2aXk2UGJia3B0ZnlGOHMwM01NUnZsZ1N2UC01dV9uczREb3JFanlXX2p5b25EdnpEN2NMdTJlZDFoakJwaUxMeU9FVEVBMHAtWVZHdXp3aEZRSGI4U1UxYVN3cXpzRjEwaERmcFFFamYwaHFjYm9Mb1ZTTUVLbmRJUkx5WGVWemh4WWFic1NtX04tNkpUcm5GS3h4ejR4Q3kwWWQyNEVpVUs5WUgyQWcxalFmV3FKaEl6RE1QU2VhVm9hOHgzUTFKUVM1X2Vicld1b0tNNEN3ODZXbWs0dVJkTDFZcjRuZGJuMnJKWEEyTldnLTBZMzNGeEdZTVlGd002aDVZa0xlR2M0T0d4aDlsTTF2QktsTm5WRTlISGRoY1dxZzhpeFBYMUswbGlGVTZwOG1nR1h6TkFESURLWHBqMmRtc2hTeXU2eEhRV05CMW12cFNkTnBOVHllMU5FNnNyQ3dLUkhpWVhvMjBHN21DUVFlZWlLZkRPUUF3dlJOeEdmbGZGRElwNjBMd2YySlM0Ni04enBsUHFGRTJ5Vm5Nb1RxV2hpVnZacm5fbkp5Q2g5SGNzVEpQb1VaM2RfVVBUbi16Z2hKQWRDV0w5VVNHMmdENHpDNjduSWNxbG1EcGZXdlRsWTkwc3kwYmJIRWNCMG1rQ1hOZFdpREJfWmdsUExOdVY0VFlEM3pPempraGtwRVl6Nk9ZQUUwd1MxWjhfMFRCU2Y3MzBZLlFzS1AyajhQODhzazZ4c0NZY2QzZ3c.O9RJMXKjoC7rVbzz_x_fyER4KZfWvbA3NYI2Y0Y09jk; session-jwt=eyJ0eXAiOiJKV1QiLCJraWQiOiJvcGVuaWRtLWp3dHNlc3Npb25obWFjLWtleSIsImN0eSI6IkpXVCIsImFsZyI6IkhTMjU2In0.ZXlKMGVYQWlPaUpLVjFRaUxDSnJhV1FpT2lKdmNHVnVhV1J0TFd4dlkyRnNhRzl6ZENJc0ltVnVZeUk2SWtFeE1qaERRa010U0ZNeU5UWWlMQ0poYkdjaU9pSlNVMEV4WHpVaWZRLkF3TmdxZjV4cUxtQ0d5d2tOQm03Y1pvVERaOGtVcXFGalI2dFhpYjUzZ3lqb092LWdWYURodFZ5Z1JsaGt4X0VJSmdPTmRVcG9ublZLZEt2cXlKY3lidEt6UjZRWi1FbXlJaUZCSE5jSC1aUW91dEJ1M3ZXTHJWSm5nVjlvdEo3S0FiOHRJNi1jbVdmOVZkUHpjZ2FRb3FVQlNlbG9DTmRkZ2toRGV6bU9PMk1XUU1MNWEwd3dwOXF6R3BPZnUzQnEyUGF3ZzhUMXJscmw1NmtnMUg2bmdCcUVPSDJfa1d0LXdiQUZJYVpfSHZ3Y2UzeGo4OUp0VFVLUGdjNndJc2g4a3Y1clNKX3FNUGFXU25yREI4NWs3MWxudG40bzQxUDU4WHo0UFF1SEtvamR0TklYNzJwdzVYcnBxMFlMdVcwTElLT2tqM3QzNDhlMlp5RVNYVFB2dy5zZm1pOG4xY0NQaEUtRVh1YmNvSTF3LkMxbmpMd0pVbGttM0xMVl8wZVJ4T3VjSi1wQkpmUmtKaUZBUzBkTzRtMDVZWUt0RUZYZTBIV1djdTVTcnJvZW9Bb2dnX3cyaHQ5bHZzaE1JTXhOdE5VYjlzYWJRaC1qa3NFOC16cURzSWRaZ3Vra3dKUklSWkhGa05nbXUtSllrdjcxbkpXV0xDUDdFRUYxd3NyRnZIODROSmQ4Q1JSYTloQkJGQWE5MTFWVkcwVnFZRTZJY1lucDVkU19MVFV2d2s5dDlSN3doSVptMW9DczlNQmRJV2ZNcUFRU2paOFd3VldQREdhZVJibFVpRENXamtpQmwwUi10N3JJckZrVFBXcnZpWWUzSm9aUnkzcTFabHY4RGszYmZzU1VDMVhrR0VjUnZuZU1Nd0EtRjF3bjcxRDR0Z21aQkR0QW1QQ1NGX1VPaXpSak1TVU5lM0J5bzZTTWJvc2V6VDJPTmRLc1laTWpUbGFNLWY1blBvSU5uaDJ2aTBKUUpSNF9BS1FwbEZkOXZwdmh2V0RpZXpMRi1iUXRSTWxwSGtES1lKbWl1Y3ZkVHdXek53VjBnVUl0QkVkcldlNzlpaUdzWXNIYU05YUpiY2wwbnBEM08xQlV4X2d1OGNtNmk2bDE2RktqSzhGWHpEdHR3Q0g2bzVjckozOGViOFdzOUVLTUVvdEpJakFOMHcyQk5raHVkaE9lYTlocXhvWTQxYUN4OWZ3V2dGT3RjZlpiVlFpOElUY2M1bjFGa2hxZTI0RXgtRHBBUGowQVhESmQ1aFhUTUs2MmRyYXFFeFlDX1o5S3pBWk5WTGk0al9maENlblNhRXJsWUJ5cHA5MGRNOTh5Z2RvUVA5c214QlhKay1jelFJS0IxOUgtSHVBLlJ2VHNRc0VrUlhnbU14S1ZiUEFfWEE.SEMnxY2QyplGJGKtUYKCElL6IT2M3e60JXWPAkJ-8r4; session-jwt=eyJ0eXAiOiJKV1QiLCJraWQiOiJvcGVuaWRtLWp3dHNlc3Npb25obWFjLWtleSIsImN0eSI6IkpXVCIsImFsZyI6IkhTMjU2In0.ZXlKMGVYQWlPaUpLVjFRaUxDSnJhV1FpT2lKdmNHVnVhV1J0TFd4dlkyRnNhRzl6ZENJc0ltVnVZeUk2SWtFeE1qaERRa010U0ZNeU5UWWlMQ0poYkdjaU9pSlNVMEV4WHpVaWZRLlhKWThROWJjbjFpUjVqdEdqZmRWMkZFRFItRmFFLU5oc2txWVJxcUpLNnVkSEV5T3FIOVp5WlBCV2JzRW82dWRzZDY4NEJvM0hBWEFSMWlucTkycksyelotZnNpR0EzbU9HY3FpR3BzbmJlZXFsdjQ5dU9XZF8wQXFyMGRiUTROcENTb0huNzFFMjVXd2hWTW83aGM1NzVLYkw4YmkxeFNQTDJ4eG5vWTVJUzNYSHk1anVuM2EzWXFNYUVrY3VZN0g5bU0teTVSMXFSRU5tODB2QzQ5Y2hTamlXdndjN1NFNThkS0ZaZ2o5RVVMMzhVVkFzR2FGNmFsSW5GNHdpVGxiVE93Z01DX0dmdmZHaENCcnI3WVM4c3R6SFdkVHdReVVmZ2VWbGpSSklkNUxDNXVuMFVIaE4zT2stZmlScHVJZ1kzU2tLaDdtWlNMNXpRd2ZKS193QS5PVEJiN0syM3Fia2NCSmdxRVUzelFBLnpzVy1iajBwT0luZnNXR0xuY3RKSFVoXy1QZ2hnOVVORWtYSzI0aU9oQWxldTg0Vy1PM2hFQTdqckhhQ1dqcVo1bl9VaVdGbHVSV1AxdFEzazRTbGs0X09BQ2xJcWdqWWFLVzBzV1BBajJhQjFTX2xmYzF6ZERhRUN6VngtaEppdVNUbTdNMnExbXF6d1lmZEwteEVsamsxVzJOMExkUEJVMnlmY1ZZdVNUTHlxaXBBOExJYnEtcWNMYTdSRDNnNktkbnJHUkVhZGhpRWpyYXBRcXBvQ3ZXOVZWcDFEaE1GZWQyYmZEWGx2bklEUGFwdTRxS3RnZ21BZUhiVnJ6NU56LU5KaXgzV2ZSWTlVWXJpeXh2UzBwMXFEVXlZZUtXX2Q2TmdqOEhXWmtIUDRsLU44UnhlMjBCTzRsNkJJU044RTBYc2pYd3FiQklvcDlrUXZjTzE1bmxUTUprRnlIcmxuT3VrV1FBR0FIMkhMS1Z0RTBKcUlNcVFCY0JrenRHV0JOQTMwajRKZFJBNXJjWEV2OWV5Q3RVTk5jdHh2TXhIX1I5OGs4UzVtOU9wNFJvNy1Oa0RWa1lIYjlVeUJ6YzJLS00tN0dmTmtrckpjSG1kb3djTzM4dUpVdTQxaVpWUFh2ZXFBR3Fqdm16MGtEdVFUZV90YnBScGtKWUx6OUctTFZYVnVVQ3U1dmt2aHNGMUxEM1VvOHNyYjlpSWNIaFcyT1Rqd1paMHh6WS1aZi1kdDhmUjZST0U1SHVhb1FORzByaXNGdnVPbloxYnRnbWhYeXJkWFd4NDdwLWFfMmhkNVdXNkRJSXFaOFVEWlNZS1UybVN1SnpXYzBPX2Q1eGN0LXVyY21OTkxHWEM2a3ZYaG45VkpnLkdmWGhJVDVqalRIUVpONVpYZnhhNVE.gfxEdvaHXfd4hWv4Dk2ouoGFoUq0fWBnOO186RdeM3o";
                break;
        }  */

        try {
            if (System.getProperty("platform").equalsIgnoreCase("ios")) {
                response = Unirest.post("" + BASE_URL + "/openidm/endpoint/getOTPCode")
                        .header("Content-Type", "application/json")
                        .header("X-OpenIDM-Username", "anonymous")
                        .header("X-OpenIDM-Password", "anonymous")
                        .header("Authorization", BEARER_TOKEN)
                        .header("Cookie", COOKIE)
        //              .header("LegacyToGlobal", "true")
                        .body("{\"id_value\":\"" + accountEmailId + "\"}")
                        .asString();
            } else {
                response = Unirest.post("" + BASE_URL + "/openidm/endpoint/getOTPCode")
                        .header("Content-Type", "application/json")
                        .header("X-OpenIDM-Username", "anonymous")
                        .header("X-OpenIDM-Password", "anonymous")
                        .header("Authorization", BEARER_TOKEN)
                        .header("Cookie", COOKIE)
                        .body("{\"id_value\":\"" + accountEmailId + "\"}")
                        .asString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        createLog("getOTPCode response is : " + response.getBody());

        String emailOTP = JsonPath.read(response.getBody(), "$.otpCode").toString();
        createLog("Email OTP is: " + emailOTP);
        createLog("Completed - Getting Email OTP for - " + accountEmailId + "and Email OTP is: " + emailOTP);
        return emailOTP;
    }



    public static void ios_handlePages() {
        for (int i = 0; i <= 1; i++) {
            if(sc.isElementFound("NATIVE","xpath=//*[@id='Vehicle image, double tap to open vehicle info.']"))
                break;
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Later' or @id='LATER']"))
                sc.click("NATIVE", "xpath=//*[@id='Later' or @id='LATER']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Cancel']"))
                sc.click("", "//*[@id='Cancel']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@value='1']"))
                sc.click("NATIVE", "xpath=//*[@value='1']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='CONTINUE' or @id='Continue']"))
                sc.click("NATIVE", "xpath=//*[@id='CONTINUE' or @id='Continue']", 0, 1);
            if(sc.isElementFound("NATIVE","xpath=//*[@label='Connected Services Master Data Consent']"))
                sc.click("NATIVE", "xpath=//*[@accessibilityLabel='COMBINED_DATACONSENT_ACCEPT']", 0, 1);
            //Language
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Yes, change to English']"))
                click("NATIVE", "xpath=//*[@id='Yes, change to English']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]"))
                sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
            // Save Password
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Allow' or @id='Allow Once']"))
                sc.click("NATIVE", "xpath=//*[@id='Allow' or @id='Allow Once']", 0, 1);
            sc.syncElements(5000, 30000);
            // Skip For Now
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']"))
                click("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Tap on the account icon to explore the Take a Tour option.']"))
                click("NATIVE", "xpath=//*[@text='OK'] | //*[@text='Ok']", 0, 1);
            sc.syncElements(5000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Announcements']"))
                sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@label,'Allow') and contains(@label,'to use your location')])[1]"))
                sc.click("NATIVE", "xpath=//*[@text='Allow While Using App']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath= //*[@text='Background Location Permission']")) {
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                sc.syncElements(2000, 4000);
            }
            if(sc.isElementFound("NATIVE","xpath=//*[@content-desc='Location permission' or @text='Location permission']")) {
                click("NATIVE", "xpath=//*[@id='allow_always_radio_button']", 0, 1);
                click("NATIVE", "xpath=(//*[@content-desc='Navigate up' or @contentDescription='Back'])[1]", 0, 1);
                sc.syncElements(2000, 4000);
            }
            if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Please allow Display over other apps setting')]",0)){
                click("NATIVE", "xpath=//*[@text='Cancel' and contains(@id,'button')]", 0, 1);
                sc.syncElements(1000, 2000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
                break;
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Turn On']"))
            turnOnNotifications_iOS();
    }

    public static void ios_emailSignOut() {
        createLog("Started - iOS SignOut");
        sc.syncElements(5000, 10000);

        if(!sc.isElementFound("NATIVE","xpath=//*[@id='person' or @id='user_profile_button']")){
            reLaunchApp_iOS();
        }
        sc.waitForElement("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0, 30);
        click("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0, 1);
        sc.syncElements(2000, 5000);

        //Sign out
        click("NATIVE", "xpath=//*[@text='Sign Out' or @text='"+convertTextToUTF8("Se déconnecter")+"' or @text='Desconectar']", 0, 1);
        sc.syncElements(2000, 5000);

        click("NATIVE", "xpath=//*[@text='Sign Out' or @text='"+convertTextToUTF8("Se déconnecter")+"' or @text='Desconectar'][2]", 0, 1);
        sc.syncElements(5000, 10000);

        sc.waitForElement("NATIVE","xpath=//*[@id='LOGIN_BUTTON_COUNTRY']",0,10);
        //Verify Login screen displayed
        verifyElementFound("NATIVE","xpath=//*[@id='LOGIN_BUTTON_COUNTRY']",0);

        //Verify Country selected before sign in and after sing out are same
        createLog("Verify Country selected before sign in and after sing out");
        verifyCountryAfterSignOut_IOS();

        //Verify Language selected before sign in and after sing out are same
        createLog("Verify Language selected before sign in and after sing out");
        verifyLanguageAfterSignOut_IOS();
        createLog("Finished - iOS SignOut");
    }

    public static void verifyCountryAfterSignOut_IOS() {
        createLog("Country Selected before sign in "+selectedCountry);
        String countryInSignInScreen = sc.elementGetText("NATIVE", "xpath=//*[@id='LOGIN_BUTTON_COUNTRY']", 0);
        createLog("Country displayed after sing out "+countryInSignInScreen);
        if (countryInSignInScreen.contains(selectedCountry)) {
            sc.report("Expected Country name:"+selectedCountry+" | Actual Country name Displayed:"+countryInSignInScreen,true);
            createLog("Expected Country name:"+selectedCountry+" | Actual Country name Displayed:"+countryInSignInScreen);
        }else{
            sc.report("Expected Country name:"+selectedCountry+" | Actual Country name Displayed:"+countryInSignInScreen,false);
            createErrorLog("Expected Country name:"+selectedCountry+" | Actual Country name Displayed:"+countryInSignInScreen);
        }
    }

    public static void verifyLanguageAfterSignOut_IOS() {
        createLog("Language Selected before sign in "+selectedLanguage);
        String languageInSignInScreen = sc.elementGetText("NATIVE", "xpath=//*[@id='LOGIN_BUTTON_LANGUAGE']", 0);
        createLog("Language displayed after sing out "+languageInSignInScreen);

        if (languageInSignInScreen.contains(selectedLanguage)) {
            sc.report("Expected Language:"+selectedLanguage+" | Actual Language Displayed:"+languageInSignInScreen,true);
            createLog("Expected Language:"+selectedLanguage+" | Actual Language Displayed:"+languageInSignInScreen);
        }else{
            sc.report("Expected Language:"+selectedLanguage+" | Actual Language Displayed:"+languageInSignInScreen,false);
            createErrorLog("Expected Language:"+selectedLanguage+" | Actual Language Displayed:"+languageInSignInScreen);
        }
    }

    public static void delay(int intTime) {
        try {
            Thread.sleep(intTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void android_emailLoginIn(String strEmail, String strPassword) {
        createLog("Start: Android Email login");
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Okay']"))
            click("NATIVE", "xpath=//*[@text='Okay']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1' and @text='OK']"))
            click("NATIVE", "xpath=/*[@id='button1' and @text='OK']", 0, 1);
        click("NATIVE", "xpath=//*[@id='login_login_btn']", 0, 1);
        sc.syncElements(2000, 4000);
        sendText("NATIVE", "xpath=//*[@id='etName']", 0, strEmail);
        String email = sc.elementGetProperty("NATIVE", "xpath=//*[@id='etName']", 0,"text");
        createLog("Email Entered as:"+email);
        click("NATIVE", "xpath=//*[@id='btContinue']", 0, 1);
        sc.syncElements(5000, 10000);
        sendText("NATIVE", "xpath=//*[@id='etPassword']", 0, strPassword);
        createLog("Entered password");
        click("NATIVE", "xpath=//*[@id='btSignIn']", 0, 1);
        createLog("Signing into app");
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='etCode']")) {
            String activationCode = new CTMailClient().getActivationCode(email);
            sendText("NATIVE", "xpath=//*[@id='etCode']", 0, activationCode);
            createLog("OTP Entered");
            click("NATIVE", "xpath=//*[@id='btCodeSignIn']", 0, 1);
            sc.syncElements(10000, 30000);
            if(sc.isElementFound("NATIVE","xpath=//*[@id='etCode']")) {
                createLog("Retrying to enter new code");
                click("NATIVE", "xpath=//*[@id='tvReset' or @id='tvLexusReset']", 0, 1);
                sc.syncElements(3000, 30000);
                activationCode = new CTMailClient().getActivationCode(email);
                sendText("NATIVE", "xpath=//*[@id='etCode']", 0, activationCode);
                createLog("New OTP Entered");
                click("NATIVE", "xpath=//*[@id='btCodeSignIn']", 0, 1);
                sc.syncElements(10000, 30000);
            }
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='switchEnable']")) {
            String checkedValue = sc.elementGetProperty("NATIVE", "xpath=//*[@id='switchEnable']", 0, "checked");
            if (checkedValue.equalsIgnoreCase("true")) {
                click("NATIVE", "xpath=//*[@id='switchEnable']", 0, 1);
                click("NATIVE", "xpath=//*[@id='complete_purchase']", 0, 1);
            }
        }
        if (blnDeclinePopup) {
            createLog("Post email login pop ups handle started - location permission decline");
            android_declineHandlePages();
            createLog("Post email login pop ups handle finished - location permission decline");
        } else {
            createLog("Post email login pop ups handle started");
            android_handlePages();
            createLog("Post email login pop ups handle finished");
        }
        if(isDeepLinksTest) {
            createLog("Deeplink test - skipping dashboard screen verification");
        } else {
            if (!sc.isElementFound("NATIVE", "xpath=//*[@text='Info' or @content-desc='Stay Connected Image']")) {
                createLog("Vehicle image not found ..triggering gotoDashboard");
                android_gotodashboard();
            }
        }
        /*if(sc.isElementFound("NATIVE","xpath=//*[@text='Turn On']")){
            click("NATIVE","xpath=//*[@text='Turn On']",0,1);
            sc.syncElements(2000,5000);
            click("NATIVE","xpath=//*[@id='switch_widget']",0,1);
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        }*/
        sc.syncElements(5000, 30000);
        createLog("End: Android Email login");
    }

    public static void android_SignOut() {
        createLog("Started - Android SignOut");
        sc.syncElements(5000, 10000);

        if(!sc.isElementFound("NATIVE","xpath=//*[@resource-id='top_nav_profile_icon']")){
            reLaunchApp_android();
        }
        sc.waitForElement("NATIVE", convertTextToUTF8("//*[@id='top_nav_profile_icon']"), 0, 30);
        click("NATIVE", convertTextToUTF8("//*[@id='top_nav_profile_icon']"), 0, 1);
        sc.syncElements(2000, 5000);

        //Sign out
        click("NATIVE", "xpath=//*[@text='Sign Out' or @text='"+convertTextToUTF8("Se déconnecter")+"' or @text='Desconectar']", 0, 1);
        sc.syncElements(2000, 5000);

        click("NATIVE", "xpath=//*[(@text='Sign Out' or @text='" + convertTextToUTF8("Se déconnecter") + "' or @text='Desconectar')]//following-sibling::*[@class='android.widget.Button']", 0, 1);
        sc.syncElements(5000, 10000);

        sc.waitForElement("NATIVE","xpath=//*[@id='login_region_value_txt']",0,10);
        //Verify Login screen displayed
        verifyElementFound("NATIVE","xpath=//*[@id='login_region_value_txt']",0);

        //Verify Country selected before sign in and after sing out are same
        createLog("Verify Country selected before sign in and after sing out");
        validateCountrySelectionBeforeSignInAfterSignOutAndroid();

        //Verify Language selected before sign in and after sing out are same
        //createLog("Verify Language selected before sign in and after sing out");
        //validateLanguageSelectionBeforeSignInAfterSignOutAndroid();
        //*[@id='login_region_value_txt']
        createLog("Finished - Android SignOut");
    }

    public static  void android_emailLoginFrench(String strEmail, String strPassword) {
        createLog("Started:Email Login French");
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Okay'] | //*[contains(@text,'accord')]"))
            click("NATIVE", "xpath=//*[@text='Okay'] | //*[contains(@text,'accord')]", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1' or  @text='OK']"))
            click("NATIVE", "xpath=//*[@id='button1' or  @text='OK']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1' or  @text='OK']"))
            click("NATIVE", "xpath=//*[@id='button1' or  @text='OK']", 0, 1);
        sc.syncElements(5000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@class='android.widget.Button' and contains(text(),'accord')]", 0))
            click("NATIVE", "xpath=//*[@class='android.widget.Button' and contains(text(),'accord')]", 0, 1);
        sc.syncElements(2000, 4000);

        click("NATIVE", convertTextToUTF8("//*[@text='Se connecter' or @text='SE CONNECTER']"), 0, 1);
        sc.syncElements(20000, 40000);
        sendText("NATIVE", convertTextToUTF8("//*[@text='Adresse courriel ou numéro cellulaire' or @text='ADRESSE COURRIEL OU NUMÉRO CELLULAIRE']"), 0, strEmail);
        String email = sc.elementGetText("NATIVE", "xpath=//*[@id='etName']", 0);
        createLog("Entered username:"+strEmail);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@text='Continuer' or @text='CONTINUER']", 0, 1);
        sc.syncElements(20000, 40000);
        sendText("NATIVE", "xpath=//*[@text='Mot de passe' or @text='MOT DE PASSE']", 0, strPassword);
        sc.syncElements(2000, 4000);
        createLog("Entered password");
        click("NATIVE", "xpath=//*[@text='Connexion' or @text='CONNEXION']", 0, 1);
        sc.syncElements(20000, 40000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='etCode']")) {
            createLog("Enter activation code and sign in");
            String activationCode = new CTMailClient().getActivationCode(email);
            sendText("NATIVE", "xpath=//*[@id='etCode']", 0, activationCode);
            click("NATIVE", "xpath=//*[@id='btCodeSignIn']", 0, 1);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='switchEnable']")) {
            String checkedValue = sc.elementGetProperty("NATIVE", "xpath=//*[@id='switchEnable']", 0, "checked");
            if (checkedValue.equalsIgnoreCase("true")) {
                click("NATIVE", "xpath=//*[@id='switchEnable']", 0, 1);
                click("NATIVE", "xpath=//*[@id='complete_purchase']", 0, 1);
            }
        }
        sc.syncElements(20000, 40000);
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Langue de préférence']"))) {
            createLog("language preference popup");
            click("NATIVE", convertTextToUTF8("//*[@text='Oui, changer pour le français']"), 0, 1);
            sc.syncElements(20000, 40000);
        }
        android_translationsGotoDashBoard();
        createLog("Ended:Email Login French");
    }

    public void android_emailLoginSpanish(String strEmail, String strPassword) {
        createLog("Email Login Spanish");
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1' or  @text='OK']"))
            click("NATIVE", "xpath=//*[@id='button1' or  @text='OK']", 0, 1);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1' or  @text='OK']"))
            click("NATIVE", "xpath=//*[@id='button1' or  @text='OK']", 0, 1);
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Iniciar sesión']"), 0)) {
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Iniciar sesión']"), 0);
        }

        click("NATIVE", convertTextToUTF8("//*[@text='Iniciar sesión' or @text='INICIAR SESIÓN']"), 0, 1);
        sc.syncElements(5000, 10000);
        createLog("Enter username:"+strEmail);
        sendText("NATIVE", convertTextToUTF8("xpath=//*[@text='Correo electrónico o número cellular' or @text='CORREO ELECTRÓNICO O NÚMERO CELLULAR']"), 0, strEmail);
        String email = sc.elementGetText("NATIVE", "xpath=//*[@id='etName']", 0);
        sc.syncElements(5000, 10000);
        click("NATIVE", convertTextToUTF8("//*[@text='CONTINÚE' or @text='Continúe']"), 0, 1);
        delay(5000);
        sc.syncElements(5000, 10000);
        createLog("Enter password:");
        sendText("NATIVE", convertTextToUTF8("xpath=//*[@text='Contraseña' or @text='CONTRASEÑA']"), 0, strPassword);
        click("NATIVE", convertTextToUTF8("xpath=//*[@text='Iniciar sesión' or @text='INICIAR SESIÓN']"), 0, 1);
        delay(5000);
        sc.syncElements(5000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='etCode']")) {
            createLog("Enter activation code");
            String activationCode = new CTMailClient().getActivationCode(email);
            sendText("NATIVE", "xpath=//*[@id='etCode']", 0, activationCode);
            click("NATIVE", "xpath=//*[@id='btCodeSignIn']", 0, 1);
        }
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='switchEnable']")) {
            String checkedValue = sc.elementGetProperty("NATIVE", "xpath=//*[@id='switchEnable']", 0, "checked");
            if (checkedValue.equalsIgnoreCase("true")) {
                click("NATIVE", "xpath=//*[@id='switchEnable']", 0, 1);
                click("NATIVE", "xpath=//*[@id='complete_purchase']", 0, 1);
            }
        }
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Idioma preferido']"))) {
            createLog("Language popup preference");
            verifyElementFound("NATIVE", convertTextToUTF8("xpath=//*[contains(@text,'El idioma que seleccionó no es el idioma preferido en su perfil')]"), 0);
            click("NATIVE", convertTextToUTF8("//*[@text='Si, cámbielo a Español']"), 0, 1);
            sc.syncElements(10000, 20000);
        }
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Omitir por ahora']"))){
            click("NATIVE", convertTextToUTF8("//*[@text='Omitir por ahora' or @contentDescription='Omitir por ahora']"), 0, 1);
            sc.syncElements(10000, 20000);
            if (sc.isElementFound("NATIVE",convertTextToUTF8("//*[@text='Ok']"))) {
                click("NATIVE", convertTextToUTF8("//*[@text='Ok']"), 0, 1);
            }
        }
        android_translationsGotoDashBoard();
        createLog("Ended: Email Login Spanish");
    }


    public static void validateCountrySelectionBeforeSignInAfterSignOutAndroid(){
        createLog("Country Selected before sign in "+selectedCountry);
        //Read the country name after sing out
        String countryName=sc.elementGetText("NATIVE","xpath=//*[@id='login_region_value_txt']",0).trim();
        createLog("Country displayed after sing out "+countryName);
        if(selectedCountry.equalsIgnoreCase("USA")){
            //selectedCountry="United States";
            if(selectedCountry.equalsIgnoreCase(countryName)){
                sc.report("Expected Country name:"+selectedCountry+" | Actual Country name Displayed:"+countryName,true);
                createLog("Expected Country name:"+selectedCountry+" | Actual Country name Displayed:"+countryName);
            }else{
                sc.report("Expected Country name:"+selectedCountry+" | Actual Country name Displayed:"+countryName,false);
                createErrorLog("Expected Country name:"+selectedCountry+" | Actual Country name Displayed:"+countryName);
            }
        }
    }
    public static void validateLanguageSelectionBeforeSignInAfterSignOutAndroid(){
        createLog("Language Selected before sign in "+selectedLanguage);
        //Read the language  after sing out
        String language=sc.elementGetText("NATIVE","xpath=//*[@id='login_language_value_txt']",0).trim();
        createLog("Language displayed after sing out "+language);

        if(selectedLanguage.equalsIgnoreCase(language)){
            sc.report("Expected Language:"+selectedLanguage+" | Actual Language Displayed:"+language,true);
            createLog("Expected Language:"+selectedLanguage+" | Actual Language Displayed:"+language);
        }else{
            sc.report("Expected Language:"+selectedLanguage+" | Actual Language Displayed:"+language,false);
            createErrorLog("Expected Language:"+selectedLanguage+" | Actual Language Displayed:"+language);
        }


    }
    public static String convertTextToUTF8(String text) {
        String convertedText = null;
        try {
            convertedText = URLDecoder.decode(new String(text.getBytes(), "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            sc.report("Failed to change the text:" + text + " to UTF8", false);
            createErrorLog("Failed to change the text:" + text + " to UTF8");
        }
        return convertedText;
    }


    public static void android_vehicleSwitcher(String strVin) {
        createLog("Switching to vehicle: " + strVin);
        sc.syncElements(5000, 10000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='vehicle_icon']"))
            sc.longClick("NATIVE", "xpath=//*[@content-desc='vehicle_icon']", 0, 1, 0, 0);
        String strObject = sc.getText("TEXT");
        if (strObject.contains((strVin.substring(13)))) {
            sc.report("Found the expected VIN on switcher screen", true);
            sc.click("NATIVE", "xpath=//*[@content-desc='close_button']", 0, 1);
        } else {
            sc.swipeWhileNotFound("Right", sc.p2cx(50), 3000, "TEXT", strVin.substring(13), 0, 1000, 10, false);
            if (sc.isElementFound("TEXT", strVin.substring(13))) {
                sc.click("NATIVE", "xpath=//*[@content-desc='close_button']", 0, 1);
            } else {
                sc.swipeWhileNotFound("Left", sc.p2cx(50), 3000, "TEXT", strVin.substring(13), 0, 1000, 10, false);
                if (sc.isElementFound("TEXT", strVin.substring(13))) {
                    sc.click("NATIVE", "xpath=//*[@content-desc='close_button']", 0, 1);
                } else {
                    sc.report("VIN " + strVin + " Not Found in both directions", false);
                    Assertions.assertFalse(true, "VIN " + strVin + " Not Found in both directions");
                }
            }
        }
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Announcements' or @content-desc='Annonces' or @content-desc='Anuncios']")) {
            sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            sc.syncElements(2000, 30000);
        }
    }

    public static void iOS_vehicleSwitcher(String strVin) {
        createLog("Switching to vehicle: " + strVin);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='vehicle_icon']"))
            sc.longClick("NATIVE", "xpath=//*[@id='vehicle_icon']", 0, 1, 0, 0);
        String strObject = sc.getText("TEXT");
        if (strObject.contains(strVin.substring(13))) {
            sc.report("Found the expected VIN on switcher screen", true);
            sc.click("NATIVE", "xpath=//*[@id='close_button']", 0, 1);
        } else {
            sc.swipeWhileNotFound("Right", sc.p2cx(50), 3000, "TEXT", strVin.substring(13), 0, 1000, 10, false);
            if (sc.isElementFound("TEXT", strVin.substring(13))) {
                sc.click("NATIVE", "xpath=//*[@id='close_button']", 0, 1);
            } else {
                sc.swipeWhileNotFound("Left", sc.p2cx(50), 3000, "TEXT", strVin.substring(13), 0, 1000, 10, false);
                if (sc.isElementFound("TEXT", strVin.substring(13))) {
                    sc.click("NATIVE", "xpath=//*[@id='close_button']", 0, 1);
                } else {
                    sc.report("VIN " + strVin + " Not Found in both directions", false);
                    Assertions.assertFalse(true, "VIN " + strVin + " Not Found in both directions");
                }
            }
        }
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Announcements' or @id='Annonces' or @id='Anuncios']")) {
            sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            sc.syncElements(2000, 30000);
        }
    }

    public static void iOS_DefaultVehicle(String strVin) {
        createLog("Defaulting to vehicle: " + strVin);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='vehicle_icon']"))
            sc.longClick("NATIVE", "xpath=//*[@id='vehicle_icon']", 0, 1, 0, 0);
        sc.syncElements(3000, 30000);
        String strObject = sc.getText("TEXT");
        if (strObject.contains((strVin.substring(13)))) {
            sc.report("Found the expected VIN on switcher screen", true);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Default')]"))
                click("NATIVE", "xpath=//*[contains(@id,'Default')]", 0, 1);
            sc.click("NATIVE", "xpath=//*[@id='close_button']", 0, 1);
        } else {
            sc.swipeWhileNotFound("Right", sc.p2cx(50), 3000, "TEXT", strVin.substring(13), 0, 1000, 10, false);
            if (sc.isElementFound("TEXT", strVin.substring(13))) {
                if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Default')]"))
                    click("NATIVE", "xpath=//*[contains(@id,'Default')]", 0, 1);
                sc.click("NATIVE", "xpath=//*[@id='close_button']", 0, 1);
            } else {
                sc.swipeWhileNotFound("Left", sc.p2cx(50), 3000, "TEXT", strVin.substring(13), 0, 1000, 10, false);
                if (sc.isElementFound("TEXT", strVin.substring(13))) {
                    if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Default')]"))
                        click("NATIVE", "xpath=//*[contains(@id,'Default')]", 0, 1);
                    sc.click("NATIVE", "xpath=//*[@id='close_button']", 0, 1);
                } else {
                    sc.report("VIN " + strVin + " Not Found in both directions", false);
                    createLog("VIN " + strVin + " Not Found in both directions");
                    Assertions.assertFalse(true, "VIN " + strVin + " Not Found in both directions");
                }
            }
        }
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Announcements' or @id='Annonces' or @id='Anuncios']")) {
            sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            sc.syncElements(2000, 30000);
        }
    }

    public static void android_DefaultVehicle(String strVin) {
        createLog("Defaulting to vehicle: "+strVin);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='vehicle_icon']"))
            sc.longClick("NATIVE", "xpath=//*[@content-desc='vehicle_icon']", 0, 1, 0, 0);
        sc.syncElements(3000, 30000);
        String strObject = sc.getText("TEXT");
        if (strObject.contains((strVin.substring(13)))) {
            sc.report("Found the expected VIN on switcher screen", true);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Default')]"))
                click("NATIVE", "xpath=//*[contains(@content-desc,'Default')]", 0, 1);
            sc.click("NATIVE", "xpath=//*[@content-desc='close_button']", 0, 1);
        } else {
            sc.swipeWhileNotFound("Right", sc.p2cx(50), 3000, "TEXT", strVin.substring(13), 0, 1000, 10, false);
            if (sc.isElementFound("TEXT", strVin.substring(13))) {
                if (sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Default')]"))
                    click("NATIVE", "xpath=//*[contains(@content-desc,'Default')]", 0, 1);
                sc.click("NATIVE", "xpath=//*[@content-desc='close_button']", 0, 1);
            } else {
                sc.swipeWhileNotFound("Left", sc.p2cx(50), 3000, "TEXT", strVin.substring(13), 0, 1000, 10, false);
                if (sc.isElementFound("TEXT", strVin.substring(13))) {
                    if (sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Default')]"))
                        click("NATIVE", "xpath=//*[contains(@content-desc,'Default')]", 0, 1);
                    sc.click("NATIVE", "xpath=//*[@content-desc='close_button']", 0, 1);
                } else {
                    sc.report("VIN " + strVin + " Not Found in both directions", false);
                    Assertions.assertFalse(true, "VIN " + strVin + " Not Found in both directions");
                }
            }
        }
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Announcements' or @content-desc='Annonces' or @content-desc='Anuncios']")) {
            sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            sc.syncElements(2000, 30000);
        }
    }

    public static String setVinType(String Vin) {
        String vinType = "";
        /*vin Lexus NX350 21MM = JTJHKCEZ1N2004122, 5TDADAB59RS000015
          vin Toyota RAV4 17CYPlus PHEV = JTMFB3FV5MD006786
          vin Toyota Avalon 17CYPlus = 4T1BZ1FB5KU014237
          vin Lexus ES350 = 58ABZ1B17KU001401
          Vin Stage = 4T4BK46K090011043
        */
        switch (Vin) {
            case ("5TDADAB59RS000015"):
                vinType = "21MM";
                createLog("This VIN is for 21mm vehicle");
                break;
            case ("JTJHKCEZ1N2004122"):
                vinType = "21MM";
                createLog("This VIN is for 21MM vehicle");
                break;
            case ("4T4BK46K090011043"):
                vinType = "21MM";
                createLog("This Stage VIN is for 21MM vehicle");
                break;
            case ("JTMFB3FV5MD006786"):
                vinType = "17CYPLUS";
                createLog("This VIN is for 17CYPLUS vehicle");
                break;
        }
        strVINType = vinType;
        return strVINType;
    }

    public static void reLaunchApp_iOS() {
        createLog("Relaunching App");
        String appPackage = "";
        ConfigSingleton.INSTANCE.loadConfigProperties();
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case ("toyota"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
                break;
            case ("lexus"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
                break;
            case ("subaru"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameSubaru");
                break;
            case ("toyotaappstore"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreToyota");
                break;
            case ("lexusappstore"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreLexus");
                break;
            case ("subaruappstore"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreSubaru");
                break;
            case ("toyotastage"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                break;
            case ("lexusstage"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
                break;
            case ("subarustage"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameSubaruStage");
                break;
            case (""):
                if (System.getProperty("cloudApp").equalsIgnoreCase("toyota")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("lexus")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("subaru")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameSubaru");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("toyotaappstore")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreToyota");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("lexusappstore")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreLexus");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("subaruappstore")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreSubaru");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("toyotastage")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("lexusstage")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("subarustage")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameSubaruStage");
                    break;
                }
                break;
            default:
                createLog("Kill and Relaunch failed");
        }
        sc.closeAllApplications();
        sc.launch(appPackage, false, false);
        sc.syncElements(5000, 30000);
//        sc.setProperty("element.visibility.level", "full");
        sc.syncElements(5000, 30000);
        ios_handlepopups();
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='person' or @id='user_profile_button']",0)){
            sc.launch(appPackage, false, false);
        }
        sc.syncElements(2000, 5000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']")) {
            createErrorLog("Login screen is displayed on relaunching app - expected to display Dashboard screen");
        }
    }

    public static void reLaunchApp_android() {
        createLog("Relaunching App");
        String appPackage = "";
        ConfigSingleton.INSTANCE.loadConfigProperties();
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case ("toyota"):
                appPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                break;
            case ("lexus"):
                appPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
                break;
            case ("toyotastage"):
                appPackage = ConfigSingleton.configMap.get("appPackageStageToyota");
                break;
            case ("lexusstage"):
                appPackage = ConfigSingleton.configMap.get("appPackageStageLexus");
                break;
            case ("toyotaplaystore"):
                createLog("Launch Android Toyota In Market app");
                appPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                break;
            case ("lexusplaystore"):
                createLog("Launch Android Lexus In Market app");
                appPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
                break;
            case ("subarustage"):
                appPackage = ConfigSingleton.configMap.get("appPackageStageSubaru");
                break;
            case ("subaru"):
                appPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                break;
            case ("subaruplaystore"):
                createLog("Launch Android Subaru In Market app");
                appPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                break;
            case (""):
                if (System.getProperty("cloudApp").equalsIgnoreCase("toyota")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("lexus")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("toyotaplaystore")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageProdToyota");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("lexusplaystore")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageProdLexus");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("toyotastage")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageStageToyota");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("lexusstage")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageStageLexus");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("subarustage")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageStageSubaru");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("subaru")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("subaruplaystore")) {
                    appPackage = ConfigSingleton.configMap.get("appPackageProdSubaru");
                    break;
                }
                break;
            default:
                createLog("Kill and Relaunch failed");
        }
        sc.deviceAction("Home");
        sc.closeAllApplications();
        sc.launch(appPackage, false, false);
        sc.setProperty("element.visibility.level", "full");
        sc.syncElements(5000, 30000);
        Android_handlepopups();
        if (blnDeclinePopup) {
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'access this device’s location')]"))
                sc.click("NATIVE", "xpath=//*[@text='Don’t allow']", 0, 1);
        }
        sc.syncElements(2000, 5000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='login_login_btn']")) {
            createErrorLog("Login screen is displayed on relaunching app - expected to display Dashboard screen");
        }
    }

    public static void selectionOfCountry_IOS(String country) {
        createLog("Selecting country/region " + country + " on login screen");
        try{
            //Country or Region
            verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_COUNTRY']", 0);
            click("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_COUNTRY']", 0, 1);
            switch (country.toLowerCase()) {
                case ("puerto rico"):
                    selectedCountry = convertTextToUTF8("Puerto Rico");
                    break;
                case ("canada"):
                    selectedCountry = convertTextToUTF8("Canada");
                    break;
                case ("mexico"):
                    selectedCountry = convertTextToUTF8("Mexico");
                    break;
                case ("usa"):
                    selectedCountry =convertTextToUTF8("USA");
                    break;
                default:
                    throw new RuntimeException("Please provide valid country name from the list [Puerto Rico,Canada,Mexico,USA]");
            }

            if (sc.isElementFound("NATIVE", "xpath=//*[@text='" + selectedCountry + "']"))
                click("NATIVE", "xpath=//*[@text='" + selectedCountry + "']", 0, 1);
            else
                click("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("États-Unis")+"']", 0, 1);

            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Verified Links' or @text='"+convertTextToUTF8("Liens vérifiés")+"' or @text='Enlaces verificados']")) {
                createLog("Handling Verified Links popup");
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(500, 1000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            sc.syncElements(2000, 4000);
            createLog("Selected country " + selectedCountry);
            createLog("Selected country/region on login screen");

        } catch (Exception e) {
            sc.report("Failed to select Country " + country, false);
            createErrorLog("Failed to select Country " + country + ":" + e.toString());
        }
    }

    public static void selectionOfLanguage_IOS(String language) {
        createLog("Selecting Language " + language + " on login screen");
        try {
            sc.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_LANGUAGE']", 0, 30);
            click("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_LANGUAGE']", 0, 1);
            sc.syncElements(2000, 30000);

            switch (language.toLowerCase()) {
                case ("english"):
                    selectedLanguage = convertTextToUTF8("English");
                    break;
                case ("spanish"):
                    selectedLanguage =  convertTextToUTF8("Español");
                    break;
                case ("french"):
                    selectedLanguage = convertTextToUTF8("Français");
                    break;
                default:
                    throw new RuntimeException("Please provide valid country name from the list [english,spanish,french]");
            }
            click("NATIVE", "xpath=//*[@text='" + selectedLanguage + "']", 0, 1);
            sc.syncElements(2000, 4000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Verified Links' or @text='"+convertTextToUTF8("Liens vérifiés")+"' or @text='Enlaces verificados']")) {
                createLog("Handling verified links popup");
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(500, 1000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Okay'] | //*[contains(@text,'accord')]"))
                click("NATIVE", "xpath=//*[@text='Okay'] | //*[contains(@text,'accord')]", 0, 1);

            if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);

        } catch (Exception e) {
            sc.report("Failed to select language " + selectedLanguage, false);
            createErrorLog("Failed to select language " + selectedLanguage + ":" + e.toString());
        }
    }


    public static void selectionOfCountry_Android(String country) {
        createLog("Selecting country/region " + country + " on login screen");
        try{
            //Country or Region
            verifyElementFound("NATIVE", "xpath=//*[@id='login_region_value_txt']", 0);
            click("NATIVE", "xpath=//*[@id='login_region_value_txt']", 0, 1);
            switch (country.toLowerCase()) {
                case ("puerto rico"):
                    selectedCountry = convertTextToUTF8("Puerto Rico");
                    break;
                case ("canada"):
                    selectedCountry = convertTextToUTF8("Canada");
                    break;
                case ("mexico"):
                    selectedCountry = convertTextToUTF8("Mexico");
                    break;
                case ("usa"):
                    selectedCountry =convertTextToUTF8("USA");
                    break;
                default:
                    throw new RuntimeException("Please provide valid country name from the list [Puerto Rico,Canada,Mexico,USA]");
            }

            //Select the country name based on type of the app
            if (strAppType.equalsIgnoreCase("lexus")) {
                if (sc.isElementFound("NATIVE", "xpath=//*[@text='" + selectedCountry.toUpperCase() + "']"))
                    click("NATIVE", "xpath=//*[@text='" + selectedCountry.toUpperCase() + "']", 0, 1);
                else
                    click("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("États-Unis").toUpperCase()+"']", 0, 1);
            } else {
                if (sc.isElementFound("NATIVE", "xpath=//*[@text='" + selectedCountry + "']"))
                    click("NATIVE", "xpath=//*[@text='" + selectedCountry + "']", 0, 1);
                else
                    click("NATIVE", "xpath=//*[@text='"+convertTextToUTF8("États-Unis")+"']", 0, 1);
            }

            if (sc.isElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Verified Links' or @text='Liens vérifiés' or @text='Enlaces verificados']"))) {
                createLog("Handling Verified Links popup");
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(500, 1000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            sc.syncElements(2000, 4000);
            createLog("Selected country " + selectedCountry);
            createLog("Selected country/region on login screen");

        } catch (Exception e) {
            sc.report("Failed to select Country " + country, false);
            createErrorLog("Failed to select Country " + country + ":" + e.toString());
        }
    }

    //common Method-Selection of a language
    public static void selectionOfLanguage_Android(String language) {
        createLog("Selecting Language " + language + " on login screen");
        try {
            sc.waitForElement("NATIVE", "xpath=//*[@id='login_language_value_txt']", 0, 30);
            click("NATIVE", "xpath=//*[@id='login_language_value_txt']", 0, 1);
            sc.syncElements(2000, 30000);

            switch (language.toLowerCase()) {
                case ("english"):
                    selectedLanguage = convertTextToUTF8("English");
                    break;
                case ("spanish"):
                    selectedLanguage =  convertTextToUTF8("Español");
                    break;
                case ("french"):
                    selectedLanguage = convertTextToUTF8("Français");
                    break;
                default:
                    throw new RuntimeException("Please provide valid country name from the list [english,spanish,french]");
            }

            if (strAppType.equalsIgnoreCase("toyota") || strAppType.equalsIgnoreCase("subaru"))
                click("NATIVE", "xpath=//*[@text='" + selectedLanguage + "']", 0, 1);
            else
                click("NATIVE", "xpath=//*[@text='" + selectedLanguage.toUpperCase() + "']", 0, 1);
            sc.syncElements(2000, 4000);
            if (sc.isElementFound("NATIVE", convertTextToUTF8("xpath=//*[@text='Verified Links' or @text='Liens vérifiés' or @text='Enlaces verificados']"))) {
                createLog("Handling verified links popup");
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(500, 1000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Okay'] | //*[contains(@text,'accord')]"))
                click("NATIVE", "xpath=//*[@text='Okay'] | //*[contains(@text,'accord')]", 0, 1);

            if (sc.isElementFound("NATIVE", "xpath=//*[@text='OK']"))
                sc.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);



        } catch (Exception e) {
            sc.report("Failed to select language " + selectedLanguage, false);
            createErrorLog("Failed to select language " + selectedLanguage + ":" + e.toString());
        }
    }
    /*
    Export report and artifacts
     */

    private static void writeToFile(String destination) {
        File dir = new File(System.getProperty("user.dir") + "//reports//");
        if (!dir.exists())
            dir.mkdirs();
        InputStream inputStream = responseInputStream.getRawBody();
        try {
            File file = new File(destination);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void quitDriver(String testID, String testName) {
        createLog("Quit initiated");
        try {
            createLog(testName + " Report URL: " + sc.generateReport(true));
            driver.quit();
        } catch (Exception e) {
            createLog("QUIT handled failed with Exception : " + e.getMessage());
        }
        createLog("Quit Completed");
//        createLog("GetAttachments Initiated");
//        String url = ConfigSingleton.configMap.get("urlBase") + "/reporter/api/" + testID + "/attachments";
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(Date.from(Instant.now()));
//        try {
//            responseInputStream = Unirest.get(url)
//                    //.basicAuth(user, password)
//                    .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey"))
//                    .header("content-type", "application/json")
//                    .asBinary();
//            String destination = System.getProperty("user.dir") + "//reports//" + String.format(testName + "-%1$tY%1$tm%1$td%1$tk%1$tS%1$tp", cal) + ".zip";
//            writeToFile(destination);
//        } catch (Exception e) {
//            createLog("GetAttachments handled failed with Exception");
//        }
//        createLog("GetAttachments Completed");
    }

    public static void waitForDevice(String deviceType) {
        HttpResponse<String> response;
        try {
            for (int i = 0; i < 30; i++) {
                if (blnImageTag) {
                    String strTag = "imageDevices";
                    createLog("waitForDevice - boolean image tag is set to true in test class - checking for device availability with device tag " + strTag + "");
                    String queryParam = "'" + deviceType + "'" + " and tag=" + "'" + strTag + "'";
                    String encodedQueryParam = URLEncoder.encode(queryParam, "UTF-8");
                    response = Unirest.get("https://tmna.experitest.com/api/v1/devices?query=@os=" + encodedQueryParam)
                            .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey")).asString();
                } else {
                    createLog("waitForDevice - boolean image tag is set to false in test class - checking for device availability without device tag");
                    response = Unirest.get("https://tmna.experitest.com/api/v1/devices?query=@os=" + "'" + deviceType + "'")
                            .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey")).asString();
                }
                createLog(response.toString());
                String strAvailable = JsonPath.read(response.getBody(), "$.data..displayStatus").toString();
                createLog(strAvailable);
                if (strAvailable.contains("Available")) {
                    createLog("Device Available at : " + i + " minute");
                    break;
                } else {
                    createLog("Waiting for " + i + " minute");
                    delay(60000);
                }
            }
        } catch (Exception e) {
            createErrorLog("Wait for device method threw error: " + e);
        }
    }

    public static String getAppVersion(String appName) throws UnirestException {
        String[] appNameArr = appName.split("=");
        appName = appNameArr[1];
        HttpResponse<String> response = Unirest.get("https://tmna.experitest.com/api/v1/applications?uniqueName=" + appName)
                .header("Authorization", "Bearer " + ConfigSingleton.configMap.get("accessKey"))
                .asString();

        String releaseVersion = JsonPath.read(response.getBody(), "$..releaseVersion").toString();
        String buildVersion = JsonPath.read(response.getBody(), "$..buildVersion").toString();
        String appVersion = releaseVersion + "(" + buildVersion + ")";
        appVersion = appVersion.replaceAll("[^0-9.()]", "");
        createLog("app version is: " + appVersion);
        return appVersion;
    }

    public static void clearS3Reports() {
        createLog("Device tag received" + System.getProperty("deviceTag"));
        deviceTag = System.getProperty("deviceTag").toLowerCase();
        // Creation of Buckets
        if (!s3client.doesBucketExistV2(deviceTag))
            s3client.createBucket(deviceTag);
        if (!s3client.doesBucketExistV2(deviceTag + "-reportbuckets3"))
            s3client.createBucket(deviceTag + "-reportbuckets3");

        ObjectListing listing = s3client.listObjects(deviceTag + "-reportbuckets3");
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        if (summaries.size() > 0) {
            for (int i = 0; i < summaries.size(); i++) {
                createLog(summaries.get(i).getKey());
                s3client.deleteObject(deviceTag + "-reportbuckets3", summaries.get(i).getKey());
            }
        }
        createLog("All reports Cleared");
    }

    //putting the XML file in the S3 bucket
    public static void invokeCTAXML(String ctaXML) {
        File F1 = new File(System.getProperty("user.dir") + "//cta//Tests//Test Cases//Smoke//" + ctaXML);
        createLog(F1.toString());
        s3client.putObject(
                deviceTag, ctaXML, new File(System.getProperty("user.dir") + "//cta//Tests//Test Cases//Smoke//" + ctaXML)
        );
        createLog(ctaXML + " placed in S3 bucket");
    }

    public static void generateCTAReport(int j) {
        // Check until report is found
        while (true) {
            ObjectListing listing = s3client.listObjects(deviceTag + "-reportbuckets3");
            if (listing.getObjectSummaries().size() > 0) {
                for (int i = 0; i < listing.getObjectSummaries().size(); i++) {
                    System.out.println(listing.getObjectSummaries().get(0).getKey().toString().toLowerCase());
                    File localFile = new File(System.getProperty("user.dir") + "//ctareports//CTAReport" + j + ".zip");
                    s3client.getObject(new GetObjectRequest(deviceTag + "-reportbuckets3", listing.getObjectSummaries().get(i).getKey()), localFile);
                    s3client.deleteObject(deviceTag + "-reportbuckets3", listing.getObjectSummaries().get(i).getKey());
                    if (listing.getObjectSummaries().get(0).getKey().toLowerCase().contains("pass")) {
                        createLogWithAttachment(listing.getObjectSummaries().get(0).getKey() + ": Test Passed", localFile);
                    } else
                        createErrorLogWithAttachment(listing.getObjectSummaries().get(0).getKey() + ": Test Failed", localFile);
                }
                break;
            }
            delay(10000);
        }
        createLog("CTAReport test Completed");
    }

    public static void updateCTAXML(String ctaXML, String valToUpdate, String _list1, String _list2) {
        String filePath = System.getProperty("user.dir") + "//cta//Tests//Test Cases//Smoke//" + ctaXML + ".xml";
        createLog(filePath);
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();

            // parse xml file and load into document
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            // update Element value

            updateElementValue(doc, valToUpdate, _list1, _list1);

            //writeXMLFile(doc);
            TransformerFactory tf = TransformerFactory.newInstance();

            Transformer transformer = tf.newTransformer();

            DOMSource src = new DOMSource(doc);

            StreamResult res = new StreamResult(new File(filePath));

            transformer.transform(src, res);

        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private static void updateElementValue(Document doc, String valToUpdate, String _list1, String _list2) {
        NodeList list1 = doc.getElementsByTagName(_list1); //"Text_to_Verify"
        NodeList list2 = doc.getElementsByTagName(_list2);  //"Select_List_Item_2"


        for (int i = 0; i < list1.getLength(); i++) {
            Node node1 = list1.item(i).getFirstChild();
            try {
                node1.setNodeValue(valToUpdate);
                createLog("XML Node1 Updated");
            } catch (Exception DOMException) {
            }
            for (int j = 0; j < list2.getLength(); j++) {
                Node node2 = list2.item(j).getFirstChild();
                createLog("XML Node2 Updated");
                try {
                    node2.setNodeValue(valToUpdate);
                } catch (Exception DOMException) {

                }
            }
        }
    }

    /*
    CTA specific - If brand = Lexus then strPackage= strPackageNameAppStoreLexus
                   If brand = Lexusappcenter then strPackage= appPackageProdLexus
     */
    public static void commonSetupCTA(String testName) throws Exception {
        //ConfigSingleton.INSTANCE.loadConfigProperties();

        if (System.getProperty("platform").equalsIgnoreCase("ios") && System.getProperty("brand").equalsIgnoreCase("lexus")) {
            strAppType = "lexus";
            ios_SetUpCTA(System.getProperty("CTAIP"), System.getProperty("CTAUDID"), ConfigSingleton.configMap.get("strPackageNameAppStoreLexus"), strAppType + " " + testName);
            createLog("Common setup Lexus  completed");//
        }
        if (System.getProperty("platform").equalsIgnoreCase("ios") && System.getProperty("brand").equalsIgnoreCase("toyota")) {
            strAppType = "toyota";
            ios_SetUpCTA(System.getProperty("CTAIP"), System.getProperty("CTAUDID"), ConfigSingleton.configMap.get("strPackageNameAppStoreToyota"), strAppType + " " + testName);
        }
        if (System.getProperty("platform").equalsIgnoreCase("android") && System.getProperty("brand").equalsIgnoreCase("lexus")) {
            strAppType = "lexus";
            android_SetupCTA(System.getProperty("CTAIP"), System.getProperty("CTAUDID"), ConfigSingleton.configMap.get("appPackageProdLexus"), strAppType + " " + testName);
        }
        if (System.getProperty("platform").equalsIgnoreCase("android") && System.getProperty("brand").equalsIgnoreCase("toyota")) {
            strAppType = "toyota";
            android_SetupCTA(System.getProperty("CTAIP"), System.getProperty("CTAUDID"), ConfigSingleton.configMap.get("appPackageProdToyota"), strAppType + " " + testName);
        }
        if (System.getProperty("platform").equalsIgnoreCase("ios") && System.getProperty("brand").equalsIgnoreCase("lexusappcenter")) {
            strAppType = "lexus";
            ios_SetUpCTA(System.getProperty("CTAIP"), System.getProperty("CTAUDID"), ConfigSingleton.configMap.get("strPackageNameLexus"), strAppType + " " + testName);
            createLog("Common setup Lexus  completed");//
        }
        if (System.getProperty("platform").equalsIgnoreCase("ios") && System.getProperty("brand").equalsIgnoreCase("toyotaappcenter")) {
            strAppType = "toyota";
            ios_SetUpCTA(System.getProperty("CTAIP"), System.getProperty("CTAUDID"), ConfigSingleton.configMap.get("strPackageNameToyota"), strAppType + " " + testName);
        }
        if (System.getProperty("platform").equalsIgnoreCase("android") && System.getProperty("brand").equalsIgnoreCase("lexus")) {
            strAppType = "lexus";
            android_SetupCTA(System.getProperty("CTAIP"), System.getProperty("CTAUDID"), ConfigSingleton.configMap.get("appPackageProdLexus"), strAppType + " " + testName);
        }
        if (System.getProperty("platform").equalsIgnoreCase("android") && System.getProperty("brand").equalsIgnoreCase("toyota")) {
            strAppType = "toyota";
            android_SetupCTA(System.getProperty("CTAIP"), System.getProperty("CTAUDID"), ConfigSingleton.configMap.get("appPackageProdToyota"), strAppType + " " + testName);
        }
        createLog("Common setup CTA completed");
    }

    public static void commonSetup(String testName) throws Exception {
        createLog("Common set up started");
        createLog("Current IP: " + InetAddress.getLocalHost() + "Computer Name is : " + InetAddress.getLocalHost().getHostName());
        ConfigSingleton.INSTANCE.loadConfigProperties();
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case ("toyota"):
                closeAppiumSessions();
                strAppType = "toyota";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameToyota"), strAppType + " " + testName);
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageProdToyota"), strAppType + " " + testName);
                break;
            case ("lexus"):
                closeAppiumSessions();
                strAppType = "lexus";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameLexus"), strAppType + " " + testName);
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageProdLexus"), strAppType + " " + testName);
                break;
            case ("toyotaappstore"):
                closeAppiumSessions();
                strAppType = "toyota";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameAppStoreToyota"), strAppType + " " + testName);
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageProdToyota"), strAppType + " " + testName);
                break;
            case ("lexusappstore"):
                closeAppiumSessions();
                strAppType = "lexus";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameAppStoreLexus"), strAppType + " " + testName);
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageProdLexus"), strAppType + " " + testName);
                break;
            case ("toyotastage"):
                closeAppiumSessions();
                strAppType = "toyota";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameToyotaStage"), strAppType + " " + testName);
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageStageToyota"), strAppType + " " + testName);
                break;
            case ("lexusstage"):
                closeAppiumSessions();
                strAppType = "lexus";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameLexusStage"), strAppType + " " + testName);
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageStageLexus"), strAppType + " " + testName);
                break;
            case (""):
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyota")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("ios"))) {
                    strAppType = "toyota";
                    iOS_Setup(ConfigSingleton.configMap.get("strPackageNameToyota"), strAppType + " " + testName, ConfigSingleton.configMap.get("cloudAppToyotaProd"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppToyotaProd");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexus")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("ios"))) {
                    strAppType = "lexus";
                    iOS_Setup(ConfigSingleton.configMap.get("strPackageNameLexus"), strAppType + " " + testName, ConfigSingleton.configMap.get("cloudAppLexusProd"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppLexusProd");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotaappstore")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("ios"))) {
                    strAppType = "toyota";
                    iOS_Setup(ConfigSingleton.configMap.get("strPackageNameAppStoreToyota"), strAppType + " " + testName, "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusappstore")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("ios"))) {
                    strAppType = "lexus";
                    iOS_Setup(ConfigSingleton.configMap.get("strPackageNameAppStoreLexus"), strAppType + " " + testName, "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotastage")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("ios"))) {
                    strAppType = "toyotastage";
                    iOS_Setup(ConfigSingleton.configMap.get("strPackageNameToyotaStage"), strAppType + " " + testName, ConfigSingleton.configMap.get("cloudAppStageToyotaIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageToyotaIOS");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusstage")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("ios"))) {
                    strAppType = "lexusstage";
                    iOS_Setup(ConfigSingleton.configMap.get("strPackageNameLexusStage"), strAppType + " " + testName, ConfigSingleton.configMap.get("cloudAppStageLexusIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageLexusIOS");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyota")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("android"))) {
                    strAppType = "toyota";
                    android_Setup(ConfigSingleton.configMap.get("appPackageProdToyota"), strAppType + " " + testName, ConfigSingleton.configMap.get("cloudAppToyotaAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppToyotaAndroid");
                }
                if ((System.getProperty("cloudApp").equals("lexus")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("android"))) {
                    strAppType = "lexus";
                    android_Setup(ConfigSingleton.configMap.get("appPackageProdLexus"), strAppType + " " + testName, ConfigSingleton.configMap.get("cloudAppLexusAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppLexusAndroid");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotaplaystore")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("android"))) {
                    strAppType = "toyota";
                    android_Setup(ConfigSingleton.configMap.get("appPackageProdToyota"), strAppType + " " + testName, "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusplaystore")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("android"))) {
                    strAppType = "lexus";
                    android_Setup(ConfigSingleton.configMap.get("appPackageProdLexus"), strAppType + " " + testName, "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotastage")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("android"))) {
                    strAppType = "toyota";
                    android_Setup(ConfigSingleton.configMap.get("appPackageStageToyota"), strAppType + " " + testName, ConfigSingleton.configMap.get("cloudAppStageToyotaAndroid"));
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusstage")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("android"))) {
                    strAppType = "lexus";
                    android_Setup(ConfigSingleton.configMap.get("appPackageStageLexus"), strAppType + " " + testName, ConfigSingleton.configMap.get("cloudAppStageLexusAndroid"));
                }
                break;
            default:
                createLog("Please either provide the value of local as Lexus or Toyota to run test on local or leave blank to run on cloud");
        }
        if (cloudAppName.equals(""))
            sc.report("Build Version" + ConfigSingleton.configMap.get("buildver"), true);
        else
            sc.report("Build Version: " + getAppVersion(cloudAppName), true);

        createLog("Common set up completed");
    }

    public static void commonSetupV2_5(String testName) throws Exception {
        createLog("Common set up started");
        createLog("Current IP: " + InetAddress.getLocalHost() + "Computer Name is : " + InetAddress.getLocalHost().getHostName());
        ConfigSingleton.INSTANCE.loadConfigProperties();
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case ("toyota"):
                closeAppiumSessions();
                strAppType = "toyota";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameToyota"), "");
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageProdToyota"), strAppType + " " + testName);
                break;
            case ("lexus"):
                closeAppiumSessions();
                strAppType = "lexus";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameLexus"), "");
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageProdLexus"), strAppType + " " + testName);
                break;
            case ("toyotaappstore"):
                closeAppiumSessions();
                strAppType = "toyota";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameAppStoreToyota"), "");
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageProdToyota"), strAppType + " " + testName);
                break;
            case ("lexusappstore"):
                closeAppiumSessions();
                strAppType = "lexus";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameAppStoreLexus"), "");
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageProdLexus"), strAppType + " " + testName);
                break;
            case ("toyotastage"):
                closeAppiumSessions();
                strAppType = "toyota";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameToyotaStage"), "");
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageStageToyota"), strAppType + " " + testName);
                break;
            case ("lexusstage"):
                closeAppiumSessions();
                strAppType = "lexus";
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("ios"))
                    iOS_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("strPackageNameLexusStage"), "");
                if (ConfigSingleton.configMap.get("platform").equalsIgnoreCase("android"))
                    android_Setup(ConfigSingleton.configMap.get("port"), ConfigSingleton.configMap.get("udID"), ConfigSingleton.configMap.get("appPackageStageLexus"), strAppType + " " + testName);
                break;
            case (""):
//                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyota")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
//                    strAppType = "toyota";
//                    iOS_Setup("",ConfigSingleton.configMap.get("strPackageNameToyota"), System.getProperty("deviceName"),ConfigSingleton.configMap.get("cloudAppToyotaProd"));
//                    cloudAppName = ConfigSingleton.configMap.get("cloudAppToyotaProd");
//                }
//                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexus")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
//                    strAppType = "lexus";
//                    iOS_Setup("",ConfigSingleton.configMap.get("strPackageNameLexus"), System.getProperty("deviceName"),ConfigSingleton.configMap.get("cloudAppLexusProd"));
//                    cloudAppName = ConfigSingleton.configMap.get("cloudAppLexusProd");
//                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotaappstore")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
                    strAppType = "toyota";
                    iOS_Setup("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameAppStoreToyota") , "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusappstore")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
                    strAppType = "lexus";
                    iOS_Setup("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameAppStoreLexus") , "");
                }
                if ((System.getProperty("cloudApp").contains("toyota")) && (System.getProperty("platform").equalsIgnoreCase("ios")) && ConfigSingleton.configMap.get("stageRun").equalsIgnoreCase("true")) {
                    strAppType = "toyotastage";
                    iOS_Setup("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameToyotaStage") , ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaIOS");
                }
                if ((System.getProperty("cloudApp").contains("lexus")) && (System.getProperty("platform").equalsIgnoreCase("ios")) && ConfigSingleton.configMap.get("stageRun").equalsIgnoreCase("true")) {
                    strAppType = "lexusstage";
                    iOS_Setup("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameLexusStage") , ConfigSingleton.configMap.get("cloudAppStageV2_5LexusIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5LexusIOS");
                }
//               && ConfigSingleton.configMap.get("stageRun").equalsIgnoreCase("false")
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexus")) && (System.getProperty("platform").equalsIgnoreCase("ios")) && ConfigSingleton.configMap.get("stageRun").equalsIgnoreCase("false"))  {
                    strAppType = "lexus";
                    iOS_Setup("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameLexus") , ConfigSingleton.configMap.get("cloudAppProdV2_5LexusIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5LexusIOS");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyota")) && (System.getProperty("platform").equalsIgnoreCase("ios")) && ConfigSingleton.configMap.get("stageRun").equalsIgnoreCase("false")) {
                    strAppType = "toyota";
                    iOS_Setup("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameToyota") , ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaIOS");
                }
//                && ConfigSingleton.configMap.get("stageRun").equalsIgnoreCase("false"))
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyota")) && (System.getProperty("platform").equalsIgnoreCase("android"))) {
                    strAppType = "toyota";
                    android_Setup(ConfigSingleton.configMap.get("appPackageProdToyota"),System.getProperty("deviceName") , ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaAndroid");
                }
//                && ConfigSingleton.configMap.get("stageRun").equalsIgnoreCase("false"))
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexus")) && (System.getProperty("platform").equalsIgnoreCase("android"))) {
                    strAppType = "lexus";
                    android_Setup(ConfigSingleton.configMap.get("appPackageProdLexus"), System.getProperty("deviceName"), ConfigSingleton.configMap.get("cloudAppProdV2_5LexusAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5LexusAndroid");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotaplaystore")) && (System.getProperty("platform").equalsIgnoreCase("android"))) {
                    strAppType = "toyota";
                    android_Setup(ConfigSingleton.configMap.get("appPackageProdToyota"), System.getProperty("deviceName"), "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusplaystore")) && (System.getProperty("platform").equalsIgnoreCase("android"))) {
                    strAppType = "lexus";
                    android_Setup(ConfigSingleton.configMap.get("appPackageProdLexus"), System.getProperty("deviceName"), "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotastage")) && (System.getProperty("platform").equalsIgnoreCase("android")) || ConfigSingleton.configMap.get("stageRun").equalsIgnoreCase("true") && (System.getProperty("platform").equalsIgnoreCase("android")) && (System.getProperty("cloudApp").equalsIgnoreCase("toyotastage"))) {
                    strAppType = "toyota";
                    android_Setup(ConfigSingleton.configMap.get("appPackageStageToyota"), System.getProperty("deviceName"), ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaAndroid");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusstage")) && (System.getProperty("platform").equalsIgnoreCase("android")) || ConfigSingleton.configMap.get("stageRun").equalsIgnoreCase("true") && (System.getProperty("platform").equalsIgnoreCase("android")) && (System.getProperty("cloudApp").equalsIgnoreCase("lexusstage"))) {
                    strAppType = "lexus";
                    android_Setup(ConfigSingleton.configMap.get("appPackageStageLexus"), System.getProperty("deviceName"), ConfigSingleton.configMap.get("cloudAppStageV2_5LexusAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5LexusAndroid");
                }
                break;
            default:
                createLog("Please either provide the value of local as Lexus or Toyota to run test on local or leave blank to run on cloud");
        }
        if (cloudAppName.equals(""))
            sc.report("Build Version" + ConfigSingleton.configMap.get("buildver"), true);
        else
            sc.report("Build Version: " + getAppVersion(cloudAppName), true);

        createLog("Common set up completed");
    }
    public static void UninstallV2_5(String testName) throws Exception {
        createLog("App uninstallation started");
        createLog("Current IP: " + InetAddress.getLocalHost() + "Computer Name is : " + InetAddress.getLocalHost().getHostName());
        ConfigSingleton.INSTANCE.loadConfigProperties();
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case (""):
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotaappstore")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
                    strAppType = "toyota";
                    iOS_Uninstall("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameAppStoreToyota") , "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusappstore")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
                    strAppType = "lexus";
                    iOS_Uninstall("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameAppStoreLexus") , "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotastage")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
                    strAppType = "toyotastage";
                    iOS_Uninstall("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameToyotaStage") , ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaIOS");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusstage")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
                    strAppType = "lexusstage";
                    iOS_Uninstall("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameLexusStage") , ConfigSingleton.configMap.get("cloudAppStageV2_5LexusIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5LexusIOS");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexus")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
                    strAppType = "lexus";
                    iOS_Uninstall("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameLexus") , ConfigSingleton.configMap.get("cloudAppProdV2_5LexusIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5LexusIOS");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyota")) && (System.getProperty("platform").equalsIgnoreCase("ios"))) {
                    strAppType = "toyota";
                    iOS_Uninstall("",System.getProperty("deviceName"),ConfigSingleton.configMap.get("strPackageNameToyota") , ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaIOS"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaIOS");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyota")) && (System.getProperty("platform").toLowerCase().equalsIgnoreCase("android"))) {
                    strAppType = "toyota";
                    android_Uninstall(ConfigSingleton.configMap.get("appPackageProdToyota"),System.getProperty("deviceName") , ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5ToyotaAndroid");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexus")) && (System.getProperty("platform").equalsIgnoreCase("android"))) {
                    strAppType = "lexus";
                    android_Uninstall(ConfigSingleton.configMap.get("appPackageProdLexus"), System.getProperty("deviceName"), ConfigSingleton.configMap.get("cloudAppProdV2_5LexusAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppProdV2_5LexusAndroid");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotaplaystore")) && (System.getProperty("platform").equalsIgnoreCase("android"))) {
                    strAppType = "toyota";
                    android_Uninstall(ConfigSingleton.configMap.get("appPackageProdToyota"), System.getProperty("deviceName"), "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusplaystore")) && (System.getProperty("platform").equalsIgnoreCase("android"))) {
                    strAppType = "lexus";
                    android_Uninstall(ConfigSingleton.configMap.get("appPackageProdLexus"), System.getProperty("deviceName"), "");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("toyotastage")) && (System.getProperty("platform").equalsIgnoreCase("android"))) {
                    strAppType = "toyota";
                    android_Uninstall(ConfigSingleton.configMap.get("appPackageStageToyota"), System.getProperty("deviceName"), ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5ToyotaAndroid");
                }
                if ((System.getProperty("cloudApp").equalsIgnoreCase("lexusstage")) && (System.getProperty("platform").equalsIgnoreCase("android"))) {
                    strAppType = "lexus";
                    android_Uninstall(ConfigSingleton.configMap.get("appPackageStageLexus"), System.getProperty("deviceName"), ConfigSingleton.configMap.get("cloudAppStageV2_5LexusAndroid"));
                    cloudAppName = ConfigSingleton.configMap.get("cloudAppStageV2_5LexusAndroid");
                }
                break;
            default:
                createLog("Please either provide the value of local as Lexus or Toyota to run test on local or leave blank to run on cloud");
        }
        if (cloudAppName.equals(""))
            sc.report("Build Version" + ConfigSingleton.configMap.get("buildver"), true);
        else
            sc.report("Build Version: " + getAppVersion(cloudAppName), true);

        createLog("Uninstall Apps from Devices completed");
    }

    public static void appLauncher() {
        switch (System.getProperty("cloudApp").toLowerCase()) {
            case ("lexusstageios"):
                createLog("Launching Lexus Stage V2.5 IOS app");
                sc.launch(ConfigSingleton.configMap.get("strPackageNameLexus"), false, true);
                break;
            case ("toyotastageios"):
                createLog("Launching Toyota Stage V2.5 IOS app");
                sc.launch(ConfigSingleton.configMap.get("strPackageNameToyota"), false, true);
                break;
            case ("lexusstageandroid"):
                createLog("Launching Lexus Stage V2.5 Android app");
                sc.launch(ConfigSingleton.configMap.get("appPackageStageLexus"), false, true);
                break;
            case ("toyotastageandroid"):
                createLog("Launching Toyota Stage V2.5 Android app");
                sc.launch(ConfigSingleton.configMap.get("appPackageStageToyota"), false, true);
                break;
            case ("lexusprodios"):
                createLog("Launching Lexus Prod V2.5 IOS app");
                sc.launch(ConfigSingleton.configMap.get("strPackageNameLexus"), false, true);
                break;
            case ("toyotaprodios"):
                createLog("Launching Toyota Prod V2.5 IOS app");
                sc.launch(ConfigSingleton.configMap.get("strPackageNameToyota"), false, true);
                break;
            case ("lexusprodandroid"):
                createLog("Launching Lexus Prod V2.5 Android app");
                sc.launch(ConfigSingleton.configMap.get("appPackageProdLexus"), false, true);
                break;
            case ("toyotaprodandroid"):
                createLog("Launching Toyota Prod V2.5 Android app");
                sc.launch(ConfigSingleton.configMap.get("appPackageProdToyota"), false, true);
                break;
        }

    }

    public static void exitAll(String testName) {
        blnImageTag = false;
        blnDeclinePopup = false;
        elementsDumpBundleId = "";
        strAppPackage = "";
        createLog("exitAll Initiated");
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case ("lexus"):
            case ("toyota"):
            case ("toyotastage"):
            case ("lexusstage"):
            case ("lexusappstore"):
            case ("toyotaappstore"):
                createLog(testName + " Report URL: " + sc.generateReport(true));
                delay(15000);
                break;
            case (""):
                createLog("Report URL : " + driver.getCapabilities().getCapability("reportUrl"));
                Long reportTestId = (Long) driver.getCapabilities().getCapability("reportTestId");
                try { driver.executeScript("mobile: terminateApp", java.util.Map.of("appId", strAppPackage)); } catch (Exception ignored) {}
                try { driver.executeScript("mobile: activateApp", java.util.Map.of("appId", strAppPackage)); } catch (Exception ignored) {}
                quitDriver(reportTestId.toString(), strAppType + " " + testName);
                break;
        }
    }

    public static String getTextiPhoneCamera() {
        sc.deviceAction("Home");
        sc.swipe("Up", sc.p2cx(70), 100);
        if (sc.isElementFound("NATIVE", "xpath=//*[@class='UIAView' and ./parent::*[@placeholder='Search']]"))
            sc.sendText("camera");
        click("NATIVE", "xpath=//*[@id='Camera']", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@accessibilityLabel='PhotoCapture']", 0, 1);
        click("NATIVE", "xpath=//*[@accessibilityLabel='GoToCameraRoll']", 0, 1);
        sc.syncElements(2000, 4000);
//        String getcode = sc.getText("TEXT");
//        String connectbycode = getcode.substring(getcode.lastIndexOf("mobile") + 1).substring(11, 21);
//        createLog(connectbycode);
        String _connectbycode = sc.elementGetProperty("NATIVE", "xpath=//*[@id='com.apple.visionkit.textElement'][3]", 0, "Value");
        createLog(_connectbycode);
//        if (connectbycode == _connectbycode)
//            sc.report("getText, Connect by code from the HU: " + connectbycode, true);
//        else
        sc.report("Value Attribute, Connect by code from the HU : " + _connectbycode, true);
        sc.deviceAction("Home");
        return _connectbycode;
    }

    public static void createLog(String strMessage) {
        logger.info(strMessage);
        ReportPortal.emitLog(strMessage, "info", new Date());
//        ReportPortal.emitLog(strMessage, "info", new Date(), ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE));
    }

    public static void createErrorLog(String strMessage) {
        logger.error(strMessage);
        String xml = sc.getVisualDump("NATIVE");

        createLog("Screenshot attachment for observed error");
        ReportPortal.emitLog(strMessage, "error", new Date(), ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE));

        try {
            File errorLogFile = new File(System.getProperty("user.dir") + "\\errorXML.txt");
            if (errorLogFile.exists()) {
                errorLogFile.delete();
            } else {
                errorLogFile.createNewFile();
            }
            FileWriter myWriter = new FileWriter(System.getProperty("user.dir") + "\\errorXML.txt");
            myWriter.write(xml);
            myWriter.close();
        } catch (Exception e) {
            createLog(String.valueOf(e));
        }
        ReportPortal.emitLog(strMessage, "error", new Date(), new File(System.getProperty("user.dir") + "\\errorXML.txt"));
        fail(strMessage);
    }

    public static void compareImage(String strScreenName, String strTestName) {
        try {
            String strRepoPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + sc.getDeviceProperty("device.os") + File.separator + sc.getDeviceProperty("device.sn") + File.separator + strAppType + File.separator + strTestName;
            Aeye aeye = new Aeye(strRepoPath, driver);
            boolean blnMatch = aeye.see(strScreenName).exclude(statusBarRectangle).compare();
            if (blnMatch)
                createLog("compareImage Passed Successfully - No Mismatch : " + strScreenName);
            else {
                ReportPortal.emitLog("Repository Path : " + strRepoPath, "error", new Date());
                ReportPortal.emitLog(strScreenName + " - Baseline : ", "error", new Date(), new File(strRepoPath + File.separator + "baseline" + File.separator + strScreenName + ".png"));
                ReportPortal.emitLog(strScreenName + " - Actual : ", "error", new Date(), new File(strRepoPath + File.separator + "actual" + File.separator + strScreenName + ".png"));
                ReportPortal.emitLog(strScreenName + " - Result : ", "error", new Date(), new File(strRepoPath + File.separator + "result" + File.separator + strScreenName + ".png"));
                createErrorLog("Mismatching screens!");
            }
        } catch (Exception e) {
            sc.report("Failure in compareImage : " + strScreenName, false);
            createErrorLog(strScreenName + ":" + e.toString());
        }
    }

    public static void compareImage(String strScreenName, String strTestName, boolean blnCompareSoftly) {
        try {
            String strRepoPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + sc.getDeviceProperty("device.os") + File.separator + sc.getDeviceProperty("device.sn") + File.separator + strAppType + File.separator + strTestName;
            Aeye aeye = new Aeye(strRepoPath, driver);
            if (aeye.see(strScreenName).exclude(statusBarRectangle).compare(blnCompareSoftly))
                createErrorLog("compareImage softly shows mismatch - Please check results : " + strScreenName);
            else
                createLog("compareImage Passed Successfully - No Mismatch : " + strScreenName);
        } catch (Exception e) {
            sc.report("Failure in compareImage : " + strScreenName, false);
            createErrorLog(strScreenName + ":" + e.toString());
        }
    }

    public static void compareImage(String strScreenName, String strTestName, Rectangle... rectangles) {
        try {
            String strRepoPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + sc.getDeviceProperty("device.os") + File.separator + sc.getDeviceProperty("device.sn") + File.separator + strAppType + File.separator + strTestName;
            Aeye aeye = new Aeye(strRepoPath, driver);
            boolean blnMatch = aeye.see(strScreenName).exclude(statusBarRectangle).exclude(rectangles).compare();
            if (blnMatch)
                createLog("compareImage Passed Successfully - No Mismatch : " + strScreenName);
            else {
                ReportPortal.emitLog("Repository Path : " + strRepoPath, "error", new Date());
                ReportPortal.emitLog(strScreenName + " - Baseline : ", "error", new Date(), new File(strRepoPath + File.separator + "baseline" + File.separator + strScreenName + ".png"));
                ReportPortal.emitLog(strScreenName + " - Actual : ", "error", new Date(), new File(strRepoPath + File.separator + "actual" + File.separator + strScreenName + ".png"));
                ReportPortal.emitLog(strScreenName + " - Result : ", "error", new Date(), new File(strRepoPath + File.separator + "result" + File.separator + strScreenName + ".png"));
                createErrorLog("Mismatching screens!");
            }
        } catch (Exception e) {
            sc.report("Failure in compareImage with Excluded Rectangles : " + strScreenName, false);
            createErrorLog(strScreenName + ":" + e.toString());
        }
    }

    public static void compareImageForElement(String strScreenNameObjectName, String strTestName, MobileElement elm) {
        try {
            String strRepoPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + sc.getDeviceProperty("device.os") + File.separator + sc.getDeviceProperty("device.sn") + File.separator + strAppType + File.separator + strTestName;
            Aeye aeye = new Aeye(strRepoPath, driver);
            boolean blnMatch = aeye.see(strScreenNameObjectName, elm).compare();
            if (blnMatch)
                createLog("compareImageForElement Passed Successfully - No Mismatch : " + strScreenNameObjectName);
            else {
                ReportPortal.emitLog("Repository Path : " + strRepoPath, "error", new Date());
                ReportPortal.emitLog(strScreenNameObjectName + " - Baseline : ", "error", new Date(), new File(strRepoPath + File.separator + "baseline" + File.separator + strScreenNameObjectName + ".png"));
                ReportPortal.emitLog(strScreenNameObjectName + " - Actual : ", "error", new Date(), new File(strRepoPath + File.separator + "actual" + File.separator + strScreenNameObjectName + ".png"));
                ReportPortal.emitLog(strScreenNameObjectName + " - Result : ", "error", new Date(), new File(strRepoPath + File.separator + "result" + File.separator + strScreenNameObjectName + ".png"));
                createErrorLog("Mismatching screens!");
            }
        } catch (Exception e) {
            sc.report("Failure in compareImageForElement : " + strScreenNameObjectName, false);
            createErrorLog(strScreenNameObjectName + ":" + e.toString());
        }
    }

    public static void deleteEmailEnv(String strEmail) throws Exception {
        ConfigSingleton.INSTANCE.loadConfigProperties();
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case ("toyotastage"):
                deleteEmail("stg", strEmail, ConfigSingleton.configMap.get("gatewayUsername"), ConfigSingleton.configMap.get("gatewayPassword"));
                break;
            case ("lexusstage"):
                deleteEmail("stg", strEmail, ConfigSingleton.configMap.get("gatewayUsername"), ConfigSingleton.configMap.get("gatewayPassword"));
                break;
            case ("toyota"):
                deleteEmail("prod", strEmail, ConfigSingleton.configMap.get("gatewayUsername"), ConfigSingleton.configMap.get("gatewayPassword"));
                break;
            case ("lexus"):
                deleteEmail("prod", strEmail, ConfigSingleton.configMap.get("gatewayUsername"), ConfigSingleton.configMap.get("gatewayPassword"));
                break;
            case (""):
                if ((System.getProperty("cloudApp").toLowerCase().equalsIgnoreCase("toyota"))) {
                    deleteEmail("prod", strEmail, ConfigSingleton.configMap.get("gatewayUsername"), ConfigSingleton.configMap.get("gatewayPassword"));
                }
                if ((System.getProperty("cloudApp").toLowerCase().equalsIgnoreCase("lexus"))) {
                    deleteEmail("prod", strEmail, ConfigSingleton.configMap.get("gatewayUsername"), ConfigSingleton.configMap.get("gatewayPassword"));
                }
                if ((System.getProperty("cloudApp").toLowerCase().equalsIgnoreCase("toyotastage"))) {
                    deleteEmail("stg", strEmail, ConfigSingleton.configMap.get("gatewayUsername"), ConfigSingleton.configMap.get("gatewayPassword"));
                }
                if ((System.getProperty("cloudApp").toLowerCase().equalsIgnoreCase("lexusstage"))) {
                    deleteEmail("stg", strEmail, ConfigSingleton.configMap.get("gatewayUsername"), ConfigSingleton.configMap.get("gatewayPassword"));
                }
        }
    }

    public static void deleteEmail(String env, String email, String gatewayUserName, String gatewayPassword) throws Exception {
        // Set environment and load configurations
        System.setProperty("env", env);
        System.setProperty("awsprodsecrets", "");
        System.clearProperty("awsprodsecrets");
        ConfigSingleton.INSTANCE.loadConfigProperties();
        ConfigSingleton.INSTANCE.loadConfigFiles(env);
        TokenGeneration tokenGen = new TokenGeneration();
        ConfigSingleton.loadGateWayInfo();

        //OnePortal - Fetch Account Details and then extract guild corresponding to the account
        GatewayInfoPojo gatewayDetAccountDetails = tokenGen.getGatewayInfo("ONEPORTAL", "US_DEALER");
        List<Header> headersListAccountDetails = APIkeywordsImpl.setStandardHeaders("ONEPORTAL", "US_DEALER", "OnePortal-US-TOY", gatewayDetAccountDetails);

        headersListAccountDetails.add(new Header("X-CHANNEL", gatewayDetAccountDetails.x_channel));
        headersListAccountDetails.add(new Header("Content-Type", "application/json"));
        headersListAccountDetails.add(new Header("X-BRAND", "T"));
        headersListAccountDetails.add(new Header("email", email)); //replace email with phone if you want to search by phone number

        String apiResponseAccountDetails = APIkeywordsImpl.callAPIGetMethod(gatewayDetAccountDetails.baseURI, "/v1/account", "application/json", headersListAccountDetails);
        System.out.println("AccountDetails Response = " + apiResponseAccountDetails);
        String guidForTheEmail = JsonPath.read(apiResponseAccountDetails, "payload.customer.guid").toString();
        System.out.println("Guid For the Account " + email + " is = " + guidForTheEmail);

        // Admin portal - Delete account based on email id
        GatewayInfoPojo gatewayDet = tokenGen.getGatewayInfo("ADMINPORTAL", "OP_ADMIN");

        List<Header> headersList = APIkeywordsImpl.setStandardHeaders("ADMINPORTAL", "OP_ADMIN", gatewayUserName, gatewayPassword, gatewayDet);
        headersList.add(new Header("guid", guidForTheEmail));
        System.out.println("HearList is : " + headersList);

        String jsonBody = APIkeywordsImpl.buildJsonBody("\\requests\\DeleteAccountV2.json", email);
        System.out.println("jsonBody is : " + jsonBody);

        String response = APIkeywordsImpl.callAPIDeleteMethodwithBody(gatewayDet.baseURI, "v2/account", "application/json", headersList, jsonBody);
        System.out.println("Account delete API Response is : " + response);

    }


    public static void RemoteAuthForGuestDriver(String env, String email, String vin) throws Exception {
        // Set environment and load configurations
        System.setProperty("env", env);
        System.setProperty("awsprodsecrets", "");
        System.clearProperty("awsprodsecrets");
        ConfigSingleton.INSTANCE.loadConfigProperties();
        ConfigSingleton.INSTANCE.loadConfigFiles(env);
        TokenGeneration tokenGen = new TokenGeneration();
        ConfigSingleton.loadGateWayInfo();

        //OnePortal - Fetch Account Details and then extract guild corresponding to the account
        GatewayInfoPojo gatewayDetAccountDetails = tokenGen.getGatewayInfo("ONEPORTAL", "US_DEALER");
        List<Header> headersListAccountDetails = APIkeywordsImpl.setStandardHeaders("ONEPORTAL", "US_DEALER", "OnePortal-US-TOY", gatewayDetAccountDetails);

        headersListAccountDetails.add(new Header("X-CHANNEL", gatewayDetAccountDetails.x_channel));
        headersListAccountDetails.add(new Header("Content-Type", "application/json"));
        headersListAccountDetails.add(new Header("X-BRAND", "T"));
        headersListAccountDetails.add(new Header("email", email)); //replace email with phone if you want to search by phone number

        String apiResponseAccountDetails = APIkeywordsImpl.callAPIGetMethod(gatewayDetAccountDetails.baseURI, "/v1/account", "application/json", headersListAccountDetails);
        System.out.println("AccountDetails Response = " + apiResponseAccountDetails);
        String guidForTheEmail = JsonPath.read(apiResponseAccountDetails, "payload.customer.guid").toString();
        System.out.println("Guid For the Account " + email + " is = " + guidForTheEmail);

        //OnePortal - Post Auth Code
        GatewayInfoPojo gatewayDetAuthCode = tokenGen.getGatewayInfo("ONEPORTAL", "US_DEALER");
        List<Header> headersListAuthCode = APIkeywordsImpl.setStandardHeaders("ONEPORTAL", "US_DEALER", "OnePortal-US-TOY", gatewayDetAuthCode);

        headersListAuthCode.add(new Header("x-brand", "T"));
        headersListAuthCode.set(2, new Header("x-channel", "US_DEALER"));
        System.out.println("AuthCode HearList is : " + headersListAuthCode);
        boolean sendNotification = true;

        String argStrAuthCode = vin + "|" + guidForTheEmail + "|" + sendNotification;
        String jsonBodyAuthCode = APIkeywordsImpl.buildJsonBody("\\requests\\AuthCode-OnePortal.json", argStrAuthCode);

        String responseAuthCode = APIkeywordsImpl.callAPIPostMethod(gatewayDetAuthCode.baseURI, "/authcode/v1/remote", "application/json", headersListAuthCode, null, jsonBodyAuthCode, null);
        System.out.println("AuthCode Response = " + responseAuthCode);

    }

    public static void ios_checkIsDashboardScreenDisplayed() {
        createLog("Checking dashboard screen is displayed");
        if (sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']")) {
            createLog("Dashboard screen is displayed...Trying to Sign out.");
            ios_emailSignOut();
        }
    }

    public static void android_checkIsDashboardScreenDisplayed() {
        createLog("Checking dashboard screen is displayed");
        if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Dashboard Vehicle Image']")) {
            createLog("Dashboard screen is displayed...Trying to Sign out.");
            android_SignOut();
        }
    }

    public static void closeAppiumSessions() throws UnirestException {
        if (System.getProperty("CTAIP") == null) {
            createLog("CATIP Is null. Please provide the CTAIP unless testing locally");
            if (!ConfigSingleton.configMap.get("local").isEmpty()) {
                String checkSessions = Unirest.get("http://localhost:" + ConfigSingleton.configMap.get("port") + "/wd/hub/sessions")
                        .asString()
                        .getBody();
                if (!checkSessions.equalsIgnoreCase("[]")) {
                    String currentSessions = checkSessions.substring(26, 62);
                    createLog(currentSessions);
                    Unirest.setTimeouts(0, 0);
                    HttpResponse<String> response = Unirest.delete("http://localhost:" + ConfigSingleton.configMap.get("port") + "/wd/hub/session/" + currentSessions)
                            .asString();
                }
            }
        } else if (System.getProperty("CTAIP").contains("desk")) {
            String checkSessions = Unirest.get(System.getProperty("CTAIP") + "/sessions")
                    .asString()
                    .getBody();
            if (!checkSessions.equalsIgnoreCase("[]")) {
                String currentSessions = checkSessions.substring(26, 62);
                createLog(currentSessions);
                Unirest.setTimeouts(0, 0);
                HttpResponse<String> response = Unirest.delete(System.getProperty("CTAIP") + "/session/" + currentSessions)
                        .asString();
            }
        }
    }

    public static void android_declineHandlePages() {
        for (int i = 0; i < 3; i++) {
            sc.syncElements(2000, 4000);
            /*if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);*/
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='btn_later']"))
                click("NATIVE", "xpath=//*[@id='btn_later']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'access this device’s location')]"))
                sc.click("NATIVE", "xpath=//*[@text='Don’t allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@contentDescription='Skip For Now' or @contentDescription='Skip for Now']")) {
                click("NATIVE", "xpath=//*[@contentDescription='Skip For Now' or @contentDescription='Skip for Now']", 0, 1);
                sc.syncElements(5000, 20000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'access this device’s location')]"))
                sc.click("NATIVE", "xpath=//*[@text='Don’t allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='alertTitle' and @id='button1']")) {
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Navigate')]")) {
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(10000, 20000);
            }
            /*if (sc.isElementFound("NATIVE", "xpath=//*[contains(@content-desc,'Navigate')]"))
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Back']"))
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));*/
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'determine the relative position of nearby devices?')]"))
                sc.click("NATIVE", "xpath=//*[@text='Don’t allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@contentDescription='Skip For Now' or @contentDescription='Skip for Now']")) {
                click("NATIVE", "xpath=//*[@contentDescription='Skip For Now' or @contentDescription='Skip for Now']", 0, 1);
                sc.syncElements(5000, 20000);
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'access this device’s location')]"))
                sc.click("NATIVE", "xpath=//*[@text='Don’t allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Find a Station']"))
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Announcements']"))
                sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Dashboard Vehicle Image']"))
                break;
            sc.syncElements(2000, 4000);
        }
    }

    public static void Android_declineHandlePopups() {
        createLog("Declining pop up");
        for (int i = 0; i < 3; i++) {
            sc.syncElements(2000, 4000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@content-desc='Dashboard Vehicle Image']"))
                break;
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='btn_later']"))
                click("NATIVE", "xpath=//*[@id='btn_later']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='alertTitle' and (@text='Verified Links' or @text='Enlaces verificados' or @text='Liens vérifiés')]")) {
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            }
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='button1']"))
                click("NATIVE", "xpath=//*[@id='button1']", 0, 1);
            sc.syncElements(2000, 4000);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@text,'access this device’s location')]"))
                sc.click("NATIVE", "xpath=//*[@text='Don’t allow']", 0, 1);
            // Skip For Now
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']"))
                click("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Tap on the account icon to explore the Take a Tour option.']"))
                click("NATIVE", "xpath=//*[@text='OK'] | //*[@text='Ok']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='login_login_btn']"))
                break;
        }
    }

    public static void ios_declineHandlePopups() {
        for (int i = 0; i < 2; i++) {
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
                break;
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@label,'Would Like to Use Bluetooth')])[1]"))
                sc.click("NATIVE", "xpath=//*[@label='Don’t Allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@label,'Would Like to Send You Notifications')])[1]"))
                sc.click("NATIVE", "xpath=//*[@label='Don’t Allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@label,'Would Like to Use Bluetooth')])[1]"))
                sc.click("NATIVE", "xpath=//*[@label='Don’t Allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@label,'use your location')])[1]"))
                sc.click("NATIVE", "xpath=//*[@label='Don’t Allow']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Later' or @id='LATER']"))
                sc.click("NATIVE", "xpath=//*[@id='Later' or @id='LATER']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@id,'Try Face ID')]"))
                click("NATIVE", "xpath=//*[@id='Cancel']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='OK']"))
                sc.click("NATIVE", "xpath=//*[@id='OK']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Allow' or @id='Allow Once']"))
                sc.click("NATIVE", "xpath=//*[@id='Allow' or @id='Allow Once']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']"))
                break;
        }
    }

    public static void ios_declineHandlePages() {
        for (int i = 0; i <= 1; i++) {
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Later' or @id='LATER']"))
                sc.click("NATIVE", "xpath=//*[@id='Later' or @id='LATER']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Cancel']"))
                sc.click("", "//*[@id='Cancel']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@value='1']"))
                sc.click("NATIVE", "xpath=//*[@value='1']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='CONTINUE' or @id='Continue' or @id='"+(convertTextToUTF8("CONTINÚAR")+"']")))
                sc.click("NATIVE", "xpath=//*[@id='CONTINUE' or @id='Continue' or @id='"+convertTextToUTF8("CONTINÚAR")+"']", 0, 1);
            //Language
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Yes, change to English']"))
                click("NATIVE", "xpath=//*[@id='Yes, change to English']", 0, 1);
            sc.syncElements(5000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@label,'use your location')])[1]"))
                sc.click("NATIVE", "xpath=//*[@label='Don’t Allow']", 0, 1);
            // Skip For Now
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']"))
                click("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@text='Tap on the account icon to explore the Take a Tour option.']"))
                click("NATIVE", "xpath=//*[@text='OK'] | //*[@text='Ok']", 0, 1);
            sc.syncElements(5000, 30000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Announcements']"))
                sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
                break;
        }
    }

    public static void android_chromeAcceptTerms() {
        //chrome terms accept screen
        createLog("android - chrome browser accept term screen handle started");
        if (sc.isElementFound("NATIVE", "xpath=//*[@text='Welcome to Chrome']")) {
            click("NATIVE", "xpath=//*[@id='terms_accept']", 0, 1);
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='negative_button']"))
                click("NATIVE", "xpath=//*[@id='negative_button']", 0, 1);
        }
        createLog("android - chrome browser accept term screen handle completed");
    }

    public static void ios_declinePreSetup() {
        createLog("ios - decline pre condition handle started");
        sc.deviceAction("Home");
        sc.swipe("Up", sc.p2cx(70), 100);
        verifyElementFound("NATIVE", "xpath=//*[@class='UIAView' and ./parent::*[@placeholder='Search']]", 0);
        sc.sendText("Settings");
        verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Settings']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='Settings']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Settings' and @class='UIAStaticText']", 0);
        sc.swipe("Up", sc.p2cx(70), 100);
        click("NATIVE", "xpath=//*[@id='NOTIFICATIONS_ID']", 0, 1);
        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Notifications' and @class='UIAStaticText']", 0);

        if (strAppType.equalsIgnoreCase("toyota"))
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@label='Toyota' and @class='UIAStaticText']", 0, 1000, 5, true);
        else
            sc.swipeWhileNotFound("Down", sc.p2cy(70), 2000, "NATIVE", "xpath=//*[@label='Lexus' and @class='UIAStaticText']", 0, 1000, 5, true);

        sc.syncElements(5000, 30000);
        verifyElementFound("NATIVE", "xpath=//*[@label='Allow Notifications' and @class='UIASwitch']", 0);
        String notificationSwitchVal = sc.elementGetProperty("NATIVE", "xpath=//*[@label='Allow Notifications' and @class='UIASwitch']", 0, "value");
        if (notificationSwitchVal.equalsIgnoreCase("1")) {
            createLog("Allow notifications is enabled - disabling notifications");
            click("NATIVE", "xpath=//*[@label='Allow Notifications' and @class='UIASwitch']", 0, 1);
        } else {
            createLog("Allow notifications already disabled");
        }
        sc.syncElements(5000, 30000);
        sc.verifyElementNotFound("NATIVE", "xpath=//*[@accessibilityLabel='ALERTS']", 0);
        reLaunchApp_iOS();
        createLog("ios - decline pre condition handle completed");
    }

    //For CTA HU Interactions
    public static void createLogWithAttachment(String strMessage, File file) {
        logger.info(strMessage);
        ReportPortal.emitLog(strMessage, "info", new Date(), file);
    }

    public static void createErrorLogWithAttachment(String strMessage, File file) {
        logger.error(strMessage);
        ReportPortal.emitLog(strMessage, "error", new Date(), file);
        fail(strMessage);
    }

    //For CTA Mobile Interactions
    public static void createLogWithScreenshot(String strMessage) {
        if (driver != null) {
            logger.info(strMessage);
            ReportPortal.emitLog(strMessage, "info", new Date(), ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE));
        }
    }

    public static boolean turnOnNotifications() {
        boolean isTurnOnNotificationFound=false;
        sc.syncElements(10000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@contentDescription='Turn On'] | //*[@text='Turn On']")) {
            isTurnOnNotificationFound=true;
            click("NATIVE", "xpath=//*[@contentDescription='Turn On'] | //*[@text='Turn On']", 0, 1);
            sc.syncElements(2000, 4000);
            if(sc.isElementFound("NATIVE","xpath=//*[@text='Blocked']",0)) {
                click("NATIVE", "xpath=//*[@text='Blocked']", 0, 1);
                sc.syncElements(2000, 4000);
            }
            click("NATIVE", "xpath=//*[@id='switch_widget']", 0, 1);
            sc.syncElements(2000, 4000);
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            reLaunchApp_android();
            sc.syncElements(10000, 20000);
        }
        return isTurnOnNotificationFound;

    }

    public static void checkIsLoginScreenDisplayed_IOS() {
        createLog("Checking for login screen");
        sc.syncElements(5000, 60000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='LOGIN_BUTTON_SIGNIN']")) {
            createLog("Login page ready");
        } else {
            createLog("Login page was not Ready ... Signing out from the app");
            createLog("Started - iOS SignOut");
            sc.syncElements(5000, 10000);

            if(!sc.isElementFound("NATIVE","xpath=//*[@id='person' or @id='user_profile_button']")){
                reLaunchApp_iOS();
                if(sc.isElementFound("NATIVE","xpath=//*[@text='Skip For Now' or @text='Skip for Now']")){
                    click("NATIVE", "xpath=//*[@text='Skip For Now' or @text='Skip for Now']", 0, 1);
                    if (sc.isElementFound("NATIVE", "xpath=//*[@text='Tap on the account icon to explore the Take a Tour option.']")) {
                        click("NATIVE", "xpath=//*[@text='OK'] | //*[@text='Ok']", 0, 1);
                    }
                    sc.syncElements(5000, 30000);
                }
            }
            sc.waitForElement("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0, 30);
            click("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0, 1);
            sc.syncElements(2000, 5000);

            //Sign out
            click("NATIVE", "xpath=//*[@text='Sign Out' or @text='"+convertTextToUTF8("Se déconnecter")+"' or @text='Desconectar']", 0, 1);
            sc.syncElements(2000, 5000);

            click("NATIVE", "xpath=//*[@text='Sign Out' or @text='"+convertTextToUTF8("Se déconnecter")+"' or @text='Desconectar'][2]", 0, 1);
            sc.syncElements(5000, 10000);

            sc.waitForElement("NATIVE","xpath=//*[@id='LOGIN_BUTTON_COUNTRY']",0,10);
            //Verify Login screen displayed
            verifyElementFound("NATIVE","xpath=//*[@id='LOGIN_BUTTON_COUNTRY']",0);
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='LOGIN_BUTTON_SIGNIN']", 0);
        createLog("Verified Login screen is displayed");
    }

    public static void checkIsLoginScreenDisplayed_android() {
        createLog("Checking for login screen");
        sc.syncElements(5000, 60000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='login_login_btn']")) {
            createLog("Login page ready");
        } else {
            createLog("Login page was not Ready ... Signing out from the app");
            if(!(sc.isElementFound("NATIVE","xpath=//*[@resource-id='top_nav_profile_icon']"))) {
                reLaunchApp_android();
            }
            if(sc.isElementFound("NATIVE", "xpath=//*[@id='login_login_btn']")) {
                createLog("Login Page is Ready");
            }
            else if(sc.isElementFound("NATIVE","xpath=//*[@resource-id='top_nav_profile_icon']")) {
                createLog("Started - Android SignOut");
                click("NATIVE", "xpath=//*[@resource-id='top_nav_profile_icon']", 0, 1);
                sc.syncElements(2000, 5000);

                //Sign out
                click("NATIVE", "xpath=//*[@text='Sign Out' or @text='" + convertTextToUTF8("Se déconnecter") + "' or @text='Desconectar']", 0, 1);
                sc.syncElements(2000, 5000);

                click("NATIVE", "xpath=//*[(@text='Sign Out' or @text='" + convertTextToUTF8("Se déconnecter") + "' or @text='Desconectar')]//following-sibling::*[@class='android.widget.Button']", 0, 1);
                sc.syncElements(5000, 10000);

                sc.waitForElement("NATIVE", "xpath=//*[@id='login_region_value_txt']", 0, 10);
                //Verify Login screen displayed
                verifyElementFound("NATIVE", "xpath=//*[@id='login_region_value_txt']", 0);
            }
        }
        verifyElementFound("NATIVE", "xpath=//*[@id='login_login_btn']", 0);
        createLog("Verified Login screen is displayed");
    }

    public static void android_translationsKeepMeSignedIn(boolean blnKeepMeLoggedIn) {
        if (blnKeepMeLoggedIn) {
            if (!sc.isElementFound("NATIVE", "xpath=//*[@id='login_keep_login_txt' and @checked='true']"))
                click("NATIVE", "xpath=//*[@id='login_keep_login_txt']", 0, 1);
        } else {
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='login_keep_login_txt' and @checked='true']"))
                click("NATIVE", "xpath=//*[@id='login_keep_login_txt']", 0, 1);
        }
    }

    public static void ios_translationsKeepMeSignedIn(boolean blnKeepMeLoggedIn) {
        if (blnKeepMeLoggedIn) {
            if (sc.isElementFound("NATIVE", "xpath=//*[contains(@value,'unselected')]"))
                click("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_KEEP_ME_LOGGED_IN']", 0, 1);
            createLog("Keep Logged in Selected");
        } else {
            if (!sc.isElementFound("NATIVE", "xpath=//*[contains(@value,'unselected')]"))
                click("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_KEEP_ME_LOGGED_IN']", 0, 1);
            createLog("Keep Logged in Not Selected");
        }
    }

    public static void clearPNS() {
        sc.deviceAction("Home");
        createLog("Started - Clearing PNS");
        sc.swipe("Up", 50, 2000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='NotificationShortLookView']")){
            click("NATIVE","xpath=//*[@accessibilityLabel='NotificationShortLookView']",0,1);
            if(sc.isElementFound("NATIVE","xpath=//*[@text='x']")){
                click("NATIVE", "xpath=//*[@text='x']", 0, 1);
                click("NATIVE", "xpath=//*[@id='clear-button']", 0, 1);
                createLog("Cleared PNS");
            }
        }
        sc.deviceAction("Home");
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Search']")){
            click("NATIVE","xpath=//*[@id='Search']",0,1);
            if(Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
                sendText("NATIVE", "xpath=//*[@id='SpotlightSearchField']", 0, System.getProperty("cloudApp").substring(0, 4));
                if(System.getProperty("cloudApp").toLowerCase().contains("lexus")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Lexus']")){
                        createLog("Launching Lexus app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Lexus']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(System.getProperty("cloudApp").toLowerCase().contains("toyota")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Toyota']")){
                        createLog("Launching Toyota app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Toyota']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(System.getProperty("cloudApp").toLowerCase().contains("subaru")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']")){
                        createLog("Launching Subaru app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='SUBARU CONNECT']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
            }
            else{
                sendText("NATIVE", "xpath=//*[@id='SpotlightSearchField']", 0, ConfigSingleton.configMap.get("local"));
                if(ConfigSingleton.configMap.get("local").toLowerCase().contains("lexus")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Lexus']")){
                        createLog("Launching Lexus app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Lexus']",0,1);
                    }
                    else{
                        createLog(ConfigSingleton.configMap.get("local")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(ConfigSingleton.configMap.get("local").toLowerCase().contains("toyota")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Toyota']")){
                        createLog("Launching Toyota app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Toyota']",0,1);
                    }
                    else{
                        createLog(ConfigSingleton.configMap.get("local")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(System.getProperty("local").toLowerCase().contains("subaru")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']")){
                        createLog("Launching Subaru app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='SUBARU CONNECT']",0,1);
                    }
                    else{
                        createLog(ConfigSingleton.configMap.get("local")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
            }
        }
//        while (sc.isElementFound("NATIVE", "xpath=//*[@id='NotificationCell']")) {
//            if (sc.isElementFound("NATIVE", "xpath=//*[@id='NotificationCell']")) {
//                sc.flickElement("NATIVE", "xpath=//*[@id='NotificationCell']", 0, "Left");
//                delay(4000);
//                click("NATIVE", "xpath=//*[@label='Clear All' or @label='Clear']", 0, 1);
//            }
//        }
        createLog("Completed - Clearing PNS");
    }

    public static void _PNS(String command) {
        
        createLog("PNS verification in progress...");
        sc.deviceAction("Home");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Do you want to continue to allow background location use') or contains(@id,'Always Allow')])[1]")) {
            click("NATIVE", "xpath=//*[contains(@id,'Always Allow')]", 0, 1);
        }
        sc.swipe("Up", 50, 2000);
        sc.syncElements(2000, 5000);
        switch (command) {
            case ("Lock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The vehicle is now locked') or contains(@value,'already locked')])[1]", 0);
                break;
            case ("Unlock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The vehicle is now unlocked') or contains(@value,'already unlocked')])[1]", 0);
                break;
            case ("Start"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'Vehicle was started and will automatically shut off in 10 minutes')])[1]", 0);
                break;
            case ("Stop"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'Stop Request was successful')])[1]", 0);
                break;
            case ("Trunk Unlock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The remote trunk/hatch unlock was successful')])[1]", 0);
                break;
            case ("Trunk Lock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The remote trunk/hatch lock was successful')])[1]", 0);
                break;
            case ("Horn"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The remote horn request was successful')])[1]", 0);
                break;
            case ("StartAfterUnlock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'Remote command failed. Please ensure the vehicle is not in use, secured (doors, trunk and hood are closed and locked), and keyfob was not left inside your vehicle.')])[1]", 0);
                break;
            case ("EVStart"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'Vehicle was started and will automatically shut off in 20 minutes')])[1]", 0);
                break;
        }
//        
    }
    public static void PNS(String command) {
        createLog("PNS verification in progress...");
        sc.deviceAction("Home");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Do you want to continue to allow background location use') or contains(@id,'Always Allow')])[1]")) {
            click("NATIVE", "xpath=//*[contains(@id,'Always Allow')]", 0, 1);
        }
        sc.swipe("Up", 50, 2000);
        sc.syncElements(2000, 5000);
        switch (command) {
            case ("Lock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The vehicle is now locked') or contains(@value,'already locked') or contains(@value,'Your request could not be completed because a keyfob was detected in your vehicle') or contains(@value,'Please ensure your vehicle is located in an area with cellular network coverage')])[1]", 0);
                break;
            case ("Unlock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The vehicle is now unlocked') or contains(@value,'already unlocked') or contains(@value,'Please ensure your vehicle is located in an area with cellular network coverage')])[1]", 0);
                break;
            case ("Start"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'Vehicle was started and will automatically shut off') or contains(@value,'minute cycles without an occupant') or contains(@value,'keyfob was not left inside your vehicle') or contains(@value,'Please ensure your vehicle is located in an area with cellular network coverage')])[1]", 0);
                break;
            case ("Stop"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'Stop Request was successful') or contains(@value,'Please ensure your vehicle is located in an area with cellular network coverage')])[1]", 0);
                break;
            case ("Trunk Unlock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The remote trunk/hatch unlock was successful') or contains(@value,'Please ensure your vehicle is located in an area with cellular network coverage')])[1]", 0);
                break;
            case ("Trunk Lock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The remote trunk/hatch lock was successful') or contains(@value,'Your request could not be completed because a keyfob was detected in your vehicle') or contains(@value,'Please ensure your vehicle is located in an area with cellular network coverage')])[1]", 0);
                break;
            case ("Horn"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'The remote horn request was successful') or contains(@value,'Please ensure your vehicle is located in an area with cellular network coverage')])[1]", 0);
                break;
            case ("StartAfterUnlock"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'Remote command failed. Please ensure the vehicle is not in use, secured (doors, trunk and hood are closed and locked), and keyfob was not left inside your vehicle.')  or contains(@value,'Please ensure your vehicle is located in an area with cellular network coverage')])[1]", 0);
                break;
            case ("EVStart"):
                verifyElementFound("NATIVE", "xpath=(//*[@id='NotificationCell' and contains(@value,'Vehicle was started and will automatically shut off in 20 minutes') or contains(@value,'Please ensure your vehicle is located in an area with cellular network coverage')])[1]", 0);
                break;
        }
        sc.deviceAction("Home");
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Search']")){
            click("NATIVE","xpath=//*[@id='Search']",0,1);
            if(Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
                sendText("NATIVE", "xpath=//*[@id='SpotlightSearchField']", 0, System.getProperty("cloudApp").substring(0, 4));
                if(System.getProperty("cloudApp").toLowerCase().contains("lexus")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Lexus']")){
                        createLog("Launching Lexus app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Lexus']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(System.getProperty("cloudApp").toLowerCase().contains("toyota")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Toyota']")){
                        createLog("Launching Toyota app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Toyota']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(System.getProperty("cloudApp").toLowerCase().contains("subaru")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']")){
                        createLog("Launching Subaru app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='SUBARU CONNECT']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
            }
            else{
                sendText("NATIVE", "xpath=//*[@id='SpotlightSearchField']", 0, ConfigSingleton.configMap.get("local"));
                if(ConfigSingleton.configMap.get("local").toLowerCase().contains("lexus")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Lexus']")){
                        createLog("Launching Lexus app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Lexus']",0,1);
                    }
                    else{
                        createLog(ConfigSingleton.configMap.get("local")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(ConfigSingleton.configMap.get("local").toLowerCase().contains("toyota")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Toyota']")){
                        createLog("Launching Toyota app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Toyota']",0,1);
                    }
                    else{
                        createLog(ConfigSingleton.configMap.get("local")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(System.getProperty("local").toLowerCase().contains("subaru")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']")){
                        createLog("Launching Subaru app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='SUBARU CONNECT']",0,1);
                    }
                    else{
                        createLog(ConfigSingleton.configMap.get("local")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
            }
        }
    }

    public void ios_emailLoginFrench(String strEmail, String strPassword) {
        sc.startStepsGroup("Email Login French");
        createLog("Started: Email Login French");
        sc.syncElements(2000, 5000);
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'accord') or contains(@label,'Ok')]")) {
            click("NATIVE", "xpath=//*[contains(@label,'accord') or contains(@label,'Ok')]", 0, 1);
            sc.syncElements(2000, 5000);
        }
        click("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']", 0, 1);
        sc.syncElements(10000, 20000);

        createLog("Enter username and continue");
        sc.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0, 20);
        sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0, strEmail);
        sc.syncElements(2000, 5000);
        String email = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_CONTINUE_BUTTON']", 0, 1);
        sc.syncElements(10000, 20000);
        sc.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD'']", 0, 20);
        //Enter Password
        createLog("Enter password");
        sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD']", 0, strPassword);
        sc.syncElements(2000, 5000);
        //Continue
        createLog("Sign in");
        sc.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']", 0, 20);
        click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']", 0, 1);
        sc.syncElements(20000, 40000);

        //enter activation code if OTP screen displayed
        createLog("enter activation code if OTP screen displayed");
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_TITLELABEL']")) {
            String activationCode = new CTMailClient().getActivationCode(email);
            sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_INPUT_TEXTFIELD']", 0, activationCode);
            click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON']", 0, 1);
            sc.syncElements(2000, 5000);
        }

        //Handle Allow While Using App popup
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Allow While Using App']"))
            click("NATIVE", "xpath=//*[@id='Allow While Using App']", 0, 1);
        sc.syncElements(5000, 30000);

        // Handle preferred language if displayed
        createLog("Handle preferred language if displayed");
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@id='Langue préférée']"))) {
            verifyElementFound("NATIVE", convertTextToUTF8("//*[contains(@id,'La langue que vous avez sélectionnée diffère de la langue')]"), 0);
            sc.syncElements(5000, 10000);
        }
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@id='Oui, changer pour le français']"))) {
            click("NATIVE", convertTextToUTF8("//*[@id='Oui, changer pour le français']"), 0, 1);
            sc.syncElements(5000, 10000);
        }

        //Handle Skip Now if displayed
        createLog("Handle Skip Now if displayed");
        if (sc.isElementFound("NATIVE", convertTextToUTF8("//*[@text='Ignorer pour l’instant']"))) {
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Ignorer pour l’instant']"), 0);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Voir les nouveautés']"), 0);
            click("NATIVE", convertTextToUTF8("//*[@text='Ignorer pour l’instant']"), 0, 1);
            sc.syncElements(5000, 10000);
            verifyElementFound("NATIVE", convertTextToUTF8("//*[@text='Ignorer pour l’instant']"), 0);
            click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
            sc.syncElements(5000, 10000);
        }
        //Handle Announcement popup if displayed
        createLog("Handle Announcement popup if displayed");
        for (int i = 1; i <= 2; i++) {
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Annonces']"))
                sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            sc.syncElements(5000, 10000);
        }
        sc.startStepsGroup("Email Login French");
        createLog("Complete: Email Login French");

    }

    public void ios_emailLoginSpanish(String strEmail, String strPassword) {
        sc.startStepsGroup("Email Login Spanish");
        createLog("Started:Email Login Spanish");
        if (sc.isElementFound("NATIVE", "xpath=//*[contains(@label,'accord') or contains(@label,'Ok')]"))
            click("NATIVE", "xpath=//*[contains(@label,'accord') or contains(@label,'Ok')]", 0, 1);

        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']")) {
            click("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']", 0, 1);
            sc.syncElements(5000, 10000);
        }
        //Enter credentials and otp
        createLog("Enter username and continue");
        sc.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0, 20);
        sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0, strEmail);
        sc.syncElements(2000, 5000);
        String email = sc.elementGetText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_USERNAME_TEXTFIELD']", 0);
        click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_SIGNIN_CONTINUE_BUTTON']", 0, 1);
        sc.syncElements(10000, 20000);
        sc.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD'']", 0, 20);
        //Enter Password
        createLog("Enter password");
        sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_INPUT_TEXTFIELD']", 0, strPassword);
        sc.syncElements(2000, 5000);
        //Continue
        createLog("Sign in");
        sc.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']", 0, 20);
        click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_ENTER_PASSWORD_SIGN_IN_BUTTON']", 0, 1);
        sc.syncElements(20000, 40000);

        //enter activation code if OTP screen displayed
        createLog("enter activation code if OTP screen displayed");
        if (sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_TITLELABEL']")) {
            String activationCode = new CTMailClient().getActivationCode(email);
            sendText("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_INPUT_TEXTFIELD']", 0, activationCode);
            click("NATIVE", "xpath=//*[@accessibilityLabel='FR_NATIVE_OTP_VERIFY_ACCOUNT_BUTTON']", 0, 1);
            sc.syncElements(2000, 5000);
        }

        //Handle Allow While Using App popup
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Allow While Using App']"))
            click("NATIVE", "xpath=//*[@id='Allow While Using App']", 0, 1);
        sc.syncElements(5000, 30000);

        // Handle preferred language if displayed
        createLog("Handle preferred language if displayed");
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Idioma preferido']")) {
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'su perfil para recibir todas las comunicaciones en Español?')]", 0);
            click("NATIVE", "xpath=//*[@id='Si, cámbielo a Español']", 0, 1);
        }
        //Handle Skip Now if displayed
        createLog("Handle Skip Now if displayed");
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='Omitir por ahora']")) {
            verifyElementFound("NATIVE", "xpath=//*[@id='Omitir por ahora']", 0);
            verifyElementFound("NATIVE", "xpath=//*[@id='Ver qué hay de nuevo']", 0);
            click("NATIVE", "xpath=//*[@id='Omitir por ahora']", 0, 1);
        }
        //Handle Announcement popup if displayed
        createLog("Handle Announcement popup if displayed");
        for (int i = 1; i <= 2; i++) {
            if (sc.isElementFound("NATIVE", "xpath=//*[@id='Anuncios']"))
                sc.clickCoordinate(sc.p2cx(50), sc.p2cy(20), 1);
            sc.syncElements(5000, 30000);
        }
        //Handle Turn On Bluetooth popup if displayed
        createLog("Handle Turn On Bluetooth popup if displayed");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'Turn On Bluetooth to Allow')])[1]")) {
            sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
            sc.syncElements(5000, 30000);
        }
        sc.startStepsGroup("Email Login Spanish");
        createLog("Ended :Email Login Spanish");

    }
    public static void android_handleBrowserNavigation() {
        //browser navigation handle
        createLog("android - browser navigation handle started");
        sc.syncElements(10000, 30000);
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Allow access to your location?']") || sc.isElementFound("NATIVE","xpath=//*[contains(@text,'wants to use your device') and contains(@text,'location')]"))
            click("NATIVE", "xpath=//*[@text='Allow']", 0, 1);
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'access this device') and contains(@text,'location')]"))
            click("NATIVE", "xpath=//*[@id='permission_allow_foreground_only_button']", 0, 1);
        if(sc.isElementFound("NATIVE","xpath=//*[@text='Download file again?']"))
            click("NATIVE", "xpath=//*[@text='Cancel']", 0, 1);
        createLog("android - browser navigation handle completed");
    }

    /*
    Method used to go to the notifications screen from Advanced Remote screen
     */
    public static void android_goToNotificationsScreen() {
        createLog("Started navigation to Notifications screen");

        if (sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Are you enjoying')]")) {
            enjoyingAppAlertAndroid();
        }

        //pull down advanced remote section if its displayed
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']"))
            click("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, 1);
        sc.syncElements(3000, 15000);

        //account icon attributes not exposed correctly - required to update xpath once fixed
        verifyElementFound("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(3000, 15000);
        sc.waitForElement("NATIVE", "xpath=//*[@text='Account']", 0, 3000);
        click("NATIVE", "xpath=//*[@text='Notifications']", 0, 1);
        sc.syncElements(4000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_message']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Notifications' or @text='NOTIFICATIONS']", 0);
        createLog("Completed navigating to Notifications screen");
    }
    public static void android_goToAccountsPage(){
        createLog("Started navigation to Account screen");
        //pull down advanced remote section if its displayed
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']"))
            click("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, 1);
        sc.syncElements(3000, 15000);

        //account icon attributes not exposed correctly - required to update xpath once fixed
        verifyElementFound("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0);
        click("NATIVE", "xpath=//*[@id='top_nav_profile_icon']", 0, 1);
        sc.syncElements(3000, 15000);
        sc.waitForElement("NATIVE", "xpath=//*[@text='Account']", 0, 3000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.syncElements(4000, 20000);
        createLog("Completed navigation to Account screen");
    }

    /*
    Method used to go to the Advanced Remote screen from notifications screen
     */
    public static void android_goToRemoteCommandsScreen() {
        createLog("Started navigation to Advanced Remote Commands screen");
        //click back button in notifications screen
        click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        sc.syncElements(3000, 6000);
        sc.flickElement("NATIVE", "xpath=//*[@contentDescription='Drag']", 0, "Down");
        //((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        sc.syncElements(2000, 4000);
        verifyElementFound("NATIVE", "xpath=//*[@text='Info']", 0);
//        sc.waitForElement("NATIVE", "xpath=//*[@resource-id='dashboard_remote_open_iconbutton']", 0, 3000);
//        click("NATIVE", "xpath=//*[@resource-id='dashboard_remote_open_iconbutton']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[@text='Unlock']", 0);
        createLog("Completed navigating to Advanced Remote Commands screen");
    }
    public static void android_PNS(String command) {
        createLog("PNS verification in progress...");
        sc.deviceAction("Home");
        sc.syncElements(2000, 4000);
        sc.swipe("Up",20,500);
        for(int i=1; i<4; i++){
            if(sc.isElementFound("NATIVE", "xpath=//*[@id='quickcontrol_title']"))
                break;
            else {
                sc.swipe("Up", 20, 500);
                sc.syncElements(2000, 4000);
            }
        }
        switch (command) {
            case ("Lock"):
                verifyElementFound("NATIVE", "xpath=//*[contains(@id,'text') and (contains(@text,'The vehicle is now locked') or contains(@text,'already locked') or contains(@text,'keyfob was detected in your vehicle')) ]", 0);
                break;
            case ("Unlock"):
                verifyElementFound("NATIVE", "xpath=//*[contains(@id,'text') and (contains(@text,'The vehicle is now unlocked') or contains(@text,'multiple doors unlocked') or contains(@text,'Doors are already unlocked'))]", 0);
                break;
            case ("Start"):
                verifyElementFound("NATIVE", "xpath=//*[contains(@id,'text') and (contains(@text,'started and will automatically shut off in 10 minutes') or contains(@text,'keyfob was not left inside your vehicle') or contains(@text,'minute cycles without an occupant'))]", 0);
                break;
            case ("Stop"):
                verifyElementFound("NATIVE", "xpath=//*[contains(@id,'text') and (contains(@text,'stop request was successful') or contains(@text,'Stop Request was successful') or contains(@text,'keyfob was not left inside your vehicle'))]", 0);
                break;
            case ("Horn"):
                verifyElementFound("NATIVE", "xpath=//*[contains(@id,'text') and (contains(@text,'horn request was successful'))]", 0);
                break;
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='clear_all' and @enabled='true']")) {
            createLog("clear button is displayed and enabled - clearing existing push notifications from device");
            click("NATIVE", "xpath=//*[@id='clear_all' and @enabled='true']", 0, 1);
            delay(2000);
            createLog("Completed - Clearing PNS");
        } else {
            createLog("clear button is displayed and disabled");
        }
        sc.deviceAction("Home");
        reLaunchApp_android();
    }

    //Method to get the elements dump from only on-screen - ios.dump.focus.process
    public static void setOnScreenElementsDump() {
        createLog("Starting - Set property(ios.dump.focus.process) for getting only on-screen elements dump");
        createLog("package name : "+ strAppPackage);
        elementsDumpBundleId = "bundle_id:"+strAppPackage+"";
        createLog("elementsDumpBundleId : "+ elementsDumpBundleId);
        sc.setProperty("ios.dump.focus.process", elementsDumpBundleId);
        createLog("Completed - Set property(ios.dump.focus.process) for getting only on-screen elements dump");
    }

    // for alerts, popups - reset elements dump property ios.dump.focus.process to null
    public static void resetOnScreenElementsDump() {
        createLog("Starting - resetting property(ios.dump.focus.process=null) on-screen elements dump - to handle alerts");
        createLog("package name : "+ strAppPackage);
        elementsDumpBundleId = "";
        createLog("elementsDumpBundleId : "+ elementsDumpBundleId);
        sc.setProperty("ios.dump.focus.process", elementsDumpBundleId);
        createLog("Completed - resetting property(ios.dump.focus.process=null) on-screen elements dump - to handle alerts");
    }

    public static void clearTextFieldData(String xpathStr) {
        createLog("Started - Clearing text field data for field : "+ xpathStr);
        driver.findElement(By.xpath(xpathStr)).clear();
        createLog("Completed - Clearing text field data for field : "+ xpathStr);
    }

    /*
    Method used to go to the notifications screen from Advanced Remote screen
     */
    public static void iOS_goToNotificationsScreen() {
        createLog("Started navigation to Notifications screen");
        sc.syncElements(25000,75000);
        //Check Enjoying App is displayed
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@value,'Are you enjoying')]",0)) {
            enjoyingAppAlert_iOS();
        }
        //pull down advanced remote section if its displayed
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        }
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_LaunchApp();
            sc.syncElements(3000, 15000);
        }
        //click on account icon
        verifyElementFound("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0);
        click("NATIVE", "xpath=//*[@id='person' or @id='user_profile_button']", 0, 1);
        sc.syncElements(3000, 15000);
        sc.waitForElement("NATIVE", "xpath=//*[@text='Account']", 0, 3000);
        click("NATIVE", "xpath=//*[@text='Notifications' and @class='UIAStaticText']", 0, 1);
        sc.syncElements(4000, 20000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='permission_message']"))
            click("NATIVE", "xpath=//*[@id='permission_allow_button']", 0, 1);
        verifyElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']", 0);

        //applying notification filter
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@id,'notification filter')]")) {
            createLog("Applying notification filter - Remote Commands");
            click("NATIVE", "xpath=//*[contains(@id,'notification filter')]", 0, 1);
            sc.waitForElement("NATIVE", "xpath=//*[contains(@id,'Filter by') or contains(@id,'FILTER BY')]", 0,3000);
            verifyElementFound("NATIVE", "xpath=//*[contains(@id,'Remote Commands')]", 0);
            click("NATIVE", "xpath=//*[contains(@id,'Remote Commands')]", 0, 1);
            sc.waitForElement("NATIVE", "xpath=//*[@text='Notifications' or @text='NOTIFICATIONS']", 0,3000);
            verifyElementFound("NATIVE", "xpath=//*[@text='Notifications' or @text='NOTIFICATIONS']", 0);
            createLog("Applied notification filter - Remote Commands");
        }

        createLog("Completed navigating to Notifications screen");
    }
    public static void iOS_goToAccountsPage() {
        createLog("Started navigation to Accounts screen");
        //pull down advanced remote section if its displayed
        
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']"))
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
        sc.syncElements(3000, 15000);
        //click on account icon
        verifyElementFound("NATIVE", "xpath=//*[@id='person']", 0);
        click("NATIVE", "xpath=//*[@id='person']", 0, 1);
        sc.syncElements(3000, 15000);
        sc.waitForElement("NATIVE", "xpath=//*[@text='Account']", 0, 3000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.syncElements(4000,8000);
        createLog("Completed navigating to Accounts screen");
    }

    /*
    Method to verify remote command notification
     */
    public static void remoteCommandNotificationVerification(String notificationText, String reportText) {
        createLog("Started verification for ||Remote - "+reportText+"|| notification is Displayed");
        sc.waitForElement("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0, 10);
        String firstNotification = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"value");
        String firstNotificationDate = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"id").split("notification received")[1].trim();
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") |  firstNotificationDate.contains("minutes ago");
        if (firstNotification.contains(notificationText) && dateCondition) {
            sc.report("Remote "+reportText+"  Notification Displayed", true);
            createLog("Remote "+reportText+"  Notification Displayed");
            createLog("Notification displayed is: "+firstNotification);
        } else {
            sc.report("Remote "+reportText+"  Notification not found", false);
            createLog("Notification displayed is: "+firstNotification);
            createErrorLog("Remote "+reportText+"  Notification not found");
        }
    }
    /*
   Method to verify remote command notification
    */
    public static void remoteCommandNotificationVerification(String notificationText1,String notificationText2,String reportText){
        createLog("Started verification for ||Remote - "+reportText+"|| notification is Displayed");
        sc.waitForElement("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0, 10);
        String firstNotification = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"value");
        String firstNotificationDate = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"id").split("notification received")[1].trim();
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") |  firstNotificationDate.contains("minutes ago");
        if ((firstNotification.contains(notificationText1) | firstNotification.contains(notificationText2)) && dateCondition) {
            sc.report("Remote "+reportText+"  Notification Displayed", true);
            createLog("Remote "+reportText+"  Notification Displayed");
            createLog("Notification displayed is: "+firstNotification);
        } else {
            sc.report("Remote "+reportText+"  Notification not found", false);
            createLog("Notification displayed is: "+firstNotification);
            createErrorLog("Remote "+reportText+"  Notification not found");
        }
    }

    /*
   Method to get element attribute value
   added because sc.elementGetProperty will not work for few fields
    */
    public static String getElementAttributeValue(String xpathStr, String attribute) {
        createLog("Started - Getting element attribute value for xpath:"+ xpathStr +"and attribute: "+attribute);
        MobileElement element = (MobileElement) driver.findElement(MobileBy.xpath(xpathStr));
        String attributeValue = element.getAttribute(attribute);
        createLog("Element attribute value is : "+ attributeValue);
        createLog("Completed - Getting element attribute value for xpath:"+ xpathStr +"and attribute: "+attribute);
        return attributeValue;
    }

    //IOS - Set Location Permission
    public static void setLocationPermission(){
        createLog("Starting :  set location permission from settings");
        sc.closeAllApplications();
        sc.deviceAction("Home");
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@id,'Always Allow')]")){
            click("NATIVE","xpath=//*[contains(@id,'Always Allow')]",0,1);
            createLog("Before the set location could start, Location Permission pop up appeared and handled");
        }
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Settings']",0))
            click("NATIVE","xpath=//*[@id='Settings']",0,1);
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='APPLE_ACCOUNT']")){
            //bluetooth screen displayed
            click("NATIVE","xpath=//*[@label='Settings' and @class='UIAButton']",0,1);
        }
        sc.flickElement("NATIVE","xpath=//*[@label='Airplane Mode']",0,"Down");
        if(ConfigSingleton.configMap.get("local").toLowerCase().contains("lexus")){
            sendText("NATIVE","xpath=//*[@id='Search']",0,"lexus");
        }
        else if(ConfigSingleton.configMap.get("local").toLowerCase().contains("toyota")){
            sendText("NATIVE","xpath=//*[@id='Search']",0,"toyota");
        }
        if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("")) {
            if (System.getProperty("cloudApp").toLowerCase().contains("lexus")) {
                searchForAppBrandAndSetLocationIOS("Lexus");
            } else if (System.getProperty("cloudApp").toLowerCase().contains("toyota")) {
                searchForAppBrandAndSetLocationIOS("Toyota");
            }
        } else {
            if (ConfigSingleton.configMap.get("local").toLowerCase().contains("lexus")) {
                searchForAppBrandAndSetLocationIOS("Lexus");
            } else if (ConfigSingleton.configMap.get("local").toLowerCase().contains("toyota")) {
                searchForAppBrandAndSetLocationIOS("Toyota");
            }
        }
        createLog("Completed :  set location permission from settings");
    }

    public static void navigateBackToSearch(){
        for(int i=1; i<5;i++){
           if(sc.isElementFound("NATIVE","xpath=//*[contains(@text,'Results')]")){
               break;
           }else {
               ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                sc.syncElements(1000, 2000);
           }
        }
    }
    public static void searchForAppBrandAndSetLocation(String appBrand){
        sc.sendText(appBrand);
        // sendText("NATIVE", "xpath=//*[@id='Search']", 0, "Lexus");
        if (sc.getElementCount("NATIVE", "xpath=//*[@text='"+appBrand+"' and @id='title']") > 1) {
            for (int i = 1; i <= 2; i++) {
                click("NATIVE", "xpath=(//*[@text='"+appBrand+"' and @id='title'])[" + i + "]", 0, 1);
                sc.syncElements(2000, 4000);
                click("NATIVE", "xpath=//*[@text='Permissions']", 0, 1);
                if (!(sc.isElementFound("NATIVE", "xpath=//*[@text='Location']"))) {
                    sc.swipe("Down", sc.p2cy(20), 5000);
                }
                click("NATIVE", "xpath=//*[@text='Location']", 0, 1);
                click("NATIVE", "xpath=//*[@text='Allow all the time']", 0, 1);
                navigateBackToSearch();
            }
        } else {
            click("NATIVE", "xpath=//*[@text='"+appBrand+"' and @id='title']", 0, 1);
            sc.syncElements(2000, 4000);
            click("NATIVE", "xpath=//*[@text='Permissions']", 0, 1);
            if (!(sc.isElementFound("NATIVE", "xpath=//*[@text='Location']"))) {
                sc.swipe("Down", sc.p2cy(20), 5000);
            }
            click("NATIVE", "xpath=//*[@text='Location']", 0, 1);
            click("NATIVE", "xpath=//*[@text='Allow all the time']", 0, 1);
            click("NATIVE", "xpath=//*[@class='android.widget.ImageButton']", 0, 1);
        }
        sc.closeAllApplications();
        sc.deviceAction("Home");
    }

    public static void setLocationPermissionAndroid() {
        sc.closeAllApplications();
        sc.deviceAction("Home");
        for (int i = 1; i <= 2; i++) {
            sc.swipe("Down", sc.p2cx(40), 100);
            sc.syncElements(2000, 4000);
            if (sc.isElementFound("NATIVE", "xpath=//*[@class='android.widget.TextView' and contains(@text,'Search')]")) {
                break;
            }
        }
        click("NATIVE", "xpath=//*[@class='android.widget.TextView' and contains(@text,'Search')]", 0, 1);
        //sendText("NATIVE","xpath=//*[@class='android.widget.TextView' and contains(@text,'Search')]",0,"Settings");
        sc.sendText("settings");
        sc.syncElements(2000, 4000);
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
        sc.syncElements(2000, 4000);

        click("NATIVE", "xpath=//*[(@id='app_label' or @id='label') and @text='Settings']", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@contentDescription='Search settings'] | //*[@contentDescription='Search']", 0, 1);
        sc.syncElements(2000, 4000);
        click("NATIVE", "xpath=//*[@id='search_src_text']", 0, 1);

        if (ConfigSingleton.configMap.get("local").equalsIgnoreCase("")) {
            if (System.getProperty("cloudApp").toLowerCase().contains("lexus")) {
                searchForAppBrandAndSetLocation("Lexus");
            } else if (System.getProperty("cloudApp").toLowerCase().contains("toyota")) {
                searchForAppBrandAndSetLocation("Toyota");
            }
            } else {
                if (ConfigSingleton.configMap.get("local").toLowerCase().contains("lexus")) {
                    searchForAppBrandAndSetLocation("Lexus");
                } else if (ConfigSingleton.configMap.get("local").toLowerCase().contains("toyota")) {
                    searchForAppBrandAndSetLocation("Toyota");
                }
        }
    }

    public static void searchForAppBrandAndSetLocationIOS(String appBrand) {
        createLog("Starting : Search app and set location for : "+appBrand+" ");
        sendText("NATIVE", "xpath=//*[@id='Search']", 0, appBrand);
        sc.syncElements(2000, 4000);
        createLog("app count in search result is : " +sc.getElementCount("NATIVE", "xpath=(//*[@id='Search results']//following-sibling::*[@id='"+appBrand+"' and @XCElementType='XCUIElementTypeCell'])"));
        if (sc.getElementCount("NATIVE", "xpath=(//*[@id='Search results']//following-sibling::*[@id='"+appBrand+"' and @XCElementType='XCUIElementTypeCell'])") > 1) {
            createLog("2 "+appBrand+" apps found");
            for (int i = 1; i <= 2; i++) {
                //click app brand from search result
                click("NATIVE", "xpath=(//*[@id='Search results']//following-sibling::*[@id='"+appBrand+"' and @XCElementType='XCUIElementTypeCell'])[" + i + "]", 0, 1);
                sc.syncElements(2000, 4000);
                if(sc.isElementFound("NATIVE","xpath=//*[@id='Location']")) {
                    click("NATIVE", "xpath=//*[@id='Location']", 0, 1);
                    sc.syncElements(2000, 4000);
                    if(sc.isElementFound("NATIVE","xpath=//*[@id='LOCATION_SERVICES_AUTH_ALWAYS']")) {
                        click("NATIVE", "xpath=//*[@id='LOCATION_SERVICES_AUTH_ALWAYS']", 0, 1);
                    }
                    sc.syncElements(2000, 4000);
                    //navigate back to search app brand screen
                    //navigating back from app brand location screen
                    click("NATIVE","xpath=//*[@id='"+appBrand+"']",0,1);
                    sc.syncElements(2000, 4000);
                    //navigating back from app brand screen
                    click("NATIVE","xpath=//*[@id='Settings' and @class='UIAButton']",0,1);
                } else {
                    //navigating back from app brand screen
                    click("NATIVE","xpath=//*[@id='Settings' and @class='UIAButton']",0,1);
                }
            }
        } else {
            createLog("only one "+appBrand+" app found");
            //click app brand from search result
            click("NATIVE", "xpath=(//*[@id='Search results']//following-sibling::*[@id='"+appBrand+"' and @XCElementType='XCUIElementTypeCell'])[1]", 0, 1);
            sc.syncElements(2000, 4000);
            if(sc.isElementFound("NATIVE","xpath=//*[@id='Location']")) {
                click("NATIVE", "xpath=//*[@id='Location']", 0, 1);
                sc.syncElements(2000, 4000);
                if(sc.isElementFound("NATIVE","xpath=//*[@id='LOCATION_SERVICES_AUTH_ALWAYS']")) {
                    click("NATIVE", "xpath=//*[@id='LOCATION_SERVICES_AUTH_ALWAYS']", 0, 1);
                }
            }
        }
        sc.closeAllApplications();
        sc.deviceAction("Home");
        createLog("Completed : Search app and set location for : "+appBrand+" ");
    }

    public static void clearPNS_android() {
        createLog("Started - Clearing PNS");
        sc.deviceAction("Home");
        sc.syncElements(2000, 4000);
        sc.swipe("Up",20,500);
        delay(5000);
        if (sc.isElementFound("NATIVE", "xpath=//*[@id='clear_all' and @enabled='true']")) {
            createLog("clear button is displayed and enabled - clearing existing push notifications from device");
            click("NATIVE", "xpath=//*[@id='clear_all' and @enabled='true']", 0, 1);
            delay(2000);
            createLog("Completed - Clearing PNS");
        } else {
            createLog("clear button is displayed and disabled");
        }
    }

    public static void environmentSelection_iOS(String environment) {
        createLog("Started - Environment Selection - if stage app");
        if(sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='LOGIN_LABEL_VERSION']")) {
            createLog("Login screen environment selection element is displayed, selecting environment: "+environment);
            sc.report("Login screen environment selection element is displayed, selecting environment: "+environment+"", true);
            String appVersion = sc.elementGetText("NATIVE","xpath=//*[@accessibilityLabel='LOGIN_LABEL_VERSION']",0);
            createLog("App Version is: "+appVersion);
            switch (environment) {
                case "stage" :
                    createLog("Changing app to stage environment");
                    sc.longClick("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_LABEL_VERSION']", 0, 1, 0, 0);
                    delay(2000);
                    click("NATIVE", "xpath=//*[@text='Staging']", 0, 1);
                    delay(2000);
                    click("NATIVE", "xpath=//*[@text='Restart']", 0, 1);
                    delay(2000);
                    reLaunchAppAfterEnvChange_iOS();
                    break;
                case "prod" :
                    createLog("Changing app to prod environment");
                    sc.longClick("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_LABEL_VERSION']", 0, 1, 0, 0);
                    delay(2000);
                    click("NATIVE", "xpath=//*[@text='Production']", 0, 1);
                    delay(2000);
                    click("NATIVE", "xpath=//*[@text='Restart']", 0, 1);
                    delay(2000);
                    reLaunchAppAfterEnvChange_iOS();
                    break;
                default:
                    createLog("Please provide valid environment (stage or prod)");
            }
            appVersion = sc.elementGetText("NATIVE","xpath=//*[@accessibilityLabel='LOGIN_LABEL_VERSION']",0);
            createLog("App Version after selection and relaunch: "+appVersion);
        } else {
            createLog("Login screen environment selection element is not displayed");
        }
        createLog("Completed - Environment Selection - if stage app");
    }

    public static void reLaunchAppAfterEnvChange_iOS() {
        createLog("Re launching App after environment change");
        String appPackage = "";
        ConfigSingleton.INSTANCE.loadConfigProperties();
        switch (ConfigSingleton.configMap.get("local").toLowerCase()) {
            case ("toyota"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
                break;
            case ("lexus"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
                break;
            case ("toyotaappstore"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreToyota");
                break;
            case ("lexusappstore"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreLexus");
                break;
            case ("toyotastage"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                break;
            case ("lexusstage"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
                break;
            case ("subarustage"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameSubaruStage");
                break;
            case ("subaru"):
                appPackage = ConfigSingleton.configMap.get("strPackageNameSubaru");
                break;
            case (""):
                if (System.getProperty("cloudApp").equalsIgnoreCase("toyota")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameToyota");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("lexus")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameLexus");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("toyotaappstore")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreToyota");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("subaru")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameSubaru");
                    break;
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("lexusappstore")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameAppStoreLexus");
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("toyotastage")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameToyotaStage");
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("lexusstage")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameLexusStage");
                }
                if (System.getProperty("cloudApp").equalsIgnoreCase("subarustage")) {
                    appPackage = ConfigSingleton.configMap.get("strPackageNameSubaruStage");
                }
                break;
            default:
                createLog("Kill and Relaunch failed");
        }
        sc.closeAllApplications();
        sc.launch(appPackage, false, false);
        sc.syncElements(5000, 30000);
//        sc.setProperty("element.visibility.level", "full");
        sc.syncElements(5000, 30000);
        ios_handlepopups();
        if(!sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']",0)){
            sc.launch(appPackage, false, false);
        }
        sc.syncElements(2000, 10000);
        if (!sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='LOGIN_BUTTON_SIGNIN']")) {
            createErrorLog("Login screen is not displayed on launching app - expected to display Login screen");
        }
    }

    public static void turnOnNotifications_iOS(){
        createLog("Notifications Disabled - Turn On notifications is displayed in the dashboard screen");
        createLog("Started : Turn on notifications");
        click("NATIVE", "xpath=//*[@label='Turn On']", 0, 1);
        delay(2000);
        click("NATIVE", "xpath=//*[@text='Notifications, Off']", 0, 1);
        delay(2000);
        verifyElementFound("NATIVE","xpath=//*[@accessibilityLabel='ALLOW_NOTIFICATIONS_ID']",0);
        click("NATIVE", "xpath=//*[@class='UIASwitch']", 0, 1);
        delay(2000);
        verifyElementFound("NATIVE","xpath=//*[@text='ALERTS' and ./parent::*[@text='ALERTS']]",0);
        reLaunchApp_iOS();
        sc.verifyElementNotFound("NATIVE","xpath=//*[@label='Turn On']",0);
        createLog("Completed : Turn on notifications");
    }

    public static void trustApp_iOS(){
        createLog("Untrusted alert is displayed");
        sc.syncElements(2000,4000);
        for(int i=0;i<=2;i++) {
            if(sc.isElementFound("NATIVE", "xpath=//*[@id='Cancel']")) {
                click("NATIVE", "xpath=//*[@id='Cancel']", 0, 1);
            }
        }
        createLog("Starting : Trust App");
        sc.closeAllApplications();
        sc.deviceAction("Home");
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Settings']",0))
            click("NATIVE","xpath=//*[@id='Settings']",0,1);
        sc.flickElement("NATIVE","xpath=//*[@label='Airplane Mode']",0,"Down");
        sendText("NATIVE","xpath=//*[@id='Search']",0,"Device Management");
        delay(1000);
        verifyElementFound("NATIVE","xpath=//*[@value='VPN & Device Management']",0);
        click("NATIVE","xpath=//*[@value='VPN & Device Management']",0,1);
        delay(1000);
        verifyElementFound("NATIVE","xpath=//*[@value='VPN & Device Management' and @class='UIANavigationBar']",0);
        verifyElementFound("NATIVE","xpath=//*[contains(@value,'Toyota Motor Sales')]",0);
        click("NATIVE","xpath=//*[contains(@value,'Toyota Motor Sales')]",0,1);
        verifyElementFound("NATIVE","xpath=//*[starts-with(@id,'Trust')]",0);
        click("NATIVE","xpath=//*[starts-with(@id,'Trust')]",0,1);
        verifyElementFound("NATIVE","xpath=//*[@id='Trust']",0);
        click("NATIVE","xpath=//*[@id='Trust']",0,1);
        verifyElementFound("NATIVE","xpath=//*[@id='Delete App' or @id='Delete Apps']",0);
        sc.closeAllApplications();
        createLog("Completed : Trust App");
    }

    public static void enjoyingAppAlert_iOS() {
        createLog("Enjoying App alert is displayed");
        createLog("Started : Handling Enjoying App alert");
        verifyElementFound("NATIVE","xpath=//*[contains(@value,'Are you enjoying')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Yes' and @class='UIAButton']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='No' and @class='UIAButton']",0);
        //Yes
        click("NATIVE","xpath=//*[@id='Yes' and @class='UIAButton']",0,1);
        delay(1000);
        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Tap a star to rate it on the App') and contains(@id,'Store')]",0);
        verifyElementFound("NATIVE","xpath=//*[@id='Not Now']",0);
        click("NATIVE","xpath=//*[@id='Not Now']",0,1);
        delay(1000);
        createLog("Completed : Handling Enjoying App alert");
    }

    public static void iOS_goToDashboardFromNotificationScreen() {
        createLog("Started : Navigation to dashboard");
        //navigate back to dashboard screen
        click("NATIVE","xpath=//*[@id='TOOLBAR_BUTTON_LEFT']",0,1);
        sc.syncElements(3000,15000);
        verifyElementFound("NATIVE","xpath=//*[@text='Account']",0);
        sc.flickElement("NATIVE", "xpath=//*[@text='drag']", 0, "Down");
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        createLog("Completed : Navigating to dashboard");
    }

    //remote common methods
    public static void iOS_remoteUnLock() {
        createLog("Started : Remote UnLock");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }    
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_door_unlock_button']", 0);

        //Verify Press and Hold is displayed - when remote command is only clicked instead of press and hold (long click)
        verifyPressAndHoldIsDisplayed_iOS();

        sc.longClick("NATIVE", "xpath=//*[@id='remote_door_unlock_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Unlocking");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote UnLock");
    }

    public static void iOS_remoteLock() {
        createLog("Started : Remote Lock");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }   
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_door_lock_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_door_lock_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Locking");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote Lock");
    }

    public static void iOS_remoteStart() {
        createLog("Started : Remote Start");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }       
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_engine_start_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_engine_start_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Starting");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote Start");
    }

    public static void iOS_remoteStop() {
        createLog("Started : Remote Stop");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }       
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_engine_stop_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_engine_stop_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Stopping");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote Stop");
    }

    public static void iOS_remoteLights() {
        createLog("Started : Remote Lights");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }      
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_lights_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_lights_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Lights on");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote Lights");
    }

    public static void iOS_remoteHazard() {
        createLog("Started : Remote Hazard");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }      
        sc.syncElements(2000, 10000);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        }
        if(!sc.isElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_hazard_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_hazard_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Hazards on");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote Hazard");
    }

    public static void iOS_remoteBuzzer() {
        createLog("Started : Remote Buzzer");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }      
        sc.syncElements(2000, 10000);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        }
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_buzzer_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_buzzer_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Buzzer on");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote Buzzer");
    }

    public static void iOS_remoteTrunkUnlock() {
        createLog("Started : Remote Trunk Unlock");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }      
        sc.syncElements(2000, 10000);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        }
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_trunk_unlock_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_trunk_unlock_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Trunk Unlocking");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote Trunk Unlock");
    }

    public static void iOS_remoteTrunkLock() {
        createLog("Started : Remote Trunk Lock");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }      
        sc.syncElements(2000, 10000);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        }
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_trunk_lock_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_trunk_lock_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Trunk Locking");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote Trunk Lock");
    }

    public static void iOS_remoteHorn() {
        createLog("Started : Remote Horn");
        if (!sc.isElementFound("NATIVE", "xpath=//*[@label='Vehicle image, double tap to open vehicle info.']"))
            reLaunchApp_iOS();
        if (sc.isElementFound("NATIVE", "xpath=//*[(@text='Notifications' or @text='NOTIFICATIONS') and @id='TOOLBAR_LABEL_TITLE']")) {
            iOS_goToDashboardFromNotificationScreen();
            sc.syncElements(3000, 15000);
        }      
        sc.syncElements(2000, 10000);
        if(sc.isElementFound("NATIVE","xpath=//*[@id='dashboard_remote_close_iconbutton' and @visible='true']")) {
            sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_close_iconbutton']", 0, "Down");
            sc.syncElements(3000, 15000);
        }
        verifyElementFound("NATIVE","xpath=//*[@label='Vehicle image, double tap to open vehicle info.']",0);
        verifyElementFound("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0);
        sc.flickElement("NATIVE", "xpath=//*[@id='dashboard_remote_open_iconbutton']", 0, "Up");
        sc.syncElements(2000, 10000);
        verifyElementFound("NATIVE", "xpath=//*[@id='remote_horn_button']", 0);
        sc.longClick("NATIVE", "xpath=//*[@id='remote_horn_button']", 0, 1, 0, 0);

        //Verify remote command action - (Example : Locking)
        verify_iOS_remoteCommandAction("Horn on");

        //Notifications screen navigation
        iOS_goToNotificationsScreen();
        createLog("Completed : Remote Horn");
    }
    public static void iOS_LaunchApp(){
        createLog("Started - launch app");
        if (sc.isElementFound("NATIVE", "xpath=(//*[contains(@id,'A new iOS update is now available')])[1]"))
            sc.click("NATIVE", "xpath=//*[@id='Close']", 0, 1);
        sc.deviceAction("Home");
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Search']")){
            click("NATIVE","xpath=//*[@id='Search']",0,1);
            if(Objects.equals(ConfigSingleton.configMap.get("local"), "")) {
                sendText("NATIVE", "xpath=//*[@id='SpotlightSearchField']", 0, System.getProperty("cloudApp").substring(0, 4));
                if(System.getProperty("cloudApp").toLowerCase().contains("lexus")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Lexus']")){
                        createLog("Launching Lexus app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Lexus']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(System.getProperty("cloudApp").toLowerCase().contains("toyota")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Toyota']")){
                        createLog("Launching Toyota app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Toyota']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(System.getProperty("cloudApp").toLowerCase().contains("subaru")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']")){
                        createLog("Launching Subaru app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='SUBARU CONNECT']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
            }
            else{
                sendText("NATIVE", "xpath=//*[@id='SpotlightSearchField']", 0, ConfigSingleton.configMap.get("local"));
                if(ConfigSingleton.configMap.get("local").toLowerCase().contains("lexus")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Lexus']")){
                        createLog("Launching Lexus app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Lexus']",0,1);
                    }
                    else{
                        createLog(ConfigSingleton.configMap.get("local")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(ConfigSingleton.configMap.get("local").toLowerCase().contains("toyota")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Toyota']")){
                        createLog("Launching Toyota app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='Toyota']",0,1);
                    }
                    else{
                        createLog(ConfigSingleton.configMap.get("local")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
                else if(System.getProperty("local").toLowerCase().contains("subaru")){
                    if(sc.isElementFound("NATIVE", "xpath=//*[@accessibilityLabel='SUBARU CONNECT']")){
                        createLog("Launching Subaru app");
                        click("NATIVE","xpath=//*[@accessibilityLabel='SUBARU CONNECT']",0,1);
                    }
                    else{
                        createLog(System.getProperty("cloudApp")+" App not found on the device");
                        reLaunchApp_iOS();
                    }
                }
            }
        }
        createLog("Completed - launch app");
    }

    public static void verify_iOS_remoteCommandAction(String strRemoteCommandAction) {
        createLog("Verifying remote command action");
        boolean isSendingDisplayed = sc.waitForElement("NATIVE","xpath=//*[@label='Sending']",0,10);
        if(isSendingDisplayed) {
            createLog("Sending is displayed");
            sc.report("Sending is displayed",true);
        }

        boolean isRemoteActionDisplayed = sc.waitForElement("NATIVE","xpath=//*[@label='"+strRemoteCommandAction+"']",0,15000);

        if(strRemoteCommandAction.equalsIgnoreCase("Locking")) {
            //Click on remote command - verify Press and Hold is not displayed - when other command is in progress
            verifyPressAndHoldNotDisplayed_iOS();
        }

        createLog("RemoteAction boolean val :"+isRemoteActionDisplayed);
        if(isRemoteActionDisplayed) {
            createLog(""+strRemoteCommandAction+" is displayed");
            sc.report(""+strRemoteCommandAction+" is displayed",true);
            sc.syncElements(15000, 60000);
        } else {
            sc.report(""+strRemoteCommandAction+" is not displayed",false);
            createErrorLog(""+strRemoteCommandAction+" is not displayed");
        }
        //Check Enjoying App is displayed
        if(sc.isElementFound("NATIVE","xpath=//*[contains(@value,'Are you enjoying')]",0)) {
            enjoyingAppAlert_iOS();
        }
        sc.verifyElementNotFound("NATIVE","xpath=//*[@label='"+strRemoteCommandAction+"']",0);
        createLog("Verified remote command action");
    }

    public static void iOS_safariClearCache() {
        createLog("Started - Clearing safari cache");
        sc.closeAllApplications();
        sc.deviceAction("Home");
        if(sc.isElementFound("NATIVE","xpath=//*[@id='Settings']",0))
            click("NATIVE","xpath=//*[@id='Settings']",0,1);
        if(!sc.isElementFound("NATIVE","xpath=//*[@id='APPLE_ACCOUNT']")){
            //bluetooth screen displayed
            click("NATIVE","xpath=//*[@label='Settings' and @class='UIAButton']",0,1);
        }
        sc.flickElement("NATIVE","xpath=//*[@label='Airplane Mode']",0,"Down");
        sendText("NATIVE","xpath=//*[@id='Search']",0,"Clear History and Website Data");
        verifyElementFound("NATIVE", "xpath=//*[@id='Safari' and @class='UIAStaticText']", 0);
        click("NATIVE","xpath=//*[@id='Safari' and @class='UIAStaticText']",0,1);

        verifyElementFound("NATIVE", "xpath=//*[@id='Safari' and @class='UIANavigationBar']", 0);
        sc.swipeWhileNotFound("Down", 80, 500, "NATIVE", "xpath=//*[@id='CLEAR_HISTORY_AND_DATA' and @visible='true']", 0, 500, 5, true);

        if(sc.isElementFound("NATIVE","xpath=//*[@id='Clear History and Data']")) {
            //for iOS 17 and below
            click("NATIVE","xpath=//*[@id='Clear History and Data']",0,1);
            click("NATIVE","xpath=//*[@id='Close Tabs' and @class='UIAButton']",0,1);
        } else {
            //for iOS 17 and above
            click("NATIVE","xpath=//*[@id='All history']",0,1);
            if(sc.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='CloseAllTabsSwitch' and @enabled='true' and @value='0']")) {
                click("NATIVE","xpath=//*[@accessibilityLabel='CloseAllTabsSwitch']",0,1);
            }
            click("NATIVE","xpath=//*[@id='ClearHistoryButton']",0,1);
        }
        createLog("Completed - Clearing safari cache");
        sc.closeAllApplications();
    }

    public static void verify_Android_remoteCommandAction(String strRemoteCommandAction) {
        createLog("Verifying remote command action");
        boolean isSendingDisplayed = sc.waitForElement("NATIVE","xpath=//*[@text='Sending']",0,1000);
        if(isSendingDisplayed) {
            createLog("Sending is displayed");
            sc.report("Sending is displayed",true);
        }
        boolean isRemoteActionDisplayed = sc.waitForElement("NATIVE","xpath=//*[@text='"+strRemoteCommandAction+"']",0,15000);
        createLog("RemoteAction boolean val :"+isRemoteActionDisplayed);
        if(isRemoteActionDisplayed) {
            createLog(""+strRemoteCommandAction+" is displayed");
            sc.report(""+strRemoteCommandAction+" is displayed",true);
            sc.syncElements(15000, 60000);
        } else {
            sc.report(""+strRemoteCommandAction+" is not displayed",false);
            createErrorLog(""+strRemoteCommandAction+" is not displayed");
        }
        sc.verifyElementNotFound("NATIVE","xpath=//*[@text='"+strRemoteCommandAction+"']",0);
        createLog("Verified remote command action");
    }
    public static void enjoyingAppAlertAndroid() {
        createLog("Enjoying App alert is displayed");
        createLog("Started : Handling Enjoying App alert");
        verifyElementFound("NATIVE","xpath=//*[@text='Are you enjoying the Toyota app?' or @text='Are you enjoying the Lexus app?']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='apptentive_enjoyment_dialog_no']",0);
        verifyElementFound("NATIVE","xpath=//*[@id='apptentive_enjoyment_dialog_yes']",0);
        //Yes
        click("NATIVE","xpath=//*[@id='apptentive_enjoyment_dialog_no']",0,1);
        delay(1000);
//        verifyElementFound("NATIVE","xpath=//*[contains(@id,'Tap a star to rate it on the App') and contains(@id,'Store')]",0);
//        verifyElementFound("NATIVE","xpath=//*[@id='Not Now']",0);
//        click("NATIVE","xpath=//*[@id='Not Now']",0,1);
//        delay(1000);
        createLog("Completed : Handling Enjoying App alert");
    }

    public static void verifyPressAndHoldNotDisplayed_iOS() {
        //verify unable to click on other commands
        createLog("Verifying Press and Hold is not displayed - when other command is in progress");
        sc.click("NATIVE", "xpath=//*[@id='remote_door_unlock_button']", 0, 1);
        sc.verifyElementNotFound("NATIVE","xpath=//*[@label='Press and hold']",0);
        createLog("Verified Press and Hold is not displayed - when other command is in progress");
    }

    public static void verifyPressAndHoldIsDisplayed_iOS() {
        //verify press and hold is displayed when remote command is only clicked
        createLog("Verifying Press and Hold is displayed - when remote command is only clicked instead of press and hold");
        sc.click("NATIVE", "xpath=//*[@id='remote_door_unlock_button']", 0, 1);
        sc.verifyElementFound("NATIVE","xpath=//*[@label='Press and hold']",0);
        createLog("Verified Press and Hold is displayed - when remote command is only clicked instead of press and hold");
    }

    public static void remoteCommandNotificationVerification(String notificationText1,String notificationText2,String notificationText3,String reportText){
        createLog("Started verification for ||Remote - "+reportText+"|| notification is Displayed");
        sc.waitForElement("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0, 10);
        String firstNotification = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"value");
        String firstNotificationDate = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"id").split("notification received")[1].trim();
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") |  firstNotificationDate.contains("minutes ago");
        if ((firstNotification.contains(notificationText1) | firstNotification.contains(notificationText2) | firstNotification.contains(notificationText3)) && dateCondition) {
            sc.report("Remote "+reportText+"  Notification Displayed", true);
            createLog("Remote "+reportText+"  Notification Displayed");
            createLog("Notification displayed is: "+firstNotification);
        } else {
            sc.report("Remote "+reportText+"  Notification not found", false);
            createLog("Notification displayed is: "+firstNotification);
            createErrorLog("Remote "+reportText+"  Notification not found");
        }
    }

    public static void remoteCommandNotificationVerification(String notificationText1,String notificationText2,String notificationText3,String notificationText4,String reportText){
        createLog("Started verification for ||Remote - "+reportText+"|| notification is Displayed");
        sc.waitForElement("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0, 10);
        String firstNotification = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"value");
        String firstNotificationDate = sc.elementGetProperty("NATIVE", "xpath=(//*[@XCElementType='XCUIElementTypeCell' and @class='UIAView'])[1]", 0,"id").split("notification received")[1].trim();
        boolean dateCondition = firstNotificationDate.contains("second ago") | firstNotificationDate.contains("seconds ago") | firstNotificationDate.contains("minute ago") |  firstNotificationDate.contains("minutes ago");
        if ((firstNotification.contains(notificationText1) | firstNotification.contains(notificationText2) | firstNotification.contains(notificationText3) | firstNotification.contains(notificationText4)) && dateCondition) {
            sc.report("Remote "+reportText+"  Notification Displayed", true);
            createLog("Remote "+reportText+"  Notification Displayed");
            createLog("Notification displayed is: "+firstNotification);
        } else {
            sc.report("Remote "+reportText+"  Notification not found", false);
            createLog("Notification displayed is: "+firstNotification);
            createErrorLog("Remote "+reportText+"  Notification not found");
        }
    }
}