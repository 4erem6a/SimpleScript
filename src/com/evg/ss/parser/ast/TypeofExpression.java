package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class TypeofExpression implements Expression {

    private Expression expression;

    public TypeofExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value eval() {
        return expression.eval().getType().getTypeValue();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public int hashCode() {
        return expression.hashCode() ^ (40 * 6 * 31);
    }
}