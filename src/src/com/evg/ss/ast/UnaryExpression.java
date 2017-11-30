package com.evg.ss.ast;

import com.evg.ss.exceptions.UnknownOperatorException;
import com.evg.ss.values.BoolValue;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.NumberValue;
import com.evg.ss.values.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 4erem6a
 */
public final class UnaryExpression implements Expression {

    private final UnaryOperations operator;
    private final Expression expression;
    private final String operationKey;

    public UnaryExpression(String operator, Expression expression) {
        if (!OperationsMap.containsKey(operator))
            throw new UnknownOperatorException(operator, UnknownOperatorException.OperatorTypes.Unary);
        this.operator = OperationsMap.get(operator);
        this.expression = expression;
        this.operationKey = operator;
    }

    public enum UnaryOperations {
        UnaryPlus,
        UnaryMinus,
        BitwiseNot,
        LogicalNot
    }

    public static Map<String, UnaryOperations> OperationsMap = new HashMap<>();
    static {
        OperationsMap.put("+", UnaryOperations.UnaryPlus);
        OperationsMap.put("-", UnaryOperations.UnaryMinus);
        OperationsMap.put("~", UnaryOperations.BitwiseNot);
        OperationsMap.put("!", UnaryOperations.LogicalNot);
    }

    @Override
    public Value eval() {
        final Value value = expression.eval();
        switch (operator) {
            case UnaryPlus:
                return value;
            case UnaryMinus:
                return negateValue(value);
            case BitwiseNot:
                return new NumberValue(~value.asNumber().intValue());
            case LogicalNot:
                return new BoolValue(!value.asBoolean());
            default:
                return new NullValue();
        }
    }

    private Value negateValue(Value value) {
        if (value instanceof NumberValue)
            return new NumberValue(-value.asNumber());
        else return new NullValue();
    }

    @Override
    public String toString() {
        return String.format("%s%s", operationKey, expression);
    }
}