package com.evg.ss.values;

import com.evg.ss.util.builders.SSArrayBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MapValue implements Value, Container, Callable, Iterable<Map.Entry<Value, Value>> {

    private Map<Value, Value> map = new HashMap<>();

    public MapValue(MapValue map) {
        map.forEach(e -> this.map.put(e.getKey(), e.getValue()));
    }

    public MapValue() {
    }

    public boolean containsKey(Value key) {
        return map.containsKey(key);
    }

    public void set(Value key, Value value) {
        map.put(key, value);
    }

    public Value get(Value key) {
        if (map.containsKey(key))
            return map.get(key);
        return new UndefinedValue();
    }

    public Map<Value, Value> getMap() {
        return new HashMap<>(map);
    }

    public void setMap(MapValue map) {
        this.map = map.map;
    }

    public Map<Value, Value> getMapReference() {
        return map;
    }

    public ArrayValue toArray() {
        final SSArrayBuilder builder = new SSArrayBuilder();
        for (Map.Entry<Value, Value> entry : this) {
            final Value key = entry.getKey() instanceof MapValue
                    ? ((MapValue) entry.getKey()).toArray()
                    : entry.getKey();
            final Value value = entry.getValue() instanceof MapValue
                    ? ((MapValue) entry.getValue()).toArray()
                    : entry.getValue();
            builder.setElement(new SSArrayBuilder()
                    .setElement(key)
                    .setElement(value)
                    .build());
        }
        return builder.build();
    }

    public int size() {
        return map.size();
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
        final StringBuilder builder = new StringBuilder("{");
        for (Map.Entry<Value, Value> entry : this)
            builder.append(entry.getKey().asString())
                    .append(":")
                    .append(entry.getValue().asString())
                    .append(",");
        if (builder.charAt(builder.length() - 1) == ',')
            return builder.deleteCharAt(builder.length() - 1).append("}").toString();
        return builder.append("}").toString();
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
            return toArray().compareTo(((MapValue) o).toArray());
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return map.hashCode() ^ Type.Map.hashCode();
    }

    @Override
    public Value clone() {
        final MapValue result = new MapValue();
        result.map = this.getMap();
        return result;
    }

    @Override
    public Value call(Value... args) {
        if (!map.containsKey(Value.of("$call")))
            return new UndefinedValue();
        final Value call = map.get(Value.of("$call"));
        if (call.getType() != Type.Function)
            return new UndefinedValue();
        return ((FunctionValue) call).call(args);
    }
}