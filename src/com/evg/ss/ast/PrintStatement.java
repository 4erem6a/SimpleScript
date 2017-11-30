package com.evg.ss.ast;

import com.evg.ss.values.*;

/**
 * @author 4erem6a
 */
public class PrintStatement implements Statement {

    public Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        final Value value = expression.eval();
        System.out.println(StringValue.asStringValue(value).asString());
    }

    @Override
    public String toString() {
        return String.format("print %s\n", expression);
    }
}
