@Login @Regression
Feature: User Login Functionality
  As a registered user
  I want to be able to log into the application
  So that I can access the products dashboard and place orders

  Background:
    Given the user is on the login page

  @Smoke @ValidLogin
  Scenario: Verify successful login with valid credentials
    When the user enters username "standard_user" and password "secret_sauce"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    And the dashboard page header title should be "Products"

  @Negative @LockedUser
  Scenario: Verify error message when logging in with locked out user
    When the user enters username "locked_out_user" and password "secret_sauce"
    And the user clicks on the login button
    Then an error message should be displayed containing "Epic sadface: Sorry, this user has been locked out."

  @DataDriven @ScenarioOutline
  Scenario Outline: Verify login validations with multiple invalid credentials
    When the user enters username "<username>" and password "<password>"
    And the user clicks on the login button
    Then an error message should be displayed containing "<expected_error>"

    Examples:
      | username        | password        | expected_error                                              |
      | invalid_user    | secret_sauce    | Username and password do not match any user in this service |
      | standard_user   | wrong_password  | Username and password do not match any user in this service |
      |                 | secret_sauce    | Epic sadface: Username is required                         |

  @DataTable
  Scenario: Verify login using Cucumber Data Table
    When the user enters the following credentials:
      | username      | password     |
      | standard_user | secret_sauce |
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  @ExcelData
  Scenario: Verify login using Apache POI Excel test data reader
    When the user logs in using test data from Excel sheet "LoginData" for test case "TC_001"
    Then the user should be redirected to the dashboard page
