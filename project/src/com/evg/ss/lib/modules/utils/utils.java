package com.evg.ss.lib.modules.utils;

import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.NumberValue;
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

    @Override
    public MapValue require() {
        final SSMapBuilder builder = SSMapBuilder.create();
        builder.setMethod("hashCode", utils::hashCode);
        builder.setMethod("clone", utils::clone);
        builder.setMethod("compare", utils::compare);
        return builder.build();
    }
}