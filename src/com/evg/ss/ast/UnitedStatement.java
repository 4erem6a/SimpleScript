package com.evg.ss.ast;

import java.util.ArrayList;
import java.util.List;

public final class UnitedStatement implements Statement {

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
}
