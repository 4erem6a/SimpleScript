package com.evg.ss.parser.ast;

import com.evg.ss.lib.Function;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

import java.util.Arrays;

public final class FunctionDefinitionStatement implements Statement, Lockable {

    private String name;
    private Statement body;
    private ArgumentExpression[] args;
    private boolean locked = false;

    public FunctionDefinitionStatement(String name, ArgumentExpression[] args, Statement body) {
        this.name = name;
        this.body = body;
        this.args = args;
    }

    public ArgumentExpression[] getArgs() {
        return args;
    }

    public void setArgs(ArgumentExpression[] args) {
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Function getFunction() {
        final SSFunction function = new SSFunction(null, name, args, body);
        function.setLocked(locked);
        return function;
    }

    @Override
    public void execute() {
        SS.Functions.put(name, getFunction());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
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
        return name.hashCode()
                ^ body.hashCode()
                ^ Arrays.hashCode(args)
                ^ Boolean.hashCode(locked)
                ^ (18 * 28 * 31);
    }
}