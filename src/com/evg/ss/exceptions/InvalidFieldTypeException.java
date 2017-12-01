package com.evg.ss.exceptions;

import com.evg.ss.values.Type;

public final class InvalidFieldTypeException extends SSExecutionException {
    public InvalidFieldTypeException(Type got) {
        super(String.format("Invalid field type to performing operation: %s.", got));
    }
}