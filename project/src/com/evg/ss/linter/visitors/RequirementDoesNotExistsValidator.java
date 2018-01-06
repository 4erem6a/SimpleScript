package com.evg.ss.linter.visitors;

import com.evg.ss.exceptions.SSLintException;
import com.evg.ss.lib.Linker;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.parser.ast.RequireExpression;
import com.evg.ss.parser.visitors.AbstractVisitor;

import java.io.File;

import static com.evg.ss.parser.ast.RequireExpression.RequireMode;

public final class RequirementDoesNotExistsValidator extends AbstractVisitor {

    @Override
    public void visit(RequireExpression target) {
        final RequireMode mode = target.getMode();
        final String module = target.getModuleName();
        switch (mode) {
            case MODULE:
                if (!SSModule.isModuleExists(module))
                    throw new SSLintException("SSLintException: Required module '%s' does not exists.", module);
                break;
            case LOCAL:
                if (Linker.getLink(module) == null)
                    throw new SSLintException("SSLintException: Required local file '%s.ss' does not exists.", module);
                break;
            case EXTERNAL:
                if (!new File(module).exists())
                    throw new SSLintException("SSLintException: Required file '%s' does not exists.", module);
                break;
        }
    }
}
