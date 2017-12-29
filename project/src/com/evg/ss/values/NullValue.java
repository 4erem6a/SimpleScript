package com.evg.ss.values;

import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.ValueExpression;

/**
 * @author 4erem6a
 */
public class NullValue implements Value {

    public static final Expression NullExpression = new ValueExpression();

    @Override
    public Double asNumber() {
        return null;
    }

    @Override
    public Boolean asBoolean() {
        return null;
    }

    @Override
    public String asString() {
        return "";
    }

    @Override
    public Object asObject() {
        return null;
    }

    @Override
    public Type getType() {
        return Type.Null;
    }

    @Override
    public int compareTo(Value o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return getType().ordinal();
    }
}