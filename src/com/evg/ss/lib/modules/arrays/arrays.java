package com.evg.ss.lib.modules.arrays;

import com.evg.ss.exceptions.execution.InvalidValueException;
import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSArrayBuilder;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

import java.util.Arrays;

public final class arrays extends SSModule {

    @Override
    public MapValue require() {
        final SSMapBuilder builder = SSMapBuilder.create();
        builder.setMethod("length", this::length);
        builder.setMethod("resize", this::resize);
        builder.setMethod("create", this::create);
        return builder.build();
    }

    private Value create(Value... values) {
        for (Value value : values)
            Arguments.checkArgTypesOrDie(new Value[] {value}, Type.Number);
        final int[] sizeArr = Arrays.stream(values).mapToInt(v -> v.asNumber().intValue()).toArray();
        for (int size : sizeArr)
            if (size < 1)
                throw new InvalidValueException(Value.of(size));
        return createArray(values, 0);
    }

    private ArrayValue createArray(Value[] args, int index) {
        final int size = args[index].asNumber().intValue();
        final int last = args.length - 1;
        final SSArrayBuilder builder = SSArrayBuilder.create();
        if (index == last) {
            for (int i = 0; i < size; i++) {
                builder.setElement(Value.of(0));
            }
        } else if (index < last) {
            for (int i = 0; i < size; i++) {
                builder.setElement(createArray(args, index + 1));
            }
        }
        return builder.build();
    }

    private Value length(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        if (!Arguments.checkArgTypes(args, Type.Array) &&
                !Arguments.checkArgTypes(args, Type.String))
            throw new InvalidValueTypeException(args[0].getType());
        final ArrayValue array = args[0] instanceof StringValue ?
                ((StringValue) args[0]).asCharArray() :
                ((ArrayValue) args[0]);
        return new NumberValue(array.length());
    }

    private Value resize(Value... args) {
        Arguments.checkArgcOrDie(args, 2);
        if (!Arguments.checkArgTypes(args, Type.Array, Type.Number) &&
                !Arguments.checkArgTypes(args, Type.String, Type.Number))
            throw new InvalidValueTypeException(args[0].getType());
        final ArrayValue array = args[0] instanceof StringValue ?
                ((StringValue) args[0]).asCharArray() :
                ((ArrayValue) args[0]);
        array.resize(args[1].asNumber().intValue());
        return array;
    }
}