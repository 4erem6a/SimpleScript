package com.evg.ss.exceptions;

import com.evg.ss.values.Value;

public final class SSThrownException extends SSException {

    private final Value value;

    public SSThrownException(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return value;
    }
}