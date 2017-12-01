package com.evg.ss.ast;

import com.evg.ss.containers.Variables;
import com.evg.ss.exceptions.VariableAlreadyExistsException;
import com.evg.ss.values.NullValue;

public final class LetStatement implements Statement {
    private final String name;
    private final Expression value;
    private final boolean isConst;

    public String getName() {
        return name;
    }

    public LetStatement(String name, Expression value, boolean isConst) {
        this.name = name;
        this.value = value;
        this.isConst = isConst;
    }

    public LetStatement(String name) {
        this(name, NullValue.NullExpression, false);
    }

    @Override
    public void execute() {
        if (!Variables.existsTop(name))
            Variables.put(name, value.eval(), isConst);
        else throw new VariableAlreadyExistsException(name);
    }

    @Override
    public String toString() {
        return String.format("let %s%s = %s\n", isConst ? "const " : "", name, value);
    }
}