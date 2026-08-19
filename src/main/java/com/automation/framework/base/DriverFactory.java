package com.automation.framework.base;

import com.automation.framework.library.ConfigReader;
import com.automation.framework.library.LoggerUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Objects;

/**
 * Factory class for managing thread-safe WebDriver instances across parallel test threads.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverFactory() {}

    /**
     * Initializes a WebDriver instance based on the requested browser type.
     *
     * @param browser Browser name (chrome, firefox, edge)
     * @return WebDriver instance
     */
    public static WebDriver initializeDriver(String browser) {
        WebDriver driver;
        boolean isHeadless = ConfigReader.getBooleanProperty("headless");
        String browserName = (browser != null && !browser.trim().isEmpty()) ? browser.toLowerCase().trim() : "chrome";

        LoggerUtil.info("Initializing WebDriver for browser: " + browserName + " (Headless: " + isHeadless + ")");

        switch (browserName) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (isHeadless) {
                    firefoxOptions.addArguments("-headless");
                }
                firefoxOptions.addArguments("--disable-notifications");
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        setDriver(driver);
        return getDriver();
    }

    /**
     * Retrieves the current thread's WebDriver instance.
     *
     * @return WebDriver
     */
    public static WebDriver getDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    /**
     * Stores the WebDriver in ThreadLocal.
     *
     * @param driver WebDriver instance
     */
    public static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    /**
     * Quits and cleanly tears down the current thread's WebDriver instance.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (Objects.nonNull(driver)) {
            try {
                driver.quit();
                LoggerUtil.info("WebDriver quit successfully for current thread.");
            } catch (Exception e) {
                LoggerUtil.error("Error while quitting WebDriver", e);
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }
}
