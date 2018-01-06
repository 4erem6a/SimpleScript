package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.exceptions.inner.SSContinueException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

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
            } catch (SSBreakException ignored) {
                break;
            } catch (SSContinueException ignored) {
            }
        } while (condition.eval().asBoolean());
    }

    @Override
    public String toString() {
        return String.format("do %s while %s\n", body, condition);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Statement getBody() {
        return body;
    }

    public Expression getCondition() {
        return condition;
    }

    @Override
    public int hashCode() {
        return body.hashCode() ^ condition.hashCode() ^ (11 * 35 * 31);
    }
}