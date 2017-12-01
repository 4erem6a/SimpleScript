package com.evg.ss.values;

/**
 * @author 4erem6a
 */
public interface Value extends Comparable<Value> {

    Double asNumber();
    Boolean asBoolean();
    String asString();
    Object asObject();

    Type getType();

}