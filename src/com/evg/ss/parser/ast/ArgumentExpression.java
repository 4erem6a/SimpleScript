package com.evg.ss.parser.ast;

import com.evg.ss.lib.Argument;
import com.evg.ss.values.Type;
import com.evg.ss.values.TypeValue;

public final class ArgumentExpression {

    private final String name;
    private final Expression defaultValue;
    private final ConstTypeExpression requiredType;
    private final boolean isVariadic;

    public ArgumentExpression(String name, boolean isVariadic, ConstTypeExpression requiredType, Expression defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.requiredType = requiredType;
        this.isVariadic = isVariadic;
    }

    public Argument getArgument() {
        final Type type = requiredType == null ? null : ((TypeValue) requiredType.eval()).getValue();
        return new Argument(name, isVariadic, defaultValue, type);
    }

    public String getName() {
        return name;
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public ConstTypeExpression getRequiredType() {
        return requiredType;
    }

    @Override
    public int hashCode() {
        return name.hashCode()
                ^ (defaultValue == null ? 1 : defaultValue.hashCode())
                ^ (requiredType == null ? 1 : requiredType.hashCode())
                ^ Boolean.hashCode(isVariadic)
                ^ (2 * 44 * 31);
    }
}