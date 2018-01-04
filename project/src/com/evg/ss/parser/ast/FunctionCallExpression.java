package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.ConstructorFunction;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class FunctionCallExpression implements Expression {

    private Expression function;
    private Expression[] args;
    private boolean isNew = false;

    public FunctionCallExpression(Expression function, Expression... args) {
        this.function = function;
        this.args = args;
    }

    @Override
    public Value eval() {
        final Value[] args = Arrays.stream(this.args).map(Expression::eval).toArray(Value[]::new);
        final Value functionValue;
        if (function instanceof VariableExpression) {
            final String name = ((VariableExpression) function).getName();
            if (SS.Functions.exists(name))
                functionValue = new FunctionValue(SS.Functions.get(name));
            else functionValue = function.eval();
        } else {
            functionValue = function.eval();
            if (function.eval() == null)
                System.out.println(function.getClass());
        }
        if (!(functionValue instanceof FunctionValue))
            throw new InvalidValueTypeException(functionValue.getType());
        final Function function = ((FunctionValue) functionValue).getValue();
        if (isNew)
            return function instanceof ConstructorFunction
                    ? ((ConstructorFunction) function).executeAsNew(args)
                    : new MapValue();
        return function.execute(args);
    }

    public FunctionCallExpression setNew() {
        this.isNew = true;
        return this;
    }

    @Override
    public String toString() {
        return String.format("call %s [%s]", function, Arrays.toString(args));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Expression getFunction() {
        return function;
    }

    public Expression[] getArgs() {
        return args;
    }

    @Override
    public int hashCode() {
        return function.hashCode()
                ^ Arrays.hashCode(args)
                ^ Boolean.hashCode(isNew)
                ^ (17 * 29 * 31);
    }
}