package com.evg.ss.ast;

import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.exceptions.inner.SSContinueException;
import com.evg.ss.exceptions.inner.SSInnerException;
import com.evg.ss.lib.SS;

public final class ForStatement implements Statement {

    private Statement initialization, iteration, body;
    private Expression condition;

    public ForStatement(Statement initialization, Expression condition, Statement iteration, Statement body) {
        this.initialization = initialization;
        this.condition = condition;
        this.iteration = iteration;
        this.body = body;
    }

    @Override
    public void execute() {
        SS.Scopes.up();
        if (initialization != null)
            initialization.execute();
        while (condition != null ? condition.eval().asBoolean() : true) {
            try {
                body.execute();
            } catch (SSInnerException e) {
                if (e instanceof SSBreakException)
                    break;
                if (e instanceof SSContinueException) {
                    if (iteration != null)
                        iteration.execute();
                    continue;
                }
                throw e;
            }
            if (iteration != null)
                iteration.execute();
        }
        SS.Scopes.down();
    }

    @Override
    public String toString() {
        return String.format("for [%s; %s; %s] %s\n", initialization, condition, iteration, body);
    }
}