package com.automation.framework.stepdefinitions;

import com.automation.framework.library.ConfigReader;
import com.automation.framework.library.ExcelDataReader;
import com.automation.framework.library.LoggerUtil;
import com.automation.framework.pages.DashboardPage;
import com.automation.framework.pages.LoginPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * Step Definitions matching Gherkin steps in Login.feature.
 */
public class LoginSteps {

    private LoginPage loginPage = new LoginPage();
    private DashboardPage dashboardPage;

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed!");
        LoggerUtil.info("User is verified to be on the login page.");
    }

    @When("the user enters username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("the user clicks on the login button")
    public void theUserClicksOnTheLoginButton() {
        loginPage.clickLoginButton();
        dashboardPage = new DashboardPage();
    }

    @When("the user logs in with valid credentials from config")
    public void theUserLogsInWithValidCredentialsFromConfig() {
        String username = ConfigReader.getProperty("valid.username");
        String password = ConfigReader.getProperty("valid.password");
        dashboardPage = loginPage.loginAs(username, password);
    }

    @When("the user enters the following credentials:")
    public void theUserEntersTheFollowingCredentials(DataTable dataTable) {
        List<Map<String, String>> credentials = dataTable.asMaps(String.class, String.class);
        String username = credentials.get(0).get("username");
        String password = credentials.get(0).get("password");
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("the user logs in using test data from Excel sheet {string} for test case {string}")
    public void theUserLogsInUsingTestDataFromExcel(String sheetName, String testCaseId) {
        Map<String, String> testData = ExcelDataReader.getTestDataByCaseId(sheetName, "TestCaseId", testCaseId);
        Assert.assertFalse(testData.isEmpty(), "No test data found in Excel for testCaseId: " + testCaseId);

        String username = testData.get("Username");
        String password = testData.get("Password");
        LoggerUtil.info("Read test data from Excel -> Username: " + username);

        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        dashboardPage = new DashboardPage();
    }

    @Then("an error message should be displayed containing {string}")
    public void anErrorMessageShouldBeDisplayedContaining(String expectedMessage) {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was expected but not displayed.");
        String actualMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Expected error message containing: [" + expectedMessage + "], but got: [" + actualMessage + "]");
        LoggerUtil.info("Verified error message successfully: " + actualMessage);
    }
}
