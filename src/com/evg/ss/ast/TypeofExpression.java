package com.evg.ss.ast;

import com.evg.ss.values.Value;

public final class TypeofExpression implements Expression {

    private Expression expression;

    public TypeofExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value eval() {
        return expression.eval().getType().getTypeValue();
    }
}