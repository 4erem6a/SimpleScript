package com.evg.ss.values;

/**
 * @author 4erem6a
 */
public class NumberValue implements Value {

    private double value;

    public NumberValue(double value) {
        this.value = value;
    }

    @Override
    public Double asNumber() {
        return value;
    }

    @Override
    public Boolean asBoolean() {
        return value > 0;
    }

    @Override
    public String asString() {
        return Double.toString(value);
    }

    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.Number;
    }

    @Override
    public int compareTo(Value o) {
        return (o.getType() == Type.Null ? -1 : Double.compare(value, o.asNumber()));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value) ^ Type.Number.hashCode();
    }

    @Override
    public Value clone() {
        return Value.of(value);
    }
}