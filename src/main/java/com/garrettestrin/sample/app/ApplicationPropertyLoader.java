package com.garrettestrin.sample.app;

import com.google.common.io.Resources;
import lombok.extern.jbosslog.JBossLog;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

/**
 * Load dropwizard configuration via system environment or properties
 */
@JBossLog
public class ApplicationPropertyLoader {

    private static final String SYSTEM_PROPERTIES_FILENAME = "PROPERTIES_FILENAME";


    /**
     * - parameter = PROPERTIES_FILENAME from System Property (file -> resource)
     * - parameter = PROPERTIES_FILENAME from System Environment (file->resource)
     */
    public static void loadProperties() throws IOException {
        Properties properties = new ApplicationPropertyLoader()
                .loadPropertiesConfiguration(
                        System.getProperty(SYSTEM_PROPERTIES_FILENAME),
                        System.getenv(SYSTEM_PROPERTIES_FILENAME));
        if (properties == null) {
            throw new IOException("Cannot load the configuration, PROPERTIES_FILENAME must be provided");
        }
        properties.stringPropertyNames()
                .forEach((name) -> System.setProperty("DW_" + name, properties.getProperty(name)));
    }

    /**
     * Load configuration with prioritization, the first resource in parameter keep the highest priority
     */
    private Properties loadPropertiesConfiguration(String... configurations) throws IOException {
        Properties properties = null;
        for (String resourceName : configurations) {
            Properties loadedProperties = loadResourceProperty(resourceName);
            properties = merge(properties, loadedProperties);
        }

        return properties;
    }

    private Properties loadResourceProperty(String resourceName) throws IOException {
        if (resourceName == null) {
            return null;
        }
        if (resourceName.startsWith("classpath:")) {
            return tryGetResource(StringUtils.removeFirst(resourceName, "classpath:"));
        }

        // otherwise try with file then resource
        Properties properties = loadProperty(new File(resourceName));
        if (properties == null) {
            return tryGetResource(resourceName);
        }
        return properties;
    }

    private Properties loadProperty(File configFile) throws IOException {
        if (configFile == null) {
            return null;
        }

        Properties props = new Properties();
        if (!configFile.exists()) {
            log.warnf("Configuration file '%s' does not exist. Ignore.", configFile);
            return null;
        }

        try (InputStream inputStream = new FileInputStream(configFile)) {
            props.load(inputStream);
            log.infof("Configuration found '%s'", configFile);
        }
        return props;
    }

    private Properties merge(Properties firstProperties, Properties secondProperties) {
        if (firstProperties == null) {
            return secondProperties;
        }
        if (secondProperties == null) {
            return firstProperties;
        }

        secondProperties.putAll(firstProperties);
        return secondProperties;
    }

    private Properties tryGetResource(String resourceName) throws IOException {
        try {
            Properties properties = new Properties();
            URL url = Resources.getResource(resourceName);
            log.infof("Configuration found '%s'", url);
            try (InputStreamReader isr = new InputStreamReader(url.openConnection().getInputStream())) {
                properties.load(isr);
            }
            return properties;
        } catch (IllegalArgumentException iae) {
            log.warnf("Configuration resource '%s' does not exist. Ignore.", resourceName);
            return null;
        }
    }
}
