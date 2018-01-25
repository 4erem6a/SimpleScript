package com.evg.ss.lib.msc;

import com.evg.ss.parser.ast.*;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.values.BoolValue;
import com.evg.ss.values.NumberValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

import java.util.Map;

public class MSCVisitor implements ResultVisitor<String> {

    private static String cutKeyword(String keyword, String block) {
        return block.substring(keyword.length());
    }

    private String processArgDefinition(ArgumentExpression... args) {
        final StringBuilder builder = new StringBuilder();
        for (ArgumentExpression arg : args) {
            final StringBuilder argBuilder = new StringBuilder();
            argBuilder.append(arg.getName());
            if (arg.getDefaultValue() != null)
                argBuilder.append(String.format("=%s", arg.getDefaultValue().accept(this)));
            builder.append(argBuilder.append(',').toString());
        }
        return args.length > 0 ? builder.deleteCharAt(builder.length() - 1).toString() : "";
    }

    private String processArgs(Expression... args) {
        final StringBuilder builder = new StringBuilder();
        for (Expression arg : args)
            builder.append(arg.accept(this)).append(",");
        return args.length > 0 ? builder.deleteCharAt(builder.length() - 1).toString() : "";
    }

    @Override
    public String visit(AnonymousFunctionExpression target) {
        return String.format("%sfunction(%s)%s",
                (target.isLocked() ? "locked " : ""),
                processArgDefinition(target.getArgs()),
                target.getBody().accept(this));
    }

    @Override
    public String visit(ContainerAccessExpression target) {
        return String.format("%s[%s]", target.getTarget().accept(this), target.getKey().accept(this));
    }

    @Override
    public String visit(ArrayExpression target) {
        final StringBuilder builder = new StringBuilder();
        for (Expression expression : target.getExpressions())
            builder.append(expression.accept(this)).append(",");
        return String.format("[%s]", builder.deleteCharAt(builder.length() - 1).toString());
    }

    @Override
    public String visit(AssignmentExpression target) {
        return String.format("%s=%s", target.getTarget().accept(this), target.getValue().accept(this));
    }

    @Override
    public String visit(BinaryExpression target) {
        return String.format("(%s%s%s)", target.getLeft().accept(this), target.getOperation().getKey(), target.getRight().accept(this));
    }

    @Override
    public String visit(BlockStatement target) {
        final StringBuilder builder = new StringBuilder();
        for (Statement statement : target.getStatements())
            builder.append(statement.accept(this)).append(";");
        return String.format("%sblock{%s}", (target.isLocked() ? "locked " : ""), builder.toString());
    }

    @Override
    public String visit(BreakStatement target) {
        return "break";
    }

    @Override
    public String visit(ConstTypeExpression target) {
        return target.getTypename();
    }

    @Override
    public String visit(ContinueStatement target) {
        return "continue";
    }

    @Override
    public String visit(DoWhileStatement target) {
        return String.format("do %s while(%s)", target.getBody().accept(this), target.getCondition().accept(this));
    }

    @Override
    public String visit(ExportsStatement target) {
        return String.format("exports %s", target.getExpression().accept(this));
    }

    @Override
    public String visit(ExpressionStatement target) {
        return String.format(">%s", target.getExpression().accept(this));
    }

    @Override
    public String visit(ForEachStatement target) {
        return String.format("foreach(%s in %s)%s",
                target.getIteratorDefinition().accept(this),
                target.getTarget().accept(this),
                target.getBody().accept(this));
    }

    @Override
    public String visit(ForStatement target) {
        final Statement ini = target.getInitialization();
        final Expression cond = target.getCondition();
        final Statement iter = target.getIteration();
        return String.format("for(%s;%s;%s)%s",
                ini == null ? "" : ini.accept(this),
                cond == null ? "" : cond.accept(this),
                iter == null ? "" : iter.accept(this),
                target.getBody().accept(this));
    }

    @Override
    public String visit(FunctionCallExpression target) {
        return String.format("%s(%s)",
                target.getFunction().accept(this),
                processArgs(target.getArgs()));
    }

    @Override
    public String visit(FunctionDefinitionStatement target) {
        return String.format("%sfunction %s(%s)%s",
                (target.isLocked() ? "locked " : ""),
                target.getName(),
                processArgDefinition(target.getFunction().getArgs()),
                target.getFunction().getBody().accept(this));
    }

    @Override
    public String visit(IfStatement target) {
        final Statement _else = target.getElseStatement();
        return String.format("if(%s)%s%s",
                target.getCondition().accept(this),
                target.getIfStatement().accept(this),
                _else == null ? "" : String.format(" else %s", _else.accept(this)));
    }

    @Override
    public String visit(InterpolatedStringExpression target) {
        return String.format("`%s`", target.getString());
    }

    @Override
    public String visit(LetExpression target) {
        final Expression value = target.getValue();
        return String.format("let %s%s",
                target.getName(),
                value == null ? "" : String.format("=%s", value.accept(this)));
    }

    @Override
    public String visit(LetStatement target) {
        final boolean _const = target.isConst();
        final Expression value = target.getValue();
        return String.format("let%s %s%s",
                _const ? " const" : "",
                target.getName(),
                value == null ? "" : String.format("=%s", value.accept(this)));
    }

