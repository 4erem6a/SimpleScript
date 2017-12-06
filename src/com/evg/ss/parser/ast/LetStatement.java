package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.VariableAlreadyExistsException;
import com.evg.ss.lib.SS;
import com.evg.ss.values.NullValue;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class LetStatement implements Statement {
    private final String name;
    private final Expression value;
    private final boolean isConst;

    public String getName() {
        return name;
    }

    public LetStatement(String name, Expression value, boolean isConst) {
        this.name = name;
        this.value = value;
        this.isConst = isConst;
    }

    public LetStatement(String name) {
        this(name, NullValue.NullExpression, false);
    }

    @Override
    public void execute() {
        if (!SS.Variables.existsTop(name))
            SS.Variables.put(name, value.eval(), isConst);
        else throw new VariableAlreadyExistsException(name);
    }

    @Override
    public String toString() {
        return String.format("let %s%s = %s\n", isConst ? "const " : "", name, value);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Expression getValue() {
        return value;
    }
}