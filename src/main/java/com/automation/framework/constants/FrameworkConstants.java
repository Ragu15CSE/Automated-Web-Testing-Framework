package com.automation.framework.constants;

import java.io.File;
import java.time.Duration;

/**
 * Centralized repository for all framework constants such as timeouts,
 * file paths, and environment settings.
 */
public final class FrameworkConstants {

    // Private constructor to prevent instantiation
    private FrameworkConstants() {}

    // Base Paths
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = PROJECT_PATH + File.separator + "src" + File.separator + "test" + File.separator + "resources";
    
    // Config & Test Data Paths
    public static final String CONFIG_FILE_PATH = RESOURCES_PATH + File.separator + "config" + File.separator + "config.properties";
    public static final String TEST_DATA_EXCEL_PATH = RESOURCES_PATH + File.separator + "testdata" + File.separator + "TestData.xlsx";
    public static final String TEST_DATA_PROPERTIES_PATH = RESOURCES_PATH + File.separator + "testdata" + File.separator + "testdata.properties";
    
    // Reports & Screenshots Paths
    public static final String EXTENT_REPORT_FOLDER_PATH = PROJECT_PATH + File.separator + "test-output" + File.separator + "ExtentReport";
    public static final String SCREENSHOTS_PATH = PROJECT_PATH + File.separator + "test-output" + File.separator + "screenshots";

    // Timeouts
    public static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    public static final Duration EXPLICIT_WAIT = Duration.ofSeconds(20);
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);

    // Default Browser Configuration
    public static final String DEFAULT_BROWSER = "chrome";
}
