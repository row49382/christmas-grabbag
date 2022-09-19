package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.CSVPOJOFileReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParticipantFileReader extends CSVPOJOFileReader<Participant, List<Participant>> {

    public CSVParticipantFileReader(String fileName, char delimiter) {
        super(fileName, delimiter);
    }

    public List<Participant> read() throws IOException {
        List<Participant> participants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
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
