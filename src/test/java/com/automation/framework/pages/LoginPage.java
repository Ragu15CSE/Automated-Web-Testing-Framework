package com.automation.framework.pages;

import com.automation.framework.library.LoggerUtil;
import org.openqa.selenium.By;

/**
 * Page Object Model representation of the Login Page.
 * Contains page elements (locators) and business actions.
 */
public class LoginPage extends BasePage {

    // ==========================================
    // Locators (Page Elements)
    // ==========================================
    private final By txtUsername = By.id("user-name");
    private final By txtPassword = By.id("password");
    private final By btnLogin = By.id("login-button");
    private final By lblErrorMessage = By.cssSelector("[data-test='error']");
    private final By lblLoginLogo = By.className("login_logo");

    // ==========================================
    // Page Actions / Business Methods
    // ==========================================

    public boolean isLoginPageDisplayed() {
        return isDisplayed(lblLoginLogo) && isDisplayed(btnLogin);
    }

    public LoginPage enterUsername(String username) {
        LoggerUtil.info("Entering username: " + username);
        sendKeys(txtUsername, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        LoggerUtil.info("Entering password.");
        sendKeys(txtPassword, password);
        return this;
    }

    public void clickLoginButton() {
        LoggerUtil.info("Clicking Login button.");
        click(btnLogin);
    }

    public DashboardPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new DashboardPage();
    }

    public String getErrorMessage() {
        return getText(lblErrorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(lblErrorMessage);
    }
}
