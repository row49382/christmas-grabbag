package com.row49382.util.impl;

import com.row49382.util.PropertiesManager;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public abstract class PropertiesManagerImplBaseTest {
    protected PropertiesManager propertiesManager;
    public abstract void testPropertyValueLoadedMatchesExpectation(
            String propertyKey,
            Object propertyValue);
}
