package com.evg.ss.exceptions.execution;

public final class ModuleLoadingException extends SSExecutionException {
    private Exception innerException = null;

    public ModuleLoadingException(String name) {
        super(String.format("Unable to load module '%s'.", name));
    }

    public ModuleLoadingException(String name, Exception innerException) {
        this(name);
        this.innerException = innerException;
    }

    public Exception getInnerException() {
        return innerException;
    }
}