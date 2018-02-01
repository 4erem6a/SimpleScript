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
    private Map<String, ClassValue.ClassMember> members = new HashMap<>();

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

    public SSClassBuilder setField(String name) {
        members.put(name, new ClassValue.ClassField(false, name));
        return this;
    }

    public SSClassBuilder setField(String name, Value value) {
        members.put(name, new ClassValue.ClassField(false, name, value));
        return this;
    }

    public SSClassBuilder setStaticField(String name) {
        members.put(name, new ClassValue.ClassField(true, name));
        return this;
    }

    public SSClassBuilder setStaticField(String name, Value value) {
        members.put(name, new ClassValue.ClassField(true, name, value));
        return this;
    }

    public SSClassBuilder setMethod(String name, Function function) {
        members.put(name, new ClassValue.ClassField(false, name));
        return this;
    }

    public SSClassBuilder setStaticMethod(String name, Function function) {
        members.put(name, new ClassValue.ClassField(true, name));
        return this;
    }

    public ClassValue build() {
        return new ClassValue(base, constructor, new ArrayList<>(members.values()));
    }
}