package com.evg.ss.ast;

import com.evg.ss.values.Value;

public final class TernaryExpression implements Expression {

    private Expression condition, onTrue, onFalse;

    public TernaryExpression(Expression condition, Expression onTrue, Expression onFalse) {
        this.condition = condition;
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    @Override
    public Value eval() {
        final boolean condition = this.condition.eval().asBoolean();
        final Value onTrue = this.onTrue.eval();
        final Value onFalse = this.onFalse.eval();
        return condition ? onTrue : onFalse;
    }

    @Override
    public String toString() {
        return String.format("[%s ? %s : %s]", condition, onTrue, onFalse);
    }
}