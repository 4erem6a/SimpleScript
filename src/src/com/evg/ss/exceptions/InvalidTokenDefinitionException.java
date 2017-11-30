package com.evg.ss.exceptions;

import com.evg.ss.lexer.Position;
import com.evg.ss.lexer.TokenType;

/**
 * @author 4erem6a
 */
public final class InvalidTokenDefinitionException extends SSLexerException {
    public InvalidTokenDefinitionException(TokenType type, Position position) {
        super(String.format("Invalid %s token definition at %s.", type.toString().toLowerCase(), position));
    }
}