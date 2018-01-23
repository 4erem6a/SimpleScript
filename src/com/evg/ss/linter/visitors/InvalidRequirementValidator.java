package com.evg.ss.linter.visitors;

import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.SSLintException;
import com.evg.ss.lib.Linker;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.parser.ast.ExportsStatement;
import com.evg.ss.parser.ast.RequireExpression;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.parser.visitors.AbstractVisitor;
import com.evg.ss.util.Utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.evg.ss.parser.ast.RequireExpression.RequireMode;

public final class InvalidRequirementValidator extends AbstractVisitor {

    @Override
    public void visit(RequireExpression target) {
        final RequireMode mode = target.getMode();
        final String module = target.getModuleName();
        final Path path;
        if (mode == RequireMode.MODULE) {
            if (!SSModule.isModuleExists(module))
                throw new SSLintException("Required module '%s' does not exists.", module);
            return;
        } else if (mode == RequireMode.LOCAL) {
            path = Linker.getLink(module);
            if (path == null)
                throw new SSLintException("Required local file '%s.ss' does not exists.", module);
        } else if (mode == RequireMode.EXTERNAL) {
            if (!new File(module).exists())
                throw new SSLintException("Required file '%s' does not exists.", module);
            path = Paths.get(module);
            if (path == null)
                throw new SSLintException("Required local file '%s.ss' does not exists.", module);
        } else path = null;
        final String source = Utils.loadSource(path);
        if (source == null)
            throw new SSLintException("Unable to read required local file '%s.ss'.", module);
        try {
            SimpleScript.fromSource(source).compile();
        } catch (Exception e) {
            throw new SSLintException("Required local file '%s.ss' is invalid.", module);
        }
        if (!ContainsExportsVisitor.containsExports(SimpleScript.fromSource(source).compile().getProgram()))
            throw new SSLintException("Required local file '%s.ss' is missing exports statement.", module);
    }

    private static class ContainsExportsVisitor extends AbstractVisitor {

        boolean exports = false;

        public static boolean containsExports(Statement root) {
            final ContainsExportsVisitor visitor = new ContainsExportsVisitor();
            root.accept(visitor);
            return visitor.containsExports();
        }

        @Override
        public void visit(ExportsStatement target) {
            super.visit(target);
            exports = true;
        }

        private boolean containsExports() {
            return exports;
        }
    }
}