package com.innowise.arrays.reader;

import com.innowise.arrays.exception.CustomArrayException;

import java.util.List;

public interface CustomArrayReader {

    List<String> readAllLinesFromFile(String path) throws CustomArrayException;
}
