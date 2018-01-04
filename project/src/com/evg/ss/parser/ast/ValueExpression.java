package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
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

    public ValueExpression(Value value) {
        this.value = value;
    }

    public ValueExpression() {
        this.value = new NullValue();
    }

    @Override
    public Value eval() {
        return value;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.asString();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return value.hashCode() ^ (44 * 2 * 31);
    }
}