    @Override
    public String visit(MapExpression target) {
        final StringBuilder builder = new StringBuilder();
        for (Map.Entry<Expression, Expression> entry : target.getMap().entrySet())
            builder.append(entry.getKey().accept(this)).append(":").append(entry.getValue().accept(this)).append(",");
        return String.format("{%s}", target.getMap().entrySet().size() > 0 ? builder.deleteCharAt(builder.length() - 1).toString() : "");
    }

    @Override
    public String visit(RequireExpression target) {
        final RequireExpression.RequireMode mode = target.getMode();
        final StringBuilder builder = new StringBuilder("require");
        switch (mode) {
            case LOCAL:
                builder.append(" local");
            case EXTERNAL:
                builder.append(" external");
        }
        builder.append(String.format("(\"%s\")", target.getModuleName()));
        if (target.getVariableName() != null)
            builder.append(String.format("as\"%s\"", target.getVariableName()));
        return builder.toString();
    }

    @Override
    public String visit(ValueCloneExpression valueCloneExpression) {
        return String.format("@%s", valueCloneExpression.getExpression().accept(this));
    }

    @Override
    public String visit(ReturnStatement target) {
        return String.format("return %s", target.getExpression().accept(this));
    }

    private String processCase(SwitchStatement.Case _case) {
        return String.format("case %s:%s ",
                _case.getValue().accept(this),
                _case.getBody().accept(this));
    }

    @Override
    public String visit(SwitchStatement target) {
        final StringBuilder builder = new StringBuilder();
        for (SwitchStatement.Case _case : target.getCases())
            builder.append(processCase(_case));
        return String.format("switch(%s){%s}",
                target.getValue().accept(this),
                builder.toString());
    }

    @Override
    public String visit(TernaryExpression target) {
        return String.format("(%s?%s:%s)",
                target.getCondition().accept(this),
                target.getOnTrue().accept(this),
                target.getOnFalse().accept(this));
    }

    @Override
    public String visit(TypeofExpression target) {
        return String.format("typeof(%s)", target.getExpression().accept(this));
    }

    @Override
    public String visit(UnaryExpression target) {
        return String.format("%s%s",
                target.getOperator().getKey(),
                target.getExpression().accept(this));
    }

    @Override
    public String visit(UnitedStatement target) {
        final StringBuilder builder = new StringBuilder();
        for (Statement statement : target.getStatements())
            builder.append(statement.accept(this)).append(";");
        return builder.toString();
    }

    @Override
    public String visit(ValueExpression target) {
        final Value value = target.getValue();
        if (value instanceof StringValue)
            return String.format("\"%s\"", value.asString());
        if (value instanceof NumberValue) {
            if (value.asNumber() == Double.NaN)
                return "nan";
            return String.format("%s", value.asNumber());
        }
        if (value instanceof BoolValue) {
            if (value.asBoolean())
                return "true";
            return "false";
        }
        return "null";
    }

    @Override
    public String visit(VariableExpression target) {
        return target.getName();
    }

    @Override
    public String visit(WhileStatement target) {
        return String.format("while(%s)%s",
                target.getCondition().accept(this),
                target.getBody().accept(this));
    }

    @Override
    public String visit(NameofExpression nameofExpression) {
        return String.format("nameof(%s)", nameofExpression.getName());
    }

    @Override
    public String visit(TypeExpression typeExpression) {
        return String.format("type(%s)", typeExpression.getType().accept(this));
    }

    @Override
    public String visit(ImportStatement importStatement) {
        return String.format("import %s", importStatement.getPath().accept(this));
    }

    @Override
    public String visit(ThisExpression thisExpression) {
        return "this";
    }

    @Override
    public String visit(NewExpression newExpression) {
        return String.format("new %s", newExpression.getFunctionCall().accept(this));
    }

    @Override
    public String visit(RequireStatement target) {
        return target.getExpression().accept(this);
    }

    @Override
    public String visit(ThrowStatement throwStatement) {
        return String.format("throw %s", throwStatement.getExpression().accept(this));
    }

    @Override
    public String visit(TryCatchFinallyStatement target) {
        return String.format("try %s %s finally %s",
                target.getTry().accept(this),
                target.getCatches().stream().map(this::processCatch).reduce(String::concat),
                target.getFinally().accept(this));
    }

    private String processCatch(TryCatchFinallyStatement.Catch _catch) {
        return String.format("catch(%s)%s%s",
                _catch.getArgName(),
                _catch.getCondition() == null ? "" : String.format("if(%s) ",
                        _catch.getCondition().accept(this)),
                _catch.getBody().accept(this));
    }

    @Override
    public String visit(InstantFunctionExpression target) {
        return String.format("function %s", cutKeyword("block", target.getBody().accept(this)));
    }

    @Override
    public String visit(AnonymousClassExpression anonymousClassExpression) {
        return "<Classes aren't supported yet>";
    }

    @Override
    public String visit(ClassDefinitionStatement classDefinitionStatement) {
        return String.format("class %s", cutKeyword("class", classDefinitionStatement.accept(this)));
    }
}