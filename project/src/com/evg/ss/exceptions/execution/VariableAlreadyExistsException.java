package com.evg.ss.exceptions.execution;

/**
 * @author 4erem6a
 */
public final class VariableAlreadyExistsException extends SSExecutionException {
    public VariableAlreadyExistsException(String name) {
        super(String.format("Variable '%s' already exists.", name));
    }
}