package com.row49382.service.impl;

import com.row49382.domain.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CSVParticipantFileReaderTest extends CSVFileReaderTest<List<Participant>> {
    private static final String CSV_FILE_NAME = "participants-test-multiple.csv";
    private static final char DEFAULT_DELIMITER = ',';

    @BeforeEach
    public void setup() {
        this.csvFileReader = new CSVParticipantFileReader(CSV_FILE_NAME, DEFAULT_DELIMITER);
    }

    @Override
    @Test
    public void testExpectedCSVIsLoaded() throws IOException {
        List<Participant> expected = List.of(
                new Participant("participant1", "email1@email.com"),
                new Participant("participant2", "email2@email.com"),
                new Participant("participant3", "email3@email.com")
        );

        assertIterableEquals(expected, this.csvFileReader.read());
    }

    @Override
    @Test
    public void testInvalidFileThrowsIOException() {
        this.csvFileReader = new CSVParticipantFileReader("invalidFile.csv", DEFAULT_DELIMITER);
        this.assertIOExceptionThrownWhenFileNotFound();
    }

    @Override
    @Test
    public void testReadIsCachedIfRanOnce() throws IOException {
        List<Participant> participants = this.csvFileReader.read();
        assertSame(participants, this.csvFileReader.read());
    }
}
