package com.evg.ss.parser.ast;

import com.evg.ss.lib.Arguments;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.Value;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class AnonymousFunctionExpression extends Expression implements Lockable {

    private ArgumentExpression[] args;
    private Statement body;
    private boolean locked = false;
    private String name;

    public AnonymousFunctionExpression(ArgumentExpression[] args, Statement body, String name) {
        this.args = args;
        this.body = body;
        this.name = name;
    }

    public AnonymousFunctionExpression(ArgumentExpression[] args, Statement body) {
        this(args, body, null);
    }

    public String getName() {
        return name;
    }

    public SSFunction toSSFunction() {
        return new SSFunction(SS.CallContext.get(),
                new Arguments(Arrays.stream(args)
                        .map(ArgumentExpression::getArgument)
                        .collect(Collectors.toList())),
                body, name);
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

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }
}