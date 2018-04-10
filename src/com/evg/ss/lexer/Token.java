package com.evg.ss.lexer;

/**
 * @author 4erem6a
 */
public final class Token {
    private TokenTypes type;
    private String value;
    private SourcePosition position;

    public Token(TokenTypes type, String value, SourcePosition position) {
        this.type = type;
        this.value = value;
        this.position = position;
        this.position.setSym(position.getSym() - 1);
    }

    public Token(TokenTypes type, String value) {
        this.type = type;
        this.value = value;
    }

    public Token(TokenTypes type) {
        this(type, null);
    }

    public TokenTypes getType() {
        return type;
    }

    public void setType(TokenTypes type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SourcePosition getPosition() {
        return position;
    }

    public void setPosition(SourcePosition position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("[%s:%s:%s]", type, value, position);
    }
}