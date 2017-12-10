package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.VariableNotFoundException;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public class NameofExpression implements Expression {

    private String name;

    public NameofExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if (SS.Variables.exists(name))
            return Value.of(name);
        throw new VariableNotFoundException(name);
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
