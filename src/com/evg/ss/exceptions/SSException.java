package com.evg.ss.exceptions;

/**
 * @author 4erem6a
 */
public abstract class SSException extends RuntimeException {
    public SSException(String message) {
        super(message);
    }

    public SSException() {
        super();
    }
}
