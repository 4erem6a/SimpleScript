package com.evg.ss.values;

import com.evg.ss.exceptions.execution.ClassMethodAccessException;
import com.evg.ss.exceptions.execution.FieldNotFoundException;
import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.Argument;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.SSFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassValue implements Value, Container {

    private final ClassValue base;
    private final List<ClassMember> members;
    private final SSFunction constructor;

    public ClassValue(ClassValue base, SSFunction constructor) {
        this(base, constructor, new ArrayList<>());
    }

    public ClassValue(ClassValue base, SSFunction constructor, List<ClassMember> members) {
        this.base = base;
        this.constructor = constructor;
        this.members = members;
    }


    private List<ClassMember> getStaticMembers() {
        return members.stream().filter(m -> m.isStatic).collect(Collectors.toList());
    }

    private ClassMember getMemberByName(List<ClassMember> members, String name) {
        return members.stream().anyMatch(m -> m.name.equals(name))
                ? members.stream().filter(m -> m.name.equals(name)).findFirst().get()
                : null;
    }

    @Override
    public Value get(Value key) {
        if (key.getType() != Type.String)
            throw new InvalidValueTypeException(key.getType());
        final ClassMember member = getMemberByName(getStaticMembers(), key.asString());
        if (member != null)
            return member instanceof ClassField
                    ? ((ClassField) member).getValue()
                    : Value.of(((ClassMethod) member).getFunction());
        throw new FieldNotFoundException(key);
    }

    @Override
    public void set(Value key, Value value) {
        if (key.getType() != Type.String)
            throw new InvalidValueTypeException(key.getType());
        final ClassMember member = getMemberByName(getStaticMembers(), key.asString());
        if (member == null)
            throw new FieldNotFoundException(key);
        if (member instanceof ClassMethod)
            throw new ClassMethodAccessException();
        ((ClassField) member).setValue(value);
    }

    public ClassValue getBase() {
        return base;
    }

    public Function getConstructor() {
        return constructor;
    }

    public List<ClassMember> getMembers() {
        return members;
    }

    public ObjectValue createObject(Value... args) {
        final ObjectValue object = new ObjectValue(this);
        for (ClassMember member : members) {
            if (member instanceof ClassField && !member.isStatic())
                object.getMapReference().put(Value.of(member.getName()), ((ClassField) member).getValue());
            else if (member instanceof ClassMethod)
                object.getMapReference().put(Value.of(member.getName()), Value.of(((ClassMethod) member).getFunction()));
        }
        if (constructor != null) {
            final SSFunction constructor = new SSFunction(object,
                    this.constructor.getArgs().toArray(new Argument[0]),
                    this.constructor.getBody());
            constructor.setName("$new");
            constructor.execute(args);
        }
        return object;
    }

    @Override
    public Double asNumber() {
        return Double.NaN;
    }

    @Override
    public Boolean asBoolean() {
        return false;
    }

    @Override
    public String asString() {
        return "class";
    }

    @Override
    public Object asObject() {
        return this;
    }

    @Override
    public Type getType() {
        return Type.Class;
    }

    @Override
    public Value clone() {
        return this.clone();
    }

    @Override
    public int compareTo(Value o) {
        if (o.getType() == Type.Class)
            return hashCode() - o.hashCode();
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Value && compareTo(((Value) obj)) == 0;
    }

    public static abstract class ClassMember {

        private final boolean isStatic;
        private final String name;

        public ClassMember(boolean isStatic, String name) {
            this.isStatic = isStatic;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean isStatic() {
            return isStatic;
        }
    }

    public static class ClassField extends ClassMember {
        private Value value;

        public ClassField(boolean isStatic, String name, Value value) {
            super(isStatic, name);
            this.value = value;
        }

        public ClassField(boolean isStatic, String name) {
            super(isStatic, name);
            this.value = new NullValue();
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value.hashCode()
                    ^ Boolean.hashCode(isStatic())
                    ^ getName().hashCode()
                    ^ 1024;
        }
    }

    public static class ClassMethod extends ClassMember {
        private final Function function;

        public ClassMethod(boolean isStatic, String name, Function function) {
            super(isStatic, name);
            this.function = function;
        }

        public Function getFunction() {
            return function;
        }

        @Override
        public int hashCode() {
            return function.hashCode()
                    ^ Boolean.hashCode(isStatic())
                    ^ getName().hashCode()
                    ^ 2048;
        }
    }

    @Override
    public int hashCode() {
        return members.hashCode()
                ^ (constructor == null ? 1 : constructor.hashCode())
                ^ (base == null ? 1 : base.hashCode());
    }
}