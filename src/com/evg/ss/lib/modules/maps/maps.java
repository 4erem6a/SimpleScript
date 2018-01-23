package com.evg.ss.lib.modules.maps;

import com.evg.ss.lib.MapMatcher;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.NumberValue;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

public final class maps extends SSModule {

    private final static MapValue MAP_EMPTY = new MapValue();

    private static Value ssToArray(Value... args) {
        Arguments.checkArgTypesOrDie(args, Type.Map);
        return ((MapValue) args[0]).toArray();
    }

    private static Value ssSize(Value... args) {
        Arguments.checkArgTypesOrDie(args, Type.Map);
        return new NumberValue(((MapValue) args[0]).size());
    }

    private static Value match(Value... args) {
        Arguments.checkArgTypesOrDie(args, Type.Map, Type.Map);
        return Value.of(new MapMatcher(((MapValue) args[0])).match(((MapValue) args[1])));
    }

    @Override
    public MapValue require() {
        final SSMapBuilder builder = SSMapBuilder.create();
        builder.setField("MAP_EMPTY", MAP_EMPTY);
        builder.setMethod("toArray", maps::ssToArray);
        builder.setMethod("size", maps::ssSize);
        builder.setMethod("match", maps::match);
        return builder.build();
    }
}