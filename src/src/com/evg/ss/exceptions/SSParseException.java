package com.evg.ss.exceptions;

/**
 * @author 4erem6a
 */
public abstract class SSParseException extends SSException {
    public SSParseException(String message) {
        super(message);
    }
    public SSParseException() {
        super();
    }
}