package com.evg.ss.ast;

import com.evg.ss.util.interpolation.InterpolatedString;
import com.evg.ss.values.Value;

public final class InterpolatedStringExpression extends ValueExpression {

    private String string;

    public InterpolatedStringExpression(String string) {
        this.string = string;
    }

    @Override
    public Value eval() {
        return new ValueExpression(new InterpolatedString(string).calculate()).eval();
    }

    @Override
    public String toString() {
        return String.format("$[%s]", string);
    }
}