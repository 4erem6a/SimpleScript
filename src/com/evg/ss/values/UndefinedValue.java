package com.evg.ss.values;

import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.ValueExpression;

/**
 * @author 4erem6a
 */
public class UndefinedValue implements Value {

    public static final Expression UndefinedExpression = new ValueExpression(ValueExpression.ConstValues.Undefined);

    @Override
    public Double asNumber() {
        return Double.NaN;
    }

    @Override
    public Boolean asBoolean() {
        return false;
    }

    @Override
    public String asString() {
        return "undefined";
    }

    @Override
    public Object asObject() {
        return this;
    }

    @Override
    public Types getType() {
        return Types.Undefined;
    }

    @Override
    public int compareTo(Value o) {
        return (o.getType() == Types.Undefined ? 0 : -1);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return Types.Undefined.hashCode();
    }

    @Override
    public Value clone() {
        return new UndefinedValue();
    }
}