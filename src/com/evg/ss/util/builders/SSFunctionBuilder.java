package com.evg.ss.util.builders;

import com.evg.ss.lib.Argument;
import com.evg.ss.lib.Arguments;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.ReturnStatement;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.parser.ast.ValueExpression;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.UndefinedValue;
import com.evg.ss.values.Value;

public final class SSFunctionBuilder {

    private Arguments arguments = new Arguments();
    private Statement body = new ReturnStatement(UndefinedValue.UndefinedExpression);
    private MapValue callContext = null;
    private String name = null;

    public SSFunctionBuilder(String name) {
        this.name = name;
    }

    public SSFunctionBuilder() {
    }

    public SSFunctionBuilder setArgs(Argument... args) {
        this.arguments = new Arguments(args);
        return this;
    }

    public SSFunctionBuilder setArgs(Arguments args) {
        this.arguments = args;
        return this;
    }

    public SSFunctionBuilder setContext(MapValue context) {
        this.callContext = context;
        return this;
    }

    public SSFunctionBuilder returns(Expression expression) {
        this.body = new ReturnStatement(expression);
        return this;
    }

    public SSFunctionBuilder returns(Value value) {
        this.body = new ReturnStatement(new ValueExpression(value));
        return this;
    }

    public SSFunctionBuilder setBody(Statement body) {
        this.body = body;
        return this;
    }

    public SSFunctionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SSFunction build() {
        return new SSFunction(callContext, arguments, body, name);
    }
}