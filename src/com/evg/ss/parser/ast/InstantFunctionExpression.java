package com.evg.ss.parser.ast;

import com.evg.ss.lib.Arguments;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class InstantFunctionExpression extends Expression implements Lockable {

    private Statement body;
    private boolean locked = false;

    public InstantFunctionExpression(Statement body) {
        this.body = body;
    }

    @Override
    public Value eval() {
        final SSFunction function = new SSFunction(SS.CallContext.get(), new Arguments(), body);
        function.setLocked(locked);
        function.setName("$instant");
        return function.execute();
    }

    public Statement getBody() {
        return body;
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
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }
}