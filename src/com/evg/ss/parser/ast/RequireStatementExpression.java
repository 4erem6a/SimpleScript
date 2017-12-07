package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.ModuleLoadingException;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class RequireStatementExpression implements Statement, Expression {

    private String moduleName, variableName = null;
    private RequireMode mode;

    public RequireStatementExpression(String moduleName, RequireMode mode) {
        this.moduleName = moduleName;
        this.mode = mode;
    }

    public RequireStatementExpression(String moduleName, String variableName, RequireMode mode) {
        this.moduleName = moduleName;
        this.variableName = variableName;
        this.mode = mode;
    }

    @Override
    public void execute() {
        final Value module = eval();
        SS.Variables.put(variableName == null ? moduleName : variableName, module, true);
    }

    @Override
    public Value eval() {
        final Value module;
        if (mode == RequireMode.MODULE)
            module = SSModule.require(moduleName);
        else if (mode == RequireMode.EXTERNAL)
            module = SSModule.requireExternal(moduleName);
        else if (mode == RequireMode.LOCAL)
            module = SSModule.requireLocal(moduleName);
        else throw new ModuleLoadingException(moduleName);
        return module;
    }

    @Override
    public String toString() {
        return String.format("require(%s) as '%s'\n", moduleName, variableName);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public enum RequireMode {
        MODULE,
        LOCAL,
        EXTERNAL
    }
}