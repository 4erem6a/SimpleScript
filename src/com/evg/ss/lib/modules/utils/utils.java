package com.evg.ss.lib.modules.utils;

import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSArrayBuilder;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.NumberValue;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

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
            Arguments.checkArgTypesOrDie(args, Type.Number);
        else Arguments.checkArgTypesOrDie(args, Type.Number, Type.Number);
        int from = Math.round(args.length == 1 ? 0 : args[0].asNumber().intValue());
        int to = Math.round(args[args.length == 1 ? 0 : 1].asNumber().intValue());
        final SSArrayBuilder result = SSArrayBuilder.create();
        if (from < to)
            for (int i = from; i < to; i++)
                result.setElement(Value.of(i));
        if (to < from)
            for (int i = from; i > to; i--)
                result.setElement(Value.of(i));
        return result.build();
    }

    @Override
    public MapValue require() {
        final SSMapBuilder builder = SSMapBuilder.create();
        builder.setMethod("hashCode", utils::hashCode);
        builder.setMethod("clone", utils::clone);
        builder.setMethod("compare", utils::compare);
        builder.setMethod("range", utils::range);
        return builder.build();
    }
}