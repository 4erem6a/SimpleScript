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
        return "";
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
        if (o instanceof FunctionValue)
            if (((FunctionValue) o).getValue() == value)
                return 0;
        return -1;
    }
}