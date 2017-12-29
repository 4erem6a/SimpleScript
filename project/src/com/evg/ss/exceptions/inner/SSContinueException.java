package com.evg.ss.exceptions.inner;

public final class SSContinueException extends SSInnerException {
    @Override
    public String onInvalidUsage() {
        return "Continue statement must be in the body of the cycle.";
    }
}