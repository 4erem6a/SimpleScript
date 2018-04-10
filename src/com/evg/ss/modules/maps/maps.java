package com.evg.ss.modules.maps;

import com.evg.ss.lib.MapMatcher;
import com.evg.ss.modules.SSExports;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.values.*;

public final class maps {

    @SSExports("MAP_EMPTY")
    public final static Value MAP_EMPTY = new MapValue();

    @SSExports("toArray")
    public static Value toArray(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        if (!Arguments.checkArgTypes(args, Types.Map))
            return new UndefinedValue();
        return ((MapValue) args[0]).toArray();
    }

    @SSExports("size")
    public static Value size(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        if (!Arguments.checkArgTypes(args, Types.Map))
            return new UndefinedValue();
        return new NumberValue(((MapValue) args[0]).size());
    }

    @SSExports("match")
    public static Value match(Value... args) {
        Arguments.checkArgcOrDie(args, 2);
        if (!Arguments.checkArgTypes(args, Types.Map, Types.Map))
            return new UndefinedValue();
        return Value.of(new MapMatcher(((MapValue) args[0])).match(((MapValue) args[1])));
    }

    @SSExports("remove")
    public static Value remove(Value... args) {
        Arguments.checkArgcOrDie(args, 2);
        if (!Arguments.checkArgTypes(args, Types.Map, null))
            return new UndefinedValue();
        final MapValue value = ((MapValue) args[0]);
        value.getMapReference().remove(args[1]);
        return new UndefinedValue();
    }
}