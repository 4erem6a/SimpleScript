package com.evg.ss.values;

/**
 * @author 4erem6a
 */
public class StringValue implements Value {

    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public static StringValue asStringValue(Value value) {
        final String result;
        if (value instanceof NumberValue)
            result = value.asNumber().toString();
        else if (value instanceof BoolValue)
            result = value.asBoolean().toString();
        else if (value instanceof StringValue)
            result = value.asString();
        else if (value instanceof NullValue)
            result = null;
        else result = value.asObject().toString();
        return new StringValue(result);
    }

    @Override
    public Double asNumber() {
        try {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    @Override
    public Boolean asBoolean() {
        try {
            return Boolean.parseBoolean(value);
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public Types getType() {
        return Types.String;
    }

    @Override
    public int compareTo(Value o) {
        return value.compareTo(o.asString());
    }
}
