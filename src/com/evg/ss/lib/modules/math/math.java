package com.evg.ss.lib.modules.math;

import com.evg.ss.lib.Function;
import com.evg.ss.lib.SimpleFunction;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Type;
import com.evg.ss.values.UndefinedValue;
import com.evg.ss.values.Value;

public final class math extends SSModule {
    @Override
    public MapValue require() {
        final SSMapBuilder math = SSMapBuilder.create();
        math.setMethod("sin", new MathFunction(1, args -> Value.of(Math.sin(args[0].asNumber()))));
        math.setMethod("cos", new MathFunction(1, args -> Value.of(Math.cos(args[0].asNumber()))));
        math.setMethod("sinh", new MathFunction(1, args -> Value.of(Math.sinh(args[0].asNumber()))));
        math.setMethod("cosh", new MathFunction(1, args -> Value.of(Math.cosh(args[0].asNumber()))));
        math.setMethod("asin", new MathFunction(1, args -> Value.of(Math.asin(args[0].asNumber()))));
        math.setMethod("acos", new MathFunction(1, args -> Value.of(Math.acos(args[0].asNumber()))));
        math.setMethod("abs", new MathFunction(1, args -> Value.of(Math.abs(args[0].asNumber()))));
        math.setMethod("tan", new MathFunction(1, args -> Value.of(Math.tan(args[0].asNumber()))));
        math.setMethod("tanh", new MathFunction(1, args -> Value.of(Math.tanh(args[0].asNumber()))));
        math.setMethod("atan", new MathFunction(1, args -> Value.of(Math.atan(args[0].asNumber()))));
        math.setMethod("atan2", new MathFunction(2, args -> Value.of(Math.atan2(args[0].asNumber(), args[1].asNumber()))));
        math.setMethod("cbrt", new MathFunction(1, args -> Value.of(Math.cbrt(args[0].asNumber()))));
        math.setMethod("ceil", new MathFunction(1, args -> Value.of(Math.ceil(args[0].asNumber()))));
        math.setMethod("ceil", new MathFunction(1, args -> Value.of(Math.ceil(args[0].asNumber()))));
        math.setMethod("exp", new MathFunction(1, args -> Value.of(Math.exp(args[0].asNumber()))));
        math.setMethod("expm1", new MathFunction(1, args -> Value.of(Math.expm1(args[0].asNumber()))));
        math.setMethod("floor", new MathFunction(1, args -> Value.of(Math.floor(args[0].asNumber()))));
        math.setMethod("getExponent", new MathFunction(1, args -> Value.of(Math.getExponent(args[0].asNumber()))));
        math.setMethod("hypot", new MathFunction(2, args -> Value.of(Math.hypot(args[0].asNumber(), args[1].asNumber()))));
        math.setMethod("IEEERemainder", new MathFunction(2, args -> Value.of(Math.IEEEremainder(args[0].asNumber(), args[1].asNumber()))));
        math.setMethod("log", new MathFunction(1, args -> Value.of(Math.log(args[0].asNumber()))));
        math.setMethod("log1p", new MathFunction(1, args -> Value.of(Math.log1p(args[0].asNumber()))));
        math.setMethod("log10", new MathFunction(1, args -> Value.of(Math.log10(args[0].asNumber()))));
        math.setMethod("max", new MathFunction(2, args -> Value.of(Math.max(args[0].asNumber(), args[1].asNumber()))));
        math.setMethod("min", new MathFunction(2, args -> Value.of(Math.min(args[0].asNumber(), args[1].asNumber()))));
        math.setMethod("nextAfter", new MathFunction(2, args -> Value.of(Math.nextAfter(args[0].asNumber(), args[1].asNumber()))));
        math.setMethod("nextDown", new MathFunction(1, args -> Value.of(Math.nextDown(args[0].asNumber()))));
        math.setMethod("nextUp", new MathFunction(1, args -> Value.of(Math.nextUp(args[0].asNumber()))));
        math.setMethod("rint", new MathFunction(1, args -> Value.of(Math.rint(args[0].asNumber()))));
        math.setMethod("pow", new MathFunction(2, args -> Value.of(Math.pow(args[0].asNumber(), args[1].asNumber()))));
        math.setMethod("round", new MathFunction(1, args -> Value.of(Math.round(args[0].asNumber()))));
        math.setMethod("signum", new MathFunction(1, args -> Value.of(Math.signum(args[0].asNumber()))));
        math.setMethod("scalb", new MathFunction(2, args -> Value.of(Math.scalb(args[0].asNumber(), args[1].asNumber().intValue()))));
        math.setMethod("sqrt", new MathFunction(1, args -> Value.of(Math.sqrt(args[0].asNumber()))));
        math.setMethod("toDegrees", new MathFunction(1, args -> Value.of(Math.toDegrees(args[0].asNumber()))));
        math.setMethod("toRadians", new MathFunction(1, args -> Value.of(Math.toRadians(args[0].asNumber()))));
        math.setMethod("ulp", new MathFunction(1, args -> Value.of(Math.ulp(args[0].asNumber()))));
        math.setMethod("random", new MathFunction(0, args -> Value.of(Math.random())));
        math.setField("PI", Value.of(Math.PI));
        math.setField("E", Value.of(Math.E));
        return math.build();
    }

    private static class MathFunction extends SimpleFunction {

        public MathFunction(int argc, Function callback) {
            super(argc, callback);
        }

        @Override
        public Value execute(Value... args) {
            if (getArgc() != -1)
                Arguments.checkArgcOrDie(args, getArgc());
            for (Value arg : args)
                if (arg.getType() != Type.Number)
                    return new UndefinedValue();
            return getCallback().execute(args);
        }
    }
}