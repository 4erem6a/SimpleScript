package com.evg.ss.lib.modules.utils;

import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.NumberValue;
import com.evg.ss.values.Value;

public final class utils extends SSModule {

    @Override
    public MapValue require() {
        final SSMapBuilder builder = SSMapBuilder.create();
        builder.setMethod("hashCode", utils::ssHashCode);
        return builder.build();
    }

    private static Value ssHashCode(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        return new NumberValue(args[0].hashCode());
    }
}