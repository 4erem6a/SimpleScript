package com.evg.ss.exceptions.execution;

public final class ModuleLoadingException extends SSExecutionException {
    private Exception innerException = null;

    public ModuleLoadingException(String name) {
        super(String.format("Unable to load module '%s'.", name));
    }

    public ModuleLoadingException(String name, Exception innerException) {
        super(String.format("Unable to load module '%s':\n\t\t%s\n\t\t%s", name,
                innerException.getClass().getSimpleName(),
                innerException.getMessage()));
        this.innerException = innerException;
    }

    public Exception getInnerException() {
        return innerException;
    }
}