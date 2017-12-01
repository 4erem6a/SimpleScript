package com.evg.ss.ast;

import com.evg.ss.exceptions.IndexOutOfBoundsException;
import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class ArrayAccessExpression implements Expression {

    private Value value;
    private String name;
    private Expression[] indices;

    public ArrayAccessExpression(String name, Expression... indices) {
        this.name = name;
        this.indices = indices;
        this.value = null;
    }

    public ArrayAccessExpression(Value value, Expression... indices) {
        this.value = value;
        this.indices = indices;
        this.name = null;
    }

    @Override
    public Value eval() {
        if (value == null) value = new VariableExpression(name).eval();
        final ArrayValue arrayValue;
        if (value instanceof StringValue)
            arrayValue = ((StringValue) value).asCharArray();
        else if (value instanceof ArrayValue)
            arrayValue = (ArrayValue) value;
        else throw new InvalidValueTypeException(value.getType());
        final int index = indices[0].eval().asNumber().intValue();
        if (index < 0 || index >= arrayValue.length())
            throw new IndexOutOfBoundsException(name, index);
        final Value result = arrayValue.get(index);
        if (indices.length > 1)
            return new ArrayAccessExpression(result,
                    Arrays.stream(indices)
                            .skip(1)
                            .toArray(Expression[]::new)).eval();
        return result;
    }
}