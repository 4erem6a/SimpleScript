package com.evg.ss.ast;

import com.evg.ss.containers.Variables;
import com.evg.ss.exceptions.ArgumentCountMismatchException;
import com.evg.ss.exceptions.inner.SSReturnException;
import com.evg.ss.lexer.Scopes;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.Value;

public final class AnonymousFunctionExpression implements Expression {

    private String[] argNames;
    private Statement body;

    public AnonymousFunctionExpression(String[] argNames, Statement body) {
        this.argNames = argNames;
        this.body = body;
    }

    @Override
    public Value eval() {
        return new FunctionValue(this::execute);
    }

    private Value execute(Value... args) {
        Scopes.up();
        if (args.length != argNames.length)
            throw new ArgumentCountMismatchException("", args.length);
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