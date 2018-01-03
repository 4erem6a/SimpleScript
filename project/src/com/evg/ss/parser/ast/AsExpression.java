package com.evg.ss.parser.ast;

import com.evg.ss.lib.Converter;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Type;
import com.evg.ss.values.TypeValue;
import com.evg.ss.values.Value;

public final class AsExpression implements Expression {

    private Expression target, type;

    public AsExpression(Expression target, Expression type) {
        this.target = target;
        this.type = type;
    }

    @Override
    public Value eval() {
        final Value target = this.target.eval();
        final Type type;
        final Value value = this.type.eval();
        if (value instanceof TypeValue)
            type = ((TypeValue) value).getValue();
        else type = value.getType();
        return new Converter(target.getType(), type).convert(target);
    }

    public Expression getTarget() {
        return target;
    }

    public Expression getType() {
        return type;
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
