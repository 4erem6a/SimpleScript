package com.evg.ss.lexer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLexer {

    protected final String source;
    protected final int sourceLength;

    protected final List<Token> tokens;

    protected int position;

    protected AbstractLexer(String source) {
        this.source = source;
        sourceLength = source.length();

        tokens = new ArrayList<>();
    }

    public abstract List<Token> tokenize();

    protected char peekLocal(int position, String string) {
        if (position >= string.length()) return '\0';
        return string.charAt(position);
    }

    protected char next() {
        position++;
        return peek(0);
    }

    protected char peek(int relativePosition) {
        final int position = this.position + relativePosition;
        if (position >= sourceLength) return '\0';
        return source.charAt(position);
    }

    protected SourcePosition calculatePosition() {
        String substring = source.substring(0, position);
        final int line = (int) substring.chars().filter(c -> c == '\n').count() + 1;
        if (source.indexOf('\n') > position)
            substring = source.substring(0, position);
        else substring = source.substring(substring.lastIndexOf('\n') + 1, position);
        final int col = substring.length() + 1;
        return new SourcePosition(line, col);
    }

    protected void addToken(TokenTypes type) {
        addToken(type, "");
    }

    protected void addToken(TokenTypes type, String text) {
        tokens.add(new Token(type, text, calculatePosition()));
    }

}