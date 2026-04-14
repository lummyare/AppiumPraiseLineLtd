package com.suban.cucumber.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Step definitions for Demo functionality
 * Showcases automatic reporting capabilities
 */
public class DemoStepDefinitions {
    
    private static final Logger logger = LogManager.getLogger(DemoStepDefinitions.class);
    
    @Given("the application is initialized")
    public void the_application_is_initialized() {
        logger.info("Application initialization step");
    }
    
    @When("I perform a basic test")
    public void i_perform_a_basic_test() {
        logger.info("Performing basic test operations");
    }
    
    @Then("the test should pass successfully")
    public void the_test_should_pass_successfully() {
        logger.info("Test passed successfully");
    }
    
    @And("the report should display beautifully")
    public void the_report_should_display_beautifully() {
        logger.info("Report display verification");
    }
    
    @When("I perform a test that fails")
    public void i_perform_a_test_that_fails() {
        logger.info("Performing a test that will fail");
        throw new RuntimeException("Intentional test failure for demonstration");
    }
    
    @Then("the test should fail as expected")
    public void the_test_should_fail_as_expected() {
        logger.info("Test failed as expected");
    }
    
    @And("the failure should be clearly visible in the report")
    public void the_failure_should_be_clearly_visible_in_the_report() {
        logger.info("Failure visibility verification");
    }
    
    @When("I test with data {string}")
    public void i_test_with_data(String testData) {
        logger.info("Testing with data: {}", testData);
    }
    
    @Then("the result should be {string}")
    public void the_result_should_be(String expectedResult) {
        logger.info("Expected result: {}", expectedResult);
        if ("failure".equals(expectedResult)) {
            throw new RuntimeException("Expected failure scenario for demo: " + expectedResult);
        }
    }

}
