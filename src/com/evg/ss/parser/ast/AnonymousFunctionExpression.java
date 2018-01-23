package com.evg.ss.parser.ast;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class AnonymousFunctionExpression implements Expression, Lockable {

    private ArgumentExpression[] args;
    private Statement body;
    private boolean locked = false;

    public AnonymousFunctionExpression(ArgumentExpression[] args, Statement body) {
        this.args = args;
        this.body = body;
    }

    public SSFunction toSSFunction() {
        return new SSFunction(SS.CallContext.get(), args, body);
    }

    @Override
    public Value eval() {
        final SSFunction function = toSSFunction();
        function.setLocked(locked);
        return new FunctionValue(function);
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }

    public ArgumentExpression[] getArgs() {
        return args;
    }

    public void setArgs(ArgumentExpression[] args) {
        this.args = args;
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
        return locked;
    }

    @Override
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
        return Arrays.hashCode(args) ^ body.hashCode() ^ Boolean.hashCode(locked) ^ (45 * 31);
    }
}