package com.automation.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.TestNG;
import org.testng.annotations.DataProvider;

/**
 * Runner dedicated to re-running only failed scenarios captured in target/rerun.txt.
 */
@CucumberOptions(
        features = "@target/rerun.txt",
        glue = {
                "com.automation.framework.stepdefinitions",
                "com.automation.framework.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/rerun-html-report.html",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
)
public class FailedTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[] { FailedTestRunner.class });
        testNG.run();
    }
}

