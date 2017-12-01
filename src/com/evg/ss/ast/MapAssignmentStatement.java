package com.evg.ss.ast;

import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

public final class MapAssignmentStatement implements Statement {

    private String name;
    private String field;
    private Expression value;

    public MapAssignmentStatement(String name, String field, Expression value) {
        this.name = name;
        this.field = field;
        this.value = value;
    }

    @Override
    public void execute() {
        final Value value = new VariableExpression(name).eval();
        if (!(value instanceof MapValue))
            throw new InvalidValueTypeException(value.getType());
        final MapValue map = (MapValue) value;
        map.setField(field, this.value.eval(), false);
    }
}