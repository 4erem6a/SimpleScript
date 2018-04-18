package com.evg.ss.linter.visitors;

import com.evg.ss.exceptions.SSLintException;
import com.evg.ss.lib.Identifier;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.containers.IdentifierMap;
import com.evg.ss.parser.ast.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class IdentifierDeclarationValidator extends LintVisitor {

    private static final Identifier DUMMY_IDENTIFIER = new Identifier(null, false);

    private IdentifierMap identifiers = new IdentifierMap(null);

    private void registerIdentifier(String name, Identifier identifier) {
        if (identifiers.contains(name))
            throw new SSLintException("Identifier '%s' declared multiple times in same scope.", name);
        identifiers.put(name, identifier);
    }

    private void up() {
        identifiers = new IdentifierMap(identifiers);
    }

    private void down() {
        identifiers = identifiers.getParent();
    }

    private void reset() {
        identifiers = new IdentifierMap(null);
    }

    private SS.Scopes lock() {
        SS.Scopes result = new SS.Scopes(identifiers);
        reset();
        return result;
    }

    private void unlock(SS.Scopes scopes) {
        identifiers = scopes.getIdentifiers();
    }

    @Override
    public void visit(LetStatement target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        super.visit(target);
        registerIdentifier(target.getName(), DUMMY_IDENTIFIER);
    }

    @Override
    public void visit(LetExpression target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        super.visit(target);
        registerIdentifier(target.getName(), DUMMY_IDENTIFIER);
    }

    @Override
    public void visit(BlockStatement target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        if (target.isModifierPresent("United")) {
            new UnitedStatement(target.getStatements()).accept(this);
            return;
        }
        if (target.isLocked()) {
            final SS.Scopes scopes = lock();
            target.getStatements().stream()
                    .filter(s -> s instanceof FunctionDefinitionStatement)
                    .forEach(s -> registerIdentifier(((FunctionDefinitionStatement) s).getName(), DUMMY_IDENTIFIER));
            target.getStatements().stream()
                    .filter(s -> !(s instanceof FunctionDefinitionStatement))
                    .forEach(s -> s.accept(this));
            unlock(scopes);
        } else {
            up();
            target.getStatements().stream()
                    .filter(s -> s instanceof FunctionDefinitionStatement)
                    .forEach(s -> registerIdentifier(((FunctionDefinitionStatement) s).getName(), DUMMY_IDENTIFIER));
            target.getStatements().stream()
                    .filter(s -> !(s instanceof FunctionDefinitionStatement))
                    .forEach(s -> s.accept(this));
            down();
        }
    }

    @Override
    public void visit(ForStatement target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        up();
        super.visit(target);
        down();
    }

    @Override
    public void visit(ForEachStatement target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        up();
        super.visit(target);
        down();
    }

    @Override
    public void visit(FunctionDefinitionStatement target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        if (target.isLocked()) {
            final SS.Scopes scopes = lock();
            registerIdentifier(target.getName(), DUMMY_IDENTIFIER);
            final List<String> args = Arrays.stream(target.getFunction().getArgs()).map(ArgumentExpression::getName).collect(Collectors.toList());
            for (String arg : args)
                registerIdentifier(arg, DUMMY_IDENTIFIER);
            super.visit(target);
            unlock(scopes);
        } else {
            registerIdentifier(target.getName(), DUMMY_IDENTIFIER);
            up();
            final List<String> args = Arrays.stream(target.getFunction().getArgs()).map(ArgumentExpression::getName).collect(Collectors.toList());
            for (String arg : args)
                registerIdentifier(arg, DUMMY_IDENTIFIER);
            super.visit(target);
            down();
        }
    }

    @Override
    public void visit(AnonymousFunctionExpression target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        if (target.isLocked()) {
            final SS.Scopes scopes = lock();
            final List<String> args = Arrays.stream(target.getArgs()).map(ArgumentExpression::getName).collect(Collectors.toList());
            for (String arg : args)
                registerIdentifier(arg, DUMMY_IDENTIFIER);
            super.visit(target);
            unlock(scopes);
        } else {
            up();
            final List<String> args = Arrays.stream(target.getArgs()).map(ArgumentExpression::getName).collect(Collectors.toList());
            for (String arg : args)
                registerIdentifier(arg, DUMMY_IDENTIFIER);
            super.visit(target);
            down();
        }
    }

    @Override
    public void visit(VariableExpression target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        super.visit(target);
        if (identifiers.get(target.getName()) == null)
            throw new SSLintException("Identifier '%s' used without being declared.", target.getName());
    }

    @Override
    public void visit(TryCatchFinallyStatement target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        if (target.getTry() != null)
            target.getTry().accept(this);
        for (TryCatchFinallyStatement.Catch _catch : target.getCatches()) {
            up();
            registerIdentifier(_catch.getArgName(), DUMMY_IDENTIFIER);
            if (_catch.getCondition() != null)
                _catch.getCondition().accept(this);
            up();
            _catch.getBody().accept(this);
            down();
            down();
        }
        if (target.getFinally() != null)
            target.getFinally().accept(this);
    }

    @Override
    public void visit(NameofExpression target) {
        if (identifiers.get(target.getName()) == null)
            throw new SSLintException("Identifier '%s' used without being declared.", target.getName());
    }

    @Override
    public void visit(ClassDefinitionStatement target) {
        if (target.isModifierPresent(LINTER_IGNORE))
            return;
        registerIdentifier(target.getName(), DUMMY_IDENTIFIER);
        super.visit(target);
    }
}