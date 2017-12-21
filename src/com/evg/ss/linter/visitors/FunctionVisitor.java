package com.evg.ss.linter.visitors;

import com.evg.ss.parser.ast.FunctionDefinitionStatement;
import com.evg.ss.parser.visitors.AbstractVisitor;

import java.util.ArrayList;
import java.util.List;

public final class FunctionVisitor extends AbstractVisitor {

    private List<String> functions = new ArrayList<>();

    @Override
    public void visit(FunctionDefinitionStatement target) {
        functions.add(target.getName());
    }

    public List<String> getFunctions() {
        return functions;
    }
}
