package com.evg.ss.ast;

import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.exceptions.inner.SSContinueException;
import com.evg.ss.exceptions.inner.SSInnerException;

public final class DoWhileStatement implements Statement {

    private Statement body;
    private Expression condition;

    public DoWhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute() {
        do {
            try {
                body.execute();
            } catch (SSInnerException e) {
                if (e instanceof SSBreakException)
                    break;
                if (e instanceof SSContinueException)
                    continue;
                throw e;
            }
        } while (condition.eval().asBoolean());
    }

    @Override
    public String toString() {
        return String.format("do %s while %s\n", body, condition);
    }
}