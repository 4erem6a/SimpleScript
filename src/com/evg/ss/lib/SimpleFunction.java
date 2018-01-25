package com.evg.ss.lib;

import com.evg.ss.util.args.Arguments;
import com.evg.ss.values.Value;

public class SimpleFunction implements Function {

    private final int argc;
    private final Function callback;

    public SimpleFunction(int argc, Function callback) {
        this.argc = argc;
        this.callback = callback;
    }

    @Override
    public Value execute(Value... args) {
        if (argc != -1)
            Arguments.checkArgcOrDie(args, argc);
        return callback.execute(args);
    }

    public int getArgc() {
        return argc;
    }

    public Function getCallback() {
        return callback;
    }
}