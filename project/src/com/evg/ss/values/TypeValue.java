package com.evg.ss.values;

public class TypeValue implements Value {

    private Type value;

    public TypeValue(Type value) {
        this.value = value;
    }

    public Type getValue() {
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
        return value.name();
    }

    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.Type;
    }

    @Override
    public int compareTo(Value o) {
        if (o instanceof TypeValue && ((TypeValue) o).value == value)
            return 0;
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return getType().ordinal() | value.hashCode();
    }
}