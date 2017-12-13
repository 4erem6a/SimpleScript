package com.evg.ss.lib.modules.arrays;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

public final class arrays extends SSModule {

    @Override
    public MapValue require() {
        final SSMapBuilder builder = SSMapBuilder.create();
        builder.setMethod("length", this::length);
        builder.setMethod("resize", this::resize);
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