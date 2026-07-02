package com.innowise.arrays.reader;

import com.innowise.arrays.exception.CustomArrayException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CustomArrayReaderImpl implements CustomArrayReader {

    private static final Logger log = LogManager.getLogger(CustomArrayReaderImpl.class);

    @Override
    public List<String> readAllLinesFromFile(String path) throws CustomArrayException {
        if (path == null) {
            log.error("Path is null");
            throw new CustomArrayException("Path cannot be null");
        }

        try {
            Path target = Paths.get(path);
            List<String> lines = Files.readAllLines(target);
            log.info("File read successfully: {}", path);
            return lines;

        } catch (IOException exception) {
            log.error("Failed to read file: {}", path, exception);
            throw new CustomArrayException("Failed to read file: " + path, exception);
        }
    }

}