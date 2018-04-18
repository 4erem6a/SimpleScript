package com.evg.ss.exceptions.execution;

import com.evg.ss.parser.ast.Expression;

public class SelfReferencedAssignmentException extends SSExecutionException {

    public SelfReferencedAssignmentException(Expression expression) {
        super("Unable to assign self reference: " + expression.toString());
    }
}