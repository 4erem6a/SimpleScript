package com.evg.ss.lib.modules.lists;

import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

import java.util.ArrayList;
import java.util.List;

public class ListValue extends MapValue {

    private List<Value> list;

    public ListValue(ListValue list, MapValue base) {
        this.list = list.list;
        setMap(base);
    }

    public ListValue(ListValue list) {
        this.list = list.list;
    }

    public ListValue() {
        this.list = new ArrayList<>();
    }

    public void add(Value value) {
        list.add(value);
    }

    public void remove(Value value) {
        list.remove(value);
    }

    public void remove(int index) {
        list.remove(index);
    }

    public void addAll(ListValue values) {
        list.addAll(values.list);
    }


    public void addAll(int index, ListValue values) {
        list.addAll(index, values.list);
    }

    public Value get(int index) {
        return list.get(index);
    }

    @Override
    public ArrayValue toArray() {
        return (ArrayValue) Value.of(list.toArray(new Value[0]));
    }

    @Override
    public int compareTo(Value o) {
        return super.compareTo(o);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Type.Map.ordinal() | Type.Array.ordinal() | list.stream().mapToInt(Value::hashCode).reduce((a, b) -> a | b).getAsInt();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public String asString() {
        return Value.of(list.toArray(new Value[0])).asString();
    }

    @Override
    public String toString() {
        return asString();
    }
}