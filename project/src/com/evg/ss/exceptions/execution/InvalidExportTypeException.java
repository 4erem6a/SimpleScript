package com.evg.ss.exceptions.execution;

public final class InvalidExportTypeException extends SSExecutionException {
    public InvalidExportTypeException() {
        super("Invalid export type. Module can only export maps.");
    }
}