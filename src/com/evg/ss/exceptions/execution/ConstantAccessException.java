package com.evg.ss.exceptions.execution;

/**
 * @author 4erem6a
 */
public final class ConstantAccessException extends SSExecutionException {
    public ConstantAccessException(String name) {
        super(String.format("Failed to change constant '%s'.", name));
    }
}
