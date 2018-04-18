package com.evg.ss.exceptions.execution;

import com.evg.ss.lib.Operations;
import com.evg.ss.values.Types;

public final class InvalidValueTypeException extends SSExecutionException {

    public InvalidValueTypeException(Types type) {
        super(String.format("Invalid value type to performing operation: %s", type));
    }

    public InvalidValueTypeException(Types type, Operations operation) {
        super(String.format("Invalid value type to performing operation: %s -> %s", type, operation.getKey()));
    }
}