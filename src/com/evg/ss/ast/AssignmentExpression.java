package com.evg.ss.ast;

import com.evg.ss.exceptions.InvalidAssignmentTargetException;
import com.evg.ss.values.Value;

public final class AssignmentExpression implements Expression, Accessible {

    private Expression target, value;

    public AssignmentExpression(Expression target, Expression value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public Value eval() {
        return set(value.eval());
    }

    @Override
    public Value get() {
        return target.eval();
    }

    @Override
    public Value set(Value value) {
        if (!(target instanceof Accessible))
            throw new InvalidAssignmentTargetException();
        return ((Accessible) target).set(value);
    }
}