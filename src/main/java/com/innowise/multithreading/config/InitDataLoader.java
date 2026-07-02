package com.innowise.multithreading.config;

import com.innowise.multithreading.exception.CustomAutoException;

public interface InitDataLoader {
    InitData load(String path) throws CustomAutoException;
}
