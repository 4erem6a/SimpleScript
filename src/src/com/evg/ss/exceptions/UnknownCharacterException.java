package com.evg.ss.exceptions;

import com.evg.ss.lexer.Position;

/**
 * @author 4erem6a
 */
public final class UnknownCharacterException extends SSLexerException {
    public UnknownCharacterException(char unknown, Position position)
    {
        super(String.format("Unknown character '%c' at %s.", unknown, position));
    }
}