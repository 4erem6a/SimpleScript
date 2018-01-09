package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class TypeExpression implements Expression {

    private ConstTypeExpression type;

    public TypeExpression(ConstTypeExpression type) {
        this.type = type;
    }

    @Override
    public Value eval() {
        return type.getType().getTypeValue();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public ConstTypeExpression getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return type.hashCode() ^ (39 * 7 * 31);
    }
}
