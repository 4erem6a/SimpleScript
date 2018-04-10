package com.evg.ss.values;

import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.ValueExpression;

/**
 * @author 4erem6a
 */
public class NullValue implements Value {

    public static final Expression NullExpression = new ValueExpression(ValueExpression.ConstValues.Null);

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
        return "null";
    }

    @Override
    public Object asObject() {
        return this;
    }

    @Override
    public Types getType() {
        return Types.Null;
    }

    @Override
    public int compareTo(Value o) {
        return (o.getType() == Types.Null ? 0 : -1);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return Types.Null.hashCode();
    }

    @Override
    public Value clone() {
        return new NullValue();
    }
}