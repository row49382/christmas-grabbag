package com.row49382.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CSVParticipantExemptionsFileReaderTest extends CSVFileReaderTest<Map<String, String[]>> {
    private static final String CSV_FILE_NAME = "participant-exemptions-test.csv";
    private static final char DEFAULT_DELIMITER = ',';
    private static final char DEFAULT_EXEMPTION_NAME_COL_DELIMITER = ';';

    @BeforeEach
    public void setup() {
        this.csvFileReader =
                new CSVParticipantExemptionsFileReader(
                        CSV_FILE_NAME,
                        DEFAULT_DELIMITER,
                        DEFAULT_EXEMPTION_NAME_COL_DELIMITER);
    }

    @Test
    @Override
    public void testExpectedCSVIsLoaded() throws IOException {
        Map<String, String[]> expected = Map.of(
                "name1", new String[] { "name2", "name3", "name4" },
                "name2", new String[] { "name1" });

        Map<String, String[]> actual = this.csvFileReader.read();

        for (Map.Entry<String, String[]> expectedEntry : expected.entrySet()) {
            String[] actualValues = actual.get(expectedEntry.getKey());
            assertArrayEquals(expectedEntry.getValue(), actualValues);
        }
    }

    @Test
    @Override
    public void testInvalidFileThrowsIOException() {
        this.csvFileReader = new CSVParticipantExemptionsFileReader(
                "invalidFile.csv",
                DEFAULT_DELIMITER,
                DEFAULT_EXEMPTION_NAME_COL_DELIMITER);

        this.assertIOExceptionThrownWhenFileNotFound();
    }

    @Test
    @Override
    public void testReadIsCachedIfRanOnce() throws IOException {
        Object data = this.csvFileReader.read();
        assertSame(data, this.csvFileReader.read());
    }
}
