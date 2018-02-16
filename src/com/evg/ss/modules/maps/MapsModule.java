package com.evg.ss.modules.maps;

import com.evg.ss.lib.MapMatcher;
import com.evg.ss.modules.SSExports;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.values.*;

@SSExports("maps")
public final class MapsModule {

    @SSExports("MAP_EMPTY")
    private final static Value MAP_EMPTY = new MapValue();

    @SSExports("toArray")
    private static Value toArray(Value... args) {
        if (!Arguments.checkArgTypes(args, Type.Map))
            return new UndefinedValue();
        return ((MapValue) args[0]).toArray();
    }

    @SSExports("size")
    private static Value size(Value... args) {
        if (!Arguments.checkArgTypes(args, Type.Map))
            return new UndefinedValue();
        return new NumberValue(((MapValue) args[0]).size());
    }

    @SSExports("match")
    private static Value match(Value... args) {
        if (!Arguments.checkArgTypes(args, Type.Map, Type.Map))
            return new UndefinedValue();
        return Value.of(new MapMatcher(((MapValue) args[0])).match(((MapValue) args[1])));
    }
}