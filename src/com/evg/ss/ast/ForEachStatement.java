package com.evg.ss.ast;

import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.exceptions.inner.SSContinueException;
import com.evg.ss.exceptions.inner.SSInnerException;
import com.evg.ss.lib.SS;
import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

public final class ForEachStatement implements Statement {

    private Statement iteratorDefinition;
    private Expression target;
    private Statement body;

    public ForEachStatement(Statement iteratorDefinition, Expression target, Statement body) {
        this.iteratorDefinition = iteratorDefinition;
        this.target = target;
        this.body = body;
    }

    @Override
    public void execute() {
        SS.Scopes.up();
        iteratorDefinition.execute();
        final String name = ((LetStatement) iteratorDefinition).getName();
        final Value value = target.eval();
        final ArrayValue array;
        if (value instanceof StringValue)
            array = ((StringValue) value).asCharArray();
        else if (value instanceof ArrayValue)
            array = (ArrayValue) value;
        else throw new InvalidValueTypeException(value.getType());
        for (Value iteration : array.getValue()) {
            try {
                SS.Variables.set(name, iteration);
                body.execute();
            } catch (SSInnerException e) {
                if (e instanceof SSBreakException)
                    break;
                if (e instanceof SSContinueException) {
                    continue;
                }
                throw e;
            }
        }
        SS.Scopes.down();
    }

    @Override
    public String toString() {
        return String.format("foreach [%s:%s] %s\n", iteratorDefinition, target, body);
    }
}