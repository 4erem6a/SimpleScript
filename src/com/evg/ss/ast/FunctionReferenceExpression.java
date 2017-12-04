package com.evg.ss.ast;

import com.evg.ss.exceptions.FunctionNotFoundException;
import com.evg.ss.lib.SS;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.Value;

public final class FunctionReferenceExpression implements Expression {

    private String name;

    public FunctionReferenceExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if (SS.Functions.exists(name))
            return new FunctionValue(SS.Functions.get(name));
        throw new FunctionNotFoundException(name);
    }
}