package com.evg.ss.values;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ArrayValue implements Value, Iterable<Value> {

    private Value[] value;

    public ArrayValue(Value... values) {
        this.value = values;
    }

    public ArrayValue(int size) {
        this.value = new Value[size];
    }

    public Value[] getValue() {
        return value;
    }

    public void resize(int size) {
        final Value[] newValue = new Value[size];
        System.arraycopy(value, 0, newValue, 0, newValue.length > value.length ? value.length : newValue.length);
    }

    public int length() {
        return value.length;
    }

    public Value get(int index) {
        return this.value[index];
    }

    public void set(int index, Value value) {
        this.value[index] = value;
    }

    @Override
    public Double asNumber() {
        return (double) length();
    }

    @Override
    public Boolean asBoolean() {
        return false;
    }

    @Override
    public String asString() {
        if (length() == 0)
            return "[]";
        final StringBuilder builder = new StringBuilder("[");
        Arrays.stream(value).map(e -> StringValue.asStringValue(e).asString() + ',').forEach(builder::append);
        builder.deleteCharAt(builder.length() - 1);
        builder.append(']');
        return builder.toString();
    }

    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.Array;
    }

    @Override
    public int compareTo(Value o) {
        if (!(o instanceof ArrayValue))
            return -1;
        else return Arrays.compare(value, ((ArrayValue) o).value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        int hash = getType().ordinal();
        for (Value value : value)
            hash |= value.hashCode();
        return hash;
    }

    @Override
    public Iterator<Value> iterator() {
        return Arrays.stream(value).iterator();
    }

    @Override
    public void forEach(Consumer<? super Value> action) {
        Arrays.stream(value).forEach(action);
    }

    @Override
    public Spliterator<Value> spliterator() {
        return Arrays.stream(value).spliterator();
    }
}