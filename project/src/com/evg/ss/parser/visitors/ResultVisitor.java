package com.evg.ss.parser.visitors;

import com.evg.ss.parser.ast.*;

public interface ResultVisitor<TResult> {

    TResult visit(AnonymousFunctionExpression target);

    TResult visit(ContainerAccessExpression target);

    TResult visit(ArrayExpression target);

    TResult visit(AssignmentExpression target);

    TResult visit(BinaryExpression target);

    TResult visit(BlockStatement target);

    TResult visit(BreakStatement target);

    TResult visit(ConstTypeExpression target);

    TResult visit(ContinueStatement target);

    TResult visit(DoWhileStatement target);

    TResult visit(ExportsStatement target);

    TResult visit(ExpressionStatement target);

    TResult visit(ForEachStatement target);

    TResult visit(ForStatement target);

    TResult visit(FunctionCallExpression target);

    TResult visit(FunctionDefinitionStatement target);

    TResult visit(FunctionReferenceExpression target);

    TResult visit(IfStatement target);

    TResult visit(InterpolatedStringExpression target);

    TResult visit(LetExpression target);

    TResult visit(LetStatement target);

    TResult visit(MapExpression target);

    TResult visit(RequireStatement target);

    TResult visit(ReturnStatement target);

    TResult visit(SwitchStatement target);

    TResult visit(TernaryExpression target);

    TResult visit(TypeofExpression target);

    TResult visit(UnaryExpression target);

    TResult visit(UnitedStatement target);

    TResult visit(ValueExpression target);

    TResult visit(VariableExpression target);

    TResult visit(WhileStatement target);

    TResult visit(NameofExpression nameofExpression);

    TResult visit(TypeExpression typeExpression);

    TResult visit(ImportStatement importStatement);

    TResult visit(ThisExpression thisExpression);

    TResult visit(NewExpression newExpression);

    TResult visit(RequireExpression requireExpression);

    TResult visit(ValueCloneExpression valueCloneExpression);

    TResult visit(ThrowStatement throwStatement);

    TResult visit(TryCatchFinallyStatement tryCatchFinallyStatement);
}