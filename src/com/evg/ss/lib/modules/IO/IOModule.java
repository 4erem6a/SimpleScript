package com.evg.ss.lib.modules.IO;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.lib.modules.SSModuleInfo;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

import java.util.Scanner;

public final class IOModule implements SSModule {

    private static final String NAME = "IO";
    private static final String IMPORT_NAME = "io";
    private static final String AUTHOR = "4erem6a";

    @Override
    public SSModuleInfo getModuleInfo() {
        return new SSModuleInfo(NAME, IMPORT_NAME, AUTHOR);
    }

    @Override
    public void init() {
        SS.Functions.put("print", this::print);
        SS.Functions.put("println", this::println);
        SS.Functions.put("input", this::input);
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