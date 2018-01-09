package com.evg.ss.exceptions.inner;

import com.evg.ss.values.Value;

public final class SSReturnException extends SSInnerException {
    private final Value value;

    public SSReturnException(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String onInvalidUsage() {
        return "Return statement must be in the body of the function.";
    }
}