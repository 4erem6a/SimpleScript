package com.evg.ss.parser.ast;

import com.evg.ss.lib.Argument;

public final class ArgumentExpression {

    private final String name;
    private final Expression defaultValue;
    private final boolean isVariadic;
    private final boolean isConst;

    public ArgumentExpression(String name, boolean isConst, boolean isVariadic, Expression defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.isVariadic = isVariadic;
        this.isConst = isConst;
    }

    public Argument getArgument() {
        return new Argument(name, isConst, isVariadic, defaultValue);
    }

    public String getName() {
        return name;
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public boolean isVariadic() {
        return isVariadic;
    }

    @Override
    public int hashCode() {
        return name.hashCode()
                ^ (defaultValue == null ? 1 : defaultValue.hashCode())
                ^ Boolean.hashCode(isVariadic)
                ^ (2 * 44 * 31);
    }
}