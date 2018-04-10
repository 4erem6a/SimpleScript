package com.evg.ss.modules.environment;

import com.evg.ss.Environment;
import com.evg.ss.lib.Identifier;
import com.evg.ss.modules.SSExports;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.Types;
import com.evg.ss.values.UndefinedValue;
import com.evg.ss.values.Value;

import java.util.Map;

public final class environment {

    @SSExports("variables")
    public static Value variables(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        final SSMapBuilder builder = new SSMapBuilder();
        for (Map.Entry<String, Identifier> entry : Environment.getVariables())
            builder.setField(entry.getKey(), entry.getValue().getValue());
        return builder.build();
    }

    @SSExports("exists")
    public static Value exists(Value... args) {
        if (!Arguments.checkArgTypes(args, Types.String))
            return new UndefinedValue();
        return Value.of(Environment.envVariableExists(args[0].asString()));
    }

    @SSExports("set")
    public static Value setEnvVariable(Value... args) {
        if (!Arguments.checkArgTypes(args, Types.String, null))
            return new UndefinedValue();
        return Value.of(Environment.setEnvVariable(args[0].asString(), args[1]));
    }

    @SSExports("get")
    public static Value getEnvVariable(Value... args) {
        if (!Arguments.checkArgTypes(args, Types.String))
            return new UndefinedValue();
        return Environment.getEnvVariable(args[0].asString());
    }
}