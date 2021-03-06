package com.evg.ss.exceptions.execution;

public final class ModuleNotFoundException extends SSExecutionException {
    public ModuleNotFoundException(String name) {
        super(String.format("Module '%s' does not exists.", name));
    }
}
