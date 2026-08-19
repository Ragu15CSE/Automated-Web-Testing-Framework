package com.automation.framework.stepdefinitions;

import com.automation.framework.library.LoggerUtil;
import com.automation.framework.pages.DashboardPage;
import com.automation.framework.pages.LoginPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step Definitions matching Gherkin steps in Dashboard.feature.
 */
public class DashboardSteps {

    private DashboardPage dashboardPage = new DashboardPage();
    private LoginPage loginPage;

    @Then("the user should be redirected to the dashboard page")
    public void theUserShouldBeRedirectedToTheDashboardPage() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard page is not displayed!");
        LoggerUtil.info("User is successfully redirected to Dashboard page.");
    }

    @Then("the dashboard page header title should be {string}")
    public void theDashboardPageHeaderTitleShouldBe(String expectedHeader) {
        String actualHeader = dashboardPage.getHeaderTitle();
        Assert.assertEquals(actualHeader, expectedHeader, "Dashboard header title mismatch!");
        LoggerUtil.info("Dashboard header title matched: " + actualHeader);
    }

    @Then("the dashboard should display at least {int} products")
    public void theDashboardShouldDisplayAtLeastProducts(int minCount) {
        int actualCount = dashboardPage.getProductCount();
        Assert.assertTrue(actualCount >= minCount,
                "Expected at least " + minCount + " products, but found " + actualCount);
        LoggerUtil.info("Verified products count on dashboard: " + actualCount);
    }

    @When("the user logs out of the application")
    public void theUserLogsOutOfTheApplication() {
        loginPage = dashboardPage.logout();
    }
}
