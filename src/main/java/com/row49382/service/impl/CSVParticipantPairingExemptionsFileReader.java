package com.row49382.service.impl;

import com.row49382.service.CSVFileReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(Character.toString(this.delimiter));
                String name = lineData[0];
                String[] exemptions = lineData[1].split(Character.toString(this.exemptionsDelimiter));

                exemptionsByName.put(name, exemptions);
            }
        }

        return exemptionsByName;
    }
}
