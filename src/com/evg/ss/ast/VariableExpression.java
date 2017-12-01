package com.evg.ss.ast;

import com.evg.ss.exceptions.VariableNotFoundException;
import com.evg.ss.lib.SS;
import com.evg.ss.values.Value;

/**
 * @author 4erem6a
 */
public final class VariableExpression implements Expression {

    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if (SS.Variables.getValue(name) != null)
            return SS.Variables.getValue(name);
        else throw new VariableNotFoundException(name);
    }

    @Override
    public String toString() {
        return String.format("[value_of %s]", name);
    }
}