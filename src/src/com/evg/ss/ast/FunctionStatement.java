package com.evg.ss.ast;

public final class FunctionStatement implements Statement {

    private FunctionExpression expression;

    public FunctionStatement(FunctionExpression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        expression.eval();
    }

    @Override
    public String toString() {
        return expression.toString() + "\n";
    }
}