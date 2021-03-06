package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.Converter;
import com.evg.ss.lib.MapMatcher;
import com.evg.ss.lib.Operations;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.*;
import com.evg.ss.values.ClassValue;
import javafx.util.Pair;

/**
 * @author 4erem6a
 */
public final class BinaryExpression extends Expression {

    private final Expression left, right;
    private final Operations operation;

    public BinaryExpression(Operations operation, Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        switch (operation) {
            case Addition:
                return add(left.eval(), right.eval());
            case Subtraction:
                return subtract(left.eval(), right.eval());
            case Division:
                return divide(left.eval(), right.eval());
            case Multiplication:
                return multiply(left.eval(), right.eval());
            case Modulus:
                return mod(left.eval(), right.eval());
            case BitwiseOr:
                return new NumberValue(left.eval().asNumber().intValue() | right.eval().asNumber().intValue());
            case BitwiseAnd:
                return new NumberValue(left.eval().asNumber().intValue() ^ right.eval().asNumber().intValue());
            case BitwiseXor:
                return new NumberValue(left.eval().asNumber().intValue() ^ right.eval().asNumber().intValue());
            case LShift:
                return new NumberValue(left.eval().asNumber().intValue() << right.eval().asNumber().intValue());
            case RShift:
                return new NumberValue(left.eval().asNumber().intValue() >> right.eval().asNumber().intValue());
            case URShift:
                return new NumberValue(left.eval().asNumber().intValue() >>> right.eval().asNumber().intValue());
            case BooleanOr:
                return new BoolValue(left.eval().asBoolean() || right.eval().asBoolean());
            case BooleanAnd:
                return new BoolValue(left.eval().asBoolean() && right.eval().asBoolean());
            case LessThen:
                return new BoolValue(left.eval().compareTo(right.eval()) < 0);
            case GreaterThen:
                return new BoolValue(left.eval().compareTo(right.eval()) > 0);
            case GreaterThenOrEquals:
                return new BoolValue(left.eval().compareTo(right.eval()) >= 0);
            case LessThenOrEquals:
                return new BoolValue(left.eval().compareTo(right.eval()) <= 0);
            case Equals:
                return new BoolValue(left.eval().compareTo(right.eval()) == 0);
            case NotEquals:
                return new BoolValue(left.eval().compareTo(right.eval()) != 0);
            case Compare:
                return Value.of(left.eval().compareTo(right.eval()));
            case Is:
                return is(left.eval(), right.eval());
            case As:
                return as(left.eval(), right.eval());
            case FieldDeletion:
                return delete(left.eval(), right.eval());
            default:
                return new NullValue();
        }
    }

    private Value delete(Value leftValue, Value rightValue) {
        if (leftValue.getType() != Types.Map)
            throw new InvalidValueTypeException(leftValue.getType(), Operations.FieldDeletion);
        if (((MapValue) leftValue).containsKey(left.eval()))
            ((MapValue) leftValue).getMapReference().remove(left.eval());
        return leftValue;
    }

    private Value divide(Value leftValue, Value rightValue) {
        if (leftValue instanceof NullValue || rightValue instanceof NullValue)
            return new NullValue();
        if (leftValue instanceof StringValue) {
            final String leftString = leftValue.asString();
            if (rightValue instanceof StringValue) {
                return divideString(leftString, rightValue.asString());
            } else if (rightValue instanceof NumberValue || rightValue instanceof BoolValue) {
                return divideString(leftString, rightValue.asNumber().intValue());
            } else return new NullValue();
        } else if (leftValue instanceof NumberValue || leftValue instanceof BoolValue) {
            final double leftNumber = leftValue.asNumber();
            if (rightValue instanceof StringValue) {
                return new NullValue();
            } else if (rightValue instanceof NumberValue || rightValue instanceof BoolValue) {
                final double rightNumber = rightValue.asNumber();
                if (leftValue instanceof NumberValue)
                    return new NumberValue(leftNumber / rightNumber);
                else return new BoolValue(new NumberValue(leftNumber / rightNumber).asBoolean());
            } else return new NullValue();
        } else return new NullValue();
    }

    private Value divideString(String string, String divisor) {
        boolean flag = true;
        while (flag) {
            Pair<Boolean, Pair<String, String>> result = stringDivisionCycle(string, divisor);
            string = result.getValue().getKey();
            divisor = result.getValue().getValue();
            flag = result.getKey();
        }
        return multiplyString(string, divisor);
    }

    private Pair<Boolean, Pair<String, String>> stringDivisionCycle(String string, String divisor) {
        boolean flag = false;
        for (int i = 0; i < string.length(); i++) {
            final char current = string.charAt(i);
            if (divisor.indexOf(current) != -1) {
                final int index = divisor.indexOf(current);
                divisor = divisor.substring(0, index) + divisor.substring(index + 1);
                string = string.substring(0, i) + string.substring(i + 1);
                flag = true;
            }
        }
        return new Pair<>(flag, new Pair<>(string, divisor));
    }

    private Value divideString(String string, int divisor) {
        if (divisor > 0) {
            return new StringValue(string.substring(0, string.length() / divisor));
        } else if (divisor < 0) {
            return new StringValue(string.substring(string.length() - string.length() / Math.abs(divisor)));
        } else return new StringValue(string);
    }

