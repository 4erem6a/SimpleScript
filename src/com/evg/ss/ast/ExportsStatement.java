package com.evg.ss.ast;

import com.evg.ss.exceptions.InvalidExportTypeException;
import com.evg.ss.exceptions.inner.SSExportsException;
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
}