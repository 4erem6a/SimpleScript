package com.evg.ss.exceptions;

import com.evg.ss.lexer.Position;

public final class InvalidInterpolationException extends SSExecutionException {
    public InvalidInterpolationException(Position position, String pureString) {
        super(String.format("Can't perform interpolation to `%s` at %s", pureString, position));
    }
}