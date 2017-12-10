package com.evg.ss.exceptions.lexer;

import com.evg.ss.lexer.SourcePosition;
import com.evg.ss.lexer.TokenType;

/**
 * @author 4erem6a
 */
public final class InvalidTokenDefinitionException extends SSLexerException {
    public InvalidTokenDefinitionException(TokenType type, SourcePosition position) {
        super(String.format("Invalid %s token definition at %s.", type.toString().toLowerCase(), position));
    }
}