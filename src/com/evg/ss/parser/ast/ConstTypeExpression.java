package com.evg.ss.parser.ast;

import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Types;
import com.evg.ss.values.Value;

public final class ConstTypeExpression extends Expression {

    private Types type;
    private String typename;

    public ConstTypeExpression(String type) {
        this.type = getType(type);
        this.typename = type;
    }

    private static Types getType(String type) {
        switch (type.toLowerCase()) {
            case "function":
                return Types.Function;
            case "map":
                return Types.Map;
            case "number":
                return Types.Number;
            case "array":
                return Types.Array;
            case "string":
                return Types.String;
            case "type":
                return Types.Type;
            case "null":
                return Types.Null;
            case "boolean":
            case "bool":
                return Types.Boolean;
            case "class":
                return Types.Class;
            case "object":
                return Types.Object;
            default:
                return Types.Undefined;
        }
    }

    public String getTypename() {
        return typename;
    }

    public Types getType() {
        return type;
    }

    @Override
    public Value eval() {
        return type.getTypeValue();
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
        return type.hashCode() ^ typename.hashCode() ^ (8 * 38 * 31);
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }
}