package com.evg.ss.ast;

import com.evg.ss.lexer.Scopes;

import java.util.ArrayList;
import java.util.List;

public final class BlockStatement implements Statement {

    private List<Statement> statements = new ArrayList<>();

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void execute() {
        Scopes.up();
        statements.forEach(Statement::execute);
        Scopes.down();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("block [\n");
        for (Statement statement : statements)
            builder.append(statement);
        builder.append("]");
        return builder.toString();
    }
}