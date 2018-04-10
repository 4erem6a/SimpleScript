package com.evg.ss.values;

public class TypeValue implements Value {

    private Types value;

    public TypeValue(Types value) {
        this.value = value;
    }

    public Types getValue() {
        return value;
    }

    @Override
    public Double asNumber() {
        return Double.NaN;
    }

    @Override
    public Boolean asBoolean() {
        return true;
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
    public Types getType() {
        return Types.Type;
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
        return value.hashCode() ^ Types.Type.hashCode();
    }

    @Override
    public Value clone() {
        return Value.of(value);
    }
}