package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.CSVPOJOFileReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVParticipantFileReader extends CSVPOJOFileReader<Participant, List<Participant>> {

    public CSVParticipantFileReader(String fileName, char delimiter) {
        super(fileName, delimiter);
    }

    public List<Participant> read() throws IOException {
        List<Participant> participants = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream(this.fileName)))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                // skip the first line of all csv files, so you don't read header values
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] lineData = line.split(Character.toString(this.delimiter));
                participants.add(this.mapToPOJO(lineData));
            }
        }

        return participants;
    }

    @Override
    protected Participant mapToPOJO(String[] row) {
        String name = row[0];
        String email = row[1];

        return new Participant(name, email);
    }
}
