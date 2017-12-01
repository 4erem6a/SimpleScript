package com.evg.ss.ast;

import com.evg.ss.exceptions.inner.SSReturnException;
import com.evg.ss.lib.modules.SSModuleManager;

public final class ReturnStatement implements Statement {

    private Expression expression;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        throw new SSReturnException(expression.eval());
    }

    @Override
    public String toString() {
        return String.format("return %s\n", expression);
    }
}
