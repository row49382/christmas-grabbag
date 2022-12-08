package com.row49382.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {
    private static Stream<Arguments> getToStringExpectedStringAndReceiverArguments() {
        return Stream.of(
                Arguments.of("name has null", null),
                Arguments.of("name has henry", new Participant("henry", "email@email.com")));
    }

    private static Stream<Arguments> getCompareToParticipantsAndIsZeroResultArguments() {
        return Stream.of(
                Arguments.of(new Participant("name1", "email1"), new Participant("name1", "email1"), true),
                Arguments.of(new Participant("name1", "email1"), new Participant("name2", "email1"), false),
                Arguments.of(new Participant("name1", "email1"), new Participant("name1", "email2"), false),
                Arguments.of(new Participant("name1", "email1"), new Participant("name2", "email2"), false)
        );
    }

    private final String name = "name";
    private final String email = "email@email.com";

    private Participant participant;

    @BeforeEach
    public void setup() {
        this.participant = new Participant(this.name, this.email);
    }

    @Test
    void testGettersReturnFields() {
        assertEquals(this.name, this.participant.getName());
        assertEquals(this.email, this.participant.getEmail());
        assertFalse(this.participant.isPicked());
        assertNull(this.participant.getReceiver());
    }

    @Test
    void testSetReceiverReturnsValueFromGetter() {
        Participant receiver = new Participant("name1", this.email);
        this.participant.setReceiver(receiver);

        assertEquals(receiver, this.participant.getReceiver());
    }

    @Test
    void testSetPickedReturnsValueFromGetter() {
        this.participant.setPicked(true);

        assertTrue(this.participant.isPicked());
    }

    @ParameterizedTest
    @MethodSource("getToStringExpectedStringAndReceiverArguments")
    void testToStringReturnsExpectedString(String expected, Participant receiver) {
        this.participant.setReceiver(receiver);
        assertEquals(expected, this.participant.toString());
    }

    @ParameterizedTest
    @MethodSource("getCompareToParticipantsAndIsZeroResultArguments")
    void testCompareToExpectations(Participant a, Participant b, boolean areEqual) {
        if (areEqual) {
            assertEquals(a, b);
        } else {
            assertNotEquals(a, b);
        }
    }
}
