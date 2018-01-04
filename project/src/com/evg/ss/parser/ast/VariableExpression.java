package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.ConstantAccessException;
import com.evg.ss.exceptions.execution.VariableNotFoundException;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

/**
 * @author 4erem6a
 */
public final class VariableExpression implements Expression, Accessible {

    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Value eval() {
        if (SS.Variables.getValue(name) != null)
            return SS.Variables.getValue(name);
        else throw new VariableNotFoundException(name);
    }

    @Override
    public Value get() {
        if (!SS.Variables.exists(name))
            throw new VariableNotFoundException(name);
        return SS.Variables.getValue(name);
    }

    @Override
    public Value set(Value value) {
        if (!SS.Variables.exists(name))
            throw new VariableNotFoundException(name);
        if (SS.Variables.get(name).isConst())
            throw new ConstantAccessException(name);
        SS.Variables.set(name, value);
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