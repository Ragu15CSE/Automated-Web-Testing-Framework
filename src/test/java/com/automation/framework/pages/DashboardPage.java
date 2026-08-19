package com.automation.framework.pages;

import com.automation.framework.library.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object Model representation of the Products / Dashboard Page.
 */
public class DashboardPage extends BasePage {

    // ==========================================
    // Locators
    // ==========================================
    private final By lblPageTitle = By.className("title");
    private final By btnMenu = By.id("react-burger-menu-btn");
    private final By lnkLogout = By.id("logout_sidebar_link");
    private final By inventoryItems = By.className("inventory_item");
    private final By btnShoppingCart = By.className("shopping_cart_link");

    // ==========================================
    // Actions / Verifications
    // ==========================================

    public boolean isDashboardDisplayed() {
        return isDisplayed(lblPageTitle) && isDisplayed(btnShoppingCart);
    }

    public String getHeaderTitle() {
        return getText(lblPageTitle);
    }

    public int getProductCount() {
        List<WebElement> items = getElements(inventoryItems);
        LoggerUtil.info("Total products displayed on dashboard: " + items.size());
        return items.size();
    }

    public LoginPage logout() {
        LoggerUtil.info("Logging out from application.");
        click(btnMenu);
        click(lnkLogout);
        return new LoginPage();
    }
}
