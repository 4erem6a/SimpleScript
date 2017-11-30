package com.evg.ss.ast;

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
}