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
        return (double)(value ? 1 : 0);
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
    public Types getType() {
        return Types.Boolean;
    }

    @Override
    public int compareTo(Value o) {
        return Boolean.compare(value, o.asBoolean());
    }
}