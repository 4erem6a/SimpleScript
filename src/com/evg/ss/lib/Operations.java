package com.evg.ss.lib;

public enum Operations {

    //Unary
    UnaryPlus("+"),
    UnaryMinus("-"),
    BitwiseNot("~"),
    BooleanNot("!"),
    PrefixIncrement("++"),
    PrefixDecrement("--"),
    PostfixIncrement("++"),
    PostfixDecrement("--"),
    StaticAccess(".static"),
    ClassAccess(".class"),
    ConstructorAccess(".new"),
    PrototypeAccess(".super"),
    FieldDeletion(".delete"),
    ValueClone("@"),

    //Assignment:
    Assignment("="),

    //Binary
    ////Arithmetical
    Addition("+"),
    Subtraction("-"),
    Division("/"),
    Multiplication("*"),
    Modulus("%"),

    ////Bitwise
    BitwiseOr("|"),
    BitwiseAnd("&"),
    BitwiseXor("^"),

    LShift("<<"),
    RShift(">>"),
    URShift(">>>"),

    ////Boolean
    BooleanOr("||"),
    BooleanAnd("&&"),

    LessThen("<"),
    GreaterThen(">"),
    GreaterThenOrEquals(">="),
    LessThenOrEquals("<="),
    Equals("=="),
    NotEquals("!="),

    ////Comparison
    Compare("=?"),

    ////Membership
    Is("is"),
    As("as"),

    //Ternary
    Ternary("?:"),

    //Other
    Catch(".catch"),
    Class("class"),
    Call("()"),
    ContainerAccess("[]");
    private final String key;

    Operations(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getSpacedKey() {
        return String.format("%s%s%s",
                Character.isLetter(key.charAt(0)) ? " " : "",
                key,
                Character.isLetter(key.charAt(key.length() - 1)) ? " " : "");
    }
}