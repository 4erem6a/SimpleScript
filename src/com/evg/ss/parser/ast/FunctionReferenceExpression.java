package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.FunctionNotFoundException;
import com.evg.ss.lib.SS;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.Value;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class FunctionReferenceExpression implements Expression {

    private String name;

    public FunctionReferenceExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if (SS.Functions.exists(name))
            return new FunctionValue(SS.Functions.get(name));
        throw new FunctionNotFoundException(name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }
}