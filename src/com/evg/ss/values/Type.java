package com.evg.ss.values;

/**
 * @author 4erem6a
 */
public enum Type {

    Class,
    Object,
    Number,
    String,
    Boolean,
    Array,
    Function,
    Type,
    Map,
    Null,
    Undefined;

    public TypeValue getTypeValue() {
        return new TypeValue(this);
    }
}