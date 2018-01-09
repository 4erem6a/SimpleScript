package com.evg.ss.exceptions;

public final class SSLintException extends SSException {

    public SSLintException(String message, Object... format) {
        super(String.format(message, format));
    }

}