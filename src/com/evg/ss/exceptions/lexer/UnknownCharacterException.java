package com.evg.ss.exceptions.lexer;

import com.evg.ss.lexer.SourcePosition;

/**
 * @author 4erem6a
 */
public final class UnknownCharacterException extends SSLexerException {
    public UnknownCharacterException(char unknown, SourcePosition position) {
        super(String.format("Unknown character '%c' at %s.", unknown, position));
    }
}