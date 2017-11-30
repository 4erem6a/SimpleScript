package com.evg.ss.lexer;

/**
 * @author 4erem6a
 */
public final class Token {
    private TokenType type;
    private String value;
    private Position position;

    public Token(TokenType type, String value, Position position) {
        this.type = type;
        this.value = value;
        this.position = position;
        this.position.setSym(position.getSym() - 1);
    }

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Token(TokenType type) {
        this(type, null);
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("[%s:%s:%s]", type, value, position);
    }
}