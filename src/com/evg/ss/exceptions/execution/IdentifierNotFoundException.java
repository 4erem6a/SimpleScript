package com.evg.ss.exceptions.execution;

public final class IdentifierNotFoundException extends SSExecutionException {
    public IdentifierNotFoundException(String name) {
        super(String.format("Identifier '%s' does not exists.", name));
    }
}
