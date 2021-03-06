package com.evg.ss.exceptions.execution;

import com.evg.ss.lexer.SourcePosition;

public final class InvalidInterpolationException extends SSExecutionException {
    public InvalidInterpolationException(SourcePosition position, String pureString) {
        super(String.format("Can't perform interpolation to `%s` at %s.", pureString, position));
    }

    public InvalidInterpolationException(String pureString) {
        super(String.format("Can't perform interpolation to `%s`.", pureString));
    }
}