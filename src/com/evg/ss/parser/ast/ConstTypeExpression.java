package com.evg.ss.parser.ast;

import com.evg.ss.values.Type;
import com.evg.ss.values.Value;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class ConstTypeExpression implements Expression {

    private Type type;

    public ConstTypeExpression(String type) {
        this.type = getType(type);
    }

    @Override
    public Value eval() {
        return type.getTypeValue();
    }

    private static Type getType(String type) {
        switch (type.toLowerCase()) {
            case "function":
                return Type.Function;
            case "object":
                return Type.Object;
            case "number":
                return Type.Number;
            case "array":
                return Type.Array;
            case "string":
                return Type.String;
            case "type":
                return Type.Type;
            case "null":
                return Type.String;
            case "boolean":
            case "bool":
                return Type.Boolean;
                default:
                    return Type.Null;
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }
}