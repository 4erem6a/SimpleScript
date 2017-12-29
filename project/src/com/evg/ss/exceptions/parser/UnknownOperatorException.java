package com.evg.ss.exceptions.parser;

/**
 * @author 4erem6a
 */
public final class UnknownOperatorException extends SSParserException {
    public UnknownOperatorException(String operator, OperatorTypes type) {
        super(String.format("Unknown %s operator '%s.'", type.toString().toLowerCase(), operator));
    }

    public enum OperatorTypes {
        Unary,
        Binary,
        Ternary
    }
}