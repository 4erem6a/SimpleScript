package com.evg.ss.util.builders;

import com.evg.ss.lib.Function;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.values.ClassValue;
import com.evg.ss.values.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class SSClassBuilder {

    private final ClassValue base;
    private SSFunction constructor = null;
    private Map<Value, ClassValue.ClassMember> members = new HashMap<>();

    public SSClassBuilder(ClassValue base) {
        this.base = base;
    }

    public SSClassBuilder() {
        this.base = null;
    }

    public SSClassBuilder setConstructor(SSFunction function) {
        this.constructor = function;
        return this;
    }

    public SSClassBuilder setStaticField(Value key, Value value) {
        members.put(key, new ClassValue.ClassField(true, key, value));
        return this;
    }

    public SSClassBuilder setStaticField(String name, Value value) {
        return setStaticField(Value.of(name), value);
    }

    public SSClassBuilder setMethod(Value key, Function function) {
        members.put(key, new ClassValue.ClassMethod(false, key, function));
        return this;
    }

    public SSClassBuilder setMethod(String name, Function function) {
        return setMethod(Value.of(name), function);
    }

    public SSClassBuilder setField(Value key, Value value) {
        members.put(key, new ClassValue.ClassField(false, key, value));
        return this;
    }

    public SSClassBuilder setField(String name, Value value) {
        return setField(Value.of(name), value);
    }

    public SSClassBuilder setStaticMethod(Value key, Function function) {
        members.put(key, new ClassValue.ClassMethod(true, key, function));
        return this;
    }

    public SSClassBuilder setStaticMMethod(String name, Function function) {
        return setStaticMethod(Value.of(name), function);
    }

    public ClassValue build() {
        return new ClassValue(base, constructor, new ArrayList<>(members.values()));
    }
}