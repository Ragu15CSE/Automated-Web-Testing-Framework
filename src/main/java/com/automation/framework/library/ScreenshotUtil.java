package com.automation.framework.library;

import com.automation.framework.base.DriverFactory;
import com.automation.framework.constants.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for capturing screenshots in Base64 and File formats.
 */
public final class ScreenshotUtil {

    private ScreenshotUtil() {}

    /**
     * Captures screenshot as byte array for embedding directly into Cucumber Scenario reports.
     *
     * @return byte[] screenshot data
     */
    public static byte[] getByteScreenshot() {
        return ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Captures screenshot as Base64 String for ExtentReports.
     *
     * @return Base64 encoded screenshot string
     */
    public static String getBase64Screenshot() {
        return ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Saves a screenshot to disk with timestamped filename.
     *
     * @param testName Name of test/step for file naming
     * @return Absolute file path to saved screenshot
     */
    public static String captureScreenshotToFile(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destinationPath = FrameworkConstants.SCREENSHOTS_PATH + File.separator + testName + "_" + timestamp + ".png";
        
        File sourceFile = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(destinationPath);
        
        try {
            FileUtils.copyFile(sourceFile, destinationFile);
            LoggerUtil.info("Screenshot saved successfully to: " + destinationPath);
        } catch (IOException e) {
            LoggerUtil.error("Failed to save screenshot to destination: " + destinationPath, e);
        }
        return destinationPath;
    }
}
