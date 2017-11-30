package com.evg.ss.ast;

import com.evg.ss.exceptions.inner.SSContinueException;

public final class ContinueStatement implements Statement {
    @Override
    public void execute() {
        throw new SSContinueException();
    }

    @Override
    public String toString() {
        return "continue\n";
    }
}
