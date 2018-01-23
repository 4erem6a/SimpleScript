package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidTypeNameException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

public final class ConstTypeExpression implements Expression {

    private Type type;
    private String typename;

    public ConstTypeExpression(String type) {
        this.type = getType(type);
        this.typename = type;
    }

    private static Type getType(String type) {
        switch (type.toLowerCase()) {
            case "function":
                return Type.Function;
            case "map":
                return Type.Map;
            case "number":
                return Type.Number;
            case "array":
                return Type.Array;
            case "string":
                return Type.String;
            case "type":
                return Type.Type;
            case "null":
                return Type.Null;
            case "boolean":
            case "bool":
                return Type.Boolean;
            case "class":
                return Type.Class;
            case "object":
                return Type.Object;
            default:
                throw new InvalidTypeNameException(type);
        }
    }

    public String getTypename() {
        return typename;
    }

    public Type getType() {
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
}