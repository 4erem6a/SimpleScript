package com.evg.ss.exceptions.lexer;

import com.evg.ss.exceptions.SSException;

/**
 * @author 4erem6a
 */
public abstract class SSLexerException extends SSException {
    public SSLexerException(String message) {
        super(message);
    }

    public SSLexerException() {
        super();
    }
}
