package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.SSThrownException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class ThrowStatement extends Statement {

    private final Expression expression;

    public ThrowStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        throw new SSThrownException(expression.eval());
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

    @Override
    public int hashCode() {
        return expression.hashCode() ^ (37 * 9 * 31);
    }
}