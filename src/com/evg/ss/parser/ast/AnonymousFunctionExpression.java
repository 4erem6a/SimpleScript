package com.evg.ss.parser.ast;

import com.evg.ss.lib.SSFunction;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.Value;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class AnonymousFunctionExpression implements Expression {

    private ArgumentExpression[] args;
    private Statement body;

    public AnonymousFunctionExpression(ArgumentExpression[] args, Statement body) {
        this.args = args;
        this.body = body;
    }

    @Override
    public Value eval() {
        return new FunctionValue(new SSFunction(args, body));
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }
}