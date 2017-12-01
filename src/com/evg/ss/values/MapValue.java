package com.evg.ss.values;

import com.evg.ss.exceptions.ArgumentCountMismatchException;
import com.evg.ss.exceptions.StaticFieldAccessException;
import com.evg.ss.exceptions.StaticMapAccessException;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.Variable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MapValue implements Value, Iterable<Map.Entry<String, Variable>> {

    private boolean isStatic;
    private Map<String, Variable> map;

    public MapValue(boolean isStatic) {
        this.map = new HashMap<>();
        addSystemFields();
        this.isStatic = isStatic;
    }

    private void addSystemFields() {
        setField("isStatic", new BoolValue(isStatic), true);
        setMethod("toArray", this::toArray, true);
        setMethod("size", this::size, true);
    }

    private Value toArray(Value... values) {
        if (values.length > 0)
            throw new ArgumentCountMismatchException("toArray", values.length);
        return mapToPairs(this);
    }

    private ArrayValue mapToPairs(MapValue value) {
        int index = 0;
        final ArrayValue array = new ArrayValue(value.size());
        for (Map.Entry<String, Variable> pair : this) {
            final ArrayValue pairArray = new ArrayValue(2);
            pairArray.set(0, new StringValue(pair.getKey()));
            final Value pairValue = pair.getValue().getValue();
            if (pairValue instanceof MapValue)
                pairArray.set(1, mapToPairs((MapValue) pairValue));
            else pairArray.set(1, pairValue);
            array.set(index++, pairArray);
        }
        return array;
    }

    private Value size(Value... values) {
        if (values.length > 0)
            throw new ArgumentCountMismatchException("size", values.length);
        return new NumberValue(size());
    }

    public int size() {
        return map.size();
    }

    public Value get(String name) {
        if (!map.containsKey(name))
            return null;
        return map.get(name).getValue();
    }

    public boolean exists(String name) {
        return get(name) != null;
    }

    public void setField(String name, Value value, boolean isStatic) {
        if (this.isStatic)
            throw new StaticMapAccessException();
        if (map.containsKey(name))
            if (map.get(name).isConst())
                throw new StaticFieldAccessException(name);
        map.put(name, new Variable(value, isStatic));
    }

    public void setMethod(String name, Function value, boolean isStatic) {
        if (this.isStatic)
            throw new StaticMapAccessException();
        if (map.containsKey(name))
            if (map.get(name).isConst())
                throw new StaticFieldAccessException(name);
        map.put(name, new Variable(new FunctionValue(value), isStatic));
    }

    public void putField(String name, Value value, boolean isStatic) {
        map.put(name, new Variable(value, isStatic));
    }

    @Override
    public Iterator<Map.Entry<String, Variable>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<String, Variable>> action) {
        map.entrySet().forEach(action);
    }

    @Override
    public Spliterator<Map.Entry<String, Variable>> spliterator() {
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
    public java.lang.String asString() {
        return mapToPairs(this).asString();
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
            return ((MapValue) o).map.equals(map) ? 0 : -1;
        return -1;
    }
}