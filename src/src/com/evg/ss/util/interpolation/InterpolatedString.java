package com.evg.ss.util.interpolation;

import com.evg.ss.lexer.Lexer;
import com.evg.ss.lexer.Token;
import com.evg.ss.parser.Parser;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InterpolatedString {

    private final static String pattern = "\\{[^{}]*}";
    private String string;

    public InterpolatedString(String string) {
        this.string = string;
    }

    public String calculate() {
        final Pattern pattern = Pattern.compile(InterpolatedString.pattern);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            final String group = matcher.group();
            final String body = group.substring(1, group.length() - 1);
            final List<Token> tokens = new Lexer(body).tokenize();
            final Value value = new Parser(tokens).express().eval();
            final String result = StringValue.asStringValue(value).asString();
            string = matcher.replaceFirst(result == null ? "" : result);
            matcher = pattern.matcher(string);
        }
        return string;
    }
}