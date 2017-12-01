package com.evg.ss.exceptions;

/**
 * @author 4erem6a
 */
public final class StaticFieldAccessException extends SSExecutionException {
    public StaticFieldAccessException(String name) {
        super(String.format("Failed to change static field: %s.", name));
    }
}
