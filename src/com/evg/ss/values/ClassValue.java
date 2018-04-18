package com.evg.ss.values;

import com.evg.ss.exceptions.execution.ClassMethodAccessException;
import com.evg.ss.exceptions.execution.IdentifierAlreadyExistsException;
import com.evg.ss.lib.Arguments;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.parser.ast.ReturnStatement;
import com.evg.ss.parser.ast.ValueExpression;
import com.evg.ss.util.builders.SSMapBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassValue implements Value, Container, NewCallable {

    private final ClassValue base;
    private final Map<Value, ClassMember> members;
    private final SSFunction constructor;
    private final ObjectValue staticContext;
    private String name = null;

    public ClassValue(ClassValue base, SSFunction constructor, List<ClassMember> members) {
        this.base = base;
        this.constructor = (constructor == null
                ? base == null
                ? new SSFunction(null, new Arguments(), new ReturnStatement(new ValueExpression()))
                : base.constructor
                : constructor);
        this.members = new HashMap<>();
        for (ClassMember member : members) {
            if (this.members.containsKey(member.key))
                throw new IdentifierAlreadyExistsException(member.key.asString());
            this.members.put(member.key, member);
        }
        staticContext = createObject(true);
    }

    public ClassValue(ClassValue base, SSFunction constructor, List<ClassMember> members, String name) {
        this(base, constructor, members);
        this.name = name;
    }

    private static String getMethodName(String className, String memberName) {
        return String.format("$%s.%s", className == null ? "" : className, memberName);
    }

    private List<ClassMember> getStaticMembers() {
        return members.values().stream().filter(m -> m.isStatic).collect(Collectors.toList());
    }

    private ClassMember getMemberByKey(List<ClassMember> members, Value key) {
        return members.stream().anyMatch(m -> m.key.equals(key))
                ? members.stream().filter(m -> m.key.equals(key)).findFirst().get()
                : null;
    }

    @Override
    public Value get(Value key) {
        final ClassMember member = getMemberByKey(getStaticMembers(), key);
        if (member != null)
            return staticContext.get(key);
        return new UndefinedValue();
    }

    @Override
    public void set(Value key, Value value) {
        final ClassMember member = getMemberByKey(getStaticMembers(), key);
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
        final ObjectValue object = createObject(false);
        final SSFunction constructor = new SSFunction(object,
                this.constructor.getArgs(),
                this.constructor.getBody());
        constructor.setName(getMethodName(name, "new"));
        constructor.execute(args);
        object.setConstructor(constructor);
        return object;
    }

    private ObjectValue createObject(boolean _static) {
        final ObjectValue object;
        if (base == null)
            object = new ObjectValue(this);
        else object = new ObjectValue(base.createObject(_static), this);
        for (ClassMember member : members.values()) {
            if (member.isStatic && !_static || !member.isStatic && _static)
                continue;
            if (member instanceof ClassField)
                object.getMapReference().put(member.getKey(), ((ClassField) member).getValue());
            if (member instanceof ClassMethod) {
                final Value method = Value.of(((ClassMethod) member).getFunction()).clone();
                ((SSFunction) ((FunctionValue) method).getValue()).setName(getMethodName(name, member.getKey().asString()));
                ((SSFunction) ((FunctionValue) method).getValue()).setCallContext(object);
                object.getMapReference().put(member.getKey(), method);
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
        return true;
    }

    @Override
    public String asString() {
        return "class" + (this.name == null ? "" : " " + this.name);
    }

    @Override
    public Object asObject() {
        return this;
    }

    @Override
    public Types getType() {
        return Types.Class;
    }

    @Override
    public Value clone() {
        return new ClassValue(base, constructor, getMembers());
    }

    @Override
    public int compareTo(Value o) {
        if (o.getType() == Types.Class)
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
                ^ Types.Class.hashCode()
                ^ (constructor == null ? 1 : constructor.hashCode())
                ^ (base == null ? 1 : base.hashCode());
    }

    @Override
    public Value _new(Value... args) {
        return construct(args);
    }

    public MapValue getSignature() {
        final SSMapBuilder builder;
        if (base == null)
            builder = new SSMapBuilder();
        else builder = new SSMapBuilder(base.getSignature());
        for (Map.Entry<Value, ClassMember> memberEntry : members.entrySet()) {
            if (!memberEntry.getValue().isStatic())
                builder.setField(memberEntry.getKey(), new UndefinedValue());
        }
        return builder.build();
    }

    public boolean is(ClassValue _class) {
        return this.equals(_class) || this.base != null && this.base.is(_class);
    }

    public static abstract class ClassMember {

        private final boolean isStatic;
        private final Value key;

        public ClassMember(boolean isStatic, Value key) {
            this.isStatic = isStatic;
            this.key = key;
        }

        public Value getKey() {
            return key;
        }

        public boolean isStatic() {
            return isStatic;
        }
    }

    public static class ClassField extends ClassMember {
        private Value value;

        public ClassField(boolean isStatic, Value key, Value value) {
            super(isStatic, key);
            this.value = value;
        }

        public ClassField(boolean isStatic, Value name) {
            this(isStatic, name, new NullValue());
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
                    ^ getKey().hashCode()
                    ^ 1024;
        }
    }

    public static class ClassMethod extends ClassMember {
        private final Function function;

        public ClassMethod(boolean isStatic, Value key, Function function) {
            super(isStatic, key);
            this.function = function;
        }

        public Function getFunction() {
            return function;
        }

        @Override
        public int hashCode() {
            return function.hashCode()
                    ^ Boolean.hashCode(isStatic())
                    ^ getKey().hashCode()
                    ^ 2048;
        }
    }
}