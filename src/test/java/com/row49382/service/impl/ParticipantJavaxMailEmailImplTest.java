package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.exception.EmailServiceException;
import com.row49382.service.Emailable;
import com.row49382.util.PropertiesManager;
import com.row49382.util.impl.ApplicationPropertiesManager;
import com.row49382.util.impl.MailPropertiesManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import test_util.factory.ParticipantsFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ParticipantJavaxMailEmailImplTest {
    private static Stream<Arguments> getParticipantsAndVerificationTimesExpectations() throws IOException {
        List<Participant> emptyParticipants = ParticipantsFactory.of(ParticipantsFactory.ParticipantTestOption.EMPTY);
        List<Participant> singleParticipants = ParticipantsFactory.of(ParticipantsFactory.ParticipantTestOption.SINGLE);
        List<Participant> multipleParticipants = ParticipantsFactory.of(ParticipantsFactory.ParticipantTestOption.MULTIPLE);

        return Stream.of(
                Arguments.of(emptyParticipants, emptyParticipants.size()),
                Arguments.of(singleParticipants, singleParticipants.size()),
                Arguments.of(multipleParticipants, multipleParticipants.size())
        );
    }

    private ApplicationPropertiesManager applicationPropertiesManager;

    private PropertiesManager mailPropertiesManager;

    @BeforeEach
    public void setup() {
        this.applicationPropertiesManager = new ApplicationPropertiesManager();
        this.mailPropertiesManager = new MailPropertiesManager();
    }

    @ParameterizedTest
    @MethodSource("getParticipantsAndVerificationTimesExpectations")
    void testNumberOfEmailsSentAreSameAsParticipantsSize(List<Participant> participants, int expectedEmailsSent) {
        Emailable emailService =  new ParticipantJavaxMailEmailImpl(
                this.applicationPropertiesManager,
                this.mailPropertiesManager,
                participants);

        try (MockedStatic<Transport> transportMock = Mockito.mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class)))
                    .thenAnswer((Answer<Void>) invocation -> null);

            emailService.send();
            transportMock.verify(() -> Transport.send(any(Message.class)), times(expectedEmailsSent));
        } catch (EmailServiceException e) {
            fail("Test failed because of error while sending message", e);
        }
    }

    @Test
    void testWhenParticipantsHasOneOrMoreNullReceiverErrorOccursDuringSend() {
        assertThrows(
                EmailServiceException.class,
                () -> new ParticipantJavaxMailEmailImpl(
                        this.applicationPropertiesManager,
                        this.mailPropertiesManager,
                        ParticipantsFactory.of(ParticipantsFactory.ParticipantTestOption.MULTIPLE_NULL_RECEIVERS))
                        .send());
    }

    @Test
    void verifyWhenTransportThrowsMessagingExceptionEmailServiceExceptionIsThrownInstead() throws IOException {
        Emailable emailService =  new ParticipantJavaxMailEmailImpl(
                this.applicationPropertiesManager,
                this.mailPropertiesManager,
                ParticipantsFactory.of(ParticipantsFactory.ParticipantTestOption.MULTIPLE));

        try (MockedStatic<Transport> transportMock = Mockito.mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class)))
                    .thenThrow(MessagingException.class);

            assertThrows(
                    EmailServiceException.class,
                    emailService::send);
        }
    }
}
