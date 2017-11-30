package com.evg.ss.values;

import com.evg.ss.ast.Expression;
import com.evg.ss.ast.ValueExpression;

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
        return null;
    }

    @Override
    public Object asObject() {
        return null;
    }

    @Override
    public Types getType() {
        return Types.Null;
    }

    @Override
    public int compareTo(Value o) {
        return 0;
    }
}