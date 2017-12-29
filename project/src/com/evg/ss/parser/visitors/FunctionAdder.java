package com.evg.ss.parser.visitors;

import com.evg.ss.parser.ast.FunctionDefinitionStatement;

public final class FunctionAdder extends AbstractVisitor {

    @Override
    public void visit(FunctionDefinitionStatement target) {
        target.execute();
    }

}