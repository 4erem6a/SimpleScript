package com.evg.ss.linter.visitors;

import com.evg.ss.parser.ast.*;
import com.evg.ss.parser.visitors.AbstractVisitor;

public abstract class LintVisitor extends AbstractVisitor {
    public static final String LINTER_IGNORE = "LinterIgnore";

    @Override
    public void visit(AnonymousFunctionExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ContainerAccessExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ArrayExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(AssignmentExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(BinaryExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(BlockStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(BreakStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ConstTypeExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ContinueStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(DoWhileStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ExportsStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ExpressionStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ForEachStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ForStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(CallExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(FunctionDefinitionStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(IfStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(InterpolatedStringExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(LetExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(LetStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(MapExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ReturnStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(SwitchStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(TernaryExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(TypeofExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(UnaryExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(UnitedStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ValueExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(VariableExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(WhileStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(NameofExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(TypeExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ThisExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ThatExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(NewExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(RequireExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ThrowStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(TryCatchFinallyStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(InstantFunctionExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(AnonymousClassExpression target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }

    @Override
    public void visit(ClassDefinitionStatement target) {
        if (!target.isModifierPresent(LINTER_IGNORE))
            super.visit(target);
    }
}