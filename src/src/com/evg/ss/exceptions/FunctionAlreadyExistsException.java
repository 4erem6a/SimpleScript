package com.evg.ss.exceptions;

/**
 * @author 4erem6a
 */
public final class FunctionAlreadyExistsException extends SSExecutionException {
    public FunctionAlreadyExistsException(String name) {
        super(String.format("Function '%s' already exists.", name));
    }
}