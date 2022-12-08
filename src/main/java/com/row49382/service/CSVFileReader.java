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
        if (!exists()) {
            throw new IOException(String.format("File %s was not found in resources folder", this.fileName));
        }

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

                this.loadLine(line);
            }
        }

        return this.data;
    }

    @Override
    public boolean exists() {
        return getClass().getClassLoader().getResource(this.fileName) != null;
    }

    protected abstract void loadLine(String line);

    protected abstract boolean isDataLoaded();
}
