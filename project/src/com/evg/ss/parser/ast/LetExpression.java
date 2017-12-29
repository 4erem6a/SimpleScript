package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class LetExpression implements Expression {

    private String name;
    private Expression value;

    public LetExpression(String name) {
        this.name = name;
    }

    public LetExpression(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Value eval() {
        new LetStatement(name, value, false).execute();
        return new VariableExpression(name).eval();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("let %s = %s", name, value);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Expression getValue() {
        return value;
    }
}