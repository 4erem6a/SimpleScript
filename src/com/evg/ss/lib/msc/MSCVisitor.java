package com.evg.ss.lib.msc;

import com.evg.ss.lib.Operations;
import com.evg.ss.parser.ast.*;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.values.*;

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
            if (arg.isVariadic())
                argBuilder.append("...");
            if (arg.getDefaultValue() != null)
                argBuilder.append(String.format("=%s", arg.getDefaultValue().accept(this)));
            builder.append(argBuilder.append(',').toString());
        }
        return args.length > 0 ? builder.deleteCharAt(builder.length() - 1).toString() : "";
    }

    private String processArgs(boolean lastArray, Expression... args) {
        final StringBuilder builder = new StringBuilder();
        for (Expression arg : args)
            builder.append(arg.accept(this)).append(",");
        if (args.length > 0)
            builder.deleteCharAt(builder.length() - 1);
        if (lastArray)
            builder.append("...");
        return builder.toString();
    }

    private String processModifiers(Node node) {
        final StringBuilder builder = new StringBuilder();
        for (String mod : node.getModifiers().keySet())
            builder.append("##").append(mod);
        if (node.getModifiers().size() > 0)
            builder.append(" ");
        return builder.toString();
    }

    @Override
    public String visit(AnonymousFunctionExpression target) {
        return processModifiers(target) +
                String.format("%sfunction%s(%s)%s",
                        (target.isLocked() ? "locked " : ""),
                        target.getName() == null ? "" : String.format(" %s", target.getName()),
                        processArgDefinition(target.getArgs()),
                        target.getBody().accept(this));
    }

    @Override
    public String visit(ContainerAccessExpression target) {
        return processModifiers(target) +
                String.format("%s[%s]",
                        target.getTarget().accept(this),
                        target.getKey().accept(this));
    }

    @Override
    public String visit(ArrayExpression target) {
        final StringBuilder builder = new StringBuilder();
        for (Expression expression : target.getExpressions())
            builder.append(expression.accept(this)).append(",");
        if (builder.length() == 0)
            builder.append(',');
        return processModifiers(target) + String.format("[%s]",
                builder.deleteCharAt(builder.length() - 1).toString());
    }

    @Override
    public String visit(AssignmentExpression target) {
        return processModifiers(target) +
                String.format("%s=%s",
                        target.getTarget().accept(this),
                        target.getValue().accept(this));
    }

    @Override
    public String visit(BinaryExpression target) {
        return processModifiers(target) +
                String.format(target.getOperation() == Operations.FieldDeletion
                                ? "(%s%s(%s))"
                                : "(%s%s%s)",
                        target.getLeft().accept(this),
                        target.getOperation().getSpacedKey(),
                        target.getRight().accept(this));
    }

    @Override
    public String visit(BlockStatement target) {
        final StringBuilder builder = new StringBuilder();
        for (Statement statement : target.getStatements())
            builder.append(statement.accept(this)).append(";");
        return processModifiers(target) +
                String.format("%sblock{%s}",
                        (target.isLocked() ? "locked " : ""),
                        builder.toString());
    }

    @Override
    public String visit(BreakStatement target) {
        return processModifiers(target) + "break";
    }

    @Override
    public String visit(ConstTypeExpression target) {
        return processModifiers(target) + target.getTypename();
    }

    @Override
    public String visit(ContinueStatement target) {
        return processModifiers(target) + "continue";
    }

    @Override
    public String visit(DoWhileStatement target) {
        return processModifiers(target) +
                String.format("do %s while(%s)",
                        target.getBody().accept(this),
                        target.getCondition().accept(this));
    }

    @Override
    public String visit(ExportsStatement target) {
        return processModifiers(target) +
                String.format("exports %s",
                        target.getExpression().accept(this));
    }

    @Override
    public String visit(ExpressionStatement target) {
        return processModifiers(target) +
                String.format(">%s",
                        target.getExpression().accept(this));
    }

    @Override
    public String visit(ForEachStatement target) {
        return processModifiers(target) +
                String.format("foreach(%s in %s)%s",
                        target.getIteratorDefinition().accept(this),
                        target.getTarget().accept(this),
                        target.getBody().accept(this));
    }

    @Override
    public String visit(ForStatement target) {
        final Statement ini = target.getInitialization();
        final Expression cond = target.getCondition();
        final Statement iter = target.getIteration();
        return processModifiers(target) +
                String.format("for(%s;%s;%s)%s",
                        ini == null ? "" : ini.accept(this),
                        cond == null ? "" : cond.accept(this),
                        iter == null ? "" : iter.accept(this),
                        target.getBody().accept(this));
    }

    @Override
    public String visit(CallExpression target) {
        return processModifiers(target) +
                String.format("%s(%s)",
                        target.getValue().accept(this),
                        processArgs(target.isLastArray(), target.getArgs()));
    }

    @Override
    public String visit(FunctionDefinitionStatement target) {
        return processModifiers(target) +
                String.format("%sfunction %s(%s)%s",
                        (target.isLocked() ? "locked " : ""),
                        target.getName(),
                        processArgDefinition(target.getFunction().getArgs()),
                        target.getFunction().getBody().accept(this));
    }

    @Override
    public String visit(IfStatement target) {
        final Statement _else = target.getElseStatement();
        return processModifiers(target) +
                String.format("if(%s)%s%s",
                        target.getCondition().accept(this),
                        target.getIfStatement().accept(this),
                        _else == null ? "" : String.format(" else %s", _else.accept(this)));
    }

    @Override
    public String visit(InterpolatedStringExpression target) {
        return processModifiers(target) +
                String.format("`%s`", target.getString());
    }

    @Override
    public String visit(LetExpression target) {
        final Expression value = target.getValue();
        return processModifiers(target) +
                String.format("let %s%s",
                        target.getName(),
                        value == null ? "" : String.format("=%s", value.accept(this)));
    }

    @Override
    public String visit(LetStatement target) {
        final boolean _const = target.isConst();
        final Expression value = target.getValue();
        return processModifiers(target) +
                String.format("let%s %s%s",
                        _const ? " const" : "",
                        target.getName(),
                        value == null ? "" : String.format("=%s", value.accept(this)));
    }

    @Override
    public String visit(MapExpression target) {
        final StringBuilder builder = new StringBuilder();
        for (Map.Entry<Expression, Expression> entry : target.getMap().entrySet())
            builder.append(entry.getKey().accept(this))
                    .append(":")
                    .append(entry.getValue().accept(this))
                    .append(",");
        return processModifiers(target) +
                String.format("{%s}",
                        target.getMap().entrySet().size() > 0
                                ? builder.deleteCharAt(builder.length() - 1).toString()
                                : "");
    }

    @Override
    public String visit(RequireExpression target) {
        return processModifiers(target) +
                String.format("require(\"%s\")",
                        target.getPath());
    }

    @Override
    public String visit(ReturnStatement target) {
        return processModifiers(target) +
                String.format("return %s",
                        target.getExpression().accept(this));
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
        return processModifiers(target) +
                String.format("switch(%s){%s}",
                        target.getValue().accept(this),
                        builder.toString());
    }

    @Override
    public String visit(TernaryExpression target) {
        return processModifiers(target) +
                String.format("(%s?%s:%s)",
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
        switch (target.getOperation()) {
            case UnaryPlus:
            case UnaryMinus:
            case BitwiseNot:
            case BooleanNot:
            case PrefixIncrement:
            case PrefixDecrement:
            case ValueClone:
                return processModifiers(target) +
                        String.format("%s%s",
                                target.getOperation().getSpacedKey(),
                                target.getExpression().accept(this));
            case PostfixIncrement:
            case PostfixDecrement:
            case StaticAccess:
            case ClassAccess:
            case ConstructorAccess:
            case PrototypeAccess:
                return processModifiers(target) +
                        String.format("%s%s",
                                target.getExpression().accept(this),
                                target.getOperation().getKey());
        }
        return processModifiers(target) +
                String.format("%s%s",
                        target.getOperation().getSpacedKey(),
                        target.getExpression().accept(this));
    }

    @Override
    public String visit(UnitedStatement target) {
        if (!target.isModifierPresent("United"))
            target.modify("United");
        final StringBuilder builder = new StringBuilder();
        for (Statement statement : target.getStatements())
            builder.append(statement.accept(this)).append(";");
        return processModifiers(target) + String.format("block{%s}", builder.toString());
    }

    @Override
    public String visit(ValueExpression target) {
        final String mods = processModifiers(target);
        final Value value = target.getValue();
        if (value instanceof StringValue)
            return mods + String.format("\"%s\"", value.asString());
        if (value instanceof NumberValue) {
            if (value.asNumber() == Double.NaN)
                return mods + "nan";
            return mods + String.format("%s", value.asNumber());
        }
        if (value instanceof BoolValue) {
            if (value.asBoolean())
                return mods + "true";
            return mods + "false";
        }
        if (value instanceof NullValue)
            return mods + "null";
        return mods + "undefined";
    }

    @Override
    public String visit(VariableExpression target) {
        return processModifiers(target) + target.getName();
    }

    @Override
    public String visit(WhileStatement target) {
        return processModifiers(target) +
                String.format("while(%s)%s",
                        target.getCondition().accept(this),
                        target.getBody().accept(this));
    }

    @Override
    public String visit(NameofExpression target) {
        return processModifiers(target) +
                String.format("nameof(%s)",
                        target.getName());
    }

    @Override
    public String visit(TypeExpression target) {
        return processModifiers(target) +
                String.format("type(%s)",
                        target.getType().accept(this));
    }

    @Override
    public String visit(ThisExpression target) {
        return processModifiers(target) + "this";
    }

    public String visit(ThatExpression target) {
        return processModifiers(target) + "that";
    }

    @Override
    public String visit(CatchExpression target) {
        String result = String.format("%s.catch",
                target.getExpression().accept(this));
        if (target.getHandler() != null)
            result += String.format("(%s)", target.getHandler().accept(this));
        return result;
    }

    @Override
    public String visit(NewExpression target) {
        return processModifiers(target) +
                String.format("new %s",
                        target.getFunctionCall().accept(this));
    }

    @Override
    public String visit(ThrowStatement target) {
        return processModifiers(target) +
                String.format("throw %s",
                        target.getExpression().accept(this));
    }

    @Override
    public String visit(TryCatchFinallyStatement target) {
        return processModifiers(target) +
                String.format("try %s %s finally %s",
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
        return processModifiers(target) +
                String.format("function %s",
                        cutKeyword("block", target.getBody().accept(this)));
    }

    @Override
    public String visit(AnonymousClassExpression target) {
        final StringBuilder builder = new StringBuilder(
                String.format("class%s", target.getName() == null ? "" : " " + target.getName()));
        if (target.getBase() != null)
            builder.append(" extends ")
                    .append(target.getBase().accept(this));
        builder.append("{");
        if (target.getConstructor() != null)
            builder.append("new(")
                    .append(processArgDefinition(target.getConstructor().getArgs()))
                    .append(")")
                    .append(target.getConstructor().getBody().accept(this))
                    .append(";");
        for (AnonymousClassExpression.ASTClassMember member : target.getMembers())
            builder.append(processClassMember(member));
        return processModifiers(target) + builder.append("}").toString();
    }

    @Override
    public String visit(ClassDefinitionStatement target) {
        return processModifiers(target) +
                String.format("class %s %s",
                        target.getName(),
                        cutKeyword("class", target.getClassExpression().accept(this)));
    }

    private String processClassMember(AnonymousClassExpression.ASTClassMember member) {
        final StringBuilder builder = new StringBuilder();
        if (member.isStatic())
            builder.append("static ");
        if (member instanceof AnonymousClassExpression.ASTClassMethod)
            if (((AnonymousClassExpression.ASTClassMethod) member).getFunction().isLocked())
                builder.append("locked ");
        builder.append(member.getKey().accept(this));
        if (member instanceof AnonymousClassExpression.ASTClassMethod)
            builder.append("(")
                    .append(processArgDefinition(((AnonymousClassExpression.ASTClassMethod) member).getFunction().getArgs()))
                    .append(")")
                    .append(((AnonymousClassExpression.ASTClassMethod) member).getFunction().getBody().accept(this));
        if (member instanceof AnonymousClassExpression.ASTClassField)
            if (((AnonymousClassExpression.ASTClassField) member).getValue() != null)
                builder.append("=")
                        .append(((AnonymousClassExpression.ASTClassField) member).getValue().accept(this));
        return builder.append(";").toString();
    }
}