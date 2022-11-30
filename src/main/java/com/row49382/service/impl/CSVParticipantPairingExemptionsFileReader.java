package com.row49382.service.impl;

import com.row49382.service.CSVFileReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CSVParticipantPairingExemptionsFileReader extends CSVFileReader<Map<String, String[]>> {
    private char exemptionsDelimiter;

    public CSVParticipantPairingExemptionsFileReader(
            String fileName,
            char delimiter,
            char exemptionsDelimiter) {
        super(fileName, delimiter);
        this.exemptionsDelimiter = exemptionsDelimiter;
    }

    @Override
    public Map<String, String[]> read() throws IOException {
        Map<String, String[]> exemptionsByName = new HashMap<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream(this.fileName)))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                // skip the first line of all csv files so you don't read header values
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] lineData = line.split(Character.toString(this.delimiter));
                String name = lineData[0];
                String[] exemptions = lineData[1].split(Character.toString(this.exemptionsDelimiter));

                exemptionsByName.put(name, exemptions);
            }
        }

        return exemptionsByName;
    }
}
