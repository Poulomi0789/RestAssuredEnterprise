package utils;

import exceptions.FrameworkException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties = new Properties();

    static {
        // 1. Fetch environment from System Property (-Denv=qa)
        String env = System.getProperty("env");

        // 2. Default to 'qa' if no environment is provided
        if (env == null || env.trim().isEmpty()) {
            env = "qa";
            System.out.println("[CONFIG] No environment specified via -Denv. Defaulting to: qa");
        } else {
            System.out.println("[CONFIG] Loading configuration for environment: " + env);
        }

        // 3. Define the path to the properties file
        String configPath = "src/test/resources/config/" + env + ".properties";

        // 4. Load the file
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
        } catch (IOException e) {
            // Use FrameworkException instead of generic RuntimeException
            throw new FrameworkException("FATAL: Configuration file not found for environment: " + env, e);
        }
    }

    /**
     * Retrieves a value from the loaded properties file.
     * @param key The property key (e.g., "base.url")
     * @return The property value
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            System.err.println("[WARNING] Property key '" + key + "' not found in config file.");
        }
        return value;
    }
}