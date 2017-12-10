package com.evg.ss.exceptions.execution;

import com.evg.ss.exceptions.SSException;

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