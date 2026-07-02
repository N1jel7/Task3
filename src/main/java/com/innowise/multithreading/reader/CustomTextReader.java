package com.innowise.multithreading.reader;

import com.innowise.multithreading.exception.CustomAutoException;

import java.util.List;

public interface CustomTextReader {

    List<String> readAllLinesFromFile(String path) throws CustomAutoException;

}
