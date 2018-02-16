package com.evg.ss.values;

import com.evg.ss.exceptions.execution.ClassMethodAccessException;
import com.evg.ss.exceptions.execution.IdentifierAlreadyExistsException;
import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.Argument;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.parser.ast.ReturnStatement;
import com.evg.ss.parser.ast.ValueExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassValue implements Value, Container, NewCallable {

    private final ClassValue base;
    private final Map<String, ClassMember> members;
    private final SSFunction constructor;
    private final ObjectValue staticContext;
    private String name = null;

    public ClassValue(ClassValue base, SSFunction constructor) {
        this(base, constructor, new ArrayList<>());
    }

    public ClassValue(ClassValue base, SSFunction constructor, List<ClassMember> members) {
        this.base = base;
        this.constructor = (constructor == null
                ? base == null
                ? new SSFunction(null, new Argument[0], new ReturnStatement(new ValueExpression()))
                : base.constructor
                : constructor);
        this.members = new HashMap<>();
        for (ClassMember member : members) {
            if (this.members.containsKey(member.name))
                throw new IdentifierAlreadyExistsException(member.name);
            this.members.put(member.name, member);
        }
        staticContext = createObject_v2(true);
    }

    private static String getMethodName(String className, String memberName) {
        return String.format("$%s.%s", className == null ? "" : className, memberName);
    }

    private List<ClassMember> getStaticMembers() {
        return members.values().stream().filter(m -> m.isStatic).collect(Collectors.toList());
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
        if (key.asString().equals("static"))
            return staticContext;
        final ClassMember member = getMemberByName(getStaticMembers(), key.asString());
        if (member != null)
            return staticContext.get(key);
        return new UndefinedValue();
    }

    @Override
    public void set(Value key, Value value) {
        if (key.getType() != Type.String)
            throw new InvalidValueTypeException(key.getType());
        final ClassMember member = getMemberByName(getStaticMembers(), key.asString());
        if (member == null)
            return;
        if (member instanceof ClassMethod) {
            throw new ClassMethodAccessException();
        }
        ((ClassField) member).setValue(value);
        staticContext.set(key, value);
    }

    public ClassValue getBase() {
        return base;
    }

    public Function getConstructor() {
        return constructor;
    }

    public List<ClassMember> getMembers() {
        return new ArrayList<>(members.values());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectValue construct(Value... args) {
        final ObjectValue object = createObject_v2(false);
        if (constructor != null) {
            final SSFunction constructor = new SSFunction(object,
                    this.constructor.getArgs().toArray(new Argument[0]),
                    this.constructor.getBody());
            constructor.setName(getMethodName(name, "new"));
            constructor.execute(args);
        }
        return object;
    }

    private ObjectValue createObject_v2(boolean _static) {
        final ObjectValue object;
        if (base == null)
            object = new ObjectValue(this);
        else object = base.createObject_v2(_static);
        for (ClassMember member : members.values()) {
            if (member.isStatic && !_static || !member.isStatic && _static)
                continue;
            if (member instanceof ClassField)
                object.getMapReference().put(Value.of(member.getName()), ((ClassField) member).getValue());
            if (member instanceof ClassMethod) {
                final Value method = Value.of(((ClassMethod) member).getFunction()).clone();
                ((SSFunction) ((FunctionValue) method).getValue()).setName(getMethodName(name, member.getName()));
                ((SSFunction) ((FunctionValue) method).getValue()).setCallContext(object);
                object.getMapReference().put(Value.of(member.getName()), method);
            }
        }
        return object;
    }

    public ObjectValue getStaticContext() {
        return staticContext;
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
        return new ClassValue(base, constructor, getMembers());
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

    @Override
    public int hashCode() {
        return members.hashCode()
                ^ (constructor == null ? 1 : constructor.hashCode())
                ^ (base == null ? 1 : base.hashCode());
    }

    @Override
    public Value _new(Value... args) {
        return construct(args);
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
}