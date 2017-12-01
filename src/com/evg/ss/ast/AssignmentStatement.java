package com.evg.ss.ast;

import com.evg.ss.exceptions.VariableNotFoundException;
import com.evg.ss.lib.SS;

public final class AssignmentStatement implements Statement {

    private String name;
    private Expression value;

    public AssignmentStatement(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void execute() {
        if (SS.Variables.exists(name))
            SS.Variables.set(name, value.eval());
        else throw new VariableNotFoundException(name);
    }

    @Override
    public String toString() {
        return String.format("%s = %s\n", name, value);
    }
}
