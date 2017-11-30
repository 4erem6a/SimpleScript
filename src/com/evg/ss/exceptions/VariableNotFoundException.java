package com.evg.ss.exceptions;

public final class VariableNotFoundException extends SSExecutionException {
    public VariableNotFoundException(String name) {
        super(String.format("Variable '%s' does not exists.", name));
    }
}
