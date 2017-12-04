package com.evg.ss.ast;

import com.evg.ss.exceptions.IndexOutOfBoundsException;
import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class ArrayAccessExpression implements Expression, Accessible {

    private Expression expression;
    private Expression[] indices;

    public ArrayAccessExpression(Expression expression, Expression... indices) {
        this.expression = expression;
        this.indices = indices;
    }

    @Override
    public Value eval() {
        final int lastIndex = getLastIndex();
        final ArrayValue arrayValue = getTargetArray();
        if (lastIndex < 0 || lastIndex > arrayValue.length())
            throw new IndexOutOfBoundsException(lastIndex);
        return arrayValue.get(lastIndex);
    }

    private ArrayValue getTargetArray() {
        final Value value = expression.eval();
        ArrayValue arrayValue;
        if (value instanceof StringValue)
            arrayValue = ((StringValue) value).asCharArray();
        else if (value instanceof ArrayValue)
            arrayValue = (ArrayValue) value;
        else throw new InvalidValueTypeException(value.getType());
        for (int index : getIndices()) {
            if (index < 0 || index > arrayValue.length())
                throw new IndexOutOfBoundsException(index);
            final Value current = arrayValue.get(index);
            if (current instanceof ArrayValue)
                arrayValue = (ArrayValue) current;
            else throw new InvalidValueTypeException(current.getType());
        }
        return arrayValue;
    }

    private int[] getIndices() {
        return Arrays.stream(this.indices).limit(this.indices.length - 1).mapToInt(i -> i.eval().asNumber().intValue()).toArray();
    }

    private int getLastIndex() {
        return this.indices[this.indices.length - 1].eval().asNumber().intValue();
    }

    @Override
    public Value get() {
        return eval();
    }

    @Override
    public Value set(Value value) {
        final int lastIndex = getLastIndex();
        final ArrayValue arrayValue = getTargetArray();
        if (lastIndex < 0 || lastIndex > arrayValue.length())
            throw new IndexOutOfBoundsException(lastIndex);
        arrayValue.set(lastIndex, value);
        return arrayValue.get(lastIndex);
    }
}