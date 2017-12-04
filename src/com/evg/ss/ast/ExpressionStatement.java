package com.evg.ss.ast;

public final class ExpressionStatement implements Statement {

    private Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        expression.eval();
    }

}
