package com.automation.framework.hooks;

import com.automation.framework.base.BaseTest;
import com.automation.framework.base.DriverFactory;
import com.automation.framework.library.LoggerUtil;
import com.automation.framework.library.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber Hooks to manage setup, teardown, and screenshot capture per scenario.
 */
public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        LoggerUtil.info("==========================================================================");
        LoggerUtil.info("STARTING SCENARIO: " + scenario.getName() + " | Tags: " + scenario.getSourceTagNames());
        LoggerUtil.info("==========================================================================");
        
        BaseTest.setUpDriver();
    }

    @AfterStep
    public void captureScreenshotOnStepFailure(Scenario scenario) {
        if (scenario.isFailed() && DriverFactory.getDriver() != null) {
            try {
                byte[] screenshot = ScreenshotUtil.getByteScreenshot();
                scenario.attach(screenshot, "image/png", "Failed_Step_Screenshot");
                LoggerUtil.error("Step failed in Scenario: " + scenario.getName() + ". Screenshot attached to report.");
            } catch (Exception e) {
                LoggerUtil.error("Failed to capture screenshot on step failure", e);
            }
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && DriverFactory.getDriver() != null) {
                byte[] screenshot = ScreenshotUtil.getByteScreenshot();
                scenario.attach(screenshot, "image/png", "Scenario_Failure_Screenshot");
                LoggerUtil.error("SCENARIO FAILED: " + scenario.getName());
            } else {
                LoggerUtil.info("SCENARIO PASSED: " + scenario.getName());
            }
        } catch (Exception e) {
            LoggerUtil.error("Error during After hook execution", e);
        } finally {
            BaseTest.tearDownDriver();
            LoggerUtil.info("==========================================================================");
            LoggerUtil.info("COMPLETED SCENARIO: " + scenario.getName() + " | Status: " + scenario.getStatus());
            LoggerUtil.info("==========================================================================");
        }
    }
}
