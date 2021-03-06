package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.IdentifierNotFoundException;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public class NameofExpression extends Expression {

    private String name;

    public NameofExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if (SS.Identifiers.exists(name))
            return Value.of(name);
        throw new IdentifierNotFoundException(name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ (27 * 19 * 31);
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }
}
