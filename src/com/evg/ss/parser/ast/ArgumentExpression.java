package com.evg.ss.parser.ast;

import com.evg.ss.lib.Argument;

public final class ArgumentExpression {

    private final String name;
    private final Expression defaultValue;
    private final boolean isVariadic;

    public ArgumentExpression(String name, boolean isVariadic, Expression defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.isVariadic = isVariadic;
    }

    public Argument getArgument() {
        return new Argument(name, isVariadic, defaultValue);
    }

    public String getName() {
        return name;
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    @Override
    public int hashCode() {
        return name.hashCode()
                ^ (defaultValue == null ? 1 : defaultValue.hashCode())
                ^ Boolean.hashCode(isVariadic)
                ^ (2 * 44 * 31);
    }
}