package com.evg.ss.parser.ast;

import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class RequireStatement implements Statement {

    private RequireExpression require;

    public RequireStatement(String moduleName, RequireExpression.RequireMode mode) {
        this.require = new RequireExpression(moduleName, mode);
    }

    public RequireStatement(String moduleName, String variableName, RequireExpression.RequireMode mode) {
        this.require = new RequireExpression(moduleName, variableName, mode);
    }

    @Override
    public void execute() {
        final Value module = require.eval();
        SS.Variables.put(require.getVariableName() == null ? require.getModuleName() : require.getVariableName(), module, true);
    }

    public RequireExpression getExpression() {
        return require;
    }

    @Override
    public String toString() {
        return require.toString();
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
        return require.hashCode() ^ (31 * 15 * 31);
    }
}