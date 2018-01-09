package com.evg.ss.values;

import com.evg.ss.lib.Function;

public class FunctionValue implements Value {

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
        return Value.of(value);
    }
}