package com.evg.ss.exceptions.execution;

import com.evg.ss.exceptions.SSException;
import com.evg.ss.values.Value;

public class InvalidValueException extends SSException {
    public InvalidValueException(Value value) {
        super(String.format("Invalid value: %s.", value));
    }
}