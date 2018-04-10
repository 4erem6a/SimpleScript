package com.evg.ss.parser.ast;

import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

import java.util.ArrayList;
import java.util.List;

public final class UnitedStatement extends Statement {

    private List<Statement> statements = new ArrayList<>();

    public UnitedStatement(List<Statement> statements) {
        this.statements = statements;
    }

    public UnitedStatement() {
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void execute() {
        statements.forEach(Statement::execute);
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public int size() {
        return statements.size();
    }

    public Statement first() {
        if (statements.size() >= 1)
            return statements.get(0);
        return null;
    }

    @Override
    public int hashCode() {
        return statements.hashCode() ^ (42 * 4 * 31);
    }
}
