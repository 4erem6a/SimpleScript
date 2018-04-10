package com.evg.ss.exceptions.execution;

import com.evg.ss.parser.ast.Expression;

public final class InvalidAssignmentTargetException extends SSExecutionException {
    public InvalidAssignmentTargetException(Expression expression) {
        super(String.format("Invalid assignment target: %s", expression));
    }
}
