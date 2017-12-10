package com.evg.ss.util.args;

import com.evg.ss.exceptions.execution.ArgumentCountMismatchException;
import com.evg.ss.exceptions.execution.ArgumentTypeMismatchException;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

import java.util.stream.IntStream;

public final class Arguments {

    public static int checkArgcOrDie(Object[] args, int... required) {
        if (IntStream.of(required).anyMatch(i -> i == args.length))
            return IntStream.of(required).filter(i -> i == args.length).findFirst().getAsInt();
        throw new ArgumentCountMismatchException(args.length, required);
    }

    public static int checkArgc(Object[] args, int... required) {
        if (IntStream.of(required).anyMatch(i -> i == args.length))
            return IntStream.of(required).filter(i -> i == args.length).findFirst().getAsInt();
        return -1;
    }

    public static void checkArgTypesOrDie(Value[] args, Class... types) {
        if (args.length != types.length)
            throw new ArgumentCountMismatchException(args.length, types.length);
        for (int i = 0; i < args.length; i++) {
            if (types[i] != null && !args[i].getClass().equals(types[i]))
                throw new ArgumentTypeMismatchException();
        }
    }

    public static void checkArgTypesOrDie(Value[] args, Type... types) {
        if (args.length != types.length)
            throw new ArgumentCountMismatchException(args.length, types.length);
        for (int i = 0; i < args.length; i++) {
            if (types[i] != null && args[i].getType() != types[i])
                throw new ArgumentTypeMismatchException();
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