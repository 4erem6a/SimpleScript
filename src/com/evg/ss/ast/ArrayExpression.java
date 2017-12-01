package com.evg.ss.ast;

import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class ArrayExpression implements Expression {

    private Expression[] expressions;

    public ArrayExpression() {
        this.expressions = new Expression[0];
    }

    public ArrayExpression(Expression[] expressions) {
        this.expressions = expressions;
    }

    @Override
    public Value eval() {
        return new ArrayValue(Arrays.stream(expressions).map(Expression::eval).toArray(Value[]::new));
    }
}