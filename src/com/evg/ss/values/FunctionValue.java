package com.evg.ss.values;

import com.evg.ss.lib.ConstructorFunction;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.modules.jfunctions.jfunctions;
import com.evg.ss.util.args.Arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionValue implements Value, Callable, NewCallable, Container {

    private Function value;

    public FunctionValue(Function value) {
        this.value = value;
    }

    public Function getValue() {
        return value;
    }

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
        return getType().toString().toLowerCase();
    }

    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.Function;
    }

    @Override
    public int compareTo(Value o) {
        return (o.getType() == Type.Function ? hashCode() - o.hashCode() : -1);
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode() ^ Type.Function.hashCode();
    }

    @Override
    public Value clone() {
        if (value instanceof SSFunction)
            return new FunctionValue(((SSFunction) value).clone());
        return new FunctionValue(value);
    }

    @Override
    public Value call(Value... args) {
        return value.execute(args);
    }

    @Override
    public Value _new(Value... args) {
        return value instanceof ConstructorFunction
                ? ((ConstructorFunction) value).executeAsNew(args)
                : new UndefinedValue();
    }

    @Override
    public Value get(Value key) {
        switch (key.asString()) {
            case "apply":
                return Value.of(this::apply);
            case "info":
                return Value.of(this::info);
        }
        return new UndefinedValue();
    }

    private Value apply(Value... args) {
        if (Arguments.checkArgc(args, 1) == -1)
            Arguments.checkArgcOrDie(args, 2);
        final List<Value> _args = new ArrayList<>();
        _args.add(this);
        Collections.addAll(_args, args);
        return jfunctions.execute(_args.toArray(new Value[0]));
    }

    private Value info(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        return jfunctions.info(this);
    }

    @Override
    public void set(Value key, Value value) { }
}