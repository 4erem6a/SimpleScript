package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.ConstantAccessException;
import com.evg.ss.exceptions.execution.IdentifierNotFoundException;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

/**
 * @author 4erem6a
 */
public final class VariableExpression extends Expression implements Accessible {

    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Value eval() {
        if (SS.Identifiers.getValue(name) != null)
            return SS.Identifiers.getValue(name);
        else throw new IdentifierNotFoundException(name);
    }

    @Override
    public Value get() {
        if (!SS.Identifiers.exists(name))
            throw new IdentifierNotFoundException(name);
        return SS.Identifiers.getValue(name);
    }

    @Override
    public Value set(Value value) {
        if (!SS.Identifiers.exists(name))
            throw new IdentifierNotFoundException(name);
        if (SS.Identifiers.get(name).isConst())
            throw new ConstantAccessException(name);
        SS.Identifiers.set(name, value);
        return value;
    }

    @Override
    public String toString() {
        return String.format("[value_of %s]", name);
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
        return name.hashCode() ^ (45 * 31);
    }
}