package com.evg.ss.values;

import java.util.Arrays;

public class ArrayValue implements Value {

    private Value[] value;

    public Value[] getValue() {
        return value;
    }

    public ArrayValue(Value... values) {
        this.value = values;
    }

    public ArrayValue(int size) {
        this.value = new Value[size];
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
}