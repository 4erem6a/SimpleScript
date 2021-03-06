package com.evg.ss.modules.arrays;

import com.evg.ss.exceptions.execution.InvalidValueException;
import com.evg.ss.lib.Function;
import com.evg.ss.modules.SSExports;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSArrayBuilder;
import com.evg.ss.values.*;

import java.util.Arrays;
import java.util.Comparator;

public final class arrays {

    @SSExports("sort")
    public static Value sort(Value... args) {
        if (!Arguments.checkArgTypes(args, Types.Array, Types.Function))
            return new UndefinedValue();
        final Value[] array = ((ArrayValue) args[0]).getValue().clone();
        final Function function = ((FunctionValue) args[1]).getValue();
        Arrays.sort(array, (a, b) -> function.execute(a, b).asNumber().intValue());
        return Value.of(array);
    }

    @SSExports("sortBy")
    public static Value sortBy(Value... args) {
        if (!Arguments.checkArgTypes(args, Types.Array, Types.Function))
            return new UndefinedValue();
        final Value[] array = ((ArrayValue) args[0]).getValue().clone();
        final Function function = ((FunctionValue) args[1]).getValue();
        Arrays.sort(array, Comparator.comparing(function::execute));
        return Value.of(array);
    }

    @SSExports("create")
    public static Value create(Value... args) {
        for (Value value : args)
            if (!Arguments.checkArgTypes(new Value[]{value}, Types.Number))
                return new UndefinedValue();
        final int[] sizeArr = Arrays.stream(args).mapToInt(v -> v.asNumber().intValue()).toArray();
        for (int size : sizeArr)
            if (size < 1)
                throw new InvalidValueException(Value.of(size));
        return createArray(args, 0);
    }

    private static ArrayValue createArray(Value[] args, int index) {
        final int size = args[index].asNumber().intValue();
        final int last = args.length - 1;
        final SSArrayBuilder builder = new SSArrayBuilder();
        if (index == last) {
            for (int i = 0; i < size; i++) {
                builder.setElement(Value.of(0));
            }
        } else if (index < last) {
            for (int i = 0; i < size; i++) {
                builder.setElement(createArray(args, index + 1));
            }
        }
        return builder.build();
    }

    @SSExports("length")
    public static Value length(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        if (!Arguments.checkArgTypes(args, Types.Array) &&
                !Arguments.checkArgTypes(args, Types.String))
            return new UndefinedValue();
        final ArrayValue array = args[0] instanceof StringValue ?
                ((StringValue) args[0]).asCharArray() :
                ((ArrayValue) args[0]);
        return new NumberValue(array.length());
    }

    @SSExports("resize")
    public static Value resize(Value... args) {
        Arguments.checkArgcOrDie(args, 2);
        if (!Arguments.checkArgTypes(args, Types.Array, Types.Number) &&
                !Arguments.checkArgTypes(args, Types.String, Types.Number))
            return new UndefinedValue();
        final ArrayValue array = args[0] instanceof StringValue ?
                ((StringValue) args[0]).asCharArray() :
                ((ArrayValue) args[0]);
        array.resize(args[1].asNumber().intValue());
        return array;
    }

    @SSExports("copy")
    public static Value copy(Value... args) {
        Arguments.checkArgcOrDie(args, 5);
        if (!Arguments.checkArgTypes(args, Types.Array, Types.Number, Types.Array, Types.Number, Types.Number) &&
                !Arguments.checkArgTypes(args, Types.String, Types.Number, Types.String, Types.Number, Types.Number))
            return new UndefinedValue();
        final ArrayValue from = args[0] instanceof StringValue ?
                ((StringValue) args[0]).asCharArray() :
                ((ArrayValue) args[0]);
        final ArrayValue to = args[2] instanceof StringValue ?
                ((StringValue) args[2]).asCharArray() :
                ((ArrayValue) args[2]);
        final int fromIdx = args[1].asNumber().intValue();
        final int toIdx = args[3].asNumber().intValue();
        final int length = args[4].asNumber().intValue();
        if (fromIdx < 0 || fromIdx >= from.length())
            throw new InvalidValueException(args[1]);
        if (toIdx < 0 || toIdx >= to.length())
            throw new InvalidValueException(args[3]);
        if (length < 0)
            throw new InvalidValueException(args[4]);
        System.arraycopy(from.getValue(), fromIdx, to.getValue(), toIdx, length);
        return to;
    }
}