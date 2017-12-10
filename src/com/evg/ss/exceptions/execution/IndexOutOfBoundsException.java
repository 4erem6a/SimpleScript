package com.evg.ss.exceptions.execution;

public final class IndexOutOfBoundsException extends SSExecutionException {
    public IndexOutOfBoundsException(int index) {
        super(String.format("Error accessing array by index [%d]", index));
    }
}