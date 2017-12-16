package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidAssignmentTargetException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.BoolValue;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.NumberValue;
import com.evg.ss.values.Value;

/**
 * @author 4erem6a
 */
public final class UnaryExpression implements Expression {

    private final UnaryOperations operator;
    private final Expression expression;

    public UnaryExpression(UnaryOperations operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
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
            case PrefixIncrement:
            case PrefixDecrement:
            case PostfixIncrement:
            case PostfixDecrement:
                return changeValue(operator);
            default:
                return new NullValue();
        }
    }

    private Value changeValue(UnaryOperations operation) {
        if (!(expression instanceof Accessible))
            throw new InvalidAssignmentTargetException();
        if (operation == UnaryOperations.PrefixIncrement) {
            ((Accessible) expression).set(new NumberValue(((Accessible) expression).get().asNumber() + 1));
            return ((Accessible) expression).get();
        }
        if (operation == UnaryOperations.PostfixIncrement) {
            final Value value = ((Accessible) expression).get();
            ((Accessible) expression).set(new NumberValue(((Accessible) expression).get().asNumber() + 1));
            return value;
        }
        if (operation == UnaryOperations.PrefixDecrement) {
            ((Accessible) expression).set(new NumberValue(((Accessible) expression).get().asNumber() - 1));
            return ((Accessible) expression).get();
        }
        if (operation == UnaryOperations.PostfixDecrement) {
            final Value value = ((Accessible) expression).get();
            ((Accessible) expression).set(new NumberValue(((Accessible) expression).get().asNumber() - 1));
            return value;
        }
        return new NullValue();
    }

    private Value negateValue(Value value) {
        if (value instanceof NumberValue)
            return new NumberValue(-value.asNumber());
        else return new NullValue();
    }

    @Override
    public String toString() {
        return String.format("%s%s", operator.key, expression);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public UnaryOperations getOperator() {
        return operator;
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Expression getExpression() {
        return expression;
    }

    public enum UnaryOperations {
        UnaryPlus("+"),
        UnaryMinus("-"),
        BitwiseNot("~"),
        LogicalNot("!"),
        PrefixIncrement("++"),
        PrefixDecrement("--"),
        PostfixIncrement("++"),
        PostfixDecrement("--");
        private String key;

        UnaryOperations(String operationKey) {
            this.key = operationKey;
        }

        public String getKey() {
            return key;
        }
    }
}