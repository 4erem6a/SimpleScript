package com.evg.ss.lib;

import com.evg.ss.parser.ast.Expression;
import com.evg.ss.values.Type;

public final class Argument {

    private final String name;
    private final Expression value;
    private final Type type;

    public Argument(String name, Expression value, Type type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public boolean hasType() {
        return type != null;
    }

    public boolean hasValue() {
        return value != null;
    }

    @Override
    public int hashCode() {
        return name.hashCode() & (hasType() ? type.hashCode() : 1);
    }
}