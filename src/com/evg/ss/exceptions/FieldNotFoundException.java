package com.evg.ss.exceptions;

public final class FieldNotFoundException extends SSExecutionException {
    public FieldNotFoundException(String name) {
        super(String.format("Field named '%s' does not exists in current object.", name));
    }
}
