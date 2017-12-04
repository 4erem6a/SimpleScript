package com.evg.ss.util.builders;

import com.evg.ss.lib.Function;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

public final class SSMapBuilder {

    private MapValue map;

    private SSMapBuilder() {
        map = new MapValue();
    }

    private SSMapBuilder(MapValue base) {
        map = new MapValue(base);
    }

    public static SSMapBuilder create() {
        return new SSMapBuilder();
    }

    public static SSMapBuilder create(MapValue base) {
        return new SSMapBuilder(base);
    }

    public SSMapBuilder setField(Value key, Value value) {
        map.put(key, value);
        return this;
    }

    public SSMapBuilder setMethod(Value key, Function function) {
        map.put(key, new FunctionValue(function));
        return this;
    }

    public SSMapBuilder setField(String key, Value value) {
        map.put(new StringValue(key), value);
        return this;
    }

    public SSMapBuilder setMethod(String key, Function function) {
        map.put(new StringValue(key), new FunctionValue(function));
        return this;
    }

    public MapValue build() {
        return map;
    }

}