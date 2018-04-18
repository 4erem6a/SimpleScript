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
        if (target.getInitialization() != null)
            target.getInitialization().accept(this);
        if (target.getCondition() != null)
            target.getCondition().accept(this);
        if (target.getIteration() != null)
            target.getIteration().accept(this);
        target.getBody().accept(this);
    }

    @Override
    public void visit(CallExpression target) {
        target.getValue().accept(this);
        Arrays.stream(target.getArgs()).forEach(a -> a.accept(this));
    }

    @Override
    public void visit(FunctionDefinitionStatement target) {
        target.getFunction().getBody().accept(this);
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
    public void visit(NameofExpression target) {

    }

    @Override
    public void visit(TypeExpression target) {

    }

    @Override
    public void visit(ThisExpression target) {

    }

    @Override
    public void visit(ThatExpression target) {

    }

    @Override
    public void visit(NewExpression target) {
        target.getFunctionCall().accept(this);
    }

    @Override
    public void visit(RequireExpression target) {
    }

    @Override
    public void visit(ThrowStatement target) {
        target.getExpression().accept(this);
    }

    @Override
    public void visit(TryCatchFinallyStatement target) {
        target.getTry().accept(this);
        target.getCatches().forEach(_catch -> {
            if (_catch.getCondition() != null)
                _catch.getCondition().accept(this);
            _catch.getBody().accept(this);
        });
        if (target.getFinally() != null)
            target.getFinally().accept(this);
    }

    @Override
    public void visit(InstantFunctionExpression target) {
        target.getBody().accept(this);
    }

    @Override
    public void visit(AnonymousClassExpression target) {
        if (target.getConstructor() != null)
            target.getConstructor().accept(this);
        if (target.getBase() != null)
            target.getBase().accept(this);
        target.getMembers().forEach(member -> {
            if (member instanceof AnonymousClassExpression.ASTClassField)
                if (((AnonymousClassExpression.ASTClassField) member).getValue() != null)
                    ((AnonymousClassExpression.ASTClassField) member).getValue().accept(this);
            if (member instanceof AnonymousClassExpression.ASTClassMethod)
                ((AnonymousClassExpression.ASTClassMethod) member).getFunction().accept(this);
        });
    }

    @Override
    public void visit(ClassDefinitionStatement target) {
        target.getClassExpression().accept(this);
    }

    @Override
    public void visit(CatchExpression target) {
        target.getExpression().accept(this);
        if (target.getHandler() != null)
            target.getHandler().accept(this);
    }
}