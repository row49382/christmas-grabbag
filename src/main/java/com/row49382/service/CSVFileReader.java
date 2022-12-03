package com.row49382.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class CSVFileReader<T> implements FileReadable<T> {
    protected String fileName;
    protected char delimiter;
    protected T data;

    protected CSVFileReader(String fileName, char delimiter) {
        this.fileName = fileName;
        this.delimiter = delimiter;
    }

    @Override
    public T read() throws IOException {
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

                this.readLine(line);
            }
        }

        return this.data;
    }

    protected abstract void readLine(String line);

    protected abstract boolean isDataLoaded();
}
