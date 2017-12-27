package com.evg.ss.linter.visitors;

import com.evg.ss.lib.Function;
import com.evg.ss.lib.Variable;
import com.evg.ss.lib.containers.FunctionMap;
import com.evg.ss.lib.containers.VariableMap;
import com.evg.ss.linter.LintException;
import com.evg.ss.parser.ast.*;
import com.evg.ss.parser.visitors.AbstractVisitor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
//Fixme 21.12.2017:
public final class VariableUsedWithoutBeingDeclaredValidator extends AbstractVisitor {

    private static final Variable DUMMY_VARIABLE = new Variable(null, false);
    private static final Function DUMMY_FUNCTION = (args) -> null;

    private VariableMap variables = new VariableMap(null);
    private FunctionMap functions = new FunctionMap(null);

    private void up() {
        variables = new VariableMap(variables);
        functions = new FunctionMap(functions);
    }

    private void down() {
        variables = variables.getParent();
        functions = functions.getParent();
    }

    @Override
    public void visit(LetStatement target) {
        super.visit(target);
        variables.put(target.getName(), DUMMY_VARIABLE);
    }

    @Override
    public void visit(LetExpression target) {
        super.visit(target);
        variables.put(target.getName(), DUMMY_VARIABLE);
    }

    @Override
    public void visit(BlockStatement target) {
        up();
        super.visit(target);
        down();
    }

    @Override
    public void visit(ForStatement target) {
        up();
        final Statement ini = target.getInitialization();
        if (ini instanceof LetStatement)
            variables.put(((LetStatement) ini).getName(), DUMMY_VARIABLE);
        else if (ini instanceof UnitedStatement) {
            for (Statement let : ((UnitedStatement) ini).getStatements())
                variables.put(((LetStatement) let).getName(), DUMMY_VARIABLE);
        }
        super.visit(target);
        down();
    }

    @Override
    public void visit(ForEachStatement target) {
        up();
        final LetStatement let = (LetStatement) target.getIteratorDefinition();
        variables.put(let.getName(), DUMMY_VARIABLE);
        super.visit(target);
        down();
    }

    @Override
    public void visit(FunctionDefinitionStatement target) {
        functions.put(target.getName(), DUMMY_FUNCTION);
        up();
        final List<String> args = Arrays.stream(target.getArgs()).map(ArgumentExpression::getName).collect(Collectors.toList());
        for (String arg : args)
            variables.put(arg, DUMMY_VARIABLE);
        super.visit(target);
        down();
    }

    @Override
    public void visit(AnonymousFunctionExpression target) {
        up();
        final List<String> args = Arrays.stream(target.getArgs()).map(ArgumentExpression::getName).collect(Collectors.toList());
        for (String arg : args)
            variables.put(arg, DUMMY_VARIABLE);
        super.visit(target);
        down();
    }

    @Override
    public void visit(RequireStatement target) {
        final String varName = target.getExpression().getVariableName() == null
                ? target.getExpression().getModuleName()
                : target.getExpression().getVariableName();
        variables.put(varName, DUMMY_VARIABLE);
        super.visit(target);
    }

    @Override
    public void visit(ImportStatement target) {
        super.visit(target);
        final String name = ((ValueExpression) ((ContainerAccessExpression) target.getPath()).getKey()).getValue().asString();
        variables.put(name, DUMMY_VARIABLE);
    }

    @Override
    public void visit(FunctionCallExpression target) {
        if (target.getFunction() instanceof VariableExpression) {
            final String name = ((VariableExpression) target.getFunction()).getName();
            if (functions.get(name) == null)
                super.visit(target);
        } else super.visit(target);
    }

    @Override
    public void visit(VariableExpression target) throws LintException {
        super.visit(target);
        if (variables.get(target.getName()) == null)
            throw new LintException("LintException: Variable %s used without being declared.", target.getName());
    }
}