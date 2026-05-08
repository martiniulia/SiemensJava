package com.trainticket.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    public static final String ADMIN_PASSWORD = "123";
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Warning: Could not load config.properties");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
