package com.evg.ss.lib.modules.io;

import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;
import test.Main;

import java.io.File;
import java.util.Scanner;

public final class io extends SSModule {

    @Override
    public MapValue require() {
        final SSMapBuilder builder = SSMapBuilder.create();
        builder.setMethod("print", this::print);
        builder.setMethod("println", this::println);
        builder.setMethod("input", this::input);
        return builder.build();
    }

    private Value print(Value... args) {
        if (args != null)
            for (Value arg : args)
                System.out.print(StringValue.asStringValue(arg).asString() + " ");
        return new NullValue();
    }

    private Value println(Value... args) {
        if (args != null)
            for (Value arg : args)
                System.out.print(StringValue.asStringValue(arg).asString() + " ");
        System.out.println();
        return new NullValue();
    }

    private Value input(Value... args) {
        print(args);
        return new StringValue(new Scanner(System.in).next());
    }
}