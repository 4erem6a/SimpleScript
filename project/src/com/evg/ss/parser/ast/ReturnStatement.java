package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.inner.SSReturnException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class ReturnStatement implements Statement {

    private Expression expression;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        throw new SSReturnException(expression.eval());
    }

    @Override
    public String toString() {
        return String.format("return %s\n", expression);
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
        return expression.hashCode() ^ (32 * 14 * 31);
    }
}
