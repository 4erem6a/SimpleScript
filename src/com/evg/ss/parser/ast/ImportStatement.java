package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidExpressionException;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

public final class ImportStatement extends Statement {

    private Expression path;

    public ImportStatement(Expression path) {
        this.path = path;
    }

    @Override
    public void execute() {
        if (!(path instanceof ContainerAccessExpression))
            throw new InvalidExpressionException();
        final ContainerAccessExpression path = ((ContainerAccessExpression) this.path);
        final Value key = path.getKey().eval();
        if (!(key instanceof StringValue))
            throw new InvalidExpressionException();
        SS.Identifiers.put(key.asString(), path.eval(), true);
    }

    public Expression getPath() {
        return path;
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
        return path.hashCode() ^ (21 * 25 * 31);
    }
}