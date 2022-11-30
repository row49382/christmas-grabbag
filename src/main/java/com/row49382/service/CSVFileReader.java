package com.row49382.service;

public abstract class CSVFileReader<T> implements FileReaderService<T> {
    protected String fileName;
    protected char delimiter;
    protected boolean isFirstLine;

    protected CSVFileReader(String fileName, char delimiter) {
        this.fileName = fileName;
        this.delimiter = delimiter;
        this.isFirstLine = true;
    }
}
