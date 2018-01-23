package com.evg.ss.linter.visitors;

import com.evg.ss.exceptions.SSLintException;
import com.evg.ss.parser.ast.ContainerAccessExpression;
import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.ImportStatement;
import com.evg.ss.parser.ast.ValueExpression;
import com.evg.ss.parser.visitors.AbstractVisitor;
import com.evg.ss.values.StringValue;

public final class InvalidImportExpressionValidator extends AbstractVisitor {

    private final boolean inner;

    public InvalidImportExpressionValidator() {
        this(false);
    }

    private InvalidImportExpressionValidator(boolean inner) {
        this.inner = inner;
    }

    @Override
    public void visit(ImportStatement target) {
        if (!(target.getPath() instanceof ContainerAccessExpression))
            except();
        target.getPath().accept(new InvalidImportExpressionValidator(true));
    }

    @Override
    public void visit(ContainerAccessExpression target) {
        if (inner) {
            final Expression key = target.getKey();
            if (!(key instanceof ValueExpression))
                except();
            if (!(((ValueExpression) key).getValue() instanceof StringValue))
                except();
        }
        super.visit(target);
    }

    private void except() {
        throw new SSLintException("Invalid import expression.");
    }
}