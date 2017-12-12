package com.evg.ss.parser.ast;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class FunctionDefinitionStatement implements Statement {

    private String name;
    private Statement body;
    private ArgumentExpression[] args;

    public FunctionDefinitionStatement(String name, ArgumentExpression[] args, Statement body) {
        this.name = name;
        this.body = body;
        this.args = args;
    }

    @Override
    public void execute() {
        SS.Functions.put(name, new SSFunction(args, body));
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
}