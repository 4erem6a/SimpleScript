package com.evg.ss.exceptions.inner;

public final class SSBreakException extends SSInnerException {
    @Override
    public String onInvalidUsage() {
        return "Break statement must be in the body of the cycle.";
    }
}