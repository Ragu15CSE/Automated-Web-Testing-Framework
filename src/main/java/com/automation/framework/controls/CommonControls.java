package com.automation.framework.controls;

import com.automation.framework.base.DriverFactory;
import com.automation.framework.constants.FrameworkConstants;
import com.automation.framework.library.LoggerUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Common Controls / WebDriver Utility class providing reliable, synchronized
 * methods for web element interactions (Waits, Clicks, Typing, Select, JS, Frames, Alerts).
 */
public class CommonControls {

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    protected WebDriverWait getWait(Duration timeout) {
        return new WebDriverWait(getDriver(), timeout);
    }

    protected WebDriverWait getWait() {
        return getWait(FrameworkConstants.EXPLICIT_WAIT);
    }

    // ==========================================
    // Synchronization / Explicit Wait Methods
    // ==========================================

    public WebElement waitForVisibility(By locator) {
        LoggerUtil.debug("Waiting for visibility of element: " + locator);
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickability(By locator) {
        LoggerUtil.debug("Waiting for element to be clickable: " + locator);
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForPresence(By locator) {
        LoggerUtil.debug("Waiting for presence of element: " + locator);
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public boolean waitForInvisibility(By locator) {
        LoggerUtil.debug("Waiting for invisibility of element: " + locator);
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForTextToBePresent(By locator, String text) {
        LoggerUtil.debug("Waiting for text '" + text + "' in element: " + locator);
        return getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // ==========================================
    // Click & Input Actions
    // ==========================================

    public void click(By locator) {
        try {
            WebElement element = waitForClickability(locator);
            element.click();
            LoggerUtil.info("Clicked on element: " + locator);
        } catch (Exception e) {
            LoggerUtil.warn("Standard click failed on " + locator + ". Retrying with JavaScript click.");
            clickUsingJS(locator);
        }
    }

    public void sendKeys(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
        LoggerUtil.info("Entered text '" + text + "' into element: " + locator);
    }

    public void clear(By locator) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        LoggerUtil.info("Cleared text in element: " + locator);
    }

    public String getText(By locator) {
        WebElement element = waitForVisibility(locator);
        String text = element.getText().trim();
        LoggerUtil.info("Retrieved text '" + text + "' from element: " + locator);
        return text;
    }

    public String getAttribute(By locator, String attributeName) {
        WebElement element = waitForPresence(locator);
        return element.getAttribute(attributeName);
    }

    public boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {
        return waitForPresence(locator).isEnabled();
    }

    public boolean isSelected(By locator) {
        return waitForPresence(locator).isSelected();
    }

    public List<WebElement> getElements(By locator) {
        return getDriver().findElements(locator);
    }

    // ==========================================
    // Dropdown / Select Methods
    // ==========================================

    public void selectByVisibleText(By locator, String visibleText) {
        WebElement element = waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
        LoggerUtil.info("Selected '" + visibleText + "' from dropdown: " + locator);
    }

    public void selectByValue(By locator, String value) {
        WebElement element = waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByValue(value);
        LoggerUtil.info("Selected value '" + value + "' from dropdown: " + locator);
    }

    public void selectByIndex(By locator, int index) {
        WebElement element = waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByIndex(index);
        LoggerUtil.info("Selected index '" + index + "' from dropdown: " + locator);
    }

    public String getSelectedDropdownText(By locator) {
        WebElement element = waitForVisibility(locator);
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText().trim();
    }

    // ==========================================
    // JavaScript Execution Helpers
    // ==========================================

    public void clickUsingJS(By locator) {
        WebElement element = waitForPresence(locator);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].click();", element);
        LoggerUtil.info("Clicked using JavaScript on element: " + locator);
    }

    public void typeUsingJS(By locator, String text) {
        WebElement element = waitForPresence(locator);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].value = arguments[1];", element, text);
        LoggerUtil.info("Typed using JavaScript into element: " + locator);
    }

    public void scrollToElement(By locator) {
        WebElement element = waitForPresence(locator);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        LoggerUtil.info("Scrolled to element: " + locator);
    }

    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        LoggerUtil.info("Scrolled to the bottom of the page.");
    }

    // ==========================================
    // Advanced Actions (Mouse & Keyboard)
    // ==========================================

    public void hoverOverElement(By locator) {
        WebElement element = waitForVisibility(locator);
        Actions actions = new Actions(getDriver());
        actions.moveToElement(element).perform();
        LoggerUtil.info("Hovered over element: " + locator);
    }

    public void doubleClick(By locator) {
        WebElement element = waitForClickability(locator);
        Actions actions = new Actions(getDriver());
        actions.doubleClick(element).perform();
        LoggerUtil.info("Double clicked element: " + locator);
    }

    public void rightClick(By locator) {
        WebElement element = waitForClickability(locator);
        Actions actions = new Actions(getDriver());
        actions.contextClick(element).perform();
        LoggerUtil.info("Right clicked element: " + locator);
    }

    public void dragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = waitForVisibility(sourceLocator);
        WebElement target = waitForVisibility(targetLocator);
        Actions actions = new Actions(getDriver());
        actions.dragAndDrop(source, target).perform();
        LoggerUtil.info("Dragged element " + sourceLocator + " to " + targetLocator);
    }

    // ==========================================
    // Windows & Frames Switching
    // ==========================================

    public void switchToWindowByTitle(String targetTitle) {
        String currentWindow = getDriver().getWindowHandle();
        Set<String> allWindows = getDriver().getWindowHandles();
        for (String windowHandle : allWindows) {
            getDriver().switchTo().window(windowHandle);
            if (getDriver().getTitle().equalsIgnoreCase(targetTitle)) {
                LoggerUtil.info("Switched to window with title: " + targetTitle);
                return;
            }
        }
        getDriver().switchTo().window(currentWindow);
        LoggerUtil.warn("Could not find window with title: " + targetTitle);
    }

    public void switchToFrame(By frameLocator) {
        WebElement frameElement = waitForPresence(frameLocator);
        getDriver().switchTo().frame(frameElement);
        LoggerUtil.info("Switched to frame: " + frameLocator);
    }

    public void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
        LoggerUtil.info("Switched back to default content.");
    }

    // ==========================================
    // Alert Handling
    // ==========================================

    public void acceptAlert() {
        Alert alert = getWait().until(ExpectedConditions.alertIsPresent());
        alert.accept();
        LoggerUtil.info("Accepted alert popup.");
    }

    public void dismissAlert() {
        Alert alert = getWait().until(ExpectedConditions.alertIsPresent());
        alert.dismiss();
        LoggerUtil.info("Dismissed alert popup.");
    }

    public String getAlertText() {
        Alert alert = getWait().until(ExpectedConditions.alertIsPresent());
        return alert.getText();
    }
}
