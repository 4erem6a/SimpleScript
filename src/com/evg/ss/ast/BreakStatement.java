package com.evg.ss.ast;

import com.evg.ss.exceptions.inner.SSBreakException;

public final class BreakStatement implements Statement {
    @Override
    public void execute() {
        throw new SSBreakException();
    }

    @Override
    public String toString() {
        return "break\n";
    }
}
