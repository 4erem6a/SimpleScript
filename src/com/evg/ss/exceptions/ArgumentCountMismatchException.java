package com.evg.ss.exceptions;

public final class ArgumentCountMismatchException extends SSExecutionException {
    public ArgumentCountMismatchException(String name, int argsCount) {
        super(String.format("Argument count mismatch: function %s does not take %d arguments.", name, argsCount));
    }
}