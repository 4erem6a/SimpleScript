package com.evg.ss.lib;

import com.evg.ss.parser.ast.*;
import com.evg.ss.values.NullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AutoClassGenerator {

    private final List<ArgumentExpression> args;
    private final Expression base;
    private final String name;

    public AutoClassGenerator(List<ArgumentExpression> args, Expression base, String name) {
        this.args = args;
        this.base = base;
        this.name = name;
    }

    public AnonymousClassExpression generate() {
        return new AnonymousClassExpression(constructor(), fields(), base, name);
    }

    private AnonymousFunctionExpression constructor() {
        final BlockStatement _body = new BlockStatement();
        for (ArgumentExpression arg : args) {
            _body.addStatement(
                    new ExpressionStatement(
                            new AssignmentExpression(
                                    new ContainerAccessExpression(
                                            new ThisExpression(),
                                            new ValueExpression(arg.getName())
                                    ),
                                    new VariableExpression(arg.getName())
                            )
                    ));
        }
        return new AnonymousFunctionExpression(args.toArray(new ArgumentExpression[0]), _body);
    }

    private List<AnonymousClassExpression.ASTClassMember> fields() {
        final List<AnonymousClassExpression.ASTClassField> fields = new ArrayList<>();
        for (ArgumentExpression arg : args) {
            final Expression value = arg.getDefaultValue();
            fields.add(
                    new AnonymousClassExpression.ASTClassField(
                            false,
                            new ValueExpression(arg.getName()),
                            value == null
                                    ? NullValue.NullExpression
                                    : value));
        }
        return fields.stream()
                .map(AnonymousClassExpression.ASTClassMember.class::cast)
                .collect(Collectors.toList());
    }
}