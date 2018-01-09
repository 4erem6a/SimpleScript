package com.evg.ss.exceptions.parser;

import com.evg.ss.exceptions.SSException;

/**
 * @author 4erem6a
 */
public abstract class SSParserException extends SSException {
    public SSParserException(String message) {
        super(message);
    }

    public SSParserException() {
        super();
    }
}