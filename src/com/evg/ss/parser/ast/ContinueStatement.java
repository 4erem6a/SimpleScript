package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.inner.SSContinueException;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class ContinueStatement extends Statement {
    @Override
    public void execute() {
        throw new SSContinueException();
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

    @Override
    public int hashCode() {
        return (10 * 36 * 31);
    }
}
