package com.evg.ss.values;

/**
 * @author 4erem6a
 */
public enum Type {

    Object,
    Number,
    String,
    Boolean,
    Array,
    Function,
    Type,
    Null,
    Map;

    public TypeValue getTypeValue() {
        return new TypeValue(this);
    }
}