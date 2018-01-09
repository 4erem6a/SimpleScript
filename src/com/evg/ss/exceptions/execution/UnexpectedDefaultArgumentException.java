package com.evg.ss.exceptions.execution;

public class UnexpectedDefaultArgumentException extends SSExecutionException {
    public UnexpectedDefaultArgumentException() {
        super("Arguments with default value must be last.");
    }
}