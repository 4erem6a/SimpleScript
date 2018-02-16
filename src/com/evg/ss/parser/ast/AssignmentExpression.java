package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidAssignmentTargetException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class AssignmentExpression extends Expression implements Accessible {

    private Expression target, value;

    public AssignmentExpression(Expression target, Expression value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public Value eval() {
        return set(value instanceof Accessible ? ((Accessible) value).get() : value.eval());
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

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Expression getTarget() {
        return target;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return target.hashCode() ^ value.hashCode() ^ (4 * 42 * 31);
    }
}