package com.evg.ss.exceptions;

import com.evg.ss.values.Type;

public final class ArgumentTypeMismatchException extends SSExecutionException {
    public ArgumentTypeMismatchException(Type expected, Type got) {
        super(String.format("Argument type mismatch: expected %s but got %s.",
                expected.toString().toLowerCase(), got.toString().toLowerCase()));
    }

    public ArgumentTypeMismatchException() {
        super("Argument type mismatch.");
    }
}