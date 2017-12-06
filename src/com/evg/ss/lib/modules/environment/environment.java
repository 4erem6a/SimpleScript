package com.evg.ss.lib.modules.environment;

import com.evg.ss.Environment;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

public final class environment extends SSModule {

    @Override
    public MapValue require() {
        final SSMapBuilder environment = SSMapBuilder.create();
        environment.setMethod("setVariable", this::setEnvVariable);
        environment.setMethod("getVariable", this::getEnvVariable);
        return environment.build();
    }

    private Value setEnvVariable(Value... args) {
        Arguments.checkArgTypesOrDie(args, Type.String, null);
        return Value.of(Environment.setEnvVariable(args[0].asString(), args[1]));
    }

    private Value getEnvVariable(Value... args) {
        Arguments.checkArgTypesOrDie(args, Type.String);
        return Environment.getEnvVariable(args[0].asString());
    }

}
