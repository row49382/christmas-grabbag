package com.row49382.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class CSVFileWriter<T> implements FileWritable {
    private static final Logger LOG = LoggerFactory.getLogger(CSVFileWriter.class);
    protected String fileName;
    protected char delimiter;
    protected T data;

    protected CSVFileWriter(String fileName, char delimiter, T data) {
        this.fileName = fileName;
        this.delimiter = delimiter;
        this.data = data;
    }

    @Override
    public void write() throws IOException {
        File targetDirectory = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
        Path resultsDirectory = Paths.get(targetDirectory + "\\results\\");

        if (!Files.exists(resultsDirectory)) {
            Files.createDirectories(resultsDirectory);
        }

        String resultsFilePath = resultsDirectory + "\\" + this.fileName + "\\";
        LOG.debug("Writing results file to location {}", resultsFilePath);

        Files.write(
                Paths.get(resultsFilePath),
                this.writeDataToCSV().getBytes());
    }

    protected abstract String writeDataToCSV();
}
