package com.evg.ss.modules.async;

import com.evg.ss.lib.Function;
import com.evg.ss.modules.SSExports;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.values.*;

import java.util.concurrent.FutureTask;

public final class async {

    @SSExports("current")
    public static Value current(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        return Value.of(Thread.currentThread().toString());
    }

    @SSExports("sleep")
    public static Value sleep(Value... args) {
        Arguments.checkArgcOrDie(args, 0, 1);
        final int time;
        if (args.length == 0)
            time = 1000;
        else time = args[0].asNumber().intValue();
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
        return new UndefinedValue();
    }

    @SSExports("await")
    public static Value await(Value... args) {
        Arguments.checkArgcOrDie(args, 1, 2);
        if (args[0].getType() != Types.Function)
            return new UndefinedValue();
        final Function function = ((FunctionValue) args[0]).getValue();
        switch (args.length) {
            case 1: {
                return _await(function);
            }
            case 2: {
                final Value[] _args;
                if (args[1].getType() == Types.Array) {
                    _args = ((ArrayValue) args[1]).getValue();
                } else _args = new Value[]{args[1]};
                return _await(function, _args);
            }
        }
        return new UndefinedValue();
    }

    private static Value _await(Function function, Value... args) {
        final FutureTask<Value> task = new FutureTask<>(() -> function.execute(args));
        final Thread thread = new Thread(task);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ignored) {
        }
        try {
            return task.get();
        } catch (Exception ignored) {
        }
        return new UndefinedValue();
    }
}