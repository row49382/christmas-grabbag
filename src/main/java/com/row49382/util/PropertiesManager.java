package com.row49382.util;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public abstract class PropertiesManager {
    protected final Properties properties;

    protected PropertiesManager(String propertiesFileName) {
        Objects.requireNonNull(propertiesFileName);

        this.properties = new Properties();

        try {
            this.properties.load(getClass().getClassLoader().getResourceAsStream(propertiesFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Properties getProperties() {
        return this.properties;
    }
}
