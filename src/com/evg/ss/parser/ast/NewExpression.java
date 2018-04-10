package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidExpressionException;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.ClassValue;
import com.evg.ss.values.Types;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class NewExpression extends Expression {

    private Expression functionCall;

    public NewExpression(Expression functionCall) {
        this.functionCall = functionCall;
    }

    @Override
    public Value eval() {
        if (!(functionCall instanceof CallExpression))
            throw new InvalidExpressionException();
        final Expression expression = ((CallExpression) functionCall).getValue();
        if (expression instanceof VariableExpression || expression instanceof ContainerAccessExpression) {
            final Value value = expression.eval();
            if (value.getType() == Types.Class) {
                final Value[] args = Arrays
                        .stream(((CallExpression) functionCall).getArgs())
                        .map(Expression::eval).toArray(Value[]::new);
                return ((ClassValue) value).construct(args);
            }
        }
        return ((CallExpression) functionCall).setNew().eval();
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

    @Override
    public int hashCode() {
        return functionCall.hashCode() ^ (28 * 18 * 31);
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }
}
