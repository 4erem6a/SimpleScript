package com.evg.ss.parser.ast;

import com.evg.ss.values.Value;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class TernaryExpression implements Expression {

    private Expression condition, onTrue, onFalse;

    public TernaryExpression(Expression condition, Expression onTrue, Expression onFalse) {
        this.condition = condition;
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    @Override
    public Value eval() {
        final boolean condition = this.condition.eval().asBoolean();
        final Value onTrue = this.onTrue.eval();
        final Value onFalse = this.onFalse.eval();
        return condition ? onTrue : onFalse;
    }

    @Override
    public String toString() {
        return String.format("[%s ? %s : %s]", condition, onTrue, onFalse);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getOnTrue() {
        return onTrue;
    }

    public Expression getOnFalse() {
        return onFalse;
    }
}