package com.evg.ss.exceptions.execution;

/**
 * @author 4erem6a
 */
public final class FunctionAlreadyExistsException extends SSExecutionException {
    public FunctionAlreadyExistsException(String name) {
        super(String.format("Function '%s' is already exists.", name));
    }
}