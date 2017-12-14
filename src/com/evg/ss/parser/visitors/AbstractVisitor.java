package com.evg.ss.parser.visitors;

import com.evg.ss.parser.ast.*;

import java.util.Arrays;

public abstract class AbstractVisitor implements Visitor {

    @Override
    public void visit(AnonymousFunctionExpression target) {
        target.getBody().accept(this);
    }

    @Override
    public void visit(ContainerAccessExpression target) {
        target.getTarget().accept(this);
        target.getKey().accept(this);
    }

    @Override
    public void visit(ArrayExpression target) {
        Arrays.stream(target.getExpressions()).forEach(i -> i.accept(this));
    }

    @Override
    public void visit(AssignmentExpression target) {
        target.getTarget().accept(this);
        target.getValue().accept(this);
    }

    @Override
    public void visit(BinaryExpression target) {
        target.getLeft().accept(this);
        target.getRight().accept(this);
    }

    @Override
    public void visit(BlockStatement target) {
        target.getStatements().forEach(s -> s.accept(this));
    }

    @Override
    public void visit(BreakStatement target) {
    }

    @Override
    public void visit(ConstTypeExpression target) {
    }

    @Override
    public void visit(ContinueStatement target) {
    }

    @Override
    public void visit(DoWhileStatement target) {
        target.getBody().accept(this);
        target.getCondition().accept(this);
    }

    @Override
    public void visit(ExportsStatement target) {
        target.getExpression().accept(this);
    }

    @Override
    public void visit(ExpressionStatement target) {
        target.getExpression().accept(this);
    }

    @Override
    public void visit(ForEachStatement target) {
        target.getIteratorDefinition().accept(this);
        target.getTarget().accept(this);
        target.getBody().accept(this);
    }

    @Override
    public void visit(ForStatement target) {
        target.getInitialization().accept(this);
        target.getCondition().accept(this);
        target.getIteration().accept(this);
        target.getBody().accept(this);
    }

    @Override
    public void visit(FunctionCallExpression target) {
        target.getFunction().accept(this);
        Arrays.stream(target.getArgs()).forEach(a -> a.accept(this));
    }

    @Override
    public void visit(FunctionDefinitionStatement target) {
        target.getBody().accept(this);
    }

    @Override
    public void visit(FunctionReferenceExpression target) {
    }

    @Override
    public void visit(FunctionStatement target) {
        target.getExpression().accept(this);
    }

    @Override
    public void visit(IfStatement target) {
        target.getCondition().accept(this);
        target.getIfStatement().accept(this);
        if (target.getElseStatement() != null)
            target.getElseStatement().accept(this);
    }

    @Override
    public void visit(InterpolatedStringExpression target) {
    }

    @Override
    public void visit(LetExpression target) {
        target.getValue().accept(this);
    }

    @Override
    public void visit(LetStatement target) {
        target.getValue().accept(this);
    }

    @Override
    public void visit(MapExpression target) {
        target.getMap().forEach((k, v) -> {
            k.accept(this);
            v.accept(this);
        });
    }

    @Override
    public void visit(RequireStatementExpression target) {
    }

    @Override
    public void visit(ReturnStatement target) {
        target.getExpression().accept(this);
    }

    @Override
    public void visit(SwitchStatement target) {
        target.getValue().accept(this);
        target.getCases().forEach(c -> {
            c.getValue().accept(this);
            c.getBody().accept(this);
        });
    }

    @Override
    public void visit(TernaryExpression target) {
        target.getCondition().accept(this);
        target.getOnTrue().accept(this);
        target.getOnFalse().accept(this);
    }

    @Override
    public void visit(TypeofExpression target) {
        target.getExpression().accept(this);
    }

    @Override
    public void visit(UnaryExpression target) {
        target.getExpression().accept(this);
    }

    @Override
    public void visit(UnitedStatement target) {
        target.getStatements().forEach(s -> s.accept(this));
    }

    @Override
    public void visit(ValueExpression target) {
    }

    @Override
    public void visit(VariableExpression target) {
    }

    @Override
    public void visit(WhileStatement target) {
        target.getCondition().accept(this);
        target.getBody().accept(this);
    }

    @Override
    public void visit(AsExpression target) {
        target.getTarget().accept(this);
        target.getType().accept(this);
    }

    @Override
    public void visit(NameofExpression target) {
    }

    @Override
    public void visit(TypeExpression typeExpression) {

    }
}