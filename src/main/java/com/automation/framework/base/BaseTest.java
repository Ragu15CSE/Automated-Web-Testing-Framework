package com.automation.framework.base;

import com.automation.framework.constants.FrameworkConstants;
import com.automation.framework.library.ConfigReader;
import com.automation.framework.library.LoggerUtil;
import org.openqa.selenium.WebDriver;

/**
 * Base class providing startup, teardown, and lifecycle management for test executions.
 */
public class BaseTest {

    /**
     * Sets up the WebDriver for test execution and navigates to the application URL.
     */
    public static void setUpDriver() {
        String browser = ConfigReader.getProperty("browser", FrameworkConstants.DEFAULT_BROWSER);
        WebDriver driver = DriverFactory.initializeDriver(browser);
        
        driver.manage().timeouts().implicitlyWait(FrameworkConstants.IMPLICIT_WAIT);
        driver.manage().timeouts().pageLoadTimeout(FrameworkConstants.PAGE_LOAD_TIMEOUT);
        driver.manage().window().maximize();

        String appUrl = ConfigReader.getProperty("url");
        if (appUrl != null && !appUrl.trim().isEmpty()) {
            LoggerUtil.info("Navigating to URL: " + appUrl);
            driver.get(appUrl);
        } else {
            LoggerUtil.warn("No 'url' property configured in config.properties");
        }
    }

    /**
     * Cleans up driver resources after test completion.
     */
    public static void tearDownDriver() {
        DriverFactory.quitDriver();
    }
}
