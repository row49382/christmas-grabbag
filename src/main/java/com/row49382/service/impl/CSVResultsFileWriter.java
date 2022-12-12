package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.CSVFileWriter;

import java.util.List;

public class CSVResultsFileWriter extends CSVFileWriter<List<Participant>> {
    public CSVResultsFileWriter(String fileName, char delimiter, List<Participant> data) {
        super(fileName, delimiter, data);
    }

    @Override
    protected String writeDataToCSV() {
        String headers = String.format("participant%sreceiver", this.delimiter);
        return this.data.stream()
                .map(p -> {
                    String receiver = p.getReceiver() == null ? "null" : p.getReceiver().getName();
                    return p.getName() + this.delimiter + receiver;
                })
                .reduce(
                        headers,
                        (acc, curr) -> acc + System.lineSeparator() + curr);
    }
}
