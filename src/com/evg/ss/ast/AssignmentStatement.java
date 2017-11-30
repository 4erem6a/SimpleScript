package com.evg.ss.ast;

import com.evg.ss.containers.Variables;
import com.evg.ss.exceptions.VariableNotFoundException;

public final class AssignmentStatement implements Statement {

    private String name;
    private Expression value;

    public AssignmentStatement(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void execute() {
        if (Variables.exists(name))
            Variables.set(name, value.eval());
        else throw new VariableNotFoundException(name);
    }

    @Override
    public String toString() {
        return String.format("%s = %s\n", name, value);
    }
}
