package com.evg.ss.parser.ast;

import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.FunctionAdder;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

import java.util.ArrayList;
import java.util.List;

public final class BlockStatement implements Statement, Lockable {

    private List<Statement> statements = new ArrayList<>();
    private boolean locked;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    public BlockStatement() {
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void execute() {
        this.accept(new FunctionAdder());
        if (locked) {
            SS.Scopes scopes = SS.Scopes.lock();
            try {
                statements.forEach(Statement::execute);
            } finally {
                SS.Scopes.unlock(scopes);
            }
        } else {
            SS.Scopes.up();
            try {
                statements.forEach(Statement::execute);
            } finally {
                SS.Scopes.down();
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("block [\n");
        for (Statement statement : statements)
            builder.append(statement);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public void lock() {
        locked = true;
    }

    @Override
    public void unlock() {
        locked = false;
    }

    @Override
    public int hashCode() {
        return statements.hashCode() ^ (6 * 40 * 31);
    }
}