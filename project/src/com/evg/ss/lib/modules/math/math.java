package com.evg.ss.lib.modules.math;

import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

public final class math extends SSModule {
    @Override
    public MapValue require() {
        final SSMapBuilder math = SSMapBuilder.create();
        math.setMethod("sin", args -> Value.of(Math.sin(args[0].asNumber())));
        math.setMethod("cos", args -> Value.of(Math.cos(args[0].asNumber())));
        math.setMethod("sinh", args -> Value.of(Math.sinh(args[0].asNumber())));
        math.setMethod("cosh", args -> Value.of(Math.cosh(args[0].asNumber())));
        math.setMethod("asin", args -> Value.of(Math.asin(args[0].asNumber())));
        math.setMethod("acos", args -> Value.of(Math.acos(args[0].asNumber())));
        math.setMethod("abs", args -> Value.of(Math.abs(args[0].asNumber())));
        math.setMethod("tan", args -> Value.of(Math.tan(args[0].asNumber())));
        math.setMethod("tanh", args -> Value.of(Math.tanh(args[0].asNumber())));
        math.setMethod("atan", args -> Value.of(Math.atan(args[0].asNumber())));
        math.setMethod("atan2", args -> Value.of(Math.atan2(args[0].asNumber(), args[1].asNumber())));
        math.setMethod("cbrt", args -> Value.of(Math.cbrt(args[0].asNumber())));
        math.setMethod("ceil", args -> Value.of(Math.ceil(args[0].asNumber())));
        math.setMethod("ceil", args -> Value.of(Math.ceil(args[0].asNumber())));
        math.setMethod("exp", args -> Value.of(Math.exp(args[0].asNumber())));
        math.setMethod("expm1", args -> Value.of(Math.expm1(args[0].asNumber())));
        math.setMethod("floor", args -> Value.of(Math.floor(args[0].asNumber())));
        math.setMethod("getExponent", args -> Value.of(Math.getExponent(args[0].asNumber())));
        math.setMethod("hypot", args -> Value.of(Math.hypot(args[0].asNumber(), args[1].asNumber())));
        math.setMethod("IEEERemainder", args -> Value.of(Math.IEEEremainder(args[0].asNumber(), args[1].asNumber())));
        math.setMethod("log", args -> Value.of(Math.log(args[0].asNumber())));
        math.setMethod("log1p", args -> Value.of(Math.log1p(args[0].asNumber())));
        math.setMethod("log10", args -> Value.of(Math.log10(args[0].asNumber())));
        math.setMethod("max", args -> Value.of(Math.max(args[0].asNumber(), args[1].asNumber())));
        math.setMethod("min", args -> Value.of(Math.min(args[0].asNumber(), args[1].asNumber())));
        math.setMethod("nextAfter", args -> Value.of(Math.nextAfter(args[0].asNumber(), args[1].asNumber())));
        math.setMethod("nextDown", args -> Value.of(Math.nextDown(args[0].asNumber())));
        math.setMethod("nextUp", args -> Value.of(Math.nextUp(args[0].asNumber())));
        math.setMethod("rint", args -> Value.of(Math.rint(args[0].asNumber())));
        math.setMethod("pow", args -> Value.of(Math.pow(args[0].asNumber(), args[1].asNumber())));
        math.setMethod("round", args -> Value.of(Math.round(args[0].asNumber())));
        math.setMethod("signum", args -> Value.of(Math.signum(args[0].asNumber())));
        math.setMethod("scalb", args -> Value.of(Math.scalb(args[0].asNumber(), args[1].asNumber().intValue())));
        math.setMethod("sqrt", args -> Value.of(Math.sqrt(args[0].asNumber())));
        math.setMethod("toDegrees", args -> Value.of(Math.toDegrees(args[0].asNumber())));
        math.setMethod("toRadians", args -> Value.of(Math.toRadians(args[0].asNumber())));
        math.setMethod("ulp", args -> Value.of(Math.ulp(args[0].asNumber())));
        math.setField("Pi", Value.of(Math.PI));
        math.setField("E", Value.of(Math.E));
        return math.build();
    }

}