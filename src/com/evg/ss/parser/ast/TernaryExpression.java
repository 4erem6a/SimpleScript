package com.evg.ss.parser.ast;

import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class TernaryExpression extends Expression {

    private Expression condition, onTrue, onFalse;

    public TernaryExpression(Expression condition, Expression onTrue, Expression onFalse) {
        this.condition = condition;
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    @Override
    public Value eval() {
        final boolean condition = this.condition.eval().asBoolean();
        return condition ? onTrue.eval() : onFalse.eval();
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
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

    @Override
    public int hashCode() {
        return condition.hashCode() ^ onTrue.hashCode() ^ onFalse.hashCode() ^ (35 * 11 * 31);
    }
}