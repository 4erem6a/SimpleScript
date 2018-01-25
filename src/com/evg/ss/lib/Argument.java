package com.evg.ss.lib;

import com.evg.ss.parser.ast.Expression;

public final class Argument {

    private final String name;
    private final Expression value;
    private final boolean isVariadic;

    public Argument(String name, boolean isVariadic, Expression value) {
        this.name = name;
        this.isVariadic = isVariadic;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }

    public boolean isVariadic() {
        return isVariadic;
    }

    @Override
    public int hashCode() {
        return name.hashCode()
                ^ (value == null ? 1 : value.hashCode())
                ^ Boolean.hashCode(isVariadic);
    }
}