package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.Operations;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.ClassValue;
import com.evg.ss.values.Types;
import com.evg.ss.values.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AnonymousClassExpression extends Expression {

    private AnonymousFunctionExpression constructor;
    private List<ASTClassMember> members = new ArrayList<>();
    private Expression base;
    private String name;

    public AnonymousClassExpression(AnonymousFunctionExpression constructor, List<ASTClassMember> members, Expression base) {
        this(constructor, members, base, null);
    }

    public AnonymousClassExpression(AnonymousFunctionExpression constructor, Expression base) {
        this(constructor, new ArrayList<>(), base, null);
    }

    public AnonymousClassExpression(AnonymousFunctionExpression constructor, List<ASTClassMember> members, Expression base, String name) {
        this.constructor = constructor;
        this.members = members;
        this.base = base;
        this.name = name;
    }

    public AnonymousClassExpression(AnonymousFunctionExpression constructor, Expression base, String name) {
        this(constructor, new ArrayList<>(), base, name);
    }

    public String getName() {
        return name;
    }

    @Override
    public Value eval() {
        final Value base;
        if (this.base != null) {
            base = this.base.eval();
            if (base.getType() != Types.Class)
                throw new InvalidValueTypeException(base.getType(), Operations.Class);
        } else base = null;
        return new ClassValue(base == null ? null : ((ClassValue) base),
                constructor == null ? null : constructor.toSSFunction(),
                members.stream().map(ASTClassMember::toClassMember).collect(Collectors.toList()), name);
    }

    public void addMember(ASTClassMember member) {
        this.members.add(member);
    }

    public AnonymousFunctionExpression getConstructor() {
        return constructor;
    }

    public List<ASTClassMember> getMembers() {
        return members;
    }

    public Expression getBase() {
        return base;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }

    public static abstract class ASTClassMember {

        private final boolean isStatic;
        private final Expression key;

        public ASTClassMember(boolean isStatic, Expression key) {
            this.isStatic = isStatic;
            this.key = key;
        }

        public boolean isStatic() {
            return isStatic;
        }

        public Expression getKey() {
            return key;
        }

        public abstract ClassValue.ClassMember toClassMember();
    }

    public static class ASTClassField extends ASTClassMember {

        private final Expression value;

        public ASTClassField(boolean isStatic, Expression key, Expression value) {
            super(isStatic, key);
            this.value = value;
        }

        public Expression getValue() {
            return value;
        }

        @Override
        public ClassValue.ClassMember toClassMember() {
            return new ClassValue.ClassField(isStatic(), getKey().eval(), value.eval());
        }
    }

    public static class ASTClassMethod extends ASTClassMember {

        private final AnonymousFunctionExpression function;

        public ASTClassMethod(boolean isStatic, Expression key, AnonymousFunctionExpression function) {
            super(isStatic, key);
            this.function = function;
        }

        public AnonymousFunctionExpression getFunction() {
            return function;
        }

        @Override
        public ClassValue.ClassMember toClassMember() {
            return new ClassValue.ClassMethod(isStatic(), getKey().eval(), function.toSSFunction());
        }
    }
}