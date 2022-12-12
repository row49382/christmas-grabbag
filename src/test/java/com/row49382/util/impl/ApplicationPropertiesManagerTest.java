package com.row49382.util.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationPropertiesManagerTest extends PropertiesManagerImplBaseTest {
    private static final ApplicationPropertiesManager applicationPropertiesManager =
            new ApplicationPropertiesManager();

    public static Stream<Arguments> getPropertyKeyAndExpectedValue() {
        return Stream.of(
                Arguments.of("officiant.email", applicationPropertiesManager.getOfficiantEmail()),
                Arguments.of("application.email.account.username", applicationPropertiesManager.getApplicationFromEmailUserName()),
                Arguments.of("application.email.account.password", applicationPropertiesManager.getApplicationFromEmailPassword()),
                Arguments.of("application.email.account.from_email", applicationPropertiesManager.getApplicationFromEmailAddress()),
                Arguments.of("application.pairing.maxretrycount", Integer.toString(applicationPropertiesManager.getPairingMaxRetryCount())),
                Arguments.of("application.log.level", applicationPropertiesManager.getLogLevel()),
                Arguments.of("application.csv.exemptions.file_name", applicationPropertiesManager.getExemptionsCSVFileName()),
                Arguments.of("application.csv.exemptions.name_exemptions_delimiter", Character.toString(applicationPropertiesManager.getExemptionsCSVNameExemptionsDelimiter())),
                Arguments.of("application.csv.participants.file_name", applicationPropertiesManager.getParticipantsCSVFileName()),
                Arguments.of("application.csv.delimiter", Character.toString(applicationPropertiesManager.getCsvDelimiter())),
                Arguments.of("application.email.do_send", Boolean.toString(applicationPropertiesManager.isSendEmail())));
    }

    @Override
    @ParameterizedTest
    @MethodSource("getPropertyKeyAndExpectedValue")
    public void testPropertyValueLoadedMatchesExpectation(String propertyKey, Object propertyValue) {
        assertEquals(propertyValue, applicationPropertiesManager.getProperties().get(propertyKey));
    }
}
