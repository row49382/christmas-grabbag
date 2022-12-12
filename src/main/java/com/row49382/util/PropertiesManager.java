package com.row49382.util;

import com.row49382.exception.PropertiesLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public abstract class PropertiesManager {
    protected final Properties properties;

    protected PropertiesManager(String propertiesFileName) {
        Objects.requireNonNull(propertiesFileName);

        this.properties = new Properties();

        this.loadProperties(propertiesFileName);
    }

    public Properties getProperties() {
        return this.properties;
    }

    private void loadProperties(String propertiesFileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
        Objects.requireNonNull(is);

        try {
            this.getProperties().load(is);
        } catch (IOException e) {
            throw new PropertiesLoadException(e);
        }
    }
}
