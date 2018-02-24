package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.SSThrownException;
import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.Type;
import com.evg.ss.values.UndefinedValue;
import com.evg.ss.values.Value;

public final class CatchExpression extends Expression {

    private final Expression expression;
    private final Expression handler;

    public CatchExpression(Expression expression, Expression handler) {
        this.expression = expression;
        this.handler = handler;
    }

    @Override
    public Value eval() {
        final Value handler = this.handler == null ? null : this.handler.eval();
        if (handler != null && handler.getType() != Type.Function)
            throw new InvalidValueTypeException(handler.getType());
        try {
            return expression.eval();
        } catch (SSThrownException e) {
            return handler == null
                    ? new UndefinedValue()
                    : ((FunctionValue) handler).call(e.getValue());
        }
    }

    public Expression getExpression() {
        return expression;
    }

    public Expression getHandler() {
        return handler;
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