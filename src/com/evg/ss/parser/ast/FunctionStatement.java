package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class FunctionStatement implements Statement {

    private FunctionCallExpression expression;

    public FunctionStatement(FunctionCallExpression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        expression.eval();
    }

    @Override
    public String toString() {
        return expression.toString() + "\n";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public FunctionCallExpression getExpression() {
        return expression;
    }
}