package com.evg.ss.values;

import com.evg.ss.exceptions.execution.FieldNotFoundException;
import com.evg.ss.exceptions.execution.IndexOutOfBoundsException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ArrayValue implements Value, Container, Iterable<Value> {

    private Value[] value;

    public ArrayValue(Value... values) {
        this.value = values;
    }

    public Value[] getValue() {
        return value;
    }

    public void resize(int size) {
        final Value[] newValue = new Value[size];
        System.arraycopy(value, 0, newValue, 0, newValue.length > value.length ? value.length : newValue.length);
        this.value = newValue;
    }

    public int length() {
        return value.length;
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
        if (o.getType() != Type.Array)
            return -1;
        else return compareArrays(value, ((ArrayValue) o).value);
    }

    private int compareArrays(Value[] a1, Value[] a2) {
        if (a1.length != a2.length)
            return a1.length - a2.length;
        int result = 0;
        for (int i = 0; i < a1.length; i++)
            result += a1[i].compareTo(a2[i]);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value) ^ Type.Array.hashCode();
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

    @Override
    public Value clone() {
        return Value.of(value);
    }

    @Override
    public Value get(Value key) {
        if (key instanceof NumberValue) {
            final int index = key.asNumber().intValue();
            if (index < 0 || index >= value.length)
                throw new IndexOutOfBoundsException(index);
            return value[index];
        } else if (key instanceof StringValue) {
            ///Properties:
            switch (key.asString()) {
                case "length":
                    return Value.of(value.length);
            }
        }
        throw new FieldNotFoundException(key);
    }

    @Override
    public void set(Value key, Value value) {
        if (key instanceof NumberValue) {
            final int index = key.asNumber().intValue();
            if (index < 0 || index >= this.value.length)
                throw new IndexOutOfBoundsException(index);
            this.value[index] = value;
        }
        throw new FieldNotFoundException(key);
    }
}