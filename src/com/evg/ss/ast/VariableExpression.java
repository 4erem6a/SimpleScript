package com.evg.ss.ast;

import com.evg.ss.containers.Variables;
import com.evg.ss.exceptions.VariableNotFoundException;
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
        if (Variables.getValue(name) != null)
            return Variables.getValue(name);
        else throw new VariableNotFoundException(name);
    }

    @Override
    public String toString() {
        return String.format("[value_of %s]", name);
    }
}