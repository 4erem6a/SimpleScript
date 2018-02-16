package com.evg.ss.exceptions.execution;

public class ModifierAlreadySetException extends SSExecutionException {
    public ModifierAlreadySetException(String name) {
        super(String.format("Modifier '%s' is already set.", name));
    }
}