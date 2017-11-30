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
        return String.format("%f", value);
    }

    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public Types getType() {
        return Types.Number;
    }

    @Override
    public int compareTo(Value o) {
        return Double.compare(value, o.asNumber());
    }
}