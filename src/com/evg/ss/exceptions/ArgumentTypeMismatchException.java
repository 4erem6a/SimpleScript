package com.evg.ss.exceptions;

import com.evg.ss.values.Type;

public final class ArgumentTypeMismatchException extends SSExecutionException {
    public ArgumentTypeMismatchException(String name, Type expected, Type got) {
        super(String.format("Argument type mismatch in function %s: expected %s but got %s.",
                name, expected.toString().toLowerCase(), got.toString().toLowerCase()));
    }
}