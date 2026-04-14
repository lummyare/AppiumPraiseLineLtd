package com.suban.cucumber.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

/**
 * Cucumber TestNG Runner with automatic reporting
 */
/*
 * Tag filtering is controlled at runtime via the cucumber.filter.tags system property.
 * Default value (@login = all scenarios) is set in pom.xml <properties>.
 *
 * Run shortcuts (from the project root):
 *   ./run.sh          → all @login scenarios
 *   ./run.sh 21mm     → @21mmEVDummy1 only
 *   ./run.sh 24mm     → @24MMEVDummy1 only
 *   ./run.sh smoke    → @smoke only
 *   ./run.sh tag @X   → any custom tag
 *
 * Or pass directly to Maven:
 *   mvn clean test -Dcucumber.filter.tags="@24MMEVDummy1"
 */
@CucumberOptions(
    features = "src/test/java/com/suban/cucumber/features",
    glue = {"com.suban.cucumber.stepdefinitions", "com.suban.cucumber.hooks"},
    // No 'tags' here — controlled entirely by cucumber.filter.tags system property
    // (set via run.sh or -Dcucumber.filter.tags on the mvn command line).
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-report.html",
        "json:target/cucumber-reports/cucumber-report.json",
        "junit:target/cucumber-reports/cucumber-report.xml",
        "com.suban.framework.reporting.CucumberEventListener"
    },
    monochrome = true,
    dryRun = false
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
    
    @BeforeSuite
    public void globalSetup() throws Exception {
        System.out.println("=== Cucumber Test Suite Starting ===");
        System.out.println("=== Automatic Reporting Enabled ===");
    }
    
    @AfterSuite
    public void globalTearDown() throws Exception {
        System.out.println("=== Cucumber Test Suite Completed ===");
        System.out.println("=== Check ExtentReports for detailed results ===");
    }
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
