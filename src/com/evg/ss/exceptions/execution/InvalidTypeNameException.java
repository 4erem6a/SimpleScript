package com.evg.ss.exceptions.execution;

public class InvalidTypeNameException extends SSExecutionException {
    public InvalidTypeNameException(String typename) {
        super(String.format("Invalid typename: %s.", typename));
    }
}
