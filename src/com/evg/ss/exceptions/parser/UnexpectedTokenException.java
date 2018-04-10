package com.evg.ss.exceptions.parser;

import com.evg.ss.lexer.SourcePosition;
import com.evg.ss.lexer.Token;
import com.evg.ss.lexer.TokenTypes;

/**
 * @author 4erem6a
 */
public final class UnexpectedTokenException extends SSParserException {
    public UnexpectedTokenException(TokenTypes expected, Token got) {
        super(String.format("Unexpected token at %s: expected '%s', but got '%s'.", got.getPosition(), expected.getName(), got.getType().getName()));
    }

    public UnexpectedTokenException(Token got) {
        super(String.format("Unexpected token at %s: '%s'.", got.getPosition(), got.getType().getName()));
    }

    public UnexpectedTokenException(SourcePosition position, String word) {
        super(String.format("Unexpected token at %s: '%s'.", position, word));
    }
}