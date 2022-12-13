package com.row49382.service.impl;

import com.row49382.service.CSVFileReader;
import com.row49382.service.FileReadable;
import com.row49382.service.FileWritable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test_util.factory.ParticipantsFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CSVResultsFileWriterTest {
    private static final File TARGET_DIRECTORY =
            new File(CSVResultsFileWriter.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath()).getParentFile();

    private static final String ACTUAL_RESULT_FILE_NAME = "participants-test-result-output.csv";
    private static final String TEST_RESULT_SUB_DIRECTORY = File.separator + "results" + File.separator + "test" + File.separator;
    private static final String ACTUAL_RESULT_TEST_SUB_DIRECTORY = TARGET_DIRECTORY + TEST_RESULT_SUB_DIRECTORY;
    private static final String RESULTS_FILE_PATH = ACTUAL_RESULT_TEST_SUB_DIRECTORY + ACTUAL_RESULT_FILE_NAME;
    private static final String CSV_TEST_RESULTS_FILE_NAME = "participants-test-multiple-results.csv";
    private static final char DEFAULT_DELIMITER = ',';

    @BeforeEach
    public void setup() {
        if (Files.exists(Paths.get(ACTUAL_RESULT_TEST_SUB_DIRECTORY))) {
            FileUtils.deleteQuietly(new File(ACTUAL_RESULT_TEST_SUB_DIRECTORY));
        }
    }

    private final FileReadable<String> csvTestResultReader = new CSVFileReader<>(CSV_TEST_RESULTS_FILE_NAME, DEFAULT_DELIMITER) {
        @Override
        public String read() throws IOException {
            this.data = "";

            if (this.isDataLoaded()) {
                return this.data;
            }

            super.read();
            // remove last newline character that was added
            return this.data.replaceAll("[\r\n]$", "").replaceAll("[\r]$", "");
        }

        @Override
        public boolean exists() {
            return super.exists();
        }

        @Override
        protected void loadLine(String line) {
            this.data += line + System.lineSeparator();
        }

        @Override
        protected boolean isDataLoaded() {
            return !StringUtils.isEmpty(this.data);
        }
    };

    @Test
    void testExpectedCSVIsWritten() throws IOException {
        String expectedCSV = this.csvTestResultReader.read();
        FileWritable resultsWriter = new CSVResultsFileWriter(
                ACTUAL_RESULT_FILE_NAME,
                DEFAULT_DELIMITER,
                ParticipantsFactory.of(ParticipantsFactory.ParticipantTestOption.MULTIPLE),
                TEST_RESULT_SUB_DIRECTORY);

        resultsWriter.write();
        String actualCSV = Files.readAllLines(Paths.get(RESULTS_FILE_PATH))
                .stream()
                .skip(1)
                .collect(Collectors.joining(System.lineSeparator()));

        assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testCreateResultsDirectoryIfDoesNotExist() throws IOException {
        FileWritable resultsWriter = new CSVResultsFileWriter(
                ACTUAL_RESULT_FILE_NAME,
                DEFAULT_DELIMITER,
                ParticipantsFactory.of(ParticipantsFactory.ParticipantTestOption.MULTIPLE),
                TEST_RESULT_SUB_DIRECTORY);
        resultsWriter.write();

        assertTrue(Files.exists(Paths.get(ACTUAL_RESULT_TEST_SUB_DIRECTORY)));
    }
}
