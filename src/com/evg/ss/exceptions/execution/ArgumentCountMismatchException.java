package com.evg.ss.exceptions.execution;

import java.util.Arrays;

public final class ArgumentCountMismatchException extends SSExecutionException {
    public ArgumentCountMismatchException(int got, int... required) {
        super(String.format("Argument count mismatch: required %s but got %s", Arrays.toString(required), got));
    }

    public ArgumentCountMismatchException(int got) {
        super(String.format("Argument count mismatch: function does not take %d arguments.", got));
    }
}