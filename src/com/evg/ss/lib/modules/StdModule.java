package com.evg.ss.lib.modules;

import com.evg.ss.containers.Functions;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

public final class StdModule implements SSModule {
    @Override
    public void init() {
        Functions.put("println", this::println);
    }

    private Value println(Value... args) {
        if (args != null)
            for (Value arg : args)
                System.out.print(StringValue.asStringValue(arg).asString() + " ");
        System.out.println();
        return new NullValue();
    }
}