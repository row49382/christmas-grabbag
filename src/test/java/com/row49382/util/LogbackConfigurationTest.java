package com.row49382.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogbackConfigurationTest {
    private static Stream<Arguments> getInvalidLogLevels() {
       return Stream.of(
                null,
                Arguments.of("invalidLogLevel"));
    }

    private static Stream<Arguments> getValidLogLevels() {
        return Stream.of(
                Arguments.of(Level.OFF),
                Arguments.of(Level.ERROR),
                Arguments.of(Level.WARN),
                Arguments.of(Level.INFO),
                Arguments.of(Level.DEBUG),
                Arguments.of(Level.TRACE),
                Arguments.of(Level.ALL));
    }

    @AfterEach
    public void cleanup() {
        LogbackConfiguration.setLevel(Level.DEBUG.levelStr);
    }

    @ParameterizedTest
    @MethodSource("getInvalidLogLevels")
    void testWhenLogLevelNotValidThatDebugLevelIsUsed(String invalidLogLevel) {
        LogbackConfiguration.setLevel(invalidLogLevel);
        assertEquals(Level.DEBUG, this.getRootLogger().getLevel());
    }

    @ParameterizedTest
    @MethodSource("getValidLogLevels")
    void testSetLevelSetsLogbackLevel(Level logLevel) {
        LogbackConfiguration.setLevel(logLevel.levelStr);
        assertEquals(logLevel, this.getRootLogger().getLevel());
    }

    private Logger getRootLogger() {
        return (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }
}
