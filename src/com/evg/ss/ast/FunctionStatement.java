package com.evg.ss.ast;

public final class FunctionStatement implements Statement {

    private FunctionCallExpression expression;

    public FunctionStatement(FunctionCallExpression expression) {
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