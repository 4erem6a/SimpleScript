package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

import java.util.ArrayList;
import java.util.List;

public final class UnitedStatement extends Statement {

    private List<Statement> statements = new ArrayList<>();

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void execute() {
        statements.forEach(Statement::execute);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("union [\n");
        for (Statement statement : statements)
            builder.append(statement);
        builder.append("]");
        return builder.toString();
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

    @Override
    public int hashCode() {
        return statements.hashCode() ^ (42 * 4 * 31);
    }
}
