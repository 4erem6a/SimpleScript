package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.exceptions.inner.SSContinueException;
import com.evg.ss.exceptions.inner.SSInnerException;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

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
        try {
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
        } finally {
            SS.Scopes.down();
        }
    }

    @Override
    public String toString() {
        return String.format("for [%s; %s; %s] %s\n", initialization, condition, iteration, body);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Statement getInitialization() {
        return initialization;
    }

    public Statement getIteration() {
        return iteration;
    }

    public Statement getBody() {
        return body;
    }

    public Expression getCondition() {
        return condition;
    }

    @Override
    public int hashCode() {
        return (initialization == null ? 1 : initialization.hashCode())
                ^ (condition == null ? 1 : condition.hashCode())
                ^ (iteration == null ? 1 : iteration.hashCode())
                ^ (16 * 30 * 31);
    }
}