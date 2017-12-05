package com.evg.ss.ast;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.values.Value;

public final class RequireStatementExpression implements Statement, Expression {

    private String moduleName, variableName = null;
    private boolean external;

    public RequireStatementExpression(String moduleName, boolean external) {
        this.moduleName = moduleName;
        this.external = external;
    }

    public RequireStatementExpression(String moduleName, String variableName, boolean external) {
        this.moduleName = moduleName;
        this.variableName = variableName;
        this.external = external;
    }

    @Override
    public void execute() {
        final Value module = external ? SSModule.requireExternal(moduleName) : SSModule.require(moduleName);
        SS.Variables.put(variableName == null ? moduleName : variableName, module, true);
    }

    @Override
    public Value eval() {
        return external ? SSModule.requireExternal(moduleName) : SSModule.require(moduleName);
    }

    @Override
    public String toString() {
        return String.format("require(%s) as '%s'\n", moduleName, variableName);
    }
}