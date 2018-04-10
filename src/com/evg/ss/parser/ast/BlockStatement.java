package com.evg.ss.parser.ast;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

import java.util.ArrayList;
import java.util.List;

public final class BlockStatement extends Statement implements Lockable {

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
        if (isModifierPresent("United")) {
            new UnitedStatement(statements).execute();
            return;
        }
        if (locked) {
            SS.Scopes scopes = SS.Scopes.lock();
            try {
                statements.stream()
                        .filter(s -> s instanceof FunctionDefinitionStatement)
                        .forEach(Statement::execute);
                statements.stream()
                        .filter(s -> !(s instanceof FunctionDefinitionStatement))
                        .forEach(Statement::execute);
            } finally {
                SS.Scopes.unlock(scopes);
            }
        } else {
            SS.Scopes.up();
            try {
                statements.stream()
                        .filter(s -> s instanceof FunctionDefinitionStatement)
                        .forEach(Statement::execute);
                statements.stream()
                        .filter(s -> !(s instanceof FunctionDefinitionStatement))
                        .forEach(Statement::execute);
            } finally {
                SS.Scopes.down();
            }
        }
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
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