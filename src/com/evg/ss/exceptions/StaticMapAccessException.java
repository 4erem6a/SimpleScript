package com.evg.ss.exceptions;

/**
 * @author 4erem6a
 */
public final class StaticMapAccessException extends SSExecutionException {
    public StaticMapAccessException() {
        super("Failed to change static map.");
    }
}
