package com.evg.ss.parser.visitors;

import com.evg.ss.parser.ast.ClassDefinitionStatement;

public final class ClassAdder extends AbstractVisitor {

    @Override
    public void visit(ClassDefinitionStatement target) {
        target.execute();
    }

}