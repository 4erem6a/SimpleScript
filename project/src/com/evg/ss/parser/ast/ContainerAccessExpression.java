package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Container;
import com.evg.ss.values.Value;

public final class ContainerAccessExpression implements Expression, Accessible {

    private Expression target;
    private Expression key;

    public ContainerAccessExpression(Expression target, Expression key) {
        this.target = target;
        this.key = key;
    }

    @Override
    public Value eval() {
        final Value target = this.target.eval();
        if (target instanceof Container)
            return ((Container) target).get(key.eval());
        throw new InvalidValueTypeException(target.getType());
    }

    @Override
    public Value get() {
        return eval();
    }

    @Override
    public Value set(Value value) {
        final Value target = this.target.eval();
        if (target instanceof Container)
            ((Container) target).set(key.eval(), value);
        else throw new InvalidValueTypeException(target.getType());
        return eval();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Expression getTarget() {
        return target;
    }

    public Expression getKey() {
        return key;
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", target, key);
    }

    @Override
    public int hashCode() {
        return target.hashCode() ^ key.hashCode() ^ (9 * 37 * 31);
    }
}