package com.innowise.multithreading.reader;

import com.innowise.multithreading.exception.CustomAutoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CustomTextReaderImpl implements CustomTextReader {

    private static final Logger log = LogManager.getLogger(CustomTextReaderImpl.class);

    @Override
    public List<String> readAllLinesFromFile(String path) throws CustomAutoException {
        if (path == null) {
            log.error("Path is null");
            throw new CustomAutoException("Path cannot be null");
        }

        try {
            Path target = Paths.get(path);
            List<String> lines = Files.readAllLines(target);
            log.info("File read successfully: {}", path);
            return lines;

        } catch (IOException exception) {
            log.error("Failed to read file: {}", path, exception);
            throw new CustomAutoException("Failed to read file: " + path, exception);
        }
    }

}