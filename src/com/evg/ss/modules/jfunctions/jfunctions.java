package com.evg.ss.modules.jfunctions;

import com.evg.ss.exceptions.execution.FunctionExecutionException;
import com.evg.ss.lib.Argument;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.modules.SSExports;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSArrayBuilder;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

public final class jfunctions {

    @SSExports("info")
    public static Value info(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        if (args[0].getType() != Type.Function || !(((FunctionValue) args[0]).getValue() instanceof SSFunction))
            return new UndefinedValue();
        final SSFunction function = ((SSFunction) ((FunctionValue) args[0]).getValue());
        final SSMapBuilder builder = new SSMapBuilder();
        builder.setField("name", Value.of(function.getName()));
        final SSArrayBuilder array = new SSArrayBuilder();
        for (Argument arg : function.getArgs())
            array.setElement(new SSMapBuilder()
                    .setField("name", Value.of(arg.getName()))
                    .setField("isDefault", Value.of(arg.getValue() == null))
                    .setField("isVariadic", Value.of(arg.isVariadic()))
                    .setMethod("default", a -> (arg.getValue() == null ? new NullValue() : arg.getValue().eval()))
                    .build());
        builder.setField("args", array.build());
        return builder.build();
    }

    @SSExports("getContext")
    public static Value getContext(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        if (args[0].getType() != Type.Function || !(((FunctionValue) args[0]).getValue() instanceof SSFunction))
            return new UndefinedValue();
        return ((SSFunction) ((FunctionValue) args[0]).getValue()).getCallContext();
    }

    @SSExports("execute")
    public static Value execute(Value... args) {
        if (!Arguments.checkArgTypes(args, Type.Function, Type.Map, Type.Array)
                && !Arguments.checkArgTypes(args, Type.Function, Type.Array))
            return new UndefinedValue();
        final Function function = ((FunctionValue) args[0]).getValue();
        final MapValue callContext = (args[1].getType() == Type.Map ? ((MapValue) args[1]) : null);
        final Value[] _args = ((ArrayValue) args[args[1].getType() == Type.Map ? 2 : 1]).getValue();
        if (!(function instanceof SSFunction) && callContext != null)
            throw new FunctionExecutionException("Unable to call function: call context is not supported.");
        if (callContext != null)
            ((SSFunction) function).setCallContext(callContext);
        return new SSMapBuilder()
                .setField("result", function.execute(_args))
                .setField("callContext", callContext == null ? new NullValue() : callContext)
                .build();
    }
}