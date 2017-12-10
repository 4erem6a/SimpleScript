package com.evg.ss.exceptions.execution;

import com.evg.ss.values.Value;

public final class FieldNotFoundException extends SSExecutionException {
    public FieldNotFoundException(Value key) {
        super(String.format("Field '%s' does not exists in current object.", key));
    }
}
