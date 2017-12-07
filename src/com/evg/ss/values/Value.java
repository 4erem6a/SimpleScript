package com.evg.ss.values;

import com.evg.ss.lib.Function;

/**
 * @author 4erem6a
 */
public interface Value extends Comparable<Value> {

    static Value of(double value) {
        return new NumberValue(value);
    }

    static Value of(boolean value) {
        return new BoolValue(value);
    }

    static Value of(String value) {
        return new StringValue(value);
    }

    static Value of(Value... value) {
        return new ArrayValue(value);
    }

    static Value of(Function value) {
        return new FunctionValue(value);
    }

    static Value of(Type value) {
        return new TypeValue(value);
    }

    Double asNumber();

    Boolean asBoolean();

    String asString();

    Object asObject();

    Type getType();

}