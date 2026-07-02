package com.innowise.multithreading.exception;

public class CustomAutoException extends Exception{
    public CustomAutoException(String message) {
        super(message);
    }

    public CustomAutoException(String message, Throwable cause) {
        super(message, cause);
    }
}
