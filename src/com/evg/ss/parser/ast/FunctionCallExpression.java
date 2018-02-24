package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class FunctionCallExpression extends Expression {

    private Expression value;
    private Expression[] args;
    private boolean isNew = false;
    private boolean lastArray = false;

    public FunctionCallExpression(Expression expression, boolean lastArray, Expression... args) {
        this.value = expression;
        this.lastArray = lastArray;
        this.args = args;
    }

    @Override
    public Value eval() {
        final Value value = this.value.eval();
        final Value[] args;
        if (lastArray) {
            final List<Value> _args;
            if (this.args.length > 1)
                _args = Arrays.stream(this.args)
                        .limit(this.args.length - 1)
                        .map(Expression::eval)
                        .collect(Collectors.toList());
            else _args = new ArrayList<>();
            if (this.args.length > 0) {
                final Value last = Arrays.stream(this.args)
                        .skip(this.args.length - 1)
                        .findFirst().get().eval();
                if (last.getType() != Type.Array)
                    _args.add(last);
                else _args.addAll(Arrays.asList(((ArrayValue) last).getValue()));
            }
            args = _args.toArray(new Value[0]);
        } else args = Arrays.stream(this.args).map(Expression::eval).toArray(Value[]::new);
        if (isNew) {
            if (value instanceof NewCallable)
                return ((NewCallable) value)._new(args);
        } else {
            if (value instanceof Callable)
                return ((Callable) value).call(args);
        }
        throw new InvalidValueTypeException(value.getType());
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isLastArray() {
        return lastArray;
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