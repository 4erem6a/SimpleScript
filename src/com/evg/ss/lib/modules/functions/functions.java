package com.evg.ss.lib.modules.functions;

import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.execution.FunctionExecutionException;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.Utils;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

public final class functions extends SSModule {

    private static final String FILENAME = "functions.ss";

    @Override
    public MapValue require() {
        final String source = Utils.istream2string(getClass().getResourceAsStream(FILENAME));
        final MapValue constants = SimpleScript.fromSource(source).compile().require();
        final SSMapBuilder builder = SSMapBuilder.create(constants);
        builder.setMethod("execute", this::execute);
        return builder.build();
    }

    private Value execute(Value... args) {
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
        return SSMapBuilder.create()
                .setField("result", function.execute(_args))
                .setField("callContext", callContext == null ? new NullValue() : callContext)
                .build();
    }
}