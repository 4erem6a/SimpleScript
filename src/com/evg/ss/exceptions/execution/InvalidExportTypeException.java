package com.evg.ss.exceptions.execution;

import com.evg.ss.values.Types;

public final class InvalidExportTypeException extends SSExecutionException {
    public InvalidExportTypeException(Types got) {
        super(String.format("Invalid export type: %s. Module can only export MapsModule.", got));
    }
}