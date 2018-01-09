package com.evg.ss.exceptions.execution;

import com.evg.ss.values.Type;

public final class InvalidExportTypeException extends SSExecutionException {
    public InvalidExportTypeException(Type got) {
        super(String.format("Invalid export type: %s. Module can only export maps.", got));
    }
}