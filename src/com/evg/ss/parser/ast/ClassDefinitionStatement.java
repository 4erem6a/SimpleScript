package com.evg.ss.parser.ast;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.ClassValue;

public final class ClassDefinitionStatement extends Statement {

    private String name;
    private AnonymousClassExpression classExpression;

    public ClassDefinitionStatement(String name, AnonymousClassExpression classExpression) {
        this.name = name;
        this.classExpression = classExpression;
    }

    @Override
    public void execute() {
        final ClassValue _class = ((ClassValue) classExpression.eval());
        _class.setName(name);
        SS.Identifiers.put(name, _class, false);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    public AnonymousClassExpression getClassExpression() {
        return classExpression;
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }
}