package com.evg.ss.lib.mssp;

import com.evg.ss.lib.Linker;
import com.evg.ss.parser.ast.RequireStatementExpression;
import com.evg.ss.parser.visitors.AbstractVisitor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.evg.ss.parser.ast.RequireStatementExpression.*;

public final class LinkVisitor extends AbstractVisitor {

    private final Map<String, Path> linkedModules = new HashMap<>();

    @Override
    public void visit(RequireStatementExpression target) {
        final RequireMode mode = target.getMode();
        if (mode == RequireMode.MODULE)
            return;
        final String name;
        if (mode == RequireMode.LOCAL)
            name = target.getVariableName() == null ? target.getModuleName() : target.getVariableName();
        else name = target.getVariableName();
        final Path path;
        if (mode == RequireMode.LOCAL)
            path = Linker.getLink(name);
        else path = Paths.get(target.getModuleName());
        linkedModules.put(name, path);
    }

    public Map<String, Path> getLinkedModules() {
        return linkedModules;
    }
}