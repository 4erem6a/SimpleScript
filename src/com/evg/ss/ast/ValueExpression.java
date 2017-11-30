package com.evg.ss.ast;

import com.evg.ss.values.*;

/**
 * @author 4erem6a
 */
public class ValueExpression implements Expression {

    private Value value;

    public ValueExpression(double value) {
        this.value = new NumberValue(value);
    }

    public ValueExpression(boolean value) {
        this.value = new BoolValue(value);
    }

    public ValueExpression(String value) {
        this.value = new StringValue(value);
    }

    public ValueExpression() {
        this.value = new NullValue();
    }

    @Override
    public Value eval() {
        return value;
    }

    @Override
    public String toString() {
        return value.asString();
    }
}