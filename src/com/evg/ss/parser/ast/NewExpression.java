package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidExpressionException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class NewExpression implements Expression {

    private Expression functionCall;

    public NewExpression(Expression functionCall) {
        this.functionCall = functionCall;
    }

    @Override
    public Value eval() {
        if (!(functionCall instanceof FunctionCallExpression))
            throw new InvalidExpressionException();
        return ((FunctionCallExpression) functionCall).setNew().eval();
    }

    public Expression getFunctionCall() {
        return functionCall;
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
