package com.evg.ss.exceptions;

import java.util.Arrays;

public final class ArgumentCountMismatchException extends SSExecutionException {
    public ArgumentCountMismatchException(int required, int... got) {
        super(String.format("Argument count mismatch: required %d but got %s", required, Arrays.toString(got)));
    }

    public ArgumentCountMismatchException(int got) {
        super(String.format("Argument count mismatch: function does not take %d arguments.", got));
    }
}