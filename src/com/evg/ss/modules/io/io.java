package com.evg.ss.modules.io;

import com.evg.ss.modules.SSExports;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.UndefinedValue;
import com.evg.ss.values.Value;

import java.util.Scanner;

public final class io {

    @SSExports("print")
    public static Value print(Value... args) {
        for (Value arg : args)
            System.out.print(StringValue.asStringValue(arg).asString());
        return new UndefinedValue();
    }

    @SSExports("println")
    public static Value println(Value... args) {
        for (Value arg : args)
            System.out.print(StringValue.asStringValue(arg).asString());
        System.out.println();
        return new UndefinedValue();
    }

    @SSExports("input")
    public static Value input(Value... args) {
        print(args);
        return new StringValue(new Scanner(System.in).nextLine());
    }
}