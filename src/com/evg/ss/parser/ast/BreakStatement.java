package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class BreakStatement extends Statement {
    @Override
    public void execute() {
        throw new SSBreakException();
    }

    @Override
    public String toString() {
        return "break\n";
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
        return (7 * 39 * 31);
    }
}
