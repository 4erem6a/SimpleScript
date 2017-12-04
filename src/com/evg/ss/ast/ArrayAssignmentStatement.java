package com.evg.ss.ast;

import com.evg.ss.exceptions.IndexOutOfBoundsException;
import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class ArrayAssignmentStatement implements Statement {

    private Expression value;
    private String name;
    private Value target;
    private Expression[] indices;

    public ArrayAssignmentStatement(String name, Expression[] indices, Expression value) {
        this.name = name;
        this.indices = indices;
        this.value = value;
        this.target = null;
    }

    private ArrayAssignmentStatement(Value target, Expression[] indices, Expression value) {
        this.name = null;
        this.indices = indices;
        this.value = value;
        this.target = target;
    }

    @Override
    public void execute() {
        if (target == null) target = new VariableExpression(name).eval();
        final ArrayValue arrayValue;
        if (!(target instanceof ArrayValue))
            throw new InvalidValueTypeException(target.getType());
        arrayValue = (ArrayValue) target;
        final int index = indices[0].eval().asNumber().intValue();
        if (index < 0 || index >= arrayValue.length())
            throw new IndexOutOfBoundsException(index);
        if (indices.length == 1) {
            arrayValue.set(index, value.eval());
            return;
        }
        new ArrayAssignmentStatement(new ArrayAccessExpression(() -> arrayValue, indices[0]).eval(),
                Arrays.stream(indices)
                    .skip(1)
                    .toArray(Expression[]::new),
                value).execute();
    }
}