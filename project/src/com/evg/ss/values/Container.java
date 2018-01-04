package com.evg.ss.values;

public interface Container {

    Value get(Value key);

    void set(Value key, Value value);

}