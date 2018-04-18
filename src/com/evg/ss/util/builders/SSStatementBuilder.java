package com.evg.ss.util.builders;

import com.evg.ss.parser.ast.BlockStatement;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.parser.ast.UnitedStatement;

import java.util.ArrayList;
import java.util.List;

public final class SSStatementBuilder {

    private final List<Statement> statements = new ArrayList<>();

    public SSStatementBuilder set(Statement statement) {
        this.statements.add(statement);
        return this;
    }

    public BlockStatement toBlock() {
        return new BlockStatement(this.statements);
    }

    public UnitedStatement toUnited() {
        return new UnitedStatement(this.statements);
    }

}