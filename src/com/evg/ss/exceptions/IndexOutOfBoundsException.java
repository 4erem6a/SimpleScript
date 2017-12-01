package com.evg.ss.exceptions;

public final class IndexOutOfBoundsException extends SSExecutionException {
    public IndexOutOfBoundsException(String name, int index) {
        super(String.format("Error accessing array %s by index [%d]", name, index));
    }
}