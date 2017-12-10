package com.evg.ss.exceptions.execution;

public final class VariableNotFoundException extends SSExecutionException {
    public VariableNotFoundException(String name) {
        super(String.format("Variable '%s' does not exists.", name));
    }
}
