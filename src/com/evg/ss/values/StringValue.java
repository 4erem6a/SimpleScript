package com.evg.ss.values;

/**
 * @author 4erem6a
 */
public class StringValue implements Value {

    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
            result = "";
        else if (value instanceof ArrayValue)
            result = value.asString();
        else if (value instanceof TypeValue)
            result = value.asString();
        else result = value.asObject().toString();
        return new StringValue(result);
    }

    public ArrayValue asCharArray() {
        final char[] chars = value.toCharArray();
        final Value[] values = new Value[chars.length];
        for (int i = 0; i < chars.length; i++) {
            final String string = new String(new char[] {chars[i]});
            values[i] = new StringValue(string);
        }
        return new ArrayValue(values);
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
    public Type getType() {
        return Type.String;
    }

    @Override
    public int compareTo(Value o) {
        return value.compareTo(o.asString());
    }
}
