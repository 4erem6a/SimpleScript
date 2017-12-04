package com.evg.ss.values;

import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSArrayBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MapValue implements Value, Iterable<Map.Entry<Value, Value>> {

    public static final MapValue MAP_BASE = new MapValue();
    public static final MapValue MAP_EMPTY = createEmpty();

    private Value ssToArray(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        return toArray();
    }

    private Value ssSize(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        return new NumberValue(map.size());
    }

    private Map<Value, Value> map = new HashMap<>();

    public MapValue(MapValue map) {
        map.forEach(e -> this.map.put(e.getKey(), e.getValue()));
    }

    public MapValue() {
        this.put(new StringValue("toArray"), new FunctionValue(this::ssToArray));
        this.put(new StringValue("size"), new FunctionValue(this::ssSize));
    }

    private static MapValue createEmpty() {
        final MapValue value = new MapValue();
        value.map = new HashMap<>();
        return value;
    }

    public boolean containsKey(Value key) {
        return map.containsKey(key);
    }

    public void put(Value key, Value value) {
        map.put(key, value);
    }

    public Value get(Value key) {
        return map.get(key);
    }

    public Map<Value, Value> getMap() {
        return new HashMap<>(map);
    }

    public ArrayValue toArray() {
        final SSArrayBuilder builder = SSArrayBuilder.create();
        for (Map.Entry<Value, Value> entry : this) {
            final Value key = entry.getKey() instanceof MapValue
                    ? ((MapValue) entry.getKey()).toArray()
                    : entry.getKey();
            final Value value = entry.getValue() instanceof MapValue
                    ? ((MapValue) entry.getValue()).toArray()
                    : entry.getValue();
            builder.setElement(SSArrayBuilder.create()
                    .setElement(key)
                    .setElement(value)
                    .build());
        }
        return builder.build();
    }

    @Override
    public Iterator<Map.Entry<Value, Value>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<Value, Value>> action) {
        map.entrySet().forEach(action);
    }

    @Override
    public Spliterator<Map.Entry<Value, Value>> spliterator() {
        return map.entrySet().spliterator();
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
        return toArray().asString();
    }

    @Override
    public Object asObject() {
        return this;
    }

    @Override
    public Type getType() {
        return Type.Map;
    }

    @Override
    public int compareTo(Value o) {
        if (o instanceof MapValue)
            return ((MapValue) o).toArray().compareTo(toArray());
        if (o instanceof ArrayValue)
            return o.compareTo(toArray());
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return getType().ordinal() | toArray().hashCode();
    }

}