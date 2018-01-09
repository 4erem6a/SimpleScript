package com.evg.ss.exceptions.inner;

import com.evg.ss.exceptions.SSException;

public abstract class SSInnerException extends SSException {
    public SSInnerException(String message) {
        super(message);
    }

    public SSInnerException() {
    }

    public abstract String onInvalidUsage();
}