package com.evg.ss.exceptions.inner;

import com.evg.ss.values.Value;

public final class SSExportsException extends SSInnerException {
    private final Value value;

    public SSExportsException(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String onInvalidUsage() {
        return "Exports statement must be in the external module.";
    }
}