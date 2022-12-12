package com.row49382.util.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailPropertiesManagerTest extends PropertiesManagerImplBaseTest {
    public static Stream<Arguments> getPropertyKeyAndExpectedValue() {
        return Stream.of(
                Arguments.of("mail.smtp.auth", "true"),
                Arguments.of("mail.smtp.starttls.enable", "true"),
                Arguments.of("mail.smtp.host", "smtp.gmail.com"),
                Arguments.of("mail.smtp.port", "587"));
    }

    @BeforeEach
    public void setup() {
        this.propertiesManager = new MailPropertiesManager();
    }

    @Override
    @ParameterizedTest
    @MethodSource("getPropertyKeyAndExpectedValue")
    public void testPropertyValueLoadedMatchesExpectation(String propertyKey, Object propertyValue) {
        assertEquals(propertyValue, this.propertiesManager.getProperties().get(propertyKey));
    }
}
