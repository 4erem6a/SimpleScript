package com.evg.ss.ast;

import com.evg.ss.containers.Functions;
import com.evg.ss.containers.Variables;
import com.evg.ss.exceptions.ArgumentCountMismatchException;
import com.evg.ss.exceptions.inner.SSReturnException;
import com.evg.ss.lexer.Scopes;
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
        Functions.put(name, this::execute);
    }

    private Value execute(Value... args) {
        Scopes.up();
        if (args.length != argNames.length)
            throw new ArgumentCountMismatchException(name, args.length);
        for (int i = 0; i < argNames.length; i++)
            Variables.put(argNames[i], args[i], false);
        try {
            body.execute();
        } catch (SSReturnException e) {
            Scopes.down();
            return e.getValue();
        }
        Scopes.down();
        return new NullValue();
    }
}