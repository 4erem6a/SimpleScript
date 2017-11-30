package com.evg.ss.ast;

import com.evg.ss.containers.Functions;
import com.evg.ss.exceptions.FunctionNotFoundException;
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
        if (!Functions.exists(name))
            throw new FunctionNotFoundException(name);
        Value[] args = new Value[this.args.length];
        for (int i = 0; i < this.args.length; i++)
            args[i] = this.args[i].eval();
        return Functions.get(name).execute(args);
    }

    @Override
    public String toString() {
        return String.format("call %s [%s]", name, Arrays.toString(args));
    }
}