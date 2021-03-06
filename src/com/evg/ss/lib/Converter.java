package com.evg.ss.lib;

import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Converter {

    private final Types from, to;

    public Converter(Types from, Types to) {
        this.from = from;
        this.to = to;
    }

    private static Value array2bool(Value array) {
        return Value.of(Arrays.stream(((ArrayValue) array).getValue()).map(Value::asBoolean).reduce((a, b) -> a && b).get());
    }

    private static Value array2map(Value array) {
        final List<Value> values = Arrays.stream(((ArrayValue) array).getValue()).collect(Collectors.toList());
        if (values.size() % 2 != 0)
            values.add(new NullValue());
        final SSMapBuilder mapBuilder = new SSMapBuilder();
        for (int i = 0; i < values.size(); i += 2)
            mapBuilder.setField(values.get(i), values.get(i + 1));
        return mapBuilder.build();
    }

    public Value convert(Value target) {
        if (to == Types.Null)
            return new NullValue();
        switch (from) {
            case Number:
                switch (to) {
                    case Number:
                        return target;
                    case String:
                        return Value.of(target.asNumber());
                    case Boolean:
                        return Value.of(target.asBoolean());
                    case Array:
                        return Value.of(target);
                    case Type:
                        return Value.of(target.getType());
                    default:
                        return new UndefinedValue();
                }
            case String:
                switch (to) {
                    case Number:
                        return Value.of(target.asNumber());
                    case String:
                        return target;
                    case Boolean:
                        return Value.of(target.asBoolean());
                    case Array:
                        return Value.of(target);
                    case Type:
                        return Value.of(target.getType());
                    default:
                        return new UndefinedValue();
                }
            case Boolean:
                switch (to) {
                    case Number:
                        return Value.of(target.asNumber());
                    case String:
                        return Value.of(target.asString());
                    case Boolean:
                        return target;
                    case Array:
                        return Value.of(target);
                    case Type:
                        return Value.of(target.getType());
                    default:
                        return new UndefinedValue();
                }
            case Array:
                switch (to) {
                    case String:
                        return Value.of(target.asString());
                    case Boolean:
                        return array2bool(target);
                    case Array:
                        return target;
                    case Type:
                        return Value.of(target.getType());
                    case Map:
                        return array2map(target);
                    default:
                        return new UndefinedValue();
                }
            case Function:
                switch (to) {
                    case Function:
                        return target;
                    case Type:
                        return Value.of(target.getType());
                    case Array:
                        return Value.of(target);
                    case String:
                        return Value.of(target.asString());
                    case Boolean:
                        return Value.of(target.asBoolean());
                    case Number:
                        return Value.of(target.asNumber());
                    default:
                        return new UndefinedValue();
                }
            case Type:
                switch (to) {
                    case Type:
                        return Value.of(target.getType());
                    case String:
                        return Value.of(target.asString());
                    case Boolean:
                        return Value.of(target.asBoolean());
                    case Number:
                        return Value.of(target.asNumber());
                    case Array:
                        return Value.of(target);
                    default:
                        return new UndefinedValue();
                }
            case Map:
                switch (to) {
                    case String:
                        return Value.of(target.asString());
                    case Array:
                        return ((MapValue) target).toArray();
                    case Type:
                        return Value.of(target.getType());
                    case Map:
                        return target;
                    case Boolean:
                        return Value.of(target.asBoolean());
                    case Number:
                        return Value.of(target.asNumber());
                    default:
                        return new UndefinedValue();
                }
            case Null:
                switch (to) {
                    case Boolean:
                        return Value.of(target.asBoolean());
                    case Number:
                        return Value.of(target.asNumber());
                    case Type:
                        return Value.of(target.getType());
                    case Array:
                        return Value.of(target);
                    case String:
                        return Value.of(target.asString());
                    default:
                        return new NullValue();
                }
            case Class:
                switch (to) {
                    case Boolean:
                        return Value.of(target.asBoolean());
                    case Number:
                        return Value.of(target.asNumber());
                    case Type:
                        return Value.of(target.getType());
                    case Array:
                        return Value.of(target);
                    case String:
                        return Value.of(target.asString());
                    default:
                        return new UndefinedValue();
                }
        }
        return new UndefinedValue();
    }
}