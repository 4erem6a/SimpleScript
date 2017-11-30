package com.evg.ss.lexer;

/**
 * @author 4erem6a
 */
public final class Position {
    private int line, sym;

    public Position(int line, int sym) {
        this.line = line;
        this.sym = sym;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getSym() {
        return sym;
    }

    public void setSym(int sym) {
        this.sym = sym;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]", line, sym);
    }
}