package com.evg.ss.values;

import com.evg.ss.lib.*;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSArrayBuilder;
import com.evg.ss.util.builders.SSMapBuilder;

public class FunctionValue implements Value, Callable, NewCallable, Container {

    private Function value;

    public FunctionValue(Function value) {
        this.value = value;
    }

    public Function getValue() {
        return value;
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
        final String name;
        if (this.value instanceof SSFunction)
            name = ((SSFunction) this.value).getName();
        else name = null;
        return "function" + (name == null ? "" : " " + name);
    }

    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public Types getType() {
        return Types.Function;
    }

    @Override
    public int compareTo(Value o) {
        return (o.getType() == Types.Function ? hashCode() - o.hashCode() : -1);
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode() ^ Types.Function.hashCode();
    }

    @Override
    public Value clone() {
        if (value instanceof SSFunction)
            return new FunctionValue(((SSFunction) value).clone());
        return new FunctionValue(value);
    }

    @Override
    public Value call(Value... args) {
        return value.execute(args);
    }

    @Override
    public Value _new(Value... args) {
        return value instanceof ConstructorFunction
                ? ((ConstructorFunction) value).executeAsNew(args)
                : new UndefinedValue();
    }

    @Override
    public Value get(Value key) {
        switch (key.asString()) {
            case "apply":
                return Value.of(this::apply);
            case "info":
                return Value.of(this::info);
            case "name":
                return value instanceof SSFunction
                        ? Value.of(((SSFunction) value).getName())
                        : new UndefinedValue();
            case "hashCode":
                return Value.of(value.hashCode());
            case "isUDF":
                return Value.of(value instanceof SSFunction);
            case "toString":
                if (value instanceof SSFunction)
                    return Value.of(args -> Value.of(String.format("%s: %s",
                            getType().toString().toLowerCase(),
                            new MSCGenerator(((SSFunction) value).getBody()).generate())));
                else return Value.of(args -> Value.of(asString()));
        }
        return new UndefinedValue();
    }

    private Value apply(Value... args) {
        if (Arguments.checkArgc(args, 1) == -1)
            Arguments.checkArgcOrDie(args, 2);
        if (args.length == 2 && args[0].getType() != Types.Map)
            return new UndefinedValue();
        final MapValue callContext = args.length == 2
                ? ((MapValue) args[0]) : null;
        final Value __args = new Converter(
                args[args.length - 1].getType(),
                Types.Array).convert(args[args.length - 1]);
        if (__args.getType() == Types.Undefined)
            return new UndefinedValue();
        final Value[] _args = ((ArrayValue) __args).getValue();
        if (!(value instanceof SSFunction) && callContext != null)
            return new UndefinedValue();
        if (callContext != null)
            ((SSFunction) value).setCallContext(callContext);
        return new SSMapBuilder()
                .setField("result", value.execute(_args))
                .setField("callContext", callContext == null ? new NullValue() : callContext)
                .build();
    }

    private Value info(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        if (!(value instanceof SSFunction))
            return new UndefinedValue();
        final SSMapBuilder builder = new SSMapBuilder();
        builder.setField("name", Value.of(((SSFunction) value).getName()));
        final SSArrayBuilder array = new SSArrayBuilder();
        for (Argument arg : ((SSFunction) value).getArgs())
            array.setElement(new SSMapBuilder()
                    .setField("name", Value.of(arg.getName()))
                    .setField("isDefault", Value.of(arg.getValue() != null))
                    .setMethod("default", a -> (arg.getValue() == null ? new UndefinedValue() : arg.getValue().eval()))
                    .build());
        builder.setField("args", array.build());
        builder.setField("isVariadic", Value.of(((SSFunction) value).getArgs().isVariadic()));
        return builder.build();
    }

    @Override
    public void set(Value key, Value value) {
    }
}