    private Value multiply(Value leftValue, Value rightValue) {
        if (leftValue instanceof NullValue || rightValue instanceof NullValue)
            return new NullValue();
        if (leftValue instanceof StringValue) {
            final String leftString = leftValue.asString();
            if (rightValue instanceof NumberValue || rightValue instanceof BoolValue) {
                final int multiplier = rightValue.asNumber().intValue();
                return multiplyString(leftString, multiplier);
            } else if (rightValue instanceof StringValue) {
                return multiplyString(leftString, rightValue.asString());
            } else return new NullValue();
        } else if (leftValue instanceof NumberValue || leftValue instanceof BoolValue) {
            final double leftNumber = leftValue.asNumber();
            if (rightValue instanceof NumberValue || rightValue instanceof BoolValue) {
                final double rightNumber = rightValue.asNumber();
                return new NumberValue(leftNumber * rightNumber);
            } else if (rightValue instanceof StringValue) {
                final String rightString = rightValue.asString();
                return multiplyString(rightString, (int) leftNumber);
            } else return new NullValue();
        } else return new NullValue();
    }

    private Value multiplyString(String string, String multiplier) {
        final StringBuilder builder = new StringBuilder();
        final int max = string.length() > multiplier.length() ? multiplier.length() : string.length();
        boolean flag = true;
        for (int i = 0; i < max; i++) {
            builder.append(string.charAt(i));
            builder.append(multiplier.charAt(i));
            flag = !flag;
        }
        if (max < string.length())
            builder.append(string.substring(max));
        else if (max < multiplier.length())
            builder.append(multiplier.substring(max));
        return new StringValue(builder.toString());
    }

    private Value multiplyString(String string, int multiplier) {
        if (multiplier > 0) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < multiplier; i++)
                builder.append(string);
            return new StringValue(builder.toString());
        } else if (multiplier == 0)
            return new StringValue("");
        else return new StringValue(string);
    }

    private Value mod(Value left, Value right) {
        if (!(left instanceof NumberValue) || !(right instanceof NumberValue))
            return new NullValue();
        return new NumberValue(left.asNumber() % right.asNumber());
    }

    private Value subtract(Value leftValue, Value rightValue) {
        if (leftValue instanceof NullValue || rightValue instanceof NullValue)
            return new NullValue();
        if (leftValue instanceof StringValue) {
            final String leftString = leftValue.asString();
            if (rightValue instanceof StringValue) {
                final String rightString = rightValue.asString();
                if (leftString.length() > rightString.length())
                    return new StringValue(leftString.substring(0, leftString.length() - rightString.length()));
                else return new StringValue(leftString);
            } else {
                final double rightDouble = rightValue.asNumber();
                if (leftString.length() > rightDouble && rightDouble > 0)
                    return new StringValue(leftString.substring(0, leftString.length() - (int) rightDouble));
                else return new StringValue(leftString);
            }
        } else if (leftValue instanceof BoolValue)
            return new BoolValue(new NumberValue(leftValue.asNumber() - leftValue.asNumber()).asBoolean());
        else if (leftValue instanceof NumberValue)
            return new NumberValue(leftValue.asNumber() - rightValue.asNumber());
        else return new NullValue();
    }

    private Value add(Value leftValue, Value rightValue) {
        if (leftValue instanceof NullValue || rightValue instanceof NullValue)
            return new NullValue();
        if (leftValue instanceof StringValue)
            return new StringValue(leftValue.asString() + rightValue.asString());
        else if (leftValue instanceof BoolValue)
            return new BoolValue(new NumberValue(leftValue.asNumber() + leftValue.asNumber()).asBoolean());
        else if (leftValue instanceof NumberValue)
            return new NumberValue(leftValue.asNumber() + rightValue.asNumber());
        else return new NullValue();
    }

    private Value is(Value left, Value right) {
        if (right instanceof TypeValue)
            if (((TypeValue) right).getValue() == Types.Object)
                return Value.of(left instanceof ObjectValue);
            else return getBinaryComparison(Value.of(left.getType()), Value.of(((TypeValue) right).getValue()));
        else if (left instanceof MapValue && right instanceof MapValue)
            return Value.of(new MapMatcher((MapValue) left).match((MapValue) right));
        else if (left instanceof MapValue && right instanceof ArrayValue)
            return getBinaryComparison(((MapValue) left).toArray(), right);
        else if (left instanceof ArrayValue && right instanceof MapValue)
            return getBinaryComparison(left, ((MapValue) right).toArray());
        else if (left instanceof ObjectValue && right instanceof ClassValue)
            return Value.of(((ObjectValue) left).isInstanceOfClass(((ClassValue) right)));
        else if (left instanceof ClassValue && right instanceof ClassValue)
            return Value.of(((ClassValue) left).is(((ClassValue) right)));
        else return getBinaryComparison(left, right);
    }

    private Value getBinaryComparison(Value left, Value right) {
        return new BinaryExpression(Operations.Equals, new ValueExpression(left),
                new ValueExpression(right)).eval();
    }

    private Value as(Value left, Value right) {
        final Types targetType;
        if (right instanceof TypeValue)
            targetType = ((TypeValue) right).getValue();
        else targetType = right.getType();
        return new Converter(left.getType(), targetType).convert(left);
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public Operations getOperation() {
        return operation;
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode() ^ operation.hashCode() ^ (5 * 41 * 31);
    }
}