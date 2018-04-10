package com.evg.ss.parser.ast;

import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.modules.SSModule;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

public final class RequireExpression extends Expression {

    private String path;

    public RequireExpression(String path) {
        this.path = path;
    }

    @Override
    public Value eval() {
        return SSModule.require(path);
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
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
        return path.hashCode()
                ^ (30 * 16 * 31);
    }
}