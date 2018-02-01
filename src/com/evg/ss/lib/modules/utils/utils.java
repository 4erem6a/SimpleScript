package com.evg.ss.lib.modules.utils;

import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSArrayBuilder;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

public final class utils extends SSModule {

    private static Value compare(Value... args) {
        Arguments.checkArgcOrDie(args, 2);
        return Value.of(args[0].compareTo(args[1]));
    }

    private static Value hashCode(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        return new NumberValue(args[0].hashCode());
    }

    private static Value clone(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        return args[0].clone();
    }

    private static Value range(Value... args) {
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

    private static Value refcmp(Value... args) {
        Arguments.checkArgcOrDie(args, 2);
        return Value.of(args[0] == args[1]);
    }

    @Override
    public MapValue require() {
        final SSMapBuilder builder = new SSMapBuilder();
        builder.setMethod("hashCode", utils::hashCode);
        builder.setMethod("clone", utils::clone);
        builder.setMethod("compare", utils::compare);
        builder.setMethod("refcmp", utils::refcmp);
        builder.setMethod("range", utils::range);
        return builder.build();
    }
}