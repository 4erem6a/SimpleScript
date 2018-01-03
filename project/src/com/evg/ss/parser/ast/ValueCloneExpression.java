package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class ValueCloneExpression implements Expression {

    private final Expression expression;

    public ValueCloneExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value eval() {
        return expression.eval().clone();
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }
}
