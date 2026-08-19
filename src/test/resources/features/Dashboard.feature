@Dashboard @Regression
Feature: Dashboard and Navigation Functionality
  As an authenticated user
  I want to interact with the dashboard
  So that I can browse products and log out securely

  Background:
    Given the user is on the login page
    When the user logs in with valid credentials from config
    Then the user should be redirected to the dashboard page

  @Smoke @Products
  Scenario: Verify products listing on dashboard
    Then the dashboard page header title should be "Products"
    And the dashboard should display at least 6 products

  @Logout
  Scenario: Verify user logout functionality
    When the user logs out of the application
    Then the user is on the login page
