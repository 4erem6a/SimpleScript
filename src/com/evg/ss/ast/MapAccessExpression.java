package com.evg.ss.ast;

import com.evg.ss.exceptions.FieldNotFoundException;
import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class MapAccessExpression implements Expression, Accessible {

    private Expression map;
    private Expression key;

    public MapAccessExpression(Expression map, Expression key) {
        this.map = map;
        this.key = key;
    }

    @Override
    public Value eval() {
        final Value value = map.eval();
        final Value key = this.key.eval();
        if (!(value instanceof MapValue))
            throw new InvalidValueTypeException(value.getType());
        final MapValue map = (MapValue) value;
        if (!map.containsKey(key))
            throw new FieldNotFoundException(key);
        return map.get(key);
    }

    @Override
    public Value get() {
        return eval();
    }

    @Override
    public Value set(Value newValue) {
        final Value value = map.eval();
        final Value key = this.key.eval();
        if (!(value instanceof MapValue))
            throw new InvalidValueTypeException(value.getType());
        final MapValue map = (MapValue) value;
        map.put(key, newValue);
        return map.get(key);
    }
}