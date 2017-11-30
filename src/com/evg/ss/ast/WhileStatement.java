package com.evg.ss.ast;

import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.exceptions.inner.SSContinueException;
import com.evg.ss.exceptions.inner.SSInnerException;

public final class WhileStatement implements Statement {

    private Statement body;
    private Expression condition;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute() {
        while (condition.eval().asBoolean()) {
            try {
                body.execute();
            } catch (SSInnerException e) {
                if (e instanceof SSBreakException)
                    break;
                if (e instanceof SSContinueException)
                    continue;
                throw e;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("while [%s] %s\n", condition, body);
    }
}