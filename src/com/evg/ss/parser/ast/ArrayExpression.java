package com.evg.ss.parser.ast;

import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.Value;

import java.util.Arrays;

public final class ArrayExpression extends Expression {

    private Expression[] expressions;

    public ArrayExpression() {
        this.expressions = new Expression[0];
    }

    public ArrayExpression(Expression[] expressions) {
        this.expressions = expressions;
    }

    @Override
    public Value eval() {
        return new ArrayValue(Arrays.stream(expressions).map(Expression::eval).toArray(Value[]::new));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Expression[] getExpressions() {
        return expressions;
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(expressions) ^ (3 * 43 * 31);
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }
}