package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.ClassValue;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AnonymousClassExpression implements Expression {

    private AnonymousFunctionExpression constructor;
    private List<ASTClassMember> members = new ArrayList<>();
    private Expression base;

    public AnonymousClassExpression(AnonymousFunctionExpression constructor, List<ASTClassMember> members, Expression base) {
        this.constructor = constructor;
        this.members = members;
        this.base = base;
    }

    public AnonymousClassExpression(AnonymousFunctionExpression constructor, Expression base) {
        this(constructor, new ArrayList<>(), base);
    }

    @Override
    public Value eval() {
        final Value base;
        if (this.base != null) {
            base = this.base.eval();
            if (base.getType() != Type.Class)
                throw new InvalidValueTypeException(base.getType());
        } else base = null;
        return new ClassValue(base == null ? null : ((ClassValue) base),
                constructor.toSSFunction(),
                members.stream().map(ASTClassMember::toClassMember).collect(Collectors.toList()));
    }

    public void addMember(ASTClassMember member) {
        this.members.add(member);
    }

    public static abstract class ASTClassMember {

        private final boolean isStatic;
        private final String name;

        public ASTClassMember(boolean isStatic, String name) {
            this.isStatic = isStatic;
            this.name = name;
        }

        public boolean isStatic() {
            return isStatic;
        }

        public String getName() {
            return name;
        }

        public abstract ClassValue.ClassMember toClassMember();
    }

    public static class ASTClassField extends ASTClassMember {

        private final Expression value;

        public ASTClassField(boolean isStatic, String name, Expression value) {
            super(isStatic, name);
            this.value = value;
        }

        public Expression getValue() {
            return value;
        }

        @Override
        public ClassValue.ClassMember toClassMember() {
            return new ClassValue.ClassField(isStatic(), getName(), value.eval());
        }
    }

    public static class ASTClassMethod extends ASTClassMember {

        private final AnonymousFunctionExpression function;

        public ASTClassMethod(boolean isStatic, String name, AnonymousFunctionExpression function) {
            super(isStatic, name);
            this.function = function;
        }

        public AnonymousFunctionExpression getFunction() {
            return function;
        }

        @Override
        public ClassValue.ClassMember toClassMember() {
            return new ClassValue.ClassMethod(isStatic(), getName(), function.toSSFunction());
        }
    }

    @Override
    public void accept(Visitor visitor) {

    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return null;
    }

}