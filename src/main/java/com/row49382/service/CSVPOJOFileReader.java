package com.row49382.service;

import java.util.List;

public abstract class CSVPOJOFileReader<T, V> extends CSVFileReader<V> {
    protected CSVPOJOFileReader(String fileName, char delimiter) {
        super(fileName, delimiter);
    }

    protected abstract T mapToPOJO(String[] row);
}
