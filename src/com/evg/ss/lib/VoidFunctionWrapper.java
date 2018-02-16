package com.evg.ss.lib;

import com.evg.ss.util.args.Arguments;
import com.evg.ss.values.UndefinedValue;
import com.evg.ss.values.Value;

public class VoidFunctionWrapper implements Function {

    private final VoidFunction function;

    public VoidFunctionWrapper(VoidFunction function) {
        this.function = function;
    }

    @Override
    public Value execute(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        function.action();
        return new UndefinedValue();
    }
}