package com.evg.ss.linter.visitors;

import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.SSLintException;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.parser.ast.ExportsStatement;
import com.evg.ss.parser.ast.RequireExpression;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.parser.visitors.AbstractVisitor;

public final class InvalidRequirementValidator extends AbstractVisitor {

    @Override
    public void visit(RequireExpression target) {
        if (!SSModule.isModuleExists(target.getPath()))
            throw new SSLintException("Required module '%s' does not exists or contains errors.", target.getPath());
        final SimpleScript.CompiledScript module = SSModule.loadModule(target.getPath());
        if (!SSModule.isNative(target.getPath())
                && module != null
                && !ContainsExportsVisitor.containsExports(module.getProgram()))
            throw new SSLintException("Required module '%s' is missing exports statement.", module);
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