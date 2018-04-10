package com.evg.ss.values;

import com.evg.ss.exceptions.execution.IndexOutOfBoundsException;

/**
 * @author 4erem6a
 */
public class StringValue implements Value, Container {

    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public static StringValue asStringValue(Value value) {
        final String result;
        if (value instanceof NumberValue)
            result = value.asNumber().toString();
        else if (value != null) result = value.asString();
        else result = new NullValue().asString();
        return new StringValue(result);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayValue asCharArray() {
        final char[] chars = value.toCharArray();
        final Value[] values = new Value[chars.length];
        for (int i = 0; i < chars.length; i++) {
            final String string = new String(new char[]{chars[i]});
            values[i] = new StringValue(string);
        }
        return new ArrayValue(values);
    }

    @Override
    public Double asNumber() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    @Override
    public Boolean asBoolean() {
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
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
        return (o.getType() == Types.Null || o.getType() == Types.Undefined ? -1 : value.compareTo(o.asString()));
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Value && ((Value) obj).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode() ^ Types.String.hashCode();
    }

    @Override
    public Value clone() {
        return Value.of(value);
    }

    @Override
    public Value get(Value key) {
        if (key instanceof NumberValue) {
            final int index = key.asNumber().intValue();
            if (index < 0 || index >= value.length())
                throw new IndexOutOfBoundsException(index);
            return Value.of(Character.toString(value.charAt(index)));
        } else if (key instanceof StringValue) {
            ///Properties:
            switch (key.asString()) {
                case "length":
                    return Value.of(value.length());
            }
        }
        return new UndefinedValue();
    }

    @Override
    public void set(Value key, Value value) {
        if (key.getType() == Types.Number) {
            final int index = key.asNumber().intValue();
            if (index < 0 || index >= this.value.length())
                throw new IndexOutOfBoundsException(index);
            this.value = this.value.substring(0, index) +
                    value.asString() +
                    (index == this.value.length() ? "" : this.value.substring(index + 1));
        }
    }
}
