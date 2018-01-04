package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class IfStatement implements Statement {
    private Expression condition;
    private Statement ifStatement, elseStatement;

    public IfStatement(Expression condition, Statement ifStatement, Statement elseStatement) {
        this.condition = condition;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void execute() {
        if (condition.eval().asBoolean())
            ifStatement.execute();
        else if (elseStatement != null)
            elseStatement.execute();
    }

    @Override
    public String toString() {
        return String.format("if [%s] %s else %s\n", condition, ifStatement, elseStatement);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getIfStatement() {
        return ifStatement;
    }

    public Statement getElseStatement() {
        return elseStatement;
    }

    @Override
    public int hashCode() {
        return condition.hashCode()
                ^ ifStatement.hashCode()
                ^ (elseStatement == null ? 1 : elseStatement.hashCode())
                ^ (20 * 26 * 31);
    }
}