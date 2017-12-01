package com.evg.ss.ast;

import com.evg.ss.exceptions.FieldNotFoundException;
import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class MapAccessExpression implements Expression {

    private Expression map;
    private String field;
    private Expression[] args;

    public MapAccessExpression(Expression map, String field) {
        this.map = map;
        this.field = field;
        this.args = null;
    }

    public MapAccessExpression(Expression map, String field, Expression... args) {
        this.map = map;
        this.field = field;
        this.args = args;
    }

    @Override
    public Value eval() {
        final Value value = map.eval();
        if (!(value instanceof MapValue))
            throw new InvalidValueTypeException(value.getType());
        final MapValue map = (MapValue) value;
        if (!map.exists(field))
            throw new FieldNotFoundException(field);
        if (args == null)
            return map.get(field);
        else {
            final Value method = map.get(field);
            if (!(method instanceof FunctionValue))
                throw new InvalidValueTypeException(method.getType());
            return ((FunctionValue) method).getValue().execute(Arrays.stream(args).map(Expression::eval).toArray(Value[]::new));
        }
    }
}