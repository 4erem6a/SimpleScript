package com.evg.ss.exceptions.execution;

/**
 * @author 4erem6a
 */
public final class IdentifierAlreadyExistsException extends SSExecutionException {
    public IdentifierAlreadyExistsException(String name) {
        super(String.format("Identifier '%s' already exists.", name));
    }
}
