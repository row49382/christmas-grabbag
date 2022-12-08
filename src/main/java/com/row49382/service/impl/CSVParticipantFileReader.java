package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.CSVFileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParticipantFileReader extends CSVFileReader<List<Participant>> {

    public CSVParticipantFileReader(String fileName, char delimiter) {
        super(fileName, delimiter);
        this.data = new ArrayList<>();
    }

    public List<Participant> read() throws IOException {
        if (this.isDataLoaded()) {
            return this.data;
        }

        return super.read();
    }

    @Override
    public void loadLine(String line) {
        String[] lineData = line.split(Character.toString(this.delimiter));
        String name = lineData[0];
        String email = lineData[1];

        this.data.add(new Participant(name, email));
    }

    @Override
    public boolean isDataLoaded() {
        return !this.data.isEmpty();
    }
}
