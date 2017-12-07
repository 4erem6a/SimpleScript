package com.evg.ss.lib;

import com.evg.ss.exceptions.ArgumentCountMismatchException;
import com.evg.ss.exceptions.inner.SSReturnException;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.Value;

import java.util.List;

public final class SSFunction implements Function {

    private final List<String> argNames;
    private final Statement body;

    public SSFunction(List<String> argNames, Statement body) {
        this.argNames = argNames;
        this.body = body;
    }

    public int getArgc() {
        return argNames.size();
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public Value execute(Value... args) {
        if (args.length != getArgc())
            throw new ArgumentCountMismatchException(args.length, getArgc());
        SS.Scopes.up();
        for (int i = 0; i < getArgc(); i++)
            SS.Variables.put(argNames.get(i), args[i], false);
        try {
            body.execute();
        } catch (SSReturnException e) {
            SS.Scopes.down();
            return e.getValue();
        }
        SS.Scopes.down();
        return new NullValue();
    }

    @Override
    public int hashCode() {
        return getArgc() | argNames.stream().mapToInt(String::hashCode).reduce((a, b) -> a | b).getAsInt();
    }
}