package com.evg.ss.modules.utils;

import com.evg.ss.modules.SSExports;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSArrayBuilder;
import com.evg.ss.values.*;

public final class utils {

    @SSExports("compare")
    public static Value compare(Value... args) {
        Arguments.checkArgcOrDie(args, 2);
        return Value.of(args[0].compareTo(args[1]));
    }

    @SSExports("hashCode")
    public static Value hashCode(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        return new NumberValue(args[0].hashCode());
    }

    @SSExports("clone")
    public static Value clone(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        return args[0].clone();
    }

    @SSExports("range")
    public static Value range(Value... args) {
        Arguments.checkArgcOrDie(args, 1, 2);
        if (args.length == 1)
            if (!Arguments.checkArgTypes(args, Type.Number))
                return new UndefinedValue();
            else if (!Arguments.checkArgTypes(args, Type.Number, Type.Number))
                return new UndefinedValue();
        int from = Math.round(args.length == 1 ? 0 : args[0].asNumber().intValue());
        int to = Math.round(args[args.length == 1 ? 0 : 1].asNumber().intValue());
        final SSArrayBuilder result = new SSArrayBuilder();
        if (from < to)
            for (int i = from; i < to; i++)
                result.setElement(Value.of(i));
        if (to < from)
            for (int i = from; i > to; i--)
                result.setElement(Value.of(i));
        return result.build();
    }

    @SSExports("refcmp")
    public static Value refcmp(Value... args) {
        Arguments.checkArgcOrDie(args, 2);
        return Value.of(args[0] == args[1]);
    }

    @SSExports("getClass")
    public static Value getClass(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        if (!Arguments.checkArgTypes(args, Type.Object))
            return new UndefinedValue();
        return ((ObjectValue) args[0]).getSSClass();
    }
}