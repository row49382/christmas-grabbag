package com.row49382.service.impl;

import com.row49382.service.CSVFileReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVParticipantExemptionsFileReader extends CSVFileReader<Map<String, String[]>> {
    private char exemptionsDelimiter;

    public CSVParticipantExemptionsFileReader(
            String fileName,
            char delimiter,
            char exemptionsDelimiter) {
        super(fileName, delimiter);
        this.exemptionsDelimiter = exemptionsDelimiter;
        this.data = new HashMap<>();
    }

    @Override
    public Map<String, String[]> read() throws IOException {
        if (this.isDataLoaded()) {
            return this.data;
        }

        return super.read();
    }

    @Override
    public void readLine(String line) {
        String[] lineData = line.split(Character.toString(this.delimiter));
        String name = lineData[0];
        String[] exemptions = lineData[1].split(Character.toString(this.exemptionsDelimiter));

        this.data.put(name, exemptions);
    }

    @Override
    public boolean isDataLoaded() {
        return !this.data.isEmpty();
    }
}