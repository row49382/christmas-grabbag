package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.exception.PairingGenerateException;
import com.row49382.service.FileReadable;
import com.row49382.service.PairingGeneratable;
import com.row49382.util.impl.ApplicationPropertiesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantWithExemptionsPairingGeneratorTest {
    private static final String PARTICIPANTS_TEST_CSV_FILE_NAME =
            "participants-test-multiple-exemptions.csv";
    private static final String PARTICIPANTS_EXEMPTIONS_TEST_CSV_FILE_NAME =
            "participant-exemptions-test-multiple-exemptions.csv";
    private static final String PARTICIPANTS_TEST_SAME_EXEMPTIONS_CSV_FILE_NAME =
            "participants-test-same-exemptions.csv";
    private static final String PARTICIPANTS_EXEMPTIONS_TEST_SAME_EXEMPTIONS_CSV_FILE_NAME =
            "participant-exemptions-test-same-exemptions.csv";
    private static final char DEFAULT_DELIMITER = ',';
    private static final char DEFAULT_NAME_EXEMPTIONS_DELIMITER = ';';

    private ApplicationPropertiesManager applicationPropertiesManager;

    @BeforeEach
    public void setup() {
        this.applicationPropertiesManager = new ApplicationPropertiesManager();
    }

    @Test
    void testExpectedGeneratedRandomPairsDoesNotChooseAnyExemptions() throws IOException, PairingGenerateException {
        FileReadable<List<Participant>> participantFileReader = new CSVParticipantFileReader(
                PARTICIPANTS_TEST_CSV_FILE_NAME,
                DEFAULT_DELIMITER);

        FileReadable<Map<String, String[]>> exemptionsFileReader = new CSVParticipantExemptionsFileReader(
                PARTICIPANTS_EXEMPTIONS_TEST_CSV_FILE_NAME,
                DEFAULT_DELIMITER,
                DEFAULT_NAME_EXEMPTIONS_DELIMITER);

        List<Participant> participants = participantFileReader.read();
        Map<String, String[]> exemptionsByParticipantName = exemptionsFileReader.read();

        PairingGeneratable pairingGenerator = new ParticipantWithExemptionsPairingGenerator(
                participants,
                exemptionsByParticipantName,
                new Random(),
                this.applicationPropertiesManager);

        pairingGenerator.generate();

        this.assertParticipantPairingsDoNotHaveAnExemption(participants, exemptionsByParticipantName);
    }

    @Test
    void testWhenRetryCountExceededExceptionIsThrown() throws IOException {
        ApplicationPropertiesManager applicationPropertiesManagerMock = mock(ApplicationPropertiesManager.class);
        when(applicationPropertiesManagerMock.getPairingMaxRetryCount()).thenReturn(5);

        FileReadable<List<Participant>> participantFileReader = new CSVParticipantFileReader(
                PARTICIPANTS_TEST_SAME_EXEMPTIONS_CSV_FILE_NAME,
                DEFAULT_DELIMITER);

        FileReadable<Map<String, String[]>> exemptionsFileReader = new CSVParticipantExemptionsFileReader(
                PARTICIPANTS_EXEMPTIONS_TEST_SAME_EXEMPTIONS_CSV_FILE_NAME,
                DEFAULT_DELIMITER,
                DEFAULT_NAME_EXEMPTIONS_DELIMITER);

        List<Participant> participants = participantFileReader.read();
        Map<String, String[]> exemptionsByParticipantName = exemptionsFileReader.read();

        PairingGeneratable pairingGenerator = new ParticipantWithExemptionsPairingGenerator(
                participants,
                exemptionsByParticipantName,
                new Random(),
                applicationPropertiesManagerMock);

        assertThrows(PairingGenerateException.class, pairingGenerator::generate);
    }

    private void assertParticipantPairingsDoNotHaveAnExemption(List<Participant> participants, Map<String, String[]> exemptionsByParticipantName) {
        for (Participant participant : participants) {
            String[] exemptions = exemptionsByParticipantName.get(participant.getName());

            if (exemptions != null) {
                assertTrue(Arrays.stream(exemptions).noneMatch(e -> e.equals(participant.getReceiver().getName())));
            }
        }
    }
}
