package com.suban.cucumber.context;

import io.appium.java_client.AppiumDriver;
import com.suban.framework.pages.common.HomePage;
import com.suban.framework.pages.common.LoginPage;
import com.suban.framework.pages.common.LoginSuccessPage;

/**
 * Test context to share data between step definitions
 * Stores page objects and test data
 */
public class TestContext {
    
    private AppiumDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;
    private LoginSuccessPage loginSuccessPage;

    
    public AppiumDriver getDriver() {
        return driver;
    }
    
    public void setDriver(AppiumDriver driver) {
        this.driver = driver;
    }
    
    public HomePage getHomePage() {
        if (homePage == null && driver != null) {
            homePage = new HomePage(driver);
        }
        return homePage;
    }
    
    public LoginPage getLoginPage() {
        if (loginPage == null && driver != null) {
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }
    
    public LoginSuccessPage getLoginSuccessPage() {
        if (loginSuccessPage == null && driver != null) {
            loginSuccessPage = new LoginSuccessPage(driver);
        }
        return loginSuccessPage;
    }
    
    public void resetPages() {
        homePage = null;
        loginPage = null;
        loginSuccessPage = null;
    }
    
   }
