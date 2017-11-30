package com.evg.ss.exceptions;

/**
 * @author 4erem6a
 */
public abstract class SSExecutionException extends SSException {
    public SSExecutionException(String message) {
        super(message);
    }
    public SSExecutionException() {
        super();
    }
}