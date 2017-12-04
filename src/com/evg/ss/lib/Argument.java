package com.evg.ss.lib;

import com.evg.ss.ast.Expression;
import com.evg.ss.values.Type;

@Deprecated
public class Argument {
//TODO: Finish @Argument & insert it to @SSFunction, @FunctionExpression, @AnonymousFunctionExpression


    private final String name;
    private final Expression defaultValue;
    private final Type requiredType;

    public Argument(String name) {
        this(name, null);
    }

    public Argument(String name, Expression defaultValue) {
        this(name, defaultValue, null);
    }

    public Argument(String name, Expression defaultValue, Type requiredType) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.requiredType = requiredType;
    }
}