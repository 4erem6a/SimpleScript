package com.evg.ss.util.args;

import com.evg.ss.exceptions.ArgumentCountMismatchException;
import com.evg.ss.exceptions.ArgumentTypeMismatchException;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

import java.util.stream.IntStream;

public final class Arguments {

    public static int checkArgcOrDie(Value[] args, int... required) {
        if (IntStream.of(required).anyMatch(i -> i == args.length))
            return IntStream.of(required).filter(i -> i == args.length).findFirst().getAsInt();
        throw new ArgumentCountMismatchException(args.length, required);
    }

    public static void checkArgTypesOrDie(Value[] args, Type... types) {
        if (args.length != types.length)
            throw new ArgumentCountMismatchException(args.length, types.length);
        for (int i = 0; i < args.length; i++) {
            if (types[i] != null && args[i].getType() != types[i])
                throw new ArgumentTypeMismatchException(args[i].getType(), types[i]);
        }
    }

    public static boolean checkArgTypes(Value[] args, Type... types) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].getType() != types[i])
                return false;
        }
        return true;
    }

}