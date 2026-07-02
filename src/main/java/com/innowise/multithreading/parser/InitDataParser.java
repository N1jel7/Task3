package com.innowise.multithreading.parser;

import com.innowise.multithreading.entity.InitData;
import com.innowise.multithreading.exception.CustomAutoException;

public interface InitDataParser {

    InitData parse(String path) throws CustomAutoException;

}
