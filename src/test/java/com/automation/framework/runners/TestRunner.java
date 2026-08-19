package com.automation.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.TestNG;
import org.testng.annotations.DataProvider;

/**
 * Main TestNG Cucumber Runner.
 * Configured with plugins for ExtentReports, HTML/JSON reports, and failed
 * scenario rerun tracking.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.automation.framework.stepdefinitions",
                "com.automation.framework.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-html-report.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml",
                "rerun:target/rerun.txt",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        dryRun = false,
        tags = "@Smoke or @Regression"
)
public class TestRunner extends AbstractTestNGCucumberTests {

        /**
         * Enables parallel execution of Cucumber scenarios across threads.
         */
        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }

        /**
         * Standard Java main method allowing TestRunner to be executed directly
         * as a standalone Java Application from any IDE or command line.
         */
        public static void main(String[] args) {
                TestNG testNG = new TestNG();
                testNG.setTestClasses(new Class[] { TestRunner.class });
                testNG.run();
        }
}
