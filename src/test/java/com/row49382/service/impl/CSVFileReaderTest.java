package com.row49382.service.impl;

import com.row49382.service.CSVFileReader;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

public abstract class CSVFileReaderTest<T> {
    protected CSVFileReader<T> csvFileReader;

    protected abstract void testExpectedCSVIsLoaded() throws IOException;
    protected abstract void testInvalidFileThrowsIOException();
    protected abstract void testReadIsCachedIfRanOnce() throws IOException;

    protected void assertIOExceptionThrownWhenFileNotFound() {
        Assertions.assertThrows(IOException.class, () -> this.csvFileReader.read());
    }
}
