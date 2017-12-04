package com.evg.ss.lib.modules.arrays;

import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.lib.modules.SSModuleInfo;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

public final class ArraysModule implements SSModule {

    private static final String NAME = "Arrays";
    private static final String IMPORT_NAME = "arrays";
    private static final String AUTHOR = "4erem6a";

    @Override
    public SSModuleInfo getModuleInfo() {
        return new SSModuleInfo(NAME, IMPORT_NAME, AUTHOR);
    }

    @Override
    public void init() {

        final MapValue module = SSMapBuilder.create()
                .setMethod("length", this::length)
                .setMethod("resize", this::resize)
                .build();

        SS.Variables.put(IMPORT_NAME, module, true);

    }

    private Value length(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        if (Arguments.checkArgTypes(args, Type.Array) ||
                Arguments.checkArgTypes(args, Type.String))
            throw new InvalidValueTypeException(args[0].getType());
        final ArrayValue array = args[0] instanceof StringValue ?
                ((StringValue) args[0]).asCharArray() :
                ((ArrayValue) args[0]);
        return new NumberValue(array.length());
    }

    private Value resize(Value... args) {
        Arguments.checkArgcOrDie(args, 2);
        if (Arguments.checkArgTypes(args, Type.Array, Type.Number) ||
                Arguments.checkArgTypes(args, Type.String, Type.Number))
            throw new InvalidValueTypeException(args[0].getType());
        final ArrayValue array = args[0] instanceof StringValue ?
                ((StringValue) args[0]).asCharArray() :
                ((ArrayValue) args[0]);
        array.resize(args[1].asNumber().intValue());
        return array;
    }
}