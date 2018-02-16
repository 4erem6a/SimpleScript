package com.evg.ss.parser.ast;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class FunctionDefinitionStatement extends Statement implements Lockable {

    private String name;
    private AnonymousFunctionExpression function;

    public FunctionDefinitionStatement(String name, AnonymousFunctionExpression function) {
        this.name = name;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnonymousFunctionExpression getFunction() {
        return function;
    }

    @Override
    public void execute() {
        final SSFunction function = this.function.toSSFunction();
        function.setName(name);
        SS.Identifiers.put(name, Value.of(function), false);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isLocked() {
        return function.isLocked();
    }

    @Override
    public void setLocked(boolean locked) {
        function.setLocked(locked);
    }

    @Override
    public void lock() {
        function.lock();
    }

    @Override
    public void unlock() {
        function.unlock();
    }

    @Override
    public int hashCode() {
        return function.hashCode();
    }
}