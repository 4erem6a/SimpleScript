package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidAssignmentTargetException;
import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.Operations;
import com.evg.ss.lib.SSFunction;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.*;
import com.evg.ss.values.ClassValue;

/**
 * @author 4erem6a
 */
public final class UnaryExpression extends Expression implements Accessible {

    private final Operations operation;
    private final Expression expression;

    public UnaryExpression(Operations operation, Expression expression) {
        this.operation = operation;
        this.expression = expression;
    }

    @Override
    public Value eval() {
        final Value value = expression.eval();
        switch (operation) {
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
                return changeValue(operation);
            case StaticAccess:
                return _static(value);
            case ClassAccess:
                return _class(value);
            case ConstructorAccess:
                return _new(value);
            case PrototypeAccess:
                return _super(value);
            case ValueClone:
                return value.clone();
            default:
                return new NullValue();
        }
    }

    private Value _super(Value value) {
        if (value.getType() == Types.Class) {
            final ClassValue base = ((ClassValue) value).getBase();
            return base == null ? new UndefinedValue() : base;
        }
        if (value.getType() == Types.Map) {
            final Value prototype = ((MapValue) value).getPrototype();
            return prototype == null ? new UndefinedValue() : prototype;
        }
        throw new InvalidValueTypeException(value.getType(), Operations.PrototypeAccess);
    }

    private Value _new(Value value) {
        if (value instanceof ObjectValue)
            throw new InvalidValueTypeException(value.getType(), Operations.ConstructorAccess);
        final SSFunction ctor = ((ObjectValue) value).getConstructor();
        if (ctor == null)
            return new NullValue();
        return Value.of(ctor);
    }

    private Value _class(Value value) {
        if (value.getType() == Types.Object)
            return ((ObjectValue) value).getSSClass();
        else if (value.getType() == Types.Class)
            return value;
        throw new InvalidValueTypeException(value.getType(), Operations.ClassAccess);
    }

    private Value _static(Value value) {
        if (value.getType() == Types.Class)
            return ((ClassValue) value).getStaticContext();
        throw new InvalidValueTypeException(value.getType(), Operations.StaticAccess);
    }

    private Value changeValue(Operations operation) {
        if (!(expression instanceof Accessible))
            throw new InvalidAssignmentTargetException(expression);
        if (operation == Operations.PrefixIncrement) {
            ((Accessible) expression).set(new NumberValue(((Accessible) expression).get().asNumber() + 1));
            return ((Accessible) expression).get();
        }
        if (operation == Operations.PostfixIncrement) {
            final Value value = ((Accessible) expression).get();
            ((Accessible) expression).set(new NumberValue(((Accessible) expression).get().asNumber() + 1));
            return value;
        }
        if (operation == Operations.PrefixDecrement) {
            ((Accessible) expression).set(new NumberValue(((Accessible) expression).get().asNumber() - 1));
            return ((Accessible) expression).get();
        }
        if (operation == Operations.PostfixDecrement) {
            final Value value = ((Accessible) expression).get();
            ((Accessible) expression).set(new NumberValue(((Accessible) expression).get().asNumber() - 1));
            return value;
        }
        return new UndefinedValue();
    }

    private Value negateValue(Value value) {
        if (value instanceof NumberValue)
            return new NumberValue(-value.asNumber());
        else return new UndefinedValue();
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Operations getOperation() {
        return operation;
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
        return expression.hashCode() ^ operation.hashCode() ^ (41 * 5 * 31);
    }

    @Override
    public Value get() {
        return eval();
    }

    @Override
    public Value set(Value value) {
        if (operation != Operations.PrototypeAccess)
            throw new InvalidAssignmentTargetException(this);
        final Value target = expression.eval();
        if (target.getType() != Types.Map)
            throw new InvalidValueTypeException(target.getType(), Operations.PrototypeAccess);
        if (value.getType() != Types.Map)
            throw new InvalidValueTypeException(value.getType());
        ((MapValue) target).setPrototype((MapValue) value);
        return value;
    }
}