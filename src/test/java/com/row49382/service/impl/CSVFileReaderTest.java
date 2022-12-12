package com.row49382.service.impl;

import com.row49382.service.CSVFileReader;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

public abstract class CSVFileReaderTest<T> {
    protected CSVFileReader<T> csvFileReader;

    public abstract void testExpectedCSVIsLoaded() throws IOException;
    public abstract void testInvalidFileThrowsIOException();
    public abstract void testReadIsCachedIfRanOnce() throws IOException;

    protected void assertIOExceptionThrownWhenFileNotFound() {
        Assertions.assertThrows(IOException.class, () -> this.csvFileReader.read());
    }
}
