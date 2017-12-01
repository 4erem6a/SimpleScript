package com.evg.ss.ast;

import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

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
}