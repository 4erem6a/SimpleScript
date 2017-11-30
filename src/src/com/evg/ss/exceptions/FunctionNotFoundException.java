package com.evg.ss.exceptions;

public final class FunctionNotFoundException extends SSExecutionException {
    public FunctionNotFoundException(String name) {
        super(String.format("Function '%s' does not exists.", name));
    }
}
