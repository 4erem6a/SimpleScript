package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
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

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }
}