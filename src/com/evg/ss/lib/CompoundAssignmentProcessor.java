package com.evg.ss.lib;

import com.evg.ss.parser.ast.AssignmentExpression;
import com.evg.ss.parser.ast.BinaryExpression;
import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.UnaryExpression;

public final class CompoundAssignmentProcessor {

    private final Operations operation;
    private final Expression target;
    private final Expression value;

    public CompoundAssignmentProcessor(Operations operation, Expression target, Expression value) {
        this.operation = operation;
        this.target = target;
        this.value = value;
    }

    public AssignmentExpression processBinary() {
        return new AssignmentExpression(target, new BinaryExpression(operation, target, value));
    }

    public AssignmentExpression processUnary() {
        return new AssignmentExpression(target, new UnaryExpression(operation, value));
    }
}