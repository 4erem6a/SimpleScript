package com.evg.ss.exceptions.execution;

public class InvalidExpressionException extends SSExecutionException {
    public InvalidExpressionException() {
        super("Invalid expression.");
    }
}