package com.evg.ss.exceptions.execution;

public class InvalidCallContextException extends SSExecutionException {
    public InvalidCallContextException() {
        super("Invalid call context.");
    }
}