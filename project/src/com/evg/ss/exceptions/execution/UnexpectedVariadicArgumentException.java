package com.evg.ss.exceptions.execution;

public class UnexpectedVariadicArgumentException extends SSExecutionException {

    public UnexpectedVariadicArgumentException() {
        super("Unexpected variadic argument: Variadic argument must be last.");
    }

}