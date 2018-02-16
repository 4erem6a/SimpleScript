package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidCallContextException;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;


public final class ThisExpression extends Expression {

    @Override
    public Value eval() {
        final MapValue context = SS.CallContext.get();
        if (context == null)
            throw new InvalidCallContextException();
        return context;
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
        return (36 * 10 * 31);
    }
}
