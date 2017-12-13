package com.evg.ss.lib;

import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    private final Type from, to;

    public Converter(Type from, Type to) {
        this.from = from;
        this.to = to;
    }

    public Value convert(Value target) {
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
                        return new NullValue();
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
                        return new NullValue();
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
                        return new NullValue();
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
                        return new NullValue();
                }
            case Function:
                switch (to) {
                    case Function:
                        return target;
                    case Type:
                        return Value.of(target.getType());
                    default:
                        return new NullValue();
                }
            case Type:
                switch (to) {
                    case Type:
                        return Value.of(target.getType());
                    default:
                        return new NullValue();
                }
            case Null:
                return new NullValue();
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
                    default:
                        return new NullValue();
                }
        }
        return new NullValue();
    }

    private static Value array2bool(Value array) {
        return Value.of(Arrays.stream(((ArrayValue) array).getValue()).map(Value::asBoolean).reduce((a, b) -> a && b).get());
    }

    private static Value array2map(Value array) {
        final List<Value> values = Arrays.stream(((ArrayValue) array).getValue()).collect(Collectors.toList());
        if (values.size() % 2 != 0)
            values.add(new NullValue());
        final SSMapBuilder mapBuilder = SSMapBuilder.create();
        for (int i = 0; i < values.size(); i += 2)
            mapBuilder.setField(values.get(i), values.get(i + 1));
        return mapBuilder.build();
    }
}