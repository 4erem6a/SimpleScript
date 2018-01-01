package com.evg.ss.parser.visitors;

import com.evg.ss.linter.LintException;
import com.evg.ss.parser.ast.*;

public interface Visitor {

    void visit(AnonymousFunctionExpression target);

    void visit(ContainerAccessExpression target);

    void visit(ArrayExpression target);

    void visit(AssignmentExpression target);

    void visit(BinaryExpression target);

    void visit(BlockStatement target);

    void visit(BreakStatement target);

    void visit(ConstTypeExpression target);

    void visit(ContinueStatement target);

    void visit(DoWhileStatement target);

    void visit(ExportsStatement target);

    void visit(ExpressionStatement target);

    void visit(ForEachStatement target);

    void visit(ForStatement target);

    void visit(FunctionCallExpression target);

    void visit(FunctionDefinitionStatement target);

    void visit(FunctionReferenceExpression target);

    void visit(IfStatement target);

    void visit(InterpolatedStringExpression target);

    void visit(LetExpression target);

    void visit(LetStatement target);

    void visit(MapExpression target);

    void visit(RequireStatement target);

    void visit(ReturnStatement target);

    void visit(SwitchStatement target);

    void visit(TernaryExpression target);

    void visit(TypeofExpression target);

    void visit(UnaryExpression target);

    void visit(UnitedStatement target);

    void visit(ValueExpression target);

    void visit(VariableExpression target) throws LintException;

    void visit(WhileStatement target);

    void visit(NameofExpression nameofExpression);

    void visit(AsExpression asExpression);

    void visit(TypeExpression typeExpression);

    void visit(ImportStatement importStatement);

    void visit(ThisExpression thisExpression);

    void visit(NewExpression newExpression);

    void visit(RequireExpression requireExpression);
}