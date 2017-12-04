package com.evg.ss.ast;

import com.evg.ss.exceptions.UnknownOperatorException;
import com.evg.ss.values.*;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 4erem6a
 */
public final class BinaryExpression implements Expression {

    private final Expression left, right;
    private final BinaryOperations operation;

    public BinaryExpression(BinaryOperations operation, Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    public enum BinaryOperations {
        Addition("+"),
        Subtraction("-"),
        Division("/"),
        Multiplication("*"),
        Modulo("%"),

        BitwiseOr("|"),
        BitwiseAnd("&"),
        BitwiseXor("^"),

        LogicalOr("||"),
        LogicalAnd("&&"),

        LessThen("<"),
        GreaterThen(">"),
        GreaterThenOrEquals(">="),
        LessThenOrEquals("<="),
        Equals("=="),
        NotEquals("!=");
        public String key;
        BinaryOperations(String operationKey) {
            this.key = operationKey;
        }
    }

    @Override
    public Value eval() {
        final Value leftValue = left.eval();
        final Value rightValue = right.eval();
        switch (operation) {
            case Addition:
                return add(leftValue, rightValue);
            case Subtraction:
                return subtract(leftValue, rightValue);
            case Division:
                return divide(leftValue, rightValue);
            case Multiplication:
                return multiply(leftValue, rightValue);
            case Modulo:
                return mod(leftValue, rightValue);
            case BitwiseOr:
                return new NumberValue(leftValue.asNumber().intValue() | rightValue.asNumber().intValue());
            case BitwiseAnd:
                return new NumberValue(leftValue.asNumber().intValue() & rightValue.asNumber().intValue());
            case BitwiseXor:
                return new NumberValue(leftValue.asNumber().intValue() ^ rightValue.asNumber().intValue());
            case LogicalOr:
                return new BoolValue(leftValue.asBoolean() || rightValue.asBoolean());
            case LogicalAnd:
                return new BoolValue(leftValue.asBoolean() && rightValue.asBoolean());
            case LessThen:
                return new BoolValue(leftValue.compareTo(rightValue) < 0);
            case GreaterThen:
                return new BoolValue(leftValue.compareTo(rightValue) > 0);
            case GreaterThenOrEquals:
                return new BoolValue(leftValue.compareTo(rightValue) >= 0);
            case LessThenOrEquals:
                return new BoolValue(leftValue.compareTo(rightValue) <= 0);
            case Equals:
            return new BoolValue(leftValue.compareTo(rightValue) == 0);
            case NotEquals:
                return new BoolValue(leftValue.compareTo(rightValue) != 0);
            default:
                return new NullValue();
        }
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
                return multiplyString(rightString, (int)leftNumber);
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
                    return new StringValue(leftString.substring(0, leftString.length() - (int)rightDouble));
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

    @Override
    public String toString() {
        return String.format("[%s %s %s]", left, operation.key, right);
    }
}