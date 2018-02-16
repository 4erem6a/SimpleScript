package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Callable;
import com.evg.ss.values.NewCallable;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class FunctionCallExpression extends Expression {

    private Expression value;
    private Expression[] args;
    private boolean isNew = false;

    public FunctionCallExpression(Expression expression, Expression... args) {
        this.value = expression;
        this.args = args;
    }

    @Override
    public Value eval() {
        final Value[] args = Arrays.stream(this.args).map(Expression::eval).toArray(Value[]::new);
        final Value value = this.value.eval();
        if (isNew) {
            if (value instanceof NewCallable)
                return ((NewCallable) value)._new(args);
        } else {
            if (value instanceof Callable)
                return ((Callable) value).call(args);
        }
        throw new InvalidValueTypeException(value.getType());
    }

    public FunctionCallExpression setNew() {
        this.isNew = true;
        return this;
    }

    @Override
    public String toString() {
        return String.format("call %s [%s]", value, Arrays.toString(args));
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

    public Expression[] getArgs() {
        return args;
    }

    @Override
    public int hashCode() {
        return value.hashCode()
                ^ Arrays.hashCode(args)
                ^ Boolean.hashCode(isNew)
                ^ (17 * 29 * 31);
    }
}