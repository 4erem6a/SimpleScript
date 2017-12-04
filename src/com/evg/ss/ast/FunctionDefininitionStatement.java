package com.evg.ss.ast;

import com.evg.ss.exceptions.ArgumentCountMismatchException;
import com.evg.ss.exceptions.inner.SSReturnException;
import com.evg.ss.lib.SS;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.Value;

public final class FunctionDefininitionStatement implements Statement {

    private String name;
    private String[] argNames;
    private Statement body;

    public FunctionDefininitionStatement(String name, String[] argNames, Statement body) {
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }

    @Override
    public void execute() {
        SS.Functions.put(name, this::execute);
    }

    private Value execute(Value... args) {
        SS.Scopes.up();
        if (args.length != argNames.length)
            throw new ArgumentCountMismatchException(args.length);
        for (int i = 0; i < argNames.length; i++)
            SS.Variables.put(argNames[i], args[i], false);
        try {
            body.execute();
        } catch (SSReturnException e) {
            SS.Scopes.down();
            return e.getValue();
        }
        SS.Scopes.down();
        return new NullValue();
    }
}