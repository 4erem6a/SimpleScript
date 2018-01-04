package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.ModuleLoadingException;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class RequireExpression implements Expression {

    private String moduleName, variableName = null;
    private RequireMode mode;

    public RequireExpression(String moduleName, RequireMode mode) {
        this.moduleName = moduleName;
        this.mode = mode;
    }

    public RequireExpression(String moduleName, String variableName, RequireMode mode) {
        this.moduleName = moduleName;
        this.variableName = variableName;
        this.mode = mode;
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

    public String getModuleName() {
        return moduleName;
    }

    public String getVariableName() {
        return variableName;
    }

    public RequireMode getMode() {
        return mode;
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

    @Override
    public int hashCode() {
        return moduleName.hashCode()
                ^ (variableName == null ? 1 : variableName.hashCode())
                ^ mode.hashCode()
                ^ (30 * 16 * 31);
    }

    public enum RequireMode {
        MODULE,
        LOCAL,
        EXTERNAL
    }
}