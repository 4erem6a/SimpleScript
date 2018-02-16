package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.IdentifierAlreadyExistsException;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.NullValue;

public final class LetStatement extends Statement {
    private final String name;
    private final Expression value;
    private final boolean isConst;

    public LetStatement(String name, Expression value, boolean isConst) {
        this.name = name;
        this.value = value;
        this.isConst = isConst;
    }

    public LetStatement(String name) {
        this(name, NullValue.NullExpression, false);
    }

    public String getName() {
        return name;
    }

    @Override
    public void execute() {
        if (!SS.Identifiers.existsTop(name))
            SS.Identifiers.put(name, value instanceof Accessible ? ((Accessible) value).get() : value.eval(), isConst);
        else throw new IdentifierAlreadyExistsException(name);
    }

    public boolean isConst() {
        return isConst;
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

    @Override
    public int hashCode() {
        return name.hashCode() ^ value.hashCode() ^ Boolean.hashCode(isConst) ^ (24 * 22 * 31);
    }
}