package com.evg.ss.lib.modules.Arrays;

import com.evg.ss.containers.Functions;
import com.evg.ss.exceptions.ArgumentCountMismatchException;
import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.lib.modules.SSModuleInfo;
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
        Functions.put("length", this::length);
    }

    private Value length(Value... args) {
        if (args.length != 1)
            throw new ArgumentCountMismatchException("length", args.length);
        if (!(args[0] instanceof StringValue || args[0] instanceof ArrayValue))
            throw new InvalidValueTypeException(args[0].getType());
        final ArrayValue array = args[0] instanceof StringValue ?
                ((StringValue) args[0]).asCharArray() :
                ((ArrayValue) args[0]);
        return new NumberValue(array.length());
    }
}