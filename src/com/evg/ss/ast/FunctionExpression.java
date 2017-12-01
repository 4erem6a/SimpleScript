package com.evg.ss.ast;

import com.evg.ss.containers.Functions;
import com.evg.ss.containers.Variables;
import com.evg.ss.exceptions.FunctionNotFoundException;
import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class FunctionExpression implements Expression {

    private String name;
    private Expression[] args;

    public FunctionExpression(String name, Expression... args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public Value eval() {
        Value[] args = new Value[this.args.length];
        for (int i = 0; i < this.args.length; i++)
            args[i] = this.args[i].eval();
        if (!Functions.exists(name)) {
            if (!Variables.exists(name))
                throw new FunctionNotFoundException(name);
            final Value value = Variables.getValue(name);
            if (!(value instanceof FunctionValue))
                throw new InvalidValueTypeException(value.getType());
            final FunctionValue functionValue = (FunctionValue) value;
            return  functionValue.getValue().execute(args);
        }
        return Functions.get(name).execute(args);
    }

    @Override
    public String toString() {
        return String.format("call %s [%s]", name, Arrays.toString(args));
    }
}