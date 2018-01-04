package com.evg.ss.values;

/**
 * @author 4erem6a
 */
public class BoolValue implements Value {

    private boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    @Override
    public Double asNumber() {
        return (double) (value ? 1 : 0);
    }

    @Override
    public Boolean asBoolean() {
        return value;
    }

    @Override
    public String asString() {
        return String.format("%s", value);
    }

    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.Boolean;
    }

    @Override
    public int compareTo(Value o) {
        return o.getType() == Type.Null ? -1 : Boolean.compare(value, o.asBoolean());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value) ^ Type.Boolean.hashCode();
    }

    @Override
    public Value clone() {
        return Value.of(value);
    }
}