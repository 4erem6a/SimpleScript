package com.evg.ss.parser.ast;

import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.*;

/**
 * @author 4erem6a
 */
public class ValueExpression extends Expression {

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

    public ValueExpression(ConstValues value) {
        if (value == ConstValues.Null)
            this.value = new NullValue();
        else this.value = new UndefinedValue();
    }

    public ValueExpression() {
        this.value = new UndefinedValue();
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
        return new MSCGenerator(this).generate();
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

    public enum ConstValues {
        Null,
        Undefined
    }
}