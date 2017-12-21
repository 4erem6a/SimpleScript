package com.evg.ss.linter;

import com.evg.ss.exceptions.SSException;

public final class LintException extends SSException {

    public LintException(String message, Object... format) {
        super(String.format(message, format));
    }

}