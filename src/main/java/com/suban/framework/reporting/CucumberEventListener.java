package com.suban.framework.reporting;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Automatic Cucumber Event Listener for reporting
 * This listener automatically captures step execution results and integrates with ExtentReports
 * No manual ReportUtils calls needed in step definitions
 */
public class CucumberEventListener implements EventListener {
    
    private static final Logger logger = LogManager.getLogger(CucumberEventListener.class);
    private String currentScenarioName;
    
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStarted);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
    }
    
    private void handleTestRunStarted(TestRunStarted event) {
        logger.info("Test run started");
        ReportUtils.initReports();
    }
    
    private void handleTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        String scenarioName = testCase.getName();
        String featureName = extractFeatureName(testCase);
        String tags = formatTags(testCase);
        
        currentScenarioName = scenarioName; // Track current scenario name
        
        logger.info("Test case started: {}", scenarioName);
        ReportUtils.createScenario(scenarioName, featureName, tags);
    }
    
    private void handleTestStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            String stepText = pickleStep.getStep().getText();
            logger.debug("Step started: {}", stepText);
        }
    }
    
    private void handleTestStepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            String stepText = pickleStep.getStep().getText();
            Result result = event.getResult();
            
            logger.debug("Step finished: {} with status: {}", stepText, result.getStatus());
            
            switch (result.getStatus()) {
                case PASSED:
                    ReportUtils.logStepPass(stepText, "Step executed successfully");
                    break;
                case FAILED:
                    // Try to get screenshot path from registry for failed steps
                    String screenshotPath = ScreenshotRegistry.getScreenshotPath(currentScenarioName);
                    ReportUtils.logStepFail(stepText, result.getError(), screenshotPath);
                    break;
                case SKIPPED:
                    ReportUtils.logStepSkip(stepText, "Step skipped");
                    break;
                case PENDING:
                    ReportUtils.logStepSkip(stepText, "Step pending implementation");
                    break;
                default:
                    ReportUtils.logStepWarning("Step status: " + result.getStatus() + " for: " + stepText);
                    break;
            }
        }
    }
    
    private String getCurrentScenarioName() {
        return currentScenarioName;
    }
    
    private void handleTestCaseFinished(TestCaseFinished event) {
        TestCase testCase = event.getTestCase();
        Result result = event.getResult();
        logger.info("Test case finished: {} with status: {}", testCase.getName(), result.getStatus());
        
        // Clean up screenshot registry for this scenario
        ScreenshotRegistry.removeScreenshotPath(testCase.getName());
    }
    
    private void handleTestRunFinished(TestRunFinished event) {
        logger.info("Test run finished");
        ReportUtils.flushReports();
    }
    
    private String extractFeatureName(TestCase testCase) {
        String uri = testCase.getUri().toString();
        String[] parts = uri.split("/");
        String fileName = parts[parts.length - 1];
        return fileName.replace(".feature", "").replace("-", " ").replace("_", " ");
    }
    
    private String formatTags(TestCase testCase) {
        return testCase.getTags().stream()
                .map(tag -> tag.replace("@", ""))
                .reduce((tag1, tag2) -> tag1 + ", " + tag2)
                .orElse("No Tags");
    }
}
