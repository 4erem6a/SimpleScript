package com.evg.ss.parser.ast;

import com.evg.ss.lib.Argument;
import com.evg.ss.values.Type;
import com.evg.ss.values.TypeValue;

public final class ArgumentExpression {

    private final String name;
    private final Expression defaultValue;
    private final ConstTypeExpression requiredType;

    public ArgumentExpression(String name, ConstTypeExpression requiredType, Expression defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.requiredType = requiredType;
    }

    public Argument getArgument() {
        final Type type = requiredType == null ? null : ((TypeValue) requiredType.eval()).getValue();
        return new Argument(name, defaultValue, type);
    }

}