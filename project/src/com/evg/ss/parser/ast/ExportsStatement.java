package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidExportTypeException;
import com.evg.ss.exceptions.inner.SSExportsException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

public final class ExportsStatement implements Statement {
    private Expression expression;

    public ExportsStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        final Value exports = expression.eval();
        if (!(exports instanceof MapValue))
            throw new InvalidExportTypeException();
        throw new SSExportsException(exports);
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
        return expression.hashCode() ^ (12 * 34 * 31);
    }
}