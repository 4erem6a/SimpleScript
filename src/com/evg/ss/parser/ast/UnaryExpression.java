package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidAssignmentTargetException;
import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.*;
import com.evg.ss.values.ClassValue;

/**
 * @author 4erem6a
 */
public final class UnaryExpression extends Expression {

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
            case StaticAccess:
                return _static(value);
            case ClassAccess:
                return _class(value);
            case ConstructorAccess:
                return _new(value);
            default:
                return new NullValue();
        }
    }

    private Value _new(Value value) {
        if (value.getType() != Type.Object)
            throw new InvalidValueTypeException(value.getType());
        final SSFunction ctor = ((ObjectValue) value).getConstructor();
        if (ctor == null)
            return new NullValue();
        return Value.of(ctor);
    }

    private Value _class(Value value) {
        if (value.getType() == Type.Object)
            return ((ObjectValue) value).getSSClass();
        else if (value.getType() == Type.Class)
            return value;
        throw new InvalidValueTypeException(value.getType());
    }

    private Value _static(Value value) {
        if (value.getType() == Type.Class)
            return ((ClassValue) value).getStaticContext();
        throw new InvalidValueTypeException(value.getType());
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

    @Override
    public int hashCode() {
        return expression.hashCode() ^ operator.hashCode() ^ (41 * 5 * 31);
    }

    public enum UnaryOperations {
        UnaryPlus("+"),
        UnaryMinus("-"),
        BitwiseNot("~"),
        LogicalNot("!"),
        PrefixIncrement("++"),
        PrefixDecrement("--"),
        PostfixIncrement("++"),
        PostfixDecrement("--"),
        StaticAccess(" static "),
        ClassAccess(" class "),
        ConstructorAccess(" new ");
        private String key;

        UnaryOperations(String operationKey) {
            this.key = operationKey;
        }

        public String getKey() {
            return key;
        }
    }
}