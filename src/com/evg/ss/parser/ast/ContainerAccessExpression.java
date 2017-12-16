package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.FieldNotFoundException;
import com.evg.ss.exceptions.execution.IndexOutOfBoundsException;
import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

public final class ContainerAccessExpression implements Expression, Accessible {

    private Expression target;
    private Expression key;

    public ContainerAccessExpression(Expression target, Expression key) {
        this.target = target;
        this.key = key;
    }

    @Override
    public Value eval() {
        final Value target = this.target.eval();
        final Value key = this.key.eval();
        if (target instanceof ArrayValue)
            return accessArray((ArrayValue) target, key);
        if (target instanceof MapValue)
            return accessMap((MapValue) target, key);
        throw new InvalidValueTypeException(target.getType());
    }

    private Value accessArray(ArrayValue target, Value key) {
        final int index = key.asNumber().intValue();
        if (index < 0 || index >= target.length())
            throw new IndexOutOfBoundsException(index);
        return target.get(index);
    }

    private Value accessMap(MapValue target, Value key) {
        if (!target.containsKey(key))
            throw new FieldNotFoundException(key);
        return target.get(key);
    }

    @Override
    public Value get() {
        return eval();
    }

    @Override
    public Value set(Value value) {
        final Value target = this.target.eval();
        final Value key = this.key.eval();
        if (target instanceof ArrayValue)
            return setArray((ArrayValue) target, key, value);
        if (target instanceof MapValue)
            return setMap((MapValue) target, key, value);
        throw new InvalidValueTypeException(target.getType());
    }

    private Value setArray(ArrayValue target, Value key, Value value) {
        final int index = key.asNumber().intValue();
        if (index < 0 || index >= target.length())
            throw new IndexOutOfBoundsException(index);
        target.set(index, value);
        return target.get(index);
    }

    private Value setMap(MapValue target, Value key, Value value) {
        target.put(key, value);
        return target.get(key);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Expression getTarget() {
        return target;
    }

    public Expression getKey() {
        return key;
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", target, key);
    }
}