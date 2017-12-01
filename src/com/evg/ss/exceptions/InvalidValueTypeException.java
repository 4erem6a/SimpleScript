package com.evg.ss.exceptions;

import com.evg.ss.values.Type;

public final class InvalidValueTypeException extends SSExecutionException {
    public InvalidValueTypeException(Type got) {
        super(String.format("Invalid value type to performing operation: %s.", got));
    }
}