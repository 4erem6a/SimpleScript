package com.evg.ss.util.builders;

import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.Value;

import java.util.ArrayList;
import java.util.List;

public final class SSArrayBuilder {

    private List<Value> values = new ArrayList<>();

    private SSArrayBuilder() {
    }

    public static SSArrayBuilder create() {
        return new SSArrayBuilder();
    }

    public SSArrayBuilder setElement(Value value) {
        values.add(value);
        return this;
    }

    public SSArrayBuilder setElement(int index, Value value) {
        values.add(index, value);
        return this;
    }

    public ArrayValue build() {
        return new ArrayValue(values.toArray(new Value[values.size()]));
    }
}