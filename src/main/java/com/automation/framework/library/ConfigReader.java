package com.automation.framework.library;

import com.automation.framework.constants.FrameworkConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Utility class to read properties from config.properties file.
 * Supports command-line property overrides (e.g. -Dbrowser=firefox).
 */
public final class ConfigReader {

    private static Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            LoggerUtil.error("Failed to load configuration properties from: " + FrameworkConstants.CONFIG_FILE_PATH, e);
            throw new RuntimeException("Property file not found or unable to read at " + FrameworkConstants.CONFIG_FILE_PATH, e);
        }
    }

    private ConfigReader() {}

    /**
     * Retrieves the property value for a given key.
     * Checks System properties first (command line overrides), then properties file.
     *
     * @param key Property key name
     * @return Property value as String
     */
    public static String getProperty(String key) {
        String systemValue = System.getProperty(key);
        if (Objects.nonNull(systemValue) && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }

        String value = properties.getProperty(key);
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            LoggerUtil.warn("Property '" + key + "' was not found in config.properties or system properties.");
            return null;
        }
        return value.trim();
    }

    /**
     * Retrieves the property value with a default fallback if key is absent.
     *
     * @param key          Property key name
     * @param defaultValue Default value to return if key is null/empty
     * @return Property value or defaultValue
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (Objects.isNull(value) || value.trim().isEmpty()) ? defaultValue : value;
    }

    /**
     * Retrieves a boolean property.
     *
     * @param key Property key name
     * @return boolean value
     */
    public static boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }

    /**
     * Retrieves an integer property with a fallback default.
     *
     * @param key          Property key
     * @param defaultValue Fallback integer
     * @return parsed int or default
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return (value != null) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            LoggerUtil.warn("Invalid integer for key '" + key + "'. Using default: " + defaultValue);
            return defaultValue;
        }
    }
}
